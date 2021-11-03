package net.zhaoxiaobin.concurrentaction.completionService;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhaoxb
 * @date 2021-11-03 下午12:52
 */
@Slf4j
public class CompletionServiceDemo {
    @Test
    public void test1() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);
        // 开始批量提交异步任务
        for (int i = 0; i < 3; i++) {
            completionService.submit(() -> {
                int randomInt = RandomUtil.randomInt(10);
                log.info(randomInt + "");
                ThreadUtil.sleep(randomInt * 1000);
                return randomInt;
            });
        }
        // 对异步任务结果进行统计汇总，相比于线程池直接执行，多一个汇总处理的过程，而不是直接把异步任务撒出去
        int count = 0;
        for (int i = 0; i < 3; i++) {
            count += completionService.take().get();
        }
        System.out.println(count);
    }
}