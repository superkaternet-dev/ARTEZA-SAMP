/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorInflater
 *  android.animation.AnimatorSet
 *  android.animation.ObjectAnimator
 *  android.animation.ValueAnimator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.util.Log
 */
package com.google.android.material.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import com.google.android.material.animation.MotionTiming;
import java.util.ArrayList;
import java.util.List;

public class MotionSpec {
    private static final String TAG = "MotionSpec";
    private final SimpleArrayMap<String, MotionTiming> timings = new SimpleArrayMap();

    private static void addTimingFromAnimator(MotionSpec object, Animator animator2) {
        if (animator2 instanceof ObjectAnimator) {
            animator2 = (ObjectAnimator)animator2;
            ((MotionSpec)object).setTiming(animator2.getPropertyName(), MotionTiming.createFromAnimator((ValueAnimator)animator2));
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Animator must be an ObjectAnimator: ");
        ((StringBuilder)object).append(animator2);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static MotionSpec createFromAttribute(Context context, TypedArray typedArray, int n) {
        if (typedArray.hasValue(n) && (n = typedArray.getResourceId(n, 0)) != 0) {
            return MotionSpec.createFromResource(context, n);
        }
        return null;
    }

    public static MotionSpec createFromResource(Context object, int n) {
        block4: {
            Object object2;
            try {
                object2 = AnimatorInflater.loadAnimator((Context)object, (int)n);
                if (object2 instanceof AnimatorSet) {
                    return MotionSpec.createSpecFromAnimators(((AnimatorSet)object2).getChildAnimations());
                }
                if (object2 == null) break block4;
            }
            catch (Exception exception) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Can't load animation resource ID #0x");
                ((StringBuilder)object2).append(Integer.toHexString(n));
                Log.w((String)TAG, (String)((StringBuilder)object2).toString(), (Throwable)exception);
                return null;
            }
            object = new ArrayList();
            object.add(object2);
            object = MotionSpec.createSpecFromAnimators((List<Animator>)object);
            return object;
        }
        return null;
    }

    private static MotionSpec createSpecFromAnimators(List<Animator> list) {
        MotionSpec motionSpec = new MotionSpec();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            MotionSpec.addTimingFromAnimator(motionSpec, list.get(i));
        }
        return motionSpec;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (MotionSpec)object;
            return this.timings.equals(((MotionSpec)object).timings);
        }
        return false;
    }

    public MotionTiming getTiming(String string2) {
        if (this.hasTiming(string2)) {
            return this.timings.get(string2);
        }
        throw new IllegalArgumentException();
    }

    public long getTotalDuration() {
        long l = 0L;
        int n = this.timings.size();
        for (int i = 0; i < n; ++i) {
            MotionTiming motionTiming = this.timings.valueAt(i);
            l = Math.max(l, motionTiming.getDelay() + motionTiming.getDuration());
        }
        return l;
    }

    public boolean hasTiming(String string2) {
        boolean bl = this.timings.get(string2) != null;
        return bl;
    }

    public int hashCode() {
        return this.timings.hashCode();
    }

    public void setTiming(String string2, MotionTiming motionTiming) {
        this.timings.put(string2, motionTiming);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('\n');
        stringBuilder.append(this.getClass().getName());
        stringBuilder.append('{');
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" timings: ");
        stringBuilder.append(this.timings);
        stringBuilder.append("}\n");
        return stringBuilder.toString();
    }
}

