/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.util.AttributeSet
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.LinearLayout
 *  android.widget.RelativeLayout$LayoutParams
 *  android.widget.TextView
 */
package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.customview.view.AbsSavedState;
import com.akexorcist.roundcornerprogressbar.R;
import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class TextRoundCornerProgressBar
extends AnimatedRoundCornerProgressBar
implements ViewTreeObserver.OnGlobalLayoutListener {
    protected static final int DEFAULT_TEXT_MARGIN = 10;
    protected static final int DEFAULT_TEXT_SIZE = 16;
    public static final int GRAVITY_END = 1;
    public static final int GRAVITY_START = 0;
    public static final int PRIORITY_INSIDE = 0;
    public static final int PRIORITY_OUTSIDE = 1;
    private int colorTextProgress;
    private int textInsideGravity;
    private int textOutsideGravity;
    private int textPositionPriority;
    private String textProgress;
    private int textProgressMargin;
    private int textProgressSize;
    private TextView tvProgress;

    public TextRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public TextRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    private void alignTextProgressInsideProgress() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.tvProgress.getLayoutParams();
        if (this.isReverse()) {
            if (this.textInsideGravity == 1) {
                layoutParams.addRule(7, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= 17) {
                    layoutParams.addRule(19, R.id.layout_progress);
                }
            } else {
                layoutParams.addRule(5, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= 17) {
                    layoutParams.addRule(18, R.id.layout_progress);
                }
            }
        } else if (this.textInsideGravity == 1) {
            layoutParams.addRule(5, R.id.layout_progress);
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.addRule(18, R.id.layout_progress);
            }
        } else {
            layoutParams.addRule(7, R.id.layout_progress);
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.addRule(19, R.id.layout_progress);
            }
        }
        this.tvProgress.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    private void alignTextProgressOutsideProgress() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.tvProgress.getLayoutParams();
        if (this.isReverse()) {
            if (this.textOutsideGravity == 1) {
                layoutParams.addRule(9);
                if (Build.VERSION.SDK_INT >= 17) {
                    layoutParams.addRule(20);
                }
            } else {
                layoutParams.addRule(0, R.id.layout_progress);
                if (Build.VERSION.SDK_INT >= 17) {
                    layoutParams.addRule(16, R.id.layout_progress);
                }
            }
        } else if (this.textOutsideGravity == 1) {
            layoutParams.addRule(11);
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.addRule(21);
            }
        } else {
            layoutParams.addRule(1, R.id.layout_progress);
            if (Build.VERSION.SDK_INT >= 17) {
                layoutParams.addRule(17, R.id.layout_progress);
            }
        }
        this.tvProgress.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    private void clearTextProgressAlign() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.tvProgress.getLayoutParams();
        layoutParams.removeRule(0);
        layoutParams.removeRule(1);
        layoutParams.removeRule(5);
        layoutParams.removeRule(7);
        layoutParams.removeRule(9);
        layoutParams.removeRule(11);
        if (Build.VERSION.SDK_INT >= 17) {
            layoutParams.removeRule(16);
            layoutParams.removeRule(17);
            layoutParams.removeRule(18);
            layoutParams.removeRule(19);
            layoutParams.removeRule(20);
            layoutParams.removeRule(21);
        }
        this.tvProgress.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    private void drawTextProgress() {
        this.tvProgress.setText((CharSequence)this.textProgress);
    }

    private void drawTextProgressColor() {
        this.tvProgress.setTextColor(this.colorTextProgress);
    }

    private void drawTextProgressMargin() {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams)this.tvProgress.getLayoutParams();
        int n = this.textProgressMargin;
        marginLayoutParams.setMargins(n, 0, n, 0);
        this.tvProgress.setLayoutParams((ViewGroup.LayoutParams)marginLayoutParams);
    }

    private void drawTextProgressPosition() {
        this.clearTextProgressAlign();
        int n = this.tvProgress.getMeasuredWidth() + this.getTextProgressMargin() * 2;
        float f = this.getMax() / this.getProgress();
        int n2 = (int)((this.getLayoutWidth() - (float)(this.getPadding() * 2)) / f);
        if (this.textPositionPriority == 1) {
            if (this.getLayoutWidth() - (float)n2 > (float)n) {
                this.alignTextProgressOutsideProgress();
            } else {
                this.alignTextProgressInsideProgress();
            }
        } else if (this.textProgressMargin + n > n2) {
            this.alignTextProgressOutsideProgress();
        } else {
            this.alignTextProgressInsideProgress();
        }
    }

    private void drawTextProgressSize() {
        this.tvProgress.setTextSize(0, (float)this.textProgressSize);
    }

    @Override
    protected void drawProgress(LinearLayout linearLayout, GradientDrawable gradientDrawable, float f, float f2, float f3, int n, int n2, boolean bl) {
        int n3 = n - n2 / 2;
        gradientDrawable.setCornerRadii(new float[]{n3, n3, n3, n3, n3, n3, n3, n3});
        linearLayout.setBackground((Drawable)gradientDrawable);
        n3 = (int)((f3 - (float)(n2 * 2)) / (f /= f2));
        gradientDrawable = (ViewGroup.MarginLayoutParams)linearLayout.getLayoutParams();
        if (n3 / 2 + n2 < n) {
            gradientDrawable.topMargin = n = Math.max(n - n2, 0) - n3 / 2;
            gradientDrawable.bottomMargin = n;
        } else {
            gradientDrawable.topMargin = 0;
            gradientDrawable.bottomMargin = 0;
        }
        gradientDrawable.width = n3;
        linearLayout.setLayoutParams((ViewGroup.LayoutParams)gradientDrawable);
    }

    public String getProgressText() {
        return this.textProgress;
    }

    public int getTextInsideGravity() {
        return this.textInsideGravity;
    }

    public int getTextOutsideGravity() {
        return this.textOutsideGravity;
    }

    public int getTextPositionPriority() {
        return this.textPositionPriority;
    }

    public int getTextProgressColor() {
        return this.colorTextProgress;
    }

    public int getTextProgressMargin() {
        return this.textProgressMargin;
    }

    public int getTextProgressSize() {
        return this.textProgressSize;
    }

    @Override
    protected int initLayout() {
        return R.layout.layout_text_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(Context context, AttributeSet attributeSet) {
        context = context.obtainStyledAttributes(attributeSet, R.styleable.TextRoundCornerProgressBar);
        this.colorTextProgress = context.getColor(R.styleable.TextRoundCornerProgressBar_rcTextProgressColor, -1);
        this.textProgressSize = (int)context.getDimension(R.styleable.TextRoundCornerProgressBar_rcTextProgressSize, this.dp2px(16.0f));
        this.textProgressMargin = (int)context.getDimension(R.styleable.TextRoundCornerProgressBar_rcTextProgressMargin, this.dp2px(10.0f));
        this.textProgress = context.getString(R.styleable.TextRoundCornerProgressBar_rcTextProgress);
        this.textInsideGravity = context.getInt(R.styleable.TextRoundCornerProgressBar_rcTextInsideGravity, 0);
        this.textOutsideGravity = context.getInt(R.styleable.TextRoundCornerProgressBar_rcTextOutsideGravity, 0);
        this.textPositionPriority = context.getInt(R.styleable.TextRoundCornerProgressBar_rcTextPositionPriority, 0);
        context.recycle();
    }

    @Override
    protected void initView() {
        TextView textView;
        this.tvProgress = textView = (TextView)this.findViewById(R.id.tv_progress);
        textView.getViewTreeObserver().addOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
    }

    public void onGlobalLayout() {
        this.tvProgress.getViewTreeObserver().removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener)this);
        this.drawTextProgressPosition();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.colorTextProgress = parcelable.colorTextProgress;
        this.textProgressSize = parcelable.textProgressSize;
        this.textProgressMargin = parcelable.textProgressMargin;
        this.textProgress = parcelable.textProgress;
        this.textInsideGravity = parcelable.textInsideGravity;
        this.textOutsideGravity = parcelable.textOutsideGravity;
        this.textPositionPriority = parcelable.textPositionPriority;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.colorTextProgress = this.colorTextProgress;
        savedState.textProgressSize = this.textProgressSize;
        savedState.textProgressMargin = this.textProgressMargin;
        savedState.textProgress = this.textProgress;
        savedState.textInsideGravity = this.textInsideGravity;
        savedState.textOutsideGravity = this.textOutsideGravity;
        savedState.textPositionPriority = this.textPositionPriority;
        return savedState;
    }

    @Override
    protected void onViewDraw() {
        this.drawTextProgress();
        this.drawTextProgressSize();
        this.drawTextProgressMargin();
        this.post(new Runnable(this){
            final TextRoundCornerProgressBar this$0;
            {
                this.this$0 = textRoundCornerProgressBar;
            }

            @Override
            public void run() {
                this.this$0.drawTextProgressPosition();
            }
        });
        this.drawTextProgressColor();
    }

    @Override
    public void setProgress(float f) {
        super.setProgress(f);
        this.drawTextProgressPosition();
    }

    @Override
    public void setProgress(int n) {
        this.setProgress((float)n);
    }

    public void setProgressText(String string2) {
        this.textProgress = string2;
        this.drawTextProgress();
        this.drawTextProgressPosition();
    }

    public void setTextInsideGravity(int n) {
        this.textInsideGravity = n;
        this.drawTextProgressPosition();
    }

    public void setTextOutsideGravity(int n) {
        this.textOutsideGravity = n;
        this.drawTextProgressPosition();
    }

    public void setTextPositionPriority(int n) {
        this.textPositionPriority = n;
        this.drawTextProgressPosition();
    }

    public void setTextProgressColor(int n) {
        this.colorTextProgress = n;
        this.drawTextProgressColor();
    }

    public void setTextProgressMargin(int n) {
        this.textProgressMargin = n;
        this.drawTextProgressMargin();
        this.drawTextProgressPosition();
    }

    public void setTextProgressSize(int n) {
        this.textProgressSize = n;
        this.drawTextProgressSize();
        this.drawTextProgressPosition();
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
        int colorTextProgress;
        int textInsideGravity;
        int textOutsideGravity;
        int textPositionPriority;
        String textProgress;
        int textProgressMargin;
        int textProgressSize;

        private SavedState(Parcel parcel) {
            this(parcel, (ClassLoader)null);
        }

        protected SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.colorTextProgress = parcel.readInt();
            this.textProgressSize = parcel.readInt();
            this.textProgressMargin = parcel.readInt();
            this.textProgress = parcel.readString();
            this.textInsideGravity = parcel.readInt();
            this.textOutsideGravity = parcel.readInt();
            this.textPositionPriority = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.colorTextProgress);
            parcel.writeInt(this.textProgressSize);
            parcel.writeInt(this.textProgressMargin);
            parcel.writeString(this.textProgress);
            parcel.writeInt(this.textInsideGravity);
            parcel.writeInt(this.textOutsideGravity);
            parcel.writeInt(this.textPositionPriority);
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TEXT_POSITION_PRIORITY {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TEXT_PROGRESS_GRAVITY {
    }
}

