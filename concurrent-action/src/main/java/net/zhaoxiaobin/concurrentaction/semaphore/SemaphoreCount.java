package net.zhaoxiaobin.concurrentaction.semaphore;

import lombok.SneakyThrows;
import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * 使用Semaphore信号量进行累加
 *
 * @author zhaoxb
 * @date 2021-09-29 下午1:10
 */
public class SemaphoreCount {
    private volatile int count = 0;

    /**
     * 信号量，初始值为1，即只有1个线程允许进入临界区
     */
    private Semaphore semaphore = new Semaphore(1);

    @SneakyThrows
    public void addOne() {
        // 不能放入try中，防止acquire失败，多释放一次计数从而导致多个线程进入临界区
        // 这里的acquire()并非上锁，不会有互斥效果
        semaphore.acquire(); // 如果没有许可证，进入阻塞状态（如果有锁并不释放锁）
        try {
            count++;
        } finally {
            semaphore.release(); // release()happens-before于acquire()，保证线程之间可见
        }
    }

    @Test
    public void test() throws InterruptedException {
        // 分别启动2个线程对count共享变量进行累加
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                this.addOne();
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                this.addOne();
            }
        });
        t1.start();
        t2.start();
        // 等待执行完
        t1.join();
        t2.join();
        // 输出最后的结果值，join后可以保证可见性
        System.out.println(this.count);
    }
}