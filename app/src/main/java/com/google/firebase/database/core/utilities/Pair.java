/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

public final class Pair<T, U> {
    private final T first;
    private final U second;

    public Pair(T t, U u) {
        this.first = t;
        this.second = u;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (Pair)object;
            Object object2 = this.first;
            if (object2 != null ? !object2.equals(((Pair)object).first) : ((Pair)object).first != null) {
                return false;
            }
            object2 = this.second;
            return !(object2 != null ? !object2.equals(((Pair)object).second) : ((Pair)object).second != null);
        }
        return false;
    }

    public T getFirst() {
        return this.first;
    }

    public U getSecond() {
        return this.second;
    }

    public int hashCode() {
        Object object = this.first;
        int n = 0;
        int n2 = object != null ? object.hashCode() : 0;
        object = this.second;
        if (object != null) {
            n = object.hashCode();
        }
        return n2 * 31 + n;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Pair(");
        stringBuilder.append(this.first);
        stringBuilder.append(",");
        stringBuilder.append(this.second);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}

