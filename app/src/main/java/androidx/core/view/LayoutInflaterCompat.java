/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.LayoutInflater$Factory
 *  android.view.LayoutInflater$Factory2
 *  android.view.View
 */
package androidx.core.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import androidx.core.view.LayoutInflaterFactory;
import java.lang.reflect.Field;

public final class LayoutInflaterCompat {
    private static final String TAG = "LayoutInflaterCompatHC";
    private static boolean sCheckedField;
    private static Field sLayoutInflaterFactory2Field;

    private LayoutInflaterCompat() {
    }

    private static void forceSetFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 object) {
        Field field;
        if (!sCheckedField) {
            try {
                sLayoutInflaterFactory2Field = field = LayoutInflater.class.getDeclaredField("mFactory2");
                field.setAccessible(true);
            }
            catch (NoSuchFieldException noSuchFieldException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("forceSetFactory2 Could not find field 'mFactory2' on class ");
                stringBuilder.append(LayoutInflater.class.getName());
                stringBuilder.append("; inflation may have unexpected results.");
                Log.e((String)TAG, (String)stringBuilder.toString(), (Throwable)noSuchFieldException);
            }
            sCheckedField = true;
        }
        if ((field = sLayoutInflaterFactory2Field) != null) {
            try {
                field.set(layoutInflater, object);
            }
            catch (IllegalAccessException illegalAccessException) {
                object = new StringBuilder();
                ((StringBuilder)object).append("forceSetFactory2 could not set the Factory2 on LayoutInflater ");
                ((StringBuilder)object).append(layoutInflater);
                ((StringBuilder)object).append("; inflation may have unexpected results.");
                Log.e((String)TAG, (String)((StringBuilder)object).toString(), (Throwable)illegalAccessException);
            }
        }
    }

    @Deprecated
    public static LayoutInflaterFactory getFactory(LayoutInflater layoutInflater) {
        if ((layoutInflater = layoutInflater.getFactory()) instanceof Factory2Wrapper) {
            return ((Factory2Wrapper)layoutInflater).mDelegateFactory;
        }
        return null;
    }

    @Deprecated
    public static void setFactory(LayoutInflater layoutInflater, LayoutInflaterFactory object) {
        int n = Build.VERSION.SDK_INT;
        Factory2Wrapper factory2Wrapper = null;
        Factory2Wrapper factory2Wrapper2 = null;
        if (n >= 21) {
            factory2Wrapper = factory2Wrapper2;
            if (object != null) {
                factory2Wrapper = new Factory2Wrapper((LayoutInflaterFactory)object);
            }
            layoutInflater.setFactory2(factory2Wrapper);
        } else {
            object = object != null ? new Factory2Wrapper((LayoutInflaterFactory)object) : factory2Wrapper;
            layoutInflater.setFactory2((LayoutInflater.Factory2)object);
            factory2Wrapper = layoutInflater.getFactory();
            if (factory2Wrapper instanceof LayoutInflater.Factory2) {
                LayoutInflaterCompat.forceSetFactory2(layoutInflater, factory2Wrapper);
            } else {
                LayoutInflaterCompat.forceSetFactory2(layoutInflater, (LayoutInflater.Factory2)object);
            }
        }
    }

    public static void setFactory2(LayoutInflater layoutInflater, LayoutInflater.Factory2 factory2) {
        layoutInflater.setFactory2(factory2);
        if (Build.VERSION.SDK_INT < 21) {
            LayoutInflater.Factory factory = layoutInflater.getFactory();
            if (factory instanceof LayoutInflater.Factory2) {
                LayoutInflaterCompat.forceSetFactory2(layoutInflater, (LayoutInflater.Factory2)factory);
            } else {
                LayoutInflaterCompat.forceSetFactory2(layoutInflater, factory2);
            }
        }
    }

    static class Factory2Wrapper
    implements LayoutInflater.Factory2 {
        final LayoutInflaterFactory mDelegateFactory;

        Factory2Wrapper(LayoutInflaterFactory layoutInflaterFactory) {
            this.mDelegateFactory = layoutInflaterFactory;
        }

        public View onCreateView(View view, String string2, Context context, AttributeSet attributeSet) {
            return this.mDelegateFactory.onCreateView(view, string2, context, attributeSet);
        }

        public View onCreateView(String string2, Context context, AttributeSet attributeSet) {
            return this.mDelegateFactory.onCreateView(null, string2, context, attributeSet);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.getClass().getName());
            stringBuilder.append("{");
            stringBuilder.append(this.mDelegateFactory);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }
}

