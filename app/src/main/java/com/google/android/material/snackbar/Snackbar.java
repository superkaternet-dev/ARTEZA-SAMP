/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$MeasureSpec
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.accessibility.AccessibilityManager
 *  android.widget.Button
 *  android.widget.FrameLayout
 */
package com.google.android.material.snackbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.R;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.ContentViewCallback;
import com.google.android.material.snackbar.SnackbarContentLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Snackbar
extends BaseTransientBottomBar<Snackbar> {
    public static final int LENGTH_INDEFINITE = -2;
    public static final int LENGTH_LONG = 0;
    public static final int LENGTH_SHORT = -1;
    private static final int[] SNACKBAR_BUTTON_STYLE_ATTR = new int[]{R.attr.snackbarButtonStyle};
    private final AccessibilityManager accessibilityManager;
    private BaseTransientBottomBar.BaseCallback<Snackbar> callback;
    private boolean hasAction;

    private Snackbar(ViewGroup viewGroup, View view, ContentViewCallback contentViewCallback) {
        super(viewGroup, view, contentViewCallback);
        this.accessibilityManager = (AccessibilityManager)viewGroup.getContext().getSystemService("accessibility");
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup viewGroup = null;
        while (!(view instanceof CoordinatorLayout)) {
            if (view instanceof FrameLayout) {
                if (view.getId() == 0x1020002) {
                    return (ViewGroup)view;
                }
                viewGroup = (ViewGroup)view;
            }
            View view2 = view;
            if (view != null) {
                if (!((view = view.getParent()) instanceof View)) {
                    view = null;
                }
                view2 = view;
            }
            if (view2 == null) {
                return viewGroup;
            }
            view = view2;
        }
        return (ViewGroup)view;
    }

    protected static boolean hasSnackbarButtonStyleAttr(Context context) {
        context = context.obtainStyledAttributes(SNACKBAR_BUTTON_STYLE_ATTR);
        boolean bl = false;
        int n = context.getResourceId(0, -1);
        context.recycle();
        if (n != -1) {
            bl = true;
        }
        return bl;
    }

    public static Snackbar make(View view, int n, int n2) {
        return Snackbar.make(view, view.getResources().getText(n), n2);
    }

    public static Snackbar make(View object, CharSequence charSequence, int n) {
        if ((object = Snackbar.findSuitableParent((View)object)) != null) {
            Object object2 = LayoutInflater.from((Context)object.getContext());
            int n2 = Snackbar.hasSnackbarButtonStyleAttr(object.getContext()) ? R.layout.mtrl_layout_snackbar_include : R.layout.design_layout_snackbar_include;
            object2 = (SnackbarContentLayout)object2.inflate(n2, (ViewGroup)object, false);
            object = new Snackbar((ViewGroup)object, (View)object2, (ContentViewCallback)object2);
            ((Snackbar)object).setText(charSequence);
            ((BaseTransientBottomBar)object).setDuration(n);
            return object;
        }
        throw new IllegalArgumentException("No suitable parent found from the given view. Please provide a valid view.");
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public int getDuration() {
        int n = this.hasAction && this.accessibilityManager.isTouchExplorationEnabled() ? -2 : super.getDuration();
        return n;
    }

    @Override
    public boolean isShown() {
        return super.isShown();
    }

    public Snackbar setAction(int n, View.OnClickListener onClickListener) {
        return this.setAction(this.getContext().getText(n), onClickListener);
    }

    public Snackbar setAction(CharSequence charSequence, View.OnClickListener onClickListener) {
        Button button = ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView();
        if (!TextUtils.isEmpty((CharSequence)charSequence) && onClickListener != null) {
            this.hasAction = true;
            button.setVisibility(0);
            button.setText(charSequence);
            button.setOnClickListener(new View.OnClickListener(this, onClickListener){
                final Snackbar this$0;
                final View.OnClickListener val$listener;
                {
                    this.this$0 = snackbar;
                    this.val$listener = onClickListener;
                }

                public void onClick(View view) {
                    this.val$listener.onClick(view);
                    this.this$0.dispatchDismiss(1);
                }
            });
        } else {
            button.setVisibility(8);
            button.setOnClickListener(null);
            this.hasAction = false;
        }
        return this;
    }

    public Snackbar setActionTextColor(int n) {
        ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView().setTextColor(n);
        return this;
    }

    public Snackbar setActionTextColor(ColorStateList colorStateList) {
        ((SnackbarContentLayout)this.view.getChildAt(0)).getActionView().setTextColor(colorStateList);
        return this;
    }

    @Deprecated
    public Snackbar setCallback(Callback callback) {
        BaseTransientBottomBar.BaseCallback<Snackbar> baseCallback = this.callback;
        if (baseCallback != null) {
            this.removeCallback(baseCallback);
        }
        if (callback != null) {
            this.addCallback(callback);
        }
        this.callback = callback;
        return this;
    }

    public Snackbar setText(int n) {
        return this.setText(this.getContext().getText(n));
    }

    public Snackbar setText(CharSequence charSequence) {
        ((SnackbarContentLayout)this.view.getChildAt(0)).getMessageView().setText(charSequence);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    public static class Callback
    extends BaseTransientBottomBar.BaseCallback<Snackbar> {
        public static final int DISMISS_EVENT_ACTION = 1;
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;
        public static final int DISMISS_EVENT_MANUAL = 3;
        public static final int DISMISS_EVENT_SWIPE = 0;
        public static final int DISMISS_EVENT_TIMEOUT = 2;

        @Override
        public void onDismissed(Snackbar snackbar, int n) {
        }

        @Override
        public void onShown(Snackbar snackbar) {
        }
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface Duration {
    }

    public static final class SnackbarLayout
    extends BaseTransientBottomBar.SnackbarBaseLayout {
        public SnackbarLayout(Context context) {
            super(context);
        }

        public SnackbarLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        protected void onMeasure(int n, int n2) {
            super.onMeasure(n, n2);
            int n3 = this.getChildCount();
            n2 = this.getMeasuredWidth();
            int n4 = this.getPaddingLeft();
            int n5 = this.getPaddingRight();
            for (n = 0; n < n3; ++n) {
                View view = this.getChildAt(n);
                if (view.getLayoutParams().width != -1) continue;
                view.measure(View.MeasureSpec.makeMeasureSpec((int)(n2 - n4 - n5), (int)0x40000000), View.MeasureSpec.makeMeasureSpec((int)view.getMeasuredHeight(), (int)0x40000000));
            }
        }
    }
}

