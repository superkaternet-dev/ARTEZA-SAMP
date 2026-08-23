/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.GradientDrawable
 *  android.util.AttributeSet
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.widget.LinearLayout
 */
package com.akexorcist.roundcornerprogressbar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

public class CenteredRoundCornerProgressBar
extends RoundCornerProgressBar {
    public CenteredRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public CenteredRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public CenteredRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    @Override
    protected void drawProgress(LinearLayout linearLayout, GradientDrawable gradientDrawable, float f, float f2, float f3, int n, int n2, boolean bl) {
        super.drawProgress(linearLayout, gradientDrawable, f, f2, f3, n, n2, bl);
        gradientDrawable = (ViewGroup.MarginLayoutParams)linearLayout.getLayoutParams();
        f /= f2;
        f = f3 - (f3 - (float)(n2 * 2)) / f;
        gradientDrawable.setMargins((int)(f / 2.0f), gradientDrawable.topMargin, (int)(f / 2.0f), gradientDrawable.bottomMargin);
        linearLayout.setLayoutParams((ViewGroup.LayoutParams)gradientDrawable);
    }
}

