/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Rect
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.transition;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentTransitionImpl;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import java.util.ArrayList;

public class FragmentTransitionSupport
extends FragmentTransitionImpl {
    private static boolean hasSimpleTarget(Transition transition) {
        boolean bl = !(FragmentTransitionSupport.isNullOrEmpty(transition.getTargetIds()) && FragmentTransitionSupport.isNullOrEmpty(transition.getTargetNames()) && FragmentTransitionSupport.isNullOrEmpty(transition.getTargetTypes()));
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
                int n = ((TransitionSet)object).getTransitionCount();
                for (int i = 0; i < n; ++i) {
                    this.addTargets(((TransitionSet)object).getTransitionAt(i), arrayList);
                }
                break block4;
            }
            if (FragmentTransitionSupport.hasSimpleTarget((Transition)object) || !FragmentTransitionSupport.isNullOrEmpty(((Transition)object).getTargets())) break block4;
            int n = arrayList.size();
            for (int i = 0; i < n; ++i) {
                ((Transition)object).addTarget(arrayList.get(i));
            }
        }
    }

    @Override
    public void beginDelayedTransition(ViewGroup viewGroup, Object object) {
        TransitionManager.beginDelayedTransition(viewGroup, (Transition)object);
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
                ((TransitionSet)object2).addTransition((Transition)object);
            }
            ((TransitionSet)object2).addTransition((Transition)object3);
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
    public void replaceTargets(Object list, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        block4: {
            Transition transition;
            block3: {
                transition = (Transition)((Object)list);
                if (!(transition instanceof TransitionSet)) break block3;
                list = (TransitionSet)transition;
                int n = ((TransitionSet)((Object)list)).getTransitionCount();
                for (int i = 0; i < n; ++i) {
                    this.replaceTargets(((TransitionSet)((Object)list)).getTransitionAt(i), arrayList, arrayList2);
                }
                break block4;
            }
            if (FragmentTransitionSupport.hasSimpleTarget(transition) || (list = transition.getTargets()).size() != arrayList.size() || !list.containsAll(arrayList)) break block4;
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
            final FragmentTransitionSupport this$0;
            final ArrayList val$exitingViews;
            final View val$fragmentView;
            {
                this.this$0 = fragmentTransitionSupport;
                this.val$fragmentView = view;
                this.val$exitingViews = arrayList;
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                this.val$fragmentView.setVisibility(8);
                int n = this.val$exitingViews.size();
                for (int i = 0; i < n; ++i) {
                    ((View)this.val$exitingViews.get(i)).setVisibility(0);
                }
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }

            @Override
            public void onTransitionStart(Transition transition) {
            }
        });
    }

    @Override
    public void scheduleRemoveTargets(Object object, Object object2, ArrayList<View> arrayList, Object object3, ArrayList<View> arrayList2, Object object4, ArrayList<View> arrayList3) {
        ((Transition)object).addListener(new Transition.TransitionListener(this, object2, arrayList, object3, arrayList2, object4, arrayList3){
            final FragmentTransitionSupport this$0;
            final Object val$enterTransition;
            final ArrayList val$enteringViews;
            final Object val$exitTransition;
            final ArrayList val$exitingViews;
            final Object val$sharedElementTransition;
            final ArrayList val$sharedElementsIn;
            {
                this.this$0 = fragmentTransitionSupport;
                this.val$enterTransition = object;
                this.val$enteringViews = arrayList;
                this.val$exitTransition = object2;
                this.val$exitingViews = arrayList2;
                this.val$sharedElementTransition = object3;
                this.val$sharedElementsIn = arrayList3;
            }

            @Override
            public void onTransitionCancel(Transition transition) {
            }

            @Override
            public void onTransitionEnd(Transition transition) {
            }

            @Override
            public void onTransitionPause(Transition transition) {
            }

            @Override
            public void onTransitionResume(Transition transition) {
            }

            @Override
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
                final FragmentTransitionSupport this$0;
                final Rect val$epicenter;
                {
                    this.this$0 = fragmentTransitionSupport;
                    this.val$epicenter = rect;
                }

                @Override
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
            ((Transition)object).setEpicenterCallback(new Transition.EpicenterCallback(this, rect){
                final FragmentTransitionSupport this$0;
                final Rect val$epicenter;
                {
                    this.this$0 = fragmentTransitionSupport;
                    this.val$epicenter = rect;
                }

                @Override
                public Rect onGetEpicenter(Transition transition) {
                    return this.val$epicenter;
                }
            });
        }
    }

    @Override
    public void setSharedElementTargets(Object list, View view, ArrayList<View> arrayList) {
        TransitionSet transitionSet = (TransitionSet)((Object)list);
        list = transitionSet.getTargets();
        list.clear();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            FragmentTransitionSupport.bfsAddViewChildren(list, arrayList.get(i));
        }
        list.add(view);
        arrayList.add(view);
        this.addTargets(transitionSet, arrayList);
    }

    @Override
    public void swapSharedElementTargets(Object object, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        if ((object = (TransitionSet)object) != null) {
            ((Transition)object).getTargets().clear();
            ((Transition)object).getTargets().addAll(arrayList2);
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

