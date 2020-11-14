/**
 * Copyright (C), 2015-2019
 */
package com.zxb.libai.regex;

import cn.hutool.core.util.ReUtil;
import org.junit.Test;

import java.util.ArrayList;

/**
 * 概括字符集
 *
 * \d=[0-9] \D
 * \w=[A-Za-z0-9_] \W
 * \s=[\r\n\t ]空白字符 \S
 * .匹配除\n外的任意字符
 * []内的字符除了"^"和"-"具有特殊意义，其它字符都表示其本身。而且[]内的表达式都为或的关系
 *
 *
 * 量词
 *
 * 只作用于前1个字符或前1组()字符
 * {n} 只能n次
 * {n,} 至少n次
 * {n,m} 至少n次最多m次
 * * 0或多次 {0,}
 * + 至少1次 {1,}
 * ? 0或1 {0,1}
 *
 *
 * 除了查找以外还有匹配、替换功能是使用了String类的API
 *
 * @author zhaoxb
 * @create 2020-01-06 16:02
 */
public class FindByHutool {
    /**
     * 贪婪与非贪婪
     *
     * 在量词后加"?"表示非贪婪模式(勉强模式)，默认是贪婪模式
     * 非贪婪模式下只要匹配成功立即返回不再继续匹配
     */
    @Test
    public void find01() {
        String str = "<out>zxb</out>";
        String s1 = ReUtil.get("<out>(.*?)</out>", str, 0);
        String s2 = ReUtil.get("<out>(.*?)</out>", str, 1);
        System.out.println(s1);
        System.out.println(s2);
    }

    @Test
    public void find02() {
        String str = "<out>name:zxb,age:28,addr:Shanghai</out><out>name:wqq,age:30,addr:Ningbo</out>";
        String regex = "<out>name:(.*?),age:(.*?),addr:(.*?)</out>";
        ArrayList<String> nameList = ReUtil.findAll(regex, str, 1, new ArrayList<>());
        ArrayList<String> ageList = ReUtil.findAll(regex, str, 2, new ArrayList<>());
        ArrayList<String> addrList = ReUtil.findAll(regex, str, 3, new ArrayList<>());
        System.out.println(nameList);
        System.out.println(ageList);
        System.out.println(addrList);
    }

    /**
     * 在正则表达式前加"^"表示开头匹配，加"$"表示结尾匹配
     */
    @Test
    public void find03() {
        String str = "java2python3java4c#5golang6golang";
        String s1 = ReUtil.get("^java", str, 0);
        String s2 = ReUtil.get("golang$", str, 0);
        System.out.println(s1);
        System.out.println(s2);

    }
}