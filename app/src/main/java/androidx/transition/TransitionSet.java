/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.util.AndroidRuntimeException
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewGroup
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.transition;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.util.AndroidRuntimeException;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.PathMotion;
import androidx.transition.Styleable;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import androidx.transition.TransitionPropagation;
import androidx.transition.TransitionValues;
import androidx.transition.TransitionValuesMaps;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class TransitionSet
extends Transition {
    private static final int FLAG_CHANGE_EPICENTER = 8;
    private static final int FLAG_CHANGE_INTERPOLATOR = 1;
    private static final int FLAG_CHANGE_PATH_MOTION = 4;
    private static final int FLAG_CHANGE_PROPAGATION = 2;
    public static final int ORDERING_SEQUENTIAL = 1;
    public static final int ORDERING_TOGETHER = 0;
    private int mChangeFlags = 0;
    int mCurrentListeners;
    private boolean mPlayTogether = true;
    boolean mStarted = false;
    private ArrayList<Transition> mTransitions = new ArrayList();

    public TransitionSet() {
    }

    public TransitionSet(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        context = context.obtainStyledAttributes(attributeSet, Styleable.TRANSITION_SET);
        this.setOrdering(TypedArrayUtils.getNamedInt((TypedArray)context, (XmlPullParser)((XmlResourceParser)attributeSet), "transitionOrdering", 0, 0));
        context.recycle();
    }

    private void setupStartEndListeners() {
        TransitionSetListener transitionSetListener = new TransitionSetListener(this);
        Iterator<Transition> iterator2 = this.mTransitions.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().addListener(transitionSetListener);
        }
        this.mCurrentListeners = this.mTransitions.size();
    }

    @Override
    public TransitionSet addListener(Transition.TransitionListener transitionListener) {
        return (TransitionSet)super.addListener(transitionListener);
    }

    @Override
    public TransitionSet addTarget(int n) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(n);
        }
        return (TransitionSet)super.addTarget(n);
    }

    @Override
    public TransitionSet addTarget(View view) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(view);
        }
        return (TransitionSet)super.addTarget(view);
    }

    @Override
    public TransitionSet addTarget(Class clazz) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(clazz);
        }
        return (TransitionSet)super.addTarget(clazz);
    }

    @Override
    public TransitionSet addTarget(String string2) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).addTarget(string2);
        }
        return (TransitionSet)super.addTarget(string2);
    }

    public TransitionSet addTransition(Transition transition) {
        this.mTransitions.add(transition);
        transition.mParent = this;
        if (this.mDuration >= 0L) {
            transition.setDuration(this.mDuration);
        }
        if ((this.mChangeFlags & 1) != 0) {
            transition.setInterpolator(this.getInterpolator());
        }
        if ((this.mChangeFlags & 2) != 0) {
            transition.setPropagation(this.getPropagation());
        }
        if ((this.mChangeFlags & 4) != 0) {
            transition.setPathMotion(this.getPathMotion());
        }
        if ((this.mChangeFlags & 8) != 0) {
            transition.setEpicenterCallback(this.getEpicenterCallback());
        }
        return this;
    }

    @Override
    protected void cancel() {
        super.cancel();
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).cancel();
        }
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (Transition transition : this.mTransitions) {
                if (!transition.isValidTarget(transitionValues.view)) continue;
                transition.captureEndValues(transitionValues);
                transitionValues.mTargetedTransitions.add(transition);
            }
        }
    }

    @Override
    void capturePropagationValues(TransitionValues transitionValues) {
        super.capturePropagationValues(transitionValues);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).capturePropagationValues(transitionValues);
        }
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        if (this.isValidTarget(transitionValues.view)) {
            for (Transition transition : this.mTransitions) {
                if (!transition.isValidTarget(transitionValues.view)) continue;
                transition.captureStartValues(transitionValues);
                transitionValues.mTargetedTransitions.add(transition);
            }
        }
    }

    @Override
    public Transition clone() {
        TransitionSet transitionSet = (TransitionSet)super.clone();
        transitionSet.mTransitions = new ArrayList();
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            transitionSet.addTransition(this.mTransitions.get(i).clone());
        }
        return transitionSet;
    }

    @Override
    protected void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        long l = this.getStartDelay();
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            Transition transition = this.mTransitions.get(i);
            if (l > 0L && (this.mPlayTogether || i == 0)) {
                long l2 = transition.getStartDelay();
                if (l2 > 0L) {
                    transition.setStartDelay(l + l2);
                } else {
                    transition.setStartDelay(l);
                }
            }
            transition.createAnimators(viewGroup, transitionValuesMaps, transitionValuesMaps2, arrayList, arrayList2);
        }
    }

    @Override
    public Transition excludeTarget(int n, boolean bl) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(n, bl);
        }
        return super.excludeTarget(n, bl);
    }

    @Override
    public Transition excludeTarget(View view, boolean bl) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(view, bl);
        }
        return super.excludeTarget(view, bl);
    }

    @Override
    public Transition excludeTarget(Class clazz, boolean bl) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(clazz, bl);
        }
        return super.excludeTarget(clazz, bl);
    }

    @Override
    public Transition excludeTarget(String string2, boolean bl) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).excludeTarget(string2, bl);
        }
        return super.excludeTarget(string2, bl);
    }

    @Override
    void forceToEnd(ViewGroup viewGroup) {
        super.forceToEnd(viewGroup);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).forceToEnd(viewGroup);
        }
    }

    public int getOrdering() {
        return this.mPlayTogether ^ 1;
    }

    public Transition getTransitionAt(int n) {
        if (n >= 0 && n < this.mTransitions.size()) {
            return this.mTransitions.get(n);
        }
        return null;
    }

    public int getTransitionCount() {
        return this.mTransitions.size();
    }

    @Override
    public void pause(View view) {
        super.pause(view);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).pause(view);
        }
    }

    @Override
    public TransitionSet removeListener(Transition.TransitionListener transitionListener) {
        return (TransitionSet)super.removeListener(transitionListener);
    }

    @Override
    public TransitionSet removeTarget(int n) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(n);
        }
        return (TransitionSet)super.removeTarget(n);
    }

    @Override
    public TransitionSet removeTarget(View view) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(view);
        }
        return (TransitionSet)super.removeTarget(view);
    }

    @Override
    public TransitionSet removeTarget(Class clazz) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(clazz);
        }
        return (TransitionSet)super.removeTarget(clazz);
    }

    @Override
    public TransitionSet removeTarget(String string2) {
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).removeTarget(string2);
        }
        return (TransitionSet)super.removeTarget(string2);
    }

    public TransitionSet removeTransition(Transition transition) {
        this.mTransitions.remove(transition);
        transition.mParent = null;
        return this;
    }

    @Override
    public void resume(View view) {
        super.resume(view);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).resume(view);
        }
    }

    @Override
    protected void runAnimators() {
        if (this.mTransitions.isEmpty()) {
            this.start();
            this.end();
            return;
        }
        this.setupStartEndListeners();
        if (!this.mPlayTogether) {
            for (int i = 1; i < this.mTransitions.size(); ++i) {
                this.mTransitions.get(i - 1).addListener(new TransitionListenerAdapter(this, this.mTransitions.get(i)){
                    final TransitionSet this$0;
                    final Transition val$nextTransition;
                    {
                        this.this$0 = transitionSet;
                        this.val$nextTransition = transition;
                    }

                    @Override
                    public void onTransitionEnd(Transition transition) {
                        this.val$nextTransition.runAnimators();
                        transition.removeListener(this);
                    }
                });
            }
            Transition transition = this.mTransitions.get(0);
            if (transition != null) {
                transition.runAnimators();
            }
        } else {
            Iterator<Transition> iterator2 = this.mTransitions.iterator();
            while (iterator2.hasNext()) {
                iterator2.next().runAnimators();
            }
        }
    }

    @Override
    void setCanRemoveViews(boolean bl) {
        super.setCanRemoveViews(bl);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).setCanRemoveViews(bl);
        }
    }

    @Override
    public TransitionSet setDuration(long l) {
        super.setDuration(l);
        if (this.mDuration >= 0L) {
            int n = this.mTransitions.size();
            for (int i = 0; i < n; ++i) {
                this.mTransitions.get(i).setDuration(l);
            }
        }
        return this;
    }

    @Override
    public void setEpicenterCallback(Transition.EpicenterCallback epicenterCallback) {
        super.setEpicenterCallback(epicenterCallback);
        this.mChangeFlags |= 8;
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).setEpicenterCallback(epicenterCallback);
        }
    }

    @Override
    public TransitionSet setInterpolator(TimeInterpolator timeInterpolator) {
        this.mChangeFlags |= 1;
        ArrayList<Transition> arrayList = this.mTransitions;
        if (arrayList != null) {
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                this.mTransitions.get(i).setInterpolator(timeInterpolator);
            }
        }
        return (TransitionSet)super.setInterpolator(timeInterpolator);
    }

    public TransitionSet setOrdering(int n) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid parameter for TransitionSet ordering: ");
                stringBuilder.append(n);
                throw new AndroidRuntimeException(stringBuilder.toString());
            }
            case 1: {
                this.mPlayTogether = false;
                break;
            }
            case 0: {
                this.mPlayTogether = true;
            }
        }
        return this;
    }

    @Override
    public void setPathMotion(PathMotion pathMotion) {
        super.setPathMotion(pathMotion);
        this.mChangeFlags |= 4;
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            this.mTransitions.get(i).setPathMotion(pathMotion);
        }
    }

    @Override
    public void setPropagation(TransitionPropagation transitionPropagation) {
        super.setPropagation(transitionPropagation);
        this.mChangeFlags |= 2;
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).setPropagation(transitionPropagation);
        }
    }

    @Override
    TransitionSet setSceneRoot(ViewGroup viewGroup) {
        super.setSceneRoot(viewGroup);
        int n = this.mTransitions.size();
        for (int i = 0; i < n; ++i) {
            this.mTransitions.get(i).setSceneRoot(viewGroup);
        }
        return this;
    }

    @Override
    public TransitionSet setStartDelay(long l) {
        return (TransitionSet)super.setStartDelay(l);
    }

    @Override
    String toString(String string2) {
        Object object = super.toString(string2);
        for (int i = 0; i < this.mTransitions.size(); ++i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)object);
            stringBuilder.append("\n");
            object = this.mTransitions.get(i);
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(string2);
            stringBuilder2.append("  ");
            stringBuilder.append(((Transition)object).toString(stringBuilder2.toString()));
            object = stringBuilder.toString();
        }
        return object;
    }

    static class TransitionSetListener
    extends TransitionListenerAdapter {
        TransitionSet mTransitionSet;

        TransitionSetListener(TransitionSet transitionSet) {
            this.mTransitionSet = transitionSet;
        }

        @Override
        public void onTransitionEnd(Transition transition) {
            TransitionSet transitionSet = this.mTransitionSet;
            --transitionSet.mCurrentListeners;
            if (this.mTransitionSet.mCurrentListeners == 0) {
                this.mTransitionSet.mStarted = false;
                this.mTransitionSet.end();
            }
            transition.removeListener(this);
        }

        @Override
        public void onTransitionStart(Transition transition) {
            if (!this.mTransitionSet.mStarted) {
                this.mTransitionSet.start();
                this.mTransitionSet.mStarted = true;
            }
        }
    }
}

