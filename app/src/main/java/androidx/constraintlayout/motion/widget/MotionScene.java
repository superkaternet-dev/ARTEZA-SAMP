/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.RectF
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.SparseArray
 *  android.util.SparseIntArray
 *  android.util.TypedValue
 *  android.util.Xml
 *  android.view.MotionEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.animation.AccelerateDecelerateInterpolator
 *  android.view.animation.AccelerateInterpolator
 *  android.view.animation.AnimationUtils
 *  android.view.animation.AnticipateInterpolator
 *  android.view.animation.BounceInterpolator
 *  android.view.animation.DecelerateInterpolator
 *  android.view.animation.Interpolator
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.constraintlayout.motion.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import androidx.constraintlayout.motion.utils.Easing;
import androidx.constraintlayout.motion.widget.Debug;
import androidx.constraintlayout.motion.widget.Key;
import androidx.constraintlayout.motion.widget.KeyFrames;
import androidx.constraintlayout.motion.widget.MotionController;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.TouchResponse;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import androidx.constraintlayout.widget.StateSet;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;

public class MotionScene {
    static final int ANTICIPATE = 4;
    static final int BOUNCE = 5;
    private static final boolean DEBUG = false;
    static final int EASE_IN = 1;
    static final int EASE_IN_OUT = 0;
    static final int EASE_OUT = 2;
    private static final int INTERPOLATOR_REFRENCE_ID = -2;
    public static final int LAYOUT_HONOR_REQUEST = 1;
    public static final int LAYOUT_IGNORE_REQUEST = 0;
    static final int LINEAR = 3;
    private static final int SPLINE_STRING = -1;
    public static final String TAG = "MotionScene";
    static final int TRANSITION_BACKWARD = 0;
    static final int TRANSITION_FORWARD = 1;
    public static final int UNSET = -1;
    private boolean DEBUG_DESKTOP = false;
    private ArrayList<Transition> mAbstractTransitionList;
    private HashMap<String, Integer> mConstraintSetIdMap;
    private SparseArray<ConstraintSet> mConstraintSetMap;
    Transition mCurrentTransition = null;
    private int mDefaultDuration = 400;
    private Transition mDefaultTransition = null;
    private SparseIntArray mDeriveMap;
    private boolean mDisableAutoTransition = false;
    private boolean mIgnoreTouch = false;
    private MotionEvent mLastTouchDown;
    float mLastTouchX;
    float mLastTouchY;
    private int mLayoutDuringTransition = 0;
    private final MotionLayout mMotionLayout;
    private boolean mMotionOutsideRegion = false;
    private boolean mRtl;
    StateSet mStateSet = null;
    private ArrayList<Transition> mTransitionList = new ArrayList();
    private MotionLayout.MotionTracker mVelocityTracker;

    MotionScene(Context context, MotionLayout motionLayout, int n) {
        this.mAbstractTransitionList = new ArrayList();
        this.mConstraintSetMap = new SparseArray();
        this.mConstraintSetIdMap = new HashMap();
        this.mDeriveMap = new SparseIntArray();
        this.mMotionLayout = motionLayout;
        this.load(context, n);
        this.mConstraintSetMap.put(R.id.motion_base, (Object)new ConstraintSet());
        this.mConstraintSetIdMap.put("motion_base", R.id.motion_base);
    }

    public MotionScene(MotionLayout motionLayout) {
        this.mAbstractTransitionList = new ArrayList();
        this.mConstraintSetMap = new SparseArray();
        this.mConstraintSetIdMap = new HashMap();
        this.mDeriveMap = new SparseIntArray();
        this.mMotionLayout = motionLayout;
    }

    private int getId(Context object, String string2) {
        int n;
        int n2 = -1;
        if (string2.contains("/")) {
            CharSequence charSequence = string2.substring(string2.indexOf(47) + 1);
            n2 = n = object.getResources().getIdentifier((String)charSequence, "id", object.getPackageName());
            if (this.DEBUG_DESKTOP) {
                object = System.out;
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("id getMap res = ");
                ((StringBuilder)charSequence).append(n);
                ((PrintStream)object).println(((StringBuilder)charSequence).toString());
                n2 = n;
            }
        }
        n = n2;
        if (n2 == -1) {
            if (string2 != null && string2.length() > 1) {
                n = Integer.parseInt(string2.substring(1));
            } else {
                Log.e((String)TAG, (String)"error in parsing id");
                n = n2;
            }
        }
        return n;
    }

    private int getIndex(Transition object) {
        int n = ((Transition)object).mId;
        if (n != -1) {
            for (int i = 0; i < this.mTransitionList.size(); ++i) {
                if (this.mTransitionList.get(i).mId != n) continue;
                return i;
            }
            return -1;
        }
        object = new IllegalArgumentException("The transition must have an id");
        throw object;
    }

    private int getRealID(int n) {
        int n2;
        StateSet stateSet = this.mStateSet;
        if (stateSet != null && (n2 = stateSet.stateGetConstraintID(n, -1, -1)) != -1) {
            return n2;
        }
        return n;
    }

    private boolean hasCycleDependency(int n) {
        int n2 = this.mDeriveMap.get(n);
        int n3 = this.mDeriveMap.size();
        while (n2 > 0) {
            if (n2 == n) {
                return true;
            }
            if (n3 < 0) {
                return true;
            }
            n2 = this.mDeriveMap.get(n2);
            --n3;
        }
        return false;
    }

    private boolean isProcessingTouch() {
        boolean bl = this.mVelocityTracker != null;
        return bl;
    }

