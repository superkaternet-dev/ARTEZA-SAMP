/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.heartbeatinfo;

import com.google.firebase.heartbeatinfo.AutoValue_SdkHeartBeatResult;

public abstract class SdkHeartBeatResult
implements Comparable<SdkHeartBeatResult> {
    public static SdkHeartBeatResult create(String string2, long l) {
        return new AutoValue_SdkHeartBeatResult(string2, l);
    }

    @Override
    public int compareTo(SdkHeartBeatResult sdkHeartBeatResult) {
        int n = this.getMillis() < sdkHeartBeatResult.getMillis() ? -1 : 1;
        return n;
    }

    public abstract long getMillis();

    public abstract String getSdkName();
}

