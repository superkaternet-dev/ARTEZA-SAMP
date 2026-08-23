/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.firebase.database.connection.CompoundHash;

public interface ListenHashProvider {
    public CompoundHash getCompoundHash();

    public String getSimpleHash();

    public boolean shouldIncludeCompoundHash();
}

