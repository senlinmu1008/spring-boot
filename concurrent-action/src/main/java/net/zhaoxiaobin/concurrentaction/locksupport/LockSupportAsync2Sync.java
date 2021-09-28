package net.zhaoxiaobin.concurrentaction.locksupport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.LockSupport;

/**
 * 异步转同步类
 * 采用LockSupport实现
 *
 * @author zhaoxb
 * @date 2021-09-28 下午12:37
 */
public class LockSupportAsync2Sync {
    /**
     * 查询结果map，key-查询流水号、value-查询结果
     */
    private static Map<String, String> queryResultMap = new ConcurrentHashMap<>();

    /**
     * 调用线程map
     */
    private static Map<String, Thread> threadMap = new ConcurrentHashMap<>();

    /**
     * 查询异步返回的结果，这里包含异步转同步的过程
     *
     * @param seq 查询流水号
     * @return
     */
    public String getAsyncResult(String seq) {
        Thread currentThread = Thread.currentThread();
        try {
            threadMap.put(seq, currentThread);
            if (queryResultMap.get(seq) == null) {
                LockSupport.parkUntil(System.currentTimeMillis() + 10000L);
            }
            // 同步返回结果
            return queryResultMap.remove(seq);
        } finally {
            threadMap.remove(seq);
        }
    }


    /**
     * 将查询结果存入共享map，并通知
     *
     * @param seq    查询流水号
     * @param result 查询结果
     */
    public void putAsyncResult(String seq, String result) {
        queryResultMap.put(seq, result);
        // 可能取出为null
        Thread notifyThread = threadMap.get(seq);
        LockSupport.unpark(notifyThread);
    }
}