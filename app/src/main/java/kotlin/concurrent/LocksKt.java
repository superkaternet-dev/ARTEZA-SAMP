/*
 * Decompiled with CFR 0.152.
 */
package kotlin.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.InlineMarker;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001a\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a6\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0002\u0010\u0005\u001a6\u0010\u0006\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00072\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0002\u0010\b\u001a6\u0010\t\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0001*\u00020\u00022\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u0002H\u00010\u0004H\u0087\b\u00f8\u0001\u0000\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001\u00a2\u0006\u0002\u0010\u0005\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\n"}, d2={"read", "T", "Ljava/util/concurrent/locks/ReentrantReadWriteLock;", "action", "Lkotlin/Function0;", "(Ljava/util/concurrent/locks/ReentrantReadWriteLock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "withLock", "Ljava/util/concurrent/locks/Lock;", "(Ljava/util/concurrent/locks/Lock;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "write", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class LocksKt {
    private static final <T> T read(ReentrantReadWriteLock serializable, Function0<? extends T> function0) {
        serializable = ((ReentrantReadWriteLock)serializable).readLock();
        ((ReentrantReadWriteLock.ReadLock)serializable).lock();
        try {
            function0 = function0.invoke();
            return (T)function0;
        }
        finally {
            InlineMarker.finallyStart(1);
            ((ReentrantReadWriteLock.ReadLock)serializable).unlock();
            InlineMarker.finallyEnd(1);
        }
    }

    private static final <T> T withLock(Lock lock, Function0<? extends T> function0) {
        lock.lock();
        try {
            function0 = function0.invoke();
            return (T)function0;
        }
        finally {
            InlineMarker.finallyStart(1);
            lock.unlock();
            InlineMarker.finallyEnd(1);
        }
    }

    private static final <T> T write(ReentrantReadWriteLock serializable, Function0<? extends T> function0) {
        int n;
        ReentrantReadWriteLock.ReadLock readLock = ((ReentrantReadWriteLock)serializable).readLock();
        int n2 = ((ReentrantReadWriteLock)serializable).getWriteHoldCount();
        int n3 = 0;
        int n4 = 0;
        n2 = n2 == 0 ? ((ReentrantReadWriteLock)serializable).getReadHoldCount() : 0;
        for (n = 0; n < n2; ++n) {
            readLock.unlock();
        }
        serializable = ((ReentrantReadWriteLock)serializable).writeLock();
        ((ReentrantReadWriteLock.WriteLock)serializable).lock();
        try {
            function0 = function0.invoke();
        }
        catch (Throwable throwable) {
            InlineMarker.finallyStart(1);
            for (n = n3; n < n2; ++n) {
                readLock.lock();
            }
            ((ReentrantReadWriteLock.WriteLock)serializable).unlock();
            InlineMarker.finallyEnd(1);
            throw throwable;
        }
        InlineMarker.finallyStart(1);
        for (n = n4; n < n2; ++n) {
            readLock.lock();
        }
        ((ReentrantReadWriteLock.WriteLock)serializable).unlock();
        InlineMarker.finallyEnd(1);
        return (T)function0;
    }
}

