/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 *  android.os.IInterface
 */
package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import com.google.android.gms.common.internal.zzad;
import com.google.android.gms.common.internal.zzaf;
import com.google.android.gms.internal.common.zzb;

public abstract class zzae
extends zzb
implements zzaf {
    public static zzaf zzb(IBinder iBinder) {
        if (iBinder == null) {
            return null;
        }
        IInterface iInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGoogleCertificatesApi");
        if (iInterface instanceof zzaf) {
            return (zzaf)iInterface;
        }
        return new zzad(iBinder);
    }
}

