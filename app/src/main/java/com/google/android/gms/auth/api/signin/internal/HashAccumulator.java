/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.auth.api.signin.internal;

public class HashAccumulator {
    static int zaa = 31;
    private int zab = 1;

    public HashAccumulator addObject(Object object) {
        int n = zaa;
        int n2 = this.zab;
        int n3 = object == null ? 0 : object.hashCode();
        this.zab = n * n2 + n3;
        return this;
    }

    public int hash() {
        return this.zab;
    }

    public final HashAccumulator zaa(boolean bl) {
        this.zab = zaa * this.zab + bl;
        return this;
    }
}

