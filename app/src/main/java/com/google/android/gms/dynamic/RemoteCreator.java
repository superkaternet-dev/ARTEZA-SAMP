/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.IBinder
 */
package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.internal.Preconditions;

public abstract class RemoteCreator<T> {
    private final String zza;
    private T zzb;

    protected RemoteCreator(String string2) {
        this.zza = string2;
    }

    protected abstract T getRemoteCreator(IBinder var1);

    protected final T getRemoteCreatorInstance(Context object) throws RemoteCreatorException {
        if (this.zzb == null) {
            Preconditions.checkNotNull(object);
            object = GooglePlayServicesUtilLight.getRemoteContext((Context)object);
            if (object != null) {
                object = object.getClassLoader();
                try {
                    this.zzb = this.getRemoteCreator((IBinder)((ClassLoader)object).loadClass(this.zza).newInstance());
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw new RemoteCreatorException("Could not access creator.", illegalAccessException);
                }
                catch (InstantiationException instantiationException) {
                    throw new RemoteCreatorException("Could not instantiate creator.", instantiationException);
                }
                catch (ClassNotFoundException classNotFoundException) {
                    throw new RemoteCreatorException("Could not load creator class.", classNotFoundException);
                }
            } else {
                throw new RemoteCreatorException("Could not get remote context.");
            }
        }
        return this.zzb;
    }

    public static class RemoteCreatorException
    extends Exception {
        public RemoteCreatorException(String string2) {
            super(string2);
        }

        public RemoteCreatorException(String string2, Throwable throwable) {
            super(string2, throwable);
        }
    }
}

