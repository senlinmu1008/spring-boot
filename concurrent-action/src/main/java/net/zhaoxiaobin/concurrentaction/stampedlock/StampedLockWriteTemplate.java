package net.zhaoxiaobin.concurrentaction.stampedlock;

import java.util.concurrent.locks.StampedLock;

/**
 * StampedLock写模板
 *
 * @author zhaoxb
 * @date 2021-10-11 下午1:54
 */
public class StampedLockWriteTemplate {
    public void readTemplate() {
        StampedLock sl = new StampedLock();
        long stamp = sl.writeLock();
        try {
            // 写共享变量
            // ......
        } finally {
            sl.unlockWrite(stamp);
        }
    }
}