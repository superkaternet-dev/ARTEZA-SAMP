/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.graphics.drawable.Drawable
 *  android.text.TextUtils
 *  android.text.TextUtils$TruncateAt
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewPropertyAnimator
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.accessibility.AccessibilityNodeInfo
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  android.widget.AbsListView$LayoutParams
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.BaseAdapter
 *  android.widget.HorizontalScrollView
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.Spinner
 *  android.widget.SpinnerAdapter
 *  android.widget.TextView
 */
package androidx.appcompat.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import androidx.appcompat.R;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.view.ActionBarPolicy;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.TintTypedArray;
import androidx.appcompat.widget.TooltipCompat;

public class ScrollingTabContainerView
extends HorizontalScrollView
implements AdapterView.OnItemSelectedListener {
    private static final int FADE_DURATION = 200;
    private static final String TAG = "ScrollingTabContainerView";
    private static final Interpolator sAlphaInterpolator = new DecelerateInterpolator();
    private boolean mAllowCollapse;
    private int mContentHeight;
    int mMaxTabWidth;
    private int mSelectedTabIndex;
    int mStackedTabMaxWidth;
    private TabClickListener mTabClickListener;
    LinearLayoutCompat mTabLayout;
    Runnable mTabSelector;
    private Spinner mTabSpinner;
    protected final VisibilityAnimListener mVisAnimListener = new VisibilityAnimListener(this);
    protected ViewPropertyAnimator mVisibilityAnim;

    public ScrollingTabContainerView(Context object) {
        super(object);
        this.setHorizontalScrollBarEnabled(false);
        object = ActionBarPolicy.get(object);
        this.setContentHeight(object.getTabContainerHeight());
        this.mStackedTabMaxWidth = object.getStackedTabMaxWidth();
        object = this.createTabLayout();
        this.mTabLayout = object;
        this.addView((View)object, new ViewGroup.LayoutParams(-2, -1));
    }

    private Spinner createSpinner() {
        AppCompatSpinner appCompatSpinner = new AppCompatSpinner(this.getContext(), null, R.attr.actionDropDownStyle);
        appCompatSpinner.setLayoutParams((ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
        appCompatSpinner.setOnItemSelectedListener(this);
        return appCompatSpinner;
    }

    private LinearLayoutCompat createTabLayout() {
        LinearLayoutCompat linearLayoutCompat = new LinearLayoutCompat(this.getContext(), null, R.attr.actionBarTabBarStyle);
        linearLayoutCompat.setMeasureWithLargestChildEnabled(true);
        linearLayoutCompat.setGravity(17);
        linearLayoutCompat.setLayoutParams((ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(-2, -1));
        return linearLayoutCompat;
    }

    private boolean isCollapsed() {
        Spinner spinner = this.mTabSpinner;
        boolean bl = spinner != null && spinner.getParent() == this;
        return bl;
    }

    private void performCollapse() {
        Runnable runnable;
        if (this.isCollapsed()) {
            return;
        }
        if (this.mTabSpinner == null) {
            this.mTabSpinner = this.createSpinner();
        }
        this.removeView((View)this.mTabLayout);
        this.addView((View)this.mTabSpinner, new ViewGroup.LayoutParams(-2, -1));
        if (this.mTabSpinner.getAdapter() == null) {
            this.mTabSpinner.setAdapter((SpinnerAdapter)new TabAdapter(this));
        }
        if ((runnable = this.mTabSelector) != null) {
            this.removeCallbacks(runnable);
            this.mTabSelector = null;
        }
        this.mTabSpinner.setSelection(this.mSelectedTabIndex);
    }

    private boolean performExpand() {
        if (!this.isCollapsed()) {
            return false;
        }
        this.removeView((View)this.mTabSpinner);
        this.addView((View)this.mTabLayout, new ViewGroup.LayoutParams(-2, -1));
        this.setTabSelected(this.mTabSpinner.getSelectedItemPosition());
        return false;
    }

    public void addTab(ActionBar.Tab tab, int n, boolean bl) {
        TabView tabView = this.createTabView(tab, false);
        this.mTabLayout.addView((View)tabView, n, (ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        tab = this.mTabSpinner;
        if (tab != null) {
            ((TabAdapter)tab.getAdapter()).notifyDataSetChanged();
        }
        if (bl) {
            tabView.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    public void addTab(ActionBar.Tab tab, boolean bl) {
        TabView tabView = this.createTabView(tab, false);
        this.mTabLayout.addView((View)tabView, (ViewGroup.LayoutParams)new LinearLayoutCompat.LayoutParams(0, -1, 1.0f));
        tab = this.mTabSpinner;
        if (tab != null) {
            ((TabAdapter)tab.getAdapter()).notifyDataSetChanged();
        }
        if (bl) {
            tabView.setSelected(true);
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    public void animateToTab(int n) {
        View view = this.mTabLayout.getChildAt(n);
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            this.removeCallbacks(runnable);
        }
        this.mTabSelector = runnable = new Runnable(this, view){
            final ScrollingTabContainerView this$0;
            final View val$tabView;
            {
                this.this$0 = scrollingTabContainerView;
                this.val$tabView = view;
            }

            @Override
            public void run() {
                int n = this.val$tabView.getLeft();
                int n2 = (this.this$0.getWidth() - this.val$tabView.getWidth()) / 2;
                this.this$0.smoothScrollTo(n - n2, 0);
                this.this$0.mTabSelector = null;
            }
        };
        this.post(runnable);
    }

    public void animateToVisibility(int n) {
        ViewPropertyAnimator viewPropertyAnimator = this.mVisibilityAnim;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        if (n == 0) {
            if (this.getVisibility() != 0) {
                this.setAlpha(0.0f);
            }
            viewPropertyAnimator = this.animate().alpha(1.0f);
            viewPropertyAnimator.setDuration(200L);
            viewPropertyAnimator.setInterpolator((TimeInterpolator)sAlphaInterpolator);
            viewPropertyAnimator.setListener((Animator.AnimatorListener)this.mVisAnimListener.withFinalVisibility(viewPropertyAnimator, n));
            viewPropertyAnimator.start();
        } else {
            viewPropertyAnimator = this.animate().alpha(0.0f);
            viewPropertyAnimator.setDuration(200L);
            viewPropertyAnimator.setInterpolator((TimeInterpolator)sAlphaInterpolator);
            viewPropertyAnimator.setListener((Animator.AnimatorListener)this.mVisAnimListener.withFinalVisibility(viewPropertyAnimator, n));
            viewPropertyAnimator.start();
        }
    }

    TabView createTabView(ActionBar.Tab object, boolean bl) {
        object = new TabView(this, this.getContext(), (ActionBar.Tab)object, bl);
        if (bl) {
            object.setBackgroundDrawable(null);
            object.setLayoutParams((ViewGroup.LayoutParams)new AbsListView.LayoutParams(-1, this.mContentHeight));
        } else {
            object.setFocusable(true);
            if (this.mTabClickListener == null) {
                this.mTabClickListener = new TabClickListener(this);
            }
            object.setOnClickListener(this.mTabClickListener);
        }
        return object;
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            this.post(runnable);
        }
    }

    protected void onConfigurationChanged(Configuration object) {
        super.onConfigurationChanged((Configuration)object);
        object = ActionBarPolicy.get(this.getContext());
        this.setContentHeight(((ActionBarPolicy)object).getTabContainerHeight());
        this.mStackedTabMaxWidth = ((ActionBarPolicy)object).getStackedTabMaxWidth();
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Runnable runnable = this.mTabSelector;
        if (runnable != null) {
            this.removeCallbacks(runnable);
        }
    }

    public void onItemSelected(AdapterView<?> adapterView, View view, int n, long l) {
        ((TabView)view).getTab().select();
    }

    public void onMeasure(int n, int n2) {
        int n3 = View.MeasureSpec.getMode((int)n);
        n2 = 1;
        boolean bl = n3 == 0x40000000;
        this.setFillViewport(bl);
        int n4 = this.mTabLayout.getChildCount();
        if (n4 > 1 && (n3 == 0x40000000 || n3 == Integer.MIN_VALUE)) {
            this.mMaxTabWidth = n4 > 2 ? (int)((float)View.MeasureSpec.getSize((int)n) * 0.4f) : View.MeasureSpec.getSize((int)n) / 2;
            this.mMaxTabWidth = Math.min(this.mMaxTabWidth, this.mStackedTabMaxWidth);
        } else {
            this.mMaxTabWidth = -1;
        }
        n4 = View.MeasureSpec.makeMeasureSpec((int)this.mContentHeight, (int)0x40000000);
        if (bl || !this.mAllowCollapse) {
            n2 = 0;
        }
        if (n2 != 0) {
            this.mTabLayout.measure(0, n4);
            if (this.mTabLayout.getMeasuredWidth() > View.MeasureSpec.getSize((int)n)) {
                this.performCollapse();
            } else {
                this.performExpand();
            }
        } else {
            this.performExpand();
        }
        n2 = this.getMeasuredWidth();
        super.onMeasure(n, n4);
        n = this.getMeasuredWidth();
        if (bl && n2 != n) {
            this.setTabSelected(this.mSelectedTabIndex);
        }
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    public void removeAllTabs() {
        this.mTabLayout.removeAllViews();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter)spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    public void removeTabAt(int n) {
        this.mTabLayout.removeViewAt(n);
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter)spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    public void setAllowCollapse(boolean bl) {
        this.mAllowCollapse = bl;
    }

    public void setContentHeight(int n) {
        this.mContentHeight = n;
        this.requestLayout();
    }

    public void setTabSelected(int n) {
        Spinner spinner;
        this.mSelectedTabIndex = n;
        int n2 = this.mTabLayout.getChildCount();
        for (int i = 0; i < n2; ++i) {
            spinner = this.mTabLayout.getChildAt(i);
            boolean bl = i == n;
            spinner.setSelected(bl);
            if (!bl) continue;
            this.animateToTab(n);
        }
        spinner = this.mTabSpinner;
        if (spinner != null && n >= 0) {
            spinner.setSelection(n);
        }
    }

    public void updateTab(int n) {
        ((TabView)this.mTabLayout.getChildAt(n)).update();
        Spinner spinner = this.mTabSpinner;
        if (spinner != null) {
            ((TabAdapter)spinner.getAdapter()).notifyDataSetChanged();
        }
        if (this.mAllowCollapse) {
            this.requestLayout();
        }
    }

    private class TabAdapter
    extends BaseAdapter {
        final ScrollingTabContainerView this$0;

        TabAdapter(ScrollingTabContainerView scrollingTabContainerView) {
            this.this$0 = scrollingTabContainerView;
        }

        public int getCount() {
            return this.this$0.mTabLayout.getChildCount();
        }

        public Object getItem(int n) {
            return ((TabView)this.this$0.mTabLayout.getChildAt(n)).getTab();
        }

        public long getItemId(int n) {
            return n;
        }

        public View getView(int n, View object, ViewGroup viewGroup) {
            if (object == null) {
                object = this.this$0.createTabView((ActionBar.Tab)this.getItem(n), true);
            } else {
                ((TabView)((Object)object)).bindTab((ActionBar.Tab)this.getItem(n));
            }
            return object;
        }
    }

    private class TabClickListener
    implements View.OnClickListener {
        final ScrollingTabContainerView this$0;

        TabClickListener(ScrollingTabContainerView scrollingTabContainerView) {
            this.this$0 = scrollingTabContainerView;
        }

        public void onClick(View view) {
            ((TabView)view).getTab().select();
            int n = this.this$0.mTabLayout.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view2 = this.this$0.mTabLayout.getChildAt(i);
                boolean bl = view2 == view;
                view2.setSelected(bl);
            }
        }
    }

    private class TabView
    extends LinearLayout {
        private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.app.ActionBar$Tab";
        private final int[] BG_ATTRS;
        private View mCustomView;
        private ImageView mIconView;
        private ActionBar.Tab mTab;
        private TextView mTextView;
        final ScrollingTabContainerView this$0;

        public TabView(ScrollingTabContainerView object, Context context, ActionBar.Tab tab, boolean bl) {
            this.this$0 = object;
            super(context, null, R.attr.actionBarTabStyle);
            object = new int[1];
            object[0] = (ScrollingTabContainerView)16842964;
            this.BG_ATTRS = (int[])object;
            this.mTab = tab;
            object = TintTypedArray.obtainStyledAttributes(context, null, (int[])object, R.attr.actionBarTabStyle, 0);
            if (((TintTypedArray)object).hasValue(0)) {
                this.setBackgroundDrawable(((TintTypedArray)object).getDrawable(0));
            }
            ((TintTypedArray)object).recycle();
            if (bl) {
                this.setGravity(8388627);
            }
            this.update();
        }

        public void bindTab(ActionBar.Tab tab) {
            this.mTab = tab;
            this.update();
        }

        public ActionBar.Tab getTab() {
            return this.mTab;
        }

        public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)ACCESSIBILITY_CLASS_NAME);
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
            accessibilityNodeInfo.setClassName((CharSequence)ACCESSIBILITY_CLASS_NAME);
        }

        public void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            if (this.this$0.mMaxTabWidth > 0 && this.getMeasuredWidth() > this.this$0.mMaxTabWidth) {
                super.onMeasure(View.MeasureSpec.makeMeasureSpec((int)this.this$0.mMaxTabWidth, (int)0x40000000), n2);
            }
        }

        public void setSelected(boolean bl) {
            boolean bl2 = this.isSelected() != bl;
            super.setSelected(bl);
            if (bl2 && bl) {
                this.sendAccessibilityEvent(4);
            }
        }

        public void update() {
            ActionBar.Tab tab = this.mTab;
            Object object = tab.getCustomView();
            Object object2 = null;
            if (object != null) {
                object2 = object.getParent();
                if (object2 != this) {
                    if (object2 != null) {
                        ((ViewGroup)object2).removeView(object);
                    }
                    this.addView((View)object);
                }
                this.mCustomView = object;
                object2 = this.mTextView;
                if (object2 != null) {
                    object2.setVisibility(8);
                }
                if ((object2 = this.mIconView) != null) {
                    object2.setVisibility(8);
                    this.mIconView.setImageDrawable(null);
                }
            } else {
                Object object3;
                object = this.mCustomView;
                if (object != null) {
                    this.removeView((View)object);
                    this.mCustomView = null;
                }
                Drawable drawable2 = tab.getIcon();
                object = tab.getText();
                if (drawable2 != null) {
                    if (this.mIconView == null) {
                        object3 = new AppCompatImageView(this.getContext());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-2, -2);
                        layoutParams.gravity = 16;
                        object3.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
                        this.addView((View)object3, 0);
                        this.mIconView = object3;
                    }
                    this.mIconView.setImageDrawable(drawable2);
                    this.mIconView.setVisibility(0);
                } else {
                    object3 = this.mIconView;
                    if (object3 != null) {
                        object3.setVisibility(8);
                        this.mIconView.setImageDrawable(null);
                    }
                }
                boolean bl = TextUtils.isEmpty((CharSequence)object) ^ true;
                if (bl) {
                    if (this.mTextView == null) {
                        object3 = new AppCompatTextView(this.getContext(), null, R.attr.actionBarTabTextStyle);
                        object3.setEllipsize(TextUtils.TruncateAt.END);
                        drawable2 = new LinearLayout.LayoutParams(-2, -2);
                        drawable2.gravity = 16;
                        object3.setLayoutParams((ViewGroup.LayoutParams)drawable2);
                        this.addView((View)object3);
                        this.mTextView = object3;
                    }
                    this.mTextView.setText((CharSequence)object);
                    this.mTextView.setVisibility(0);
                } else {
                    object = this.mTextView;
                    if (object != null) {
                        object.setVisibility(8);
                        this.mTextView.setText(null);
                    }
                }
                object = this.mIconView;
                if (object != null) {
                    object.setContentDescription(tab.getContentDescription());
                }
                if (!bl) {
                    object2 = tab.getContentDescription();
                }
                TooltipCompat.setTooltipText((View)this, (CharSequence)object2);
            }
        }
    }

    protected class VisibilityAnimListener
    extends AnimatorListenerAdapter {
        private boolean mCanceled;
        private int mFinalVisibility;
        final ScrollingTabContainerView this$0;

        protected VisibilityAnimListener(ScrollingTabContainerView scrollingTabContainerView) {
            this.this$0 = scrollingTabContainerView;
            this.mCanceled = false;
        }

        public void onAnimationCancel(Animator animator2) {
            this.mCanceled = true;
        }

        public void onAnimationEnd(Animator animator2) {
            if (this.mCanceled) {
                return;
            }
            this.this$0.mVisibilityAnim = null;
            this.this$0.setVisibility(this.mFinalVisibility);
        }

        public void onAnimationStart(Animator animator2) {
            this.this$0.setVisibility(0);
            this.mCanceled = false;
        }

        public VisibilityAnimListener withFinalVisibility(ViewPropertyAnimator viewPropertyAnimator, int n) {
            this.mFinalVisibility = n;
            this.this$0.mVisibilityAnim = viewPropertyAnimator;
            return this;
        }
    }
}

