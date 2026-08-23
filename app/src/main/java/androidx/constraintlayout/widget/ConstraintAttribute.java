/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.Color
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.util.DisplayMetrics
 *  android.util.Log
 *  android.util.TypedValue
 *  android.util.Xml
 *  android.view.View
 *  org.xmlpull.v1.XmlPullParser
 */
package androidx.constraintlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.util.Xml;
import android.view.View;
import androidx.constraintlayout.widget.R;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;

public class ConstraintAttribute {
    private static final String TAG = "TransitionLayout";
    boolean mBooleanValue;
    private int mColorValue;
    private float mFloatValue;
    private int mIntegerValue;
    String mName;
    private String mStringValue;
    private AttributeType mType;

    public ConstraintAttribute(ConstraintAttribute constraintAttribute, Object object) {
        this.mName = constraintAttribute.mName;
        this.mType = constraintAttribute.mType;
        this.setValue(object);
    }

    public ConstraintAttribute(String string2, AttributeType attributeType) {
        this.mName = string2;
        this.mType = attributeType;
    }

    public ConstraintAttribute(String string2, AttributeType attributeType, Object object) {
        this.mName = string2;
        this.mType = attributeType;
        this.setValue(object);
    }

    private static int clamp(int n) {
        n = (n & ~(n >> 31)) - 255;
        return (n & n >> 31) + 255;
    }

    public static HashMap<String, ConstraintAttribute> extractAttributes(HashMap<String, ConstraintAttribute> hashMap, View view) {
        HashMap<String, ConstraintAttribute> hashMap2 = new HashMap<String, ConstraintAttribute>();
        Class<?> clazz = view.getClass();
        for (String string2 : hashMap.keySet()) {
            ConstraintAttribute constraintAttribute = hashMap.get(string2);
            try {
                Object object;
                if (string2.equals("BackgroundColor")) {
                    int n = ((ColorDrawable)view.getBackground()).getColor();
                    object = new ConstraintAttribute(constraintAttribute, n);
                    hashMap2.put(string2, (ConstraintAttribute)object);
                    continue;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("getMap");
                ((StringBuilder)object).append(string2);
                object = clazz.getMethod(((StringBuilder)object).toString(), new Class[0]).invoke((Object)view, new Object[0]);
                ConstraintAttribute constraintAttribute2 = new ConstraintAttribute(constraintAttribute, object);
                hashMap2.put(string2, constraintAttribute2);
            }
            catch (InvocationTargetException invocationTargetException) {
                invocationTargetException.printStackTrace();
            }
            catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
            catch (NoSuchMethodException noSuchMethodException) {
                noSuchMethodException.printStackTrace();
            }
        }
        return hashMap2;
    }

    public static void parse(Context context, XmlPullParser object, HashMap<String, ConstraintAttribute> hashMap) {
        TypedArray typedArray = context.obtainStyledAttributes(Xml.asAttributeSet((XmlPullParser)object), R.styleable.CustomAttribute);
        String string2 = null;
        Object object2 = null;
        AttributeType attributeType = null;
        int n = typedArray.getIndexCount();
        for (int i = 0; i < n; ++i) {
            AttributeType attributeType2;
            String string3;
            int n2 = typedArray.getIndex(i);
            if (n2 == R.styleable.CustomAttribute_attributeName) {
                string3 = string2 = typedArray.getString(n2);
                object = object2;
                attributeType2 = attributeType;
                if (string2 != null) {
                    string3 = string2;
                    object = object2;
                    attributeType2 = attributeType;
                    if (string2.length() > 0) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append(Character.toUpperCase(string2.charAt(0)));
                        ((StringBuilder)object).append(string2.substring(1));
                        string3 = ((StringBuilder)object).toString();
                        object = object2;
                        attributeType2 = attributeType;
                    }
                }
            } else if (n2 == R.styleable.CustomAttribute_customBoolean) {
                object = typedArray.getBoolean(n2, false);
                attributeType2 = AttributeType.BOOLEAN_TYPE;
                string3 = string2;
            } else if (n2 == R.styleable.CustomAttribute_customColorValue) {
                attributeType2 = AttributeType.COLOR_TYPE;
                object = typedArray.getColor(n2, 0);
                string3 = string2;
            } else if (n2 == R.styleable.CustomAttribute_customColorDrawableValue) {
                attributeType2 = AttributeType.COLOR_DRAWABLE_TYPE;
                object = typedArray.getColor(n2, 0);
                string3 = string2;
            } else if (n2 == R.styleable.CustomAttribute_customPixelDimension) {
                attributeType2 = AttributeType.DIMENSION_TYPE;
                object = Float.valueOf(TypedValue.applyDimension((int)1, (float)typedArray.getDimension(n2, 0.0f), (DisplayMetrics)context.getResources().getDisplayMetrics()));
                string3 = string2;
            } else if (n2 == R.styleable.CustomAttribute_customDimension) {
                attributeType2 = AttributeType.DIMENSION_TYPE;
                object = Float.valueOf(typedArray.getDimension(n2, 0.0f));
                string3 = string2;
            } else if (n2 == R.styleable.CustomAttribute_customFloatValue) {
                attributeType2 = AttributeType.FLOAT_TYPE;
                object = Float.valueOf(typedArray.getFloat(n2, Float.NaN));
                string3 = string2;
            } else if (n2 == R.styleable.CustomAttribute_customIntegerValue) {
                attributeType2 = AttributeType.INT_TYPE;
                object = typedArray.getInteger(n2, -1);
                string3 = string2;
            } else {
                string3 = string2;
                object = object2;
                attributeType2 = attributeType;
                if (n2 == R.styleable.CustomAttribute_customStringValue) {
                    attributeType2 = AttributeType.STRING_TYPE;
                    object = typedArray.getString(n2);
                    string3 = string2;
                }
            }
            string2 = string3;
            object2 = object;
            attributeType = attributeType2;
        }
        if (string2 != null && object2 != null) {
            hashMap.put(string2, new ConstraintAttribute(string2, attributeType, object2));
        }
        typedArray.recycle();
    }

