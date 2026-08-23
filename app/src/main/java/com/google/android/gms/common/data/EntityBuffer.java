/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.data;

import com.google.android.gms.common.data.AbstractDataBuffer;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.common.internal.Preconditions;
import java.util.ArrayList;

public abstract class EntityBuffer<T>
extends AbstractDataBuffer<T> {
    private boolean zaa = false;
    private ArrayList<Integer> zab;

    protected EntityBuffer(DataHolder dataHolder) {
        super(dataHolder);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private final void zab() {
        synchronized (this) {
            block7: {
                int n;
                Object object;
                int n2;
                String string2;
                Object object2;
                int n3;
                block8: {
                    if (this.zaa) return;
                    n3 = Preconditions.checkNotNull(this.mDataHolder).getCount();
                    object2 = new ArrayList();
                    this.zab = object2;
                    if (n3 <= 0) break block7;
                    ((ArrayList)object2).add(0);
                    string2 = this.getPrimaryDataMarkerColumn();
                    n2 = this.mDataHolder.getWindowIndex(0);
                    object = this.mDataHolder.getString(string2, 0, n2);
                    for (n2 = 1; n2 < n3; ++n2) {
                        n = this.mDataHolder.getWindowIndex(n2);
                        String string3 = this.mDataHolder.getString(string2, n2, n);
                        if (string3 != null) {
                            object2 = object;
                            if (!string3.equals(object)) {
                                this.zab.add(n2);
                                object2 = string3;
                            }
                            object = object2;
                            continue;
                        }
                        break block8;
                    }
                    break block7;
                }
                n3 = String.valueOf(string2).length();
                object = new StringBuilder(n3 + 78);
                ((StringBuilder)object).append("Missing value for markerColumn: ");
                ((StringBuilder)object).append(string2);
                ((StringBuilder)object).append(", at row: ");
                ((StringBuilder)object).append(n2);
                ((StringBuilder)object).append(", for window: ");
                ((StringBuilder)object).append(n);
                object2 = new NullPointerException(((StringBuilder)object).toString());
                throw object2;
            }
            this.zaa = true;
            return;
        }
    }

    @Override
    public final T get(int n) {
        this.zab();
        int n2 = this.zaa(n);
        int n3 = 0;
        if (n >= 0) {
            if (n == this.zab.size()) {
                n = n3;
            } else {
                int n4 = n == this.zab.size() - 1 ? Preconditions.checkNotNull(this.mDataHolder).getCount() - this.zab.get(n) : this.zab.get(n + 1) - this.zab.get(n);
                if (n4 == 1) {
                    n = this.zaa(n);
                    n4 = Preconditions.checkNotNull(this.mDataHolder).getWindowIndex(n);
                    String string2 = this.getChildDataMarkerColumn();
                    n = string2 != null && this.mDataHolder.getString(string2, n, n4) == null ? n3 : 1;
                } else {
                    n = n4;
                }
            }
        } else {
            n = n3;
        }
        return this.getEntry(n2, n);
    }

    protected String getChildDataMarkerColumn() {
        return null;
    }

    @Override
    public int getCount() {
        this.zab();
        return this.zab.size();
    }

    protected abstract T getEntry(int var1, int var2);

    protected abstract String getPrimaryDataMarkerColumn();

    final int zaa(int n) {
        if (n >= 0 && n < this.zab.size()) {
            return this.zab.get(n);
        }
        StringBuilder stringBuilder = new StringBuilder(53);
        stringBuilder.append("Position ");
        stringBuilder.append(n);
        stringBuilder.append(" is out of bounds for this buffer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}

