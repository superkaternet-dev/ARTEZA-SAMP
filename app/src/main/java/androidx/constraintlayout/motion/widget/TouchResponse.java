/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.RectF
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.Xml
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.R;
import androidx.core.widget.NestedScrollView;
import org.xmlpull.v1.XmlPullParser;

class TouchResponse {
    private static final boolean DEBUG = false;
    static final int FLAG_DISABLE_POST_SCROLL = 1;
    static final int FLAG_DISABLE_SCROLL = 2;
    private static final int SIDE_BOTTOM = 3;
    private static final int SIDE_END = 6;
    private static final int SIDE_LEFT = 1;
    private static final int SIDE_MIDDLE = 4;
    private static final int SIDE_RIGHT = 2;
    private static final int SIDE_START = 5;
    private static final int SIDE_TOP = 0;
    private static final String TAG = "TouchResponse";
    private static final float[][] TOUCH_DIRECTION;
    private static final int TOUCH_DOWN = 1;
    private static final int TOUCH_END = 5;
    private static final int TOUCH_LEFT = 2;
    private static final int TOUCH_RIGHT = 3;
    private static final float[][] TOUCH_SIDES;
    private static final int TOUCH_START = 4;
    private static final int TOUCH_UP = 0;
    private float[] mAnchorDpDt = new float[2];
    private float mDragScale = 1.0f;
    private boolean mDragStarted = false;
    private float mDragThreshold = 10.0f;
    private int mFlags = 0;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mLimitBoundsTo = -1;
    private float mMaxAcceleration = 1.2f;
    private float mMaxVelocity = 4.0f;
    private final MotionLayout mMotionLayout;
    private boolean mMoveWhenScrollAtTop = true;
    private int mOnTouchUp = 0;
    private int mTouchAnchorId = -1;
    private int mTouchAnchorSide = 0;
    private float mTouchAnchorX = 0.5f;
    private float mTouchAnchorY = 0.5f;
    private float mTouchDirectionX = 0.0f;
    private float mTouchDirectionY = 1.0f;
    private int mTouchRegionId = -1;
    private int mTouchSide = 0;

    static {
        TOUCH_SIDES = new float[][]{{0.5f, 0.0f}, {0.0f, 0.5f}, {1.0f, 0.5f}, {0.5f, 1.0f}, {0.5f, 0.5f}, {0.0f, 0.5f}, {1.0f, 0.5f}};
        float[] fArray = new float[]{0.0f, 1.0f};
        float[] fArray2 = new float[]{-1.0f, 0.0f};
        float[] fArray3 = new float[]{1.0f, 0.0f};
        TOUCH_DIRECTION = new float[][]{{0.0f, -1.0f}, fArray, {-1.0f, 0.0f}, {1.0f, 0.0f}, fArray2, fArray3};
    }

    TouchResponse(Context context, MotionLayout motionLayout, XmlPullParser xmlPullParser) {
        this.mMotionLayout = motionLayout;
        this.fillFromAttributeList(context, Xml.asAttributeSet((XmlPullParser)xmlPullParser));
    }

