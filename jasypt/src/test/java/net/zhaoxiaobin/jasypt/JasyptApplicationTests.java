package net.zhaoxiaobin.jasypt;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JasyptApplicationTests {
    @Autowired
    private StringEncryptor encryptor;

    @Test
    public void testEncrypt() {
        System.out.println(encryptor.encrypt("123456")); // LEvdXWT9sxdAQS3bcoJ4fIzaAbF+/43Oo/sm8ohv5CIg5NhW0C1gyEl/yYCAfCXA
    }
}
