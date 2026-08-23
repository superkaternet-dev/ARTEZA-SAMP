/*
 * Decompiled with CFR 0.152.
 */
package androidx.lifecycle;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;

@Deprecated
public interface LifecycleRegistryOwner
extends LifecycleOwner {
    @Override
    public LifecycleRegistry getLifecycle();
}

