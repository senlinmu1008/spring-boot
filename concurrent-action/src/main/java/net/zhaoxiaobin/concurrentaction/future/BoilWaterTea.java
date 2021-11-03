package net.zhaoxiaobin.concurrentaction.future;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * 烧水泡茶
 *
 * @author zhaoxb
 * @date 2021-10-15 下午1:51
 */
@Slf4j
public class BoilWaterTea {
    @Test
    public void boil1() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // 任务1：洗茶壶->洗茶杯->拿茶叶
        Future<String> future1 = executorService.submit(() -> {
            log.info("洗茶壶");
            ThreadUtil.sleep(1000L);
            log.info("洗茶杯");
            ThreadUtil.sleep(2000L);
            log.info("拿茶叶");
            ThreadUtil.sleep(1000L);
            return "西湖龙井";
        });
        // 任务2：洗水壶->烧开水
        Future<String> future2 = executorService.submit(() -> {
            log.info("洗水壶");
            ThreadUtil.sleep(1000L);
            log.info("烧开水");
            ThreadUtil.sleep(15000L);
            try {
                String result = future1.get();
                return "上茶:" + result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        log.info(future2.get());
    }

    @Test
    public void boil2() throws ExecutionException, InterruptedException {
        // 任务1：洗茶壶->洗茶杯->拿茶叶
        FutureTask<String> futureTask1 = new FutureTask<>(() -> {
            log.info("洗茶壶");
            ThreadUtil.sleep(1000L);
            log.info("洗茶杯");
            ThreadUtil.sleep(2000L);
            log.info("拿茶叶");
            ThreadUtil.sleep(1000L);
            return "西湖龙井";
        });
        // 任务2：洗水壶->烧开水
        FutureTask<String> futureTask2 = new FutureTask<>(() -> {
            log.info("洗水壶");
            ThreadUtil.sleep(1000L);
            log.info("烧开水");
            ThreadUtil.sleep(15000L);
            try {
                String result = futureTask1.get();
                return "上茶:" + result;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        // 执行
        Thread t1 = new Thread(futureTask1, "T1");
        Thread t2 = new Thread(futureTask2, "T2");
        t1.start();
        t2.start();
        log.info(futureTask2.get());
    }

    @Test
    public void boil3() {
        // 任务1：洗茶壶->洗茶杯->拿茶叶
        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            log.info("洗茶壶");
            ThreadUtil.sleep(1000L);
            log.info("洗茶杯");
            ThreadUtil.sleep(2000L);
            log.info("拿茶叶");
            ThreadUtil.sleep(1000L);
            return "西湖龙井";
        });
        // 任务2：洗水壶->烧开水
        CompletableFuture<Void> completableFuture2 = CompletableFuture.runAsync(() -> {
            log.info("洗水壶");
            ThreadUtil.sleep(1000L);
            log.info("烧开水");
            ThreadUtil.sleep(15000L);
        });
        // 组合任务1和任务2
        CompletableFuture<Object> completableFuture3 = completableFuture2.thenCombine(completableFuture1, (__, result) -> "上茶:" + result);
        log.info(completableFuture3.join().toString());
    }
}