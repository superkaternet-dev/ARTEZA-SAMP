/*
 * Decompiled with CFR 0.152.
 */
package com.smarteist.autoimageslider.IndicatorView.draw.data;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.data.RtlMode;

public class Indicator {
    public static final int COUNT_NONE = -1;
    public static final int DEFAULT_COUNT = 3;
    public static final int DEFAULT_PADDING_DP = 8;
    public static final int DEFAULT_RADIUS_DP = 6;
    public static final int MIN_COUNT = 1;
    private long animationDuration;
    private IndicatorAnimationType animationType;
    private boolean autoVisibility;
    private int count = 3;
    private boolean dynamicCount;
    private int height;
    private boolean interactiveAnimation;
    private int lastSelectedPosition;
    private Orientation orientation;
    private int padding;
    private int paddingBottom;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int radius;
    private RtlMode rtlMode;
    private float scaleFactor;
    private int selectedColor;
    private int selectedPosition;
    private int selectingPosition;
    private int stroke;
    private int unselectedColor;
    private int viewPagerId = -1;
    private int width;

    public long getAnimationDuration() {
        return this.animationDuration;
    }

    public IndicatorAnimationType getAnimationType() {
        if (this.animationType == null) {
            this.animationType = IndicatorAnimationType.NONE;
        }
        return this.animationType;
    }

    public int getCount() {
        return this.count;
    }

    public int getHeight() {
        return this.height;
    }

    public int getLastSelectedPosition() {
        return this.lastSelectedPosition;
    }

    public Orientation getOrientation() {
        if (this.orientation == null) {
            this.orientation = Orientation.HORIZONTAL;
        }
        return this.orientation;
    }

    public int getPadding() {
        return this.padding;
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public int getRadius() {
        return this.radius;
    }

    public RtlMode getRtlMode() {
        if (this.rtlMode == null) {
            this.rtlMode = RtlMode.Off;
        }
        return this.rtlMode;
    }

    public float getScaleFactor() {
        return this.scaleFactor;
    }

    public int getSelectedColor() {
        return this.selectedColor;
    }

    public int getSelectedPosition() {
        return this.selectedPosition;
    }

    public int getSelectingPosition() {
        return this.selectingPosition;
    }

    public int getStroke() {
        return this.stroke;
    }

    public int getUnselectedColor() {
        return this.unselectedColor;
    }

    public int getViewPagerId() {
        return this.viewPagerId;
    }

    public int getWidth() {
        return this.width;
    }

    public boolean isAutoVisibility() {
        return this.autoVisibility;
    }

    public boolean isDynamicCount() {
        return this.dynamicCount;
    }

    public boolean isInteractiveAnimation() {
        return this.interactiveAnimation;
    }

    public void setAnimationDuration(long l) {
        this.animationDuration = l;
    }

    public void setAnimationType(IndicatorAnimationType indicatorAnimationType) {
        this.animationType = indicatorAnimationType;
    }

    public void setAutoVisibility(boolean bl) {
        this.autoVisibility = bl;
    }

    public void setCount(int n) {
        this.count = n;
    }

    public void setDynamicCount(boolean bl) {
        this.dynamicCount = bl;
    }

    public void setHeight(int n) {
        this.height = n;
    }

    public void setInteractiveAnimation(boolean bl) {
        this.interactiveAnimation = bl;
    }

    public void setLastSelectedPosition(int n) {
        this.lastSelectedPosition = n;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setPadding(int n) {
        this.padding = n;
    }

    public void setPaddingBottom(int n) {
        this.paddingBottom = n;
    }

    public void setPaddingLeft(int n) {
        this.paddingLeft = n;
    }

    public void setPaddingRight(int n) {
        this.paddingRight = n;
    }

    public void setPaddingTop(int n) {
        this.paddingTop = n;
    }

    public void setRadius(int n) {
        this.radius = n;
    }

    public void setRtlMode(RtlMode rtlMode) {
        this.rtlMode = rtlMode;
    }

    public void setScaleFactor(float f) {
        this.scaleFactor = f;
    }

    public void setSelectedColor(int n) {
        this.selectedColor = n;
    }

    public void setSelectedPosition(int n) {
        this.selectedPosition = n;
    }

    public void setSelectingPosition(int n) {
        this.selectingPosition = n;
    }

    public void setStroke(int n) {
        this.stroke = n;
    }

    public void setUnselectedColor(int n) {
        this.unselectedColor = n;
    }

    public void setViewPagerId(int n) {
        this.viewPagerId = n;
    }

    public void setWidth(int n) {
        this.width = n;
    }
}

