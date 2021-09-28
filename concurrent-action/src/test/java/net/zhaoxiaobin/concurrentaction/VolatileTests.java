package net.zhaoxiaobin.concurrentaction;

import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhaoxb
 * @date 2021-05-09 9:07 下午
 */
@Slf4j
public class VolatileTests {

    /**
     * 测试可见性
     */
    private boolean flag = true;
//    private volatile boolean flag = true;
    @Test
    public void test1() throws InterruptedException {
        Thread t = new Thread(() -> {
            System.out.println("开始循环");
            while (flag) {

            }
            System.out.println("结束循环");
        });
        t.start();
        ThreadUtil.sleep(1000L);
        flag = false;
        t.join();
    }

    /**
     * 测试指令重排序
     */
//    private int x, y, a, b;
    private volatile int x, y, a, b;
    @Test
    public void test2() throws InterruptedException {
        for(int n = 1;;n++) {
            x = 0; y = 0;
            a = 0; b = 0;
            Thread t1 = new Thread(() -> {
                a = 1; // 1
                x = b; // 2
            });
            Thread t2 = new Thread(() -> {
                b = 1; // 3
                y = a; // 4
            });
            // 启动2个线程
            t1.start(); t2.start();
            // 等这2个线程都执行完毕后再输出x、y的值
            t1.join(); t2.join();
            /*
            假设不出现指令重排序，则会出现以下几种排列组合
            1 2 3 4 x = 0 y = 1
            1 3 4 2 x = 1 y = 1
            1 3 2 4 x = 1 y = 1
            3 1 2 4 x = 1 y = 1
            3 4 1 2 x = 1 y = 0
            3 1 4 2 x = 1 y = 1
            如果出现 x = 0 && y = 0这种情况，则可以反证一定出现了重排序
             */
            if (x == 0 && y == 0) {
                log.info("第{}次出现了x = 0、Y = 0的情况", n);
                break;
            }
        }
    }
}