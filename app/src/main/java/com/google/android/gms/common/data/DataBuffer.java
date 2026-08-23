/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.data;

import android.os.Bundle;
import com.google.android.gms.common.api.Releasable;
import java.io.Closeable;
import java.util.Iterator;

public interface DataBuffer<T>
extends Iterable<T>,
Releasable,
Closeable {
    @Override
    public void close();

    public T get(int var1);

    public int getCount();

    public Bundle getMetadata();

    @Deprecated
    public boolean isClosed();

    @Override
    public Iterator<T> iterator();

    @Override
    public void release();

    public Iterator<T> singleRefIterator();
}

