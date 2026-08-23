/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.LayoutTransition
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Canvas
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.WindowInsets
 *  android.widget.FrameLayout
 */
package androidx.fragment.app;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.ArrayList;

public final class FragmentContainerView
extends FrameLayout {
    private ArrayList<View> mDisappearingFragmentChildren;
    private boolean mDrawDisappearingViewsFirst = true;
    private ArrayList<View> mTransitioningFragmentViews;

    public FragmentContainerView(Context context) {
        super(context);
    }

    public FragmentContainerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public FragmentContainerView(Context object, AttributeSet object2, int n) {
        super(object, object2, n);
        if (object2 != null) {
            CharSequence charSequence = object2.getClassAttribute();
            String string2 = "class";
            TypedArray typedArray = object.obtainStyledAttributes(object2, R.styleable.FragmentContainerView);
            object2 = charSequence;
            object = string2;
            if (charSequence == null) {
                object2 = typedArray.getString(R.styleable.FragmentContainerView_android_name);
                object = "android:name";
            }
            typedArray.recycle();
            if (object2 != null && !this.isInEditMode()) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("FragmentContainerView must be within a FragmentActivity to use ");
                ((StringBuilder)charSequence).append((String)object);
                ((StringBuilder)charSequence).append("=\"");
                ((StringBuilder)charSequence).append((String)object2);
                ((StringBuilder)charSequence).append("\"");
                throw new UnsupportedOperationException(((StringBuilder)charSequence).toString());
            }
        }
    }

    FragmentContainerView(Context object, AttributeSet object2, FragmentManager fragmentManager) {
        super((Context)object, (AttributeSet)object2);
        String string2 = object2.getClassAttribute();
        Object object3 = object.obtainStyledAttributes((AttributeSet)object2, R.styleable.FragmentContainerView);
        Object object4 = string2;
        if (string2 == null) {
            object4 = object3.getString(R.styleable.FragmentContainerView_android_name);
        }
        string2 = object3.getString(R.styleable.FragmentContainerView_android_tag);
        object3.recycle();
        int n = this.getId();
        object3 = fragmentManager.findFragmentById(n);
        if (object4 != null && object3 == null) {
            if (n <= 0) {
                if (string2 != null) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append(" with tag ");
                    ((StringBuilder)object).append(string2);
                    object = ((StringBuilder)object).toString();
                } else {
                    object = "";
                }
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("FragmentContainerView must have an android:id to add Fragment ");
                ((StringBuilder)object2).append((String)object4);
                ((StringBuilder)object2).append((String)object);
                throw new IllegalStateException(((StringBuilder)object2).toString());
            }
            object4 = fragmentManager.getFragmentFactory().instantiate(object.getClassLoader(), (String)object4);
            ((Fragment)object4).onInflate((Context)object, (AttributeSet)object2, null);
            fragmentManager.beginTransaction().setReorderingAllowed(true).add((ViewGroup)this, (Fragment)object4, string2).commitNowAllowingStateLoss();
        }
        fragmentManager.onContainerAvailable(this);
    }

    private void addDisappearingFragmentView(View view) {
        ArrayList<View> arrayList = this.mTransitioningFragmentViews;
        if (arrayList != null && arrayList.contains(view)) {
            if (this.mDisappearingFragmentChildren == null) {
                this.mDisappearingFragmentChildren = new ArrayList();
            }
            this.mDisappearingFragmentChildren.add(view);
        }
    }

    public void addView(View view, int n, ViewGroup.LayoutParams object) {
        if (FragmentManager.getViewFragment(view) != null) {
            super.addView(view, n, (ViewGroup.LayoutParams)object);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Views added to a FragmentContainerView must be associated with a Fragment. View ");
        ((StringBuilder)object).append(view);
        ((StringBuilder)object).append(" is not associated with a Fragment.");
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    protected boolean addViewInLayout(View view, int n, ViewGroup.LayoutParams object, boolean bl) {
        if (FragmentManager.getViewFragment(view) != null) {
            return super.addViewInLayout(view, n, (ViewGroup.LayoutParams)object, bl);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Views added to a FragmentContainerView must be associated with a Fragment. View ");
        ((StringBuilder)object).append(view);
        ((StringBuilder)object).append(" is not associated with a Fragment.");
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    protected void dispatchDraw(Canvas canvas) {
        if (this.mDrawDisappearingViewsFirst && this.mDisappearingFragmentChildren != null) {
            for (int i = 0; i < this.mDisappearingFragmentChildren.size(); ++i) {
                super.drawChild(canvas, this.mDisappearingFragmentChildren.get(i), this.getDrawingTime());
            }
        }
        super.dispatchDraw(canvas);
    }

    protected boolean drawChild(Canvas canvas, View view, long l) {
        ArrayList<View> arrayList;
        if (this.mDrawDisappearingViewsFirst && (arrayList = this.mDisappearingFragmentChildren) != null && arrayList.size() > 0 && this.mDisappearingFragmentChildren.contains(view)) {
            return false;
        }
        return super.drawChild(canvas, view, l);
    }

    public void endViewTransition(View view) {
        ArrayList<View> arrayList = this.mTransitioningFragmentViews;
        if (arrayList != null) {
            arrayList.remove(view);
            arrayList = this.mDisappearingFragmentChildren;
            if (arrayList != null && arrayList.remove(view)) {
                this.mDrawDisappearingViewsFirst = true;
            }
        }
        super.endViewTransition(view);
    }

    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        for (int i = 0; i < this.getChildCount(); ++i) {
            this.getChildAt(i).dispatchApplyWindowInsets(new WindowInsets(windowInsets));
        }
        return windowInsets;
    }

    public void removeAllViewsInLayout() {
        for (int i = this.getChildCount() - 1; i >= 0; --i) {
            this.addDisappearingFragmentView(this.getChildAt(i));
        }
        super.removeAllViewsInLayout();
    }

    protected void removeDetachedView(View view, boolean bl) {
        if (bl) {
            this.addDisappearingFragmentView(view);
        }
        super.removeDetachedView(view, bl);
    }

    public void removeView(View view) {
        this.addDisappearingFragmentView(view);
        super.removeView(view);
    }

    public void removeViewAt(int n) {
        this.addDisappearingFragmentView(this.getChildAt(n));
        super.removeViewAt(n);
    }

    public void removeViewInLayout(View view) {
        this.addDisappearingFragmentView(view);
        super.removeViewInLayout(view);
    }

    public void removeViews(int n, int n2) {
        for (int i = n; i < n + n2; ++i) {
            this.addDisappearingFragmentView(this.getChildAt(i));
        }
        super.removeViews(n, n2);
    }

    public void removeViewsInLayout(int n, int n2) {
        for (int i = n; i < n + n2; ++i) {
            this.addDisappearingFragmentView(this.getChildAt(i));
        }
        super.removeViewsInLayout(n, n2);
    }

    void setDrawDisappearingViewsLast(boolean bl) {
        this.mDrawDisappearingViewsFirst = bl;
    }

    public void setLayoutTransition(LayoutTransition layoutTransition) {
        if (Build.VERSION.SDK_INT < 18) {
            super.setLayoutTransition(layoutTransition);
            return;
        }
        throw new UnsupportedOperationException("FragmentContainerView does not support Layout Transitions or animateLayoutChanges=\"true\".");
    }

    public void startViewTransition(View view) {
        if (view.getParent() == this) {
            if (this.mTransitioningFragmentViews == null) {
                this.mTransitioningFragmentViews = new ArrayList();
            }
            this.mTransitioningFragmentViews.add(view);
        }
        super.startViewTransition(view);
    }
}