    private void fill(TypedArray typedArray) {
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; ++i) {
            float[][] fArray;
            int n2 = typedArray.getIndex(i);
            if (n2 == R.styleable.OnSwipe_touchAnchorId) {
                this.mTouchAnchorId = typedArray.getResourceId(n2, this.mTouchAnchorId);
                continue;
            }
            if (n2 == R.styleable.OnSwipe_touchAnchorSide) {
                this.mTouchAnchorSide = n2 = typedArray.getInt(n2, this.mTouchAnchorSide);
                fArray = TOUCH_SIDES;
                this.mTouchAnchorX = fArray[n2][0];
                this.mTouchAnchorY = fArray[n2][1];
                continue;
            }
            if (n2 == R.styleable.OnSwipe_dragDirection) {
                this.mTouchSide = n2 = typedArray.getInt(n2, this.mTouchSide);
                fArray = TOUCH_DIRECTION;
                this.mTouchDirectionX = fArray[n2][0];
                this.mTouchDirectionY = fArray[n2][1];
                continue;
            }
            if (n2 == R.styleable.OnSwipe_maxVelocity) {
                this.mMaxVelocity = typedArray.getFloat(n2, this.mMaxVelocity);
                continue;
            }
            if (n2 == R.styleable.OnSwipe_maxAcceleration) {
                this.mMaxAcceleration = typedArray.getFloat(n2, this.mMaxAcceleration);
                continue;
            }
            if (n2 == R.styleable.OnSwipe_moveWhenScrollAtTop) {
                this.mMoveWhenScrollAtTop = typedArray.getBoolean(n2, this.mMoveWhenScrollAtTop);
                continue;
            }
            if (n2 == R.styleable.OnSwipe_dragScale) {
                this.mDragScale = typedArray.getFloat(n2, this.mDragScale);
                continue;
            }
            if (n2 == R.styleable.OnSwipe_dragThreshold) {
                this.mDragThreshold = typedArray.getFloat(n2, this.mDragThreshold);
                continue;
            }
            if (n2 == R.styleable.OnSwipe_touchRegionId) {
                this.mTouchRegionId = typedArray.getResourceId(n2, this.mTouchRegionId);
                continue;
            }
            if (n2 == R.styleable.OnSwipe_onTouchUp) {
                this.mOnTouchUp = typedArray.getInt(n2, this.mOnTouchUp);
                continue;
            }
            if (n2 == R.styleable.OnSwipe_nestedScrollFlags) {
                this.mFlags = typedArray.getInteger(n2, 0);
                continue;
            }
            if (n2 != R.styleable.OnSwipe_limitBoundsTo) continue;
            this.mLimitBoundsTo = typedArray.getResourceId(n2, 0);
        }
    }

    private void fillFromAttributeList(Context context, AttributeSet attributeSet) {
        context = context.obtainStyledAttributes(attributeSet, R.styleable.OnSwipe);
        this.fill((TypedArray)context);
        context.recycle();
    }

    float dot(float f, float f2) {
        return this.mTouchDirectionX * f + this.mTouchDirectionY * f2;
    }

    public int getAnchorId() {
        return this.mTouchAnchorId;
    }

    public int getFlags() {
        return this.mFlags;
    }

    RectF getLimitBoundsTo(ViewGroup viewGroup, RectF rectF) {
        int n = this.mLimitBoundsTo;
        if (n == -1) {
            return null;
        }
        if ((viewGroup = viewGroup.findViewById(n)) == null) {
            return null;
        }
        rectF.set((float)viewGroup.getLeft(), (float)viewGroup.getTop(), (float)viewGroup.getRight(), (float)viewGroup.getBottom());
        return rectF;
    }

    int getLimitBoundsToId() {
        return this.mLimitBoundsTo;
    }

    float getMaxAcceleration() {
        return this.mMaxAcceleration;
    }

    public float getMaxVelocity() {
        return this.mMaxVelocity;
    }

    boolean getMoveWhenScrollAtTop() {
        return this.mMoveWhenScrollAtTop;
    }

    float getProgressDirection(float f, float f2) {
        float f3 = this.mMotionLayout.getProgress();
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, f3, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        f3 = this.mTouchDirectionX;
        if (f3 != 0.0f) {
            float[] fArray = this.mAnchorDpDt;
            if (fArray[0] == 0.0f) {
                fArray[0] = 1.0E-7f;
            }
            f = f3 * f / fArray[0];
        } else {
            float[] fArray = this.mAnchorDpDt;
            if (fArray[1] == 0.0f) {
                fArray[1] = 1.0E-7f;
            }
            f = this.mTouchDirectionY * f2 / fArray[1];
        }
        return f;
    }

    RectF getTouchRegion(ViewGroup viewGroup, RectF rectF) {
        int n = this.mTouchRegionId;
        if (n == -1) {
            return null;
        }
        if ((viewGroup = viewGroup.findViewById(n)) == null) {
            return null;
        }
        rectF.set((float)viewGroup.getLeft(), (float)viewGroup.getTop(), (float)viewGroup.getRight(), (float)viewGroup.getBottom());
        return rectF;
    }

    int getTouchRegionId() {
        return this.mTouchRegionId;
    }

    void processTouchEvent(MotionEvent object, MotionLayout.MotionTracker motionTracker, int n, MotionScene object2) {
        motionTracker.addMovement((MotionEvent)object);
        switch (object.getAction()) {
            default: {
                break;
            }
            case 2: {
                float f;
                float f2 = object.getRawY() - this.mLastTouchY;
                float f3 = object.getRawX() - this.mLastTouchX;
                if (!(Math.abs(this.mTouchDirectionX * f3 + this.mTouchDirectionY * f2) > this.mDragThreshold) && !this.mDragStarted) break;
                float f4 = this.mMotionLayout.getProgress();
                if (!this.mDragStarted) {
                    this.mDragStarted = true;
                    this.mMotionLayout.setProgress(f4);
                }
                if ((n = this.mTouchAnchorId) != -1) {
                    this.mMotionLayout.getAnchorDpDt(n, f4, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
                } else {
                    f = Math.min(this.mMotionLayout.getWidth(), this.mMotionLayout.getHeight());
                    object2 = this.mAnchorDpDt;
                    object2[1] = this.mTouchDirectionY * f;
                    object2[0] = this.mTouchDirectionX * f;
                }
                f = this.mTouchDirectionX;
                object2 = this.mAnchorDpDt;
                if ((double)Math.abs((f * object2[0] + this.mTouchDirectionY * object2[1]) * this.mDragScale) < 0.01) {
                    object2 = this.mAnchorDpDt;
                    object2[0] = 0.01f;
                    object2[1] = 0.01f;
                }
                f2 = this.mTouchDirectionX != 0.0f ? f3 / this.mAnchorDpDt[0] : (f2 /= this.mAnchorDpDt[1]);
                f2 = Math.max(Math.min(f4 + f2, 1.0f), 0.0f);
                if (f2 != this.mMotionLayout.getProgress()) {
                    this.mMotionLayout.setProgress(f2);
                    motionTracker.computeCurrentVelocity(1000);
                    f2 = motionTracker.getXVelocity();
                    f4 = motionTracker.getYVelocity();
                    f2 = this.mTouchDirectionX != 0.0f ? (f2 /= this.mAnchorDpDt[0]) : f4 / this.mAnchorDpDt[1];
                    this.mMotionLayout.mLastVelocity = f2;
                } else {
                    this.mMotionLayout.mLastVelocity = 0.0f;
                }
                this.mLastTouchX = object.getRawX();
                this.mLastTouchY = object.getRawY();
                break;
            }
            case 1: {
                float f;
                this.mDragStarted = false;
                motionTracker.computeCurrentVelocity(1000);
                float f5 = motionTracker.getXVelocity();
                float f6 = motionTracker.getYVelocity();
                float f7 = this.mMotionLayout.getProgress();
                n = this.mTouchAnchorId;
                if (n != -1) {
                    this.mMotionLayout.getAnchorDpDt(n, f7, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
                } else {
                    f = Math.min(this.mMotionLayout.getWidth(), this.mMotionLayout.getHeight());
                    object = this.mAnchorDpDt;
                    object[1] = (MotionEvent)(this.mTouchDirectionY * f);
                    object[0] = (MotionEvent)(this.mTouchDirectionX * f);
                }
                f = this.mTouchDirectionX;
                object = this.mAnchorDpDt;
                Object object3 = object[0];
                object3 = this.mTouchDirectionY;
                object3 = object[1];
                f5 = f != 0.0f ? (f5 /= object[0]) : f6 / object[1];
                f6 = !Float.isNaN(f5) ? f7 + f5 / 3.0f : f7;
                if (f6 != 0.0f && f6 != 1.0f && (n = this.mOnTouchUp) != 3) {
                    object = this.mMotionLayout;
                    f6 = (double)f6 < 0.5 ? 0.0f : 1.0f;
                    ((MotionLayout)object).touchAnimateTo(n, f6, f5);
                    if (!(0.0f >= f7) && !(1.0f <= f7)) break;
                    this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
                    break;
                }
                if (!(0.0f >= f6) && !(1.0f <= f6)) break;
                this.mMotionLayout.setState(MotionLayout.TransitionState.FINISHED);
                break;
            }
            case 0: {
                this.mLastTouchX = object.getRawX();
                this.mLastTouchY = object.getRawY();
                this.mDragStarted = false;
            }
        }
    }

    void scrollMove(float f, float f2) {
        float f3 = this.mTouchDirectionX;
        f3 = this.mTouchDirectionY;
        f3 = this.mMotionLayout.getProgress();
        if (!this.mDragStarted) {
            this.mDragStarted = true;
            this.mMotionLayout.setProgress(f3);
        }
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, f3, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        float f4 = this.mTouchDirectionX;
        float[] fArray = this.mAnchorDpDt;
        if ((double)Math.abs(f4 * fArray[0] + this.mTouchDirectionY * fArray[1]) < 0.01) {
            fArray = this.mAnchorDpDt;
            fArray[0] = 0.01f;
            fArray[1] = 0.01f;
        }
        f = (f4 = this.mTouchDirectionX) != 0.0f ? f4 * f / this.mAnchorDpDt[0] : this.mTouchDirectionY * f2 / this.mAnchorDpDt[1];
        if ((f = Math.max(Math.min(f3 + f, 1.0f), 0.0f)) != this.mMotionLayout.getProgress()) {
            this.mMotionLayout.setProgress(f);
        }
    }

    void scrollUp(float f, float f2) {
        boolean bl = false;
        this.mDragStarted = false;
        float f3 = this.mMotionLayout.getProgress();
        this.mMotionLayout.getAnchorDpDt(this.mTouchAnchorId, f3, this.mTouchAnchorX, this.mTouchAnchorY, this.mAnchorDpDt);
        float f4 = this.mTouchDirectionX;
        Object object = this.mAnchorDpDt;
        float f5 = object[0];
        float f6 = this.mTouchDirectionY;
        f5 = object[1];
        f5 = 0.0f;
        f = f4 != 0.0f ? f4 * f / object[0] : f6 * f2 / object[1];
        f2 = f3;
        if (!Float.isNaN(f)) {
            f2 = f3 + f / 3.0f;
        }
        if (f2 != 0.0f) {
            boolean bl2 = f2 != 1.0f;
            int n = this.mOnTouchUp;
            if (n != 3) {
                bl = true;
            }
            if (bl & bl2) {
                object = this.mMotionLayout;
                f2 = (double)f2 < 0.5 ? f5 : 1.0f;
                ((MotionLayout)object).touchAnimateTo(n, f2, f);
            }
        }
    }

    public void setAnchorId(int n) {
        this.mTouchAnchorId = n;
    }

    void setDown(float f, float f2) {
        this.mLastTouchX = f;
        this.mLastTouchY = f2;
    }

    public void setMaxAcceleration(float f) {
        this.mMaxAcceleration = f;
    }

    public void setMaxVelocity(float f) {
        this.mMaxVelocity = f;
    }

    public void setRTL(boolean bl) {
        float[][] fArray;
        if (bl) {
            fArray = TOUCH_DIRECTION;
            fArray[4] = fArray[3];
            fArray[5] = fArray[2];
            fArray = TOUCH_SIDES;
            fArray[5] = fArray[2];
            fArray[6] = fArray[1];
        } else {
            fArray = TOUCH_DIRECTION;
            fArray[4] = fArray[2];
            fArray[5] = fArray[3];
            fArray = TOUCH_SIDES;
            fArray[5] = fArray[1];
            fArray[6] = fArray[2];
        }
        fArray = TOUCH_SIDES;
        int n = this.mTouchAnchorSide;
        this.mTouchAnchorX = fArray[n][0];
        this.mTouchAnchorY = fArray[n][1];
        fArray = TOUCH_DIRECTION;
        n = this.mTouchSide;
        this.mTouchDirectionX = fArray[n][0];
        this.mTouchDirectionY = fArray[n][1];
    }

    public void setTouchAnchorLocation(float f, float f2) {
        this.mTouchAnchorX = f;
        this.mTouchAnchorY = f2;
    }

    void setUpTouchEvent(float f, float f2) {
        this.mLastTouchX = f;
        this.mLastTouchY = f2;
        this.mDragStarted = false;
    }

    void setupTouch() {
        Object object = null;
        int n = this.mTouchAnchorId;
        if (n != -1) {
            View view = this.mMotionLayout.findViewById(n);
            object = view;
            if (view == null) {
                object = new StringBuilder();
                ((StringBuilder)object).append("cannot find TouchAnchorId @id/");
                ((StringBuilder)object).append(Debug.getName(this.mMotionLayout.getContext(), this.mTouchAnchorId));
                Log.e((String)TAG, (String)((StringBuilder)object).toString());
                object = view;
            }
        }
        if (object instanceof NestedScrollView) {
            object = (NestedScrollView)object;
            object.setOnTouchListener(new View.OnTouchListener(this){
                final TouchResponse this$0;
                {
                    this.this$0 = touchResponse;
                }

                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return false;
                }
            });
            ((NestedScrollView)object).setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(this){
                final TouchResponse this$0;
                {
                    this.this$0 = touchResponse;
                }

                @Override
                public void onScrollChange(NestedScrollView nestedScrollView, int n, int n2, int n3, int n4) {
                }
            });
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mTouchDirectionX);
        stringBuilder.append(" , ");
        stringBuilder.append(this.mTouchDirectionY);
        return stringBuilder.toString();
    }
}

