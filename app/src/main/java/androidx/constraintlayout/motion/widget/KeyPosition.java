/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.RectF
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseIntArray
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.widget.KeyPositionBase;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;

public class KeyPosition
extends KeyPositionBase {
    static final int KEY_TYPE = 2;
    static final String NAME = "KeyPosition";
    private static final String PERCENT_X = "percentX";
    private static final String PERCENT_Y = "percentY";
    private static final String TAG = "KeyPosition";
    public static final int TYPE_CARTESIAN = 0;
    public static final int TYPE_PATH = 1;
    public static final int TYPE_SCREEN = 2;
    float mAltPercentX;
    float mAltPercentY;
    private float mCalculatedPositionX;
    private float mCalculatedPositionY;
    int mDrawPath = 0;
    int mPathMotionArc = UNSET;
    float mPercentHeight;
    float mPercentWidth = Float.NaN;
    float mPercentX;
    float mPercentY;
    int mPositionType = 0;
    String mTransitionEasing = null;

    public KeyPosition() {
        this.mPercentHeight = Float.NaN;
        this.mPercentX = Float.NaN;
        this.mPercentY = Float.NaN;
        this.mAltPercentX = Float.NaN;
        this.mAltPercentY = Float.NaN;
        this.mCalculatedPositionX = Float.NaN;
        this.mCalculatedPositionY = Float.NaN;
        this.mType = 2;
    }

    private void calcCartesianPosition(float f, float f2, float f3, float f4) {
        float f5 = f3 - f;
        float f6 = f4 - f2;
        boolean bl = Float.isNaN(this.mPercentX);
        float f7 = 0.0f;
        f3 = bl ? 0.0f : this.mPercentX;
        f4 = Float.isNaN(this.mAltPercentY) ? 0.0f : this.mAltPercentY;
        float f8 = Float.isNaN(this.mPercentY) ? 0.0f : this.mPercentY;
        if (!Float.isNaN(this.mAltPercentX)) {
            f7 = this.mAltPercentX;
        }
        this.mCalculatedPositionX = (int)(f5 * f3 + f + f6 * f7);
        this.mCalculatedPositionY = (int)(f5 * f4 + f2 + f6 * f8);
    }

    private void calcPathPosition(float f, float f2, float f3, float f4) {
        float f5 = f4 - f2;
        f4 = -f5;
        float f6 = this.mPercentX;
        float f7 = this.mPercentY;
        this.mCalculatedPositionX = (f3 -= f) * f6 + f + f4 * f7;
        this.mCalculatedPositionY = f6 * f5 + f2 + f7 * f3;
    }

    private void calcScreenPosition(int n, int n2) {
        float f = n - 0;
        float f2 = this.mPercentX;
        this.mCalculatedPositionX = f * f2 + (float)(0 / 2);
        this.mCalculatedPositionY = (float)(n2 - 0) * f2 + (float)(0 / 2);
    }

    @Override
    public void addValues(HashMap<String, SplineSet> hashMap) {
    }

    @Override
    void calcPosition(int n, int n2, float f, float f2, float f3, float f4) {
        switch (this.mPositionType) {
            default: {
                this.calcCartesianPosition(f, f2, f3, f4);
                return;
            }
            case 2: {
                this.calcScreenPosition(n, n2);
                return;
            }
            case 1: 
        }
        this.calcPathPosition(f, f2, f3, f4);
    }

    @Override
    float getPositionX() {
        return this.mCalculatedPositionX;
    }

    @Override
    float getPositionY() {
        return this.mCalculatedPositionY;
    }

    @Override
    public boolean intersects(int n, int n2, RectF rectF, RectF rectF2, float f, float f2) {
        this.calcPosition(n, n2, rectF.centerX(), rectF.centerY(), rectF2.centerX(), rectF2.centerY());
        return Math.abs(f - this.mCalculatedPositionX) < 20.0f && Math.abs(f2 - this.mCalculatedPositionY) < 20.0f;
    }

    @Override
    public void load(Context context, AttributeSet attributeSet) {
        Loader.read(this, context.obtainStyledAttributes(attributeSet, R.styleable.KeyPosition));
    }

    @Override
    public void positionAttributes(View view, RectF rectF, RectF rectF2, float f, float f2, String[] stringArray, float[] fArray) {
        switch (this.mPositionType) {
            default: {
                this.positionCartAttributes(rectF, rectF2, f, f2, stringArray, fArray);
                return;
            }
            case 2: {
                this.positionScreenAttributes(view, rectF, rectF2, f, f2, stringArray, fArray);
                return;
            }
            case 1: 
        }
        this.positionPathAttributes(rectF, rectF2, f, f2, stringArray, fArray);
    }

    void positionCartAttributes(RectF rectF, RectF rectF2, float f, float f2, String[] stringArray, float[] fArray) {
        float f3 = rectF.centerX();
        float f4 = rectF.centerY();
        float f5 = rectF2.centerX();
        float f6 = rectF2.centerY();
        f5 -= f3;
        f6 -= f4;
        if (stringArray[0] != null) {
            if (PERCENT_X.equals(stringArray[0])) {
                fArray[0] = (f - f3) / f5;
                fArray[1] = (f2 - f4) / f6;
            } else {
                fArray[1] = (f - f3) / f5;
                fArray[0] = (f2 - f4) / f6;
            }
        } else {
            stringArray[0] = PERCENT_X;
            fArray[0] = (f - f3) / f5;
            stringArray[1] = PERCENT_Y;
            fArray[1] = (f2 - f4) / f6;
        }
    }

    void positionPathAttributes(RectF rectF, RectF rectF2, float f, float f2, String[] stringArray, float[] fArray) {
        float f3 = rectF.centerX();
        float f4 = rectF.centerY();
        float f5 = rectF2.centerX();
        float f6 = rectF2.centerY();
        float f7 = f5 - f3;
        if ((double)(f6 = (float)Math.hypot(f7, f5 = f6 - f4)) < 1.0E-4) {
            System.out.println("distance ~ 0");
            fArray[0] = 0.0f;
            fArray[1] = 0.0f;
            return;
        }
        float f8 = f5 / f6;
        f5 = ((f2 - f4) * (f7 /= f6) - (f - f3) * f8) / f6;
        f = ((f - f3) * f7 + (f2 - f4) * f8) / f6;
        if (stringArray[0] != null) {
            if (PERCENT_X.equals(stringArray[0])) {
                fArray[0] = f;
                fArray[1] = f5;
            }
        } else {
            stringArray[0] = PERCENT_X;
            stringArray[1] = PERCENT_Y;
            fArray[0] = f;
            fArray[1] = f5;
        }
    }

    void positionScreenAttributes(View view, RectF rectF, RectF rectF2, float f, float f2, String[] stringArray, float[] fArray) {
        rectF.centerX();
        rectF.centerY();
        rectF2.centerX();
        rectF2.centerY();
        view = (ViewGroup)view.getParent();
        int n = view.getWidth();
        int n2 = view.getHeight();
        if (stringArray[0] != null) {
            if (PERCENT_X.equals(stringArray[0])) {
                fArray[0] = f / (float)n;
                fArray[1] = f2 / (float)n2;
            } else {
                fArray[1] = f / (float)n;
                fArray[0] = f2 / (float)n2;
            }
        } else {
            stringArray[0] = PERCENT_X;
            fArray[0] = f / (float)n;
            stringArray[1] = PERCENT_Y;
            fArray[1] = f2 / (float)n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setValue(String string2, Object object) {
        int n;
        block18: {
            switch (string2.hashCode()) {
                case 428090548: {
                    if (!string2.equals(PERCENT_Y)) break;
                    n = 6;
                    break block18;
                }
                case 428090547: {
                    if (!string2.equals(PERCENT_X)) break;
                    n = 5;
                    break block18;
                }
                case -200259324: {
                    if (!string2.equals("sizePercent")) break;
                    n = 4;
                    break block18;
                }
                case -827014263: {
                    if (!string2.equals("drawPath")) break;
                    n = 1;
                    break block18;
                }
                case -1017587252: {
                    if (!string2.equals("percentHeight")) break;
                    n = 3;
                    break block18;
                }
                case -1127236479: {
                    if (!string2.equals("percentWidth")) break;
                    n = 2;
                    break block18;
                }
                case -1812823328: {
                    if (!string2.equals("transitionEasing")) break;
                    n = 0;
                    break block18;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                return;
            }
            case 6: {
                this.mPercentY = this.toFloat(object);
                return;
            }
            case 5: {
                this.mPercentX = this.toFloat(object);
                return;
            }
            case 4: {
                float f;
                this.mPercentWidth = f = this.toFloat(object);
                this.mPercentHeight = f;
                return;
            }
            case 3: {
                this.mPercentHeight = this.toFloat(object);
                return;
            }
            case 2: {
                this.mPercentWidth = this.toFloat(object);
                return;
            }
            case 1: {
                this.mDrawPath = this.toInt(object);
                return;
            }
            case 0: 
        }
        this.mTransitionEasing = object.toString();
    }

    private static class Loader {
        private static final int CURVE_FIT = 4;
        private static final int DRAW_PATH = 5;
        private static final int FRAME_POSITION = 2;
        private static final int PATH_MOTION_ARC = 10;
        private static final int PERCENT_HEIGHT = 12;
        private static final int PERCENT_WIDTH = 11;
        private static final int PERCENT_X = 6;
        private static final int PERCENT_Y = 7;
        private static final int SIZE_PERCENT = 8;
        private static final int TARGET_ID = 1;
        private static final int TRANSITION_EASING = 3;
        private static final int TYPE = 9;
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray;
            mAttrMap = sparseIntArray = new SparseIntArray();
            sparseIntArray.append(R.styleable.KeyPosition_motionTarget, 1);
            mAttrMap.append(R.styleable.KeyPosition_framePosition, 2);
            mAttrMap.append(R.styleable.KeyPosition_transitionEasing, 3);
            mAttrMap.append(R.styleable.KeyPosition_curveFit, 4);
            mAttrMap.append(R.styleable.KeyPosition_drawPath, 5);
            mAttrMap.append(R.styleable.KeyPosition_percentX, 6);
            mAttrMap.append(R.styleable.KeyPosition_percentY, 7);
            mAttrMap.append(R.styleable.KeyPosition_keyPositionType, 9);
            mAttrMap.append(R.styleable.KeyPosition_sizePercent, 8);
            mAttrMap.append(R.styleable.KeyPosition_percentWidth, 11);
            mAttrMap.append(R.styleable.KeyPosition_percentHeight, 12);
            mAttrMap.append(R.styleable.KeyPosition_pathMotionArc, 10);
        }

        private Loader() {
        }

        private static void read(KeyPosition keyPosition, TypedArray typedArray) {
            int n = typedArray.getIndexCount();
            block14: for (int i = 0; i < n; ++i) {
                int n2 = typedArray.getIndex(i);
                switch (mAttrMap.get(n2)) {
                    default: {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("unused attribute 0x");
                        stringBuilder.append(Integer.toHexString(n2));
                        stringBuilder.append("   ");
                        stringBuilder.append(mAttrMap.get(n2));
                        Log.e((String)"KeyPosition", (String)stringBuilder.toString());
                        continue block14;
                    }
                    case 12: {
                        keyPosition.mPercentHeight = typedArray.getFloat(n2, keyPosition.mPercentHeight);
                        continue block14;
                    }
                    case 11: {
                        keyPosition.mPercentWidth = typedArray.getFloat(n2, keyPosition.mPercentWidth);
                        continue block14;
                    }
                    case 10: {
                        keyPosition.mPathMotionArc = typedArray.getInt(n2, keyPosition.mPathMotionArc);
                        continue block14;
                    }
                    case 9: {
                        keyPosition.mPositionType = typedArray.getInt(n2, keyPosition.mPositionType);
                        continue block14;
                    }
                    case 8: {
                        float f;
                        keyPosition.mPercentWidth = f = typedArray.getFloat(n2, keyPosition.mPercentHeight);
                        keyPosition.mPercentHeight = f;
                        continue block14;
                    }
                    case 7: {
                        keyPosition.mPercentY = typedArray.getFloat(n2, keyPosition.mPercentY);
                        continue block14;
                    }
                    case 6: {
                        keyPosition.mPercentX = typedArray.getFloat(n2, keyPosition.mPercentX);
                        continue block14;
                    }
                    case 5: {
                        keyPosition.mDrawPath = typedArray.getInt(n2, keyPosition.mDrawPath);
                        continue block14;
                    }
                    case 4: {
                        keyPosition.mCurveFit = typedArray.getInteger(n2, keyPosition.mCurveFit);
                        continue block14;
                    }
                    case 3: {
                        if (typedArray.peekValue((int)n2).type == 3) {
                            keyPosition.mTransitionEasing = typedArray.getString(n2);
                            continue block14;
                        }
                        keyPosition.mTransitionEasing = Easing.NAMED_EASING[typedArray.getInteger(n2, 0)];
                        continue block14;
                    }
                    case 2: {
                        keyPosition.mFramePosition = typedArray.getInt(n2, keyPosition.mFramePosition);
                        continue block14;
                    }
                    case 1: {
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            keyPosition.mTargetId = typedArray.getResourceId(n2, keyPosition.mTargetId);
                            if (keyPosition.mTargetId != -1) continue block14;
                            keyPosition.mTargetString = typedArray.getString(n2);
                            continue block14;
                        }
                        if (typedArray.peekValue((int)n2).type == 3) {
                            keyPosition.mTargetString = typedArray.getString(n2);
                            continue block14;
                        }
                        keyPosition.mTargetId = typedArray.getResourceId(n2, keyPosition.mTargetId);
                    }
                }
            }
            if (keyPosition.mFramePosition == -1) {
                Log.e((String)"KeyPosition", (String)"no frame position");
            }
        }
    }
}

