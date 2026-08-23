/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.os.Build
 *  android.os.ParcelFileDescriptor
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import java.io.IOException;

public final class ParcelFileDescriptorBitmapDecoder
implements ResourceDecoder<ParcelFileDescriptor, Bitmap> {
    private static final int MAXIMUM_FILE_BYTE_SIZE_FOR_FILE_DESCRIPTOR_DECODER = 0x20000000;
    private final Downsampler downsampler;

    public ParcelFileDescriptorBitmapDecoder(Downsampler downsampler) {
        this.downsampler = downsampler;
    }

    private boolean isSafeToTryDecoding(ParcelFileDescriptor parcelFileDescriptor) {
        boolean bl = "HUAWEI".equalsIgnoreCase(Build.MANUFACTURER);
        boolean bl2 = true;
        if (!bl && !"HONOR".equalsIgnoreCase(Build.MANUFACTURER)) {
            return true;
        }
        if (parcelFileDescriptor.getStatSize() > 0x20000000L) {
            bl2 = false;
        }
        return bl2;
    }

    @Override
    public Resource<Bitmap> decode(ParcelFileDescriptor parcelFileDescriptor, int n, int n2, Options options) throws IOException {
        return this.downsampler.decode(parcelFileDescriptor, n, n2, options);
    }

    @Override
    public boolean handles(ParcelFileDescriptor parcelFileDescriptor, Options options) {
        boolean bl = this.isSafeToTryDecoding(parcelFileDescriptor) && this.downsampler.handles(parcelFileDescriptor);
        return bl;
    }
}

