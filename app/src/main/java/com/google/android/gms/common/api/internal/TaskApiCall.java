/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.RemoteException
 */
package com.google.android.gms.common.api.internal;

import android.os.RemoteException;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.RemoteCall;
import com.google.android.gms.common.api.internal.zacu;
import com.google.android.gms.common.api.internal.zacv;
import com.google.android.gms.common.api.internal.zacw;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.BiConsumer;
import com.google.android.gms.tasks.TaskCompletionSource;

public abstract class TaskApiCall<A extends Api.AnyClient, ResultT> {
    private final Feature[] zaa;
    private final boolean zab;
    private final int zac;

    @Deprecated
    public TaskApiCall() {
        this.zaa = null;
        this.zab = false;
        this.zac = 0;
    }

    protected TaskApiCall(Feature[] featureArray, boolean bl, int n) {
        boolean bl2;
        this.zaa = featureArray;
        boolean bl3 = bl2 = false;
        if (featureArray != null) {
            bl3 = bl2;
            if (bl) {
                bl3 = true;
            }
        }
        this.zab = bl3;
        this.zac = n;
    }

    public static <A extends Api.AnyClient, ResultT> Builder<A, ResultT> builder() {
        return new Builder(null);
    }

    protected abstract void doExecute(A var1, TaskCompletionSource<ResultT> var2) throws RemoteException;

    public boolean shouldAutoResolveMissingFeatures() {
        return this.zab;
    }

    public final int zaa() {
        return this.zac;
    }

    public final Feature[] zab() {
        return this.zaa;
    }

    public static class Builder<A extends Api.AnyClient, ResultT> {
        private RemoteCall<A, TaskCompletionSource<ResultT>> zaa;
        private boolean zab = true;
        private Feature[] zac;
        private int zad = 0;

        private Builder() {
        }

        /* synthetic */ Builder(zacw zacw2) {
        }

        static /* bridge */ /* synthetic */ RemoteCall zaa(Builder builder) {
            return builder.zaa;
        }

        public TaskApiCall<A, ResultT> build() {
            boolean bl = this.zaa != null;
            Preconditions.checkArgument(bl, "execute parameter required");
            return new zacv(this, this.zac, this.zab, this.zad);
        }

        @Deprecated
        public Builder<A, ResultT> execute(BiConsumer<A, TaskCompletionSource<ResultT>> biConsumer) {
            this.zaa = new zacu(biConsumer);
            return this;
        }

        public Builder<A, ResultT> run(RemoteCall<A, TaskCompletionSource<ResultT>> remoteCall) {
            this.zaa = remoteCall;
            return this;
        }

        public Builder<A, ResultT> setAutoResolveMissingFeatures(boolean bl) {
            this.zab = bl;
            return this;
        }

        public Builder<A, ResultT> setFeatures(Feature ... featureArray) {
            this.zac = featureArray;
            return this;
        }

        public Builder<A, ResultT> setMethodKey(int n) {
            this.zad = n;
            return this;
        }
    }
}

