/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.ObjectAnimator
 *  android.animation.TypeEvaluator
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;
import androidx.transition.RectEvaluator;
import androidx.transition.Transition;
import androidx.transition.TransitionValues;
import androidx.transition.ViewUtils;

public class ChangeClipBounds
extends Transition {
    private static final String PROPNAME_BOUNDS = "android:clipBounds:bounds";
    private static final String PROPNAME_CLIP = "android:clipBounds:clip";
    private static final String[] sTransitionProperties = new String[]{"android:clipBounds:clip"};

    public ChangeClipBounds() {
    }

    public ChangeClipBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void captureValues(TransitionValues transitionValues) {
        View view = transitionValues.view;
        if (view.getVisibility() == 8) {
            return;
        }
        Rect rect = ViewCompat.getClipBounds(view);
        transitionValues.values.put(PROPNAME_CLIP, rect);
        if (rect == null) {
            view = new Rect(0, 0, view.getWidth(), view.getHeight());
            transitionValues.values.put(PROPNAME_BOUNDS, view);
        }
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        this.captureValues(transitionValues);
    }

    @Override
    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues != null && transitionValues2 != null && transitionValues.values.containsKey(PROPNAME_CLIP) && transitionValues2.values.containsKey(PROPNAME_CLIP)) {
            Rect rect = (Rect)transitionValues.values.get(PROPNAME_CLIP);
            Object object = (Rect)transitionValues2.values.get(PROPNAME_CLIP);
            boolean bl = object == null;
            if (rect == null && object == null) {
                return null;
            }
            if (rect == null) {
                transitionValues = (Rect)transitionValues.values.get(PROPNAME_BOUNDS);
                viewGroup = object;
            } else {
                transitionValues = rect;
                viewGroup = object;
                if (object == null) {
                    viewGroup = (Rect)transitionValues2.values.get(PROPNAME_BOUNDS);
                    transitionValues = rect;
                }
            }
            if (transitionValues.equals(viewGroup)) {
                return null;
            }
            ViewCompat.setClipBounds(transitionValues2.view, (Rect)transitionValues);
            object = new RectEvaluator(new Rect());
            viewGroup = ObjectAnimator.ofObject((Object)transitionValues2.view, ViewUtils.CLIP_BOUNDS, (TypeEvaluator)object, (Object[])new Rect[]{transitionValues, viewGroup});
            if (bl) {
                viewGroup.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, transitionValues2.view){
                    final ChangeClipBounds this$0;
                    final View val$endView;
                    {
                        this.this$0 = changeClipBounds;
                        this.val$endView = view;
                    }

                    public void onAnimationEnd(Animator animator2) {
                        ViewCompat.setClipBounds(this.val$endView, null);
                    }
                });
            }
            return viewGroup;
        }
        return null;
    }

    @Override
    public String[] getTransitionProperties() {
        return sTransitionProperties;
    }
}

