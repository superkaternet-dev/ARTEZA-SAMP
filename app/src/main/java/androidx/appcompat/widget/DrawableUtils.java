/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Rect
 *  android.graphics.drawable.Drawable
 *  android.graphics.drawable.DrawableContainer
 *  android.graphics.drawable.DrawableContainer$DrawableContainerState
 *  android.graphics.drawable.GradientDrawable
 *  android.graphics.drawable.InsetDrawable
 *  android.graphics.drawable.LayerDrawable
 *  android.graphics.drawable.ScaleDrawable
 *  android.os.Build$VERSION
 */
package androidx.appcompat.widget;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Build;
import androidx.appcompat.graphics.drawable.DrawableWrapper;
import androidx.core.graphics.drawable.WrappedDrawable;

public class DrawableUtils {
    private static final int[] CHECKED_STATE_SET = new int[]{0x10100A0};
    private static final int[] EMPTY_STATE_SET = new int[0];
    public static final Rect INSETS_NONE = new Rect();
    private static final String TAG = "DrawableUtils";
    private static final String VECTOR_DRAWABLE_CLAZZ_NAME = "android.graphics.drawable.VectorDrawable";
    private static Class<?> sInsetsClazz;

    static {
        if (Build.VERSION.SDK_INT >= 18) {
            try {
                sInsetsClazz = Class.forName("android.graphics.Insets");
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
    }

    private DrawableUtils() {
    }

    public static boolean canSafelyMutateDrawable(Drawable drawableArray) {
        if (Build.VERSION.SDK_INT < 15 && drawableArray instanceof InsetDrawable) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 15 && drawableArray instanceof GradientDrawable) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 17 && drawableArray instanceof LayerDrawable) {
            return false;
        }
        if (drawableArray instanceof DrawableContainer) {
            if ((drawableArray = drawableArray.getConstantState()) instanceof DrawableContainer.DrawableContainerState) {
                drawableArray = ((DrawableContainer.DrawableContainerState)drawableArray).getChildren();
                int n = drawableArray.length;
                for (int i = 0; i < n; ++i) {
                    if (DrawableUtils.canSafelyMutateDrawable(drawableArray[i])) continue;
                    return false;
                }
            }
        } else {
            if (drawableArray instanceof WrappedDrawable) {
                return DrawableUtils.canSafelyMutateDrawable(((WrappedDrawable)drawableArray).getWrappedDrawable());
            }
            if (drawableArray instanceof DrawableWrapper) {
                return DrawableUtils.canSafelyMutateDrawable(((DrawableWrapper)drawableArray).getWrappedDrawable());
            }
            if (drawableArray instanceof ScaleDrawable) {
                return DrawableUtils.canSafelyMutateDrawable(((ScaleDrawable)drawableArray).getDrawable());
            }
        }
        return true;
    }

    static void fixDrawable(Drawable drawable2) {
        if (Build.VERSION.SDK_INT == 21 && VECTOR_DRAWABLE_CLAZZ_NAME.equals(drawable2.getClass().getName())) {
            DrawableUtils.fixVectorDrawableTinting(drawable2);
        }
    }

    private static void fixVectorDrawableTinting(Drawable drawable2) {
        int[] nArray = drawable2.getState();
        if (nArray != null && nArray.length != 0) {
            drawable2.setState(EMPTY_STATE_SET);
        } else {
            drawable2.setState(CHECKED_STATE_SET);
        }
        drawable2.setState(nArray);
    }

    /*
     * Exception decompiling
     */
    public static Rect getOpticalBounds(Drawable var0) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [3[TRYBLOCK]], but top level block is 8[SWITCH]
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

    public static PorterDuff.Mode parseTintMode(int n, PorterDuff.Mode mode) {
        switch (n) {
            default: {
                return mode;
            }
            case 16: {
                return PorterDuff.Mode.ADD;
            }
            case 15: {
                return PorterDuff.Mode.SCREEN;
            }
            case 14: {
                return PorterDuff.Mode.MULTIPLY;
            }
            case 9: {
                return PorterDuff.Mode.SRC_ATOP;
            }
            case 5: {
                return PorterDuff.Mode.SRC_IN;
            }
            case 3: 
        }
        return PorterDuff.Mode.SRC_OVER;
    }
}

