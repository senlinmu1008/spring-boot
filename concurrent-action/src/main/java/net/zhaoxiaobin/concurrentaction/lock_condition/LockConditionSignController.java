package net.zhaoxiaobin.concurrentaction.lock_condition;

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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

/**
 * lock + condition + sign实现等待-通知
 * @author zhaoxb
 * @date 2021-09-16 下午23:00
 */
@RestController
@Slf4j
public class LockConditionSignController {
    /**
     * 查询结果map，key-查询流水号、value-查询结果
     */
    private Map<String, String> queryResultMap = new ConcurrentHashMap<>();

    /**
     * 请求url地址，这里为了模拟，客户端和服务端其实是同一个
     */
    private String baseUrl = "http://127.0.0.1:11400/";

    /**
     * 锁
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 完成条件
     */
    private final Condition done = lock.newCondition();

    /**
     * =============客户端=============
     * 同步查询结果，最后将查询结果同步返回
     *
     * @return
     * @throws InterruptedException
     */
    @PostMapping("/syncQueryData2")
    public String syncQueryData2() throws InterruptedException {
        String seq = RandomUtil.randomNumbers(10);
        log.info("客户端生成查询流水号:{}", seq);
        // 发送查询请求
        HttpRequest.post(baseUrl + "asyncQueryData2")
                .contentType(ContentType.TEXT_PLAIN.toString())
                .body(seq).execute();
        // 经典的编程范式，上锁，等待条件满足被通知，解锁
        // 相当于手动版的synchronized{}
        lock.lock();
        try {
            // 循环判断查询结果中有无该线程查询的流水号结果，如果没有则继续进入等待状态
            // 每次进入await状态会释放锁，每次苏醒会重新竞争锁（如果竞争不到会排队）
            while (queryResultMap.get(seq) == null) {
                done.await(10, TimeUnit.SECONDS); // 超时10秒结束等待
            }
        } finally {
            lock.unlock();
        }
        // 同步返回结果
        return queryResultMap.remove(seq);
    }

    /**
     * =============客户端=============
     * 异步通知查询结果
     *
     * @param result 响应结果
     */
    @PostMapping("/asyncNotice2")
    public void asyncNotice2(@RequestBody String result) {
        log.info("客户端接收异步响应结果:{}", result);
        String seq = result.substring(0, 10);
        // 将响应结果存入map，并通知所有线程（会有一个线程命中）
        queryResultMap.put(seq, result);
        // 相当于手动版的synchronized{} + notifyAll()
        lock.lock();
        try {
            // 通知该对象等待队列中的所有线程
            done.signalAll();
        } finally {
            lock.unlock();
        }
    }


    /**
     * =============服务端=============
     * 接收到请求后，不是同步响应，而是通过异步请求发送响应给客户端
     *
     * @param seq 查询流水号
     */
    @PostMapping("/asyncQueryData2")
    public void asyncQueryData2(@RequestBody String seq) {
        log.info("服务端接收查询流水号:{}", seq);
        ThreadUtil.sleep(100); // 模拟服务端查询耗时
        String now = DateUtil.format(new Date(), "yyyyMMddHHmmssSSS");
        new Thread(() -> HttpRequest.post(baseUrl + "asyncNotice2")
                .contentType(ContentType.TEXT_PLAIN.toString())
                .body(seq + ":" + now).execute()).start();
    }

    @Test
    public void test() {
        long start = System.currentTimeMillis();
        IntStream.range(0, 1000).parallel().forEach(i -> {
            HttpResponse httpResponse = HttpRequest.post("http://127.0.0.1:11400/syncQueryData2")
                    .contentType(ContentType.TEXT_PLAIN.toString())
                    .execute();
            System.out.println("响应码:" + httpResponse.getStatus());
        });
        long end = System.currentTimeMillis();
        System.out.println(end - start); // 10467,10139
    }
}