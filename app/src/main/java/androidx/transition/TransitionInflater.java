/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 *  android.view.InflateException
 *  android.view.ViewGroup
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.transition;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.ViewGroup;
import androidx.collection.ArrayMap;
import androidx.core.content.res.TypedArrayUtils;
import androidx.transition.ArcMotion;
import androidx.transition.AutoTransition;
import androidx.transition.ChangeBounds;
import androidx.transition.ChangeClipBounds;
import androidx.transition.ChangeImageTransform;
import androidx.transition.ChangeScroll;
import androidx.transition.ChangeTransform;
import androidx.transition.Explode;
import androidx.transition.Fade;
import androidx.transition.PathMotion;
import androidx.transition.PatternPathMotion;
import androidx.transition.Scene;
import androidx.transition.Slide;
import androidx.transition.Styleable;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.transition.TransitionSet;
import java.io.IOException;
import java.lang.reflect.Constructor;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class TransitionInflater {
    private static final ArrayMap<String, Constructor> CONSTRUCTORS;
    private static final Class<?>[] CONSTRUCTOR_SIGNATURE;
    private final Context mContext;

    static {
        CONSTRUCTOR_SIGNATURE = new Class[]{Context.class, AttributeSet.class};
        CONSTRUCTORS = new ArrayMap();
    }

    private TransitionInflater(Context context) {
        this.mContext = context;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Object createCustom(AttributeSet object, Class clazz, String constructor) {
        ArrayMap<String, Constructor> arrayMap;
        String string2 = object.getAttributeValue(null, "class");
        if (string2 == null) {
            object = new StringBuilder();
            object.append((String)((Object)constructor));
            object.append(" tag must have a 'class' attribute");
            throw new InflateException(object.toString());
        }
        try {
            arrayMap = CONSTRUCTORS;
            synchronized (arrayMap) {
            }
        }
        catch (Exception exception) {
            object = new StringBuilder();
            object.append("Could not instantiate ");
            object.append(clazz);
            object.append(" class ");
            object.append(string2);
            throw new InflateException(object.toString(), (Throwable)exception);
        }
        {
            Constructor constructor2 = (Constructor)arrayMap.get(string2);
            constructor = constructor2;
            if (constructor2 == null) {
                Class clazz2 = this.mContext.getClassLoader().loadClass(string2).asSubclass(clazz);
                constructor = constructor2;
                if (clazz2 != null) {
                    constructor = clazz2.getConstructor(CONSTRUCTOR_SIGNATURE);
                    constructor.setAccessible(true);
                    arrayMap.put(string2, constructor);
                }
            }
            return constructor.newInstance(this.mContext, object);
        }
    }

    private Transition createTransitionFromXml(XmlPullParser xmlPullParser, AttributeSet object, Transition transition) throws XmlPullParserException, IOException {
        int n;
        Object object2 = null;
        int n2 = xmlPullParser.getDepth();
        TransitionSet transitionSet = transition instanceof TransitionSet ? (TransitionSet)transition : null;
        while (((n = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n2) && n != 1) {
            block27: {
                block28: {
                    Object object3;
                    block11: {
                        block25: {
                            block26: {
                                block23: {
                                    block24: {
                                        block22: {
                                            block21: {
                                                block20: {
                                                    block19: {
                                                        block18: {
                                                            block17: {
                                                                block16: {
                                                                    block15: {
                                                                        block14: {
                                                                            block13: {
                                                                                block12: {
                                                                                    block10: {
                                                                                        if (n != 2) continue;
                                                                                        object3 = xmlPullParser.getName();
                                                                                        if (!"fade".equals(object3)) break block10;
                                                                                        object2 = new Fade(this.mContext, (AttributeSet)object);
                                                                                        break block11;
                                                                                    }
                                                                                    if (!"changeBounds".equals(object3)) break block12;
                                                                                    object2 = new ChangeBounds(this.mContext, (AttributeSet)object);
                                                                                    break block11;
                                                                                }
                                                                                if (!"slide".equals(object3)) break block13;
                                                                                object2 = new Slide(this.mContext, (AttributeSet)object);
                                                                                break block11;
                                                                            }
                                                                            if (!"explode".equals(object3)) break block14;
                                                                            object2 = new Explode(this.mContext, (AttributeSet)object);
                                                                            break block11;
                                                                        }
                                                                        if (!"changeImageTransform".equals(object3)) break block15;
                                                                        object2 = new ChangeImageTransform(this.mContext, (AttributeSet)object);
                                                                        break block11;
                                                                    }
                                                                    if (!"changeTransform".equals(object3)) break block16;
                                                                    object2 = new ChangeTransform(this.mContext, (AttributeSet)object);
                                                                    break block11;
                                                                }
                                                                if (!"changeClipBounds".equals(object3)) break block17;
                                                                object2 = new ChangeClipBounds(this.mContext, (AttributeSet)object);
                                                                break block11;
                                                            }
                                                            if (!"autoTransition".equals(object3)) break block18;
                                                            object2 = new AutoTransition(this.mContext, (AttributeSet)object);
                                                            break block11;
                                                        }
                                                        if (!"changeScroll".equals(object3)) break block19;
                                                        object2 = new ChangeScroll(this.mContext, (AttributeSet)object);
                                                        break block11;
                                                    }
                                                    if (!"transitionSet".equals(object3)) break block20;
                                                    object2 = new TransitionSet(this.mContext, (AttributeSet)object);
                                                    break block11;
                                                }
                                                if (!"transition".equals(object3)) break block21;
                                                object2 = (Transition)this.createCustom((AttributeSet)object, Transition.class, "transition");
                                                break block11;
                                            }
                                            if (!"targets".equals(object3)) break block22;
                                            this.getTargetIds(xmlPullParser, (AttributeSet)object, transition);
                                            break block11;
                                        }
                                        if (!"arcMotion".equals(object3)) break block23;
                                        if (transition == null) break block24;
                                        transition.setPathMotion(new ArcMotion(this.mContext, (AttributeSet)object));
                                        break block11;
                                    }
                                    throw new RuntimeException("Invalid use of arcMotion element");
                                }
                                if (!"pathMotion".equals(object3)) break block25;
                                if (transition == null) break block26;
                                transition.setPathMotion((PathMotion)this.createCustom((AttributeSet)object, PathMotion.class, "pathMotion"));
                                break block11;
                            }
                            throw new RuntimeException("Invalid use of pathMotion element");
                        }
                        if (!"patternPathMotion".equals(object3)) break block27;
                        if (transition == null) break block28;
                        transition.setPathMotion(new PatternPathMotion(this.mContext, (AttributeSet)object));
                    }
                    object3 = object2;
                    if (object2 != null) {
                        if (!xmlPullParser.isEmptyElementTag()) {
                            this.createTransitionFromXml(xmlPullParser, (AttributeSet)object, (Transition)object2);
                        }
                        if (transitionSet != null) {
                            transitionSet.addTransition((Transition)object2);
                            object3 = null;
                        } else if (transition == null) {
                            object3 = object2;
                        } else {
                            throw new InflateException("Could not add transition to another transition.");
                        }
                    }
                    object2 = object3;
                    continue;
                }
                throw new RuntimeException("Invalid use of patternPathMotion element");
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unknown scene name: ");
            ((StringBuilder)object).append(xmlPullParser.getName());
            throw new RuntimeException(((StringBuilder)object).toString());
        }
        return object2;
    }

    private TransitionManager createTransitionManagerFromXml(XmlPullParser xmlPullParser, AttributeSet object, ViewGroup viewGroup) throws XmlPullParserException, IOException {
        int n;
        int n2 = xmlPullParser.getDepth();
        TransitionManager transitionManager = null;
        while (((n = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n2) && n != 1) {
            if (n != 2) continue;
            String string2 = xmlPullParser.getName();
            if (string2.equals("transitionManager")) {
                transitionManager = new TransitionManager();
                continue;
            }
            if (string2.equals("transition") && transitionManager != null) {
                this.loadTransition((AttributeSet)object, xmlPullParser, viewGroup, transitionManager);
                continue;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unknown scene name: ");
            ((StringBuilder)object).append(xmlPullParser.getName());
            throw new RuntimeException(((StringBuilder)object).toString());
        }
        return transitionManager;
    }

    public static TransitionInflater from(Context context) {
        return new TransitionInflater(context);
    }

    /*
     * Loose catch block
     */
    private void getTargetIds(XmlPullParser xmlPullParser, AttributeSet object, Transition transition) throws XmlPullParserException, IOException {
        int n;
        int n2 = xmlPullParser.getDepth();
        while (((n = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n2) && n != 1) {
            if (n != 2) continue;
            if (xmlPullParser.getName().equals("target")) {
                String string2;
                TypedArray typedArray;
                block15: {
                    typedArray = this.mContext.obtainStyledAttributes((AttributeSet)object, Styleable.TRANSITION_TARGET);
                    n = TypedArrayUtils.getNamedResourceId(typedArray, xmlPullParser, "targetId", 1, 0);
                    if (n != 0) {
                        transition.addTarget(n);
                    } else {
                        n = TypedArrayUtils.getNamedResourceId(typedArray, xmlPullParser, "excludeId", 2, 0);
                        if (n != 0) {
                            transition.excludeTarget(n, true);
                        } else {
                            string2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "targetName", 4);
                            if (string2 != null) {
                                transition.addTarget(string2);
                            } else {
                                string2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "excludeName", 5);
                                if (string2 != null) {
                                    transition.excludeTarget(string2, true);
                                } else {
                                    String string3;
                                    String string4 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "excludeClass", 3);
                                    if (string4 != null) {
                                        string2 = string4;
                                        transition.excludeTarget(Class.forName(string4), true);
                                    }
                                    string2 = string4;
                                    string4 = string3 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "targetClass", 0);
                                    if (string3 == null) break block15;
                                    string2 = string4;
                                    transition.addTarget(Class.forName(string4));
                                }
                            }
                        }
                    }
                }
                typedArray.recycle();
                continue;
                catch (ClassNotFoundException classNotFoundException) {
                    typedArray.recycle();
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Could not create ");
                    ((StringBuilder)object).append(string2);
                    throw new RuntimeException(((StringBuilder)object).toString(), classNotFoundException);
                }
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unknown scene name: ");
            ((StringBuilder)object).append(xmlPullParser.getName());
            throw new RuntimeException(((StringBuilder)object).toString());
        }
    }

    private void loadTransition(AttributeSet object, XmlPullParser object2, ViewGroup object3, TransitionManager transitionManager) throws Resources.NotFoundException {
        TypedArray typedArray = this.mContext.obtainStyledAttributes((AttributeSet)object, Styleable.TRANSITION_MANAGER);
        int n = TypedArrayUtils.getNamedResourceId(typedArray, object2, "transition", 2, -1);
        int n2 = TypedArrayUtils.getNamedResourceId(typedArray, object2, "fromScene", 0, -1);
        Object var7_8 = null;
        object = n2 < 0 ? null : Scene.getSceneForLayout(object3, n2, this.mContext);
        n2 = TypedArrayUtils.getNamedResourceId(typedArray, object2, "toScene", 1, -1);
        object2 = n2 < 0 ? var7_8 : Scene.getSceneForLayout(object3, n2, this.mContext);
        if (n >= 0 && (object3 = this.inflateTransition(n)) != null) {
            if (object2 != null) {
                if (object == null) {
                    transitionManager.setTransition((Scene)object2, (Transition)object3);
                } else {
                    transitionManager.setTransition((Scene)object, (Scene)object2, (Transition)object3);
                }
            } else {
                object = new StringBuilder();
                ((StringBuilder)object).append("No toScene for transition ID ");
                ((StringBuilder)object).append(n);
                throw new RuntimeException(((StringBuilder)object).toString());
            }
        }
        typedArray.recycle();
    }

    /*
     * Exception decompiling
     */
    public Transition inflateTransition(int var1_1) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 1[TRYBLOCK] [3 : 37->96)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
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
     * Exception decompiling
     */
    public TransitionManager inflateTransitionManager(int var1_1, ViewGroup var2_2) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 1[TRYBLOCK] [3 : 37->101)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
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
}

