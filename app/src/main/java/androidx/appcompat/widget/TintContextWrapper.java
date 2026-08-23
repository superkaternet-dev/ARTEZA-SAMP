/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.ContextWrapper
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.content.res.Resources$Theme
 *  android.os.Build$VERSION
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Build;
import androidx.appcompat.widget.TintResources;
import androidx.appcompat.widget.VectorEnabledTintResources;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TintContextWrapper
extends ContextWrapper {
    private static final Object CACHE_LOCK = new Object();
    private static ArrayList<WeakReference<TintContextWrapper>> sCache;
    private final Resources mResources;
    private final Resources.Theme mTheme;

    private TintContextWrapper(Context context) {
        super(context);
        if (VectorEnabledTintResources.shouldBeUsed()) {
            VectorEnabledTintResources vectorEnabledTintResources = new VectorEnabledTintResources((Context)this, context.getResources());
            this.mResources = vectorEnabledTintResources;
            vectorEnabledTintResources = vectorEnabledTintResources.newTheme();
            this.mTheme = vectorEnabledTintResources;
            vectorEnabledTintResources.setTo(context.getTheme());
        } else {
            this.mResources = new TintResources((Context)this, context.getResources());
            this.mTheme = null;
        }
    }

    private static boolean shouldWrap(Context context) {
        boolean bl = context instanceof TintContextWrapper;
        boolean bl2 = false;
        if (!(bl || context.getResources() instanceof TintResources || context.getResources() instanceof VectorEnabledTintResources)) {
            if (Build.VERSION.SDK_INT < 21 || VectorEnabledTintResources.shouldBeUsed()) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Context wrap(Context object) {
        if (!TintContextWrapper.shouldWrap((Context)object)) {
            return object;
        }
        Object object2 = CACHE_LOCK;
        synchronized (object2) {
            Object object3 = sCache;
            if (object3 == null) {
                object3 = new Object();
                sCache = object3;
            } else {
                int n;
                for (n = object3.size() - 1; n >= 0; --n) {
                    object3 = sCache.get(n);
                    if (object3 != null && ((Reference)object3).get() != null) continue;
                    sCache.remove(n);
                }
                for (n = sCache.size() - 1; n >= 0; --n) {
                    object3 = sCache.get(n);
                    object3 = object3 != null ? (TintContextWrapper)((Object)((Reference)object3).get()) : null;
                    if (object3 == null || object3.getBaseContext() != object) continue;
                    return object3;
                }
            }
            object3 = new Object((Context)object);
            object = sCache;
            WeakReference<Object> weakReference = new WeakReference<Object>(object3);
            ((ArrayList)object).add(weakReference);
            return object3;
        }
    }

    public AssetManager getAssets() {
        return this.mResources.getAssets();
    }

    public Resources getResources() {
        return this.mResources;
    }

    public Resources.Theme getTheme() {
        Resources.Theme theme;
        Resources.Theme theme2 = theme = this.mTheme;
        if (theme == null) {
            theme2 = super.getTheme();
        }
        return theme2;
    }

    public void setTheme(int n) {
        Resources.Theme theme = this.mTheme;
        if (theme == null) {
            super.setTheme(n);
        } else {
            theme.applyStyle(n, true);
        }
    }
}

