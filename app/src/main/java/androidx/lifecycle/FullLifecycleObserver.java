/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

interface FullLifecycleObserver
extends LifecycleObserver {
    public void onCreate(LifecycleOwner var1);

    public void onDestroy(LifecycleOwner var1);

    public void onPause(LifecycleOwner var1);

    public void onResume(LifecycleOwner var1);

    public void onStart(LifecycleOwner var1);

    public void onStop(LifecycleOwner var1);
}

