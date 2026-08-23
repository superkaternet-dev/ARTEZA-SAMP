/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ActivityNotFoundException
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.os.Build
 *  android.util.Log
 */
package com.google.android.gms.common.internal;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import androidx.fragment.app.Fragment;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.internal.zad;
import com.google.android.gms.common.internal.zae;
import com.google.android.gms.common.internal.zaf;

public abstract class zag
implements DialogInterface.OnClickListener {
    public static zag zab(Activity activity, Intent intent, int n) {
        return new zad(intent, activity, n);
    }

    public static zag zac(Fragment fragment, Intent intent, int n) {
        return new zae(intent, fragment, n);
    }

    public static zag zad(LifecycleFragment lifecycleFragment, Intent intent, int n) {
        return new zaf(intent, lifecycleFragment, 2);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void onClick(DialogInterface dialogInterface, int n) {
        Throwable throwable2;
        block5: {
            this.zaa();
            {
                catch (Throwable throwable2) {
                    break block5;
                }
                catch (ActivityNotFoundException activityNotFoundException) {}
                String string2 = "Failed to start resolution intent.";
                {
                    if (Build.FINGERPRINT.contains("generic")) {
                        string2 = "Failed to start resolution intent. This may occur when resolving Google Play services connection issues on emulators with Google APIs but not Google Play Store.";
                    }
                    Log.e((String)"DialogRedirect", (String)string2, (Throwable)activityNotFoundException);
                }
            }
            dialogInterface.dismiss();
            return;
        }
        dialogInterface.dismiss();
        throw throwable2;
    }

    protected abstract void zaa();
}

