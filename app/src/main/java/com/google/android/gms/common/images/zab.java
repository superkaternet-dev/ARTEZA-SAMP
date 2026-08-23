/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.SystemClock
 */
package com.google.android.gms.common.images;

import android.os.SystemClock;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.images.zad;
import com.google.android.gms.common.images.zaf;
import com.google.android.gms.common.images.zag;
import com.google.android.gms.common.internal.Asserts;

final class zab
implements Runnable {
    final ImageManager zaa;
    private final zag zab;

    public zab(ImageManager imageManager, zag zag2) {
        this.zaa = imageManager;
        this.zab = zag2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public final void run() {
        Asserts.checkMainThread("LoadImageRunnable must be executed on the main thread");
        Object object = (ImageManager.ImageReceiver)((Object)ImageManager.zag(this.zaa).get(this.zab));
        if (object != null) {
            ImageManager.zag(this.zaa).remove(this.zab);
            ((ImageManager.ImageReceiver)((Object)object)).zac(this.zab);
        }
        object = this.zab;
        zad zad2 = ((zag)object).zaa;
        Object object2 = zad2.zaa;
        if (object2 == null) {
            object2 = this.zaa;
            ((zag)object).zab(ImageManager.zaa((ImageManager)object2), ImageManager.zac((ImageManager)object2), true);
            return;
        }
        object = (Long)ImageManager.zaf(this.zaa).get(object2);
        if (object != null) {
            if (SystemClock.elapsedRealtime() - (Long)object < 3600000L) {
                object = this.zab;
                object2 = this.zaa;
                ((zag)object).zab(ImageManager.zaa((ImageManager)object2), ImageManager.zac((ImageManager)object2), true);
                return;
            }
            ImageManager.zaf(this.zaa).remove(zad2.zaa);
        }
        this.zab.zaa(null, false, true, false);
        object2 = (ImageManager.ImageReceiver)((Object)ImageManager.zah(this.zaa).get(zad2.zaa));
        object = object2;
        if (object2 == null) {
            object = new ImageManager.ImageReceiver(this.zaa, zad2.zaa);
            ImageManager.zah(this.zaa).put(zad2.zaa, object);
        }
        ((ImageManager.ImageReceiver)((Object)object)).zab(this.zab);
        object2 = this.zab;
        if (!(object2 instanceof zaf)) {
            ImageManager.zag(this.zaa).put(object2, object);
        }
        object2 = ImageManager.zad();
        synchronized (object2) {
            if (!ImageManager.zae().contains(zad2.zaa)) {
                ImageManager.zae().add(zad2.zaa);
                ((ImageManager.ImageReceiver)((Object)object)).zad();
            }
            return;
        }
    }
}

