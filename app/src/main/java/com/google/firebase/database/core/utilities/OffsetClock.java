/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.core.utilities.Clock;

public class OffsetClock
implements Clock {
    private final Clock baseClock;
    private long offset = 0L;

    public OffsetClock(Clock clock, long l) {
        this.baseClock = clock;
        this.offset = l;
    }

    @Override
    public long millis() {
        return this.baseClock.millis() + this.offset;
    }

    public void setOffset(long l) {
        this.offset = l;
    }
}

