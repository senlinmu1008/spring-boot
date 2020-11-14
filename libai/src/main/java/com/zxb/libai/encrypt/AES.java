/**
 * Copyright (C), 2015-2020
 */
package com.zxb.libai.encrypt;

import com.zxb.libai.utils.AESUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 *
 * @author zhaoxb
 * @create 2020-01-09 13:52
 */
@Slf4j
public class AES {
    public static final String KEY = "zxb";

    @Test
    public void testAES() {
        String content = "{\"name:\":\"zxb\"}";
        String encryptStr = AESUtil.encrypt(content, KEY);
        log.info("加密后:{}", encryptStr);
        String msg = AESUtil.decrypt(encryptStr, KEY);
        log.info("解密后:{}", msg);
    }
}