package net.zhaoxiaobin.concurrentaction.readwritelock;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 本地缓存实现类
 * 读读不互斥
 * 读写互斥
 * 写写互斥
 *
 * @author zhaoxb
 * @date 2021-10-08 下午7:21
 */
@Slf4j
public class LocalCache {
    /**
     * 缓存map
     */
    private static Map<String, Object> cacheMap = new HashMap<>();

    /**
     * 读写锁
     */
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * 读锁
     */
    private static Lock readLock = readWriteLock.readLock();

    /**
     * 写锁
     */
    private static Lock writeLock = readWriteLock.writeLock();

    /**
     * 读取缓存，读读不互斥，与写操作互斥
     *
     * @param key
     * @return
     */
    public static Object get(String key) {
        readLock.lock();
        try {
            return cacheMap.get(key);
        } finally {
            readLock.unlock(); // happen-before lock()
        }
    }

    /**
     * 写缓存，与写操作和读操作都互斥
     *
     * @param key
     * @param value
     */
    public static void put(String key, Object value) {
        writeLock.lock();
        try {
            cacheMap.put(key, value);
        } finally {
            writeLock.unlock(); // happen-before lock()
        }
    }
}