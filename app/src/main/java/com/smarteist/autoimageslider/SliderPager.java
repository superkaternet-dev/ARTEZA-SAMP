/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources$NotFoundException
 *  android.database.DataSetObserver
 *  android.graphics.Canvas
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.FocusFinder
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.SoundEffectConstants
 *  android.view.VelocityTracker
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.ViewConfiguration
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityEvent
 *  android.view.animation.Interpolator
 *  android.widget.EdgeEffect
 *  android.widget.Scroller
 */
package com.smarteist.autoimageslider;

import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.EdgeEffect;
import android.widget.Scroller;
import androidx.core.content.ContextCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.viewpager.widget.PagerAdapter;
import com.smarteist.autoimageslider.InfiniteAdapter.InfinitePagerAdapter;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SliderPager
extends ViewGroup {
    private static final int CLOSE_ENOUGH = 2;
    private static final Comparator<ItemInfo> COMPARATOR;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_GUTTER_SIZE = 16;
    private static final int DEFAULT_OFFSCREEN_PAGES = 1;
    public static final int DEFAULT_SCROLL_DURATION = 250;
    private static final int DRAW_ORDER_DEFAULT = 0;
    private static final int DRAW_ORDER_FORWARD = 1;
    private static final int DRAW_ORDER_REVERSE = 2;
    private static final int INVALID_POINTER = -1;
    static final int[] LAYOUT_ATTRS;
    private static final int MAX_SETTLE_DURATION = 600;
    private static final int MIN_DISTANCE_FOR_FLING = 25;
    private static final int MIN_FLING_VELOCITY = 400;
    public static final int SCROLL_STATE_DRAGGING = 1;
    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLL_STATE_SETTLING = 2;
    private static final String TAG = "SliderPager";
    private static final boolean USE_CACHE = false;
    private static final Interpolator sInterpolator;
    private static final ViewPositionComparator sPositionComparator;
    private int mActivePointerId = -1;
    PagerAdapter mAdapter;
    private List<OnAdapterChangeListener> mAdapterChangeListeners;
    private int mBottomPageBounds;
    private boolean mCalledSuper;
    private int mChildHeightMeasureSpec;
    private int mChildWidthMeasureSpec;
    private int mCloseEnough;
    int mCurItem;
    private int mDecorChildCount;
    private int mDefaultGutterSize;
    private int mDrawingOrder;
    private ArrayList<View> mDrawingOrderedChildren;
    private final Runnable mEndScrollRunnable;
    private int mExpectedAdapterCount;
    private long mFakeDragBeginTime;
    private boolean mFakeDragging;
    private boolean mFirstLayout = true;
    private float mFirstOffset = -3.4028235E38f;
    private int mFlingDistance;
    private int mGutterSize;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private OnPageChangeListener mInternalPageChangeListener;
    private boolean mIsBeingDragged;
    private boolean mIsScrollStarted;
    private boolean mIsUnableToDrag;
    private final ArrayList<ItemInfo> mItems = new ArrayList();
    private float mLastMotionX;
    private float mLastMotionY;
    private float mLastOffset;
    private EdgeEffect mLeftEdge;
    private Drawable mMarginDrawable;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    private boolean mNeedCalculatePageOffsets = false;
    private PagerObserver mObserver;
    private int mOffscreenPageLimit = 1;
    private OnPageChangeListener mOnPageChangeListener;
    private List<OnPageChangeListener> mOnPageChangeListeners;
    private int mPageMargin;
    private PageTransformer mPageTransformer;
    private int mPageTransformerLayerType;
    private boolean mPopulatePending;
    private Parcelable mRestoredAdapterState = null;
    private ClassLoader mRestoredClassLoader = null;
    private int mRestoredCurItem = -1;
    private EdgeEffect mRightEdge;
    private int mScrollState = 0;
    private Scroller mScroller;
    private boolean mScrollingCacheEnabled;
    private final ItemInfo mTempItem = new ItemInfo();
    private final Rect mTempRect = new Rect();
    private int mTopPageBounds;
    private int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    static {
        LAYOUT_ATTRS = new int[]{16842931};
        COMPARATOR = new Comparator<ItemInfo>(){

            @Override
            public int compare(ItemInfo itemInfo, ItemInfo itemInfo2) {
                return itemInfo.position - itemInfo2.position;
            }
        };
        sInterpolator = new Interpolator(){

            public float getInterpolation(float f) {
                return (f -= 1.0f) * f * f * f * f + 1.0f;
            }
        };
        sPositionComparator = new ViewPositionComparator();
    }

    public SliderPager(Context context) {
        super(context);
        this.mLastOffset = Float.MAX_VALUE;
        this.mEndScrollRunnable = new Runnable(this){
            final SliderPager this$0;
            {
                this.this$0 = sliderPager;
            }

            @Override
            public void run() {
                this.this$0.setScrollState(0);
                this.this$0.populate();
            }
        };
        this.initSliderPager();
    }

    public SliderPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLastOffset = Float.MAX_VALUE;
        this.mEndScrollRunnable = new /* invalid duplicate definition of identical inner class */;
        this.initSliderPager();
    }

    private void calculatePageOffsets(ItemInfo itemInfo, int n, ItemInfo itemInfo2) {
        int n2;
        float f;
        float f2;
        int n3;
        int n4 = this.mAdapter.getCount();
        int n5 = this.getClientWidth();
        float f3 = n5 > 0 ? (float)this.mPageMargin / (float)n5 : 0.0f;
        if (itemInfo2 != null) {
            n5 = itemInfo2.position;
            if (n5 < itemInfo.position) {
                n3 = 0;
                f2 = itemInfo2.offset + itemInfo2.widthFactor + f3;
                ++n5;
                while (n5 <= itemInfo.position && n3 < this.mItems.size()) {
                    itemInfo2 = this.mItems.get(n3);
                    while (true) {
                        f = f2;
                        n2 = n5;
                        if (n5 <= itemInfo2.position) break;
                        f = f2;
                        n2 = n5;
                        if (n3 >= this.mItems.size() - 1) break;
                        itemInfo2 = this.mItems.get(++n3);
                    }
                    while (n2 < itemInfo2.position) {
                        f += this.mAdapter.getPageWidth(n2) + f3;
                        ++n2;
                    }
                    itemInfo2.offset = f;
                    f2 = f + (itemInfo2.widthFactor + f3);
                    n5 = n2 + 1;
                }
            } else if (n5 > itemInfo.position) {
                n3 = this.mItems.size() - 1;
                f2 = itemInfo2.offset;
                --n5;
                while (n5 >= itemInfo.position && n3 >= 0) {
                    itemInfo2 = this.mItems.get(n3);
                    while (true) {
                        f = f2;
                        n2 = n5;
                        if (n5 >= itemInfo2.position) break;
                        f = f2;
                        n2 = n5;
                        if (n3 <= 0) break;
                        itemInfo2 = this.mItems.get(--n3);
                    }
                    while (n2 > itemInfo2.position) {
                        f -= this.mAdapter.getPageWidth(n2) + f3;
                        --n2;
                    }
                    itemInfo2.offset = f2 = f - (itemInfo2.widthFactor + f3);
                    n5 = n2 - 1;
                }
            }
        }
        n2 = this.mItems.size();
        f = itemInfo.offset;
        n5 = itemInfo.position - 1;
        f2 = itemInfo.position == 0 ? itemInfo.offset : -3.4028235E38f;
        this.mFirstOffset = f2;
        f2 = itemInfo.position == n4 - 1 ? itemInfo.offset + itemInfo.widthFactor - 1.0f : Float.MAX_VALUE;
        this.mLastOffset = f2;
        n3 = n - 1;
        f2 = f;
        while (n3 >= 0) {
            itemInfo2 = this.mItems.get(n3);
            while (n5 > itemInfo2.position) {
                f2 -= this.mAdapter.getPageWidth(n5) + f3;
                --n5;
            }
            itemInfo2.offset = f2 -= itemInfo2.widthFactor + f3;
            if (itemInfo2.position == 0) {
                this.mFirstOffset = f2;
            }
            --n3;
            --n5;
        }
        f2 = itemInfo.offset + itemInfo.widthFactor + f3;
        n3 = itemInfo.position + 1;
        n5 = n + 1;
        n = n3;
        while (n5 < n2) {
            itemInfo = this.mItems.get(n5);
            while (n < itemInfo.position) {
                f2 += this.mAdapter.getPageWidth(n) + f3;
                ++n;
            }
            if (itemInfo.position == n4 - 1) {
                this.mLastOffset = itemInfo.widthFactor + f2 - 1.0f;
            }
            itemInfo.offset = f2;
            f2 += itemInfo.widthFactor + f3;
            ++n5;
            ++n;
        }
        this.mNeedCalculatePageOffsets = false;
    }

    private void completeScroll(boolean bl) {
        int n;
        boolean bl2 = this.mScrollState == 2;
        if (bl2) {
            this.setScrollingCacheEnabled(false);
            if (true ^ this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                int n2 = this.getScrollX();
                n = this.getScrollY();
                int n3 = this.mScroller.getCurrX();
                int n4 = this.mScroller.getCurrY();
                if (n2 != n3 || n != n4) {
                    this.scrollTo(n3, n4);
                    if (n3 != n2) {
                        this.pageScrolled(n3);
                    }
                }
            }
        }
        this.mPopulatePending = false;
        for (n = 0; n < this.mItems.size(); ++n) {
            ItemInfo itemInfo = this.mItems.get(n);
            if (!itemInfo.scrolling) continue;
            bl2 = true;
            itemInfo.scrolling = false;
        }
        if (bl2) {
            if (bl) {
                ViewCompat.postOnAnimation((View)this, this.mEndScrollRunnable);
            } else {
                this.mEndScrollRunnable.run();
            }
        }
    }

    private int determineTargetPage(int n, float f, int n2, int n3) {
        if (Math.abs(n3) > this.mFlingDistance && Math.abs(n2) > this.mMinimumVelocity) {
            if (n2 <= 0) {
                ++n;
            }
        } else {
            float f2 = n >= this.mCurItem ? 0.4f : 0.6f;
            n = (int)(f + f2) + n;
        }
        n2 = n;
        if (this.mItems.size() > 0) {
            ItemInfo itemInfo = this.mItems.get(0);
            Object object = this.mItems;
            object = ((ArrayList)object).get(((ArrayList)object).size() - 1);
            n2 = Math.max(itemInfo.position, Math.min(n, ((ItemInfo)object).position));
        }
        return n2;
    }

    private void dispatchOnPageScrolled(int n, float f, int n2) {
        Object object = this.mOnPageChangeListener;
        if (object != null) {
            object.onPageScrolled(n, f, n2);
        }
        if ((object = this.mOnPageChangeListeners) != null) {
            int n3 = object.size();
            for (int i = 0; i < n3; ++i) {
                object = this.mOnPageChangeListeners.get(i);
                if (object == null) continue;
                object.onPageScrolled(n, f, n2);
            }
        }
        if ((object = this.mInternalPageChangeListener) != null) {
            object.onPageScrolled(n, f, n2);
        }
    }

    private void dispatchOnPageSelected(int n) {
        Object object = this.mOnPageChangeListener;
        if (object != null) {
            object.onPageSelected(n);
        }
        if ((object = this.mOnPageChangeListeners) != null) {
            int n2 = object.size();
            for (int i = 0; i < n2; ++i) {
                object = this.mOnPageChangeListeners.get(i);
                if (object == null) continue;
                object.onPageSelected(n);
            }
        }
        if ((object = this.mInternalPageChangeListener) != null) {
            object.onPageSelected(n);
        }
    }

    private void dispatchOnScrollStateChanged(int n) {
        Object object = this.mOnPageChangeListener;
        if (object != null) {
            object.onPageScrollStateChanged(n);
        }
        if ((object = this.mOnPageChangeListeners) != null) {
            int n2 = object.size();
            for (int i = 0; i < n2; ++i) {
                object = this.mOnPageChangeListeners.get(i);
                if (object == null) continue;
                object.onPageScrollStateChanged(n);
            }
        }
        if ((object = this.mInternalPageChangeListener) != null) {
            object.onPageScrollStateChanged(n);
        }
    }

    private void enableLayers(boolean bl) {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            int n2 = bl ? this.mPageTransformerLayerType : 0;
            this.getChildAt(i).setLayerType(n2, null);
        }
    }

    private void endDrag() {
        this.mIsBeingDragged = false;
        this.mIsUnableToDrag = false;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    private Rect getChildRectInPagerCoordinates(Rect rect, View view) {
        Rect rect2 = rect;
        if (rect == null) {
            rect2 = new Rect();
        }
        if (view == null) {
            rect2.set(0, 0, 0, 0);
            return rect2;
        }
        rect2.left = view.getLeft();
        rect2.right = view.getRight();
        rect2.top = view.getTop();
        rect2.bottom = view.getBottom();
        for (rect = view.getParent(); rect instanceof ViewGroup && rect != this; rect = rect.getParent()) {
            rect = (ViewGroup)rect;
            rect2.left += rect.getLeft();
            rect2.right += rect.getRight();
            rect2.top += rect.getTop();
            rect2.bottom += rect.getBottom();
        }
        return rect2;
    }

    private int getClientWidth() {
        return this.getMeasuredWidth() - this.getPaddingLeft() - this.getPaddingRight();
    }

    private ItemInfo infoForCurrentScrollPosition() {
        int n = this.getClientWidth();
        float f = 0.0f;
        float f2 = n > 0 ? (float)this.getScrollX() / (float)n : 0.0f;
        if (n > 0) {
            f = (float)this.mPageMargin / (float)n;
        }
        int n2 = -1;
        float f3 = 0.0f;
        float f4 = 0.0f;
        boolean bl = true;
        ItemInfo itemInfo = null;
        n = 0;
        while (n < this.mItems.size()) {
            ItemInfo itemInfo2 = this.mItems.get(n);
            int n3 = n;
            ItemInfo itemInfo3 = itemInfo2;
            if (!bl) {
                n3 = n;
                itemInfo3 = itemInfo2;
                if (itemInfo2.position != n2 + 1) {
                    itemInfo3 = this.mTempItem;
                    itemInfo3.offset = f3 + f4 + f;
                    itemInfo3.position = n2 + 1;
                    itemInfo3.widthFactor = this.mAdapter.getPageWidth(itemInfo3.position);
                    n3 = n - 1;
                }
            }
            f3 = itemInfo3.offset;
            f4 = itemInfo3.widthFactor;
            if (!bl && !(f2 >= f3)) {
                return itemInfo;
            }
            if (!(f2 < f4 + f3 + f) && n3 != this.mItems.size() - 1) {
                bl = false;
                n2 = itemInfo3.position;
                f4 = itemInfo3.widthFactor;
                n = n3 + 1;
                itemInfo = itemInfo3;
                continue;
            }
            return itemInfo3;
        }
        return itemInfo;
    }

    private static boolean isDecorView(View view) {
        boolean bl = view.getClass().getAnnotation(DecorView.class) != null;
        return bl;
    }

    private boolean isGutterDrag(float f, float f2) {
        boolean bl = f < (float)this.mGutterSize && f2 > 0.0f || f > (float)(this.getWidth() - this.mGutterSize) && f2 < 0.0f;
        return bl;
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int n = motionEvent.getActionIndex();
        if (motionEvent.getPointerId(n) == this.mActivePointerId) {
            n = n == 0 ? 1 : 0;
            this.mLastMotionX = motionEvent.getX(n);
            this.mActivePointerId = motionEvent.getPointerId(n);
            motionEvent = this.mVelocityTracker;
            if (motionEvent != null) {
                motionEvent.clear();
            }
        }
    }

    private boolean pageScrolled(int n) {
        if (this.mItems.size() == 0) {
            if (this.mFirstLayout) {
                return false;
            }
            this.mCalledSuper = false;
            this.onPageScrolled(0, 0.0f, 0);
            if (this.mCalledSuper) {
                return false;
            }
            throw new IllegalStateException("onPageScrolled did not call superclass implementation");
        }
        ItemInfo itemInfo = this.infoForCurrentScrollPosition();
        int n2 = this.getClientWidth();
        int n3 = this.mPageMargin;
        float f = (float)n3 / (float)n2;
        int n4 = itemInfo.position;
        f = ((float)n / (float)n2 - itemInfo.offset) / (itemInfo.widthFactor + f);
        n = (int)((float)(n2 + n3) * f);
        this.mCalledSuper = false;
        this.onPageScrolled(n4, f, n);
        if (this.mCalledSuper) {
            return true;
        }
        throw new IllegalStateException("onPageScrolled did not call superclass implementation");
    }

    private boolean performDrag(float f) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        float f2 = this.mLastMotionX;
        this.mLastMotionX = f;
        float f3 = (float)this.getScrollX() + (f2 - f);
        int n = this.getClientWidth();
        f = (float)n * this.mFirstOffset;
        f2 = (float)n * this.mLastOffset;
        boolean bl4 = true;
        boolean bl5 = true;
        ItemInfo itemInfo = this.mItems.get(0);
        Object object = this.mItems;
        object = ((ArrayList)object).get(((ArrayList)object).size() - 1);
        if (itemInfo.position != 0) {
            bl4 = false;
            f = itemInfo.offset * (float)n;
        }
        if (((ItemInfo)object).position != this.mAdapter.getCount() - 1) {
            bl5 = false;
            f2 = ((ItemInfo)object).offset * (float)n;
        }
        if (f3 < f) {
            if (bl4) {
                this.mLeftEdge.onPull(Math.abs(f - f3) / (float)n);
                bl3 = true;
            }
        } else {
            bl3 = bl2;
            f = f3;
            if (f3 > f2) {
                bl3 = bl;
                if (bl5) {
                    this.mRightEdge.onPull(Math.abs(f3 - f2) / (float)n);
                    bl3 = true;
                }
                f = f2;
            }
        }
        this.mLastMotionX += f - (float)((int)f);
        this.scrollTo((int)f, this.getScrollY());
        this.pageScrolled((int)f);
        return bl3;
    }

    private void recomputeScrollPosition(int n, int n2, int n3, int n4) {
        if (n2 > 0 && !this.mItems.isEmpty()) {
            if (!this.mScroller.isFinished()) {
                this.mScroller.setFinalX(this.getCurrentItem() * this.getClientWidth());
            } else {
                int n5 = this.getPaddingLeft();
                int n6 = this.getPaddingRight();
                int n7 = this.getPaddingLeft();
                int n8 = this.getPaddingRight();
                float f = (float)this.getScrollX() / (float)(n2 - n7 - n8 + n4);
                this.scrollTo((int)((float)(n - n5 - n6 + n3) * f), this.getScrollY());
            }
        } else {
            ItemInfo itemInfo = this.infoForPosition(this.mCurItem);
            float f = itemInfo != null ? Math.min(itemInfo.offset, this.mLastOffset) : 0.0f;
            if ((n = (int)((float)(n - this.getPaddingLeft() - this.getPaddingRight()) * f)) != this.getScrollX()) {
                this.completeScroll(false);
                this.scrollTo(n, this.getScrollY());
            }
        }
    }

    private void removeNonDecorViews() {
        int n = 0;
        while (n < this.getChildCount()) {
            int n2 = n;
            if (!((LayoutParams)this.getChildAt((int)n).getLayoutParams()).isDecor) {
                this.removeViewAt(n);
                n2 = n - 1;
            }
            n = n2 + 1;
        }
    }

    private void requestParentDisallowInterceptTouchEvent(boolean bl) {
        ViewParent viewParent = this.getParent();
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(bl);
        }
    }

    private boolean resetTouch() {
        this.mActivePointerId = -1;
        this.endDrag();
        this.mLeftEdge.onRelease();
        this.mRightEdge.onRelease();
        boolean bl = this.mLeftEdge.isFinished() || this.mRightEdge.isFinished();
        return bl;
    }

    private void scrollToItem(int n, boolean bl, int n2, boolean bl2) {
        ItemInfo itemInfo = this.infoForPosition(n);
        int n3 = 0;
        if (itemInfo != null) {
            n3 = (int)((float)this.getClientWidth() * Math.max(this.mFirstOffset, Math.min(itemInfo.offset, this.mLastOffset)));
        }
        if (bl) {
            this.smoothScrollTo(n3, 0, n2);
            if (bl2) {
                this.triggerOnPageChangeEvent(n);
            }
        } else {
            if (bl2) {
                this.triggerOnPageChangeEvent(n);
            }
            this.completeScroll(false);
            this.scrollTo(n3, 0);
            this.pageScrolled(n3);
        }
    }

    private void setAdapterViewPagerObserver(PagerObserver pagerObserver) {
        try {
            Method method = PagerAdapter.class.getDeclaredMethod("setViewPagerObserver", DataSetObserver.class);
            method.setAccessible(true);
            method.invoke((Object)this.mAdapter, new Object[]{pagerObserver});
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setScrollingCacheEnabled(boolean bl) {
        if (this.mScrollingCacheEnabled != bl) {
            this.mScrollingCacheEnabled = bl;
        }
    }

    private void sortChildDrawingOrder() {
        if (this.mDrawingOrder != 0) {
            View view = this.mDrawingOrderedChildren;
            if (view == null) {
                this.mDrawingOrderedChildren = new ArrayList();
            } else {
                view.clear();
            }
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                view = this.getChildAt(i);
                this.mDrawingOrderedChildren.add(view);
            }
            Collections.sort(this.mDrawingOrderedChildren, sPositionComparator);
        }
    }

    private void triggerOnPageChangeEvent(int n) {
        OnPageChangeListener onPageChangeListener2;
        for (OnPageChangeListener onPageChangeListener2 : this.mOnPageChangeListeners) {
            if (onPageChangeListener2 == null) continue;
            PagerAdapter pagerAdapter = this.mAdapter;
            if (pagerAdapter instanceof InfinitePagerAdapter) {
                onPageChangeListener2.onPageSelected(((InfinitePagerAdapter)pagerAdapter).getRealPosition(n));
                continue;
            }
            onPageChangeListener2.onPageSelected(n);
        }
        onPageChangeListener2 = this.mInternalPageChangeListener;
        if (onPageChangeListener2 != null) {
            onPageChangeListener2.onPageSelected(n);
        }
    }

    public void addFocusables(ArrayList<View> arrayList, int n, int n2) {
        int n3 = arrayList.size();
        int n4 = this.getDescendantFocusability();
        if (n4 != 393216) {
            for (int i = 0; i < this.getChildCount(); ++i) {
                ItemInfo itemInfo;
                View view = this.getChildAt(i);
                if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem) continue;
                view.addFocusables(arrayList, n, n2);
            }
        }
        if (n4 != 262144 || n3 == arrayList.size()) {
            if (!this.isFocusable()) {
                return;
            }
            if ((n2 & 1) == 1 && this.isInTouchMode() && !this.isFocusableInTouchMode()) {
                return;
            }
            if (arrayList != null) {
                arrayList.add((View)this);
            }
        }
    }

    ItemInfo addNewItem(int n, int n2) {
        ItemInfo itemInfo = new ItemInfo();
        itemInfo.position = n;
        itemInfo.object = this.mAdapter.instantiateItem(this, n);
        itemInfo.widthFactor = this.mAdapter.getPageWidth(n);
        if (n2 >= 0 && n2 < this.mItems.size()) {
            this.mItems.add(n2, itemInfo);
        } else {
            this.mItems.add(itemInfo);
        }
        return itemInfo;
    }

    public void addOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
        if (this.mAdapterChangeListeners == null) {
            this.mAdapterChangeListeners = new ArrayList<OnAdapterChangeListener>();
        }
        this.mAdapterChangeListeners.add(onAdapterChangeListener);
    }

    public void addOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        if (this.mOnPageChangeListeners == null) {
            this.mOnPageChangeListeners = new ArrayList<OnPageChangeListener>();
        }
        this.mOnPageChangeListeners.add(onPageChangeListener);
    }

    public void addTouchables(ArrayList<View> arrayList) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            ItemInfo itemInfo;
            View view = this.getChildAt(i);
            if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem) continue;
            view.addTouchables(arrayList);
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        ViewGroup.LayoutParams layoutParams2 = layoutParams;
        if (!this.checkLayoutParams(layoutParams)) {
            layoutParams2 = this.generateLayoutParams(layoutParams);
        }
        layoutParams = (LayoutParams)layoutParams2;
        layoutParams.isDecor |= SliderPager.isDecorView(view);
        if (this.mInLayout) {
            if (layoutParams != null && layoutParams.isDecor) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }
            layoutParams.needsMeasure = true;
            this.addViewInLayout(view, n, layoutParams2);
        } else {
            super.addView(view, n, layoutParams2);
        }
    }

    public boolean arrowScroll(int n) {
        int n2;
        int n3;
        Object object;
        View view = this.findFocus();
        if (view == this) {
            object = null;
        } else {
            object = view;
            if (view != null) {
                n3 = 0;
                object = view.getParent();
                while (true) {
                    n2 = n3;
                    if (!(object instanceof ViewGroup)) break;
                    if (object == this) {
                        n2 = 1;
                        break;
                    }
                    object = object.getParent();
                }
                object = view;
                if (n2 == 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(view.getClass().getSimpleName());
                    object = view.getParent();
                    while (object instanceof ViewGroup) {
                        stringBuilder.append(" => ");
                        stringBuilder.append(object.getClass().getSimpleName());
                        object = object.getParent();
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("arrowScroll tried to find focus based on non-child current focused view ");
                    ((StringBuilder)object).append(stringBuilder.toString());
                    Log.e((String)TAG, (String)((StringBuilder)object).toString());
                    object = null;
                }
            }
        }
        boolean bl = false;
        boolean bl2 = false;
        view = FocusFinder.getInstance().findNextFocus((ViewGroup)this, (View)object, n);
        if (view != null && view != object) {
            if (n == 17) {
                n3 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)view).left;
                n2 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)object).left;
                bl = object != null && n3 >= n2 ? this.pageLeft() : view.requestFocus();
            } else {
                bl = bl2;
                if (n == 66) {
                    n3 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)view).left;
                    n2 = this.getChildRectInPagerCoordinates((Rect)this.mTempRect, (View)object).left;
                    bl = object != null && n3 <= n2 ? this.pageRight() : view.requestFocus();
                }
            }
        } else if (n != 17 && n != 1) {
            if (n == 66 || n == 2) {
                bl = this.pageRight();
            }
        } else {
            bl = this.pageLeft();
        }
        if (bl) {
            this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection((int)n));
        }
        return bl;
    }

    public boolean beginFakeDrag() {
        if (this.mIsBeingDragged) {
            return false;
        }
        this.mFakeDragging = true;
        this.setScrollState(1);
        this.mLastMotionX = 0.0f;
        this.mInitialMotionX = 0.0f;
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
        long l = SystemClock.uptimeMillis();
        velocityTracker = MotionEvent.obtain((long)l, (long)l, (int)0, (float)0.0f, (float)0.0f, (int)0);
        this.mVelocityTracker.addMovement((MotionEvent)velocityTracker);
        velocityTracker.recycle();
        this.mFakeDragBeginTime = l;
        return true;
    }

    protected boolean canScroll(View view, boolean bl, int n, int n2, int n3) {
        boolean bl2 = view instanceof ViewGroup;
        boolean bl3 = true;
        if (bl2) {
            ViewGroup viewGroup = (ViewGroup)view;
            int n4 = view.getScrollX();
            int n5 = view.getScrollY();
            for (int i = viewGroup.getChildCount() - 1; i >= 0; --i) {
                View view2 = viewGroup.getChildAt(i);
                if (n2 + n4 < view2.getLeft() || n2 + n4 >= view2.getRight() || n3 + n5 < view2.getTop() || n3 + n5 >= view2.getBottom() || !this.canScroll(view2, true, n, n2 + n4 - view2.getLeft(), n3 + n5 - view2.getTop())) continue;
                return true;
            }
        }
        bl = bl && view.canScrollHorizontally(-n) ? bl3 : false;
        return bl;
    }

    public boolean canScrollHorizontally(int n) {
        PagerAdapter pagerAdapter = this.mAdapter;
        boolean bl = false;
        boolean bl2 = false;
        if (pagerAdapter == null) {
            return false;
        }
        int n2 = this.getClientWidth();
        int n3 = this.getScrollX();
        if (n < 0) {
            bl = bl2;
            if (n3 > (int)((float)n2 * this.mFirstOffset)) {
                bl = true;
            }
            return bl;
        }
        if (n > 0) {
            if (n3 < (int)((float)n2 * this.mLastOffset)) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        boolean bl = layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams);
        return bl;
    }

    public void clearOnPageChangeListeners() {
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            list.clear();
        }
    }

    public void computeScroll() {
        this.mIsScrollStarted = true;
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            int n = this.getScrollX();
            int n2 = this.getScrollY();
            int n3 = this.mScroller.getCurrX();
            int n4 = this.mScroller.getCurrY();
            if (n != n3 || n2 != n4) {
                this.scrollTo(n3, n4);
                if (!this.pageScrolled(n3)) {
                    this.mScroller.abortAnimation();
                    this.scrollTo(0, n4);
                }
            }
            ViewCompat.postInvalidateOnAnimation((View)this);
            return;
        }
        this.completeScroll(true);
    }

    void dataSetChanged() {
        Object object;
        int n;
        this.mExpectedAdapterCount = n = this.mAdapter.getCount();
        int n2 = this.mItems.size() < this.mOffscreenPageLimit * 2 + 1 && this.mItems.size() < n ? 1 : 0;
        int n3 = this.mCurItem;
        int n4 = 0;
        int n5 = 0;
        while (n5 < this.mItems.size()) {
            int n6;
            int n7;
            int n8;
            object = this.mItems.get(n5);
            int n9 = this.mAdapter.getItemPosition(object.object);
            if (n9 == -1) {
                n8 = n3;
                n7 = n4;
                n6 = n5;
            } else if (n9 == -2) {
                this.mItems.remove(n5);
                n9 = n5 - 1;
                n5 = n4;
                if (n4 == 0) {
                    this.mAdapter.startUpdate(this);
                    n5 = 1;
                }
                this.mAdapter.destroyItem(this, object.position, object.object);
                n2 = 1;
                n8 = n3;
                n7 = n5;
                n6 = n9;
                if (this.mCurItem == object.position) {
                    n8 = Math.max(0, Math.min(this.mCurItem, n - 1));
                    n2 = 1;
                    n7 = n5;
                    n6 = n9;
                }
            } else {
                n8 = n3;
                n7 = n4;
                n6 = n5;
                if (object.position != n9) {
                    if (object.position == this.mCurItem) {
                        n3 = n9;
                    }
                    object.position = n9;
                    n2 = 1;
                    n6 = n5;
                    n7 = n4;
                    n8 = n3;
                }
            }
            n5 = n6 + 1;
            n3 = n8;
            n4 = n7;
        }
        if (n4 != 0) {
            this.mAdapter.finishUpdate(this);
        }
        Collections.sort(this.mItems, COMPARATOR);
        if (n2 != 0) {
            n4 = this.getChildCount();
            for (n2 = 0; n2 < n4; ++n2) {
                object = (LayoutParams)this.getChildAt(n2).getLayoutParams();
                if (((LayoutParams)((Object)object)).isDecor) continue;
                ((LayoutParams)((Object)object)).widthFactor = 0.0f;
            }
            this.setCurrentItemInternal(n3, false, true);
            this.requestLayout();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        boolean bl = super.dispatchKeyEvent(keyEvent) || this.executeKeyEvent(keyEvent);
        return bl;
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        if (accessibilityEvent.getEventType() == 4096) {
            return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            ItemInfo itemInfo;
            View view = this.getChildAt(i);
            if (view.getVisibility() != 0 || (itemInfo = this.infoForChild(view)) == null || itemInfo.position != this.mCurItem || !view.dispatchPopulateAccessibilityEvent(accessibilityEvent)) continue;
            return true;
        }
        return false;
    }

    float distanceInfluenceForSnapDuration(float f) {
        return (float)Math.sin((f - 0.5f) * 0.47123894f);
    }

    public void draw(Canvas canvas) {
        PagerAdapter pagerAdapter;
        super.draw(canvas);
        int n = 0;
        int n2 = 0;
        int n3 = this.getOverScrollMode();
        if (n3 != 0 && (n3 != 1 || (pagerAdapter = this.mAdapter) == null || pagerAdapter.getCount() <= 1)) {
            this.mLeftEdge.finish();
            this.mRightEdge.finish();
        } else {
            if (!this.mLeftEdge.isFinished()) {
                n = canvas.save();
                n2 = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
                n3 = this.getWidth();
                canvas.rotate(270.0f);
                canvas.translate((float)(-n2 + this.getPaddingTop()), this.mFirstOffset * (float)n3);
                this.mLeftEdge.setSize(n2, n3);
                n2 = 0 | this.mLeftEdge.draw(canvas);
                canvas.restoreToCount(n);
            }
            n = n2;
            if (!this.mRightEdge.isFinished()) {
                n3 = canvas.save();
                int n4 = this.getWidth();
                int n5 = this.getHeight();
                int n6 = this.getPaddingTop();
                n = this.getPaddingBottom();
                canvas.rotate(90.0f);
                canvas.translate((float)(-this.getPaddingTop()), -(this.mLastOffset + 1.0f) * (float)n4);
                this.mRightEdge.setSize(n5 - n6 - n, n4);
                n = n2 | this.mRightEdge.draw(canvas);
                canvas.restoreToCount(n3);
            }
        }
        if (n != 0) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable2 = this.mMarginDrawable;
        if (drawable2 != null && drawable2.isStateful()) {
            drawable2.setState(this.getDrawableState());
        }
    }

    public void endFakeDrag() {
        if (this.mFakeDragging) {
            if (this.mAdapter != null) {
                Object object = this.mVelocityTracker;
                object.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                int n = (int)object.getXVelocity(this.mActivePointerId);
                this.mPopulatePending = true;
                int n2 = this.getClientWidth();
                int n3 = this.getScrollX();
                object = this.infoForCurrentScrollPosition();
                this.setCurrentItemInternal(this.determineTargetPage(object.position, ((float)n3 / (float)n2 - object.offset) / object.widthFactor, n, (int)(this.mLastMotionX - this.mInitialMotionX)), true, true, n);
            }
            this.endDrag();
            this.mFakeDragging = false;
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    public boolean executeKeyEvent(KeyEvent keyEvent) {
        boolean bl;
        boolean bl2 = bl = false;
        if (keyEvent.getAction() == 0) {
            switch (keyEvent.getKeyCode()) {
                default: {
                    bl2 = bl;
                    break;
                }
                case 61: {
                    if (keyEvent.hasNoModifiers()) {
                        bl2 = this.arrowScroll(2);
                        break;
                    }
                    bl2 = bl;
                    if (!keyEvent.hasModifiers(1)) break;
                    bl2 = this.arrowScroll(1);
                    break;
                }
                case 22: {
                    if (keyEvent.hasModifiers(2)) {
                        bl2 = this.pageRight();
                        break;
                    }
                    bl2 = this.arrowScroll(66);
                    break;
                }
                case 21: {
                    bl2 = keyEvent.hasModifiers(2) ? this.pageLeft() : this.arrowScroll(17);
                }
            }
        }
        return bl2;
    }

    public void fakeDragBy(float f) {
        if (this.mFakeDragging) {
            if (this.mAdapter == null) {
                return;
            }
            this.mLastMotionX += f;
            float f2 = (float)this.getScrollX() - f;
            int n = this.getClientWidth();
            f = (float)n * this.mFirstOffset;
            float f3 = (float)n * this.mLastOffset;
            ItemInfo itemInfo = this.mItems.get(0);
            Object object = this.mItems;
            object = ((ArrayList)object).get(((ArrayList)object).size() - 1);
            if (itemInfo.position != 0) {
                f = itemInfo.offset * (float)n;
            }
            if (((ItemInfo)object).position != this.mAdapter.getCount() - 1) {
                f3 = ((ItemInfo)object).offset * (float)n;
            }
            if (!(f2 < f)) {
                f = f2;
                if (f2 > f3) {
                    f = f3;
                }
            }
            this.mLastMotionX += f - (float)((int)f);
            this.scrollTo((int)f, this.getScrollY());
            this.pageScrolled((int)f);
            long l = SystemClock.uptimeMillis();
            itemInfo = MotionEvent.obtain((long)this.mFakeDragBeginTime, (long)l, (int)2, (float)this.mLastMotionX, (float)0.0f, (int)0);
            this.mVelocityTracker.addMovement((MotionEvent)itemInfo);
            itemInfo.recycle();
            return;
        }
        throw new IllegalStateException("No fake drag in progress. Call beginFakeDrag first.");
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return this.generateDefaultLayoutParams();
    }

    public PagerAdapter getAdapter() {
        return this.mAdapter;
    }

    protected int getChildDrawingOrder(int n, int n2) {
        n = this.mDrawingOrder == 2 ? n - 1 - n2 : n2;
        ArrayList<View> arrayList = this.mDrawingOrderedChildren;
        if (arrayList == null || arrayList.size() != this.getChildCount()) {
            this.sortChildDrawingOrder();
        }
        return ((LayoutParams)this.mDrawingOrderedChildren.get((int)n).getLayoutParams()).childIndex;
    }

    public int getCurrentItem() {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter instanceof InfinitePagerAdapter && ((InfinitePagerAdapter)pagerAdapter).getRealCount() > 0) {
            return ((InfinitePagerAdapter)this.mAdapter).getRealPosition(this.mCurItem);
        }
        return this.mCurItem;
    }

    public int getOffscreenPageLimit() {
        return this.mOffscreenPageLimit;
    }

    public int getPageMargin() {
        return this.mPageMargin;
    }

    ItemInfo infoForAnyChild(View view) {
        ViewParent viewParent;
        while ((viewParent = view.getParent()) != this) {
            if (viewParent != null && viewParent instanceof View) {
                view = (View)viewParent;
                continue;
            }
            return null;
        }
        return this.infoForChild(view);
    }

    ItemInfo infoForChild(View view) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (!this.mAdapter.isViewFromObject(view, itemInfo.object)) continue;
            return itemInfo;
        }
        return null;
    }

    ItemInfo infoForPosition(int n) {
        for (int i = 0; i < this.mItems.size(); ++i) {
            ItemInfo itemInfo = this.mItems.get(i);
            if (itemInfo.position != n) continue;
            return itemInfo;
        }
        return null;
    }

    void initSliderPager() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        Context context = this.getContext();
        this.mScroller = new OwnScroller(this, context, 250, sInterpolator);
        ViewConfiguration viewConfiguration = ViewConfiguration.get((Context)context);
        float f = context.getResources().getDisplayMetrics().density;
        this.mTouchSlop = viewConfiguration.getScaledPagingTouchSlop();
        this.mMinimumVelocity = (int)(400.0f * f);
        this.mMaximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mLeftEdge = new EdgeEffect(context);
        this.mRightEdge = new EdgeEffect(context);
        this.mFlingDistance = (int)(25.0f * f);
        this.mCloseEnough = (int)(2.0f * f);
        this.mDefaultGutterSize = (int)(16.0f * f);
        ViewCompat.setAccessibilityDelegate((View)this, new MyAccessibilityDelegate(this));
        if (ViewCompat.getImportantForAccessibility((View)this) == 0) {
            ViewCompat.setImportantForAccessibility((View)this, 1);
        }
        ViewCompat.setOnApplyWindowInsetsListener((View)this, new OnApplyWindowInsetsListener(this){
            private final Rect mTempRect;
            final SliderPager this$0;
            {
                this.this$0 = sliderPager;
                this.mTempRect = new Rect();
            }

            @Override
            public WindowInsetsCompat onApplyWindowInsets(View object, WindowInsetsCompat windowInsetsCompat) {
                if (((WindowInsetsCompat)(object = ViewCompat.onApplyWindowInsets((View)object, windowInsetsCompat))).isConsumed()) {
                    return object;
                }
                windowInsetsCompat = this.mTempRect;
                ((Rect)windowInsetsCompat).left = ((WindowInsetsCompat)object).getSystemWindowInsetLeft();
                ((Rect)windowInsetsCompat).top = ((WindowInsetsCompat)object).getSystemWindowInsetTop();
                ((Rect)windowInsetsCompat).right = ((WindowInsetsCompat)object).getSystemWindowInsetRight();
                ((Rect)windowInsetsCompat).bottom = ((WindowInsetsCompat)object).getSystemWindowInsetBottom();
                int n = this.this$0.getChildCount();
                for (int i = 0; i < n; ++i) {
                    WindowInsetsCompat windowInsetsCompat2 = ViewCompat.dispatchApplyWindowInsets(this.this$0.getChildAt(i), (WindowInsetsCompat)object);
                    ((Rect)windowInsetsCompat).left = Math.min(windowInsetsCompat2.getSystemWindowInsetLeft(), ((Rect)windowInsetsCompat).left);
                    ((Rect)windowInsetsCompat).top = Math.min(windowInsetsCompat2.getSystemWindowInsetTop(), ((Rect)windowInsetsCompat).top);
                    ((Rect)windowInsetsCompat).right = Math.min(windowInsetsCompat2.getSystemWindowInsetRight(), ((Rect)windowInsetsCompat).right);
                    ((Rect)windowInsetsCompat).bottom = Math.min(windowInsetsCompat2.getSystemWindowInsetBottom(), ((Rect)windowInsetsCompat).bottom);
                }
                return ((WindowInsetsCompat)object).replaceSystemWindowInsets(((Rect)windowInsetsCompat).left, ((Rect)windowInsetsCompat).top, ((Rect)windowInsetsCompat).right, ((Rect)windowInsetsCompat).bottom);
            }
        });
    }

    public boolean isFakeDragging() {
        return this.mFakeDragging;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        this.removeCallbacks(this.mEndScrollRunnable);
        Scroller scroller = this.mScroller;
        if (scroller != null && !scroller.isFinished()) {
            this.mScroller.abortAnimation();
        }
        super.onDetachedFromWindow();
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mPageMargin > 0 && this.mMarginDrawable != null && this.mItems.size() > 0 && this.mAdapter != null) {
            int n = this.getScrollX();
            int n2 = this.getWidth();
            float f = (float)this.mPageMargin / (float)n2;
            int n3 = 0;
            Object object = this.mItems.get(0);
            float f2 = ((ItemInfo)object).offset;
            int n4 = this.mItems.size();
            int n5 = this.mItems.get((int)(n4 - 1)).position;
            for (int i = ((ItemInfo)object).position; i < n5; ++i) {
                float f3;
                while (i > ((ItemInfo)object).position && n3 < n4) {
                    object = this.mItems;
                    object = (ItemInfo)((ArrayList)object).get(++n3);
                }
                if (i == ((ItemInfo)object).position) {
                    f3 = (((ItemInfo)object).offset + ((ItemInfo)object).widthFactor) * (float)n2;
                    f2 = ((ItemInfo)object).offset + ((ItemInfo)object).widthFactor + f;
                } else {
                    f3 = this.mAdapter.getPageWidth(i);
                    float f4 = n2;
                    float f5 = f2 + (f3 + f);
                    f3 = (f2 + f3) * f4;
                    f2 = f5;
                }
                if ((float)this.mPageMargin + f3 > (float)n) {
                    this.mMarginDrawable.setBounds(Math.round(f3), this.mTopPageBounds, Math.round((float)this.mPageMargin + f3), this.mBottomPageBounds);
                    this.mMarginDrawable.draw(canvas);
                }
                if (f3 > (float)(n + n2)) break;
            }
        }
    }

    /*
     * Exception decompiling
     */
    public boolean onInterceptTouchEvent(MotionEvent var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [18[CASE]], but top level block is 3[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        LayoutParams layoutParams;
        int n5;
        int n6;
        Object object;
        int n7;
        int n8 = this.getChildCount();
        int n9 = n3 - n;
        int n10 = n4 - n2;
        n = this.getPaddingLeft();
        n2 = this.getPaddingTop();
        int n11 = this.getPaddingRight();
        n4 = this.getPaddingBottom();
        int n12 = this.getScrollX();
        int n13 = 0;
        for (n7 = 0; n7 < n8; ++n7) {
            object = this.getChildAt(n7);
            n3 = n;
            n6 = n2;
            n5 = n11;
            int n14 = n4;
            int n15 = n13;
            if (object.getVisibility() != 8) {
                layoutParams = (LayoutParams)object.getLayoutParams();
                if (layoutParams.isDecor) {
                    n3 = layoutParams.gravity;
                    n5 = layoutParams.gravity;
                    switch (n3 & 7) {
                        default: {
                            n3 = n;
                            n6 = n;
                            break;
                        }
                        case 5: {
                            n3 = n9 - n11 - object.getMeasuredWidth();
                            n11 += object.getMeasuredWidth();
                            n6 = n;
                            break;
                        }
                        case 3: {
                            n3 = n;
                            n6 = n + object.getMeasuredWidth();
                            break;
                        }
                        case 1: {
                            n3 = Math.max((n9 - object.getMeasuredWidth()) / 2, n);
                            n6 = n;
                        }
                    }
                    switch (n5 & 0x70) {
                        default: {
                            n = n2;
                            break;
                        }
                        case 80: {
                            n = n10 - n4 - object.getMeasuredHeight();
                            n4 += object.getMeasuredHeight();
                            break;
                        }
                        case 48: {
                            n = n2;
                            n2 += object.getMeasuredHeight();
                            break;
                        }
                        case 16: {
                            n = Math.max((n10 - object.getMeasuredHeight()) / 2, n2);
                        }
                    }
                    object.layout(n3 += n12, n, n3 + object.getMeasuredWidth(), n + object.getMeasuredHeight());
                    n15 = n13 + 1;
                    n3 = n6;
                    n6 = n2;
                    n5 = n11;
                    n14 = n4;
                } else {
                    n15 = n13;
                    n14 = n4;
                    n5 = n11;
                    n6 = n2;
                    n3 = n;
                }
            }
            n = n3;
            n2 = n6;
            n11 = n5;
            n4 = n14;
            n13 = n15;
        }
        n7 = n9 - n - n11;
        n3 = n9;
        n11 = n8;
        for (n6 = 0; n6 < n11; ++n6) {
            View view = this.getChildAt(n6);
            if (view.getVisibility() == 8) continue;
            layoutParams = (LayoutParams)view.getLayoutParams();
            if (layoutParams.isDecor || (object = this.infoForChild(view)) == null) continue;
            n5 = n + (int)((float)n7 * object.offset);
            if (layoutParams.needsMeasure) {
                layoutParams.needsMeasure = false;
                view.measure(View.MeasureSpec.makeMeasureSpec((int)((int)((float)n7 * layoutParams.widthFactor)), (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)(n10 - n2 - n4), (int)0x40000000));
            }
            view.layout(n5, n2, view.getMeasuredWidth() + n5, view.getMeasuredHeight() + n2);
        }
        this.mTopPageBounds = n2;
        this.mBottomPageBounds = n10 - n4;
        this.mDecorChildCount = n13;
        if (this.mFirstLayout) {
            this.scrollToItem(this.mCurItem, false, 0, false);
        }
        this.mFirstLayout = false;
    }

    protected void onMeasure(int n, int n2) {
        int n3;
        LayoutParams layoutParams;
        View view;
        this.setMeasuredDimension(SliderPager.getDefaultSize((int)0, (int)n), SliderPager.getDefaultSize((int)0, (int)n2));
        int n4 = this.getMeasuredWidth();
        int n5 = n4 / 10;
        this.mGutterSize = Math.min(n5, this.mDefaultGutterSize);
        n = n4 - this.getPaddingLeft() - this.getPaddingRight();
        n2 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom();
        int n6 = this.getChildCount();
        for (int i = 0; i < n6; ++i) {
            int n7;
            view = this.getChildAt(i);
            if (view.getVisibility() != 8) {
                layoutParams = (LayoutParams)view.getLayoutParams();
                if (layoutParams != null && layoutParams.isDecor) {
                    int n8;
                    n3 = layoutParams.gravity & 7;
                    int n9 = layoutParams.gravity & 0x70;
                    int n10 = Integer.MIN_VALUE;
                    n7 = Integer.MIN_VALUE;
                    n9 = n9 != 48 && n9 != 80 ? 0 : 1;
                    boolean bl = n3 == 3 || n3 == 5;
                    if (n9 != 0) {
                        n3 = 0x40000000;
                    } else {
                        n3 = n10;
                        if (bl) {
                            n7 = 0x40000000;
                            n3 = n10;
                        }
                    }
                    if (layoutParams.width != -2) {
                        n8 = 0x40000000;
                        n3 = layoutParams.width != -1 ? layoutParams.width : n;
                    } else {
                        n10 = n;
                        n8 = n3;
                        n3 = n10;
                    }
                    if (layoutParams.height != -2) {
                        if (layoutParams.height != -1) {
                            n10 = layoutParams.height;
                            n7 = 0x40000000;
                        } else {
                            n7 = 0x40000000;
                            n10 = n2;
                        }
                    } else {
                        n10 = n2;
                    }
                    view.measure(View.MeasureSpec.makeMeasureSpec((int)n3, (int)n8), View.MeasureSpec.makeMeasureSpec((int)n10, (int)n7));
                    if (n9 != 0) {
                        n7 = n2 - view.getMeasuredHeight();
                        n3 = n;
                    } else {
                        n3 = n;
                        n7 = n2;
                        if (bl) {
                            n3 = n - view.getMeasuredWidth();
                            n7 = n2;
                        }
                    }
                } else {
                    n3 = n;
                    n7 = n2;
                }
            } else {
                n7 = n2;
                n3 = n;
            }
            n = n3;
            n2 = n7;
        }
        this.mChildWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)n, (int)0x40000000);
        this.mChildHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec((int)n2, (int)0x40000000);
        this.mInLayout = true;
        this.populate();
        this.mInLayout = false;
        n3 = this.getChildCount();
        for (n2 = 0; n2 < n3; ++n2) {
            view = this.getChildAt(n2);
            if (view.getVisibility() == 8 || (layoutParams = (LayoutParams)view.getLayoutParams()) != null && layoutParams.isDecor) continue;
            view.measure(View.MeasureSpec.makeMeasureSpec((int)((int)((float)n * layoutParams.widthFactor)), (int)0x40000000), this.mChildHeightMeasureSpec);
        }
    }

    protected void onPageScrolled(int n, float f, int n2) {
        View view;
        int n3;
        if (this.mDecorChildCount > 0) {
            int n4 = this.getScrollX();
            n3 = this.getPaddingLeft();
            int n5 = this.getPaddingRight();
            int n6 = this.getWidth();
            int n7 = this.getChildCount();
            for (int i = 0; i < n7; ++i) {
                int n8;
                int n9;
                view = this.getChildAt(i);
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (!layoutParams.isDecor) {
                    n9 = n3;
                    n8 = n5;
                } else {
                    switch (layoutParams.gravity & 7) {
                        default: {
                            n8 = n3;
                            break;
                        }
                        case 5: {
                            n8 = n6 - n5 - view.getMeasuredWidth();
                            n5 += view.getMeasuredWidth();
                            break;
                        }
                        case 3: {
                            n8 = n3;
                            n3 += view.getWidth();
                            break;
                        }
                        case 1: {
                            n8 = Math.max((n6 - view.getMeasuredWidth()) / 2, n3);
                        }
                    }
                    int n10 = n8 + n4 - view.getLeft();
                    n9 = n3;
                    n8 = n5;
                    if (n10 != 0) {
                        view.offsetLeftAndRight(n10);
                        n8 = n5;
                        n9 = n3;
                    }
                }
                n3 = n9;
                n5 = n8;
            }
        }
        this.dispatchOnPageScrolled(n, f, n2);
        if (this.mPageTransformer != null) {
            n2 = this.getScrollX();
            n3 = this.getChildCount();
            for (n = 0; n < n3; ++n) {
                view = this.getChildAt(n);
                if (((LayoutParams)view.getLayoutParams()).isDecor) continue;
                f = (float)(view.getLeft() - n2) / (float)this.getClientWidth();
                this.mPageTransformer.transformPage(view, f);
            }
        }
        this.mCalledSuper = true;
    }

    protected boolean onRequestFocusInDescendants(int n, Rect rect) {
        int n2;
        int n3;
        int n4 = this.getChildCount();
        if ((n & 2) != 0) {
            n3 = 0;
            n2 = 1;
        } else {
            n3 = n4 - 1;
            n2 = -1;
            n4 = -1;
        }
        while (n3 != n4) {
            ItemInfo itemInfo;
            View view = this.getChildAt(n3);
            if (view.getVisibility() == 0 && (itemInfo = this.infoForChild(view)) != null && itemInfo.position == this.mCurItem && view.requestFocus(n, rect)) {
                return true;
            }
            n3 += n2;
        }
        return false;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        parcelable = (SavedState)parcelable;
        super.onRestoreInstanceState(parcelable.getSuperState());
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            pagerAdapter.restoreState(parcelable.adapterState, parcelable.loader);
            this.setCurrentItemInternal(parcelable.position, false, true);
        } else {
            this.mRestoredCurItem = parcelable.position;
            this.mRestoredAdapterState = parcelable.adapterState;
            this.mRestoredClassLoader = parcelable.loader;
        }
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.position = this.mCurItem;
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null) {
            savedState.adapterState = pagerAdapter.saveState();
        }
        return savedState;
    }

    protected void onSizeChanged(int n, int n2, int n3, int n4) {
        super.onSizeChanged(n, n2, n3, n4);
        if (n != n3) {
            n2 = this.mPageMargin;
            this.recomputeScrollPosition(n, n3, n2, n2);
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mFakeDragging) {
            return true;
        }
        if (motionEvent.getAction() == 0 && motionEvent.getEdgeFlags() != 0) {
            return false;
        }
        Object object = this.mAdapter;
        if (object != null && ((PagerAdapter)object).getCount() != 0) {
            if (this.mVelocityTracker == null) {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            int n = motionEvent.getAction();
            boolean bl = false;
            switch (n & 0xFF) {
                default: {
                    break;
                }
                case 6: {
                    this.onSecondaryPointerUp(motionEvent);
                    this.mLastMotionX = motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId));
                    break;
                }
                case 5: {
                    n = motionEvent.getActionIndex();
                    this.mLastMotionX = motionEvent.getX(n);
                    this.mActivePointerId = motionEvent.getPointerId(n);
                    break;
                }
                case 3: {
                    if (!this.mIsBeingDragged) break;
                    this.scrollToItem(this.mCurItem, true, 0, false);
                    bl = this.resetTouch();
                    break;
                }
                case 2: {
                    if (!this.mIsBeingDragged) {
                        n = motionEvent.findPointerIndex(this.mActivePointerId);
                        if (n == -1) {
                            bl = this.resetTouch();
                            break;
                        }
                        float f = motionEvent.getX(n);
                        float f2 = Math.abs(f - this.mLastMotionX);
                        float f3 = motionEvent.getY(n);
                        float f4 = Math.abs(f3 - this.mLastMotionY);
                        if (f2 > (float)this.mTouchSlop && f2 > f4) {
                            this.mIsBeingDragged = true;
                            this.requestParentDisallowInterceptTouchEvent(true);
                            f4 = this.mInitialMotionX;
                            f = f - f4 > 0.0f ? f4 + (float)this.mTouchSlop : f4 - (float)this.mTouchSlop;
                            this.mLastMotionX = f;
                            this.mLastMotionY = f3;
                            this.setScrollState(1);
                            this.setScrollingCacheEnabled(true);
                            object = this.getParent();
                            if (object != null) {
                                object.requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                    if (!this.mIsBeingDragged) break;
                    bl = false | this.performDrag(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)));
                    break;
                }
                case 1: {
                    if (!this.mIsBeingDragged) break;
                    object = this.mVelocityTracker;
                    object.computeCurrentVelocity(1000, (float)this.mMaximumVelocity);
                    int n2 = (int)object.getXVelocity(this.mActivePointerId);
                    this.mPopulatePending = true;
                    n = this.getClientWidth();
                    int n3 = this.getScrollX();
                    object = this.infoForCurrentScrollPosition();
                    float f = (float)this.mPageMargin / (float)n;
                    this.setCurrentItemInternal(this.determineTargetPage(((ItemInfo)object).position, ((float)n3 / (float)n - ((ItemInfo)object).offset) / (((ItemInfo)object).widthFactor + f), n2, (int)(motionEvent.getX(motionEvent.findPointerIndex(this.mActivePointerId)) - this.mInitialMotionX)), true, true, n2);
                    bl = this.resetTouch();
                    break;
                }
                case 0: {
                    float f;
                    this.mScroller.abortAnimation();
                    this.mPopulatePending = false;
                    this.populate();
                    this.mInitialMotionX = f = motionEvent.getX();
                    this.mLastMotionX = f;
                    this.mInitialMotionY = f = motionEvent.getY();
                    this.mLastMotionY = f;
                    this.mActivePointerId = motionEvent.getPointerId(0);
                }
            }
            if (bl) {
                ViewCompat.postInvalidateOnAnimation((View)this);
            }
            return true;
        }
        return false;
    }

    boolean pageLeft() {
        int n = this.mCurItem;
        if (n > 0) {
            this.setCurrentItem(n - 1, true);
            return true;
        }
        return false;
    }

    boolean pageRight() {
        PagerAdapter pagerAdapter = this.mAdapter;
        if (pagerAdapter != null && this.mCurItem < pagerAdapter.getCount() - 1) {
            this.setCurrentItem(this.mCurItem + 1, true);
            return true;
        }
        return false;
    }

    void populate() {
        this.populate(this.mCurItem);
    }

    void populate(int n) {
        Object object;
        Object object2;
        int n2 = this.mCurItem;
        if (n2 != n) {
            object2 = this.infoForPosition(n2);
            this.mCurItem = n;
        } else {
            object2 = null;
        }
        if (this.mAdapter == null) {
            this.sortChildDrawingOrder();
            return;
        }
        if (this.mPopulatePending) {
            this.sortChildDrawingOrder();
            return;
        }
        if (this.getWindowToken() == null) {
            return;
        }
        this.mAdapter.startUpdate(this);
        int n3 = this.mOffscreenPageLimit;
        int n4 = Math.max(0, this.mCurItem - n3);
        int n5 = this.mAdapter.getCount();
        int n6 = Math.min(n5 - 1, this.mCurItem + n3);
        if (n5 == this.mExpectedAdapterCount) {
            ItemInfo itemInfo;
            Object object3;
            ItemInfo itemInfo2 = null;
            n = 0;
            while (true) {
                object3 = itemInfo2;
                if (n >= this.mItems.size()) break;
                itemInfo = this.mItems.get(n);
                if (itemInfo.position >= this.mCurItem) {
                    object3 = itemInfo2;
                    if (itemInfo.position != this.mCurItem) break;
                    object3 = itemInfo;
                    break;
                }
                ++n;
            }
            itemInfo2 = object3;
            if (object3 == null) {
                itemInfo2 = object3;
                if (n5 > 0) {
                    itemInfo2 = this.addNewItem(this.mCurItem, n);
                }
            }
            if (itemInfo2 != null) {
                float f;
                int n7;
                float f2 = 0.0f;
                int n8 = n - 1;
                object3 = n8 >= 0 ? this.mItems.get(n8) : null;
                int n9 = this.getClientWidth();
                float f3 = n9 <= 0 ? 0.0f : 2.0f - itemInfo2.widthFactor + (float)this.getPaddingLeft() / (float)n9;
                itemInfo = object3;
                int n10 = n;
                for (n7 = this.mCurItem - 1; n7 >= 0; --n7) {
                    if (f2 >= f3 && n7 < n4) {
                        if (itemInfo == null) break;
                        n = n10;
                        f = f2;
                        n2 = n8;
                        object3 = itemInfo;
                        if (n7 == itemInfo.position) {
                            n = n10;
                            f = f2;
                            n2 = n8;
                            object3 = itemInfo;
                            if (!itemInfo.scrolling) {
                                this.mItems.remove(n8);
                                this.mAdapter.destroyItem(this, n7, itemInfo.object);
                                n2 = n8 - 1;
                                n = n10 - 1;
                                object3 = n2 >= 0 ? this.mItems.get(n2) : null;
                                f = f2;
                            }
                        }
                    } else if (itemInfo != null && n7 == itemInfo.position) {
                        f = f2 + itemInfo.widthFactor;
                        n2 = n8 - 1;
                        object3 = n2 >= 0 ? this.mItems.get(n2) : null;
                        n = n10;
                    } else {
                        f = f2 + this.addNewItem((int)n7, (int)(n8 + 1)).widthFactor;
                        n = n10 + 1;
                        object3 = n8 >= 0 ? this.mItems.get(n8) : null;
                        n2 = n8;
                    }
                    n10 = n;
                    f2 = f;
                    n8 = n2;
                    itemInfo = object3;
                }
                f = itemInfo2.widthFactor;
                n = n10 + 1;
                if (f < 2.0f) {
                    object3 = n < this.mItems.size() ? this.mItems.get(n) : null;
                    f3 = n9 <= 0 ? 0.0f : (float)this.getPaddingRight() / (float)n9 + 2.0f;
                    n7 = n4;
                    n8 = n3;
                    for (n2 = this.mCurItem + 1; n2 < n5; ++n2) {
                        if (f >= f3 && n2 > n6) {
                            if (object3 == null) break;
                            if (n2 != object3.position || object3.scrolling) continue;
                            this.mItems.remove(n);
                            this.mAdapter.destroyItem(this, n2, object3.object);
                            if (n < this.mItems.size()) {
                                object3 = this.mItems.get(n);
                                continue;
                            }
                            object3 = null;
                            continue;
                        }
                        if (object3 != null && n2 == object3.position) {
                            f += object3.widthFactor;
                            if (++n < this.mItems.size()) {
                                object3 = this.mItems.get(n);
                                continue;
                            }
                            object3 = null;
                            continue;
                        }
                        object3 = this.addNewItem(n2, n);
                        f += object3.widthFactor;
                        object3 = ++n < this.mItems.size() ? this.mItems.get(n) : null;
                    }
                }
                this.calculatePageOffsets(itemInfo2, n10, (ItemInfo)object2);
                this.mAdapter.setPrimaryItem(this, this.mCurItem, itemInfo2.object);
            }
            this.mAdapter.finishUpdate(this);
            n2 = this.getChildCount();
            for (n = 0; n < n2; ++n) {
                object2 = this.getChildAt(n);
                object3 = (LayoutParams)object2.getLayoutParams();
                ((LayoutParams)((Object)object3)).childIndex = n;
                if (((LayoutParams)((Object)object3)).isDecor || ((LayoutParams)((Object)object3)).widthFactor != 0.0f || (object2 = this.infoForChild((View)object2)) == null) continue;
                ((LayoutParams)((Object)object3)).widthFactor = ((ItemInfo)object2).widthFactor;
                ((LayoutParams)((Object)object3)).position = ((ItemInfo)object2).position;
            }
            this.sortChildDrawingOrder();
            if (this.hasFocus() && ((object3 = (object3 = this.findFocus()) != null ? this.infoForAnyChild((View)object3) : null) == null || object3.position != this.mCurItem)) {
                for (n = 0; !(n >= this.getChildCount() || (object3 = this.infoForChild((View)(object2 = this.getChildAt(n)))) != null && object3.position == this.mCurItem && object2.requestFocus(2)); ++n) {
                }
            }
            return;
        }
        try {
            object = this.getResources().getResourceName(this.getId());
        }
        catch (Resources.NotFoundException notFoundException) {
            object = Integer.toHexString(this.getId());
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("The application's PagerAdapter changed the adapter's contents without calling PagerAdapter#notifyDataSetChanged! Expected adapter item count: ");
        ((StringBuilder)object2).append(this.mExpectedAdapterCount);
        ((StringBuilder)object2).append(", found: ");
        ((StringBuilder)object2).append(n5);
        ((StringBuilder)object2).append(" Pager id: ");
        ((StringBuilder)object2).append((String)object);
        ((StringBuilder)object2).append(" Pager class: ");
        ((StringBuilder)object2).append(((Object)((Object)this)).getClass());
        ((StringBuilder)object2).append(" Problematic adapter: ");
        ((StringBuilder)object2).append(this.mAdapter.getClass());
        object = new IllegalStateException(((StringBuilder)object2).toString());
        throw object;
    }

    public void removeOnAdapterChangeListener(OnAdapterChangeListener onAdapterChangeListener) {
        List<OnAdapterChangeListener> list = this.mAdapterChangeListeners;
        if (list != null) {
            list.remove(onAdapterChangeListener);
        }
    }

    public void removeOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        List<OnPageChangeListener> list = this.mOnPageChangeListeners;
        if (list != null) {
            list.remove(onPageChangeListener);
        }
    }

    public void removeView(View view) {
        if (this.mInLayout) {
            this.removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        List<OnAdapterChangeListener> list;
        Object object;
        int n;
        if (this.mAdapter != null) {
            this.setAdapterViewPagerObserver(null);
            this.mAdapter.startUpdate(this);
            for (n = 0; n < this.mItems.size(); ++n) {
                object = this.mItems.get(n);
                this.mAdapter.destroyItem(this, ((ItemInfo)object).position, ((ItemInfo)object).object);
            }
            this.mAdapter.finishUpdate(this);
            this.mItems.clear();
            this.removeNonDecorViews();
            this.mCurItem = 0;
            this.scrollTo(0, 0);
        }
        object = this.mAdapter;
        this.mAdapter = pagerAdapter;
        this.mExpectedAdapterCount = 0;
        if (pagerAdapter != null) {
            if (this.mObserver == null) {
                this.mObserver = new PagerObserver(this);
            }
            this.setAdapterViewPagerObserver(this.mObserver);
            try {
                this.mAdapter.registerDataSetObserver(this.mObserver);
            }
            catch (Exception exception) {
                // empty catch block
            }
            this.mPopulatePending = false;
            boolean bl = this.mFirstLayout;
            this.mFirstLayout = true;
            this.mExpectedAdapterCount = this.mAdapter.getCount();
            if (this.mRestoredCurItem >= 0) {
                this.mAdapter.restoreState(this.mRestoredAdapterState, this.mRestoredClassLoader);
                this.setCurrentItemInternal(this.mRestoredCurItem, false, true);
                this.mRestoredCurItem = -1;
                this.mRestoredAdapterState = null;
                this.mRestoredClassLoader = null;
            } else if (!bl) {
                this.populate();
            } else {
                this.requestLayout();
            }
        }
        if ((list = this.mAdapterChangeListeners) != null && !list.isEmpty()) {
            int n2 = this.mAdapterChangeListeners.size();
            for (n = 0; n < n2; ++n) {
                this.mAdapterChangeListeners.get(n).onAdapterChanged(this, (PagerAdapter)object, pagerAdapter);
            }
        }
    }

    public void setCurrentItem(int n) {
        this.mPopulatePending = false;
        this.setCurrentItem(n, this.mFirstLayout ^ true);
    }

    public void setCurrentItem(int n, boolean bl) {
        PagerAdapter pagerAdapter = this.mAdapter;
        int n2 = n;
        if (pagerAdapter instanceof InfinitePagerAdapter) {
            n2 = ((InfinitePagerAdapter)pagerAdapter).getMiddlePosition(n);
        }
        this.mPopulatePending = false;
        this.setCurrentItemInternal(n2, bl, false);
    }

    void setCurrentItemInternal(int n, boolean bl, boolean bl2) {
        this.setCurrentItemInternal(n, bl, bl2, 0);
    }

    void setCurrentItemInternal(int n, boolean bl, boolean bl2, int n2) {
        PagerAdapter pagerAdapter = this.mAdapter;
        boolean bl3 = false;
        if (pagerAdapter != null && pagerAdapter.getCount() > 0) {
            int n3;
            if (!bl2 && this.mCurItem == n && this.mItems.size() != 0) {
                this.setScrollingCacheEnabled(false);
                return;
            }
            if (n < 0) {
                n3 = 0;
            } else {
                n3 = n;
                if (n >= this.mAdapter.getCount()) {
                    n3 = this.mAdapter.getCount() - 1;
                }
            }
            n = this.mOffscreenPageLimit;
            int n4 = this.mCurItem;
            if (n3 > n4 + n || n3 < n4 - n) {
                for (n = 0; n < this.mItems.size(); ++n) {
                    this.mItems.get((int)n).scrolling = true;
                }
            }
            bl2 = bl3;
            if (this.mCurItem != n3) {
                bl2 = true;
            }
            if (this.mFirstLayout) {
                this.mCurItem = n3;
                this.triggerOnPageChangeEvent(n3);
                this.requestLayout();
            } else {
                this.populate(n3);
                this.scrollToItem(n3, bl, n2, bl2);
            }
            return;
        }
        this.setScrollingCacheEnabled(false);
    }

    OnPageChangeListener setInternalPageChangeListener(OnPageChangeListener onPageChangeListener) {
        OnPageChangeListener onPageChangeListener2 = this.mInternalPageChangeListener;
        this.mInternalPageChangeListener = onPageChangeListener;
        return onPageChangeListener2;
    }

    public void setOffscreenPageLimit(int n) {
        int n2 = n;
        if (n < 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Requested offscreen page limit ");
            stringBuilder.append(n);
            stringBuilder.append(" too small; defaulting to ");
            stringBuilder.append(1);
            Log.w((String)TAG, (String)stringBuilder.toString());
            n2 = 1;
        }
        if (n2 != this.mOffscreenPageLimit) {
            this.mOffscreenPageLimit = n2;
            this.populate();
        }
    }

    @Deprecated
    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.mOnPageChangeListener = onPageChangeListener;
    }

    public void setPageMargin(int n) {
        int n2 = this.mPageMargin;
        this.mPageMargin = n;
        int n3 = this.getWidth();
        this.recomputeScrollPosition(n3, n3, n, n2);
        this.requestLayout();
    }

    public void setPageMarginDrawable(int n) {
        this.setPageMarginDrawable(ContextCompat.getDrawable(this.getContext(), n));
    }

    public void setPageMarginDrawable(Drawable drawable2) {
        this.mMarginDrawable = drawable2;
        if (drawable2 != null) {
            this.refreshDrawableState();
        }
        boolean bl = drawable2 == null;
        this.setWillNotDraw(bl);
        this.invalidate();
    }

    public void setPageTransformer(boolean bl, PageTransformer pageTransformer) {
        this.setPageTransformer(bl, pageTransformer, 2);
    }

    public void setPageTransformer(boolean bl, PageTransformer pageTransformer, int n) {
        int n2 = 1;
        boolean bl2 = pageTransformer != null;
        boolean bl3 = this.mPageTransformer != null;
        boolean bl4 = bl2 != bl3;
        this.mPageTransformer = pageTransformer;
        this.setChildrenDrawingOrderEnabled(bl2);
        if (bl2) {
            if (bl) {
                n2 = 2;
            }
            this.mDrawingOrder = n2;
            this.mPageTransformerLayerType = n;
        } else {
            this.mDrawingOrder = 0;
        }
        if (bl4) {
            this.populate();
        }
    }

    public void setScrollDuration(int n) {
        this.setScrollDuration(n, null);
    }

    public void setScrollDuration(int n, Interpolator interpolator2) {
        this.mScroller = interpolator2 != null ? new OwnScroller(this, this.getContext(), n, interpolator2) : new OwnScroller(this, this.getContext(), n);
    }

    void setScrollState(int n) {
        if (this.mScrollState == n) {
            return;
        }
        this.mScrollState = n;
        if (this.mPageTransformer != null) {
            boolean bl = n != 0;
            this.enableLayers(bl);
        }
        this.dispatchOnScrollStateChanged(n);
    }

    void smoothScrollTo(int n, int n2) {
        this.smoothScrollTo(n, n2, 0);
    }

    void smoothScrollTo(int n, int n2, int n3) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
            return;
        }
        Scroller scroller = this.mScroller;
        int n4 = scroller != null && !scroller.isFinished() ? 1 : 0;
        if (n4 != 0) {
            n4 = this.mIsScrollStarted ? this.mScroller.getCurrX() : this.mScroller.getStartX();
            this.mScroller.abortAnimation();
            this.setScrollingCacheEnabled(false);
        } else {
            n4 = this.getScrollX();
        }
        int n5 = this.getScrollY();
        int n6 = n - n4;
        if (n6 == 0 && (n2 -= n5) == 0) {
            this.completeScroll(false);
            this.populate();
            this.setScrollState(0);
            return;
        }
        this.setScrollingCacheEnabled(true);
        this.setScrollState(2);
        n = this.getClientWidth();
        int n7 = n / 2;
        float f = Math.min(1.0f, (float)Math.abs(n6) * 1.0f / (float)n);
        float f2 = n7;
        float f3 = n7;
        f = this.distanceInfluenceForSnapDuration(f);
        n3 = Math.abs(n3);
        if (n3 > 0) {
            n = Math.round(Math.abs((f2 + f3 * f) / (float)n3) * 1000.0f) * 4;
        } else {
            f3 = n;
            f2 = this.mAdapter.getPageWidth(this.mCurItem);
            n = (int)((1.0f + (float)Math.abs(n6) / ((float)this.mPageMargin + f3 * f2)) * 100.0f);
        }
        n = Math.min(n, 600);
        this.mIsScrollStarted = false;
        this.mScroller.startScroll(n4, n5, n6, n2, n);
        ViewCompat.postInvalidateOnAnimation((View)this);
    }

    protected boolean verifyDrawable(Drawable drawable2) {
        boolean bl = super.verifyDrawable(drawable2) || drawable2 == this.mMarginDrawable;
        return bl;
    }

    @Inherited
    @Retention(value=RetentionPolicy.RUNTIME)
    @Target(value={ElementType.TYPE})
    public static @interface DecorView {
    }

    static class ItemInfo {
        Object object;
        float offset;
        int position;
        boolean scrolling;
        float widthFactor;

        ItemInfo() {
        }
    }

    public static class LayoutParams
    extends ViewGroup.LayoutParams {
        int childIndex;
        public int gravity;
        public boolean isDecor;
        boolean needsMeasure;
        int position;
        float widthFactor = 0.0f;

        public LayoutParams() {
            super(-1, -1);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
            this.gravity = context.getInteger(0, 48);
            context.recycle();
        }
    }

    class MyAccessibilityDelegate
    extends AccessibilityDelegateCompat {
        final SliderPager this$0;

        MyAccessibilityDelegate(SliderPager sliderPager) {
            this.this$0 = sliderPager;
        }

        private boolean canScroll() {
            PagerAdapter pagerAdapter = this.this$0.mAdapter;
            boolean bl = true;
            if (pagerAdapter == null || this.this$0.mAdapter.getCount() <= 1) {
                bl = false;
            }
            return bl;
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)SliderPager.class.getName());
            accessibilityEvent.setScrollable(this.canScroll());
            if (accessibilityEvent.getEventType() == 4096 && this.this$0.mAdapter != null) {
                accessibilityEvent.setItemCount(this.this$0.mAdapter.getCount());
                accessibilityEvent.setFromIndex(this.this$0.mCurItem);
                accessibilityEvent.setToIndex(this.this$0.mCurItem);
            }
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            accessibilityNodeInfoCompat.setClassName(SliderPager.class.getName());
            accessibilityNodeInfoCompat.setScrollable(this.canScroll());
            if (this.this$0.canScrollHorizontally(1)) {
                accessibilityNodeInfoCompat.addAction(4096);
            }
            if (this.this$0.canScrollHorizontally(-1)) {
                accessibilityNodeInfoCompat.addAction(8192);
            }
        }

        @Override
        public boolean performAccessibilityAction(View object, int n, Bundle bundle) {
            if (super.performAccessibilityAction((View)object, n, bundle)) {
                return true;
            }
            switch (n) {
                default: {
                    return false;
                }
                case 8192: {
                    if (this.this$0.canScrollHorizontally(-1)) {
                        object = this.this$0;
                        ((SliderPager)((Object)object)).setCurrentItem(((SliderPager)((Object)object)).mCurItem - 1);
                        return true;
                    }
                    return false;
                }
                case 4096: 
            }
            if (this.this$0.canScrollHorizontally(1)) {
                object = this.this$0;
                ((SliderPager)((Object)object)).setCurrentItem(((SliderPager)((Object)object)).mCurItem + 1);
                return true;
            }
            return false;
        }
    }

    public static interface OnAdapterChangeListener {
        public void onAdapterChanged(SliderPager var1, PagerAdapter var2, PagerAdapter var3);
    }

    public static interface OnPageChangeListener {
        public void onPageScrollStateChanged(int var1);

        public void onPageScrolled(int var1, float var2, int var3);

        public void onPageSelected(int var1);
    }

    class OwnScroller
    extends Scroller {
        private int durationScrollMillis;
        final SliderPager this$0;

        OwnScroller(SliderPager sliderPager, Context context, int n) {
            this.this$0 = sliderPager;
            super(context, sInterpolator);
            this.durationScrollMillis = n;
        }

        OwnScroller(SliderPager sliderPager, Context context, int n, Interpolator interpolator2) {
            this.this$0 = sliderPager;
            super(context, interpolator2);
            this.durationScrollMillis = n;
        }

        public void startScroll(int n, int n2, int n3, int n4, int n5) {
            super.startScroll(n, n2, n3, n4, this.durationScrollMillis);
        }
    }

    public static interface PageTransformer {
        public void transformPage(View var1, float var2);
    }

    private class PagerObserver
    extends DataSetObserver {
        final SliderPager this$0;

        PagerObserver(SliderPager sliderPager) {
            this.this$0 = sliderPager;
        }

        public void onChanged() {
            this.this$0.dataSetChanged();
        }

        public void onInvalidated() {
            this.this$0.dataSetChanged();
        }
    }

    public static class SavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.ClassLoaderCreator<SavedState>(){

            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            public SavedState[] newArray(int n) {
                return new SavedState[n];
            }
        };
        Parcelable adapterState;
        ClassLoader loader;
        int position;

        SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            ClassLoader classLoader2 = classLoader;
            if (classLoader == null) {
                classLoader2 = this.getClass().getClassLoader();
            }
            this.position = parcel.readInt();
            this.adapterState = parcel.readParcelable(classLoader2);
            this.loader = classLoader2;
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("FragmentPager.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" position=");
            stringBuilder.append(this.position);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.position);
            parcel.writeParcelable(this.adapterState, n);
        }
    }

    public static class SimpleOnPageChangeListener
    implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int n) {
        }

        @Override
        public void onPageScrolled(int n, float f, int n2) {
        }

        @Override
        public void onPageSelected(int n) {
        }
    }

    static class ViewPositionComparator
    implements Comparator<View> {
        ViewPositionComparator() {
        }

        @Override
        public int compare(View object, View object2) {
            object = (LayoutParams)object.getLayoutParams();
            object2 = (LayoutParams)object2.getLayoutParams();
            if (object.isDecor != object2.isDecor) {
                int n = object.isDecor ? 1 : -1;
                return n;
            }
            return object.position - object2.position;
        }
    }
}

