/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 */
package com.google.android.gms.common.images;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.images.zag;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Objects;
import java.lang.ref.WeakReference;

public final class zaf
extends zag {
    private final WeakReference<ImageManager.OnImageLoadedListener> zac;

    public zaf(ImageManager.OnImageLoadedListener onImageLoadedListener, Uri uri) {
        super(uri, 0);
        Asserts.checkNotNull(onImageLoadedListener);
        this.zac = new WeakReference<ImageManager.OnImageLoadedListener>(onImageLoadedListener);
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof zaf)) {
            return false;
        }
        zaf zaf2 = (zaf)object;
        object = (ImageManager.OnImageLoadedListener)this.zac.get();
        ImageManager.OnImageLoadedListener onImageLoadedListener = (ImageManager.OnImageLoadedListener)zaf2.zac.get();
        return onImageLoadedListener != null && object != null && Objects.equal(onImageLoadedListener, object) && Objects.equal(zaf2.zaa, this.zaa);
    }

    public final int hashCode() {
        return Objects.hashCode(this.zaa);
    }

    @Override
    protected final void zaa(Drawable drawable2, boolean bl, boolean bl2, boolean bl3) {
        ImageManager.OnImageLoadedListener onImageLoadedListener;
        if (!bl2 && (onImageLoadedListener = (ImageManager.OnImageLoadedListener)this.zac.get()) != null) {
            onImageLoadedListener.onImageLoaded(this.zaa.zaa, drawable2, bl3);
        }
    }
}

