/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.util.Log
 *  android.view.View
 */
package androidx.constraintlayout.motion.widget;

import android.os.Build;
import android.util.Log;
import android.view.View;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.motion.utils.Oscillator;
import androidx.constraintlayout.motion.widget.KeyCycleOscillator$1$$ExternalSyntheticBackport0;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintAttribute;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public abstract class KeyCycleOscillator {
    private static final String TAG = "KeyCycleOscillator";
    private CurveFit mCurveFit;
    protected ConstraintAttribute mCustom;
    private CycleOscillator mCycleOscillator;
    private String mType;
    public int mVariesBy = 0;
    ArrayList<WavePoint> mWavePoints = new ArrayList();
    private int mWaveShape = 0;

    /*
     * Enabled aggressive block sorting
     */
    static KeyCycleOscillator makeSpline(String string2) {
        if (string2.startsWith("CUSTOM")) {
            return new CustomSet();
        }
        int n = -1;
        switch (string2.hashCode()) {
            case 156108012: {
                if (!string2.equals("waveOffset")) break;
                n = 8;
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
            case -797520672: {
                if (!string2.equals("waveVariesBy")) break;
                n = 9;
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
                n = 13;
                break;
            }
            case -1225497655: {
                if (!string2.equals("translationZ")) break;
                n = 12;
                break;
            }
            case -1225497656: {
                if (!string2.equals("translationY")) break;
                n = 11;
                break;
            }
            case -1225497657: {
                if (!string2.equals("translationX")) break;
                n = 10;
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
                return null;
            }
            case 13: {
                return new ProgressSet();
            }
            case 12: {
                return new TranslationZset();
            }
            case 11: {
                return new TranslationYset();
            }
            case 10: {
                return new TranslationXset();
            }
            case 9: {
                return new AlphaSet();
            }
            case 8: {
                return new AlphaSet();
            }
            case 7: {
                return new ScaleYset();
            }
            case 6: {
                return new ScaleXset();
            }
            case 5: {
                return new PathRotateSet();
            }
            case 4: {
                return new RotationYset();
            }
            case 3: {
                return new RotationXset();
            }
            case 2: {
                return new RotationSet();
            }
            case 1: {
                return new ElevationSet();
            }
            case 0: 
        }
        return new AlphaSet();
    }

    public float get(float f) {
        return (float)this.mCycleOscillator.getValues(f);
    }

    public CurveFit getCurveFit() {
        return this.mCurveFit;
    }

    public float getSlope(float f) {
        return (float)this.mCycleOscillator.getSlope(f);
    }

    public void setPoint(int n, int n2, int n3, float f, float f2, float f3) {
        this.mWavePoints.add(new WavePoint(n, f, f2, f3));
        if (n3 != -1) {
            this.mVariesBy = n3;
        }
        this.mWaveShape = n2;
    }

    public void setPoint(int n, int n2, int n3, float f, float f2, float f3, ConstraintAttribute constraintAttribute) {
        this.mWavePoints.add(new WavePoint(n, f, f2, f3));
        if (n3 != -1) {
            this.mVariesBy = n3;
        }
        this.mWaveShape = n2;
        this.mCustom = constraintAttribute;
    }

    public abstract void setProperty(View var1, float var2);

    public void setType(String string2) {
        this.mType = string2;
    }

    public void setup(float f) {
        int n = this.mWavePoints.size();
        if (n == 0) {
            return;
        }
        Collections.sort(this.mWavePoints, new Comparator<WavePoint>(this){
            final KeyCycleOscillator this$0;
            {
                this.this$0 = keyCycleOscillator;
            }

            @Override
            public int compare(WavePoint wavePoint, WavePoint wavePoint2) {
                return KeyCycleOscillator$1$$ExternalSyntheticBackport0.m(wavePoint.mPosition, wavePoint2.mPosition);
            }
        });
        double[] dArray = new double[n];
        double[][] dArray2 = new double[n][2];
        this.mCycleOscillator = new CycleOscillator(this.mWaveShape, this.mVariesBy, n);
        n = 0;
        for (WavePoint wavePoint : this.mWavePoints) {
            double d = wavePoint.mPeriod;
            Double.isNaN(d);
            dArray[n] = d * 0.01;
            dArray2[n][0] = wavePoint.mValue;
            dArray2[n][1] = wavePoint.mOffset;
            this.mCycleOscillator.setPoint(n, wavePoint.mPosition, wavePoint.mPeriod, wavePoint.mOffset, wavePoint.mValue);
            ++n;
        }
        this.mCycleOscillator.setup(f);
        this.mCurveFit = CurveFit.get(0, dArray, dArray2);
    }

    public String toString() {
        String string2 = this.mType;
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        for (WavePoint wavePoint : this.mWavePoints) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append("[");
            stringBuilder.append(wavePoint.mPosition);
            stringBuilder.append(" , ");
            stringBuilder.append(decimalFormat.format(wavePoint.mValue));
            stringBuilder.append("] ");
            string2 = stringBuilder.toString();
        }
        return string2;
    }

    public boolean variesByPath() {
        int n = this.mVariesBy;
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    static class AlphaSet
    extends KeyCycleOscillator {
        AlphaSet() {
        }

        @Override
        public void setProperty(View view, float f) {
            view.setAlpha(this.get(f));
        }
    }

    static class CustomSet
    extends KeyCycleOscillator {
        float[] value = new float[1];

        CustomSet() {
        }

        @Override
        public void setProperty(View view, float f) {
            this.value[0] = this.get(f);
            this.mCustom.setInterpolatedValue(view, this.value);
        }
    }

    static class CycleOscillator {
        private static final String TAG = "CycleOscillator";
        static final int UNSET = -1;
        CurveFit mCurveFit;
        public HashMap<String, ConstraintAttribute> mCustomConstraints;
        float[] mOffset;
        Oscillator mOscillator = new Oscillator();
        float mPathLength;
        float[] mPeriod;
        double[] mPosition;
        float[] mScale;
        double[] mSplineSlopeCache;
        double[] mSplineValueCache;
        float[] mValues;
        private final int mVariesBy;
        int mWaveShape;

        CycleOscillator(int n, int n2, int n3) {
            this.mCustomConstraints = new HashMap();
            this.mWaveShape = n;
            this.mVariesBy = n2;
            this.mOscillator.setType(n);
            this.mValues = new float[n3];
            this.mPosition = new double[n3];
            this.mPeriod = new float[n3];
            this.mOffset = new float[n3];
            this.mScale = new float[n3];
        }

        private ConstraintAttribute get(String object, ConstraintAttribute.AttributeType object2) {
            if (this.mCustomConstraints.containsKey(object)) {
                if (((ConstraintAttribute)(object = this.mCustomConstraints.get(object))).getType() != object2) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("ConstraintAttribute is already a ");
                    ((StringBuilder)object2).append(((ConstraintAttribute)object).getType().name());
                    throw new IllegalArgumentException(((StringBuilder)object2).toString());
                }
            } else {
                object2 = new ConstraintAttribute((String)object, (ConstraintAttribute.AttributeType)((Object)object2));
                this.mCustomConstraints.put((String)object, (ConstraintAttribute)object2);
                object = object2;
            }
            return object;
        }

        public double getSlope(float f) {
            Object object = this.mCurveFit;
            if (object != null) {
                ((CurveFit)object).getSlope((double)f, this.mSplineSlopeCache);
                this.mCurveFit.getPos((double)f, this.mSplineValueCache);
            } else {
                object = this.mSplineSlopeCache;
                object[0] = 0.0;
                object[1] = 0.0;
            }
            double d = this.mOscillator.getValue(f);
            double d2 = this.mOscillator.getSlope(f);
            object = this.mSplineSlopeCache;
            return (double)(object[0] + object[1] * d + this.mSplineValueCache[1] * d2);
        }

        public double getValues(float f) {
            Object object = this.mCurveFit;
            if (object != null) {
                ((CurveFit)object).getPos((double)f, this.mSplineValueCache);
            } else {
                object = this.mSplineValueCache;
                object[0] = (double)this.mOffset[0];
                object[1] = (double)this.mValues[0];
            }
            double d = this.mSplineValueCache[0];
            double d2 = this.mOscillator.getValue(f);
            return this.mSplineValueCache[1] * d2 + d;
        }

        public void setPoint(int n, int n2, float f, float f2, float f3) {
            double[] dArray = this.mPosition;
            double d = n2;
            Double.isNaN(d);
            dArray[n] = d / 100.0;
            this.mPeriod[n] = f;
            this.mOffset[n] = f2;
            this.mValues[n] = f3;
        }

        public void setup(float f) {
            int n;
            this.mPathLength = f;
            double[][] dArray = new double[this.mPosition.length][2];
            Object[] objectArray = this.mValues;
            this.mSplineValueCache = new double[objectArray.length + 1];
            this.mSplineSlopeCache = new double[objectArray.length + 1];
            if (this.mPosition[0] > 0.0) {
                this.mOscillator.addPoint(0.0, this.mPeriod[0]);
            }
            if ((objectArray = (Object[])this.mPosition)[n = objectArray.length - 1] < 1.0) {
                this.mOscillator.addPoint(1.0, this.mPeriod[n]);
            }
            for (n = 0; n < dArray.length; ++n) {
                dArray[n][0] = this.mOffset[n];
                for (int i = 0; i < (objectArray = this.mValues).length; ++i) {
                    dArray[i][1] = objectArray[i];
                }
                this.mOscillator.addPoint(this.mPosition[n], this.mPeriod[n]);
            }
            this.mOscillator.normalize();
            objectArray = this.mPosition;
            this.mCurveFit = objectArray.length > 1 ? CurveFit.get(0, objectArray, dArray) : null;
        }
    }

    static class ElevationSet
    extends KeyCycleOscillator {
        ElevationSet() {
        }

        @Override
        public void setProperty(View view, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setElevation(this.get(f));
            }
        }
    }

    private static class IntDoubleSort {
        private IntDoubleSort() {
        }

        private static int partition(int[] nArray, float[] fArray, int n, int n2) {
            int n3 = nArray[n2];
            int n4 = n;
            while (n < n2) {
                int n5 = n4;
                if (nArray[n] <= n3) {
                    IntDoubleSort.swap(nArray, fArray, n4, n);
                    n5 = n4 + 1;
                }
                ++n;
                n4 = n5;
            }
            IntDoubleSort.swap(nArray, fArray, n4, n2);
            return n4;
        }

        static void sort(int[] nArray, float[] fArray, int n, int n2) {
            int[] nArray2 = new int[nArray.length + 10];
            int n3 = 0 + 1;
            nArray2[0] = n2;
            n2 = n3 + 1;
            nArray2[n3] = n;
            n = n2;
            while (n > 0) {
                int n4 = nArray2[--n];
                n2 = n - 1;
                int n5 = nArray2[n2];
                n = n2;
                if (n4 >= n5) continue;
                n3 = IntDoubleSort.partition(nArray, fArray, n4, n5);
                int n6 = n2 + 1;
                nArray2[n2] = n3 - 1;
                n = n6 + 1;
                nArray2[n6] = n4;
                n2 = n + 1;
                nArray2[n] = n5;
                n = n2 + 1;
                nArray2[n2] = n3 + 1;
            }
        }

        private static void swap(int[] nArray, float[] fArray, int n, int n2) {
            int n3 = nArray[n];
            nArray[n] = nArray[n2];
            nArray[n2] = n3;
            float f = fArray[n];
            fArray[n] = fArray[n2];
            fArray[n2] = f;
        }
    }

    private static class IntFloatFloatSort {
        private IntFloatFloatSort() {
        }

        private static int partition(int[] nArray, float[] fArray, float[] fArray2, int n, int n2) {
            int n3;
            int n4 = nArray[n2];
            int n5 = n3 = n;
            while (n < n2) {
                n3 = n5;
                if (nArray[n] <= n4) {
                    IntFloatFloatSort.swap(nArray, fArray, fArray2, n5, n);
                    n3 = n5 + 1;
                }
                ++n;
                n5 = n3;
            }
            IntFloatFloatSort.swap(nArray, fArray, fArray2, n5, n2);
            return n5;
        }

        static void sort(int[] nArray, float[] fArray, float[] fArray2, int n, int n2) {
            int[] nArray2 = new int[nArray.length + 10];
            int n3 = 0 + 1;
            nArray2[0] = n2;
            n2 = n3 + 1;
            nArray2[n3] = n;
            n = n2;
            while (n > 0) {
                int n4 = nArray2[--n];
                n2 = n - 1;
                int n5 = nArray2[n2];
                n = n2;
                if (n4 >= n5) continue;
                n3 = IntFloatFloatSort.partition(nArray, fArray, fArray2, n4, n5);
                n = n2 + 1;
                nArray2[n2] = n3 - 1;
                n2 = n + 1;
                nArray2[n] = n4;
                n4 = n2 + 1;
                nArray2[n2] = n5;
                n = n4 + 1;
                nArray2[n4] = n3 + 1;
            }
        }

        private static void swap(int[] nArray, float[] fArray, float[] fArray2, int n, int n2) {
            int n3 = nArray[n];
            nArray[n] = nArray[n2];
            nArray[n2] = n3;
            float f = fArray[n];
            fArray[n] = fArray[n2];
            fArray[n2] = f;
            f = fArray2[n];
            fArray2[n] = fArray2[n2];
            fArray2[n2] = f;
        }
    }

    static class PathRotateSet
    extends KeyCycleOscillator {
        PathRotateSet() {
        }

        public void setPathRotate(View view, float f, double d, double d2) {
            view.setRotation(this.get(f) + (float)Math.toDegrees(Math.atan2(d2, d)));
        }

        @Override
        public void setProperty(View view, float f) {
        }
    }

    static class ProgressSet
    extends KeyCycleOscillator {
        boolean mNoMethod = false;

        ProgressSet() {
        }

        @Override
        public void setProperty(View view, float f) {
            if (view instanceof MotionLayout) {
                ((MotionLayout)view).setProgress(this.get(f));
            } else {
                if (this.mNoMethod) {
                    return;
                }
                Method method = null;
                try {
                    Method method2;
                    method = method2 = view.getClass().getMethod("setProgress", Float.TYPE);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    this.mNoMethod = true;
                }
                if (method != null) {
                    try {
                        method.invoke((Object)view, Float.valueOf(this.get(f)));
                    }
                    catch (InvocationTargetException invocationTargetException) {
                        Log.e((String)KeyCycleOscillator.TAG, (String)"unable to setProgress", (Throwable)invocationTargetException);
                    }
                    catch (IllegalAccessException illegalAccessException) {
                        Log.e((String)KeyCycleOscillator.TAG, (String)"unable to setProgress", (Throwable)illegalAccessException);
                    }
                }
            }
        }
    }

    static class RotationSet
    extends KeyCycleOscillator {
        RotationSet() {
        }

        @Override
        public void setProperty(View view, float f) {
            view.setRotation(this.get(f));
        }
    }

    static class RotationXset
    extends KeyCycleOscillator {
        RotationXset() {
        }

        @Override
        public void setProperty(View view, float f) {
            view.setRotationX(this.get(f));
        }
    }

    static class RotationYset
    extends KeyCycleOscillator {
        RotationYset() {
        }

        @Override
        public void setProperty(View view, float f) {
            view.setRotationY(this.get(f));
        }
    }

    static class ScaleXset
    extends KeyCycleOscillator {
        ScaleXset() {
        }

        @Override
        public void setProperty(View view, float f) {
            view.setScaleX(this.get(f));
        }
    }

    static class ScaleYset
    extends KeyCycleOscillator {
        ScaleYset() {
        }

        @Override
        public void setProperty(View view, float f) {
            view.setScaleY(this.get(f));
        }
    }

    static class TranslationXset
    extends KeyCycleOscillator {
        TranslationXset() {
        }

        @Override
        public void setProperty(View view, float f) {
            view.setTranslationX(this.get(f));
        }
    }

    static class TranslationYset
    extends KeyCycleOscillator {
        TranslationYset() {
        }

        @Override
        public void setProperty(View view, float f) {
            view.setTranslationY(this.get(f));
        }
    }

    static class TranslationZset
    extends KeyCycleOscillator {
        TranslationZset() {
        }

        @Override
        public void setProperty(View view, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(this.get(f));
            }
        }
    }

    static class WavePoint {
        float mOffset;
        float mPeriod;
        int mPosition;
        float mValue;

        public WavePoint(int n, float f, float f2, float f3) {
            this.mPosition = n;
            this.mValue = f3;
            this.mOffset = f2;
            this.mPeriod = f;
        }
    }
}

