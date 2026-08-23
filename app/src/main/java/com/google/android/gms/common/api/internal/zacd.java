/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.SystemClock
 */
package com.google.android.gms.common.api.internal;

import android.os.SystemClock;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.zabq;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;
import com.google.android.gms.common.internal.MethodInvocation;
import com.google.android.gms.common.internal.RootTelemetryConfigManager;
import com.google.android.gms.common.internal.RootTelemetryConfiguration;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

final class zacd<T>
implements OnCompleteListener<T> {
    private final GoogleApiManager zaa;
    private final int zab;
    private final ApiKey<?> zac;
    private final long zad;
    private final long zae;

    zacd(GoogleApiManager googleApiManager, int n, ApiKey<?> apiKey, long l, long l2, String string2, String string3) {
        this.zaa = googleApiManager;
        this.zab = n;
        this.zac = apiKey;
        this.zad = l;
        this.zae = l2;
    }

    static <T> zacd<T> zaa(GoogleApiManager googleApiManager, int n, ApiKey<?> apiKey) {
        boolean bl;
        if (!googleApiManager.zaF()) {
            return null;
        }
        Object object = RootTelemetryConfigManager.getInstance().getConfig();
        if (object != null) {
            if (!((RootTelemetryConfiguration)object).getMethodInvocationTelemetryEnabled()) {
                return null;
            }
            boolean bl2 = ((RootTelemetryConfiguration)object).getMethodTimingTelemetryEnabled();
            object = googleApiManager.zak(apiKey);
            bl = bl2;
            if (object != null) {
                if (!(((zabq)object).zaf() instanceof BaseGmsClient)) {
                    return null;
                }
                Object object2 = (BaseGmsClient)((Object)((zabq)object).zaf());
                bl = bl2;
                if (((BaseGmsClient)object2).hasConnectionInfo()) {
                    bl = bl2;
                    if (!((BaseGmsClient)object2).isConnecting()) {
                        if ((object2 = zacd.zab(object, object2, n)) == null) {
                            return null;
                        }
                        ((zabq)object).zaq();
                        bl = ((ConnectionTelemetryConfiguration)object2).getMethodTimingTelemetryEnabled();
                    }
                }
            }
        } else {
            bl = true;
        }
        long l = bl ? System.currentTimeMillis() : 0L;
        long l2 = bl ? SystemClock.elapsedRealtime() : 0L;
        return new zacd<T>(googleApiManager, n, apiKey, l, l2, null, null);
    }

    private static ConnectionTelemetryConfiguration zab(zabq<?> zabq2, BaseGmsClient<?> object, int n) {
        int[] nArray;
        if ((object = ((BaseGmsClient)object).getTelemetryConfiguration()) != null && ((ConnectionTelemetryConfiguration)object).getMethodInvocationTelemetryEnabled() && !((nArray = ((ConnectionTelemetryConfiguration)object).getMethodInvocationMethodKeyAllowlist()) != null ? !ArrayUtils.contains(nArray, n) : (nArray = ((ConnectionTelemetryConfiguration)object).getMethodInvocationMethodKeyDisallowlist()) != null && ArrayUtils.contains(nArray, n))) {
            if (zabq2.zac() < ((ConnectionTelemetryConfiguration)object).getMaxMethodInvocationsLogged()) {
                return object;
            }
            return null;
        }
        return null;
    }

    @Override
    public final void onComplete(Task<T> object) {
        if (!this.zaa.zaF()) {
            return;
        }
        RootTelemetryConfiguration rootTelemetryConfiguration = RootTelemetryConfigManager.getInstance().getConfig();
        if (rootTelemetryConfiguration != null && !rootTelemetryConfiguration.getMethodInvocationTelemetryEnabled()) {
            return;
        }
        Object object2 = this.zaa.zak(this.zac);
        if (object2 != null && ((zabq)object2).zaf() instanceof BaseGmsClient) {
            long l;
            int n;
            int n2;
            int n3;
            int n4;
            BaseGmsClient baseGmsClient = (BaseGmsClient)((Object)((zabq)object2).zaf());
            long l2 = this.zad;
            int n5 = 1;
            int n6 = l2 > 0L ? 1 : 0;
            int n7 = baseGmsClient.getGCoreServiceId();
            if (rootTelemetryConfiguration != null) {
                n6 &= rootTelemetryConfiguration.getMethodTimingTelemetryEnabled();
                n4 = rootTelemetryConfiguration.getBatchPeriodMillis();
                n3 = rootTelemetryConfiguration.getMaxMethodInvocationsInBatch();
                n2 = rootTelemetryConfiguration.getVersion();
                if (baseGmsClient.hasConnectionInfo() && !baseGmsClient.isConnecting()) {
                    if ((object2 = zacd.zab(object2, baseGmsClient, this.zab)) == null) {
                        return;
                    }
                    n6 = ((ConnectionTelemetryConfiguration)object2).getMethodTimingTelemetryEnabled() && this.zad > 0L ? n5 : 0;
                    n5 = ((ConnectionTelemetryConfiguration)object2).getMaxMethodInvocationsLogged();
                } else {
                    n5 = n3;
                }
                n3 = n5;
                n = n6;
            } else {
                n2 = 0;
                n4 = 5000;
                n3 = 100;
                n = n6;
            }
            object2 = this.zaa;
            if (((Task)object).isSuccessful()) {
                n6 = 0;
                n5 = 0;
            } else if (((Task)object).isCanceled()) {
                n6 = 100;
                n5 = -1;
            } else if ((object = ((Task)object).getException()) instanceof ApiException) {
                object = ((ApiException)object).getStatus();
                int n8 = ((Status)object).getStatusCode();
                n6 = (object = ((Status)object).getConnectionResult()) == null ? -1 : ((ConnectionResult)object).getErrorCode();
                n5 = n6;
                n6 = n8;
            } else {
                n6 = 101;
                n5 = -1;
            }
            if (n != 0) {
                l = this.zad;
                l2 = System.currentTimeMillis();
                n = (int)(SystemClock.elapsedRealtime() - this.zae);
            } else {
                l = 0L;
                l2 = 0L;
                n = -1;
            }
            ((GoogleApiManager)object2).zay(new MethodInvocation(this.zab, n6, n5, l, l2, null, null, n7, n), n2, n4, n3);
            return;
        }
    }
}

