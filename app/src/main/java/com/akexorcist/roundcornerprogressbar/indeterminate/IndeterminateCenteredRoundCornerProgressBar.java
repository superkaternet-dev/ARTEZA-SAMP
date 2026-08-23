/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.LinearLayout
 */
package com.akexorcist.roundcornerprogressbar.indeterminate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.akexorcist.roundcornerprogressbar.CenteredRoundCornerProgressBar;

public class IndeterminateCenteredRoundCornerProgressBar
extends CenteredRoundCornerProgressBar {
    public IndeterminateCenteredRoundCornerProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public IndeterminateCenteredRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    public IndeterminateCenteredRoundCornerProgressBar(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
    }

    private void startIndeterminateAnimation() {
        this.disableAnimation();
        this.setProgress(0);
        this.enableAnimation();
        this.setProgress(100);
    }

    private void stopIndeterminateAnimation() {
        super.stopProgressAnimationImmediately();
    }

    @Override
    protected void initView() {
        super.initView();
        this.setMax(100.0f);
    }

    @Override
    protected void onProgressChangeAnimationEnd(LinearLayout linearLayout) {
        if (this.isShown()) {
            this.startIndeterminateAnimation();
        }
    }

    @Override
    protected void onProgressChangeAnimationUpdate(LinearLayout linearLayout, float f, float f2) {
        super.onProgressChangeAnimationUpdate(linearLayout, f, f2);
        if (!this.isShown()) {
            super.stopProgressAnimationImmediately();
        }
    }

    protected void onVisibilityChanged(View view, int n) {
        super.onVisibilityChanged(view, n);
        if (n == 0) {
            this.startIndeterminateAnimation();
        }
    }
}

