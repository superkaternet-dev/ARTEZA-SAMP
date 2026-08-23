/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.lang.ref.Reference;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Address;
import okhttp3.internal.Platform;
import okhttp3.internal.RouteDatabase;
import okhttp3.internal.Util;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.io.RealConnection;

public final class ConnectionPool {
    static final boolean $assertionsDisabled = false;
    private static final Executor executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttp ConnectionPool", true));
    private final Runnable cleanupRunnable = new Runnable(this){
        final ConnectionPool this$0;
        {
            this.this$0 = connectionPool;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void run() {
            Throwable throwable2;
            while (true) {
                long l;
                if ((l = this.this$0.cleanup(System.nanoTime())) == -1L) {
                    return;
                }
                if (l <= 0L) continue;
                long l2 = l / 1000000L;
                ConnectionPool connectionPool = this.this$0;
                synchronized (connectionPool) {
                    try {
                        try {
                            this.this$0.wait(l2, (int)(l - 1000000L * l2));
                        }
                        catch (InterruptedException interruptedException) {
                            // empty catch block
                        }
                    }
                    catch (Throwable throwable2) {
                        break;
                    }
                }
            }
            {
                throw throwable2;
            }
        }
    };
    boolean cleanupRunning;
    private final Deque<RealConnection> connections = new ArrayDeque<RealConnection>();
    private final long keepAliveDurationNs;
    private final int maxIdleConnections;
    final RouteDatabase routeDatabase = new RouteDatabase();

    public ConnectionPool() {
        this(5, 5L, TimeUnit.MINUTES);
    }

    public ConnectionPool(int n, long l, TimeUnit object) {
        this.maxIdleConnections = n;
        this.keepAliveDurationNs = ((TimeUnit)((Object)object)).toNanos(l);
        if (l > 0L) {
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("keepAliveDuration <= 0: ");
        ((StringBuilder)object).append(l);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    private int pruneAndGetAllocationCount(RealConnection realConnection, long l) {
        List<Reference<StreamAllocation>> list = realConnection.allocations;
        int n = 0;
        while (n < list.size()) {
            if (list.get(n).get() != null) {
                ++n;
                continue;
            }
            Platform platform = Platform.get();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("A connection to ");
            stringBuilder.append(realConnection.route().address().url());
            stringBuilder.append(" was leaked. Did you forget to close a response body?");
            platform.log(5, stringBuilder.toString(), null);
            list.remove(n);
            realConnection.noNewStreams = true;
            if (!list.isEmpty()) continue;
            realConnection.idleAtNanos = l - this.keepAliveDurationNs;
            return 0;
        }
        return list.size();
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    long cleanup(long l) {
        int n = 0;
        int n2 = 0;
        RealConnection realConnection = null;
        long l2 = Long.MIN_VALUE;
        // MONITORENTER : this
        for (RealConnection realConnection2 : this.connections) {
            if (this.pruneAndGetAllocationCount(realConnection2, l) > 0) {
                ++n;
                continue;
            }
            ++n2;
            long l3 = l - realConnection2.idleAtNanos;
            long l4 = l2;
            if (l3 > l2) {
                l4 = l3;
                realConnection = realConnection2;
            }
            l2 = l4;
        }
        l = this.keepAliveDurationNs;
        if (l2 < l && n2 <= this.maxIdleConnections) {
            if (n2 > 0) {
                // MONITOREXIT : this
                return l - l2;
            }
            if (n > 0) {
                // MONITOREXIT : this
                return l;
            }
            this.cleanupRunning = false;
            // MONITOREXIT : this
            return -1L;
        }
        this.connections.remove(realConnection);
        // MONITOREXIT : this
        {
            catch (Throwable throwable) {}
            {
                // MONITOREXIT : this
                throw throwable;
            }
        }
        Util.closeQuietly(realConnection.socket());
        return 0L;
    }

    boolean connectionBecameIdle(RealConnection realConnection) {
        if (Thread.holdsLock(this)) {
            if (!realConnection.noNewStreams && this.maxIdleConnections != 0) {
                this.notifyAll();
                return false;
            }
            this.connections.remove(realConnection);
            return true;
        }
        throw new AssertionError();
    }

    public int connectionCount() {
        synchronized (this) {
            int n = this.connections.size();
            return n;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void evictAll() {
        Iterator<RealConnection> iterator2;
        ArrayList<RealConnection> arrayList = new ArrayList<RealConnection>();
        synchronized (this) {
            iterator2 = this.connections.iterator();
            while (iterator2.hasNext()) {
                RealConnection realConnection = iterator2.next();
                if (!realConnection.allocations.isEmpty()) continue;
                realConnection.noNewStreams = true;
                arrayList.add(realConnection);
                iterator2.remove();
            }
            // MONITOREXIT @DISABLED, blocks:[0, 2] lbl13 : MonitorExitStatement: MONITOREXIT : this
            iterator2 = arrayList.iterator();
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
        while (iterator2.hasNext()) {
            Util.closeQuietly(iterator2.next().socket());
        }
    }

    RealConnection get(Address object, StreamAllocation streamAllocation) {
        if (Thread.holdsLock(this)) {
            for (RealConnection realConnection : this.connections) {
                if (realConnection.allocations.size() >= realConnection.allocationLimit || !((Address)object).equals(realConnection.route().address) || realConnection.noNewStreams) continue;
                streamAllocation.acquire(realConnection);
                return realConnection;
            }
            return null;
        }
        object = new AssertionError();
        throw object;
    }

    public int idleConnectionCount() {
        synchronized (this) {
            int n = 0;
            try {
                Iterator<RealConnection> iterator2 = this.connections.iterator();
                while (iterator2.hasNext()) {
                    boolean bl = iterator2.next().allocations.isEmpty();
                    int n2 = n;
                    if (bl) {
                        n2 = n + 1;
                    }
                    n = n2;
                }
            }
            catch (Throwable throwable) {}
            {
                throw throwable;
            }
            return n;
        }
    }

    void put(RealConnection realConnection) {
        if (Thread.holdsLock(this)) {
            if (!this.cleanupRunning) {
                this.cleanupRunning = true;
                executor.execute(this.cleanupRunnable);
            }
            this.connections.add(realConnection);
            return;
        }
        throw new AssertionError();
    }
}

