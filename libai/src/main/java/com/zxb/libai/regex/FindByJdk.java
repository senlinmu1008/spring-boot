/**
 * Copyright (C), 2015-2020
 */
package com.zxb.libai.regex;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class FindByJdk{

    /**
     * 贪婪与非贪婪
     *
     * 在量词后加"?"表示非贪婪模式(勉强模式)，默认是贪婪模式
     * 非贪婪模式下只要匹配成功立即返回不再继续匹配
     */
    @Test
    public void find01() {
        String str = "<out>zxb</out>";
        Pattern pattern = Pattern.compile("<out>(.*?)</out>");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()) {
            System.out.println(matcher.group(0)); // 匹配整个Pattern
            System.out.println(matcher.group(1)); // 只匹配()内的文本
        }
    }

    @Test
    public void find02() {
        String str = "<out>name:zxb,age:28,addr:Shanghai</out><out>name:wqq,age:30,addr:Ningbo</out>";
        Pattern pattern = Pattern.compile("<out>name:(.*?),age:(.*?),addr:(.*?)</out>");
        Matcher matcher = pattern.matcher(str);
        List<String> nameList = new ArrayList<>();
        List<String> ageList = new ArrayList<>();
        List<String> addrList = new ArrayList<>();
        while(matcher.find()) {
            nameList.add(matcher.group(1));
            ageList.add(matcher.group(2));
            addrList.add(matcher.group(3));
        }
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
        Pattern pattern = Pattern.compile("^java");
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()) {
            System.out.println(matcher.group());
        }

        pattern = Pattern.compile("golang$");
        matcher = pattern.matcher(str);
        if(matcher.find()) {
            System.out.println(matcher.group());
        }
    }

    /**
     * Pattern.CASE_INSENSITIVE 忽略大小写
     * Pattern.DOTALL 单行模式，将整个文本看作1行字符串，只有1个开头1个结尾，而且可以使小数点"."匹配任何字符包括"\n"换行符
     * Pattern.MULTILINE 多行模式，每一行都是1个字符串，每一行开头和结尾都可以用"^"或"$"匹配。
     * 如果在多行模式下要仅匹配真正开头和结尾的位置，可以使用\A、\Z来表示
     *
     * 同时生效多个模式: Pattern pattern = Pattern.compile("regex", Pattern.CASE_INSENSITIVE|Pattern.DOTALL);
     */
    @Test
    public void find04() {
        String str = "JAVA";
        Pattern pattern = Pattern.compile("java", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        if(matcher.find()) {
            System.out.println(matcher.group());
        }

        String str2 = "zx\nb";
        Pattern pattern2 = Pattern.compile(".*", Pattern.DOTALL);
        Matcher matcher2 = pattern2.matcher(str2);
        if(matcher2.find()) {
            System.out.println(matcher2.group());
        }
    }
}