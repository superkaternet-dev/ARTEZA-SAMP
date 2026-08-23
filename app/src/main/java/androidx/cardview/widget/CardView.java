/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.Color
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.widget.FrameLayout
 */
package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.cardview.R;
import androidx.cardview.widget.CardViewApi17Impl;
import androidx.cardview.widget.CardViewApi21Impl;
import androidx.cardview.widget.CardViewBaseImpl;
import androidx.cardview.widget.CardViewDelegate;
import androidx.cardview.widget.CardViewImpl;

public class CardView
extends FrameLayout {
    private static final int[] COLOR_BACKGROUND_ATTR = new int[]{0x1010031};
    private static final CardViewImpl IMPL = Build.VERSION.SDK_INT >= 21 ? new CardViewApi21Impl() : (Build.VERSION.SDK_INT >= 17 ? new CardViewApi17Impl() : new CardViewBaseImpl());
    private final CardViewDelegate mCardViewDelegate;
    private boolean mCompatPadding;
    final Rect mContentPadding;
    private boolean mPreventCornerOverlap;
    final Rect mShadowBounds;
    int mUserSetMinHeight;
    int mUserSetMinWidth;

    static {
        IMPL.initStatic();
    }

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.cardViewStyle);
    }

    public CardView(Context context, AttributeSet object, int n) {
        super(context, object, n);
        CardViewDelegate cardViewDelegate;
        Rect rect;
        this.mContentPadding = rect = new Rect();
        this.mShadowBounds = new Rect();
        this.mCardViewDelegate = cardViewDelegate = new CardViewDelegate(this){
            private Drawable mCardBackground;
            final CardView this$0;
            {
                this.this$0 = cardView;
            }

            @Override
            public Drawable getCardBackground() {
                return this.mCardBackground;
            }

            @Override
            public View getCardView() {
                return this.this$0;
            }

            @Override
            public boolean getPreventCornerOverlap() {
                return this.this$0.getPreventCornerOverlap();
            }

            @Override
            public boolean getUseCompatPadding() {
                return this.this$0.getUseCompatPadding();
            }

            @Override
            public void setCardBackground(Drawable drawable2) {
                this.mCardBackground = drawable2;
                this.this$0.setBackgroundDrawable(drawable2);
            }

            @Override
            public void setMinWidthHeightInternal(int n, int n2) {
                if (n > this.this$0.mUserSetMinWidth) {
                    CardView.super.setMinimumWidth(n);
                }
                if (n2 > this.this$0.mUserSetMinHeight) {
                    CardView.super.setMinimumHeight(n2);
                }
            }

            @Override
            public void setShadowPadding(int n, int n2, int n3, int n4) {
                this.this$0.mShadowBounds.set(n, n2, n3, n4);
                CardView cardView = this.this$0;
                CardView.super.setPadding(cardView.mContentPadding.left + n, this.this$0.mContentPadding.top + n2, this.this$0.mContentPadding.right + n3, this.this$0.mContentPadding.bottom + n4);
            }
        };
        TypedArray typedArray = context.obtainStyledAttributes(object, R.styleable.CardView, n, R.style.CardView);
        if (typedArray.hasValue(R.styleable.CardView_cardBackgroundColor)) {
            object = typedArray.getColorStateList(R.styleable.CardView_cardBackgroundColor);
        } else {
            object = this.getContext().obtainStyledAttributes(COLOR_BACKGROUND_ATTR);
            n = object.getColor(0, 0);
            object.recycle();
            object = new float[3];
            Color.colorToHSV((int)n, (float[])object);
            n = object[2] > 0.5f ? this.getResources().getColor(R.color.cardview_light_background) : this.getResources().getColor(R.color.cardview_dark_background);
            object = ColorStateList.valueOf((int)n);
        }
        float f = typedArray.getDimension(R.styleable.CardView_cardCornerRadius, 0.0f);
        float f2 = typedArray.getDimension(R.styleable.CardView_cardElevation, 0.0f);
        float f3 = typedArray.getDimension(R.styleable.CardView_cardMaxElevation, 0.0f);
        this.mCompatPadding = typedArray.getBoolean(R.styleable.CardView_cardUseCompatPadding, false);
        this.mPreventCornerOverlap = typedArray.getBoolean(R.styleable.CardView_cardPreventCornerOverlap, true);
        n = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPadding, 0);
        rect.left = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingLeft, n);
        rect.top = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingTop, n);
        rect.right = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingRight, n);
        rect.bottom = typedArray.getDimensionPixelSize(R.styleable.CardView_contentPaddingBottom, n);
        if (f2 > f3) {
            f3 = f2;
        }
        this.mUserSetMinWidth = typedArray.getDimensionPixelSize(R.styleable.CardView_android_minWidth, 0);
        this.mUserSetMinHeight = typedArray.getDimensionPixelSize(R.styleable.CardView_android_minHeight, 0);
        typedArray.recycle();
        IMPL.initialize(cardViewDelegate, context, (ColorStateList)object, f, f2, f3);
    }

    public ColorStateList getCardBackgroundColor() {
        return IMPL.getBackgroundColor(this.mCardViewDelegate);
    }

    public float getCardElevation() {
        return IMPL.getElevation(this.mCardViewDelegate);
    }

    public int getContentPaddingBottom() {
        return this.mContentPadding.bottom;
    }

    public int getContentPaddingLeft() {
        return this.mContentPadding.left;
    }

    public int getContentPaddingRight() {
        return this.mContentPadding.right;
    }

    public int getContentPaddingTop() {
        return this.mContentPadding.top;
    }

    public float getMaxCardElevation() {
        return IMPL.getMaxElevation(this.mCardViewDelegate);
    }

    public boolean getPreventCornerOverlap() {
        return this.mPreventCornerOverlap;
    }

    public float getRadius() {
        return IMPL.getRadius(this.mCardViewDelegate);
    }

    public boolean getUseCompatPadding() {
        return this.mCompatPadding;
    }

    protected void onMeasure(int n, int n2) {
        CardViewImpl cardViewImpl = IMPL;
        if (!(cardViewImpl instanceof CardViewApi21Impl)) {
            int n3;
            int n4 = View.MeasureSpec.getMode((int)n);
            switch (n4) {
                default: {
                    break;
                }
                case -2147483648: 
                case 0x40000000: {
                    n3 = (int)Math.ceil(cardViewImpl.getMinWidth(this.mCardViewDelegate));
                    n = View.MeasureSpec.makeMeasureSpec((int)Math.max(n3, View.MeasureSpec.getSize((int)n)), (int)n4);
                }
            }
            n4 = View.MeasureSpec.getMode((int)n2);
            switch (n4) {
                default: {
                    break;
                }
                case -2147483648: 
                case 0x40000000: {
                    n3 = (int)Math.ceil(cardViewImpl.getMinHeight(this.mCardViewDelegate));
                    n2 = View.MeasureSpec.makeMeasureSpec((int)Math.max(n3, View.MeasureSpec.getSize((int)n2)), (int)n4);
                }
            }
            super.onMeasure(n, n2);
        } else {
            super.onMeasure(n, n2);
        }
    }

    public void setCardBackgroundColor(int n) {
        IMPL.setBackgroundColor(this.mCardViewDelegate, ColorStateList.valueOf((int)n));
    }

    public void setCardBackgroundColor(ColorStateList colorStateList) {
        IMPL.setBackgroundColor(this.mCardViewDelegate, colorStateList);
    }

    public void setCardElevation(float f) {
        IMPL.setElevation(this.mCardViewDelegate, f);
    }

    public void setContentPadding(int n, int n2, int n3, int n4) {
        this.mContentPadding.set(n, n2, n3, n4);
        IMPL.updatePadding(this.mCardViewDelegate);
    }

    public void setMaxCardElevation(float f) {
        IMPL.setMaxElevation(this.mCardViewDelegate, f);
    }

    public void setMinimumHeight(int n) {
        this.mUserSetMinHeight = n;
        super.setMinimumHeight(n);
    }

    public void setMinimumWidth(int n) {
        this.mUserSetMinWidth = n;
        super.setMinimumWidth(n);
    }

    public void setPadding(int n, int n2, int n3, int n4) {
    }

    public void setPaddingRelative(int n, int n2, int n3, int n4) {
    }

    public void setPreventCornerOverlap(boolean bl) {
        if (bl != this.mPreventCornerOverlap) {
            this.mPreventCornerOverlap = bl;
            IMPL.onPreventCornerOverlapChanged(this.mCardViewDelegate);
        }
    }

    public void setRadius(float f) {
        IMPL.setRadius(this.mCardViewDelegate, f);
    }

    public void setUseCompatPadding(boolean bl) {
        if (this.mCompatPadding != bl) {
            this.mCompatPadding = bl;
            IMPL.onCompatPaddingChanged(this.mCardViewDelegate);
        }
    }
}

