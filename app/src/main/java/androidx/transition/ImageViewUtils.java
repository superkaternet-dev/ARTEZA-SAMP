/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.graphics.Matrix
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.widget.ImageView
 *  android.widget.ImageView$ScaleType
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Matrix;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import androidx.transition.MatrixUtils;
import androidx.transition.R;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ImageViewUtils {
    private static final String TAG = "ImageViewUtils";
    private static Method sAnimateTransformMethod;
    private static boolean sAnimateTransformMethodFetched;

    private ImageViewUtils() {
    }

    static void animateTransform(ImageView imageView, Matrix matrix) {
        if (Build.VERSION.SDK_INT < 21) {
            imageView.setImageMatrix(matrix);
        } else {
            ImageViewUtils.fetchAnimateTransformMethod();
            Method method = sAnimateTransformMethod;
            if (method != null) {
                try {
                    method.invoke((Object)imageView, matrix);
                }
                catch (InvocationTargetException invocationTargetException) {
                    throw new RuntimeException(invocationTargetException.getCause());
                }
                catch (IllegalAccessException illegalAccessException) {
                    // empty catch block
                }
            }
        }
    }

    private static void fetchAnimateTransformMethod() {
        if (!sAnimateTransformMethodFetched) {
            try {
                Method method;
                sAnimateTransformMethod = method = ImageView.class.getDeclaredMethod("animateTransform", Matrix.class);
                method.setAccessible(true);
            }
            catch (NoSuchMethodException noSuchMethodException) {
                Log.i((String)TAG, (String)"Failed to retrieve animateTransform method", (Throwable)noSuchMethodException);
            }
            sAnimateTransformMethodFetched = true;
        }
    }

    static void reserveEndAnimateTransform(ImageView imageView, Animator animator2) {
        if (Build.VERSION.SDK_INT < 21) {
            animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(imageView){
                final ImageView val$view;
                {
                    this.val$view = imageView;
                }

                public void onAnimationEnd(Animator animator2) {
                    ImageView.ScaleType scaleType = (ImageView.ScaleType)this.val$view.getTag(R.id.save_scale_type);
                    this.val$view.setScaleType(scaleType);
                    this.val$view.setTag(R.id.save_scale_type, null);
                    if (scaleType == ImageView.ScaleType.MATRIX) {
                        scaleType = this.val$view;
                        scaleType.setImageMatrix((Matrix)scaleType.getTag(R.id.save_image_matrix));
                        this.val$view.setTag(R.id.save_image_matrix, null);
                    }
                    animator2.removeListener((Animator.AnimatorListener)this);
                }
            });
        }
    }

    static void startAnimateTransform(ImageView imageView) {
        if (Build.VERSION.SDK_INT < 21) {
            ImageView.ScaleType scaleType = imageView.getScaleType();
            imageView.setTag(R.id.save_scale_type, (Object)scaleType);
            if (scaleType == ImageView.ScaleType.MATRIX) {
                imageView.setTag(R.id.save_image_matrix, (Object)imageView.getImageMatrix());
            } else {
                imageView.setScaleType(ImageView.ScaleType.MATRIX);
            }
            imageView.setImageMatrix(MatrixUtils.IDENTITY_MATRIX);
        }
    }
}

