/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Rect
 *  android.graphics.RectF
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcelable
 *  android.view.View
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 */
package androidx.core.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import java.util.List;
import java.util.Map;

public abstract class SharedElementCallback {
    private static final String BUNDLE_SNAPSHOT_BITMAP = "sharedElement:snapshot:bitmap";
    private static final String BUNDLE_SNAPSHOT_IMAGE_MATRIX = "sharedElement:snapshot:imageMatrix";
    private static final String BUNDLE_SNAPSHOT_IMAGE_SCALETYPE = "sharedElement:snapshot:imageScaleType";
    private static final int MAX_IMAGE_SIZE = 0x100000;
    private Matrix mTempMatrix;

    private static Bitmap createDrawableBitmap(Drawable drawable2) {
        int n = drawable2.getIntrinsicWidth();
        int n2 = drawable2.getIntrinsicHeight();
        if (n > 0 && n2 > 0) {
            float f = Math.min(1.0f, 1048576.0f / (float)(n * n2));
            if (drawable2 instanceof BitmapDrawable && f == 1.0f) {
                return ((BitmapDrawable)drawable2).getBitmap();
            }
            n = (int)((float)n * f);
            int n3 = (int)((float)n2 * f);
            Bitmap bitmap = Bitmap.createBitmap((int)n, (int)n3, (Bitmap.Config)Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            Rect rect = drawable2.getBounds();
            int n4 = rect.left;
            int n5 = rect.top;
            n2 = rect.right;
            int n6 = rect.bottom;
            drawable2.setBounds(0, 0, n, n3);
            drawable2.draw(canvas);
            drawable2.setBounds(n4, n5, n2, n6);
            return bitmap;
        }
        return null;
    }

    public Parcelable onCaptureSharedElementSnapshot(View view, Matrix matrix, RectF object) {
        Drawable drawable2;
        ImageView imageView;
        if (view instanceof ImageView) {
            imageView = (ImageView)view;
            drawable2 = imageView.getDrawable();
            Drawable drawable3 = imageView.getBackground();
            if (drawable2 != null && drawable3 == null && (drawable2 = SharedElementCallback.createDrawableBitmap(drawable2)) != null) {
                view = new Bundle();
                view.putParcelable(BUNDLE_SNAPSHOT_BITMAP, (Parcelable)drawable2);
                view.putString(BUNDLE_SNAPSHOT_IMAGE_SCALETYPE, imageView.getScaleType().toString());
                if (imageView.getScaleType() == ImageView.ScaleType.MATRIX) {
                    matrix = imageView.getImageMatrix();
                    object = new float[9];
                    matrix.getValues((float[])object);
                    view.putFloatArray(BUNDLE_SNAPSHOT_IMAGE_MATRIX, (float[])object);
                }
                return view;
            }
        }
        int n = Math.round(object.width());
        int n2 = Math.round(object.height());
        drawable2 = null;
        imageView = drawable2;
        if (n > 0) {
            imageView = drawable2;
            if (n2 > 0) {
                float f = Math.min(1.0f, 1048576.0f / (float)(n * n2));
                n = (int)((float)n * f);
                n2 = (int)((float)n2 * f);
                if (this.mTempMatrix == null) {
                    this.mTempMatrix = new Matrix();
                }
                this.mTempMatrix.set(matrix);
                this.mTempMatrix.postTranslate(-object.left, -object.top);
                this.mTempMatrix.postScale(f, f);
                imageView = Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)Bitmap.Config.ARGB_8888);
                matrix = new Canvas((Bitmap)imageView);
                matrix.concat(this.mTempMatrix);
                view.draw((Canvas)matrix);
            }
        }
        return imageView;
    }

    public View onCreateSnapshotView(Context context, Parcelable parcelable) {
        Bitmap bitmap;
        block2: {
            block1: {
                bitmap = null;
                if (!(parcelable instanceof Bundle)) break block1;
                Object object = (Bundle)parcelable;
                bitmap = (Bitmap)object.getParcelable(BUNDLE_SNAPSHOT_BITMAP);
                if (bitmap == null) {
                    return null;
                }
                parcelable = new ImageView(context);
                context = parcelable;
                parcelable.setImageBitmap(bitmap);
                parcelable.setScaleType(ImageView.ScaleType.valueOf((String)object.getString(BUNDLE_SNAPSHOT_IMAGE_SCALETYPE)));
                bitmap = context;
                if (parcelable.getScaleType() != ImageView.ScaleType.MATRIX) break block2;
                object = object.getFloatArray(BUNDLE_SNAPSHOT_IMAGE_MATRIX);
                bitmap = new Matrix();
                bitmap.setValues((float[])object);
                parcelable.setImageMatrix((Matrix)bitmap);
                bitmap = context;
                break block2;
            }
            if (!(parcelable instanceof Bitmap)) break block2;
            parcelable = (Bitmap)parcelable;
            bitmap = new ImageView(context);
            bitmap.setImageBitmap((Bitmap)parcelable);
        }
        return bitmap;
    }

    public void onMapSharedElements(List<String> list, Map<String, View> map) {
    }

    public void onRejectSharedElements(List<View> list) {
    }

    public void onSharedElementEnd(List<String> list, List<View> list2, List<View> list3) {
    }

    public void onSharedElementStart(List<String> list, List<View> list2, List<View> list3) {
    }

    public void onSharedElementsArrived(List<String> list, List<View> list2, OnSharedElementsReadyListener onSharedElementsReadyListener) {
        onSharedElementsReadyListener.onSharedElementsReady();
    }

    public static interface OnSharedElementsReadyListener {
        public void onSharedElementsReady();
    }
}

