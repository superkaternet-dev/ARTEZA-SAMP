/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.icu.util.ULocale
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package androidx.core.text;

import android.icu.util.ULocale;
import android.os.Build;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

public final class ICUCompat {
    private static final String TAG = "ICUCompat";
    private static Method sAddLikelySubtagsMethod;
    private static Method sGetScriptMethod;

    static {
        block8: {
            if (Build.VERSION.SDK_INT < 21) {
                Class<?> clazz = Class.forName("libcore.icu.ICU");
                if (clazz == null) break block8;
                try {
                    sGetScriptMethod = clazz.getMethod("getScript", String.class);
                    sAddLikelySubtagsMethod = clazz.getMethod("addLikelySubtags", String.class);
                }
                catch (Exception exception) {
                    sGetScriptMethod = null;
                    sAddLikelySubtagsMethod = null;
                    Log.w((String)TAG, (Throwable)exception);
                }
            } else if (Build.VERSION.SDK_INT < 24) {
                try {
                    sAddLikelySubtagsMethod = Class.forName("libcore.icu.ICU").getMethod("addLikelySubtags", Locale.class);
                }
                catch (Exception exception) {
                    throw new IllegalStateException(exception);
                }
            }
        }
    }

    private ICUCompat() {
    }

    private static String addLikelySubtags(Locale object) {
        block4: {
            object = ((Locale)object).toString();
            Object object2 = sAddLikelySubtagsMethod;
            if (object2 == null) break block4;
            try {
                object2 = (String)((Method)object2).invoke(null, object);
                return object2;
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.w((String)TAG, (Throwable)invocationTargetException);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.w((String)TAG, (Throwable)illegalAccessException);
            }
        }
        return object;
    }

    private static String getScript(String string2) {
        block4: {
            Method method = sGetScriptMethod;
            if (method == null) break block4;
            try {
                string2 = (String)method.invoke(null, string2);
                return string2;
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.w((String)TAG, (Throwable)invocationTargetException);
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.w((String)TAG, (Throwable)illegalAccessException);
            }
        }
        return null;
    }

    public static String maximizeAndGetScript(Locale object) {
        if (Build.VERSION.SDK_INT >= 24) {
            return ULocale.addLikelySubtags((ULocale)ULocale.forLocale((Locale)object)).getScript();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                String string2 = ((Locale)sAddLikelySubtagsMethod.invoke(null, object)).getScript();
                return string2;
            }
            catch (IllegalAccessException illegalAccessException) {
                Log.w((String)TAG, (Throwable)illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                Log.w((String)TAG, (Throwable)invocationTargetException);
            }
            return ((Locale)object).getScript();
        }
        if ((object = ICUCompat.addLikelySubtags((Locale)object)) != null) {
            return ICUCompat.getScript((String)object);
        }
        return null;
    }
}