    /*
     * Exception decompiling
     */
    private void load(Context var1_1, int var2_4) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [30[CASE]], but top level block is 9[TRYBLOCK]
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.processEndingBlocks(Op04StructuredStatement.java:435)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:484)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op03SimpleStatement.createInitialStructuredBlock(Op03SimpleStatement.java:736)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:850)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    /*
     * Enabled aggressive block sorting
     */
    private void parseConstraintSet(Context context, XmlPullParser xmlPullParser) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.setForceId(false);
        int n = xmlPullParser.getAttributeCount();
        int n2 = -1;
        int n3 = -1;
        int n4 = 0;
        while (true) {
            String string2;
            int n5;
            block13: {
                n5 = 1;
                if (n4 >= n) break;
                String string3 = xmlPullParser.getAttributeName(n4);
                string2 = xmlPullParser.getAttributeValue(n4);
                if (this.DEBUG_DESKTOP) {
                    PrintStream printStream = System.out;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("id string = ");
                    stringBuilder.append(string2);
                    printStream.println(stringBuilder.toString());
                }
                switch (string3.hashCode()) {
                    case 3355: {
                        if (!string3.equals("id")) break;
                        n5 = 0;
                        break block13;
                    }
                    case -1496482599: {
                        if (string3.equals("deriveConstraintsFrom")) break block13;
                    }
                }
                n5 = -1;
            }
            switch (n5) {
                default: {
                    break;
                }
                case 1: {
                    n3 = this.getId(context, string2);
                    break;
                }
                case 0: {
                    n2 = this.getId(context, string2);
                    this.mConstraintSetIdMap.put(MotionScene.stripID(string2), n2);
                }
            }
            ++n4;
        }
        if (n2 != -1) {
            if (this.mMotionLayout.mDebugPath != 0) {
                constraintSet.setValidateOnParse(true);
            }
            constraintSet.load(context, xmlPullParser);
            if (n3 != -1) {
                this.mDeriveMap.put(n2, n3);
            }
            this.mConstraintSetMap.put(n2, (Object)constraintSet);
        }
    }

    private void parseMotionSceneTags(Context context, XmlPullParser xmlPullParser) {
        context = context.obtainStyledAttributes(Xml.asAttributeSet((XmlPullParser)xmlPullParser), R.styleable.MotionScene);
        int n = context.getIndexCount();
        for (int i = 0; i < n; ++i) {
            int n2 = context.getIndex(i);
            if (n2 == R.styleable.MotionScene_defaultDuration) {
                this.mDefaultDuration = context.getInt(n2, this.mDefaultDuration);
                continue;
            }
            if (n2 != R.styleable.MotionScene_layoutDuringTransition) continue;
            this.mLayoutDuringTransition = context.getInteger(n2, 0);
        }
        context.recycle();
    }

    private void readConstraintChain(int n) {
        int n2 = this.mDeriveMap.get(n);
        if (n2 > 0) {
            this.readConstraintChain(this.mDeriveMap.get(n));
            ConstraintSet constraintSet = (ConstraintSet)this.mConstraintSetMap.get(n);
            Object object = (ConstraintSet)this.mConstraintSetMap.get(n2);
            if (object == null) {
                object = new StringBuilder();
                ((StringBuilder)object).append("ERROR! invalid deriveConstraintsFrom: @id/");
                ((StringBuilder)object).append(Debug.getName(this.mMotionLayout.getContext(), n2));
                Log.e((String)TAG, (String)((StringBuilder)object).toString());
                return;
            }
            constraintSet.readFallback((ConstraintSet)object);
            this.mDeriveMap.put(n, -1);
        }
    }

    public static String stripID(String string2) {
        if (string2 == null) {
            return "";
        }
        int n = string2.indexOf(47);
        if (n < 0) {
            return string2;
        }
        return string2.substring(n + 1);
    }

    public void addOnClickListeners(MotionLayout motionLayout, int n) {
        for (Transition transition : this.mTransitionList) {
            if (transition.mOnClicks.size() <= 0) continue;
            Iterator iterator2 = transition.mOnClicks.iterator();
            while (iterator2.hasNext()) {
                ((Transition.TransitionOnClick)iterator2.next()).removeOnClickListeners(motionLayout);
            }
        }
        for (Transition transition : this.mAbstractTransitionList) {
            if (transition.mOnClicks.size() <= 0) continue;
            Iterator iterator3 = transition.mOnClicks.iterator();
            while (iterator3.hasNext()) {
                ((Transition.TransitionOnClick)iterator3.next()).removeOnClickListeners(motionLayout);
            }
        }
        for (Transition transition : this.mTransitionList) {
            if (transition.mOnClicks.size() <= 0) continue;
            Iterator iterator4 = transition.mOnClicks.iterator();
            while (iterator4.hasNext()) {
                ((Transition.TransitionOnClick)iterator4.next()).addOnClickListeners(motionLayout, n, transition);
            }
        }
        for (Transition transition : this.mAbstractTransitionList) {
            if (transition.mOnClicks.size() <= 0) continue;
            Iterator<Transition> iterator2 = transition.mOnClicks.iterator();
            while (iterator2.hasNext()) {
                ((Transition.TransitionOnClick)((Object)iterator2.next())).addOnClickListeners(motionLayout, n, transition);
            }
        }
    }

    public void addTransition(Transition transition) {
        int n = this.getIndex(transition);
        if (n == -1) {
            this.mTransitionList.add(transition);
        } else {
            this.mTransitionList.set(n, transition);
        }
    }

    boolean autoTransition(MotionLayout motionLayout, int n) {
        if (this.isProcessingTouch()) {
            return false;
        }
        if (this.mDisableAutoTransition) {
            return false;
        }
        for (Transition transition : this.mTransitionList) {
            if (transition.mAutoTransition == 0 || this.mCurrentTransition == transition) continue;
            if (n == transition.mConstraintSetStart && (transition.mAutoTransition == 4 || transition.mAutoTransition == 2)) {
                motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                motionLayout.setTransition(transition);
                if (transition.mAutoTransition == 4) {
                    motionLayout.transitionToEnd();
                    motionLayout.setState(MotionLayout.TransitionState.SETUP);
                    motionLayout.setState(MotionLayout.TransitionState.MOVING);
                } else {
                    motionLayout.setProgress(1.0f);
                    motionLayout.evaluate(true);
                    motionLayout.setState(MotionLayout.TransitionState.SETUP);
                    motionLayout.setState(MotionLayout.TransitionState.MOVING);
                    motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                    motionLayout.onNewStateAttachHandlers();
                }
                return true;
            }
            if (n != transition.mConstraintSetEnd || transition.mAutoTransition != 3 && transition.mAutoTransition != 1) continue;
            motionLayout.setState(MotionLayout.TransitionState.FINISHED);
            motionLayout.setTransition(transition);
            if (transition.mAutoTransition == 3) {
                motionLayout.transitionToStart();
                motionLayout.setState(MotionLayout.TransitionState.SETUP);
                motionLayout.setState(MotionLayout.TransitionState.MOVING);
            } else {
                motionLayout.setProgress(0.0f);
                motionLayout.evaluate(true);
                motionLayout.setState(MotionLayout.TransitionState.SETUP);
                motionLayout.setState(MotionLayout.TransitionState.MOVING);
                motionLayout.setState(MotionLayout.TransitionState.FINISHED);
                motionLayout.onNewStateAttachHandlers();
            }
            return true;
        }
        return false;
    }

    public Transition bestTransitionFor(int n, float f, float f2, MotionEvent motionEvent) {
        if (n != -1) {
            Object object = this.getTransitionsWithState(n);
            float f3 = 0.0f;
            RectF rectF = null;
            RectF rectF2 = new RectF();
            for (Transition transition : object) {
                if (transition.mDisable) continue;
                float f4 = f3;
                object = rectF;
                if (transition.mTouchResponse != null) {
                    transition.mTouchResponse.setRTL(this.mRtl);
                    object = transition.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF2);
                    if (object != null && motionEvent != null && !object.contains(motionEvent.getX(), motionEvent.getY()) || (object = transition.mTouchResponse.getTouchRegion(this.mMotionLayout, rectF2)) != null && motionEvent != null && !object.contains(motionEvent.getX(), motionEvent.getY())) continue;
                    float f5 = transition.mTouchResponse.dot(f, f2);
                    f5 = transition.mConstraintSetEnd == n ? (f5 *= -1.0f) : (f5 *= 1.1f);
                    f4 = f3;
                    object = rectF;
                    if (f5 > f3) {
                        object = transition;
                        f4 = f5;
                    }
                }
                f3 = f4;
                rectF = object;
            }
            return rectF;
        }
        return this.mCurrentTransition;
    }

    public void disableAutoTransition(boolean bl) {
        this.mDisableAutoTransition = bl;
    }

    public int gatPathMotionArc() {
        Transition transition = this.mCurrentTransition;
        int n = transition != null ? transition.mPathMotionArc : -1;
        return n;
    }

    ConstraintSet getConstraintSet(int n) {
        return this.getConstraintSet(n, -1, -1);
    }

    ConstraintSet getConstraintSet(int n, int n2, int n3) {
        SparseArray<ConstraintSet> sparseArray;
        if (this.DEBUG_DESKTOP) {
            Appendable appendable = System.out;
            sparseArray = new StringBuilder();
            sparseArray.append("id ");
            sparseArray.append(n);
            ((PrintStream)appendable).println(sparseArray.toString());
            sparseArray = System.out;
            appendable = new StringBuilder();
            ((StringBuilder)appendable).append("size ");
            ((StringBuilder)appendable).append(this.mConstraintSetMap.size());
            sparseArray.println(((StringBuilder)appendable).toString());
        }
        sparseArray = this.mStateSet;
        int n4 = n;
        if (sparseArray != null) {
            n2 = sparseArray.stateGetConstraintID(n, n2, n3);
            n4 = n;
            if (n2 != -1) {
                n4 = n2;
            }
        }
        if (this.mConstraintSetMap.get(n4) == null) {
            sparseArray = new StringBuilder();
            sparseArray.append("Warning could not find ConstraintSet id/");
            sparseArray.append(Debug.getName(this.mMotionLayout.getContext(), n4));
            sparseArray.append(" In MotionScene");
            Log.e((String)TAG, (String)sparseArray.toString());
            sparseArray = this.mConstraintSetMap;
            return (ConstraintSet)sparseArray.get(sparseArray.keyAt(0));
        }
        return (ConstraintSet)this.mConstraintSetMap.get(n4);
    }

    public ConstraintSet getConstraintSet(Context context, String string2) {
        Object object;
        Appendable appendable;
        if (this.DEBUG_DESKTOP) {
            appendable = System.out;
            object = new StringBuilder();
            ((StringBuilder)object).append("id ");
            ((StringBuilder)object).append(string2);
            ((PrintStream)appendable).println(((StringBuilder)object).toString());
            object = System.out;
            appendable = new StringBuilder();
            ((StringBuilder)appendable).append("size ");
            ((StringBuilder)appendable).append(this.mConstraintSetMap.size());
            ((PrintStream)object).println(((StringBuilder)appendable).toString());
        }
        for (int i = 0; i < this.mConstraintSetMap.size(); ++i) {
            int n = this.mConstraintSetMap.keyAt(i);
            object = context.getResources().getResourceName(n);
            if (this.DEBUG_DESKTOP) {
                appendable = System.out;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Id for <");
                stringBuilder.append(i);
                stringBuilder.append("> is <");
                stringBuilder.append((String)object);
                stringBuilder.append("> looking for <");
                stringBuilder.append(string2);
                stringBuilder.append(">");
                ((PrintStream)appendable).println(stringBuilder.toString());
            }
            if (!string2.equals(object)) continue;
            return (ConstraintSet)this.mConstraintSetMap.get(n);
        }
        return null;
    }

    public int[] getConstraintSetIds() {
        int[] nArray = new int[this.mConstraintSetMap.size()];
        for (int i = 0; i < nArray.length; ++i) {
            nArray[i] = this.mConstraintSetMap.keyAt(i);
        }
        return nArray;
    }

    public ArrayList<Transition> getDefinedTransitions() {
        return this.mTransitionList;
    }

    public int getDuration() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mDuration;
        }
        return this.mDefaultDuration;
    }

    int getEndId() {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return -1;
        }
        return transition.mConstraintSetEnd;
    }

    public Interpolator getInterpolator() {
        switch (this.mCurrentTransition.mDefaultInterpolator) {
            default: {
                return null;
            }
            case 5: {
                return new BounceInterpolator();
            }
            case 4: {
                return new AnticipateInterpolator();
            }
            case 3: {
                return null;
            }
            case 2: {
                return new DecelerateInterpolator();
            }
            case 1: {
                return new AccelerateInterpolator();
            }
            case 0: {
                return new AccelerateDecelerateInterpolator();
            }
            case -1: {
                return new Interpolator(this, Easing.getInterpolator(this.mCurrentTransition.mDefaultInterpolatorString)){
                    final MotionScene this$0;
                    final Easing val$easing;
                    {
                        this.this$0 = motionScene;
                        this.val$easing = easing;
                    }

                    public float getInterpolation(float f) {
                        return (float)this.val$easing.get(f);
                    }
                };
            }
            case -2: 
        }
        return AnimationUtils.loadInterpolator((Context)this.mMotionLayout.getContext(), (int)this.mCurrentTransition.mDefaultInterpolatorID);
    }

    Key getKeyFrame(Context object, int n, int n2, int n3) {
        object = this.mCurrentTransition;
        if (object == null) {
            return null;
        }
        for (KeyFrames keyFrames : ((Transition)object).mKeyFramesList) {
            for (Integer n4 : keyFrames.getKeys()) {
                if (n2 != n4) continue;
                for (Key key : keyFrames.getKeyFramesForView(n4)) {
                    if (key.mFramePosition != n3 || key.mType != n) continue;
                    return key;
                }
            }
        }
        return null;
    }

    public void getKeyFrames(MotionController motionController) {
        Object object = this.mCurrentTransition;
        if (object == null) {
            object = this.mDefaultTransition;
            if (object != null) {
                object = ((Transition)object).mKeyFramesList.iterator();
                while (object.hasNext()) {
                    ((KeyFrames)object.next()).addFrames(motionController);
                }
            }
            return;
        }
        object = ((Transition)object).mKeyFramesList.iterator();
        while (object.hasNext()) {
            ((KeyFrames)object.next()).addFrames(motionController);
        }
    }

    float getMaxAcceleration() {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            return this.mCurrentTransition.mTouchResponse.getMaxAcceleration();
        }
        return 0.0f;
    }

    float getMaxVelocity() {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            return this.mCurrentTransition.mTouchResponse.getMaxVelocity();
        }
        return 0.0f;
    }

    boolean getMoveWhenScrollAtTop() {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            return this.mCurrentTransition.mTouchResponse.getMoveWhenScrollAtTop();
        }
        return false;
    }

    public float getPathPercent(View view, int n) {
        return 0.0f;
    }

    float getProgressDirection(float f, float f2) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            return this.mCurrentTransition.mTouchResponse.getProgressDirection(f, f2);
        }
        return 0.0f;
    }

    public float getStaggered() {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            return transition.mStagger;
        }
        return 0.0f;
    }

    int getStartId() {
        Transition transition = this.mCurrentTransition;
        if (transition == null) {
            return -1;
        }
        return transition.mConstraintSetStart;
    }

    public Transition getTransitionById(int n) {
        for (Transition transition : this.mTransitionList) {
            if (transition.mId != n) continue;
            return transition;
        }
        return null;
    }

    int getTransitionDirection(int n) {
        Iterator<Transition> iterator2 = this.mTransitionList.iterator();
        while (iterator2.hasNext()) {
            if (iterator2.next().mConstraintSetStart != n) continue;
            return 0;
        }
        return 1;
    }

    public List<Transition> getTransitionsWithState(int n) {
        n = this.getRealID(n);
        ArrayList<Transition> arrayList = new ArrayList<Transition>();
        for (Transition transition : this.mTransitionList) {
            if (transition.mConstraintSetStart != n && transition.mConstraintSetEnd != n) continue;
            arrayList.add(transition);
        }
        return arrayList;
    }

    boolean hasKeyFramePosition(View view, int n) {
        Object object = this.mCurrentTransition;
        if (object == null) {
            return false;
        }
        Iterator iterator2 = ((Transition)object).mKeyFramesList.iterator();
        while (iterator2.hasNext()) {
            object = ((KeyFrames)iterator2.next()).getKeyFramesForView(view.getId()).iterator();
            while (object.hasNext()) {
                if (((Key)object.next()).mFramePosition != n) continue;
                return true;
            }
        }
        return false;
    }

    public int lookUpConstraintId(String string2) {
        return this.mConstraintSetIdMap.get(string2);
    }

    public String lookUpConstraintName(int n) {
        for (Map.Entry<String, Integer> entry : this.mConstraintSetIdMap.entrySet()) {
            if (entry.getValue() != n) continue;
            return entry.getKey();
        }
        return null;
    }

    protected void onLayout(boolean bl, int n, int n2, int n3, int n4) {
    }

    void processScrollMove(float f, float f2) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollMove(f, f2);
        }
    }

    void processScrollUp(float f, float f2) {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollUp(f, f2);
        }
    }

    void processTouchEvent(MotionEvent object, int n, MotionLayout motionLayout) {
        Object object2 = new RectF();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = this.mMotionLayout.obtainVelocityTracker();
        }
        this.mVelocityTracker.addMovement((MotionEvent)object);
        if (n != -1) {
            int n2 = object.getAction();
            boolean bl = false;
            switch (n2) {
                default: {
                    break;
                }
                case 2: {
                    Object object3;
                    if (this.mIgnoreTouch) break;
                    float f = object.getRawY() - this.mLastTouchY;
                    float f2 = object.getRawX() - this.mLastTouchX;
                    if ((double)f2 == 0.0 && (double)f == 0.0 || (object3 = this.mLastTouchDown) == null) {
                        return;
                    }
                    if ((object3 = this.bestTransitionFor(n, f2, f, (MotionEvent)object3)) == null) break;
                    motionLayout.setTransition((Transition)object3);
                    object2 = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, (RectF)object2);
                    if (object2 != null && !object2.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                        bl = true;
                    }
                    this.mMotionOutsideRegion = bl;
                    this.mCurrentTransition.mTouchResponse.setUpTouchEvent(this.mLastTouchX, this.mLastTouchY);
                    break;
                }
                case 0: {
                    this.mLastTouchX = object.getRawX();
                    this.mLastTouchY = object.getRawY();
                    this.mLastTouchDown = object;
                    this.mIgnoreTouch = false;
                    if (this.mCurrentTransition.mTouchResponse != null) {
                        object = this.mCurrentTransition.mTouchResponse.getLimitBoundsTo(this.mMotionLayout, (RectF)object2);
                        if (object != null && !object.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                            this.mLastTouchDown = null;
                            this.mIgnoreTouch = true;
                            return;
                        }
                        object = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, (RectF)object2);
                        this.mMotionOutsideRegion = object != null && !object.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY());
                        this.mCurrentTransition.mTouchResponse.setDown(this.mLastTouchX, this.mLastTouchY);
                    }
                    return;
                }
            }
        }
        if (this.mIgnoreTouch) {
            return;
        }
        object2 = this.mCurrentTransition;
        if (object2 != null && ((Transition)object2).mTouchResponse != null && !this.mMotionOutsideRegion) {
            this.mCurrentTransition.mTouchResponse.processTouchEvent((MotionEvent)object, this.mVelocityTracker, n, this);
        }
        this.mLastTouchX = object.getRawX();
        this.mLastTouchY = object.getRawY();
        if (object.getAction() == 1 && (object = this.mVelocityTracker) != null) {
            object.recycle();
            this.mVelocityTracker = null;
            if (motionLayout.mCurrentState != -1) {
                this.autoTransition(motionLayout, motionLayout.mCurrentState);
            }
        }
    }

    void readFallback(MotionLayout motionLayout) {
        int n;
        for (n = 0; n < this.mConstraintSetMap.size(); ++n) {
            int n2 = this.mConstraintSetMap.keyAt(n);
            if (this.hasCycleDependency(n2)) {
                Log.e((String)TAG, (String)"Cannot be derived from yourself");
                return;
            }
            this.readConstraintChain(n2);
        }
        for (n = 0; n < this.mConstraintSetMap.size(); ++n) {
            ((ConstraintSet)this.mConstraintSetMap.valueAt(n)).readFallback(motionLayout);
        }
    }

    public void removeTransition(Transition transition) {
        int n = this.getIndex(transition);
        if (n != -1) {
            this.mTransitionList.remove(n);
        }
    }

    public void setConstraintSet(int n, ConstraintSet constraintSet) {
        this.mConstraintSetMap.put(n, (Object)constraintSet);
    }

    public void setDuration(int n) {
        Transition transition = this.mCurrentTransition;
        if (transition != null) {
            transition.setDuration(n);
        } else {
            this.mDefaultDuration = n;
        }
    }

    public void setKeyframe(View view, int n, String string2, Object object) {
        Object object2 = this.mCurrentTransition;
        if (object2 == null) {
            return;
        }
        object2 = ((Transition)object2).mKeyFramesList.iterator();
        while (object2.hasNext()) {
            Iterator<Key> iterator2 = ((KeyFrames)object2.next()).getKeyFramesForView(view.getId()).iterator();
            while (iterator2.hasNext()) {
                if (iterator2.next().mFramePosition != n) continue;
                float f = 0.0f;
                if (object != null) {
                    f = ((Float)object).floatValue();
                }
                if (f == 0.0f) {
                    // empty if block
                }
                string2.equalsIgnoreCase("app:PerpendicularPath_percent");
            }
        }
    }

    public void setRtl(boolean bl) {
        this.mRtl = bl;
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
        }
    }

    void setTransition(int n, int n2) {
        int n3 = n;
        int n4 = n2;
        Object object = this.mStateSet;
        int n5 = n3;
        int n6 = n4;
        if (object != null) {
            n5 = ((StateSet)object).stateGetConstraintID(n, -1, -1);
            if (n5 != -1) {
                n3 = n5;
            }
            int n7 = this.mStateSet.stateGetConstraintID(n2, -1, -1);
            n5 = n3;
            n6 = n4;
            if (n7 != -1) {
                n6 = n7;
                n5 = n3;
            }
        }
        for (Transition transition : this.mTransitionList) {
            if ((transition.mConstraintSetEnd != n6 || transition.mConstraintSetStart != n5) && (transition.mConstraintSetEnd != n2 || transition.mConstraintSetStart != n)) continue;
            this.mCurrentTransition = transition;
            if (transition != null && transition.mTouchResponse != null) {
                this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
            }
            return;
        }
        object = this.mDefaultTransition;
        for (Transition transition : this.mAbstractTransitionList) {
            if (transition.mConstraintSetEnd != n2) continue;
            object = transition;
        }
        object = new Transition(this, (Transition)object);
        Transition.access$102((Transition)object, n5);
        Transition.access$002((Transition)object, n6);
        if (n5 != -1) {
            this.mTransitionList.add((Transition)object);
        }
        this.mCurrentTransition = object;
    }

    public void setTransition(Transition transition) {
        this.mCurrentTransition = transition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setRTL(this.mRtl);
        }
    }

    void setupTouch() {
        Transition transition = this.mCurrentTransition;
        if (transition != null && transition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setupTouch();
        }
    }

    boolean supportTouch() {
        boolean bl;
        Object object;
        block2: {
            object = this.mTransitionList.iterator();
            do {
                boolean bl2 = object.hasNext();
                bl = true;
                if (!bl2) break block2;
            } while (object.next().mTouchResponse == null);
            return true;
        }
        object = this.mCurrentTransition;
        if (object == null || ((Transition)object).mTouchResponse == null) {
            bl = false;
        }
        return bl;
    }

    public boolean validateLayout(MotionLayout motionLayout) {
        boolean bl = motionLayout == this.mMotionLayout && motionLayout.mScene == this;
        return bl;
    }

    public static class Transition {
        public static final int AUTO_ANIMATE_TO_END = 4;
        public static final int AUTO_ANIMATE_TO_START = 3;
        public static final int AUTO_JUMP_TO_END = 2;
        public static final int AUTO_JUMP_TO_START = 1;
        public static final int AUTO_NONE = 0;
        static final int TRANSITION_FLAG_FIRST_DRAW = 1;
        private int mAutoTransition = 0;
        private int mConstraintSetEnd = -1;
        private int mConstraintSetStart = -1;
        private int mDefaultInterpolator = 0;
        private int mDefaultInterpolatorID = -1;
        private String mDefaultInterpolatorString = null;
        private boolean mDisable = false;
        private int mDuration = 400;
        private int mId = -1;
        private boolean mIsAbstract = false;
        private ArrayList<KeyFrames> mKeyFramesList = new ArrayList();
        private int mLayoutDuringTransition = 0;
        private final MotionScene mMotionScene;
        private ArrayList<TransitionOnClick> mOnClicks = new ArrayList();
        private int mPathMotionArc = -1;
        private float mStagger = 0.0f;
        private TouchResponse mTouchResponse = null;
        private int mTransitionFlags = 0;

        public Transition(int n, MotionScene motionScene, int n2, int n3) {
            this.mId = n;
            this.mMotionScene = motionScene;
            this.mConstraintSetStart = n2;
            this.mConstraintSetEnd = n3;
            this.mDuration = motionScene.mDefaultDuration;
            this.mLayoutDuringTransition = motionScene.mLayoutDuringTransition;
        }

        Transition(MotionScene motionScene, Context context, XmlPullParser xmlPullParser) {
            this.mDuration = motionScene.mDefaultDuration;
            this.mLayoutDuringTransition = motionScene.mLayoutDuringTransition;
            this.mMotionScene = motionScene;
            this.fillFromAttributeList(motionScene, context, Xml.asAttributeSet((XmlPullParser)xmlPullParser));
        }

        Transition(MotionScene motionScene, Transition transition) {
            this.mMotionScene = motionScene;
            if (transition != null) {
                this.mPathMotionArc = transition.mPathMotionArc;
                this.mDefaultInterpolator = transition.mDefaultInterpolator;
                this.mDefaultInterpolatorString = transition.mDefaultInterpolatorString;
                this.mDefaultInterpolatorID = transition.mDefaultInterpolatorID;
                this.mDuration = transition.mDuration;
                this.mKeyFramesList = transition.mKeyFramesList;
                this.mStagger = transition.mStagger;
                this.mLayoutDuringTransition = transition.mLayoutDuringTransition;
            }
        }

        static /* synthetic */ int access$002(Transition transition, int n) {
            transition.mConstraintSetEnd = n;
            return n;
        }

        static /* synthetic */ int access$102(Transition transition, int n) {
            transition.mConstraintSetStart = n;
            return n;
        }

        static /* synthetic */ boolean access$1200(Transition transition) {
            return transition.mIsAbstract;
        }

        static /* synthetic */ TouchResponse access$202(Transition transition, TouchResponse touchResponse) {
            transition.mTouchResponse = touchResponse;
            return touchResponse;
        }

        private void fill(MotionScene motionScene, Context context, TypedArray typedArray) {
            int n = typedArray.getIndexCount();
            for (int i = 0; i < n; ++i) {
                Object object;
                int n2 = typedArray.getIndex(i);
                if (n2 == R.styleable.Transition_constraintSetEnd) {
                    this.mConstraintSetEnd = typedArray.getResourceId(n2, this.mConstraintSetEnd);
                    if (!"layout".equals(context.getResources().getResourceTypeName(this.mConstraintSetEnd))) continue;
                    object = new ConstraintSet();
                    ((ConstraintSet)object).load(context, this.mConstraintSetEnd);
                    motionScene.mConstraintSetMap.append(this.mConstraintSetEnd, object);
                    continue;
                }
                if (n2 == R.styleable.Transition_constraintSetStart) {
                    this.mConstraintSetStart = typedArray.getResourceId(n2, this.mConstraintSetStart);
                    if (!"layout".equals(context.getResources().getResourceTypeName(this.mConstraintSetStart))) continue;
                    object = new ConstraintSet();
                    ((ConstraintSet)object).load(context, this.mConstraintSetStart);
                    motionScene.mConstraintSetMap.append(this.mConstraintSetStart, object);
                    continue;
                }
                if (n2 == R.styleable.Transition_motionInterpolator) {
                    object = typedArray.peekValue(n2);
                    if (((TypedValue)object).type == 1) {
                        this.mDefaultInterpolatorID = n2 = typedArray.getResourceId(n2, -1);
                        if (n2 == -1) continue;
                        this.mDefaultInterpolator = -2;
                        continue;
                    }
                    if (((TypedValue)object).type == 3) {
                        object = typedArray.getString(n2);
                        this.mDefaultInterpolatorString = object;
                        if (((String)object).indexOf("/") > 0) {
                            this.mDefaultInterpolatorID = typedArray.getResourceId(n2, -1);
                            this.mDefaultInterpolator = -2;
                            continue;
                        }
                        this.mDefaultInterpolator = -1;
                        continue;
                    }
                    this.mDefaultInterpolator = typedArray.getInteger(n2, this.mDefaultInterpolator);
                    continue;
                }
                if (n2 == R.styleable.Transition_duration) {
                    this.mDuration = typedArray.getInt(n2, this.mDuration);
                    continue;
                }
                if (n2 == R.styleable.Transition_staggered) {
                    this.mStagger = typedArray.getFloat(n2, this.mStagger);
                    continue;
                }
                if (n2 == R.styleable.Transition_autoTransition) {
                    this.mAutoTransition = typedArray.getInteger(n2, this.mAutoTransition);
                    continue;
                }
                if (n2 == R.styleable.Transition_android_id) {
                    this.mId = typedArray.getResourceId(n2, this.mId);
                    continue;
                }
                if (n2 == R.styleable.Transition_transitionDisable) {
                    this.mDisable = typedArray.getBoolean(n2, this.mDisable);
                    continue;
                }
                if (n2 == R.styleable.Transition_pathMotionArc) {
                    this.mPathMotionArc = typedArray.getInteger(n2, -1);
                    continue;
                }
                if (n2 == R.styleable.Transition_layoutDuringTransition) {
                    this.mLayoutDuringTransition = typedArray.getInteger(n2, 0);
                    continue;
                }
                if (n2 != R.styleable.Transition_transitionFlags) continue;
                this.mTransitionFlags = typedArray.getInteger(n2, 0);
            }
            if (this.mConstraintSetStart == -1) {
                this.mIsAbstract = true;
            }
        }

        private void fillFromAttributeList(MotionScene motionScene, Context context, AttributeSet attributeSet) {
            attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.Transition);
            this.fill(motionScene, context, (TypedArray)attributeSet);
            attributeSet.recycle();
        }

        public void addOnClick(Context context, XmlPullParser xmlPullParser) {
            this.mOnClicks.add(new TransitionOnClick(context, this, xmlPullParser));
        }

        public String debugString(Context object) {
            String string2 = this.mConstraintSetStart == -1 ? "null" : object.getResources().getResourceEntryName(this.mConstraintSetStart);
            if (this.mConstraintSetEnd == -1) {
                object = new StringBuilder();
                ((StringBuilder)object).append(string2);
                ((StringBuilder)object).append(" -> null");
                object = ((StringBuilder)object).toString();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(string2);
                stringBuilder.append(" -> ");
                stringBuilder.append(object.getResources().getResourceEntryName(this.mConstraintSetEnd));
                object = stringBuilder.toString();
            }
            return object;
        }

        public int getAutoTransition() {
            return this.mAutoTransition;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public int getEndConstraintSetId() {
            return this.mConstraintSetEnd;
        }

        public int getId() {
            return this.mId;
        }

        public List<KeyFrames> getKeyFrameList() {
            return this.mKeyFramesList;
        }

        public int getLayoutDuringTransition() {
            return this.mLayoutDuringTransition;
        }

        public List<TransitionOnClick> getOnClickList() {
            return this.mOnClicks;
        }

        public int getPathMotionArc() {
            return this.mPathMotionArc;
        }

        public float getStagger() {
            return this.mStagger;
        }

        public int getStartConstraintSetId() {
            return this.mConstraintSetStart;
        }

        public TouchResponse getTouchResponse() {
            return this.mTouchResponse;
        }

        public boolean isEnabled() {
            return this.mDisable ^ true;
        }

        public boolean isTransitionFlag(int n) {
            boolean bl = (this.mTransitionFlags & n) != 0;
            return bl;
        }

        public void setAutoTransition(int n) {
            this.mAutoTransition = n;
        }

        public void setDuration(int n) {
            this.mDuration = n;
        }

        public void setEnable(boolean bl) {
            this.mDisable = bl ^ true;
        }

        public void setPathMotionArc(int n) {
            this.mPathMotionArc = n;
        }

        public void setStagger(float f) {
            this.mStagger = f;
        }

        static class TransitionOnClick
        implements View.OnClickListener {
            public static final int ANIM_TOGGLE = 17;
            public static final int ANIM_TO_END = 1;
            public static final int ANIM_TO_START = 16;
            public static final int JUMP_TO_END = 256;
            public static final int JUMP_TO_START = 4096;
            int mMode = 17;
            int mTargetId = -1;
            private final Transition mTransition;

            public TransitionOnClick(Context context, Transition transition, XmlPullParser xmlPullParser) {
                this.mTransition = transition;
                context = context.obtainStyledAttributes(Xml.asAttributeSet((XmlPullParser)xmlPullParser), R.styleable.OnClick);
                int n = context.getIndexCount();
                for (int i = 0; i < n; ++i) {
                    int n2 = context.getIndex(i);
                    if (n2 == R.styleable.OnClick_targetId) {
                        this.mTargetId = context.getResourceId(n2, this.mTargetId);
                        continue;
                    }
                    if (n2 != R.styleable.OnClick_clickAction) continue;
                    this.mMode = context.getInt(n2, this.mMode);
                }
                context.recycle();
            }

            public void addOnClickListeners(MotionLayout object, int n, Transition transition) {
                int n2 = this.mTargetId;
                if (n2 != -1) {
                    object = object.findViewById(n2);
                }
                if (object == null) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("OnClick could not find id ");
                    ((StringBuilder)object).append(this.mTargetId);
                    Log.e((String)MotionScene.TAG, (String)((StringBuilder)object).toString());
                    return;
                }
                int n3 = transition.mConstraintSetStart;
                int n4 = transition.mConstraintSetEnd;
                if (n3 == -1) {
                    object.setOnClickListener((View.OnClickListener)this);
                    return;
                }
                int n5 = this.mMode;
                int n6 = 0;
                n2 = (n5 & 1) != 0 && n == n3 ? 1 : 0;
                int n7 = (n5 & 0x100) != 0 && n == n3 ? 1 : 0;
                n3 = (n5 & 1) != 0 && n == n3 ? 1 : 0;
                int n8 = (n5 & 0x10) != 0 && n == n4 ? 1 : 0;
                int n9 = n6;
                if ((n5 & 0x1000) != 0) {
                    n9 = n6;
                    if (n == n4) {
                        n9 = 1;
                    }
                }
                if ((n2 | n7 | n3 | n8 | n9) != 0) {
                    object.setOnClickListener((View.OnClickListener)this);
                }
            }

            boolean isTransitionViable(Transition transition, MotionLayout motionLayout) {
                Transition transition2 = this.mTransition;
                boolean bl = true;
                boolean bl2 = true;
                if (transition2 == transition) {
                    return true;
                }
                int n = transition2.mConstraintSetEnd;
                int n2 = this.mTransition.mConstraintSetStart;
                if (n2 == -1) {
                    if (motionLayout.mCurrentState == n) {
                        bl2 = false;
                    }
                    return bl2;
                }
                bl2 = bl;
                if (motionLayout.mCurrentState != n2) {
                    bl2 = motionLayout.mCurrentState == n ? bl : false;
                }
                return bl2;
            }

            public void onClick(View object) {
                object = this.mTransition.mMotionScene.mMotionLayout;
                if (!((MotionLayout)object).isInteractionEnabled()) {
                    return;
                }
                if (this.mTransition.mConstraintSetStart == -1) {
                    int n = ((MotionLayout)object).getCurrentState();
                    if (n == -1) {
                        ((MotionLayout)object).transitionToState(this.mTransition.mConstraintSetEnd);
                        return;
                    }
                    Transition transition = new Transition(this.mTransition.mMotionScene, this.mTransition);
                    Transition.access$102(transition, n);
                    Transition.access$002(transition, this.mTransition.mConstraintSetEnd);
                    ((MotionLayout)object).setTransition(transition);
                    ((MotionLayout)object).transitionToEnd();
                    return;
                }
                Transition transition = ((Transition)this.mTransition).mMotionScene.mCurrentTransition;
                int n = this.mMode;
                boolean bl = false;
                boolean bl2 = (n & 1) != 0 || (n & 0x100) != 0;
                n = (n & 0x10) == 0 && (n & 0x1000) == 0 ? 0 : 1;
                boolean bl3 = bl;
                if (bl2) {
                    bl3 = bl;
                    if (n != 0) {
                        bl3 = true;
                    }
                }
                int n2 = n;
                bl = bl2;
                if (bl3) {
                    Transition transition2 = ((Transition)this.mTransition).mMotionScene.mCurrentTransition;
                    Transition transition3 = this.mTransition;
                    if (transition2 != transition3) {
                        ((MotionLayout)object).setTransition(transition3);
                    }
                    if (((MotionLayout)object).getCurrentState() != ((MotionLayout)object).getEndState() && !(((MotionLayout)object).getProgress() > 0.5f)) {
                        n2 = 0;
                        bl = bl2;
                    } else {
                        bl = false;
                        n2 = n;
                    }
                }
                if (this.isTransitionViable(transition, (MotionLayout)object)) {
                    if (bl && (1 & this.mMode) != 0) {
                        ((MotionLayout)object).setTransition(this.mTransition);
                        ((MotionLayout)object).transitionToEnd();
                    } else if (n2 != 0 && (this.mMode & 0x10) != 0) {
                        ((MotionLayout)object).setTransition(this.mTransition);
                        ((MotionLayout)object).transitionToStart();
                    } else if (bl && (this.mMode & 0x100) != 0) {
                        ((MotionLayout)object).setTransition(this.mTransition);
                        ((MotionLayout)object).setProgress(1.0f);
                    } else if (n2 != 0 && (this.mMode & 0x1000) != 0) {
                        ((MotionLayout)object).setTransition(this.mTransition);
                        ((MotionLayout)object).setProgress(0.0f);
                    }
                }
            }

            public void removeOnClickListeners(MotionLayout object) {
                int n = this.mTargetId;
                if (n == -1) {
                    return;
                }
                if ((object = object.findViewById(n)) == null) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append(" (*)  could not find id ");
                    ((StringBuilder)object).append(this.mTargetId);
                    Log.e((String)MotionScene.TAG, (String)((StringBuilder)object).toString());
                    return;
                }
                object.setOnClickListener(null);
            }
        }
    }
}

