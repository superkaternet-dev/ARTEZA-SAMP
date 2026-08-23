/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 */
package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.customview.view.AbsSavedState;
import com.akexorcist.roundcornerprogressbar.R;
import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar;

public class IconRoundCornerProgressBar
extends AnimatedRoundCornerProgressBar {
    protected static final int DEFAULT_ICON_PADDING_BOTTOM = 0;
    protected static final int DEFAULT_ICON_PADDING_LEFT = 0;
    protected static final int DEFAULT_ICON_PADDING_RIGHT = 0;
    protected static final int DEFAULT_ICON_PADDING_TOP = 0;
    protected static final int DEFAULT_ICON_SIZE = 20;
    private int colorIconBackground;
    private Bitmap iconBitmap;
    private OnIconClickListener iconClickListener;
    private Drawable iconDrawable;
    private int iconHeight;
    private int iconPadding;
    private int iconPaddingBottom;
    private int iconPaddingLeft;
    private int iconPaddingRight;
    private int iconPaddingTop;
    private int iconResource;
    private int iconSize;
    private int iconWidth;
    private ImageView ivProgressIcon;

    public IconRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public IconRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public IconRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    private void drawIconBackgroundColor() {
        GradientDrawable gradientDrawable = this.createGradientDrawable(this.colorIconBackground);
        int n = this.getRadius() - this.getPadding() / 2;
        gradientDrawable.setCornerRadii(new float[]{n, n, 0.0f, 0.0f, 0.0f, 0.0f, n, n});
        this.ivProgressIcon.setBackground((Drawable)gradientDrawable);
    }

    private void drawImageIcon() {
        int n = this.iconResource;
        if (n != -1) {
            this.ivProgressIcon.setImageResource(n);
        } else {
            Bitmap bitmap = this.iconBitmap;
            if (bitmap != null) {
                this.ivProgressIcon.setImageBitmap(bitmap);
            } else {
                bitmap = this.iconDrawable;
                if (bitmap != null) {
                    this.ivProgressIcon.setImageDrawable((Drawable)bitmap);
                }
            }
        }
    }

    private void drawImageIconPadding() {
        int n = this.iconPadding;
        if (n != -1 && n != 0) {
            this.ivProgressIcon.setPadding(n, n, n, n);
        } else {
            this.ivProgressIcon.setPadding(this.iconPaddingLeft, this.iconPaddingTop, this.iconPaddingRight, this.iconPaddingBottom);
        }
        this.ivProgressIcon.invalidate();
    }

    private void drawImageIconSize() {
        if (this.iconSize == -1) {
            this.ivProgressIcon.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(this.iconWidth, this.iconHeight));
        } else {
            ImageView imageView = this.ivProgressIcon;
            int n = this.iconSize;
            imageView.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(n, n));
        }
    }

    @Override
    protected void drawProgress(LinearLayout linearLayout, GradientDrawable gradientDrawable, float f, float f2, float f3, int n, int n2, boolean bl) {
        int n3 = n - n2 / 2;
        if (bl && f2 != f) {
            gradientDrawable.setCornerRadii(new float[]{n3, n3, n3, n3, n3, n3, n3, n3});
        } else {
            gradientDrawable.setCornerRadii(new float[]{0.0f, 0.0f, n3, n3, n3, n3, 0.0f, 0.0f});
        }
        linearLayout.setBackground((Drawable)gradientDrawable);
        n3 = (int)((f3 - (float)(n2 * 2 + this.ivProgressIcon.getWidth())) / (f /= f2));
        gradientDrawable = (ViewGroup.MarginLayoutParams)linearLayout.getLayoutParams();
        if (bl) {
            if (n2 + n3 / 2 < n) {
                gradientDrawable.topMargin = n = Math.max(n - n2, 0) - n3 / 2;
                gradientDrawable.bottomMargin = n;
            } else {
                gradientDrawable.topMargin = 0;
                gradientDrawable.bottomMargin = 0;
            }
        }
        gradientDrawable.width = n3;
        linearLayout.setLayoutParams((ViewGroup.LayoutParams)gradientDrawable);
    }

    public int getColorIconBackground() {
        return this.colorIconBackground;
    }

    public Bitmap getIconImageBitmap() {
        return this.iconBitmap;
    }

    public Drawable getIconImageDrawable() {
        return this.iconDrawable;
    }

    public int getIconImageResource() {
        return this.iconResource;
    }

    public int getIconPadding() {
        return this.iconPadding;
    }

    public int getIconPaddingBottom() {
        return this.iconPaddingBottom;
    }

    public int getIconPaddingLeft() {
        return this.iconPaddingLeft;
    }

    public int getIconPaddingRight() {
        return this.iconPaddingRight;
    }

    public int getIconPaddingTop() {
        return this.iconPaddingTop;
    }

    public int getIconSize() {
        return this.iconSize;
    }

    @Override
    public int initLayout() {
        return R.layout.layout_icon_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(Context context, AttributeSet attributeSet) {
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.IconRoundCornerProgressBar);
        this.iconResource = attributeSet.getResourceId(R.styleable.IconRoundCornerProgressBar_rcIconSrc, -1);
        this.iconSize = (int)attributeSet.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconSize, -1.0f);
        this.iconWidth = (int)attributeSet.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconWidth, this.dp2px(20.0f));
        this.iconHeight = (int)attributeSet.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconHeight, this.dp2px(20.0f));
        this.iconPadding = (int)attributeSet.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPadding, -1.0f);
        this.iconPaddingLeft = (int)attributeSet.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingLeft, this.dp2px(0.0f));
        this.iconPaddingRight = (int)attributeSet.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingRight, this.dp2px(0.0f));
        this.iconPaddingTop = (int)attributeSet.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingTop, this.dp2px(0.0f));
        this.iconPaddingBottom = (int)attributeSet.getDimension(R.styleable.IconRoundCornerProgressBar_rcIconPaddingBottom, this.dp2px(0.0f));
        int n = context.getResources().getColor(R.color.round_corner_progress_bar_background_default);
        this.colorIconBackground = attributeSet.getColor(R.styleable.IconRoundCornerProgressBar_rcIconBackgroundColor, n);
        attributeSet.recycle();
    }

    @Override
    protected void initView() {
        ImageView imageView;
        this.ivProgressIcon = imageView = (ImageView)this.findViewById(R.id.iv_progress_icon);
        imageView.setOnClickListener(new View.OnClickListener(this){
            final IconRoundCornerProgressBar this$0;
            {
                this.this$0 = iconRoundCornerProgressBar;
            }

            public void onClick(View view) {
                if (this.this$0.iconClickListener != null) {
                    this.this$0.iconClickListener.onIconClick();
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        this.iconResource = parcelable.iconResource;
        this.iconSize = parcelable.iconSize;
        this.iconWidth = parcelable.iconWidth;
        this.iconHeight = parcelable.iconHeight;
        this.iconPadding = parcelable.iconPadding;
        this.iconPaddingLeft = parcelable.iconPaddingLeft;
        this.iconPaddingRight = parcelable.iconPaddingRight;
        this.iconPaddingTop = parcelable.iconPaddingTop;
        this.iconPaddingBottom = parcelable.iconPaddingBottom;
        this.colorIconBackground = parcelable.colorIconBackground;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.iconResource = this.iconResource;
        savedState.iconSize = this.iconSize;
        savedState.iconWidth = this.iconWidth;
        savedState.iconHeight = this.iconHeight;
        savedState.iconPadding = this.iconPadding;
        savedState.iconPaddingLeft = this.iconPaddingLeft;
        savedState.iconPaddingRight = this.iconPaddingRight;
        savedState.iconPaddingTop = this.iconPaddingTop;
        savedState.iconPaddingBottom = this.iconPaddingBottom;
        savedState.colorIconBackground = this.colorIconBackground;
        return savedState;
    }

    @Override
    protected void onViewDraw() {
        this.drawImageIcon();
        this.drawImageIconSize();
        this.drawImageIconPadding();
        this.drawIconBackgroundColor();
    }

    public void setIconBackgroundColor(int n) {
        this.colorIconBackground = n;
        this.drawIconBackgroundColor();
    }

    public void setIconImageBitmap(Bitmap bitmap) {
        this.iconResource = -1;
        this.iconBitmap = bitmap;
        this.iconDrawable = null;
        this.drawImageIcon();
    }

    public void setIconImageDrawable(Drawable drawable2) {
        this.iconResource = -1;
        this.iconBitmap = null;
        this.iconDrawable = drawable2;
        this.drawImageIcon();
    }

    public void setIconImageResource(int n) {
        this.iconResource = n;
        this.iconBitmap = null;
        this.iconDrawable = null;
        this.drawImageIcon();
    }

    public void setIconPadding(int n) {
        if (n >= 0) {
            this.iconPadding = n;
        }
        this.drawImageIconPadding();
    }

    public void setIconPaddingBottom(int n) {
        if (n > 0) {
            this.iconPaddingBottom = n;
        }
        this.drawImageIconPadding();
    }

    public void setIconPaddingLeft(int n) {
        if (n > 0) {
            this.iconPaddingLeft = n;
        }
        this.drawImageIconPadding();
    }

    public void setIconPaddingRight(int n) {
        if (n > 0) {
            this.iconPaddingRight = n;
        }
        this.drawImageIconPadding();
    }

    public void setIconPaddingTop(int n) {
        if (n > 0) {
            this.iconPaddingTop = n;
        }
        this.drawImageIconPadding();
    }

    public void setIconSize(int n) {
        if (n >= 0) {
            this.iconSize = n;
        }
        this.drawImageIconSize();
    }

    public void setOnIconClickListener(OnIconClickListener onIconClickListener) {
        this.iconClickListener = onIconClickListener;
    }

    public static interface OnIconClickListener {
        public void onIconClick();
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
        int colorIconBackground;
        int iconHeight;
        int iconPadding;
        int iconPaddingBottom;
        int iconPaddingLeft;
        int iconPaddingRight;
        int iconPaddingTop;
        int iconResource;
        int iconSize;
        int iconWidth;

        SavedState(Parcel parcel) {
            this(parcel, null);
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.iconResource = parcel.readInt();
            this.iconSize = parcel.readInt();
            this.iconWidth = parcel.readInt();
            this.iconHeight = parcel.readInt();
            this.iconPadding = parcel.readInt();
            this.iconPaddingLeft = parcel.readInt();
            this.iconPaddingRight = parcel.readInt();
            this.iconPaddingTop = parcel.readInt();
            this.iconPaddingBottom = parcel.readInt();
            this.colorIconBackground = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.iconResource);
            parcel.writeInt(this.iconSize);
            parcel.writeInt(this.iconWidth);
            parcel.writeInt(this.iconHeight);
            parcel.writeInt(this.iconPadding);
            parcel.writeInt(this.iconPaddingLeft);
            parcel.writeInt(this.iconPaddingRight);
            parcel.writeInt(this.iconPaddingTop);
            parcel.writeInt(this.iconPaddingBottom);
            parcel.writeInt(this.colorIconBackground);
        }
    }
}

