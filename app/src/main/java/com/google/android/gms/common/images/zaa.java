/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.BitmapFactory
 *  android.net.Uri
 *  android.os.ParcelFileDescriptor
 *  android.util.Log
 */
package com.google.android.gms.common.images;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.google.android.gms.common.images.ImageManager;
import com.google.android.gms.common.images.zac;
import com.google.android.gms.common.internal.Asserts;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

final class zaa
implements Runnable {
    final ImageManager zaa;
    private final Uri zab;
    private final ParcelFileDescriptor zac;

    public zaa(ImageManager imageManager, Uri uri, ParcelFileDescriptor parcelFileDescriptor) {
        this.zaa = imageManager;
        this.zab = uri;
        this.zac = parcelFileDescriptor;
    }

    @Override
    public final void run() {
        Object object;
        Asserts.checkNotMainThread("LoadBitmapFromDiskRunnable can't be executed in the main thread");
        Object object2 = this.zac;
        ParcelFileDescriptor parcelFileDescriptor = null;
        boolean bl = false;
        if (object2 != null) {
            try {
                parcelFileDescriptor = object2 = BitmapFactory.decodeFileDescriptor((FileDescriptor)object2.getFileDescriptor());
            }
            catch (OutOfMemoryError outOfMemoryError) {
                object = String.valueOf(this.zab);
                String.valueOf(object).length();
                Log.e((String)"ImageManager", (String)"OOM while loading bitmap for uri: ".concat(String.valueOf(object)), (Throwable)outOfMemoryError);
                bl = true;
            }
            try {
                this.zac.close();
            }
            catch (IOException iOException) {
                Log.e((String)"ImageManager", (String)"closed failed", (Throwable)iOException);
            }
        } else {
            parcelFileDescriptor = null;
            bl = false;
        }
        object = new CountDownLatch(1);
        object2 = this.zaa;
        ImageManager.zab((ImageManager)object2).post((Runnable)new zac((ImageManager)object2, this.zab, (Bitmap)parcelFileDescriptor, bl, (CountDownLatch)object));
        try {
            ((CountDownLatch)object).await();
            return;
        }
        catch (InterruptedException interruptedException) {
            String string2 = String.valueOf(this.zab);
            String.valueOf(string2).length();
            Log.w((String)"ImageManager", (String)"Latch interrupted while posting ".concat(String.valueOf(string2)));
            return;
        }
    }
}

