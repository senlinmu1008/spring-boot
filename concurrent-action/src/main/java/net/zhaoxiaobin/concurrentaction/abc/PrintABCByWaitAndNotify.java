package net.zhaoxiaobin.concurrentaction.abc;

import cn.hutool.core.thread.ThreadUtil;
import lombok.SneakyThrows;

/**
 * 有3个线程分别输出A、B、C，按顺序ABCABC...打印
 *
 * @author zhaoxb
 * @date 2021-09-28 下午1:46
 */
public class PrintABCByWaitAndNotify {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> print("A"), "T1");
        Thread t2 = new Thread(() -> print("B"), "T2");
        Thread t3 = new Thread(() -> print("C"), "T3");
        t1.start();
        t2.start();
        t3.start();
        // 主线程定时挨个唤醒线程
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            // 先阻塞一会儿再notify，防止notify的时候还未进入wait状态
            ThreadUtil.sleep(500L);
            if (i % 3 == 0) {
                synchronized (t1) {
                    t1.notify();
                }
            } else if (i % 3 == 1) {
                synchronized (t2) {
                    t2.notify();
                }
            } else if (i % 3 == 2) {
                synchronized (t3) {
                    t3.notify();
                }
            }
        }
    }

    @SneakyThrows
    public static void print(String s) {
        while (true) {
            Thread currentThread = Thread.currentThread();
            synchronized (currentThread) {
                currentThread.wait();
            }
            System.out.println("线程:" + Thread.currentThread().getName() + "输出:" + s);
        }
    }
}