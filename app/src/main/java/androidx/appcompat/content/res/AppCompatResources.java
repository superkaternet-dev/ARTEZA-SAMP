/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.content.res.XmlResourceParser
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.SparseArray
 *  android.util.TypedValue
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.appcompat.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ColorStateListInflaterCompat;
import java.util.WeakHashMap;
import org.xmlpull.v1.XmlPullParser;

public final class AppCompatResources {
    private static final String LOG_TAG = "AppCompatResources";
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE = new ThreadLocal();
    private static final Object sColorStateCacheLock;
    private static final WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> sColorStateCaches;

    static {
        sColorStateCaches = new WeakHashMap(0);
        sColorStateCacheLock = new Object();
    }

    private AppCompatResources() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void addColorStateListToCache(Context context, int n, ColorStateList colorStateList) {
        Object object = sColorStateCacheLock;
        synchronized (object) {
            Object object2;
            WeakHashMap<Context, SparseArray<ColorStateListCacheEntry>> weakHashMap = sColorStateCaches;
            SparseArray sparseArray = object2 = weakHashMap.get(context);
            if (object2 == null) {
                sparseArray = new SparseArray();
                weakHashMap.put(context, (SparseArray<ColorStateListCacheEntry>)sparseArray);
            }
            object2 = new ColorStateListCacheEntry(colorStateList, context.getResources().getConfiguration());
            sparseArray.append(n, object2);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static ColorStateList getCachedColorStateList(Context context, int n) {
        Object object = sColorStateCacheLock;
        synchronized (object) {
            SparseArray<ColorStateListCacheEntry> sparseArray = sColorStateCaches.get(context);
            if (sparseArray == null) return null;
            if (sparseArray.size() <= 0) return null;
            ColorStateListCacheEntry colorStateListCacheEntry = (ColorStateListCacheEntry)sparseArray.get(n);
            if (colorStateListCacheEntry == null) return null;
            if (colorStateListCacheEntry.configuration.equals(context.getResources().getConfiguration())) {
                return colorStateListCacheEntry.value;
            }
            sparseArray.remove(n);
            return null;
        }
    }

    public static ColorStateList getColorStateList(Context context, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            return context.getColorStateList(n);
        }
        ColorStateList colorStateList = AppCompatResources.getCachedColorStateList(context, n);
        if (colorStateList != null) {
            return colorStateList;
        }
        colorStateList = AppCompatResources.inflateColorStateList(context, n);
        if (colorStateList != null) {
            AppCompatResources.addColorStateListToCache(context, n, colorStateList);
            return colorStateList;
        }
        return ContextCompat.getColorStateList(context, n);
    }

    public static Drawable getDrawable(Context context, int n) {
        return ResourceManagerInternal.get().getDrawable(context, n);
    }

    private static TypedValue getTypedValue() {
        TypedValue typedValue;
        ThreadLocal<TypedValue> threadLocal = TL_TYPED_VALUE;
        TypedValue typedValue2 = typedValue = threadLocal.get();
        if (typedValue == null) {
            typedValue2 = new TypedValue();
            threadLocal.set(typedValue2);
        }
        return typedValue2;
    }

    private static ColorStateList inflateColorStateList(Context context, int n) {
        if (AppCompatResources.isColorInt(context, n)) {
            return null;
        }
        Resources resources = context.getResources();
        XmlResourceParser xmlResourceParser = resources.getXml(n);
        try {
            context = ColorStateListInflaterCompat.createFromXml(resources, (XmlPullParser)xmlResourceParser, context.getTheme());
            return context;
        }
        catch (Exception exception) {
            Log.e((String)LOG_TAG, (String)"Failed to inflate ColorStateList, leaving it to the framework", (Throwable)exception);
            return null;
        }
    }

    private static boolean isColorInt(Context context, int n) {
        context = context.getResources();
        TypedValue typedValue = AppCompatResources.getTypedValue();
        boolean bl = true;
        context.getValue(n, typedValue, true);
        if (typedValue.type < 28 || typedValue.type > 31) {
            bl = false;
        }
        return bl;
    }

    private static class ColorStateListCacheEntry {
        final Configuration configuration;
        final ColorStateList value;

        ColorStateListCacheEntry(ColorStateList colorStateList, Configuration configuration) {
            this.value = colorStateList;
            this.configuration = configuration;
        }
    }
}

