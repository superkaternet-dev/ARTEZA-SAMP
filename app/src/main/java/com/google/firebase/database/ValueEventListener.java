/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface ValueEventListener {
    public void onCancelled(DatabaseError var1);

    public void onDataChange(DataSnapshot var1);
}

