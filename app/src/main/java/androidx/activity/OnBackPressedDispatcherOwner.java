/*
 * Decompiled with CFR 0.152.
 */
package androidx.activity;

import androidx.activity.OnBackPressedDispatcher;
import androidx.lifecycle.LifecycleOwner;

public interface OnBackPressedDispatcherOwner
extends LifecycleOwner {
    public OnBackPressedDispatcher getOnBackPressedDispatcher();
}

