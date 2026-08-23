/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.graphics.Color
 *  android.util.Log
 *  android.util.TypedValue
 *  android.view.View
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import androidx.appcompat.R;
import androidx.appcompat.widget.TintTypedArray;
import androidx.core.graphics.ColorUtils;

public class ThemeUtils {
    static final int[] ACTIVATED_STATE_SET;
    static final int[] CHECKED_STATE_SET;
    static final int[] DISABLED_STATE_SET;
    static final int[] EMPTY_STATE_SET;
    static final int[] FOCUSED_STATE_SET;
    static final int[] NOT_PRESSED_OR_FOCUSED_STATE_SET;
    static final int[] PRESSED_STATE_SET;
    static final int[] SELECTED_STATE_SET;
    private static final String TAG = "ThemeUtils";
    private static final int[] TEMP_ARRAY;
    private static final ThreadLocal<TypedValue> TL_TYPED_VALUE;

    static {
        TL_TYPED_VALUE = new ThreadLocal();
        DISABLED_STATE_SET = new int[]{-16842910};
        FOCUSED_STATE_SET = new int[]{16842908};
        ACTIVATED_STATE_SET = new int[]{16843518};
        PRESSED_STATE_SET = new int[]{16842919};
        CHECKED_STATE_SET = new int[]{0x10100A0};
        SELECTED_STATE_SET = new int[]{0x10100A1};
        NOT_PRESSED_OR_FOCUSED_STATE_SET = new int[]{-16842919, -16842908};
        EMPTY_STATE_SET = new int[0];
        TEMP_ARRAY = new int[1];
    }

    private ThemeUtils() {
    }

    public static void checkAppCompatTheme(View view, Context context) {
        context = context.obtainStyledAttributes(R.styleable.AppCompatTheme);
        try {
            if (!context.hasValue(R.styleable.AppCompatTheme_windowActionBar)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("View ");
                stringBuilder.append(view.getClass());
                stringBuilder.append(" is an AppCompat widget that can only be used with a Theme.AppCompat theme (or descendant).");
                Log.e((String)TAG, (String)stringBuilder.toString());
            }
            return;
        }
        finally {
            context.recycle();
        }
    }

    public static ColorStateList createDisabledStateList(int n, int n2) {
        int[][] nArrayArray = new int[2][];
        int[] nArray = new int[2];
        nArrayArray[0] = DISABLED_STATE_SET;
        nArray[0] = n2;
        n2 = 0 + 1;
        nArrayArray[n2] = EMPTY_STATE_SET;
        nArray[n2] = n;
        return new ColorStateList((int[][])nArrayArray, nArray);
    }

    public static int getDisabledThemeAttrColor(Context context, int n) {
        ColorStateList colorStateList = ThemeUtils.getThemeAttrColorStateList(context, n);
        if (colorStateList != null && colorStateList.isStateful()) {
            return colorStateList.getColorForState(DISABLED_STATE_SET, colorStateList.getDefaultColor());
        }
        colorStateList = ThemeUtils.getTypedValue();
        context.getTheme().resolveAttribute(0x1010033, (TypedValue)colorStateList, true);
        return ThemeUtils.getThemeAttrColor(context, n, colorStateList.getFloat());
    }

    public static int getThemeAttrColor(Context context, int n) {
        Object object = TEMP_ARRAY;
        object[0] = n;
        object = TintTypedArray.obtainStyledAttributes(context, null, (int[])object);
        try {
            n = ((TintTypedArray)object).getColor(0, 0);
            return n;
        }
        finally {
            ((TintTypedArray)object).recycle();
        }
    }

    static int getThemeAttrColor(Context context, int n, float f) {
        n = ThemeUtils.getThemeAttrColor(context, n);
        return ColorUtils.setAlphaComponent(n, Math.round((float)Color.alpha((int)n) * f));
    }

    public static ColorStateList getThemeAttrColorStateList(Context object, int n) {
        Object object2 = TEMP_ARRAY;
        object2[0] = n;
        object = TintTypedArray.obtainStyledAttributes((Context)object, null, object2);
        try {
            object2 = ((TintTypedArray)object).getColorStateList(0);
            return object2;
        }
        finally {
            ((TintTypedArray)object).recycle();
        }
    }

    private static TypedValue getTypedValue() {
        TypedValue typedValue;
        ThreadLocal<TypedValue> threadLocal = TL_TYPED_VALUE;
        TypedValue typedValue2 = typedValue = threadLocal.get();
        if (typedValue == null) {
            typedValue2 = new TypedValue();
            threadLocal.set(typedValue2);
        }
        return typedValue2;
    }
}

