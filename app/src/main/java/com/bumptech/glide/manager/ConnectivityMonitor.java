/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.manager;

import com.bumptech.glide.manager.LifecycleListener;

public interface ConnectivityMonitor
extends LifecycleListener {

    public static interface ConnectivityListener {
        public void onConnectivityChanged(boolean var1);
    }
}

