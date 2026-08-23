/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Pair
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.constraintlayout.motion.widget;

import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.motion.widget.Key;
import androidx.constraintlayout.motion.widget.KeyPositionBase;
import androidx.constraintlayout.motion.widget.MotionController;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.constraintlayout.motion.widget.ProxyInterface;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.R;
import java.io.PrintStream;
import java.util.HashMap;

public class DesignTool
implements ProxyInterface {
    private static final boolean DEBUG = false;
    private static final String TAG = "DesignTool";
    static final HashMap<Pair<Integer, Integer>, String> allAttributes;
    static final HashMap<String, String> allMargins;
    private String mLastEndState = null;
    private int mLastEndStateId = -1;
    private String mLastStartState = null;
    private int mLastStartStateId = -1;
    private final MotionLayout mMotionLayout;
    private MotionScene mSceneCache;

    static {
        HashMap<Pair, String> hashMap = new HashMap<Pair, String>();
        allAttributes = hashMap;
        HashMap<String, String> hashMap2 = new HashMap<String, String>();
        allMargins = hashMap2;
        Integer n = 4;
        hashMap.put(Pair.create((Object)n, (Object)n), "layout_constraintBottom_toBottomOf");
        Integer n2 = 3;
        hashMap.put(Pair.create((Object)n, (Object)n2), "layout_constraintBottom_toTopOf");
        hashMap.put(Pair.create((Object)n2, (Object)n), "layout_constraintTop_toBottomOf");
        hashMap.put(Pair.create((Object)n2, (Object)n2), "layout_constraintTop_toTopOf");
        n = 6;
        hashMap.put(Pair.create((Object)n, (Object)n), "layout_constraintStart_toStartOf");
        n2 = 7;
        hashMap.put(Pair.create((Object)n, (Object)n2), "layout_constraintStart_toEndOf");
        hashMap.put(Pair.create((Object)n2, (Object)n), "layout_constraintEnd_toStartOf");
        hashMap.put(Pair.create((Object)n2, (Object)n2), "layout_constraintEnd_toEndOf");
        n = 1;
        hashMap.put(Pair.create((Object)n, (Object)n), "layout_constraintLeft_toLeftOf");
        n2 = 2;
        hashMap.put(Pair.create((Object)n, (Object)n2), "layout_constraintLeft_toRightOf");
        hashMap.put(Pair.create((Object)n2, (Object)n2), "layout_constraintRight_toRightOf");
        hashMap.put(Pair.create((Object)n2, (Object)n), "layout_constraintRight_toLeftOf");
        n = 5;
        hashMap.put(Pair.create((Object)n, (Object)n), "layout_constraintBaseline_toBaselineOf");
        hashMap2.put("layout_constraintBottom_toBottomOf", "layout_marginBottom");
        hashMap2.put("layout_constraintBottom_toTopOf", "layout_marginBottom");
        hashMap2.put("layout_constraintTop_toBottomOf", "layout_marginTop");
        hashMap2.put("layout_constraintTop_toTopOf", "layout_marginTop");
        hashMap2.put("layout_constraintStart_toStartOf", "layout_marginStart");
        hashMap2.put("layout_constraintStart_toEndOf", "layout_marginStart");
        hashMap2.put("layout_constraintEnd_toStartOf", "layout_marginEnd");
        hashMap2.put("layout_constraintEnd_toEndOf", "layout_marginEnd");
        hashMap2.put("layout_constraintLeft_toLeftOf", "layout_marginLeft");
        hashMap2.put("layout_constraintLeft_toRightOf", "layout_marginLeft");
        hashMap2.put("layout_constraintRight_toRightOf", "layout_marginRight");
        hashMap2.put("layout_constraintRight_toLeftOf", "layout_marginRight");
    }

    public DesignTool(MotionLayout motionLayout) {
        this.mMotionLayout = motionLayout;
    }

    private static void Connect(int n, ConstraintSet constraintSet, View view, HashMap<String, String> hashMap, int n2, int n3) {
        block0: {
            String string2 = allAttributes.get(Pair.create((Object)n2, (Object)n3));
            String string3 = hashMap.get(string2);
            if (string3 == null) break block0;
            int n4 = 0;
            n = (string2 = allMargins.get(string2)) != null ? DesignTool.GetPxFromDp(n, hashMap.get(string2)) : n4;
            n4 = Integer.parseInt(string3);
            constraintSet.connect(view.getId(), n2, n4, n3, n);
        }
    }

    private static int GetPxFromDp(int n, String string2) {
        if (string2 == null) {
            return 0;
        }
        int n2 = string2.indexOf(100);
        if (n2 == -1) {
            return 0;
        }
        return (int)((float)(Integer.valueOf(string2.substring(0, n2)) * n) / 160.0f);
    }

    private static void SetAbsolutePositions(int n, ConstraintSet constraintSet, View view, HashMap<String, String> object) {
        String string2 = ((HashMap)object).get("layout_editor_absoluteX");
        if (string2 != null) {
            constraintSet.setEditorAbsoluteX(view.getId(), DesignTool.GetPxFromDp(n, string2));
        }
        if ((object = ((HashMap)object).get("layout_editor_absoluteY")) != null) {
            constraintSet.setEditorAbsoluteY(view.getId(), DesignTool.GetPxFromDp(n, (String)object));
        }
    }

    private static void SetBias(ConstraintSet constraintSet, View view, HashMap<String, String> object, int n) {
        String string2 = "layout_constraintHorizontal_bias";
        if (n == 1) {
            string2 = "layout_constraintVertical_bias";
        }
        if ((object = ((HashMap)object).get(string2)) != null) {
            if (n == 0) {
                constraintSet.setHorizontalBias(view.getId(), Float.parseFloat((String)object));
            } else if (n == 1) {
                constraintSet.setVerticalBias(view.getId(), Float.parseFloat((String)object));
            }
        }
    }

    private static void SetDimensions(int n, ConstraintSet constraintSet, View view, HashMap<String, String> object, int n2) {
        String string2 = "layout_width";
        if (n2 == 1) {
            string2 = "layout_height";
        }
        if ((object = ((HashMap)object).get(string2)) != null) {
            int n3 = -2;
            if (!((String)object).equalsIgnoreCase("wrap_content")) {
                n3 = DesignTool.GetPxFromDp(n, (String)object);
            }
            if (n2 == 0) {
                constraintSet.constrainWidth(view.getId(), n3);
            } else {
                constraintSet.constrainHeight(view.getId(), n3);
            }
        }
    }

    @Override
    public int designAccess(int n, String string2, Object object, float[] object2, int n2, float[] fArray, int n3) {
        object2 = (View)object;
        object = null;
        if (n != 0) {
            if (this.mMotionLayout.mScene == null) {
                return -1;
            }
            if (object2 != null) {
                object2 = this.mMotionLayout.mFrameArrayList.get(object2);
                object = object2;
                if (object2 == null) {
                    return -1;
                }
            } else {
                return -1;
            }
        }
        switch (n) {
            default: {
                return -1;
            }
            case 3: {
                n = this.mMotionLayout.mScene.getDuration() / 16;
                return ((MotionController)object).getAttributeValues(string2, fArray, n3);
            }
            case 2: {
                n = this.mMotionLayout.mScene.getDuration() / 16;
                ((MotionController)object).buildKeyFrames(fArray, null);
                return n;
            }
            case 1: {
                n = this.mMotionLayout.mScene.getDuration() / 16;
                ((MotionController)object).buildPath(fArray, n);
                return n;
            }
            case 0: 
        }
        return 1;
    }

    public void disableAutoTransition(boolean bl) {
        this.mMotionLayout.disableAutoTransition(bl);
    }

    public void dumpConstraintSet(String string2) {
        if (this.mMotionLayout.mScene == null) {
            this.mMotionLayout.mScene = this.mSceneCache;
        }
        int n = this.mMotionLayout.lookUpConstraintId(string2);
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" dumping  ");
        stringBuilder.append(string2);
        stringBuilder.append(" (");
        stringBuilder.append(n);
        stringBuilder.append(")");
        printStream.println(stringBuilder.toString());
        try {
            this.mMotionLayout.mScene.getConstraintSet(n).dump(this.mMotionLayout.mScene, new int[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public int getAnimationKeyFrames(Object object, float[] fArray) {
        if (this.mMotionLayout.mScene == null) {
            return -1;
        }
        int n = this.mMotionLayout.mScene.getDuration() / 16;
        if ((object = this.mMotionLayout.mFrameArrayList.get(object)) == null) {
            return 0;
        }
        ((MotionController)object).buildKeyFrames(fArray, null);
        return n;
    }

    public int getAnimationPath(Object object, float[] fArray, int n) {
        if (this.mMotionLayout.mScene == null) {
            return -1;
        }
        if ((object = this.mMotionLayout.mFrameArrayList.get(object)) == null) {
            return 0;
        }
        ((MotionController)object).buildPath(fArray, n);
        return n;
    }

    public void getAnimationRectangles(Object object, float[] fArray) {
        if (this.mMotionLayout.mScene == null) {
            return;
        }
        int n = this.mMotionLayout.mScene.getDuration() / 16;
        if ((object = this.mMotionLayout.mFrameArrayList.get(object)) == null) {
            return;
        }
        ((MotionController)object).buildRectangles(fArray, n);
    }

    public String getEndState() {
        int n = this.mMotionLayout.getEndState();
        if (this.mLastEndStateId == n) {
            return this.mLastEndState;
        }
        String string2 = this.mMotionLayout.getConstraintSetNames(n);
        if (string2 != null) {
            this.mLastEndState = string2;
            this.mLastEndStateId = n;
        }
        return string2;
    }

    public int getKeyFrameInfo(Object object, int n, int[] nArray) {
        if ((object = this.mMotionLayout.mFrameArrayList.get((View)object)) == null) {
            return 0;
        }
        return ((MotionController)object).getKeyFrameInfo(n, nArray);
    }

    @Override
    public float getKeyFramePosition(Object object, int n, float f, float f2) {
        return this.mMotionLayout.mFrameArrayList.get((View)object).getKeyFrameParameter(n, f, f2);
    }

    public int getKeyFramePositions(Object object, int[] nArray, float[] fArray) {
        if ((object = this.mMotionLayout.mFrameArrayList.get((View)object)) == null) {
            return 0;
        }
        return ((MotionController)object).getkeyFramePositions(nArray, fArray);
    }

    public Object getKeyframe(int n, int n2, int n3) {
        if (this.mMotionLayout.mScene == null) {
            return null;
        }
        return this.mMotionLayout.mScene.getKeyFrame(this.mMotionLayout.getContext(), n, n2, n3);
    }

    public Object getKeyframe(Object object, int n, int n2) {
        if (this.mMotionLayout.mScene == null) {
            return null;
        }
        int n3 = ((View)object).getId();
        return this.mMotionLayout.mScene.getKeyFrame(this.mMotionLayout.getContext(), n, n3, n2);
    }

    @Override
    public Object getKeyframeAtLocation(Object object, float f, float f2) {
        View view = (View)object;
        if (this.mMotionLayout.mScene == null) {
            return -1;
        }
        if (view != null) {
            object = this.mMotionLayout.mFrameArrayList.get(view);
            if (object == null) {
                return null;
            }
            view = (ViewGroup)view.getParent();
            return ((MotionController)object).getPositionKeyframe(view.getWidth(), view.getHeight(), f, f2);
        }
        return null;
    }

    @Override
    public Boolean getPositionKeyframe(Object object, Object object2, float f, float f2, String[] stringArray, float[] fArray) {
        if (object instanceof KeyPositionBase) {
            object = (KeyPositionBase)object;
            this.mMotionLayout.mFrameArrayList.get((View)object2).positionKeyframe((View)object2, (KeyPositionBase)object, f, f2, stringArray, fArray);
            this.mMotionLayout.rebuildScene();
            this.mMotionLayout.mInTransition = true;
            return true;
        }
        return false;
    }

    public float getProgress() {
        return this.mMotionLayout.getProgress();
    }

    public String getStartState() {
        int n = this.mMotionLayout.getStartState();
        if (this.mLastStartStateId == n) {
            return this.mLastStartState;
        }
        String string2 = this.mMotionLayout.getConstraintSetNames(n);
        if (string2 != null) {
            this.mLastStartState = string2;
            this.mLastStartStateId = n;
        }
        return this.mMotionLayout.getConstraintSetNames(n);
    }

    public String getState() {
        if (this.mLastStartState != null && this.mLastEndState != null) {
            float f = this.getProgress();
            if (f <= 0.01f) {
                return this.mLastStartState;
            }
            if (f >= 1.0f - 0.01f) {
                return this.mLastEndState;
            }
        }
        return this.mLastStartState;
    }

    @Override
    public long getTransitionTimeMs() {
        return this.mMotionLayout.getTransitionTimeMs();
    }

    public boolean isInTransition() {
        boolean bl = this.mLastStartState != null && this.mLastEndState != null;
        return bl;
    }

    @Override
    public void setAttributes(int n, String object, Object object2, Object object3) {
        object2 = (View)object2;
        object3 = (HashMap)object3;
        int n2 = this.mMotionLayout.lookUpConstraintId((String)object);
        if ((object = this.mMotionLayout.mScene.getConstraintSet(n2)) == null) {
            return;
        }
        ((ConstraintSet)object).clear(object2.getId());
        DesignTool.SetDimensions(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 0);
        DesignTool.SetDimensions(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 1);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 6, 6);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 6, 7);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 7, 7);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 7, 6);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 1, 1);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 1, 2);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 2, 2);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 2, 1);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 3, 3);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 3, 4);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 4, 3);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 4, 4);
        DesignTool.Connect(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 5, 5);
        DesignTool.SetBias((ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 0);
        DesignTool.SetBias((ConstraintSet)object, (View)object2, (HashMap<String, String>)object3, 1);
        DesignTool.SetAbsolutePositions(n, (ConstraintSet)object, (View)object2, (HashMap<String, String>)object3);
        this.mMotionLayout.updateState(n2, (ConstraintSet)object);
        this.mMotionLayout.requestLayout();
    }

    @Override
    public void setKeyFrame(Object object, int n, String string2, Object object2) {
        if (this.mMotionLayout.mScene != null) {
            this.mMotionLayout.mScene.setKeyframe((View)object, n, string2, object2);
            this.mMotionLayout.mTransitionGoalPosition = (float)n / 100.0f;
            this.mMotionLayout.mTransitionLastPosition = 0.0f;
            this.mMotionLayout.rebuildScene();
            this.mMotionLayout.evaluate(true);
        }
    }

    @Override
    public boolean setKeyFramePosition(Object object, int n, int n2, float f, float f2) {
        if (this.mMotionLayout.mScene != null) {
            MotionController motionController = this.mMotionLayout.mFrameArrayList.get(object);
            n = (int)(this.mMotionLayout.mTransitionPosition * 100.0f);
            if (motionController != null && this.mMotionLayout.mScene.hasKeyFramePosition((View)object, n)) {
                float f3 = motionController.getKeyFrameParameter(2, f, f2);
                f = motionController.getKeyFrameParameter(5, f, f2);
                this.mMotionLayout.mScene.setKeyframe((View)object, n, "motion:percentX", Float.valueOf(f3));
                this.mMotionLayout.mScene.setKeyframe((View)object, n, "motion:percentY", Float.valueOf(f));
                this.mMotionLayout.rebuildScene();
                this.mMotionLayout.evaluate(true);
                this.mMotionLayout.invalidate();
                return true;
            }
        }
        return false;
    }

    public void setKeyframe(Object object, String string2, Object object2) {
        if (object instanceof Key) {
            ((Key)object).setValue(string2, object2);
            this.mMotionLayout.rebuildScene();
            this.mMotionLayout.mInTransition = true;
        }
    }

    public void setState(String string2) {
        String string3 = string2;
        if (string2 == null) {
            string3 = "motion_base";
        }
        if (this.mLastStartState == string3) {
            return;
        }
        this.mLastStartState = string3;
        this.mLastEndState = null;
        if (this.mMotionLayout.mScene == null) {
            this.mMotionLayout.mScene = this.mSceneCache;
        }
        int n = string3 != null ? this.mMotionLayout.lookUpConstraintId(string3) : R.id.motion_base;
        this.mLastStartStateId = n;
        if (n != 0) {
            if (n == this.mMotionLayout.getStartState()) {
                this.mMotionLayout.setProgress(0.0f);
            } else if (n == this.mMotionLayout.getEndState()) {
                this.mMotionLayout.setProgress(1.0f);
            } else {
                this.mMotionLayout.transitionToState(n);
                this.mMotionLayout.setProgress(1.0f);
            }
        }
        this.mMotionLayout.requestLayout();
    }

    @Override
    public void setToolPosition(float f) {
        if (this.mMotionLayout.mScene == null) {
            this.mMotionLayout.mScene = this.mSceneCache;
        }
        this.mMotionLayout.setProgress(f);
        this.mMotionLayout.evaluate(true);
        this.mMotionLayout.requestLayout();
        this.mMotionLayout.invalidate();
    }

    public void setTransition(String string2, String string3) {
        if (this.mMotionLayout.mScene == null) {
            this.mMotionLayout.mScene = this.mSceneCache;
        }
        int n = this.mMotionLayout.lookUpConstraintId(string2);
        int n2 = this.mMotionLayout.lookUpConstraintId(string3);
        this.mMotionLayout.setTransition(n, n2);
        this.mLastStartStateId = n;
        this.mLastEndStateId = n2;
        this.mLastStartState = string2;
        this.mLastEndState = string3;
    }

    public void setViewDebug(Object object, int n) {
        if ((object = this.mMotionLayout.mFrameArrayList.get(object)) != null) {
            ((MotionController)object).setDrawPath(n);
            this.mMotionLayout.invalidate();
        }
    }
}

