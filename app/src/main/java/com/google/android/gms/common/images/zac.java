/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.net.Uri
 *  android.os.SystemClock
 */
package com.google.android.gms.common.images;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.SystemClock;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.images.zaf;
import com.google.android.gms.common.images.zag;
import com.google.android.gms.common.internal.Asserts;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

final class zac
implements Runnable {
    final ImageManager zaa;
    private final Uri zab;
    private final Bitmap zac;
    private final CountDownLatch zad;

    public zac(ImageManager imageManager, Uri uri, Bitmap bitmap, boolean bl, CountDownLatch countDownLatch) {
        this.zaa = imageManager;
        this.zab = uri;
        this.zac = bitmap;
        this.zad = countDownLatch;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        Asserts.checkMainThread("OnBitmapLoadedRunnable must be executed in the main thread");
        Bitmap bitmap = this.zac;
        Object object = (ImageManager.ImageReceiver)((Object)ImageManager.zah(this.zaa).remove(this.zab));
        if (object != null) {
            ArrayList arrayList = ImageManager.ImageReceiver.zaa((ImageManager.ImageReceiver)((Object)object));
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                object = (zag)arrayList.get(i);
                Object object2 = this.zac;
                if (object2 != null && bitmap != null) {
                    ((zag)object).zac(ImageManager.zaa(this.zaa), (Bitmap)object2, false);
                } else {
                    ImageManager.zaf(this.zaa).put(this.zab, SystemClock.elapsedRealtime());
                    object2 = this.zaa;
                    ((zag)object).zab(ImageManager.zaa((ImageManager)object2), ImageManager.zac((ImageManager)object2), false);
                }
                if (object instanceof zaf) continue;
                ImageManager.zag(this.zaa).remove(object);
            }
        }
        this.zad.countDown();
        object = ImageManager.zad();
        synchronized (object) {
            ImageManager.zae().remove(this.zab);
            return;
        }
    }
}

