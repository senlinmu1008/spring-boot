package net.zhaoxiaobin.concurrentaction.lock_condition;

import lombok.SneakyThrows;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 异步转同步类
 * 采用lock + condition + signal实现
 *
 * @author zhaoxb
 * @date 2021-09-26 下午5:21
 */
public class LockConditionSignalAsync2Sync {
    /**
     * 查询结果map，key-查询流水号、value-查询结果
     */
    private static Map<String, String> queryResultMap = new ConcurrentHashMap<>();

    /**
     * 可重入锁
     */
    private Lock lock = new ReentrantLock();

    /**
     * 是否已经返回查询结果的条件
     */
    private Condition backed = lock.newCondition();

    /**
     * 查询异步返回的结果，这里包含异步转同步的过程
     *
     * @param seq 查询流水号
     * @return
     */
    @SneakyThrows
    public String getAsyncResult(String seq) {
        // 相当于手动版的synchronized{}
        // 不能放try中，如果lock()失败，执行unlock()会异常
        lock.lock();
        try {
            // 循环判断查询结果中有无该线程查询的流水号结果，如果没有则继续进入等待状态
            // 每次进入await状态会释放锁，每次唤醒会重新竞争锁（如果竞争不到会排队）
            while (queryResultMap.get(seq) == null) {
                backed.await(10, TimeUnit.SECONDS); // 超时10秒结束等待
            }
        } finally {
            lock.unlock(); // happen-before lock()
        }
        // 同步返回结果
        return queryResultMap.remove(seq);
    }

    /**
     * 将查询结果存入共享map，并通知
     *
     * @param seq    查询流水号
     * @param result 查询结果
     */
    public void putAsyncResult(String seq, String result) {
        // 将响应结果存入map，并通知所有线程（会有一个线程命中）
        queryResultMap.put(seq, result);
        // 相当于手动版的synchronized{} + notifyAll()
        lock.lock();
        try {
            // 通知该对象等待队列中的所有线程
            backed.signalAll();
        } finally {
            lock.unlock();
        }
    }
}