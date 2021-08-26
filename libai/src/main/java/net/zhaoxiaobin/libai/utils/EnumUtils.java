package net.zhaoxiaobin.libai.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoxb
 * @date 2021-08-26 下午6:36
 */
@Slf4j
public class EnumUtils {
    /**
     * 将枚举转换为map
     *
     * @param enumClazz 枚举类对象
     * @return
     */
    public static Map<String, String> convert2Map(Class<? extends Enum> enumClazz) {
        return convert2Map(enumClazz, "code", "message");
    }

    /**
     * 将枚举转换为map
     *
     * @param enumClazz        枚举类对象
     * @param fieldNameOfKey   枚举中作为key的属性名
     * @param fieldNameOfValue 枚举中作为value的属性名
     * @return
     */
    public static Map<String, String> convert2Map(Class<? extends Enum> enumClazz, String fieldNameOfKey, String fieldNameOfValue) {
        Enum[] enumConstants = enumClazz.getEnumConstants();
        Map<String, String> enumMap = new HashMap<>();
        try {
            Field keyField = enumClazz.getDeclaredField(fieldNameOfKey);
            Field valueField = enumClazz.getDeclaredField(fieldNameOfValue);
            keyField.setAccessible(true);
            valueField.setAccessible(true);
            for (Enum enumConstant : enumConstants) {
                enumMap.put(keyField.get(enumConstant).toString(), valueField.get(enumConstant).toString());
            }
        } catch (Exception e) {
            log.error("反射获取枚举值异常,请确认在{}枚举类中,有{}和{}属性", enumClazz.getName(), fieldNameOfKey, fieldNameOfValue, e);
            throw new RuntimeException("反射获取枚举值异常");
        }
        return enumMap;
    }
}