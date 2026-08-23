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
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.Key;
import androidx.constraintlayout.motion.widget.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.R;
import java.util.HashMap;
import java.util.HashSet;

public class KeyCycle
extends Key {
    public static final int KEY_TYPE = 4;
    static final String NAME = "KeyCycle";
    private static final String TAG = "KeyCycle";
    private float mAlpha;
    private int mCurveFit = 0;
    private float mElevation;
    private float mProgress;
    private float mRotation;
    private float mRotationX;
    private float mRotationY;
    private float mScaleX;
    private float mScaleY;
    private String mTransitionEasing = null;
    private float mTransitionPathRotate;
    private float mTranslationX;
    private float mTranslationY;
    private float mTranslationZ;
    private float mWaveOffset = 0.0f;
    private float mWavePeriod = Float.NaN;
    private int mWaveShape = -1;
    private int mWaveVariesBy = -1;

    public KeyCycle() {
        this.mProgress = Float.NaN;
        this.mAlpha = Float.NaN;
        this.mElevation = Float.NaN;
        this.mRotation = Float.NaN;
        this.mTransitionPathRotate = Float.NaN;
        this.mRotationX = Float.NaN;
        this.mRotationY = Float.NaN;
        this.mScaleX = Float.NaN;
        this.mScaleY = Float.NaN;
        this.mTranslationX = Float.NaN;
        this.mTranslationY = Float.NaN;
        this.mTranslationZ = Float.NaN;
        this.mType = 4;
        this.mCustomConstraints = new HashMap();
    }

    static /* synthetic */ float access$1002(KeyCycle keyCycle, float f) {
        keyCycle.mRotationX = f;
        return f;
    }

    static /* synthetic */ String access$102(KeyCycle keyCycle, String string2) {
        keyCycle.mTransitionEasing = string2;
        return string2;
    }

    static /* synthetic */ float access$1102(KeyCycle keyCycle, float f) {
        keyCycle.mRotationY = f;
        return f;
    }

    static /* synthetic */ float access$1202(KeyCycle keyCycle, float f) {
        keyCycle.mTransitionPathRotate = f;
        return f;
    }

    static /* synthetic */ float access$1302(KeyCycle keyCycle, float f) {
        keyCycle.mScaleX = f;
        return f;
    }

    static /* synthetic */ float access$1402(KeyCycle keyCycle, float f) {
        keyCycle.mScaleY = f;
        return f;
    }

    static /* synthetic */ float access$1502(KeyCycle keyCycle, float f) {
        keyCycle.mTranslationX = f;
        return f;
    }

    static /* synthetic */ float access$1602(KeyCycle keyCycle, float f) {
        keyCycle.mTranslationY = f;
        return f;
    }

    static /* synthetic */ float access$1702(KeyCycle keyCycle, float f) {
        keyCycle.mTranslationZ = f;
        return f;
    }

    static /* synthetic */ float access$1802(KeyCycle keyCycle, float f) {
        keyCycle.mProgress = f;
        return f;
    }

    static /* synthetic */ int access$202(KeyCycle keyCycle, int n) {
        keyCycle.mCurveFit = n;
        return n;
    }

    static /* synthetic */ int access$302(KeyCycle keyCycle, int n) {
        keyCycle.mWaveShape = n;
        return n;
    }

    static /* synthetic */ float access$402(KeyCycle keyCycle, float f) {
        keyCycle.mWavePeriod = f;
        return f;
    }

    static /* synthetic */ float access$502(KeyCycle keyCycle, float f) {
        keyCycle.mWaveOffset = f;
        return f;
    }

    static /* synthetic */ int access$602(KeyCycle keyCycle, int n) {
        keyCycle.mWaveVariesBy = n;
        return n;
    }

    static /* synthetic */ float access$702(KeyCycle keyCycle, float f) {
        keyCycle.mAlpha = f;
        return f;
    }

    static /* synthetic */ float access$802(KeyCycle keyCycle, float f) {
        keyCycle.mElevation = f;
        return f;
    }

    static /* synthetic */ float access$902(KeyCycle keyCycle, float f) {
        keyCycle.mRotation = f;
        return f;
    }

    public void addCycleValues(HashMap<String, KeyCycleOscillator> hashMap) {
        for (String string2 : hashMap.keySet()) {
            if (string2.startsWith("CUSTOM")) {
                Object object = string2.substring("CUSTOM".length() + 1);
                if ((object = (ConstraintAttribute)this.mCustomConstraints.get(object)) == null || ((ConstraintAttribute)object).getType() != ConstraintAttribute.AttributeType.FLOAT_TYPE) continue;
                hashMap.get(string2).setPoint(this.mFramePosition, this.mWaveShape, this.mWaveVariesBy, this.mWavePeriod, this.mWaveOffset, ((ConstraintAttribute)object).getValueToInterpolate(), (ConstraintAttribute)object);
                continue;
            }
            float f = this.getValue(string2);
            if (Float.isNaN(f)) continue;
            hashMap.get(string2).setPoint(this.mFramePosition, this.mWaveShape, this.mWaveVariesBy, this.mWavePeriod, this.mWaveOffset, f);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void addValues(HashMap<String, SplineSet> hashMap) {
        Object object = new StringBuilder();
        ((StringBuilder)object).append("add ");
        ((StringBuilder)object).append(hashMap.size());
        ((StringBuilder)object).append(" values");
        Debug.logStack("KeyCycle", ((StringBuilder)object).toString(), 2);
        object = hashMap.keySet().iterator();
        block30: while (object.hasNext()) {
            String string2 = (String)object.next();
            Object object2 = hashMap.get(string2);
            int n = -1;
            switch (string2.hashCode()) {
                case 156108012: {
                    if (!string2.equals("waveOffset")) break;
                    n = 11;
                    break;
                }
                case 92909918: {
                    if (!string2.equals("alpha")) break;
                    n = 0;
                    break;
                }
                case 37232917: {
                    if (!string2.equals("transitionPathRotate")) break;
                    n = 5;
                    break;
                }
                case -4379043: {
                    if (!string2.equals("elevation")) break;
                    n = 1;
                    break;
                }
                case -40300674: {
                    if (!string2.equals("rotation")) break;
                    n = 2;
                    break;
                }
                case -908189617: {
                    if (!string2.equals("scaleY")) break;
                    n = 7;
                    break;
                }
                case -908189618: {
                    if (!string2.equals("scaleX")) break;
                    n = 6;
                    break;
                }
                case -1001078227: {
                    if (!string2.equals("progress")) break;
                    n = 12;
                    break;
                }
                case -1225497655: {
                    if (!string2.equals("translationZ")) break;
                    n = 10;
                    break;
                }
                case -1225497656: {
                    if (!string2.equals("translationY")) break;
                    n = 9;
                    break;
                }
                case -1225497657: {
                    if (!string2.equals("translationX")) break;
                    n = 8;
                    break;
                }
                case -1249320805: {
                    if (!string2.equals("rotationY")) break;
                    n = 4;
                    break;
                }
                case -1249320806: {
                    if (!string2.equals("rotationX")) break;
                    n = 3;
                }
            }
            switch (n) {
                default: {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("WARNING KeyCycle UNKNOWN  ");
                    ((StringBuilder)object2).append(string2);
                    Log.v((String)"KeyCycle", (String)((StringBuilder)object2).toString());
                    continue block30;
                }
                case 12: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mProgress);
                    continue block30;
                }
                case 11: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mWaveOffset);
                    continue block30;
                }
                case 10: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mTranslationZ);
                    continue block30;
                }
                case 9: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mTranslationY);
                    continue block30;
                }
                case 8: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mTranslationX);
                    continue block30;
                }
                case 7: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mScaleY);
                    continue block30;
                }
                case 6: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mScaleX);
                    continue block30;
                }
                case 5: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mTransitionPathRotate);
                    continue block30;
                }
                case 4: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mRotationY);
                    continue block30;
                }
                case 3: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mRotationX);
                    continue block30;
                }
                case 2: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mRotation);
                    continue block30;
                }
                case 1: {
                    ((SplineSet)object2).setPoint(this.mFramePosition, this.mElevation);
                    continue block30;
                }
                case 0: 
            }
            ((SplineSet)object2).setPoint(this.mFramePosition, this.mAlpha);
        }
        return;
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
        if (!Float.isNaN(this.mScaleX)) {
            hashSet.add("scaleX");
        }
        if (!Float.isNaN(this.mScaleY)) {
            hashSet.add("scaleY");
        }
        if (!Float.isNaN(this.mTransitionPathRotate)) {
            hashSet.add("transitionPathRotate");
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
        if (this.mCustomConstraints.size() > 0) {
            for (String string2 : this.mCustomConstraints.keySet()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("CUSTOM,");
                stringBuilder.append(string2);
                hashSet.add(stringBuilder.toString());
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public float getValue(String string2) {
        int n;
        block30: {
            switch (string2.hashCode()) {
                case 156108012: {
                    if (!string2.equals("waveOffset")) break;
                    n = 11;
                    break block30;
                }
                case 92909918: {
                    if (!string2.equals("alpha")) break;
                    n = 0;
                    break block30;
                }
                case 37232917: {
                    if (!string2.equals("transitionPathRotate")) break;
                    n = 5;
                    break block30;
                }
                case -4379043: {
                    if (!string2.equals("elevation")) break;
                    n = 1;
                    break block30;
                }
                case -40300674: {
                    if (!string2.equals("rotation")) break;
                    n = 2;
                    break block30;
                }
                case -908189617: {
                    if (!string2.equals("scaleY")) break;
                    n = 7;
                    break block30;
                }
                case -908189618: {
                    if (!string2.equals("scaleX")) break;
                    n = 6;
                    break block30;
                }
                case -1001078227: {
                    if (!string2.equals("progress")) break;
                    n = 12;
                    break block30;
                }
                case -1225497655: {
                    if (!string2.equals("translationZ")) break;
                    n = 10;
                    break block30;
                }
                case -1225497656: {
                    if (!string2.equals("translationY")) break;
                    n = 9;
                    break block30;
                }
                case -1225497657: {
                    if (!string2.equals("translationX")) break;
                    n = 8;
                    break block30;
                }
                case -1249320805: {
                    if (!string2.equals("rotationY")) break;
                    n = 4;
                    break block30;
                }
                case -1249320806: {
                    if (!string2.equals("rotationX")) break;
                    n = 3;
                    break block30;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("WARNING! KeyCycle UNKNOWN  ");
                stringBuilder.append(string2);
                Log.v((String)"KeyCycle", (String)stringBuilder.toString());
                return Float.NaN;
            }
            case 12: {
                return this.mProgress;
            }
            case 11: {
                return this.mWaveOffset;
            }
            case 10: {
                return this.mTranslationZ;
            }
            case 9: {
                return this.mTranslationY;
            }
            case 8: {
                return this.mTranslationX;
            }
            case 7: {
                return this.mScaleY;
            }
            case 6: {
                return this.mScaleX;
            }
            case 5: {
                return this.mTransitionPathRotate;
            }
            case 4: {
                return this.mRotationY;
            }
            case 3: {
                return this.mRotationX;
            }
            case 2: {
                return this.mRotation;
            }
            case 1: {
                return this.mElevation;
            }
            case 0: 
        }
        return this.mAlpha;
    }

    @Override
    public void load(Context context, AttributeSet attributeSet) {
        Loader.read(this, context.obtainStyledAttributes(attributeSet, R.styleable.KeyCycle));
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setValue(String string2, Object object) {
        int n;
        block36: {
            switch (string2.hashCode()) {
                case 1317633238: {
                    if (!string2.equals("mTranslationZ")) break;
                    n = 13;
                    break block36;
                }
                case 579057826: {
                    if (!string2.equals("curveFit")) break;
                    n = 1;
                    break block36;
                }
                case 184161818: {
                    if (!string2.equals("wavePeriod")) break;
                    n = 14;
                    break block36;
                }
                case 156108012: {
                    if (!string2.equals("waveOffset")) break;
                    n = 15;
                    break block36;
                }
                case 92909918: {
                    if (!string2.equals("alpha")) break;
                    n = 0;
                    break block36;
                }
                case 37232917: {
                    if (!string2.equals("transitionPathRotate")) break;
                    n = 10;
                    break block36;
                }
                case -4379043: {
                    if (!string2.equals("elevation")) break;
                    n = 2;
                    break block36;
                }
                case -40300674: {
                    if (!string2.equals("rotation")) break;
                    n = 4;
                    break block36;
                }
                case -908189617: {
                    if (!string2.equals("scaleY")) break;
                    n = 8;
                    break block36;
                }
                case -908189618: {
                    if (!string2.equals("scaleX")) break;
                    n = 7;
                    break block36;
                }
                case -1001078227: {
                    if (!string2.equals("progress")) break;
                    n = 3;
                    break block36;
                }
                case -1225497656: {
                    if (!string2.equals("translationY")) break;
                    n = 12;
                    break block36;
                }
                case -1225497657: {
                    if (!string2.equals("translationX")) break;
                    n = 11;
                    break block36;
                }
                case -1249320805: {
                    if (!string2.equals("rotationY")) break;
                    n = 6;
                    break block36;
                }
                case -1249320806: {
                    if (!string2.equals("rotationX")) break;
                    n = 5;
                    break block36;
                }
                case -1812823328: {
                    if (!string2.equals("transitionEasing")) break;
                    n = 9;
                    break block36;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                return;
            }
            case 15: {
                this.mWaveOffset = this.toFloat(object);
                return;
            }
            case 14: {
                this.mWavePeriod = this.toFloat(object);
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
        private static final int ANDROID_ALPHA = 9;
        private static final int ANDROID_ELEVATION = 10;
        private static final int ANDROID_ROTATION = 11;
        private static final int ANDROID_ROTATION_X = 12;
        private static final int ANDROID_ROTATION_Y = 13;
        private static final int ANDROID_SCALE_X = 15;
        private static final int ANDROID_SCALE_Y = 16;
        private static final int ANDROID_TRANSLATION_X = 17;
        private static final int ANDROID_TRANSLATION_Y = 18;
        private static final int ANDROID_TRANSLATION_Z = 19;
        private static final int CURVE_FIT = 4;
        private static final int FRAME_POSITION = 2;
        private static final int PROGRESS = 20;
        private static final int TARGET_ID = 1;
        private static final int TRANSITION_EASING = 3;
        private static final int TRANSITION_PATH_ROTATE = 14;
        private static final int WAVE_OFFSET = 7;
        private static final int WAVE_PERIOD = 6;
        private static final int WAVE_SHAPE = 5;
        private static final int WAVE_VARIES_BY = 8;
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray;
            mAttrMap = sparseIntArray = new SparseIntArray();
            sparseIntArray.append(R.styleable.KeyCycle_motionTarget, 1);
            mAttrMap.append(R.styleable.KeyCycle_framePosition, 2);
            mAttrMap.append(R.styleable.KeyCycle_transitionEasing, 3);
            mAttrMap.append(R.styleable.KeyCycle_curveFit, 4);
            mAttrMap.append(R.styleable.KeyCycle_waveShape, 5);
            mAttrMap.append(R.styleable.KeyCycle_wavePeriod, 6);
            mAttrMap.append(R.styleable.KeyCycle_waveOffset, 7);
            mAttrMap.append(R.styleable.KeyCycle_waveVariesBy, 8);
            mAttrMap.append(R.styleable.KeyCycle_android_alpha, 9);
            mAttrMap.append(R.styleable.KeyCycle_android_elevation, 10);
            mAttrMap.append(R.styleable.KeyCycle_android_rotation, 11);
            mAttrMap.append(R.styleable.KeyCycle_android_rotationX, 12);
            mAttrMap.append(R.styleable.KeyCycle_android_rotationY, 13);
            mAttrMap.append(R.styleable.KeyCycle_transitionPathRotate, 14);
            mAttrMap.append(R.styleable.KeyCycle_android_scaleX, 15);
            mAttrMap.append(R.styleable.KeyCycle_android_scaleY, 16);
            mAttrMap.append(R.styleable.KeyCycle_android_translationX, 17);
            mAttrMap.append(R.styleable.KeyCycle_android_translationY, 18);
            mAttrMap.append(R.styleable.KeyCycle_android_translationZ, 19);
            mAttrMap.append(R.styleable.KeyCycle_motionProgress, 20);
        }

        private Loader() {
        }

        private static void read(KeyCycle keyCycle, TypedArray typedArray) {
            int n = typedArray.getIndexCount();
            block22: for (int i = 0; i < n; ++i) {
                int n2 = typedArray.getIndex(i);
                switch (mAttrMap.get(n2)) {
                    default: {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("unused attribute 0x");
                        stringBuilder.append(Integer.toHexString(n2));
                        stringBuilder.append("   ");
                        stringBuilder.append(mAttrMap.get(n2));
                        Log.e((String)"KeyCycle", (String)stringBuilder.toString());
                        continue block22;
                    }
                    case 20: {
                        KeyCycle.access$1802(keyCycle, typedArray.getFloat(n2, keyCycle.mProgress));
                        continue block22;
                    }
                    case 19: {
                        if (Build.VERSION.SDK_INT < 21) continue block22;
                        KeyCycle.access$1702(keyCycle, typedArray.getDimension(n2, keyCycle.mTranslationZ));
                        continue block22;
                    }
                    case 18: {
                        KeyCycle.access$1602(keyCycle, typedArray.getDimension(n2, keyCycle.mTranslationY));
                        continue block22;
                    }
                    case 17: {
                        KeyCycle.access$1502(keyCycle, typedArray.getDimension(n2, keyCycle.mTranslationX));
                        continue block22;
                    }
                    case 16: {
                        KeyCycle.access$1402(keyCycle, typedArray.getFloat(n2, keyCycle.mScaleY));
                        continue block22;
                    }
                    case 15: {
                        KeyCycle.access$1302(keyCycle, typedArray.getFloat(n2, keyCycle.mScaleX));
                        continue block22;
                    }
                    case 14: {
                        KeyCycle.access$1202(keyCycle, typedArray.getFloat(n2, keyCycle.mTransitionPathRotate));
                        continue block22;
                    }
                    case 13: {
                        KeyCycle.access$1102(keyCycle, typedArray.getFloat(n2, keyCycle.mRotationY));
                        continue block22;
                    }
                    case 12: {
                        KeyCycle.access$1002(keyCycle, typedArray.getFloat(n2, keyCycle.mRotationX));
                        continue block22;
                    }
                    case 11: {
                        KeyCycle.access$902(keyCycle, typedArray.getFloat(n2, keyCycle.mRotation));
                        continue block22;
                    }
                    case 10: {
                        KeyCycle.access$802(keyCycle, typedArray.getDimension(n2, keyCycle.mElevation));
                        continue block22;
                    }
                    case 9: {
                        KeyCycle.access$702(keyCycle, typedArray.getFloat(n2, keyCycle.mAlpha));
                        continue block22;
                    }
                    case 8: {
                        KeyCycle.access$602(keyCycle, typedArray.getInt(n2, keyCycle.mWaveVariesBy));
                        continue block22;
                    }
                    case 7: {
                        if (typedArray.peekValue((int)n2).type == 5) {
                            KeyCycle.access$502(keyCycle, typedArray.getDimension(n2, keyCycle.mWaveOffset));
                            continue block22;
                        }
                        KeyCycle.access$502(keyCycle, typedArray.getFloat(n2, keyCycle.mWaveOffset));
                        continue block22;
                    }
                    case 6: {
                        KeyCycle.access$402(keyCycle, typedArray.getFloat(n2, keyCycle.mWavePeriod));
                        continue block22;
                    }
                    case 5: {
                        KeyCycle.access$302(keyCycle, typedArray.getInt(n2, keyCycle.mWaveShape));
                        continue block22;
                    }
                    case 4: {
                        KeyCycle.access$202(keyCycle, typedArray.getInteger(n2, keyCycle.mCurveFit));
                        continue block22;
                    }
                    case 3: {
                        KeyCycle.access$102(keyCycle, typedArray.getString(n2));
                        continue block22;
                    }
                    case 2: {
                        keyCycle.mFramePosition = typedArray.getInt(n2, keyCycle.mFramePosition);
                        continue block22;
                    }
                    case 1: {
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            keyCycle.mTargetId = typedArray.getResourceId(n2, keyCycle.mTargetId);
                            if (keyCycle.mTargetId != -1) continue block22;
                            keyCycle.mTargetString = typedArray.getString(n2);
                            continue block22;
                        }
                        if (typedArray.peekValue((int)n2).type == 3) {
                            keyCycle.mTargetString = typedArray.getString(n2);
                            continue block22;
                        }
                        keyCycle.mTargetId = typedArray.getResourceId(n2, keyCycle.mTargetId);
                    }
                }
            }
        }
    }
}

