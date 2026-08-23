/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.os.CancellationSignal
 *  android.os.ParcelFileDescriptor
 *  android.system.ErrnoException
 *  android.system.Os
 *  android.system.OsConstants
 *  android.util.Log
 */
package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompatBaseImpl;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.provider.FontsContractCompat;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class TypefaceCompatApi21Impl
extends TypefaceCompatBaseImpl {
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi21Impl";
    private static Method sAddFontWeightStyle;
    private static Method sCreateFromFamiliesWithDefault;
    private static Class<?> sFontFamily;
    private static Constructor<?> sFontFamilyCtor;
    private static boolean sHasInitBeenCalled;

    static {
        sHasInitBeenCalled = false;
    }

    TypefaceCompatApi21Impl() {
    }

    /*
     * WARNING - void declaration
     */
    private static boolean addFontWeightStyle(Object object, String string2, int n, boolean bl) {
        void var0_3;
        TypefaceCompatApi21Impl.init();
        try {
            bl = (Boolean)sAddFontWeightStyle.invoke(object, string2, n, bl);
            return bl;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        throw new RuntimeException((Throwable)var0_3);
    }

    /*
     * WARNING - void declaration
     */
    private static Typeface createFromFamiliesWithDefault(Object object) {
        void var0_3;
        TypefaceCompatApi21Impl.init();
        try {
            Object object2 = Array.newInstance(sFontFamily, 1);
            Array.set(object2, 0, object);
            object = (Typeface)sCreateFromFamiliesWithDefault.invoke(null, object2);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        throw new RuntimeException((Throwable)var0_3);
    }

    private File getFile(ParcelFileDescriptor object) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("/proc/self/fd/");
            stringBuilder.append(object.getFd());
            object = Os.readlink((String)stringBuilder.toString());
            if (OsConstants.S_ISREG((int)Os.stat((String)object).st_mode)) {
                object = new File((String)object);
                return object;
            }
            return null;
        }
        catch (ErrnoException errnoException) {
            return null;
        }
    }

    private static void init() {
        Method method;
        Method method2;
        Constructor<?> constructor;
        Class<?> clazz;
        block4: {
            if (sHasInitBeenCalled) {
                return;
            }
            sHasInitBeenCalled = true;
            try {
                clazz = Class.forName(FONT_FAMILY_CLASS);
                constructor = clazz.getConstructor(new Class[0]);
                method2 = clazz.getMethod(ADD_FONT_WEIGHT_STYLE_METHOD, String.class, Integer.TYPE, Boolean.TYPE);
                method = Typeface.class.getMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, Array.newInstance(clazz, 1).getClass());
                break block4;
            }
            catch (NoSuchMethodException noSuchMethodException) {
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
            Log.e((String)TAG, (String)method2.getClass().getName(), (Throwable)((Object)method2));
            clazz = null;
            constructor = null;
            method2 = null;
            method = null;
        }
        sFontFamilyCtor = constructor;
        sFontFamily = clazz;
        sAddFontWeightStyle = method2;
        sCreateFromFamiliesWithDefault = method;
    }

    /*
     * WARNING - void declaration
     */
    private static Object newFamily() {
        void var0_4;
        TypefaceCompatApi21Impl.init();
        try {
            Object obj = sFontFamilyCtor.newInstance(new Object[0]);
            return obj;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (InstantiationException instantiationException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        throw new RuntimeException((Throwable)var0_4);
    }

    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry object, Resources resources, int n) {
        Object object2 = TypefaceCompatApi21Impl.newFamily();
        for (FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry : ((FontResourcesParserCompat.FontFamilyFilesResourceEntry)object).getEntries()) {
            block7: {
                boolean bl;
                block6: {
                    object = TypefaceCompatUtil.getTempFile(context);
                    if (object == null) {
                        return null;
                    }
                    bl = TypefaceCompatUtil.copyToFile((File)object, resources, fontFileResourceEntry.getResourceId());
                    if (bl) break block6;
                    ((File)object).delete();
                    return null;
                }
                try {
                    bl = TypefaceCompatApi21Impl.addFontWeightStyle(object2, ((File)object).getPath(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic());
                    if (bl) break block7;
                }
                catch (Throwable throwable) {
                    ((File)object).delete();
                    throw throwable;
                }
                catch (RuntimeException runtimeException) {
                    ((File)object).delete();
                    return null;
                }
                ((File)object).delete();
                return null;
            }
            ((File)object).delete();
        }
        return TypefaceCompatApi21Impl.createFromFamiliesWithDefault(object2);
    }

    /*
     * Exception decompiling
     */
    @Override
    public Typeface createFromFontInfo(Context var1_1, CancellationSignal var2_5, FontsContractCompat.FontInfo[] var3_7, int var4_9) {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.getStartingBlocks(Op04StructuredStatement.java:412)
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.buildNestedBlocks(Op04StructuredStatement.java:487)
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
}

