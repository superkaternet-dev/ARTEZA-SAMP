/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.RealCall;
import okhttp3.internal.Util;

public final class Dispatcher {
    private ExecutorService executorService;
    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;
    private final Deque<RealCall.AsyncCall> readyAsyncCalls = new ArrayDeque<RealCall.AsyncCall>();
    private final Deque<RealCall.AsyncCall> runningAsyncCalls = new ArrayDeque<RealCall.AsyncCall>();
    private final Deque<RealCall> runningSyncCalls = new ArrayDeque<RealCall>();

    public Dispatcher() {
    }

    public Dispatcher(ExecutorService executorService) {
        this.executorService = executorService;
    }

    private void promoteCalls() {
        if (this.runningAsyncCalls.size() >= this.maxRequests) {
            return;
        }
        if (this.readyAsyncCalls.isEmpty()) {
            return;
        }
        Iterator<RealCall.AsyncCall> iterator2 = this.readyAsyncCalls.iterator();
        while (iterator2.hasNext()) {
            RealCall.AsyncCall asyncCall = iterator2.next();
            if (this.runningCallsForHost(asyncCall) < this.maxRequestsPerHost) {
                iterator2.remove();
                this.runningAsyncCalls.add(asyncCall);
                this.executorService().execute(asyncCall);
            }
            if (this.runningAsyncCalls.size() < this.maxRequests) continue;
            return;
        }
    }

    private int runningCallsForHost(RealCall.AsyncCall asyncCall) {
        int n = 0;
        Iterator<RealCall.AsyncCall> iterator2 = this.runningAsyncCalls.iterator();
        while (iterator2.hasNext()) {
            int n2 = n;
            if (iterator2.next().host().equals(asyncCall.host())) {
                n2 = n + 1;
            }
            n = n2;
        }
        return n;
    }

    public void cancelAll() {
        synchronized (this) {
            try {
                Iterator<Object> iterator2 = this.readyAsyncCalls.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().cancel();
                }
                iterator2 = this.runningAsyncCalls.iterator();
                while (iterator2.hasNext()) {
                    iterator2.next().cancel();
                }
                iterator2 = this.runningSyncCalls.iterator();
                while (iterator2.hasNext()) {
                    ((RealCall)iterator2.next()).cancel();
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    void enqueue(RealCall.AsyncCall asyncCall) {
        synchronized (this) {
            if (this.runningAsyncCalls.size() < this.maxRequests && this.runningCallsForHost(asyncCall) < this.maxRequestsPerHost) {
                this.runningAsyncCalls.add(asyncCall);
                this.executorService().execute(asyncCall);
            } else {
                this.readyAsyncCalls.add(asyncCall);
            }
            return;
        }
    }

    void executed(RealCall realCall) {
        synchronized (this) {
            this.runningSyncCalls.add(realCall);
            return;
        }
    }

    public ExecutorService executorService() {
        synchronized (this) {
            ExecutorService executorService;
            if (this.executorService == null) {
                TimeUnit timeUnit = TimeUnit.SECONDS;
                SynchronousQueue<Runnable> synchronousQueue = new SynchronousQueue<Runnable>();
                executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, timeUnit, synchronousQueue, Util.threadFactory("OkHttp Dispatcher", false));
                this.executorService = executorService;
            }
            executorService = this.executorService;
            return executorService;
        }
    }

    void finished(Call object) {
        synchronized (this) {
            block4: {
                boolean bl = this.runningSyncCalls.remove(object);
                if (!bl) break block4;
                return;
            }
            object = new AssertionError((Object)"Call wasn't in-flight!");
            throw object;
        }
    }

    void finished(RealCall.AsyncCall object) {
        synchronized (this) {
            if (this.runningAsyncCalls.remove(object)) {
                this.promoteCalls();
                return;
            }
            object = new AssertionError((Object)"AsyncCall wasn't running!");
            throw object;
        }
    }

    public int getMaxRequests() {
        synchronized (this) {
            int n = this.maxRequests;
            return n;
        }
    }

    public int getMaxRequestsPerHost() {
        synchronized (this) {
            int n = this.maxRequestsPerHost;
            return n;
        }
    }

    public List<Call> queuedCalls() {
        synchronized (this) {
            try {
                ArrayList<RealCall> arrayList = new ArrayList<RealCall>();
                Object object = this.readyAsyncCalls.iterator();
                while (object.hasNext()) {
                    arrayList.add(object.next().get());
                }
                object = Collections.unmodifiableList(arrayList);
                return object;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public int queuedCallsCount() {
        synchronized (this) {
            int n = this.readyAsyncCalls.size();
            return n;
        }
    }

    public List<Call> runningCalls() {
        synchronized (this) {
            try {
                ArrayList<RealCall> arrayList = new ArrayList<RealCall>();
                arrayList.addAll(this.runningSyncCalls);
                Object object = this.runningAsyncCalls.iterator();
                while (object.hasNext()) {
                    arrayList.add(object.next().get());
                }
                object = Collections.unmodifiableList(arrayList);
                return object;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public int runningCallsCount() {
        synchronized (this) {
            int n = this.runningAsyncCalls.size();
            int n2 = this.runningSyncCalls.size();
            return n + n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setMaxRequests(int n) {
        synchronized (this) {
            Throwable throwable2;
            if (n >= 1) {
                try {
                    this.maxRequests = n;
                    this.promoteCalls();
                    return;
                }
                catch (Throwable throwable2) {}
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("max < 1: ");
                stringBuilder.append(n);
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
                throw illegalArgumentException;
            }
            throw throwable2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setMaxRequestsPerHost(int n) {
        synchronized (this) {
            Throwable throwable2;
            if (n >= 1) {
                try {
                    this.maxRequestsPerHost = n;
                    this.promoteCalls();
                    return;
                }
                catch (Throwable throwable2) {}
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("max < 1: ");
                stringBuilder.append(n);
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
                throw illegalArgumentException;
            }
            throw throwable2;
        }
    }
}

