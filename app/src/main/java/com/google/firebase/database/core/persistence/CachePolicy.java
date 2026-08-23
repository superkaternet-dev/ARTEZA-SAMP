/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.persistence;

public interface CachePolicy {
    public static final CachePolicy NONE = new CachePolicy(){

        @Override
        public long getMaxNumberOfQueriesToKeep() {
            return Long.MAX_VALUE;
        }

        @Override
        public float getPercentOfQueriesToPruneAtOnce() {
            return 0.0f;
        }

        @Override
        public boolean shouldCheckCacheSize(long l) {
            return false;
        }

        @Override
        public boolean shouldPrune(long l, long l2) {
            return false;
        }
    };

    public long getMaxNumberOfQueriesToKeep();

    public float getPercentOfQueriesToPruneAtOnce();

    public boolean shouldCheckCacheSize(long var1);

    public boolean shouldPrune(long var1, long var3);
}

