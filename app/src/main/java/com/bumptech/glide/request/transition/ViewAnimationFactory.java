/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 */
package com.bumptech.glide.request.transition;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewTransition;

public class ViewAnimationFactory<R>
implements TransitionFactory<R> {
    private Transition<R> transition;
    private final ViewTransition.ViewTransitionAnimationFactory viewTransitionAnimationFactory;

    public ViewAnimationFactory(int n) {
        this(new ResourceViewTransitionAnimationFactory(n));
    }

    public ViewAnimationFactory(Animation animation) {
        this(new ConcreteViewTransitionAnimationFactory(animation));
    }

    ViewAnimationFactory(ViewTransition.ViewTransitionAnimationFactory viewTransitionAnimationFactory) {
        this.viewTransitionAnimationFactory = viewTransitionAnimationFactory;
    }

    @Override
    public Transition<R> build(DataSource dataSource, boolean bl) {
        if (dataSource != DataSource.MEMORY_CACHE && bl) {
            if (this.transition == null) {
                this.transition = new ViewTransition(this.viewTransitionAnimationFactory);
            }
            return this.transition;
        }
        return NoTransition.get();
    }

    private static class ConcreteViewTransitionAnimationFactory
    implements ViewTransition.ViewTransitionAnimationFactory {
        private final Animation animation;

        ConcreteViewTransitionAnimationFactory(Animation animation) {
            this.animation = animation;
        }

        @Override
        public Animation build(Context context) {
            return this.animation;
        }
    }

    private static class ResourceViewTransitionAnimationFactory
    implements ViewTransition.ViewTransitionAnimationFactory {
        private final int animationId;

        ResourceViewTransitionAnimationFactory(int n) {
            this.animationId = n;
        }

        @Override
        public Animation build(Context context) {
            return AnimationUtils.loadAnimation((Context)context, (int)this.animationId);
        }
    }
}

