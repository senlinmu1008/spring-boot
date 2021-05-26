package net.zhaoxiaobin.html2pdf.exception;

/**
 * @author zhaoxb
 * @date 2019-06-14 9:11
 * @return
 */
public enum PdfErrorCode {
    SUCCESS("000000", "交易成功"),
    PDF_TEMPLATE_RENDER_FAIL("LS1001", "渲染freemarker模板失败,模板路径可能不正确或模板文件不存在！"),
    SET_PDF_FONT_FAIL("LS1004", "设置pdf正文字体失败！"),
    HTML_CONVERT2PDF_FAIL("LS1005", "html转换为pdf文件失败，freemarker模板可能有标签未关闭！"),
    ;
    private String value;

    private String desc;

    PdfErrorCode(String value, String desc) {
        this.setValue(value);
        this.setDesc(desc);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "[" + this.value + "]" + this.desc;
    }

}
