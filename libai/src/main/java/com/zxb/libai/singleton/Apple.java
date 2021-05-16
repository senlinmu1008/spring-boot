package com.zxb.libai.singleton;

/**
 * @author zhaoxb
 * @date 2021-05-10 11:05 下午
 */
public class Apple {
    private static volatile Apple apple;

    private Apple() {

    }

    public static Apple getInstance() {
        if (apple == null) {
            synchronized (Apple.class) {
                if (apple == null) {
                    apple = new Apple();
                }
            }
        }
        return apple;
    }
}