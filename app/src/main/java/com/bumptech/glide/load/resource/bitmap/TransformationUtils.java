/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.BitmapShader
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Path
 *  android.graphics.Path$Direction
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.PorterDuffXfermode
 *  android.graphics.RectF
 *  android.graphics.Shader
 *  android.graphics.Shader$TileMode
 *  android.graphics.Xfermode
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class TransformationUtils {
    private static final Lock BITMAP_DRAWABLE_LOCK;
    private static final Paint CIRCLE_CROP_BITMAP_PAINT;
    private static final int CIRCLE_CROP_PAINT_FLAGS = 7;
    private static final Paint CIRCLE_CROP_SHAPE_PAINT;
    private static final Paint DEFAULT_PAINT;
    private static final Set<String> MODELS_REQUIRING_BITMAP_LOCK;
    public static final int PAINT_FLAGS = 6;
    private static final String TAG = "TransformationUtils";

    static {
        DEFAULT_PAINT = new Paint(6);
        CIRCLE_CROP_SHAPE_PAINT = new Paint(7);
        Object object = new HashSet<String>(Arrays.asList("XT1085", "XT1092", "XT1093", "XT1094", "XT1095", "XT1096", "XT1097", "XT1098", "XT1031", "XT1028", "XT937C", "XT1032", "XT1008", "XT1033", "XT1035", "XT1034", "XT939G", "XT1039", "XT1040", "XT1042", "XT1045", "XT1063", "XT1064", "XT1068", "XT1069", "XT1072", "XT1077", "XT1078", "XT1079"));
        MODELS_REQUIRING_BITMAP_LOCK = object;
        object = object.contains(Build.MODEL) ? new ReentrantLock() : new NoLock();
        BITMAP_DRAWABLE_LOCK = object;
        object = new Paint(7);
        CIRCLE_CROP_BITMAP_PAINT = object;
        object.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    private TransformationUtils() {
    }

    private static void applyMatrix(Bitmap bitmap, Bitmap bitmap2, Matrix matrix) {
        Lock lock = BITMAP_DRAWABLE_LOCK;
        lock.lock();
        try {
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawBitmap(bitmap, matrix, DEFAULT_PAINT);
            TransformationUtils.clear(canvas);
            lock.unlock();
            return;
        }
        catch (Throwable throwable) {
            BITMAP_DRAWABLE_LOCK.unlock();
            throw throwable;
        }
    }

    public static Bitmap centerCrop(BitmapPool bitmapPool, Bitmap bitmap, int n, int n2) {
        float f;
        float f2;
        float f3;
        if (bitmap.getWidth() == n && bitmap.getHeight() == n2) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        if (bitmap.getWidth() * n2 > bitmap.getHeight() * n) {
            f3 = (float)n2 / (float)bitmap.getHeight();
            f2 = ((float)n - (float)bitmap.getWidth() * f3) * 0.5f;
            f = 0.0f;
        } else {
            f3 = (float)n / (float)bitmap.getWidth();
            f2 = 0.0f;
            f = ((float)n2 - (float)bitmap.getHeight() * f3) * 0.5f;
        }
        matrix.setScale(f3, f3);
        matrix.postTranslate((float)((int)(f2 + 0.5f)), (float)((int)(0.5f + f)));
        bitmapPool = bitmapPool.get(n, n2, TransformationUtils.getNonNullConfig(bitmap));
        TransformationUtils.setAlpha(bitmap, (Bitmap)bitmapPool);
        TransformationUtils.applyMatrix(bitmap, (Bitmap)bitmapPool, matrix);
        return bitmapPool;
    }

    public static Bitmap centerInside(BitmapPool bitmapPool, Bitmap bitmap, int n, int n2) {
        if (bitmap.getWidth() <= n && bitmap.getHeight() <= n2) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"requested target size larger or equal to input, returning input");
            }
            return bitmap;
        }
        if (Log.isLoggable((String)TAG, (int)2)) {
            Log.v((String)TAG, (String)"requested target size too big for input, fit centering instead");
        }
        return TransformationUtils.fitCenter(bitmapPool, bitmap, n, n2);
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Bitmap circleCrop(BitmapPool bitmapPool, Bitmap bitmap, int n, int n2) {
        void var0_4;
        block7: {
            Paint paint;
            Canvas canvas;
            n = Math.min(n, n2);
            float f = (float)n / 2.0f;
            n2 = bitmap.getWidth();
            int n3 = bitmap.getHeight();
            float f2 = Math.max((float)n / (float)n2, (float)n / (float)n3);
            float f3 = f2 * (float)n2;
            float f4 = f2 * (float)n3;
            f2 = ((float)n - f3) / 2.0f;
            float f5 = ((float)n - f4) / 2.0f;
            RectF rectF = new RectF(f2, f5, f2 + f3, f5 + f4);
            Bitmap bitmap2 = TransformationUtils.getAlphaSafeBitmap(bitmapPool, bitmap);
            Bitmap bitmap3 = bitmapPool.get(n, n, TransformationUtils.getAlphaSafeConfig(bitmap));
            bitmap3.setHasAlpha(true);
            Lock lock = BITMAP_DRAWABLE_LOCK;
            lock.lock();
            try {
                canvas = new Canvas(bitmap3);
            }
            catch (Throwable throwable) {
                // empty catch block
                break block7;
            }
            try {
                canvas.drawCircle(f, f, f, CIRCLE_CROP_SHAPE_PAINT);
                paint = CIRCLE_CROP_BITMAP_PAINT;
            }
            catch (Throwable throwable) {
                break block7;
            }
            try {
                canvas.drawBitmap(bitmap2, null, rectF, paint);
                TransformationUtils.clear(canvas);
                lock.unlock();
                if (!bitmap2.equals(bitmap)) {
                    bitmapPool.put(bitmap2);
                }
                return bitmap3;
            }
            catch (Throwable throwable) {}
        }
        BITMAP_DRAWABLE_LOCK.unlock();
        throw var0_4;
    }

    private static void clear(Canvas canvas) {
        canvas.setBitmap(null);
    }

    public static Bitmap fitCenter(BitmapPool bitmapPool, Bitmap bitmap, int n, int n2) {
        StringBuilder stringBuilder;
        if (bitmap.getWidth() == n && bitmap.getHeight() == n2) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"requested target size matches input, returning input");
            }
            return bitmap;
        }
        float f = Math.min((float)n / (float)bitmap.getWidth(), (float)n2 / (float)bitmap.getHeight());
        int n3 = Math.round((float)bitmap.getWidth() * f);
        int n4 = Math.round((float)bitmap.getHeight() * f);
        if (bitmap.getWidth() == n3 && bitmap.getHeight() == n4) {
            if (Log.isLoggable((String)TAG, (int)2)) {
                Log.v((String)TAG, (String)"adjusted target size matches input, returning input");
            }
            return bitmap;
        }
        bitmapPool = bitmapPool.get((int)((float)bitmap.getWidth() * f), (int)((float)bitmap.getHeight() * f), TransformationUtils.getNonNullConfig(bitmap));
        TransformationUtils.setAlpha(bitmap, (Bitmap)bitmapPool);
        if (Log.isLoggable((String)TAG, (int)2)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("request: ");
            stringBuilder.append(n);
            stringBuilder.append("x");
            stringBuilder.append(n2);
            Log.v((String)TAG, (String)stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("toFit:   ");
            stringBuilder.append(bitmap.getWidth());
            stringBuilder.append("x");
            stringBuilder.append(bitmap.getHeight());
            Log.v((String)TAG, (String)stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("toReuse: ");
            stringBuilder.append(bitmapPool.getWidth());
            stringBuilder.append("x");
            stringBuilder.append(bitmapPool.getHeight());
            Log.v((String)TAG, (String)stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("minPct:   ");
            stringBuilder.append(f);
            Log.v((String)TAG, (String)stringBuilder.toString());
        }
        stringBuilder = new Matrix();
        stringBuilder.setScale(f, f);
        TransformationUtils.applyMatrix(bitmap, (Bitmap)bitmapPool, (Matrix)stringBuilder);
        return bitmapPool;
    }

    private static Bitmap getAlphaSafeBitmap(BitmapPool bitmapPool, Bitmap bitmap) {
        Bitmap.Config config = TransformationUtils.getAlphaSafeConfig(bitmap);
        if (config.equals((Object)bitmap.getConfig())) {
            return bitmap;
        }
        bitmapPool = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(), config);
        new Canvas((Bitmap)bitmapPool).drawBitmap(bitmap, 0.0f, 0.0f, null);
        return bitmapPool;
    }

    private static Bitmap.Config getAlphaSafeConfig(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 26 && Bitmap.Config.RGBA_F16.equals((Object)bitmap.getConfig())) {
            return Bitmap.Config.RGBA_F16;
        }
        return Bitmap.Config.ARGB_8888;
    }

    public static Lock getBitmapDrawableLock() {
        return BITMAP_DRAWABLE_LOCK;
    }

    public static int getExifOrientationDegrees(int n) {
        switch (n) {
            default: {
                n = 0;
                break;
            }
            case 7: 
            case 8: {
                n = 270;
                break;
            }
            case 5: 
            case 6: {
                n = 90;
                break;
            }
            case 3: 
            case 4: {
                n = 180;
            }
        }
        return n;
    }

    private static Bitmap.Config getNonNullConfig(Bitmap bitmap) {
        bitmap = bitmap.getConfig() != null ? bitmap.getConfig() : Bitmap.Config.ARGB_8888;
        return bitmap;
    }

    static void initializeMatrixForRotation(int n, Matrix matrix) {
        switch (n) {
            default: {
                break;
            }
            case 8: {
                matrix.setRotate(-90.0f);
                break;
            }
            case 7: {
                matrix.setRotate(-90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            }
            case 6: {
                matrix.setRotate(90.0f);
                break;
            }
            case 5: {
                matrix.setRotate(90.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            }
            case 4: {
                matrix.setRotate(180.0f);
                matrix.postScale(-1.0f, 1.0f);
                break;
            }
            case 3: {
                matrix.setRotate(180.0f);
                break;
            }
            case 2: {
                matrix.setScale(-1.0f, 1.0f);
            }
        }
    }

    public static boolean isExifOrientationRequired(int n) {
        switch (n) {
            default: {
                return false;
            }
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
            case 7: 
            case 8: 
        }
        return true;
    }

    public static Bitmap rotateImage(Bitmap bitmap, int n) {
        Bitmap bitmap2;
        block2: {
            Bitmap bitmap3;
            bitmap2 = bitmap3 = bitmap;
            if (n == 0) break block2;
            try {
                bitmap2 = new Matrix();
                bitmap2.setRotate((float)n);
                bitmap2 = Bitmap.createBitmap((Bitmap)bitmap, (int)0, (int)0, (int)bitmap.getWidth(), (int)bitmap.getHeight(), (Matrix)bitmap2, (boolean)true);
            }
            catch (Exception exception) {
                bitmap2 = bitmap3;
                if (!Log.isLoggable((String)TAG, (int)6)) break block2;
                Log.e((String)TAG, (String)"Exception when trying to orient image", (Throwable)exception);
                bitmap2 = bitmap3;
            }
        }
        return bitmap2;
    }

    public static Bitmap rotateImageExif(BitmapPool bitmapPool, Bitmap bitmap, int n) {
        if (!TransformationUtils.isExifOrientationRequired(n)) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        TransformationUtils.initializeMatrixForRotation(n, matrix);
        RectF rectF = new RectF(0.0f, 0.0f, (float)bitmap.getWidth(), (float)bitmap.getHeight());
        matrix.mapRect(rectF);
        bitmapPool = bitmapPool.get(Math.round(rectF.width()), Math.round(rectF.height()), TransformationUtils.getNonNullConfig(bitmap));
        matrix.postTranslate(-rectF.left, -rectF.top);
        bitmapPool.setHasAlpha(bitmap.hasAlpha());
        TransformationUtils.applyMatrix(bitmap, (Bitmap)bitmapPool, matrix);
        return bitmapPool;
    }

    public static Bitmap roundedCorners(BitmapPool bitmapPool, Bitmap bitmap, float f, float f2, float f3, float f4) {
        return TransformationUtils.roundedCorners(bitmapPool, bitmap, new DrawRoundedCornerFn(f, f2, f3, f4){
            final float val$bottomLeft;
            final float val$bottomRight;
            final float val$topLeft;
            final float val$topRight;
            {
                this.val$topLeft = f;
                this.val$topRight = f2;
                this.val$bottomRight = f3;
                this.val$bottomLeft = f4;
            }

            @Override
            public void drawRoundedCorners(Canvas canvas, Paint paint, RectF rectF) {
                Path path = new Path();
                float f = this.val$topLeft;
                float f2 = this.val$topRight;
                float f3 = this.val$bottomRight;
                float f4 = this.val$bottomLeft;
                Path.Direction direction = Path.Direction.CW;
                path.addRoundRect(rectF, new float[]{f, f, f2, f2, f3, f3, f4, f4}, direction);
                canvas.drawPath(path, paint);
            }
        });
    }

    public static Bitmap roundedCorners(BitmapPool bitmapPool, Bitmap bitmap, int n) {
        boolean bl = n > 0;
        Preconditions.checkArgument(bl, "roundingRadius must be greater than 0.");
        return TransformationUtils.roundedCorners(bitmapPool, bitmap, new DrawRoundedCornerFn(n){
            final int val$roundingRadius;
            {
                this.val$roundingRadius = n;
            }

            @Override
            public void drawRoundedCorners(Canvas canvas, Paint paint, RectF rectF) {
                int n = this.val$roundingRadius;
                canvas.drawRoundRect(rectF, (float)n, (float)n, paint);
            }
        });
    }

    @Deprecated
    public static Bitmap roundedCorners(BitmapPool bitmapPool, Bitmap bitmap, int n, int n2, int n3) {
        return TransformationUtils.roundedCorners(bitmapPool, bitmap, n3);
    }

    private static Bitmap roundedCorners(BitmapPool bitmapPool, Bitmap bitmap, DrawRoundedCornerFn drawRoundedCornerFn) {
        Bitmap.Config config = TransformationUtils.getAlphaSafeConfig(bitmap);
        Bitmap bitmap2 = TransformationUtils.getAlphaSafeBitmap(bitmapPool, bitmap);
        Bitmap bitmap3 = bitmapPool.get(bitmap2.getWidth(), bitmap2.getHeight(), config);
        bitmap3.setHasAlpha(true);
        Object object = new BitmapShader(bitmap2, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        config = new Paint();
        config.setAntiAlias(true);
        config.setShader((Shader)object);
        RectF rectF = new RectF(0.0f, 0.0f, (float)bitmap3.getWidth(), (float)bitmap3.getHeight());
        object = BITMAP_DRAWABLE_LOCK;
        object.lock();
        try {
            Canvas canvas = new Canvas(bitmap3);
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            drawRoundedCornerFn.drawRoundedCorners(canvas, (Paint)config, rectF);
            TransformationUtils.clear(canvas);
            object.unlock();
            if (!bitmap2.equals(bitmap)) {
                bitmapPool.put(bitmap2);
            }
            return bitmap3;
        }
        catch (Throwable throwable) {
            BITMAP_DRAWABLE_LOCK.unlock();
            throw throwable;
        }
    }

    public static void setAlpha(Bitmap bitmap, Bitmap bitmap2) {
        bitmap2.setHasAlpha(bitmap.hasAlpha());
    }

    private static interface DrawRoundedCornerFn {
        public void drawRoundedCorners(Canvas var1, Paint var2, RectF var3);
    }

    private static final class NoLock
    implements Lock {
        NoLock() {
        }

        @Override
        public void lock() {
        }

        @Override
        public void lockInterruptibly() throws InterruptedException {
        }

        @Override
        public Condition newCondition() {
            throw new UnsupportedOperationException("Should not be called");
        }

        @Override
        public boolean tryLock() {
            return true;
        }

        @Override
        public boolean tryLock(long l, TimeUnit timeUnit) throws InterruptedException {
            return true;
        }

        @Override
        public void unlock() {
        }
    }
}

