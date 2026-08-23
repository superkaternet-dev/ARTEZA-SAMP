/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.ColorSpace
 *  android.graphics.ColorSpace$Named
 *  android.graphics.ImageDecoder
 *  android.graphics.ImageDecoder$DecodeException
 *  android.graphics.ImageDecoder$ImageInfo
 *  android.graphics.ImageDecoder$OnHeaderDecodedListener
 *  android.graphics.ImageDecoder$OnPartialImageListener
 *  android.graphics.ImageDecoder$Source
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.util.Size
 */
package com.bumptech.glide.load.resource;

import android.graphics.ColorSpace;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;

public final class DefaultOnHeaderDecodedListener
implements ImageDecoder.OnHeaderDecodedListener {
    private static final String TAG = "ImageDecoder";
    private final DecodeFormat decodeFormat;
    private final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
    private final boolean isHardwareConfigAllowed;
    private final PreferredColorSpace preferredColorSpace;
    private final int requestedHeight;
    private final int requestedWidth;
    private final DownsampleStrategy strategy;

    public DefaultOnHeaderDecodedListener(int n, int n2, Options options) {
        this.requestedWidth = n;
        this.requestedHeight = n2;
        this.decodeFormat = options.get(Downsampler.DECODE_FORMAT);
        this.strategy = options.get(DownsampleStrategy.OPTION);
        boolean bl = options.get(Downsampler.ALLOW_HARDWARE_CONFIG) != null && options.get(Downsampler.ALLOW_HARDWARE_CONFIG) != false;
        this.isHardwareConfigAllowed = bl;
        this.preferredColorSpace = options.get(Downsampler.PREFERRED_COLOR_SPACE);
    }

    public void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source object) {
        object = this.hardwareConfigState;
        int n = this.requestedWidth;
        int n2 = this.requestedHeight;
        boolean bl = this.isHardwareConfigAllowed;
        int n3 = 0;
        if (((HardwareConfigState)object).isHardwareConfigAllowed(n, n2, bl, false)) {
            imageDecoder.setAllocator(3);
        } else {
            imageDecoder.setAllocator(1);
        }
        if (this.decodeFormat == DecodeFormat.PREFER_RGB_565) {
            imageDecoder.setMemorySizePolicy(0);
        }
        imageDecoder.setOnPartialImageListener(new ImageDecoder.OnPartialImageListener(this){
            final DefaultOnHeaderDecodedListener this$0;
            {
                this.this$0 = defaultOnHeaderDecodedListener;
            }

            public boolean onPartialImage(ImageDecoder.DecodeException decodeException) {
                return false;
            }
        });
        Size size = imageInfo.getSize();
        n = this.requestedWidth;
        if (this.requestedWidth == Integer.MIN_VALUE) {
            n = size.getWidth();
        }
        n2 = this.requestedHeight;
        if (this.requestedHeight == Integer.MIN_VALUE) {
            n2 = size.getHeight();
        }
        float f = this.strategy.getScaleFactor(size.getWidth(), size.getHeight(), n, n2);
        n = Math.round((float)size.getWidth() * f);
        n2 = Math.round((float)size.getHeight() * f);
        if (Log.isLoggable((String)TAG, (int)2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Resizing from [");
            ((StringBuilder)object).append(size.getWidth());
            ((StringBuilder)object).append("x");
            ((StringBuilder)object).append(size.getHeight());
            ((StringBuilder)object).append("] to [");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append("x");
            ((StringBuilder)object).append(n2);
            ((StringBuilder)object).append("] scaleFactor: ");
            ((StringBuilder)object).append(f);
            Log.v((String)TAG, (String)((StringBuilder)object).toString());
        }
        imageDecoder.setTargetSize(n, n2);
        if (this.preferredColorSpace != null) {
            if (Build.VERSION.SDK_INT >= 28) {
                n = this.preferredColorSpace == PreferredColorSpace.DISPLAY_P3 && imageInfo.getColorSpace() != null && imageInfo.getColorSpace().isWideGamut() ? 1 : n3;
                imageInfo = n != 0 ? ColorSpace.Named.DISPLAY_P3 : ColorSpace.Named.SRGB;
                imageDecoder.setTargetColorSpace(ColorSpace.get((ColorSpace.Named)imageInfo));
            } else if (Build.VERSION.SDK_INT >= 26) {
                imageDecoder.setTargetColorSpace(ColorSpace.get((ColorSpace.Named)ColorSpace.Named.SRGB));
            }
        }
    }
}

