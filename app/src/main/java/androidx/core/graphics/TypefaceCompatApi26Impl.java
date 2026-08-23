/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.AssetManager
 *  android.content.res.Resources
 *  android.graphics.Typeface
 *  android.graphics.Typeface$Builder
 *  android.graphics.fonts.FontVariationAxis
 *  android.net.Uri
 *  android.os.CancellationSignal
 *  android.util.Log
 */
package androidx.core.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompatApi21Impl;
import androidx.core.provider.FontsContractCompat;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;

public class TypefaceCompatApi26Impl
extends TypefaceCompatApi21Impl {
    private static final String ABORT_CREATION_METHOD = "abortCreation";
    private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
    private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
    private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
    private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
    private static final String FREEZE_METHOD = "freeze";
    private static final int RESOLVE_BY_FONT_TABLE = -1;
    private static final String TAG = "TypefaceCompatApi26Impl";
    protected final Method mAbortCreation;
    protected final Method mAddFontFromAssetManager;
    protected final Method mAddFontFromBuffer;
    protected final Method mCreateFromFamiliesWithDefault;
    protected final Class<?> mFontFamily;
    protected final Constructor<?> mFontFamilyCtor;
    protected final Method mFreeze;

    public TypefaceCompatApi26Impl() {
        Method method;
        Method method2;
        Method method3;
        Method method4;
        Method method5;
        Object object;
        Class<?> clazz;
        block3: {
            try {
                clazz = this.obtainFontFamily();
                object = this.obtainFontFamilyCtor(clazz);
                method5 = this.obtainAddFontFromAssetManagerMethod(clazz);
                method4 = this.obtainAddFontFromBufferMethod(clazz);
                method3 = this.obtainFreezeMethod(clazz);
                method2 = this.obtainAbortCreationMethod(clazz);
                method = this.obtainCreateFromFamiliesWithDefaultMethod(clazz);
                break block3;
            }
            catch (NoSuchMethodException noSuchMethodException) {
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unable to collect necessary methods for class ");
            ((StringBuilder)object).append(method5.getClass().getName());
            Log.e((String)TAG, (String)((StringBuilder)object).toString(), (Throwable)((Object)method5));
            clazz = null;
            object = null;
            method5 = null;
            method4 = null;
            method3 = null;
            method2 = null;
            method = null;
        }
        this.mFontFamily = clazz;
        this.mFontFamilyCtor = object;
        this.mAddFontFromAssetManager = method5;
        this.mAddFontFromBuffer = method4;
        this.mFreeze = method3;
        this.mAbortCreation = method2;
        this.mCreateFromFamiliesWithDefault = method;
    }

    private void abortCreation(Object object) {
        try {
            this.mAbortCreation.invoke(object, new Object[0]);
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
    }

    private boolean addFontFromAssetManager(Context context, Object object, String string2, int n, int n2, int n3, FontVariationAxis[] fontVariationAxisArray) {
        try {
            boolean bl = (Boolean)this.mAddFontFromAssetManager.invoke(object, context.getAssets(), string2, 0, false, n, n2, n3, fontVariationAxisArray);
            return bl;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        return false;
    }

    private boolean addFontFromBuffer(Object object, ByteBuffer byteBuffer, int n, int n2, int n3) {
        try {
            boolean bl = (Boolean)this.mAddFontFromBuffer.invoke(object, byteBuffer, n, null, n2, n3);
            return bl;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        return false;
    }

    private boolean freeze(Object object) {
        try {
            boolean bl = (Boolean)this.mFreeze.invoke(object, new Object[0]);
            return bl;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        return false;
    }

    private boolean isFontFamilyPrivateAPIAvailable() {
        if (this.mAddFontFromAssetManager == null) {
            Log.w((String)TAG, (String)"Unable to collect necessary private methods. Fallback to legacy implementation.");
        }
        boolean bl = this.mAddFontFromAssetManager != null;
        return bl;
    }

    private Object newFamily() {
        try {
            Object obj = this.mFontFamilyCtor.newInstance(new Object[0]);
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

    protected Typeface createFromFamiliesWithDefault(Object object) {
        try {
            Object object2 = Array.newInstance(this.mFontFamily, 1);
            Array.set(object2, 0, object);
            object = (Typeface)this.mCreateFromFamiliesWithDefault.invoke(null, object2, -1, -1);
            return object;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        return null;
    }

    @Override
    public Typeface createFromFontFamilyFilesResourceEntry(Context context, FontResourcesParserCompat.FontFamilyFilesResourceEntry fontFileResourceEntryArray, Resources object, int n) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromFontFamilyFilesResourceEntry(context, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)fontFileResourceEntryArray, (Resources)object, n);
        }
        object = this.newFamily();
        if (object == null) {
            return null;
        }
        for (FontResourcesParserCompat.FontFileResourceEntry fontFileResourceEntry : fontFileResourceEntryArray.getEntries()) {
            if (this.addFontFromAssetManager(context, object, fontFileResourceEntry.getFileName(), fontFileResourceEntry.getTtcIndex(), fontFileResourceEntry.getWeight(), fontFileResourceEntry.isItalic() ? 1 : 0, FontVariationAxis.fromFontVariationSettings((String)fontFileResourceEntry.getVariationSettings()))) continue;
            this.abortCreation(object);
            return null;
        }
        if (!this.freeze(object)) {
            return null;
        }
        return this.createFromFamiliesWithDefault(object);
    }

    /*
     * Loose catch block
     */
    @Override
    public Typeface createFromFontInfo(Context object, CancellationSignal object2, FontsContractCompat.FontInfo[] object3, int n) {
        if (((FontsContractCompat.FontInfo[])object3).length < 1) {
            return null;
        }
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            block20: {
                block18: {
                    block19: {
                        object3 = this.findBestInfo((FontsContractCompat.FontInfo[])object3, n);
                        object = object.getContentResolver();
                        object = object.openFileDescriptor(((FontsContractCompat.FontInfo)object3).getUri(), "r", object2);
                        if (object != null) break block18;
                        if (object == null) break block19;
                        {
                            catch (IOException iOException) {
                                return null;
                            }
                        }
                        object.close();
                    }
                    return null;
                }
                object2 = new Typeface.Builder(object.getFileDescriptor());
                object2 = object2.setWeight(((FontsContractCompat.FontInfo)object3).getWeight()).setItalic(((FontsContractCompat.FontInfo)object3).isItalic()).build();
                if (object == null) break block20;
                object.close();
            }
            return object2;
            catch (Throwable throwable) {
                if (object != null) {
                    try {
                        object.close();
                    }
                    catch (Throwable throwable2) {
                        // empty catch block
                    }
                }
                throw throwable;
            }
        }
        Map<Uri, ByteBuffer> map = FontsContractCompat.prepareFontData((Context)object, (FontsContractCompat.FontInfo[])object3, object2);
        object2 = this.newFamily();
        if (object2 == null) {
            return null;
        }
        int n2 = ((Object)object3).length;
        boolean bl = false;
        for (int i = 0; i < n2; ++i) {
            object = object3[i];
            ByteBuffer byteBuffer = map.get(((FontsContractCompat.FontInfo)object).getUri());
            if (byteBuffer == null) continue;
            if (!this.addFontFromBuffer(object2, byteBuffer, ((FontsContractCompat.FontInfo)object).getTtcIndex(), ((FontsContractCompat.FontInfo)object).getWeight(), ((FontsContractCompat.FontInfo)object).isItalic() ? 1 : 0)) {
                this.abortCreation(object2);
                return null;
            }
            bl = true;
        }
        if (!bl) {
            this.abortCreation(object2);
            return null;
        }
        if (!this.freeze(object2)) {
            return null;
        }
        object = this.createFromFamiliesWithDefault(object2);
        if (object == null) {
            return null;
        }
        return Typeface.create((Typeface)object, (int)n);
    }

    @Override
    public Typeface createFromResourcesFontFile(Context context, Resources object, int n, String string2, int n2) {
        if (!this.isFontFamilyPrivateAPIAvailable()) {
            return super.createFromResourcesFontFile(context, (Resources)object, n, string2, n2);
        }
        object = this.newFamily();
        if (object == null) {
            return null;
        }
        if (!this.addFontFromAssetManager(context, object, string2, 0, -1, -1, null)) {
            this.abortCreation(object);
            return null;
        }
        if (!this.freeze(object)) {
            return null;
        }
        return this.createFromFamiliesWithDefault(object);
    }

    protected Method obtainAbortCreationMethod(Class<?> clazz) throws NoSuchMethodException {
        return clazz.getMethod(ABORT_CREATION_METHOD, new Class[0]);
    }

    protected Method obtainAddFontFromAssetManagerMethod(Class<?> clazz) throws NoSuchMethodException {
        return clazz.getMethod(ADD_FONT_FROM_ASSET_MANAGER_METHOD, AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
    }

    protected Method obtainAddFontFromBufferMethod(Class<?> clazz) throws NoSuchMethodException {
        return clazz.getMethod(ADD_FONT_FROM_BUFFER_METHOD, ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
    }

    protected Method obtainCreateFromFamiliesWithDefaultMethod(Class<?> genericDeclaration) throws NoSuchMethodException {
        genericDeclaration = Typeface.class.getDeclaredMethod(CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD, Array.newInstance(genericDeclaration, 1).getClass(), Integer.TYPE, Integer.TYPE);
        ((Method)genericDeclaration).setAccessible(true);
        return genericDeclaration;
    }

    protected Class<?> obtainFontFamily() throws ClassNotFoundException {
        return Class.forName(FONT_FAMILY_CLASS);
    }

    protected Constructor<?> obtainFontFamilyCtor(Class<?> clazz) throws NoSuchMethodException {
        return clazz.getConstructor(new Class[0]);
    }

    protected Method obtainFreezeMethod(Class<?> clazz) throws NoSuchMethodException {
        return clazz.getMethod(FREEZE_METHOD, new Class[0]);
    }
}

