/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import okhttp3.internal.Util;

public final class Challenge {
    private final String realm;
    private final String scheme;

    public Challenge(String string2, String string3) {
        this.scheme = string2;
        this.realm = string3;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof Challenge && Util.equal(this.scheme, ((Challenge)object).scheme) && Util.equal(this.realm, ((Challenge)object).realm);
        return bl;
    }

    public int hashCode() {
        String string2 = this.realm;
        int n = 0;
        int n2 = string2 != null ? string2.hashCode() : 0;
        string2 = this.scheme;
        if (string2 != null) {
            n = string2.hashCode();
        }
        return (29 * 31 + n2) * 31 + n;
    }

    public String realm() {
        return this.realm;
    }

    public String scheme() {
        return this.scheme;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.scheme);
        stringBuilder.append(" realm=\"");
        stringBuilder.append(this.realm);
        stringBuilder.append("\"");
        return stringBuilder.toString();
    }
}

