/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;

public final class GmsLogger {
    private final String zza;
    private final String zzb;

    public GmsLogger(String string2) {
        this(string2, null);
    }

    public GmsLogger(String string2, String string3) {
        Preconditions.checkNotNull(string2, "log tag cannot be null");
        boolean bl = string2.length() <= 23;
        Preconditions.checkArgument(bl, "tag \"%s\" is longer than the %d character maximum", string2, 23);
        this.zza = string2;
        if (string3 != null && string3.length() > 0) {
            this.zzb = string3;
            return;
        }
        this.zzb = null;
    }

    private final String zza(String string2) {
        String string3 = this.zzb;
        if (string3 == null) {
            return string2;
        }
        return string3.concat(string2);
    }

    private final String zzb(String string2, Object ... object) {
        string2 = String.format(string2, (Object[])object);
        object = this.zzb;
        if (object == null) {
            return string2;
        }
        return ((String)object).concat(string2);
    }

    public boolean canLog(int n) {
        return Log.isLoggable((String)this.zza, (int)n);
    }

    public boolean canLogPii() {
        return false;
    }

    public void d(String string2, String string3) {
        if (this.canLog(3)) {
            Log.d((String)string2, (String)this.zza(string3));
        }
    }

    public void d(String string2, String string3, Throwable throwable) {
        if (this.canLog(3)) {
            Log.d((String)string2, (String)this.zza(string3), (Throwable)throwable);
        }
    }

    public void e(String string2, String string3) {
        if (this.canLog(6)) {
            Log.e((String)string2, (String)this.zza(string3));
        }
    }

    public void e(String string2, String string3, Throwable throwable) {
        if (this.canLog(6)) {
            Log.e((String)string2, (String)this.zza(string3), (Throwable)throwable);
        }
    }

    public void efmt(String string2, String string3, Object ... objectArray) {
        if (this.canLog(6)) {
            Log.e((String)string2, (String)this.zzb(string3, objectArray));
        }
    }

    public void i(String string2, String string3) {
        if (this.canLog(4)) {
            Log.i((String)string2, (String)this.zza(string3));
        }
    }

    public void i(String string2, String string3, Throwable throwable) {
        if (this.canLog(4)) {
            Log.i((String)string2, (String)this.zza(string3), (Throwable)throwable);
        }
    }

    public void pii(String string2, String string3) {
    }

    public void pii(String string2, String string3, Throwable throwable) {
    }

    public void v(String string2, String string3) {
        if (this.canLog(2)) {
            Log.v((String)string2, (String)this.zza(string3));
        }
    }

    public void v(String string2, String string3, Throwable throwable) {
        if (this.canLog(2)) {
            Log.v((String)string2, (String)this.zza(string3), (Throwable)throwable);
        }
    }

    public void w(String string2, String string3) {
        if (this.canLog(5)) {
            Log.w((String)string2, (String)this.zza(string3));
        }
    }

    public void w(String string2, String string3, Throwable throwable) {
        if (this.canLog(5)) {
            Log.w((String)string2, (String)this.zza(string3), (Throwable)throwable);
        }
    }

    public void wfmt(String string2, String string3, Object ... objectArray) {
        if (this.canLog(5)) {
            Log.w((String)this.zza, (String)this.zzb(string3, objectArray));
        }
    }

    public void wtf(String string2, String string3, Throwable throwable) {
        if (this.canLog(7)) {
            Log.e((String)string2, (String)this.zza(string3), (Throwable)throwable);
            Log.wtf((String)string2, (String)this.zza(string3), (Throwable)throwable);
        }
    }
}

