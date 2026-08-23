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
import androidx.constraintlayout.motion.widget.TimeCycleSplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class KeyTimeCycle
extends Key {
    public static final int KEY_TYPE = 3;
    static final String NAME = "KeyTimeCycle";
    private static final String TAG = "KeyTimeCycle";
    private float mAlpha = Float.NaN;
    private int mCurveFit = -1;
    private float mElevation = Float.NaN;
    private float mProgress;
    private float mRotation = Float.NaN;
    private float mRotationX = Float.NaN;
    private float mRotationY = Float.NaN;
    private float mScaleX;
    private float mScaleY;
    private String mTransitionEasing;
    private float mTransitionPathRotate = Float.NaN;
    private float mTranslationX;
    private float mTranslationY;
    private float mTranslationZ;
    private float mWaveOffset = 0.0f;
    private float mWavePeriod;
    private int mWaveShape = 0;

    public KeyTimeCycle() {
        this.mScaleX = Float.NaN;
        this.mScaleY = Float.NaN;
        this.mTranslationX = Float.NaN;
        this.mTranslationY = Float.NaN;
        this.mTranslationZ = Float.NaN;
        this.mProgress = Float.NaN;
        this.mWavePeriod = Float.NaN;
        this.mType = 3;
        this.mCustomConstraints = new HashMap();
    }

    static /* synthetic */ float access$002(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mAlpha = f;
        return f;
    }

    static /* synthetic */ String access$1002(KeyTimeCycle keyTimeCycle, String string2) {
        keyTimeCycle.mTransitionEasing = string2;
        return string2;
    }

    static /* synthetic */ float access$102(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mElevation = f;
        return f;
    }

    static /* synthetic */ float access$1102(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mScaleY = f;
        return f;
    }

    static /* synthetic */ float access$1202(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mTransitionPathRotate = f;
        return f;
    }

    static /* synthetic */ float access$1302(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mTranslationX = f;
        return f;
    }

    static /* synthetic */ float access$1402(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mTranslationY = f;
        return f;
    }

    static /* synthetic */ float access$1502(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mTranslationZ = f;
        return f;
    }

    static /* synthetic */ float access$1602(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mProgress = f;
        return f;
    }

    static /* synthetic */ float access$202(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mRotation = f;
        return f;
    }

    static /* synthetic */ int access$302(KeyTimeCycle keyTimeCycle, int n) {
        keyTimeCycle.mCurveFit = n;
        return n;
    }

    static /* synthetic */ int access$402(KeyTimeCycle keyTimeCycle, int n) {
        keyTimeCycle.mWaveShape = n;
        return n;
    }

    static /* synthetic */ float access$502(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mWavePeriod = f;
        return f;
    }

    static /* synthetic */ float access$602(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mWaveOffset = f;
        return f;
    }

    static /* synthetic */ float access$702(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mScaleX = f;
        return f;
    }

    static /* synthetic */ float access$802(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mRotationX = f;
        return f;
    }

    static /* synthetic */ float access$902(KeyTimeCycle keyTimeCycle, float f) {
        keyTimeCycle.mRotationY = f;
        return f;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void addTimeValues(HashMap<String, TimeCycleSplineSet> hashMap) {
        Iterator<String> iterator2 = hashMap.keySet().iterator();
        block28: while (true) {
            int n;
            Object object;
            String string2;
            block31: {
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
                    ((TimeCycleSplineSet.CustomSet)object).setPoint(this.mFramePosition, constraintAttribute, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue;
                }
                switch (string2.hashCode()) {
                    case 92909918: {
                        if (!string2.equals("alpha")) break;
                        n = 0;
                        break block31;
                    }
                    case 37232917: {
                        if (!string2.equals("transitionPathRotate")) break;
                        n = 5;
                        break block31;
                    }
                    case -4379043: {
                        if (!string2.equals("elevation")) break;
                        break block31;
                    }
                    case -40300674: {
                        if (!string2.equals("rotation")) break;
                        n = 2;
                        break block31;
                    }
                    case -908189617: {
                        if (!string2.equals("scaleY")) break;
                        n = 7;
                        break block31;
                    }
                    case -908189618: {
                        if (!string2.equals("scaleX")) break;
                        n = 6;
                        break block31;
                    }
                    case -1001078227: {
                        if (!string2.equals("progress")) break;
                        n = 11;
                        break block31;
                    }
                    case -1225497655: {
                        if (!string2.equals("translationZ")) break;
                        n = 10;
                        break block31;
                    }
                    case -1225497656: {
                        if (!string2.equals("translationY")) break;
                        n = 9;
                        break block31;
                    }
                    case -1225497657: {
                        if (!string2.equals("translationX")) break;
                        n = 8;
                        break block31;
                    }
                    case -1249320805: {
                        if (!string2.equals("rotationY")) break;
                        n = 4;
                        break block31;
                    }
                    case -1249320806: {
                        if (!string2.equals("rotationX")) break;
                        n = 3;
                        break block31;
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
                    Log.e((String)"KeyTimeCycles", (String)((StringBuilder)object).toString());
                    continue block28;
                }
                case 11: {
                    if (Float.isNaN(this.mProgress)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mProgress, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 10: {
                    if (Float.isNaN(this.mTranslationZ)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mTranslationZ, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 9: {
                    if (Float.isNaN(this.mTranslationY)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mTranslationY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 8: {
                    if (Float.isNaN(this.mTranslationX)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mTranslationX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 7: {
                    if (Float.isNaN(this.mScaleY)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mScaleY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 6: {
                    if (Float.isNaN(this.mScaleX)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mScaleX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 5: {
                    if (Float.isNaN(this.mTransitionPathRotate)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mTransitionPathRotate, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 4: {
                    if (Float.isNaN(this.mRotationY)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mRotationY, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 3: {
                    if (Float.isNaN(this.mRotationX)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mRotationX, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 2: {
                    if (Float.isNaN(this.mRotation)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mRotation, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 1: {
                    if (Float.isNaN(this.mElevation)) continue block28;
                    ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mElevation, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
                    continue block28;
                }
                case 0: 
            }
            if (Float.isNaN(this.mAlpha)) continue;
            ((TimeCycleSplineSet)object).setPoint(this.mFramePosition, this.mAlpha, this.mWavePeriod, this.mWaveShape, this.mWaveOffset);
        }
    }

    @Override
    public void addValues(HashMap<String, SplineSet> hashMap) {
        throw new IllegalArgumentException(" KeyTimeCycles do not support SplineSet");
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

    @Override
    public void load(Context context, AttributeSet attributeSet) {
        Loader.read(this, context.obtainStyledAttributes(attributeSet, R.styleable.KeyTimeCycle));
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
        if (!Float.isNaN(this.mScaleX)) {
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
        block32: {
            switch (string2.hashCode()) {
                case 1317633238: {
                    if (!string2.equals("mTranslationZ")) break;
                    n = 13;
                    break block32;
                }
                case 579057826: {
                    if (!string2.equals("curveFit")) break;
                    n = 1;
                    break block32;
                }
                case 92909918: {
                    if (!string2.equals("alpha")) break;
                    n = 0;
                    break block32;
                }
                case 37232917: {
                    if (!string2.equals("transitionPathRotate")) break;
                    n = 10;
                    break block32;
                }
                case -4379043: {
                    if (!string2.equals("elevation")) break;
                    n = 2;
                    break block32;
                }
                case -40300674: {
                    if (!string2.equals("rotation")) break;
                    n = 4;
                    break block32;
                }
                case -908189617: {
                    if (!string2.equals("scaleY")) break;
                    n = 8;
                    break block32;
                }
                case -908189618: {
                    if (!string2.equals("scaleX")) break;
                    n = 7;
                    break block32;
                }
                case -1001078227: {
                    if (!string2.equals("progress")) break;
                    n = 3;
                    break block32;
                }
                case -1225497656: {
                    if (!string2.equals("translationY")) break;
                    n = 12;
                    break block32;
                }
                case -1225497657: {
                    if (!string2.equals("translationX")) break;
                    n = 11;
                    break block32;
                }
                case -1249320805: {
                    if (!string2.equals("rotationY")) break;
                    n = 6;
                    break block32;
                }
                case -1249320806: {
                    if (!string2.equals("rotationX")) break;
                    n = 5;
                    break block32;
                }
                case -1812823328: {
                    if (!string2.equals("transitionEasing")) break;
                    n = 9;
                    break block32;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                return;
            }
            case 13: {
                this.mTranslationZ = this.toFloat(object);
                return;
            }
            case 12: {
                this.mTranslationY = this.toFloat(object);
                return;
            }
            case 11: {
                this.mTranslationX = this.toFloat(object);
                return;
            }
            case 10: {
                this.mTransitionPathRotate = this.toFloat(object);
                return;
            }
            case 9: {
                this.mTransitionEasing = object.toString();
                return;
            }
            case 8: {
                this.mScaleY = this.toFloat(object);
                return;
            }
            case 7: {
                this.mScaleX = this.toFloat(object);
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
        private static final int WAVE_OFFSET = 21;
        private static final int WAVE_PERIOD = 20;
        private static final int WAVE_SHAPE = 19;
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray;
            mAttrMap = sparseIntArray = new SparseIntArray();
            sparseIntArray.append(R.styleable.KeyTimeCycle_android_alpha, 1);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_elevation, 2);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotation, 4);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotationX, 5);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_rotationY, 6);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_scaleX, 7);
            mAttrMap.append(R.styleable.KeyTimeCycle_transitionPathRotate, 8);
            mAttrMap.append(R.styleable.KeyTimeCycle_transitionEasing, 9);
            mAttrMap.append(R.styleable.KeyTimeCycle_motionTarget, 10);
            mAttrMap.append(R.styleable.KeyTimeCycle_framePosition, 12);
            mAttrMap.append(R.styleable.KeyTimeCycle_curveFit, 13);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_scaleY, 14);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationX, 15);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationY, 16);
            mAttrMap.append(R.styleable.KeyTimeCycle_android_translationZ, 17);
            mAttrMap.append(R.styleable.KeyTimeCycle_motionProgress, 18);
            mAttrMap.append(R.styleable.KeyTimeCycle_wavePeriod, 20);
            mAttrMap.append(R.styleable.KeyTimeCycle_waveOffset, 21);
            mAttrMap.append(R.styleable.KeyTimeCycle_waveShape, 19);
        }

        private Loader() {
        }

        public static void read(KeyTimeCycle keyTimeCycle, TypedArray typedArray) {
            int n = typedArray.getIndexCount();
            block21: for (int i = 0; i < n; ++i) {
                int n2 = typedArray.getIndex(i);
                switch (mAttrMap.get(n2)) {
                    default: {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("unused attribute 0x");
                        stringBuilder.append(Integer.toHexString(n2));
                        stringBuilder.append("   ");
                        stringBuilder.append(mAttrMap.get(n2));
                        Log.e((String)"KeyTimeCycle", (String)stringBuilder.toString());
                        continue block21;
                    }
                    case 21: {
                        if (typedArray.peekValue((int)n2).type == 5) {
                            KeyTimeCycle.access$602(keyTimeCycle, typedArray.getDimension(n2, keyTimeCycle.mWaveOffset));
                            continue block21;
                        }
                        KeyTimeCycle.access$602(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mWaveOffset));
                        continue block21;
                    }
                    case 20: {
                        KeyTimeCycle.access$502(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mWavePeriod));
                        continue block21;
                    }
                    case 19: {
                        KeyTimeCycle.access$402(keyTimeCycle, typedArray.getInt(n2, keyTimeCycle.mWaveShape));
                        continue block21;
                    }
                    case 18: {
                        KeyTimeCycle.access$1602(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mProgress));
                        continue block21;
                    }
                    case 17: {
                        if (Build.VERSION.SDK_INT < 21) continue block21;
                        KeyTimeCycle.access$1502(keyTimeCycle, typedArray.getDimension(n2, keyTimeCycle.mTranslationZ));
                        continue block21;
                    }
                    case 16: {
                        KeyTimeCycle.access$1402(keyTimeCycle, typedArray.getDimension(n2, keyTimeCycle.mTranslationY));
                        continue block21;
                    }
                    case 15: {
                        KeyTimeCycle.access$1302(keyTimeCycle, typedArray.getDimension(n2, keyTimeCycle.mTranslationX));
                        continue block21;
                    }
                    case 14: {
                        KeyTimeCycle.access$1102(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mScaleY));
                        continue block21;
                    }
                    case 13: {
                        KeyTimeCycle.access$302(keyTimeCycle, typedArray.getInteger(n2, keyTimeCycle.mCurveFit));
                        continue block21;
                    }
                    case 12: {
                        keyTimeCycle.mFramePosition = typedArray.getInt(n2, keyTimeCycle.mFramePosition);
                        continue block21;
                    }
                    case 10: {
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            keyTimeCycle.mTargetId = typedArray.getResourceId(n2, keyTimeCycle.mTargetId);
                            if (keyTimeCycle.mTargetId != -1) continue block21;
                            keyTimeCycle.mTargetString = typedArray.getString(n2);
                            continue block21;
                        }
                        if (typedArray.peekValue((int)n2).type == 3) {
                            keyTimeCycle.mTargetString = typedArray.getString(n2);
                            continue block21;
                        }
                        keyTimeCycle.mTargetId = typedArray.getResourceId(n2, keyTimeCycle.mTargetId);
                        continue block21;
                    }
                    case 9: {
                        KeyTimeCycle.access$1002(keyTimeCycle, typedArray.getString(n2));
                        continue block21;
                    }
                    case 8: {
                        KeyTimeCycle.access$1202(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mTransitionPathRotate));
                        continue block21;
                    }
                    case 7: {
                        KeyTimeCycle.access$702(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mScaleX));
                        continue block21;
                    }
                    case 6: {
                        KeyTimeCycle.access$902(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mRotationY));
                        continue block21;
                    }
                    case 5: {
                        KeyTimeCycle.access$802(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mRotationX));
                        continue block21;
                    }
                    case 4: {
                        KeyTimeCycle.access$202(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mRotation));
                        continue block21;
                    }
                    case 2: {
                        KeyTimeCycle.access$102(keyTimeCycle, typedArray.getDimension(n2, keyTimeCycle.mElevation));
                        continue block21;
                    }
                    case 1: {
                        KeyTimeCycle.access$002(keyTimeCycle, typedArray.getFloat(n2, keyTimeCycle.mAlpha));
                    }
                }
            }
        }
    }
}

