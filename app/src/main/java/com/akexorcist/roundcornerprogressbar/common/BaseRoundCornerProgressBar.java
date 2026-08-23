/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.GradientDrawable$Orientation
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.util.AttributeSet
 *  android.util.DisplayMetrics
 *  android.util.TypedValue
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.LinearLayout
 *  android.widget.RelativeLayout$LayoutParams
 */
package com.akexorcist.roundcornerprogressbar.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.customview.view.AbsSavedState;
import com.akexorcist.roundcornerprogressbar.R;

public abstract class BaseRoundCornerProgressBar
extends LinearLayout {
    protected static final int DEFAULT_BACKGROUND_PADDING = 0;
    protected static final int DEFAULT_MAX_PROGRESS = 100;
    protected static final int DEFAULT_PROGRESS = 0;
    protected static final int DEFAULT_PROGRESS_RADIUS = 30;
    protected static final int DEFAULT_SECONDARY_PROGRESS = 0;
    protected int backgroundColor;
    protected boolean isReverse;
    protected LinearLayout layoutBackground;
    protected LinearLayout layoutProgress;
    protected LinearLayout layoutSecondaryProgress;
    protected float max;
    protected int padding;
    protected float progress;
    protected OnProgressChangedListener progressChangedListener;
    private int progressColor;
    protected int[] progressColors;
    protected GradientDrawable progressDrawable;
    protected int radius;
    protected float secondaryProgress;
    protected int secondaryProgressColor;
    protected int[] secondaryProgressColors;
    protected GradientDrawable secondaryProgressDrawable;
    protected int totalWidth;

    public BaseRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setup(context, attributeSet);
    }

    public BaseRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.setup(context, attributeSet);
    }

    public BaseRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.setup(context, attributeSet);
    }

    private void drawBackgroundProgress() {
        GradientDrawable gradientDrawable = this.createGradientDrawable(this.backgroundColor);
        int n = this.radius - this.padding / 2;
        gradientDrawable.setCornerRadii(new float[]{n, n, n, n, n, n, n, n});
        this.layoutBackground.setBackground((Drawable)gradientDrawable);
    }

    private void drawPadding() {
        LinearLayout linearLayout = this.layoutBackground;
        int n = this.padding;
        linearLayout.setPadding(n, n, n, n);
    }

    private void drawPrimaryProgress() {
        int n = Math.min(this.radius, this.layoutBackground.getMeasuredHeight() / 2);
        this.drawProgress(this.layoutProgress, this.progressDrawable, this.max, this.progress, this.totalWidth, n, this.padding, this.isReverse);
    }

    private void drawProgressReverse() {
        this.setupProgressReversing(this.layoutProgress, this.isReverse);
        this.setupProgressReversing(this.layoutSecondaryProgress, this.isReverse);
    }

    private void drawSecondaryProgress() {
        int n = Math.min(this.radius, this.layoutBackground.getMeasuredHeight() / 2);
        this.drawProgress(this.layoutSecondaryProgress, this.secondaryProgressDrawable, this.max, this.secondaryProgress, this.totalWidth, n, this.padding, this.isReverse);
    }

    private void removeLayoutParamsRule(RelativeLayout.LayoutParams layoutParams) {
        if (Build.VERSION.SDK_INT >= 17) {
            layoutParams.removeRule(11);
            layoutParams.removeRule(21);
            layoutParams.removeRule(9);
            layoutParams.removeRule(20);
        } else {
            layoutParams.addRule(11, 0);
            layoutParams.addRule(9, 0);
        }
    }

    private void setupProgressReversing(LinearLayout linearLayout, boolean bl) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)linearLayout.getLayoutParams();
        this.removeLayoutParamsRule(layoutParams);
        if (bl) {
            layoutParams.addRule(11);
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.addRule(21);
            }
        } else {
            layoutParams.addRule(9);
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.addRule(20);
            }
        }
        linearLayout.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    private void updateProgressDrawable() {
        int[] nArray;
        int n = this.progressColor;
        this.progressDrawable = n != -1 ? this.createGradientDrawable(n) : ((nArray = this.progressColors) != null && nArray.length > 0 ? this.createGradientDrawable(nArray) : this.createGradientDrawable(this.getResources().getColor(R.color.round_corner_progress_bar_progress_default)));
    }

    private void updateSecondaryProgressDrawable() {
        int[] nArray;
        int n = this.secondaryProgressColor;
        this.secondaryProgressDrawable = n != -1 ? this.createGradientDrawable(n) : ((nArray = this.secondaryProgressColors) != null && nArray.length > 0 ? this.createGradientDrawable(nArray) : this.createGradientDrawable(this.getResources().getColor(R.color.round_corner_progress_bar_secondary_progress_default)));
    }

    protected GradientDrawable createGradientDrawable(int n) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        gradientDrawable.setColor(n);
        return gradientDrawable;
    }

    protected GradientDrawable createGradientDrawable(int[] nArray) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(0);
        GradientDrawable.Orientation orientation = !this.isReverse() ? GradientDrawable.Orientation.LEFT_RIGHT : GradientDrawable.Orientation.RIGHT_LEFT;
        gradientDrawable.setOrientation(orientation);
        gradientDrawable.setColors(nArray);
        return gradientDrawable;
    }

    protected float dp2px(float f) {
        return TypedValue.applyDimension((int)1, (float)f, (DisplayMetrics)this.getContext().getResources().getDisplayMetrics());
    }

    protected void drawAll() {
        this.drawBackgroundProgress();
        this.drawPadding();
        this.drawProgressReverse();
        this.drawPrimaryProgress();
        this.drawSecondaryProgress();
        this.onViewDraw();
    }

    protected abstract void drawProgress(LinearLayout var1, GradientDrawable var2, float var3, float var4, float var5, int var6, int var7, boolean var8);

    public float getLayoutWidth() {
        return this.totalWidth;
    }

    public float getMax() {
        return this.max;
    }

    public int getPadding() {
        return this.padding;
    }

    public float getProgress() {
        return this.progress;
    }

    public int getProgressBackgroundColor() {
        return this.backgroundColor;
    }

    public int getProgressColor() {
        return this.progressColor;
    }

    public int[] getProgressColors() {
        return this.progressColors;
    }

    public int getRadius() {
        return this.radius;
    }

    public float getSecondaryProgress() {
        return this.secondaryProgress;
    }

    public int getSecondaryProgressColor() {
        return this.secondaryProgressColor;
    }

    public int[] getSecondaryProgressColors() {
        return this.secondaryProgressColors;
    }

    public float getSecondaryProgressWidth() {
        LinearLayout linearLayout = this.layoutSecondaryProgress;
        if (linearLayout != null) {
            return linearLayout.getWidth();
        }
        return 0.0f;
    }

    protected abstract int initLayout();

    protected abstract void initStyleable(Context var1, AttributeSet var2);

    protected abstract void initView();

    public void invalidate() {
        super.invalidate();
        this.drawAll();
    }

    public boolean isReverse() {
        return this.isReverse;
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.max = parcelable.max;
        this.progress = parcelable.progress;
        this.secondaryProgress = parcelable.secondaryProgress;
        this.radius = parcelable.radius;
        this.padding = parcelable.padding;
        this.backgroundColor = parcelable.colorBackground;
        this.progressColor = parcelable.colorProgress;
        this.secondaryProgressColor = parcelable.colorSecondaryProgress;
        this.progressColors = parcelable.colorProgressArray;
        this.secondaryProgressColors = parcelable.colorSecondaryProgressArray;
        this.isReverse = parcelable.isReverse;
        this.updateProgressDrawable();
        this.updateSecondaryProgressDrawable();
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.max = this.max;
        savedState.progress = this.progress;
        savedState.secondaryProgress = this.secondaryProgress;
        savedState.radius = this.radius;
        savedState.padding = this.padding;
        savedState.colorBackground = this.backgroundColor;
        savedState.colorProgress = this.progressColor;
        savedState.colorSecondaryProgress = this.secondaryProgressColor;
        savedState.colorProgressArray = this.progressColors;
        savedState.colorSecondaryProgressArray = this.secondaryProgressColors;
        savedState.isReverse = this.isReverse;
        return savedState;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        this.totalWidth = n;
        this.drawBackgroundProgress();
        this.drawPadding();
        this.drawProgressReverse();
        this.post(new Runnable(this){
            final BaseRoundCornerProgressBar this$0;
            {
                this.this$0 = baseRoundCornerProgressBar;
            }

            @Override
            public void run() {
                this.this$0.drawPrimaryProgress();
                this.this$0.drawSecondaryProgress();
            }
        });
        this.onViewDraw();
    }

    protected abstract void onViewDraw();

    public void setMax(float f) {
        if (f >= 0.0f) {
            this.max = f;
        }
        if (this.progress > f) {
            this.progress = f;
        }
        this.drawPrimaryProgress();
        this.drawSecondaryProgress();
    }

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.progressChangedListener = onProgressChangedListener;
    }

    public void setPadding(int n) {
        if (n >= 0) {
            this.padding = n;
        }
        this.drawPadding();
        this.drawPrimaryProgress();
        this.drawSecondaryProgress();
    }

    public void setProgress(float f) {
        this.progress = f < 0.0f ? 0.0f : Math.min(f, this.max);
        this.drawPrimaryProgress();
        OnProgressChangedListener onProgressChangedListener = this.progressChangedListener;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onProgressChanged((View)this, this.progress, true, false);
        }
    }

    public void setProgress(int n) {
        this.setProgress((float)n);
    }

    public void setProgressBackgroundColor(int n) {
        this.backgroundColor = n;
        this.drawBackgroundProgress();
    }

    public void setProgressColor(int n) {
        this.progressColor = n;
        this.progressColors = null;
        this.updateProgressDrawable();
        this.drawPrimaryProgress();
    }

    public void setProgressColors(int[] nArray) {
        this.progressColor = -1;
        this.progressColors = nArray;
        this.updateProgressDrawable();
        this.drawPrimaryProgress();
    }

    public void setRadius(int n) {
        if (n >= 0) {
            this.radius = n;
        }
        this.drawBackgroundProgress();
        this.drawPrimaryProgress();
        this.drawSecondaryProgress();
    }

    public void setReverse(boolean bl) {
        this.isReverse = bl;
        this.drawProgressReverse();
        this.drawPrimaryProgress();
        this.drawSecondaryProgress();
    }

    public void setSecondaryProgress(float f) {
        this.secondaryProgress = f < 0.0f ? 0.0f : Math.min(f, this.max);
        this.drawSecondaryProgress();
        OnProgressChangedListener onProgressChangedListener = this.progressChangedListener;
        if (onProgressChangedListener != null) {
            onProgressChangedListener.onProgressChanged((View)this, this.secondaryProgress, false, true);
        }
    }

    public void setSecondaryProgress(int n) {
        this.setSecondaryProgress((float)n);
    }

    public void setSecondaryProgressColor(int n) {
        this.secondaryProgressColor = n;
        this.secondaryProgressColors = null;
        this.updateSecondaryProgressDrawable();
        this.drawSecondaryProgress();
    }

    public void setSecondaryProgressColors(int[] nArray) {
        this.secondaryProgressColor = -1;
        this.secondaryProgressColors = nArray;
        this.updateSecondaryProgressDrawable();
        this.drawSecondaryProgress();
    }

    public void setup(Context context, AttributeSet attributeSet) {
        this.setupStyleable(context, attributeSet);
        this.removeAllViews();
        LayoutInflater.from((Context)context).inflate(this.initLayout(), (ViewGroup)this);
        this.layoutBackground = (LinearLayout)this.findViewById(R.id.layout_background);
        this.layoutProgress = (LinearLayout)this.findViewById(R.id.layout_progress);
        this.layoutSecondaryProgress = (LinearLayout)this.findViewById(R.id.layout_secondary_progress);
        this.initView();
    }

    public void setupStyleable(Context context, AttributeSet attributeSet) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BaseRoundCornerProgressBar);
        this.radius = (int)typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcRadius, this.dp2px(30.0f));
        this.padding = (int)typedArray.getDimension(R.styleable.BaseRoundCornerProgressBar_rcBackgroundPadding, this.dp2px(0.0f));
        this.isReverse = typedArray.getBoolean(R.styleable.BaseRoundCornerProgressBar_rcReverse, false);
        this.max = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcMax, 100.0f);
        this.progress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcProgress, 0.0f);
        this.secondaryProgress = typedArray.getFloat(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgress, 0.0f);
        int n = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        this.backgroundColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcBackgroundColor, n);
        this.progressColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcProgressColor, -1);
        n = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcProgressColors, 0);
        this.progressColors = (int[])(n != 0 ? this.getResources().getIntArray(n) : null);
        this.secondaryProgressColor = typedArray.getColor(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColor, -1);
        n = typedArray.getResourceId(R.styleable.BaseRoundCornerProgressBar_rcSecondaryProgressColors, 0);
        this.secondaryProgressColors = (int[])(n != 0 ? this.getResources().getIntArray(n) : null);
        typedArray.recycle();
        this.updateProgressDrawable();
        this.updateSecondaryProgressDrawable();
        this.initStyleable(context, attributeSet);
    }

    public static interface OnProgressChangedListener {
        public void onProgressChanged(View var1, float var2, boolean var3, boolean var4);
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
        int colorBackground;
        int colorProgress;
        int[] colorProgressArray;
        int colorSecondaryProgress;
        int[] colorSecondaryProgressArray;
        boolean isReverse;
        float max;
        int padding;
        float progress;
        int radius;
        float secondaryProgress;

        SavedState(Parcel parcel) {
            this(parcel, null);
        }

        SavedState(Parcel parcel, ClassLoader object) {
            super(parcel, (ClassLoader)object);
            this.max = parcel.readFloat();
            this.progress = parcel.readFloat();
            this.secondaryProgress = parcel.readFloat();
            this.radius = parcel.readInt();
            this.padding = parcel.readInt();
            this.colorBackground = parcel.readInt();
            this.colorProgress = parcel.readInt();
            this.colorSecondaryProgress = parcel.readInt();
            object = new int[parcel.readInt()];
            this.colorProgressArray = (int[])object;
            parcel.readIntArray((int[])object);
            object = new int[parcel.readInt()];
            this.colorSecondaryProgressArray = (int[])object;
            parcel.readIntArray((int[])object);
            boolean bl = parcel.readByte() != 0;
            this.isReverse = bl;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeFloat(this.max);
            parcel.writeFloat(this.progress);
            parcel.writeFloat(this.secondaryProgress);
            parcel.writeInt(this.radius);
            parcel.writeInt(this.padding);
            parcel.writeInt(this.colorBackground);
            parcel.writeInt(this.colorProgress);
            parcel.writeInt(this.colorSecondaryProgress);
            int[] nArray = this.colorProgressArray;
            n = nArray != null ? nArray.length : 0;
            parcel.writeInt(n);
            nArray = this.colorProgressArray;
            if (nArray == null) {
                nArray = new int[]{};
            }
            parcel.writeIntArray(nArray);
            nArray = this.colorSecondaryProgressArray;
            n = nArray != null ? nArray.length : 0;
            parcel.writeInt(n);
            nArray = this.colorSecondaryProgressArray;
            if (nArray == null) {
                nArray = new int[]{};
            }
            parcel.writeIntArray(nArray);
            parcel.writeByte((byte)(this.isReverse ? 1 : 0));
        }
    }
}

