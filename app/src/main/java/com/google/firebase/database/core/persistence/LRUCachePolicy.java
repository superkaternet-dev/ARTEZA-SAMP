/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.persistence;

import com.google.firebase.database.core.persistence.CachePolicy;

public class LRUCachePolicy
implements CachePolicy {
    private static final long MAX_NUMBER_OF_PRUNABLE_QUERIES_TO_KEEP = 1000L;
    private static final float PERCENT_OF_QUERIES_TO_PRUNE_AT_ONCE = 0.2f;
    private static final long SERVER_UPDATES_BETWEEN_CACHE_SIZE_CHECKS = 1000L;
    public final long maxSizeBytes;

    public LRUCachePolicy(long l) {
        this.maxSizeBytes = l;
    }

    @Override
    public long getMaxNumberOfQueriesToKeep() {
        return 1000L;
    }

    @Override
    public float getPercentOfQueriesToPruneAtOnce() {
        return 0.2f;
    }

    @Override
    public boolean shouldCheckCacheSize(long l) {
        boolean bl = l > 1000L;
        return bl;
    }

    @Override
    public boolean shouldPrune(long l, long l2) {
        boolean bl = l > this.maxSizeBytes || l2 > 1000L;
        return bl;
    }
}

