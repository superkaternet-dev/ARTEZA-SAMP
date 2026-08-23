/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package com.google.android.gms.dynamic;

import android.os.IBinder;
import android.os.IInterface;
import com.google.android.gms.dynamic.zzb;

public interface IObjectWrapper
extends IInterface {

    public static abstract class Stub
    extends com.google.android.gms.internal.common.zzb
    implements IObjectWrapper {
        public Stub() {
            super("com.google.android.gms.dynamic.IObjectWrapper");
        }

        public static IObjectWrapper asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamic.IObjectWrapper");
            if (iInterface instanceof IObjectWrapper) {
                return (IObjectWrapper)iInterface;
            }
            return new zzb(iBinder);
        }
    }
}

