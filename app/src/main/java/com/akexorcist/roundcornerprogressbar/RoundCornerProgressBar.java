/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.util.AttributeSet
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.widget.LinearLayout
 */
package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.akexorcist.roundcornerprogressbar.R;
import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar;

public class RoundCornerProgressBar
extends AnimatedRoundCornerProgressBar {
    public RoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RoundCornerProgressBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public RoundCornerProgressBar(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    @Override
    protected void drawProgress(LinearLayout linearLayout, GradientDrawable gradientDrawable, float f, float f2, float f3, int n, int n2, boolean bl) {
        int n3 = n - n2 / 2;
        gradientDrawable.setCornerRadii(new float[]{n3, n3, n3, n3, n3, n3, n3, n3});
        linearLayout.setBackground((Drawable)gradientDrawable);
        n3 = (int)((f3 - (float)(n2 * 2)) / (f /= f2));
        gradientDrawable = (ViewGroup.MarginLayoutParams)linearLayout.getLayoutParams();
        gradientDrawable.width = n3;
        if (n3 / 2 + n2 < n) {
            gradientDrawable.topMargin = n = Math.max(n - n2, 0) - n3 / 2;
            gradientDrawable.bottomMargin = n;
        } else {
            gradientDrawable.topMargin = 0;
            gradientDrawable.bottomMargin = 0;
        }
        linearLayout.setLayoutParams((ViewGroup.LayoutParams)gradientDrawable);
    }

    @Override
    public int initLayout() {
        return R.layout.layout_round_corner_progress_bar;
    }

    @Override
    protected void initStyleable(Context context, AttributeSet attributeSet) {
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onViewDraw() {
    }
}

