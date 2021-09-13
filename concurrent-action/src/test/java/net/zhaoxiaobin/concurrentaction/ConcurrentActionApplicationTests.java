package net.zhaoxiaobin.concurrentaction;

import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.stream.IntStream;

@Slf4j
public class ConcurrentActionApplicationTests {

    @Test
    public void testAsyncNotice() {
        IntStream.range(0, 1000).parallel().forEach(i -> {
            HttpResponse httpResponse = HttpRequest.post("http://127.0.0.1:11400/syncQueryData")
                    .contentType(ContentType.TEXT_PLAIN.toString())
                    .execute();
            System.out.println("响应码:" + httpResponse.getStatus());
        });
    }
}
