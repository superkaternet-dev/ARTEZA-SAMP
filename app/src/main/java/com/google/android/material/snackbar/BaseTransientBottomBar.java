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
 *  android.content.res.TypedArray
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.ViewGroup$MarginLayoutParams
 *  android.view.ViewParent
 *  android.view.accessibility.AccessibilityManager
 *  android.widget.FrameLayout
 */
package com.google.android.material.snackbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.accessibility.AccessibilityManagerCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.google.android.material.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.behavior.SwipeDismissBehavior;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.snackbar.SnackbarManager;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTransientBottomBar<B extends BaseTransientBottomBar<B>> {
    static final int ANIMATION_DURATION = 250;
    static final int ANIMATION_FADE_DURATION = 180;
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    static final int MSG_DISMISS = 1;
    static final int MSG_SHOW = 0;
    private static final int[] SNACKBAR_STYLE_ATTR;
    private static final boolean USE_OFFSET_API;
    static final Handler handler;
    private final AccessibilityManager accessibilityManager;
    private Behavior behavior;
    private List<BaseCallback<B>> callbacks;
    private final com.google.android.material.snackbar.ContentViewCallback contentViewCallback;
    private final Context context;
    private int duration;
    final SnackbarManager.Callback managerCallback = new SnackbarManager.Callback(this){
        final BaseTransientBottomBar this$0;
        {
            this.this$0 = baseTransientBottomBar;
        }

        @Override
        public void dismiss(int n) {
            handler.sendMessage(handler.obtainMessage(1, n, 0, (Object)this.this$0));
        }

        @Override
        public void show() {
            handler.sendMessage(handler.obtainMessage(0, (Object)this.this$0));
        }
    };
    private final ViewGroup targetParent;
    protected final SnackbarBaseLayout view;

    static {
        boolean bl = Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT <= 19;
        USE_OFFSET_API = bl;
        SNACKBAR_STYLE_ATTR = new int[]{R.attr.snackbarStyle};
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback(){

            public boolean handleMessage(Message message) {
                switch (message.what) {
                    default: {
                        return false;
                    }
                    case 1: {
                        ((BaseTransientBottomBar)message.obj).hideView(message.arg1);
                        return true;
                    }
                    case 0: 
                }
                ((BaseTransientBottomBar)message.obj).showView();
                return true;
            }
        });
    }

    protected BaseTransientBottomBar(ViewGroup object, View view, com.google.android.material.snackbar.ContentViewCallback contentViewCallback) {
        if (object != null) {
            if (view != null) {
                if (contentViewCallback != null) {
                    this.targetParent = object;
                    this.contentViewCallback = contentViewCallback;
                    contentViewCallback = object.getContext();
                    this.context = contentViewCallback;
                    ThemeEnforcement.checkAppCompatTheme((Context)contentViewCallback);
                    object = (SnackbarBaseLayout)LayoutInflater.from((Context)contentViewCallback).inflate(this.getSnackbarBaseLayoutResId(), object, false);
                    this.view = object;
                    object.addView(view);
                    ViewCompat.setAccessibilityLiveRegion((View)object, 1);
                    ViewCompat.setImportantForAccessibility((View)object, 1);
                    ViewCompat.setFitsSystemWindows((View)object, true);
                    ViewCompat.setOnApplyWindowInsetsListener((View)object, new OnApplyWindowInsetsListener(this){
                        final BaseTransientBottomBar this$0;
                        {
                            this.this$0 = baseTransientBottomBar;
                        }

                        @Override
                        public WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), windowInsetsCompat.getSystemWindowInsetBottom());
                            return windowInsetsCompat;
                        }
                    });
                    ViewCompat.setAccessibilityDelegate((View)object, new AccessibilityDelegateCompat(this){
                        final BaseTransientBottomBar this$0;
                        {
                            this.this$0 = baseTransientBottomBar;
                        }

                        @Override
                        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
                            accessibilityNodeInfoCompat.addAction(0x100000);
                            accessibilityNodeInfoCompat.setDismissable(true);
                        }

                        @Override
                        public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
                            if (n == 0x100000) {
                                this.this$0.dismiss();
                                return true;
                            }
                            return super.performAccessibilityAction(view, n, bundle);
                        }
                    });
                    this.accessibilityManager = (AccessibilityManager)contentViewCallback.getSystemService("accessibility");
                    return;
                }
                throw new IllegalArgumentException("Transient bottom bar must have non-null callback");
            }
            throw new IllegalArgumentException("Transient bottom bar must have non-null content");
        }
        throw new IllegalArgumentException("Transient bottom bar must have non-null parent");
    }

    private void animateViewOut(int n) {
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[]{0, this.getTranslationYBottom()});
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, n){
            final BaseTransientBottomBar this$0;
            final int val$event;
            {
                this.this$0 = baseTransientBottomBar;
                this.val$event = n;
            }

            public void onAnimationEnd(Animator animator2) {
                this.this$0.onViewHidden(this.val$event);
            }

            public void onAnimationStart(Animator animator2) {
                this.this$0.contentViewCallback.animateContentOut(0, 180);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this){
            private int previousAnimatedIntValue;
            final BaseTransientBottomBar this$0;
            {
                this.this$0 = baseTransientBottomBar;
                this.previousAnimatedIntValue = 0;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int n = (Integer)valueAnimator.getAnimatedValue();
                if (USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom((View)this.this$0.view, n - this.previousAnimatedIntValue);
                } else {
                    this.this$0.view.setTranslationY(n);
                }
                this.previousAnimatedIntValue = n;
            }
        });
        valueAnimator.start();
    }

    private int getTranslationYBottom() {
        int n = this.view.getHeight();
        ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
        int n2 = n;
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            n2 = n + ((ViewGroup.MarginLayoutParams)layoutParams).bottomMargin;
        }
        return n2;
    }

    public B addCallback(BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return (B)this;
        }
        if (this.callbacks == null) {
            this.callbacks = new ArrayList<BaseCallback<B>>();
        }
        this.callbacks.add(baseCallback);
        return (B)this;
    }

    void animateViewIn() {
        int n = this.getTranslationYBottom();
        if (USE_OFFSET_API) {
            ViewCompat.offsetTopAndBottom((View)this.view, n);
        } else {
            this.view.setTranslationY(n);
        }
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(new int[]{n, 0});
        valueAnimator.setInterpolator(AnimationUtils.FAST_OUT_SLOW_IN_INTERPOLATOR);
        valueAnimator.setDuration(250L);
        valueAnimator.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this){
            final BaseTransientBottomBar this$0;
            {
                this.this$0 = baseTransientBottomBar;
            }

            public void onAnimationEnd(Animator animator2) {
                this.this$0.onViewShown();
            }

            public void onAnimationStart(Animator animator2) {
                this.this$0.contentViewCallback.animateContentIn(70, 180);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this, n){
            private int previousAnimatedIntValue;
            final BaseTransientBottomBar this$0;
            final int val$translationYBottom;
            {
                this.this$0 = baseTransientBottomBar;
                this.val$translationYBottom = n;
                this.previousAnimatedIntValue = n;
            }

            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int n = (Integer)valueAnimator.getAnimatedValue();
                if (USE_OFFSET_API) {
                    ViewCompat.offsetTopAndBottom((View)this.this$0.view, n - this.previousAnimatedIntValue);
                } else {
                    this.this$0.view.setTranslationY(n);
                }
                this.previousAnimatedIntValue = n;
            }
        });
        valueAnimator.start();
    }

    public void dismiss() {
        this.dispatchDismiss(3);
    }

    protected void dispatchDismiss(int n) {
        SnackbarManager.getInstance().dismiss(this.managerCallback, n);
    }

    public Behavior getBehavior() {
        return this.behavior;
    }

    public Context getContext() {
        return this.context;
    }

    public int getDuration() {
        return this.duration;
    }

    protected SwipeDismissBehavior<? extends View> getNewBehavior() {
        return new Behavior();
    }

    protected int getSnackbarBaseLayoutResId() {
        int n = this.hasSnackbarStyleAttr() ? R.layout.mtrl_layout_snackbar : R.layout.design_layout_snackbar;
        return n;
    }

    public View getView() {
        return this.view;
    }

    protected boolean hasSnackbarStyleAttr() {
        TypedArray typedArray = this.context.obtainStyledAttributes(SNACKBAR_STYLE_ATTR);
        boolean bl = false;
        int n = typedArray.getResourceId(0, -1);
        typedArray.recycle();
        if (n != -1) {
            bl = true;
        }
        return bl;
    }

    final void hideView(int n) {
        if (this.shouldAnimate() && this.view.getVisibility() == 0) {
            this.animateViewOut(n);
        } else {
            this.onViewHidden(n);
        }
    }

    public boolean isShown() {
        return SnackbarManager.getInstance().isCurrent(this.managerCallback);
    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(this.managerCallback);
    }

    void onViewHidden(int n) {
        SnackbarManager.getInstance().onDismissed(this.managerCallback);
        ViewParent viewParent = this.callbacks;
        if (viewParent != null) {
            for (int i = viewParent.size() - 1; i >= 0; --i) {
                this.callbacks.get(i).onDismissed(this, n);
            }
        }
        if ((viewParent = this.view.getParent()) instanceof ViewGroup) {
            ((ViewGroup)viewParent).removeView((View)this.view);
        }
    }

    void onViewShown() {
        SnackbarManager.getInstance().onShown(this.managerCallback);
        List<BaseCallback<B>> list = this.callbacks;
        if (list != null) {
            for (int i = list.size() - 1; i >= 0; --i) {
                this.callbacks.get(i).onShown(this);
            }
        }
    }

    public B removeCallback(BaseCallback<B> baseCallback) {
        if (baseCallback == null) {
            return (B)this;
        }
        List<BaseCallback<B>> list = this.callbacks;
        if (list == null) {
            return (B)this;
        }
        list.remove(baseCallback);
        return (B)this;
    }

    public B setBehavior(Behavior behavior) {
        this.behavior = behavior;
        return (B)this;
    }

    public B setDuration(int n) {
        this.duration = n;
        return (B)this;
    }

    boolean shouldAnimate() {
        Object object = this.accessibilityManager;
        boolean bl = true;
        if ((object = object.getEnabledAccessibilityServiceList(1)) == null || !object.isEmpty()) {
            bl = false;
        }
        return bl;
    }

    public void show() {
        SnackbarManager.getInstance().show(this.getDuration(), this.managerCallback);
    }

    final void showView() {
        if (this.view.getParent() == null) {
            Object object = this.view.getLayoutParams();
            if (object instanceof CoordinatorLayout.LayoutParams) {
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)((Object)object);
                object = this.behavior;
                if (object == null) {
                    object = this.getNewBehavior();
                }
                if (object instanceof Behavior) {
                    ((Behavior)object).setBaseTransientBottomBar(this);
                }
                ((SwipeDismissBehavior)object).setListener(new SwipeDismissBehavior.OnDismissListener(this){
                    final BaseTransientBottomBar this$0;
                    {
                        this.this$0 = baseTransientBottomBar;
                    }

                    @Override
                    public void onDismiss(View view) {
                        view.setVisibility(8);
                        this.this$0.dispatchDismiss(0);
                    }

                    @Override
                    public void onDragStateChanged(int n) {
                        switch (n) {
                            default: {
                                break;
                            }
                            case 1: 
                            case 2: {
                                SnackbarManager.getInstance().pauseTimeout(this.this$0.managerCallback);
                                break;
                            }
                            case 0: {
                                SnackbarManager.getInstance().restoreTimeoutIfPaused(this.this$0.managerCallback);
                            }
                        }
                    }
                });
                layoutParams.setBehavior((CoordinatorLayout.Behavior)object);
                layoutParams.insetEdge = 80;
            }
            this.targetParent.addView((View)this.view);
        }
        this.view.setOnAttachStateChangeListener(new OnAttachStateChangeListener(this){
            final BaseTransientBottomBar this$0;
            {
                this.this$0 = baseTransientBottomBar;
            }

            @Override
            public void onViewAttachedToWindow(View view) {
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
                if (this.this$0.isShownOrQueued()) {
                    handler.post(new Runnable(this){
                        final 6 this$1;
                        {
                            this.this$1 = var1_1;
                        }

                        @Override
                        public void run() {
                            this.this$1.this$0.onViewHidden(3);
                        }
                    });
                }
            }
        });
        if (ViewCompat.isLaidOut((View)this.view)) {
            if (this.shouldAnimate()) {
                this.animateViewIn();
            } else {
                this.onViewShown();
            }
        } else {
            this.view.setOnLayoutChangeListener(new OnLayoutChangeListener(this){
                final BaseTransientBottomBar this$0;
                {
                    this.this$0 = baseTransientBottomBar;
                }

                @Override
                public void onLayoutChange(View view, int n, int n2, int n3, int n4) {
                    this.this$0.view.setOnLayoutChangeListener(null);
                    if (this.this$0.shouldAnimate()) {
                        this.this$0.animateViewIn();
                    } else {
                        this.this$0.onViewShown();
                    }
                }
            });
        }
    }

    public static abstract class BaseCallback<B> {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        public void onDismissed(B b, int n) {
        }

        public void onShown(B b) {
        }

        @Retention(value=RetentionPolicy.SOURCE)
        public static @interface DismissEvent {
        }
    }

    public static class Behavior
    extends SwipeDismissBehavior<View> {
        private final BehaviorDelegate delegate = new BehaviorDelegate(this);

        private void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.delegate.setBaseTransientBottomBar(baseTransientBottomBar);
        }

        @Override
        public boolean canSwipeDismissView(View view) {
            return this.delegate.canSwipeDismissView(view);
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            this.delegate.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
            return super.onInterceptTouchEvent(coordinatorLayout, view, motionEvent);
        }
    }

    public static class BehaviorDelegate {
        private SnackbarManager.Callback managerCallback;

        public BehaviorDelegate(SwipeDismissBehavior<?> swipeDismissBehavior) {
            swipeDismissBehavior.setStartAlphaSwipeDistance(0.1f);
            swipeDismissBehavior.setEndAlphaSwipeDistance(0.6f);
            swipeDismissBehavior.setSwipeDirection(0);
        }

        public boolean canSwipeDismissView(View view) {
            return view instanceof SnackbarBaseLayout;
        }

        public void onInterceptTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
            switch (motionEvent.getActionMasked()) {
                default: {
                    break;
                }
                case 1: 
                case 3: {
                    SnackbarManager.getInstance().restoreTimeoutIfPaused(this.managerCallback);
                    break;
                }
                case 0: {
                    if (!coordinatorLayout.isPointInChildBounds(view, (int)motionEvent.getX(), (int)motionEvent.getY())) break;
                    SnackbarManager.getInstance().pauseTimeout(this.managerCallback);
                }
            }
        }

        public void setBaseTransientBottomBar(BaseTransientBottomBar<?> baseTransientBottomBar) {
            this.managerCallback = baseTransientBottomBar.managerCallback;
        }
    }

    @Deprecated
    public static interface ContentViewCallback
    extends com.google.android.material.snackbar.ContentViewCallback {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Duration {
    }

    protected static interface OnAttachStateChangeListener {
        public void onViewAttachedToWindow(View var1);

        public void onViewDetachedFromWindow(View var1);
    }

    protected static interface OnLayoutChangeListener {
        public void onLayoutChange(View var1, int var2, int var3, int var4, int var5);
    }

    protected static class SnackbarBaseLayout
    extends FrameLayout {
        private final AccessibilityManager accessibilityManager;
        private OnAttachStateChangeListener onAttachStateChangeListener;
        private OnLayoutChangeListener onLayoutChangeListener;
        private final AccessibilityManagerCompat.TouchExplorationStateChangeListener touchExplorationStateChangeListener;

        protected SnackbarBaseLayout(Context context) {
            this(context, null);
        }

        protected SnackbarBaseLayout(Context object, AttributeSet attributeSet) {
            super(object, attributeSet);
            attributeSet = object.obtainStyledAttributes(attributeSet, R.styleable.SnackbarLayout);
            if (attributeSet.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation((View)this, attributeSet.getDimensionPixelSize(R.styleable.SnackbarLayout_elevation, 0));
            }
            attributeSet.recycle();
            attributeSet = (AccessibilityManager)object.getSystemService("accessibility");
            this.accessibilityManager = attributeSet;
            object = new AccessibilityManagerCompat.TouchExplorationStateChangeListener(this){
                final SnackbarBaseLayout this$0;
                {
                    this.this$0 = snackbarBaseLayout;
                }

                @Override
                public void onTouchExplorationStateChanged(boolean bl) {
                    this.this$0.setClickableOrFocusableBasedOnAccessibility(bl);
                }
            };
            this.touchExplorationStateChangeListener = object;
            AccessibilityManagerCompat.addTouchExplorationStateChangeListener((AccessibilityManager)attributeSet, (AccessibilityManagerCompat.TouchExplorationStateChangeListener)object);
            this.setClickableOrFocusableBasedOnAccessibility(attributeSet.isTouchExplorationEnabled());
        }

        private void setClickableOrFocusableBasedOnAccessibility(boolean bl) {
            this.setClickable(bl ^ true);
            this.setFocusable(bl);
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            OnAttachStateChangeListener onAttachStateChangeListener = this.onAttachStateChangeListener;
            if (onAttachStateChangeListener != null) {
                onAttachStateChangeListener.onViewAttachedToWindow((View)this);
            }
            ViewCompat.requestApplyInsets((View)this);
        }

        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            OnAttachStateChangeListener onAttachStateChangeListener = this.onAttachStateChangeListener;
            if (onAttachStateChangeListener != null) {
                onAttachStateChangeListener.onViewDetachedFromWindow((View)this);
            }
            AccessibilityManagerCompat.removeTouchExplorationStateChangeListener(this.accessibilityManager, this.touchExplorationStateChangeListener);
        }

        protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
            super.onLayout(bl, n, n2, n3, n4);
            OnLayoutChangeListener onLayoutChangeListener = this.onLayoutChangeListener;
            if (onLayoutChangeListener != null) {
                onLayoutChangeListener.onLayoutChange((View)this, n, n2, n3, n4);
            }
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener onAttachStateChangeListener) {
            this.onAttachStateChangeListener = onAttachStateChangeListener;
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            this.onLayoutChangeListener = onLayoutChangeListener;
        }
    }
}

