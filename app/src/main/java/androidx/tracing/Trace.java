/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.Trace
 *  android.util.Log
 */
package androidx.tracing;

import android.os.Build;
import android.util.Log;
import androidx.tracing.TraceApi18Impl;
import androidx.tracing.TraceApi29Impl;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Trace {
    static final String TAG = "Trace";
    private static Method sAsyncTraceBeginMethod;
    private static Method sAsyncTraceEndMethod;
    private static Method sIsTagEnabledMethod;
    private static Method sTraceCounterMethod;
    private static long sTraceTagApp;

    private Trace() {
    }

    public static void beginAsyncSection(String string2, int n) {
        try {
            if (sAsyncTraceBeginMethod == null) {
                TraceApi29Impl.beginAsyncSection(string2, n);
                return;
            }
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
        }
        catch (NoSuchMethodError noSuchMethodError) {
            // empty catch block
        }
        Trace.beginAsyncSectionFallback(string2, n);
    }

    private static void beginAsyncSectionFallback(String string2, int n) {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (sAsyncTraceBeginMethod == null) {
                    sAsyncTraceBeginMethod = android.os.Trace.class.getMethod("asyncTraceBegin", Long.TYPE, String.class, Integer.TYPE);
                }
                sAsyncTraceBeginMethod.invoke(null, sTraceTagApp, string2, n);
            }
            catch (Exception exception) {
                Trace.handleException("asyncTraceBegin", exception);
            }
        }
    }

    public static void beginSection(String string2) {
        if (Build.VERSION.SDK_INT >= 18) {
            TraceApi18Impl.beginSection(string2);
        }
    }

    public static void endAsyncSection(String string2, int n) {
        try {
            if (sAsyncTraceEndMethod == null) {
                TraceApi29Impl.endAsyncSection(string2, n);
                return;
            }
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
        }
        catch (NoSuchMethodError noSuchMethodError) {
            // empty catch block
        }
        Trace.endAsyncSectionFallback(string2, n);
    }

    private static void endAsyncSectionFallback(String string2, int n) {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (sAsyncTraceEndMethod == null) {
                    sAsyncTraceEndMethod = android.os.Trace.class.getMethod("asyncTraceEnd", Long.TYPE, String.class, Integer.TYPE);
                }
                sAsyncTraceEndMethod.invoke(null, sTraceTagApp, string2, n);
            }
            catch (Exception exception) {
                Trace.handleException("asyncTraceEnd", exception);
            }
        }
    }

    public static void endSection() {
        if (Build.VERSION.SDK_INT >= 18) {
            TraceApi18Impl.endSection();
        }
    }

    private static void handleException(String object, Exception exception) {
        if (exception instanceof InvocationTargetException) {
            object = exception.getCause();
            if (object instanceof RuntimeException) {
                throw (RuntimeException)object;
            }
            throw new RuntimeException((Throwable)object);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to call ");
        stringBuilder.append((String)object);
        stringBuilder.append(" via reflection");
        Log.v((String)TAG, (String)stringBuilder.toString(), (Throwable)exception);
    }

    public static boolean isEnabled() {
        try {
            if (sIsTagEnabledMethod == null) {
                boolean bl = android.os.Trace.isEnabled();
                return bl;
            }
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
        }
        catch (NoSuchMethodError noSuchMethodError) {
            // empty catch block
        }
        return Trace.isEnabledFallback();
    }

    private static boolean isEnabledFallback() {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (sIsTagEnabledMethod == null) {
                    sTraceTagApp = android.os.Trace.class.getField("TRACE_TAG_APP").getLong(null);
                    sIsTagEnabledMethod = android.os.Trace.class.getMethod("isTagEnabled", Long.TYPE);
                }
                boolean bl = (Boolean)sIsTagEnabledMethod.invoke(null, sTraceTagApp);
                return bl;
            }
            catch (Exception exception) {
                Trace.handleException("isTagEnabled", exception);
            }
        }
        return false;
    }

    public static void setCounter(String string2, int n) {
        try {
            if (sTraceCounterMethod == null) {
                TraceApi29Impl.setCounter(string2, n);
                return;
            }
        }
        catch (NoClassDefFoundError noClassDefFoundError) {
        }
        catch (NoSuchMethodError noSuchMethodError) {
            // empty catch block
        }
        Trace.setCounterFallback(string2, n);
    }

    private static void setCounterFallback(String string2, int n) {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                if (sTraceCounterMethod == null) {
                    sTraceCounterMethod = android.os.Trace.class.getMethod("traceCounter", Long.TYPE, String.class, Integer.TYPE);
                }
                sTraceCounterMethod.invoke(null, sTraceTagApp, string2, n);
            }
            catch (Exception exception) {
                Trace.handleException("traceCounter", exception);
            }
        }
    }
}

