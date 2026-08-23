/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Looper
 */
package com.bumptech.glide.util;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import com.bumptech.glide.load.model.Model;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

public final class Util {
    private static final int HASH_ACCUMULATOR = 17;
    private static final int HASH_MULTIPLIER = 31;
    private static final char[] HEX_CHAR_ARRAY = "0123456789abcdef".toCharArray();
    private static final char[] SHA_256_CHARS = new char[64];
    private static volatile Handler mainThreadHandler;

    private Util() {
    }

    public static void assertBackgroundThread() {
        if (Util.isOnBackgroundThread()) {
            return;
        }
        throw new IllegalArgumentException("You must call this method on a background thread");
    }

    public static void assertMainThread() {
        if (Util.isOnMainThread()) {
            return;
        }
        throw new IllegalArgumentException("You must call this method on the main thread");
    }

    public static boolean bothModelsNullEquivalentOrEquals(Object object, Object object2) {
        if (object == null) {
            boolean bl = object2 == null;
            return bl;
        }
        if (object instanceof Model) {
            return ((Model)object).isEquivalentTo(object2);
        }
        return object.equals(object2);
    }

    public static boolean bothNullOrEqual(Object object, Object object2) {
        boolean bl = object == null ? object2 == null : object.equals(object2);
        return bl;
    }

    private static String bytesToHex(byte[] byArray, char[] cArray) {
        for (int i = 0; i < byArray.length; ++i) {
            int n = byArray[i] & 0xFF;
            char[] cArray2 = HEX_CHAR_ARRAY;
            cArray[i * 2] = cArray2[n >>> 4];
            cArray[i * 2 + 1] = cArray2[n & 0xF];
        }
        return new String(cArray);
    }

    public static <T> Queue<T> createQueue(int n) {
        return new ArrayDeque(n);
    }

    public static int getBitmapByteSize(int n, int n2, Bitmap.Config config) {
        return n * n2 * Util.getBytesPerPixel(config);
    }

    public static int getBitmapByteSize(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            if (Build.VERSION.SDK_INT >= 19) {
                try {
                    int n = bitmap.getAllocationByteCount();
                    return n;
                }
                catch (NullPointerException nullPointerException) {
                    // empty catch block
                }
            }
            return bitmap.getHeight() * bitmap.getRowBytes();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot obtain size for recycled Bitmap: ");
        stringBuilder.append(bitmap);
        stringBuilder.append("[");
        stringBuilder.append(bitmap.getWidth());
        stringBuilder.append("x");
        stringBuilder.append(bitmap.getHeight());
        stringBuilder.append("] ");
        stringBuilder.append(bitmap.getConfig());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public static int getBytesPerPixel(Bitmap.Config config) {
        int n;
        Bitmap.Config config2 = config;
        if (config == null) {
            config2 = Bitmap.Config.ARGB_8888;
        }
        switch (1.$SwitchMap$android$graphics$Bitmap$Config[config2.ordinal()]) {
            default: {
                n = 4;
                break;
            }
            case 4: {
                n = 8;
                break;
            }
            case 2: 
            case 3: {
                n = 2;
                break;
            }
            case 1: {
                n = 1;
            }
        }
        return n;
    }

    @Deprecated
    public static int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    public static <T> List<T> getSnapshot(Collection<T> collection2) {
        ArrayList<Collection<T>> arrayList = new ArrayList<Collection<T>>(collection2.size());
        for (Collection<T> collection2 : collection2) {
            if (collection2 == null) continue;
            arrayList.add(collection2);
        }
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static Handler getUiThreadHandler() {
        if (mainThreadHandler != null) return mainThreadHandler;
        synchronized (Util.class) {
            Handler handler;
            if (mainThreadHandler != null) return mainThreadHandler;
            mainThreadHandler = handler = new Handler(Looper.getMainLooper());
            return mainThreadHandler;
        }
    }

    public static int hashCode(float f) {
        return Util.hashCode(f, 17);
    }

    public static int hashCode(float f, int n) {
        return Util.hashCode(Float.floatToIntBits(f), n);
    }

    public static int hashCode(int n) {
        return Util.hashCode(n, 17);
    }

    public static int hashCode(int n, int n2) {
        return n2 * 31 + n;
    }

    public static int hashCode(Object object, int n) {
        int n2 = object == null ? 0 : object.hashCode();
        return Util.hashCode(n2, n);
    }

    public static int hashCode(boolean bl) {
        return Util.hashCode(bl, 17);
    }

    public static int hashCode(boolean bl, int n) {
        return Util.hashCode(bl ? 1 : 0, n);
    }

    public static boolean isOnBackgroundThread() {
        return Util.isOnMainThread() ^ true;
    }

    public static boolean isOnMainThread() {
        boolean bl = Looper.myLooper() == Looper.getMainLooper();
        return bl;
    }

    private static boolean isValidDimension(int n) {
        boolean bl = n > 0 || n == Integer.MIN_VALUE;
        return bl;
    }

    public static boolean isValidDimensions(int n, int n2) {
        boolean bl = Util.isValidDimension(n) && Util.isValidDimension(n2);
        return bl;
    }

    public static void postOnUiThread(Runnable runnable) {
        Util.getUiThreadHandler().post(runnable);
    }

    public static void removeCallbacksOnUiThread(Runnable runnable) {
        Util.getUiThreadHandler().removeCallbacks(runnable);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String sha256BytesToHex(byte[] object) {
        char[] cArray = SHA_256_CHARS;
        synchronized (cArray) {
            return Util.bytesToHex(object, cArray);
        }
    }
}

