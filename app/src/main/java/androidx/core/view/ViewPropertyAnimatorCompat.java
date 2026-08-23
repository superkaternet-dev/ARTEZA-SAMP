/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.animation.ValueAnimator
 *  android.animation.ValueAnimator$AnimatorUpdateListener
 *  android.os.Build$VERSION
 *  android.view.View
 *  android.view.animation.Interpolator
 */
package androidx.core.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Build;
import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.view.ViewPropertyAnimatorListener;
import androidx.core.view.ViewPropertyAnimatorUpdateListener;
import java.lang.ref.WeakReference;

public final class ViewPropertyAnimatorCompat {
    static final int LISTENER_TAG_ID = 0x7E000000;
    Runnable mEndAction = null;
    int mOldLayerType = -1;
    Runnable mStartAction = null;
    private WeakReference<View> mView;

    ViewPropertyAnimatorCompat(View view) {
        this.mView = new WeakReference<View>(view);
    }

    private void setListenerInternal(View view, ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        if (viewPropertyAnimatorListener != null) {
            view.animate().setListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, viewPropertyAnimatorListener, view){
                final ViewPropertyAnimatorCompat this$0;
                final ViewPropertyAnimatorListener val$listener;
                final View val$view;
                {
                    this.this$0 = viewPropertyAnimatorCompat;
                    this.val$listener = viewPropertyAnimatorListener;
                    this.val$view = view;
                }

                public void onAnimationCancel(Animator animator2) {
                    this.val$listener.onAnimationCancel(this.val$view);
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$listener.onAnimationEnd(this.val$view);
                }

                public void onAnimationStart(Animator animator2) {
                    this.val$listener.onAnimationStart(this.val$view);
                }
            });
        } else {
            view.animate().setListener(null);
        }
    }

    public ViewPropertyAnimatorCompat alpha(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().alpha(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat alphaBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().alphaBy(f);
        }
        return this;
    }

    public void cancel() {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().cancel();
        }
    }

    public long getDuration() {
        View view = (View)this.mView.get();
        if (view != null) {
            return view.animate().getDuration();
        }
        return 0L;
    }

    public Interpolator getInterpolator() {
        View view = (View)this.mView.get();
        if (view != null && Build.VERSION.SDK_INT >= 18) {
            return (Interpolator)view.animate().getInterpolator();
        }
        return null;
    }

    public long getStartDelay() {
        View view = (View)this.mView.get();
        if (view != null) {
            return view.animate().getStartDelay();
        }
        return 0L;
    }

    public ViewPropertyAnimatorCompat rotation(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().rotation(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().rotationBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationX(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().rotationX(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationXBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().rotationXBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationY(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().rotationY(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat rotationYBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().rotationYBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleX(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().scaleX(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleXBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().scaleXBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleY(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().scaleY(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat scaleYBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().scaleYBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setDuration(long l) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().setDuration(l);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setInterpolator(Interpolator interpolator2) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().setInterpolator((TimeInterpolator)interpolator2);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setListener(ViewPropertyAnimatorListener viewPropertyAnimatorListener) {
        View view = (View)this.mView.get();
        if (view != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                this.setListenerInternal(view, viewPropertyAnimatorListener);
            } else {
                view.setTag(0x7E000000, (Object)viewPropertyAnimatorListener);
                this.setListenerInternal(view, new ViewPropertyAnimatorListenerApi14(this));
            }
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setStartDelay(long l) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().setStartDelay(l);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat setUpdateListener(ViewPropertyAnimatorUpdateListener viewPropertyAnimatorUpdateListener) {
        View view = (View)this.mView.get();
        if (view != null && Build.VERSION.SDK_INT >= 19) {
            ValueAnimator.AnimatorUpdateListener animatorUpdateListener = null;
            if (viewPropertyAnimatorUpdateListener != null) {
                animatorUpdateListener = new ValueAnimator.AnimatorUpdateListener(this, viewPropertyAnimatorUpdateListener, view){
                    final ViewPropertyAnimatorCompat this$0;
                    final ViewPropertyAnimatorUpdateListener val$listener;
                    final View val$view;
                    {
                        this.this$0 = viewPropertyAnimatorCompat;
                        this.val$listener = viewPropertyAnimatorUpdateListener;
                        this.val$view = view;
                    }

                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        this.val$listener.onAnimationUpdate(this.val$view);
                    }
                };
            }
            view.animate().setUpdateListener(animatorUpdateListener);
        }
        return this;
    }

    public void start() {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().start();
        }
    }

    public ViewPropertyAnimatorCompat translationX(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().translationX(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationXBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().translationXBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationY(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().translationY(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationYBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().translationYBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationZ(float f) {
        View view = (View)this.mView.get();
        if (view != null && Build.VERSION.SDK_INT >= 21) {
            view.animate().translationZ(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat translationZBy(float f) {
        View view = (View)this.mView.get();
        if (view != null && Build.VERSION.SDK_INT >= 21) {
            view.animate().translationZBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withEndAction(Runnable runnable) {
        View view = (View)this.mView.get();
        if (view != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                view.animate().withEndAction(runnable);
            } else {
                this.setListenerInternal(view, new ViewPropertyAnimatorListenerApi14(this));
                this.mEndAction = runnable;
            }
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withLayer() {
        View view = (View)this.mView.get();
        if (view != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                view.animate().withLayer();
            } else {
                this.mOldLayerType = view.getLayerType();
                this.setListenerInternal(view, new ViewPropertyAnimatorListenerApi14(this));
            }
        }
        return this;
    }

    public ViewPropertyAnimatorCompat withStartAction(Runnable runnable) {
        View view = (View)this.mView.get();
        if (view != null) {
            if (Build.VERSION.SDK_INT >= 16) {
                view.animate().withStartAction(runnable);
            } else {
                this.setListenerInternal(view, new ViewPropertyAnimatorListenerApi14(this));
                this.mStartAction = runnable;
            }
        }
        return this;
    }

    public ViewPropertyAnimatorCompat x(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().x(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat xBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().xBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat y(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().y(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat yBy(float f) {
        View view = (View)this.mView.get();
        if (view != null) {
            view.animate().yBy(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat z(float f) {
        View view = (View)this.mView.get();
        if (view != null && Build.VERSION.SDK_INT >= 21) {
            view.animate().z(f);
        }
        return this;
    }

    public ViewPropertyAnimatorCompat zBy(float f) {
        View view = (View)this.mView.get();
        if (view != null && Build.VERSION.SDK_INT >= 21) {
            view.animate().zBy(f);
        }
        return this;
    }

    static class ViewPropertyAnimatorListenerApi14
    implements ViewPropertyAnimatorListener {
        boolean mAnimEndCalled;
        ViewPropertyAnimatorCompat mVpa;

        ViewPropertyAnimatorListenerApi14(ViewPropertyAnimatorCompat viewPropertyAnimatorCompat) {
            this.mVpa = viewPropertyAnimatorCompat;
        }

        @Override
        public void onAnimationCancel(View view) {
            Object object = view.getTag(0x7E000000);
            ViewPropertyAnimatorListener viewPropertyAnimatorListener = null;
            if (object instanceof ViewPropertyAnimatorListener) {
                viewPropertyAnimatorListener = (ViewPropertyAnimatorListener)object;
            }
            if (viewPropertyAnimatorListener != null) {
                viewPropertyAnimatorListener.onAnimationCancel(view);
            }
        }

        @Override
        public void onAnimationEnd(View view) {
            if (this.mVpa.mOldLayerType > -1) {
                view.setLayerType(this.mVpa.mOldLayerType, null);
                this.mVpa.mOldLayerType = -1;
            }
            if (Build.VERSION.SDK_INT >= 16 || !this.mAnimEndCalled) {
                Object object;
                if (this.mVpa.mEndAction != null) {
                    object = this.mVpa.mEndAction;
                    this.mVpa.mEndAction = null;
                    object.run();
                }
                Object object2 = view.getTag(0x7E000000);
                object = null;
                if (object2 instanceof ViewPropertyAnimatorListener) {
                    object = (ViewPropertyAnimatorListener)object2;
                }
                if (object != null) {
                    object.onAnimationEnd(view);
                }
                this.mAnimEndCalled = true;
            }
        }

        @Override
        public void onAnimationStart(View view) {
            Object object;
            this.mAnimEndCalled = false;
            if (this.mVpa.mOldLayerType > -1) {
                view.setLayerType(2, null);
            }
            if (this.mVpa.mStartAction != null) {
                object = this.mVpa.mStartAction;
                this.mVpa.mStartAction = null;
                object.run();
            }
            Object object2 = view.getTag(0x7E000000);
            object = null;
            if (object2 instanceof ViewPropertyAnimatorListener) {
                object = (ViewPropertyAnimatorListener)object2;
            }
            if (object != null) {
                object.onAnimationStart(view);
            }
        }
    }
}