    public static void setAttributes(View view, HashMap<String, ConstraintAttribute> hashMap) {
        Class<?> clazz = view.getClass();
        block13: for (String string2 : hashMap.keySet()) {
            Object noSuchMethodException = hashMap.get(string2);
            CharSequence charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("set");
            ((StringBuilder)charSequence).append(string2);
            charSequence = ((StringBuilder)charSequence).toString();
            try {
                switch (1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[((ConstraintAttribute)noSuchMethodException).mType.ordinal()]) {
                    default: {
                        continue block13;
                    }
                    case 7: {
                        clazz.getMethod((String)charSequence, Float.TYPE).invoke((Object)view, Float.valueOf(((ConstraintAttribute)noSuchMethodException).mFloatValue));
                        continue block13;
                    }
                    case 6: {
                        clazz.getMethod((String)charSequence, Boolean.TYPE).invoke((Object)view, ((ConstraintAttribute)noSuchMethodException).mBooleanValue);
                        continue block13;
                    }
                    case 5: {
                        clazz.getMethod((String)charSequence, CharSequence.class).invoke((Object)view, ((ConstraintAttribute)noSuchMethodException).mStringValue);
                        continue block13;
                    }
                    case 4: {
                        clazz.getMethod((String)charSequence, Float.TYPE).invoke((Object)view, Float.valueOf(((ConstraintAttribute)noSuchMethodException).mFloatValue));
                        continue block13;
                    }
                    case 3: {
                        clazz.getMethod((String)charSequence, Integer.TYPE).invoke((Object)view, ((ConstraintAttribute)noSuchMethodException).mIntegerValue);
                        continue block13;
                    }
                    case 2: {
                        Method method = clazz.getMethod((String)charSequence, Drawable.class);
                        ColorDrawable colorDrawable = new ColorDrawable();
                        colorDrawable.setColor(((ConstraintAttribute)noSuchMethodException).mColorValue);
                        method.invoke((Object)view, colorDrawable);
                        continue block13;
                    }
                    case 1: 
                }
                clazz.getMethod((String)charSequence, Integer.TYPE).invoke((Object)view, ((ConstraintAttribute)noSuchMethodException).mColorValue);
            }
            catch (InvocationTargetException invocationTargetException) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append(" Custom Attribute \"");
                ((StringBuilder)charSequence).append(string2);
                ((StringBuilder)charSequence).append("\" not found on ");
                ((StringBuilder)charSequence).append(clazz.getName());
                Log.e((String)TAG, (String)((StringBuilder)charSequence).toString());
                invocationTargetException.printStackTrace();
            }
            catch (IllegalAccessException illegalAccessException) {
                noSuchMethodException = new StringBuilder();
                ((StringBuilder)noSuchMethodException).append(" Custom Attribute \"");
                ((StringBuilder)noSuchMethodException).append(string2);
                ((StringBuilder)noSuchMethodException).append("\" not found on ");
                ((StringBuilder)noSuchMethodException).append(clazz.getName());
                Log.e((String)TAG, (String)((StringBuilder)noSuchMethodException).toString());
                illegalAccessException.printStackTrace();
            }
            catch (NoSuchMethodException noSuchMethodException2) {
                Log.e((String)TAG, (String)noSuchMethodException2.getMessage());
                noSuchMethodException = new StringBuilder();
                ((StringBuilder)noSuchMethodException).append(" Custom Attribute \"");
                ((StringBuilder)noSuchMethodException).append(string2);
                ((StringBuilder)noSuchMethodException).append("\" not found on ");
                ((StringBuilder)noSuchMethodException).append(clazz.getName());
                Log.e((String)TAG, (String)((StringBuilder)noSuchMethodException).toString());
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(clazz.getName());
                stringBuilder.append(" must have a method ");
                stringBuilder.append((String)charSequence);
                Log.e((String)TAG, (String)stringBuilder.toString());
            }
        }
    }

    public boolean diff(ConstraintAttribute constraintAttribute) {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        boolean bl5 = false;
        boolean bl6 = false;
        if (constraintAttribute != null && this.mType == constraintAttribute.mType) {
            switch (1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
                default: {
                    return false;
                }
                case 7: {
                    if (this.mFloatValue == constraintAttribute.mFloatValue) {
                        bl6 = true;
                    }
                    return bl6;
                }
                case 6: {
                    bl6 = bl;
                    if (this.mBooleanValue == constraintAttribute.mBooleanValue) {
                        bl6 = true;
                    }
                    return bl6;
                }
                case 5: {
                    bl6 = bl2;
                    if (this.mIntegerValue == constraintAttribute.mIntegerValue) {
                        bl6 = true;
                    }
                    return bl6;
                }
                case 4: {
                    bl6 = bl3;
                    if (this.mFloatValue == constraintAttribute.mFloatValue) {
                        bl6 = true;
                    }
                    return bl6;
                }
                case 3: {
                    bl6 = bl4;
                    if (this.mIntegerValue == constraintAttribute.mIntegerValue) {
                        bl6 = true;
                    }
                    return bl6;
                }
                case 1: 
                case 2: 
            }
            bl6 = bl5;
            if (this.mColorValue == constraintAttribute.mColorValue) {
                bl6 = true;
            }
            return bl6;
        }
        return false;
    }

    public AttributeType getType() {
        return this.mType;
    }

    public float getValueToInterpolate() {
        switch (1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            default: {
                return Float.NaN;
            }
            case 7: {
                return this.mFloatValue;
            }
            case 6: {
                float f = this.mBooleanValue ? 1.0f : 0.0f;
                return f;
            }
            case 5: {
                throw new RuntimeException("Cannot interpolate String");
            }
            case 4: {
                return this.mFloatValue;
            }
            case 3: {
                return this.mIntegerValue;
            }
            case 1: 
            case 2: 
        }
        throw new RuntimeException("Color does not have a single color to interpolate");
    }

    public void getValuesToInterpolate(float[] fArray) {
        switch (1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            default: {
                break;
            }
            case 7: {
                fArray[0] = this.mFloatValue;
                break;
            }
            case 6: {
                float f = this.mBooleanValue ? 1.0f : 0.0f;
                fArray[0] = f;
                break;
            }
            case 5: {
                throw new RuntimeException("Color does not have a single color to interpolate");
            }
            case 4: {
                fArray[0] = this.mFloatValue;
                break;
            }
            case 3: {
                fArray[0] = this.mIntegerValue;
                break;
            }
            case 1: 
            case 2: {
                int n = this.mColorValue;
                float f = (float)Math.pow((float)(n >> 16 & 0xFF) / 255.0f, 2.2);
                float f2 = (float)Math.pow((float)(n >> 8 & 0xFF) / 255.0f, 2.2);
                float f3 = (float)Math.pow((float)(n & 0xFF) / 255.0f, 2.2);
                fArray[0] = f;
                fArray[1] = f2;
                fArray[2] = f3;
                fArray[3] = (float)(n >> 24 & 0xFF) / 255.0f;
            }
        }
    }

    public int noOfInterpValues() {
        switch (1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            default: {
                return 1;
            }
            case 1: 
            case 2: 
        }
        return 4;
    }

    public void setColorValue(int n) {
        this.mColorValue = n;
    }

    public void setFloatValue(float f) {
        this.mFloatValue = f;
    }

    public void setIntValue(int n) {
        this.mIntegerValue = n;
    }

    /*
     * Exception decompiling
     */
    public void setInterpolatedValue(View var1_1, float[] var2_3) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [5[CASE]], but top level block is 1[TRYBLOCK]
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

    public void setStringValue(String string2) {
        this.mStringValue = string2;
    }

    public void setValue(Object object) {
        switch (1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()]) {
            default: {
                break;
            }
            case 7: {
                this.mFloatValue = ((Float)object).floatValue();
                break;
            }
            case 6: {
                this.mBooleanValue = (Boolean)object;
                break;
            }
            case 5: {
                this.mStringValue = (String)object;
                break;
            }
            case 4: {
                this.mFloatValue = ((Float)object).floatValue();
                break;
            }
            case 3: {
                this.mIntegerValue = (Integer)object;
                break;
            }
            case 1: 
            case 2: {
                this.mColorValue = (Integer)object;
            }
        }
    }

    public void setValue(float[] fArray) {
        int n = 1.$SwitchMap$androidx$constraintlayout$widget$ConstraintAttribute$AttributeType[this.mType.ordinal()];
        boolean bl = false;
        switch (n) {
            default: {
                break;
            }
            case 7: {
                this.mFloatValue = fArray[0];
                break;
            }
            case 6: {
                if ((double)fArray[0] > 0.5) {
                    bl = true;
                }
                this.mBooleanValue = bl;
                break;
            }
            case 5: {
                throw new RuntimeException("Color does not have a single color to interpolate");
            }
            case 4: {
                this.mFloatValue = fArray[0];
                break;
            }
            case 3: {
                this.mIntegerValue = (int)fArray[0];
                break;
            }
            case 1: 
            case 2: {
                this.mColorValue = n = Color.HSVToColor((float[])fArray);
                this.mColorValue = n & 0xFFFFFF | ConstraintAttribute.clamp((int)(fArray[3] * 255.0f)) << 24;
            }
        }
    }

    public static final class AttributeType
    extends Enum<AttributeType> {
        private static final AttributeType[] $VALUES;
        public static final /* enum */ AttributeType BOOLEAN_TYPE;
        public static final /* enum */ AttributeType COLOR_DRAWABLE_TYPE;
        public static final /* enum */ AttributeType COLOR_TYPE;
        public static final /* enum */ AttributeType DIMENSION_TYPE;
        public static final /* enum */ AttributeType FLOAT_TYPE;
        public static final /* enum */ AttributeType INT_TYPE;
        public static final /* enum */ AttributeType STRING_TYPE;

        static {
            AttributeType attributeType;
            AttributeType attributeType2;
            AttributeType attributeType3;
            AttributeType attributeType4;
            AttributeType attributeType5;
            AttributeType attributeType6;
            AttributeType attributeType7;
            INT_TYPE = attributeType7 = new AttributeType();
            FLOAT_TYPE = attributeType6 = new AttributeType();
            COLOR_TYPE = attributeType5 = new AttributeType();
            COLOR_DRAWABLE_TYPE = attributeType4 = new AttributeType();
            STRING_TYPE = attributeType3 = new AttributeType();
            BOOLEAN_TYPE = attributeType2 = new AttributeType();
            DIMENSION_TYPE = attributeType = new AttributeType();
            $VALUES = new AttributeType[]{attributeType7, attributeType6, attributeType5, attributeType4, attributeType3, attributeType2, attributeType};
        }

        public static AttributeType valueOf(String string2) {
            return Enum.valueOf(AttributeType.class, string2);
        }

        public static AttributeType[] values() {
            return (AttributeType[])$VALUES.clone();
        }
    }
}

