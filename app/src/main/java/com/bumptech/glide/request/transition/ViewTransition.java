/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.animation.Animation
 */
package com.bumptech.glide.request.transition;

import android.content.Context;
import android.view.animation.Animation;
import com.bumptech.glide.request.transition.Transition;

public class ViewTransition<R>
implements Transition<R> {
    private final ViewTransitionAnimationFactory viewTransitionAnimationFactory;

    ViewTransition(ViewTransitionAnimationFactory viewTransitionAnimationFactory) {
        this.viewTransitionAnimationFactory = viewTransitionAnimationFactory;
    }

    @Override
    public boolean transition(R object, Transition.ViewAdapter viewAdapter) {
        object = viewAdapter.getView();
        if (object != null) {
            object.clearAnimation();
            object.startAnimation(this.viewTransitionAnimationFactory.build(object.getContext()));
        }
        return false;
    }

    static interface ViewTransitionAnimationFactory {
        public Animation build(Context var1);
    }
}

