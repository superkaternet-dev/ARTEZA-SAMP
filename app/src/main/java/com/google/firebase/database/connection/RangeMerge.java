/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import java.util.List;

public class RangeMerge {
    private final List<String> optExclusiveStart;
    private final List<String> optInclusiveEnd;
    private final Object snap;

    public RangeMerge(List<String> list, List<String> list2, Object object) {
        this.optExclusiveStart = list;
        this.optInclusiveEnd = list2;
        this.snap = object;
    }

    public List<String> getOptExclusiveStart() {
        return this.optExclusiveStart;
    }

    public List<String> getOptInclusiveEnd() {
        return this.optInclusiveEnd;
    }

    public Object getSnap() {
        return this.snap;
    }
}

