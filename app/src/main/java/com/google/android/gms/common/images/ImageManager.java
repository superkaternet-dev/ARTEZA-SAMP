/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.ParcelFileDescriptor
 *  android.os.Parcelable
 *  android.os.ResultReceiver
 *  android.widget.ImageView
 */
package com.google.android.gms.common.images;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.widget.ImageView;
import com.google.android.gms.common.images.zaa;
import com.google.android.gms.common.images.zab;
import com.google.android.gms.common.images.zae;
import com.google.android.gms.common.images.zaf;
import com.google.android.gms.common.images.zag;
import com.google.android.gms.common.internal.Asserts;
import com.google.android.gms.internal.base.zak;
import com.google.android.gms.internal.base.zap;
import com.google.android.gms.internal.base.zaq;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public final class ImageManager {
    private static final Object zaa = new Object();
    private static HashSet<Uri> zab = new HashSet();
    private static ImageManager zac;
    private final Context zad;
    private final Handler zae;
    private final ExecutorService zaf;
    private final zak zag;
    private final Map<zag, ImageReceiver> zah;
    private final Map<Uri, ImageReceiver> zai;
    private final Map<Uri, Long> zaj;

    private ImageManager(Context context, boolean bl) {
        this.zad = context.getApplicationContext();
        this.zae = new zaq(Looper.getMainLooper());
        this.zaf = zap.zaa().zab(4, 2);
        this.zag = new zak();
        this.zah = new HashMap<zag, ImageReceiver>();
        this.zai = new HashMap<Uri, ImageReceiver>();
        this.zaj = new HashMap<Uri, Long>();
    }

    public static ImageManager create(Context context) {
        if (zac == null) {
            zac = new ImageManager(context, false);
        }
        return zac;
    }

    static /* bridge */ /* synthetic */ Handler zab(ImageManager imageManager) {
        return imageManager.zae;
    }

    static /* bridge */ /* synthetic */ zak zac(ImageManager imageManager) {
        return imageManager.zag;
    }

    static /* bridge */ /* synthetic */ Object zad() {
        return zaa;
    }

    static /* bridge */ /* synthetic */ HashSet zae() {
        return zab;
    }

    static /* bridge */ /* synthetic */ Map zaf(ImageManager imageManager) {
        return imageManager.zaj;
    }

    static /* bridge */ /* synthetic */ Map zag(ImageManager imageManager) {
        return imageManager.zah;
    }

    static /* bridge */ /* synthetic */ Map zah(ImageManager imageManager) {
        return imageManager.zai;
    }

    public void loadImage(ImageView imageView, int n) {
        this.zaj(new zae(imageView, n));
    }

    public void loadImage(ImageView imageView, Uri uri) {
        this.zaj(new zae(imageView, uri));
    }

    public void loadImage(ImageView object, Uri uri, int n) {
        object = new zae((ImageView)object, uri);
        object.zab = n;
        this.zaj((zag)object);
    }

    public void loadImage(OnImageLoadedListener onImageLoadedListener, Uri uri) {
        this.zaj(new zaf(onImageLoadedListener, uri));
    }

    public void loadImage(OnImageLoadedListener object, Uri uri, int n) {
        object = new zaf((OnImageLoadedListener)object, uri);
        ((zag)object).zab = n;
        this.zaj((zag)object);
    }

    public final void zaj(zag zag2) {
        Asserts.checkMainThread("ImageManager.loadImage() must be called in the main thread");
        new zab(this, zag2).run();
    }

    private final class ImageReceiver
    extends ResultReceiver {
        final ImageManager zaa;
        private final Uri zab;
        private final ArrayList<zag> zac;

        ImageReceiver(ImageManager imageManager, Uri uri) {
            this.zaa = imageManager;
            super((Handler)new zaq(Looper.getMainLooper()));
            this.zab = uri;
            this.zac = new ArrayList();
        }

        static /* bridge */ /* synthetic */ ArrayList zaa(ImageReceiver imageReceiver) {
            return imageReceiver.zac;
        }

        public final void onReceiveResult(int n, Bundle object) {
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor)object.getParcelable("com.google.android.gms.extra.fileDescriptor");
            object = this.zaa;
            ((ImageManager)object).zaf.execute(new zaa((ImageManager)object, this.zab, parcelFileDescriptor));
        }

        public final void zab(zag zag2) {
            Asserts.checkMainThread("ImageReceiver.addImageRequest() must be called in the main thread");
            this.zac.add(zag2);
        }

        public final void zac(zag zag2) {
            Asserts.checkMainThread("ImageReceiver.removeImageRequest() must be called in the main thread");
            this.zac.remove(zag2);
        }

        public final void zad() {
            Intent intent = new Intent("com.google.android.gms.common.images.LOAD_IMAGE");
            intent.setPackage("com.google.android.gms");
            intent.putExtra("com.google.android.gms.extras.uri", (Parcelable)this.zab);
            intent.putExtra("com.google.android.gms.extras.resultReceiver", (Parcelable)this);
            intent.putExtra("com.google.android.gms.extras.priority", 3);
            this.zaa.zad.sendBroadcast(intent);
        }
    }

    public static interface OnImageLoadedListener {
        public void onImageLoaded(Uri var1, Drawable var2, boolean var3);
    }
}

