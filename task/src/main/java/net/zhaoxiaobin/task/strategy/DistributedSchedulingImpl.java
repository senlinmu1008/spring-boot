package net.zhaoxiaobin.task.strategy;

import cn.hutool.core.net.NetUtil;
import lombok.extern.slf4j.Slf4j;
import net.zhaoxiaobin.task.domain.SpringScheduleCron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 分布式调度策略实现类
 *
 * @author zhaoxb
 * @date 2021-07-28 5:13 下午
 */
@Component
@Slf4j
public class DistributedSchedulingImpl implements TaskSchedulingStrategy {
    @Autowired
    private Environment environment;

    @Autowired(required = false)
    private ZSetOperations zSetOperations;

    @Autowired(required = false)
    private ValueOperations stringOperations;

    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    /**
     * 当前服务实例的唯一标识
     */
    private String currentInstanceId;

    @PostConstruct
    private void init() {
        // ip:port作为微服务实例的唯一标志
        currentInstanceId = NetUtil.getLocalhostStr() + ":" + environment.getProperty("server.port");
        log.info("当前服务实例Id:{}", currentInstanceId);
    }

    /**
     * 使用redis实现分布式调度，zset排行榜 + 分布式锁
     *
     * @param springScheduleCron
     * @return
     */
    @Override
    public boolean currentCanExecute(SpringScheduleCron springScheduleCron) {
        String taskId = springScheduleCron.getId().toString();
        // 定义redis中插入的key，加上有含义的前缀防止冲突
        String zsetKey = "schedule_rank_" + taskId; // 各个任务的排行榜key
        String lockKey = "schedule_lock_" + taskId; // 各个任务的分布式锁key
        // TODO 分布式锁的key粒度细化到每个任务具体的执行时间点，防止因为共用key互斥不同执行周期的任务执行（周期短的情况）
        String impeachKeyPrefix = "schedule_impeach_" + taskId + "_"; // 各个任务检举key前缀
        try {
            // 查询该任务执行的排行榜
            Set<Object> currentTaskRankSet = zSetOperations.range(zsetKey, 0, -1);
            if (!currentTaskRankSet.contains(currentInstanceId)) {
                // 当前该任务排行榜没有当前实例执行的记录，新增后竞争锁
                // 这里不能先竞争锁再加入排行榜，否则一些节点可能因为时间慢一些永远拿不到锁而导致一直无法加入
                log.info("当前服务实例:{}加入排行榜:{}", currentInstanceId, zsetKey);
                zSetOperations.add(zsetKey, currentInstanceId, System.currentTimeMillis());
                // 指定过期时间
                redisTemplate.expire(zsetKey, 30, TimeUnit.DAYS);
            } else {
                // 遍历一波排行榜，把无效的服务实例剔除，以免挂掉的服务一直未被执行占着排行榜首位
                for (Object instanceId : currentTaskRankSet) {
                    if (!this.validInstance(instanceId.toString())) {
                        log.warn("服务实例:{}处于无效状态,从排行榜:{}中剔除", instanceId, zsetKey);
                        zSetOperations.remove(zsetKey, instanceId);
                    }
                }
                // 取最早执行的服务实例包含执行时间
                Set<ZSetOperations.TypedTuple<Object>> earliestTuple = zSetOperations.rangeWithScores(zsetKey, 0, 0);
                // 如果当前有分布式锁，说明当前周期内已经执行过了，榜首不是本次周期的执行者，不进行检举和剔除服务
                if (redisTemplate.hasKey(lockKey)) {
                    return false;
                }
                // 如果当前没有分布式锁，说明榜首还是上个周期内执行的服务实例，进行检测
                if (earliestTuple.isEmpty()) {
                    // 一般情况下，这里不太会为空，除非2种情况
                    // 1.定时任务周期比较短，执行到这里时，服务还未完全注册好就从排行榜中剔除了
                    // 2.运行一段时间后，健康检查因为某些因素未通过，导致实例被剔除；但依旧处于运行状态
                    log.warn("排行榜为空,如果服务刚启动还未完全注册好则忽略之;如果运行一段时间出现此警告,说明服务就在刚刚被剔除了");
                } else {
                    // 取出最早的一个服务实例包含执行时间
                    ZSetOperations.TypedTuple<Object> instanceTuple = earliestTuple.iterator().next();
                    String firstInstanceId = instanceTuple.getValue().toString();
                    if (!currentInstanceId.equals(firstInstanceId)) {
                        // 防止因为某一台机器挂掉但检测依旧活着，定时任务也一直不执行，一直霸榜
                        // 判断同一个任务、同一个检举者有没有检举过此实例，如果被同一检举者举报2次，且2次执行时间一致，则剔除
                        String executeTimeMillis = instanceTuple.getScore().toString();
                        // schedule_impeach_任务id + 当前实例 + 被检举实例作为key，比如schedule_impeach_1_192.168.0.1_192.168.0.2
                        String impeachKey = impeachKeyPrefix + currentInstanceId + "_" + firstInstanceId;
                        if (!executeTimeMillis.equals(stringOperations.get(impeachKey))) {
                            // 检举该服务实例，检举记录保留30天
//                            log.debug("当前实例:{}检举:{}实例,任务id:{},bean:{}", currentInstanceId, firstInstanceId, taskId, springScheduleCron.getBeanName());
                            stringOperations.set(impeachKey, executeTimeMillis, 30, TimeUnit.DAYS);
                            return false;
                        }
                        // 榜首有霸榜嫌疑，被检测到2次最后的执行时间一样，需要剔除该实例
                        log.warn("服务实例:{}处于无效状态,从排行榜:{}中剔除", firstInstanceId, zsetKey);
                        zSetOperations.remove(zsetKey, firstInstanceId);
                    }
                }
            }
            // 竞争分布式锁，自动过期，防止不同机器因为时间不同步或进程GC延迟导致同一个周期内重复执行定时任务
            boolean distributionLock = stringOperations.setIfAbsent(lockKey, "", 5, TimeUnit.SECONDS);
            if (distributionLock) {
                // 更新该服务实例的执行时间
//                log.debug("当前实例:{}获得执行权,任务id:{},bean:{}", currentInstanceId, taskId, springScheduleCron.getBeanName());
                zSetOperations.add(zsetKey, currentInstanceId, System.currentTimeMillis());
            }
            return distributionLock;
        } catch (Exception e) {
            log.error("redis异常", e);
            // 为了不让redis部分节点挂了影响定时任务的执行，如果与redis通信失败，还是执行定时任务
            return true;
        }
    }

    /**
     * 根据服务实例id判断该服务是否还有效
     *
     * @param instanceId
     * @return
     */
    private boolean validInstance(String instanceId) {
        // 可以从注册中心拉取服务健康列表，或者根据ip:port自行健康检查
        // 这里模拟返回true
        return true;
    }
}