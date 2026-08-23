/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.data;

import com.google.android.gms.common.data.DataBuffer;
import com.google.android.gms.common.data.DataBufferIterator;
import com.google.android.gms.common.data.DataBufferRef;
import com.google.android.gms.common.internal.Preconditions;
import java.util.NoSuchElementException;

public class SingleRefDataBufferIterator<T>
extends DataBufferIterator<T> {
    private T zac;

    public SingleRefDataBufferIterator(DataBuffer<T> dataBuffer) {
        super(dataBuffer);
    }

    @Override
    public final T next() {
        if (this.hasNext()) {
            int n;
            this.zab = n = this.zab + 1;
            if (n == 0) {
                Object object = Preconditions.checkNotNull(this.zaa.get(0));
                this.zac = object;
                if (!(object instanceof DataBufferRef)) {
                    object = String.valueOf(object.getClass());
                    StringBuilder stringBuilder = new StringBuilder(String.valueOf(object).length() + 44);
                    stringBuilder.append("DataBuffer reference of type ");
                    stringBuilder.append((String)object);
                    stringBuilder.append(" is not movable");
                    throw new IllegalStateException(stringBuilder.toString());
                }
            } else {
                ((DataBufferRef)Preconditions.checkNotNull(this.zac)).zaa(this.zab);
            }
            return this.zac;
        }
        int n = this.zab;
        StringBuilder stringBuilder = new StringBuilder(46);
        stringBuilder.append("Cannot advance the iterator beyond ");
        stringBuilder.append(n);
        throw new NoSuchElementException(stringBuilder.toString());
    }
}

