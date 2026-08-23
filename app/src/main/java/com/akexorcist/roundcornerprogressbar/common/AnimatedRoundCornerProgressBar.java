/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.util.AttributeSet
 *  android.widget.LinearLayout
 */
package com.akexorcist.roundcornerprogressbar.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.customview.view.AbsSavedState;
import com.akexorcist.roundcornerprogressbar.R;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;

public abstract class AnimatedRoundCornerProgressBar
extends BaseRoundCornerProgressBar {
    public static final long DEFAULT_DURATION = 500L;
    private float animationSpeedScale;
    private boolean isAnimationEnabled;
    private boolean isProgressAnimating = false;
    private boolean isSecondaryProgressAnimating = false;
    private float lastProgress;
    private float lastSecondaryProgress;
    private AnimatorListenerAdapter progressAnimationAdapterListener;
    private ValueAnimator.AnimatorUpdateListener progressAnimationUpdateListener = new ValueAnimator.AnimatorUpdateListener(this){
        final AnimatedRoundCornerProgressBar this$0;
        {
            this.this$0 = animatedRoundCornerProgressBar;
        }

        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            this.this$0.onUpdateProgressByAnimation(((Float)valueAnimator.getAnimatedValue()).floatValue());
        }
    };
    private ValueAnimator progressAnimator;
    private AnimatorListenerAdapter secondaryProgressAnimationAdapterListener;
    private ValueAnimator.AnimatorUpdateListener secondaryProgressAnimationUpdateListener;
    private ValueAnimator secondaryProgressAnimator;

    public AnimatedRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.progressAnimationAdapterListener = new AnimatorListenerAdapter(this){
            final AnimatedRoundCornerProgressBar this$0;
            {
                this.this$0 = animatedRoundCornerProgressBar;
            }

            public void onAnimationCancel(Animator animator2) {
                AnimatedRoundCornerProgressBar.access$102(this.this$0, false);
            }

            public void onAnimationEnd(Animator animator2) {
                AnimatedRoundCornerProgressBar.access$102(this.this$0, false);
                this.this$0.onProgressAnimationEnd();
            }
        };
        this.secondaryProgressAnimationUpdateListener = new ValueAnimator.AnimatorUpdateListener(this){
            final AnimatedRoundCornerProgressBar this$0;
            {
                this.this$0 = animatedRoundCornerProgressBar;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                this.this$0.onUpdateSecondaryProgressByAnimation(((Float)valueAnimator.getAnimatedValue()).floatValue());
            }
        };
        this.secondaryProgressAnimationAdapterListener = new AnimatorListenerAdapter(this){
            final AnimatedRoundCornerProgressBar this$0;
            {
                this.this$0 = animatedRoundCornerProgressBar;
            }

            public void onAnimationCancel(Animator animator2) {
                AnimatedRoundCornerProgressBar.access$402(this.this$0, false);
            }

            public void onAnimationEnd(Animator animator2) {
                AnimatedRoundCornerProgressBar.access$402(this.this$0, false);
                this.this$0.onSecondaryProgressAnimationEnd();
            }
        };
    }

    public AnimatedRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.progressAnimationAdapterListener = new /* invalid duplicate definition of identical inner class */;
        this.secondaryProgressAnimationUpdateListener = new /* invalid duplicate definition of identical inner class */;
        this.secondaryProgressAnimationAdapterListener = new /* invalid duplicate definition of identical inner class */;
    }

    public AnimatedRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.progressAnimationAdapterListener = new /* invalid duplicate definition of identical inner class */;
        this.secondaryProgressAnimationUpdateListener = new /* invalid duplicate definition of identical inner class */;
        this.secondaryProgressAnimationAdapterListener = new /* invalid duplicate definition of identical inner class */;
    }

    static /* synthetic */ boolean access$102(AnimatedRoundCornerProgressBar animatedRoundCornerProgressBar, boolean bl) {
        animatedRoundCornerProgressBar.isProgressAnimating = bl;
        return bl;
    }

    static /* synthetic */ boolean access$402(AnimatedRoundCornerProgressBar animatedRoundCornerProgressBar, boolean bl) {
        animatedRoundCornerProgressBar.isSecondaryProgressAnimating = bl;
        return bl;
    }

    private void clearProgressAnimation() {
        ValueAnimator valueAnimator = this.progressAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.progressAnimator.cancel();
        }
    }

    private void clearSecondaryProgressAnimation() {
        ValueAnimator valueAnimator = this.secondaryProgressAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.secondaryProgressAnimator.cancel();
        }
    }

    private long getAnimationDuration(float f, float f2, float f3, float f4) {
        return (long)(500.0f * Math.abs(f - f2) / f3 * f4);
    }

    private void onProgressAnimationEnd() {
        this.onProgressChangeAnimationEnd(this.layoutProgress);
    }

    private void onSecondaryProgressAnimationEnd() {
        this.onProgressChangeAnimationEnd(this.layoutSecondaryProgress);
    }

    private void onUpdateProgressByAnimation(float f) {
        super.setProgress(f);
        this.onProgressChangeAnimationUpdate(this.layoutProgress, f, this.lastProgress);
    }

    private void onUpdateSecondaryProgressByAnimation(float f) {
        super.setSecondaryProgress(f);
        this.onProgressChangeAnimationUpdate(this.layoutSecondaryProgress, f, this.lastProgress);
    }

    private void restoreProgressAnimationState() {
        if (this.isProgressAnimating) {
            this.startProgressAnimation(super.getProgress(), this.lastProgress);
        }
    }

    private void restoreSecondaryProgressAnimationState() {
        if (this.isSecondaryProgressAnimating) {
            this.startSecondaryProgressAnimation(super.getSecondaryProgress(), this.lastSecondaryProgress);
        }
    }

    private void startProgressAnimation(float f, float f2) {
        this.isProgressAnimating = true;
        ValueAnimator valueAnimator = this.progressAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeUpdateListener(this.progressAnimationUpdateListener);
            this.progressAnimator.removeListener((Animator.AnimatorListener)this.progressAnimationAdapterListener);
            this.progressAnimator.cancel();
            this.progressAnimator = null;
        }
        this.progressAnimator = valueAnimator = ValueAnimator.ofFloat((float[])new float[]{f, f2});
        valueAnimator.setDuration(this.getAnimationDuration(f, f2, this.max, this.animationSpeedScale));
        this.progressAnimator.addUpdateListener(this.progressAnimationUpdateListener);
        this.progressAnimator.addListener((Animator.AnimatorListener)this.progressAnimationAdapterListener);
        this.progressAnimator.start();
    }

    private void startSecondaryProgressAnimation(float f, float f2) {
        this.isSecondaryProgressAnimating = true;
        ValueAnimator valueAnimator = this.secondaryProgressAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeUpdateListener(this.secondaryProgressAnimationUpdateListener);
            this.secondaryProgressAnimator.removeListener((Animator.AnimatorListener)this.secondaryProgressAnimationAdapterListener);
            this.secondaryProgressAnimator.cancel();
            this.secondaryProgressAnimator = null;
        }
        this.secondaryProgressAnimator = valueAnimator = ValueAnimator.ofFloat((float[])new float[]{f, f2});
        valueAnimator.setDuration(this.getAnimationDuration(f, f2, this.max, this.animationSpeedScale));
        this.secondaryProgressAnimator.addUpdateListener(this.secondaryProgressAnimationUpdateListener);
        this.secondaryProgressAnimator.addListener((Animator.AnimatorListener)this.secondaryProgressAnimationAdapterListener);
        this.secondaryProgressAnimator.start();
    }

    public void disableAnimation() {
        this.isAnimationEnabled = false;
    }

    public void enableAnimation() {
        this.isAnimationEnabled = true;
    }

    public float getAnimationSpeedScale() {
        return this.animationSpeedScale;
    }

    @Override
    public float getProgress() {
        if (!this.isAnimationEnabled && !this.isProgressAnimating) {
            return super.getProgress();
        }
        return this.lastProgress;
    }

    @Override
    public float getSecondaryProgress() {
        if (!this.isAnimationEnabled && !this.isSecondaryProgressAnimating) {
            return super.getSecondaryProgress();
        }
        return this.lastSecondaryProgress;
    }

    public boolean isProgressAnimating() {
        return this.isProgressAnimating;
    }

    public boolean isSecondaryProgressAnimating() {
        return this.isSecondaryProgressAnimating;
    }

    protected void onProgressChangeAnimationEnd(LinearLayout linearLayout) {
    }

    protected void onProgressChangeAnimationUpdate(LinearLayout linearLayout, float f, float f2) {
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.isProgressAnimating = parcelable.isProgressAnimating;
        this.isSecondaryProgressAnimating = parcelable.isSecondaryProgressAnimating;
        this.lastProgress = parcelable.lastProgress;
        this.lastSecondaryProgress = parcelable.lastSecondaryProgress;
        this.animationSpeedScale = parcelable.animationSpeedScale;
        this.isAnimationEnabled = parcelable.isAnimationEnabled;
        this.restoreProgressAnimationState();
        this.restoreSecondaryProgressAnimationState();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.isProgressAnimating = this.isProgressAnimating;
        savedState.isSecondaryProgressAnimating = this.isSecondaryProgressAnimating;
        savedState.lastProgress = this.lastProgress;
        savedState.lastSecondaryProgress = this.lastSecondaryProgress;
        savedState.animationSpeedScale = this.animationSpeedScale;
        savedState.isAnimationEnabled = this.isAnimationEnabled;
        return savedState;
    }

    public void setAnimationSpeedScale(float f) {
        this.animationSpeedScale = Math.max(Math.min(f, 5.0f), 0.2f);
    }

    @Override
    public void setProgress(float f) {
        f = f < 0.0f ? 0.0f : Math.min(f, this.max);
        this.clearProgressAnimation();
        this.lastProgress = f;
        if (this.isAnimationEnabled) {
            this.startProgressAnimation(super.getProgress(), f);
        } else {
            super.setProgress(f);
        }
    }

    @Override
    public void setProgress(int n) {
        this.setProgress((float)n);
    }

    @Override
    public void setSecondaryProgress(float f) {
        f = f < 0.0f ? 0.0f : Math.min(f, this.max);
        this.clearSecondaryProgressAnimation();
        this.lastSecondaryProgress = f;
        if (this.isAnimationEnabled) {
            this.startSecondaryProgressAnimation(super.getSecondaryProgress(), f);
        } else {
            super.setSecondaryProgress(f);
        }
    }

    @Override
    public void setSecondaryProgress(int n) {
        this.setSecondaryProgress((float)n);
    }

    @Override
    public void setupStyleable(Context context, AttributeSet attributeSet) {
        super.setupStyleable(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, R.styleable.AnimatedRoundCornerProgressBar);
        this.isAnimationEnabled = context.getBoolean(R.styleable.AnimatedRoundCornerProgressBar_rcAnimationEnable, false);
        this.animationSpeedScale = context.getFloat(R.styleable.AnimatedRoundCornerProgressBar_rcAnimationSpeedScale, 1.0f);
        context.recycle();
        this.lastProgress = super.getProgress();
        this.lastSecondaryProgress = super.getSecondaryProgress();
    }

    protected void stopProgressAnimationImmediately() {
        this.clearProgressAnimation();
        this.clearSecondaryProgressAnimation();
        if (this.isAnimationEnabled && this.isProgressAnimating) {
            this.disableAnimation();
            if (this.isProgressAnimating) {
                super.setProgress(this.lastProgress);
            }
            if (this.isSecondaryProgressAnimating) {
                super.setSecondaryProgress(this.lastProgress);
            }
            this.enableAnimation();
        }
    }

    protected static class SavedState
    extends AbsSavedState {
        public static final Parcelable.ClassLoaderCreator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        float animationSpeedScale;
        boolean isAnimationEnabled;
        boolean isProgressAnimating;
        boolean isSecondaryProgressAnimating;
        float lastProgress;
        float lastSecondaryProgress;

        SavedState(Parcel parcel) {
            this(parcel, null);
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            byte by = parcel.readByte();
            boolean bl = true;
            boolean bl2 = by != 0;
            this.isProgressAnimating = bl2;
            bl2 = parcel.readByte() != 0;
            this.isSecondaryProgressAnimating = bl2;
            this.lastProgress = parcel.readFloat();
            this.lastSecondaryProgress = parcel.readFloat();
            this.animationSpeedScale = parcel.readFloat();
            bl2 = parcel.readByte() != 0 ? bl : false;
            this.isAnimationEnabled = bl2;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeByte((byte)(this.isProgressAnimating ? 1 : 0));
            parcel.writeByte((byte)(this.isSecondaryProgressAnimating ? 1 : 0));
            parcel.writeFloat(this.lastProgress);
            parcel.writeFloat(this.lastSecondaryProgress);
            parcel.writeFloat(this.animationSpeedScale);
            parcel.writeByte((byte)(this.isAnimationEnabled ? 1 : 0));
        }
    }
}

