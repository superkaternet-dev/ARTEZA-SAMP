/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Color
 *  android.util.AttributeSet
 */
package com.smarteist.autoimageslider.IndicatorView.draw.controller;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.data.RtlMode;
import com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils;
import com.smarteist.autoimageslider.R;

public class AttributeController {
    private Indicator indicator;

    public AttributeController(Indicator indicator) {
        this.indicator = indicator;
    }

    private IndicatorAnimationType getAnimationType(int n) {
        switch (n) {
            default: {
                return IndicatorAnimationType.NONE;
            }
            case 9: {
                return IndicatorAnimationType.SCALE_DOWN;
            }
            case 8: {
                return IndicatorAnimationType.SWAP;
            }
            case 7: {
                return IndicatorAnimationType.DROP;
            }
            case 6: {
                return IndicatorAnimationType.THIN_WORM;
            }
            case 5: {
                return IndicatorAnimationType.FILL;
            }
            case 4: {
                return IndicatorAnimationType.SLIDE;
            }
            case 3: {
                return IndicatorAnimationType.WORM;
            }
            case 2: {
                return IndicatorAnimationType.SCALE;
            }
            case 1: {
                return IndicatorAnimationType.COLOR;
            }
            case 0: 
        }
        return IndicatorAnimationType.NONE;
    }

    public static RtlMode getRtlMode(int n) {
        switch (n) {
            default: {
                return RtlMode.Auto;
            }
            case 2: {
                return RtlMode.Auto;
            }
            case 1: {
                return RtlMode.Off;
            }
            case 0: 
        }
        return RtlMode.On;
    }

    private void initAnimationAttribute(TypedArray object) {
        int n;
        boolean bl = object.getBoolean(R.styleable.PageIndicatorView_piv_interactiveAnimation, false);
        int n2 = n = object.getInt(R.styleable.PageIndicatorView_piv_animationDuration, 350);
        if (n < 0) {
            n2 = 0;
        }
        IndicatorAnimationType indicatorAnimationType = this.getAnimationType(object.getInt(R.styleable.PageIndicatorView_piv_animationType, IndicatorAnimationType.NONE.ordinal()));
        object = AttributeController.getRtlMode(object.getInt(R.styleable.PageIndicatorView_piv_rtl_mode, RtlMode.Off.ordinal()));
        this.indicator.setAnimationDuration(n2);
        this.indicator.setInteractiveAnimation(bl);
        this.indicator.setAnimationType(indicatorAnimationType);
        this.indicator.setRtlMode((RtlMode)((Object)object));
    }

    private void initColorAttribute(TypedArray typedArray) {
        int n = typedArray.getColor(R.styleable.PageIndicatorView_piv_unselectedColor, Color.parseColor((String)"#33ffffff"));
        int n2 = typedArray.getColor(R.styleable.PageIndicatorView_piv_selectedColor, Color.parseColor((String)"#ffffff"));
        this.indicator.setUnselectedColor(n);
        this.indicator.setSelectedColor(n2);
    }

    private void initCountAttribute(TypedArray typedArray) {
        int n;
        int n2;
        int n3 = typedArray.getResourceId(R.styleable.PageIndicatorView_piv_viewPager, -1);
        boolean bl = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_autoVisibility, true);
        boolean bl2 = typedArray.getBoolean(R.styleable.PageIndicatorView_piv_dynamicCount, false);
        int n4 = n2 = typedArray.getInt(R.styleable.PageIndicatorView_piv_count, -1);
        if (n2 == -1) {
            n4 = 3;
        }
        if ((n = typedArray.getInt(R.styleable.PageIndicatorView_piv_select, 0)) < 0) {
            n2 = 0;
        } else {
            n2 = n;
            if (n4 > 0) {
                n2 = n;
                if (n > n4 - 1) {
                    n2 = n4 - 1;
                }
            }
        }
        this.indicator.setViewPagerId(n3);
        this.indicator.setAutoVisibility(bl);
        this.indicator.setDynamicCount(bl2);
        this.indicator.setCount(n4);
        this.indicator.setSelectedPosition(n2);
        this.indicator.setSelectingPosition(n2);
        this.indicator.setLastSelectedPosition(n2);
    }

    private void initSizeAttribute(TypedArray typedArray) {
        int n;
        float f;
        float f2;
        int n2;
        Orientation orientation = typedArray.getInt(R.styleable.PageIndicatorView_piv_orientation, Orientation.HORIZONTAL.ordinal()) == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        int n3 = n2 = (int)typedArray.getDimension(R.styleable.PageIndicatorView_piv_radius, (float)DensityUtils.dpToPx(6));
        if (n2 < 0) {
            n3 = 0;
        }
        int n4 = n2 = (int)typedArray.getDimension(R.styleable.PageIndicatorView_piv_padding, (float)DensityUtils.dpToPx(8));
        if (n2 < 0) {
            n4 = 0;
        }
        if ((f2 = typedArray.getFloat(R.styleable.PageIndicatorView_piv_scaleFactor, 0.7f)) < 0.3f) {
            f = 0.3f;
        } else {
            f = f2;
            if (f2 > 1.0f) {
                f = 1.0f;
            }
        }
        n2 = n = (int)typedArray.getDimension(R.styleable.PageIndicatorView_piv_strokeWidth, (float)DensityUtils.dpToPx(1));
        if (n > n3) {
            n2 = n3;
        }
        if (this.indicator.getAnimationType() != IndicatorAnimationType.FILL) {
            n2 = 0;
        }
        this.indicator.setRadius(n3);
        this.indicator.setOrientation(orientation);
        this.indicator.setPadding(n4);
        this.indicator.setScaleFactor(f);
        this.indicator.setStroke(n2);
    }

    public void init(Context context, AttributeSet attributeSet) {
        context = context.obtainStyledAttributes(attributeSet, R.styleable.PageIndicatorView, 0, 0);
        this.initCountAttribute((TypedArray)context);
        this.initColorAttribute((TypedArray)context);
        this.initAnimationAttribute((TypedArray)context);
        this.initSizeAttribute((TypedArray)context);
        context.recycle();
    }
}

