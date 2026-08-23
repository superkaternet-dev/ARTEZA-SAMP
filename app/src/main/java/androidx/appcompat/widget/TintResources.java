/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.graphics.drawable.Drawable
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.ResourceManagerInternal;
import androidx.appcompat.widget.ResourcesWrapper;
import java.lang.ref.WeakReference;

class TintResources
extends ResourcesWrapper {
    private final WeakReference<Context> mContextRef;

    public TintResources(Context context, Resources resources) {
        super(resources);
        this.mContextRef = new WeakReference<Context>(context);
    }

    @Override
    public Drawable getDrawable(int n) throws Resources.NotFoundException {
        Drawable drawable2 = super.getDrawable(n);
        Context context = (Context)this.mContextRef.get();
        if (drawable2 != null && context != null) {
            ResourceManagerInternal.get().tintDrawableUsingColorFilter(context, n, drawable2);
        }
        return drawable2;
    }
}

