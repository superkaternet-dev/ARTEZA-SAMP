/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public interface ChildEventListener {
    public void onCancelled(DatabaseError var1);

    public void onChildAdded(DataSnapshot var1, String var2);

    public void onChildChanged(DataSnapshot var1, String var2);

    public void onChildMoved(DataSnapshot var1, String var2);

    public void onChildRemoved(DataSnapshot var1);
}

