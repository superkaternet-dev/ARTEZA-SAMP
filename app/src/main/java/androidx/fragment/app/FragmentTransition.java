/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.os.Build$VERSION
 *  android.util.SparseArray
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.fragment.app;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.collection.SimpleArrayMap;
import androidx.core.app.SharedElementCallback;
import androidx.core.os.CancellationSignal;
import androidx.core.view.OneShotPreDrawListener;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStateManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransitionCompat21;
import androidx.fragment.app.FragmentTransitionImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class FragmentTransition {
    private static final int[] INVERSE_OPS = new int[]{0, 3, 0, 1, 5, 4, 7, 6, 9, 8, 10};
    static final FragmentTransitionImpl PLATFORM_IMPL;
    static final FragmentTransitionImpl SUPPORT_IMPL;

    static {
        FragmentTransitionCompat21 fragmentTransitionCompat21 = Build.VERSION.SDK_INT >= 21 ? new FragmentTransitionCompat21() : null;
        PLATFORM_IMPL = fragmentTransitionCompat21;
        SUPPORT_IMPL = FragmentTransition.resolveSupportImpl();
    }

    private FragmentTransition() {
    }

    private static void addSharedElementsWithMatchingNames(ArrayList<View> arrayList, ArrayMap<String, View> arrayMap, Collection<String> collection) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            View view = (View)arrayMap.valueAt(i);
            if (!collection.contains(ViewCompat.getTransitionName(view))) continue;
            arrayList.add(view);
        }
    }

    private static void addToFirstInLastOut(BackStackRecord backStackRecord, FragmentTransaction.Op object, SparseArray<FragmentContainerTransition> sparseArray, boolean bl, boolean bl2) {
        Object object2;
        int n;
        Fragment fragment;
        block20: {
            int n2;
            block21: {
                fragment = ((FragmentTransaction.Op)object).mFragment;
                if (fragment == null) {
                    return;
                }
                n2 = fragment.mContainerId;
                if (n2 == 0) {
                    return;
                }
                n = bl ? INVERSE_OPS[((FragmentTransaction.Op)object).mCmd] : ((FragmentTransaction.Op)object).mCmd;
                boolean bl3 = false;
                int n3 = 0;
                int n4 = 0;
                boolean bl4 = false;
                int n5 = 1;
                int n6 = 1;
                int n7 = 1;
                int n8 = 1;
                boolean bl5 = true;
                boolean bl6 = true;
                switch (n) {
                    default: {
                        n = n3;
                        break;
                    }
                    case 5: {
                        bl3 = bl2 ? (fragment.mHiddenChanged && !fragment.mHidden && fragment.mAdded ? bl6 : false) : fragment.mHidden;
                        bl4 = true;
                        n = n3;
                        break;
                    }
                    case 4: {
                        if (bl2) {
                            n = fragment.mHiddenChanged && fragment.mAdded && fragment.mHidden ? n5 : 0;
                            n4 = n;
                        } else {
                            n = fragment.mAdded && !fragment.mHidden ? n6 : 0;
                            n4 = n;
                        }
                        n = 1;
                        break;
                    }
                    case 3: 
                    case 6: {
                        if (bl2) {
                            n = !fragment.mAdded && fragment.mView != null && fragment.mView.getVisibility() == 0 && fragment.mPostponedAlpha >= 0.0f ? n7 : 0;
                            n4 = n;
                        } else {
                            n = fragment.mAdded && !fragment.mHidden ? n8 : 0;
                            n4 = n;
                        }
                        n = 1;
                        break;
                    }
                    case 1: 
                    case 7: {
                        bl3 = bl2 ? fragment.mIsNewlyAdded : (!fragment.mAdded && !fragment.mHidden ? bl5 : false);
                        bl4 = true;
                        n = n3;
                    }
                }
                object = object2 = (FragmentContainerTransition)sparseArray.get(n2);
                if (bl3) {
                    object = FragmentTransition.ensureContainer((FragmentContainerTransition)object2, sparseArray, n2);
                    ((FragmentContainerTransition)object).lastIn = fragment;
                    ((FragmentContainerTransition)object).lastInIsPop = bl;
                    ((FragmentContainerTransition)object).lastInTransaction = backStackRecord;
                }
                if (!bl2 && bl4) {
                    if (object != null && ((FragmentContainerTransition)object).firstOut == fragment) {
                        ((FragmentContainerTransition)object).firstOut = null;
                    }
                    if (!backStackRecord.mReorderingAllowed) {
                        FragmentManager fragmentManager = backStackRecord.mManager;
                        object2 = fragmentManager.createOrGetFragmentStateManager(fragment);
                        fragmentManager.getFragmentStore().makeActive((FragmentStateManager)object2);
                        fragmentManager.moveToState(fragment);
                    }
                }
                object2 = object;
                if (n4 == 0) break block20;
                if (object == null) break block21;
                object2 = object;
                if (((FragmentContainerTransition)object).firstOut != null) break block20;
            }
            object2 = FragmentTransition.ensureContainer((FragmentContainerTransition)object, sparseArray, n2);
            ((FragmentContainerTransition)object2).firstOut = fragment;
            ((FragmentContainerTransition)object2).firstOutIsPop = bl;
            ((FragmentContainerTransition)object2).firstOutTransaction = backStackRecord;
        }
        if (!bl2 && n != 0 && object2 != null && ((FragmentContainerTransition)object2).lastIn == fragment) {
            ((FragmentContainerTransition)object2).lastIn = null;
        }
    }

    public static void calculateFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean bl) {
        int n = backStackRecord.mOps.size();
        for (int i = 0; i < n; ++i) {
            FragmentTransition.addToFirstInLastOut(backStackRecord, (FragmentTransaction.Op)backStackRecord.mOps.get(i), sparseArray, false, bl);
        }
    }

    private static ArrayMap<String, String> calculateNameOverrides(int n, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n2, int n3) {
        ArrayMap<String, String> arrayMap = new ArrayMap<String, String>();
        --n3;
        while (n3 >= n2) {
            Object object = arrayList.get(n3);
            if (((BackStackRecord)object).interactsWith(n)) {
                boolean bl = arrayList2.get(n3);
                if (((BackStackRecord)object).mSharedElementSourceNames != null) {
                    ArrayList arrayList3;
                    ArrayList arrayList4;
                    int n4 = ((BackStackRecord)object).mSharedElementSourceNames.size();
                    if (bl) {
                        arrayList4 = ((BackStackRecord)object).mSharedElementSourceNames;
                        arrayList3 = ((BackStackRecord)object).mSharedElementTargetNames;
                    } else {
                        arrayList3 = ((BackStackRecord)object).mSharedElementSourceNames;
                        arrayList4 = ((BackStackRecord)object).mSharedElementTargetNames;
                    }
                    for (int i = 0; i < n4; ++i) {
                        String string2 = (String)arrayList3.get(i);
                        String string3 = (String)arrayList4.get(i);
                        object = (String)arrayMap.remove(string3);
                        if (object != null) {
                            arrayMap.put(string2, (String)object);
                            continue;
                        }
                        arrayMap.put(string2, string3);
                    }
                }
            }
            --n3;
        }
        return arrayMap;
    }

    public static void calculatePopFragments(BackStackRecord backStackRecord, SparseArray<FragmentContainerTransition> sparseArray, boolean bl) {
        if (!backStackRecord.mManager.getContainer().onHasView()) {
            return;
        }
        for (int i = backStackRecord.mOps.size() - 1; i >= 0; --i) {
            FragmentTransition.addToFirstInLastOut(backStackRecord, (FragmentTransaction.Op)backStackRecord.mOps.get(i), sparseArray, true, bl);
        }
    }

    static void callSharedElementStartEnd(Fragment object, Fragment object2, boolean bl, ArrayMap<String, View> arrayMap, boolean bl2) {
        object = bl ? ((Fragment)object2).getEnterTransitionCallback() : ((Fragment)object).getEnterTransitionCallback();
        if (object != null) {
            object2 = new ArrayList();
            ArrayList<String> arrayList = new ArrayList<String>();
            int n = arrayMap == null ? 0 : arrayMap.size();
            for (int i = 0; i < n; ++i) {
                arrayList.add((String)arrayMap.keyAt(i));
                ((ArrayList)object2).add(arrayMap.valueAt(i));
            }
            if (bl2) {
                ((SharedElementCallback)object).onSharedElementStart(arrayList, (List<View>)object2, null);
            } else {
                ((SharedElementCallback)object).onSharedElementEnd(arrayList, (List<View>)object2, null);
            }
        }
    }

    private static boolean canHandleAll(FragmentTransitionImpl fragmentTransitionImpl, List<Object> list) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            if (fragmentTransitionImpl.canHandle(list.get(i))) continue;
            return false;
        }
        return true;
    }

    static ArrayMap<String, View> captureInSharedElements(FragmentTransitionImpl object, ArrayMap<String, String> arrayMap, Object object2, FragmentContainerTransition object3) {
        Fragment fragment = ((FragmentContainerTransition)object3).lastIn;
        View view = fragment.getView();
        if (!arrayMap.isEmpty() && object2 != null && view != null) {
            ArrayMap<String, View> arrayMap2 = new ArrayMap<String, View>();
            ((FragmentTransitionImpl)object).findNamedViews(arrayMap2, view);
            object = ((FragmentContainerTransition)object3).lastInTransaction;
            if (((FragmentContainerTransition)object3).lastInIsPop) {
                object2 = fragment.getExitTransitionCallback();
                object = ((BackStackRecord)object).mSharedElementSourceNames;
            } else {
                object2 = fragment.getEnterTransitionCallback();
                object = ((BackStackRecord)object).mSharedElementTargetNames;
            }
            if (object != null) {
                arrayMap2.retainAll((Collection<?>)object);
                arrayMap2.retainAll(arrayMap.values());
            }
            if (object2 != null) {
                ((SharedElementCallback)object2).onMapSharedElements((List<String>)object, arrayMap2);
                for (int i = ((ArrayList)object).size() - 1; i >= 0; --i) {
                    object3 = (String)((ArrayList)object).get(i);
                    object2 = (View)arrayMap2.get(object3);
                    if (object2 == null) {
                        object2 = FragmentTransition.findKeyForValue(arrayMap, (String)object3);
                        if (object2 == null) continue;
                        arrayMap.remove(object2);
                        continue;
                    }
                    if (((String)object3).equals(ViewCompat.getTransitionName((View)object2)) || (object3 = FragmentTransition.findKeyForValue(arrayMap, (String)object3)) == null) continue;
                    arrayMap.put((String)object3, ViewCompat.getTransitionName((View)object2));
                }
            } else {
                FragmentTransition.retainValues(arrayMap, arrayMap2);
            }
            return arrayMap2;
        }
        arrayMap.clear();
        return null;
    }

    private static ArrayMap<String, View> captureOutSharedElements(FragmentTransitionImpl object, ArrayMap<String, String> arrayMap, Object object2, FragmentContainerTransition object3) {
        if (!arrayMap.isEmpty() && object2 != null) {
            object2 = ((FragmentContainerTransition)object3).firstOut;
            ArrayMap<String, View> arrayMap2 = new ArrayMap<String, View>();
            ((FragmentTransitionImpl)object).findNamedViews(arrayMap2, ((Fragment)object2).requireView());
            object = ((FragmentContainerTransition)object3).firstOutTransaction;
            if (((FragmentContainerTransition)object3).firstOutIsPop) {
                object2 = ((Fragment)object2).getEnterTransitionCallback();
                object = ((BackStackRecord)object).mSharedElementTargetNames;
            } else {
                object2 = ((Fragment)object2).getExitTransitionCallback();
                object = ((BackStackRecord)object).mSharedElementSourceNames;
            }
            if (object != null) {
                arrayMap2.retainAll((Collection<?>)object);
            }
            if (object2 != null) {
                ((SharedElementCallback)object2).onMapSharedElements((List<String>)object, arrayMap2);
                for (int i = ((ArrayList)object).size() - 1; i >= 0; --i) {
                    object3 = (String)((ArrayList)object).get(i);
                    object2 = (View)arrayMap2.get(object3);
                    if (object2 == null) {
                        arrayMap.remove(object3);
                        continue;
                    }
                    if (((String)object3).equals(ViewCompat.getTransitionName((View)object2))) continue;
                    object3 = (String)arrayMap.remove(object3);
                    arrayMap.put(ViewCompat.getTransitionName((View)object2), (String)object3);
                }
            } else {
                arrayMap.retainAll(arrayMap2.keySet());
            }
            return arrayMap2;
        }
        arrayMap.clear();
        return null;
    }

    private static FragmentTransitionImpl chooseImpl(Fragment object, Fragment object2) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        if (object != null) {
            Object object3 = ((Fragment)object).getExitTransition();
            if (object3 != null) {
                arrayList.add(object3);
            }
            if ((object3 = ((Fragment)object).getReturnTransition()) != null) {
                arrayList.add(object3);
            }
            if ((object = ((Fragment)object).getSharedElementReturnTransition()) != null) {
                arrayList.add(object);
            }
        }
        if (object2 != null) {
            object = ((Fragment)object2).getEnterTransition();
            if (object != null) {
                arrayList.add(object);
            }
            if ((object = ((Fragment)object2).getReenterTransition()) != null) {
                arrayList.add(object);
            }
            if ((object = ((Fragment)object2).getSharedElementEnterTransition()) != null) {
                arrayList.add(object);
            }
        }
        if (arrayList.isEmpty()) {
            return null;
        }
        object2 = PLATFORM_IMPL;
        if (object2 != null && FragmentTransition.canHandleAll((FragmentTransitionImpl)object2, arrayList)) {
            return object2;
        }
        object = SUPPORT_IMPL;
        if (object != null && FragmentTransition.canHandleAll((FragmentTransitionImpl)object, arrayList)) {
            return object;
        }
        if (object2 == null && object == null) {
            return null;
        }
        throw new IllegalArgumentException("Invalid Transition types");
    }

    static ArrayList<View> configureEnteringExitingViews(FragmentTransitionImpl fragmentTransitionImpl, Object object, Fragment fragment, ArrayList<View> arrayList, View view) {
        ArrayList<View> arrayList2 = null;
        if (object != null) {
            ArrayList<View> arrayList3 = new ArrayList<View>();
            if ((fragment = fragment.getView()) != null) {
                fragmentTransitionImpl.captureTransitioningViews(arrayList3, (View)fragment);
            }
            if (arrayList != null) {
                arrayList3.removeAll(arrayList);
            }
            arrayList2 = arrayList3;
            if (!arrayList3.isEmpty()) {
                arrayList3.add(view);
                fragmentTransitionImpl.addTargets(object, arrayList3);
                arrayList2 = arrayList3;
            }
        }
        return arrayList2;
    }

    private static Object configureSharedElementsOrdered(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, View view, ArrayMap<String, String> arrayMap, FragmentContainerTransition fragmentContainerTransition, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object object, Object object2) {
        Fragment fragment = fragmentContainerTransition.lastIn;
        Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment != null && fragment2 != null) {
            boolean bl = fragmentContainerTransition.lastInIsPop;
            Object object3 = arrayMap.isEmpty() ? null : FragmentTransition.getSharedElementTransition(fragmentTransitionImpl, fragment, fragment2, bl);
            ArrayMap<String, View> arrayMap2 = FragmentTransition.captureOutSharedElements(fragmentTransitionImpl, arrayMap, object3, fragmentContainerTransition);
            if (arrayMap.isEmpty()) {
                object3 = null;
            } else {
                arrayList.addAll(arrayMap2.values());
            }
            if (object == null && object2 == null && object3 == null) {
                return null;
            }
            FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, arrayMap2, true);
            if (object3 != null) {
                Rect rect = new Rect();
                fragmentTransitionImpl.setSharedElementTargets(object3, view, arrayList);
                FragmentTransition.setOutEpicenter(fragmentTransitionImpl, object3, object2, arrayMap2, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
                if (object != null) {
                    fragmentTransitionImpl.setEpicenter(object, rect);
                }
                object2 = rect;
            } else {
                object2 = null;
            }
            OneShotPreDrawListener.add((View)viewGroup, new Runnable(fragmentTransitionImpl, arrayMap, object3, fragmentContainerTransition, arrayList2, view, fragment, fragment2, bl, arrayList, object, (Rect)object2){
                final Object val$enterTransition;
                final Object val$finalSharedElementTransition;
                final FragmentContainerTransition val$fragments;
                final FragmentTransitionImpl val$impl;
                final Rect val$inEpicenter;
                final Fragment val$inFragment;
                final boolean val$inIsPop;
                final ArrayMap val$nameOverrides;
                final View val$nonExistentView;
                final Fragment val$outFragment;
                final ArrayList val$sharedElementsIn;
                final ArrayList val$sharedElementsOut;
                {
                    this.val$impl = fragmentTransitionImpl;
                    this.val$nameOverrides = arrayMap;
                    this.val$finalSharedElementTransition = object;
                    this.val$fragments = fragmentContainerTransition;
                    this.val$sharedElementsIn = arrayList;
                    this.val$nonExistentView = view;
                    this.val$inFragment = fragment;
                    this.val$outFragment = fragment2;
                    this.val$inIsPop = bl;
                    this.val$sharedElementsOut = arrayList2;
                    this.val$enterTransition = object2;
                    this.val$inEpicenter = rect;
                }

                @Override
                public void run() {
                    ArrayMap<String, View> arrayMap = FragmentTransition.captureInSharedElements(this.val$impl, this.val$nameOverrides, this.val$finalSharedElementTransition, this.val$fragments);
                    if (arrayMap != null) {
                        this.val$sharedElementsIn.addAll(arrayMap.values());
                        this.val$sharedElementsIn.add(this.val$nonExistentView);
                    }
                    FragmentTransition.callSharedElementStartEnd(this.val$inFragment, this.val$outFragment, this.val$inIsPop, arrayMap, false);
                    Object object = this.val$finalSharedElementTransition;
                    if (object != null) {
                        this.val$impl.swapSharedElementTargets(object, this.val$sharedElementsOut, this.val$sharedElementsIn);
                        object = FragmentTransition.getInEpicenterView(arrayMap, this.val$fragments, this.val$enterTransition, this.val$inIsPop);
                        if (object != null) {
                            this.val$impl.getBoundsOnScreen((View)object, this.val$inEpicenter);
                        }
                    }
                }
            });
            return object3;
        }
        return null;
    }

    private static Object configureSharedElementsReordered(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, View view, ArrayMap<String, String> object, FragmentContainerTransition fragmentContainerTransition, ArrayList<View> arrayList, ArrayList<View> arrayList2, Object object2, Object object3) {
        Fragment fragment = fragmentContainerTransition.lastIn;
        Fragment fragment2 = fragmentContainerTransition.firstOut;
        if (fragment != null) {
            fragment.requireView().setVisibility(0);
        }
        if (fragment != null && fragment2 != null) {
            boolean bl = fragmentContainerTransition.lastInIsPop;
            Object object4 = ((SimpleArrayMap)object).isEmpty() ? null : FragmentTransition.getSharedElementTransition(fragmentTransitionImpl, fragment, fragment2, bl);
            ArrayMap<String, View> arrayMap = FragmentTransition.captureOutSharedElements(fragmentTransitionImpl, object, object4, fragmentContainerTransition);
            ArrayMap<String, View> arrayMap2 = FragmentTransition.captureInSharedElements(fragmentTransitionImpl, object, object4, fragmentContainerTransition);
            if (((SimpleArrayMap)object).isEmpty()) {
                if (arrayMap != null) {
                    arrayMap.clear();
                }
                if (arrayMap2 != null) {
                    arrayMap2.clear();
                }
                object = null;
            } else {
                FragmentTransition.addSharedElementsWithMatchingNames(arrayList, arrayMap, ((ArrayMap)object).keySet());
                FragmentTransition.addSharedElementsWithMatchingNames(arrayList2, arrayMap2, ((ArrayMap)object).values());
                object = object4;
            }
            if (object2 == null && object3 == null && object == null) {
                return null;
            }
            FragmentTransition.callSharedElementStartEnd(fragment, fragment2, bl, arrayMap, true);
            if (object != null) {
                arrayList2.add(view);
                fragmentTransitionImpl.setSharedElementTargets(object, view, arrayList);
                FragmentTransition.setOutEpicenter(fragmentTransitionImpl, object, object3, arrayMap, fragmentContainerTransition.firstOutIsPop, fragmentContainerTransition.firstOutTransaction);
                view = new Rect();
                fragmentContainerTransition = FragmentTransition.getInEpicenterView(arrayMap2, fragmentContainerTransition, object2, bl);
                if (fragmentContainerTransition != null) {
                    fragmentTransitionImpl.setEpicenter(object2, (Rect)view);
                }
            } else {
                view = null;
                fragmentContainerTransition = null;
            }
            OneShotPreDrawListener.add((View)viewGroup, new Runnable(fragment, fragment2, bl, arrayMap2, (View)fragmentContainerTransition, fragmentTransitionImpl, (Rect)view){
                final Rect val$epicenter;
                final View val$epicenterView;
                final FragmentTransitionImpl val$impl;
                final Fragment val$inFragment;
                final boolean val$inIsPop;
                final ArrayMap val$inSharedElements;
                final Fragment val$outFragment;
                {
                    this.val$inFragment = fragment;
                    this.val$outFragment = fragment2;
                    this.val$inIsPop = bl;
                    this.val$inSharedElements = arrayMap;
                    this.val$epicenterView = view;
                    this.val$impl = fragmentTransitionImpl;
                    this.val$epicenter = rect;
                }

                @Override
                public void run() {
                    FragmentTransition.callSharedElementStartEnd(this.val$inFragment, this.val$outFragment, this.val$inIsPop, this.val$inSharedElements, false);
                    View view = this.val$epicenterView;
                    if (view != null) {
                        this.val$impl.getBoundsOnScreen(view, this.val$epicenter);
                    }
                }
            });
            return object;
        }
        return null;
    }

    private static void configureTransitionsOrdered(ViewGroup viewGroup, FragmentContainerTransition object, View view, ArrayMap<String, String> arrayMap, Callback object2) {
        block4: {
            Fragment fragment = ((FragmentContainerTransition)object).firstOut;
            Fragment fragment2 = ((FragmentContainerTransition)object).lastIn;
            FragmentTransitionImpl fragmentTransitionImpl = FragmentTransition.chooseImpl(fragment, fragment2);
            if (fragmentTransitionImpl == null) {
                return;
            }
            boolean bl = ((FragmentContainerTransition)object).lastInIsPop;
            boolean bl2 = ((FragmentContainerTransition)object).firstOutIsPop;
            Object object3 = FragmentTransition.getEnterTransition(fragmentTransitionImpl, fragment2, bl);
            Object object4 = FragmentTransition.getExitTransition(fragmentTransitionImpl, fragment, bl2);
            Object object5 = new ArrayList<View>();
            ArrayList<View> arrayList = new ArrayList<View>();
            Object object6 = FragmentTransition.configureSharedElementsOrdered(fragmentTransitionImpl, viewGroup, view, arrayMap, (FragmentContainerTransition)object, object5, arrayList, object3, object4);
            if (object3 == null && object6 == null && object4 == null) {
                return;
            }
            ArrayList<View> arrayList2 = FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, object4, fragment, object5, view);
            if (arrayList2 == null || arrayList2.isEmpty()) {
                object4 = null;
            }
            fragmentTransitionImpl.addTarget(object3, view);
            object = FragmentTransition.mergeTransitions(fragmentTransitionImpl, object3, object4, object6, fragment2, ((FragmentContainerTransition)object).lastInIsPop);
            if (fragment != null && arrayList2 != null && (arrayList2.size() > 0 || ((ArrayList)object5).size() > 0)) {
                object5 = new CancellationSignal();
                object2.onStart(fragment, (CancellationSignal)object5);
                fragmentTransitionImpl.setListenerForTransitionEnd(fragment, object, (CancellationSignal)object5, new Runnable((Callback)object2, fragment, (CancellationSignal)object5){
                    final Callback val$callback;
                    final Fragment val$outFragment;
                    final CancellationSignal val$signal;
                    {
                        this.val$callback = callback;
                        this.val$outFragment = fragment;
                        this.val$signal = cancellationSignal;
                    }

                    @Override
                    public void run() {
                        this.val$callback.onComplete(this.val$outFragment, this.val$signal);
                    }
                });
            }
            if (object == null) break block4;
            object2 = new ArrayList();
            fragmentTransitionImpl.scheduleRemoveTargets(object, object3, (ArrayList<View>)object2, object4, arrayList2, object6, arrayList);
            FragmentTransition.scheduleTargetChange(fragmentTransitionImpl, viewGroup, fragment2, view, arrayList, object3, (ArrayList<View>)object2, object4, arrayList2);
            fragmentTransitionImpl.setNameOverridesOrdered((View)viewGroup, arrayList, arrayMap);
            fragmentTransitionImpl.beginDelayedTransition(viewGroup, object);
            fragmentTransitionImpl.scheduleNameReset(viewGroup, arrayList, arrayMap);
        }
    }

    private static void configureTransitionsReordered(ViewGroup viewGroup, FragmentContainerTransition object, View object2, ArrayMap<String, String> arrayMap, Callback object3) {
        block3: {
            Fragment fragment = ((FragmentContainerTransition)object).firstOut;
            Object object4 = ((FragmentContainerTransition)object).lastIn;
            FragmentTransitionImpl fragmentTransitionImpl = FragmentTransition.chooseImpl(fragment, (Fragment)object4);
            if (fragmentTransitionImpl == null) {
                return;
            }
            boolean bl = ((FragmentContainerTransition)object).lastInIsPop;
            boolean bl2 = ((FragmentContainerTransition)object).firstOutIsPop;
            ArrayList<View> arrayList = new ArrayList<View>();
            ArrayList<View> arrayList2 = new ArrayList<View>();
            Object object5 = FragmentTransition.getEnterTransition(fragmentTransitionImpl, (Fragment)object4, bl);
            Object object6 = FragmentTransition.getExitTransition(fragmentTransitionImpl, fragment, bl2);
            Object object7 = FragmentTransition.configureSharedElementsReordered(fragmentTransitionImpl, viewGroup, object2, arrayMap, (FragmentContainerTransition)object, arrayList2, arrayList, object5, object6);
            if (object5 == null && object7 == null && object6 == null) {
                return;
            }
            object = FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, object6, fragment, arrayList2, object2);
            object2 = FragmentTransition.configureEnteringExitingViews(fragmentTransitionImpl, object5, (Fragment)object4, arrayList, object2);
            FragmentTransition.setViewVisibility((ArrayList<View>)object2, 4);
            object4 = FragmentTransition.mergeTransitions(fragmentTransitionImpl, object5, object6, object7, (Fragment)object4, bl);
            if (fragment != null && object != null && (((ArrayList)object).size() > 0 || arrayList2.size() > 0)) {
                CancellationSignal cancellationSignal = new CancellationSignal();
                object3.onStart(fragment, cancellationSignal);
                fragmentTransitionImpl.setListenerForTransitionEnd(fragment, object4, cancellationSignal, new Runnable((Callback)object3, fragment, cancellationSignal){
                    final Callback val$callback;
                    final Fragment val$outFragment;
                    final CancellationSignal val$signal;
                    {
                        this.val$callback = callback;
                        this.val$outFragment = fragment;
                        this.val$signal = cancellationSignal;
                    }

                    @Override
                    public void run() {
                        this.val$callback.onComplete(this.val$outFragment, this.val$signal);
                    }
                });
            }
            if (object4 == null) break block3;
            FragmentTransition.replaceHide(fragmentTransitionImpl, object6, fragment, (ArrayList<View>)object);
            object3 = fragmentTransitionImpl.prepareSetNameOverridesReordered(arrayList);
            fragmentTransitionImpl.scheduleRemoveTargets(object4, object5, (ArrayList<View>)object2, object6, (ArrayList<View>)object, object7, arrayList);
            fragmentTransitionImpl.beginDelayedTransition(viewGroup, object4);
            fragmentTransitionImpl.setNameOverridesReordered((View)viewGroup, arrayList2, arrayList, (ArrayList<String>)object3, arrayMap);
            FragmentTransition.setViewVisibility((ArrayList<View>)object2, 0);
            fragmentTransitionImpl.swapSharedElementTargets(object7, arrayList2, arrayList);
        }
    }

    private static FragmentContainerTransition ensureContainer(FragmentContainerTransition fragmentContainerTransition, SparseArray<FragmentContainerTransition> sparseArray, int n) {
        FragmentContainerTransition fragmentContainerTransition2 = fragmentContainerTransition;
        if (fragmentContainerTransition == null) {
            fragmentContainerTransition2 = new FragmentContainerTransition();
            sparseArray.put(n, (Object)fragmentContainerTransition2);
        }
        return fragmentContainerTransition2;
    }

    static String findKeyForValue(ArrayMap<String, String> arrayMap, String string2) {
        int n = arrayMap.size();
        for (int i = 0; i < n; ++i) {
            if (!string2.equals(arrayMap.valueAt(i))) continue;
            return (String)arrayMap.keyAt(i);
        }
        return null;
    }

    private static Object getEnterTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment object, boolean bl) {
        if (object == null) {
            return null;
        }
        object = bl ? ((Fragment)object).getReenterTransition() : ((Fragment)object).getEnterTransition();
        return fragmentTransitionImpl.cloneTransition(object);
    }

    private static Object getExitTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment object, boolean bl) {
        if (object == null) {
            return null;
        }
        object = bl ? ((Fragment)object).getReturnTransition() : ((Fragment)object).getExitTransition();
        return fragmentTransitionImpl.cloneTransition(object);
    }

    static View getInEpicenterView(ArrayMap<String, View> arrayMap, FragmentContainerTransition object, Object object2, boolean bl) {
        object = ((FragmentContainerTransition)object).lastInTransaction;
        if (object2 != null && arrayMap != null && ((BackStackRecord)object).mSharedElementSourceNames != null && !((BackStackRecord)object).mSharedElementSourceNames.isEmpty()) {
            object = bl ? (String)((BackStackRecord)object).mSharedElementSourceNames.get(0) : (String)((BackStackRecord)object).mSharedElementTargetNames.get(0);
            return (View)arrayMap.get(object);
        }
        return null;
    }

    private static Object getSharedElementTransition(FragmentTransitionImpl fragmentTransitionImpl, Fragment object, Fragment fragment, boolean bl) {
        if (object != null && fragment != null) {
            object = bl ? fragment.getSharedElementReturnTransition() : ((Fragment)object).getSharedElementEnterTransition();
            return fragmentTransitionImpl.wrapTransitionInSet(fragmentTransitionImpl.cloneTransition(object));
        }
        return null;
    }

    private static Object mergeTransitions(FragmentTransitionImpl object, Object object2, Object object3, Object object4, Fragment fragment, boolean bl) {
        boolean bl2;
        boolean bl3 = bl2 = true;
        if (object2 != null) {
            bl3 = bl2;
            if (object3 != null) {
                bl3 = bl2;
                if (fragment != null) {
                    bl = bl ? fragment.getAllowReturnTransitionOverlap() : fragment.getAllowEnterTransitionOverlap();
                    bl3 = bl;
                }
            }
        }
        object = bl3 ? ((FragmentTransitionImpl)object).mergeTransitionsTogether(object3, object2, object4) : ((FragmentTransitionImpl)object).mergeTransitionsInSequence(object3, object2, object4);
        return object;
    }

    private static void replaceHide(FragmentTransitionImpl fragmentTransitionImpl, Object object, Fragment fragment, ArrayList<View> arrayList) {
        if (fragment != null && object != null && fragment.mAdded && fragment.mHidden && fragment.mHiddenChanged) {
            fragment.setHideReplaced(true);
            fragmentTransitionImpl.scheduleHideFragmentView(object, fragment.getView(), arrayList);
            OneShotPreDrawListener.add((View)fragment.mContainer, new Runnable(arrayList){
                final ArrayList val$exitingViews;
                {
                    this.val$exitingViews = arrayList;
                }

                @Override
                public void run() {
                    FragmentTransition.setViewVisibility(this.val$exitingViews, 4);
                }
            });
        }
    }

    private static FragmentTransitionImpl resolveSupportImpl() {
        try {
            FragmentTransitionImpl fragmentTransitionImpl = (FragmentTransitionImpl)Class.forName("androidx.transition.FragmentTransitionSupport").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
            return fragmentTransitionImpl;
        }
        catch (Exception exception) {
            return null;
        }
    }

    static void retainValues(ArrayMap<String, String> arrayMap, ArrayMap<String, View> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            if (arrayMap2.containsKey((String)arrayMap.valueAt(i))) continue;
            arrayMap.removeAt(i);
        }
    }

    private static void scheduleTargetChange(FragmentTransitionImpl fragmentTransitionImpl, ViewGroup viewGroup, Fragment fragment, View view, ArrayList<View> arrayList, Object object, ArrayList<View> arrayList2, Object object2, ArrayList<View> arrayList3) {
        OneShotPreDrawListener.add((View)viewGroup, new Runnable(object, fragmentTransitionImpl, view, fragment, arrayList, arrayList2, arrayList3, object2){
            final Object val$enterTransition;
            final ArrayList val$enteringViews;
            final Object val$exitTransition;
            final ArrayList val$exitingViews;
            final FragmentTransitionImpl val$impl;
            final Fragment val$inFragment;
            final View val$nonExistentView;
            final ArrayList val$sharedElementsIn;
            {
                this.val$enterTransition = object;
                this.val$impl = fragmentTransitionImpl;
                this.val$nonExistentView = view;
                this.val$inFragment = fragment;
                this.val$sharedElementsIn = arrayList;
                this.val$enteringViews = arrayList2;
                this.val$exitingViews = arrayList3;
                this.val$exitTransition = object2;
            }

            @Override
            public void run() {
                ArrayList<View> arrayList = this.val$enterTransition;
                if (arrayList != null) {
                    this.val$impl.removeTarget(arrayList, this.val$nonExistentView);
                    arrayList = FragmentTransition.configureEnteringExitingViews(this.val$impl, this.val$enterTransition, this.val$inFragment, this.val$sharedElementsIn, this.val$nonExistentView);
                    this.val$enteringViews.addAll(arrayList);
                }
                if (this.val$exitingViews != null) {
                    if (this.val$exitTransition != null) {
                        arrayList = new ArrayList<View>();
                        arrayList.add(this.val$nonExistentView);
                        this.val$impl.replaceTargets(this.val$exitTransition, this.val$exitingViews, arrayList);
                    }
                    this.val$exitingViews.clear();
                    this.val$exitingViews.add(this.val$nonExistentView);
                }
            }
        });
    }

    private static void setOutEpicenter(FragmentTransitionImpl fragmentTransitionImpl, Object object, Object object2, ArrayMap<String, View> view, boolean bl, BackStackRecord object3) {
        if (((BackStackRecord)object3).mSharedElementSourceNames != null && !((BackStackRecord)object3).mSharedElementSourceNames.isEmpty()) {
            object3 = bl ? (String)((BackStackRecord)object3).mSharedElementTargetNames.get(0) : (String)((BackStackRecord)object3).mSharedElementSourceNames.get(0);
            view = (View)view.get(object3);
            fragmentTransitionImpl.setEpicenter(object, view);
            if (object2 != null) {
                fragmentTransitionImpl.setEpicenter(object2, view);
            }
        }
    }

    static void setViewVisibility(ArrayList<View> arrayList, int n) {
        if (arrayList == null) {
            return;
        }
        for (int i = arrayList.size() - 1; i >= 0; --i) {
            arrayList.get(i).setVisibility(n);
        }
    }

    static void startTransitions(Context context, FragmentContainer fragmentContainer, ArrayList<BackStackRecord> arrayList, ArrayList<Boolean> arrayList2, int n, int n2, boolean bl, Callback callback) {
        Object object;
        int n3;
        SparseArray sparseArray = new SparseArray();
        for (n3 = n; n3 < n2; ++n3) {
            object = arrayList.get(n3);
            if (arrayList2.get(n3).booleanValue()) {
                FragmentTransition.calculatePopFragments((BackStackRecord)object, (SparseArray<FragmentContainerTransition>)sparseArray, bl);
                continue;
            }
            FragmentTransition.calculateFragments((BackStackRecord)object, (SparseArray<FragmentContainerTransition>)sparseArray, bl);
        }
        if (sparseArray.size() != 0) {
            context = new View(context);
            int n4 = sparseArray.size();
            for (n3 = 0; n3 < n4; ++n3) {
                ViewGroup viewGroup;
                int n5 = sparseArray.keyAt(n3);
                ArrayMap<String, String> arrayMap = FragmentTransition.calculateNameOverrides(n5, arrayList, arrayList2, n, n2);
                object = (FragmentContainerTransition)sparseArray.valueAt(n3);
                if (!fragmentContainer.onHasView() || (viewGroup = (ViewGroup)fragmentContainer.onFindViewById(n5)) == null) continue;
                if (bl) {
                    FragmentTransition.configureTransitionsReordered(viewGroup, (FragmentContainerTransition)object, (View)context, arrayMap, callback);
                    continue;
                }
                FragmentTransition.configureTransitionsOrdered(viewGroup, (FragmentContainerTransition)object, (View)context, arrayMap, callback);
            }
        }
    }

    static boolean supportsTransition() {
        boolean bl = PLATFORM_IMPL != null || SUPPORT_IMPL != null;
        return bl;
    }

    static interface Callback {
        public void onComplete(Fragment var1, CancellationSignal var2);

        public void onStart(Fragment var1, CancellationSignal var2);
    }

    static class FragmentContainerTransition {
        public Fragment firstOut;
        public boolean firstOutIsPop;
        public BackStackRecord firstOutTransaction;
        public Fragment lastIn;
        public boolean lastInIsPop;
        public BackStackRecord lastInTransaction;

        FragmentContainerTransition() {
        }
    }
}

