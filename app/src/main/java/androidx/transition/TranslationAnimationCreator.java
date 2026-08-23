/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ObjectAnimator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TimeInterpolator
 *  android.util.Property
 *  android.view.View
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.util.Property;
import android.view.View;
import androidx.transition.AnimatorUtils;
import androidx.transition.R;
import androidx.transition.TransitionValues;

class TranslationAnimationCreator {
    private TranslationAnimationCreator() {
    }

    static Animator createAnimation(View object, TransitionValues transitionValues, int n, int n2, float f, float f2, float f3, float f4, TimeInterpolator timeInterpolator) {
        float f5 = object.getTranslationX();
        float f6 = object.getTranslationY();
        Object object2 = (int[])transitionValues.view.getTag(R.id.transition_position);
        if (object2 != null) {
            f = object2[0] - n;
            f2 = object2[1] - n2;
            f += f5;
            f2 += f6;
        }
        int n3 = Math.round(f - f5);
        int n4 = Math.round(f2 - f6);
        object.setTranslationX(f);
        object.setTranslationY(f2);
        if (f == f3 && f2 == f4) {
            return null;
        }
        object2 = ObjectAnimator.ofPropertyValuesHolder((Object)object, (PropertyValuesHolder[])new PropertyValuesHolder[]{PropertyValuesHolder.ofFloat((Property)View.TRANSLATION_X, (float[])new float[]{f, f3}), PropertyValuesHolder.ofFloat((Property)View.TRANSLATION_Y, (float[])new float[]{f2, f4})});
        object = new TransitionPositionListener((View)object, transitionValues.view, n + n3, n2 + n4, f5, f6);
        object2.addListener((Animator.AnimatorListener)object);
        AnimatorUtils.addPauseListener((Animator)object2, (AnimatorListenerAdapter)object);
        object2.setInterpolator(timeInterpolator);
        return object2;
    }

    private static class TransitionPositionListener
    extends AnimatorListenerAdapter {
        private final View mMovingView;
        private float mPausedX;
        private float mPausedY;
        private final int mStartX;
        private final int mStartY;
        private final float mTerminalX;
        private final float mTerminalY;
        private int[] mTransitionPosition;
        private final View mViewInHierarchy;

        TransitionPositionListener(View object, View view, int n, int n2, float f, float f2) {
            this.mMovingView = object;
            this.mViewInHierarchy = view;
            this.mStartX = n - Math.round(object.getTranslationX());
            this.mStartY = n2 - Math.round(object.getTranslationY());
            this.mTerminalX = f;
            this.mTerminalY = f2;
            object = (int[])view.getTag(R.id.transition_position);
            this.mTransitionPosition = (int[])object;
            if (object != null) {
                view.setTag(R.id.transition_position, null);
            }
        }

        public void onAnimationCancel(Animator animator2) {
            if (this.mTransitionPosition == null) {
                this.mTransitionPosition = new int[2];
            }
            this.mTransitionPosition[0] = Math.round((float)this.mStartX + this.mMovingView.getTranslationX());
            this.mTransitionPosition[1] = Math.round((float)this.mStartY + this.mMovingView.getTranslationY());
            this.mViewInHierarchy.setTag(R.id.transition_position, (Object)this.mTransitionPosition);
        }

        public void onAnimationEnd(Animator animator2) {
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        public void onAnimationPause(Animator animator2) {
            this.mPausedX = this.mMovingView.getTranslationX();
            this.mPausedY = this.mMovingView.getTranslationY();
            this.mMovingView.setTranslationX(this.mTerminalX);
            this.mMovingView.setTranslationY(this.mTerminalY);
        }

        public void onAnimationResume(Animator animator2) {
            this.mMovingView.setTranslationX(this.mPausedX);
            this.mMovingView.setTranslationY(this.mPausedY);
        }
    }
}

