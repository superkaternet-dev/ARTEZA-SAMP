/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.android.gms.common.logging;

import android.util.Log;
import com.google.android.gms.common.internal.GmsLogger;
import java.util.Locale;

public class Logger {
    private final String zza;
    private final String zzb;
    private final GmsLogger zzc;
    private final int zzd;

    public Logger(String string2, String ... object) {
        int n;
        int n2 = ((String[])object).length;
        if (n2 == 0) {
            object = "";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append('[');
            for (n = 0; n < n2; ++n) {
                String string3 = object[n];
                if (stringBuilder.length() > 1) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(string3);
            }
            stringBuilder.append("] ");
            object = stringBuilder.toString();
        }
        this.zzb = object;
        this.zza = string2;
        this.zzc = new GmsLogger(string2);
        for (n = 2; n <= 7 && !Log.isLoggable((String)this.zza, (int)n); ++n) {
        }
        this.zzd = n;
    }

    public void d(String string2, Object ... objectArray) {
        if (this.isLoggable(3)) {
            Log.d((String)this.zza, (String)this.format(string2, objectArray));
        }
    }

    public void e(String string2, Throwable throwable, Object ... objectArray) {
        Log.e((String)this.zza, (String)this.format(string2, objectArray), (Throwable)throwable);
    }

    public void e(String string2, Object ... objectArray) {
        Log.e((String)this.zza, (String)this.format(string2, objectArray));
    }

    protected String format(String string2, Object ... objectArray) {
        String string3 = string2;
        if (objectArray != null) {
            string3 = string2;
            if (objectArray.length > 0) {
                string3 = String.format(Locale.US, string2, objectArray);
            }
        }
        return this.zzb.concat(string3);
    }

    public String getTag() {
        return this.zza;
    }

    public void i(String string2, Object ... objectArray) {
        Log.i((String)this.zza, (String)this.format(string2, objectArray));
    }

    public boolean isLoggable(int n) {
        return this.zzd <= n;
    }

    public void v(String string2, Throwable throwable, Object ... objectArray) {
        if (this.isLoggable(2)) {
            Log.v((String)this.zza, (String)this.format(string2, objectArray), (Throwable)throwable);
        }
    }

    public void v(String string2, Object ... objectArray) {
        if (this.isLoggable(2)) {
            Log.v((String)this.zza, (String)this.format(string2, objectArray));
        }
    }

    public void w(String string2, Object ... objectArray) {
        Log.w((String)this.zza, (String)this.format(string2, objectArray));
    }

    public void wtf(String string2, Throwable throwable, Object ... objectArray) {
        Log.wtf((String)this.zza, (String)this.format(string2, objectArray), (Throwable)throwable);
    }

    public void wtf(Throwable throwable) {
        Log.wtf((String)this.zza, (Throwable)throwable);
    }
}

