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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.constraintlayout.widget.ConstraintsChangedListener;
import androidx.constraintlayout.widget.R;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

public class ConstraintLayoutStates {
    private static final boolean DEBUG = false;
    public static final String TAG = "ConstraintLayoutStates";
    private final ConstraintLayout mConstraintLayout;
    private SparseArray<ConstraintSet> mConstraintSetMap;
    private ConstraintsChangedListener mConstraintsChangedListener = null;
    int mCurrentConstraintNumber = -1;
    int mCurrentStateId = -1;
    ConstraintSet mDefaultConstraintSet;
    private SparseArray<State> mStateList = new SparseArray();

    ConstraintLayoutStates(Context context, ConstraintLayout constraintLayout, int n) {
        this.mConstraintSetMap = new SparseArray();
        this.mConstraintLayout = constraintLayout;
        this.load(context, n);
    }

    /*
     * Exception decompiling
     */
    private void load(Context var1_1, int var2_4) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [1[TRYBLOCK]], but top level block is 17[SWITCH]
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

    private void parseConstraintSet(Context context, XmlPullParser xmlPullParser) {
        ConstraintSet constraintSet = new ConstraintSet();
        int n = xmlPullParser.getAttributeCount();
        for (int i = 0; i < n; ++i) {
            if (!"id".equals(xmlPullParser.getAttributeName(i))) continue;
            String string2 = xmlPullParser.getAttributeValue(i);
            i = -1;
            if (string2.contains("/")) {
                String string3 = string2.substring(string2.indexOf(47) + 1);
                i = context.getResources().getIdentifier(string3, "id", context.getPackageName());
            }
            n = i;
            if (i == -1) {
                if (string2 != null && string2.length() > 1) {
                    n = Integer.parseInt(string2.substring(1));
                } else {
                    Log.e((String)TAG, (String)"error in parsing id");
                    n = i;
                }
            }
            constraintSet.load(context, xmlPullParser);
            this.mConstraintSetMap.put(n, (Object)constraintSet);
            break;
        }
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

    public void updateConstraints(int n, float f, float f2) {
        int n2 = this.mCurrentStateId;
        if (n2 == n) {
            Object object = n == -1 ? (State)this.mStateList.valueAt(0) : (State)this.mStateList.get(n2);
            if (this.mCurrentConstraintNumber != -1 && ((State)object).mVariants.get(this.mCurrentConstraintNumber).match(f, f2)) {
                return;
            }
            n2 = ((State)object).findMatch(f, f2);
            if (this.mCurrentConstraintNumber == n2) {
                return;
            }
            ConstraintSet constraintSet = n2 == -1 ? this.mDefaultConstraintSet : ((State)object).mVariants.get((int)n2).mConstraintSet;
            n = n2 == -1 ? ((State)object).mConstraintID : ((State)object).mVariants.get((int)n2).mConstraintID;
            if (constraintSet == null) {
                return;
            }
            this.mCurrentConstraintNumber = n2;
            object = this.mConstraintsChangedListener;
            if (object != null) {
                ((ConstraintsChangedListener)object).preLayoutChange(-1, n);
            }
            constraintSet.applyTo(this.mConstraintLayout);
            object = this.mConstraintsChangedListener;
            if (object != null) {
                ((ConstraintsChangedListener)object).postLayoutChange(-1, n);
            }
        } else {
            this.mCurrentStateId = n;
            Object object = (State)this.mStateList.get(n);
            int n3 = ((State)object).findMatch(f, f2);
            Object object2 = n3 == -1 ? ((State)object).mConstraintSet : ((State)object).mVariants.get((int)n3).mConstraintSet;
            n2 = n3 == -1 ? ((State)object).mConstraintID : ((State)object).mVariants.get((int)n3).mConstraintID;
            if (object2 == null) {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("NO Constraint set found ! id=");
                ((StringBuilder)object2).append(n);
                ((StringBuilder)object2).append(", dim =");
                ((StringBuilder)object2).append(f);
                ((StringBuilder)object2).append(", ");
                ((StringBuilder)object2).append(f2);
                Log.v((String)TAG, (String)((StringBuilder)object2).toString());
                return;
            }
            this.mCurrentConstraintNumber = n3;
            object = this.mConstraintsChangedListener;
            if (object != null) {
                ((ConstraintsChangedListener)object).preLayoutChange(n, n2);
            }
            ((ConstraintSet)object2).applyTo(this.mConstraintLayout);
            object2 = this.mConstraintsChangedListener;
            if (object2 != null) {
                ((ConstraintsChangedListener)object2).postLayoutChange(n, n2);
            }
        }
    }

    static class State {
        int mConstraintID = -1;
        ConstraintSet mConstraintSet;
        int mId;
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
                Object object = context.getResources().getResourceTypeName(this.mConstraintID);
                context.getResources().getResourceName(this.mConstraintID);
                if (!"layout".equals(object)) continue;
                this.mConstraintSet = object = new ConstraintSet();
                ((ConstraintSet)object).clone(context, this.mConstraintID);
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
        ConstraintSet mConstraintSet;
        int mId;
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
                    Object object = context.getResources().getResourceTypeName(this.mConstraintID);
                    context.getResources().getResourceName(this.mConstraintID);
                    if (!"layout".equals(object)) continue;
                    this.mConstraintSet = object = new ConstraintSet();
                    ((ConstraintSet)object).clone(context, this.mConstraintID);
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
                Log.v((String)ConstraintLayoutStates.TAG, (String)"Unknown tag");
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

