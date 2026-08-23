/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.api;

import android.os.Bundle;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.common.data.AbstractDataBuffer;
import com.google.android.gms.common.data.DataBuffer;
import java.util.Iterator;

public class DataBufferResponse<T, R extends AbstractDataBuffer<T>>
extends Response<R>
implements DataBuffer<T> {
    public DataBufferResponse() {
    }

    public DataBufferResponse(R r) {
        super(r);
    }

    @Override
    public final void close() {
        ((AbstractDataBuffer)this.getResult()).close();
    }

    @Override
    public final T get(int n) {
        return ((AbstractDataBuffer)this.getResult()).get(n);
    }

    @Override
    public final int getCount() {
        return ((AbstractDataBuffer)this.getResult()).getCount();
    }

    @Override
    public final Bundle getMetadata() {
        return ((AbstractDataBuffer)this.getResult()).getMetadata();
    }

    @Override
    public final boolean isClosed() {
        return ((AbstractDataBuffer)this.getResult()).isClosed();
    }

    @Override
    public final Iterator<T> iterator() {
        return ((AbstractDataBuffer)this.getResult()).iterator();
    }

    @Override
    public final void release() {
        ((AbstractDataBuffer)this.getResult()).release();
    }

    @Override
    public final Iterator<T> singleRefIterator() {
        return ((AbstractDataBuffer)this.getResult()).singleRefIterator();
    }
}

