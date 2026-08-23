/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.transition.Transition
 *  android.transition.Transition$EpicenterCallback
 *  android.transition.Transition$TransitionListener
 *  android.transition.TransitionManager
 *  android.transition.TransitionSet
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.graphics.Rect;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransitionImpl;
import java.util.ArrayList;
import java.util.List;

class FragmentTransitionCompat21
extends FragmentTransitionImpl {
    FragmentTransitionCompat21() {
    }

    private static boolean hasSimpleTarget(Transition transition) {
        boolean bl = !(FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetIds()) && FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetNames()) && FragmentTransitionCompat21.isNullOrEmpty(transition.getTargetTypes()));
        return bl;
    }

    @Override
    public void addTarget(Object object, View view) {
        if (object != null) {
            ((Transition)object).addTarget(view);
        }
    }

    @Override
    public void addTargets(Object object, ArrayList<View> arrayList) {
        block4: {
            block3: {
                if ((object = (Transition)object) == null) {
                    return;
                }
                if (!(object instanceof TransitionSet)) break block3;
                object = (TransitionSet)object;
                int n = object.getTransitionCount();
                for (int i = 0; i < n; ++i) {
                    this.addTargets(object.getTransitionAt(i), arrayList);
                }
                break block4;
            }
            if (FragmentTransitionCompat21.hasSimpleTarget((Transition)object) || !FragmentTransitionCompat21.isNullOrEmpty(object.getTargets())) break block4;
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                object.addTarget(arrayList.get(i));
            }
        }
    }

    @Override
    public void beginDelayedTransition(ViewGroup viewGroup, Object object) {
        TransitionManager.beginDelayedTransition((ViewGroup)viewGroup, (Transition)((Transition)object));
    }

    @Override
    public boolean canHandle(Object object) {
        return object instanceof Transition;
    }

    @Override
    public Object cloneTransition(Object object) {
        Transition transition = null;
        if (object != null) {
            transition = ((Transition)object).clone();
        }
        return transition;
    }

    @Override
    public Object mergeTransitionsInSequence(Object object, Object object2, Object object3) {
        Object var4_4 = null;
        object = (Transition)object;
        object2 = (Transition)object2;
        object3 = (Transition)object3;
        if (object != null && object2 != null) {
            object = new TransitionSet().addTransition((Transition)object).addTransition((Transition)object2).setOrdering(1);
        } else if (object == null) {
            object = var4_4;
            if (object2 != null) {
                object = object2;
            }
        }
        if (object3 != null) {
            object2 = new TransitionSet();
            if (object != null) {
                object2.addTransition((Transition)object);
            }
            object2.addTransition((Transition)object3);
            return object2;
        }
        return object;
    }

    @Override
    public Object mergeTransitionsTogether(Object object, Object object2, Object object3) {
        TransitionSet transitionSet = new TransitionSet();
        if (object != null) {
            transitionSet.addTransition((Transition)object);
        }
        if (object2 != null) {
            transitionSet.addTransition((Transition)object2);
        }
        if (object3 != null) {
            transitionSet.addTransition((Transition)object3);
        }
        return transitionSet;
    }

    @Override
    public void removeTarget(Object object, View view) {
        if (object != null) {
            ((Transition)object).removeTarget(view);
        }
    }

    @Override
    public void replaceTargets(Object object, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        block4: {
            Transition transition;
            block3: {
                transition = (Transition)object;
                if (!(transition instanceof TransitionSet)) break block3;
                object = (TransitionSet)transition;
                int n = object.getTransitionCount();
                for (int i = 0; i < n; ++i) {
                    this.replaceTargets(object.getTransitionAt(i), arrayList, arrayList2);
                }
                break block4;
            }
            if (FragmentTransitionCompat21.hasSimpleTarget(transition) || (object = transition.getTargets()) == null || object.size() != arrayList.size() || !object.containsAll(arrayList)) break block4;
            int n = arrayList2 == null ? 0 : arrayList2.size();
            for (int i = 0; i < n; ++i) {
                transition.addTarget(arrayList2.get(i));
            }
            for (n = arrayList.size() - 1; n >= 0; --n) {
                transition.removeTarget(arrayList.get(n));
            }
        }
    }

    @Override
    public void scheduleHideFragmentView(Object object, View view, ArrayList<View> arrayList) {
        ((Transition)object).addListener(new Transition.TransitionListener(this, view, arrayList){
            final FragmentTransitionCompat21 this$0;
            final ArrayList val$exitingViews;
            final View val$fragmentView;
            {
                this.this$0 = fragmentTransitionCompat21;
                this.val$fragmentView = view;
                this.val$exitingViews = arrayList;
            }

            public void onTransitionCancel(Transition transition) {
            }

            public void onTransitionEnd(Transition transition) {
                transition.removeListener((Transition.TransitionListener)this);
                this.val$fragmentView.setVisibility(8);
                int n = this.val$exitingViews.size();
                for (int i = 0; i < n; ++i) {
                    ((View)this.val$exitingViews.get(i)).setVisibility(0);
                }
            }

            public void onTransitionPause(Transition transition) {
            }

            public void onTransitionResume(Transition transition) {
            }

            public void onTransitionStart(Transition transition) {
                transition.removeListener((Transition.TransitionListener)this);
                transition.addListener((Transition.TransitionListener)this);
            }
        });
    }

    @Override
    public void scheduleRemoveTargets(Object object, Object object2, ArrayList<View> arrayList, Object object3, ArrayList<View> arrayList2, Object object4, ArrayList<View> arrayList3) {
        ((Transition)object).addListener(new Transition.TransitionListener(this, object2, arrayList, object3, arrayList2, object4, arrayList3){
            final FragmentTransitionCompat21 this$0;
            final Object val$enterTransition;
            final ArrayList val$enteringViews;
            final Object val$exitTransition;
            final ArrayList val$exitingViews;
            final Object val$sharedElementTransition;
            final ArrayList val$sharedElementsIn;
            {
                this.this$0 = fragmentTransitionCompat21;
                this.val$enterTransition = object;
                this.val$enteringViews = arrayList;
                this.val$exitTransition = object2;
                this.val$exitingViews = arrayList2;
                this.val$sharedElementTransition = object3;
                this.val$sharedElementsIn = arrayList3;
            }

            public void onTransitionCancel(Transition transition) {
            }

            public void onTransitionEnd(Transition transition) {
                transition.removeListener((Transition.TransitionListener)this);
            }

            public void onTransitionPause(Transition transition) {
            }

            public void onTransitionResume(Transition transition) {
            }

            public void onTransitionStart(Transition object) {
                object = this.val$enterTransition;
                if (object != null) {
                    this.this$0.replaceTargets(object, this.val$enteringViews, null);
                }
                if ((object = this.val$exitTransition) != null) {
                    this.this$0.replaceTargets(object, this.val$exitingViews, null);
                }
                if ((object = this.val$sharedElementTransition) != null) {
                    this.this$0.replaceTargets(object, this.val$sharedElementsIn, null);
                }
            }
        });
    }

    @Override
    public void setEpicenter(Object object, Rect rect) {
        if (object != null) {
            ((Transition)object).setEpicenterCallback(new Transition.EpicenterCallback(this, rect){
                final FragmentTransitionCompat21 this$0;
                final Rect val$epicenter;
                {
                    this.this$0 = fragmentTransitionCompat21;
                    this.val$epicenter = rect;
                }

                public Rect onGetEpicenter(Transition transition) {
                    transition = this.val$epicenter;
                    if (transition != null && !transition.isEmpty()) {
                        return this.val$epicenter;
                    }
                    return null;
                }
            });
        }
    }

    @Override
    public void setEpicenter(Object object, View view) {
        if (view != null) {
            object = (Transition)object;
            Rect rect = new Rect();
            this.getBoundsOnScreen(view, rect);
            object.setEpicenterCallback(new Transition.EpicenterCallback(this, rect){
                final FragmentTransitionCompat21 this$0;
                final Rect val$epicenter;
                {
                    this.this$0 = fragmentTransitionCompat21;
                    this.val$epicenter = rect;
                }

                public Rect onGetEpicenter(Transition transition) {
                    return this.val$epicenter;
                }
            });
        }
    }

    @Override
    public void setListenerForTransitionEnd(Fragment fragment, Object object, CancellationSignal cancellationSignal, Runnable runnable) {
        ((Transition)object).addListener(new Transition.TransitionListener(this, runnable){
            final FragmentTransitionCompat21 this$0;
            final Runnable val$transitionCompleteRunnable;
            {
                this.this$0 = fragmentTransitionCompat21;
                this.val$transitionCompleteRunnable = runnable;
            }

            public void onTransitionCancel(Transition transition) {
            }

            public void onTransitionEnd(Transition transition) {
                this.val$transitionCompleteRunnable.run();
            }

            public void onTransitionPause(Transition transition) {
            }

            public void onTransitionResume(Transition transition) {
            }

            public void onTransitionStart(Transition transition) {
            }
        });
    }

    @Override
    public void setSharedElementTargets(Object object, View view, ArrayList<View> arrayList) {
        TransitionSet transitionSet = (TransitionSet)object;
        object = transitionSet.getTargets();
        object.clear();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            FragmentTransitionCompat21.bfsAddViewChildren((List<View>)object, arrayList.get(i));
        }
        object.add(view);
        arrayList.add(view);
        this.addTargets(transitionSet, arrayList);
    }

    @Override
    public void swapSharedElementTargets(Object object, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        if ((object = (TransitionSet)object) != null) {
            object.getTargets().clear();
            object.getTargets().addAll(arrayList2);
            this.replaceTargets(object, arrayList, arrayList2);
        }
    }

    @Override
    public Object wrapTransitionInSet(Object object) {
        if (object == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition((Transition)object);
        return transitionSet;
    }
}

