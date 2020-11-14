package com.zxb.httpdecrypt;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.util.Base64;
import org.junit.Test;

public class HttpdecryptApplicationTests {
    @Test
    public void test() {
        HttpResponse response = HttpRequest
                .post("http://127.0.0.1:10400/decrypt/test")
                .body("eyJlbmNyeXB0SW5mbyI6IuWKoOWvhuaVsOaNriIsInZlcnNpb24iOiIxLjAifQ==")
                .send();
        String result = response.bodyText();
        System.out.println(Base64.decodeToString(result)); // success-交易成功
    }
}
