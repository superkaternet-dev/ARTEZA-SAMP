/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.Animatable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintHelper;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.R;

public class MotionHelper
extends ConstraintHelper
implements Animatable,
MotionLayout.TransitionListener {
    private float mProgress;
    private boolean mUseOnHide = false;
    private boolean mUseOnShow = false;
    protected View[] views;

    public MotionHelper(Context context) {
        super(context);
    }

    public MotionHelper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(attributeSet);
    }

    public MotionHelper(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init(attributeSet);
    }

    @Override
    public float getProgress() {
        return this.mProgress;
    }

    @Override
    protected void init(AttributeSet attributeSet) {
        super.init(attributeSet);
        if (attributeSet != null) {
            attributeSet = this.getContext().obtainStyledAttributes(attributeSet, R.styleable.MotionHelper);
            int n = attributeSet.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = attributeSet.getIndex(i);
                if (n2 == R.styleable.MotionHelper_onShow) {
                    this.mUseOnShow = attributeSet.getBoolean(n2, this.mUseOnShow);
                    continue;
                }
                if (n2 != R.styleable.MotionHelper_onHide) continue;
                this.mUseOnHide = attributeSet.getBoolean(n2, this.mUseOnHide);
            }
            attributeSet.recycle();
        }
    }

    public boolean isUseOnHide() {
        return this.mUseOnHide;
    }

    public boolean isUsedOnShow() {
        return this.mUseOnShow;
    }

    @Override
    public void onTransitionChange(MotionLayout motionLayout, int n, int n2, float f) {
    }

    @Override
    public void onTransitionCompleted(MotionLayout motionLayout, int n) {
    }

    @Override
    public void onTransitionStarted(MotionLayout motionLayout, int n, int n2) {
    }

    @Override
    public void onTransitionTrigger(MotionLayout motionLayout, int n, boolean bl, float f) {
    }

    @Override
    public void setProgress(float f) {
        this.mProgress = f;
        if (this.mCount > 0) {
            this.views = this.getViews((ConstraintLayout)this.getParent());
            for (int i = 0; i < this.mCount; ++i) {
                this.setProgress(this.views[i], f);
            }
        } else {
            ViewGroup viewGroup = (ViewGroup)this.getParent();
            int n = viewGroup.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view = viewGroup.getChildAt(i);
                if (view instanceof MotionHelper) continue;
                this.setProgress(view, f);
            }
        }
    }

    public void setProgress(View view, float f) {
    }
}

