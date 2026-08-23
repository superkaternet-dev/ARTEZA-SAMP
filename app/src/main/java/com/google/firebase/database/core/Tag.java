/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

public final class Tag {
    private final long tagNumber;

    public Tag(long l) {
        this.tagNumber = l;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (Tag)object;
            return this.tagNumber == ((Tag)object).tagNumber;
        }
        return false;
    }

    public long getTagNumber() {
        return this.tagNumber;
    }

    public int hashCode() {
        long l = this.tagNumber;
        return (int)(l ^ l >>> 32);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Tag{tagNumber=");
        stringBuilder.append(this.tagNumber);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

