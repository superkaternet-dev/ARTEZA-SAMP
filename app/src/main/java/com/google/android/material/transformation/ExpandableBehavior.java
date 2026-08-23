/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewTreeObserver$OnPreDrawListener
 */
package com.google.android.material.transformation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import com.google.android.material.expandable.ExpandableWidget;
import java.util.List;

public abstract class ExpandableBehavior
extends CoordinatorLayout.Behavior<View> {
    private static final int STATE_COLLAPSED = 2;
    private static final int STATE_EXPANDED = 1;
    private static final int STATE_UNINITIALIZED = 0;
    private int currentState = 0;

    public ExpandableBehavior() {
    }

    public ExpandableBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private boolean didStateChange(boolean bl) {
        boolean bl2;
        block4: {
            block6: {
                block5: {
                    bl2 = false;
                    boolean bl3 = false;
                    if (!bl) break block4;
                    int n = this.currentState;
                    if (n == 0) break block5;
                    bl = bl3;
                    if (n != 2) break block6;
                }
                bl = true;
            }
            return bl;
        }
        bl = bl2;
        if (this.currentState == 1) {
            bl = true;
        }
        return bl;
    }

    public static <T extends ExpandableBehavior> T from(View object, Class<T> clazz) {
        if ((object = object.getLayoutParams()) instanceof CoordinatorLayout.LayoutParams) {
            if ((object = ((CoordinatorLayout.LayoutParams)((Object)object)).getBehavior()) instanceof ExpandableBehavior) {
                return (T)((ExpandableBehavior)clazz.cast(object));
            }
            throw new IllegalArgumentException("The view is not associated with ExpandableBehavior");
        }
        throw new IllegalArgumentException("The view is not a child of CoordinatorLayout");
    }

    protected ExpandableWidget findExpandableWidget(CoordinatorLayout coordinatorLayout, View view) {
        List<View> list = coordinatorLayout.getDependencies(view);
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            View view2 = list.get(i);
            if (!this.layoutDependsOn(coordinatorLayout, view, view2)) continue;
            return (ExpandableWidget)view2;
        }
        return null;
    }

    @Override
    public abstract boolean layoutDependsOn(CoordinatorLayout var1, View var2, View var3);

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout object, View view, View view2) {
        object = (ExpandableWidget)view2;
        if (this.didStateChange(object.isExpanded())) {
            int n = object.isExpanded() ? 1 : 2;
            this.currentState = n;
            return this.onExpandedStateChange((View)object, view, object.isExpanded(), true);
        }
        return false;
    }

    protected abstract boolean onExpandedStateChange(View var1, View var2, boolean var3, boolean var4);

    @Override
    public boolean onLayoutChild(CoordinatorLayout object, View view, int n) {
        if (!ViewCompat.isLaidOut(view) && (object = this.findExpandableWidget((CoordinatorLayout)object, view)) != null && this.didStateChange(object.isExpanded())) {
            n = object.isExpanded() ? 1 : 2;
            n = this.currentState = n;
            view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener(this, view, n, (ExpandableWidget)object){
                final ExpandableBehavior this$0;
                final View val$child;
                final ExpandableWidget val$dep;
                final int val$expectedState;
                {
                    this.this$0 = expandableBehavior;
                    this.val$child = view;
                    this.val$expectedState = n;
                    this.val$dep = expandableWidget;
                }

                public boolean onPreDraw() {
                    this.val$child.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
                    if (this.this$0.currentState == this.val$expectedState) {
                        ExpandableBehavior expandableBehavior = this.this$0;
                        ExpandableWidget expandableWidget = this.val$dep;
                        expandableBehavior.onExpandedStateChange((View)expandableWidget, this.val$child, expandableWidget.isExpanded(), false);
                    }
                    return false;
                }
            });
        }
        return false;
    }
}

