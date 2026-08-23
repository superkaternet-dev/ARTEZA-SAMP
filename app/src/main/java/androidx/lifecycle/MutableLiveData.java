/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.lifecycle.LiveData;

public class MutableLiveData<T>
extends LiveData<T> {
    public MutableLiveData() {
    }

    public MutableLiveData(T t) {
        super(t);
    }

    @Override
    public void postValue(T t) {
        super.postValue(t);
    }

    @Override
    public void setValue(T t) {
        super.setValue(t);
    }
}

