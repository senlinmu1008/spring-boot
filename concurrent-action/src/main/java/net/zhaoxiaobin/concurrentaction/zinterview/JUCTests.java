package net.zhaoxiaobin.concurrentaction.zinterview;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zhaoxb
 * @date 2024-01-15 上午9:48
 */
public class JUCTests {
    /**
     * 让线程池按顺序执行任务
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void test1() throws ExecutionException, InterruptedException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
        ScheduledFuture<?> future1 = executor.schedule(() -> System.out.println(1), 0, TimeUnit.MINUTES);
        future1.get();
        ScheduledFuture<?> future2 = executor.schedule(() -> System.out.println(2), 0, TimeUnit.MINUTES);
        future2.get();
        ScheduledFuture<?> future3 = executor.schedule(() -> System.out.println(3), 0, TimeUnit.MINUTES);
        future3.get();
        executor.shutdown();
    }

    /**
     * 有3个线程分别输出A、B、C，按顺序ABCABC...打印 ？
     *
     * @throws InterruptedException
     */
    @Test
    public void test2() throws InterruptedException {
        Thread t1 = new Thread(new PrintRunnable("A"));
        Thread t2 = new Thread(new PrintRunnable("B"));
        Thread t3 = new Thread(new PrintRunnable("C"));
        t1.start();
        t2.start();
        t3.start();
        for (int i = 0; i < 100; i++) {
            Thread.sleep(1000);
            if (i % 3 == 0) {
                LockSupport.unpark(t1);
            } else if (i % 3 == 1) {
                LockSupport.unpark(t2);
            } else {
                LockSupport.unpark(t3);
            }
        }
    }

    private class PrintRunnable implements Runnable {
        private String c;

        public PrintRunnable(String c) {
            this.c = c;
        }

        @Override
        public void run() {
            while (true) {
                System.out.println(c);
                LockSupport.park();
            }
        }
    }

    @Test
    public void test3() {

    }

}