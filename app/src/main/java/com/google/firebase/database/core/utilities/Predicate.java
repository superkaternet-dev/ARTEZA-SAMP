/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

public interface Predicate<T> {
    public static final Predicate<Object> TRUE = new Predicate<Object>(){

        @Override
        public boolean evaluate(Object object) {
            return true;
        }
    };

    public boolean evaluate(T var1);
}

