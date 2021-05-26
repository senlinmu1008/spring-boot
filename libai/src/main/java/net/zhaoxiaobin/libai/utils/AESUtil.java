/**
 * Copyright (C), 2015-2020
 */
package net.zhaoxiaobin.libai.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 *
 * @author zhaoxb
 * @create 2020-01-08 14:40
 */
public class AESUtil {
    public static final String KEY_AES = "AES";
    public static final String SHA = "SHA1PRNG";

    public static String encrypt(String content, String key) {
        return AES(content, key, Cipher.ENCRYPT_MODE);
    }

    public static String decrypt(String content, String key) {
        return AES(content, key, Cipher.DECRYPT_MODE);
    }

    @SneakyThrows
    public static String AES(String content, String key, int mode) {
        if(StringUtils.isBlank(content) || StringUtils.isBlank(key)) {
            return StringUtils.EMPTY;
        }
        byte[] bytes = mode == Cipher.ENCRYPT_MODE ? content.getBytes(CharsetUtil.UTF_8) : HexUtil.decodeHex(content);

        //1.构造密钥生成器，指定为AES算法,不区分大小写
        KeyGenerator kgen = KeyGenerator.getInstance(KEY_AES);

        //2.根据encodeRules规则初始化密钥生成器
        //生成一个128位的随机源,根据传入的字节数组
        SecureRandom secureRandom = SecureRandom.getInstance(SHA);
        secureRandom.setSeed(key.getBytes());
        kgen.init(128, secureRandom);

        //3.产生原始对称密钥
        SecretKey secretKey = kgen.generateKey();

        //4.获得原始对称密钥的字节数组
        byte[] enCodeFormat = secretKey.getEncoded();

        //5.根据字节数组生成AES密钥
        SecretKeySpec keySpec = new SecretKeySpec(enCodeFormat, KEY_AES);

        //6.根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance(KEY_AES);// 创建密码器

        //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(mode, keySpec);// 初始化
        byte[] result = cipher.doFinal(bytes);

        return mode == Cipher.ENCRYPT_MODE ? HexUtil.encodeHexStr(result) : new String(result, CharsetUtil.UTF_8);
    }
}