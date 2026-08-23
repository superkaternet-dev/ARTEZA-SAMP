/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.request;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.BaseRequestOptions;

public class RequestOptions
extends BaseRequestOptions<RequestOptions> {
    private static RequestOptions centerCropOptions;
    private static RequestOptions centerInsideOptions;
    private static RequestOptions circleCropOptions;
    private static RequestOptions fitCenterOptions;
    private static RequestOptions noAnimationOptions;
    private static RequestOptions noTransformOptions;
    private static RequestOptions skipMemoryCacheFalseOptions;
    private static RequestOptions skipMemoryCacheTrueOptions;

    public static RequestOptions bitmapTransform(Transformation<Bitmap> transformation) {
        return (RequestOptions)new RequestOptions().transform(transformation);
    }

    public static RequestOptions centerCropTransform() {
        if (centerCropOptions == null) {
            centerCropOptions = (RequestOptions)((RequestOptions)new RequestOptions().centerCrop()).autoClone();
        }
        return centerCropOptions;
    }

    public static RequestOptions centerInsideTransform() {
        if (centerInsideOptions == null) {
            centerInsideOptions = (RequestOptions)((RequestOptions)new RequestOptions().centerInside()).autoClone();
        }
        return centerInsideOptions;
    }

    public static RequestOptions circleCropTransform() {
        if (circleCropOptions == null) {
            circleCropOptions = (RequestOptions)((RequestOptions)new RequestOptions().circleCrop()).autoClone();
        }
        return circleCropOptions;
    }

    public static RequestOptions decodeTypeOf(Class<?> clazz) {
        return (RequestOptions)new RequestOptions().decode(clazz);
    }

    public static RequestOptions diskCacheStrategyOf(DiskCacheStrategy diskCacheStrategy) {
        return (RequestOptions)new RequestOptions().diskCacheStrategy(diskCacheStrategy);
    }

    public static RequestOptions downsampleOf(DownsampleStrategy downsampleStrategy) {
        return (RequestOptions)new RequestOptions().downsample(downsampleStrategy);
    }

    public static RequestOptions encodeFormatOf(Bitmap.CompressFormat compressFormat) {
        return (RequestOptions)new RequestOptions().encodeFormat(compressFormat);
    }

    public static RequestOptions encodeQualityOf(int n) {
        return (RequestOptions)new RequestOptions().encodeQuality(n);
    }

    public static RequestOptions errorOf(int n) {
        return (RequestOptions)new RequestOptions().error(n);
    }

    public static RequestOptions errorOf(Drawable drawable2) {
        return (RequestOptions)new RequestOptions().error(drawable2);
    }

    public static RequestOptions fitCenterTransform() {
        if (fitCenterOptions == null) {
            fitCenterOptions = (RequestOptions)((RequestOptions)new RequestOptions().fitCenter()).autoClone();
        }
        return fitCenterOptions;
    }

    public static RequestOptions formatOf(DecodeFormat decodeFormat) {
        return (RequestOptions)new RequestOptions().format(decodeFormat);
    }

    public static RequestOptions frameOf(long l) {
        return (RequestOptions)new RequestOptions().frame(l);
    }

    public static RequestOptions noAnimation() {
        if (noAnimationOptions == null) {
            noAnimationOptions = (RequestOptions)((RequestOptions)new RequestOptions().dontAnimate()).autoClone();
        }
        return noAnimationOptions;
    }

    public static RequestOptions noTransformation() {
        if (noTransformOptions == null) {
            noTransformOptions = (RequestOptions)((RequestOptions)new RequestOptions().dontTransform()).autoClone();
        }
        return noTransformOptions;
    }

    public static <T> RequestOptions option(Option<T> option, T t) {
        return (RequestOptions)new RequestOptions().set(option, t);
    }

    public static RequestOptions overrideOf(int n) {
        return RequestOptions.overrideOf(n, n);
    }

    public static RequestOptions overrideOf(int n, int n2) {
        return (RequestOptions)new RequestOptions().override(n, n2);
    }

    public static RequestOptions placeholderOf(int n) {
        return (RequestOptions)new RequestOptions().placeholder(n);
    }

    public static RequestOptions placeholderOf(Drawable drawable2) {
        return (RequestOptions)new RequestOptions().placeholder(drawable2);
    }

    public static RequestOptions priorityOf(Priority priority) {
        return (RequestOptions)new RequestOptions().priority(priority);
    }

    public static RequestOptions signatureOf(Key key) {
        return (RequestOptions)new RequestOptions().signature(key);
    }

    public static RequestOptions sizeMultiplierOf(float f) {
        return (RequestOptions)new RequestOptions().sizeMultiplier(f);
    }

    public static RequestOptions skipMemoryCacheOf(boolean bl) {
        if (bl) {
            if (skipMemoryCacheTrueOptions == null) {
                skipMemoryCacheTrueOptions = (RequestOptions)((RequestOptions)new RequestOptions().skipMemoryCache(true)).autoClone();
            }
            return skipMemoryCacheTrueOptions;
        }
        if (skipMemoryCacheFalseOptions == null) {
            skipMemoryCacheFalseOptions = (RequestOptions)((RequestOptions)new RequestOptions().skipMemoryCache(false)).autoClone();
        }
        return skipMemoryCacheFalseOptions;
    }

    public static RequestOptions timeoutOf(int n) {
        return (RequestOptions)new RequestOptions().timeout(n);
    }
}

