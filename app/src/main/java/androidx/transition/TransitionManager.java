/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.View$OnAttachStateChangeListener
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver$OnPreDrawListener
 */
package androidx.transition;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import androidx.collection.ArrayMap;
import androidx.core.view.ViewCompat;
import androidx.transition.AutoTransition;
import androidx.transition.Scene;
import androidx.transition.Transition;
import androidx.transition.TransitionListenerAdapter;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class TransitionManager {
    private static final String LOG_TAG = "TransitionManager";
    private static Transition sDefaultTransition = new AutoTransition();
    static ArrayList<ViewGroup> sPendingTransitions;
    private static ThreadLocal<WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>> sRunningTransitions;
    private ArrayMap<Scene, ArrayMap<Scene, Transition>> mScenePairTransitions;
    private ArrayMap<Scene, Transition> mSceneTransitions = new ArrayMap();

    static {
        sRunningTransitions = new ThreadLocal();
        sPendingTransitions = new ArrayList();
    }

    public TransitionManager() {
        this.mScenePairTransitions = new ArrayMap();
    }

    public static void beginDelayedTransition(ViewGroup viewGroup) {
        TransitionManager.beginDelayedTransition(viewGroup, null);
    }

    public static void beginDelayedTransition(ViewGroup viewGroup, Transition transition) {
        if (!sPendingTransitions.contains(viewGroup) && ViewCompat.isLaidOut((View)viewGroup)) {
            sPendingTransitions.add(viewGroup);
            Transition transition2 = transition;
            if (transition == null) {
                transition2 = sDefaultTransition;
            }
            transition = transition2.clone();
            TransitionManager.sceneChangeSetup(viewGroup, transition);
            Scene.setCurrentScene((View)viewGroup, null);
            TransitionManager.sceneChangeRunTransition(viewGroup, transition);
        }
    }

    private static void changeScene(Scene scene, Transition transition) {
        ViewGroup viewGroup = scene.getSceneRoot();
        if (!sPendingTransitions.contains(viewGroup)) {
            if (transition == null) {
                scene.enter();
            } else {
                sPendingTransitions.add(viewGroup);
                transition = transition.clone();
                transition.setSceneRoot(viewGroup);
                Scene scene2 = Scene.getCurrentScene((View)viewGroup);
                if (scene2 != null && scene2.isCreatedFromLayoutResource()) {
                    transition.setCanRemoveViews(true);
                }
                TransitionManager.sceneChangeSetup(viewGroup, transition);
                scene.enter();
                TransitionManager.sceneChangeRunTransition(viewGroup, transition);
            }
        }
    }

    public static void endTransitions(ViewGroup viewGroup) {
        sPendingTransitions.remove(viewGroup);
        ArrayList arrayList = (ArrayList)TransitionManager.getRunningTransitions().get(viewGroup);
        if (arrayList != null && !arrayList.isEmpty()) {
            arrayList = new ArrayList(arrayList);
            for (int i = arrayList.size() - 1; i >= 0; --i) {
                ((Transition)arrayList.get(i)).forceToEnd(viewGroup);
            }
        }
    }

    static ArrayMap<ViewGroup, ArrayList<Transition>> getRunningTransitions() {
        Object object = sRunningTransitions.get();
        if (object != null && (object = (ArrayMap)((Reference)object).get()) != null) {
            return object;
        }
        object = new ArrayMap<ViewGroup, ArrayList<Transition>>();
        WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>> weakReference = new WeakReference<ArrayMap<ViewGroup, ArrayList<Transition>>>((ArrayMap<ViewGroup, ArrayList<Transition>>)object);
        sRunningTransitions.set(weakReference);
        return object;
    }

    private Transition getTransition(Scene object) {
        ArrayMap arrayMap;
        Object object2 = ((Scene)object).getSceneRoot();
        if (object2 != null && (object2 = Scene.getCurrentScene((View)object2)) != null && (arrayMap = (ArrayMap)this.mScenePairTransitions.get(object)) != null && (object2 = (Transition)arrayMap.get(object2)) != null) {
            return object2;
        }
        if ((object = (Transition)this.mSceneTransitions.get(object)) == null) {
            object = sDefaultTransition;
        }
        return object;
    }

    public static void go(Scene scene) {
        TransitionManager.changeScene(scene, sDefaultTransition);
    }

    public static void go(Scene scene, Transition transition) {
        TransitionManager.changeScene(scene, transition);
    }

    private static void sceneChangeRunTransition(ViewGroup viewGroup, Transition object) {
        if (object != null && viewGroup != null) {
            object = new MultiListener((Transition)object, viewGroup);
            viewGroup.addOnAttachStateChangeListener((View.OnAttachStateChangeListener)object);
            viewGroup.getViewTreeObserver().addOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)object);
        }
    }

    private static void sceneChangeSetup(ViewGroup object, Transition transition) {
        Object object2 = (ArrayList)TransitionManager.getRunningTransitions().get(object);
        if (object2 != null && ((ArrayList)object2).size() > 0) {
            object2 = ((ArrayList)object2).iterator();
            while (object2.hasNext()) {
                ((Transition)object2.next()).pause((View)object);
            }
        }
        if (transition != null) {
            transition.captureValues((ViewGroup)object, true);
        }
        if ((object = Scene.getCurrentScene((View)object)) != null) {
            ((Scene)object).exit();
        }
    }

    public void setTransition(Scene scene, Scene scene2, Transition transition) {
        ArrayMap<Scene, Transition> arrayMap;
        ArrayMap<Scene, Transition> arrayMap2 = arrayMap = (ArrayMap<Scene, Transition>)this.mScenePairTransitions.get(scene2);
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap<Scene, Transition>();
            this.mScenePairTransitions.put(scene2, arrayMap2);
        }
        arrayMap2.put(scene, transition);
    }

    public void setTransition(Scene scene, Transition transition) {
        this.mSceneTransitions.put(scene, transition);
    }

    public void transitionTo(Scene scene) {
        TransitionManager.changeScene(scene, this.getTransition(scene));
    }

    private static class MultiListener
    implements ViewTreeObserver.OnPreDrawListener,
    View.OnAttachStateChangeListener {
        ViewGroup mSceneRoot;
        Transition mTransition;

        MultiListener(Transition transition, ViewGroup viewGroup) {
            this.mTransition = transition;
            this.mSceneRoot = viewGroup;
        }

        private void removeListeners() {
            this.mSceneRoot.getViewTreeObserver().removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener)this);
            this.mSceneRoot.removeOnAttachStateChangeListener((View.OnAttachStateChangeListener)this);
        }

        public boolean onPreDraw() {
            Object object;
            this.removeListeners();
            if (!sPendingTransitions.remove(this.mSceneRoot)) {
                return true;
            }
            ArrayMap<ViewGroup, ArrayList<Transition>> arrayMap = TransitionManager.getRunningTransitions();
            ArrayList<Transition> arrayList = (ArrayList<Transition>)arrayMap.get(this.mSceneRoot);
            ArrayList arrayList2 = null;
            if (arrayList == null) {
                object = new ArrayList<Transition>();
                arrayMap.put(this.mSceneRoot, (ArrayList<Transition>)object);
            } else {
                object = arrayList;
                if (arrayList.size() > 0) {
                    arrayList2 = new ArrayList(arrayList);
                    object = arrayList;
                }
            }
            ((ArrayList)object).add(this.mTransition);
            this.mTransition.addListener(new TransitionListenerAdapter(this, arrayMap){
                final MultiListener this$0;
                final ArrayMap val$runningTransitions;
                {
                    this.this$0 = multiListener;
                    this.val$runningTransitions = arrayMap;
                }

                @Override
                public void onTransitionEnd(Transition transition) {
                    ((ArrayList)this.val$runningTransitions.get(this.this$0.mSceneRoot)).remove(transition);
                }
            });
            this.mTransition.captureValues(this.mSceneRoot, false);
            if (arrayList2 != null) {
                object = arrayList2.iterator();
                while (object.hasNext()) {
                    ((Transition)object.next()).resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.playTransition(this.mSceneRoot);
            return true;
        }

        public void onViewAttachedToWindow(View view) {
        }

        public void onViewDetachedFromWindow(View object) {
            this.removeListeners();
            sPendingTransitions.remove(this.mSceneRoot);
            object = (ArrayList)TransitionManager.getRunningTransitions().get(this.mSceneRoot);
            if (object != null && ((ArrayList)object).size() > 0) {
                object = ((ArrayList)object).iterator();
                while (object.hasNext()) {
                    ((Transition)object.next()).resume((View)this.mSceneRoot);
                }
            }
            this.mTransition.clearValues(true);
        }
    }
}

