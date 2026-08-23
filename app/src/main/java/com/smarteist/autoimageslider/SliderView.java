/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Color
 *  android.os.Handler
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnTouchListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.Interpolator
 *  android.widget.FrameLayout
 *  android.widget.FrameLayout$LayoutParams
 */
package com.smarteist.autoimageslider;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import com.smarteist.autoimageslider.IndicatorView.PageIndicatorView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.AttributeController;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.IndicatorView.draw.data.Orientation;
import com.smarteist.autoimageslider.IndicatorView.draw.data.RtlMode;
import com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils;
import com.smarteist.autoimageslider.InfiniteAdapter.InfinitePagerAdapter;
import com.smarteist.autoimageslider.R;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderPager;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.smarteist.autoimageslider.Transformations.AntiClockSpinTransformation;
import com.smarteist.autoimageslider.Transformations.Clock_SpinTransformation;
import com.smarteist.autoimageslider.Transformations.CubeInDepthTransformation;
import com.smarteist.autoimageslider.Transformations.CubeInRotationTransformation;
import com.smarteist.autoimageslider.Transformations.CubeInScalingTransformation;
import com.smarteist.autoimageslider.Transformations.CubeOutDepthTransformation;
import com.smarteist.autoimageslider.Transformations.CubeOutRotationTransformation;
import com.smarteist.autoimageslider.Transformations.CubeOutScalingTransformation;
import com.smarteist.autoimageslider.Transformations.DepthTransformation;
import com.smarteist.autoimageslider.Transformations.FadeTransformation;
import com.smarteist.autoimageslider.Transformations.FanTransformation;
import com.smarteist.autoimageslider.Transformations.FidgetSpinTransformation;
import com.smarteist.autoimageslider.Transformations.GateTransformation;
import com.smarteist.autoimageslider.Transformations.HingeTransformation;
import com.smarteist.autoimageslider.Transformations.HorizontalFlipTransformation;
import com.smarteist.autoimageslider.Transformations.PopTransformation;
import com.smarteist.autoimageslider.Transformations.SimpleTransformation;
import com.smarteist.autoimageslider.Transformations.SpinnerTransformation;
import com.smarteist.autoimageslider.Transformations.TossTransformation;
import com.smarteist.autoimageslider.Transformations.VerticalFlipTransformation;
import com.smarteist.autoimageslider.Transformations.VerticalShutTransformation;
import com.smarteist.autoimageslider.Transformations.ZoomOutTransformation;

