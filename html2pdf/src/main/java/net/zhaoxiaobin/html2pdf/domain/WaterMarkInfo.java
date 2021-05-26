package net.zhaoxiaobin.html2pdf.domain;

import lombok.Data;

/**
 * 水印信息实体类
 *
 * @author zhaoxb
 * @create 2019-03-21 12:01
 */
@Data
public class WaterMarkInfo {
    /**
     * 如果为null设置水印时会报错
     */
    private String waterMark = "";
    /**
     * 水印透明度,值越小透明度越高
     */
    private float opacity = 0.2F;
    /**
     * 水印字体，如果乱码设置为本地宋体字体:fonts/simsun.ttc,1
     */
    private String fontName = "STSong-Light";
    /**
     * 水印编码格式，如果乱码设置为:BaseFont.IDENTITY_H
     */
    private String encoding = "UniGB-UCS2-H";
    /**
     * 字体大小
     */
    private float fontSize = 24;
    /**
     * 横坐标在页面宽度的百分比,左下角为原点
     */
    private float x = 50;
    /**
     * 纵坐标在页面高度的百分比,左下角为原点
     */
    private float y = 40;
    /**
     * 水印旋转角度
     */
    private float rotation = 45;
}
