/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Pair
 *  android.view.View$MeasureSpec
 */
package com.smarteist.autoimageslider.IndicatorView.draw.controller;

import android.util.Pair;
import android.view.View;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;

public class MeasureController {
    public Pair<Integer, Integer> measureViewSize(Indicator indicator, int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        int n4 = View.MeasureSpec.getSize((int)n);
        int n5 = View.MeasureSpec.getMode((int)n2);
        int n6 = View.MeasureSpec.getSize((int)n2);
        int n7 = indicator.getCount();
        n = indicator.getRadius();
        int n8 = indicator.getStroke();
        int n9 = indicator.getPadding();
        int n10 = indicator.getPaddingLeft();
        int n11 = indicator.getPaddingTop();
        int n12 = indicator.getPaddingRight();
        int n13 = indicator.getPaddingBottom();
        int n14 = n * 2;
        n = 0;
        n2 = 0;
        Orientation orientation = indicator.getOrientation();
        if (n7 != 0) {
            n = n14 * n7 + n8 * 2 * n7 + (n7 - 1) * n9;
            n2 = n14 + n8;
            if (orientation != Orientation.HORIZONTAL) {
                n8 = n2;
                n2 = n;
                n = n8;
            }
        }
        n14 = n;
        n8 = n2;
        if (indicator.getAnimationType() == IndicatorAnimationType.DROP) {
            if (orientation == Orientation.HORIZONTAL) {
                n8 = n2 * 2;
                n14 = n;
            } else {
                n14 = n * 2;
                n8 = n2;
            }
        }
        n = n10 + n12;
        n2 = n11 + n13;
        if (orientation == Orientation.HORIZONTAL) {
            n = n14 + n;
            n2 = n8 + n2;
        } else {
            n = n14 + n;
            n2 = n8 + n2;
        }
        if (n3 == 0x40000000) {
            n = n4;
        } else if (n3 == Integer.MIN_VALUE) {
            n = Math.min(n, n4);
        }
        if (n5 == 0x40000000) {
            n2 = n6;
        } else if (n5 == Integer.MIN_VALUE) {
            n2 = Math.min(n2, n6);
        }
        n8 = n;
        if (n < 0) {
            n8 = 0;
        }
        n = n2;
        if (n2 < 0) {
            n = 0;
        }
        indicator.setWidth(n8);
        indicator.setHeight(n);
        return new Pair((Object)n8, (Object)n);
    }
}

