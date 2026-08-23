/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.database.DataSetObserver
 *  android.graphics.Canvas
 *  android.graphics.Paint
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.RectF
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.RippleDrawable
 *  android.os.Build$VERSION
 *  android.text.Layout
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.widget.FrameLayout$LayoutParams
 *  android.widget.HorizontalScrollView
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 */
package com.google.android.material.tabs;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Pools;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.PointerIconCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.tabs.TabItem;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

@ViewPager.DecorView
public class TabLayout
extends HorizontalScrollView {
    private static final int ANIMATION_DURATION = 300;
    static final int DEFAULT_GAP_TEXT_ICON = 8;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int DEFAULT_HEIGHT_WITH_TEXT_ICON = 72;
    static final int FIXED_WRAP_GUTTER_MIN = 16;
    public static final int GRAVITY_CENTER = 1;
    public static final int GRAVITY_FILL = 0;
    public static final int INDICATOR_GRAVITY_BOTTOM = 0;
    public static final int INDICATOR_GRAVITY_CENTER = 1;
    public static final int INDICATOR_GRAVITY_STRETCH = 3;
    public static final int INDICATOR_GRAVITY_TOP = 2;
    private static final int INVALID_WIDTH = -1;
    private static final int MIN_INDICATOR_WIDTH = 24;
    public static final int MODE_FIXED = 1;
    public static final int MODE_SCROLLABLE = 0;
    private static final int TAB_MIN_WIDTH_MARGIN = 56;
    private static final Pools.Pool<Tab> tabPool = new Pools.SynchronizedPool<Tab>(16);
    private AdapterChangeListener adapterChangeListener;
    private int contentInsetStart;
    private BaseOnTabSelectedListener currentVpSelectedListener;
    boolean inlineLabel;
    int mode;
    private TabLayoutOnPageChangeListener pageChangeListener;
    private PagerAdapter pagerAdapter;
    private DataSetObserver pagerAdapterObserver;
    private final int requestedTabMaxWidth;
    private final int requestedTabMinWidth;
    private ValueAnimator scrollAnimator;
    private final int scrollableTabMinWidth;
    private BaseOnTabSelectedListener selectedListener;
    private final ArrayList<BaseOnTabSelectedListener> selectedListeners;
    private Tab selectedTab;
    private boolean setupViewPagerImplicitly;
    private final SlidingTabIndicator slidingTabIndicator;
    final int tabBackgroundResId;
    int tabGravity;
    ColorStateList tabIconTint;
    PorterDuff.Mode tabIconTintMode;
    int tabIndicatorAnimationDuration;
    boolean tabIndicatorFullWidth;
    int tabIndicatorGravity;
    int tabMaxWidth;
    int tabPaddingBottom;
    int tabPaddingEnd;
    int tabPaddingStart;
    int tabPaddingTop;
    ColorStateList tabRippleColorStateList;
    Drawable tabSelectedIndicator;
    int tabTextAppearance;
    ColorStateList tabTextColors;
    float tabTextMultiLineSize;
    float tabTextSize;
    private final RectF tabViewContentBounds;
    private final Pools.Pool<TabView> tabViewPool;
    private final ArrayList<Tab> tabs;
    boolean unboundedRipple;
    ViewPager viewPager;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.tabStyle);
    }

    public TabLayout(Context context, AttributeSet attributeSet, int n) {
        TypedArray typedArray;
        block4: {
            SlidingTabIndicator slidingTabIndicator;
            super(context, attributeSet, n);
            this.tabs = new ArrayList();
            this.tabViewContentBounds = new RectF();
            this.tabMaxWidth = Integer.MAX_VALUE;
            this.selectedListeners = new ArrayList();
            this.tabViewPool = new Pools.SimplePool<TabView>(12);
            this.setHorizontalScrollBarEnabled(false);
            this.slidingTabIndicator = slidingTabIndicator = new SlidingTabIndicator(this, context);
            super.addView((View)slidingTabIndicator, 0, (ViewGroup.LayoutParams)new FrameLayout.LayoutParams(-2, -1));
            typedArray = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R.styleable.TabLayout, n, R.style.Widget_Design_TabLayout, R.styleable.TabLayout_tabTextAppearance);
            slidingTabIndicator.setSelectedIndicatorHeight(typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabIndicatorHeight, -1));
            slidingTabIndicator.setSelectedIndicatorColor(typedArray.getColor(R.styleable.TabLayout_tabIndicatorColor, 0));
            this.setSelectedTabIndicator(MaterialResources.getDrawable(context, typedArray, R.styleable.TabLayout_tabIndicator));
            this.setSelectedTabIndicatorGravity(typedArray.getInt(R.styleable.TabLayout_tabIndicatorGravity, 0));
            this.setTabIndicatorFullWidth(typedArray.getBoolean(R.styleable.TabLayout_tabIndicatorFullWidth, true));
            this.tabPaddingBottom = n = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabPadding, 0);
            this.tabPaddingEnd = n;
            this.tabPaddingTop = n;
            this.tabPaddingStart = n;
            this.tabPaddingStart = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingStart, this.tabPaddingStart);
            this.tabPaddingTop = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingTop, this.tabPaddingTop);
            this.tabPaddingEnd = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingEnd, this.tabPaddingEnd);
            this.tabPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabPaddingBottom, this.tabPaddingBottom);
            this.tabTextAppearance = n = typedArray.getResourceId(R.styleable.TabLayout_tabTextAppearance, R.style.TextAppearance_Design_Tab);
            attributeSet = context.obtainStyledAttributes(n, R.styleable.TextAppearance);
            this.tabTextSize = attributeSet.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, 0);
            this.tabTextColors = MaterialResources.getColorStateList(context, (TypedArray)attributeSet, R.styleable.TextAppearance_android_textColor);
            if (!typedArray.hasValue(R.styleable.TabLayout_tabTextColor)) break block4;
            this.tabTextColors = MaterialResources.getColorStateList(context, typedArray, R.styleable.TabLayout_tabTextColor);
        }
        if (typedArray.hasValue(R.styleable.TabLayout_tabSelectedTextColor)) {
            n = typedArray.getColor(R.styleable.TabLayout_tabSelectedTextColor, 0);
            this.tabTextColors = TabLayout.createColorStateList(this.tabTextColors.getDefaultColor(), n);
        }
        this.tabIconTint = MaterialResources.getColorStateList(context, typedArray, R.styleable.TabLayout_tabIconTint);
        this.tabIconTintMode = ViewUtils.parseTintMode(typedArray.getInt(R.styleable.TabLayout_tabIconTintMode, -1), null);
        this.tabRippleColorStateList = MaterialResources.getColorStateList(context, typedArray, R.styleable.TabLayout_tabRippleColor);
        this.tabIndicatorAnimationDuration = typedArray.getInt(R.styleable.TabLayout_tabIndicatorAnimationDuration, 300);
        this.requestedTabMinWidth = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabMinWidth, -1);
        this.requestedTabMaxWidth = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabMaxWidth, -1);
        this.tabBackgroundResId = typedArray.getResourceId(R.styleable.TabLayout_tabBackground, 0);
        this.contentInsetStart = typedArray.getDimensionPixelSize(R.styleable.TabLayout_tabContentStart, 0);
        this.mode = typedArray.getInt(R.styleable.TabLayout_tabMode, 1);
        this.tabGravity = typedArray.getInt(R.styleable.TabLayout_tabGravity, 0);
        this.inlineLabel = typedArray.getBoolean(R.styleable.TabLayout_tabInlineLabel, false);
        this.unboundedRipple = typedArray.getBoolean(R.styleable.TabLayout_tabUnboundedRipple, false);
        typedArray.recycle();
        context = this.getResources();
        this.tabTextMultiLineSize = context.getDimensionPixelSize(R.dimen.design_tab_text_size_2line);
        this.scrollableTabMinWidth = context.getDimensionPixelSize(R.dimen.design_tab_scrollable_min_width);
        this.applyModeAndGravity();
        return;
        finally {
            attributeSet.recycle();
        }
    }

    private void addTabFromItemView(TabItem tabItem) {
        Tab tab = this.newTab();
        if (tabItem.text != null) {
            tab.setText(tabItem.text);
        }
        if (tabItem.icon != null) {
            tab.setIcon(tabItem.icon);
        }
        if (tabItem.customLayout != 0) {
            tab.setCustomView(tabItem.customLayout);
        }
        if (!TextUtils.isEmpty((CharSequence)tabItem.getContentDescription())) {
            tab.setContentDescription(tabItem.getContentDescription());
        }
        this.addTab(tab);
    }

    private void addTabView(Tab tab) {
        TabView tabView = tab.view;
        this.slidingTabIndicator.addView((View)tabView, tab.getPosition(), (ViewGroup.LayoutParams)this.createLayoutParamsForTabs());
    }

    private void addViewInternal(View view) {
        if (view instanceof TabItem) {
            this.addTabFromItemView((TabItem)view);
            return;
        }
        throw new IllegalArgumentException("Only TabItem instances can be added to TabLayout");
    }

    private void animateToTab(int n) {
        if (n == -1) {
            return;
        }
        if (this.getWindowToken() != null && ViewCompat.isLaidOut((View)this) && !this.slidingTabIndicator.childrenNeedLayout()) {
            int n2;
            int n3 = this.getScrollX();
            if (n3 != (n2 = this.calculateScrollXForTab(n, 0.0f))) {
                this.ensureScrollAnimator();
                this.scrollAnimator.setIntValues(new int[]{n3, n2});
                this.scrollAnimator.start();
            }
            this.slidingTabIndicator.animateIndicatorToPosition(n, this.tabIndicatorAnimationDuration);
            return;
        }
        this.setScrollPosition(n, 0.0f, true);
    }

    private void applyModeAndGravity() {
        int n = 0;
        if (this.mode == 0) {
            n = Math.max(0, this.contentInsetStart - this.tabPaddingStart);
        }
        ViewCompat.setPaddingRelative((View)this.slidingTabIndicator, n, 0, 0, 0);
        switch (this.mode) {
            default: {
                break;
            }
            case 1: {
                this.slidingTabIndicator.setGravity(1);
                break;
            }
            case 0: {
                this.slidingTabIndicator.setGravity(0x800003);
            }
        }
        this.updateTabViews(true);
    }

    private int calculateScrollXForTab(int n, float f) {
        int n2 = this.mode;
        int n3 = 0;
        if (n2 == 0) {
            View view = this.slidingTabIndicator.getChildAt(n);
            View view2 = n + 1 < this.slidingTabIndicator.getChildCount() ? this.slidingTabIndicator.getChildAt(n + 1) : null;
            n = view != null ? view.getWidth() : 0;
            if (view2 != null) {
                n3 = view2.getWidth();
            }
            n2 = view.getLeft() + n / 2 - this.getWidth() / 2;
            n = (int)((float)(n + n3) * 0.5f * f);
            n = ViewCompat.getLayoutDirection((View)this) == 0 ? n2 + n : n2 - n;
            return n;
        }
        return 0;
    }

    private void configureTab(Tab tab, int n) {
        tab.setPosition(n);
        this.tabs.add(n, tab);
        int n2 = this.tabs.size();
        ++n;
        while (n < n2) {
            this.tabs.get(n).setPosition(n);
            ++n;
        }
    }

    private static ColorStateList createColorStateList(int n, int n2) {
        int[][] nArrayArray = new int[2][];
        int[] nArray = new int[2];
        nArrayArray[0] = SELECTED_STATE_SET;
        nArray[0] = n2;
        n2 = 0 + 1;
        nArrayArray[n2] = EMPTY_STATE_SET;
        nArray[n2] = n;
        return new ColorStateList((int[][])nArrayArray, nArray);
    }

    private LinearLayout.LayoutParams createLayoutParamsForTabs() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -1);
        this.updateTabViewLayoutParams(layoutParams);
        return layoutParams;
    }

    private TabView createTabView(Tab tab) {
        Object object = this.tabViewPool;
        object = object != null ? object.acquire() : null;
        Object object2 = object;
        if (object == null) {
            object2 = new TabView(this, this.getContext());
        }
        ((TabView)((Object)object2)).setTab(tab);
        object2.setFocusable(true);
        object2.setMinimumWidth(this.getTabMinWidth());
        if (TextUtils.isEmpty((CharSequence)tab.contentDesc)) {
            object2.setContentDescription(tab.text);
        } else {
            object2.setContentDescription(tab.contentDesc);
        }
        return object2;
    }

    private void dispatchTabReselected(Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            this.selectedListeners.get(i).onTabReselected(tab);
        }
    }

    private void dispatchTabSelected(Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            this.selectedListeners.get(i).onTabSelected(tab);
        }
    }

    private void dispatchTabUnselected(Tab tab) {
        for (int i = this.selectedListeners.size() - 1; i >= 0; --i) {
            this.selectedListeners.get(i).onTabUnselected(tab);
        }
    }

    private void ensureScrollAnimator() {
        if (this.scrollAnimator == null) {
            ValueAnimator valueAnimator;
            this.scrollAnimator = valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
            this.scrollAnimator.setDuration((long)this.tabIndicatorAnimationDuration);
            this.scrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
                final TabLayout this$0;
                {
                    this.this$0 = tabLayout;
                }

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    this.this$0.scrollTo((Integer)valueAnimator.getAnimatedValue(), 0);
                }
            });
        }
    }

    private int getDefaultHeight() {
        boolean bl;
        boolean bl2 = false;
        int n = 0;
        int n2 = this.tabs.size();
        while (true) {
            bl = bl2;
            if (n >= n2) break;
            Tab tab = this.tabs.get(n);
            if (tab != null && tab.getIcon() != null && !TextUtils.isEmpty((CharSequence)tab.getText())) {
                bl = true;
                break;
            }
            ++n;
        }
        n = bl && !this.inlineLabel ? 72 : 48;
        return n;
    }

    private int getTabMinWidth() {
        int n = this.requestedTabMinWidth;
        if (n != -1) {
            return n;
        }
        n = this.mode == 0 ? this.scrollableTabMinWidth : 0;
        return n;
    }

    private int getTabScrollRange() {
        return Math.max(0, this.slidingTabIndicator.getWidth() - this.getWidth() - this.getPaddingLeft() - this.getPaddingRight());
    }

    private void removeTabViewAt(int n) {
        TabView tabView = (TabView)this.slidingTabIndicator.getChildAt(n);
        this.slidingTabIndicator.removeViewAt(n);
        if (tabView != null) {
            tabView.reset();
            this.tabViewPool.release(tabView);
        }
        this.requestLayout();
    }

    private void setSelectedTabView(int n) {
        int n2 = this.slidingTabIndicator.getChildCount();
        if (n < n2) {
            for (int i = 0; i < n2; ++i) {
                View view = this.slidingTabIndicator.getChildAt(i);
                boolean bl = false;
                boolean bl2 = i == n;
                view.setSelected(bl2);
                bl2 = bl;
                if (i == n) {
                    bl2 = true;
                }
                view.setActivated(bl2);
            }
        }
    }

    private void setupWithViewPager(ViewPager viewPager, boolean bl, boolean bl2) {
        Object object;
        ViewPager viewPager2 = this.viewPager;
        if (viewPager2 != null) {
            object = this.pageChangeListener;
            if (object != null) {
                viewPager2.removeOnPageChangeListener((ViewPager.OnPageChangeListener)object);
            }
            if ((object = this.adapterChangeListener) != null) {
                this.viewPager.removeOnAdapterChangeListener((ViewPager.OnAdapterChangeListener)object);
            }
        }
        if ((object = this.currentVpSelectedListener) != null) {
            this.removeOnTabSelectedListener((BaseOnTabSelectedListener)object);
            this.currentVpSelectedListener = null;
        }
        if (viewPager != null) {
            this.viewPager = viewPager;
            if (this.pageChangeListener == null) {
                this.pageChangeListener = new TabLayoutOnPageChangeListener(this);
            }
            this.pageChangeListener.reset();
            viewPager.addOnPageChangeListener(this.pageChangeListener);
            this.currentVpSelectedListener = object = new ViewPagerOnTabSelectedListener(viewPager);
            this.addOnTabSelectedListener((BaseOnTabSelectedListener)object);
            object = viewPager.getAdapter();
            if (object != null) {
                this.setPagerAdapter((PagerAdapter)object, bl);
            }
            if (this.adapterChangeListener == null) {
                this.adapterChangeListener = new AdapterChangeListener(this);
            }
            this.adapterChangeListener.setAutoRefresh(bl);
            viewPager.addOnAdapterChangeListener(this.adapterChangeListener);
            this.setScrollPosition(viewPager.getCurrentItem(), 0.0f, true);
        } else {
            this.viewPager = null;
            this.setPagerAdapter(null, false);
        }
        this.setupViewPagerImplicitly = bl2;
    }

    private void updateAllTabs() {
        int n = this.tabs.size();
        for (int i = 0; i < n; ++i) {
            this.tabs.get(i).updateView();
        }
    }

    private void updateTabViewLayoutParams(LinearLayout.LayoutParams layoutParams) {
        if (this.mode == 1 && this.tabGravity == 0) {
            layoutParams.width = 0;
            layoutParams.weight = 1.0f;
        } else {
            layoutParams.width = -2;
            layoutParams.weight = 0.0f;
        }
    }

    public void addOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        if (!this.selectedListeners.contains(baseOnTabSelectedListener)) {
            this.selectedListeners.add(baseOnTabSelectedListener);
        }
    }

    public void addTab(Tab tab) {
        this.addTab(tab, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int n) {
        this.addTab(tab, n, this.tabs.isEmpty());
    }

    public void addTab(Tab tab, int n, boolean bl) {
        if (tab.parent == this) {
            this.configureTab(tab, n);
            this.addTabView(tab);
            if (bl) {
                tab.select();
            }
            return;
        }
        throw new IllegalArgumentException("Tab belongs to a different TabLayout.");
    }

    public void addTab(Tab tab, boolean bl) {
        this.addTab(tab, this.tabs.size(), bl);
    }

    public void addView(View view) {
        this.addViewInternal(view);
    }

    public void addView(View view, int n) {
        this.addViewInternal(view);
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        this.addViewInternal(view);
    }

    public void addView(View view, ViewGroup.LayoutParams layoutParams) {
        this.addViewInternal(view);
    }

    public void clearOnTabSelectedListeners() {
        this.selectedListeners.clear();
    }

    protected Tab createTabFromPool() {
        Tab tab;
        Tab tab2 = tab = tabPool.acquire();
        if (tab == null) {
            tab2 = new Tab();
        }
        return tab2;
    }

    int dpToPx(int n) {
        return Math.round(this.getResources().getDisplayMetrics().density * (float)n);
    }

    public FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return this.generateDefaultLayoutParams();
    }

    public int getSelectedTabPosition() {
        Tab tab = this.selectedTab;
        int n = tab != null ? tab.getPosition() : -1;
        return n;
    }

    public Tab getTabAt(int n) {
        Tab tab = n >= 0 && n < this.getTabCount() ? this.tabs.get(n) : null;
        return tab;
    }

    public int getTabCount() {
        return this.tabs.size();
    }

    public int getTabGravity() {
        return this.tabGravity;
    }

    public ColorStateList getTabIconTint() {
        return this.tabIconTint;
    }

    public int getTabIndicatorGravity() {
        return this.tabIndicatorGravity;
    }

    int getTabMaxWidth() {
        return this.tabMaxWidth;
    }

    public int getTabMode() {
        return this.mode;
    }

    public ColorStateList getTabRippleColor() {
        return this.tabRippleColorStateList;
    }

    public Drawable getTabSelectedIndicator() {
        return this.tabSelectedIndicator;
    }

    public ColorStateList getTabTextColors() {
        return this.tabTextColors;
    }

    public boolean hasUnboundedRipple() {
        return this.unboundedRipple;
    }

    public boolean isInlineLabel() {
        return this.inlineLabel;
    }

    public boolean isTabIndicatorFullWidth() {
        return this.tabIndicatorFullWidth;
    }

    public Tab newTab() {
        Tab tab = this.createTabFromPool();
        tab.parent = this;
        tab.view = this.createTabView(tab);
        return tab;
    }

    protected void onAttachedToWindow() {
        ViewParent viewParent;
        super.onAttachedToWindow();
        if (this.viewPager == null && (viewParent = this.getParent()) instanceof ViewPager) {
            this.setupWithViewPager((ViewPager)viewParent, true, true);
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.setupViewPagerImplicitly) {
            this.setupWithViewPager(null);
            this.setupViewPagerImplicitly = false;
        }
    }

    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
            View view = this.slidingTabIndicator.getChildAt(i);
            if (!(view instanceof TabView)) continue;
            ((TabView)view).drawBackground(canvas);
        }
        super.onDraw(canvas);
    }

    protected void onMeasure(int n, int n2) {
        int n3 = this.dpToPx(this.getDefaultHeight()) + this.getPaddingTop() + this.getPaddingBottom();
        switch (View.MeasureSpec.getMode((int)n2)) {
            default: {
                break;
            }
            case 0: {
                n2 = View.MeasureSpec.makeMeasureSpec((int)n3, (int)0x40000000);
                break;
            }
            case -2147483648: {
                n2 = View.MeasureSpec.makeMeasureSpec((int)Math.min(n3, View.MeasureSpec.getSize((int)n2)), (int)0x40000000);
            }
        }
        int n4 = View.MeasureSpec.getSize((int)n);
        if (View.MeasureSpec.getMode((int)n) != 0) {
            n3 = this.requestedTabMaxWidth;
            if (n3 <= 0) {
                n3 = n4 - this.dpToPx(56);
            }
            this.tabMaxWidth = n3;
        }
        super.onMeasure(n, n2);
        n4 = this.getChildCount();
        n3 = 1;
        n = 1;
        if (n4 == 1) {
            View view = this.getChildAt(0);
            n4 = 0;
            switch (this.mode) {
                default: {
                    n = n4;
                    break;
                }
                case 1: {
                    if (view.getMeasuredWidth() != this.getMeasuredWidth()) break;
                    n = 0;
                    break;
                }
                case 0: {
                    n = view.getMeasuredWidth() < this.getMeasuredWidth() ? n3 : 0;
                }
            }
            if (n != 0) {
                n = TabLayout.getChildMeasureSpec((int)n2, (int)(this.getPaddingTop() + this.getPaddingBottom()), (int)view.getLayoutParams().height);
                view.measure(View.MeasureSpec.makeMeasureSpec((int)this.getMeasuredWidth(), (int)0x40000000), n);
            }
        }
    }

    void populateFromPagerAdapter() {
        this.removeAllTabs();
        Object object = this.pagerAdapter;
        if (object != null) {
            int n;
            int n2 = object.getCount();
            for (n = 0; n < n2; ++n) {
                this.addTab(this.newTab().setText(this.pagerAdapter.getPageTitle(n)), false);
            }
            object = this.viewPager;
            if (object != null && n2 > 0 && (n = ((ViewPager)((Object)object)).getCurrentItem()) != this.getSelectedTabPosition() && n < this.getTabCount()) {
                this.selectTab(this.getTabAt(n));
            }
        }
    }

    protected boolean releaseFromTabPool(Tab tab) {
        return tabPool.release(tab);
    }

    public void removeAllTabs() {
        for (int i = this.slidingTabIndicator.getChildCount() - 1; i >= 0; --i) {
            this.removeTabViewAt(i);
        }
        Iterator<Tab> iterator2 = this.tabs.iterator();
        while (iterator2.hasNext()) {
            Tab tab = iterator2.next();
            iterator2.remove();
            tab.reset();
            this.releaseFromTabPool(tab);
        }
        this.selectedTab = null;
    }

    public void removeOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        this.selectedListeners.remove(baseOnTabSelectedListener);
    }

    public void removeTab(Tab tab) {
        if (tab.parent == this) {
            this.removeTabAt(tab.getPosition());
            return;
        }
        throw new IllegalArgumentException("Tab does not belong to this TabLayout.");
    }

    public void removeTabAt(int n) {
        Tab tab = this.selectedTab;
        int n2 = tab != null ? tab.getPosition() : 0;
        this.removeTabViewAt(n);
        tab = this.tabs.remove(n);
        if (tab != null) {
            tab.reset();
            this.releaseFromTabPool(tab);
        }
        int n3 = this.tabs.size();
        for (int i = n; i < n3; ++i) {
            this.tabs.get(i).setPosition(i);
        }
        if (n2 == n) {
            tab = this.tabs.isEmpty() ? null : this.tabs.get(Math.max(0, n - 1));
            this.selectTab(tab);
        }
    }

    void selectTab(Tab tab) {
        this.selectTab(tab, true);
    }

    void selectTab(Tab tab, boolean bl) {
        Tab tab2 = this.selectedTab;
        if (tab2 == tab) {
            if (tab2 != null) {
                this.dispatchTabReselected(tab);
                this.animateToTab(tab.getPosition());
            }
        } else {
            int n = tab != null ? tab.getPosition() : -1;
            if (bl) {
                if ((tab2 == null || tab2.getPosition() == -1) && n != -1) {
                    this.setScrollPosition(n, 0.0f, true);
                } else {
                    this.animateToTab(n);
                }
                if (n != -1) {
                    this.setSelectedTabView(n);
                }
            }
            this.selectedTab = tab;
            if (tab2 != null) {
                this.dispatchTabUnselected(tab2);
            }
            if (tab != null) {
                this.dispatchTabSelected(tab);
            }
        }
    }

    public void setInlineLabel(boolean bl) {
        if (this.inlineLabel != bl) {
            this.inlineLabel = bl;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                View view = this.slidingTabIndicator.getChildAt(i);
                if (!(view instanceof TabView)) continue;
                ((TabView)view).updateOrientation();
            }
            this.applyModeAndGravity();
        }
    }

    public void setInlineLabelResource(int n) {
        this.setInlineLabel(this.getResources().getBoolean(n));
    }

    @Deprecated
    public void setOnTabSelectedListener(BaseOnTabSelectedListener baseOnTabSelectedListener) {
        BaseOnTabSelectedListener baseOnTabSelectedListener2 = this.selectedListener;
        if (baseOnTabSelectedListener2 != null) {
            this.removeOnTabSelectedListener(baseOnTabSelectedListener2);
        }
        this.selectedListener = baseOnTabSelectedListener;
        if (baseOnTabSelectedListener != null) {
            this.addOnTabSelectedListener(baseOnTabSelectedListener);
        }
    }

    void setPagerAdapter(PagerAdapter pagerAdapter, boolean bl) {
        DataSetObserver dataSetObserver;
        PagerAdapter pagerAdapter2 = this.pagerAdapter;
        if (pagerAdapter2 != null && (dataSetObserver = this.pagerAdapterObserver) != null) {
            pagerAdapter2.unregisterDataSetObserver(dataSetObserver);
        }
        this.pagerAdapter = pagerAdapter;
        if (bl && pagerAdapter != null) {
            if (this.pagerAdapterObserver == null) {
                this.pagerAdapterObserver = new PagerAdapterObserver(this);
            }
            pagerAdapter.registerDataSetObserver(this.pagerAdapterObserver);
        }
        this.populateFromPagerAdapter();
    }

    void setScrollAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.ensureScrollAnimator();
        this.scrollAnimator.addListener(animatorListener);
    }

    public void setScrollPosition(int n, float f, boolean bl) {
        this.setScrollPosition(n, f, bl, true);
    }

    void setScrollPosition(int n, float f, boolean bl, boolean bl2) {
        int n2 = Math.round((float)n + f);
        if (n2 >= 0 && n2 < this.slidingTabIndicator.getChildCount()) {
            ValueAnimator valueAnimator;
            if (bl2) {
                this.slidingTabIndicator.setIndicatorPositionFromTabPosition(n, f);
            }
            if ((valueAnimator = this.scrollAnimator) != null && valueAnimator.isRunning()) {
                this.scrollAnimator.cancel();
            }
            this.scrollTo(this.calculateScrollXForTab(n, f), 0);
            if (bl) {
                this.setSelectedTabView(n2);
            }
            return;
        }
    }

    public void setSelectedTabIndicator(int n) {
        if (n != 0) {
            this.setSelectedTabIndicator(AppCompatResources.getDrawable(this.getContext(), n));
        } else {
            this.setSelectedTabIndicator(null);
        }
    }

    public void setSelectedTabIndicator(Drawable drawable2) {
        if (this.tabSelectedIndicator != drawable2) {
            this.tabSelectedIndicator = drawable2;
            ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
        }
    }

    public void setSelectedTabIndicatorColor(int n) {
        this.slidingTabIndicator.setSelectedIndicatorColor(n);
    }

    public void setSelectedTabIndicatorGravity(int n) {
        if (this.tabIndicatorGravity != n) {
            this.tabIndicatorGravity = n;
            ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
        }
    }

    @Deprecated
    public void setSelectedTabIndicatorHeight(int n) {
        this.slidingTabIndicator.setSelectedIndicatorHeight(n);
    }

    public void setTabGravity(int n) {
        if (this.tabGravity != n) {
            this.tabGravity = n;
            this.applyModeAndGravity();
        }
    }

    public void setTabIconTint(ColorStateList colorStateList) {
        if (this.tabIconTint != colorStateList) {
            this.tabIconTint = colorStateList;
            this.updateAllTabs();
        }
    }

    public void setTabIconTintResource(int n) {
        this.setTabIconTint(AppCompatResources.getColorStateList(this.getContext(), n));
    }

    public void setTabIndicatorFullWidth(boolean bl) {
        this.tabIndicatorFullWidth = bl;
        ViewCompat.postInvalidateOnAnimation((View)this.slidingTabIndicator);
    }

    public void setTabMode(int n) {
        if (n != this.mode) {
            this.mode = n;
            this.applyModeAndGravity();
        }
    }

    public void setTabRippleColor(ColorStateList colorStateList) {
        if (this.tabRippleColorStateList != colorStateList) {
            this.tabRippleColorStateList = colorStateList;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                colorStateList = this.slidingTabIndicator.getChildAt(i);
                if (!(colorStateList instanceof TabView)) continue;
                ((TabView)colorStateList).updateBackgroundDrawable(this.getContext());
            }
        }
    }

    public void setTabRippleColorResource(int n) {
        this.setTabRippleColor(AppCompatResources.getColorStateList(this.getContext(), n));
    }

    public void setTabTextColors(int n, int n2) {
        this.setTabTextColors(TabLayout.createColorStateList(n, n2));
    }

    public void setTabTextColors(ColorStateList colorStateList) {
        if (this.tabTextColors != colorStateList) {
            this.tabTextColors = colorStateList;
            this.updateAllTabs();
        }
    }

    @Deprecated
    public void setTabsFromPagerAdapter(PagerAdapter pagerAdapter) {
        this.setPagerAdapter(pagerAdapter, false);
    }

    public void setUnboundedRipple(boolean bl) {
        if (this.unboundedRipple != bl) {
            this.unboundedRipple = bl;
            for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
                View view = this.slidingTabIndicator.getChildAt(i);
                if (!(view instanceof TabView)) continue;
                ((TabView)view).updateBackgroundDrawable(this.getContext());
            }
        }
    }

    public void setUnboundedRippleResource(int n) {
        this.setUnboundedRipple(this.getResources().getBoolean(n));
    }

    public void setupWithViewPager(ViewPager viewPager) {
        this.setupWithViewPager(viewPager, true);
    }

    public void setupWithViewPager(ViewPager viewPager, boolean bl) {
        this.setupWithViewPager(viewPager, bl, false);
    }

    public boolean shouldDelayChildPressedState() {
        boolean bl = this.getTabScrollRange() > 0;
        return bl;
    }

    void updateTabViews(boolean bl) {
        for (int i = 0; i < this.slidingTabIndicator.getChildCount(); ++i) {
            View view = this.slidingTabIndicator.getChildAt(i);
            view.setMinimumWidth(this.getTabMinWidth());
            this.updateTabViewLayoutParams((LinearLayout.LayoutParams)view.getLayoutParams());
            if (!bl) continue;
            view.requestLayout();
        }
    }

    private class AdapterChangeListener
    implements ViewPager.OnAdapterChangeListener {
        private boolean autoRefresh;
        final TabLayout this$0;

        AdapterChangeListener(TabLayout tabLayout) {
            this.this$0 = tabLayout;
        }

        @Override
        public void onAdapterChanged(ViewPager viewPager, PagerAdapter pagerAdapter, PagerAdapter pagerAdapter2) {
            if (this.this$0.viewPager == viewPager) {
                this.this$0.setPagerAdapter(pagerAdapter2, this.autoRefresh);
            }
        }

        void setAutoRefresh(boolean bl) {
            this.autoRefresh = bl;
        }
    }

    public static interface BaseOnTabSelectedListener<T extends Tab> {
        public void onTabReselected(T var1);

        public void onTabSelected(T var1);

        public void onTabUnselected(T var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Mode {
    }

    public static interface OnTabSelectedListener
    extends BaseOnTabSelectedListener<Tab> {
    }

    private class PagerAdapterObserver
    extends DataSetObserver {
        final TabLayout this$0;

        PagerAdapterObserver(TabLayout tabLayout) {
            this.this$0 = tabLayout;
        }

        public void onChanged() {
            this.this$0.populateFromPagerAdapter();
        }

        public void onInvalidated() {
            this.this$0.populateFromPagerAdapter();
        }
    }

    private class SlidingTabIndicator
    extends LinearLayout {
        private final GradientDrawable defaultSelectionIndicator;
        private ValueAnimator indicatorAnimator;
        private int indicatorLeft;
        private int indicatorRight;
        private int layoutDirection;
        private int selectedIndicatorHeight;
        private final Paint selectedIndicatorPaint;
        int selectedPosition;
        float selectionOffset;
        final TabLayout this$0;

        SlidingTabIndicator(TabLayout tabLayout, Context context) {
            this.this$0 = tabLayout;
            super(context);
            this.selectedPosition = -1;
            this.layoutDirection = -1;
            this.indicatorLeft = -1;
            this.indicatorRight = -1;
            this.setWillNotDraw(false);
            this.selectedIndicatorPaint = new Paint();
            this.defaultSelectionIndicator = new GradientDrawable();
        }

        private void calculateTabViewContentBounds(TabView tabView, RectF rectF) {
            int n;
            int n2 = n = tabView.getContentWidth();
            if (n < this.this$0.dpToPx(24)) {
                n2 = this.this$0.dpToPx(24);
            }
            int n3 = (tabView.getLeft() + tabView.getRight()) / 2;
            n = n2 / 2;
            rectF.set((float)(n3 - n), 0.0f, (float)((n2 /= 2) + n3), 0.0f);
        }

        private void updateIndicatorPosition() {
            int n;
            int n2;
            View view = this.getChildAt(this.selectedPosition);
            if (view != null && view.getWidth() > 0) {
                n2 = view.getLeft();
                n = view.getRight();
                int n3 = n2;
                int n4 = n;
                if (!this.this$0.tabIndicatorFullWidth) {
                    n3 = n2;
                    n4 = n;
                    if (view instanceof TabView) {
                        this.calculateTabViewContentBounds((TabView)view, this.this$0.tabViewContentBounds);
                        n3 = (int)((TabLayout)this.this$0).tabViewContentBounds.left;
                        n4 = (int)((TabLayout)this.this$0).tabViewContentBounds.right;
                    }
                }
                n2 = n3;
                n = n4;
                if (this.selectionOffset > 0.0f) {
                    n2 = n3;
                    n = n4;
                    if (this.selectedPosition < this.getChildCount() - 1) {
                        view = this.getChildAt(this.selectedPosition + 1);
                        int n5 = view.getLeft();
                        int n6 = view.getRight();
                        n = n5;
                        n2 = n6;
                        if (!this.this$0.tabIndicatorFullWidth) {
                            n = n5;
                            n2 = n6;
                            if (view instanceof TabView) {
                                this.calculateTabViewContentBounds((TabView)view, this.this$0.tabViewContentBounds);
                                n = (int)((TabLayout)this.this$0).tabViewContentBounds.left;
                                n2 = (int)((TabLayout)this.this$0).tabViewContentBounds.right;
                            }
                        }
                        float f = this.selectionOffset;
                        n3 = (int)((float)n * f + (1.0f - f) * (float)n3);
                        n = (int)((float)n2 * f + (1.0f - f) * (float)n4);
                        n2 = n3;
                    }
                }
            } else {
                n2 = -1;
                n = -1;
            }
            this.setIndicatorPosition(n2, n);
        }

        void animateIndicatorToPosition(int n, int n2) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            if ((valueAnimator = this.getChildAt(n)) == null) {
                this.updateIndicatorPosition();
                return;
            }
            int n3 = valueAnimator.getLeft();
            int n4 = valueAnimator.getRight();
            if (!this.this$0.tabIndicatorFullWidth && valueAnimator instanceof TabView) {
                this.calculateTabViewContentBounds((TabView)valueAnimator, this.this$0.tabViewContentBounds);
                n3 = (int)((TabLayout)this.this$0).tabViewContentBounds.left;
                n4 = (int)((TabLayout)this.this$0).tabViewContentBounds.right;
            }
            int n5 = this.indicatorLeft;
            int n6 = this.indicatorRight;
            if (n5 != n3 || n6 != n4) {
                this.indicatorAnimator = valueAnimator = new ValueAnimator();
                valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
                valueAnimator.setDuration((long)n2);
                valueAnimator.setFloatValues(new float[]{0.0f, 1.0f});
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, n5, n3, n6, n4){
                    final SlidingTabIndicator this$1;
                    final int val$finalTargetLeft;
                    final int val$finalTargetRight;
                    final int val$startLeft;
                    final int val$startRight;
                    {
                        this.this$1 = slidingTabIndicator;
                        this.val$startLeft = n;
                        this.val$finalTargetLeft = n2;
                        this.val$startRight = n3;
                        this.val$finalTargetRight = n4;
                    }

                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        float f = valueAnimator.getAnimatedFraction();
                        this.this$1.setIndicatorPosition(AnimationUtils.lerp(this.val$startLeft, this.val$finalTargetLeft, f), AnimationUtils.lerp(this.val$startRight, this.val$finalTargetRight, f));
                    }
                });
                valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, n){
                    final SlidingTabIndicator this$1;
                    final int val$position;
                    {
                        this.this$1 = slidingTabIndicator;
                        this.val$position = n;
                    }

                    public void onAnimationEnd(Animator animator2) {
                        this.this$1.selectedPosition = this.val$position;
                        this.this$1.selectionOffset = 0.0f;
                    }
                });
                valueAnimator.start();
            }
        }

        boolean childrenNeedLayout() {
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                if (this.getChildAt(i).getWidth() > 0) continue;
                return true;
            }
            return false;
        }

        public void draw(Canvas canvas) {
            int n = 0;
            if (this.this$0.tabSelectedIndicator != null) {
                n = this.this$0.tabSelectedIndicator.getIntrinsicHeight();
            }
            if (this.selectedIndicatorHeight >= 0) {
                n = this.selectedIndicatorHeight;
            }
            int n2 = 0;
            int n3 = 0;
            switch (this.this$0.tabIndicatorGravity) {
                default: {
                    n = n2;
                    break;
                }
                case 3: {
                    n = 0;
                    n3 = this.getHeight();
                    break;
                }
                case 2: {
                    n2 = 0;
                    n3 = n;
                    n = n2;
                    break;
                }
                case 1: {
                    n2 = (this.getHeight() - n) / 2;
                    n3 = (this.getHeight() + n) / 2;
                    n = n2;
                    break;
                }
                case 0: {
                    n = this.getHeight() - n;
                    n3 = this.getHeight();
                }
            }
            n2 = this.indicatorLeft;
            if (n2 >= 0 && this.indicatorRight > n2) {
                Object object = this.this$0.tabSelectedIndicator != null ? this.this$0.tabSelectedIndicator : this.defaultSelectionIndicator;
                object = DrawableCompat.wrap((Drawable)object);
                object.setBounds(this.indicatorLeft, n, this.indicatorRight, n3);
                if (this.selectedIndicatorPaint != null) {
                    if (Build.VERSION.SDK_INT == 21) {
                        object.setColorFilter(this.selectedIndicatorPaint.getColor(), PorterDuff.Mode.SRC_IN);
                    } else {
                        DrawableCompat.setTint((Drawable)object, this.selectedIndicatorPaint.getColor());
                    }
                }
                object.draw(canvas);
            }
            super.draw(canvas);
        }

        float getIndicatorPosition() {
            return (float)this.selectedPosition + this.selectionOffset;
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            super.onLayout(bl, n, n2, n3, n4);
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
                long l = this.indicatorAnimator.getDuration();
                this.animateIndicatorToPosition(this.selectedPosition, Math.round((1.0f - this.indicatorAnimator.getAnimatedFraction()) * (float)l));
            } else {
                this.updateIndicatorPosition();
            }
        }

        protected void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            if (View.MeasureSpec.getMode((int)n) != 0x40000000) {
                return;
            }
            if (this.this$0.mode == 1 && this.this$0.tabGravity == 1) {
                int n3;
                View view;
                int n4;
                int n5 = this.getChildCount();
                int n6 = 0;
                for (n4 = 0; n4 < n5; ++n4) {
                    view = this.getChildAt(n4);
                    n3 = n6;
                    if (view.getVisibility() == 0) {
                        n3 = Math.max(n6, view.getMeasuredWidth());
                    }
                    n6 = n3;
                }
                if (n6 <= 0) {
                    return;
                }
                n3 = this.this$0.dpToPx(16);
                n4 = 0;
                if (n6 * n5 <= this.getMeasuredWidth() - n3 * 2) {
                    for (n3 = 0; n3 < n5; ++n3) {
                        view = (LinearLayout.LayoutParams)this.getChildAt(n3).getLayoutParams();
                        if (view.width == n6 && view.weight == 0.0f) continue;
                        view.width = n6;
                        view.weight = 0.0f;
                        n4 = 1;
                    }
                } else {
                    this.this$0.tabGravity = 0;
                    this.this$0.updateTabViews(false);
                    n4 = 1;
                }
                if (n4 != 0) {
                    super.onMeasure(n, n2);
                }
            }
        }

        public void onRtlPropertiesChanged(int n) {
            super.onRtlPropertiesChanged(n);
            if (Build.VERSION.SDK_INT < 23 && this.layoutDirection != n) {
                this.requestLayout();
                this.layoutDirection = n;
            }
        }

        void setIndicatorPosition(int n, int n2) {
            if (n != this.indicatorLeft || n2 != this.indicatorRight) {
                this.indicatorLeft = n;
                this.indicatorRight = n2;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }

        void setIndicatorPositionFromTabPosition(int n, float f) {
            ValueAnimator valueAnimator = this.indicatorAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.indicatorAnimator.cancel();
            }
            this.selectedPosition = n;
            this.selectionOffset = f;
            this.updateIndicatorPosition();
        }

        void setSelectedIndicatorColor(int n) {
            if (this.selectedIndicatorPaint.getColor() != n) {
                this.selectedIndicatorPaint.setColor(n);
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }

        void setSelectedIndicatorHeight(int n) {
            if (this.selectedIndicatorHeight != n) {
                this.selectedIndicatorHeight = n;
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
        }
    }

    public static class Tab {
        public static final int INVALID_POSITION = -1;
        private CharSequence contentDesc;
        private View customView;
        private Drawable icon;
        public TabLayout parent;
        private int position = -1;
        private Object tag;
        private CharSequence text;
        public TabView view;

        public CharSequence getContentDescription() {
            Object object = this.view;
            object = object == null ? null : object.getContentDescription();
            return object;
        }

        public View getCustomView() {
            return this.customView;
        }

        public Drawable getIcon() {
            return this.icon;
        }

        public int getPosition() {
            return this.position;
        }

        public Object getTag() {
            return this.tag;
        }

        public CharSequence getText() {
            return this.text;
        }

        public boolean isSelected() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                boolean bl = tabLayout.getSelectedTabPosition() == this.position;
                return bl;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        void reset() {
            this.parent = null;
            this.view = null;
            this.tag = null;
            this.icon = null;
            this.text = null;
            this.contentDesc = null;
            this.position = -1;
            this.customView = null;
        }

        public void select() {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                tabLayout.selectTab(this);
                return;
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(int n) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return this.setContentDescription(tabLayout.getResources().getText(n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setContentDescription(CharSequence charSequence) {
            this.contentDesc = charSequence;
            this.updateView();
            return this;
        }

        public Tab setCustomView(int n) {
            return this.setCustomView(LayoutInflater.from((Context)this.view.getContext()).inflate(n, (ViewGroup)this.view, false));
        }

        public Tab setCustomView(View view) {
            this.customView = view;
            this.updateView();
            return this;
        }

        public Tab setIcon(int n) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return this.setIcon(AppCompatResources.getDrawable(tabLayout.getContext(), n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setIcon(Drawable drawable2) {
            this.icon = drawable2;
            this.updateView();
            return this;
        }

        void setPosition(int n) {
            this.position = n;
        }

        public Tab setTag(Object object) {
            this.tag = object;
            return this;
        }

        public Tab setText(int n) {
            TabLayout tabLayout = this.parent;
            if (tabLayout != null) {
                return this.setText(tabLayout.getResources().getText(n));
            }
            throw new IllegalArgumentException("Tab not attached to a TabLayout");
        }

        public Tab setText(CharSequence charSequence) {
            if (TextUtils.isEmpty((CharSequence)this.contentDesc) && !TextUtils.isEmpty((CharSequence)charSequence)) {
                this.view.setContentDescription(charSequence);
            }
            this.text = charSequence;
            this.updateView();
            return this;
        }

        void updateView() {
            TabView tabView = this.view;
            if (tabView != null) {
                tabView.update();
            }
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TabGravity {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface TabIndicatorGravity {
    }

    public static class TabLayoutOnPageChangeListener
    implements ViewPager.OnPageChangeListener {
        private int previousScrollState;
        private int scrollState;
        private final WeakReference<TabLayout> tabLayoutRef;

        public TabLayoutOnPageChangeListener(TabLayout tabLayout) {
            this.tabLayoutRef = new WeakReference<TabLayout>(tabLayout);
        }

        @Override
        public void onPageScrollStateChanged(int n) {
            this.previousScrollState = this.scrollState;
            this.scrollState = n;
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
            TabLayout tabLayout = (TabLayout)((Object)this.tabLayoutRef.get());
            if (tabLayout != null) {
                n2 = this.scrollState;
                boolean bl = false;
                boolean bl2 = n2 != 2 || this.previousScrollState == 1;
                if (n2 != 2 || this.previousScrollState != 0) {
                    bl = true;
                }
                tabLayout.setScrollPosition(n, f, bl2, bl);
            }
        }

        @Override
        public void onPageSelected(int n) {
            TabLayout tabLayout = (TabLayout)((Object)this.tabLayoutRef.get());
            if (tabLayout != null && tabLayout.getSelectedTabPosition() != n && n < tabLayout.getTabCount()) {
                int n2 = this.scrollState;
                boolean bl = n2 == 0 || n2 == 2 && this.previousScrollState == 0;
                tabLayout.selectTab(tabLayout.getTabAt(n), bl);
            }
        }

        void reset() {
            this.scrollState = 0;
            this.previousScrollState = 0;
        }
    }

    class TabView
    extends LinearLayout {
        private Drawable baseBackgroundDrawable;
        private ImageView customIconView;
        private TextView customTextView;
        private View customView;
        private int defaultMaxLines;
        private ImageView iconView;
        private Tab tab;
        private TextView textView;
        final TabLayout this$0;

        public TabView(TabLayout tabLayout, Context context) {
            this.this$0 = tabLayout;
            super(context);
            this.defaultMaxLines = 2;
            this.updateBackgroundDrawable(context);
            ViewCompat.setPaddingRelative((View)this, tabLayout.tabPaddingStart, tabLayout.tabPaddingTop, tabLayout.tabPaddingEnd, tabLayout.tabPaddingBottom);
            this.setGravity(17);
            this.setOrientation(tabLayout.inlineLabel ^ 1);
            this.setClickable(true);
            ViewCompat.setPointerIcon((View)this, PointerIconCompat.getSystemIcon(this.getContext(), 1002));
        }

        private float approximateLineWidth(Layout layout2, int n, float f) {
            return layout2.getLineWidth(n) * (f / layout2.getPaint().getTextSize());
        }

        private void drawBackground(Canvas canvas) {
            Drawable drawable2 = this.baseBackgroundDrawable;
            if (drawable2 != null) {
                drawable2.setBounds(this.getLeft(), this.getTop(), this.getRight(), this.getBottom());
                this.baseBackgroundDrawable.draw(canvas);
            }
        }

        private int getContentWidth() {
            boolean bl = false;
            int n = 0;
            int n2 = 0;
            TextView textView = this.textView;
            ImageView imageView = this.iconView;
            View view = this.customView;
            for (int i = 0; i < 3; ++i) {
                View view2 = (new View[]{textView, imageView, view})[i];
                boolean bl2 = bl;
                int n3 = n;
                int n4 = n2;
                if (view2 != null) {
                    bl2 = bl;
                    n3 = n;
                    n4 = n2;
                    if (view2.getVisibility() == 0) {
                        n4 = n3 = view2.getLeft();
                        if (bl) {
                            n4 = Math.min(n, n3);
                        }
                        n = n4;
                        n4 = n3 = view2.getRight();
                        if (bl) {
                            n4 = Math.max(n2, n3);
                        }
                        bl2 = true;
                        n3 = n;
                    }
                }
                bl = bl2;
                n = n3;
                n2 = n4;
            }
            return n2 - n;
        }

        private void updateBackgroundDrawable(Context context) {
            int n = this.this$0.tabBackgroundResId;
            Drawable drawable2 = null;
            if (n != 0) {
                context = AppCompatResources.getDrawable(context, this.this$0.tabBackgroundResId);
                this.baseBackgroundDrawable = context;
                if (context != null && context.isStateful()) {
                    this.baseBackgroundDrawable.setState(this.getDrawableState());
                }
            } else {
                this.baseBackgroundDrawable = null;
            }
            context = new GradientDrawable();
            ((GradientDrawable)context).setColor(0);
            if (this.this$0.tabRippleColorStateList != null) {
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setCornerRadius(1.0E-5f);
                gradientDrawable.setColor(-1);
                ColorStateList colorStateList = RippleUtils.convertToRippleDrawableColor(this.this$0.tabRippleColorStateList);
                if (Build.VERSION.SDK_INT >= 21) {
                    if (this.this$0.unboundedRipple) {
                        context = null;
                    }
                    if (!this.this$0.unboundedRipple) {
                        drawable2 = gradientDrawable;
                    }
                    context = new RippleDrawable(colorStateList, (Drawable)context, drawable2);
                } else {
                    drawable2 = DrawableCompat.wrap((Drawable)gradientDrawable);
                    DrawableCompat.setTintList(drawable2, colorStateList);
                    context = new LayerDrawable(new Drawable[]{context, drawable2});
                }
            }
            ViewCompat.setBackground((View)this, (Drawable)context);
            this.this$0.invalidate();
        }

        private void updateTextAndIcon(TextView object, ImageView imageView) {
            Tab tab = this.tab;
            Object var8_4 = null;
            tab = tab != null && tab.getIcon() != null ? DrawableCompat.wrap(this.tab.getIcon()).mutate() : null;
            Object object2 = this.tab;
            object2 = object2 != null ? ((Tab)object2).getText() : null;
            if (imageView != null) {
                if (tab != null) {
                    imageView.setImageDrawable((Drawable)tab);
                    imageView.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                    imageView.setImageDrawable(null);
                }
            }
            boolean bl = TextUtils.isEmpty((CharSequence)object2) ^ true;
            if (object != null) {
                if (bl) {
                    object.setText((CharSequence)object2);
                    object.setVisibility(0);
                    this.setVisibility(0);
                } else {
                    object.setVisibility(8);
                    object.setText(null);
                }
            }
            if (imageView != null) {
                int n;
                object = (ViewGroup.MarginLayoutParams)imageView.getLayoutParams();
                int n2 = n = 0;
                if (bl) {
                    n2 = n;
                    if (imageView.getVisibility() == 0) {
                        n2 = this.this$0.dpToPx(8);
                    }
                }
                if (this.this$0.inlineLabel) {
                    if (n2 != MarginLayoutParamsCompat.getMarginEnd((ViewGroup.MarginLayoutParams)object)) {
                        MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)object, n2);
                        object.bottomMargin = 0;
                        imageView.setLayoutParams((ViewGroup.LayoutParams)object);
                        imageView.requestLayout();
                    }
                } else if (n2 != object.bottomMargin) {
                    object.bottomMargin = n2;
                    MarginLayoutParamsCompat.setMarginEnd((ViewGroup.MarginLayoutParams)object, 0);
                    imageView.setLayoutParams((ViewGroup.LayoutParams)object);
                    imageView.requestLayout();
                }
            }
            object = (object = this.tab) != null ? ((Tab)object).contentDesc : null;
            if (bl) {
                object = var8_4;
            }
            TooltipCompat.setTooltipText((View)this, (CharSequence)object);
        }

        protected void drawableStateChanged() {
            super.drawableStateChanged();
            boolean bl = false;
            int[] nArray = this.getDrawableState();
            Drawable drawable2 = this.baseBackgroundDrawable;
            boolean bl2 = bl;
            if (drawable2 != null) {
                bl2 = bl;
                if (drawable2.isStateful()) {
                    bl2 = false | this.baseBackgroundDrawable.setState(nArray);
                }
            }
            if (bl2) {
                this.invalidate();
                this.this$0.invalidate();
            }
        }

        public Tab getTab() {
            return this.tab;
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ActionBar.Tab.class.getName());
        }

        public void onMeasure(int n, int n2) {
            block9: {
                float f;
                int n3;
                int n4;
                block10: {
                    block11: {
                        int n5 = View.MeasureSpec.getSize((int)n);
                        n4 = View.MeasureSpec.getMode((int)n);
                        n3 = this.this$0.getTabMaxWidth();
                        if (n3 > 0 && (n4 == 0 || n5 > n3)) {
                            n = View.MeasureSpec.makeMeasureSpec((int)this.this$0.tabMaxWidth, (int)Integer.MIN_VALUE);
                        }
                        super.onMeasure(n, n2);
                        if (this.textView == null) break block9;
                        float f2 = this.this$0.tabTextSize;
                        n4 = this.defaultMaxLines;
                        ImageView imageView = this.iconView;
                        if (imageView != null && imageView.getVisibility() == 0) {
                            n3 = 1;
                            f = f2;
                        } else {
                            imageView = this.textView;
                            f = f2;
                            n3 = n4;
                            if (imageView != null) {
                                f = f2;
                                n3 = n4;
                                if (imageView.getLineCount() > 1) {
                                    f = this.this$0.tabTextMultiLineSize;
                                    n3 = n4;
                                }
                            }
                        }
                        f2 = this.textView.getTextSize();
                        int n6 = this.textView.getLineCount();
                        n4 = TextViewCompat.getMaxLines(this.textView);
                        if (f == f2 && (n4 < 0 || n3 == n4)) break block9;
                        n4 = n5 = 1;
                        if (this.this$0.mode != 1) break block10;
                        n4 = n5;
                        if (!(f > f2)) break block10;
                        n4 = n5;
                        if (n6 != 1) break block10;
                        imageView = this.textView.getLayout();
                        if (imageView == null) break block11;
                        n4 = n5;
                        if (!(this.approximateLineWidth((Layout)imageView, 0, f) > (float)(this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight()))) break block10;
                    }
                    n4 = 0;
                }
                if (n4 != 0) {
                    this.textView.setTextSize(0, f);
                    this.textView.setMaxLines(n3);
                    super.onMeasure(n, n2);
                }
            }
        }

        public boolean performClick() {
            boolean bl = super.performClick();
            if (this.tab != null) {
                if (!bl) {
                    this.playSoundEffect(0);
                }
                this.tab.select();
                return true;
            }
            return bl;
        }

        void reset() {
            this.setTab(null);
            this.setSelected(false);
        }

        public void setSelected(boolean bl) {
            TextView textView;
            boolean bl2 = this.isSelected() != bl;
            super.setSelected(bl);
            if (bl2 && bl && Build.VERSION.SDK_INT < 16) {
                this.sendAccessibilityEvent(4);
            }
            if ((textView = this.textView) != null) {
                textView.setSelected(bl);
            }
            if ((textView = this.iconView) != null) {
                textView.setSelected(bl);
            }
            if ((textView = this.customView) != null) {
                textView.setSelected(bl);
            }
        }

        void setTab(Tab tab) {
            if (tab != this.tab) {
                this.tab = tab;
                this.update();
            }
        }

        final void update() {
            Tab tab = this.tab;
            Object var4_2 = null;
            Object object = tab != null ? tab.getCustomView() : null;
            if (object != null) {
                ViewParent viewParent = object.getParent();
                if (viewParent != this) {
                    if (viewParent != null) {
                        ((ViewGroup)viewParent).removeView(object);
                    }
                    this.addView((View)object);
                }
                this.customView = object;
                viewParent = this.textView;
                if (viewParent != null) {
                    viewParent.setVisibility(8);
                }
                if ((viewParent = this.iconView) != null) {
                    viewParent.setVisibility(8);
                    this.iconView.setImageDrawable(null);
                }
                viewParent = (TextView)object.findViewById(16908308);
                this.customTextView = viewParent;
                if (viewParent != null) {
                    this.defaultMaxLines = TextViewCompat.getMaxLines((TextView)viewParent);
                }
                this.customIconView = (ImageView)object.findViewById(16908294);
            } else {
                object = this.customView;
                if (object != null) {
                    this.removeView((View)object);
                    this.customView = null;
                }
                this.customTextView = null;
                this.customIconView = null;
            }
            object = this.customView;
            boolean bl = false;
            if (object == null) {
                if (this.iconView == null) {
                    object = (ImageView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_layout_tab_icon, (ViewGroup)this, false);
                    this.addView((View)object, 0);
                    this.iconView = object;
                }
                if ((object = tab != null && tab.getIcon() != null ? DrawableCompat.wrap(tab.getIcon()).mutate() : var4_2) != null) {
                    DrawableCompat.setTintList((Drawable)object, this.this$0.tabIconTint);
                    if (this.this$0.tabIconTintMode != null) {
                        DrawableCompat.setTintMode((Drawable)object, this.this$0.tabIconTintMode);
                    }
                }
                if (this.textView == null) {
                    object = (TextView)LayoutInflater.from((Context)this.getContext()).inflate(R.layout.design_layout_tab_text, (ViewGroup)this, false);
                    this.addView((View)object);
                    this.textView = object;
                    this.defaultMaxLines = TextViewCompat.getMaxLines((TextView)object);
                }
                TextViewCompat.setTextAppearance(this.textView, this.this$0.tabTextAppearance);
                if (this.this$0.tabTextColors != null) {
                    this.textView.setTextColor(this.this$0.tabTextColors);
                }
                this.updateTextAndIcon(this.textView, this.iconView);
            } else {
                object = this.customTextView;
                if (object != null || this.customIconView != null) {
                    this.updateTextAndIcon((TextView)object, this.customIconView);
                }
            }
            if (tab != null && !TextUtils.isEmpty((CharSequence)tab.contentDesc)) {
                this.setContentDescription(tab.contentDesc);
            }
            boolean bl2 = bl;
            if (tab != null) {
                bl2 = bl;
                if (tab.isSelected()) {
                    bl2 = true;
                }
            }
            this.setSelected(bl2);
        }

        final void updateOrientation() {
            this.setOrientation(this.this$0.inlineLabel ^ 1);
            TextView textView = this.customTextView;
            if (textView == null && this.customIconView == null) {
                this.updateTextAndIcon(this.textView, this.iconView);
            } else {
                this.updateTextAndIcon(textView, this.customIconView);
            }
        }
    }

    public static class ViewPagerOnTabSelectedListener
    implements OnTabSelectedListener {
        private final ViewPager viewPager;

        public ViewPagerOnTabSelectedListener(ViewPager viewPager) {
            this.viewPager = viewPager;
        }

        @Override
        public void onTabReselected(Tab tab) {
        }

        @Override
        public void onTabSelected(Tab tab) {
            this.viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(Tab tab) {
        }
    }
}

