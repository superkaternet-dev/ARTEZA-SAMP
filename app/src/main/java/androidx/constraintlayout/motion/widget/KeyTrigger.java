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
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.Key;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.SplineSet;
import androidx.constraintlayout.widget.R;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

public class KeyTrigger
extends Key {
    public static final int KEY_TYPE = 5;
    static final String NAME = "KeyTrigger";
    private static final String TAG = "KeyTrigger";
    RectF mCollisionRect;
    private String mCross = null;
    private int mCurveFit = -1;
    private Method mFireCross;
    private boolean mFireCrossReset = true;
    private float mFireLastPos;
    private Method mFireNegativeCross;
    private boolean mFireNegativeReset = true;
    private Method mFirePositiveCross;
    private boolean mFirePositiveReset = true;
    private float mFireThreshold;
    private String mNegativeCross = null;
    private String mPositiveCross = null;
    private boolean mPostLayout = false;
    RectF mTargetRect;
    private int mTriggerCollisionId;
    private View mTriggerCollisionView = null;
    private int mTriggerID;
    private int mTriggerReceiver = UNSET;
    float mTriggerSlack = 0.1f;

    public KeyTrigger() {
        this.mTriggerID = UNSET;
        this.mTriggerCollisionId = UNSET;
        this.mFireThreshold = Float.NaN;
        this.mCollisionRect = new RectF();
        this.mTargetRect = new RectF();
        this.mType = 5;
        this.mCustomConstraints = new HashMap();
    }

    static /* synthetic */ float access$002(KeyTrigger keyTrigger, float f) {
        keyTrigger.mFireThreshold = f;
        return f;
    }

    static /* synthetic */ String access$102(KeyTrigger keyTrigger, String string2) {
        keyTrigger.mNegativeCross = string2;
        return string2;
    }

    static /* synthetic */ String access$202(KeyTrigger keyTrigger, String string2) {
        keyTrigger.mPositiveCross = string2;
        return string2;
    }

    static /* synthetic */ String access$302(KeyTrigger keyTrigger, String string2) {
        keyTrigger.mCross = string2;
        return string2;
    }

    static /* synthetic */ int access$402(KeyTrigger keyTrigger, int n) {
        keyTrigger.mTriggerID = n;
        return n;
    }

    static /* synthetic */ int access$502(KeyTrigger keyTrigger, int n) {
        keyTrigger.mTriggerCollisionId = n;
        return n;
    }

    static /* synthetic */ boolean access$602(KeyTrigger keyTrigger, boolean bl) {
        keyTrigger.mPostLayout = bl;
        return bl;
    }

    static /* synthetic */ int access$702(KeyTrigger keyTrigger, int n) {
        keyTrigger.mTriggerReceiver = n;
        return n;
    }

    private void setUpRect(RectF rectF, View view, boolean bl) {
        rectF.top = view.getTop();
        rectF.bottom = view.getBottom();
        rectF.left = view.getLeft();
        rectF.right = view.getRight();
        if (bl) {
            view.getMatrix().mapRect(rectF);
        }
    }

    @Override
    public void addValues(HashMap<String, SplineSet> hashMap) {
    }

    public void conditionallyFire(float f, View view) {
        StringBuilder stringBuilder;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;
        boolean bl7 = false;
        boolean bl8 = false;
        boolean bl9 = false;
        boolean bl10 = false;
        boolean bl11 = false;
        boolean bl12 = false;
        if (this.mTriggerCollisionId != UNSET) {
            if (this.mTriggerCollisionView == null) {
                this.mTriggerCollisionView = ((ViewGroup)view.getParent()).findViewById(this.mTriggerCollisionId);
            }
            this.setUpRect(this.mCollisionRect, this.mTriggerCollisionView, this.mPostLayout);
            this.setUpRect(this.mTargetRect, view, this.mPostLayout);
            if (this.mCollisionRect.intersect(this.mTargetRect)) {
                bl2 = bl4;
                if (this.mFireCrossReset) {
                    bl2 = true;
                    this.mFireCrossReset = false;
                }
                if (this.mFirePositiveReset) {
                    bl12 = true;
                    this.mFirePositiveReset = false;
                }
                this.mFireNegativeReset = true;
            } else {
                bl2 = bl;
                if (!this.mFireCrossReset) {
                    bl2 = true;
                    this.mFireCrossReset = true;
                }
                bl5 = bl8;
                if (this.mFireNegativeReset) {
                    bl5 = true;
                    this.mFireNegativeReset = false;
                }
                this.mFirePositiveReset = true;
                bl12 = bl9;
            }
            bl6 = bl2;
            bl7 = bl5;
        } else {
            float f2;
            float f3;
            if (this.mFireCrossReset) {
                f3 = this.mFireThreshold;
                if ((f - f3) * (this.mFireLastPos - f3) < 0.0f) {
                    bl2 = true;
                    this.mFireCrossReset = false;
                }
            } else {
                bl2 = bl3;
                if (Math.abs(f - this.mFireThreshold) > this.mTriggerSlack) {
                    this.mFireCrossReset = true;
                    bl2 = bl3;
                }
            }
            if (this.mFireNegativeReset) {
                f2 = this.mFireThreshold;
                f3 = f - f2;
                bl5 = bl6;
                if (f3 * (this.mFireLastPos - f2) < 0.0f) {
                    bl5 = bl6;
                    if (f3 < 0.0f) {
                        bl5 = true;
                        this.mFireNegativeReset = false;
                    }
                }
            } else {
                bl5 = bl7;
                if (Math.abs(f - this.mFireThreshold) > this.mTriggerSlack) {
                    this.mFireNegativeReset = true;
                    bl5 = bl7;
                }
            }
            if (this.mFirePositiveReset) {
                f3 = this.mFireThreshold;
                f2 = f - f3;
                bl12 = bl10;
                if (f2 * (this.mFireLastPos - f3) < 0.0f) {
                    bl12 = bl10;
                    if (f2 > 0.0f) {
                        bl12 = true;
                        this.mFirePositiveReset = false;
                    }
                }
                bl6 = bl2;
                bl7 = bl5;
            } else {
                bl6 = bl2;
                bl7 = bl5;
                bl12 = bl11;
                if (Math.abs(f - this.mFireThreshold) > this.mTriggerSlack) {
                    this.mFirePositiveReset = true;
                    bl12 = bl11;
                    bl7 = bl5;
                    bl6 = bl2;
                }
            }
        }
        this.mFireLastPos = f;
        if (bl7 || bl6 || bl12) {
            ((MotionLayout)view.getParent()).fireTrigger(this.mTriggerID, bl12, f);
        }
        if (this.mTriggerReceiver != UNSET) {
            view = ((MotionLayout)view.getParent()).findViewById(this.mTriggerReceiver);
        }
        if (bl7 && this.mNegativeCross != null) {
            if (this.mFireNegativeCross == null) {
                try {
                    this.mFireNegativeCross = view.getClass().getMethod(this.mNegativeCross, new Class[0]);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Could not find method \"");
                    stringBuilder.append(this.mNegativeCross);
                    stringBuilder.append("\"on class ");
                    stringBuilder.append(view.getClass().getSimpleName());
                    stringBuilder.append(" ");
                    stringBuilder.append(Debug.getName(view));
                    Log.e((String)"KeyTrigger", (String)stringBuilder.toString());
                }
            }
            try {
                this.mFireNegativeCross.invoke((Object)view, new Object[0]);
            }
            catch (Exception exception) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Exception in call \"");
                stringBuilder.append(this.mNegativeCross);
                stringBuilder.append("\"on class ");
                stringBuilder.append(view.getClass().getSimpleName());
                stringBuilder.append(" ");
                stringBuilder.append(Debug.getName(view));
                Log.e((String)"KeyTrigger", (String)stringBuilder.toString());
            }
        }
        if (bl12 && this.mPositiveCross != null) {
            if (this.mFirePositiveCross == null) {
                try {
                    this.mFirePositiveCross = view.getClass().getMethod(this.mPositiveCross, new Class[0]);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Could not find method \"");
                    stringBuilder.append(this.mPositiveCross);
                    stringBuilder.append("\"on class ");
                    stringBuilder.append(view.getClass().getSimpleName());
                    stringBuilder.append(" ");
                    stringBuilder.append(Debug.getName(view));
                    Log.e((String)"KeyTrigger", (String)stringBuilder.toString());
                }
            }
            try {
                this.mFirePositiveCross.invoke((Object)view, new Object[0]);
            }
            catch (Exception exception) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Exception in call \"");
                stringBuilder.append(this.mPositiveCross);
                stringBuilder.append("\"on class ");
                stringBuilder.append(view.getClass().getSimpleName());
                stringBuilder.append(" ");
                stringBuilder.append(Debug.getName(view));
                Log.e((String)"KeyTrigger", (String)stringBuilder.toString());
            }
        }
        if (bl6 && this.mCross != null) {
            if (this.mFireCross == null) {
                try {
                    this.mFireCross = view.getClass().getMethod(this.mCross, new Class[0]);
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Could not find method \"");
                    stringBuilder.append(this.mCross);
                    stringBuilder.append("\"on class ");
                    stringBuilder.append(view.getClass().getSimpleName());
                    stringBuilder.append(" ");
                    stringBuilder.append(Debug.getName(view));
                    Log.e((String)"KeyTrigger", (String)stringBuilder.toString());
                }
            }
            try {
                this.mFireCross.invoke((Object)view, new Object[0]);
            }
            catch (Exception exception) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Exception in call \"");
                stringBuilder2.append(this.mCross);
                stringBuilder2.append("\"on class ");
                stringBuilder2.append(view.getClass().getSimpleName());
                stringBuilder2.append(" ");
                stringBuilder2.append(Debug.getName(view));
                Log.e((String)"KeyTrigger", (String)stringBuilder2.toString());
            }
        }
    }

    @Override
    public void getAttributeNames(HashSet<String> hashSet) {
    }

    int getCurveFit() {
        return this.mCurveFit;
    }

    @Override
    public void load(Context context, AttributeSet attributeSet) {
        Loader.read(this, context.obtainStyledAttributes(attributeSet, R.styleable.KeyTrigger), context);
    }

    @Override
    public void setValue(String string2, Object object) {
    }

    private static class Loader {
        private static final int COLLISION = 9;
        private static final int CROSS = 4;
        private static final int FRAME_POS = 8;
        private static final int NEGATIVE_CROSS = 1;
        private static final int POSITIVE_CROSS = 2;
        private static final int POST_LAYOUT = 10;
        private static final int TARGET_ID = 7;
        private static final int TRIGGER_ID = 6;
        private static final int TRIGGER_RECEIVER = 11;
        private static final int TRIGGER_SLACK = 5;
        private static SparseIntArray mAttrMap;

        static {
            SparseIntArray sparseIntArray;
            mAttrMap = sparseIntArray = new SparseIntArray();
            sparseIntArray.append(R.styleable.KeyTrigger_framePosition, 8);
            mAttrMap.append(R.styleable.KeyTrigger_onCross, 4);
            mAttrMap.append(R.styleable.KeyTrigger_onNegativeCross, 1);
            mAttrMap.append(R.styleable.KeyTrigger_onPositiveCross, 2);
            mAttrMap.append(R.styleable.KeyTrigger_motionTarget, 7);
            mAttrMap.append(R.styleable.KeyTrigger_triggerId, 6);
            mAttrMap.append(R.styleable.KeyTrigger_triggerSlack, 5);
            mAttrMap.append(R.styleable.KeyTrigger_motion_triggerOnCollision, 9);
            mAttrMap.append(R.styleable.KeyTrigger_motion_postLayoutCollision, 10);
            mAttrMap.append(R.styleable.KeyTrigger_triggerReceiver, 11);
        }

        private Loader() {
        }

        public static void read(KeyTrigger keyTrigger, TypedArray typedArray, Context object) {
            int n = typedArray.getIndexCount();
            block12: for (int i = 0; i < n; ++i) {
                int n2 = typedArray.getIndex(i);
                switch (mAttrMap.get(n2)) {
                    default: {
                        break;
                    }
                    case 11: {
                        KeyTrigger.access$702(keyTrigger, typedArray.getResourceId(n2, keyTrigger.mTriggerReceiver));
                        break;
                    }
                    case 10: {
                        KeyTrigger.access$602(keyTrigger, typedArray.getBoolean(n2, keyTrigger.mPostLayout));
                        continue block12;
                    }
                    case 9: {
                        KeyTrigger.access$502(keyTrigger, typedArray.getResourceId(n2, keyTrigger.mTriggerCollisionId));
                        continue block12;
                    }
                    case 8: {
                        keyTrigger.mFramePosition = typedArray.getInteger(n2, keyTrigger.mFramePosition);
                        KeyTrigger.access$002(keyTrigger, ((float)keyTrigger.mFramePosition + 0.5f) / 100.0f);
                        continue block12;
                    }
                    case 7: {
                        if (MotionLayout.IS_IN_EDIT_MODE) {
                            keyTrigger.mTargetId = typedArray.getResourceId(n2, keyTrigger.mTargetId);
                            if (keyTrigger.mTargetId != -1) continue block12;
                            keyTrigger.mTargetString = typedArray.getString(n2);
                            continue block12;
                        }
                        if (typedArray.peekValue((int)n2).type == 3) {
                            keyTrigger.mTargetString = typedArray.getString(n2);
                            continue block12;
                        }
                        keyTrigger.mTargetId = typedArray.getResourceId(n2, keyTrigger.mTargetId);
                        continue block12;
                    }
                    case 6: {
                        KeyTrigger.access$402(keyTrigger, typedArray.getResourceId(n2, keyTrigger.mTriggerID));
                        continue block12;
                    }
                    case 5: {
                        keyTrigger.mTriggerSlack = typedArray.getFloat(n2, keyTrigger.mTriggerSlack);
                        continue block12;
                    }
                    case 4: {
                        KeyTrigger.access$302(keyTrigger, typedArray.getString(n2));
                        continue block12;
                    }
                    case 2: {
                        KeyTrigger.access$202(keyTrigger, typedArray.getString(n2));
                        continue block12;
                    }
                    case 1: {
                        KeyTrigger.access$102(keyTrigger, typedArray.getString(n2));
                        continue block12;
                    }
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("unused attribute 0x");
                ((StringBuilder)object).append(Integer.toHexString(n2));
                ((StringBuilder)object).append("   ");
                ((StringBuilder)object).append(mAttrMap.get(n2));
                Log.e((String)"KeyTrigger", (String)((StringBuilder)object).toString());
            }
        }
    }
}

