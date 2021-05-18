package com.zxb.libai;

import cn.hutool.core.thread.ThreadUtil;
import org.junit.Test;

/**
 * @author zhaoxb
 * @date 2021-05-09 9:07 下午
 */
public class VolatileTests {
    private volatile boolean flag = true;

    /**
     * 测试可见性
     */
    @Test
    public void test1() throws InterruptedException {
        Thread t = new Thread(() -> {
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
    @Test
    public void test2() {
        for(int n = 1;;n++) {

        }
    }
}