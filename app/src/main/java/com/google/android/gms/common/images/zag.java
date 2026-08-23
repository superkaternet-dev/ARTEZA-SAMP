/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 */
package com.google.android.gms.common.images;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.google.android.gms.common.images.zad;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.internal.base.zak;

public abstract class zag {
    final zad zaa;
    protected int zab = 0;

    public zag(Uri uri, int n) {
        this.zaa = new zad(uri);
        this.zab = n;
    }

    protected abstract void zaa(Drawable var1, boolean var2, boolean var3, boolean var4);

    final void zab(Context object, zak zak2, boolean bl) {
        int n = this.zab;
        object = n != 0 ? object.getResources().getDrawable(n) : null;
        this.zaa((Drawable)object, bl, false, false);
    }

    final void zac(Context context, Bitmap bitmap, boolean bl) {
        Asserts.checkNotNull(bitmap);
        this.zaa((Drawable)new BitmapDrawable(context.getResources(), bitmap), false, false, true);
    }
}

