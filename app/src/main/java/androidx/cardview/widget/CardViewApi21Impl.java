/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 */
package androidx.cardview.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.cardview.widget.CardViewDelegate;
import androidx.cardview.widget.CardViewImpl;
import androidx.cardview.widget.RoundRectDrawable;
import androidx.cardview.widget.RoundRectDrawableWithShadow;

class CardViewApi21Impl
implements CardViewImpl {
    CardViewApi21Impl() {
    }

    private RoundRectDrawable getCardBackground(CardViewDelegate cardViewDelegate) {
        return (RoundRectDrawable)cardViewDelegate.getCardBackground();
    }

    @Override
    public ColorStateList getBackgroundColor(CardViewDelegate cardViewDelegate) {
        return this.getCardBackground(cardViewDelegate).getColor();
    }

    @Override
    public float getElevation(CardViewDelegate cardViewDelegate) {
        return cardViewDelegate.getCardView().getElevation();
    }

    @Override
    public float getMaxElevation(CardViewDelegate cardViewDelegate) {
        return this.getCardBackground(cardViewDelegate).getPadding();
    }

    @Override
    public float getMinHeight(CardViewDelegate cardViewDelegate) {
        return this.getRadius(cardViewDelegate) * 2.0f;
    }

    @Override
    public float getMinWidth(CardViewDelegate cardViewDelegate) {
        return this.getRadius(cardViewDelegate) * 2.0f;
    }

    @Override
    public float getRadius(CardViewDelegate cardViewDelegate) {
        return this.getCardBackground(cardViewDelegate).getRadius();
    }

    @Override
    public void initStatic() {
    }

    @Override
    public void initialize(CardViewDelegate cardViewDelegate, Context context, ColorStateList colorStateList, float f, float f2, float f3) {
        cardViewDelegate.setCardBackground(new RoundRectDrawable(colorStateList, f));
        context = cardViewDelegate.getCardView();
        context.setClipToOutline(true);
        context.setElevation(f2);
        this.setMaxElevation(cardViewDelegate, f3);
    }

    @Override
    public void onCompatPaddingChanged(CardViewDelegate cardViewDelegate) {
        this.setMaxElevation(cardViewDelegate, this.getMaxElevation(cardViewDelegate));
    }

    @Override
    public void onPreventCornerOverlapChanged(CardViewDelegate cardViewDelegate) {
        this.setMaxElevation(cardViewDelegate, this.getMaxElevation(cardViewDelegate));
    }

    @Override
    public void setBackgroundColor(CardViewDelegate cardViewDelegate, ColorStateList colorStateList) {
        this.getCardBackground(cardViewDelegate).setColor(colorStateList);
    }

    @Override
    public void setElevation(CardViewDelegate cardViewDelegate, float f) {
        cardViewDelegate.getCardView().setElevation(f);
    }

    @Override
    public void setMaxElevation(CardViewDelegate cardViewDelegate, float f) {
        this.getCardBackground(cardViewDelegate).setPadding(f, cardViewDelegate.getUseCompatPadding(), cardViewDelegate.getPreventCornerOverlap());
        this.updatePadding(cardViewDelegate);
    }

    @Override
    public void setRadius(CardViewDelegate cardViewDelegate, float f) {
        this.getCardBackground(cardViewDelegate).setRadius(f);
    }

    @Override
    public void updatePadding(CardViewDelegate cardViewDelegate) {
        if (!cardViewDelegate.getUseCompatPadding()) {
            cardViewDelegate.setShadowPadding(0, 0, 0, 0);
            return;
        }
        float f = this.getMaxElevation(cardViewDelegate);
        float f2 = this.getRadius(cardViewDelegate);
        int n = (int)Math.ceil(RoundRectDrawableWithShadow.calculateHorizontalPadding(f, f2, cardViewDelegate.getPreventCornerOverlap()));
        int n2 = (int)Math.ceil(RoundRectDrawableWithShadow.calculateVerticalPadding(f, f2, cardViewDelegate.getPreventCornerOverlap()));
        cardViewDelegate.setShadowPadding(n, n2, n, n2);
    }
}

