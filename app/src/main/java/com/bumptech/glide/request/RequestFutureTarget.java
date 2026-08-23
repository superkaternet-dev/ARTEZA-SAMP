/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.request;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Util;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RequestFutureTarget<R>
implements FutureTarget<R>,
RequestListener<R> {
    private static final Waiter DEFAULT_WAITER = new Waiter();
    private final boolean assertBackgroundThread;
    private GlideException exception;
    private final int height;
    private boolean isCancelled;
    private boolean loadFailed;
    private Request request;
    private R resource;
    private boolean resultReceived;
    private final Waiter waiter;
    private final int width;

    public RequestFutureTarget(int n, int n2) {
        this(n, n2, true, DEFAULT_WAITER);
    }

    RequestFutureTarget(int n, int n2, boolean bl, Waiter waiter) {
        this.width = n;
        this.height = n2;
        this.assertBackgroundThread = bl;
        this.waiter = waiter;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private R doGet(Long serializable) throws ExecutionException, InterruptedException, TimeoutException {
        synchronized (this) {
            if (this.assertBackgroundThread && !this.isDone()) {
                Util.assertBackgroundThread();
            }
            if (this.isCancelled) {
                serializable = new CancellationException();
                throw serializable;
            }
            if (this.loadFailed) {
                serializable = new ExecutionException(this.exception);
                throw serializable;
            }
            if (this.resultReceived) {
                serializable = this.resource;
                return (R)serializable;
            }
            if (serializable == null) {
                this.waiter.waitForTimeout(this, 0L);
            } else if (serializable > 0L) {
                long l = System.currentTimeMillis();
                long l2 = serializable + l;
                while (!this.isDone() && l < l2) {
                    this.waiter.waitForTimeout(this, l2 - l);
                    l = System.currentTimeMillis();
                }
            }
            if (Thread.interrupted()) {
                serializable = new InterruptedException();
                throw serializable;
            }
            if (this.loadFailed) {
                serializable = new ExecutionException(this.exception);
                throw serializable;
            }
            if (this.isCancelled) {
                serializable = new CancellationException();
                throw serializable;
            }
            if (this.resultReceived) {
                serializable = this.resource;
                return (R)serializable;
            }
            serializable = new TimeoutException();
            throw serializable;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public boolean cancel(boolean bl) {
        Request request = null;
        // MONITORENTER : this
        if (this.isDone()) {
            // MONITOREXIT : this
            return false;
        }
        this.isCancelled = true;
        this.waiter.notifyAll(this);
        if (bl) {
            request = this.request;
            this.request = null;
        }
        // MONITOREXIT : this
        if (request == null) return true;
        request.clear();
        return true;
    }

    @Override
    public R get() throws InterruptedException, ExecutionException {
        R r;
        try {
            r = this.doGet(null);
        }
        catch (TimeoutException timeoutException) {
            throw new AssertionError((Object)timeoutException);
        }
        return r;
    }

    @Override
    public R get(long l, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.doGet(timeUnit.toMillis(l));
    }

    @Override
    public Request getRequest() {
        synchronized (this) {
            Request request = this.request;
            return request;
        }
    }

    @Override
    public void getSize(SizeReadyCallback sizeReadyCallback) {
        sizeReadyCallback.onSizeReady(this.width, this.height);
    }

    @Override
    public boolean isCancelled() {
        synchronized (this) {
            boolean bl = this.isCancelled;
            return bl;
        }
    }

    @Override
    public boolean isDone() {
        synchronized (this) {
            boolean bl;
            block4: {
                if (!(this.isCancelled || this.resultReceived || (bl = this.loadFailed))) {
                    bl = false;
                    break block4;
                }
                bl = true;
            }
            return bl;
        }
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void onLoadCleared(Drawable drawable2) {
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void onLoadFailed(Drawable drawable2) {
        // MONITORENTER : this
        // MONITOREXIT : this
    }

    @Override
    public boolean onLoadFailed(GlideException glideException, Object object, Target<R> target, boolean bl) {
        synchronized (this) {
            this.loadFailed = true;
            this.exception = glideException;
            this.waiter.notifyAll(this);
            return false;
        }
    }

    @Override
    public void onLoadStarted(Drawable drawable2) {
    }

    /*
     * Enabled aggressive block sorting
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void onResourceReady(R r, Transition<? super R> transition) {
        // MONITORENTER : this
        // MONITOREXIT : this
    }

    @Override
    public boolean onResourceReady(R r, Object object, Target<R> target, DataSource dataSource, boolean bl) {
        synchronized (this) {
            this.resultReceived = true;
            this.resource = r;
            this.waiter.notifyAll(this);
            return false;
        }
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }

    @Override
    public void setRequest(Request request) {
        synchronized (this) {
            this.request = request;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public String toString() {
        CharSequence charSequence = new StringBuilder();
        charSequence.append(super.toString());
        charSequence.append("[status=");
        String string2 = charSequence.toString();
        Object object = null;
        // MONITORENTER : this
        if (this.isCancelled) {
            charSequence = "CANCELLED";
        } else if (this.loadFailed) {
            charSequence = "FAILURE";
        } else if (this.resultReceived) {
            charSequence = "SUCCESS";
        } else {
            charSequence = "PENDING";
            object = this.request;
        }
        // MONITOREXIT : this
        if (object != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append((String)charSequence);
            stringBuilder.append(", request=[");
            stringBuilder.append(object);
            stringBuilder.append("]]");
            return stringBuilder.toString();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append((String)charSequence);
        ((StringBuilder)object).append("]");
        return ((StringBuilder)object).toString();
    }

    static class Waiter {
        Waiter() {
        }

        void notifyAll(Object object) {
            object.notifyAll();
        }

        void waitForTimeout(Object object, long l) throws InterruptedException {
            object.wait(l);
        }
    }
}

