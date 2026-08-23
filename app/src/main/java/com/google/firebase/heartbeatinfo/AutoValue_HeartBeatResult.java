/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.heartbeatinfo;

import com.google.firebase.heartbeatinfo.HeartBeatResult;
import java.util.List;

final class AutoValue_HeartBeatResult
extends HeartBeatResult {
    private final List<String> usedDates;
    private final String userAgent;

    AutoValue_HeartBeatResult(String string2, List<String> list) {
        if (string2 != null) {
            this.userAgent = string2;
            if (list != null) {
                this.usedDates = list;
                return;
            }
            throw new NullPointerException("Null usedDates");
        }
        throw new NullPointerException("Null userAgent");
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (object == this) {
            return true;
        }
        if (object instanceof HeartBeatResult) {
            if (!this.userAgent.equals(((HeartBeatResult)(object = (HeartBeatResult)object)).getUserAgent()) || !this.usedDates.equals(((HeartBeatResult)object).getUsedDates())) {
                bl = false;
            }
            return bl;
        }
        return false;
    }

    @Override
    public List<String> getUsedDates() {
        return this.usedDates;
    }

    @Override
    public String getUserAgent() {
        return this.userAgent;
    }

    public int hashCode() {
        return (1 * 1000003 ^ this.userAgent.hashCode()) * 1000003 ^ this.usedDates.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HeartBeatResult{userAgent=");
        stringBuilder.append(this.userAgent);
        stringBuilder.append(", usedDates=");
        stringBuilder.append(this.usedDates);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}

