/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import java.util.Iterator;
import java.util.Map;

public class MediatorLiveData<T>
extends MutableLiveData<T> {
    private SafeIterableMap<LiveData<?>, Source<?>> mSources = new SafeIterableMap();

    public <S> void addSource(LiveData<S> object, Observer<? super S> observer) {
        Source<S> source = new Source<S>(object, observer);
        if ((object = this.mSources.putIfAbsent((LiveData<?>)object, source)) != null && ((Source)object).mObserver != observer) {
            throw new IllegalArgumentException("This source was already added with the different observer");
        }
        if (object != null) {
            return;
        }
        if (this.hasActiveObservers()) {
            source.plug();
        }
    }

    @Override
    protected void onActive() {
        Iterator<Map.Entry<LiveData<?>, Source<?>>> iterator2 = this.mSources.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().getValue().plug();
        }
    }

    @Override
    protected void onInactive() {
        Iterator<Map.Entry<LiveData<?>, Source<?>>> iterator2 = this.mSources.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().getValue().unplug();
        }
    }

    public <S> void removeSource(LiveData<S> object) {
        if ((object = this.mSources.remove((LiveData<?>)object)) != null) {
            ((Source)object).unplug();
        }
    }

    private static class Source<V>
    implements Observer<V> {
        final LiveData<V> mLiveData;
        final Observer<? super V> mObserver;
        int mVersion = -1;

        Source(LiveData<V> liveData, Observer<? super V> observer) {
            this.mLiveData = liveData;
            this.mObserver = observer;
        }

        @Override
        public void onChanged(V v) {
            if (this.mVersion != this.mLiveData.getVersion()) {
                this.mVersion = this.mLiveData.getVersion();
                this.mObserver.onChanged(v);
            }
        }

        void plug() {
            this.mLiveData.observeForever(this);
        }

        void unplug() {
            this.mLiveData.removeObserver(this);
        }
    }
}

