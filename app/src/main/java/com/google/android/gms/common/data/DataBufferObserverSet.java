/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.data;

import com.google.android.gms.common.data.DataBufferObserver;
import java.util.HashSet;
import java.util.Iterator;

public final class DataBufferObserverSet
implements DataBufferObserver,
DataBufferObserver.Observable {
    private HashSet<DataBufferObserver> zaa = new HashSet();

    @Override
    public void addObserver(DataBufferObserver dataBufferObserver) {
        this.zaa.add(dataBufferObserver);
    }

    public void clear() {
        this.zaa.clear();
    }

    public boolean hasObservers() {
        return !this.zaa.isEmpty();
    }

    @Override
    public void onDataChanged() {
        Iterator<DataBufferObserver> iterator2 = this.zaa.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDataChanged();
        }
    }

    @Override
    public void onDataRangeChanged(int n, int n2) {
        Iterator<DataBufferObserver> iterator2 = this.zaa.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDataRangeChanged(n, n2);
        }
    }

    @Override
    public void onDataRangeInserted(int n, int n2) {
        Iterator<DataBufferObserver> iterator2 = this.zaa.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDataRangeInserted(n, n2);
        }
    }

    @Override
    public void onDataRangeMoved(int n, int n2, int n3) {
        Iterator<DataBufferObserver> iterator2 = this.zaa.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDataRangeMoved(n, n2, n3);
        }
    }

    @Override
    public void onDataRangeRemoved(int n, int n2) {
        Iterator<DataBufferObserver> iterator2 = this.zaa.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDataRangeRemoved(n, n2);
        }
    }

    @Override
    public void removeObserver(DataBufferObserver dataBufferObserver) {
        this.zaa.remove(dataBufferObserver);
    }
}

