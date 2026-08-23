/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.view.View
 *  android.view.ViewParent
 */
package androidx.transition;

import android.graphics.Matrix;
import android.view.View;
import android.view.ViewParent;
import androidx.transition.R;

class ViewUtilsBase {
    private float[] mMatrixValues;

    ViewUtilsBase() {
    }

    public void clearNonTransitionAlpha(View view) {
        if (view.getVisibility() == 0) {
            view.setTag(R.id.save_non_transition_alpha, null);
        }
    }

    public float getTransitionAlpha(View view) {
        Float f = (Float)view.getTag(R.id.save_non_transition_alpha);
        if (f != null) {
            return view.getAlpha() / f.floatValue();
        }
        return view.getAlpha();
    }

    public void saveNonTransitionAlpha(View view) {
        if (view.getTag(R.id.save_non_transition_alpha) == null) {
            view.setTag(R.id.save_non_transition_alpha, (Object)Float.valueOf(view.getAlpha()));
        }
    }

    public void setAnimationMatrix(View view, Matrix matrix) {
        if (matrix != null && !matrix.isIdentity()) {
            float[] fArray;
            float[] fArray2 = fArray = this.mMatrixValues;
            if (fArray == null) {
                fArray2 = fArray = new float[9];
                this.mMatrixValues = fArray;
            }
            matrix.getValues(fArray2);
            float f = fArray2[3];
            float f2 = (float)Math.sqrt(1.0f - f * f);
            int n = fArray2[0] < 0.0f ? -1 : 1;
            float f3 = f2 * (float)n;
            f2 = (float)Math.toDegrees(Math.atan2(f, f3));
            f = fArray2[0] / f3;
            f3 = fArray2[4] / f3;
            float f4 = fArray2[2];
            float f5 = fArray2[5];
            view.setPivotX(0.0f);
            view.setPivotY(0.0f);
            view.setTranslationX(f4);
            view.setTranslationY(f5);
            view.setRotation(f2);
            view.setScaleX(f);
            view.setScaleY(f3);
        } else {
            view.setPivotX((float)(view.getWidth() / 2));
            view.setPivotY((float)(view.getHeight() / 2));
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
            view.setRotation(0.0f);
        }
    }

    public void setLeftTopRightBottom(View view, int n, int n2, int n3, int n4) {
        view.setLeft(n);
        view.setTop(n2);
        view.setRight(n3);
        view.setBottom(n4);
    }

    public void setTransitionAlpha(View view, float f) {
        Float f2 = (Float)view.getTag(R.id.save_non_transition_alpha);
        if (f2 != null) {
            view.setAlpha(f2.floatValue() * f);
        } else {
            view.setAlpha(f);
        }
    }

    public void transformMatrixToGlobal(View view, Matrix matrix) {
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof View) {
            viewParent = (View)viewParent;
            this.transformMatrixToGlobal((View)viewParent, matrix);
            matrix.preTranslate((float)(-viewParent.getScrollX()), (float)(-viewParent.getScrollY()));
        }
        matrix.preTranslate((float)view.getLeft(), (float)view.getTop());
        view = view.getMatrix();
        if (!view.isIdentity()) {
            matrix.preConcat((Matrix)view);
        }
    }

    public void transformMatrixToLocal(View view, Matrix matrix) {
        ViewParent viewParent = view.getParent();
        if (viewParent instanceof View) {
            viewParent = (View)viewParent;
            this.transformMatrixToLocal((View)viewParent, matrix);
            matrix.postTranslate((float)viewParent.getScrollX(), (float)viewParent.getScrollY());
        }
        matrix.postTranslate((float)view.getLeft(), (float)view.getTop());
        view = view.getMatrix();
        if (!view.isIdentity() && view.invert((Matrix)(viewParent = new Matrix()))) {
            matrix.postConcat((Matrix)viewParent);
        }
    }
}

