/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Canvas
 *  android.graphics.Matrix
 *  android.graphics.Paint
 *  android.graphics.Rect
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$ClassLoaderCreator
 *  android.os.Parcelable$Creator
 *  android.os.SystemClock
 *  android.util.AttributeSet
 *  android.view.KeyEvent
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnApplyWindowInsetsListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.WindowInsets
 *  android.view.accessibility.AccessibilityEvent
 */
package androidx.drawerlayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.view.AbsSavedState;
import androidx.customview.widget.ViewDragHelper;
import java.util.ArrayList;
import java.util.List;

public class DrawerLayout
extends ViewGroup {
    private static final boolean ALLOW_EDGE_LOCK = false;
    static final boolean CAN_HIDE_DESCENDANTS;
    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;
    private static final int DEFAULT_SCRIM_COLOR = -1728053248;
    private static final int DRAWER_ELEVATION = 10;
    static final int[] LAYOUT_ATTRS;
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;
    public static final int LOCK_MODE_LOCKED_OPEN = 2;
    public static final int LOCK_MODE_UNDEFINED = 3;
    public static final int LOCK_MODE_UNLOCKED = 0;
    private static final int MIN_DRAWER_MARGIN = 64;
    private static final int MIN_FLING_VELOCITY = 400;
    private static final int PEEK_DELAY = 160;
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "DrawerLayout";
    private static final int[] THEME_ATTRS;
    private static final float TOUCH_SLOP_SENSITIVITY = 1.0f;
    private final ChildAccessibilityDelegate mChildAccessibilityDelegate = new ChildAccessibilityDelegate();
    private Rect mChildHitRect;
    private Matrix mChildInvertedMatrix;
    private boolean mChildrenCanceledTouch;
    private boolean mDisallowInterceptRequested;
    private boolean mDrawStatusBarBackground;
    private float mDrawerElevation;
    private int mDrawerState;
    private boolean mFirstLayout = true;
    private boolean mInLayout;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private Object mLastInsets;
    private final ViewDragCallback mLeftCallback;
    private final ViewDragHelper mLeftDragger;
    private DrawerListener mListener;
    private List<DrawerListener> mListeners;
    private int mLockModeEnd = 3;
    private int mLockModeLeft = 3;
    private int mLockModeRight = 3;
    private int mLockModeStart = 3;
    private int mMinDrawerMargin;
    private final ArrayList<View> mNonDrawerViews;
    private final ViewDragCallback mRightCallback;
    private final ViewDragHelper mRightDragger;
    private int mScrimColor = -1728053248;
    private float mScrimOpacity;
    private Paint mScrimPaint = new Paint();
    private Drawable mShadowEnd = null;
    private Drawable mShadowLeft = null;
    private Drawable mShadowLeftResolved;
    private Drawable mShadowRight = null;
    private Drawable mShadowRightResolved;
    private Drawable mShadowStart = null;
    private Drawable mStatusBarBackground;
    private CharSequence mTitleLeft;
    private CharSequence mTitleRight;

    static {
        boolean bl = true;
        THEME_ATTRS = new int[]{16843828};
        LAYOUT_ATTRS = new int[]{16842931};
        boolean bl2 = Build.VERSION.SDK_INT >= 19;
        CAN_HIDE_DESCENDANTS = bl2;
        bl2 = Build.VERSION.SDK_INT >= 21 ? bl : false;
        SET_DRAWER_SHADOW_FROM_ELEVATION = bl2;
    }

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DrawerLayout(Context context, AttributeSet object, int n) {
        super(context, (AttributeSet)object, n);
        ViewDragHelper viewDragHelper;
        ViewDragCallback viewDragCallback;
        this.setDescendantFocusability(262144);
        float f = this.getResources().getDisplayMetrics().density;
        this.mMinDrawerMargin = (int)(64.0f * f + 0.5f);
        float f2 = 400.0f * f;
        this.mLeftCallback = viewDragCallback = new ViewDragCallback(this, 3);
        object = new ViewDragCallback(this, 5);
        this.mRightCallback = object;
        this.mLeftDragger = viewDragHelper = ViewDragHelper.create(this, 1.0f, viewDragCallback);
        viewDragHelper.setEdgeTrackingEnabled(1);
        viewDragHelper.setMinVelocity(f2);
        viewDragCallback.setDragger(viewDragHelper);
        this.mRightDragger = viewDragHelper = ViewDragHelper.create(this, 1.0f, (ViewDragHelper.Callback)object);
        viewDragHelper.setEdgeTrackingEnabled(2);
        viewDragHelper.setMinVelocity(f2);
        ((ViewDragCallback)object).setDragger(viewDragHelper);
        this.setFocusableInTouchMode(true);
        ViewCompat.setImportantForAccessibility((View)this, 1);
        ViewCompat.setAccessibilityDelegate((View)this, new AccessibilityDelegate(this));
        this.setMotionEventSplittingEnabled(false);
        if (ViewCompat.getFitsSystemWindows((View)this)) {
            if (Build.VERSION.SDK_INT >= 21) {
                this.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener(this){
                    final DrawerLayout this$0;
                    {
                        this.this$0 = drawerLayout;
                    }

                    public WindowInsets onApplyWindowInsets(View object, WindowInsets windowInsets) {
                        object = (DrawerLayout)((Object)object);
                        boolean bl = windowInsets.getSystemWindowInsetTop() > 0;
                        ((DrawerLayout)((Object)object)).setChildInsets(windowInsets, bl);
                        return windowInsets.consumeSystemWindowInsets();
                    }
                });
                this.setSystemUiVisibility(1280);
                context = context.obtainStyledAttributes(THEME_ATTRS);
                try {
                    this.mStatusBarBackground = context.getDrawable(0);
                }
                finally {
                    context.recycle();
                }
            } else {
                this.mStatusBarBackground = null;
            }
        }
        this.mDrawerElevation = 10.0f * f;
        this.mNonDrawerViews = new ArrayList();
    }

    private boolean dispatchTransformedGenericPointerEvent(MotionEvent motionEvent, View view) {
        boolean bl;
        if (!view.getMatrix().isIdentity()) {
            motionEvent = this.getTransformedMotionEvent(motionEvent, view);
            bl = view.dispatchGenericMotionEvent(motionEvent);
            motionEvent.recycle();
        } else {
            float f = this.getScrollX() - view.getLeft();
            float f2 = this.getScrollY() - view.getTop();
            motionEvent.offsetLocation(f, f2);
            bl = view.dispatchGenericMotionEvent(motionEvent);
            motionEvent.offsetLocation(-f, -f2);
        }
        return bl;
    }

    private MotionEvent getTransformedMotionEvent(MotionEvent motionEvent, View view) {
        float f = this.getScrollX() - view.getLeft();
        float f2 = this.getScrollY() - view.getTop();
        motionEvent = MotionEvent.obtain((MotionEvent)motionEvent);
        motionEvent.offsetLocation(f, f2);
        view = view.getMatrix();
        if (!view.isIdentity()) {
            if (this.mChildInvertedMatrix == null) {
                this.mChildInvertedMatrix = new Matrix();
            }
            view.invert(this.mChildInvertedMatrix);
            motionEvent.transform(this.mChildInvertedMatrix);
        }
        return motionEvent;
    }

    static String gravityToString(int n) {
        if ((n & 3) == 3) {
            return "LEFT";
        }
        if ((n & 5) == 5) {
            return "RIGHT";
        }
        return Integer.toHexString(n);
    }

    private static boolean hasOpaqueBackground(View view) {
        view = view.getBackground();
        boolean bl = false;
        if (view != null) {
            if (view.getOpacity() == -1) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    private boolean hasPeekingDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            if (!((LayoutParams)this.getChildAt((int)i).getLayoutParams()).isPeeking) continue;
            return true;
        }
        return false;
    }

    private boolean hasVisibleDrawer() {
        boolean bl = this.findVisibleDrawer() != null;
        return bl;
    }

    static boolean includeChildForAccessibility(View view) {
        boolean bl = ViewCompat.getImportantForAccessibility(view) != 4 && ViewCompat.getImportantForAccessibility(view) != 2;
        return bl;
    }

    private boolean isInBoundsOfChild(float f, float f2, View view) {
        if (this.mChildHitRect == null) {
            this.mChildHitRect = new Rect();
        }
        view.getHitRect(this.mChildHitRect);
        return this.mChildHitRect.contains((int)f, (int)f2);
    }

    private boolean mirror(Drawable drawable2, int n) {
        if (drawable2 != null && DrawableCompat.isAutoMirrored(drawable2)) {
            DrawableCompat.setLayoutDirection(drawable2, n);
            return true;
        }
        return false;
    }

    private Drawable resolveLeftShadow() {
        int n = ViewCompat.getLayoutDirection((View)this);
        if (n == 0) {
            Drawable drawable2 = this.mShadowStart;
            if (drawable2 != null) {
                this.mirror(drawable2, n);
                return this.mShadowStart;
            }
        } else {
            Drawable drawable3 = this.mShadowEnd;
            if (drawable3 != null) {
                this.mirror(drawable3, n);
                return this.mShadowEnd;
            }
        }
        return this.mShadowLeft;
    }

    private Drawable resolveRightShadow() {
        int n = ViewCompat.getLayoutDirection((View)this);
        if (n == 0) {
            Drawable drawable2 = this.mShadowEnd;
            if (drawable2 != null) {
                this.mirror(drawable2, n);
                return this.mShadowEnd;
            }
        } else {
            Drawable drawable3 = this.mShadowStart;
            if (drawable3 != null) {
                this.mirror(drawable3, n);
                return this.mShadowStart;
            }
        }
        return this.mShadowRight;
    }

    private void resolveShadowDrawables() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return;
        }
        this.mShadowLeftResolved = this.resolveLeftShadow();
        this.mShadowRightResolved = this.resolveRightShadow();
    }

    private void updateChildrenImportantForAccessibility(View view, boolean bl) {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view2 = this.getChildAt(i);
            if (!bl && !this.isDrawerView(view2) || bl && view2 == view) {
                ViewCompat.setImportantForAccessibility(view2, 1);
                continue;
            }
            ViewCompat.setImportantForAccessibility(view2, 4);
        }
    }

    public void addDrawerListener(DrawerListener drawerListener) {
        if (drawerListener == null) {
            return;
        }
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<DrawerListener>();
        }
        this.mListeners.add(drawerListener);
    }

    public void addFocusables(ArrayList<View> arrayList, int n, int n2) {
        View view;
        int n3;
        if (this.getDescendantFocusability() == 393216) {
            return;
        }
        int n4 = this.getChildCount();
        int n5 = 0;
        for (n3 = 0; n3 < n4; ++n3) {
            view = this.getChildAt(n3);
            if (this.isDrawerView(view)) {
                if (!this.isDrawerOpen(view)) continue;
                n5 = 1;
                view.addFocusables(arrayList, n, n2);
                continue;
            }
            this.mNonDrawerViews.add(view);
        }
        if (n5 == 0) {
            n5 = this.mNonDrawerViews.size();
            for (n3 = 0; n3 < n5; ++n3) {
                view = this.mNonDrawerViews.get(n3);
                if (view.getVisibility() != 0) continue;
                view.addFocusables(arrayList, n, n2);
            }
        }
        this.mNonDrawerViews.clear();
    }

    public void addView(View view, int n, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, n, layoutParams);
        if (this.findOpenDrawer() == null && !this.isDrawerView(view)) {
            ViewCompat.setImportantForAccessibility(view, 1);
        } else {
            ViewCompat.setImportantForAccessibility(view, 4);
        }
        if (!CAN_HIDE_DESCENDANTS) {
            ViewCompat.setAccessibilityDelegate(view, this.mChildAccessibilityDelegate);
        }
    }

    void cancelChildViewTouch() {
        if (!this.mChildrenCanceledTouch) {
            long l = SystemClock.uptimeMillis();
            MotionEvent motionEvent = MotionEvent.obtain((long)l, (long)l, (int)3, (float)0.0f, (float)0.0f, (int)0);
            int n = this.getChildCount();
            for (int i = 0; i < n; ++i) {
                this.getChildAt(i).dispatchTouchEvent(motionEvent);
            }
            motionEvent.recycle();
            this.mChildrenCanceledTouch = true;
        }
    }

    boolean checkDrawerViewAbsoluteGravity(View view, int n) {
        boolean bl = (this.getDrawerViewAbsoluteGravity(view) & n) == n;
        return bl;
    }

    protected boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        boolean bl = layoutParams instanceof LayoutParams && super.checkLayoutParams(layoutParams);
        return bl;
    }

    public void closeDrawer(int n) {
        this.closeDrawer(n, true);
    }

    public void closeDrawer(int n, boolean bl) {
        Object object = this.findDrawerWithGravity(n);
        if (object != null) {
            this.closeDrawer((View)object, bl);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("No drawer view found with gravity ");
        ((StringBuilder)object).append(DrawerLayout.gravityToString(n));
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public void closeDrawer(View view) {
        this.closeDrawer(view, true);
    }

    public void closeDrawer(View view, boolean bl) {
        if (this.isDrawerView(view)) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 0.0f;
                layoutParams.openState = 0;
            } else if (bl) {
                layoutParams.openState = 4 | layoutParams.openState;
                if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(view, -view.getWidth(), view.getTop());
                } else {
                    this.mRightDragger.smoothSlideViewTo(view, this.getWidth(), view.getTop());
                }
            } else {
                this.moveDrawerToOffset(view, 0.0f);
                this.updateDrawerState(layoutParams.gravity, 0, view);
                view.setVisibility(4);
            }
            this.invalidate();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append(view);
        stringBuilder.append(" is not a sliding drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void closeDrawers() {
        this.closeDrawers(false);
    }

    void closeDrawers(boolean bl) {
        int n = 0;
        int n2 = this.getChildCount();
        for (int i = 0; i < n2; ++i) {
            View view = this.getChildAt(i);
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            int n3 = n;
            if (this.isDrawerView(view)) {
                if (bl && !layoutParams.isPeeking) {
                    n3 = n;
                } else {
                    n3 = view.getWidth();
                    n = this.checkDrawerViewAbsoluteGravity(view, 3) ? (n |= this.mLeftDragger.smoothSlideViewTo(view, -n3, view.getTop())) : (n |= this.mRightDragger.smoothSlideViewTo(view, this.getWidth(), view.getTop()));
                    layoutParams.isPeeking = false;
                    n3 = n;
                }
            }
            n = n3;
        }
        this.mLeftCallback.removeCallbacks();
        this.mRightCallback.removeCallbacks();
        if (n != 0) {
            this.invalidate();
        }
    }

    public void computeScroll() {
        int n = this.getChildCount();
        float f = 0.0f;
        for (int i = 0; i < n; ++i) {
            f = Math.max(f, ((LayoutParams)this.getChildAt((int)i).getLayoutParams()).onScreen);
        }
        this.mScrimOpacity = f;
        boolean bl = this.mLeftDragger.continueSettling(true);
        boolean bl2 = this.mRightDragger.continueSettling(true);
        if (bl || bl2) {
            ViewCompat.postInvalidateOnAnimation((View)this);
        }
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        if ((motionEvent.getSource() & 2) != 0 && motionEvent.getAction() != 10 && !(this.mScrimOpacity <= 0.0f)) {
            int n = this.getChildCount();
            if (n != 0) {
                float f = motionEvent.getX();
                float f2 = motionEvent.getY();
                --n;
                while (n >= 0) {
                    View view = this.getChildAt(n);
                    if (this.isInBoundsOfChild(f, f2, view) && !this.isContentView(view) && this.dispatchTransformedGenericPointerEvent(motionEvent, view)) {
                        return true;
                    }
                    --n;
                }
            }
            return false;
        }
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    void dispatchOnDrawerClosed(View view) {
        Object object = (LayoutParams)view.getLayoutParams();
        if ((((LayoutParams)((Object)object)).openState & 1) == 1) {
            ((LayoutParams)((Object)object)).openState = 0;
            object = this.mListeners;
            if (object != null) {
                for (int i = object.size() - 1; i >= 0; --i) {
                    this.mListeners.get(i).onDrawerClosed(view);
                }
            }
            this.updateChildrenImportantForAccessibility(view, false);
            if (this.hasWindowFocus() && (view = this.getRootView()) != null) {
                view.sendAccessibilityEvent(32);
            }
        }
    }

    void dispatchOnDrawerOpened(View view) {
        Object object = (LayoutParams)view.getLayoutParams();
        if ((((LayoutParams)((Object)object)).openState & 1) == 0) {
            ((LayoutParams)((Object)object)).openState = 1;
            object = this.mListeners;
            if (object != null) {
                for (int i = object.size() - 1; i >= 0; --i) {
                    this.mListeners.get(i).onDrawerOpened(view);
                }
            }
            this.updateChildrenImportantForAccessibility(view, true);
            if (this.hasWindowFocus()) {
                this.sendAccessibilityEvent(32);
            }
        }
    }

    void dispatchOnDrawerSlide(View view, float f) {
        List<DrawerListener> list = this.mListeners;
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                this.mListeners.get(i).onDrawerSlide(view, f);
            }
        }
    }

    protected boolean drawChild(Canvas canvas, View view, long l) {
        int n;
        int n2;
        int n3 = this.getHeight();
        boolean bl = this.isContentView(view);
        int n4 = 0;
        int n5 = this.getWidth();
        int n6 = canvas.save();
        if (bl) {
            int n7 = this.getChildCount();
            for (n2 = 0; n2 < n7; ++n2) {
                View view2 = this.getChildAt(n2);
                int n8 = n4;
                n = n5;
                if (view2 != view) {
                    n8 = n4;
                    n = n5;
                    if (view2.getVisibility() == 0) {
                        n8 = n4;
                        n = n5;
                        if (DrawerLayout.hasOpaqueBackground(view2)) {
                            n8 = n4;
                            n = n5;
                            if (this.isDrawerView(view2)) {
                                if (view2.getHeight() < n3) {
                                    n8 = n4;
                                    n = n5;
                                } else if (this.checkDrawerViewAbsoluteGravity(view2, 3)) {
                                    n8 = view2.getRight();
                                    n = n4;
                                    if (n8 > n4) {
                                        n = n8;
                                    }
                                    n8 = n;
                                    n = n5;
                                } else {
                                    int n9 = view2.getLeft();
                                    n8 = n4;
                                    n = n5;
                                    if (n9 < n5) {
                                        n = n9;
                                        n8 = n4;
                                    }
                                }
                            }
                        }
                    }
                }
                n4 = n8;
                n5 = n;
            }
            canvas.clipRect(n4, 0, n5, this.getHeight());
        } else {
            n4 = 0;
        }
        boolean bl2 = super.drawChild(canvas, view, l);
        canvas.restoreToCount(n6);
        float f = this.mScrimOpacity;
        if (f > 0.0f && bl) {
            n = this.mScrimColor;
            n2 = (int)((float)((0xFF000000 & n) >>> 24) * f);
            this.mScrimPaint.setColor(n2 << 24 | n & 0xFFFFFF);
            canvas.drawRect((float)n4, 0.0f, (float)n5, (float)this.getHeight(), this.mScrimPaint);
        } else if (this.mShadowLeftResolved != null && this.checkDrawerViewAbsoluteGravity(view, 3)) {
            n = this.mShadowLeftResolved.getIntrinsicWidth();
            n5 = view.getRight();
            n4 = this.mLeftDragger.getEdgeSize();
            f = Math.max(0.0f, Math.min((float)n5 / (float)n4, 1.0f));
            this.mShadowLeftResolved.setBounds(n5, view.getTop(), n5 + n, view.getBottom());
            this.mShadowLeftResolved.setAlpha((int)(255.0f * f));
            this.mShadowLeftResolved.draw(canvas);
        } else if (this.mShadowRightResolved != null && this.checkDrawerViewAbsoluteGravity(view, 5)) {
            n5 = this.mShadowRightResolved.getIntrinsicWidth();
            n2 = view.getLeft();
            n = this.getWidth();
            n4 = this.mRightDragger.getEdgeSize();
            f = Math.max(0.0f, Math.min((float)(n - n2) / (float)n4, 1.0f));
            this.mShadowRightResolved.setBounds(n2 - n5, view.getTop(), n2, view.getBottom());
            this.mShadowRightResolved.setAlpha((int)(255.0f * f));
            this.mShadowRightResolved.draw(canvas);
        }
        return bl2;
    }

    View findDrawerWithGravity(int n) {
        int n2 = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this));
        int n3 = this.getChildCount();
        for (n = 0; n < n3; ++n) {
            View view = this.getChildAt(n);
            if ((this.getDrawerViewAbsoluteGravity(view) & 7) != (n2 & 7)) continue;
            return view;
        }
        return null;
    }

    View findOpenDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if ((((LayoutParams)view.getLayoutParams()).openState & 1) != 1) continue;
            return view;
        }
        return null;
    }

    View findVisibleDrawer() {
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            View view = this.getChildAt(i);
            if (!this.isDrawerView(view) || !this.isDrawerVisible(view)) continue;
            return view;
        }
        return null;
    }

    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(this.getContext(), attributeSet);
    }

    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams object) {
        object = object instanceof LayoutParams ? new LayoutParams((LayoutParams)((Object)object)) : (object instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams)object) : new LayoutParams((ViewGroup.LayoutParams)object));
        return object;
    }

    public float getDrawerElevation() {
        if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return this.mDrawerElevation;
        }
        return 0.0f;
    }

    public int getDrawerLockMode(int n) {
        int n2 = ViewCompat.getLayoutDirection((View)this);
        switch (n) {
            default: {
                break;
            }
            case 0x800005: {
                n = this.mLockModeEnd;
                if (n != 3) {
                    return n;
                }
                n = n2 == 0 ? this.mLockModeRight : this.mLockModeLeft;
                if (n == 3) break;
                return n;
            }
            case 0x800003: {
                n = this.mLockModeStart;
                if (n != 3) {
                    return n;
                }
                n = n2 == 0 ? this.mLockModeLeft : this.mLockModeRight;
                if (n == 3) break;
                return n;
            }
            case 5: {
                n = this.mLockModeRight;
                if (n != 3) {
                    return n;
                }
                n = n2 == 0 ? this.mLockModeEnd : this.mLockModeStart;
                if (n == 3) break;
                return n;
            }
            case 3: {
                n = this.mLockModeLeft;
                if (n != 3) {
                    return n;
                }
                n = n2 == 0 ? this.mLockModeStart : this.mLockModeEnd;
                if (n == 3) break;
                return n;
            }
        }
        return 0;
    }

    public int getDrawerLockMode(View view) {
        if (this.isDrawerView(view)) {
            return this.getDrawerLockMode(((LayoutParams)view.getLayoutParams()).gravity);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append(view);
        stringBuilder.append(" is not a drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public CharSequence getDrawerTitle(int n) {
        if ((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this))) == 3) {
            return this.mTitleLeft;
        }
        if (n == 5) {
            return this.mTitleRight;
        }
        return null;
    }

    int getDrawerViewAbsoluteGravity(View view) {
        return GravityCompat.getAbsoluteGravity(((LayoutParams)view.getLayoutParams()).gravity, ViewCompat.getLayoutDirection((View)this));
    }

    float getDrawerViewOffset(View view) {
        return ((LayoutParams)view.getLayoutParams()).onScreen;
    }

    public Drawable getStatusBarBackgroundDrawable() {
        return this.mStatusBarBackground;
    }

    boolean isContentView(View view) {
        boolean bl = ((LayoutParams)view.getLayoutParams()).gravity == 0;
        return bl;
    }

    public boolean isDrawerOpen(int n) {
        View view = this.findDrawerWithGravity(n);
        if (view != null) {
            return this.isDrawerOpen(view);
        }
        return false;
    }

    public boolean isDrawerOpen(View view) {
        if (this.isDrawerView(view)) {
            int n = ((LayoutParams)view.getLayoutParams()).openState;
            boolean bl = true;
            if ((n & 1) != 1) {
                bl = false;
            }
            return bl;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append(view);
        stringBuilder.append(" is not a drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    boolean isDrawerView(View view) {
        int n = ((LayoutParams)view.getLayoutParams()).gravity;
        if (((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection(view))) & 3) != 0) {
            return true;
        }
        return (n & 5) != 0;
    }

    public boolean isDrawerVisible(int n) {
        View view = this.findDrawerWithGravity(n);
        if (view != null) {
            return this.isDrawerVisible(view);
        }
        return false;
    }

    public boolean isDrawerVisible(View view) {
        if (this.isDrawerView(view)) {
            boolean bl = ((LayoutParams)view.getLayoutParams()).onScreen > 0.0f;
            return bl;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append(view);
        stringBuilder.append(" is not a drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    void moveDrawerToOffset(View view, float f) {
        float f2 = this.getDrawerViewOffset(view);
        int n = view.getWidth();
        int n2 = (int)((float)n * f2);
        n2 = (int)((float)n * f) - n2;
        if (!this.checkDrawerViewAbsoluteGravity(view, 3)) {
            n2 = -n2;
        }
        view.offsetLeftAndRight(n2);
        this.setDrawerViewOffset(view, f);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mFirstLayout = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mFirstLayout = true;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawStatusBarBackground && this.mStatusBarBackground != null) {
            Object object;
            int n = Build.VERSION.SDK_INT >= 21 ? ((object = this.mLastInsets) != null ? ((WindowInsets)object).getSystemWindowInsetTop() : 0) : 0;
            if (n > 0) {
                this.mStatusBarBackground.setBounds(0, 0, this.getWidth(), n);
                this.mStatusBarBackground.draw(canvas);
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int n = motionEvent.getActionMasked();
        boolean bl = this.mLeftDragger.shouldInterceptTouchEvent(motionEvent);
        boolean bl2 = this.mRightDragger.shouldInterceptTouchEvent(motionEvent);
        int n2 = 0;
        int n3 = 0;
        boolean bl3 = true;
        switch (n) {
            default: {
                n = n2;
                break;
            }
            case 2: {
                n = n2;
                if (!this.mLeftDragger.checkTouchSlop(3)) break;
                this.mLeftCallback.removeCallbacks();
                this.mRightCallback.removeCallbacks();
                n = n2;
                break;
            }
            case 1: 
            case 3: {
                this.closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                n = n2;
                break;
            }
            case 0: {
                float f = motionEvent.getX();
                float f2 = motionEvent.getY();
                this.mInitialMotionX = f;
                this.mInitialMotionY = f2;
                n = n3;
                if (this.mScrimOpacity > 0.0f) {
                    motionEvent = this.mLeftDragger.findTopChildUnder((int)f, (int)f2);
                    n = n3;
                    if (motionEvent != null) {
                        n = n3;
                        if (this.isContentView((View)motionEvent)) {
                            n = 1;
                        }
                    }
                }
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
            }
        }
        boolean bl4 = bl3;
        if (!(bl | bl2)) {
            bl4 = bl3;
            if (n == 0) {
                bl4 = bl3;
                if (!this.hasPeekingDrawer()) {
                    bl4 = this.mChildrenCanceledTouch ? bl3 : false;
                }
            }
        }
        return bl4;
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4 && this.hasVisibleDrawer()) {
            keyEvent.startTracking();
            return true;
        }
        return super.onKeyDown(n, keyEvent);
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (n == 4) {
            keyEvent = this.findVisibleDrawer();
            if (keyEvent != null && this.getDrawerLockMode((View)keyEvent) == 0) {
                this.closeDrawers();
            }
            boolean bl = keyEvent != null;
            return bl;
        }
        return super.onKeyUp(n, keyEvent);
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
        this.mInLayout = true;
        int n5 = n3 - n;
        int n6 = this.getChildCount();
        for (n3 = 0; n3 < n6; ++n3) {
            float f;
            int n7;
            View view = this.getChildAt(n3);
            if (view.getVisibility() == 8) continue;
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (this.isContentView(view)) {
                view.layout(layoutParams.leftMargin, layoutParams.topMargin, layoutParams.leftMargin + view.getMeasuredWidth(), layoutParams.topMargin + view.getMeasuredHeight());
                continue;
            }
            int n8 = view.getMeasuredWidth();
            int n9 = view.getMeasuredHeight();
            if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                n7 = -n8 + (int)((float)n8 * layoutParams.onScreen);
                f = (float)(n8 + n7) / (float)n8;
            } else {
                n7 = n5 - (int)((float)n8 * layoutParams.onScreen);
                f = (float)(n5 - n7) / (float)n8;
            }
            boolean bl2 = f != layoutParams.onScreen;
            switch (layoutParams.gravity & 0x70) {
                default: {
                    view.layout(n7, layoutParams.topMargin, n7 + n8, layoutParams.topMargin + n9);
                    break;
                }
                case 80: {
                    n = n4 - n2;
                    view.layout(n7, n - layoutParams.bottomMargin - view.getMeasuredHeight(), n7 + n8, n - layoutParams.bottomMargin);
                    break;
                }
                case 16: {
                    int n10 = n4 - n2;
                    int n11 = (n10 - n9) / 2;
                    if (n11 < layoutParams.topMargin) {
                        n = layoutParams.topMargin;
                    } else {
                        n = n11;
                        if (n11 + n9 > n10 - layoutParams.bottomMargin) {
                            n = n10 - layoutParams.bottomMargin - n9;
                        }
                    }
                    view.layout(n7, n, n7 + n8, n + n9);
                }
            }
            if (bl2) {
                this.setDrawerViewOffset(view, f);
            }
            n = layoutParams.onScreen > 0.0f ? 0 : 4;
            if (view.getVisibility() == n) continue;
            view.setVisibility(n);
        }
        this.mInLayout = false;
        this.mFirstLayout = false;
    }

    protected void onMeasure(int n, int n2) {
        block29: {
            int n3;
            int n4;
            int n5;
            int n6;
            int n7;
            int n8;
            int n9;
            block28: {
                int n10;
                int n11;
                block27: {
                    n9 = View.MeasureSpec.getMode((int)n);
                    n8 = View.MeasureSpec.getMode((int)n2);
                    n7 = View.MeasureSpec.getSize((int)n);
                    n6 = View.MeasureSpec.getSize((int)n2);
                    if (n9 != 0x40000000) break block27;
                    n11 = n9;
                    n10 = n8;
                    n5 = n7;
                    n4 = n6;
                    if (n8 == 0x40000000) break block28;
                }
                if (!this.isInEditMode()) break block29;
                if (n9 == Integer.MIN_VALUE) {
                    n3 = 0x40000000;
                } else {
                    n3 = n9;
                    if (n9 == 0) {
                        n3 = 0x40000000;
                        n7 = 300;
                    }
                }
                if (n8 == Integer.MIN_VALUE) {
                    n10 = 0x40000000;
                    n11 = n3;
                    n5 = n7;
                    n4 = n6;
                } else {
                    n11 = n3;
                    n10 = n8;
                    n5 = n7;
                    n4 = n6;
                    if (n8 == 0) {
                        n10 = 0x40000000;
                        n4 = 300;
                        n5 = n7;
                        n11 = n3;
                    }
                }
            }
            this.setMeasuredDimension(n5, n4);
            n9 = this.mLastInsets != null && ViewCompat.getFitsSystemWindows((View)this) ? 1 : 0;
            int n12 = ViewCompat.getLayoutDirection((View)this);
            n7 = 0;
            n3 = 0;
            int n13 = this.getChildCount();
            for (n6 = 0; n6 < n13; ++n6) {
                StringBuilder stringBuilder;
                View view = this.getChildAt(n6);
                if (view.getVisibility() == 8) continue;
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                if (n9 != 0) {
                    WindowInsets windowInsets;
                    n8 = GravityCompat.getAbsoluteGravity(layoutParams.gravity, n12);
                    if (ViewCompat.getFitsSystemWindows(view)) {
                        if (Build.VERSION.SDK_INT >= 21) {
                            windowInsets = (WindowInsets)this.mLastInsets;
                            if (n8 == 3) {
                                stringBuilder = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
                            } else {
                                stringBuilder = windowInsets;
                                if (n8 == 5) {
                                    stringBuilder = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                                }
                            }
                            view.dispatchApplyWindowInsets((WindowInsets)stringBuilder);
                        }
                    } else if (Build.VERSION.SDK_INT >= 21) {
                        windowInsets = (WindowInsets)this.mLastInsets;
                        if (n8 == 3) {
                            stringBuilder = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), windowInsets.getSystemWindowInsetTop(), 0, windowInsets.getSystemWindowInsetBottom());
                        } else {
                            stringBuilder = windowInsets;
                            if (n8 == 5) {
                                stringBuilder = windowInsets.replaceSystemWindowInsets(0, windowInsets.getSystemWindowInsetTop(), windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
                            }
                        }
                        layoutParams.leftMargin = stringBuilder.getSystemWindowInsetLeft();
                        layoutParams.topMargin = stringBuilder.getSystemWindowInsetTop();
                        layoutParams.rightMargin = stringBuilder.getSystemWindowInsetRight();
                        layoutParams.bottomMargin = stringBuilder.getSystemWindowInsetBottom();
                    }
                }
                if (this.isContentView(view)) {
                    view.measure(View.MeasureSpec.makeMeasureSpec((int)(n5 - layoutParams.leftMargin - layoutParams.rightMargin), (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)(n4 - layoutParams.topMargin - layoutParams.bottomMargin), (int)0x40000000));
                    continue;
                }
                if (this.isDrawerView(view)) {
                    int n14;
                    float f;
                    float f2;
                    if (SET_DRAWER_SHADOW_FROM_ELEVATION && (f2 = ViewCompat.getElevation(view)) != (f = this.mDrawerElevation)) {
                        ViewCompat.setElevation(view, f);
                    }
                    if ((n8 = (n14 = this.getDrawerViewAbsoluteGravity(view) & 7) == 3 ? 1 : 0) != 0 && n7 != 0 || n8 == 0 && n3 != 0) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Child drawer has absolute gravity ");
                        stringBuilder.append(DrawerLayout.gravityToString(n14));
                        stringBuilder.append(" but this ");
                        stringBuilder.append(TAG);
                        stringBuilder.append(" already has a ");
                        stringBuilder.append("drawer view along that edge");
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                    if (n8 != 0) {
                        n7 = 1;
                    } else {
                        n3 = 1;
                    }
                    view.measure(DrawerLayout.getChildMeasureSpec((int)n, (int)(this.mMinDrawerMargin + layoutParams.leftMargin + layoutParams.rightMargin), (int)layoutParams.width), DrawerLayout.getChildMeasureSpec((int)n2, (int)(layoutParams.topMargin + layoutParams.bottomMargin), (int)layoutParams.height));
                    continue;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Child ");
                stringBuilder.append(view);
                stringBuilder.append(" at index ");
                stringBuilder.append(n6);
                stringBuilder.append(" does not have a valid layout_gravity - must be Gravity.LEFT, ");
                stringBuilder.append("Gravity.RIGHT or Gravity.NO_GRAVITY");
                throw new IllegalStateException(stringBuilder.toString());
            }
            return;
        }
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
        throw illegalArgumentException;
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState)parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.openDrawerGravity != 0 && (parcelable = this.findDrawerWithGravity(savedState.openDrawerGravity)) != null) {
            this.openDrawer((View)parcelable);
        }
        if (savedState.lockModeLeft != 3) {
            this.setDrawerLockMode(savedState.lockModeLeft, 3);
        }
        if (savedState.lockModeRight != 3) {
            this.setDrawerLockMode(savedState.lockModeRight, 5);
        }
        if (savedState.lockModeStart != 3) {
            this.setDrawerLockMode(savedState.lockModeStart, 0x800003);
        }
        if (savedState.lockModeEnd != 3) {
            this.setDrawerLockMode(savedState.lockModeEnd, 0x800005);
        }
    }

    public void onRtlPropertiesChanged(int n) {
        this.resolveShadowDrawables();
    }

    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        int n = this.getChildCount();
        for (int i = 0; i < n; ++i) {
            LayoutParams layoutParams = (LayoutParams)this.getChildAt(i).getLayoutParams();
            int n2 = layoutParams.openState;
            boolean bl = false;
            n2 = n2 == 1 ? 1 : 0;
            if (layoutParams.openState == 2) {
                bl = true;
            }
            if (n2 == 0 && !bl) {
                continue;
            }
            savedState.openDrawerGravity = layoutParams.gravity;
            break;
        }
        savedState.lockModeLeft = this.mLockModeLeft;
        savedState.lockModeRight = this.mLockModeRight;
        savedState.lockModeStart = this.mLockModeStart;
        savedState.lockModeEnd = this.mLockModeEnd;
        return savedState;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mLeftDragger.processTouchEvent(motionEvent);
        this.mRightDragger.processTouchEvent(motionEvent);
        int n = motionEvent.getAction();
        boolean bl = true;
        switch (n & 0xFF) {
            default: {
                break;
            }
            case 3: {
                this.closeDrawers(true);
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
                break;
            }
            case 1: {
                float f = motionEvent.getX();
                float f2 = motionEvent.getY();
                boolean bl2 = true;
                motionEvent = this.mLeftDragger.findTopChildUnder((int)f, (int)f2);
                boolean bl3 = bl2;
                if (motionEvent != null) {
                    bl3 = bl2;
                    if (this.isContentView((View)motionEvent)) {
                        f -= this.mInitialMotionX;
                        f2 -= this.mInitialMotionY;
                        n = this.mLeftDragger.getTouchSlop();
                        bl3 = bl2;
                        if (f * f + f2 * f2 < (float)(n * n)) {
                            motionEvent = this.findOpenDrawer();
                            bl3 = bl2;
                            if (motionEvent != null) {
                                bl3 = this.getDrawerLockMode((View)motionEvent) == 2 ? bl : false;
                            }
                        }
                    }
                }
                this.closeDrawers(bl3);
                this.mDisallowInterceptRequested = false;
                break;
            }
            case 0: {
                float f = motionEvent.getX();
                float f3 = motionEvent.getY();
                this.mInitialMotionX = f;
                this.mInitialMotionY = f3;
                this.mDisallowInterceptRequested = false;
                this.mChildrenCanceledTouch = false;
            }
        }
        return true;
    }

    public void openDrawer(int n) {
        this.openDrawer(n, true);
    }

    public void openDrawer(int n, boolean bl) {
        Object object = this.findDrawerWithGravity(n);
        if (object != null) {
            this.openDrawer((View)object, bl);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("No drawer view found with gravity ");
        ((StringBuilder)object).append(DrawerLayout.gravityToString(n));
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public void openDrawer(View view) {
        this.openDrawer(view, true);
    }

    public void openDrawer(View view, boolean bl) {
        if (this.isDrawerView(view)) {
            LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
            if (this.mFirstLayout) {
                layoutParams.onScreen = 1.0f;
                layoutParams.openState = 1;
                this.updateChildrenImportantForAccessibility(view, true);
            } else if (bl) {
                layoutParams.openState |= 2;
                if (this.checkDrawerViewAbsoluteGravity(view, 3)) {
                    this.mLeftDragger.smoothSlideViewTo(view, 0, view.getTop());
                } else {
                    this.mRightDragger.smoothSlideViewTo(view, this.getWidth() - view.getWidth(), view.getTop());
                }
            } else {
                this.moveDrawerToOffset(view, 1.0f);
                this.updateDrawerState(layoutParams.gravity, 0, view);
                view.setVisibility(0);
            }
            this.invalidate();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append(view);
        stringBuilder.append(" is not a sliding drawer");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void removeDrawerListener(DrawerListener drawerListener) {
        if (drawerListener == null) {
            return;
        }
        List<DrawerListener> list = this.mListeners;
        if (list == null) {
            return;
        }
        list.remove(drawerListener);
    }

    public void requestDisallowInterceptTouchEvent(boolean bl) {
        super.requestDisallowInterceptTouchEvent(bl);
        this.mDisallowInterceptRequested = bl;
        if (bl) {
            this.closeDrawers(true);
        }
    }

    public void requestLayout() {
        if (!this.mInLayout) {
            super.requestLayout();
        }
    }

    public void setChildInsets(Object object, boolean bl) {
        this.mLastInsets = object;
        this.mDrawStatusBarBackground = bl;
        bl = !bl && this.getBackground() == null;
        this.setWillNotDraw(bl);
        this.requestLayout();
    }

    public void setDrawerElevation(float f) {
        this.mDrawerElevation = f;
        for (int i = 0; i < this.getChildCount(); ++i) {
            View view = this.getChildAt(i);
            if (!this.isDrawerView(view)) continue;
            ViewCompat.setElevation(view, this.mDrawerElevation);
        }
    }

    @Deprecated
    public void setDrawerListener(DrawerListener drawerListener) {
        DrawerListener drawerListener2 = this.mListener;
        if (drawerListener2 != null) {
            this.removeDrawerListener(drawerListener2);
        }
        if (drawerListener != null) {
            this.addDrawerListener(drawerListener);
        }
        this.mListener = drawerListener;
    }

    public void setDrawerLockMode(int n) {
        this.setDrawerLockMode(n, 3);
        this.setDrawerLockMode(n, 5);
    }

    public void setDrawerLockMode(int n, int n2) {
        ViewDragHelper viewDragHelper;
        int n3 = GravityCompat.getAbsoluteGravity(n2, ViewCompat.getLayoutDirection((View)this));
        switch (n2) {
            default: {
                break;
            }
            case 0x800005: {
                this.mLockModeEnd = n;
                break;
            }
            case 0x800003: {
                this.mLockModeStart = n;
                break;
            }
            case 5: {
                this.mLockModeRight = n;
                break;
            }
            case 3: {
                this.mLockModeLeft = n;
            }
        }
        if (n != 0) {
            viewDragHelper = n3 == 3 ? this.mLeftDragger : this.mRightDragger;
            viewDragHelper.cancel();
        }
        switch (n) {
            default: {
                break;
            }
            case 2: {
                viewDragHelper = this.findDrawerWithGravity(n3);
                if (viewDragHelper == null) break;
                this.openDrawer((View)viewDragHelper);
                break;
            }
            case 1: {
                viewDragHelper = this.findDrawerWithGravity(n3);
                if (viewDragHelper == null) break;
                this.closeDrawer((View)viewDragHelper);
            }
        }
    }

    public void setDrawerLockMode(int n, View view) {
        if (this.isDrawerView(view)) {
            this.setDrawerLockMode(n, ((LayoutParams)view.getLayoutParams()).gravity);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View ");
        stringBuilder.append(view);
        stringBuilder.append(" is not a ");
        stringBuilder.append("drawer with appropriate layout_gravity");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void setDrawerShadow(int n, int n2) {
        this.setDrawerShadow(ContextCompat.getDrawable(this.getContext(), n), n2);
    }

    public void setDrawerShadow(Drawable drawable2, int n) {
        block8: {
            block5: {
                block7: {
                    block6: {
                        block4: {
                            if (SET_DRAWER_SHADOW_FROM_ELEVATION) {
                                return;
                            }
                            if ((n & 0x800003) != 0x800003) break block4;
                            this.mShadowStart = drawable2;
                            break block5;
                        }
                        if ((n & 0x800005) != 0x800005) break block6;
                        this.mShadowEnd = drawable2;
                        break block5;
                    }
                    if ((n & 3) != 3) break block7;
                    this.mShadowLeft = drawable2;
                    break block5;
                }
                if ((n & 5) != 5) break block8;
                this.mShadowRight = drawable2;
            }
            this.resolveShadowDrawables();
            this.invalidate();
            return;
        }
    }

    public void setDrawerTitle(int n, CharSequence charSequence) {
        if ((n = GravityCompat.getAbsoluteGravity(n, ViewCompat.getLayoutDirection((View)this))) == 3) {
            this.mTitleLeft = charSequence;
        } else if (n == 5) {
            this.mTitleRight = charSequence;
        }
    }

    void setDrawerViewOffset(View view, float f) {
        LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
        if (f == layoutParams.onScreen) {
            return;
        }
        layoutParams.onScreen = f;
        this.dispatchOnDrawerSlide(view, f);
    }

    public void setScrimColor(int n) {
        this.mScrimColor = n;
        this.invalidate();
    }

    public void setStatusBarBackground(int n) {
        Drawable drawable2 = n != 0 ? ContextCompat.getDrawable(this.getContext(), n) : null;
        this.mStatusBarBackground = drawable2;
        this.invalidate();
    }

    public void setStatusBarBackground(Drawable drawable2) {
        this.mStatusBarBackground = drawable2;
        this.invalidate();
    }

    public void setStatusBarBackgroundColor(int n) {
        this.mStatusBarBackground = new ColorDrawable(n);
        this.invalidate();
    }

    void updateDrawerState(int n, int n2, View object) {
        n = this.mLeftDragger.getViewDragState();
        int n3 = this.mRightDragger.getViewDragState();
        n = n != 1 && n3 != 1 ? (n != 2 && n3 != 2 ? 0 : 2) : 1;
        if (object != null && n2 == 0) {
            LayoutParams layoutParams = (LayoutParams)object.getLayoutParams();
            if (layoutParams.onScreen == 0.0f) {
                this.dispatchOnDrawerClosed((View)object);
            } else if (layoutParams.onScreen == 1.0f) {
                this.dispatchOnDrawerOpened((View)object);
            }
        }
        if (n != this.mDrawerState) {
            this.mDrawerState = n;
            object = this.mListeners;
            if (object != null) {
                for (n2 = object.size() - 1; n2 >= 0; --n2) {
                    this.mListeners.get(n2).onDrawerStateChanged(n);
                }
            }
        }
    }

    class AccessibilityDelegate
    extends AccessibilityDelegateCompat {
        private final Rect mTmpRect;
        final DrawerLayout this$0;

        AccessibilityDelegate(DrawerLayout drawerLayout) {
            this.this$0 = drawerLayout;
            this.mTmpRect = new Rect();
        }

        private void addChildrenForAccessibility(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, ViewGroup viewGroup) {
            int n = viewGroup.getChildCount();
            for (int i = 0; i < n; ++i) {
                View view = viewGroup.getChildAt(i);
                if (!DrawerLayout.includeChildForAccessibility(view)) continue;
                accessibilityNodeInfoCompat.addChild(view);
            }
        }

        private void copyNodeInfoNoChildren(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2) {
            Rect rect = this.mTmpRect;
            accessibilityNodeInfoCompat2.getBoundsInParent(rect);
            accessibilityNodeInfoCompat.setBoundsInParent(rect);
            accessibilityNodeInfoCompat2.getBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setBoundsInScreen(rect);
            accessibilityNodeInfoCompat.setVisibleToUser(accessibilityNodeInfoCompat2.isVisibleToUser());
            accessibilityNodeInfoCompat.setPackageName(accessibilityNodeInfoCompat2.getPackageName());
            accessibilityNodeInfoCompat.setClassName(accessibilityNodeInfoCompat2.getClassName());
            accessibilityNodeInfoCompat.setContentDescription(accessibilityNodeInfoCompat2.getContentDescription());
            accessibilityNodeInfoCompat.setEnabled(accessibilityNodeInfoCompat2.isEnabled());
            accessibilityNodeInfoCompat.setClickable(accessibilityNodeInfoCompat2.isClickable());
            accessibilityNodeInfoCompat.setFocusable(accessibilityNodeInfoCompat2.isFocusable());
            accessibilityNodeInfoCompat.setFocused(accessibilityNodeInfoCompat2.isFocused());
            accessibilityNodeInfoCompat.setAccessibilityFocused(accessibilityNodeInfoCompat2.isAccessibilityFocused());
            accessibilityNodeInfoCompat.setSelected(accessibilityNodeInfoCompat2.isSelected());
            accessibilityNodeInfoCompat.setLongClickable(accessibilityNodeInfoCompat2.isLongClickable());
            accessibilityNodeInfoCompat.addAction(accessibilityNodeInfoCompat2.getActions());
        }

        @Override
        public boolean dispatchPopulateAccessibilityEvent(View object, AccessibilityEvent object2) {
            if (object2.getEventType() == 32) {
                int n;
                object = object2.getText();
                object2 = this.this$0.findVisibleDrawer();
                if (object2 != null && (object2 = this.this$0.getDrawerTitle(n = this.this$0.getDrawerViewAbsoluteGravity((View)object2))) != null) {
                    object.add(object2);
                }
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent((View)object, (AccessibilityEvent)object2);
        }

        @Override
        public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            super.onInitializeAccessibilityEvent(view, accessibilityEvent);
            accessibilityEvent.setClassName((CharSequence)DrawerLayout.class.getName());
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            if (CAN_HIDE_DESCENDANTS) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            } else {
                AccessibilityNodeInfoCompat accessibilityNodeInfoCompat2 = AccessibilityNodeInfoCompat.obtain(accessibilityNodeInfoCompat);
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat2);
                accessibilityNodeInfoCompat.setSource(view);
                ViewParent viewParent = ViewCompat.getParentForAccessibility(view);
                if (viewParent instanceof View) {
                    accessibilityNodeInfoCompat.setParent((View)viewParent);
                }
                this.copyNodeInfoNoChildren(accessibilityNodeInfoCompat, accessibilityNodeInfoCompat2);
                accessibilityNodeInfoCompat2.recycle();
                this.addChildrenForAccessibility(accessibilityNodeInfoCompat, (ViewGroup)view);
            }
            accessibilityNodeInfoCompat.setClassName(DrawerLayout.class.getName());
            accessibilityNodeInfoCompat.setFocusable(false);
            accessibilityNodeInfoCompat.setFocused(false);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            accessibilityNodeInfoCompat.removeAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        @Override
        public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            if (!CAN_HIDE_DESCENDANTS && !DrawerLayout.includeChildForAccessibility(view)) {
                return false;
            }
            return super.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
        }
    }

    static final class ChildAccessibilityDelegate
    extends AccessibilityDelegateCompat {
        ChildAccessibilityDelegate() {
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (!DrawerLayout.includeChildForAccessibility(view)) {
                accessibilityNodeInfoCompat.setParent(null);
            }
        }
    }

    public static interface DrawerListener {
        public void onDrawerClosed(View var1);

        public void onDrawerOpened(View var1);

        public void onDrawerSlide(View var1, float var2);

        public void onDrawerStateChanged(int var1);
    }

    public static class LayoutParams
    extends ViewGroup.MarginLayoutParams {
        private static final int FLAG_IS_CLOSING = 4;
        private static final int FLAG_IS_OPENED = 1;
        private static final int FLAG_IS_OPENING = 2;
        public int gravity = 0;
        boolean isPeeking;
        float onScreen;
        int openState;

        public LayoutParams(int n, int n2) {
            super(n, n2);
        }

        public LayoutParams(int n, int n2, int n3) {
            this(n, n2);
            this.gravity = n3;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            context = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
            this.gravity = context.getInt(0, 0);
            context.recycle();
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((ViewGroup.MarginLayoutParams)layoutParams);
            this.gravity = layoutParams.gravity;
        }
    }

    protected static class SavedState
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
        int lockModeEnd;
        int lockModeLeft;
        int lockModeRight;
        int lockModeStart;
        int openDrawerGravity = 0;

        public SavedState(Parcel parcel, ClassLoader classLoader) {
            super(parcel, classLoader);
            this.openDrawerGravity = parcel.readInt();
            this.lockModeLeft = parcel.readInt();
            this.lockModeRight = parcel.readInt();
            this.lockModeStart = parcel.readInt();
            this.lockModeEnd = parcel.readInt();
        }

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override
        public void writeToParcel(Parcel parcel, int n) {
            super.writeToParcel(parcel, n);
            parcel.writeInt(this.openDrawerGravity);
            parcel.writeInt(this.lockModeLeft);
            parcel.writeInt(this.lockModeRight);
            parcel.writeInt(this.lockModeStart);
            parcel.writeInt(this.lockModeEnd);
        }
    }

    public static abstract class SimpleDrawerListener
    implements DrawerListener {
        @Override
        public void onDrawerClosed(View view) {
        }

        @Override
        public void onDrawerOpened(View view) {
        }

        @Override
        public void onDrawerSlide(View view, float f) {
        }

        @Override
        public void onDrawerStateChanged(int n) {
        }
    }

    private class ViewDragCallback
    extends ViewDragHelper.Callback {
        private final int mAbsGravity;
        private ViewDragHelper mDragger;
        private final Runnable mPeekRunnable;
        final DrawerLayout this$0;

        ViewDragCallback(DrawerLayout drawerLayout, int n) {
            this.this$0 = drawerLayout;
            this.mPeekRunnable = new Runnable(this){
                final ViewDragCallback this$1;
                {
                    this.this$1 = viewDragCallback;
                }

                @Override
                public void run() {
                    this.this$1.peekDrawer();
                }
            };
            this.mAbsGravity = n;
        }

        private void closeOtherDrawer() {
            View view;
            int n = this.mAbsGravity;
            int n2 = 3;
            if (n == 3) {
                n2 = 5;
            }
            if ((view = this.this$0.findDrawerWithGravity(n2)) != null) {
                this.this$0.closeDrawer(view);
            }
        }

        @Override
        public int clampViewPositionHorizontal(View view, int n, int n2) {
            if (this.this$0.checkDrawerViewAbsoluteGravity(view, 3)) {
                return Math.max(-view.getWidth(), Math.min(n, 0));
            }
            n2 = this.this$0.getWidth();
            return Math.max(n2 - view.getWidth(), Math.min(n, n2));
        }

        @Override
        public int clampViewPositionVertical(View view, int n, int n2) {
            return view.getTop();
        }

        @Override
        public int getViewHorizontalDragRange(View view) {
            int n = this.this$0.isDrawerView(view) ? view.getWidth() : 0;
            return n;
        }

        @Override
        public void onEdgeDragStarted(int n, int n2) {
            View view = (n & 1) == 1 ? this.this$0.findDrawerWithGravity(3) : this.this$0.findDrawerWithGravity(5);
            if (view != null && this.this$0.getDrawerLockMode(view) == 0) {
                this.mDragger.captureChildView(view, n2);
            }
        }

        @Override
        public boolean onEdgeLock(int n) {
            return false;
        }

        @Override
        public void onEdgeTouched(int n, int n2) {
            this.this$0.postDelayed(this.mPeekRunnable, 160L);
        }

        @Override
        public void onViewCaptured(View view, int n) {
            ((LayoutParams)view.getLayoutParams()).isPeeking = false;
            this.closeOtherDrawer();
        }

        @Override
        public void onViewDragStateChanged(int n) {
            this.this$0.updateDrawerState(this.mAbsGravity, n, this.mDragger.getCapturedView());
        }

        @Override
        public void onViewPositionChanged(View view, int n, int n2, int n3, int n4) {
            n2 = view.getWidth();
            float f = this.this$0.checkDrawerViewAbsoluteGravity(view, 3) ? (float)(n2 + n) / (float)n2 : (float)(this.this$0.getWidth() - n) / (float)n2;
            this.this$0.setDrawerViewOffset(view, f);
            n = f == 0.0f ? 4 : 0;
            view.setVisibility(n);
            this.this$0.invalidate();
        }

        @Override
        public void onViewReleased(View view, float f, float f2) {
            int n;
            f2 = this.this$0.getDrawerViewOffset(view);
            int n2 = view.getWidth();
            if (this.this$0.checkDrawerViewAbsoluteGravity(view, 3)) {
                n = !(f > 0.0f || f == 0.0f && f2 > 0.5f) ? -n2 : 0;
            } else {
                n = this.this$0.getWidth();
                if (f < 0.0f || f == 0.0f && f2 > 0.5f) {
                    n -= n2;
                }
            }
            this.mDragger.settleCapturedViewAt(n, view.getTop());
            this.this$0.invalidate();
        }

        void peekDrawer() {
            View view;
            int n = this.mDragger.getEdgeSize();
            int n2 = this.mAbsGravity;
            int n3 = 0;
            if ((n2 = n2 == 3 ? 1 : 0) != 0) {
                view = this.this$0.findDrawerWithGravity(3);
                if (view != null) {
                    n3 = -view.getWidth();
                }
                n3 += n;
            } else {
                view = this.this$0.findDrawerWithGravity(5);
                n3 = this.this$0.getWidth() - n;
            }
            if (view != null && (n2 != 0 && view.getLeft() < n3 || n2 == 0 && view.getLeft() > n3) && this.this$0.getDrawerLockMode(view) == 0) {
                LayoutParams layoutParams = (LayoutParams)view.getLayoutParams();
                this.mDragger.smoothSlideViewTo(view, n3, view.getTop());
                layoutParams.isPeeking = true;
                this.this$0.invalidate();
                this.closeOtherDrawer();
                this.this$0.cancelChildViewTouch();
            }
        }

        public void removeCallbacks() {
            this.this$0.removeCallbacks(this.mPeekRunnable);
        }

        public void setDragger(ViewDragHelper viewDragHelper) {
            this.mDragger = viewDragHelper;
        }

        @Override
        public boolean tryCaptureView(View view, int n) {
            boolean bl = this.this$0.isDrawerView(view) && this.this$0.checkDrawerViewAbsoluteGravity(view, this.mAbsGravity) && this.this$0.getDrawerLockMode(view) == 0;
            return bl;
        }
    }
}

