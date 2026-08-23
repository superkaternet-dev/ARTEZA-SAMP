/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.DeadObjectException
 *  android.os.RemoteException
 */
package com.google.android.gms.common.api.internal;

import android.os.DeadObjectException;
import android.os.RemoteException;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.BasePendingResult;
import com.google.android.gms.common.internal.Preconditions;

public class BaseImplementation {

    public static abstract class ApiMethodImpl<R extends Result, A extends Api.AnyClient>
    extends BasePendingResult<R>
    implements ResultHolder<R> {
        private final Api<?> mApi;
        private final Api.AnyClientKey<A> mClientKey;

        @Deprecated
        protected ApiMethodImpl(Api.AnyClientKey<A> anyClientKey, GoogleApiClient googleApiClient) {
            super(Preconditions.checkNotNull(googleApiClient, "GoogleApiClient must not be null"));
            this.mClientKey = Preconditions.checkNotNull(anyClientKey);
            this.mApi = null;
        }

        protected ApiMethodImpl(Api<?> api, GoogleApiClient googleApiClient) {
            super(Preconditions.checkNotNull(googleApiClient, "GoogleApiClient must not be null"));
            Preconditions.checkNotNull(api, "Api must not be null");
            this.mClientKey = api.zab();
            this.mApi = api;
        }

        protected ApiMethodImpl(BasePendingResult.CallbackHandler<R> callbackHandler) {
            super(callbackHandler);
            this.mClientKey = new Api.AnyClientKey();
            this.mApi = null;
        }

        private void setFailedResult(RemoteException remoteException) {
            this.setFailedResult(new Status(8, remoteException.getLocalizedMessage(), null));
        }

        protected abstract void doExecute(A var1) throws RemoteException;

        public final Api<?> getApi() {
            return this.mApi;
        }

        public final Api.AnyClientKey<A> getClientKey() {
            return this.mClientKey;
        }

        protected void onSetFailedResult(R r) {
        }

        public final void run(A a) throws DeadObjectException {
            try {
                this.doExecute(a);
                return;
            }
            catch (RemoteException remoteException) {
                this.setFailedResult(remoteException);
                return;
            }
            catch (DeadObjectException deadObjectException) {
                this.setFailedResult((RemoteException)((Object)deadObjectException));
                throw deadObjectException;
            }
        }

        @Override
        public final void setFailedResult(Status status) {
            Preconditions.checkArgument(status.isSuccess() ^ true, "Failed result must not be success");
            status = this.createFailedResult(status);
            ((BasePendingResult)this).setResult(status);
            this.onSetFailedResult(status);
        }
    }

    public static interface ResultHolder<R> {
        public void setFailedResult(Status var1);

        public void setResult(R var1);
    }
}

