/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.DataSetObserver
 *  android.graphics.Canvas
 *  android.os.Parcelable
 *  android.util.AttributeSet
 *  android.util.Pair
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewParent
 */
package com.smarteist.autoimageslider.IndicatorView;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.text.TextUtilsCompat;
import androidx.viewpager.widget.PagerAdapter;
import com.smarteist.autoimageslider.IndicatorView.IndicatorManager;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Indicator;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.data.PositionSavedState;
import com.smarteist.autoimageslider.IndicatorView.draw.data.RtlMode;
import com.smarteist.autoimageslider.IndicatorView.utils.CoordinatesUtils;
import com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils;
import com.smarteist.autoimageslider.IndicatorView.utils.IdUtils;
import com.smarteist.autoimageslider.InfiniteAdapter.InfinitePagerAdapter;
import com.smarteist.autoimageslider.SliderPager;

public class PageIndicatorView
extends View
implements SliderPager.OnPageChangeListener,
IndicatorManager.Listener,
SliderPager.OnAdapterChangeListener {
    private boolean isInteractionEnabled;
    private IndicatorManager manager;
    private DataSetObserver setObserver;
    private SliderPager viewPager;

    public PageIndicatorView(Context context) {
        super(context);
        this.init(null);
    }

    public PageIndicatorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.init(attributeSet);
    }

    public PageIndicatorView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.init(attributeSet);
    }

    public PageIndicatorView(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.init(attributeSet);
    }

    private int adjustPosition(int n) {
        int n2;
        int n3 = this.manager.indicator().getCount() - 1;
        if (n <= 0) {
            n2 = 0;
        } else {
            n2 = n;
            if (n > n3) {
                n2 = n3;
            }
        }
        return n2;
    }

    private SliderPager findViewPager(ViewGroup viewGroup, int n) {
        if (viewGroup.getChildCount() <= 0) {
            return null;
        }
        if ((viewGroup = viewGroup.findViewById(n)) != null && viewGroup instanceof SliderPager) {
            return (SliderPager)viewGroup;
        }
        return null;
    }

    private void findViewPager(ViewParent viewParent) {
        int n = viewParent != null && viewParent instanceof ViewGroup && ((ViewGroup)viewParent).getChildCount() > 0 ? 1 : 0;
        if (n == 0) {
            return;
        }
        n = this.manager.indicator().getViewPagerId();
        SliderPager sliderPager = this.findViewPager((ViewGroup)viewParent, n);
        if (sliderPager != null) {
            this.setViewPager(sliderPager);
        } else {
            this.findViewPager(viewParent.getParent());
        }
    }

    private void init(AttributeSet attributeSet) {
        this.setupId();
        this.initIndicatorManager(attributeSet);
    }

    private void initIndicatorManager(AttributeSet object) {
        IndicatorManager indicatorManager;
        this.manager = indicatorManager = new IndicatorManager(this);
        indicatorManager.drawer().initAttributes(this.getContext(), (AttributeSet)object);
        object = this.manager.indicator();
        ((Indicator)object).setPaddingLeft(this.getPaddingLeft());
        ((Indicator)object).setPaddingTop(this.getPaddingTop());
        ((Indicator)object).setPaddingRight(this.getPaddingRight());
        ((Indicator)object).setPaddingBottom(this.getPaddingBottom());
        this.isInteractionEnabled = ((Indicator)object).isInteractiveAnimation();
    }

    private boolean isRtl() {
        int n = 2.$SwitchMap$com$smarteist$autoimageslider$IndicatorView$draw$data$RtlMode[this.manager.indicator().getRtlMode().ordinal()];
        boolean bl = false;
        switch (n) {
            default: {
                return false;
            }
            case 3: {
                if (TextUtilsCompat.getLayoutDirectionFromLocale(this.getContext().getResources().getConfiguration().locale) == 1) {
                    bl = true;
                }
                return bl;
            }
            case 2: {
                return false;
            }
            case 1: 
        }
        return true;
    }

    private boolean isViewMeasured() {
        boolean bl = this.getMeasuredHeight() != 0 || this.getMeasuredWidth() != 0;
        return bl;
    }

    private void onPageScroll(int n, float f) {
        Indicator indicator = this.manager.indicator();
        Pair<Integer, Float> pair = indicator.getAnimationType();
        boolean bl = indicator.isInteractiveAnimation();
        boolean bl2 = this.isViewMeasured() && bl && pair != IndicatorAnimationType.NONE;
        if (!bl2) {
            return;
        }
        pair = CoordinatesUtils.getProgress(indicator, n, f, this.isRtl());
        this.setProgress((Integer)pair.first, ((Float)pair.second).floatValue());
    }

    private void onPageSelect(int n) {
        Indicator indicator = this.manager.indicator();
        boolean bl = this.isViewMeasured();
        int n2 = indicator.getCount();
        if (bl) {
            int n3 = n;
            if (this.isRtl()) {
                n3 = n2 - 1 - n;
            }
            this.setSelection(n3);
        }
    }

    private void registerSetObserver() {
        SliderPager sliderPager;
        if (this.setObserver == null && (sliderPager = this.viewPager) != null && sliderPager.getAdapter() != null) {
            this.setObserver = new DataSetObserver(this){
                final PageIndicatorView this$0;
                {
                    this.this$0 = pageIndicatorView;
                }

                public void onChanged() {
                    this.this$0.updateState();
                }
            };
            try {
                this.viewPager.getAdapter().registerDataSetObserver(this.setObserver);
            }
            catch (IllegalStateException illegalStateException) {
                illegalStateException.printStackTrace();
            }
            return;
        }
    }

    private void setupId() {
        if (this.getId() == -1) {
            this.setId(IdUtils.generateViewId());
        }
    }

    private void unRegisterSetObserver() {
        SliderPager sliderPager;
        if (this.setObserver != null && (sliderPager = this.viewPager) != null && sliderPager.getAdapter() != null) {
            try {
                this.viewPager.getAdapter().unregisterDataSetObserver(this.setObserver);
                this.setObserver = null;
            }
            catch (IllegalStateException illegalStateException) {
                illegalStateException.printStackTrace();
            }
            return;
        }
    }

    private void updateState() {
        SliderPager sliderPager = this.viewPager;
        if (sliderPager != null && sliderPager.getAdapter() != null) {
            int n;
            int n2;
            if (this.viewPager.getAdapter() instanceof InfinitePagerAdapter) {
                n2 = ((InfinitePagerAdapter)this.viewPager.getAdapter()).getRealCount();
                n = n2 > 0 ? this.viewPager.getCurrentItem() % n2 : 0;
            } else {
                n2 = this.viewPager.getAdapter().getCount();
                n = this.viewPager.getCurrentItem();
            }
            if (this.isRtl()) {
                n = n2 - 1 - n;
            }
            this.manager.indicator().setSelectedPosition(n);
            this.manager.indicator().setSelectingPosition(n);
            this.manager.indicator().setLastSelectedPosition(n);
            this.manager.indicator().setCount(n2);
            this.manager.animate().end();
            this.updateVisibility();
            this.requestLayout();
            return;
        }
    }

    private void updateVisibility() {
        if (!this.manager.indicator().isAutoVisibility()) {
            return;
        }
        int n = this.manager.indicator().getCount();
        int n2 = this.getVisibility();
        if (n2 != 0 && n > 1) {
            this.setVisibility(0);
        } else if (n2 != 4 && n <= 1) {
            this.setVisibility(4);
        }
    }

    public void clearSelection() {
        Indicator indicator = this.manager.indicator();
        indicator.setInteractiveAnimation(false);
        indicator.setLastSelectedPosition(-1);
        indicator.setSelectingPosition(-1);
        indicator.setSelectedPosition(-1);
        this.manager.animate().basic();
    }

    public long getAnimationDuration() {
        return this.manager.indicator().getAnimationDuration();
    }

    public int getCount() {
        return this.manager.indicator().getCount();
    }

    public int getPadding() {
        return this.manager.indicator().getPadding();
    }

    public int getRadius() {
        return this.manager.indicator().getRadius();
    }

    public float getScaleFactor() {
        return this.manager.indicator().getScaleFactor();
    }

    public int getSelectedColor() {
        return this.manager.indicator().getSelectedColor();
    }

    public int getSelection() {
        return this.manager.indicator().getSelectedPosition();
    }

    public int getStrokeWidth() {
        return this.manager.indicator().getStroke();
    }

    public int getUnselectedColor() {
        return this.manager.indicator().getUnselectedColor();
    }

    @Override
    public void onAdapterChanged(SliderPager sliderPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
        this.updateState();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.findViewPager(this.getParent());
    }

    protected void onDetachedFromWindow() {
        this.unRegisterSetObserver();
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        this.manager.drawer().draw(canvas);
    }

    @Override
    public void onIndicatorUpdated() {
        this.invalidate();
    }

    protected void onMeasure(int n, int n2) {
        Pair<Integer, Integer> pair = this.manager.drawer().measureViewSize(n, n2);
        this.setMeasuredDimension((Integer)pair.first, (Integer)pair.second);
    }

    @Override
    public void onPageScrollStateChanged(int n) {
        if (n == 0) {
            this.manager.indicator().setInteractiveAnimation(this.isInteractionEnabled);
        }
    }

    @Override
    public void onPageScrolled(int n, float f, int n2) {
        this.onPageScroll(n, f);
    }

    @Override
    public void onPageSelected(int n) {
        this.onPageSelect(n);
    }

    public void onRestoreInstanceState(Parcelable object) {
        if (object instanceof PositionSavedState) {
            Indicator indicator = this.manager.indicator();
            object = (PositionSavedState)((Object)object);
            indicator.setSelectedPosition(((PositionSavedState)((Object)object)).getSelectedPosition());
            indicator.setSelectingPosition(((PositionSavedState)((Object)object)).getSelectingPosition());
            indicator.setLastSelectedPosition(((PositionSavedState)((Object)object)).getLastSelectedPosition());
            super.onRestoreInstanceState(object.getSuperState());
        } else {
            super.onRestoreInstanceState((Parcelable)object);
        }
    }

    public Parcelable onSaveInstanceState() {
        Indicator indicator = this.manager.indicator();
        PositionSavedState positionSavedState = new PositionSavedState(super.onSaveInstanceState());
        positionSavedState.setSelectedPosition(indicator.getSelectedPosition());
        positionSavedState.setSelectingPosition(indicator.getSelectingPosition());
        positionSavedState.setLastSelectedPosition(indicator.getLastSelectedPosition());
        return positionSavedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.manager.drawer().touch(motionEvent);
        return true;
    }

    public void releaseViewPager() {
        SliderPager sliderPager = this.viewPager;
        if (sliderPager != null) {
            sliderPager.removeOnPageChangeListener(this);
            this.viewPager = null;
        }
    }

    public void setAnimationDuration(long l) {
        this.manager.indicator().setAnimationDuration(l);
    }

    public void setAnimationType(IndicatorAnimationType indicatorAnimationType) {
        this.manager.onValueUpdated(null);
        if (indicatorAnimationType != null) {
            this.manager.indicator().setAnimationType(indicatorAnimationType);
        } else {
            this.manager.indicator().setAnimationType(IndicatorAnimationType.NONE);
        }
        this.invalidate();
    }

    public void setAutoVisibility(boolean bl) {
        if (!bl) {
            this.setVisibility(0);
        }
        this.manager.indicator().setAutoVisibility(bl);
        this.updateVisibility();
    }

    public void setClickListener(DrawController.ClickListener clickListener) {
        this.manager.drawer().setClickListener(clickListener);
    }

    public void setCount(int n) {
        if (n >= 0 && this.manager.indicator().getCount() != n) {
            this.manager.indicator().setCount(n);
            this.updateVisibility();
            this.requestLayout();
        }
    }

    public void setDynamicCount(boolean bl) {
        this.manager.indicator().setDynamicCount(bl);
        if (bl) {
            this.registerSetObserver();
        } else {
            this.unRegisterSetObserver();
        }
    }

    public void setInteractiveAnimation(boolean bl) {
        this.manager.indicator().setInteractiveAnimation(bl);
        this.isInteractionEnabled = bl;
    }

    public void setOrientation(Orientation orientation) {
        if (orientation != null) {
            this.manager.indicator().setOrientation(orientation);
            this.requestLayout();
        }
    }

    public void setPadding(float f) {
        float f2 = f;
        if (f < 0.0f) {
            f2 = 0.0f;
        }
        this.manager.indicator().setPadding((int)f2);
        this.invalidate();
    }

    public void setPadding(int n) {
        int n2 = n;
        if (n < 0) {
            n2 = 0;
        }
        n = DensityUtils.dpToPx(n2);
        this.manager.indicator().setPadding(n);
        this.invalidate();
    }

    public void setProgress(int n, float f) {
        float f2;
        int n2;
        Indicator indicator = this.manager.indicator();
        if (!indicator.isInteractiveAnimation()) {
            return;
        }
        int n3 = indicator.getCount();
        if (n3 > 0 && n >= 0) {
            n2 = n;
            if (n > n3 - 1) {
                n2 = n3 - 1;
            }
        } else {
            n2 = 0;
        }
        if (f < 0.0f) {
            f2 = 0.0f;
        } else {
            f2 = f;
            if (f > 1.0f) {
                f2 = 1.0f;
            }
        }
        if (f2 == 1.0f) {
            indicator.setLastSelectedPosition(indicator.getSelectedPosition());
            indicator.setSelectedPosition(n2);
        }
        indicator.setSelectingPosition(n2);
        this.manager.animate().interactive(f2);
    }

    public void setRadius(float f) {
        float f2 = f;
        if (f < 0.0f) {
            f2 = 0.0f;
        }
        this.manager.indicator().setRadius((int)f2);
        this.invalidate();
    }

    public void setRadius(int n) {
        int n2 = n;
        if (n < 0) {
            n2 = 0;
        }
        n = DensityUtils.dpToPx(n2);
        this.manager.indicator().setRadius(n);
        this.invalidate();
    }

    public void setRtlMode(RtlMode object) {
        int n;
        Indicator indicator = this.manager.indicator();
        if (object == null) {
            indicator.setRtlMode(RtlMode.Off);
        } else {
            indicator.setRtlMode((RtlMode)((Object)object));
        }
        if (this.viewPager == null) {
            return;
        }
        int n2 = n = indicator.getSelectedPosition();
        if (this.isRtl()) {
            n2 = indicator.getCount() - 1 - n;
        } else {
            object = this.viewPager;
            if (object != null) {
                n2 = ((SliderPager)((Object)object)).getCurrentItem();
            }
        }
        indicator.setLastSelectedPosition(n2);
        indicator.setSelectingPosition(n2);
        indicator.setSelectedPosition(n2);
        this.invalidate();
    }

    public void setScaleFactor(float f) {
        float f2;
        if (f > 1.0f) {
            f2 = 1.0f;
        } else {
            f2 = f;
            if (f < 0.3f) {
                f2 = 0.3f;
            }
        }
        this.manager.indicator().setScaleFactor(f2);
    }

    public void setSelected(int n) {
        Indicator indicator = this.manager.indicator();
        IndicatorAnimationType indicatorAnimationType = indicator.getAnimationType();
        indicator.setAnimationType(IndicatorAnimationType.NONE);
        this.setSelection(n);
        indicator.setAnimationType(indicatorAnimationType);
    }

    public void setSelectedColor(int n) {
        this.manager.indicator().setSelectedColor(n);
        this.invalidate();
    }

    public void setSelection(int n) {
        Indicator indicator = this.manager.indicator();
        if ((n = this.adjustPosition(n)) != indicator.getSelectedPosition() && n != indicator.getSelectingPosition()) {
            indicator.setInteractiveAnimation(false);
            indicator.setLastSelectedPosition(indicator.getSelectedPosition());
            indicator.setSelectingPosition(n);
            indicator.setSelectedPosition(n);
            this.manager.animate().basic();
            return;
        }
    }

    public void setStrokeWidth(float f) {
        float f2;
        int n = this.manager.indicator().getRadius();
        if (f < 0.0f) {
            f2 = 0.0f;
        } else {
            f2 = f;
            if (f > (float)n) {
                f2 = n;
            }
        }
        this.manager.indicator().setStroke((int)f2);
        this.invalidate();
    }

    public void setStrokeWidth(int n) {
        int n2 = DensityUtils.dpToPx(n);
        int n3 = this.manager.indicator().getRadius();
        if (n2 < 0) {
            n = 0;
        } else {
            n = n2;
            if (n2 > n3) {
                n = n3;
            }
        }
        this.manager.indicator().setStroke(n);
        this.invalidate();
    }

    public void setUnselectedColor(int n) {
        this.manager.indicator().setUnselectedColor(n);
        this.invalidate();
    }

    public void setViewPager(SliderPager sliderPager) {
        this.releaseViewPager();
        if (sliderPager == null) {
            return;
        }
        this.viewPager = sliderPager;
        sliderPager.addOnPageChangeListener(this);
        this.viewPager.addOnAdapterChangeListener(this);
        this.manager.indicator().setViewPagerId(this.viewPager.getId());
        this.setDynamicCount(this.manager.indicator().isDynamicCount());
        this.updateState();
    }
}

