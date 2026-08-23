/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Trace
 */
package androidx.tracing;

import android.os.Trace;

final class TraceApi29Impl {
    private TraceApi29Impl() {
    }

    public static void beginAsyncSection(String string2, int n) {
        Trace.beginAsyncSection((String)string2, (int)n);
    }

    public static void endAsyncSection(String string2, int n) {
        Trace.endAsyncSection((String)string2, (int)n);
    }

    public static void setCounter(String string2, int n) {
        Trace.setCounter((String)string2, (long)n);
    }
}

