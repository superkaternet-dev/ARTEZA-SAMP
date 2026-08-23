/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.request.transition;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.NoTransition;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;
import com.bumptech.glide.request.transition.ViewPropertyTransition;

public class ViewPropertyAnimationFactory<R>
implements TransitionFactory<R> {
    private ViewPropertyTransition<R> animation;
    private final ViewPropertyTransition.Animator animator;

    public ViewPropertyAnimationFactory(ViewPropertyTransition.Animator animator2) {
        this.animator = animator2;
    }

    @Override
    public Transition<R> build(DataSource dataSource, boolean bl) {
        if (dataSource != DataSource.MEMORY_CACHE && bl) {
            if (this.animation == null) {
                this.animation = new ViewPropertyTransition(this.animator);
            }
            return this.animation;
        }
        return NoTransition.get();
    }
}

