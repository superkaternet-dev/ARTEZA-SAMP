/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.AlertDialog$Builder
 *  android.app.Dialog
 *  android.app.FragmentManager
 *  android.app.Notification
 *  android.app.NotificationChannel
 *  android.app.NotificationManager
 *  android.app.PendingIntent
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.DialogInterface$OnCancelListener
 *  android.content.DialogInterface$OnClickListener
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.res.Resources
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.View
 *  android.widget.ProgressBar
 */
package com.google.android.gms.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.base.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.ErrorDialogFragment;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.GooglePlayServicesUtilLight;
import com.google.android.gms.common.SupportErrorDialogFragment;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiActivity;
import com.google.android.gms.common.api.HasApiKey;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.api.internal.GoogleApiManager;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.api.internal.zabw;
import com.google.android.gms.common.api.internal.zabx;
import com.google.android.gms.common.api.internal.zacc;
import com.google.android.gms.common.api.internal.zap;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zag;
import com.google.android.gms.common.util.DeviceProperties;
import com.google.android.gms.common.util.PlatformVersion;
import com.google.android.gms.common.wrappers.InstantApps;
import com.google.android.gms.common.zaa;
import com.google.android.gms.common.zab;
import com.google.android.gms.common.zac;
import com.google.android.gms.internal.base.zal;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class GoogleApiAvailability
extends GoogleApiAvailabilityLight {
    public static final String GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms";
    public static final int GOOGLE_PLAY_SERVICES_VERSION_CODE;
    private static final Object zaa;
    private static final GoogleApiAvailability zab;
    private String zac;

    static {
        zaa = new Object();
        zab = new GoogleApiAvailability();
        GOOGLE_PLAY_SERVICES_VERSION_CODE = GoogleApiAvailabilityLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
    }

    public static GoogleApiAvailability getInstance() {
        return zab;
    }

    public static final Task<Map<ApiKey<?>, String>> zai(HasApiKey<?> hasApiKey, HasApiKey<?> ... hasApiKeyArray) {
        Preconditions.checkNotNull(hasApiKey, "Requested API must not be null.");
        int n = hasApiKeyArray.length;
        for (int i = 0; i < n; ++i) {
            Preconditions.checkNotNull(hasApiKeyArray[i], "Requested API must not be null.");
        }
        ArrayList arrayList = new ArrayList(hasApiKeyArray.length + 1);
        arrayList.add(hasApiKey);
        arrayList.addAll(Arrays.asList(hasApiKeyArray));
        return GoogleApiManager.zal().zao(arrayList);
    }

    public Task<Void> checkApiAvailability(GoogleApi<?> googleApi, GoogleApi<?> ... googleApiArray) {
        return GoogleApiAvailability.zai(googleApi, googleApiArray).onSuccessTask(com.google.android.gms.common.zab.zaa);
    }

    public Task<Void> checkApiAvailability(HasApiKey<?> hasApiKey, HasApiKey<?> ... hasApiKeyArray) {
        return GoogleApiAvailability.zai(hasApiKey, hasApiKeyArray).onSuccessTask(com.google.android.gms.common.zaa.zaa);
    }

    @Override
    public int getClientVersion(Context context) {
        return super.getClientVersion(context);
    }

    public Dialog getErrorDialog(Activity activity, int n, int n2) {
        return this.getErrorDialog(activity, n, n2, null);
    }

    public Dialog getErrorDialog(Activity activity, int n, int n2, DialogInterface.OnCancelListener onCancelListener) {
        return this.zaa((Context)activity, n, zag.zab(activity, this.getErrorResolutionIntent((Context)activity, n, "d"), n2), onCancelListener);
    }

    public Dialog getErrorDialog(Fragment fragment, int n, int n2) {
        return this.getErrorDialog(fragment, n, n2, null);
    }

    public Dialog getErrorDialog(Fragment fragment, int n, int n2, DialogInterface.OnCancelListener onCancelListener) {
        Intent intent = this.getErrorResolutionIntent(fragment.requireContext(), n, "d");
        return this.zaa(fragment.requireContext(), n, zag.zac(fragment, intent, n2), onCancelListener);
    }

    @Override
    public Intent getErrorResolutionIntent(Context context, int n, String string2) {
        return super.getErrorResolutionIntent(context, n, string2);
    }

    @Override
    public PendingIntent getErrorResolutionPendingIntent(Context context, int n, int n2) {
        return super.getErrorResolutionPendingIntent(context, n, n2);
    }

    public PendingIntent getErrorResolutionPendingIntent(Context context, ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            return connectionResult.getResolution();
        }
        return this.getErrorResolutionPendingIntent(context, connectionResult.getErrorCode(), 0);
    }

    @Override
    public final String getErrorString(int n) {
        return super.getErrorString(n);
    }

    @Override
    public int isGooglePlayServicesAvailable(Context context) {
        return super.isGooglePlayServicesAvailable(context);
    }

    @Override
    public int isGooglePlayServicesAvailable(Context context, int n) {
        return super.isGooglePlayServicesAvailable(context, n);
    }

    @Override
    public final boolean isUserResolvableError(int n) {
        return super.isUserResolvableError(n);
    }

    public Task<Void> makeGooglePlayServicesAvailable(Activity object) {
        int n = GOOGLE_PLAY_SERVICES_VERSION_CODE;
        Preconditions.checkMainThread("makeGooglePlayServicesAvailable must be called from the main thread");
        n = this.isGooglePlayServicesAvailable((Context)object, n);
        if (n == 0) {
            object = Tasks.forResult(null);
        } else {
            object = zacc.zaa((Activity)object);
            ((zap)object).zah(new ConnectionResult(n, null), 0);
            object = ((zacc)object).zad();
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setDefaultNotificationChannelId(Context object, String string2) {
        if (PlatformVersion.isAtLeastO()) {
            Preconditions.checkNotNull(((NotificationManager)Preconditions.checkNotNull(object.getSystemService("notification"))).getNotificationChannel(string2));
        }
        object = zaa;
        synchronized (object) {
            this.zac = string2;
            return;
        }
    }

    public boolean showErrorDialogFragment(Activity activity, int n, int n2) {
        return this.showErrorDialogFragment(activity, n, n2, null);
    }

    public boolean showErrorDialogFragment(Activity activity, int n, int n2, DialogInterface.OnCancelListener onCancelListener) {
        Dialog dialog = this.getErrorDialog(activity, n, n2, onCancelListener);
        if (dialog == null) {
            return false;
        }
        this.zad(activity, dialog, "GooglePlayServicesErrorDialog", onCancelListener);
        return true;
    }

    public void showErrorNotification(Context context, int n) {
        this.zae(context, n, null, this.getErrorResolutionPendingIntent(context, n, 0, "n"));
    }

    public void showErrorNotification(Context context, ConnectionResult connectionResult) {
        PendingIntent pendingIntent = this.getErrorResolutionPendingIntent(context, connectionResult);
        this.zae(context, connectionResult.getErrorCode(), null, pendingIntent);
    }

    final Dialog zaa(Context object, int n, zag zag2, DialogInterface.OnCancelListener object2) {
        AlertDialog.Builder builder = null;
        if (n == 0) {
            return null;
        }
        TypedValue typedValue = new TypedValue();
        object.getTheme().resolveAttribute(16843529, typedValue, true);
        if ("Theme.Dialog.Alert".equals(object.getResources().getResourceEntryName(typedValue.resourceId))) {
            builder = new AlertDialog.Builder(object, 5);
        }
        typedValue = builder;
        if (builder == null) {
            typedValue = new AlertDialog.Builder(object);
        }
        typedValue.setMessage((CharSequence)com.google.android.gms.common.internal.zac.zad(object, n));
        if (object2 != null) {
            typedValue.setOnCancelListener(object2);
        }
        if ((object2 = com.google.android.gms.common.internal.zac.zac(object, n)) != null) {
            typedValue.setPositiveButton((CharSequence)object2, (DialogInterface.OnClickListener)zag2);
        }
        if ((object = com.google.android.gms.common.internal.zac.zag(object, n)) != null) {
            typedValue.setTitle((CharSequence)object);
        }
        Log.w((String)"GoogleApiAvailability", (String)String.format("Creating dialog for Google Play services availability issue. ConnectionResult=%s", n), (Throwable)new IllegalArgumentException());
        return typedValue.create();
    }

    public final Dialog zab(Activity activity, DialogInterface.OnCancelListener onCancelListener) {
        ProgressBar progressBar = new ProgressBar((Context)activity, null, 16842874);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(0);
        AlertDialog.Builder builder = new AlertDialog.Builder((Context)activity);
        builder.setView((View)progressBar);
        builder.setMessage((CharSequence)com.google.android.gms.common.internal.zac.zad((Context)activity, 18));
        builder.setPositiveButton((CharSequence)"", null);
        builder = builder.create();
        this.zad(activity, (Dialog)builder, "GooglePlayServicesUpdatingDialog", onCancelListener);
        return builder;
    }

    public final zabx zac(Context context, zabw zabw2) {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        zabx zabx2 = new zabx(zabw2);
        context.registerReceiver((BroadcastReceiver)zabx2, intentFilter);
        zabx2.zaa(context);
        if (!this.isUninstalledAppPossiblyUpdating(context, GOOGLE_PLAY_SERVICES_PACKAGE)) {
            zabw2.zaa();
            zabx2.zab();
            return null;
        }
        return zabx2;
    }

    final void zad(Activity object, Dialog dialog, String string2, DialogInterface.OnCancelListener onCancelListener) {
        try {
            boolean bl = object instanceof FragmentActivity;
            if (bl) {
                object = ((FragmentActivity)object).getSupportFragmentManager();
                SupportErrorDialogFragment.newInstance(dialog, onCancelListener).show((androidx.fragment.app.FragmentManager)object, string2);
                return;
            }
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
            // empty catch block
        }
        object = object.getFragmentManager();
        ErrorDialogFragment.newInstance(dialog, onCancelListener).show((FragmentManager)object, string2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    final void zae(Context object, int n, String object2, PendingIntent object3) {
        Log.w((String)"GoogleApiAvailability", (String)String.format("GMS core API Availability. ConnectionResult=%s, tag=%s", n, null), (Throwable)new IllegalArgumentException());
        if (n == 18) {
            this.zaf((Context)object);
            return;
        }
        if (object3 == null) {
            if (n == 6) {
                Log.w((String)"GoogleApiAvailability", (String)"Missing resolution for ConnectionResult.RESOLUTION_REQUIRED. Call GoogleApiAvailability#showErrorNotification(Context, ConnectionResult) instead.");
            }
            return;
        }
        Object object4 = com.google.android.gms.common.internal.zac.zaf((Context)object, n);
        object2 = com.google.android.gms.common.internal.zac.zae((Context)object, n);
        Resources resources = object.getResources();
        NotificationManager notificationManager = (NotificationManager)Preconditions.checkNotNull(object.getSystemService("notification"));
        object4 = new NotificationCompat.Builder((Context)object).setLocalOnly(true).setAutoCancel(true).setContentTitle((CharSequence)object4).setStyle(new NotificationCompat.BigTextStyle().bigText((CharSequence)object2));
        if (DeviceProperties.isWearable((Context)object)) {
            Preconditions.checkState(PlatformVersion.isAtLeastKitKatWatch());
            ((NotificationCompat.Builder)object4).setSmallIcon(object.getApplicationInfo().icon).setPriority(2);
            if (DeviceProperties.isWearableWithoutPlayStore((Context)object)) {
                ((NotificationCompat.Builder)object4).addAction(R.drawable.common_full_open_on_phone, resources.getString(R.string.common_open_on_phone), (PendingIntent)object3);
            } else {
                ((NotificationCompat.Builder)object4).setContentIntent((PendingIntent)object3);
            }
        } else {
            ((NotificationCompat.Builder)object4).setSmallIcon(17301642).setTicker(resources.getString(R.string.common_google_play_services_notification_ticker)).setWhen(System.currentTimeMillis()).setContentIntent((PendingIntent)object3).setContentText((CharSequence)object2);
        }
        if (PlatformVersion.isAtLeastO()) {
            Preconditions.checkState(PlatformVersion.isAtLeastO());
            object2 = zaa;
            synchronized (object2) {
                object3 = this.zac;
            }
            object2 = object3;
            if (object3 == null) {
                object3 = "com.google.android.gms.availability";
                resources = notificationManager.getNotificationChannel("com.google.android.gms.availability");
                object = com.google.android.gms.common.internal.zac.zab((Context)object);
                if (resources == null) {
                    notificationManager.createNotificationChannel(new NotificationChannel("com.google.android.gms.availability", (CharSequence)object, 4));
                    object2 = object3;
                } else {
                    object2 = object3;
                    if (!((String)object).contentEquals(resources.getName())) {
                        resources.setName((CharSequence)object);
                        notificationManager.createNotificationChannel((NotificationChannel)resources);
                        object2 = object3;
                    }
                }
            }
            ((NotificationCompat.Builder)object4).setChannelId((String)object2);
        }
        object = ((NotificationCompat.Builder)object4).build();
        switch (n) {
            default: {
                n = 39789;
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                GooglePlayServicesUtilLight.sCanceledAvailabilityNotification.set(false);
                n = 10436;
            }
        }
        notificationManager.notify(n, (Notification)object);
    }

    final void zaf(Context context) {
        new zac(this, context).sendEmptyMessageDelayed(1, 120000L);
    }

    public final boolean zag(Activity activity, LifecycleFragment lifecycleFragment, int n, int n2, DialogInterface.OnCancelListener onCancelListener) {
        if ((lifecycleFragment = this.zaa((Context)activity, n, zag.zad(lifecycleFragment, this.getErrorResolutionIntent((Context)activity, n, "d"), 2), onCancelListener)) == null) {
            return false;
        }
        this.zad(activity, (Dialog)lifecycleFragment, "GooglePlayServicesErrorDialog", onCancelListener);
        return true;
    }

    public final boolean zah(Context context, ConnectionResult connectionResult, int n) {
        if (InstantApps.isInstantApp(context)) {
            return false;
        }
        PendingIntent pendingIntent = this.getErrorResolutionPendingIntent(context, connectionResult);
        if (pendingIntent != null) {
            this.zae(context, connectionResult.getErrorCode(), null, zal.zaa(context, 0, GoogleApiActivity.zaa(context, pendingIntent, n, true), zal.zaa | 0x8000000));
            return true;
        }
        return false;
    }
}

