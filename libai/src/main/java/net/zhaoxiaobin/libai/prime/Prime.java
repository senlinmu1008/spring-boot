/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.libai.prime;

import org.junit.Test;

/**
 * @author zhaoxb
 * @create 2020-03-23 13:53
 */
public class Prime {
    public static final int RANGE = 1000000;

    @Test
    public void primeTest() {
        int[] primeArray = new int[5761455];
        primeArray[0] = 2;
        int cnt = 1;
        // 3以上只判断奇数
        for (int i = 3; i <= RANGE; i += 2) {
            if (!isPrime(i, primeArray, cnt)) {
                continue;
            }
            primeArray[cnt++] = i;
        }
        System.out.println(cnt);
        printPrime(primeArray, cnt);
    }

    private boolean isPrime(int num, int[] primeArray, int cnt) {
        /*
        如果是合数则可以被某个小于其的质数整除（合数可以分解为多个质数），反之则为质数
        只需要判断到这个数的算术平方根即可
         */
        for (int i = 0; i < cnt && primeArray[i] <= Math.sqrt(num); i++) {
            if (num % primeArray[i] == 0) {
                return false;
            }
        }
        return true;
    }

    private void printPrime(int[] primeArray, int cnt) {
        for (int i = 0; i < cnt; i++) {
            System.out.print(primeArray[i] + "\t");
            if ((i + 1) % 5 == 0) {
                System.out.println();
            }
        }
    }
}