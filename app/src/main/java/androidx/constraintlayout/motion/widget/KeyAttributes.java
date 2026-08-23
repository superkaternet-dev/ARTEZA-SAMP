/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseIntArray
 */
package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import androidx.constraintlayout.motion.widget.Key;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class KeyAttributes
extends Key {
    public static final int KEY_TYPE = 1;
    static final String NAME = "KeyAttribute";
    private static final String TAG = "KeyAttribute";
    private float mAlpha = Float.NaN;
    private int mCurveFit = -1;
    private float mElevation = Float.NaN;
    private float mPivotX;
    private float mPivotY;
    private float mProgress;
    private float mRotation = Float.NaN;
    private float mRotationX = Float.NaN;
    private float mRotationY = Float.NaN;
    private float mScaleX;
    private float mScaleY;
    private String mTransitionEasing;
    private float mTransitionPathRotate;
    private float mTranslationX;
    private float mTranslationY;
    private float mTranslationZ;
    private boolean mVisibility = false;

    public KeyAttributes() {
        this.mPivotX = Float.NaN;
        this.mPivotY = Float.NaN;
        this.mTransitionPathRotate = Float.NaN;
        this.mScaleX = Float.NaN;
        this.mScaleY = Float.NaN;
        this.mTranslationX = Float.NaN;
        this.mTranslationY = Float.NaN;
        this.mTranslationZ = Float.NaN;
        this.mProgress = Float.NaN;
        this.mType = 1;
        this.mCustomConstraints = new HashMap();
    }

    static /* synthetic */ float access$002(KeyAttributes keyAttributes, float f) {
        keyAttributes.mAlpha = f;
        return f;
    }

    static /* synthetic */ float access$1002(KeyAttributes keyAttributes, float f) {
        keyAttributes.mScaleY = f;
        return f;
    }

    static /* synthetic */ float access$102(KeyAttributes keyAttributes, float f) {
        keyAttributes.mElevation = f;
        return f;
    }

    static /* synthetic */ float access$1102(KeyAttributes keyAttributes, float f) {
        keyAttributes.mTransitionPathRotate = f;
        return f;
    }

    static /* synthetic */ float access$1202(KeyAttributes keyAttributes, float f) {
        keyAttributes.mTranslationX = f;
        return f;
    }

    static /* synthetic */ float access$1302(KeyAttributes keyAttributes, float f) {
        keyAttributes.mTranslationY = f;
        return f;
    }

    static /* synthetic */ float access$1402(KeyAttributes keyAttributes, float f) {
        keyAttributes.mTranslationZ = f;
        return f;
    }

    static /* synthetic */ float access$1502(KeyAttributes keyAttributes, float f) {
        keyAttributes.mProgress = f;
        return f;
    }

    static /* synthetic */ float access$202(KeyAttributes keyAttributes, float f) {
        keyAttributes.mRotation = f;
        return f;
    }

    static /* synthetic */ int access$302(KeyAttributes keyAttributes, int n) {
        keyAttributes.mCurveFit = n;
        return n;
    }

    static /* synthetic */ float access$402(KeyAttributes keyAttributes, float f) {
        keyAttributes.mScaleX = f;
        return f;
    }

    static /* synthetic */ float access$502(KeyAttributes keyAttributes, float f) {
        keyAttributes.mRotationX = f;
        return f;
    }

    static /* synthetic */ float access$602(KeyAttributes keyAttributes, float f) {
        keyAttributes.mRotationY = f;
        return f;
    }

    static /* synthetic */ float access$702(KeyAttributes keyAttributes, float f) {
        keyAttributes.mPivotX = f;
        return f;
    }

    static /* synthetic */ float access$802(KeyAttributes keyAttributes, float f) {
        keyAttributes.mPivotY = f;
        return f;
    }

    static /* synthetic */ String access$902(KeyAttributes keyAttributes, String string2) {
        keyAttributes.mTransitionEasing = string2;
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void addValues(HashMap<String, SplineSet> hashMap) {
        Iterator<String> iterator2 = hashMap.keySet().iterator();
        block32: while (true) {
            int n;
            Object object;
            String string2;
            block35: {
                if (!iterator2.hasNext()) {
                    return;
                }
                string2 = iterator2.next();
                object = hashMap.get(string2);
                boolean bl = string2.startsWith("CUSTOM");
                n = 1;
                if (bl) {
                    String string3 = string2.substring("CUSTOM".length() + 1);
                    ConstraintAttribute constraintAttribute = (ConstraintAttribute)this.mCustomConstraints.get(string3);
                    if (constraintAttribute == null) continue;
                    ((SplineSet.CustomSet)object).setPoint(this.mFramePosition, constraintAttribute);
                    continue;
                }
                switch (string2.hashCode()) {
                    case 92909918: {
                        if (!string2.equals("alpha")) break;
                        n = 0;
                        break block35;
                    }
                    case 37232917: {
                        if (!string2.equals("transitionPathRotate")) break;
                        n = 7;
                        break block35;
                    }
                    case -4379043: {
                        if (!string2.equals("elevation")) break;
                        break block35;
                    }
                    case -40300674: {
                        if (!string2.equals("rotation")) break;
                        n = 2;
                        break block35;
                    }
                    case -760884509: {
                        if (!string2.equals("transformPivotY")) break;
                        n = 6;
                        break block35;
                    }
                    case -760884510: {
                        if (!string2.equals("transformPivotX")) break;
                        n = 5;
                        break block35;
                    }
                    case -908189617: {
                        if (!string2.equals("scaleY")) break;
                        n = 9;
                        break block35;
                    }
                    case -908189618: {
                        if (!string2.equals("scaleX")) break;
                        n = 8;
                        break block35;
                    }
                    case -1001078227: {
                        if (!string2.equals("progress")) break;
                        n = 13;
                        break block35;
                    }
                    case -1225497655: {
                        if (!string2.equals("translationZ")) break;
                        n = 12;
                        break block35;
                    }
                    case -1225497656: {
                        if (!string2.equals("translationY")) break;
                        n = 11;
                        break block35;
                    }
                    case -1225497657: {
                        if (!string2.equals("translationX")) break;
                        n = 10;
                        break block35;
                    }
                    case -1249320805: {
                        if (!string2.equals("rotationY")) break;
                        n = 4;
                        break block35;
                    }
                    case -1249320806: {
                        if (!string2.equals("rotationX")) break;
                        n = 3;
                        break block35;
                    }
                }
                n = -1;
            }
            switch (n) {
                default: {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("UNKNOWN addValues \"");
                    ((StringBuilder)object).append(string2);
                    ((StringBuilder)object).append("\"");
                    Log.v((String)"KeyAttributes", (String)((StringBuilder)object).toString());
                    continue block32;
                }
                case 13: {
                    if (Float.isNaN(this.mProgress)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mProgress);
                    continue block32;
                }
                case 12: {
                    if (Float.isNaN(this.mTranslationZ)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mTranslationZ);
                    continue block32;
                }
                case 11: {
                    if (Float.isNaN(this.mTranslationY)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mTranslationY);
                    continue block32;
                }
                case 10: {
                    if (Float.isNaN(this.mTranslationX)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mTranslationX);
                    continue block32;
                }
                case 9: {
                    if (Float.isNaN(this.mScaleY)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mScaleY);
                    continue block32;
                }
                case 8: {
                    if (Float.isNaN(this.mScaleX)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mScaleX);
                    continue block32;
                }
                case 7: {
                    if (Float.isNaN(this.mTransitionPathRotate)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mTransitionPathRotate);
                    continue block32;
                }
                case 6: {
                    if (Float.isNaN(this.mRotationY)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mPivotY);
                    continue block32;
                }
                case 5: {
                    if (Float.isNaN(this.mRotationX)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mPivotX);
                    continue block32;
                }
                case 4: {
                    if (Float.isNaN(this.mRotationY)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mRotationY);
                    continue block32;
                }
                case 3: {
                    if (Float.isNaN(this.mRotationX)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mRotationX);
                    continue block32;
                }
                case 2: {
                    if (Float.isNaN(this.mRotation)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mRotation);
                    continue block32;
                }
                case 1: {
                    if (Float.isNaN(this.mElevation)) continue block32;
                    ((SplineSet)object).setPoint(this.mFramePosition, this.mElevation);
                    continue block32;
                }
                case 0: 
            }
            if (Float.isNaN(this.mAlpha)) continue;
            ((SplineSet)object).setPoint(this.mFramePosition, this.mAlpha);
        }
    }

    @Override
    public void getAttributeNames(HashSet<String> hashSet) {
        if (!Float.isNaN(this.mAlpha)) {
            hashSet.add("alpha");
        }
        if (!Float.isNaN(this.mElevation)) {
            hashSet.add("elevation");
        }
        if (!Float.isNaN(this.mRotation)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.mRotationX)) {
            hashSet.add("rotationX");
        }
        if (!Float.isNaN(this.mRotationY)) {
            hashSet.add("rotationY");
        }
        if (!Float.isNaN(this.mPivotX)) {
            hashSet.add("transformPivotX");
        }
        if (!Float.isNaN(this.mPivotY)) {
            hashSet.add("transformPivotY");
        }
        if (!Float.isNaN(this.mTranslationX)) {
            hashSet.add("translationX");
        }
        if (!Float.isNaN(this.mTranslationY)) {
            hashSet.add("translationY");
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            hashSet.add("translationZ");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.mScaleX)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.mProgress)) {
            hashSet.add("progress");
        }
        if (this.mCustomConstraints.size() > 0) {
            for (String string2 : this.mCustomConstraints.keySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CUSTOM,");
                stringBuilder.append(string2);
                hashSet.add(stringBuilder.toString());
            }
        }
    }

    int getCurveFit() {
        return this.mCurveFit;
    }

    @Override
    public void load(Context context, AttributeSet attributeSet) {
        Loader.read(this, context.obtainStyledAttributes(attributeSet, R.styleable.KeyAttribute));
    }

    @Override
    public void setInterpolation(HashMap<String, Integer> hashMap) {
        if (this.mCurveFit == -1) {
            return;
        }
        if (!Float.isNaN(this.mAlpha)) {
            hashMap.put("alpha", this.mCurveFit);
        }
        if (!Float.isNaN(this.mElevation)) {
            hashMap.put("elevation", this.mCurveFit);
        }
        if (!Float.isNaN(this.mRotation)) {
            hashMap.put("rotation", this.mCurveFit);
        }
        if (!Float.isNaN(this.mRotationX)) {
            hashMap.put("rotationX", this.mCurveFit);
        }
        if (!Float.isNaN(this.mRotationY)) {
            hashMap.put("rotationY", this.mCurveFit);
        }
        if (!Float.isNaN(this.mPivotX)) {
            hashMap.put("transformPivotX", this.mCurveFit);
        }
        if (!Float.isNaN(this.mPivotY)) {
            hashMap.put("transformPivotY", this.mCurveFit);
        }
        if (!Float.isNaN(this.mTranslationX)) {
            hashMap.put("translationX", this.mCurveFit);
        }
        if (!Float.isNaN(this.mTranslationY)) {
            hashMap.put("translationY", this.mCurveFit);
        }
        if (!Float.isNaN(this.mTranslationZ)) {
            hashMap.put("translationZ", this.mCurveFit);
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashMap.put("transitionPathRotate", this.mCurveFit);
        }
        if (!Float.isNaN(this.mScaleX)) {
            hashMap.put("scaleX", this.mCurveFit);
        }
        if (!Float.isNaN(this.mScaleY)) {
            hashMap.put("scaleY", this.mCurveFit);
        }
        if (!Float.isNaN(this.mProgress)) {
            hashMap.put("progress", this.mCurveFit);
        }
        if (this.mCustomConstraints.size() > 0) {
            for (String string2 : this.mCustomConstraints.keySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CUSTOM,");
                stringBuilder.append(string2);
                hashMap.put(stringBuilder.toString(), this.mCurveFit);
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setValue(String string2, Object object) {
        int n;
        block38: {
            switch (string2.hashCode()) {
                case 1941332754: {
                    if (!string2.equals("visibility")) break;
                    n = 12;
                    break block38;
                }
                case 1317633238: {
                    if (!string2.equals("mTranslationZ")) break;
                    n = 16;
                    break block38;
                }
                case 579057826: {
                    if (!string2.equals("curveFit")) break;
                    n = 1;
                    break block38;
                }
                case 92909918: {
                    if (!string2.equals("alpha")) break;
                    n = 0;
                    break block38;
                }
                case 37232917: {
                    if (!string2.equals("transitionPathRotate")) break;
                    n = 13;
                    break block38;
                }
                case -4379043: {
                    if (!string2.equals("elevation")) break;
                    n = 2;
                    break block38;
                }
                case -40300674: {
                    if (!string2.equals("rotation")) break;
                    n = 4;
                    break block38;
                }
                case -908189617: {
                    if (!string2.equals("scaleY")) break;
                    n = 10;
                    break block38;
                }
                case -908189618: {
                    if (!string2.equals("scaleX")) break;
                    n = 9;
                    break block38;
                }
                case -987906985: {
                    if (!string2.equals("pivotY")) break;
                    n = 8;
                    break block38;
                }
                case -987906986: {
                    if (!string2.equals("pivotX")) break;
                    n = 7;
                    break block38;
                }
                case -1225497656: {
                    if (!string2.equals("translationY")) break;
                    n = 15;
                    break block38;
                }
                case -1225497657: {
                    if (!string2.equals("translationX")) break;
                    n = 14;
                    break block38;
                }
                case -1249320805: {
                    if (!string2.equals("rotationY")) break;
                    n = 6;
                    break block38;
                }
                case -1249320806: {
                    if (!string2.equals("rotationX")) break;
                    n = 5;
                    break block38;
                }
                case -1812823328: {
                    if (!string2.equals("transitionEasing")) break;
                    n = 11;
                    break block38;
                }
                case -1913008125: {
                    if (!string2.equals("motionProgress")) break;
                    n = 3;
                    break block38;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                return;
            }
            case 16: {
                this.mTranslationZ = this.toFloat(object);
                return;
            }
            case 15: {
                this.mTranslationY = this.toFloat(object);
                return;
            }
            case 14: {
                this.mTranslationX = this.toFloat(object);
                return;
            }
            case 13: {
                this.mTransitionPathRotate = this.toFloat(object);
                return;
            }
            case 12: {
                this.mVisibility = this.toBoolean(object);
                return;
            }
            case 11: {
                this.mTransitionEasing = object.toString();
                return;
            }
            case 10: {
                this.mScaleY = this.toFloat(object);
                return;
            }
            case 9: {
                this.mScaleX = this.toFloat(object);
                return;
            }
            case 8: {
                this.mPivotY = this.toFloat(object);
                return;
            }
            case 7: {
                this.mPivotX = this.toFloat(object);
                return;
            }
            case 6: {
                this.mRotationY = this.toFloat(object);
                return;
            }
            case 5: {
                this.mRotationX = this.toFloat(object);
                return;
            }
            case 4: {
                this.mRotation = this.toFloat(object);
                return;
            }
            case 3: {
                this.mProgress = this.toFloat(object);
                return;
            }
            case 2: {
                this.mElevation = this.toFloat(object);
                return;
            }
            case 1: {
                this.mCurveFit = this.toInt(object);
                return;
            }
            case 0: 
        }
        this.mAlpha = this.toFloat(object);
    }

    private static class Loader {
        private static final int ANDROID_ALPHA = 1;
        private static final int ANDROID_ELEVATION = 2;
        private static final int ANDROID_PIVOT_X = 19;
        private static final int ANDROID_PIVOT_Y = 20;
        private static final int ANDROID_ROTATION = 4;
        private static final int ANDROID_ROTATION_X = 5;
        private static final int ANDROID_ROTATION_Y = 6;
        private static final int ANDROID_SCALE_X = 7;
        private static final int ANDROID_SCALE_Y = 14;
        private static final int ANDROID_TRANSLATION_X = 15;
        private static final int ANDROID_TRANSLATION_Y = 16;
        private static final int ANDROID_TRANSLATION_Z = 17;
        private static final int CURVE_FIT = 13;
        private static final int FRAME_POSITION = 12;
        private static final int PROGRESS = 18;
        private static final int TARGET_ID = 10;
        private static final int TRANSITION_EASING = 9;
        private static final int TRANSITION_PATH_ROTATE = 8;
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray;
            mAttrMap = sparseIntArray = new SparseIntArray();
            sparseIntArray.append(R.styleable.KeyAttribute_android_alpha, 1);
            mAttrMap.append(R.styleable.KeyAttribute_android_elevation, 2);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotation, 4);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotationX, 5);
            mAttrMap.append(R.styleable.KeyAttribute_android_rotationY, 6);
            mAttrMap.append(R.styleable.KeyAttribute_android_transformPivotX, 19);
            mAttrMap.append(R.styleable.KeyAttribute_android_transformPivotY, 20);
            mAttrMap.append(R.styleable.KeyAttribute_android_scaleX, 7);
            mAttrMap.append(R.styleable.KeyAttribute_transitionPathRotate, 8);
            mAttrMap.append(R.styleable.KeyAttribute_transitionEasing, 9);
            mAttrMap.append(R.styleable.KeyAttribute_motionTarget, 10);
            mAttrMap.append(R.styleable.KeyAttribute_framePosition, 12);
            mAttrMap.append(R.styleable.KeyAttribute_curveFit, 13);
            mAttrMap.append(R.styleable.KeyAttribute_android_scaleY, 14);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationX, 15);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationY, 16);
            mAttrMap.append(R.styleable.KeyAttribute_android_translationZ, 17);
            mAttrMap.append(R.styleable.KeyAttribute_motionProgress, 18);
        }

        private Loader() {
        }

        public static void read(KeyAttributes keyAttributes, TypedArray typedArray) {
            int n = typedArray.getIndexCount();
            block20: for (int i = 0; i < n; ++i) {
                int n2 = typedArray.getIndex(i);
                switch (mAttrMap.get(n2)) {
                    default: {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("unused attribute 0x");
                        stringBuilder.append(Integer.toHexString(n2));
                        stringBuilder.append("   ");
                        stringBuilder.append(mAttrMap.get(n2));
                        Log.e((String)"KeyAttribute", (String)stringBuilder.toString());
                        continue block20;
                    }
                    case 20: {
                        KeyAttributes.access$802(keyAttributes, typedArray.getDimension(n2, keyAttributes.mPivotY));
                        continue block20;
                    }
                    case 19: {
                        KeyAttributes.access$702(keyAttributes, typedArray.getDimension(n2, keyAttributes.mPivotX));
                        continue block20;
                    }
                    case 18: {
                        KeyAttributes.access$1502(keyAttributes, typedArray.getFloat(n2, keyAttributes.mProgress));
                        continue block20;
                    }
                    case 17: {
                        if (Build.VERSION.SDK_INT < 21) continue block20;
                        KeyAttributes.access$1402(keyAttributes, typedArray.getDimension(n2, keyAttributes.mTranslationZ));
                        continue block20;
                    }
                    case 16: {
                        KeyAttributes.access$1302(keyAttributes, typedArray.getDimension(n2, keyAttributes.mTranslationY));
                        continue block20;
                    }
                    case 15: {
                        KeyAttributes.access$1202(keyAttributes, typedArray.getDimension(n2, keyAttributes.mTranslationX));
                        continue block20;
                    }
                    case 14: {
                        KeyAttributes.access$1002(keyAttributes, typedArray.getFloat(n2, keyAttributes.mScaleY));
                        continue block20;
                    }
                    case 13: {
                        KeyAttributes.access$302(keyAttributes, typedArray.getInteger(n2, keyAttributes.mCurveFit));
                        continue block20;
                    }
                    case 12: {
                        keyAttributes.mFramePosition = typedArray.getInt(n2, keyAttributes.mFramePosition);
                        continue block20;
                    }
                    case 10: {
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            keyAttributes.mTargetId = typedArray.getResourceId(n2, keyAttributes.mTargetId);
                            if (keyAttributes.mTargetId != -1) continue block20;
                            keyAttributes.mTargetString = typedArray.getString(n2);
                            continue block20;
                        }
                        if (typedArray.peekValue((int)n2).type == 3) {
                            keyAttributes.mTargetString = typedArray.getString(n2);
                            continue block20;
                        }
                        keyAttributes.mTargetId = typedArray.getResourceId(n2, keyAttributes.mTargetId);
                        continue block20;
                    }
                    case 9: {
                        KeyAttributes.access$902(keyAttributes, typedArray.getString(n2));
                        continue block20;
                    }
                    case 8: {
                        KeyAttributes.access$1102(keyAttributes, typedArray.getFloat(n2, keyAttributes.mTransitionPathRotate));
                        continue block20;
                    }
                    case 7: {
                        KeyAttributes.access$402(keyAttributes, typedArray.getFloat(n2, keyAttributes.mScaleX));
                        continue block20;
                    }
                    case 6: {
                        KeyAttributes.access$602(keyAttributes, typedArray.getFloat(n2, keyAttributes.mRotationY));
                        continue block20;
                    }
                    case 5: {
                        KeyAttributes.access$502(keyAttributes, typedArray.getFloat(n2, keyAttributes.mRotationX));
                        continue block20;
                    }
                    case 4: {
                        KeyAttributes.access$202(keyAttributes, typedArray.getFloat(n2, keyAttributes.mRotation));
                        continue block20;
                    }
                    case 2: {
                        KeyAttributes.access$102(keyAttributes, typedArray.getDimension(n2, keyAttributes.mElevation));
                        continue block20;
                    }
                    case 1: {
                        KeyAttributes.access$002(keyAttributes, typedArray.getFloat(n2, keyAttributes.mAlpha));
                    }
                }
            }
        }
    }
}

