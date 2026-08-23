/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.util.Property
 *  android.view.MotionEvent
 *  android.view.View
 */
package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.animation.AnimatorSetCompat;
import com.google.android.material.animation.MotionTiming;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.transformation.ExpandableTransformationBehavior;
import java.util.ArrayList;
import java.util.List;

public class FabTransformationScrimBehavior
extends ExpandableTransformationBehavior {
    public static final long COLLAPSE_DELAY = 0L;
    public static final long COLLAPSE_DURATION = 150L;
    public static final long EXPAND_DELAY = 75L;
    public static final long EXPAND_DURATION = 150L;
    private final MotionTiming collapseTiming;
    private final MotionTiming expandTiming = new MotionTiming(75L, 150L);

    public FabTransformationScrimBehavior() {
        this.collapseTiming = new MotionTiming(0L, 150L);
    }

    public FabTransformationScrimBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.collapseTiming = new MotionTiming(0L, 150L);
    }

    private void createScrimAnimation(View view, boolean bl, boolean bl2, List<Animator> list, List<Animator.AnimatorListener> object) {
        object = bl ? this.expandTiming : this.collapseTiming;
        if (bl) {
            if (!bl2) {
                view.setAlpha(0.0f);
            }
            view = ObjectAnimator.ofFloat((Object)view, (Property)View.ALPHA, (float[])new float[]{1.0f});
        } else {
            view = ObjectAnimator.ofFloat((Object)view, (Property)View.ALPHA, (float[])new float[]{0.0f});
        }
        ((MotionTiming)object).apply((Animator)view);
        list.add((Animator)view);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout coordinatorLayout, View view, View view2) {
        return view2 instanceof FloatingActionButton;
    }

    @Override
    protected AnimatorSet onCreateExpandedStateChangeAnimation(View view, View view2, boolean bl, boolean bl2) {
        ArrayList<Animator> arrayList = new ArrayList<Animator>();
        this.createScrimAnimation(view2, bl, bl2, arrayList, new ArrayList<Animator.AnimatorListener>());
        view = new AnimatorSet();
        AnimatorSetCompat.playTogether((AnimatorSet)view, arrayList);
        view.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, bl, view2){
            final FabTransformationScrimBehavior this$0;
            final View val$child;
            final boolean val$expanded;
            {
                this.this$0 = fabTransformationScrimBehavior;
                this.val$expanded = bl;
                this.val$child = view;
            }

            public void onAnimationEnd(Animator animator2) {
                if (!this.val$expanded) {
                    this.val$child.setVisibility(4);
                }
            }

            public void onAnimationStart(Animator animator2) {
                if (this.val$expanded) {
                    this.val$child.setVisibility(0);
                }
            }
        });
        return view;
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, View view, MotionEvent motionEvent) {
        return super.onTouchEvent(coordinatorLayout, view, motionEvent);
    }
}

