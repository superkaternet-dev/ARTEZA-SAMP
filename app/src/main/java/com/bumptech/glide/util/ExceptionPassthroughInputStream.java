/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util;

import com.bumptech.glide.util.Util;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;

public final class ExceptionPassthroughInputStream
extends InputStream {
    private static final Queue<ExceptionPassthroughInputStream> POOL = Util.createQueue(0);
    private IOException exception;
    private InputStream wrapped;

    ExceptionPassthroughInputStream() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static void clearQueue() {
        Queue<ExceptionPassthroughInputStream> queue = POOL;
        synchronized (queue) {
            Queue<ExceptionPassthroughInputStream> queue2;
            while (!(queue2 = POOL).isEmpty()) {
                queue2.remove();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ExceptionPassthroughInputStream obtain(InputStream inputStream) {
        ExceptionPassthroughInputStream exceptionPassthroughInputStream;
        Object object = POOL;
        synchronized (object) {
            exceptionPassthroughInputStream = object.poll();
        }
        object = exceptionPassthroughInputStream;
        if (exceptionPassthroughInputStream == null) {
            object = new ExceptionPassthroughInputStream();
        }
        ((ExceptionPassthroughInputStream)object).setInputStream(inputStream);
        return object;
    }

    @Override
    public int available() throws IOException {
        return this.wrapped.available();
    }

    @Override
    public void close() throws IOException {
        this.wrapped.close();
    }

    public IOException getException() {
        return this.exception;
    }

    @Override
    public void mark(int n) {
        this.wrapped.mark(n);
    }

    @Override
    public boolean markSupported() {
        return this.wrapped.markSupported();
    }

    @Override
    public int read() throws IOException {
        try {
            int n = this.wrapped.read();
            return n;
        }
        catch (IOException iOException) {
            this.exception = iOException;
            throw iOException;
        }
    }

    @Override
    public int read(byte[] byArray) throws IOException {
        try {
            int n = this.wrapped.read(byArray);
            return n;
        }
        catch (IOException iOException) {
            this.exception = iOException;
            throw iOException;
        }
    }

    @Override
    public int read(byte[] byArray, int n, int n2) throws IOException {
        try {
            n = this.wrapped.read(byArray, n, n2);
            return n;
        }
        catch (IOException iOException) {
            this.exception = iOException;
            throw iOException;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void release() {
        this.exception = null;
        this.wrapped = null;
        Queue<ExceptionPassthroughInputStream> queue = POOL;
        synchronized (queue) {
            queue.offer(this);
            return;
        }
    }

    @Override
    public void reset() throws IOException {
        synchronized (this) {
            this.wrapped.reset();
            return;
        }
    }

    void setInputStream(InputStream inputStream) {
        this.wrapped = inputStream;
    }

    @Override
    public long skip(long l) throws IOException {
        try {
            l = this.wrapped.skip(l);
            return l;
        }
        catch (IOException iOException) {
            this.exception = iOException;
            throw iOException;
        }
    }
}

