package net.zhaoxiaobin.concurrentaction.future;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 提交异步任务，最后获取执行结果
 *
 * @author zhaoxb
 * @date 2021-10-14 下午1:17
 */
public class FutureDemo {
    private static ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Test
    public void testFuture1() throws ExecutionException, InterruptedException {
        // Runnable没有结果返回，所以获取为null
        Runnable runnable = () -> System.out.println("异步执行");
        Future<?> future = executorService.submit(runnable);
        System.out.println(future.get()); // null
    }

    @Test
    public void testFuture2() throws ExecutionException, InterruptedException {
        // 异步执行Callable任务（带返回值），并通过Future获取执行结果
        Callable<Integer> callable = () -> 1 + 1;
        Future<Integer> future = executorService.submit(callable);
        System.out.println(future.get()); // 2
    }

    @Test
    public void testFuture3() throws ExecutionException, InterruptedException {
        // 异步执行Runnable，第二个参数为子线程存放结果的引用
        List<String> list = new ArrayList<>();
        Runnable runnable = () -> list.add("1");
        Future<List<String>> future = executorService.submit(runnable, list);
        System.out.println(future.get()); // [1]
    }
}