/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.RelativeLayout
 *  android.widget.RelativeLayout$LayoutParams
 */
package com.skydoves.colorpickerview.flag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.FadeUtils;
import com.skydoves.colorpickerview.flag.FlagMode;

public abstract class FlagView
extends RelativeLayout {
    private FlagMode flagMode = FlagMode.ALWAYS;
    private boolean flipAble = true;

    public FlagView(Context context, int n) {
        super(context);
        this.initializeLayout(n);
    }

    private void initializeLayout(int n) {
        View view = LayoutInflater.from((Context)this.getContext()).inflate(n, (ViewGroup)this);
        view.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-2, -2));
        view.measure(View.MeasureSpec.makeMeasureSpec((int)0, (int)0), View.MeasureSpec.makeMeasureSpec((int)0, (int)0));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public FlagMode getFlagMode() {
        return this.flagMode;
    }

    public void gone() {
        this.setVisibility(8);
    }

    public boolean isFlipAble() {
        return this.flipAble;
    }

    public abstract void onRefresh(ColorEnvelope var1);

    public void receiveOnTouchEvent(MotionEvent motionEvent) {
        block10: {
            switch (motionEvent.getActionMasked()) {
                default: {
                    break;
                }
                case 2: {
                    if (this.getFlagMode() == FlagMode.LAST) {
                        this.gone();
                    }
                    break block10;
                }
                case 1: {
                    if (this.getFlagMode() == FlagMode.LAST) {
                        this.visible();
                        break;
                    }
                    if (this.getFlagMode() != FlagMode.FADE) break;
                    FadeUtils.fadeOut((View)this);
                    break;
                }
                case 0: {
                    if (this.getFlagMode() == FlagMode.LAST) {
                        this.gone();
                    } else if (this.getFlagMode() == FlagMode.FADE) {
                        FadeUtils.fadeIn((View)this);
                    }
                    break block10;
                }
            }
            this.visible();
        }
    }

    public void setFlagMode(FlagMode flagMode) {
        this.flagMode = flagMode;
    }

    public void setFlipAble(boolean bl) {
        this.flipAble = bl;
    }

    public void visible() {
        this.setVisibility(0);
    }
}

