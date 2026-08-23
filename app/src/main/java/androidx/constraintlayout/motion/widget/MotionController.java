/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.RectF
 *  android.util.Log
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.View$MeasureSpec
 */
package androidx.constraintlayout.motion.widget;

import android.graphics.RectF;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.constraintlayout.motion.utils.CurveFit;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.utils.VelocityMatrix;
import androidx.constraintlayout.motion.widget.Key;
import androidx.constraintlayout.motion.widget.KeyAttributes;
import androidx.constraintlayout.motion.widget.KeyCache;
import androidx.constraintlayout.motion.widget.KeyCycle;
import androidx.constraintlayout.motion.widget.KeyCycleOscillator;
import androidx.constraintlayout.motion.widget.KeyPosition;
import androidx.constraintlayout.motion.widget.KeyPositionBase;
import androidx.constraintlayout.motion.widget.KeyTimeCycle;
import androidx.constraintlayout.motion.widget.KeyTrigger;
import androidx.constraintlayout.motion.widget.MotionConstrainedPoint;
import androidx.constraintlayout.motion.widget.MotionPaths;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.motion.widget.TimeCycleSplineSet;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class MotionController {
    private static final boolean DEBUG = false;
    public static final int DRAW_PATH_AS_CONFIGURED = 4;
    public static final int DRAW_PATH_BASIC = 1;
    public static final int DRAW_PATH_CARTESIAN = 3;
    public static final int DRAW_PATH_NONE = 0;
    public static final int DRAW_PATH_RECTANGLE = 5;
    public static final int DRAW_PATH_RELATIVE = 2;
    public static final int DRAW_PATH_SCREEN = 6;
    private static final boolean FAVOR_FIXED_SIZE_VIEWS = false;
    public static final int HORIZONTAL_PATH_X = 2;
    public static final int HORIZONTAL_PATH_Y = 3;
    public static final int PATH_PERCENT = 0;
    public static final int PATH_PERPENDICULAR = 1;
    private static final String TAG = "MotionController";
    public static final int VERTICAL_PATH_X = 4;
    public static final int VERTICAL_PATH_Y = 5;
    private int MAX_DIMENSION = 4;
    String[] attributeTable;
    private CurveFit mArcSpline;
    private int[] mAttributeInterpCount;
    private String[] mAttributeNames;
    private HashMap<String, SplineSet> mAttributesMap;
    String mConstraintTag;
    private int mCurveFitType = -1;
    private HashMap<String, KeyCycleOscillator> mCycleMap;
    private MotionPaths mEndMotionPath;
    private MotionConstrainedPoint mEndPoint;
    int mId;
    private double[] mInterpolateData;
    private int[] mInterpolateVariables;
    private double[] mInterpolateVelocity;
    private ArrayList<Key> mKeyList;
    private KeyTrigger[] mKeyTriggers;
    private ArrayList<MotionPaths> mMotionPaths;
    float mMotionStagger;
    private int mPathMotionArc;
    private CurveFit[] mSpline;
    float mStaggerOffset = 0.0f;
    float mStaggerScale = 1.0f;
    private MotionPaths mStartMotionPath = new MotionPaths();
    private MotionConstrainedPoint mStartPoint;
    private HashMap<String, TimeCycleSplineSet> mTimeCycleAttributesMap;
    private float[] mValuesBuff;
    private float[] mVelocity;
    View mView;

    MotionController(View view) {
        this.mEndMotionPath = new MotionPaths();
        this.mStartPoint = new MotionConstrainedPoint();
        this.mEndPoint = new MotionConstrainedPoint();
        this.mMotionStagger = Float.NaN;
        this.mValuesBuff = new float[4];
        this.mMotionPaths = new ArrayList();
        this.mVelocity = new float[1];
        this.mKeyList = new ArrayList();
        this.mPathMotionArc = Key.UNSET;
        this.setView(view);
    }

    private float getAdjustedPosition(float f, float[] fArray) {
        float f2;
        float f3;
        float f4;
        float f5;
        if (fArray != null) {
            fArray[0] = 1.0f;
            f5 = f;
        } else {
            f4 = this.mStaggerScale;
            f5 = f;
            if ((double)f4 != 1.0) {
                f3 = this.mStaggerOffset;
                f2 = f;
                if (f < f3) {
                    f2 = 0.0f;
                }
                f5 = f2;
                if (f2 > f3) {
                    f5 = f2;
                    if ((double)f2 < 1.0) {
                        f5 = (f2 - f3) * f4;
                    }
                }
            }
        }
        f4 = f5;
        Easing easing = this.mStartMotionPath.mKeyFrameEasing;
        f2 = 0.0f;
        f = Float.NaN;
        for (MotionPaths motionPaths : this.mMotionPaths) {
            Easing easing2 = easing;
            float f6 = f2;
            f3 = f;
            if (motionPaths.mKeyFrameEasing != null) {
                if (motionPaths.time < f5) {
                    easing2 = motionPaths.mKeyFrameEasing;
                    f6 = motionPaths.time;
                    f3 = f;
                } else {
                    easing2 = easing;
                    f6 = f2;
                    f3 = f;
                    if (Float.isNaN(f)) {
                        f3 = motionPaths.time;
                        f6 = f2;
                        easing2 = easing;
                    }
                }
            }
            easing = easing2;
            f2 = f6;
            f = f3;
        }
        if (easing != null) {
            f4 = f;
            if (Float.isNaN(f)) {
                f4 = 1.0f;
            }
            f5 = (f5 - f2) / (f4 - f2);
            f4 = f = (f4 - f2) * (float)easing.get(f5) + f2;
            if (fArray != null) {
                fArray[0] = (float)easing.getDiff(f5);
                f4 = f;
            }
        }
        return f4;
    }

    private float getPreCycleDistance() {
        int n = 100;
        float[] fArray = new float[2];
        float f = 0.0f;
        float f2 = 1.0f / (float)(100 - 1);
        double d = 0.0;
        double d2 = 0.0;
        for (int i = 0; i < n; ++i) {
            float f3;
            float f4 = (float)i * f2;
            double d3 = f4;
            Easing easing = this.mStartMotionPath.mKeyFrameEasing;
            float f5 = 0.0f;
            float f6 = Float.NaN;
            for (MotionPaths motionPaths : this.mMotionPaths) {
                Easing easing2 = easing;
                f3 = f5;
                float f7 = f6;
                if (motionPaths.mKeyFrameEasing != null) {
                    if (motionPaths.time < f4) {
                        easing2 = motionPaths.mKeyFrameEasing;
                        f3 = motionPaths.time;
                        f7 = f6;
                    } else {
                        easing2 = easing;
                        f3 = f5;
                        f7 = f6;
                        if (Float.isNaN(f6)) {
                            f7 = motionPaths.time;
                            f3 = f5;
                            easing2 = easing;
                        }
                    }
                }
                easing = easing2;
                f5 = f3;
                f6 = f7;
            }
            if (easing != null) {
                f3 = f6;
                if (Float.isNaN(f6)) {
                    f3 = 1.0f;
                }
                d3 = (f3 - f5) * (float)easing.get((f4 - f5) / (f3 - f5)) + f5;
            }
            this.mSpline[0].getPos(d3, this.mInterpolateData);
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, fArray, 0);
            if (i > 0) {
                d3 = f;
                double d4 = fArray[1];
                Double.isNaN(d4);
                double d5 = fArray[0];
                Double.isNaN(d5);
                d = Math.hypot(d2 - d4, d - d5);
                Double.isNaN(d3);
                f = (float)(d3 + d);
            }
            d = fArray[0];
            d2 = fArray[1];
        }
        return f;
    }

    private void insertKey(MotionPaths motionPaths) {
        int n = Collections.binarySearch(this.mMotionPaths, motionPaths);
        if (n == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" KeyPath positon \"");
            stringBuilder.append(motionPaths.position);
            stringBuilder.append("\" outside of range");
            Log.e((String)TAG, (String)stringBuilder.toString());
        }
        this.mMotionPaths.add(-n - 1, motionPaths);
    }

    private void readView(MotionPaths motionPaths) {
        motionPaths.setBounds((int)this.mView.getX(), (int)this.mView.getY(), this.mView.getWidth(), this.mView.getHeight());
    }

    void addKey(Key key) {
        this.mKeyList.add(key);
    }

    void addKeys(ArrayList<Key> arrayList) {
        this.mKeyList.addAll(arrayList);
    }

    void buildBounds(float[] fArray, int n) {
        float f = 1.0f / (float)(n - 1);
        HashMap<String, SplineSet> hashMap = this.mAttributesMap;
        hashMap = hashMap == null ? null : hashMap.get("translationX");
        HashMap<String, SplineSet> hashMap2 = this.mAttributesMap;
        if (hashMap2 != null) {
            hashMap2 = hashMap2.get("translationY");
        }
        hashMap2 = this.mCycleMap;
        if (hashMap2 != null) {
            hashMap2 = (KeyCycleOscillator)hashMap2.get("translationX");
        }
        hashMap2 = this.mCycleMap;
        if (hashMap2 != null) {
            hashMap2 = (KeyCycleOscillator)hashMap2.get("translationY");
        }
        for (int i = 0; i < n; ++i) {
            Object object;
            float f2;
            float f3;
            float f4 = (float)i * f;
            float f5 = this.mStaggerScale;
            float f6 = f4;
            if (f5 != 1.0f) {
                f3 = this.mStaggerOffset;
                f2 = f4;
                if (f4 < f3) {
                    f2 = 0.0f;
                }
                f6 = f2;
                if (f2 > f3) {
                    f6 = f2;
                    if ((double)f2 < 1.0) {
                        f6 = (f2 - f3) * f5;
                    }
                }
            }
            double d = f6;
            hashMap2 = this.mStartMotionPath.mKeyFrameEasing;
            f4 = 0.0f;
            f2 = Float.NaN;
            for (MotionPaths motionPaths : this.mMotionPaths) {
                object = hashMap2;
                f5 = f4;
                f3 = f2;
                if (motionPaths.mKeyFrameEasing != null) {
                    if (motionPaths.time < f6) {
                        object = motionPaths.mKeyFrameEasing;
                        f5 = motionPaths.time;
                        f3 = f2;
                    } else {
                        object = hashMap2;
                        f5 = f4;
                        f3 = f2;
                        if (Float.isNaN(f2)) {
                            f3 = motionPaths.time;
                            f5 = f4;
                            object = hashMap2;
                        }
                    }
                }
                hashMap2 = object;
                f4 = f5;
                f2 = f3;
            }
            if (hashMap2 != null) {
                f3 = f2;
                if (Float.isNaN(f2)) {
                    f3 = 1.0f;
                }
                d = (f3 - f4) * (float)((Easing)((Object)hashMap2)).get((f6 - f4) / (f3 - f4)) + f4;
            }
            this.mSpline[0].getPos(d, this.mInterpolateData);
            hashMap2 = this.mArcSpline;
            if (hashMap2 != null && ((Object)(object = (Object)this.mInterpolateData)).length > 0) {
                ((CurveFit)((Object)hashMap2)).getPos(d, (double[])object);
            }
            this.mStartMotionPath.getBounds(this.mInterpolateVariables, this.mInterpolateData, fArray, i * 2);
        }
    }

    int buildKeyBounds(float[] fArray, int[] nArray) {
        if (fArray != null) {
            int n = 0;
            double[] dArray = this.mSpline[0].getTimePoints();
            if (nArray != null) {
                Iterator<MotionPaths> iterator2 = this.mMotionPaths.iterator();
                while (iterator2.hasNext()) {
                    nArray[n] = iterator2.next().mMode;
                    ++n;
                }
            }
            int n2 = 0;
            for (n = 0; n < dArray.length; ++n) {
                this.mSpline[0].getPos(dArray[n], this.mInterpolateData);
                this.mStartMotionPath.getBounds(this.mInterpolateVariables, this.mInterpolateData, fArray, n2);
                n2 += 2;
            }
            return n2 / 2;
        }
        return 0;
    }

    int buildKeyFrames(float[] fArray, int[] nArray) {
        if (fArray != null) {
            int n = 0;
            double[] dArray = this.mSpline[0].getTimePoints();
            if (nArray != null) {
                Iterator<MotionPaths> iterator2 = this.mMotionPaths.iterator();
                while (iterator2.hasNext()) {
                    nArray[n] = iterator2.next().mMode;
                    ++n;
                }
            }
            int n2 = 0;
            for (n = 0; n < dArray.length; ++n) {
                this.mSpline[0].getPos(dArray[n], this.mInterpolateData);
                this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, fArray, n2);
                n2 += 2;
            }
            return n2 / 2;
        }
        return 0;
    }

    void buildPath(float[] fArray, int n) {
        float f = 1.0f / (float)(n - 1);
        Object object = this.mAttributesMap;
        KeyCycleOscillator keyCycleOscillator = null;
        object = object == null ? null : ((HashMap)object).get("translationX");
        Object object2 = this.mAttributesMap;
        object2 = object2 == null ? null : ((HashMap)object2).get("translationY");
        Object object3 = this.mCycleMap;
        object3 = object3 == null ? null : ((HashMap)object3).get("translationX");
        HashMap<String, KeyCycleOscillator> hashMap = this.mCycleMap;
        if (hashMap != null) {
            keyCycleOscillator = hashMap.get("translationY");
        }
        for (int i = 0; i < n; ++i) {
            int n2;
            Object object4;
            float f2;
            float f3;
            float f4 = (float)i * f;
            float f5 = this.mStaggerScale;
            float f6 = f4;
            if (f5 != 1.0f) {
                f3 = this.mStaggerOffset;
                f2 = f4;
                if (f4 < f3) {
                    f2 = 0.0f;
                }
                f6 = f2;
                if (f2 > f3) {
                    f6 = f2;
                    if ((double)f2 < 1.0) {
                        f6 = (f2 - f3) * f5;
                    }
                }
            }
            double d = f6;
            hashMap = this.mStartMotionPath.mKeyFrameEasing;
            f4 = 0.0f;
            f2 = Float.NaN;
            for (MotionPaths motionPaths : this.mMotionPaths) {
                object4 = hashMap;
                f3 = f4;
                f5 = f2;
                if (motionPaths.mKeyFrameEasing != null) {
                    if (motionPaths.time < f6) {
                        object4 = motionPaths.mKeyFrameEasing;
                        f3 = motionPaths.time;
                        f5 = f2;
                    } else {
                        object4 = hashMap;
                        f3 = f4;
                        f5 = f2;
                        if (Float.isNaN(f2)) {
                            f5 = motionPaths.time;
                            f3 = f4;
                            object4 = hashMap;
                        }
                    }
                }
                hashMap = object4;
                f4 = f3;
                f2 = f5;
            }
            if (hashMap != null) {
                f5 = f2;
                if (Float.isNaN(f2)) {
                    f5 = 1.0f;
                }
                d = (f5 - f4) * (float)((Easing)((Object)hashMap)).get((f6 - f4) / (f5 - f4)) + f4;
            }
            this.mSpline[0].getPos(d, this.mInterpolateData);
            hashMap = this.mArcSpline;
            if (hashMap != null && ((Object)(object4 = (Object)this.mInterpolateData)).length > 0) {
                ((CurveFit)((Object)hashMap)).getPos(d, (double[])object4);
            }
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, fArray, i * 2);
            if (object3 != null) {
                n2 = i * 2;
                fArray[n2] = fArray[n2] + ((KeyCycleOscillator)object3).get(f6);
            } else if (object != null) {
                n2 = i * 2;
                fArray[n2] = fArray[n2] + ((SplineSet)object).get(f6);
            }
            if (keyCycleOscillator != null) {
                n2 = i * 2 + 1;
                fArray[n2] = fArray[n2] + keyCycleOscillator.get(f6);
                continue;
            }
            if (object2 == null) continue;
            n2 = i * 2 + 1;
            fArray[n2] = fArray[n2] + ((SplineSet)object2).get(f6);
        }
    }

    void buildRect(float f, float[] fArray, int n) {
        f = this.getAdjustedPosition(f, null);
        this.mSpline[0].getPos((double)f, this.mInterpolateData);
        this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, fArray, n);
    }

    void buildRectangles(float[] fArray, int n) {
        float f = 1.0f / (float)(n - 1);
        for (int i = 0; i < n; ++i) {
            float f2 = this.getAdjustedPosition((float)i * f, null);
            this.mSpline[0].getPos((double)f2, this.mInterpolateData);
            this.mStartMotionPath.getRect(this.mInterpolateVariables, this.mInterpolateData, fArray, i * 8);
        }
    }

    int getAttributeValues(String object, float[] fArray, int n) {
        float f = 1.0f / (float)(n - 1);
        if ((object = this.mAttributesMap.get(object)) == null) {
            return -1;
        }
        for (n = 0; n < fArray.length; ++n) {
            fArray[n] = ((SplineSet)object).get(n / (fArray.length - 1));
        }
        return fArray.length;
    }

    void getDpDt(float f, float f2, float f3, float[] fArray) {
        float f4 = this.getAdjustedPosition(f, this.mVelocity);
        Object object = this.mSpline;
        if (object != null) {
            double[] dArray;
            object[0].getSlope((double)f4, this.mInterpolateVelocity);
            this.mSpline[0].getPos((double)f4, this.mInterpolateData);
            f = this.mVelocity[0];
            for (int i = 0; i < (dArray = this.mInterpolateVelocity).length; ++i) {
                double d = dArray[i];
                double d2 = f;
                Double.isNaN(d2);
                dArray[i] = d * d2;
            }
            object = this.mArcSpline;
            if (object != null) {
                dArray = this.mInterpolateData;
                if (dArray.length > 0) {
                    ((CurveFit)object).getPos((double)f4, dArray);
                    this.mArcSpline.getSlope((double)f4, this.mInterpolateVelocity);
                    this.mStartMotionPath.setDpDt(f2, f3, fArray, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
                }
                return;
            }
            this.mStartMotionPath.setDpDt(f2, f3, fArray, this.mInterpolateVariables, dArray, this.mInterpolateData);
            return;
        }
        f = this.mEndMotionPath.x - this.mStartMotionPath.x;
        float f5 = this.mEndMotionPath.y - this.mStartMotionPath.y;
        float f6 = this.mEndMotionPath.width;
        f4 = this.mStartMotionPath.width;
        float f7 = this.mEndMotionPath.height;
        float f8 = this.mStartMotionPath.height;
        fArray[0] = (1.0f - f2) * f + (f + (f6 - f4)) * f2;
        fArray[1] = (1.0f - f3) * f5 + (f5 + (f7 - f8)) * f3;
    }

    public int getDrawPath() {
        int n = this.mStartMotionPath.mDrawPath;
        Iterator<MotionPaths> iterator2 = this.mMotionPaths.iterator();
        while (iterator2.hasNext()) {
            n = Math.max(n, iterator2.next().mDrawPath);
        }
        return Math.max(n, this.mEndMotionPath.mDrawPath);
    }

    float getFinalX() {
        return this.mEndMotionPath.x;
    }

    float getFinalY() {
        return this.mEndMotionPath.y;
    }

    MotionPaths getKeyFrame(int n) {
        return this.mMotionPaths.get(n);
    }

    public int getKeyFrameInfo(int n, int[] nArray) {
        int n2 = 0;
        int n3 = 0;
        float[] fArray = new float[2];
        Iterator<Key> iterator2 = this.mKeyList.iterator();
        while (true) {
            int n4 = n3;
            if (!iterator2.hasNext()) break;
            Key key = iterator2.next();
            if (key.mType != n && n == -1) {
                n3 = n4;
                continue;
            }
            nArray[n4] = 0;
            n3 = n4 + 1;
            nArray[n3] = key.mType;
            nArray[++n3] = key.mFramePosition;
            float f = (float)key.mFramePosition / 100.0f;
            this.mSpline[0].getPos((double)f, this.mInterpolateData);
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, fArray, 0);
            nArray[++n3] = Float.floatToIntBits(fArray[0]);
            int n5 = n3 + 1;
            nArray[n5] = Float.floatToIntBits(fArray[1]);
            n3 = n5;
            if (key instanceof KeyPosition) {
                key = (KeyPosition)key;
                n3 = n5 + 1;
                nArray[n3] = ((KeyPosition)key).mPositionType;
                nArray[++n3] = Float.floatToIntBits(((KeyPosition)key).mPercentX);
                nArray[++n3] = Float.floatToIntBits(((KeyPosition)key).mPercentY);
            }
            nArray[n4] = ++n3 - n4;
            ++n2;
        }
        return n2;
    }

    float getKeyFrameParameter(int n, float f, float f2) {
        float f3 = this.mEndMotionPath.x - this.mStartMotionPath.x;
        float f4 = this.mEndMotionPath.y - this.mStartMotionPath.y;
        float f5 = this.mStartMotionPath.x;
        float f6 = this.mStartMotionPath.width / 2.0f;
        float f7 = this.mStartMotionPath.y;
        float f8 = this.mStartMotionPath.height / 2.0f;
        float f9 = (float)Math.hypot(f3, f4);
        if ((double)f9 < 1.0E-7) {
            return Float.NaN;
        }
        if ((float)Math.hypot(f -= f5 + f6, f2 -= f7 + f8) == 0.0f) {
            return 0.0f;
        }
        f7 = f * f3 + f2 * f4;
        switch (n) {
            default: {
                return 0.0f;
            }
            case 5: {
                return f2 / f4;
            }
            case 4: {
                return f / f4;
            }
            case 3: {
                return f2 / f3;
            }
            case 2: {
                return f / f3;
            }
            case 1: {
                return (float)Math.sqrt(f9 * f9 - f7 * f7);
            }
            case 0: 
        }
        return f7 / f9;
    }

    KeyPositionBase getPositionKeyframe(int n, int n2, float f, float f2) {
        RectF rectF = new RectF();
        rectF.left = this.mStartMotionPath.x;
        rectF.top = this.mStartMotionPath.y;
        rectF.right = rectF.left + this.mStartMotionPath.width;
        rectF.bottom = rectF.top + this.mStartMotionPath.height;
        RectF rectF2 = new RectF();
        rectF2.left = this.mEndMotionPath.x;
        rectF2.top = this.mEndMotionPath.y;
        rectF2.right = rectF2.left + this.mEndMotionPath.width;
        rectF2.bottom = rectF2.top + this.mEndMotionPath.height;
        for (Key key : this.mKeyList) {
            if (!(key instanceof KeyPositionBase) || !((KeyPositionBase)key).intersects(n, n2, rectF, rectF2, f, f2)) continue;
            return (KeyPositionBase)key;
        }
        return null;
    }

    void getPostLayoutDvDp(float f, int n, int n2, float f2, float f3, float[] fArray) {
        float f4 = this.getAdjustedPosition(f, this.mVelocity);
        Object object = this.mAttributesMap;
        KeyCycleOscillator keyCycleOscillator = null;
        object = object == null ? null : ((HashMap)object).get("translationX");
        Object object2 = this.mAttributesMap;
        object2 = object2 == null ? null : ((HashMap)object2).get("translationY");
        Object object3 = this.mAttributesMap;
        object3 = object3 == null ? null : ((HashMap)object3).get("rotation");
        Object object4 = this.mAttributesMap;
        object4 = object4 == null ? null : ((HashMap)object4).get("scaleX");
        Object object5 = this.mAttributesMap;
        object5 = object5 == null ? null : ((HashMap)object5).get("scaleY");
        Object object6 = this.mCycleMap;
        object6 = object6 == null ? null : ((HashMap)object6).get("translationX");
        Object object7 = this.mCycleMap;
        object7 = object7 == null ? null : ((HashMap)object7).get("translationY");
        Object object8 = this.mCycleMap;
        object8 = object8 == null ? null : ((HashMap)object8).get("rotation");
        Object object9 = this.mCycleMap;
        object9 = object9 == null ? null : ((HashMap)object9).get("scaleX");
        HashMap<String, KeyCycleOscillator> hashMap = this.mCycleMap;
        if (hashMap != null) {
            keyCycleOscillator = hashMap.get("scaleY");
        }
        hashMap = new VelocityMatrix();
        ((VelocityMatrix)((Object)hashMap)).clear();
        ((VelocityMatrix)((Object)hashMap)).setRotationVelocity((SplineSet)object3, f4);
        ((VelocityMatrix)((Object)hashMap)).setTranslationVelocity((SplineSet)object, (SplineSet)object2, f4);
        ((VelocityMatrix)((Object)hashMap)).setScaleVelocity((SplineSet)object4, (SplineSet)object5, f4);
        ((VelocityMatrix)((Object)hashMap)).setRotationVelocity((KeyCycleOscillator)object8, f4);
        ((VelocityMatrix)((Object)hashMap)).setTranslationVelocity((KeyCycleOscillator)object6, (KeyCycleOscillator)object7, f4);
        ((VelocityMatrix)((Object)hashMap)).setScaleVelocity((KeyCycleOscillator)object9, keyCycleOscillator, f4);
        CurveFit curveFit = this.mArcSpline;
        if (curveFit != null) {
            object = this.mInterpolateData;
            if (((Object)object).length > 0) {
                curveFit.getPos((double)f4, (double[])object);
                this.mArcSpline.getSlope((double)f4, this.mInterpolateVelocity);
                this.mStartMotionPath.setDpDt(f2, f3, fArray, this.mInterpolateVariables, this.mInterpolateVelocity, this.mInterpolateData);
            }
            ((VelocityMatrix)((Object)hashMap)).applyTransform(f2, f3, n, n2, fArray);
            return;
        }
        if (this.mSpline != null) {
            f = this.getAdjustedPosition(f4, this.mVelocity);
            this.mSpline[0].getSlope((double)f, this.mInterpolateVelocity);
            this.mSpline[0].getPos((double)f, this.mInterpolateData);
            f = this.mVelocity[0];
            for (int i = 0; i < ((Object)(object = (Object)this.mInterpolateVelocity)).length; ++i) {
                Object object10 = object[i];
                double d = f;
                Double.isNaN(d);
                object[i] = object10 * d;
            }
            this.mStartMotionPath.setDpDt(f2, f3, fArray, this.mInterpolateVariables, (double[])object, this.mInterpolateData);
            ((VelocityMatrix)((Object)hashMap)).applyTransform(f2, f3, n, n2, fArray);
            return;
        }
        float f5 = this.mEndMotionPath.x - this.mStartMotionPath.x;
        float f6 = this.mEndMotionPath.y - this.mStartMotionPath.y;
        f = this.mEndMotionPath.width;
        float f7 = this.mStartMotionPath.width;
        float f8 = this.mEndMotionPath.height;
        float f9 = this.mStartMotionPath.height;
        fArray[0] = (1.0f - f2) * f5 + (f5 + (f - f7)) * f2;
        fArray[1] = (1.0f - f3) * f6 + (f6 + (f8 - f9)) * f3;
        ((VelocityMatrix)((Object)hashMap)).clear();
        ((VelocityMatrix)((Object)hashMap)).setRotationVelocity((SplineSet)object3, f4);
        ((VelocityMatrix)((Object)hashMap)).setTranslationVelocity((SplineSet)object, (SplineSet)object2, f4);
        ((VelocityMatrix)((Object)hashMap)).setScaleVelocity((SplineSet)object4, (SplineSet)object5, f4);
        ((VelocityMatrix)((Object)hashMap)).setRotationVelocity((KeyCycleOscillator)object8, f4);
        ((VelocityMatrix)((Object)hashMap)).setTranslationVelocity((KeyCycleOscillator)object6, (KeyCycleOscillator)object7, f4);
        ((VelocityMatrix)((Object)hashMap)).setScaleVelocity((KeyCycleOscillator)object9, keyCycleOscillator, f4);
        ((VelocityMatrix)((Object)hashMap)).applyTransform(f2, f3, n, n2, fArray);
    }

    float getStartX() {
        return this.mStartMotionPath.x;
    }

    float getStartY() {
        return this.mStartMotionPath.y;
    }

    public int getkeyFramePositions(int[] nArray, float[] fArray) {
        int n = 0;
        int n2 = 0;
        for (Key key : this.mKeyList) {
            nArray[n] = key.mFramePosition + key.mType * 1000;
            float f = (float)key.mFramePosition / 100.0f;
            this.mSpline[0].getPos((double)f, this.mInterpolateData);
            this.mStartMotionPath.getCenter(this.mInterpolateVariables, this.mInterpolateData, fArray, n2);
            n2 += 2;
            ++n;
        }
        return n;
    }

    boolean interpolate(View view, float f, long l, KeyCache object) {
        boolean bl;
        int n;
        Object object22;
        boolean bl2;
        Object object3;
        f = this.getAdjustedPosition(f, null);
        Object object42 = this.mAttributesMap;
        if (object42 != null) {
            object42 = ((HashMap)object42).values().iterator();
            while (object42.hasNext()) {
                ((SplineSet)object42.next()).setProperty(view, f);
            }
        }
        if ((object42 = this.mTimeCycleAttributesMap) != null) {
            object3 = ((HashMap)object42).values().iterator();
            bl2 = false;
            object42 = null;
            while (object3.hasNext()) {
                object22 = (TimeCycleSplineSet)object3.next();
                if (object22 instanceof TimeCycleSplineSet.PathRotate) {
                    object42 = (TimeCycleSplineSet.PathRotate)object22;
                    continue;
                }
                bl2 |= ((TimeCycleSplineSet)object22).setProperty(view, f, l, (KeyCache)object);
            }
        } else {
            bl2 = false;
            object42 = null;
        }
        if ((object3 = this.mSpline) != null) {
            object3[0].getPos((double)f, this.mInterpolateData);
            this.mSpline[0].getSlope((double)f, this.mInterpolateVelocity);
            object3 = this.mArcSpline;
            if (object3 != null && ((Object)(object22 = (Object)this.mInterpolateData)).length > 0) {
                object3.getPos((double)f, (double[])object22);
                this.mArcSpline.getSlope((double)f, this.mInterpolateVelocity);
            }
            this.mStartMotionPath.setView(view, this.mInterpolateVariables, this.mInterpolateData, this.mInterpolateVelocity, null);
            object3 = this.mAttributesMap;
            if (object3 != null) {
                for (Object object22 : object3.values()) {
                    if (!(object22 instanceof SplineSet.PathRotate)) continue;
                    SplineSet.PathRotate pathRotate = (SplineSet.PathRotate)object22;
                    object22 = this.mInterpolateVelocity;
                    pathRotate.setPathRotate(view, f, (double)object22[0], (double)object22[1]);
                }
            }
            if (object42 != null) {
                object3 = this.mInterpolateVelocity;
                bl2 = ((TimeCycleSplineSet.PathRotate)object42).setPathRotate(view, (KeyCache)object, f, l, (double)object3[0], (double)object3[1]) | bl2;
            }
            for (n = 1; n < ((CurveFit[])(object = this.mSpline)).length; ++n) {
                object[n].getPos((double)f, this.mValuesBuff);
                this.mStartMotionPath.attributes.get(this.mAttributeNames[n - 1]).setInterpolatedValue(view, this.mValuesBuff);
            }
            if (this.mStartPoint.mVisibilityMode == 0) {
                if (f <= 0.0f) {
                    view.setVisibility(this.mStartPoint.visibility);
                } else if (f >= 1.0f) {
                    view.setVisibility(this.mEndPoint.visibility);
                } else if (this.mEndPoint.visibility != this.mStartPoint.visibility) {
                    view.setVisibility(0);
                }
            }
            bl = bl2;
            if (this.mKeyTriggers != null) {
                for (n = 0; n < ((Object)(object = this.mKeyTriggers)).length; ++n) {
                    ((KeyTrigger)object[n]).conditionallyFire(f, view);
                }
                bl = bl2;
            }
        } else {
            float f2 = this.mStartMotionPath.x + (this.mEndMotionPath.x - this.mStartMotionPath.x) * f;
            float f3 = this.mStartMotionPath.y + (this.mEndMotionPath.y - this.mStartMotionPath.y) * f;
            float f4 = this.mStartMotionPath.width;
            float f5 = this.mEndMotionPath.width;
            float f6 = this.mStartMotionPath.width;
            float f7 = this.mStartMotionPath.height;
            float f8 = this.mEndMotionPath.height;
            float f9 = this.mStartMotionPath.height;
            n = (int)(f2 + 0.5f);
            int n2 = (int)(f3 + 0.5f);
            int n3 = (int)(f2 + 0.5f + (f4 + (f5 - f6) * f));
            int n4 = (int)(0.5f + f3 + (f7 + (f8 - f9) * f));
            if (this.mEndMotionPath.width != this.mStartMotionPath.width || this.mEndMotionPath.height != this.mStartMotionPath.height) {
                view.measure(View.MeasureSpec.makeMeasureSpec((int)(n3 - n), (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)(n4 - n2), (int)0x40000000));
            }
            view.layout(n, n2, n3, n4);
            bl = bl2;
        }
        if ((object = this.mCycleMap) != null) {
            for (Object object42 : ((HashMap)object).values()) {
                if (object42 instanceof KeyCycleOscillator.PathRotateSet) {
                    object42 = (KeyCycleOscillator.PathRotateSet)object42;
                    object3 = this.mInterpolateVelocity;
                    ((KeyCycleOscillator.PathRotateSet)object42).setPathRotate(view, f, (double)object3[0], (double)object3[1]);
                    continue;
                }
                ((KeyCycleOscillator)object42).setProperty(view, f);
            }
        }
        return bl;
    }

    String name() {
        return this.mView.getContext().getResources().getResourceEntryName(this.mView.getId());
    }

    void positionKeyframe(View view, KeyPositionBase keyPositionBase, float f, float f2, String[] stringArray, float[] fArray) {
        RectF rectF = new RectF();
        rectF.left = this.mStartMotionPath.x;
        rectF.top = this.mStartMotionPath.y;
        rectF.right = rectF.left + this.mStartMotionPath.width;
        rectF.bottom = rectF.top + this.mStartMotionPath.height;
        RectF rectF2 = new RectF();
        rectF2.left = this.mEndMotionPath.x;
        rectF2.top = this.mEndMotionPath.y;
        rectF2.right = rectF2.left + this.mEndMotionPath.width;
        rectF2.bottom = rectF2.top + this.mEndMotionPath.height;
        keyPositionBase.positionAttributes(view, rectF, rectF2, f, f2, stringArray, fArray);
    }

    public void setDrawPath(int n) {
        this.mStartMotionPath.mDrawPath = n;
    }

    void setEndState(ConstraintWidget constraintWidget, ConstraintSet constraintSet) {
        this.mEndMotionPath.time = 1.0f;
        this.mEndMotionPath.position = 1.0f;
        this.readView(this.mEndMotionPath);
        this.mEndMotionPath.setBounds(constraintWidget.getX(), constraintWidget.getY(), constraintWidget.getWidth(), constraintWidget.getHeight());
        this.mEndMotionPath.applyParameters(constraintSet.getParameters(this.mId));
        this.mEndPoint.setState(constraintWidget, constraintSet, this.mId);
    }

    public void setPathMotionArc(int n) {
        this.mPathMotionArc = n;
    }

    void setStartCurrentState(View view) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        this.mStartMotionPath.setBounds(view.getX(), view.getY(), view.getWidth(), view.getHeight());
        this.mStartPoint.setState(view);
    }

    void setStartState(ConstraintWidget constraintWidget, ConstraintSet constraintSet) {
        this.mStartMotionPath.time = 0.0f;
        this.mStartMotionPath.position = 0.0f;
        this.readView(this.mStartMotionPath);
        this.mStartMotionPath.setBounds(constraintWidget.getX(), constraintWidget.getY(), constraintWidget.getWidth(), constraintWidget.getHeight());
        ConstraintSet.Constraint constraint = constraintSet.getParameters(this.mId);
        this.mStartMotionPath.applyParameters(constraint);
        this.mMotionStagger = constraint.motion.mMotionStagger;
        this.mStartPoint.setState(constraintWidget, constraintSet, this.mId);
    }

    public void setView(View view) {
        this.mView = view;
        this.mId = view.getId();
        if ((view = view.getLayoutParams()) instanceof ConstraintLayout.LayoutParams) {
            this.mConstraintTag = ((ConstraintLayout.LayoutParams)view).getConstraintTag();
        }
    }

    /*
     * WARNING - void declaration
     */
    public void setup(int n, int n2, float f, long l) {
        int[] nArray;
        int n3;
        String[] stringArray;
        Object object;
        void var10_86;
        Object object2;
        HashSet object32 = new HashSet();
        Object object3 = new HashSet<String>();
        Object object4 = new HashSet<String>();
        HashSet<String> hashSet = new HashSet<String>();
        Object object5 = new HashMap();
        Object var11_36 = null;
        Object var10_82 = null;
        if (this.mPathMotionArc != Key.UNSET) {
            this.mStartMotionPath.mPathMotionArc = this.mPathMotionArc;
        }
        this.mStartPoint.different(this.mEndPoint, (HashSet<String>)object4);
        Object object6 = this.mKeyList;
        if (object6 != null) {
            object6 = ((ArrayList)object6).iterator();
            while (object6.hasNext()) {
                object2 = (MotionPaths[])object6.next();
                if (object2 instanceof KeyPosition) {
                    KeyPosition keyPosition = (KeyPosition)object2;
                    this.insertKey(new MotionPaths(n, n2, keyPosition, this.mStartMotionPath, this.mEndMotionPath));
                    if (keyPosition.mCurveFit == Key.UNSET) continue;
                    this.mCurveFitType = keyPosition.mCurveFit;
                    continue;
                }
                if (object2 instanceof KeyCycle) {
                    ((Key)object2).getAttributeNames(hashSet);
                    continue;
                }
                if (object2 instanceof KeyTimeCycle) {
                    ((Key)object2).getAttributeNames((HashSet<String>)object3);
                    continue;
                }
                if (object2 instanceof KeyTrigger) {
                    void var11_41;
                    void var10_83;
                    void var11_39 = var10_83;
                    if (var10_83 == null) {
                        ArrayList arrayList = new ArrayList();
                    }
                    var11_41.add((KeyTrigger)object2);
                    void var10_84 = var11_41;
                    continue;
                }
                ((Key)object2).setInterpolation((HashMap<String, Integer>)object5);
                ((Key)object2).getAttributeNames((HashSet<String>)object4);
            }
        } else {
            Object var10_85 = var11_36;
        }
        if (var10_86 != null) {
            this.mKeyTriggers = var10_86.toArray(new KeyTrigger[0]);
        }
        if (!((HashSet)object4).isEmpty()) {
            this.mAttributesMap = new HashMap();
            Iterator<String> iterator2 = ((HashSet)object4).iterator();
            while (iterator2.hasNext()) {
                void var11_46;
                object6 = iterator2.next();
                if (((String)object6).startsWith("CUSTOM,")) {
                    SparseArray sparseArray = new SparseArray();
                    object = ((String)object6).split(",")[1];
                    for (Key key : this.mKeyList) {
                        ConstraintAttribute constraintAttribute;
                        if (key.mCustomConstraints == null || (constraintAttribute = key.mCustomConstraints.get(object)) == null) continue;
                        sparseArray.append(key.mFramePosition, (Object)constraintAttribute);
                    }
                    SplineSet splineSet = SplineSet.makeCustomSpline((String)object6, (SparseArray<ConstraintAttribute>)sparseArray);
                } else {
                    SplineSet splineSet = SplineSet.makeSpline((String)object6);
                }
                if (var11_46 == null) continue;
                var11_46.setType((String)object6);
                this.mAttributesMap.put((String)object6, (SplineSet)var11_46);
            }
            ArrayList<Key> arrayList = this.mKeyList;
            if (arrayList != null) {
                for (Key key : arrayList) {
                    if (!(key instanceof KeyAttributes)) continue;
                    key.addValues(this.mAttributesMap);
                }
            }
            this.mStartPoint.addValues(this.mAttributesMap, 0);
            this.mEndPoint.addValues(this.mAttributesMap, 100);
            for (String string2 : this.mAttributesMap.keySet()) {
                n = 0;
                if (((HashMap)object5).containsKey(string2)) {
                    n = ((HashMap)object5).get(string2);
                }
                this.mAttributesMap.get(string2).setup(n);
            }
        }
        if (!((HashSet)object3).isEmpty()) {
            if (this.mTimeCycleAttributesMap == null) {
                this.mTimeCycleAttributesMap = new HashMap();
            }
            Iterator iterator3 = ((HashSet)object3).iterator();
            while (iterator3.hasNext()) {
                void var11_55;
                String string3 = (String)iterator3.next();
                if (this.mTimeCycleAttributesMap.containsKey(string3)) continue;
                if (string3.startsWith("CUSTOM,")) {
                    SparseArray sparseArray = new SparseArray();
                    object6 = string3.split(",")[1];
                    for (Key key : this.mKeyList) {
                        if (key.mCustomConstraints == null || (object = key.mCustomConstraints.get(object6)) == null) continue;
                        sparseArray.append(key.mFramePosition, object);
                    }
                    TimeCycleSplineSet timeCycleSplineSet = TimeCycleSplineSet.makeCustomSpline(string3, (SparseArray<ConstraintAttribute>)sparseArray);
                } else {
                    TimeCycleSplineSet timeCycleSplineSet = TimeCycleSplineSet.makeSpline(string3, l);
                }
                if (var11_55 == null) continue;
                var11_55.setType(string3);
                this.mTimeCycleAttributesMap.put(string3, (TimeCycleSplineSet)var11_55);
            }
            ArrayList<Key> arrayList = this.mKeyList;
            if (arrayList != null) {
                for (Key key : arrayList) {
                    if (!(key instanceof KeyTimeCycle)) continue;
                    ((KeyTimeCycle)key).addTimeValues(this.mTimeCycleAttributesMap);
                }
            }
            for (String string4 : this.mTimeCycleAttributesMap.keySet()) {
                n = 0;
                if (((HashMap)object5).containsKey(string4)) {
                    n = ((HashMap)object5).get(string4);
                }
                this.mTimeCycleAttributesMap.get(string4).setup(n);
            }
        }
        object2 = new MotionPaths[this.mMotionPaths.size() + 2];
        n = 1;
        object2[0] = this.mStartMotionPath;
        object2[((MotionPaths[])object2).length - 1] = this.mEndMotionPath;
        if (this.mMotionPaths.size() > 0 && this.mCurveFitType == -1) {
            this.mCurveFitType = 0;
        }
        Iterator<MotionPaths> iterator4 = this.mMotionPaths.iterator();
        while (iterator4.hasNext()) {
            object2[n] = iterator4.next();
            ++n;
        }
        int n4 = 18;
        object6 = new HashSet<String>();
        for (String string5 : this.mEndMotionPath.attributes.keySet()) {
            if (!this.mStartMotionPath.attributes.containsKey(string5)) continue;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("CUSTOM,");
            stringBuilder.append(string5);
            if (((HashSet)object4).contains(stringBuilder.toString())) continue;
            ((HashSet)object6).add(string5);
        }
        String[] stringArray2 = ((HashSet)object6).toArray(new String[0]);
        this.mAttributeNames = stringArray2;
        this.mAttributeInterpCount = new int[stringArray2.length];
        HashSet<String> hashSet2 = object3;
        block11: for (n = 0; n < (stringArray = this.mAttributeNames).length; ++n) {
            String string6 = stringArray[n];
            this.mAttributeInterpCount[n] = 0;
            for (n2 = 0; n2 < ((Object)object2).length; ++n2) {
                if (!((MotionPaths)object2[n2]).attributes.containsKey(string6)) continue;
                int[] nArray2 = this.mAttributeInterpCount;
                nArray2[n] = nArray2[n] + ((MotionPaths)object2[n2]).attributes.get(string6).noOfInterpValues();
                continue block11;
            }
        }
        boolean bl = ((MotionPaths)object2[0]).mPathMotionArc != Key.UNSET;
        boolean[] blArray = new boolean[this.mAttributeNames.length + 18];
        HashSet<String> hashSet3 = object4;
        for (n = 1; n < ((Object)object2).length; ++n) {
            ((MotionPaths)object2[n]).different((MotionPaths)object2[n - 1], blArray, this.mAttributeNames, bl);
        }
        n2 = 0;
        for (n = 1; n < blArray.length; ++n) {
            n3 = n2;
            if (blArray[n]) {
                n3 = n2 + 1;
            }
            n2 = n3;
        }
        int[] nArray3 = new int[n2];
        this.mInterpolateVariables = nArray3;
        this.mInterpolateData = new double[nArray3.length];
        this.mInterpolateVelocity = new double[nArray3.length];
        n = 0;
        for (n2 = 1; n2 < blArray.length; ++n2) {
            n3 = n;
            if (blArray[n2]) {
                this.mInterpolateVariables[n] = n2;
                n3 = n + 1;
            }
            n = n3;
        }
        object4 = new double[((Object)object2).length][this.mInterpolateVariables.length];
        object3 = new double[((Object)object2).length];
        for (n2 = 0; n2 < ((Object)object2).length; ++n2) {
            ((MotionPaths)object2[n2]).fillStandard((double[])object4[n2], this.mInterpolateVariables);
            object3[n2] = (double)((MotionPaths)object2[n2]).time;
        }
        String[] stringArray3 = object5;
        for (n = 0; n < (nArray = this.mInterpolateVariables).length; ++n) {
            void var11_70;
            void var10_109;
            void var10_106;
            void var11_67;
            if (nArray[n] < MotionPaths.names.length) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(MotionPaths.names[this.mInterpolateVariables[n]]);
                stringBuilder.append(" [");
                String string7 = stringBuilder.toString();
                for (n2 = 0; n2 < ((Object)object2).length; ++n2) {
                    void var12_18;
                    object5 = new StringBuilder();
                    ((StringBuilder)object5).append((String)var12_18);
                    ((StringBuilder)object5).append((double)object4[n2][n]);
                    String string8 = ((StringBuilder)object5).toString();
                }
                void var12_20 = var11_67;
                void var11_68 = var10_106;
                void var10_107 = var12_20;
            } else {
                void var12_21 = var10_106;
                void var10_108 = var11_67;
                void var11_69 = var12_21;
            }
            void var12_23 = var10_109;
            void var10_110 = var11_70;
            void var11_71 = var12_23;
        }
        this.mSpline = new CurveFit[this.mAttributeNames.length + 1];
        HashSet<String> hashSet4 = object6;
        n = n4;
        for (n2 = 0; n2 < ((String[])(object5 = this.mAttributeNames)).length; ++n2) {
            void var12_26;
            void var11_74;
            n4 = 0;
            double[][] dArray = null;
            Object var12_25 = null;
            object5 = object5[n2];
            for (n3 = 0; n3 < ((Object)object2).length; ++n3) {
                if (!((MotionPaths)object2[n3]).hasCustomData((String)object5)) continue;
                if (var11_74 == null) {
                    double[] dArray2 = new double[((Object)object2).length];
                    double[][] dArray3 = new double[((Object)object2).length][((MotionPaths)object2[n3]).getCustomDataCount((String)object5)];
                }
                var12_26[n4] = ((MotionPaths)object2[n3]).time;
                ((MotionPaths)object2[n3]).getCustomData((String)object5, (double[])var11_74[n4], 0);
                ++n4;
            }
            double[] dArray4 = Arrays.copyOf((double[])var12_26, n4);
            double[][] dArray5 = (double[][])Arrays.copyOf(var11_74, n4);
            this.mSpline[n2 + 1] = CurveFit.get(this.mCurveFitType, dArray4, dArray5);
        }
        this.mSpline[0] = CurveFit.get(this.mCurveFitType, (double[])object3, (double[][])object4);
        if (((MotionPaths)object2[0]).mPathMotionArc != Key.UNSET) {
            n2 = ((Object)object2).length;
            int[] nArray4 = new int[n2];
            double[] dArray = new double[n2];
            double[][] dArray6 = new double[n2][2];
            for (n = 0; n < n2; ++n) {
                nArray4[n] = ((MotionPaths)object2[n]).mPathMotionArc;
                dArray[n] = ((MotionPaths)object2[n]).time;
                dArray6[n][0] = ((MotionPaths)object2[n]).x;
                dArray6[n][1] = ((MotionPaths)object2[n]).y;
            }
            this.mArcSpline = CurveFit.getArc(nArray4, dArray, dArray6);
        }
        float f2 = Float.NaN;
        this.mCycleMap = new HashMap();
        if (this.mKeyList != null) {
            for (String string9 : hashSet) {
                KeyCycleOscillator keyCycleOscillator = KeyCycleOscillator.makeSpline(string9);
                if (keyCycleOscillator == null) continue;
                f = f2;
                if (keyCycleOscillator.variesByPath()) {
                    f = f2;
                    if (Float.isNaN(f2)) {
                        f = this.getPreCycleDistance();
                    }
                }
                keyCycleOscillator.setType(string9);
                this.mCycleMap.put(string9, keyCycleOscillator);
                f2 = f;
            }
            for (Key key : this.mKeyList) {
                if (!(key instanceof KeyCycle)) continue;
                ((KeyCycle)key).addCycleValues(this.mCycleMap);
            }
            Iterator<KeyCycleOscillator> iterator5 = this.mCycleMap.values().iterator();
            while (iterator5.hasNext()) {
                iterator5.next().setup(f2);
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" start: x: ");
        stringBuilder.append(this.mStartMotionPath.x);
        stringBuilder.append(" y: ");
        stringBuilder.append(this.mStartMotionPath.y);
        stringBuilder.append(" end: x: ");
        stringBuilder.append(this.mEndMotionPath.x);
        stringBuilder.append(" y: ");
        stringBuilder.append(this.mEndMotionPath.y);
        return stringBuilder.toString();
    }
}

