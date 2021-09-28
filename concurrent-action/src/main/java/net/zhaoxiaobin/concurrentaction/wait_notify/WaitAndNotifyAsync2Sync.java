package net.zhaoxiaobin.concurrentaction.wait_notify;

import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 异步转同步类
 * 采用synchronized + wait + notify实现
 * 单例调用还是多例调用，耗时基本相同（主要原因wait释放了锁，有并行度）
 *
 * @author zhaoxb
 * @date 2021-09-26 下午1:24
 */
public class WaitAndNotifyAsync2Sync {
    /**
     * 查询结果map，key-查询流水号、value-查询结果
     */
    private static Map<String, String> queryResultMap = new ConcurrentHashMap<>();

    /**
     * 查询异步返回的结果，这里包含异步转同步的过程
     *
     * @param seq 查询流水号
     * @return
     */
    @SneakyThrows
    public String getAsyncResult(String seq) {
        // 必须要在synchronized{}中进行wait()，而且锁的对象和通知对象需要为同一个
        synchronized (this) {
            // 循环判断查询结果中有无该线程查询的流水号结果，如果没有则继续进入等待状态
            // 每次进入wait状态会释放锁，每次唤醒会重新竞争锁（如果竞争不到会排队）
            while (queryResultMap.get(seq) == null) {
                this.wait(10000); // 超时10秒结束等待
            }
        }
        // 同步返回结果
        return queryResultMap.remove(seq);
    }

    /**
     * 将查询结果存入共享map，并通知
     *
     * @param seq    查询流水号
     * @param result 查询结果
     */
    public void putAsyncResult(String seq, String result) {
        // 将响应结果存入map，并通知所有线程（会有一个线程命中）
        queryResultMap.put(seq, result);
        // 必须要在synchronized{}中进行notifyAll()，而且锁的对象和通知对象需要为同一个
        synchronized (this) {
            // 通知该对象等待队列中的所有线程
            this.notifyAll();
        }
    }
}