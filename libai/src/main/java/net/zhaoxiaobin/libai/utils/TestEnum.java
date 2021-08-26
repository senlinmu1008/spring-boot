package net.zhaoxiaobin.libai.utils;

/**
 * @author zhaoxb
 * @date 2021-08-26 下午6:42
 */
public enum TestEnum {
    ENUM1("1", "枚举1"),
    ENUM2("2", "枚举2"),
    ENUM3("3", "枚举3"),
    ;
    private final String code;
    private final String message;

    TestEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
