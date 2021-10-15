package net.zhaoxiaobin.concurrentaction.future;

import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

/**
 * FutureTask实现了Runnable和Future接口
 * 既可以给ThreadPoolExecutor线程池去执行，还可以给Thread去执行
 * 还能通过get()获取执行结果
 *
 * @author zhaoxb
 * @date 2021-10-15 下午12:59
 */
public class FutureTaskDemo {
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Test
    public void testFutureTask1() throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask<>(() -> 1 + 1); // 构造传入Callable对象
        executorService.submit(futureTask); // FutureTask当做Runnable
        System.out.println(futureTask.get());
    }

    /**
     * 可以不通过创建线程池，而通过直接创建线程的方式执行异步任务并获取结果
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void testFutureTask2() throws ExecutionException, InterruptedException {
        FutureTask futureTask = new FutureTask<>(() -> 1 + 1); // 构造传入Callable对象
        new Thread(futureTask).start(); // FutureTask当做Runnable
        System.out.println(futureTask.get());
    }

}