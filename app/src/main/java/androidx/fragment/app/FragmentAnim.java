/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorInflater
 *  android.animation.AnimatorListenerAdapter
 *  android.content.Context
 *  android.content.res.Resources$NotFoundException
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 *  android.view.animation.AnimationSet
 *  android.view.animation.AnimationUtils
 *  android.view.animation.Transformation
 */
package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.fragment.R;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransition;

class FragmentAnim {
    private FragmentAnim() {
    }

    static void animateRemoveFragment(Fragment fragment, AnimationOrAnimator object, FragmentTransition.Callback callback) {
        View view = fragment.mView;
        ViewGroup viewGroup = fragment.mContainer;
        viewGroup.startViewTransition(view);
        CancellationSignal cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener(fragment){
            final Fragment val$fragment;
            {
                this.val$fragment = fragment;
            }

            @Override
            public void onCancel() {
                if (this.val$fragment.getAnimatingAway() != null) {
                    View view = this.val$fragment.getAnimatingAway();
                    this.val$fragment.setAnimatingAway(null);
                    view.clearAnimation();
                }
                this.val$fragment.setAnimator(null);
            }
        });
        callback.onStart(fragment, cancellationSignal);
        if (((AnimationOrAnimator)object).animation != null) {
            object = new EndViewTransitionAnimation(((AnimationOrAnimator)object).animation, viewGroup, view);
            fragment.setAnimatingAway(fragment.mView);
            object.setAnimationListener(new Animation.AnimationListener(viewGroup, fragment, callback, cancellationSignal){
                final FragmentTransition.Callback val$callback;
                final ViewGroup val$container;
                final Fragment val$fragment;
                final CancellationSignal val$signal;
                {
                    this.val$container = viewGroup;
                    this.val$fragment = fragment;
                    this.val$callback = callback;
                    this.val$signal = cancellationSignal;
                }

                public void onAnimationEnd(Animation animation) {
                    this.val$container.post(new Runnable(this){
                        final 2 this$0;
                        {
                            this.this$0 = var1_1;
                        }

                        @Override
                        public void run() {
                            if (this.this$0.val$fragment.getAnimatingAway() != null) {
                                this.this$0.val$fragment.setAnimatingAway(null);
                                this.this$0.val$callback.onComplete(this.this$0.val$fragment, this.this$0.val$signal);
                            }
                        }
                    });
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                }
            });
            fragment.mView.startAnimation((Animation)object);
        } else {
            Animator animator2 = ((AnimationOrAnimator)object).animator;
            fragment.setAnimator(((AnimationOrAnimator)object).animator);
            animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(viewGroup, view, fragment, callback, cancellationSignal){
                final FragmentTransition.Callback val$callback;
                final ViewGroup val$container;
                final Fragment val$fragment;
                final CancellationSignal val$signal;
                final View val$viewToAnimate;
                {
                    this.val$container = viewGroup;
                    this.val$viewToAnimate = view;
                    this.val$fragment = fragment;
                    this.val$callback = callback;
                    this.val$signal = cancellationSignal;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$container.endViewTransition(this.val$viewToAnimate);
                    animator2 = this.val$fragment.getAnimator();
                    this.val$fragment.setAnimator(null);
                    if (animator2 != null && this.val$container.indexOfChild(this.val$viewToAnimate) < 0) {
                        this.val$callback.onComplete(this.val$fragment, this.val$signal);
                    }
                }
            });
            animator2.setTarget((Object)fragment.mView);
            animator2.start();
        }
    }

    static AnimationOrAnimator loadAnimation(Context context, Fragment object, boolean bl) {
        block19: {
            int n;
            int n2;
            block18: {
                int n3 = ((Fragment)object).getNextTransition();
                n2 = ((Fragment)object).getNextAnim();
                ((Fragment)object).setNextAnim(0);
                if (((Fragment)object).mContainer != null && ((Fragment)object).mContainer.getTag(R.id.visible_removing_fragment_view_tag) != null) {
                    ((Fragment)object).mContainer.setTag(R.id.visible_removing_fragment_view_tag, null);
                }
                if (((Fragment)object).mContainer != null && ((Fragment)object).mContainer.getLayoutTransition() != null) {
                    return null;
                }
                Animation animation = ((Fragment)object).onCreateAnimation(n3, bl, n2);
                if (animation != null) {
                    return new AnimationOrAnimator(animation);
                }
                if ((object = ((Fragment)object).onCreateAnimator(n3, bl, n2)) != null) {
                    return new AnimationOrAnimator((Animator)object);
                }
                n = n2;
                if (n2 == 0) {
                    n = n2;
                    if (n3 != 0) {
                        n = FragmentAnim.transitToAnimResourceId(n3, bl);
                    }
                }
                if (n == 0) break block19;
                bl = "anim".equals(context.getResources().getResourceTypeName(n));
                n2 = n3 = 0;
                if (bl) {
                    block17: {
                        object = AnimationUtils.loadAnimation((Context)context, (int)n);
                        if (object == null) break block17;
                        try {
                            object = new AnimationOrAnimator((Animation)object);
                            return object;
                        }
                        catch (RuntimeException runtimeException) {
                            n2 = n3;
                            break block18;
                        }
                        catch (Resources.NotFoundException notFoundException) {
                            throw notFoundException;
                        }
                    }
                    n2 = 1;
                }
            }
            if (n2 == 0) {
                object = AnimatorInflater.loadAnimator((Context)context, (int)n);
                if (object == null) break block19;
                try {
                    object = new AnimationOrAnimator((Animator)object);
                    return object;
                }
                catch (RuntimeException runtimeException) {
                    if (!bl) {
                        if ((context = AnimationUtils.loadAnimation((Context)context, (int)n)) != null) {
                            return new AnimationOrAnimator((Animation)context);
                        }
                        break block19;
                    }
                    throw runtimeException;
                }
            }
        }
        return null;
    }

    private static int transitToAnimResourceId(int n, boolean bl) {
        int n2 = -1;
        switch (n) {
            default: {
                n = n2;
                break;
            }
            case 8194: {
                if (bl) {
                    n = R.animator.fragment_close_enter;
                    break;
                }
                n = R.animator.fragment_close_exit;
                break;
            }
            case 4099: {
                if (bl) {
                    n = R.animator.fragment_fade_enter;
                    break;
                }
                n = R.animator.fragment_fade_exit;
                break;
            }
            case 4097: {
                n = bl ? R.animator.fragment_open_enter : R.animator.fragment_open_exit;
            }
        }
        return n;
    }

    static class AnimationOrAnimator {
        public final Animation animation;
        public final Animator animator;

        AnimationOrAnimator(Animator animator2) {
            this.animation = null;
            this.animator = animator2;
            if (animator2 != null) {
                return;
            }
            throw new IllegalStateException("Animator cannot be null");
        }

        AnimationOrAnimator(Animation animation) {
            this.animation = animation;
            this.animator = null;
            if (animation != null) {
                return;
            }
            throw new IllegalStateException("Animation cannot be null");
        }
    }

    static class EndViewTransitionAnimation
    extends AnimationSet
    implements Runnable {
        private boolean mAnimating = true;
        private final View mChild;
        private boolean mEnded;
        private final ViewGroup mParent;
        private boolean mTransitionEnded;

        EndViewTransitionAnimation(Animation animation, ViewGroup viewGroup, View view) {
            super(false);
            this.mParent = viewGroup;
            this.mChild = view;
            this.addAnimation(animation);
            viewGroup.post((Runnable)this);
        }

        public boolean getTransformation(long l, Transformation transformation) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(l, transformation)) {
                this.mEnded = true;
                OneShotPreDrawListener.add((View)this.mParent, this);
            }
            return true;
        }

        public boolean getTransformation(long l, Transformation transformation, float f) {
            this.mAnimating = true;
            if (this.mEnded) {
                return true ^ this.mTransitionEnded;
            }
            if (!super.getTransformation(l, transformation, f)) {
                this.mEnded = true;
                OneShotPreDrawListener.add((View)this.mParent, this);
            }
            return true;
        }

        @Override
        public void run() {
            if (!this.mEnded && this.mAnimating) {
                this.mAnimating = false;
                this.mParent.post((Runnable)this);
            } else {
                this.mParent.endViewTransition(this.mChild);
                this.mTransitionEnded = true;
            }
        }
    }
}

