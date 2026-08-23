/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package com.bumptech.glide.request.transition;

import android.view.View;
import com.bumptech.glide.request.transition.Transition;

public class ViewPropertyTransition<R>
implements Transition<R> {
    private final Animator animator;

    public ViewPropertyTransition(Animator animator2) {
        this.animator = animator2;
    }

    @Override
    public boolean transition(R r, Transition.ViewAdapter viewAdapter) {
        if (viewAdapter.getView() != null) {
            this.animator.animate(viewAdapter.getView());
        }
        return false;
    }

    public static interface Animator {
        public void animate(View var1);
    }
}

