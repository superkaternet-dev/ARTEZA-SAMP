/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

public interface LifecycleEventObserver
extends LifecycleObserver {
    public void onStateChanged(LifecycleOwner var1, Lifecycle.Event var2);
}

