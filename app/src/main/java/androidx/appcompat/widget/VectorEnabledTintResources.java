/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.appcompat.widget.ResourceManagerInternal;
import java.lang.ref.WeakReference;

public class VectorEnabledTintResources
extends Resources {
    public static final int MAX_SDK_WHERE_REQUIRED = 20;
    private static boolean sCompatVectorFromResourcesEnabled = false;
    private final WeakReference<Context> mContextRef;

    public VectorEnabledTintResources(Context context, Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mContextRef = new WeakReference<Context>(context);
    }

    public static boolean isCompatVectorFromResourcesEnabled() {
        return sCompatVectorFromResourcesEnabled;
    }

    public static void setCompatVectorFromResourcesEnabled(boolean bl) {
        sCompatVectorFromResourcesEnabled = bl;
    }

    public static boolean shouldBeUsed() {
        boolean bl = VectorEnabledTintResources.isCompatVectorFromResourcesEnabled() && Build.VERSION.SDK_INT <= 20;
        return bl;
    }

    public Drawable getDrawable(int n) throws Resources.NotFoundException {
        Context context = (Context)this.mContextRef.get();
        if (context != null) {
            return ResourceManagerInternal.get().onDrawableLoadedFromResources(context, this, n);
        }
        return super.getDrawable(n);
    }

    final Drawable superGetDrawable(int n) {
        return super.getDrawable(n);
    }
}

