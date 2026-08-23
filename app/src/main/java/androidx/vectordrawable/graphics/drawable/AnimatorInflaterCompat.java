/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.AnimatorInflater
 *  android.animation.AnimatorSet
 *  android.animation.Keyframe
 *  android.animation.ObjectAnimator
 *  android.animation.PropertyValuesHolder
 *  android.animation.TimeInterpolator
 *  android.animation.TypeEvaluator
 *  android.animation.ValueAnimator
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.content.res.TypedArray
 *  android.graphics.Path
 *  android.graphics.PathMeasure
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.TypedValue
 *  android.util.Xml
 *  android.view.InflateException
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.vectordrawable.graphics.drawable;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.InflateException;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.graphics.PathParser;
import androidx.vectordrawable.graphics.drawable.AndroidResources;
import androidx.vectordrawable.graphics.drawable.AnimationUtilsCompat;
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class AnimatorInflaterCompat {
    private static final boolean DBG_ANIMATOR_INFLATER = false;
    private static final int MAX_NUM_POINTS = 100;
    private static final String TAG = "AnimatorInflater";
    private static final int TOGETHER = 0;
    private static final int VALUE_TYPE_COLOR = 3;
    private static final int VALUE_TYPE_FLOAT = 0;
    private static final int VALUE_TYPE_INT = 1;
    private static final int VALUE_TYPE_PATH = 2;
    private static final int VALUE_TYPE_UNDEFINED = 4;

    private AnimatorInflaterCompat() {
    }

    private static Animator createAnimatorFromXml(Context context, Resources resources, Resources.Theme theme, XmlPullParser xmlPullParser, float f) throws XmlPullParserException, IOException {
        return AnimatorInflaterCompat.createAnimatorFromXml(context, resources, theme, xmlPullParser, Xml.asAttributeSet((XmlPullParser)xmlPullParser), null, 0, f);
    }

    private static Animator createAnimatorFromXml(Context object, Resources object2, Resources.Theme theme, XmlPullParser xmlPullParser, AttributeSet attributeSet, AnimatorSet animatorSet, int n, float f) throws XmlPullParserException, IOException {
        int n2;
        int n3 = xmlPullParser.getDepth();
        ObjectAnimator objectAnimator = null;
        Object object3 = null;
        while ((n2 = xmlPullParser.next()) != 3 || xmlPullParser.getDepth() > n3) {
            block16: {
                Object object4;
                block13: {
                    block15: {
                        block14: {
                            block12: {
                                if (n2 == 1) break;
                                if (n2 != 2) continue;
                                object4 = xmlPullParser.getName();
                                n2 = 0;
                                if (!object4.equals("objectAnimator")) break block12;
                                objectAnimator = AnimatorInflaterCompat.loadObjectAnimator((Context)object, (Resources)object2, theme, attributeSet, f, xmlPullParser);
                                break block13;
                            }
                            if (!object4.equals("animator")) break block14;
                            objectAnimator = AnimatorInflaterCompat.loadAnimator((Context)object, (Resources)object2, theme, attributeSet, null, f, xmlPullParser);
                            break block13;
                        }
                        if (!object4.equals("set")) break block15;
                        objectAnimator = new AnimatorSet();
                        object4 = TypedArrayUtils.obtainAttributes((Resources)object2, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATOR_SET);
                        int n4 = TypedArrayUtils.getNamedInt((TypedArray)object4, xmlPullParser, "ordering", 0, 0);
                        AnimatorInflaterCompat.createAnimatorFromXml((Context)object, (Resources)object2, theme, xmlPullParser, attributeSet, (AnimatorSet)objectAnimator, n4, f);
                        object4.recycle();
                        break block13;
                    }
                    if (!object4.equals("propertyValuesHolder")) break block16;
                    object4 = AnimatorInflaterCompat.loadValues((Context)object, (Resources)object2, theme, xmlPullParser, Xml.asAttributeSet((XmlPullParser)xmlPullParser));
                    if (object4 != null && objectAnimator instanceof ValueAnimator) {
                        ((ValueAnimator)objectAnimator).setValues(object4);
                    }
                    n2 = 1;
                }
                object4 = object3;
                if (animatorSet != null) {
                    object4 = object3;
                    if (n2 == 0) {
                        object4 = object3;
                        if (object3 == null) {
                            object4 = new ArrayList();
                        }
                        object4.add(objectAnimator);
                    }
                }
                object3 = object4;
                continue;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unknown animator name: ");
            ((StringBuilder)object).append(xmlPullParser.getName());
            throw new RuntimeException(((StringBuilder)object).toString());
        }
        if (animatorSet != null && object3 != null) {
            object = new Animator[((ArrayList)object3).size()];
            n2 = 0;
            object2 = ((ArrayList)object3).iterator();
            while (object2.hasNext()) {
                object[n2] = (Animator)object2.next();
                ++n2;
            }
            if (n == 0) {
                animatorSet.playTogether((Animator[])object);
            } else {
                animatorSet.playSequentially((Animator[])object);
            }
        }
        return objectAnimator;
    }

    private static Keyframe createNewKeyframe(Keyframe keyframe, float f) {
        keyframe = keyframe.getType() == Float.TYPE ? Keyframe.ofFloat((float)f) : (keyframe.getType() == Integer.TYPE ? Keyframe.ofInt((float)f) : Keyframe.ofObject((float)f));
        return keyframe;
    }

    private static void distributeKeyframes(Keyframe[] keyframeArray, float f, int n, int n2) {
        f /= (float)(n2 - n + 2);
        while (n <= n2) {
            keyframeArray[n].setFraction(keyframeArray[n - 1].getFraction() + f);
            ++n;
        }
    }

    private static void dumpKeyframes(Object[] objectArray, String object) {
        if (objectArray != null && objectArray.length != 0) {
            Log.d((String)TAG, (String)object);
            int n = objectArray.length;
            for (int i = 0; i < n; ++i) {
                Keyframe keyframe = (Keyframe)objectArray[i];
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Keyframe ");
                stringBuilder.append(i);
                stringBuilder.append(": fraction ");
                float f = keyframe.getFraction();
                String string2 = "null";
                object = f < 0.0f ? "null" : Float.valueOf(keyframe.getFraction());
                stringBuilder.append(object);
                stringBuilder.append(", , value : ");
                object = string2;
                if (keyframe.hasValue()) {
                    object = keyframe.getValue();
                }
                stringBuilder.append(object);
                Log.d((String)TAG, (String)stringBuilder.toString());
            }
            return;
        }
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    private static PropertyValuesHolder getPVH(TypedArray var0, int var1_1, int var2_2, int var3_3, String var4_4) {
        block23: {
            block20: {
                block22: {
                    block21: {
                        var12_5 /* !! */  = var0 /* !! */ .peekValue(var2_2);
                        var8_6 = var12_5 /* !! */  != null;
                        var10_7 = var8_6 != false ? var12_5 /* !! */ .type : 0;
                        var12_5 /* !! */  = var0 /* !! */ .peekValue(var3_3);
                        var7_8 = var12_5 /* !! */  != null;
                        var9_9 = var7_8 != false ? var12_5 /* !! */ .type : 0;
                        if (var1_1 == 4) {
                            var1_1 = var8_6 != false && AnimatorInflaterCompat.isColorType(var10_7) != false || var7_8 != false && AnimatorInflaterCompat.isColorType(var9_9) != false ? 3 : 0;
                        }
                        var11_10 = var1_1 == 0;
                        if (var1_1 != 2) break block20;
                        var12_5 /* !! */  = var0 /* !! */ .getString(var2_2);
                        var0 /* !! */  = var0 /* !! */ .getString(var3_3);
                        var14_11 = PathParser.createNodesFromPathData((String)var12_5 /* !! */ );
                        var13_12 = PathParser.createNodesFromPathData((String)var0 /* !! */ );
                        if (var14_11 == null && var13_12 == null) ** GOTO lbl-1000
                        if (var14_11 == null) break block21;
                        var15_13 = new PathDataEvaluator();
                        if (var13_12 == null) ** GOTO lbl32
                        if (PathParser.canMorph(var14_11, var13_12)) {
                            var0 /* !! */  = PropertyValuesHolder.ofObject((String)var4_4, (TypeEvaluator)var15_13, (Object[])new Object[]{var14_11, var13_12});
                        } else {
                            var4_4 = new StringBuilder();
                            var4_4.append(" Can't morph from ");
                            var4_4.append((String)var12_5 /* !! */ );
                            var4_4.append(" to ");
                            var4_4.append((String)var0 /* !! */ );
                            throw new InflateException(var4_4.toString());
lbl32:
                            // 1 sources

                            var0 /* !! */  = PropertyValuesHolder.ofObject((String)var4_4, (TypeEvaluator)var15_13, (Object[])new Object[]{var14_11});
                        }
                        break block22;
                    }
                    if (var13_12 != null) {
                        var0 /* !! */  = PropertyValuesHolder.ofObject((String)var4_4, (TypeEvaluator)new PathDataEvaluator(), (Object[])new Object[]{var13_12});
                    } else lbl-1000:
                    // 2 sources

                    {
                        var0 /* !! */  = null;
                    }
                }
                var4_4 = var0 /* !! */ ;
                break block23;
            }
            var12_5 /* !! */  = null;
            if (var1_1 == 3) {
                var12_5 /* !! */  = ArgbEvaluator.getInstance();
            }
            if (var11_10) {
                if (var8_6) {
                    var5_14 = var10_7 == 5 ? var0 /* !! */ .getDimension(var2_2, 0.0f) : var0 /* !! */ .getFloat(var2_2, 0.0f);
                    if (var7_8) {
                        var6_16 = var9_9 == 5 ? var0 /* !! */ .getDimension(var3_3, 0.0f) : var0 /* !! */ .getFloat(var3_3, 0.0f);
                        var0 /* !! */  = PropertyValuesHolder.ofFloat((String)var4_4, (float[])new float[]{var5_14, var6_16});
                    } else {
                        var0 /* !! */  = PropertyValuesHolder.ofFloat((String)var4_4, (float[])new float[]{var5_14});
                    }
                } else {
                    var5_15 = var9_9 == 5 ? var0 /* !! */ .getDimension(var3_3, 0.0f) : var0 /* !! */ .getFloat(var3_3, 0.0f);
                    var0 /* !! */  = PropertyValuesHolder.ofFloat((String)var4_4, (float[])new float[]{var5_15});
                }
            } else if (var8_6) {
                var1_1 = var10_7 == 5 ? (int)var0 /* !! */ .getDimension(var2_2, 0.0f) : (AnimatorInflaterCompat.isColorType(var10_7) != false ? var0 /* !! */ .getColor(var2_2, 0) : var0 /* !! */ .getInt(var2_2, 0));
                if (var7_8) {
                    var2_2 = var9_9 == 5 ? (int)var0 /* !! */ .getDimension(var3_3, 0.0f) : (AnimatorInflaterCompat.isColorType(var9_9) != false ? var0 /* !! */ .getColor(var3_3, 0) : var0 /* !! */ .getInt(var3_3, 0));
                    var0 /* !! */  = PropertyValuesHolder.ofInt((String)var4_4, (int[])new int[]{var1_1, var2_2});
                } else {
                    var0 /* !! */  = PropertyValuesHolder.ofInt((String)var4_4, (int[])new int[]{var1_1});
                }
            } else if (var7_8) {
                var1_1 = var9_9 == 5 ? (int)var0 /* !! */ .getDimension(var3_3, 0.0f) : (AnimatorInflaterCompat.isColorType(var9_9) != false ? var0 /* !! */ .getColor(var3_3, 0) : var0 /* !! */ .getInt(var3_3, 0));
                var0 /* !! */  = PropertyValuesHolder.ofInt((String)var4_4, (int[])new int[]{var1_1});
            } else {
                var0 /* !! */  = null;
            }
            var4_4 = var0 /* !! */ ;
            if (var0 /* !! */  != null) {
                var4_4 = var0 /* !! */ ;
                if (var12_5 /* !! */  != null) {
                    var0 /* !! */ .setEvaluator((TypeEvaluator)var12_5 /* !! */ );
                    var4_4 = var0 /* !! */ ;
                }
            }
        }
        return var4_4;
    }

    private static int inferValueTypeFromValues(TypedArray typedArray, int n, int n2) {
        TypedValue typedValue = typedArray.peekValue(n);
        int n3 = 1;
        int n4 = 0;
        n = typedValue != null ? 1 : 0;
        int n5 = n != 0 ? typedValue.type : 0;
        if ((n2 = (typedArray = typedArray.peekValue(n2)) != null ? n3 : 0) != 0) {
            n4 = typedArray.type;
        }
        n = n != 0 && AnimatorInflaterCompat.isColorType(n5) || n2 != 0 && AnimatorInflaterCompat.isColorType(n4) ? 3 : 0;
        return n;
    }

    private static int inferValueTypeOfKeyframe(Resources resources, Resources.Theme theme, AttributeSet attributeSet, XmlPullParser xmlPullParser) {
        resources = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_KEYFRAME);
        int n = 0;
        theme = TypedArrayUtils.peekNamedValue((TypedArray)resources, xmlPullParser, "value", 0);
        if (theme != null) {
            n = 1;
        }
        n = n != 0 && AnimatorInflaterCompat.isColorType(theme.type) ? 3 : 0;
        resources.recycle();
        return n;
    }

    private static boolean isColorType(int n) {
        boolean bl = n >= 28 && n <= 31;
        return bl;
    }

    public static Animator loadAnimator(Context context, int n) throws Resources.NotFoundException {
        context = Build.VERSION.SDK_INT >= 24 ? AnimatorInflater.loadAnimator((Context)context, (int)n) : AnimatorInflaterCompat.loadAnimator(context, context.getResources(), context.getTheme(), n);
        return context;
    }

    public static Animator loadAnimator(Context context, Resources resources, Resources.Theme theme, int n) throws Resources.NotFoundException {
        return AnimatorInflaterCompat.loadAnimator(context, resources, theme, n, 1.0f);
    }

    /*
     * Exception decompiling
     */
    public static Animator loadAnimator(Context var0, Resources var1_3, Resources.Theme var2_5, int var3_6, float var4_7) throws Resources.NotFoundException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 2[TRYBLOCK] [6 : 62->66)] java.lang.Throwable
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

    private static ValueAnimator loadAnimator(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, ValueAnimator valueAnimator, float f, XmlPullParser xmlPullParser) throws Resources.NotFoundException {
        TypedArray typedArray = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_ANIMATOR);
        theme = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_PROPERTY_ANIMATOR);
        resources = valueAnimator;
        if (valueAnimator == null) {
            resources = new ValueAnimator();
        }
        AnimatorInflaterCompat.parseAnimatorFromTypeArray((ValueAnimator)resources, typedArray, (TypedArray)theme, f, xmlPullParser);
        int n = TypedArrayUtils.getNamedResourceId(typedArray, xmlPullParser, "interpolator", 0, 0);
        if (n > 0) {
            resources.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, n));
        }
        typedArray.recycle();
        if (theme != null) {
            theme.recycle();
        }
        return resources;
    }

    private static Keyframe loadKeyframe(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, int n, XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        theme = TypedArrayUtils.obtainAttributes(resources, theme, attributeSet, AndroidResources.STYLEABLE_KEYFRAME);
        resources = null;
        float f = TypedArrayUtils.getNamedFloat((TypedArray)theme, xmlPullParser, "fraction", 3, -1.0f);
        attributeSet = TypedArrayUtils.peekNamedValue((TypedArray)theme, xmlPullParser, "value", 0);
        boolean bl = attributeSet != null;
        int n2 = n;
        if (n == 4) {
            n2 = bl && AnimatorInflaterCompat.isColorType(attributeSet.type) ? 3 : 0;
        }
        if (bl) {
            switch (n2) {
                default: {
                    break;
                }
                case 1: 
                case 3: {
                    resources = Keyframe.ofInt((float)f, (int)TypedArrayUtils.getNamedInt((TypedArray)theme, xmlPullParser, "value", 0, 0));
                    break;
                }
                case 0: {
                    resources = Keyframe.ofFloat((float)f, (float)TypedArrayUtils.getNamedFloat((TypedArray)theme, xmlPullParser, "value", 0, 0.0f));
                    break;
                }
            }
        } else {
            resources = n2 == 0 ? Keyframe.ofFloat((float)f) : Keyframe.ofInt((float)f);
        }
        n = TypedArrayUtils.getNamedResourceId((TypedArray)theme, xmlPullParser, "interpolator", 1, 0);
        if (n > 0) {
            resources.setInterpolator((TimeInterpolator)AnimationUtilsCompat.loadInterpolator(context, n));
        }
        theme.recycle();
        return resources;
    }

    private static ObjectAnimator loadObjectAnimator(Context context, Resources resources, Resources.Theme theme, AttributeSet attributeSet, float f, XmlPullParser xmlPullParser) throws Resources.NotFoundException {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        AnimatorInflaterCompat.loadAnimator(context, resources, theme, attributeSet, (ValueAnimator)objectAnimator, f, xmlPullParser);
        return objectAnimator;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static PropertyValuesHolder loadPvh(Context keyframe, Resources keyframe2, Resources.Theme theme, XmlPullParser xmlPullParser, String string2, int n) throws XmlPullParserException, IOException {
        int n2;
        boolean bl = false;
        ArrayList<Keyframe> arrayList = null;
        int n3 = n;
        while (true) {
            n2 = n = xmlPullParser.next();
            if (n == 3 || n2 == 1) break;
            if (!xmlPullParser.getName().equals("keyframe")) continue;
            if (n3 == 4) {
                n3 = AnimatorInflaterCompat.inferValueTypeOfKeyframe((Resources)keyframe2, theme, Xml.asAttributeSet((XmlPullParser)xmlPullParser), xmlPullParser);
            }
            Keyframe keyframe3 = AnimatorInflaterCompat.loadKeyframe((Context)keyframe, (Resources)keyframe2, theme, Xml.asAttributeSet((XmlPullParser)xmlPullParser), n3, xmlPullParser);
            ArrayList<Keyframe> arrayList2 = arrayList;
            if (keyframe3 != null) {
                arrayList2 = arrayList;
                if (arrayList == null) {
                    arrayList2 = new ArrayList<Keyframe>();
                }
                arrayList2.add(keyframe3);
            }
            xmlPullParser.next();
            arrayList = arrayList2;
        }
        if (arrayList == null) return null;
        int n4 = n = arrayList.size();
        if (n <= 0) return null;
        keyframe2 = (Keyframe)arrayList.get(0);
        keyframe = (Keyframe)arrayList.get(n4 - 1);
        float f = keyframe.getFraction();
        n = n4;
        if (f < 1.0f) {
            if (f < 0.0f) {
                keyframe.setFraction(1.0f);
                n = n4;
            } else {
                arrayList.add(arrayList.size(), AnimatorInflaterCompat.createNewKeyframe(keyframe, 1.0f));
                n = n4 + 1;
            }
        }
        f = keyframe2.getFraction();
        int n5 = n;
        if (f != 0.0f) {
            if (f < 0.0f) {
                keyframe2.setFraction(0.0f);
                n5 = n;
            } else {
                arrayList.add(0, AnimatorInflaterCompat.createNewKeyframe(keyframe2, 0.0f));
                n5 = n + 1;
            }
        }
        keyframe = new Keyframe[n5];
        arrayList.toArray((T[])keyframe);
        n = n2;
        for (n4 = 0; n4 < n5; ++n4) {
            keyframe2 = keyframe[n4];
            if (!(keyframe2.getFraction() < 0.0f)) continue;
            if (n4 == 0) {
                keyframe2.setFraction(0.0f);
                continue;
            }
            if (n4 == n5 - 1) {
                keyframe2.setFraction(1.0f);
                continue;
            }
            int n6 = n4 + 1;
            int n7 = n4;
            n2 = n;
            n = n6;
            while (n < n5 - 1 && !(keyframe[n].getFraction() >= 0.0f)) {
                n7 = n++;
            }
            AnimatorInflaterCompat.distributeKeyframes((Keyframe[])keyframe, keyframe[n7 + 1].getFraction() - keyframe[n4 - 1].getFraction(), n4, n7);
            n = n2;
        }
        keyframe = keyframe2 = PropertyValuesHolder.ofKeyframe((String)string2, (Keyframe[])keyframe);
        if (n3 != 3) return keyframe;
        keyframe2.setEvaluator((TypeEvaluator)ArgbEvaluator.getInstance());
        return keyframe2;
    }

    private static PropertyValuesHolder[] loadValues(Context propertyValuesHolderArray, Resources propertyValuesHolderArray2, Resources.Theme theme, XmlPullParser xmlPullParser, AttributeSet attributeSet) throws XmlPullParserException, IOException {
        int n;
        Object object = null;
        while ((n = xmlPullParser.getEventType()) != 3 && n != 1) {
            if (n != 2) {
                xmlPullParser.next();
                continue;
            }
            if (xmlPullParser.getName().equals("propertyValuesHolder")) {
                TypedArray typedArray = TypedArrayUtils.obtainAttributes((Resources)propertyValuesHolderArray2, theme, attributeSet, AndroidResources.STYLEABLE_PROPERTY_VALUES_HOLDER);
                Object object2 = TypedArrayUtils.getNamedString(typedArray, xmlPullParser, "propertyName", 3);
                PropertyValuesHolder propertyValuesHolder = AnimatorInflaterCompat.loadPvh((Context)propertyValuesHolderArray, (Resources)propertyValuesHolderArray2, theme, xmlPullParser, (String)object2, n = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 2, 4));
                if (propertyValuesHolder == null) {
                    propertyValuesHolder = AnimatorInflaterCompat.getPVH(typedArray, n, 0, 1, (String)object2);
                }
                object2 = object;
                if (propertyValuesHolder != null) {
                    object2 = object;
                    if (object == null) {
                        object2 = new ArrayList<PropertyValuesHolder>();
                    }
                    ((ArrayList)object2).add(propertyValuesHolder);
                }
                typedArray.recycle();
                object = object2;
            }
            xmlPullParser.next();
        }
        propertyValuesHolderArray = null;
        if (object != null) {
            int n2 = ((ArrayList)object).size();
            propertyValuesHolderArray2 = new PropertyValuesHolder[n2];
            n = 0;
            while (true) {
                propertyValuesHolderArray = propertyValuesHolderArray2;
                if (n >= n2) break;
                propertyValuesHolderArray2[n] = (PropertyValuesHolder)((ArrayList)object).get(n);
                ++n;
            }
        }
        return propertyValuesHolderArray;
    }

    private static void parseAnimatorFromTypeArray(ValueAnimator valueAnimator, TypedArray typedArray, TypedArray typedArray2, float f, XmlPullParser xmlPullParser) {
        int n;
        long l = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "duration", 1, 300);
        long l2 = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "startOffset", 2, 0);
        int n2 = n = TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "valueType", 7, 4);
        if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueFrom")) {
            n2 = n;
            if (TypedArrayUtils.hasAttribute(xmlPullParser, "valueTo")) {
                int n3 = n;
                if (n == 4) {
                    n3 = AnimatorInflaterCompat.inferValueTypeFromValues(typedArray, 5, 6);
                }
                PropertyValuesHolder propertyValuesHolder = AnimatorInflaterCompat.getPVH(typedArray, n3, 5, 6, "");
                n2 = n3;
                if (propertyValuesHolder != null) {
                    valueAnimator.setValues(new PropertyValuesHolder[]{propertyValuesHolder});
                    n2 = n3;
                }
            }
        }
        valueAnimator.setDuration(l);
        valueAnimator.setStartDelay(l2);
        valueAnimator.setRepeatCount(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatCount", 3, 0));
        valueAnimator.setRepeatMode(TypedArrayUtils.getNamedInt(typedArray, xmlPullParser, "repeatMode", 4, 1));
        if (typedArray2 != null) {
            AnimatorInflaterCompat.setupObjectAnimator(valueAnimator, typedArray2, n2, f, xmlPullParser);
        }
    }

    private static void setupObjectAnimator(ValueAnimator object, TypedArray typedArray, int n, float f, XmlPullParser object2) {
        ObjectAnimator objectAnimator = (ObjectAnimator)object;
        String string2 = TypedArrayUtils.getNamedString(typedArray, object2, "pathData", 1);
        if (string2 != null) {
            object = TypedArrayUtils.getNamedString(typedArray, object2, "propertyXName", 2);
            object2 = TypedArrayUtils.getNamedString(typedArray, object2, "propertyYName", 3);
            if (n == 2 || n == 4) {
                // empty if block
            }
            if (object == null && object2 == null) {
                object = new StringBuilder();
                ((StringBuilder)object).append(typedArray.getPositionDescription());
                ((StringBuilder)object).append(" propertyXName or propertyYName is needed for PathData");
                throw new InflateException(((StringBuilder)object).toString());
            }
            AnimatorInflaterCompat.setupPathMotion(PathParser.createPathFromPathData(string2), objectAnimator, 0.5f * f, (String)object, (String)object2);
        } else {
            objectAnimator.setPropertyName(TypedArrayUtils.getNamedString(typedArray, object2, "propertyName", 0));
        }
    }

    private static void setupPathMotion(Path object, ObjectAnimator objectAnimator, float f, String object2, String string2) {
        Object object3 = new PathMeasure(object, false);
        float f2 = 0.0f;
        ArrayList<Float> arrayList = new ArrayList<Float>();
        arrayList.add(Float.valueOf(0.0f));
        do {
            arrayList.add(Float.valueOf(f2 += object3.getLength()));
        } while (object3.nextContour());
        PathMeasure pathMeasure = new PathMeasure(object, false);
        int n = Math.min(100, (int)(f2 / f) + 1);
        float[] fArray = new float[n];
        object3 = new float[n];
        object = new float[2];
        int n2 = 0;
        f2 /= (float)(n - 1);
        f = 0.0f;
        for (int i = 0; i < n; ++i) {
            pathMeasure.getPosTan(f - ((Float)arrayList.get(n2)).floatValue(), (float[])object, null);
            fArray[i] = (float)object[0];
            object3[i] = object[1];
            f += f2;
            int n3 = n2;
            if (n2 + 1 < arrayList.size()) {
                n3 = n2;
                if (f > ((Float)arrayList.get(n2 + 1)).floatValue()) {
                    n3 = n2 + 1;
                    pathMeasure.nextContour();
                }
            }
            n2 = n3;
        }
        object = null;
        arrayList = null;
        if (object2 != null) {
            object = PropertyValuesHolder.ofFloat((String)object2, (float[])fArray);
        }
        object2 = arrayList;
        if (string2 != null) {
            object2 = PropertyValuesHolder.ofFloat((String)string2, (float[])object3);
        }
        if (object == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{object2});
        } else if (object2 == null) {
            objectAnimator.setValues(new PropertyValuesHolder[]{object});
        } else {
            objectAnimator.setValues(new PropertyValuesHolder[]{object, object2});
        }
    }

    private static class PathDataEvaluator
    implements TypeEvaluator<PathParser.PathDataNode[]> {
        private PathParser.PathDataNode[] mNodeArray;

        PathDataEvaluator() {
        }

        PathDataEvaluator(PathParser.PathDataNode[] pathDataNodeArray) {
            this.mNodeArray = pathDataNodeArray;
        }

        public PathParser.PathDataNode[] evaluate(float f, PathParser.PathDataNode[] pathDataNodeArray, PathParser.PathDataNode[] pathDataNodeArray2) {
            if (PathParser.canMorph(pathDataNodeArray, pathDataNodeArray2)) {
                if (!PathParser.canMorph(this.mNodeArray, pathDataNodeArray)) {
                    this.mNodeArray = PathParser.deepCopyNodes(pathDataNodeArray);
                }
                for (int i = 0; i < pathDataNodeArray.length; ++i) {
                    this.mNodeArray[i].interpolatePathDataNode(pathDataNodeArray[i], pathDataNodeArray2[i], f);
                }
                return this.mNodeArray;
            }
            pathDataNodeArray = new IllegalArgumentException("Can't interpolate between two incompatible pathData");
            throw pathDataNodeArray;
        }
    }
}

