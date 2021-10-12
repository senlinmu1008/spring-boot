package net.zhaoxiaobin.concurrentaction.countdownlatch;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * CountDownLatch主要用来解决一个线程等待多个线程的场景
 *
 * @author zhaoxb
 * @date 2021-10-11 下午6:54
 */
@Slf4j
public class CountDownLatchDemo {
    @Test
    public void testCountDownLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        new Thread(() -> {
            log.info("执行线程1开始");
            ThreadUtil.sleep(2000L);
            log.info("执行线程1结束");
            latch.countDown();
        }).start();
        new Thread(() -> {
            log.info("执行线程2开始");
            ThreadUtil.sleep(3000L);
            log.info("执行线程2结束");
            latch.countDown();
        }).start();
        // 进入等待，等上面2个线程执行完才解除阻塞
        latch.await();
        log.info("全部执行完毕");
    }
}