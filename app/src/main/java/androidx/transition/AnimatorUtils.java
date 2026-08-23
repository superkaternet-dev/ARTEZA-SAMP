/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.Animator$AnimatorPauseListener
 *  android.animation.AnimatorListenerAdapter
 *  android.os.Build$VERSION
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import java.util.ArrayList;

class AnimatorUtils {
    private AnimatorUtils() {
    }

    static void addPauseListener(Animator animator2, AnimatorListenerAdapter animatorListenerAdapter) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator2.addPauseListener((Animator.AnimatorPauseListener)animatorListenerAdapter);
        }
    }

    static void pause(Animator animator2) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator2.pause();
        } else {
            ArrayList arrayList = animator2.getListeners();
            if (arrayList != null) {
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    Animator.AnimatorListener animatorListener = (Animator.AnimatorListener)arrayList.get(i);
                    if (!(animatorListener instanceof AnimatorPauseListenerCompat)) continue;
                    ((AnimatorPauseListenerCompat)animatorListener).onAnimationPause(animator2);
                }
            }
        }
    }

    static void resume(Animator animator2) {
        if (Build.VERSION.SDK_INT >= 19) {
            animator2.resume();
        } else {
            ArrayList arrayList = animator2.getListeners();
            if (arrayList != null) {
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    Animator.AnimatorListener animatorListener = (Animator.AnimatorListener)arrayList.get(i);
                    if (!(animatorListener instanceof AnimatorPauseListenerCompat)) continue;
                    ((AnimatorPauseListenerCompat)animatorListener).onAnimationResume(animator2);
                }
            }
        }
    }

    static interface AnimatorPauseListenerCompat {
        public void onAnimationPause(Animator var1);

        public void onAnimationResume(Animator var1);
    }
}

