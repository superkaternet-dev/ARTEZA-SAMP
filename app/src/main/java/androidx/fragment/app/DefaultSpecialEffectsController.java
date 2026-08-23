/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.content.Context
 *  android.graphics.Rect
 *  android.util.Log
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.Animation$AnimationListener
 */
package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.os.CancellationSignal;
import androidx.core.util.Preconditions;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentAnim;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransition;
import androidx.fragment.app.FragmentTransitionImpl;
import androidx.fragment.app.SpecialEffectsController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DefaultSpecialEffectsController
extends SpecialEffectsController {
    DefaultSpecialEffectsController(ViewGroup viewGroup) {
        super(viewGroup);
    }

    private void startAnimations(List<AnimationInfo> object, List<SpecialEffectsController.Operation> object2, boolean bl, Map<SpecialEffectsController.Operation, Boolean> object3) {
        AnimationInfo animationInfo;
        ViewGroup viewGroup = this.getContainer();
        Context context = viewGroup.getContext();
        Object object4 = new ArrayList();
        boolean bl2 = false;
        object = object.iterator();
        while (object.hasNext()) {
            animationInfo = (AnimationInfo)object.next();
            if (animationInfo.isVisibilityUnchanged()) {
                animationInfo.completeSpecialEffect();
                continue;
            }
            Object object5 = animationInfo.getAnimation(context);
            if (object5 == null) {
                animationInfo.completeSpecialEffect();
                continue;
            }
            Object object6 = ((FragmentAnim.AnimationOrAnimator)object5).animator;
            if (object6 == null) {
                ((ArrayList)object4).add(animationInfo);
                continue;
            }
            SpecialEffectsController.Operation operation = animationInfo.getOperation();
            object5 = operation.getFragment();
            if (Boolean.TRUE.equals(object3.get(operation))) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    object6 = new StringBuilder();
                    ((StringBuilder)object6).append("Ignoring Animator set on ");
                    ((StringBuilder)object6).append(object5);
                    ((StringBuilder)object6).append(" as this Fragment was involved in a Transition.");
                    Log.v((String)"FragmentManager", (String)((StringBuilder)object6).toString());
                }
                animationInfo.completeSpecialEffect();
                continue;
            }
            boolean bl3 = operation.getFinalState() == SpecialEffectsController.Operation.State.GONE;
            if (bl3) {
                object2.remove(operation);
            }
            object5 = ((Fragment)object5).mView;
            viewGroup.startViewTransition((View)object5);
            object6.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, viewGroup, (View)object5, bl3, operation, animationInfo){
                final DefaultSpecialEffectsController this$0;
                final AnimationInfo val$animationInfo;
                final ViewGroup val$container;
                final boolean val$isHideOperation;
                final SpecialEffectsController.Operation val$operation;
                final View val$viewToAnimate;
                {
                    this.this$0 = defaultSpecialEffectsController;
                    this.val$container = viewGroup;
                    this.val$viewToAnimate = view;
                    this.val$isHideOperation = bl;
                    this.val$operation = operation;
                    this.val$animationInfo = animationInfo;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$container.endViewTransition(this.val$viewToAnimate);
                    if (this.val$isHideOperation) {
                        this.val$operation.getFinalState().applyState(this.val$viewToAnimate);
                    }
                    this.val$animationInfo.completeSpecialEffect();
                }
            });
            object6.setTarget(object5);
            object6.start();
            animationInfo.getSignal().setOnCancelListener(new CancellationSignal.OnCancelListener(this, (Animator)object6){
                final DefaultSpecialEffectsController this$0;
                final Animator val$animator;
                {
                    this.this$0 = defaultSpecialEffectsController;
                    this.val$animator = animator2;
                }

                @Override
                public void onCancel() {
                    this.val$animator.end();
                }
            });
            bl2 = true;
        }
        object2 = ((ArrayList)object4).iterator();
        while (object2.hasNext()) {
            object = (AnimationInfo)object2.next();
            object4 = ((SpecialEffectsInfo)object).getOperation();
            object3 = ((SpecialEffectsController.Operation)object4).getFragment();
            if (bl) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    object4 = new StringBuilder();
                    ((StringBuilder)object4).append("Ignoring Animation set on ");
                    ((StringBuilder)object4).append(object3);
                    ((StringBuilder)object4).append(" as Animations cannot run alongside Transitions.");
                    Log.v((String)"FragmentManager", (String)((StringBuilder)object4).toString());
                }
                ((SpecialEffectsInfo)object).completeSpecialEffect();
                continue;
            }
            if (bl2) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    object4 = new StringBuilder();
                    ((StringBuilder)object4).append("Ignoring Animation set on ");
                    ((StringBuilder)object4).append(object3);
                    ((StringBuilder)object4).append(" as Animations cannot run alongside Animators.");
                    Log.v((String)"FragmentManager", (String)((StringBuilder)object4).toString());
                }
                ((SpecialEffectsInfo)object).completeSpecialEffect();
                continue;
            }
            object3 = ((Fragment)object3).mView;
            animationInfo = Preconditions.checkNotNull(Preconditions.checkNotNull(((AnimationInfo)object).getAnimation((Context)context)).animation);
            if (((SpecialEffectsController.Operation)object4).getFinalState() != SpecialEffectsController.Operation.State.REMOVED) {
                object3.startAnimation((Animation)animationInfo);
                ((SpecialEffectsInfo)object).completeSpecialEffect();
            } else {
                viewGroup.startViewTransition((View)object3);
                object4 = new FragmentAnim.EndViewTransitionAnimation((Animation)animationInfo, viewGroup, (View)object3);
                object4.setAnimationListener(new Animation.AnimationListener(this, viewGroup, (View)object3, (AnimationInfo)object){
                    final DefaultSpecialEffectsController this$0;
                    final AnimationInfo val$animationInfo;
                    final ViewGroup val$container;
                    final View val$viewToAnimate;
                    {
                        this.this$0 = defaultSpecialEffectsController;
                        this.val$container = viewGroup;
                        this.val$viewToAnimate = view;
                        this.val$animationInfo = animationInfo;
                    }

                    public void onAnimationEnd(Animation animation) {
                        this.val$container.post(new Runnable(this){
                            final 4 this$1;
                            {
                                this.this$1 = var1_1;
                            }

                            @Override
                            public void run() {
                                this.this$1.val$container.endViewTransition(this.this$1.val$viewToAnimate);
                                this.this$1.val$animationInfo.completeSpecialEffect();
                            }
                        });
                    }

                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }
                });
                object3.startAnimation((Animation)object4);
            }
            ((SpecialEffectsInfo)object).getSignal().setOnCancelListener(new CancellationSignal.OnCancelListener(this, (View)object3, viewGroup, (AnimationInfo)object){
                final DefaultSpecialEffectsController this$0;
                final AnimationInfo val$animationInfo;
                final ViewGroup val$container;
                final View val$viewToAnimate;
                {
                    this.this$0 = defaultSpecialEffectsController;
                    this.val$viewToAnimate = view;
                    this.val$container = viewGroup;
                    this.val$animationInfo = animationInfo;
                }

                @Override
                public void onCancel() {
                    this.val$viewToAnimate.clearAnimation();
                    this.val$container.endViewTransition(this.val$viewToAnimate);
                    this.val$animationInfo.completeSpecialEffect();
                }
            });
        }
    }

    private Map<SpecialEffectsController.Operation, Boolean> startTransitions(List<TransitionInfo> object, boolean bl, SpecialEffectsController.Operation object2, SpecialEffectsController.Operation operation) {
        Object object3;
        Object object4;
        Object object5;
        int n;
        Object object6;
        Object object7;
        Object object8;
        Object object9;
        Object object10;
        Object object11 = object2;
        Object object12 = operation;
        Object object13 = new HashMap<SpecialEffectsController.Operation, Boolean>();
        Object object14 = object.iterator();
        Object object15 = null;
        while (object14.hasNext()) {
            object10 = object14.next();
            if (((SpecialEffectsInfo)object10).isVisibilityUnchanged()) continue;
            object9 = ((TransitionInfo)object10).getHandlingImpl();
            if (object15 == null) {
                object8 = object9;
            } else {
                object8 = object15;
                if (object9 != null) {
                    if (object15 == object9) {
                        object8 = object15;
                    } else {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Mixing framework transitions and AndroidX transitions is not allowed. Fragment ");
                        ((StringBuilder)object).append(((SpecialEffectsInfo)object10).getOperation().getFragment());
                        ((StringBuilder)object).append(" returned Transition ");
                        ((StringBuilder)object).append(((TransitionInfo)object10).getTransition());
                        ((StringBuilder)object).append(" which uses a different Transition  type than other Fragments.");
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                }
            }
            object15 = object8;
        }
        if (object15 == null) {
            object2 = object.iterator();
            while (object2.hasNext()) {
                object = (TransitionInfo)object2.next();
                object13.put((SpecialEffectsController.Operation)((SpecialEffectsInfo)object).getOperation(), false);
                ((SpecialEffectsInfo)object).completeSpecialEffect();
            }
            return object13;
        }
        object9 = new View(this.getContainer().getContext());
        Object object16 = null;
        object14 = new Rect();
        object10 = new ArrayList();
        Object object17 = new ArrayList();
        ArrayMap<String, String> arrayMap = new ArrayMap<String, String>();
        Object object18 = object.iterator();
        object8 = null;
        boolean bl2 = false;
        while (object18.hasNext()) {
            object7 = (TransitionInfo)object18.next();
            if (((TransitionInfo)object7).hasSharedElementTransition() && object11 != null && object12 != null) {
                int n2;
                object6 = ((FragmentTransitionImpl)object15).wrapTransitionInSet(((FragmentTransitionImpl)object15).cloneTransition(((TransitionInfo)object7).getSharedElementTransition()));
                object16 = operation.getFragment().getSharedElementSourceNames();
                object11 = ((SpecialEffectsController.Operation)object2).getFragment().getSharedElementSourceNames();
                object12 = ((SpecialEffectsController.Operation)object2).getFragment().getSharedElementTargetNames();
                for (n = 0; n < ((ArrayList)object12).size(); ++n) {
                    n2 = ((ArrayList)object16).indexOf(((ArrayList)object12).get(n));
                    if (n2 == -1) continue;
                    ((ArrayList)object16).set(n2, ((ArrayList)object11).get(n));
                }
                object5 = operation.getFragment().getSharedElementTargetNames();
                if (!bl) {
                    object11 = ((SpecialEffectsController.Operation)object2).getFragment().getExitTransitionCallback();
                    object12 = operation.getFragment().getEnterTransitionCallback();
                } else {
                    object11 = ((SpecialEffectsController.Operation)object2).getFragment().getEnterTransitionCallback();
                    object12 = operation.getFragment().getExitTransitionCallback();
                }
                n = ((ArrayList)object16).size();
                for (n2 = 0; n2 < n; ++n2) {
                    arrayMap.put((String)((ArrayList)object16).get(n2), (String)((ArrayList)object5).get(n2));
                }
                object4 = new ArrayMap<String, View>();
                this.findNamedViews((Map<String, View>)object4, ((SpecialEffectsController.Operation)object2).getFragment().mView);
                ((ArrayMap)object4).retainAll((Collection<?>)object16);
                if (object11 != null) {
                    ((SharedElementCallback)object11).onMapSharedElements((List<String>)object16, (Map<String, View>)object4);
                    for (n = ((ArrayList)object16).size() - 1; n >= 0; --n) {
                        object3 = ((ArrayList)object16).get(n);
                        object7 = (View)((SimpleArrayMap)object4).get(object3);
                        if (object7 == null) {
                            arrayMap.remove(object3);
                            continue;
                        }
                        if (((String)object3).equals(ViewCompat.getTransitionName((View)object7))) continue;
                        object3 = (String)arrayMap.remove(object3);
                        arrayMap.put(ViewCompat.getTransitionName((View)object7), (String)object3);
                    }
                    object7 = object16;
                    object16 = object11;
                    object11 = object7;
                    object7 = object16;
                } else {
                    arrayMap.retainAll(((ArrayMap)object4).keySet());
                    object7 = object11;
                    object11 = object16;
                }
                object16 = new ArrayMap();
                this.findNamedViews((Map<String, View>)object16, operation.getFragment().mView);
                ((ArrayMap)object16).retainAll((Collection<?>)object5);
                ((ArrayMap)object16).retainAll(arrayMap.values());
                if (object12 != null) {
                    ((SharedElementCallback)object12).onMapSharedElements((List<String>)object5, (Map<String, View>)object16);
                    for (n = ((ArrayList)object5).size() - 1; n >= 0; --n) {
                        object3 = (String)((ArrayList)object5).get(n);
                        object7 = (View)((SimpleArrayMap)object16).get(object3);
                        if (object7 == null) {
                            object7 = FragmentTransition.findKeyForValue(arrayMap, (String)object3);
                            if (object7 == null) continue;
                            arrayMap.remove(object7);
                            continue;
                        }
                        if (((String)object3).equals(ViewCompat.getTransitionName((View)object7)) || (object3 = FragmentTransition.findKeyForValue(arrayMap, (String)object3)) == null) continue;
                        arrayMap.put((String)object3, ViewCompat.getTransitionName((View)object7));
                    }
                } else {
                    FragmentTransition.retainValues(arrayMap, object16);
                }
                this.retainMatchingViews((ArrayMap<String, View>)object4, (Collection<String>)arrayMap.keySet());
                this.retainMatchingViews((ArrayMap<String, View>)object16, arrayMap.values());
                if (arrayMap.isEmpty()) {
                    object16 = null;
                    ((ArrayList)object10).clear();
                    ((ArrayList)object17).clear();
                    object12 = object2;
                    object11 = operation;
                } else {
                    FragmentTransition.callSharedElementStartEnd(operation.getFragment(), ((SpecialEffectsController.Operation)object2).getFragment(), bl, object4, true);
                    OneShotPreDrawListener.add((View)this.getContainer(), new Runnable(this, operation, (SpecialEffectsController.Operation)object2, bl, (ArrayMap)object16){
                        final DefaultSpecialEffectsController this$0;
                        final SpecialEffectsController.Operation val$firstOut;
                        final boolean val$isPop;
                        final SpecialEffectsController.Operation val$lastIn;
                        final ArrayMap val$lastInViews;
                        {
                            this.this$0 = defaultSpecialEffectsController;
                            this.val$lastIn = operation;
                            this.val$firstOut = operation2;
                            this.val$isPop = bl;
                            this.val$lastInViews = arrayMap;
                        }

                        @Override
                        public void run() {
                            FragmentTransition.callSharedElementStartEnd(this.val$lastIn.getFragment(), this.val$firstOut.getFragment(), this.val$isPop, this.val$lastInViews, false);
                        }
                    });
                    object12 = ((ArrayMap)object4).values().iterator();
                    while (object12.hasNext()) {
                        this.captureTransitioningViews((ArrayList<View>)object10, object12.next());
                    }
                    if (!((ArrayList)object11).isEmpty()) {
                        object8 = (View)((SimpleArrayMap)object4).get((String)((ArrayList)object11).get(0));
                        ((FragmentTransitionImpl)object15).setEpicenter(object6, (View)object8);
                    }
                    object12 = ((ArrayMap)object16).values().iterator();
                    while (object12.hasNext()) {
                        this.captureTransitioningViews((ArrayList<View>)object17, (View)object12.next());
                    }
                    if (!((ArrayList)object5).isEmpty() && (object12 = (View)((SimpleArrayMap)object16).get((String)((ArrayList)object5).get(0))) != null) {
                        bl2 = true;
                        OneShotPreDrawListener.add((View)this.getContainer(), new Runnable(this, (FragmentTransitionImpl)object15, (View)object12, (Rect)object14){
                            final DefaultSpecialEffectsController this$0;
                            final FragmentTransitionImpl val$impl;
                            final Rect val$lastInEpicenterRect;
                            final View val$lastInEpicenterView;
                            {
                                this.this$0 = defaultSpecialEffectsController;
                                this.val$impl = fragmentTransitionImpl;
                                this.val$lastInEpicenterView = view;
                                this.val$lastInEpicenterRect = rect;
                            }

                            @Override
                            public void run() {
                                this.val$impl.getBoundsOnScreen(this.val$lastInEpicenterView, this.val$lastInEpicenterRect);
                            }
                        });
                    }
                    ((FragmentTransitionImpl)object15).setSharedElementTargets(object6, (View)object9, (ArrayList<View>)object10);
                    ((FragmentTransitionImpl)object15).scheduleRemoveTargets(object6, null, null, null, null, object6, (ArrayList<View>)object17);
                    object12 = object2;
                    object13.put(object12, true);
                    object11 = operation;
                    object13.put(object11, true);
                    object16 = object6;
                }
            } else {
                object7 = object11;
                object11 = object12;
                object12 = object7;
            }
            object7 = object10;
            object10 = object11;
            object11 = object12;
            object12 = object10;
            object10 = object7;
        }
        object7 = object8;
        object6 = object17;
        object2 = object13;
        object17 = object9;
        object8 = object15;
        object15 = object12;
        object3 = new ArrayList<View>();
        object18 = null;
        object5 = null;
        object4 = object.iterator();
        object13 = object7;
        object9 = object6;
        object12 = object2;
        object6 = object15;
        object15 = object10;
        object10 = object4;
        object2 = object5;
        object7 = object18;
        while (object10.hasNext()) {
            object4 = (TransitionInfo)object10.next();
            if (((SpecialEffectsInfo)object4).isVisibilityUnchanged()) {
                object12.put(((SpecialEffectsInfo)object4).getOperation(), false);
                ((SpecialEffectsInfo)object4).completeSpecialEffect();
                continue;
            }
            object18 = ((FragmentTransitionImpl)object8).cloneTransition(((TransitionInfo)object4).getTransition());
            object5 = ((SpecialEffectsInfo)object4).getOperation();
            n = object16 != null && (object5 == object11 || object5 == object6) ? 1 : 0;
            if (object18 == null) {
                if (n == 0) {
                    object12.put(object5, false);
                    ((SpecialEffectsInfo)object4).completeSpecialEffect();
                }
            } else {
                object6 = new ArrayList();
                this.captureTransitioningViews((ArrayList<View>)object6, ((SpecialEffectsController.Operation)object5).getFragment().mView);
                if (n != 0) {
                    if (object5 == object11) {
                        ((ArrayList)object6).removeAll((Collection<?>)object15);
                    } else {
                        ((ArrayList)object6).removeAll((Collection<?>)object9);
                    }
                }
                if (((ArrayList)object6).isEmpty()) {
                    ((FragmentTransitionImpl)object8).addTarget(object18, (View)object17);
                } else {
                    ((FragmentTransitionImpl)object8).addTargets(object18, (ArrayList<View>)object6);
                    ((FragmentTransitionImpl)object8).scheduleRemoveTargets(object18, object18, (ArrayList<View>)object6, null, null, null, null);
                    if (((SpecialEffectsController.Operation)object5).getFinalState() == SpecialEffectsController.Operation.State.GONE) {
                        ((FragmentTransitionImpl)object8).scheduleHideFragmentView(object18, ((SpecialEffectsController.Operation)object5).getFragment().mView, (ArrayList<View>)object6);
                        OneShotPreDrawListener.add((View)this.getContainer(), new Runnable(this, (ArrayList)object6){
                            final DefaultSpecialEffectsController this$0;
                            final ArrayList val$transitioningViews;
                            {
                                this.this$0 = defaultSpecialEffectsController;
                                this.val$transitioningViews = arrayList;
                            }

                            @Override
                            public void run() {
                                FragmentTransition.setViewVisibility(this.val$transitioningViews, 4);
                            }
                        });
                    }
                }
                if (((SpecialEffectsController.Operation)object5).getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
                    ((ArrayList)object3).addAll((Collection<View>)object6);
                    if (bl2) {
                        ((FragmentTransitionImpl)object8).setEpicenter(object18, (Rect)object14);
                    }
                } else {
                    ((FragmentTransitionImpl)object8).setEpicenter(object18, (View)object13);
                }
                object12.put(object5, true);
                if (((TransitionInfo)object4).isOverlapAllowed()) {
                    object7 = ((FragmentTransitionImpl)object8).mergeTransitionsTogether(object7, object18, null);
                } else {
                    object2 = ((FragmentTransitionImpl)object8).mergeTransitionsTogether(object2, object18, null);
                }
            }
            object6 = operation;
        }
        object14 = ((FragmentTransitionImpl)object8).mergeTransitionsInSequence(object7, object2, object16);
        object = object.iterator();
        while (object.hasNext()) {
            object10 = (TransitionInfo)object.next();
            if (((SpecialEffectsInfo)object10).isVisibilityUnchanged()) continue;
            object17 = ((TransitionInfo)object10).getTransition();
            object13 = ((SpecialEffectsInfo)object10).getOperation();
            bl2 = object16 != null && (object13 == object11 || object13 == operation);
            if (object17 == null && !bl2) continue;
            if (!ViewCompat.isLaidOut((View)this.getContainer())) {
                if (FragmentManager.isLoggingEnabled(2)) {
                    object17 = new StringBuilder();
                    ((StringBuilder)object17).append("SpecialEffectsController: Container ");
                    ((StringBuilder)object17).append(this.getContainer());
                    ((StringBuilder)object17).append(" has not been laid out. Completing operation ");
                    ((StringBuilder)object17).append(object13);
                    Log.v((String)"FragmentManager", (String)((StringBuilder)object17).toString());
                }
                ((SpecialEffectsInfo)object10).completeSpecialEffect();
                continue;
            }
            ((FragmentTransitionImpl)object8).setListenerForTransitionEnd(((SpecialEffectsInfo)object10).getOperation().getFragment(), object14, ((SpecialEffectsInfo)object10).getSignal(), new Runnable(this, (TransitionInfo)object10){
                final DefaultSpecialEffectsController this$0;
                final TransitionInfo val$transitionInfo;
                {
                    this.this$0 = defaultSpecialEffectsController;
                    this.val$transitionInfo = transitionInfo;
                }

                @Override
                public void run() {
                    this.val$transitionInfo.completeSpecialEffect();
                }
            });
        }
        if (!ViewCompat.isLaidOut((View)this.getContainer())) {
            return object12;
        }
        FragmentTransition.setViewVisibility(object3, 4);
        object = ((FragmentTransitionImpl)object8).prepareSetNameOverridesReordered((ArrayList<View>)object9);
        ((FragmentTransitionImpl)object8).beginDelayedTransition(this.getContainer(), object14);
        ((FragmentTransitionImpl)object8).setNameOverridesReordered((View)this.getContainer(), (ArrayList<View>)object15, (ArrayList<View>)object9, (ArrayList<String>)object, arrayMap);
        FragmentTransition.setViewVisibility(object3, 0);
        ((FragmentTransitionImpl)object8).swapSharedElementTargets(object16, (ArrayList<View>)object15, (ArrayList<View>)object9);
        return object12;
    }

    void applyContainerChanges(SpecialEffectsController.Operation operation) {
        View view = operation.getFragment().mView;
        operation.getFinalState().applyState(view);
    }

    void captureTransitioningViews(ArrayList<View> arrayList, View view) {
        if (view instanceof ViewGroup) {
            if (ViewGroupCompat.isTransitionGroup((ViewGroup)(view = (ViewGroup)view))) {
                arrayList.add(view);
            } else {
                int n = view.getChildCount();
                for (int i = 0; i < n; ++i) {
                    View view2 = view.getChildAt(i);
                    if (view2.getVisibility() != 0) continue;
                    this.captureTransitioningViews(arrayList, view2);
                }
            }
        } else {
            arrayList.add(view);
        }
    }

    @Override
    void executeOperations(List<SpecialEffectsController.Operation> object, boolean bl) {
        ArrayList<AnimationInfo> arrayList;
        Object object2;
        Object object3;
        Object object4;
        ArrayList<SpecialEffectsController.Operation> arrayList2 = null;
        ArrayList<AnimationInfo> arrayList3 = null;
        Object object5 = object.iterator();
        while (object5.hasNext()) {
            object4 = object5.next();
            object3 = SpecialEffectsController.Operation.State.from(((SpecialEffectsController.Operation)object4).getFragment().mView);
            switch (10.$SwitchMap$androidx$fragment$app$SpecialEffectsController$Operation$State[((SpecialEffectsController.Operation)object4).getFinalState().ordinal()]) {
                default: {
                    object2 = arrayList2;
                    arrayList = arrayList3;
                    break;
                }
                case 4: {
                    object2 = arrayList2;
                    arrayList = arrayList3;
                    if (object3 == SpecialEffectsController.Operation.State.VISIBLE) break;
                    arrayList = object4;
                    object2 = arrayList2;
                    break;
                }
                case 1: 
                case 2: 
                case 3: {
                    object2 = arrayList2;
                    arrayList = arrayList3;
                    if (object3 != SpecialEffectsController.Operation.State.VISIBLE) break;
                    object2 = arrayList2;
                    arrayList = arrayList3;
                    if (arrayList2 != null) break;
                    object2 = object4;
                    arrayList = arrayList3;
                }
            }
            arrayList2 = object2;
            arrayList3 = arrayList;
        }
        arrayList = new ArrayList<AnimationInfo>();
        object4 = new ArrayList();
        object2 = new ArrayList<SpecialEffectsController.Operation>((Collection<SpecialEffectsController.Operation>)object);
        object = object.iterator();
        while (true) {
            boolean bl2 = object.hasNext();
            boolean bl3 = true;
            if (!bl2) break;
            object5 = (SpecialEffectsController.Operation)object.next();
            object3 = new CancellationSignal();
            ((SpecialEffectsController.Operation)object5).markStartedSpecialEffect((CancellationSignal)object3);
            arrayList.add(new AnimationInfo((SpecialEffectsController.Operation)object5, (CancellationSignal)object3));
            object3 = new CancellationSignal();
            ((SpecialEffectsController.Operation)object5).markStartedSpecialEffect((CancellationSignal)object3);
            if (!(bl ? object5 == arrayList2 : object5 == arrayList3)) {
                bl3 = false;
            }
            object4.add(new TransitionInfo((SpecialEffectsController.Operation)object5, (CancellationSignal)object3, bl, bl3));
            ((SpecialEffectsController.Operation)object5).addCompletionListener(new Runnable(this, (List)object2, (SpecialEffectsController.Operation)object5){
                final DefaultSpecialEffectsController this$0;
                final List val$awaitingContainerChanges;
                final SpecialEffectsController.Operation val$operation;
                {
                    this.this$0 = defaultSpecialEffectsController;
                    this.val$awaitingContainerChanges = list;
                    this.val$operation = operation;
                }

                @Override
                public void run() {
                    if (this.val$awaitingContainerChanges.contains(this.val$operation)) {
                        this.val$awaitingContainerChanges.remove(this.val$operation);
                        this.this$0.applyContainerChanges(this.val$operation);
                    }
                }
            });
        }
        object = this.startTransitions((List<TransitionInfo>)object4, bl, (SpecialEffectsController.Operation)((Object)arrayList2), (SpecialEffectsController.Operation)((Object)arrayList3));
        this.startAnimations((List<AnimationInfo>)arrayList, (List<SpecialEffectsController.Operation>)object2, object.containsValue(true), (Map<SpecialEffectsController.Operation, Boolean>)object);
        object = object2.iterator();
        while (object.hasNext()) {
            this.applyContainerChanges((SpecialEffectsController.Operation)object.next());
        }
        object2.clear();
    }

    void findNamedViews(Map<String, View> map, View view) {
        String string2 = ViewCompat.getTransitionName(view);
        if (string2 != null) {
            map.put(string2, view);
        }
        if (view instanceof ViewGroup) {
            view = (ViewGroup)view;
            int n = view.getChildCount();
            for (int i = 0; i < n; ++i) {
                string2 = view.getChildAt(i);
                if (string2.getVisibility() != 0) continue;
                this.findNamedViews(map, (View)string2);
            }
        }
    }

    void retainMatchingViews(ArrayMap<String, View> object, Collection<String> collection) {
        object = ((ArrayMap)object).entrySet().iterator();
        while (object.hasNext()) {
            if (collection.contains(ViewCompat.getTransitionName((View)((Map.Entry)object.next()).getValue()))) continue;
            object.remove();
        }
    }

    private static class AnimationInfo
    extends SpecialEffectsInfo {
        private FragmentAnim.AnimationOrAnimator mAnimation;
        private boolean mLoadedAnim = false;

        AnimationInfo(SpecialEffectsController.Operation operation, CancellationSignal cancellationSignal) {
            super(operation, cancellationSignal);
        }

        FragmentAnim.AnimationOrAnimator getAnimation(Context object) {
            if (this.mLoadedAnim) {
                return this.mAnimation;
            }
            Fragment fragment = this.getOperation().getFragment();
            boolean bl = this.getOperation().getFinalState() == SpecialEffectsController.Operation.State.VISIBLE;
            object = FragmentAnim.loadAnimation(object, fragment, bl);
            this.mAnimation = object;
            this.mLoadedAnim = true;
            return object;
        }
    }

    private static class SpecialEffectsInfo {
        private final SpecialEffectsController.Operation mOperation;
        private final CancellationSignal mSignal;

        SpecialEffectsInfo(SpecialEffectsController.Operation operation, CancellationSignal cancellationSignal) {
            this.mOperation = operation;
            this.mSignal = cancellationSignal;
        }

        void completeSpecialEffect() {
            this.mOperation.completeSpecialEffect(this.mSignal);
        }

        SpecialEffectsController.Operation getOperation() {
            return this.mOperation;
        }

        CancellationSignal getSignal() {
            return this.mSignal;
        }

        boolean isVisibilityUnchanged() {
            SpecialEffectsController.Operation.State state;
            SpecialEffectsController.Operation.State state2 = SpecialEffectsController.Operation.State.from(this.mOperation.getFragment().mView);
            boolean bl = state2 == (state = this.mOperation.getFinalState()) || state2 != SpecialEffectsController.Operation.State.VISIBLE && state != SpecialEffectsController.Operation.State.VISIBLE;
            return bl;
        }
    }

    private static class TransitionInfo
    extends SpecialEffectsInfo {
        private final boolean mOverlapAllowed;
        private final Object mSharedElementTransition;
        private final Object mTransition;

        TransitionInfo(SpecialEffectsController.Operation operation, CancellationSignal object, boolean bl, boolean bl2) {
            super(operation, (CancellationSignal)object);
            if (operation.getFinalState() == SpecialEffectsController.Operation.State.VISIBLE) {
                object = bl ? operation.getFragment().getReenterTransition() : operation.getFragment().getEnterTransition();
                this.mTransition = object;
                boolean bl3 = bl ? operation.getFragment().getAllowReturnTransitionOverlap() : operation.getFragment().getAllowEnterTransitionOverlap();
                this.mOverlapAllowed = bl3;
            } else {
                object = bl ? operation.getFragment().getReturnTransition() : operation.getFragment().getExitTransition();
                this.mTransition = object;
                this.mOverlapAllowed = true;
            }
            this.mSharedElementTransition = bl2 ? (bl ? operation.getFragment().getSharedElementReturnTransition() : operation.getFragment().getSharedElementEnterTransition()) : null;
        }

        private FragmentTransitionImpl getHandlingImpl(Object object) {
            if (object == null) {
                return null;
            }
            if (FragmentTransition.PLATFORM_IMPL != null && FragmentTransition.PLATFORM_IMPL.canHandle(object)) {
                return FragmentTransition.PLATFORM_IMPL;
            }
            if (FragmentTransition.SUPPORT_IMPL != null && FragmentTransition.SUPPORT_IMPL.canHandle(object)) {
                return FragmentTransition.SUPPORT_IMPL;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Transition ");
            stringBuilder.append(object);
            stringBuilder.append(" for fragment ");
            stringBuilder.append(this.getOperation().getFragment());
            stringBuilder.append(" is not a valid framework Transition or AndroidX Transition");
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        FragmentTransitionImpl getHandlingImpl() {
            Object object;
            block1: {
                FragmentTransitionImpl fragmentTransitionImpl = this.getHandlingImpl(this.mTransition);
                object = this.getHandlingImpl(this.mSharedElementTransition);
                if (fragmentTransitionImpl != null && object != null && fragmentTransitionImpl != object) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Mixing framework transitions and AndroidX transitions is not allowed. Fragment ");
                    ((StringBuilder)object).append(this.getOperation().getFragment());
                    ((StringBuilder)object).append(" returned Transition ");
                    ((StringBuilder)object).append(this.mTransition);
                    ((StringBuilder)object).append(" which uses a different Transition  type than its shared element transition ");
                    ((StringBuilder)object).append(this.mSharedElementTransition);
                    throw new IllegalArgumentException(((StringBuilder)object).toString());
                }
                if (fragmentTransitionImpl == null) break block1;
                object = fragmentTransitionImpl;
            }
            return object;
        }

        public Object getSharedElementTransition() {
            return this.mSharedElementTransition;
        }

        Object getTransition() {
            return this.mTransition;
        }

        public boolean hasSharedElementTransition() {
            boolean bl = this.mSharedElementTransition != null;
            return bl;
        }

        boolean isOverlapAllowed() {
            return this.mOverlapAllowed;
        }
    }
}

