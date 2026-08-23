/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 */
package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.common.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.common.internal.zzag;

@Deprecated
public final class GoogleServices {
    private static final Object zza = new Object();
    private static GoogleServices zzb;
    private final String zzc;
    private final Status zzd;
    private final boolean zze;
    private final boolean zzf;

    GoogleServices(Context object) {
        Object object2 = object.getResources();
        int n = object2.getIdentifier("google_app_measurement_enable", "integer", object2.getResourcePackageName(R.string.common_google_play_services_unknown_issue));
        boolean bl = true;
        boolean bl2 = true;
        if (n != 0) {
            bl = (n = object2.getInteger(n)) == 0;
            if (n == 0) {
                bl2 = false;
            }
            this.zzf = bl;
        } else {
            this.zzf = false;
            bl2 = bl;
        }
        this.zze = bl2;
        object2 = zzag.zzb(object);
        object = object2 == null ? new StringResourceValueReader((Context)object).getString("google_app_id") : object2;
        if (TextUtils.isEmpty((CharSequence)object)) {
            this.zzd = new Status(10, "Missing google app id value from from string resources with name google_app_id.");
            this.zzc = null;
            return;
        }
        this.zzc = object;
        this.zzd = Status.RESULT_SUCCESS;
    }

    GoogleServices(String string2, boolean bl) {
        this.zzc = string2;
        this.zzd = Status.RESULT_SUCCESS;
        this.zze = bl;
        this.zzf = bl ^ true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static GoogleServices checkInitialized(String string2) {
        Object object = zza;
        synchronized (object) {
            Object object2 = zzb;
            if (object2 != null) {
                return object2;
            }
            int n = String.valueOf(string2).length();
            object2 = new StringBuilder(n + 34);
            ((StringBuilder)object2).append("Initialize must be called before ");
            ((StringBuilder)object2).append(string2);
            ((StringBuilder)object2).append(".");
            IllegalStateException illegalStateException = new IllegalStateException(((StringBuilder)object2).toString());
            throw illegalStateException;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static void clearInstanceForTest() {
        Object object = zza;
        synchronized (object) {
            zzb = null;
            return;
        }
    }

    public static String getGoogleAppId() {
        return GoogleServices.checkInitialized((String)"getGoogleAppId").zzc;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Status initialize(Context object) {
        Preconditions.checkNotNull(object, "Context must not be null.");
        Object object2 = zza;
        synchronized (object2) {
            GoogleServices googleServices;
            if (zzb != null) return GoogleServices.zzb.zzd;
            zzb = googleServices = new GoogleServices((Context)object);
            return GoogleServices.zzb.zzd;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Status initialize(Context object, String object2, boolean bl) {
        Preconditions.checkNotNull(object, "Context must not be null.");
        Preconditions.checkNotEmpty((String)object2, "App ID must be nonempty.");
        object = zza;
        synchronized (object) {
            GoogleServices googleServices = zzb;
            if (googleServices != null) {
                return googleServices.checkGoogleAppId((String)object2);
            }
            zzb = googleServices = new GoogleServices((String)object2, bl);
            return googleServices.zzd;
        }
    }

    public static boolean isMeasurementEnabled() {
        GoogleServices googleServices = GoogleServices.checkInitialized("isMeasurementEnabled");
        return googleServices.zzd.isSuccess() && googleServices.zze;
    }

    public static boolean isMeasurementExplicitlyDisabled() {
        return GoogleServices.checkInitialized((String)"isMeasurementExplicitlyDisabled").zzf;
    }

    Status checkGoogleAppId(String charSequence) {
        String string2 = this.zzc;
        if (string2 != null && !string2.equals(charSequence)) {
            string2 = this.zzc;
            charSequence = new StringBuilder(String.valueOf(string2).length() + 97);
            ((StringBuilder)charSequence).append("Initialize was called with two different Google App IDs.  Only the first app ID will be used: '");
            ((StringBuilder)charSequence).append(string2);
            ((StringBuilder)charSequence).append("'.");
            return new Status(10, ((StringBuilder)charSequence).toString());
        }
        return Status.RESULT_SUCCESS;
    }
}

