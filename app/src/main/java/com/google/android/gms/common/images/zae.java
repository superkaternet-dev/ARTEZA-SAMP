/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.widget.ImageView
 */
package com.google.android.gms.common.images;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import com.google.android.gms.common.images.zag;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.internal.base.zai;
import com.google.android.gms.internal.base.zaj;
import java.lang.ref.WeakReference;

public final class zae
extends zag {
    private final WeakReference<ImageView> zac;

    public zae(ImageView imageView, int n) {
        super(Uri.EMPTY, n);
        Asserts.checkNotNull(imageView);
        this.zac = new WeakReference<ImageView>(imageView);
    }

    public zae(ImageView imageView, Uri uri) {
        super(uri, 0);
        Asserts.checkNotNull(imageView);
        this.zac = new WeakReference<ImageView>(imageView);
    }

    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof zae)) {
            return false;
        }
        zae zae2 = (zae)object;
        object = (ImageView)this.zac.get();
        zae2 = (ImageView)zae2.zac.get();
        return zae2 != null && object != null && Objects.equal(zae2, object);
    }

    public final int hashCode() {
        return 0;
    }

    @Override
    protected final void zaa(Drawable object, boolean bl, boolean bl2, boolean bl3) {
        ImageView imageView = (ImageView)this.zac.get();
        if (imageView != null) {
            if (!bl2 && !bl3 && imageView instanceof zaj) {
                object = (zaj)imageView;
                throw null;
            }
            boolean bl4 = false;
            if (!bl2 && !bl) {
                bl4 = true;
            }
            Drawable drawable2 = object;
            if (bl4) {
                Drawable drawable3 = imageView.getDrawable();
                if (drawable3 != null) {
                    drawable2 = drawable3;
                    if (drawable3 instanceof zai) {
                        drawable2 = ((zai)drawable3).zaa();
                    }
                } else {
                    drawable2 = null;
                }
                drawable2 = new zai(drawable2, (Drawable)object);
            }
            imageView.setImageDrawable(drawable2);
            if (!(imageView instanceof zaj)) {
                if (drawable2 != null && bl4) {
                    ((zai)drawable2).zab(250);
                    return;
                }
            } else {
                object = (zaj)imageView;
                throw null;
            }
        }
    }
}

