/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.drawable.BitmapDrawable
 *  android.graphics.drawable.Drawable
 */
package com.bumptech.glide.request.transition;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.request.transition.TransitionFactory;

public abstract class BitmapContainerTransitionFactory<R>
implements TransitionFactory<R> {
    private final TransitionFactory<Drawable> realFactory;

    public BitmapContainerTransitionFactory(TransitionFactory<Drawable> transitionFactory) {
        this.realFactory = transitionFactory;
    }

    @Override
    public Transition<R> build(DataSource dataSource, boolean bl) {
        return new BitmapGlideAnimation(this, this.realFactory.build(dataSource, bl));
    }

    protected abstract Bitmap getBitmap(R var1);

    private final class BitmapGlideAnimation
    implements Transition<R> {
        final BitmapContainerTransitionFactory this$0;
        private final Transition<Drawable> transition;

        BitmapGlideAnimation(BitmapContainerTransitionFactory bitmapContainerTransitionFactory, Transition<Drawable> transition) {
            this.this$0 = bitmapContainerTransitionFactory;
            this.transition = transition;
        }

        @Override
        public boolean transition(R object, Transition.ViewAdapter viewAdapter) {
            object = new BitmapDrawable(viewAdapter.getView().getResources(), this.this$0.getBitmap(object));
            return this.transition.transition((Drawable)object, viewAdapter);
        }
    }
}

