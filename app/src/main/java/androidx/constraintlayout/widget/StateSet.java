/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  android.util.SparseArray
 *  android.util.Xml
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.constraintlayout.widget;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.util.Xml;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.ConstraintsChangedListener;
import androidx.constraintlayout.widget.R;
import java.util.ArrayList;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;

public class StateSet {
    private static final boolean DEBUG = false;
    public static final String TAG = "ConstraintLayoutStates";
    private SparseArray<ConstraintSet> mConstraintSetMap;
    private ConstraintsChangedListener mConstraintsChangedListener = null;
    int mCurrentConstraintNumber = -1;
    int mCurrentStateId = -1;
    ConstraintSet mDefaultConstraintSet;
    int mDefaultState = -1;
    private SparseArray<State> mStateList = new SparseArray();

    public StateSet(Context context, XmlPullParser xmlPullParser) {
        this.mConstraintSetMap = new SparseArray();
        this.load(context, xmlPullParser);
    }

    /*
     * Exception decompiling
     */
    private void load(Context var1_1, XmlPullParser var2_4) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [12[CASE]], but top level block is 1[TRYBLOCK]
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

    public int convertToConstraintSet(int n, int n2, float f, float f2) {
        State state = (State)this.mStateList.get(n2);
        if (state == null) {
            return n2;
        }
        if (f != -1.0f && f2 != -1.0f) {
            Variant variant = null;
            for (Variant variant2 : state.mVariants) {
                if (!variant2.match(f, f2)) continue;
                if (n == variant2.mConstraintID) {
                    return n;
                }
                variant = variant2;
            }
            if (variant != null) {
                return variant.mConstraintID;
            }
            return state.mConstraintID;
        }
        if (state.mConstraintID == n) {
            return n;
        }
        Iterator<Variant> iterator2 = state.mVariants.iterator();
        while (iterator2.hasNext()) {
            if (n != iterator2.next().mConstraintID) continue;
            return n;
        }
        return state.mConstraintID;
    }

    public boolean needsToChange(int n, float f, float f2) {
        int n2 = this.mCurrentStateId;
        if (n2 != n) {
            return true;
        }
        Object object = n == -1 ? this.mStateList.valueAt(0) : this.mStateList.get(n2);
        object = (State)object;
        if (this.mCurrentConstraintNumber != -1 && ((State)object).mVariants.get(this.mCurrentConstraintNumber).match(f, f2)) {
            return false;
        }
        return this.mCurrentConstraintNumber != ((State)object).findMatch(f, f2);
    }

    public void setOnConstraintsChanged(ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
    }

    public int stateGetConstraintID(int n, int n2, int n3) {
        return this.updateConstraints(-1, n, n2, n3);
    }

    public int updateConstraints(int n, int n2, float f, float f2) {
        if (n == n2) {
            State state = n2 == -1 ? (State)this.mStateList.valueAt(0) : (State)this.mStateList.get(this.mCurrentStateId);
            if (state == null) {
                return -1;
            }
            if (this.mCurrentConstraintNumber != -1 && state.mVariants.get(n).match(f, f2)) {
                return n;
            }
            n2 = state.findMatch(f, f2);
            if (n == n2) {
                return n;
            }
            n = n2 == -1 ? state.mConstraintID : state.mVariants.get((int)n2).mConstraintID;
            return n;
        }
        State state = (State)this.mStateList.get(n2);
        if (state == null) {
            return -1;
        }
        n = state.findMatch(f, f2);
        n = n == -1 ? state.mConstraintID : state.mVariants.get((int)n).mConstraintID;
        return n;
    }

    static class State {
        int mConstraintID = -1;
        int mId;
        boolean mIsLayout = false;
        ArrayList<Variant> mVariants = new ArrayList();

        public State(Context context, XmlPullParser xmlPullParser) {
            xmlPullParser = context.obtainStyledAttributes(Xml.asAttributeSet((XmlPullParser)xmlPullParser), R.styleable.State);
            int n = xmlPullParser.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = xmlPullParser.getIndex(i);
                if (n2 == R.styleable.State_android_id) {
                    this.mId = xmlPullParser.getResourceId(n2, this.mId);
                    continue;
                }
                if (n2 != R.styleable.State_constraints) continue;
                this.mConstraintID = xmlPullParser.getResourceId(n2, this.mConstraintID);
                String string2 = context.getResources().getResourceTypeName(this.mConstraintID);
                context.getResources().getResourceName(this.mConstraintID);
                if (!"layout".equals(string2)) continue;
                this.mIsLayout = true;
            }
            xmlPullParser.recycle();
        }

        void add(Variant variant) {
            this.mVariants.add(variant);
        }

        public int findMatch(float f, float f2) {
            for (int i = 0; i < this.mVariants.size(); ++i) {
                if (!this.mVariants.get(i).match(f, f2)) continue;
                return i;
            }
            return -1;
        }
    }

    static class Variant {
        int mConstraintID = -1;
        int mId;
        boolean mIsLayout = false;
        float mMaxHeight;
        float mMaxWidth;
        float mMinHeight;
        float mMinWidth = Float.NaN;

        public Variant(Context context, XmlPullParser xmlPullParser) {
            this.mMinHeight = Float.NaN;
            this.mMaxWidth = Float.NaN;
            this.mMaxHeight = Float.NaN;
            xmlPullParser = context.obtainStyledAttributes(Xml.asAttributeSet((XmlPullParser)xmlPullParser), R.styleable.Variant);
            int n = xmlPullParser.getIndexCount();
            for (int i = 0; i < n; ++i) {
                int n2 = xmlPullParser.getIndex(i);
                if (n2 == R.styleable.Variant_constraints) {
                    this.mConstraintID = xmlPullParser.getResourceId(n2, this.mConstraintID);
                    String string2 = context.getResources().getResourceTypeName(this.mConstraintID);
                    context.getResources().getResourceName(this.mConstraintID);
                    if (!"layout".equals(string2)) continue;
                    this.mIsLayout = true;
                    continue;
                }
                if (n2 == R.styleable.Variant_region_heightLessThan) {
                    this.mMaxHeight = xmlPullParser.getDimension(n2, this.mMaxHeight);
                    continue;
                }
                if (n2 == R.styleable.Variant_region_heightMoreThan) {
                    this.mMinHeight = xmlPullParser.getDimension(n2, this.mMinHeight);
                    continue;
                }
                if (n2 == R.styleable.Variant_region_widthLessThan) {
                    this.mMaxWidth = xmlPullParser.getDimension(n2, this.mMaxWidth);
                    continue;
                }
                if (n2 == R.styleable.Variant_region_widthMoreThan) {
                    this.mMinWidth = xmlPullParser.getDimension(n2, this.mMinWidth);
                    continue;
                }
                Log.v((String)StateSet.TAG, (String)"Unknown tag");
            }
            xmlPullParser.recycle();
        }

        boolean match(float f, float f2) {
            if (!Float.isNaN(this.mMinWidth) && f < this.mMinWidth) {
                return false;
            }
            if (!Float.isNaN(this.mMinHeight) && f2 < this.mMinHeight) {
                return false;
            }
            if (!Float.isNaN(this.mMaxWidth) && f > this.mMaxWidth) {
                return false;
            }
            return Float.isNaN(this.mMaxHeight) || !(f2 > this.mMaxHeight);
        }
    }
}

