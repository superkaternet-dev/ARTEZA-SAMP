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
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.solver.widgets.ConstraintWidget;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.constraintlayout.widget.ConstraintSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;

class MotionConstrainedPoint
implements Comparable<MotionConstrainedPoint> {
    static final int CARTESIAN = 2;
    public static final boolean DEBUG = false;
    static final int PERPENDICULAR = 1;
    public static final String TAG = "MotionPaths";
    static String[] names = new String[]{"position", "x", "y", "width", "height", "pathRotate"};
    private float alpha = 1.0f;
    private boolean applyElevation = false;
    LinkedHashMap<String, ConstraintAttribute> attributes;
    private float elevation = 0.0f;
    private float height;
    private int mDrawPath = 0;
    private Easing mKeyFrameEasing;
    int mMode = 0;
    private float mPathRotate;
    private float mPivotX = Float.NaN;
    private float mPivotY = Float.NaN;
    private float mProgress;
    double[] mTempDelta;
    double[] mTempValue;
    int mVisibilityMode = 0;
    private float position;
    private float rotation = 0.0f;
    private float rotationX = 0.0f;
    public float rotationY = 0.0f;
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float translationX = 0.0f;
    private float translationY = 0.0f;
    private float translationZ = 0.0f;
    int visibility;
    private float width;
    private float x;
    private float y;

    public MotionConstrainedPoint() {
        this.mPathRotate = Float.NaN;
        this.mProgress = Float.NaN;
        this.attributes = new LinkedHashMap();
        this.mTempValue = new double[18];
        this.mTempDelta = new double[18];
    }

    private boolean diff(float f, float f2) {
        boolean bl = Float.isNaN(f);
        boolean bl2 = true;
        boolean bl3 = true;
        if (!bl && !Float.isNaN(f2)) {
            if (!(Math.abs(f - f2) > 1.0E-6f)) {
                bl3 = false;
            }
            return bl3;
        }
        bl3 = Float.isNaN(f) != Float.isNaN(f2) ? bl2 : false;
        return bl3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void addValues(HashMap<String, SplineSet> hashMap, int n) {
        Iterator<String> iterator2 = hashMap.keySet().iterator();
        block32: while (true) {
            Object object;
            String string2;
            block39: {
                CharSequence charSequence;
                block40: {
                    ConstraintAttribute constraintAttribute;
                    if (!iterator2.hasNext()) {
                        return;
                    }
                    string2 = iterator2.next();
                    object = hashMap.get(string2);
                    int n2 = -1;
                    switch (string2.hashCode()) {
                        case 92909918: {
                            if (!string2.equals("alpha")) break;
                            n2 = 0;
                            break;
                        }
                        case 37232917: {
                            if (!string2.equals("transitionPathRotate")) break;
                            n2 = 7;
                            break;
                        }
                        case -4379043: {
                            if (!string2.equals("elevation")) break;
                            n2 = 1;
                            break;
                        }
                        case -40300674: {
                            if (!string2.equals("rotation")) break;
                            n2 = 2;
                            break;
                        }
                        case -760884509: {
                            if (!string2.equals("transformPivotY")) break;
                            n2 = 6;
                            break;
                        }
                        case -760884510: {
                            if (!string2.equals("transformPivotX")) break;
                            n2 = 5;
                            break;
                        }
                        case -908189617: {
                            if (!string2.equals("scaleY")) break;
                            n2 = 10;
                            break;
                        }
                        case -908189618: {
                            if (!string2.equals("scaleX")) break;
                            n2 = 9;
                            break;
                        }
                        case -1001078227: {
                            if (!string2.equals("progress")) break;
                            n2 = 8;
                            break;
                        }
                        case -1225497655: {
                            if (!string2.equals("translationZ")) break;
                            n2 = 13;
                            break;
                        }
                        case -1225497656: {
                            if (!string2.equals("translationY")) break;
                            n2 = 12;
                            break;
                        }
                        case -1225497657: {
                            if (!string2.equals("translationX")) break;
                            n2 = 11;
                            break;
                        }
                        case -1249320805: {
                            if (!string2.equals("rotationY")) break;
                            n2 = 4;
                            break;
                        }
                        case -1249320806: {
                            if (!string2.equals("rotationX")) break;
                            n2 = 3;
                        }
                    }
                    float f = 1.0f;
                    float f2 = 0.0f;
                    float f3 = 0.0f;
                    float f4 = 0.0f;
                    float f5 = 0.0f;
                    float f6 = 0.0f;
                    float f7 = 0.0f;
                    float f8 = 0.0f;
                    float f9 = 0.0f;
                    float f10 = 0.0f;
                    float f11 = 0.0f;
                    float f12 = 0.0f;
                    switch (n2) {
                        default: {
                            if (!string2.startsWith("CUSTOM")) break block39;
                            charSequence = string2.split(",")[1];
                            if (this.attributes.containsKey(charSequence)) {
                                constraintAttribute = this.attributes.get(charSequence);
                                if (!(object instanceof SplineSet.CustomSet)) break;
                                ((SplineSet.CustomSet)object).setPoint(n, constraintAttribute);
                                continue block32;
                            }
                            break block40;
                        }
                        case 13: {
                            f = Float.isNaN(this.translationZ) ? f12 : this.translationZ;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 12: {
                            f = Float.isNaN(this.translationY) ? f2 : this.translationY;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 11: {
                            f = Float.isNaN(this.translationX) ? f3 : this.translationX;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 10: {
                            if (!Float.isNaN(this.scaleY)) {
                                f = this.scaleY;
                            }
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 9: {
                            if (!Float.isNaN(this.scaleX)) {
                                f = this.scaleX;
                            }
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 8: {
                            f = Float.isNaN(this.mProgress) ? f4 : this.mProgress;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 7: {
                            f = Float.isNaN(this.mPathRotate) ? f5 : this.mPathRotate;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 6: {
                            f = Float.isNaN(this.mPivotY) ? f6 : this.mPivotY;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 5: {
                            f = Float.isNaN(this.mPivotX) ? f7 : this.mPivotX;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 4: {
                            f = Float.isNaN(this.rotationY) ? f8 : this.rotationY;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 3: {
                            f = Float.isNaN(this.rotationX) ? f9 : this.rotationX;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 2: {
                            f = Float.isNaN(this.rotation) ? f10 : this.rotation;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 1: {
                            f = Float.isNaN(this.elevation) ? f11 : this.elevation;
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                        case 0: {
                            if (!Float.isNaN(this.alpha)) {
                                f = this.alpha;
                            }
                            ((SplineSet)object).setPoint(n, f);
                            continue block32;
                        }
                    }
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append(string2);
                    ((StringBuilder)charSequence).append(" splineSet not a CustomSet frame = ");
                    ((StringBuilder)charSequence).append(n);
                    ((StringBuilder)charSequence).append(", value");
                    ((StringBuilder)charSequence).append(constraintAttribute.getValueToInterpolate());
                    ((StringBuilder)charSequence).append(object);
                    Log.e((String)TAG, (String)((StringBuilder)charSequence).toString());
                    continue;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UNKNOWN customName ");
                stringBuilder.append((String)charSequence);
                Log.e((String)TAG, (String)stringBuilder.toString());
                continue;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("UNKNOWN spline ");
            ((StringBuilder)object).append(string2);
            Log.e((String)TAG, (String)((StringBuilder)object).toString());
        }
    }

    public void applyParameters(View view) {
        this.visibility = view.getVisibility();
        float f = view.getVisibility() != 0 ? 0.0f : view.getAlpha();
        this.alpha = f;
        this.applyElevation = false;
        if (Build.VERSION.SDK_INT >= 21) {
            this.elevation = view.getElevation();
        }
        this.rotation = view.getRotation();
        this.rotationX = view.getRotationX();
        this.rotationY = view.getRotationY();
        this.scaleX = view.getScaleX();
        this.scaleY = view.getScaleY();
        this.mPivotX = view.getPivotX();
        this.mPivotY = view.getPivotY();
        this.translationX = view.getTranslationX();
        this.translationY = view.getTranslationY();
        if (Build.VERSION.SDK_INT >= 21) {
            this.translationZ = view.getTranslationZ();
        }
    }

    public void applyParameters(ConstraintSet.Constraint constraint) {
        this.mVisibilityMode = constraint.propertySet.mVisibilityMode;
        this.visibility = constraint.propertySet.visibility;
        float f = constraint.propertySet.visibility != 0 && this.mVisibilityMode == 0 ? 0.0f : constraint.propertySet.alpha;
        this.alpha = f;
        this.applyElevation = constraint.transform.applyElevation;
        this.elevation = constraint.transform.elevation;
        this.rotation = constraint.transform.rotation;
        this.rotationX = constraint.transform.rotationX;
        this.rotationY = constraint.transform.rotationY;
        this.scaleX = constraint.transform.scaleX;
        this.scaleY = constraint.transform.scaleY;
        this.mPivotX = constraint.transform.transformPivotX;
        this.mPivotY = constraint.transform.transformPivotY;
        this.translationX = constraint.transform.translationX;
        this.translationY = constraint.transform.translationY;
        this.translationZ = constraint.transform.translationZ;
        this.mKeyFrameEasing = Easing.getInterpolator(constraint.motion.mTransitionEasing);
        this.mPathRotate = constraint.motion.mPathRotate;
        this.mDrawPath = constraint.motion.mDrawPath;
        this.mProgress = constraint.propertySet.mProgress;
        for (String string2 : constraint.mCustomConstraints.keySet()) {
            ConstraintAttribute constraintAttribute = constraint.mCustomConstraints.get(string2);
            if (constraintAttribute.getType() == ConstraintAttribute.AttributeType.STRING_TYPE) continue;
            this.attributes.put(string2, constraintAttribute);
        }
    }

    @Override
    public int compareTo(MotionConstrainedPoint motionConstrainedPoint) {
        return Float.compare(this.position, motionConstrainedPoint.position);
    }

    void different(MotionConstrainedPoint motionConstrainedPoint, HashSet<String> hashSet) {
        int n;
        int n2;
        if (this.diff(this.alpha, motionConstrainedPoint.alpha)) {
            hashSet.add("alpha");
        }
        if (this.diff(this.elevation, motionConstrainedPoint.elevation)) {
            hashSet.add("elevation");
        }
        if ((n2 = this.visibility) != (n = motionConstrainedPoint.visibility) && this.mVisibilityMode == 0 && (n2 == 0 || n == 0)) {
            hashSet.add("alpha");
        }
        if (this.diff(this.rotation, motionConstrainedPoint.rotation)) {
            hashSet.add("rotation");
        }
        if (!Float.isNaN(this.mPathRotate) || !Float.isNaN(motionConstrainedPoint.mPathRotate)) {
            hashSet.add("transitionPathRotate");
        }
        if (!Float.isNaN(this.mProgress) || !Float.isNaN(motionConstrainedPoint.mProgress)) {
            hashSet.add("progress");
        }
        if (this.diff(this.rotationX, motionConstrainedPoint.rotationX)) {
            hashSet.add("rotationX");
        }
        if (this.diff(this.rotationY, motionConstrainedPoint.rotationY)) {
            hashSet.add("rotationY");
        }
        if (this.diff(this.mPivotX, motionConstrainedPoint.mPivotX)) {
            hashSet.add("transformPivotX");
        }
        if (this.diff(this.mPivotY, motionConstrainedPoint.mPivotY)) {
            hashSet.add("transformPivotY");
        }
        if (this.diff(this.scaleX, motionConstrainedPoint.scaleX)) {
            hashSet.add("scaleX");
        }
        if (this.diff(this.scaleY, motionConstrainedPoint.scaleY)) {
            hashSet.add("scaleY");
        }
        if (this.diff(this.translationX, motionConstrainedPoint.translationX)) {
            hashSet.add("translationX");
        }
        if (this.diff(this.translationY, motionConstrainedPoint.translationY)) {
            hashSet.add("translationY");
        }
        if (this.diff(this.translationZ, motionConstrainedPoint.translationZ)) {
            hashSet.add("translationZ");
        }
    }

    void different(MotionConstrainedPoint motionConstrainedPoint, boolean[] blArray, String[] stringArray) {
        int n = 0 + 1;
        blArray[0] = blArray[0] | this.diff(this.position, motionConstrainedPoint.position);
        int n2 = n + 1;
        blArray[n] = blArray[n] | this.diff(this.x, motionConstrainedPoint.x);
        n = n2 + 1;
        blArray[n2] = blArray[n2] | this.diff(this.y, motionConstrainedPoint.y);
        n2 = n + 1;
        blArray[n] = blArray[n] | this.diff(this.width, motionConstrainedPoint.width);
        blArray[n2] = blArray[n2] | this.diff(this.height, motionConstrainedPoint.height);
    }

    void fillStandard(double[] dArray, int[] nArray) {
        float[] fArray = new float[]{this.position, this.x, this.y, this.width, this.height, this.alpha, this.elevation, this.rotation, this.rotationX, this.rotationY, this.scaleX, this.scaleY, this.mPivotX, this.mPivotY, this.translationX, this.translationY, this.translationZ, this.mPathRotate};
        int n = 0;
        for (int i = 0; i < nArray.length; ++i) {
            int n2 = n;
            if (nArray[i] < fArray.length) {
                dArray[n] = fArray[nArray[i]];
                n2 = n + 1;
            }
            n = n2;
        }
    }

    int getCustomData(String object, double[] dArray, int n) {
        ConstraintAttribute constraintAttribute = this.attributes.get(object);
        if (constraintAttribute.noOfInterpValues() == 1) {
            dArray[n] = constraintAttribute.getValueToInterpolate();
            return 1;
        }
        int n2 = constraintAttribute.noOfInterpValues();
        object = new float[n2];
        constraintAttribute.getValuesToInterpolate((float[])object);
        int n3 = 0;
        while (n3 < n2) {
            dArray[n] = (double)object[n3];
            ++n3;
            ++n;
        }
        return n2;
    }

    int getCustomDataCount(String string2) {
        return this.attributes.get(string2).noOfInterpValues();
    }

    boolean hasCustomData(String string2) {
        return this.attributes.containsKey(string2);
    }

    void setBounds(float f, float f2, float f3, float f4) {
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
    }

    public void setState(View view) {
        this.setBounds(view.getX(), view.getY(), view.getWidth(), view.getHeight());
        this.applyParameters(view);
    }

    public void setState(ConstraintWidget constraintWidget, ConstraintSet constraintSet, int n) {
        this.setBounds(constraintWidget.getX(), constraintWidget.getY(), constraintWidget.getWidth(), constraintWidget.getHeight());
        this.applyParameters(constraintSet.getParameters(n));
    }
}

