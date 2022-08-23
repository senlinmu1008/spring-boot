package net.zhaoxiaobin.libai;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.stream.IntStream;

/**
 * @author zhaoxb
 * @date 2022-08-23 下午2:38
 */
public class Lottery {
    public static void main(String[] args) {
        int[] preArea = IntStream.range(1, 36).toArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            int removeIndex = RandomUtil.randomInt(0, preArea.length);
            sb.append(String.format("%02d ", preArea[removeIndex]));
            preArea = ArrayUtil.remove(preArea, removeIndex);
            ThreadUtil.sleep(10L);
        }
        int[] suffArea = IntStream.range(1, 13).toArray();
        for (int i = 0; i < 2; i++) {
            int removeIndex = RandomUtil.randomInt(0, suffArea.length);
            sb.append(String.format("%02d ", suffArea[removeIndex]));
            suffArea = ArrayUtil.remove(suffArea, removeIndex);
            ThreadUtil.sleep(10L);
        }
        String result = sb.toString().trim();
        System.out.println(result);
    }
}