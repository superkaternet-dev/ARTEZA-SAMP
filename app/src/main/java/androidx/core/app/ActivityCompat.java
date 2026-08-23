/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.SharedElementCallback
 *  android.app.SharedElementCallback$OnSharedElementsReadyListener
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentSender
 *  android.content.IntentSender$SendIntentException
 *  android.content.pm.PackageManager
 *  android.graphics.Matrix
 *  android.graphics.RectF
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Parcelable
 *  android.view.DragEvent
 *  android.view.View
 */
package androidx.core.app;

import android.app.Activity;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.view.DragEvent;
import android.view.View;
import androidx.core.app.ActivityRecreator;
import androidx.core.app.SharedElementCallback;
import androidx.core.content.ContextCompat;
import androidx.core.view.DragAndDropPermissionsCompat;
import java.util.List;
import java.util.Map;

public class ActivityCompat
extends ContextCompat {
    private static PermissionCompatDelegate sDelegate;

    protected ActivityCompat() {
    }

    public static void finishAffinity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.finishAffinity();
        } else {
            activity.finish();
        }
    }

    public static void finishAfterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.finishAfterTransition();
        } else {
            activity.finish();
        }
    }

    public static PermissionCompatDelegate getPermissionCompatDelegate() {
        return sDelegate;
    }

    public static Uri getReferrer(Activity object) {
        if (Build.VERSION.SDK_INT >= 22) {
            return object.getReferrer();
        }
        Uri uri = (Uri)(object = object.getIntent()).getParcelableExtra("android.intent.extra.REFERRER");
        if (uri != null) {
            return uri;
        }
        if ((object = object.getStringExtra("android.intent.extra.REFERRER_NAME")) != null) {
            return Uri.parse((String)object);
        }
        return null;
    }

    @Deprecated
    public static boolean invalidateOptionsMenu(Activity activity) {
        activity.invalidateOptionsMenu();
        return true;
    }

    public static void postponeEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.postponeEnterTransition();
        }
    }

    public static void recreate(Activity activity) {
        if (Build.VERSION.SDK_INT >= 28) {
            activity.recreate();
        } else if (!ActivityRecreator.recreate(activity)) {
            activity.recreate();
        }
    }

    public static DragAndDropPermissionsCompat requestDragAndDropPermissions(Activity activity, DragEvent dragEvent) {
        return DragAndDropPermissionsCompat.request(activity, dragEvent);
    }

    public static void requestPermissions(Activity activity, String[] stringArray, int n) {
        PermissionCompatDelegate permissionCompatDelegate = sDelegate;
        if (permissionCompatDelegate != null && permissionCompatDelegate.requestPermissions(activity, stringArray, n)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity instanceof RequestPermissionsRequestCodeValidator) {
                ((RequestPermissionsRequestCodeValidator)activity).validateRequestPermissionsRequestCode(n);
            }
            activity.requestPermissions(stringArray, n);
        } else if (activity instanceof OnRequestPermissionsResultCallback) {
            new Handler(Looper.getMainLooper()).post(new Runnable(stringArray, activity, n){
                final Activity val$activity;
                final String[] val$permissions;
                final int val$requestCode;
                {
                    this.val$permissions = stringArray;
                    this.val$activity = activity;
                    this.val$requestCode = n;
                }

                @Override
                public void run() {
                    int[] nArray = new int[this.val$permissions.length];
                    PackageManager packageManager = this.val$activity.getPackageManager();
                    String string2 = this.val$activity.getPackageName();
                    int n = this.val$permissions.length;
                    for (int i = 0; i < n; ++i) {
                        nArray[i] = packageManager.checkPermission(this.val$permissions[i], string2);
                    }
                    ((OnRequestPermissionsResultCallback)this.val$activity).onRequestPermissionsResult(this.val$requestCode, this.val$permissions, nArray);
                }
            });
        }
    }

    public static <T extends View> T requireViewById(Activity activity, int n) {
        if (Build.VERSION.SDK_INT >= 28) {
            return (T)activity.requireViewById(n);
        }
        if ((activity = activity.findViewById(n)) != null) {
            return (T)activity;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Activity");
    }

    public static void setEnterSharedElementCallback(Activity activity, SharedElementCallback object) {
        if (Build.VERSION.SDK_INT >= 21) {
            object = object != null ? new SharedElementCallback21Impl((SharedElementCallback)object) : null;
            activity.setEnterSharedElementCallback((android.app.SharedElementCallback)object);
        }
    }

    public static void setExitSharedElementCallback(Activity activity, SharedElementCallback object) {
        if (Build.VERSION.SDK_INT >= 21) {
            object = object != null ? new SharedElementCallback21Impl((SharedElementCallback)object) : null;
            activity.setExitSharedElementCallback((android.app.SharedElementCallback)object);
        }
    }

    public static void setPermissionCompatDelegate(PermissionCompatDelegate permissionCompatDelegate) {
        sDelegate = permissionCompatDelegate;
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity, String string2) {
        if (Build.VERSION.SDK_INT >= 23) {
            return activity.shouldShowRequestPermissionRationale(string2);
        }
        return false;
    }

    public static void startActivityForResult(Activity activity, Intent intent, int n, Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.startActivityForResult(intent, n, bundle);
        } else {
            activity.startActivityForResult(intent, n);
        }
    }

    public static void startIntentSenderForResult(Activity activity, IntentSender intentSender, int n, Intent intent, int n2, int n3, int n4, Bundle bundle) throws IntentSender.SendIntentException {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4, bundle);
        } else {
            activity.startIntentSenderForResult(intentSender, n, intent, n2, n3, n4);
        }
    }

    public static void startPostponedEnterTransition(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {
            activity.startPostponedEnterTransition();
        }
    }

    public static interface OnRequestPermissionsResultCallback {
        public void onRequestPermissionsResult(int var1, String[] var2, int[] var3);
    }

    public static interface PermissionCompatDelegate {
        public boolean onActivityResult(Activity var1, int var2, int var3, Intent var4);

        public boolean requestPermissions(Activity var1, String[] var2, int var3);
    }

    public static interface RequestPermissionsRequestCodeValidator {
        public void validateRequestPermissionsRequestCode(int var1);
    }

    private static class SharedElementCallback21Impl
    extends android.app.SharedElementCallback {
        private final SharedElementCallback mCallback;

        SharedElementCallback21Impl(SharedElementCallback sharedElementCallback) {
            this.mCallback = sharedElementCallback;
        }

        public Parcelable onCaptureSharedElementSnapshot(View view, Matrix matrix, RectF rectF) {
            return this.mCallback.onCaptureSharedElementSnapshot(view, matrix, rectF);
        }

        public View onCreateSnapshotView(Context context, Parcelable parcelable) {
            return this.mCallback.onCreateSnapshotView(context, parcelable);
        }

        public void onMapSharedElements(List<String> list, Map<String, View> map) {
            this.mCallback.onMapSharedElements(list, map);
        }

        public void onRejectSharedElements(List<View> list) {
            this.mCallback.onRejectSharedElements(list);
        }

        public void onSharedElementEnd(List<String> list, List<View> list2, List<View> list3) {
            this.mCallback.onSharedElementEnd(list, list2, list3);
        }

        public void onSharedElementStart(List<String> list, List<View> list2, List<View> list3) {
            this.mCallback.onSharedElementStart(list, list2, list3);
        }

        public void onSharedElementsArrived(List<String> list, List<View> list2, SharedElementCallback.OnSharedElementsReadyListener onSharedElementsReadyListener) {
            this.mCallback.onSharedElementsArrived(list, list2, new SharedElementCallback.OnSharedElementsReadyListener(this, onSharedElementsReadyListener){
                final SharedElementCallback21Impl this$0;
                final SharedElementCallback.OnSharedElementsReadyListener val$listener;
                {
                    this.this$0 = sharedElementCallback21Impl;
                    this.val$listener = onSharedElementsReadyListener;
                }

                @Override
                public void onSharedElementsReady() {
                    this.val$listener.onSharedElementsReady();
                }
            });
        }
    }
}

