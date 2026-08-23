/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  android.net.Uri$Builder
 *  android.os.Bundle
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;

public final class zzn {
    private static final Uri zza = new Uri.Builder().scheme("content").authority("com.google.android.gms.chimera").build();
    private final String zzb;
    private final String zzc;
    private final ComponentName zzd;
    private final int zze;
    private final boolean zzf;

    public zzn(ComponentName componentName, int n) {
        this.zzb = null;
        this.zzc = null;
        Preconditions.checkNotNull(componentName);
        this.zzd = componentName;
        this.zze = n;
        this.zzf = false;
    }

    public zzn(String string2, int n, boolean bl) {
        this(string2, "com.google.android.gms", n, false);
    }

    public zzn(String string2, String string3, int n, boolean bl) {
        Preconditions.checkNotEmpty(string2);
        this.zzb = string2;
        Preconditions.checkNotEmpty(string3);
        this.zzc = string3;
        this.zzd = null;
        this.zze = n;
        this.zzf = bl;
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof zzn)) {
            return false;
        }
        object = (zzn)object;
        return Objects.equal(this.zzb, ((zzn)object).zzb) && Objects.equal(this.zzc, ((zzn)object).zzc) && Objects.equal(this.zzd, ((zzn)object).zzd) && this.zze == ((zzn)object).zze && this.zzf == ((zzn)object).zzf;
    }

    public final int hashCode() {
        return Objects.hashCode(this.zzb, this.zzc, this.zzd, this.zze, this.zzf);
    }

    public final String toString() {
        String string2;
        String string3 = string2 = this.zzb;
        if (string2 == null) {
            Preconditions.checkNotNull(this.zzd);
            string3 = this.zzd.flattenToString();
        }
        return string3;
    }

    public final int zza() {
        return this.zze;
    }

    public final ComponentName zzb() {
        return this.zzd;
    }

    public final Intent zzc(Context object) {
        Object object2;
        if (this.zzb != null) {
            boolean bl = this.zzf;
            Bundle bundle = null;
            object2 = null;
            if (bl) {
                bundle = new Bundle();
                bundle.putString("serviceActionBundleKey", this.zzb);
                try {
                    object = object.getContentResolver().call(zza, "serviceIntentCall", null, bundle);
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    Log.w((String)"ConnectionStatusConfig", (String)"Dynamic intent resolution failed: ".concat(((Object)illegalArgumentException).toString()));
                    object = null;
                }
                object = object == null ? object2 : (Intent)object.getParcelable("serviceResponseIntentKey");
                object2 = object;
                if (object == null) {
                    object2 = String.valueOf(this.zzb);
                    object2 = ((String)object2).length() != 0 ? "Dynamic lookup for intent failed for action: ".concat((String)object2) : new String("Dynamic lookup for intent failed for action: ");
                    Log.w((String)"ConnectionStatusConfig", (String)object2);
                    object2 = object;
                }
            } else {
                object2 = bundle;
            }
            if (object2 == null) {
                return new Intent(this.zzb).setPackage(this.zzc);
            }
        } else {
            object2 = new Intent().setComponent(this.zzd);
        }
        return object2;
    }

    public final String zzd() {
        return this.zzc;
    }
}