public class SliderView
extends FrameLayout
implements Runnable,
View.OnTouchListener,
SliderViewAdapter.DataSetListener,
SliderPager.OnPageChangeListener {
    public static final int AUTO_CYCLE_DIRECTION_BACK_AND_FORTH = 2;
    public static final int AUTO_CYCLE_DIRECTION_LEFT = 1;
    public static final int AUTO_CYCLE_DIRECTION_RIGHT = 0;
    public static final String TAG = "Slider View : ";
    private int mAutoCycleDirection;
    private boolean mFlagBackAndForth;
    private final Handler mHandler = new Handler();
    private InfinitePagerAdapter mInfinitePagerAdapter;
    private boolean mIsAutoCycle;
    private boolean mIsIndicatorEnabled = true;
    private boolean mIsInfiniteAdapter = true;
    private OnSliderPageListener mPageListener;
    private SliderViewAdapter mPagerAdapter;
    private PageIndicatorView mPagerIndicator;
    private int mPreviousPosition = -1;
    private int mScrollTimeInMillis;
    private SliderPager mSliderPager;

    public SliderView(Context context) {
        super(context);
        this.setupSlideView(context);
    }

    public SliderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.setupSlideView(context);
        this.setUpAttributes(context, attributeSet);
    }

    public SliderView(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        this.setupSlideView(context);
        this.setUpAttributes(context, attributeSet);
    }

    private int getAdapterItemsCount() {
        try {
            int n = this.getSliderAdapter().getCount();
            return n;
        }
        catch (NullPointerException nullPointerException) {
            Log.e((String)TAG, (String)"getAdapterItemsCount: Slider Adapter is null so, it can't get count of items");
            return 0;
        }
    }

    private void initIndicator() {
        if (this.mPagerIndicator == null) {
            this.mPagerIndicator = new PageIndicatorView(this.getContext());
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-2, -2);
            layoutParams.gravity = 81;
            layoutParams.setMargins(20, 20, 20, 20);
            this.addView(this.mPagerIndicator, 1, (ViewGroup.LayoutParams)layoutParams);
        }
        this.mPagerIndicator.setViewPager(this.mSliderPager);
        this.mPagerIndicator.setDynamicCount(true);
    }

    private void setUpAttributes(Context object, AttributeSet attributeSet) {
        attributeSet = object.obtainStyledAttributes(attributeSet, R.styleable.SliderView, 0, 0);
        boolean bl = attributeSet.getBoolean(R.styleable.SliderView_sliderIndicatorEnabled, true);
        int n = attributeSet.getInt(R.styleable.SliderView_sliderAnimationDuration, 250);
        int n2 = attributeSet.getInt(R.styleable.SliderView_sliderScrollTimeInSec, 2);
        boolean bl2 = attributeSet.getBoolean(R.styleable.SliderView_sliderAutoCycleEnabled, true);
        boolean bl3 = attributeSet.getBoolean(R.styleable.SliderView_sliderStartAutoCycle, false);
        int n3 = attributeSet.getInt(R.styleable.SliderView_sliderAutoCycleDirection, 0);
        this.setSliderAnimationDuration(n);
        this.setScrollTimeInSec(n2);
        this.setAutoCycle(bl2);
        this.setAutoCycleDirection(n3);
        this.setAutoCycle(bl3);
        this.setIndicatorEnabled(bl);
        if (this.mIsIndicatorEnabled) {
            this.initIndicator();
            object = attributeSet.getInt(R.styleable.SliderView_sliderIndicatorOrientation, Orientation.HORIZONTAL.ordinal()) == 0 ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            n = (int)attributeSet.getDimension(R.styleable.SliderView_sliderIndicatorRadius, (float)DensityUtils.dpToPx(2));
            int n4 = (int)attributeSet.getDimension(R.styleable.SliderView_sliderIndicatorPadding, (float)DensityUtils.dpToPx(3));
            int n5 = (int)attributeSet.getDimension(R.styleable.SliderView_sliderIndicatorMargin, (float)DensityUtils.dpToPx(12));
            int n6 = (int)attributeSet.getDimension(R.styleable.SliderView_sliderIndicatorMarginLeft, (float)DensityUtils.dpToPx(12));
            n3 = (int)attributeSet.getDimension(R.styleable.SliderView_sliderIndicatorMarginTop, (float)DensityUtils.dpToPx(12));
            int n7 = (int)attributeSet.getDimension(R.styleable.SliderView_sliderIndicatorMarginRight, (float)DensityUtils.dpToPx(12));
            int n8 = (int)attributeSet.getDimension(R.styleable.SliderView_sliderIndicatorMarginBottom, (float)DensityUtils.dpToPx(12));
            int n9 = attributeSet.getInt(R.styleable.SliderView_sliderIndicatorGravity, 81);
            int n10 = attributeSet.getColor(R.styleable.SliderView_sliderIndicatorUnselectedColor, Color.parseColor((String)"#33ffffff"));
            int n11 = attributeSet.getColor(R.styleable.SliderView_sliderIndicatorSelectedColor, Color.parseColor((String)"#ffffff"));
            n2 = attributeSet.getInt(R.styleable.SliderView_sliderIndicatorAnimationDuration, 350);
            RtlMode rtlMode = AttributeController.getRtlMode(attributeSet.getInt(R.styleable.SliderView_sliderIndicatorRtlMode, RtlMode.Off.ordinal()));
            this.setIndicatorOrientation((Orientation)((Object)object));
            this.setIndicatorRadius(n);
            this.setIndicatorPadding(n4);
            this.setIndicatorMargin(n5);
            this.setIndicatorMarginCustom(n6, n3, n7, n8);
            this.setIndicatorGravity(n9);
            this.setIndicatorMargins(n6, n3, n7, n8);
            this.setIndicatorUnselectedColor(n10);
            this.setIndicatorSelectedColor(n11);
            this.setIndicatorAnimationDuration(n2);
            this.setIndicatorRtlMode(rtlMode);
        }
        attributeSet.recycle();
    }

    private void setupSlideView(Context object) {
        object = new SliderPager((Context)object);
        this.mSliderPager = object;
        object.setOverScrollMode(1);
        this.mSliderPager.setId(ViewCompat.generateViewId());
        object = new FrameLayout.LayoutParams(-1, -1);
        this.addView((View)this.mSliderPager, 0, (ViewGroup.LayoutParams)object);
        this.mSliderPager.setOnTouchListener(this);
        this.mSliderPager.addOnPageChangeListener(this);
    }

    @Override
    public void dataSetChanged() {
        if (this.mIsInfiniteAdapter) {
            this.mInfinitePagerAdapter.notifyDataSetChanged();
            this.mSliderPager.setCurrentItem(0, false);
        }
    }

    public int getAutoCycleDirection() {
        return this.mAutoCycleDirection;
    }

    public int getCurrentPagePosition() {
        if (this.getSliderAdapter() != null) {
            return this.getSliderPager().getCurrentItem();
        }
        throw new NullPointerException("Adapter not set");
    }

    public int getIndicatorRadius() {
        return this.mPagerIndicator.getRadius();
    }

    public int getIndicatorSelectedColor() {
        return this.mPagerIndicator.getSelectedColor();
    }

    public int getIndicatorUnselectedColor() {
        return this.mPagerIndicator.getUnselectedColor();
    }

    public PageIndicatorView getPagerIndicator() {
        return this.mPagerIndicator;
    }

    public int getScrollTimeInMillis() {
        return this.mScrollTimeInMillis;
    }

    public int getScrollTimeInSec() {
        return this.mScrollTimeInMillis / 1000;
    }

    public PagerAdapter getSliderAdapter() {
        return this.mPagerAdapter;
    }

    public SliderPager getSliderPager() {
        return this.mSliderPager;
    }

    public boolean isAutoCycle() {
        return this.mIsAutoCycle;
    }

    @Override
    public void onPageScrollStateChanged(int n) {
    }

    @Override
    public void onPageScrolled(int n, float f, int n2) {
    }

    @Override
    public void onPageSelected(int n) {
        OnSliderPageListener onSliderPageListener = this.mPageListener;
        if (onSliderPageListener != null) {
            onSliderPageListener.onSliderPageChanged(n);
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (this.isAutoCycle()) {
            if (motionEvent.getAction() == 2) {
                this.stopAutoCycle();
            } else if (motionEvent.getAction() == 1) {
                this.mHandler.postDelayed(new Runnable(this){
                    final SliderView this$0;
                    {
                        this.this$0 = sliderView;
                    }

                    @Override
                    public void run() {
                        this.this$0.startAutoCycle();
                    }
                }, 2000L);
            }
        }
        return false;
    }

    @Override
    public void run() {
        try {
            this.slideToNextPosition();
            return;
        }
        finally {
            if (this.mIsAutoCycle) {
                this.mHandler.postDelayed((Runnable)this, (long)this.mScrollTimeInMillis);
            }
        }
    }

    public void setAutoCycle(boolean bl) {
        this.mIsAutoCycle = bl;
    }

    public void setAutoCycleDirection(int n) {
        this.mAutoCycleDirection = n;
    }

    public void setCurrentPageListener(OnSliderPageListener onSliderPageListener) {
        this.mPageListener = onSliderPageListener;
    }

    public void setCurrentPagePosition(int n) {
        this.mSliderPager.setCurrentItem(n, true);
    }

    public void setCustomSliderTransformAnimation(SliderPager.PageTransformer pageTransformer) {
        this.mSliderPager.setPageTransformer(false, pageTransformer);
    }

    public void setIndicatorAnimation(IndicatorAnimationType indicatorAnimationType) {
        this.mPagerIndicator.setAnimationType(indicatorAnimationType);
    }

    public void setIndicatorAnimationDuration(long l) {
        this.mPagerIndicator.setAnimationDuration(l);
    }

    public void setIndicatorEnabled(boolean bl) {
        this.mIsIndicatorEnabled = bl;
        if (this.mPagerIndicator == null && bl) {
            this.initIndicator();
        }
    }

    public void setIndicatorGravity(int n) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mPagerIndicator.getLayoutParams();
        layoutParams.gravity = n;
        this.mPagerIndicator.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void setIndicatorMargin(int n) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mPagerIndicator.getLayoutParams();
        layoutParams.setMargins(n, n, n, n);
        this.mPagerIndicator.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void setIndicatorMarginCustom(int n, int n2, int n3, int n4) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mPagerIndicator.getLayoutParams();
        layoutParams.setMargins(n, n2, n3, n4);
        this.mPagerIndicator.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void setIndicatorMargins(int n, int n2, int n3, int n4) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)this.mPagerIndicator.getLayoutParams();
        layoutParams.setMargins(n, n2, n3, n4);
        this.mPagerIndicator.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
    }

    public void setIndicatorOrientation(Orientation orientation) {
        this.mPagerIndicator.setOrientation(orientation);
    }

    public void setIndicatorPadding(int n) {
        this.mPagerIndicator.setPadding(n);
    }

    public void setIndicatorRadius(int n) {
        this.mPagerIndicator.setRadius(n);
    }

    public void setIndicatorRtlMode(RtlMode rtlMode) {
        this.mPagerIndicator.setRtlMode(rtlMode);
    }

    public void setIndicatorSelectedColor(int n) {
        this.mPagerIndicator.setSelectedColor(n);
    }

    public void setIndicatorUnselectedColor(int n) {
        this.mPagerIndicator.setUnselectedColor(n);
    }

    public void setIndicatorVisibility(boolean bl) {
        if (bl) {
            this.mPagerIndicator.setVisibility(0);
        } else {
            this.mPagerIndicator.setVisibility(8);
        }
    }

    public void setInfiniteAdapterEnabled(boolean bl) {
        SliderViewAdapter sliderViewAdapter = this.mPagerAdapter;
        if (sliderViewAdapter != null) {
            this.setSliderAdapter(sliderViewAdapter, bl);
        }
    }

    public void setOffscreenPageLimit(int n) {
        this.mSliderPager.setOffscreenPageLimit(n);
    }

    public void setOnIndicatorClickListener(DrawController.ClickListener clickListener) {
        this.mPagerIndicator.setClickListener(clickListener);
    }

    public void setPageIndicatorView(PageIndicatorView pageIndicatorView) {
        this.mPagerIndicator = pageIndicatorView;
        this.initIndicator();
    }

    public void setScrollTimeInMillis(int n) {
        this.mScrollTimeInMillis = n;
    }

    public void setScrollTimeInSec(int n) {
        this.mScrollTimeInMillis = n * 1000;
    }

    public void setSliderAdapter(SliderViewAdapter pagerAdapter) {
        this.mPagerAdapter = pagerAdapter;
        pagerAdapter = new InfinitePagerAdapter((SliderViewAdapter)pagerAdapter);
        this.mInfinitePagerAdapter = pagerAdapter;
        this.mSliderPager.setAdapter(pagerAdapter);
        this.mPagerAdapter.dataSetChangedListener(this);
        this.setCurrentPagePosition(0);
    }

    public void setSliderAdapter(SliderViewAdapter sliderViewAdapter, boolean bl) {
        this.mIsInfiniteAdapter = bl;
        if (!bl) {
            this.mPagerAdapter = sliderViewAdapter;
            this.mSliderPager.setAdapter(sliderViewAdapter);
        } else {
            this.setSliderAdapter(sliderViewAdapter);
        }
    }

    public void setSliderAnimationDuration(int n) {
        this.mSliderPager.setScrollDuration(n);
    }

    public void setSliderAnimationDuration(int n, Interpolator interpolator2) {
        this.mSliderPager.setScrollDuration(n, interpolator2);
    }

    public void setSliderTransformAnimation(SliderAnimations sliderAnimations) {
        switch (2.$SwitchMap$com$smarteist$autoimageslider$SliderAnimations[sliderAnimations.ordinal()]) {
            default: {
                this.mSliderPager.setPageTransformer(false, new SimpleTransformation());
                break;
            }
            case 21: {
                this.mSliderPager.setPageTransformer(false, new ZoomOutTransformation());
                break;
            }
            case 20: {
                this.mSliderPager.setPageTransformer(false, new VerticalShutTransformation());
                break;
            }
            case 19: {
                this.mSliderPager.setPageTransformer(false, new VerticalFlipTransformation());
                break;
            }
            case 18: {
                this.mSliderPager.setPageTransformer(false, new TossTransformation());
                break;
            }
            case 17: {
                this.mSliderPager.setPageTransformer(false, new SpinnerTransformation());
                break;
            }
            case 16: {
                this.mSliderPager.setPageTransformer(false, new PopTransformation());
                break;
            }
            case 15: {
                this.mSliderPager.setPageTransformer(false, new HorizontalFlipTransformation());
                break;
            }
            case 14: {
                this.mSliderPager.setPageTransformer(false, new HingeTransformation());
                break;
            }
            case 13: {
                this.mSliderPager.setPageTransformer(false, new GateTransformation());
                break;
            }
            case 12: {
                this.mSliderPager.setPageTransformer(false, new FidgetSpinTransformation());
                break;
            }
            case 11: {
                this.mSliderPager.setPageTransformer(false, new FanTransformation());
                break;
            }
            case 10: {
                this.mSliderPager.setPageTransformer(false, new FadeTransformation());
                break;
            }
            case 9: {
                this.mSliderPager.setPageTransformer(false, new DepthTransformation());
                break;
            }
            case 8: {
                this.mSliderPager.setPageTransformer(false, new CubeOutScalingTransformation());
                break;
            }
            case 7: {
                this.mSliderPager.setPageTransformer(false, new CubeOutRotationTransformation());
                break;
            }
            case 6: {
                this.mSliderPager.setPageTransformer(false, new CubeOutDepthTransformation());
                break;
            }
            case 5: {
                this.mSliderPager.setPageTransformer(false, new CubeInScalingTransformation());
                break;
            }
            case 4: {
                this.mSliderPager.setPageTransformer(false, new CubeInRotationTransformation());
                break;
            }
            case 3: {
                this.mSliderPager.setPageTransformer(false, new CubeInDepthTransformation());
                break;
            }
            case 2: {
                this.mSliderPager.setPageTransformer(false, new Clock_SpinTransformation());
                break;
            }
            case 1: {
                this.mSliderPager.setPageTransformer(false, new AntiClockSpinTransformation());
            }
        }
    }

    public void slideToNextPosition() {
        int n = this.mSliderPager.getCurrentItem();
        int n2 = this.getAdapterItemsCount();
        if (n2 > 1) {
            if (this.mAutoCycleDirection == 2) {
                if (n % (n2 - 1) == 0 && this.mPreviousPosition != this.getAdapterItemsCount() - 1 && this.mPreviousPosition != 0) {
                    this.mFlagBackAndForth ^= true;
                }
                if (this.mFlagBackAndForth) {
                    this.mSliderPager.setCurrentItem(n + 1, true);
                } else {
                    this.mSliderPager.setCurrentItem(n - 1, true);
                }
            }
            if (this.mAutoCycleDirection == 1) {
                this.mSliderPager.setCurrentItem(n - 1, true);
            }
            if (this.mAutoCycleDirection == 0) {
                this.mSliderPager.setCurrentItem(n + 1, true);
            }
        }
        this.mPreviousPosition = n;
    }

    public void slideToPreviousPosition() {
        int n = this.mSliderPager.getCurrentItem();
        int n2 = this.getAdapterItemsCount();
        if (n2 > 1) {
            if (this.mAutoCycleDirection == 2) {
                if (n % (n2 - 1) == 0 && this.mPreviousPosition != this.getAdapterItemsCount() - 1 && this.mPreviousPosition != 0) {
                    this.mFlagBackAndForth ^= true;
                }
                if (this.mFlagBackAndForth && n < this.mPreviousPosition) {
                    this.mSliderPager.setCurrentItem(n - 1, true);
                } else {
                    this.mSliderPager.setCurrentItem(n + 1, true);
                }
            }
            if (this.mAutoCycleDirection == 1) {
                this.mSliderPager.setCurrentItem(n + 1, true);
            }
            if (this.mAutoCycleDirection == 0) {
                this.mSliderPager.setCurrentItem(n - 1, true);
            }
        }
        this.mPreviousPosition = n;
    }

    public void startAutoCycle() {
        this.mHandler.removeCallbacks((Runnable)this);
        this.mHandler.postDelayed((Runnable)this, (long)this.mScrollTimeInMillis);
    }

    public void stopAutoCycle() {
        this.mHandler.removeCallbacks((Runnable)this);
    }

    public static interface OnSliderPageListener {
        public void onSliderPageChanged(int var1);
    }
}

