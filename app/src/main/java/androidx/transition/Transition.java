/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.animation.AnimatorListenerAdapter
 *  android.animation.TimeInterpolator
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.content.res.XmlResourceParser
 *  android.graphics.Path
 *  android.graphics.Rect
 *  android.util.AttributeSet
 *  android.util.SparseArray
 *  android.util.SparseIntArray
 *  android.view.InflateException
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.AnimationUtils
 *  android.widget.ListView
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.transition.AnimatorUtils;
import androidx.transition.PathMotion;
import androidx.transition.Styleable;
import androidx.transition.TransitionPropagation;
import androidx.transition.TransitionSet;
import androidx.transition.TransitionValues;
import androidx.transition.TransitionValuesMaps;
import androidx.transition.ViewUtils;
import androidx.transition.WindowIdImpl;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import org.xmlpull.v1.XmlPullParser;

public abstract class Transition
implements Cloneable {
    static final boolean DBG = false;
    private static final int[] DEFAULT_MATCH_ORDER = new int[]{2, 1, 3, 4};
    private static final String LOG_TAG = "Transition";
    private static final int MATCH_FIRST = 1;
    public static final int MATCH_ID = 3;
    private static final String MATCH_ID_STR = "id";
    public static final int MATCH_INSTANCE = 1;
    private static final String MATCH_INSTANCE_STR = "instance";
    public static final int MATCH_ITEM_ID = 4;
    private static final String MATCH_ITEM_ID_STR = "itemId";
    private static final int MATCH_LAST = 4;
    public static final int MATCH_NAME = 2;
    private static final String MATCH_NAME_STR = "name";
    private static final PathMotion STRAIGHT_PATH_MOTION = new PathMotion(){

        @Override
        public Path getPath(float f, float f2, float f3, float f4) {
            Path path = new Path();
            path.moveTo(f, f2);
            path.lineTo(f3, f4);
            return path;
        }
    };
    private static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal();
    private ArrayList<Animator> mAnimators;
    boolean mCanRemoveViews = false;
    ArrayList<Animator> mCurrentAnimators;
    long mDuration = -1L;
    private TransitionValuesMaps mEndValues;
    private ArrayList<TransitionValues> mEndValuesList;
    private boolean mEnded = false;
    private EpicenterCallback mEpicenterCallback;
    private TimeInterpolator mInterpolator = null;
    private ArrayList<TransitionListener> mListeners = null;
    private int[] mMatchOrder;
    private String mName = this.getClass().getName();
    private ArrayMap<String, String> mNameOverrides;
    private int mNumInstances = 0;
    TransitionSet mParent = null;
    private PathMotion mPathMotion;
    private boolean mPaused = false;
    TransitionPropagation mPropagation;
    private ViewGroup mSceneRoot = null;
    private long mStartDelay = -1L;
    private TransitionValuesMaps mStartValues;
    private ArrayList<TransitionValues> mStartValuesList;
    private ArrayList<View> mTargetChildExcludes = null;
    private ArrayList<View> mTargetExcludes = null;
    private ArrayList<Integer> mTargetIdChildExcludes = null;
    private ArrayList<Integer> mTargetIdExcludes = null;
    ArrayList<Integer> mTargetIds = new ArrayList();
    private ArrayList<String> mTargetNameExcludes = null;
    private ArrayList<String> mTargetNames = null;
    private ArrayList<Class> mTargetTypeChildExcludes = null;
    private ArrayList<Class> mTargetTypeExcludes = null;
    private ArrayList<Class> mTargetTypes = null;
    ArrayList<View> mTargets = new ArrayList();

    public Transition() {
        this.mStartValues = new TransitionValuesMaps();
        this.mEndValues = new TransitionValuesMaps();
        this.mMatchOrder = DEFAULT_MATCH_ORDER;
        this.mCurrentAnimators = new ArrayList();
        this.mAnimators = new ArrayList();
        this.mPathMotion = STRAIGHT_PATH_MOTION;
    }

    public Transition(Context object, AttributeSet attributeSet) {
        int n;
        this.mStartValues = new TransitionValuesMaps();
        this.mEndValues = new TransitionValuesMaps();
        this.mMatchOrder = DEFAULT_MATCH_ORDER;
        this.mCurrentAnimators = new ArrayList();
        this.mAnimators = new ArrayList();
        this.mPathMotion = STRAIGHT_PATH_MOTION;
        TypedArray typedArray = object.obtainStyledAttributes(attributeSet, Styleable.TRANSITION);
        attributeSet = (XmlResourceParser)attributeSet;
        long l = TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)attributeSet, "duration", 1, -1);
        if (l >= 0L) {
            this.setDuration(l);
        }
        if ((l = (long)TypedArrayUtils.getNamedInt(typedArray, (XmlPullParser)attributeSet, "startDelay", 2, -1)) > 0L) {
            this.setStartDelay(l);
        }
        if ((n = TypedArrayUtils.getNamedResourceId(typedArray, (XmlPullParser)attributeSet, "interpolator", 0, 0)) > 0) {
            this.setInterpolator((TimeInterpolator)AnimationUtils.loadInterpolator((Context)object, (int)n));
        }
        if ((object = TypedArrayUtils.getNamedString(typedArray, (XmlPullParser)attributeSet, "matchOrder", 3)) != null) {
            this.setMatchOrder(Transition.parseMatchOrder((String)object));
        }
        typedArray.recycle();
    }

    private void addUnmatched(ArrayMap<View, TransitionValues> object, ArrayMap<View, TransitionValues> arrayMap) {
        int n;
        for (n = 0; n < ((SimpleArrayMap)object).size(); ++n) {
            TransitionValues transitionValues = (TransitionValues)((SimpleArrayMap)object).valueAt(n);
            if (!this.isValidTarget(transitionValues.view)) continue;
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add(null);
        }
        for (n = 0; n < arrayMap.size(); ++n) {
            object = (TransitionValues)arrayMap.valueAt(n);
            if (!this.isValidTarget(((TransitionValues)object).view)) continue;
            this.mEndValuesList.add((TransitionValues)object);
            this.mStartValuesList.add(null);
        }
    }

    private static void addViewValues(TransitionValuesMaps transitionValuesMaps, View view, TransitionValues object) {
        transitionValuesMaps.mViewValues.put(view, (TransitionValues)object);
        int n = view.getId();
        if (n >= 0) {
            if (transitionValuesMaps.mIdValues.indexOfKey(n) >= 0) {
                transitionValuesMaps.mIdValues.put(n, null);
            } else {
                transitionValuesMaps.mIdValues.put(n, (Object)view);
            }
        }
        if ((object = ViewCompat.getTransitionName(view)) != null) {
            if (transitionValuesMaps.mNameValues.containsKey(object)) {
                transitionValuesMaps.mNameValues.put((String)object, null);
            } else {
                transitionValuesMaps.mNameValues.put((String)object, view);
            }
        }
        if (view.getParent() instanceof ListView && (object = (ListView)view.getParent()).getAdapter().hasStableIds()) {
            long l = object.getItemIdAtPosition(object.getPositionForView(view));
            if (transitionValuesMaps.mItemIdValues.indexOfKey(l) >= 0) {
                view = transitionValuesMaps.mItemIdValues.get(l);
                if (view != null) {
                    ViewCompat.setHasTransientState(view, false);
                    transitionValuesMaps.mItemIdValues.put(l, null);
                }
            } else {
                ViewCompat.setHasTransientState(view, true);
                transitionValuesMaps.mItemIdValues.put(l, view);
            }
        }
    }

    private static boolean alreadyContains(int[] nArray, int n) {
        int n2 = nArray[n];
        for (int i = 0; i < n; ++i) {
            if (nArray[i] != n2) continue;
            return true;
        }
        return false;
    }

    private void captureHierarchy(View view, boolean bl) {
        int n;
        if (view == null) {
            return;
        }
        int n2 = view.getId();
        ArrayList<Constable> arrayList = this.mTargetIdExcludes;
        if (arrayList != null && arrayList.contains(n2)) {
            return;
        }
        arrayList = this.mTargetExcludes;
        if (arrayList != null && arrayList.contains(view)) {
            return;
        }
        arrayList = this.mTargetTypeExcludes;
        if (arrayList != null) {
            int n3 = arrayList.size();
            for (n = 0; n < n3; ++n) {
                if (!this.mTargetTypeExcludes.get(n).isInstance(view)) continue;
                return;
            }
        }
        if (view.getParent() instanceof ViewGroup) {
            arrayList = new TransitionValues();
            ((TransitionValues)((Object)arrayList)).view = view;
            if (bl) {
                this.captureStartValues((TransitionValues)((Object)arrayList));
            } else {
                this.captureEndValues((TransitionValues)((Object)arrayList));
            }
            ((TransitionValues)((Object)arrayList)).mTargetedTransitions.add(this);
            this.capturePropagationValues((TransitionValues)((Object)arrayList));
            if (bl) {
                Transition.addViewValues(this.mStartValues, view, arrayList);
            } else {
                Transition.addViewValues(this.mEndValues, view, arrayList);
            }
        }
        if (view instanceof ViewGroup) {
            arrayList = this.mTargetIdChildExcludes;
            if (arrayList != null && arrayList.contains(n2)) {
                return;
            }
            arrayList = this.mTargetChildExcludes;
            if (arrayList != null && arrayList.contains(view)) {
                return;
            }
            arrayList = this.mTargetTypeChildExcludes;
            if (arrayList != null) {
                n2 = arrayList.size();
                for (n = 0; n < n2; ++n) {
                    if (!this.mTargetTypeChildExcludes.get(n).isInstance(view)) continue;
                    return;
                }
            }
            view = (ViewGroup)view;
            for (n = 0; n < view.getChildCount(); ++n) {
                this.captureHierarchy(view.getChildAt(n), bl);
            }
        }
    }

    private ArrayList<Integer> excludeId(ArrayList<Integer> arrayList, int n, boolean bl) {
        ArrayList<Integer> arrayList2 = arrayList;
        if (n > 0) {
            arrayList2 = bl ? ArrayListManager.add(arrayList, n) : ArrayListManager.remove(arrayList, n);
        }
        return arrayList2;
    }

    private static <T> ArrayList<T> excludeObject(ArrayList<T> arrayList, T t, boolean bl) {
        ArrayList<T> arrayList2 = arrayList;
        if (t != null) {
            arrayList2 = bl ? ArrayListManager.add(arrayList, t) : ArrayListManager.remove(arrayList, t);
        }
        return arrayList2;
    }

    private ArrayList<Class> excludeType(ArrayList<Class> arrayList, Class clazz, boolean bl) {
        ArrayList<Class> arrayList2 = arrayList;
        if (clazz != null) {
            arrayList2 = bl ? ArrayListManager.add(arrayList, clazz) : ArrayListManager.remove(arrayList, clazz);
        }
        return arrayList2;
    }

    private ArrayList<View> excludeView(ArrayList<View> arrayList, View view, boolean bl) {
        ArrayList<View> arrayList2 = arrayList;
        if (view != null) {
            arrayList2 = bl ? ArrayListManager.add(arrayList, view) : ArrayListManager.remove(arrayList, view);
        }
        return arrayList2;
    }

    private static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap<Animator, AnimationInfo> arrayMap;
        ArrayMap<Object, AnimationInfo> arrayMap2 = arrayMap = sRunningAnimators.get();
        if (arrayMap == null) {
            arrayMap2 = new ArrayMap();
            sRunningAnimators.set(arrayMap2);
        }
        return arrayMap2;
    }

    private static boolean isValidMatch(int n) {
        boolean bl = true;
        if (n < 1 || n > 4) {
            bl = false;
        }
        return bl;
    }

    private static boolean isValueChanged(TransitionValues object, TransitionValues object2, String string2) {
        object = ((TransitionValues)object).values.get(string2);
        object2 = ((TransitionValues)object2).values.get(string2);
        boolean bl = object == null && object2 == null ? false : (object != null && object2 != null ? object.equals(object2) ^ true : true);
        return bl;
    }

    private void matchIds(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, SparseArray<View> sparseArray, SparseArray<View> sparseArray2) {
        int n = sparseArray.size();
        for (int i = 0; i < n; ++i) {
            View view;
            View view2 = (View)sparseArray.valueAt(i);
            if (view2 == null || !this.isValidTarget(view2) || (view = (View)sparseArray2.get(sparseArray.keyAt(i))) == null || !this.isValidTarget(view)) continue;
            TransitionValues transitionValues = (TransitionValues)arrayMap.get(view2);
            TransitionValues transitionValues2 = (TransitionValues)arrayMap2.get(view);
            if (transitionValues == null || transitionValues2 == null) continue;
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add(transitionValues2);
            arrayMap.remove(view2);
            arrayMap2.remove(view);
        }
    }

    private void matchInstances(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2) {
        for (int i = arrayMap.size() - 1; i >= 0; --i) {
            TransitionValues transitionValues;
            Object object = (View)arrayMap.keyAt(i);
            if (object == null || !this.isValidTarget((View)object) || (transitionValues = (TransitionValues)arrayMap2.remove(object)) == null || transitionValues.view == null || !this.isValidTarget(transitionValues.view)) continue;
            object = (TransitionValues)arrayMap.removeAt(i);
            this.mStartValuesList.add((TransitionValues)object);
            this.mEndValuesList.add(transitionValues);
        }
    }

    private void matchItemIds(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, LongSparseArray<View> longSparseArray, LongSparseArray<View> longSparseArray2) {
        int n = longSparseArray.size();
        for (int i = 0; i < n; ++i) {
            View view;
            View view2 = longSparseArray.valueAt(i);
            if (view2 == null || !this.isValidTarget(view2) || (view = longSparseArray2.get(longSparseArray.keyAt(i))) == null || !this.isValidTarget(view)) continue;
            TransitionValues transitionValues = (TransitionValues)arrayMap.get(view2);
            TransitionValues transitionValues2 = (TransitionValues)arrayMap2.get(view);
            if (transitionValues == null || transitionValues2 == null) continue;
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add(transitionValues2);
            arrayMap.remove(view2);
            arrayMap2.remove(view);
        }
    }

    private void matchNames(ArrayMap<View, TransitionValues> arrayMap, ArrayMap<View, TransitionValues> arrayMap2, ArrayMap<String, View> arrayMap3, ArrayMap<String, View> arrayMap4) {
        int n = arrayMap3.size();
        for (int i = 0; i < n; ++i) {
            View view;
            View view2 = (View)arrayMap3.valueAt(i);
            if (view2 == null || !this.isValidTarget(view2) || (view = (View)arrayMap4.get(arrayMap3.keyAt(i))) == null || !this.isValidTarget(view)) continue;
            TransitionValues transitionValues = (TransitionValues)arrayMap.get(view2);
            TransitionValues transitionValues2 = (TransitionValues)arrayMap2.get(view);
            if (transitionValues == null || transitionValues2 == null) continue;
            this.mStartValuesList.add(transitionValues);
            this.mEndValuesList.add(transitionValues2);
            arrayMap.remove(view2);
            arrayMap2.remove(view);
        }
    }

    private void matchStartAndEnd(TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2) {
        int[] nArray;
        ArrayMap<View, TransitionValues> arrayMap = new ArrayMap<View, TransitionValues>(transitionValuesMaps.mViewValues);
        ArrayMap<View, TransitionValues> arrayMap2 = new ArrayMap<View, TransitionValues>(transitionValuesMaps2.mViewValues);
        block6: for (int i = 0; i < (nArray = this.mMatchOrder).length; ++i) {
            switch (nArray[i]) {
                default: {
                    continue block6;
                }
                case 4: {
                    this.matchItemIds(arrayMap, arrayMap2, transitionValuesMaps.mItemIdValues, transitionValuesMaps2.mItemIdValues);
                    continue block6;
                }
                case 3: {
                    this.matchIds(arrayMap, arrayMap2, transitionValuesMaps.mIdValues, transitionValuesMaps2.mIdValues);
                    continue block6;
                }
                case 2: {
                    this.matchNames(arrayMap, arrayMap2, transitionValuesMaps.mNameValues, transitionValuesMaps2.mNameValues);
                    continue block6;
                }
                case 1: {
                    this.matchInstances(arrayMap, arrayMap2);
                }
            }
        }
        this.addUnmatched(arrayMap, arrayMap2);
    }

    private static int[] parseMatchOrder(String object) {
        StringTokenizer stringTokenizer = new StringTokenizer((String)object, ",");
        object = new int[stringTokenizer.countTokens()];
        int n = 0;
        while (stringTokenizer.hasMoreTokens()) {
            Object object2;
            block8: {
                block4: {
                    block7: {
                        block6: {
                            block5: {
                                block3: {
                                    object2 = stringTokenizer.nextToken().trim();
                                    if (!MATCH_ID_STR.equalsIgnoreCase((String)object2)) break block3;
                                    object[n] = 3;
                                    break block4;
                                }
                                if (!MATCH_INSTANCE_STR.equalsIgnoreCase((String)object2)) break block5;
                                object[n] = true;
                                break block4;
                            }
                            if (!MATCH_NAME_STR.equalsIgnoreCase((String)object2)) break block6;
                            object[n] = 2;
                            break block4;
                        }
                        if (!MATCH_ITEM_ID_STR.equalsIgnoreCase((String)object2)) break block7;
                        object[n] = 4;
                        break block4;
                    }
                    if (!((String)object2).isEmpty()) break block8;
                    object2 = new int[((Object)object).length - 1];
                    System.arraycopy(object, 0, object2, 0, n);
                    object = object2;
                    --n;
                }
                ++n;
                continue;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unknown match type in matchOrder: '");
            ((StringBuilder)object).append((String)object2);
            ((StringBuilder)object).append("'");
            throw new InflateException(((StringBuilder)object).toString());
        }
        return object;
    }

    private void runAnimator(Animator animator2, ArrayMap<Animator, AnimationInfo> arrayMap) {
        if (animator2 != null) {
            animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this, arrayMap){
                final Transition this$0;
                final ArrayMap val$runningAnimators;
                {
                    this.this$0 = transition;
                    this.val$runningAnimators = arrayMap;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.val$runningAnimators.remove(animator2);
                    this.this$0.mCurrentAnimators.remove(animator2);
                }

                public void onAnimationStart(Animator animator2) {
                    this.this$0.mCurrentAnimators.add(animator2);
                }
            });
            this.animate(animator2);
        }
    }

    public Transition addListener(TransitionListener transitionListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(transitionListener);
        return this;
    }

    public Transition addTarget(int n) {
        if (n != 0) {
            this.mTargetIds.add(n);
        }
        return this;
    }

    public Transition addTarget(View view) {
        this.mTargets.add(view);
        return this;
    }

    public Transition addTarget(Class clazz) {
        if (this.mTargetTypes == null) {
            this.mTargetTypes = new ArrayList();
        }
        this.mTargetTypes.add(clazz);
        return this;
    }

    public Transition addTarget(String string2) {
        if (this.mTargetNames == null) {
            this.mTargetNames = new ArrayList();
        }
        this.mTargetNames.add(string2);
        return this;
    }

    protected void animate(Animator animator2) {
        if (animator2 == null) {
            this.end();
        } else {
            if (this.getDuration() >= 0L) {
                animator2.setDuration(this.getDuration());
            }
            if (this.getStartDelay() >= 0L) {
                animator2.setStartDelay(this.getStartDelay());
            }
            if (this.getInterpolator() != null) {
                animator2.setInterpolator(this.getInterpolator());
            }
            animator2.addListener((Animator.AnimatorListener)new AnimatorListenerAdapter(this){
                final Transition this$0;
                {
                    this.this$0 = transition;
                }

                public void onAnimationEnd(Animator animator2) {
                    this.this$0.end();
                    animator2.removeListener((Animator.AnimatorListener)this);
                }
            });
            animator2.start();
        }
    }

    protected void cancel() {
        int n;
        for (n = this.mCurrentAnimators.size() - 1; n >= 0; --n) {
            this.mCurrentAnimators.get(n).cancel();
        }
        ArrayList arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
            arrayList = (ArrayList)this.mListeners.clone();
            int n2 = arrayList.size();
            for (n = 0; n < n2; ++n) {
                ((TransitionListener)arrayList.get(n)).onTransitionCancel(this);
            }
        }
    }

    public abstract void captureEndValues(TransitionValues var1);

    void capturePropagationValues(TransitionValues transitionValues) {
        if (this.mPropagation != null && !transitionValues.values.isEmpty()) {
            boolean bl;
            String[] stringArray = this.mPropagation.getPropagationProperties();
            if (stringArray == null) {
                return;
            }
            boolean bl2 = true;
            int n = 0;
            while (true) {
                bl = bl2;
                if (n >= stringArray.length) break;
                if (!transitionValues.values.containsKey(stringArray[n])) {
                    bl = false;
                    break;
                }
                ++n;
            }
            if (!bl) {
                this.mPropagation.captureValues(transitionValues);
            }
        }
    }

    public abstract void captureStartValues(TransitionValues var1);

    void captureValues(ViewGroup object, boolean bl) {
        TransitionValues transitionValues;
        int n;
        Object object2;
        this.clearValues(bl);
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0 || (object2 = this.mTargetNames) != null && !((ArrayList)object2).isEmpty() || (object2 = this.mTargetTypes) != null && !((ArrayList)object2).isEmpty()) {
            this.captureHierarchy((View)object, bl);
        } else {
            for (n = 0; n < this.mTargetIds.size(); ++n) {
                object2 = object.findViewById(this.mTargetIds.get(n).intValue());
                if (object2 == null) continue;
                transitionValues = new TransitionValues();
                transitionValues.view = object2;
                if (bl) {
                    this.captureStartValues(transitionValues);
                } else {
                    this.captureEndValues(transitionValues);
                }
                transitionValues.mTargetedTransitions.add(this);
                this.capturePropagationValues(transitionValues);
                if (bl) {
                    Transition.addViewValues(this.mStartValues, (View)object2, transitionValues);
                    continue;
                }
                Transition.addViewValues(this.mEndValues, (View)object2, transitionValues);
            }
            for (n = 0; n < this.mTargets.size(); ++n) {
                object = this.mTargets.get(n);
                object2 = new TransitionValues();
                ((TransitionValues)object2).view = object;
                if (bl) {
                    this.captureStartValues((TransitionValues)object2);
                } else {
                    this.captureEndValues((TransitionValues)object2);
                }
                ((TransitionValues)object2).mTargetedTransitions.add(this);
                this.capturePropagationValues((TransitionValues)object2);
                if (bl) {
                    Transition.addViewValues(this.mStartValues, (View)object, (TransitionValues)object2);
                    continue;
                }
                Transition.addViewValues(this.mEndValues, (View)object, (TransitionValues)object2);
            }
        }
        if (!bl && (object = this.mNameOverrides) != null) {
            int n2 = ((SimpleArrayMap)object).size();
            object = new ArrayList(n2);
            for (n = 0; n < n2; ++n) {
                object2 = (String)this.mNameOverrides.keyAt(n);
                ((ArrayList)object).add(this.mStartValues.mNameValues.remove(object2));
            }
            for (n = 0; n < n2; ++n) {
                transitionValues = (View)((ArrayList)object).get(n);
                if (transitionValues == null) continue;
                object2 = (String)this.mNameOverrides.valueAt(n);
                this.mStartValues.mNameValues.put((String)object2, (View)transitionValues);
            }
        }
    }

    void clearValues(boolean bl) {
        if (bl) {
            this.mStartValues.mViewValues.clear();
            this.mStartValues.mIdValues.clear();
            this.mStartValues.mItemIdValues.clear();
        } else {
            this.mEndValues.mViewValues.clear();
            this.mEndValues.mIdValues.clear();
            this.mEndValues.mItemIdValues.clear();
        }
    }

    public Transition clone() {
        try {
            Transition transition = (Transition)super.clone();
            Object object = new ArrayList();
            transition.mAnimators = object;
            transition.mStartValues = object = new TransitionValuesMaps();
            transition.mEndValues = object = new TransitionValuesMaps();
            transition.mStartValuesList = null;
            transition.mEndValuesList = null;
            return transition;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            return null;
        }
    }

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    protected void createAnimators(ViewGroup viewGroup, TransitionValuesMaps object, TransitionValuesMaps transitionValuesMaps, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        int n;
        int n2;
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        long l = Long.MAX_VALUE;
        SparseIntArray sparseIntArray = new SparseIntArray();
        int n3 = arrayList.size();
        for (n2 = 0; n2 < n3; ++n2) {
            long l2;
            TransitionValues transitionValues = arrayList.get(n2);
            TransitionValues transitionValues2 = arrayList2.get(n2);
            if (transitionValues != null && !transitionValues.mTargetedTransitions.contains(this)) {
                transitionValues = null;
            }
            if (transitionValues2 != null && !transitionValues2.mTargetedTransitions.contains(this)) {
                transitionValues2 = null;
            }
            if (transitionValues == null && transitionValues2 == null) {
                l2 = l;
            } else {
                n = transitionValues != null && transitionValues2 != null && !this.isTransitionRequired(transitionValues, transitionValues2) ? 0 : 1;
                if (n != 0) {
                    object = this.createAnimator(viewGroup, transitionValues, transitionValues2);
                    if (object != null) {
                        Object object2;
                        Object object3;
                        Object object4 = null;
                        if (transitionValues2 != null) {
                            block20: {
                                object3 = transitionValues2.view;
                                String[] stringArray = this.getTransitionProperties();
                                if (object3 != null && stringArray != null && stringArray.length > 0) {
                                    object2 = new TransitionValues();
                                    object2.view = object3;
                                    object4 = (TransitionValues)transitionValuesMaps.mViewValues.get(object3);
                                    if (object4 != null) {
                                        for (n = 0; n < stringArray.length; ++n) {
                                            object2.values.put(stringArray[n], ((TransitionValues)object4).values.get(stringArray[n]));
                                        }
                                    }
                                    n = arrayMap.size();
                                    for (int i = 0; i < n; ++i) {
                                        object4 = (AnimationInfo)arrayMap.get((Animator)arrayMap.keyAt(i));
                                        if (((AnimationInfo)object4).mValues == null || ((AnimationInfo)object4).mView != object3 || !((AnimationInfo)object4).mName.equals(this.getName()) || !((AnimationInfo)object4).mValues.equals(object2)) continue;
                                        object = object2;
                                        object2 = null;
                                        break block20;
                                    }
                                    object4 = object2;
                                    object2 = object;
                                    object = object4;
                                } else {
                                    object2 = object;
                                    object = object4;
                                }
                            }
                            object4 = object;
                            object = object2;
                            object2 = object3;
                            n = n2;
                        } else {
                            object2 = transitionValues.view;
                            object4 = null;
                            n = n2;
                        }
                        l2 = l;
                        n2 = n;
                        if (object != null) {
                            object3 = this.mPropagation;
                            if (object3 != null) {
                                l2 = ((TransitionPropagation)object3).getStartDelay(viewGroup, this, transitionValues, transitionValues2);
                                sparseIntArray.put(this.mAnimators.size(), (int)l2);
                                l = Math.min(l2, l);
                            }
                            arrayMap.put((Animator)object, new AnimationInfo((View)object2, this.getName(), this, ViewUtils.getWindowId((View)viewGroup), (TransitionValues)object4));
                            this.mAnimators.add((Animator)object);
                            l2 = l;
                            n2 = n;
                        }
                    } else {
                        l2 = l;
                    }
                } else {
                    l2 = l;
                }
            }
            l = l2;
        }
        if (l != 0L) {
            for (n2 = 0; n2 < sparseIntArray.size(); ++n2) {
                n = sparseIntArray.keyAt(n2);
                viewGroup = this.mAnimators.get(n);
                viewGroup.setStartDelay((long)sparseIntArray.valueAt(n2) - l + viewGroup.getStartDelay());
            }
        }
    }

    protected void end() {
        int n;
        this.mNumInstances = n = this.mNumInstances - 1;
        if (n == 0) {
            ArrayList arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                arrayList = (ArrayList)this.mListeners.clone();
                int n2 = arrayList.size();
                for (n = 0; n < n2; ++n) {
                    ((TransitionListener)arrayList.get(n)).onTransitionEnd(this);
                }
            }
            for (n = 0; n < this.mStartValues.mItemIdValues.size(); ++n) {
                arrayList = this.mStartValues.mItemIdValues.valueAt(n);
                if (arrayList == null) continue;
                ViewCompat.setHasTransientState((View)arrayList, false);
            }
            for (n = 0; n < this.mEndValues.mItemIdValues.size(); ++n) {
                arrayList = this.mEndValues.mItemIdValues.valueAt(n);
                if (arrayList == null) continue;
                ViewCompat.setHasTransientState((View)arrayList, false);
            }
            this.mEnded = true;
        }
    }

    public Transition excludeChildren(int n, boolean bl) {
        this.mTargetIdChildExcludes = this.excludeId(this.mTargetIdChildExcludes, n, bl);
        return this;
    }

    public Transition excludeChildren(View view, boolean bl) {
        this.mTargetChildExcludes = this.excludeView(this.mTargetChildExcludes, view, bl);
        return this;
    }

    public Transition excludeChildren(Class clazz, boolean bl) {
        this.mTargetTypeChildExcludes = this.excludeType(this.mTargetTypeChildExcludes, clazz, bl);
        return this;
    }

    public Transition excludeTarget(int n, boolean bl) {
        this.mTargetIdExcludes = this.excludeId(this.mTargetIdExcludes, n, bl);
        return this;
    }

    public Transition excludeTarget(View view, boolean bl) {
        this.mTargetExcludes = this.excludeView(this.mTargetExcludes, view, bl);
        return this;
    }

    public Transition excludeTarget(Class clazz, boolean bl) {
        this.mTargetTypeExcludes = this.excludeType(this.mTargetTypeExcludes, clazz, bl);
        return this;
    }

    public Transition excludeTarget(String string2, boolean bl) {
        this.mTargetNameExcludes = Transition.excludeObject(this.mTargetNameExcludes, string2, bl);
        return this;
    }

    void forceToEnd(ViewGroup object) {
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        int n = arrayMap.size();
        if (object != null) {
            object = ViewUtils.getWindowId((View)object);
            --n;
            while (n >= 0) {
                AnimationInfo animationInfo = (AnimationInfo)arrayMap.valueAt(n);
                if (animationInfo.mView != null && object != null && object.equals(animationInfo.mWindowId)) {
                    ((Animator)arrayMap.keyAt(n)).end();
                }
                --n;
            }
        }
    }

    public long getDuration() {
        return this.mDuration;
    }

    public Rect getEpicenter() {
        EpicenterCallback epicenterCallback = this.mEpicenterCallback;
        if (epicenterCallback == null) {
            return null;
        }
        return epicenterCallback.onGetEpicenter(this);
    }

    public EpicenterCallback getEpicenterCallback() {
        return this.mEpicenterCallback;
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    TransitionValues getMatchedTransitionValues(View object, boolean bl) {
        int n;
        Cloneable cloneable = this.mParent;
        if (cloneable != null) {
            return ((Transition)cloneable).getMatchedTransitionValues((View)object, bl);
        }
        cloneable = bl ? this.mStartValuesList : this.mEndValuesList;
        if (cloneable == null) {
            return null;
        }
        int n2 = ((ArrayList)cloneable).size();
        int n3 = -1;
        int n4 = 0;
        while (true) {
            n = n3;
            if (n4 >= n2) break;
            TransitionValues transitionValues = (TransitionValues)((ArrayList)cloneable).get(n4);
            if (transitionValues == null) {
                return null;
            }
            if (transitionValues.view == object) {
                n = n4;
                break;
            }
            ++n4;
        }
        object = null;
        if (n >= 0) {
            object = bl ? this.mEndValuesList : this.mStartValuesList;
            object = (TransitionValues)((ArrayList)object).get(n);
        }
        return object;
    }

    public String getName() {
        return this.mName;
    }

    public PathMotion getPathMotion() {
        return this.mPathMotion;
    }

    public TransitionPropagation getPropagation() {
        return this.mPropagation;
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public List<Integer> getTargetIds() {
        return this.mTargetIds;
    }

    public List<String> getTargetNames() {
        return this.mTargetNames;
    }

    public List<Class> getTargetTypes() {
        return this.mTargetTypes;
    }

    public List<View> getTargets() {
        return this.mTargets;
    }

    public String[] getTransitionProperties() {
        return null;
    }

    public TransitionValues getTransitionValues(View view, boolean bl) {
        Object object = this.mParent;
        if (object != null) {
            return ((Transition)object).getTransitionValues(view, bl);
        }
        object = bl ? this.mStartValues : this.mEndValues;
        return (TransitionValues)((TransitionValuesMaps)object).mViewValues.get(view);
    }

    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        boolean bl;
        block5: {
            boolean bl2 = false;
            boolean bl3 = false;
            bl = bl2;
            if (transitionValues == null) break block5;
            bl = bl2;
            if (transitionValues2 != null) {
                Object object = this.getTransitionProperties();
                if (object != null) {
                    int n = ((String[])object).length;
                    int n2 = 0;
                    while (true) {
                        bl = bl3;
                        if (n2 >= n) break block5;
                        if (Transition.isValueChanged(transitionValues, transitionValues2, (String)object[n2])) {
                            bl = true;
                            break block5;
                        }
                        ++n2;
                    }
                }
                object = transitionValues.values.keySet().iterator();
                do {
                    bl = bl2;
                    if (!object.hasNext()) break block5;
                } while (!Transition.isValueChanged(transitionValues, transitionValues2, (String)object.next()));
                bl = true;
            }
        }
        return bl;
    }

    boolean isValidTarget(View view) {
        int n;
        int n2 = view.getId();
        ArrayList<Object> arrayList = this.mTargetIdExcludes;
        if (arrayList != null && arrayList.contains(n2)) {
            return false;
        }
        arrayList = this.mTargetExcludes;
        if (arrayList != null && arrayList.contains(view)) {
            return false;
        }
        arrayList = this.mTargetTypeExcludes;
        if (arrayList != null) {
            int n3 = arrayList.size();
            for (n = 0; n < n3; ++n) {
                if (!this.mTargetTypeExcludes.get(n).isInstance(view)) continue;
                return false;
            }
        }
        if (this.mTargetNameExcludes != null && ViewCompat.getTransitionName(view) != null && this.mTargetNameExcludes.contains(ViewCompat.getTransitionName(view))) {
            return false;
        }
        if (this.mTargetIds.size() == 0 && this.mTargets.size() == 0 && ((arrayList = this.mTargetTypes) == null || arrayList.isEmpty()) && ((arrayList = this.mTargetNames) == null || arrayList.isEmpty())) {
            return true;
        }
        if (!this.mTargetIds.contains(n2) && !this.mTargets.contains(view)) {
            arrayList = this.mTargetNames;
            if (arrayList != null && arrayList.contains(ViewCompat.getTransitionName(view))) {
                return true;
            }
            if (this.mTargetTypes != null) {
                for (n = 0; n < this.mTargetTypes.size(); ++n) {
                    if (!this.mTargetTypes.get(n).isInstance(view)) continue;
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public void pause(View object) {
        if (!this.mEnded) {
            ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
            int n = arrayMap.size();
            WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)object);
            --n;
            while (n >= 0) {
                object = (AnimationInfo)arrayMap.valueAt(n);
                if (((AnimationInfo)object).mView != null && windowIdImpl.equals(((AnimationInfo)object).mWindowId)) {
                    AnimatorUtils.pause((Animator)arrayMap.keyAt(n));
                }
                --n;
            }
            object = this.mListeners;
            if (object != null && ((ArrayList)object).size() > 0) {
                object = (ArrayList)this.mListeners.clone();
                int n2 = ((ArrayList)object).size();
                for (n = 0; n < n2; ++n) {
                    ((TransitionListener)((ArrayList)object).get(n)).onTransitionPause(this);
                }
            }
            this.mPaused = true;
        }
    }

    void playTransition(ViewGroup viewGroup) {
        this.mStartValuesList = new ArrayList();
        this.mEndValuesList = new ArrayList();
        this.matchStartAndEnd(this.mStartValues, this.mEndValues);
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        int n = arrayMap.size();
        WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)viewGroup);
        --n;
        while (n >= 0) {
            AnimationInfo animationInfo;
            Animator animator2 = (Animator)arrayMap.keyAt(n);
            if (animator2 != null && (animationInfo = (AnimationInfo)arrayMap.get(animator2)) != null && animationInfo.mView != null && windowIdImpl.equals(animationInfo.mWindowId)) {
                TransitionValues transitionValues = animationInfo.mValues;
                Object object = animationInfo.mView;
                boolean bl = true;
                TransitionValues transitionValues2 = this.getTransitionValues((View)object, true);
                object = this.getMatchedTransitionValues((View)object, true);
                if (transitionValues2 == null && object == null || !animationInfo.mTransition.isTransitionRequired(transitionValues, (TransitionValues)object)) {
                    bl = false;
                }
                if (bl) {
                    if (!animator2.isRunning() && !animator2.isStarted()) {
                        arrayMap.remove(animator2);
                    } else {
                        animator2.cancel();
                    }
                }
            }
            --n;
        }
        this.createAnimators(viewGroup, this.mStartValues, this.mEndValues, this.mStartValuesList, this.mEndValuesList);
        this.runAnimators();
    }

    public Transition removeListener(TransitionListener transitionListener) {
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(transitionListener);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
        }
        return this;
    }

    public Transition removeTarget(int n) {
        if (n != 0) {
            this.mTargetIds.remove((Object)n);
        }
        return this;
    }

    public Transition removeTarget(View view) {
        this.mTargets.remove(view);
        return this;
    }

    public Transition removeTarget(Class clazz) {
        ArrayList<Class> arrayList = this.mTargetTypes;
        if (arrayList != null) {
            arrayList.remove(clazz);
        }
        return this;
    }

    public Transition removeTarget(String string2) {
        ArrayList<String> arrayList = this.mTargetNames;
        if (arrayList != null) {
            arrayList.remove(string2);
        }
        return this;
    }

    public void resume(View object) {
        if (this.mPaused) {
            if (!this.mEnded) {
                ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
                int n = arrayMap.size();
                WindowIdImpl windowIdImpl = ViewUtils.getWindowId((View)object);
                --n;
                while (n >= 0) {
                    object = (AnimationInfo)arrayMap.valueAt(n);
                    if (((AnimationInfo)object).mView != null && windowIdImpl.equals(((AnimationInfo)object).mWindowId)) {
                        AnimatorUtils.resume((Animator)arrayMap.keyAt(n));
                    }
                    --n;
                }
                object = this.mListeners;
                if (object != null && ((ArrayList)object).size() > 0) {
                    object = (ArrayList)this.mListeners.clone();
                    int n2 = ((ArrayList)object).size();
                    for (n = 0; n < n2; ++n) {
                        ((TransitionListener)((ArrayList)object).get(n)).onTransitionResume(this);
                    }
                }
            }
            this.mPaused = false;
        }
    }

    protected void runAnimators() {
        this.start();
        ArrayMap<Animator, AnimationInfo> arrayMap = Transition.getRunningAnimators();
        for (Animator animator2 : this.mAnimators) {
            if (!arrayMap.containsKey(animator2)) continue;
            this.start();
            this.runAnimator(animator2, arrayMap);
        }
        this.mAnimators.clear();
        this.end();
    }

    void setCanRemoveViews(boolean bl) {
        this.mCanRemoveViews = bl;
    }

    public Transition setDuration(long l) {
        this.mDuration = l;
        return this;
    }

    public void setEpicenterCallback(EpicenterCallback epicenterCallback) {
        this.mEpicenterCallback = epicenterCallback;
    }

    public Transition setInterpolator(TimeInterpolator timeInterpolator) {
        this.mInterpolator = timeInterpolator;
        return this;
    }

    public void setMatchOrder(int ... nArray) {
        if (nArray != null && nArray.length != 0) {
            for (int i = 0; i < nArray.length; ++i) {
                if (Transition.isValidMatch(nArray[i])) {
                    if (!Transition.alreadyContains(nArray, i)) {
                        continue;
                    }
                    throw new IllegalArgumentException("matches contains a duplicate value");
                }
                throw new IllegalArgumentException("matches contains invalid value");
            }
            this.mMatchOrder = (int[])nArray.clone();
        } else {
            this.mMatchOrder = DEFAULT_MATCH_ORDER;
        }
    }

    public void setPathMotion(PathMotion pathMotion) {
        this.mPathMotion = pathMotion == null ? STRAIGHT_PATH_MOTION : pathMotion;
    }

    public void setPropagation(TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    Transition setSceneRoot(ViewGroup viewGroup) {
        this.mSceneRoot = viewGroup;
        return this;
    }

    public Transition setStartDelay(long l) {
        this.mStartDelay = l;
        return this;
    }

    protected void start() {
        if (this.mNumInstances == 0) {
            ArrayList arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                arrayList = (ArrayList)this.mListeners.clone();
                int n = arrayList.size();
                for (int i = 0; i < n; ++i) {
                    ((TransitionListener)arrayList.get(i)).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        ++this.mNumInstances;
    }

    public String toString() {
        return this.toString("");
    }

    String toString(String charSequence) {
        block13: {
            int n;
            CharSequence charSequence2;
            block12: {
                charSequence2 = new StringBuilder();
                ((StringBuilder)charSequence2).append((String)charSequence);
                ((StringBuilder)charSequence2).append(this.getClass().getSimpleName());
                ((StringBuilder)charSequence2).append("@");
                ((StringBuilder)charSequence2).append(Integer.toHexString(this.hashCode()));
                ((StringBuilder)charSequence2).append(": ");
                charSequence = ((StringBuilder)charSequence2).toString();
                charSequence2 = charSequence;
                if (this.mDuration != -1L) {
                    charSequence2 = new StringBuilder();
                    ((StringBuilder)charSequence2).append((String)charSequence);
                    ((StringBuilder)charSequence2).append("dur(");
                    ((StringBuilder)charSequence2).append(this.mDuration);
                    ((StringBuilder)charSequence2).append(") ");
                    charSequence2 = ((StringBuilder)charSequence2).toString();
                }
                charSequence = charSequence2;
                if (this.mStartDelay != -1L) {
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append((String)charSequence2);
                    ((StringBuilder)charSequence).append("dly(");
                    ((StringBuilder)charSequence).append(this.mStartDelay);
                    ((StringBuilder)charSequence).append(") ");
                    charSequence = ((StringBuilder)charSequence).toString();
                }
                charSequence2 = charSequence;
                if (this.mInterpolator != null) {
                    charSequence2 = new StringBuilder();
                    ((StringBuilder)charSequence2).append((String)charSequence);
                    ((StringBuilder)charSequence2).append("interp(");
                    ((StringBuilder)charSequence2).append(this.mInterpolator);
                    ((StringBuilder)charSequence2).append(") ");
                    charSequence2 = ((StringBuilder)charSequence2).toString();
                }
                if (this.mTargetIds.size() > 0) break block12;
                charSequence = charSequence2;
                if (this.mTargets.size() <= 0) break block13;
            }
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append((String)charSequence2);
            ((StringBuilder)charSequence).append("tgts(");
            charSequence = ((StringBuilder)charSequence).toString();
            charSequence2 = charSequence;
            if (this.mTargetIds.size() > 0) {
                n = 0;
                while (true) {
                    charSequence2 = charSequence;
                    if (n >= this.mTargetIds.size()) break;
                    charSequence2 = charSequence;
                    if (n > 0) {
                        charSequence2 = new StringBuilder();
                        ((StringBuilder)charSequence2).append((String)charSequence);
                        ((StringBuilder)charSequence2).append(", ");
                        charSequence2 = ((StringBuilder)charSequence2).toString();
                    }
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append((String)charSequence2);
                    ((StringBuilder)charSequence).append(this.mTargetIds.get(n));
                    charSequence = ((StringBuilder)charSequence).toString();
                    ++n;
                }
            }
            charSequence = charSequence2;
            if (this.mTargets.size() > 0) {
                n = 0;
                while (true) {
                    charSequence = charSequence2;
                    if (n >= this.mTargets.size()) break;
                    charSequence = charSequence2;
                    if (n > 0) {
                        charSequence = new StringBuilder();
                        ((StringBuilder)charSequence).append((String)charSequence2);
                        ((StringBuilder)charSequence).append(", ");
                        charSequence = ((StringBuilder)charSequence).toString();
                    }
                    charSequence2 = new StringBuilder();
                    ((StringBuilder)charSequence2).append((String)charSequence);
                    ((StringBuilder)charSequence2).append(this.mTargets.get(n));
                    charSequence2 = ((StringBuilder)charSequence2).toString();
                    ++n;
                }
            }
            charSequence2 = new StringBuilder();
            ((StringBuilder)charSequence2).append((String)charSequence);
            ((StringBuilder)charSequence2).append(")");
            charSequence = ((StringBuilder)charSequence2).toString();
        }
        return charSequence;
    }

    private static class AnimationInfo {
        String mName;
        Transition mTransition;
        TransitionValues mValues;
        View mView;
        WindowIdImpl mWindowId;

        AnimationInfo(View view, String string2, Transition transition, WindowIdImpl windowIdImpl, TransitionValues transitionValues) {
            this.mView = view;
            this.mName = string2;
            this.mValues = transitionValues;
            this.mWindowId = windowIdImpl;
            this.mTransition = transition;
        }
    }

    private static class ArrayListManager {
        private ArrayListManager() {
        }

        static <T> ArrayList<T> add(ArrayList<T> arrayList, T t) {
            ArrayList<Object> arrayList2 = arrayList;
            if (arrayList == null) {
                arrayList2 = new ArrayList();
            }
            if (!arrayList2.contains(t)) {
                arrayList2.add(t);
            }
            return arrayList2;
        }

        static <T> ArrayList<T> remove(ArrayList<T> arrayList, T t) {
            ArrayList<T> arrayList2 = arrayList;
            if (arrayList != null) {
                arrayList.remove(t);
                arrayList2 = arrayList;
                if (arrayList.isEmpty()) {
                    arrayList2 = null;
                }
            }
            return arrayList2;
        }
    }

    public static abstract class EpicenterCallback {
        public abstract Rect onGetEpicenter(Transition var1);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface MatchOrder {
    }

    public static interface TransitionListener {
        public void onTransitionCancel(Transition var1);

        public void onTransitionEnd(Transition var1);

        public void onTransitionPause(Transition var1);

        public void onTransitionResume(Transition var1);

        public void onTransitionStart(Transition var1);
    }
}

