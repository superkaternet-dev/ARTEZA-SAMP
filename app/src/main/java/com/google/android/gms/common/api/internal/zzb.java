/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Fragment
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Looper
 */
package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import com.google.android.gms.common.api.internal.LifecycleFragment;
import com.google.android.gms.common.api.internal.zza;
import com.google.android.gms.internal.common.zzi;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public final class zzb
extends Fragment
implements LifecycleFragment {
    private static final WeakHashMap<Activity, WeakReference<zzb>> zza = new WeakHashMap();
    private final Map<String, LifecycleCallback> zzb = Collections.synchronizedMap(new ArrayMap());
    private int zzc = 0;
    private Bundle zzd;

    static /* bridge */ /* synthetic */ int zza(zzb zzb2) {
        return zzb2.zzc;
    }

    static /* bridge */ /* synthetic */ Bundle zzb(zzb zzb2) {
        return zzb2.zzd;
    }

    public static zzb zzc(Activity activity) {
        WeakReference<zzb> weakReference;
        WeakHashMap<Activity, WeakReference<zzb>> weakHashMap;
        block7: {
            block6: {
                zzb zzb2;
                weakHashMap = zza;
                weakReference = weakHashMap.get(activity);
                if (weakReference != null && (weakReference = (zzb)weakReference.get()) != null) {
                    return weakReference;
                }
                try {
                    zzb2 = (zzb)activity.getFragmentManager().findFragmentByTag("LifecycleFragmentImpl");
                    if (zzb2 == null) break block6;
                    weakReference = zzb2;
                }
                catch (ClassCastException classCastException) {
                    throw new IllegalStateException("Fragment with tag LifecycleFragmentImpl is not a LifecycleFragmentImpl", classCastException);
                }
                if (!zzb2.isRemoving()) break block7;
            }
            weakReference = new zzb();
            activity.getFragmentManager().beginTransaction().add((Fragment)weakReference, "LifecycleFragmentImpl").commitAllowingStateLoss();
        }
        weakHashMap.put(activity, new WeakReference<Object>(weakReference));
        return weakReference;
    }

    @Override
    public final void addCallback(String string2, LifecycleCallback object) {
        if (!this.zzb.containsKey(string2)) {
            this.zzb.put(string2, (LifecycleCallback)object);
            if (this.zzc > 0) {
                new zzi(Looper.getMainLooper()).post(new zza(this, (LifecycleCallback)object, string2));
                return;
            }
            return;
        }
        object = new StringBuilder(String.valueOf(string2).length() + 59);
        ((StringBuilder)object).append("LifecycleCallback with tag ");
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(" already added to this fragment.");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public final void dump(String string2, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] stringArray) {
        super.dump(string2, fileDescriptor, printWriter, stringArray);
        Iterator<LifecycleCallback> iterator2 = this.zzb.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().dump(string2, fileDescriptor, printWriter, stringArray);
        }
    }

    @Override
    public final <T extends LifecycleCallback> T getCallbackOrNull(String string2, Class<T> clazz) {
        return (T)((LifecycleCallback)clazz.cast(this.zzb.get(string2)));
    }

    @Override
    public final Activity getLifecycleActivity() {
        return this.getActivity();
    }

    @Override
    public final boolean isCreated() {
        return this.zzc > 0;
    }

    @Override
    public final boolean isStarted() {
        return this.zzc >= 2;
    }

    public final void onActivityResult(int n, int n2, Intent intent) {
        super.onActivityResult(n, n2, intent);
        Iterator<LifecycleCallback> iterator2 = this.zzb.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onActivityResult(n, n2, intent);
        }
    }

    /*
     * WARNING - void declaration
     */
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.zzc = 1;
        this.zzd = bundle;
        for (Map.Entry<String, LifecycleCallback> entry : this.zzb.entrySet()) {
            void object;
            LifecycleCallback lifecycleCallback = entry.getValue();
            if (bundle != null) {
                Bundle bundle2 = bundle.getBundle(entry.getKey());
            } else {
                Object var2_6 = null;
            }
            lifecycleCallback.onCreate((Bundle)object);
        }
    }

    public final void onDestroy() {
        super.onDestroy();
        this.zzc = 5;
        Iterator<LifecycleCallback> iterator2 = this.zzb.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDestroy();
        }
    }

    public final void onResume() {
        super.onResume();
        this.zzc = 3;
        Iterator<LifecycleCallback> iterator2 = this.zzb.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onResume();
        }
    }

    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle == null) {
            return;
        }
        for (Map.Entry<String, LifecycleCallback> entry : this.zzb.entrySet()) {
            Bundle bundle2 = new Bundle();
            entry.getValue().onSaveInstanceState(bundle2);
            bundle.putBundle(entry.getKey(), bundle2);
        }
    }

    public final void onStart() {
        super.onStart();
        this.zzc = 2;
        Iterator<LifecycleCallback> iterator2 = this.zzb.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onStart();
        }
    }

    public final void onStop() {
        super.onStop();
        this.zzc = 4;
        Iterator<LifecycleCallback> iterator2 = this.zzb.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onStop();
        }
    }
}

