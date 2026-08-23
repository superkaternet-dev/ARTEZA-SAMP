/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.net.Uri
 *  android.os.CancellationSignal
 *  android.util.Log
 */
package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompatBaseImpl;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.provider.FontsContractCompat;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;

class TypefaceCompatApi24Impl
extends TypefaceCompatBaseImpl {
    private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String TAG = "TypefaceCompatApi24Impl";
    private static final Method sAddFontWeightStyle;
    private static final Method sCreateFromFamiliesWithDefault;
    private static final Class<?> sFontFamily;
    private static final Constructor<?> sFontFamilyCtor;

    static {
        Method method;
        Method method2;
        Constructor<?> constructor;
        Class<?> clazz;
        block3: {
            try {
                clazz = Class.forName(FONT_FAMILY_CLASS);
                constructor = clazz.getConstructor(new Class[0]);
                method2 = clazz.getMethod(ADD_FONT_WEIGHT_STYLE_METHOD, ByteBuffer.class, Integer.TYPE, List.class, Integer.TYPE, Boolean.TYPE);
                method = Typeface.class.getMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, Array.newInstance(clazz, 1).getClass());
                break block3;
            }
            catch (NoSuchMethodException noSuchMethodException) {
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
            Log.e((String)TAG, (String)clazz.getClass().getName(), clazz);
            constructor = null;
            method2 = null;
            clazz = null;
            method = null;
        }
        sFontFamilyCtor = constructor;
        sFontFamily = clazz;
        sAddFontWeightStyle = method2;
        sCreateFromFamiliesWithDefault = method;
    }

    TypefaceCompatApi24Impl() {
    }

    private static boolean addFontWeightStyle(Object object, ByteBuffer byteBuffer, int n, int n2, boolean bl) {
        try {
            bl = (Boolean)sAddFontWeightStyle.invoke(object, byteBuffer, n, null, n2, bl);
            return bl;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        return false;
    }

    private static Typeface createFromFamiliesWithDefault(Object object) {
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
        return null;
    }

    public static boolean isUsable() {
        Method method = sAddFontWeightStyle;
        if (method == null) {
            Log.w((String)TAG, (String)"Unable to collect necessary private methods.Fallback to legacy implementation.");
        }
        boolean bl = method != null;
        return bl;
    }

    private static Object newFamily() {
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
        return null;
    }

    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFileResourceEntryArray, Resources resources, int n) {
        Object object = TypefaceCompatApi24Impl.newFamily();
        if (object == null) {
            return null;
        }
        for (FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry : fontFileResourceEntryArray.getEntries()) {
            ByteBuffer byteBuffer = TypefaceCompatUtil.copyToDirectBuffer(context, resources, fontFileResourceEntry.getResourceId());
            if (byteBuffer == null) {
                return null;
            }
            if (TypefaceCompatApi24Impl.addFontWeightStyle(object, byteBuffer, fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic())) continue;
            return null;
        }
        return TypefaceCompatApi24Impl.createFromFamiliesWithDefault(object);
    }

    @Override
    public Typeface createFromFontInfo(Context context, CancellationSignal cancellationSignal, FontsContractCompat.FontInfo[] fontInfoArray, int n) {
        Object object = TypefaceCompatApi24Impl.newFamily();
        if (object == null) {
            return null;
        }
        SimpleArrayMap<Uri, ByteBuffer> simpleArrayMap = new SimpleArrayMap<Uri, ByteBuffer>();
        for (FontsContractCompat.FontInfo fontInfo : fontInfoArray) {
            ByteBuffer byteBuffer;
            Uri uri = fontInfo.getUri();
            ByteBuffer byteBuffer2 = byteBuffer = (ByteBuffer)simpleArrayMap.get(uri);
            if (byteBuffer == null) {
                byteBuffer2 = TypefaceCompatUtil.mmap(context, cancellationSignal, uri);
                simpleArrayMap.put(uri, byteBuffer2);
            }
            if (byteBuffer2 == null) {
                return null;
            }
            if (TypefaceCompatApi24Impl.addFontWeightStyle(object, byteBuffer2, fontInfo.getTtcIndex(), fontInfo.getWeight(), fontInfo.isItalic())) continue;
            return null;
        }
        context = TypefaceCompatApi24Impl.createFromFamiliesWithDefault(object);
        if (context == null) {
            return null;
        }
        return Typeface.create((Typeface)context, (int)n);
    }
}

