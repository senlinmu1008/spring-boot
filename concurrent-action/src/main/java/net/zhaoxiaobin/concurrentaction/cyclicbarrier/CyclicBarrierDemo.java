package net.zhaoxiaobin.concurrentaction.cyclicbarrier;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.CyclicBarrier;

/**
 * CyclicBarrier是一组线程之间互相等待
 * 构造参数:parties要与创建的线程数量一致
 *
 * @author zhaoxb
 * @date 2021-10-12 下午1:03
 */
@Slf4j
public class CyclicBarrierDemo {

    @Test
    public void testCyclicBarrier() throws InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> {
            log.info("CyclicBarrier回调函数开始");
            ThreadUtil.sleep(2000);
            log.info("CyclicBarrier回调函数结束");
        });
        Thread t1 = new Thread(() -> {
            while (true) {
                log.info("执行线程T1开始");
                ThreadUtil.sleep(2000L);
                log.info("执行线程T1结束");
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    log.error("异常", e);
                }
            }
        }, "T1");
        Thread t2 = new Thread(() -> {
            while (true) {
                log.info("执行线程T2开始");
                ThreadUtil.sleep(3000L);
                log.info("执行线程T2结束");
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    log.error("异常", e);
                }
            }
        }, "T2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}