package com.zxb.libai.format;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhaoxb
 * @date 2021/04/29 10:49 下午
 */
public class FormatCode {
    private static String bathPath = "/Users/zhaoxiaobin/Documents/java/ideaWorkspace/project/spring-boot";

    private static Set<String> suffixSet = new HashSet<>();

    public static void main(String[] args) {
        File baseDir = new File(bathPath);
        format(baseDir);
        System.out.println(suffixSet);
    }

    private static void format(File dir) {
        File[] listFiles = dir.listFiles((file) -> !"target".equals(file.getName())
                && !".idea".equals(file.getName())
                && !".git".equals(file.getName())
                && !file.getName().endsWith("jpg")
                && !file.getName().endsWith("png")
                && !file.getName().endsWith("gif")
                && !file.getName().endsWith("svg")
                && !file.getName().endsWith("otf")
                && !file.getName().endsWith("eot")
                && !file.getName().endsWith("less")
                && !file.getName().endsWith("tld")
                && !file.getName().endsWith("ttc")
                && !file.getName().endsWith("ttf")
                && !file.getName().endsWith("woff")
                && !file.getName().endsWith("woff2")
                && !file.getName().endsWith("iml")
                && !file.getName().endsWith("DS_Store"));
        for (File file : listFiles) {
            if (file.isDirectory()) {
                format(file);
            } else {
                System.out.println(file.getName());
                String name = file.getName();
                if (name.contains(".")) {
                    String suffix = name.substring(name.lastIndexOf(".") + 1);
                    suffixSet.add(suffix);
                } else {
                    suffixSet.add(name);
                }

//                String content = cn.hutool.core.io.FileUtil.readString(file, StandardCharsets.UTF_8);
//                content = content.replace("\r\n", "\n");
//                content = content.replace("\n\r", "\n");
//                cn.hutool.core.io.FileUtil.writeString(content, file, StandardCharsets.UTF_8);
            }
        }
    }
}