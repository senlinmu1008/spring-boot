package net.zhaoxiaobin.task.strategy;

import net.zhaoxiaobin.task.domain.SpringScheduleCron;

/**
 * 调度策略接口
 *
 * @author zhaoxb
 * @date 2021-07-28 4:47 下午
 */
public interface TaskSchedulingStrategy {
    boolean currentCanExecute(SpringScheduleCron springScheduleCron);
}
