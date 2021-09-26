package net.zhaoxiaobin.concurrentaction.wait_notify;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.stream.IntStream;

/**
 * synchronized + wait + notify实现等待-通知
 *
 * @author zhaoxb
 * @date 2021-09-25 下午12:59
 */
@RestController
@Slf4j
public class WaitAndNotifyController {
    /**
     * 异步转同步对象
     */
    private WaitAndNotifyAsync2Sync async2Sync = new WaitAndNotifyAsync2Sync();

    /**
     * 请求url地址，这里为了模拟，客户端和服务端其实是同一个
     */
    private String baseUrl = "http://127.0.0.1:11400/";

    /**
     * =============客户端=============
     * 同步查询结果，最后将查询结果同步返回
     *
     * @return
     */
    @PostMapping("/syncQueryData1")
    public String syncQueryData1() {
        String seq = RandomUtil.randomNumbers(10);
        log.info("客户端生成查询流水号:{}", seq);
        // 发送查询请求
        HttpRequest.post(baseUrl + "asyncQueryData1")
                .contentType(ContentType.TEXT_PLAIN.toString())
                .body(seq).execute();
        return async2Sync.getAsyncResult(seq); // 单例调用
    }

    /**
     * =============客户端=============
     * 异步通知查询结果
     *
     * @param result 响应结果
     */
    @PostMapping("/asyncNotice1")
    public void asyncNotice1(@RequestBody String result) {
        log.info("客户端接收异步响应结果:{}", result);
        String seq = result.substring(0, 10);
        async2Sync.putAsyncResult(seq, result);
    }

    /**
     * =============服务端=============
     * 接收到请求后，不是同步响应，而是通过异步请求发送响应给客户端
     *
     * @param seq 查询流水号
     */
    @PostMapping("/asyncQueryData1")
    public void asyncQueryData1(@RequestBody String seq) {
        log.info("服务端接收查询流水号:{}", seq);
        ThreadUtil.sleep(100); // 模拟服务端查询耗时
        String now = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        new Thread(() -> HttpRequest.post(baseUrl + "asyncNotice1")
                .contentType(ContentType.TEXT_PLAIN.toString())
                .body(seq + ":" + now).execute()).start();
    }


    @Test
    public void test() {
        long start = System.currentTimeMillis();
        IntStream.range(0, 1000).parallel().forEach(i -> {
            HttpResponse httpResponse = HttpRequest.post("http://127.0.0.1:11400/syncQueryData1")
                    .contentType(ContentType.TEXT_PLAIN.toString())
                    .execute();
            if (!httpResponse.isOk()) {
                log.error("响应码错误:{}", httpResponse.getStatus());
            }
        });
        long end = System.currentTimeMillis();
        log.info("调用1000次耗时:{}", end - start); // 10523,10171
    }
}