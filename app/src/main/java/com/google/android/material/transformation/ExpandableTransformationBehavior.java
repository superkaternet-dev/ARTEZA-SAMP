/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.AnimatorSet
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 */
package com.google.android.material.transformation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.google.android.material.transformation.ExpandableBehavior;

public abstract class ExpandableTransformationBehavior
extends ExpandableBehavior {
    private AnimatorSet currentAnimation;

    public ExpandableTransformationBehavior() {
    }

    public ExpandableTransformationBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    static /* synthetic */ AnimatorSet access$002(ExpandableTransformationBehavior expandableTransformationBehavior, AnimatorSet animatorSet) {
        expandableTransformationBehavior.currentAnimation = animatorSet;
        return animatorSet;
    }

    protected abstract AnimatorSet onCreateExpandedStateChangeAnimation(View var1, View var2, boolean var3, boolean var4);

    @Override
    protected boolean onExpandedStateChange(View view, View view2, boolean bl, boolean bl2) {
        AnimatorSet animatorSet = this.currentAnimation;
        boolean bl3 = animatorSet != null;
        if (bl3) {
            animatorSet.cancel();
        }
        view = this.onCreateExpandedStateChangeAnimation(view, view2, bl, bl3);
        this.currentAnimation = view;
        view.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this){
            final ExpandableTransformationBehavior this$0;
            {
                this.this$0 = expandableTransformationBehavior;
            }

            public void onAnimationEnd(Animator animator2) {
                ExpandableTransformationBehavior.access$002(this.this$0, null);
            }
        });
        this.currentAnimation.start();
        if (!bl2) {
            this.currentAnimation.end();
        }
        return true;
    }
}

