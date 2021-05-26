/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.libai.json;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * hutool json库
 *
 * @author zhaoxb
 * @create 2020-06-03 4:04 下午
 */
@Slf4j
public class JsonTest {
    @Test
    @SneakyThrows
    public void testJson() {
        /*
        1.bean2String
         */
        User user = new User(1L, "赵晓斌", 28, "上海");
        log.info(JSONUtil.toJsonStr(user));
        // 格式化json
        log.info(JSONUtil.toJsonPrettyStr(user));

        /*
        2.string2bean
         */
        String str = "{\"address\":\"上海\",\"name\":\"赵晓斌\",\"id\":1,\"age\":28}";
        User bean = JSONUtil.toBean(str, User.class);
        // 输出格式化json字符串
        log.info(JSONUtil.formatJsonStr(str));

        /*
        3.string2hash，类似Fastjson中的JSONObject，也实现了Map接口具有散列的特性。
         */
        JSONObject jsonObject = JSONUtil.parseObj(str);
        jsonObject.put("key", "value");
        jsonObject.remove("key");

        /*
        4.hash2string
         */
        // 转为json字符串，不带格式化
        log.info(jsonObject.toString());
        // 自定义格式化，可以指定缩进的空格数。0-不缩进，4-缩进4格即常规的格式化json字符串
        log.info(jsonObject.toJSONString(0));
        // 格式化输出json字符串
        log.info(jsonObject.toStringPretty());

        /*
        5.bean2hash
         */
        JSONObject jsonObj = JSONUtil.parseObj(user);

        /*
        6.hash2bean
         */
        User beanObj = JSONUtil.toBean(jsonObj, User.class);

    }

    @Data
    @AllArgsConstructor
    public class User {
        private Long id;
        private String name;
        private Integer age;
        private String address;
    }

}

