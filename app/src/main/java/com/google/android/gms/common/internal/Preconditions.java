/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 *  android.text.TextUtils
 *  org.checkerframework.checker.nullness.qual.EnsuresNonNull
 */
package com.google.android.gms.common.internal;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.google.android.gms.common.util.zzb;
import org.checkerframework.checker.nullness.qual.EnsuresNonNull;

public final class Preconditions {
    private Preconditions() {
        throw new AssertionError((Object)"Uninstantiable");
    }

    public static void checkArgument(boolean bl) {
        if (bl) {
            return;
        }
        throw new IllegalArgumentException();
    }

    public static void checkArgument(boolean bl, Object object) {
        if (bl) {
            return;
        }
        throw new IllegalArgumentException(String.valueOf(object));
    }

    public static void checkArgument(boolean bl, String string2, Object ... objectArray) {
        if (bl) {
            return;
        }
        throw new IllegalArgumentException(String.format(string2, objectArray));
    }

    public static void checkHandlerThread(Handler object) {
        Object object2 = Looper.myLooper();
        if (object2 != object.getLooper()) {
            object2 = object2 != null ? object2.getThread().getName() : "null current looper";
            object = object.getLooper().getThread().getName();
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(object).length() + 36 + String.valueOf(object2).length());
            stringBuilder.append("Must be called on ");
            stringBuilder.append((String)object);
            stringBuilder.append(" thread, but got ");
            stringBuilder.append((String)object2);
            stringBuilder.append(".");
            throw new IllegalStateException(stringBuilder.toString());
        }
    }

    public static void checkHandlerThread(Handler handler, String string2) {
        if (Looper.myLooper() == handler.getLooper()) {
            return;
        }
        throw new IllegalStateException(string2);
    }

    public static void checkMainThread(String string2) {
        if (zzb.zza()) {
            return;
        }
        throw new IllegalStateException(string2);
    }

    @EnsuresNonNull(value={"#1"})
    public static String checkNotEmpty(String string2) {
        if (!TextUtils.isEmpty((CharSequence)string2)) {
            return string2;
        }
        throw new IllegalArgumentException("Given String is empty or null");
    }

    @EnsuresNonNull(value={"#1"})
    public static String checkNotEmpty(String string2, Object object) {
        if (!TextUtils.isEmpty((CharSequence)string2)) {
            return string2;
        }
        throw new IllegalArgumentException(String.valueOf(object));
    }

    public static void checkNotMainThread() {
        Preconditions.checkNotMainThread("Must not be called on the main application thread");
    }

    public static void checkNotMainThread(String string2) {
        if (!zzb.zza()) {
            return;
        }
        throw new IllegalStateException(string2);
    }

    @EnsuresNonNull(value={"#1"})
    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException("null reference");
    }

    @EnsuresNonNull(value={"#1"})
    public static <T> T checkNotNull(T t, Object object) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(object));
    }

    public static int checkNotZero(int n) {
        if (n != 0) {
            return n;
        }
        throw new IllegalArgumentException("Given Integer is zero");
    }

    public static int checkNotZero(int n, Object object) {
        if (n != 0) {
            return n;
        }
        throw new IllegalArgumentException(String.valueOf(object));
    }

    public static long checkNotZero(long l) {
        if (l != 0L) {
            return l;
        }
        throw new IllegalArgumentException("Given Long is zero");
    }

    public static long checkNotZero(long l, Object object) {
        if (l != 0L) {
            return l;
        }
        throw new IllegalArgumentException(String.valueOf(object));
    }

    public static void checkState(boolean bl) {
        if (bl) {
            return;
        }
        throw new IllegalStateException();
    }

    public static void checkState(boolean bl, Object object) {
        if (bl) {
            return;
        }
        throw new IllegalStateException(String.valueOf(object));
    }

    public static void checkState(boolean bl, String string2, Object ... objectArray) {
        if (bl) {
            return;
        }
        throw new IllegalStateException(String.format(string2, objectArray));
    }
}

