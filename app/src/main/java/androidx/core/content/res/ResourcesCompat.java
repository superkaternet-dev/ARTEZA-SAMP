/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.content.res.Resources$Theme
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.os.Looper
 *  android.util.Log
 *  android.util.TypedValue
 *  org.xmlpull.v1.XmlPullParser
 *  org.xmlpull.v1.XmlPullParserException
 */
package androidx.core.content.res;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.TypedValue;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.util.Preconditions;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ResourcesCompat {
    public static final int ID_NULL = 0;
    private static final String TAG = "ResourcesCompat";

    private ResourcesCompat() {
    }

    public static int getColor(Resources resources, int n, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColor(n, theme);
        }
        return resources.getColor(n);
    }

    public static ColorStateList getColorStateList(Resources resources, int n, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 23) {
            return resources.getColorStateList(n, theme);
        }
        return resources.getColorStateList(n);
    }

    public static Drawable getDrawable(Resources resources, int n, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawable(n, theme);
        }
        return resources.getDrawable(n);
    }

    public static Drawable getDrawableForDensity(Resources resources, int n, int n2, Resources.Theme theme) throws Resources.NotFoundException {
        if (Build.VERSION.SDK_INT >= 21) {
            return resources.getDrawableForDensity(n, n2, theme);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return resources.getDrawableForDensity(n, n2);
        }
        return resources.getDrawable(n);
    }

    public static float getFloat(Resources object, int n) {
        TypedValue typedValue = new TypedValue();
        object.getValue(n, typedValue, true);
        if (typedValue.type == 4) {
            return typedValue.getFloat();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Resource ID #0x");
        ((StringBuilder)object).append(Integer.toHexString(n));
        ((StringBuilder)object).append(" type #0x");
        ((StringBuilder)object).append(Integer.toHexString(typedValue.type));
        ((StringBuilder)object).append(" is not valid");
        throw new Resources.NotFoundException(((StringBuilder)object).toString());
    }

    public static Typeface getFont(Context context, int n) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, n, new TypedValue(), 0, null, null, false);
    }

    public static Typeface getFont(Context context, int n, TypedValue typedValue, int n2, FontCallback fontCallback) throws Resources.NotFoundException {
        if (context.isRestricted()) {
            return null;
        }
        return ResourcesCompat.loadFont(context, n, typedValue, n2, fontCallback, null, true);
    }

    public static void getFont(Context context, int n, FontCallback fontCallback, Handler handler) throws Resources.NotFoundException {
        Preconditions.checkNotNull(fontCallback);
        if (context.isRestricted()) {
            fontCallback.callbackFailAsync(-4, handler);
            return;
        }
        ResourcesCompat.loadFont(context, n, new TypedValue(), 0, fontCallback, handler, false);
    }

    private static Typeface loadFont(Context object, int n, TypedValue typedValue, int n2, FontCallback fontCallback, Handler handler, boolean bl) {
        Resources resources = object.getResources();
        resources.getValue(n, typedValue, true);
        object = ResourcesCompat.loadFont((Context)object, resources, typedValue, n, n2, fontCallback, handler, bl);
        if (object == null && fontCallback == null) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Font resource ID #0x");
            ((StringBuilder)object).append(Integer.toHexString(n));
            ((StringBuilder)object).append(" could not be retrieved.");
            throw new Resources.NotFoundException(((StringBuilder)object).toString());
        }
        return object;
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    private static Typeface loadFont(Context var0, Resources var1_15, TypedValue var2_16, int var3_17, int var4_18, FontCallback var5_19, Handler var6_20, boolean var7_21) {
        block30: {
            block31: {
                block28: {
                    block27: {
                        block29: {
                            block24: {
                                block25: {
                                    block26: {
                                        if (var2_16.string == null) break block30;
                                        var2_16 = var2_16.string.toString();
                                        if (!var2_16.startsWith("res/")) {
                                            if (var5_19 != null) {
                                                var5_19.callbackFailAsync(-3, var6_20);
                                            }
                                            return null;
                                        }
                                        var9_22 /* !! */  = TypefaceCompat.findFromCache((Resources)var1_15, var3_17, var4_18);
                                        if (var9_22 /* !! */  != null) {
                                            if (var5_19 != null) {
                                                var5_19.callbackSuccessAsync(var9_22 /* !! */ , var6_20);
                                            }
                                            return var9_22 /* !! */ ;
                                        }
                                        var8_23 = var2_16.toLowerCase().endsWith(".xml");
                                        if (!var8_23) break block24;
                                        var9_22 /* !! */  = var1_15.getXml(var3_17);
                                        var9_22 /* !! */  = FontResourcesParserCompat.parse((XmlPullParser)var9_22 /* !! */ , (Resources)var1_15);
                                        if (var9_22 /* !! */  != null) break block25;
                                        Log.e((String)"ResourcesCompat", (String)"Failed to find font-family tag");
                                        if (var5_19 == null) break block26;
                                        try {
                                            var5_19.callbackFailAsync(-3, var6_20);
                                        }
                                        catch (IOException var0_1) {
                                            break block27;
                                        }
                                        catch (XmlPullParserException var0_2) {
                                            break block28;
                                        }
                                    }
                                    return null;
                                }
                                try {
                                    var0 = TypefaceCompat.createFromResourcesFamilyXml((Context)var0, (FontResourcesParserCompat.FamilyResourceEntry)var9_22 /* !! */ , (Resources)var1_15, var3_17, var4_18, var5_19, var6_20, var7_21);
                                    return var0;
                                }
                                catch (IOException var0_3) {
                                    break block27;
                                }
                                catch (XmlPullParserException var0_4) {
                                    break block28;
                                }
                                catch (IOException var0_5) {
                                    break block27;
                                }
                                catch (XmlPullParserException var0_6) {
                                    break block28;
                                }
                            }
                            var0 = TypefaceCompat.createFromResourcesFontFile((Context)var0, (Resources)var1_15, var3_17, (String)var2_16, var4_18);
                            if (var5_19 == null) break block29;
                            if (var0 == null) ** GOTO lbl51
                            try {
                                var5_19.callbackSuccessAsync((Typeface)var0, var6_20);
                                break block29;
lbl51:
                                // 1 sources

                                var5_19.callbackFailAsync(-3, var6_20);
                            }
                            catch (IOException var0_7) {
                                break block27;
                            }
                            catch (XmlPullParserException var0_8) {
                                break block28;
                            }
                        }
                        return var0;
                        catch (IOException var0_9) {
                            break block27;
                        }
                        catch (XmlPullParserException var0_11) {
                            break block28;
                        }
                        catch (IOException var0_13) {
                            // empty catch block
                        }
                    }
                    var1_15 = new StringBuilder();
                    var1_15.append("Failed to read xml resource ");
                    var1_15.append((String)var2_16);
                    Log.e((String)"ResourcesCompat", (String)var1_15.toString(), (Throwable)var0_10);
                    break block31;
                    catch (XmlPullParserException var0_14) {
                        // empty catch block
                    }
                }
                var1_15 = new StringBuilder();
                var1_15.append("Failed to parse xml resource ");
                var1_15.append((String)var2_16);
                Log.e((String)"ResourcesCompat", (String)var1_15.toString(), (Throwable)var0_12);
            }
            if (var5_19 != null) {
                var5_19.callbackFailAsync(-3, var6_20);
            }
            return null;
        }
        var0 = new StringBuilder();
        var0.append("Resource \"");
        var0.append(var1_15.getResourceName(var3_17));
        var0.append("\" (");
        var0.append(Integer.toHexString(var3_17));
        var0.append(") is not a Font: ");
        var0.append(var2_16);
        throw new Resources.NotFoundException(var0.toString());
    }

    public static abstract class FontCallback {
        public final void callbackFailAsync(int n, Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post(new Runnable(this, n){
                final FontCallback this$0;
                final int val$reason;
                {
                    this.this$0 = fontCallback;
                    this.val$reason = n;
                }

                @Override
                public void run() {
                    this.this$0.onFontRetrievalFailed(this.val$reason);
                }
            });
        }

        public final void callbackSuccessAsync(Typeface typeface, Handler handler) {
            Handler handler2 = handler;
            if (handler == null) {
                handler2 = new Handler(Looper.getMainLooper());
            }
            handler2.post(new Runnable(this, typeface){
                final FontCallback this$0;
                final Typeface val$typeface;
                {
                    this.this$0 = fontCallback;
                    this.val$typeface = typeface;
                }

                @Override
                public void run() {
                    this.this$0.onFontRetrieved(this.val$typeface);
                }
            });
        }

        public abstract void onFontRetrievalFailed(int var1);

        public abstract void onFontRetrieved(Typeface var1);
    }

    public static final class ThemeCompat {
        private ThemeCompat() {
        }

        public static void rebase(Resources.Theme theme) {
            if (Build.VERSION.SDK_INT >= 29) {
                ImplApi29.rebase(theme);
            } else if (Build.VERSION.SDK_INT >= 23) {
                ImplApi23.rebase(theme);
            }
        }

        static class ImplApi23 {
            private static Method sRebaseMethod;
            private static boolean sRebaseMethodFetched;
            private static final Object sRebaseMethodLock;

            static {
                sRebaseMethodLock = new Object();
            }

            private ImplApi23() {
            }

            /*
             * WARNING - void declaration
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            static void rebase(Resources.Theme theme) {
                Object object = sRebaseMethodLock;
                synchronized (object) {
                    block9: {
                        Method method;
                        boolean bl = sRebaseMethodFetched;
                        if (!bl) {
                            try {
                                sRebaseMethod = method = Resources.Theme.class.getDeclaredMethod("rebase", new Class[0]);
                                method.setAccessible(true);
                            }
                            catch (NoSuchMethodException noSuchMethodException) {
                                Log.i((String)ResourcesCompat.TAG, (String)"Failed to retrieve rebase() method", (Throwable)noSuchMethodException);
                            }
                            sRebaseMethodFetched = true;
                        }
                        if ((method = sRebaseMethod) != null) {
                            void var0_3;
                            try {
                                method.invoke((Object)theme, new Object[0]);
                                break block9;
                            }
                            catch (InvocationTargetException invocationTargetException) {
                            }
                            catch (IllegalAccessException illegalAccessException) {
                                // empty catch block
                            }
                            Log.i((String)ResourcesCompat.TAG, (String)"Failed to invoke rebase() method via reflection", (Throwable)var0_3);
                            sRebaseMethod = null;
                        }
                    }
                    return;
                }
            }
        }

        static class ImplApi29 {
            private ImplApi29() {
            }

            static void rebase(Resources.Theme theme) {
                theme.rebase();
            }
        }
    }
}

