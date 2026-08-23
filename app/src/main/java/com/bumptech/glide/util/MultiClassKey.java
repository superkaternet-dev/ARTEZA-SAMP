/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util;

import com.bumptech.glide.util.Util;

public class MultiClassKey {
    private Class<?> first;
    private Class<?> second;
    private Class<?> third;

    public MultiClassKey() {
    }

    public MultiClassKey(Class<?> clazz, Class<?> clazz2) {
        this.set(clazz, clazz2);
    }

    public MultiClassKey(Class<?> clazz, Class<?> clazz2, Class<?> clazz3) {
        this.set(clazz, clazz2, clazz3);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (MultiClassKey)object;
            if (!this.first.equals(((MultiClassKey)object).first)) {
                return false;
            }
            if (!this.second.equals(((MultiClassKey)object).second)) {
                return false;
            }
            return Util.bothNullOrEqual(this.third, ((MultiClassKey)object).third);
        }
        return false;
    }

    public int hashCode() {
        int n = this.first.hashCode();
        int n2 = this.second.hashCode();
        Class<?> clazz = this.third;
        int n3 = clazz != null ? clazz.hashCode() : 0;
        return (n * 31 + n2) * 31 + n3;
    }

    public void set(Class<?> clazz, Class<?> clazz2) {
        this.set(clazz, clazz2, null);
    }

    public void set(Class<?> clazz, Class<?> clazz2, Class<?> clazz3) {
        this.first = clazz;
        this.second = clazz2;
        this.third = clazz3;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MultiClassKey{first=");
        stringBuilder.append(this.first);
        stringBuilder.append(", second=");
        stringBuilder.append(this.second);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

