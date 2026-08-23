/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

public interface EventTarget {
    public void postEvent(Runnable var1);

    public void restart();

    public void shutdown();
}

