/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.file;

import com.liulishuo.okdownload.core.Util;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class FileLock {
    private static final String TAG = "FileLock";
    private static final long WAIT_RELEASE_LOCK_NANO = TimeUnit.MILLISECONDS.toNanos(100L);
    private final Map<String, AtomicInteger> fileLockCountMap;
    private final Map<String, Thread> waitThreadForFileLockMap;

    FileLock() {
        this(new HashMap<String, AtomicInteger>(), new HashMap<String, Thread>());
    }

    FileLock(Map<String, AtomicInteger> map, Map<String, Thread> map2) {
        this.fileLockCountMap = map;
        this.waitThreadForFileLockMap = map2;
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void decreaseLock(String string2) {
        Object object = this.fileLockCountMap;
        // MONITORENTER : object
        AtomicInteger atomicInteger = this.fileLockCountMap.get(string2);
        // MONITOREXIT : object
        if (atomicInteger == null) return;
        if (atomicInteger.decrementAndGet() != 0) return;
        object = new StringBuilder();
        ((StringBuilder)object).append("decreaseLock decrease lock-count to 0 ");
        ((StringBuilder)object).append(string2);
        Util.d(TAG, ((StringBuilder)object).toString());
        object = this.waitThreadForFileLockMap;
        // MONITORENTER : object
        Thread thread2 = this.waitThreadForFileLockMap.get(string2);
        if (thread2 != null) {
            this.waitThreadForFileLockMap.remove(string2);
            // MONITOREXIT : object
        }
        if (thread2 != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append("decreaseLock ");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(" unpark locked thread ");
            ((StringBuilder)object).append(atomicInteger);
            Util.d(TAG, ((StringBuilder)object).toString());
            this.unpark(thread2);
        }
        object = this.fileLockCountMap;
        // MONITORENTER : object
        this.fileLockCountMap.remove(string2);
        // MONITOREXIT : object
        return;
        catch (Throwable throwable) {
            throw throwable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void increaseLock(String string2) {
        Object object;
        Object object2 = this.fileLockCountMap;
        synchronized (object2) {
            object = this.fileLockCountMap.get(string2);
        }
        object2 = object;
        if (object == null) {
            object2 = new AtomicInteger(0);
            object = this.fileLockCountMap;
            synchronized (object) {
                this.fileLockCountMap.put(string2, (AtomicInteger)object2);
            }
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("increaseLock increase lock-count to ");
        ((StringBuilder)object).append(((AtomicInteger)object2).incrementAndGet());
        ((StringBuilder)object).append(string2);
        Util.d(TAG, ((StringBuilder)object).toString());
    }

    boolean isNotLocked(AtomicInteger atomicInteger) {
        boolean bl = atomicInteger.get() <= 0;
        return bl;
    }

    void park() {
        LockSupport.park(WAIT_RELEASE_LOCK_NANO);
    }

    void unpark(Thread thread2) {
        LockSupport.unpark(thread2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void waitForRelease(String string2) {
        AtomicInteger atomicInteger;
        Object object = this.fileLockCountMap;
        synchronized (object) {
            atomicInteger = this.fileLockCountMap.get(string2);
        }
        if (atomicInteger == null) return;
        if (atomicInteger.get() <= 0) {
            return;
        }
        object = this.waitThreadForFileLockMap;
        synchronized (object) {
            this.waitThreadForFileLockMap.put(string2, Thread.currentThread());
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("waitForRelease start ");
        ((StringBuilder)object).append(string2);
        Util.d(TAG, ((StringBuilder)object).toString());
        while (true) {
            if (this.isNotLocked(atomicInteger)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("waitForRelease finish ");
                ((StringBuilder)object).append(string2);
                Util.d(TAG, ((StringBuilder)object).toString());
                return;
            }
            this.park();
        }
    }
}

