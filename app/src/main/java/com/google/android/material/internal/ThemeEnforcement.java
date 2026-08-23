/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.util.AttributeSet
 */
package com.google.android.material.internal;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R;

public final class ThemeEnforcement {
    private static final int[] APPCOMPAT_CHECK_ATTRS = new int[]{R.attr.colorPrimary};
    private static final String APPCOMPAT_THEME_NAME = "Theme.AppCompat";
    private static final int[] MATERIAL_CHECK_ATTRS = new int[]{R.attr.colorSecondary};
    private static final String MATERIAL_THEME_NAME = "Theme.MaterialComponents";

    private ThemeEnforcement() {
    }

    public static void checkAppCompatTheme(Context context) {
        ThemeEnforcement.checkTheme(context, APPCOMPAT_CHECK_ATTRS, APPCOMPAT_THEME_NAME);
    }

    private static void checkCompatibleTheme(Context context, AttributeSet attributeSet, int n, int n2) {
        attributeSet = context.obtainStyledAttributes(attributeSet, R.styleable.ThemeEnforcement, n, n2);
        boolean bl = attributeSet.getBoolean(R.styleable.ThemeEnforcement_enforceMaterialTheme, false);
        attributeSet.recycle();
        if (bl) {
            ThemeEnforcement.checkMaterialTheme(context);
        }
        ThemeEnforcement.checkAppCompatTheme(context);
    }

    public static void checkMaterialTheme(Context context) {
        ThemeEnforcement.checkTheme(context, MATERIAL_CHECK_ATTRS, MATERIAL_THEME_NAME);
    }

    private static void checkTextAppearance(Context context, AttributeSet attributeSet, int[] nArray, int n, int n2, int ... nArray2) {
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ThemeEnforcement, n, n2);
        int n3 = R.styleable.ThemeEnforcement_enforceTextAppearance;
        boolean bl = false;
        if (!typedArray.getBoolean(n3, false)) {
            typedArray.recycle();
            return;
        }
        if (nArray2 != null && nArray2.length != 0) {
            bl = ThemeEnforcement.isCustomTextAppearanceValid(context, attributeSet, nArray, n, n2, nArray2);
        } else if (typedArray.getResourceId(R.styleable.ThemeEnforcement_android_textAppearance, -1) != -1) {
            bl = true;
        }
        typedArray.recycle();
        if (bl) {
            return;
        }
        throw new IllegalArgumentException("This component requires that you specify a valid TextAppearance attribute. Update your app theme to inherit from Theme.MaterialComponents (or a descendant).");
    }

    private static void checkTheme(Context object, int[] nArray, String string2) {
        if (ThemeEnforcement.isTheme((Context)object, nArray)) {
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("The style on this component requires your app theme to be ");
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(" (or a descendant).");
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    public static boolean isAppCompatTheme(Context context) {
        return ThemeEnforcement.isTheme(context, APPCOMPAT_CHECK_ATTRS);
    }

    private static boolean isCustomTextAppearanceValid(Context context, AttributeSet attributeSet, int[] nArray, int n, int n2, int ... nArray2) {
        context = context.obtainStyledAttributes(attributeSet, nArray, n, n2);
        n2 = nArray2.length;
        for (n = 0; n < n2; ++n) {
            if (context.getResourceId(nArray2[n], -1) != -1) continue;
            context.recycle();
            return false;
        }
        context.recycle();
        return true;
    }

    public static boolean isMaterialTheme(Context context) {
        return ThemeEnforcement.isTheme(context, MATERIAL_CHECK_ATTRS);
    }

    private static boolean isTheme(Context context, int[] nArray) {
        context = context.obtainStyledAttributes(nArray);
        boolean bl = context.hasValue(0);
        context.recycle();
        return bl;
    }

    public static TypedArray obtainStyledAttributes(Context context, AttributeSet attributeSet, int[] nArray, int n, int n2, int ... nArray2) {
        ThemeEnforcement.checkCompatibleTheme(context, attributeSet, n, n2);
        ThemeEnforcement.checkTextAppearance(context, attributeSet, nArray, n, n2, nArray2);
        return context.obtainStyledAttributes(attributeSet, nArray, n, n2);
    }

    public static TintTypedArray obtainTintedStyledAttributes(Context context, AttributeSet attributeSet, int[] nArray, int n, int n2, int ... nArray2) {
        ThemeEnforcement.checkCompatibleTheme(context, attributeSet, n, n2);
        ThemeEnforcement.checkTextAppearance(context, attributeSet, nArray, n, n2, nArray2);
        return TintTypedArray.obtainStyledAttributes(context, attributeSet, nArray, n, n2);
    }
}

