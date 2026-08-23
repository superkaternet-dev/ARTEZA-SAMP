/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

public interface ThreadInitializer {
    public static final ThreadInitializer defaultInstance = new ThreadInitializer(){

        @Override
        public void setDaemon(Thread thread2, boolean bl) {
            thread2.setDaemon(bl);
        }

        @Override
        public void setName(Thread thread2, String string2) {
            thread2.setName(string2);
        }

        @Override
        public void setUncaughtExceptionHandler(Thread thread2, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
            thread2.setUncaughtExceptionHandler(uncaughtExceptionHandler);
        }
    };

    public void setDaemon(Thread var1, boolean var2);

    public void setName(Thread var1, String var2);

    public void setUncaughtExceptionHandler(Thread var1, Thread.UncaughtExceptionHandler var2);
}

