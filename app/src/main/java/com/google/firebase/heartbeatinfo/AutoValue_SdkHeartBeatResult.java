/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.heartbeatinfo;

import com.google.firebase.heartbeatinfo.SdkHeartBeatResult;

final class AutoValue_SdkHeartBeatResult
extends SdkHeartBeatResult {
    private final long millis;
    private final String sdkName;

    AutoValue_SdkHeartBeatResult(String string2, long l) {
        if (string2 != null) {
            this.sdkName = string2;
            this.millis = l;
            return;
        }
        throw new NullPointerException("Null sdkName");
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (object == this) {
            return true;
        }
        if (object instanceof SdkHeartBeatResult) {
            if (!this.sdkName.equals(((SdkHeartBeatResult)(object = (SdkHeartBeatResult)object)).getSdkName()) || this.millis != ((SdkHeartBeatResult)object).getMillis()) {
                bl = false;
            }
            return bl;
        }
        return false;
    }

    @Override
    public long getMillis() {
        return this.millis;
    }

    @Override
    public String getSdkName() {
        return this.sdkName;
    }

    public int hashCode() {
        int n = this.sdkName.hashCode();
        long l = this.millis;
        return (1 * 1000003 ^ n) * 1000003 ^ (int)(l ^ l >>> 32);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SdkHeartBeatResult{sdkName=");
        stringBuilder.append(this.sdkName);
        stringBuilder.append(", millis=");
        stringBuilder.append(this.millis);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

