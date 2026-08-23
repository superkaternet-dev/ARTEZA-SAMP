/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.ListenerHolder;
import com.google.android.gms.common.api.internal.RegisterListenerMethod;
import com.google.android.gms.common.api.internal.RemoteCall;
import com.google.android.gms.common.api.internal.UnregisterListenerMethod;
import com.google.android.gms.common.api.internal.zacj;
import com.google.android.gms.common.api.internal.zack;
import com.google.android.gms.common.api.internal.zacl;
import com.google.android.gms.common.api.internal.zacm;
import com.google.android.gms.common.api.internal.zacn;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.tasks.TaskCompletionSource;

public class RegistrationMethods<A extends Api.AnyClient, L> {
    public final RegisterListenerMethod<A, L> register;
    public final UnregisterListenerMethod<A, L> zaa;
    public final Runnable zab;

    /* synthetic */ RegistrationMethods(RegisterListenerMethod registerListenerMethod, UnregisterListenerMethod unregisterListenerMethod, Runnable runnable, zacn zacn2) {
        this.register = registerListenerMethod;
        this.zaa = unregisterListenerMethod;
        this.zab = runnable;
    }

    public static <A extends Api.AnyClient, L> Builder<A, L> builder() {
        return new Builder(null);
    }

    public static class Builder<A extends Api.AnyClient, L> {
        private RemoteCall<A, TaskCompletionSource<Void>> zaa;
        private RemoteCall<A, TaskCompletionSource<Boolean>> zab;
        private Runnable zac = zacj.zaa;
        private ListenerHolder<L> zad;
        private Feature[] zae;
        private boolean zaf = true;
        private int zag;

        private Builder() {
        }

        /* synthetic */ Builder(zacm zacm2) {
        }

        static /* bridge */ /* synthetic */ RemoteCall zaa(Builder builder) {
            return builder.zaa;
        }

        static /* bridge */ /* synthetic */ RemoteCall zab(Builder builder) {
            return builder.zab;
        }

        public RegistrationMethods<A, L> build() {
            Object object = this.zaa;
            boolean bl = true;
            boolean bl2 = object != null;
            Preconditions.checkArgument(bl2, "Must set register function");
            bl2 = this.zab != null;
            Preconditions.checkArgument(bl2, "Must set unregister function");
            bl2 = this.zad != null ? bl : false;
            Preconditions.checkArgument(bl2, "Must set holder");
            object = Preconditions.checkNotNull(this.zad.getListenerKey(), "Key must not be null");
            return new RegistrationMethods(new zack(this, this.zad, this.zae, this.zaf, this.zag), new zacl(this, (ListenerHolder.ListenerKey)object), this.zac, null);
        }

        public Builder<A, L> onConnectionSuspended(Runnable runnable) {
            this.zac = runnable;
            return this;
        }

        public Builder<A, L> register(RemoteCall<A, TaskCompletionSource<Void>> remoteCall) {
            this.zaa = remoteCall;
            return this;
        }

        public Builder<A, L> setAutoResolveMissingFeatures(boolean bl) {
            this.zaf = bl;
            return this;
        }

        public Builder<A, L> setFeatures(Feature ... featureArray) {
            this.zae = featureArray;
            return this;
        }

        public Builder<A, L> setMethodKey(int n) {
            this.zag = n;
            return this;
        }

        public Builder<A, L> unregister(RemoteCall<A, TaskCompletionSource<Boolean>> remoteCall) {
            this.zab = remoteCall;
            return this;
        }

        public Builder<A, L> withHolder(ListenerHolder<L> listenerHolder) {
            this.zad = listenerHolder;
            return this;
        }
    }
}

