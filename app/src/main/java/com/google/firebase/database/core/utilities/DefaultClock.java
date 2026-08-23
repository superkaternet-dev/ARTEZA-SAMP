/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.core.utilities.Clock;

public class DefaultClock
implements Clock {
    @Override
    public long millis() {
        return System.currentTimeMillis();
    }
}

