/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapFactory$Options
 *  android.graphics.ColorSpace
 *  android.graphics.ColorSpace$Named
 *  android.os.Build$VERSION
 *  android.os.ParcelFileDescriptor
 *  android.util.DisplayMetrics
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.DisplayMetrics;
import android.util.Log;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.PreferredColorSpace;
import com.bumptech.glide.load.data.ParcelFileDescriptorRewinder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.load.resource.bitmap.HardwareConfigState;
import com.bumptech.glide.load.resource.bitmap.ImageReader;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public final class Downsampler {
    public static final Option<Boolean> ALLOW_HARDWARE_CONFIG;
    public static final Option<DecodeFormat> DECODE_FORMAT;
    @Deprecated
    public static final Option<DownsampleStrategy> DOWNSAMPLE_STRATEGY;
    private static final DecodeCallbacks EMPTY_CALLBACKS;
    public static final Option<Boolean> FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS;
    private static final String ICO_MIME_TYPE = "image/x-ico";
    private static final Set<String> NO_DOWNSAMPLE_PRE_N_MIME_TYPES;
    private static final Queue<BitmapFactory.Options> OPTIONS_QUEUE;
    public static final Option<PreferredColorSpace> PREFERRED_COLOR_SPACE;
    static final String TAG = "Downsampler";
    private static final Set<ImageHeaderParser.ImageType> TYPES_THAT_USE_POOL_PRE_KITKAT;
    private static final String WBMP_MIME_TYPE = "image/vnd.wap.wbmp";
    private final BitmapPool bitmapPool;
    private final ArrayPool byteArrayPool;
    private final DisplayMetrics displayMetrics;
    private final HardwareConfigState hardwareConfigState = HardwareConfigState.getInstance();
    private final List<ImageHeaderParser> parsers;

    static {
        DECODE_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.DecodeFormat", DecodeFormat.DEFAULT);
        PREFERRED_COLOR_SPACE = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.PreferredColorSpace");
        DOWNSAMPLE_STRATEGY = DownsampleStrategy.OPTION;
        Boolean bl = false;
        FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.FixBitmapSize", bl);
        ALLOW_HARDWARE_CONFIG = Option.memory("com.bumptech.glide.load.resource.bitmap.Downsampler.AllowHardwareDecode", bl);
        NO_DOWNSAMPLE_PRE_N_MIME_TYPES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(WBMP_MIME_TYPE, ICO_MIME_TYPE)));
        EMPTY_CALLBACKS = new DecodeCallbacks(){

            @Override
            public void onDecodeComplete(BitmapPool bitmapPool, Bitmap bitmap) {
            }

            @Override
            public void onObtainBounds() {
            }
        };
        TYPES_THAT_USE_POOL_PRE_KITKAT = Collections.unmodifiableSet(EnumSet.of(ImageHeaderParser.ImageType.JPEG, ImageHeaderParser.ImageType.PNG_A, ImageHeaderParser.ImageType.PNG));
        OPTIONS_QUEUE = Util.createQueue(0);
    }

    public Downsampler(List<ImageHeaderParser> list, DisplayMetrics displayMetrics, BitmapPool bitmapPool, ArrayPool arrayPool) {
        this.parsers = list;
        this.displayMetrics = Preconditions.checkNotNull(displayMetrics);
        this.bitmapPool = Preconditions.checkNotNull(bitmapPool);
        this.byteArrayPool = Preconditions.checkNotNull(arrayPool);
    }

    private static int adjustTargetDensityForError(double d) {
        int n = Downsampler.getDensityMultiplier(d);
        double d2 = n;
        Double.isNaN(d2);
        int n2 = Downsampler.round(d2 * d);
        d2 = (float)n2 / (float)n;
        Double.isNaN(d2);
        d2 = d / d2;
        d = n2;
        Double.isNaN(d);
        return Downsampler.round(d * d2);
    }

    private void calculateConfig(ImageReader imageReader, DecodeFormat decodeFormat, boolean bl, boolean bl2, BitmapFactory.Options options, int n, int n2) {
        if (this.hardwareConfigState.setHardwareConfigIfAllowed(n, n2, options, bl, bl2)) {
            return;
        }
        if (decodeFormat != DecodeFormat.PREFER_ARGB_8888 && Build.VERSION.SDK_INT != 16) {
            block5: {
                bl2 = false;
                try {
                    bl = imageReader.getImageType().hasAlpha();
                }
                catch (IOException iOException) {
                    bl = bl2;
                    if (!Log.isLoggable((String)TAG, (int)3)) break block5;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Cannot determine whether the image has alpha or not from header, format ");
                    stringBuilder.append((Object)decodeFormat);
                    Log.d((String)TAG, (String)stringBuilder.toString(), (Throwable)iOException);
                    bl = bl2;
                }
            }
            imageReader = bl ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            options.inPreferredConfig = imageReader;
            if (options.inPreferredConfig == Bitmap.Config.RGB_565) {
                options.inDither = true;
            }
            return;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    }

    private static void calculateScaling(ImageHeaderParser.ImageType object, ImageReader object2, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool, DownsampleStrategy downsampleStrategy, int n, int n2, int n3, int n4, int n5, BitmapFactory.Options options) throws IOException {
        block23: {
            if (n2 > 0 && n3 > 0) {
                float f;
                Object object3 = n2;
                Object object4 = n3;
                if (Downsampler.isRotationRequired(n)) {
                    object3 = n3;
                    object4 = n2;
                }
                if (!((f = downsampleStrategy.getScaleFactor((int)object3, (int)object4, n4, n5)) <= 0.0f)) {
                    DownsampleStrategy.SampleSizeRounding sampleSizeRounding = downsampleStrategy.getSampleSizeRounding((int)object3, (int)object4, n4, n5);
                    if (sampleSizeRounding != null) {
                        int n6 = Downsampler.round((float)object3 * f);
                        int n7 = Downsampler.round((float)object4 * f);
                        n6 = object3 / n6;
                        n7 = object4 / n7;
                        n6 = sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY ? Math.max(n6, n7) : Math.min(n6, n7);
                        if (Build.VERSION.SDK_INT <= 23 && NO_DOWNSAMPLE_PRE_N_MIME_TYPES.contains(options.outMimeType)) {
                            n6 = 1;
                        } else {
                            n6 = n7 = Math.max(1, Integer.highestOneBit(n6));
                            if (sampleSizeRounding == DownsampleStrategy.SampleSizeRounding.MEMORY) {
                                n6 = n7;
                                if ((float)n7 < 1.0f / f) {
                                    n6 = n7 << 1;
                                }
                            }
                        }
                        options.inSampleSize = n6;
                        if (object == ImageHeaderParser.ImageType.JPEG) {
                            Object object5 = Math.min(n6, 8);
                            n7 = (int)Math.ceil((float)object3 / (float)object5);
                            object5 = (int)Math.ceil((float)object4 / (float)object5);
                            int n8 = n6 / 8;
                            object4 = n7;
                            object3 = object5;
                            if (n8 > 0) {
                                object4 = n7 / n8;
                                object3 = object5 / n8;
                            }
                        } else if (object != ImageHeaderParser.ImageType.PNG && object != ImageHeaderParser.ImageType.PNG_A) {
                            if (((ImageHeaderParser.ImageType)((Object)object)).isWebp()) {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    object3 = Math.round((float)object3 / (float)n6);
                                    n7 = Math.round((float)object4 / (float)n6);
                                    object4 = object3;
                                    object3 = n7;
                                } else {
                                    n7 = (int)Math.floor((float)object3 / (float)n6);
                                    object3 = (int)Math.floor((float)object4 / (float)n6);
                                    object4 = n7;
                                }
                            } else if (object3 % n6 == 0 && object4 % n6 == 0) {
                                n7 = object3 / n6;
                                object3 = object4 / n6;
                                object4 = n7;
                            } else {
                                object = Downsampler.getDimensions((ImageReader)object2, options, decodeCallbacks, bitmapPool);
                                object4 = (Object)object[0];
                                object3 = (Object)object[1];
                            }
                        } else {
                            object3 = (int)Math.floor((float)object3 / (float)n6);
                            n7 = (int)Math.floor((float)object4 / (float)n6);
                            object4 = object3;
                            object3 = n7;
                        }
                        double d = downsampleStrategy.getScaleFactor((int)object4, (int)object3, n4, n5);
                        if (Build.VERSION.SDK_INT >= 19) {
                            options.inTargetDensity = Downsampler.adjustTargetDensityForError(d);
                            options.inDensity = Downsampler.getDensityMultiplier(d);
                        }
                        if (Downsampler.isScaling(options)) {
                            options.inScaled = true;
                        } else {
                            options.inTargetDensity = 0;
                            options.inDensity = 0;
                        }
                        if (Log.isLoggable((String)TAG, (int)2)) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Calculate scaling, source: [");
                            ((StringBuilder)object).append(n2);
                            ((StringBuilder)object).append("x");
                            ((StringBuilder)object).append(n3);
                            ((StringBuilder)object).append("], degreesToRotate: ");
                            ((StringBuilder)object).append(n);
                            ((StringBuilder)object).append(", target: [");
                            ((StringBuilder)object).append(n4);
                            ((StringBuilder)object).append("x");
                            ((StringBuilder)object).append(n5);
                            ((StringBuilder)object).append("], power of two scaled: [");
                            ((StringBuilder)object).append((int)object4);
                            ((StringBuilder)object).append("x");
                            ((StringBuilder)object).append((int)object3);
                            ((StringBuilder)object).append("], exact scale factor: ");
                            ((StringBuilder)object).append(f);
                            ((StringBuilder)object).append(", power of 2 sample size: ");
                            ((StringBuilder)object).append(n6);
                            ((StringBuilder)object).append(", adjusted scale factor: ");
                            ((StringBuilder)object).append(d);
                            ((StringBuilder)object).append(", target density: ");
                            ((StringBuilder)object).append(options.inTargetDensity);
                            ((StringBuilder)object).append(", density: ");
                            ((StringBuilder)object).append(options.inDensity);
                            Log.v((String)TAG, (String)((StringBuilder)object).toString());
                        }
                        return;
                    }
                    throw new IllegalArgumentException("Cannot round with null rounding");
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Cannot scale with factor: ");
                ((StringBuilder)object).append(f);
                ((StringBuilder)object).append(" from: ");
                ((StringBuilder)object).append(downsampleStrategy);
                ((StringBuilder)object).append(", source: [");
                ((StringBuilder)object).append(n2);
                ((StringBuilder)object).append("x");
                ((StringBuilder)object).append(n3);
                ((StringBuilder)object).append("], target: [");
                ((StringBuilder)object).append(n4);
                ((StringBuilder)object).append("x");
                ((StringBuilder)object).append(n5);
                ((StringBuilder)object).append("]");
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            if (!Log.isLoggable((String)TAG, (int)3)) break block23;
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("Unable to determine dimensions for: ");
            ((StringBuilder)object2).append(object);
            ((StringBuilder)object2).append(" with target [");
            ((StringBuilder)object2).append(n4);
            ((StringBuilder)object2).append("x");
            ((StringBuilder)object2).append(n5);
            ((StringBuilder)object2).append("]");
            Log.d((String)TAG, (String)((StringBuilder)object2).toString());
        }
    }

    private Resource<Bitmap> decode(ImageReader object, int n, int n2, Options options, DecodeCallbacks decodeCallbacks) throws IOException {
        byte[] byArray = this.byteArrayPool.get(65536, byte[].class);
        BitmapFactory.Options options2 = Downsampler.getDefaultOptions();
        options2.inTempStorage = byArray;
        DecodeFormat decodeFormat = options.get(DECODE_FORMAT);
        PreferredColorSpace preferredColorSpace = options.get(PREFERRED_COLOR_SPACE);
        DownsampleStrategy downsampleStrategy = options.get(DownsampleStrategy.OPTION);
        boolean bl = options.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS);
        Option<Boolean> option = ALLOW_HARDWARE_CONFIG;
        boolean bl2 = options.get(option) != null && options.get(option) != false;
        try {
            object = BitmapResource.obtain(this.decodeFromWrappedStreams((ImageReader)object, options2, downsampleStrategy, decodeFormat, preferredColorSpace, bl2, n, n2, bl, decodeCallbacks), this.bitmapPool);
            return object;
        }
        finally {
            Downsampler.releaseOptions(options2);
            this.byteArrayPool.put(byArray);
        }
    }

    private Bitmap decodeFromWrappedStreams(ImageReader imageReader, BitmapFactory.Options options, DownsampleStrategy object, DecodeFormat decodeFormat, PreferredColorSpace preferredColorSpace, boolean bl, int n, int n2, boolean bl2, DecodeCallbacks decodeCallbacks) throws IOException {
        long l = LogTime.getLogTime();
        Object object2 = Downsampler.getDimensions(imageReader, options, decodeCallbacks, this.bitmapPool);
        int n3 = 0;
        int n4 = object2[0];
        int n5 = object2[1];
        object2 = options.outMimeType;
        if (n4 == -1 || n5 == -1) {
            bl = false;
        }
        int n6 = imageReader.getImageOrientation();
        int n7 = TransformationUtils.getExifOrientationDegrees(n6);
        boolean bl3 = TransformationUtils.isExifOrientationRequired(n6);
        int n8 = n == Integer.MIN_VALUE ? (Downsampler.isRotationRequired(n7) ? n5 : n4) : n;
        int n9 = n2 == Integer.MIN_VALUE ? (Downsampler.isRotationRequired(n7) ? n4 : n5) : n2;
        ImageHeaderParser.ImageType imageType = imageReader.getImageType();
        Downsampler.calculateScaling(imageType, imageReader, decodeCallbacks, this.bitmapPool, (DownsampleStrategy)object, n7, n4, n5, n8, n9, options);
        this.calculateConfig(imageReader, decodeFormat, bl, bl3, options, n8, n9);
        n7 = Build.VERSION.SDK_INT >= 19 ? 1 : 0;
        if (options.inSampleSize == 1 || n7 != 0) {
            if (this.shouldUsePool(imageType)) {
                if (n4 < 0 || n5 < 0 || !bl2 || n7 == 0) {
                    float f = Downsampler.isScaling(options) ? (float)options.inTargetDensity / (float)options.inDensity : 1.0f;
                    n7 = options.inSampleSize;
                    n8 = (int)Math.ceil((float)n4 / (float)n7);
                    n9 = (int)Math.ceil((float)n5 / (float)n7);
                    n8 = Math.round((float)n8 * f);
                    n9 = Math.round((float)n9 * f);
                    if (Log.isLoggable((String)TAG, (int)2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Calculated target [");
                        ((StringBuilder)object).append(n8);
                        ((StringBuilder)object).append("x");
                        ((StringBuilder)object).append(n9);
                        ((StringBuilder)object).append("] for source [");
                        ((StringBuilder)object).append(n4);
                        ((StringBuilder)object).append("x");
                        ((StringBuilder)object).append(n5);
                        ((StringBuilder)object).append("], sampleSize: ");
                        ((StringBuilder)object).append(n7);
                        ((StringBuilder)object).append(", targetDensity: ");
                        ((StringBuilder)object).append(options.inTargetDensity);
                        ((StringBuilder)object).append(", density: ");
                        ((StringBuilder)object).append(options.inDensity);
                        ((StringBuilder)object).append(", density multiplier: ");
                        ((StringBuilder)object).append(f);
                        Log.v((String)TAG, (String)((StringBuilder)object).toString());
                    }
                }
                if (n8 > 0 && n9 > 0) {
                    Downsampler.setInBitmap(options, this.bitmapPool, n8, n9);
                }
            }
        }
        if (preferredColorSpace != null) {
            if (Build.VERSION.SDK_INT >= 28) {
                n8 = preferredColorSpace == PreferredColorSpace.DISPLAY_P3 && options.outColorSpace != null && options.outColorSpace.isWideGamut() ? 1 : n3;
                object = n8 != 0 ? ColorSpace.Named.DISPLAY_P3 : ColorSpace.Named.SRGB;
                options.inPreferredColorSpace = ColorSpace.get((ColorSpace.Named)object);
            } else if (Build.VERSION.SDK_INT >= 26) {
                options.inPreferredColorSpace = ColorSpace.get((ColorSpace.Named)ColorSpace.Named.SRGB);
            }
        }
        object = Downsampler.decodeStream(imageReader, options, decodeCallbacks, this.bitmapPool);
        decodeCallbacks.onDecodeComplete(this.bitmapPool, (Bitmap)object);
        if (Log.isLoggable((String)TAG, (int)2)) {
            Downsampler.logDecode(n4, n5, (String)object2, options, (Bitmap)object, n, n2, l);
        }
        imageReader = null;
        if (object != null) {
            object.setDensity(this.displayMetrics.densityDpi);
            options = TransformationUtils.rotateImageExif(this.bitmapPool, (Bitmap)object, n6);
            imageReader = options;
            if (!object.equals(options)) {
                this.bitmapPool.put((Bitmap)object);
                imageReader = options;
            }
        }
        return imageReader;
    }

    /*
     * Exception decompiling
     */
    private static Bitmap decodeStream(ImageReader var0, BitmapFactory.Options var1_3, DecodeCallbacks var2_4, BitmapPool var3_5) throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 1[TRYBLOCK] [2 : 71->111)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    private static String getBitmapString(Bitmap bitmap) {
        CharSequence charSequence;
        if (bitmap == null) {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 19) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(" (");
            ((StringBuilder)charSequence).append(bitmap.getAllocationByteCount());
            ((StringBuilder)charSequence).append(")");
            charSequence = ((StringBuilder)charSequence).toString();
        } else {
            charSequence = "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(bitmap.getWidth());
        stringBuilder.append("x");
        stringBuilder.append(bitmap.getHeight());
        stringBuilder.append("] ");
        stringBuilder.append(bitmap.getConfig());
        stringBuilder.append((String)charSequence);
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static BitmapFactory.Options getDefaultOptions() {
        synchronized (Downsampler.class) {
            BitmapFactory.Options options;
            BitmapFactory.Options options2 = OPTIONS_QUEUE;
            synchronized (options2) {
                options = options2.poll();
            }
            options2 = options;
            if (options == null) {
                options2 = new BitmapFactory.Options();
                Downsampler.resetOptions(options2);
            }
            return options2;
        }
    }

    private static int getDensityMultiplier(double d) {
        if (!(d <= 1.0)) {
            d = 1.0 / d;
        }
        return (int)Math.round(d * 2.147483647E9);
    }

    private static int[] getDimensions(ImageReader imageReader, BitmapFactory.Options options, DecodeCallbacks decodeCallbacks, BitmapPool bitmapPool) throws IOException {
        options.inJustDecodeBounds = true;
        Downsampler.decodeStream(imageReader, options, decodeCallbacks, bitmapPool);
        options.inJustDecodeBounds = false;
        return new int[]{options.outWidth, options.outHeight};
    }

    private static String getInBitmapString(BitmapFactory.Options options) {
        return Downsampler.getBitmapString(options.inBitmap);
    }

    private static boolean isRotationRequired(int n) {
        boolean bl = n == 90 || n == 270;
        return bl;
    }

    private static boolean isScaling(BitmapFactory.Options options) {
        boolean bl = options.inTargetDensity > 0 && options.inDensity > 0 && options.inTargetDensity != options.inDensity;
        return bl;
    }

    private static void logDecode(int n, int n2, String string2, BitmapFactory.Options options, Bitmap bitmap, int n3, int n4, long l) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Decoded ");
        stringBuilder.append(Downsampler.getBitmapString(bitmap));
        stringBuilder.append(" from [");
        stringBuilder.append(n);
        stringBuilder.append("x");
        stringBuilder.append(n2);
        stringBuilder.append("] ");
        stringBuilder.append(string2);
        stringBuilder.append(" with inBitmap ");
        stringBuilder.append(Downsampler.getInBitmapString(options));
        stringBuilder.append(" for [");
        stringBuilder.append(n3);
        stringBuilder.append("x");
        stringBuilder.append(n4);
        stringBuilder.append("], sample size: ");
        stringBuilder.append(options.inSampleSize);
        stringBuilder.append(", density: ");
        stringBuilder.append(options.inDensity);
        stringBuilder.append(", target density: ");
        stringBuilder.append(options.inTargetDensity);
        stringBuilder.append(", thread: ");
        stringBuilder.append(Thread.currentThread().getName());
        stringBuilder.append(", duration: ");
        stringBuilder.append(LogTime.getElapsedMillis(l));
        Log.v((String)TAG, (String)stringBuilder.toString());
    }

    private static IOException newIoExceptionForInBitmapAssertion(IllegalArgumentException illegalArgumentException, int n, int n2, String string2, BitmapFactory.Options options) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Exception decoding bitmap, outWidth: ");
        stringBuilder.append(n);
        stringBuilder.append(", outHeight: ");
        stringBuilder.append(n2);
        stringBuilder.append(", outMimeType: ");
        stringBuilder.append(string2);
        stringBuilder.append(", inBitmap: ");
        stringBuilder.append(Downsampler.getInBitmapString(options));
        return new IOException(stringBuilder.toString(), illegalArgumentException);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void releaseOptions(BitmapFactory.Options options) {
        Downsampler.resetOptions(options);
        Queue<BitmapFactory.Options> queue = OPTIONS_QUEUE;
        synchronized (queue) {
            queue.offer(options);
            return;
        }
    }

    private static void resetOptions(BitmapFactory.Options options) {
        options.inTempStorage = null;
        options.inDither = false;
        options.inScaled = false;
        options.inSampleSize = 1;
        options.inPreferredConfig = null;
        options.inJustDecodeBounds = false;
        options.inDensity = 0;
        options.inTargetDensity = 0;
        if (Build.VERSION.SDK_INT >= 26) {
            options.inPreferredColorSpace = null;
            options.outColorSpace = null;
            options.outConfig = null;
        }
        options.outWidth = 0;
        options.outHeight = 0;
        options.outMimeType = null;
        options.inBitmap = null;
        options.inMutable = true;
    }

    private static int round(double d) {
        return (int)(0.5 + d);
    }

    private static void setInBitmap(BitmapFactory.Options options, BitmapPool bitmapPool, int n, int n2) {
        Bitmap.Config config = null;
        if (Build.VERSION.SDK_INT >= 26) {
            if (options.inPreferredConfig == Bitmap.Config.HARDWARE) {
                return;
            }
            config = options.outConfig;
        }
        Bitmap.Config config2 = config;
        if (config == null) {
            config2 = options.inPreferredConfig;
        }
        options.inBitmap = bitmapPool.getDirty(n, n2, config2);
    }

    private boolean shouldUsePool(ImageHeaderParser.ImageType imageType) {
        if (Build.VERSION.SDK_INT >= 19) {
            return true;
        }
        return TYPES_THAT_USE_POOL_PRE_KITKAT.contains((Object)imageType);
    }

    public Resource<Bitmap> decode(ParcelFileDescriptor parcelFileDescriptor, int n, int n2, Options options) throws IOException {
        return this.decode(new ImageReader.ParcelFileDescriptorImageReader(parcelFileDescriptor, this.parsers, this.byteArrayPool), n, n2, options, EMPTY_CALLBACKS);
    }

    public Resource<Bitmap> decode(InputStream inputStream, int n, int n2, Options options) throws IOException {
        return this.decode(inputStream, n, n2, options, EMPTY_CALLBACKS);
    }

    public Resource<Bitmap> decode(InputStream inputStream, int n, int n2, Options options, DecodeCallbacks decodeCallbacks) throws IOException {
        return this.decode(new ImageReader.InputStreamImageReader(inputStream, this.parsers, this.byteArrayPool), n, n2, options, decodeCallbacks);
    }

    public Resource<Bitmap> decode(ByteBuffer byteBuffer, int n, int n2, Options options) throws IOException {
        return this.decode(new ImageReader.ByteBufferReader(byteBuffer, this.parsers, this.byteArrayPool), n, n2, options, EMPTY_CALLBACKS);
    }

    void decode(File file, int n, int n2, Options options) throws IOException {
        this.decode(new ImageReader.FileReader(file, this.parsers, this.byteArrayPool), n, n2, options, EMPTY_CALLBACKS);
    }

    void decode(byte[] byArray, int n, int n2, Options options) throws IOException {
        this.decode(new ImageReader.ByteArrayReader(byArray, this.parsers, this.byteArrayPool), n, n2, options, EMPTY_CALLBACKS);
    }

    public boolean handles(ParcelFileDescriptor parcelFileDescriptor) {
        return ParcelFileDescriptorRewinder.isSupported();
    }

    public boolean handles(InputStream inputStream) {
        return true;
    }

    public boolean handles(ByteBuffer byteBuffer) {
        return true;
    }

    public static interface DecodeCallbacks {
        public void onDecodeComplete(BitmapPool var1, Bitmap var2) throws IOException;

        public void onObtainBounds();
    }
}

