/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.motion.widget;

import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.widget.ConstraintSet;

public class TransitionBuilder {
    private static final String TAG = "TransitionBuilder";

    public static MotionScene.Transition buildTransition(MotionScene motionScene, int n, int n2, ConstraintSet constraintSet, int n3, ConstraintSet constraintSet2) {
        MotionScene.Transition transition = new MotionScene.Transition(n, motionScene, n2, n3);
        TransitionBuilder.updateConstraintSetInMotionScene(motionScene, transition, constraintSet, constraintSet2);
        return transition;
    }

    private static void updateConstraintSetInMotionScene(MotionScene motionScene, MotionScene.Transition transition, ConstraintSet constraintSet, ConstraintSet constraintSet2) {
        int n = transition.getStartConstraintSetId();
        int n2 = transition.getEndConstraintSetId();
        motionScene.setConstraintSet(n, constraintSet);
        motionScene.setConstraintSet(n2, constraintSet2);
    }

    public static void validate(MotionLayout motionLayout) {
        if (motionLayout.mScene != null) {
            MotionScene motionScene = motionLayout.mScene;
            if (motionScene.validateLayout(motionLayout)) {
                if (motionScene.mCurrentTransition != null && !motionScene.getDefinedTransitions().isEmpty()) {
                    return;
                }
                throw new RuntimeException("Invalid motion layout. Motion Scene doesn't have any transition.");
            }
            throw new RuntimeException("MotionLayout doesn't have the right motion scene.");
        }
        throw new RuntimeException("Invalid motion layout. Layout missing Motion Scene.");
    }
}

