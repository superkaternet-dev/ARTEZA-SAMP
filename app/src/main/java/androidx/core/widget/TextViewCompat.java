/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.content.pm.PackageManager
 *  android.content.pm.ResolveInfo
 *  android.content.res.ColorStateList
 *  android.graphics.Paint
 *  android.graphics.Paint$FontMetricsInt
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.icu.text.DecimalFormatSymbols
 *  android.os.Build$VERSION
 *  android.text.Editable
 *  android.text.TextDirectionHeuristic
 *  android.text.TextDirectionHeuristics
 *  android.text.TextPaint
 *  android.text.method.PasswordTransformationMethod
 *  android.util.Log
 *  android.view.ActionMode
 *  android.view.ActionMode$Callback
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.widget.TextView
 */
package androidx.core.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormatSymbols;
import android.os.Build;
import android.text.Editable;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.util.Preconditions;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TintableCompoundDrawablesView;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class TextViewCompat {
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;
    private static final int LINES = 1;
    private static final String LOG_TAG = "TextViewCompat";
    private static Field sMaxModeField;
    private static boolean sMaxModeFieldFetched;
    private static Field sMaximumField;
    private static boolean sMaximumFieldFetched;
    private static Field sMinModeField;
    private static boolean sMinModeFieldFetched;
    private static Field sMinimumField;
    private static boolean sMinimumFieldFetched;

    private TextViewCompat() {
    }

    public static int getAutoSizeMaxTextSize(TextView textView) {
        if (Build.VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeMaxTextSize();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeMaxTextSize();
        }
        return -1;
    }

    public static int getAutoSizeMinTextSize(TextView textView) {
        if (Build.VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeMinTextSize();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeMinTextSize();
        }
        return -1;
    }

    public static int getAutoSizeStepGranularity(TextView textView) {
        if (Build.VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeStepGranularity();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeStepGranularity();
        }
        return -1;
    }

    public static int[] getAutoSizeTextAvailableSizes(TextView textView) {
        if (Build.VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeTextAvailableSizes();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeTextAvailableSizes();
        }
        return new int[0];
    }

    public static int getAutoSizeTextType(TextView textView) {
        if (Build.VERSION.SDK_INT >= 27) {
            return textView.getAutoSizeTextType();
        }
        if (textView instanceof AutoSizeableTextView) {
            return ((AutoSizeableTextView)textView).getAutoSizeTextType();
        }
        return 0;
    }

    public static ColorStateList getCompoundDrawableTintList(TextView textView) {
        Preconditions.checkNotNull(textView);
        if (Build.VERSION.SDK_INT >= 24) {
            return textView.getCompoundDrawableTintList();
        }
        if (textView instanceof TintableCompoundDrawablesView) {
            return ((TintableCompoundDrawablesView)textView).getSupportCompoundDrawablesTintList();
        }
        return null;
    }

    public static PorterDuff.Mode getCompoundDrawableTintMode(TextView textView) {
        Preconditions.checkNotNull(textView);
        if (Build.VERSION.SDK_INT >= 24) {
            return textView.getCompoundDrawableTintMode();
        }
        if (textView instanceof TintableCompoundDrawablesView) {
            return ((TintableCompoundDrawablesView)textView).getSupportCompoundDrawablesTintMode();
        }
        return null;
    }

    public static Drawable[] getCompoundDrawablesRelative(TextView textView) {
        if (Build.VERSION.SDK_INT >= 18) {
            return textView.getCompoundDrawablesRelative();
        }
        if (Build.VERSION.SDK_INT >= 17) {
            int n = textView.getLayoutDirection();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            Drawable[] drawableArray = textView.getCompoundDrawables();
            if (bl) {
                Drawable drawable2 = drawableArray[2];
                textView = drawableArray[0];
                drawableArray[0] = drawable2;
                drawableArray[2] = textView;
            }
            return drawableArray;
        }
        return textView.getCompoundDrawables();
    }

    public static int getFirstBaselineToTopHeight(TextView textView) {
        return textView.getPaddingTop() - textView.getPaint().getFontMetricsInt().top;
    }

    public static int getLastBaselineToBottomHeight(TextView textView) {
        return textView.getPaddingBottom() + textView.getPaint().getFontMetricsInt().bottom;
    }

    public static int getMaxLines(TextView textView) {
        Field field;
        if (Build.VERSION.SDK_INT >= 16) {
            return textView.getMaxLines();
        }
        if (!sMaxModeFieldFetched) {
            sMaxModeField = TextViewCompat.retrieveField("mMaxMode");
            sMaxModeFieldFetched = true;
        }
        if ((field = sMaxModeField) != null && TextViewCompat.retrieveIntFromField(field, textView) == 1) {
            if (!sMaximumFieldFetched) {
                sMaximumField = TextViewCompat.retrieveField("mMaximum");
                sMaximumFieldFetched = true;
            }
            if ((field = sMaximumField) != null) {
                return TextViewCompat.retrieveIntFromField(field, textView);
            }
        }
        return -1;
    }

    public static int getMinLines(TextView textView) {
        Field field;
        if (Build.VERSION.SDK_INT >= 16) {
            return textView.getMinLines();
        }
        if (!sMinModeFieldFetched) {
            sMinModeField = TextViewCompat.retrieveField("mMinMode");
            sMinModeFieldFetched = true;
        }
        if ((field = sMinModeField) != null && TextViewCompat.retrieveIntFromField(field, textView) == 1) {
            if (!sMinimumFieldFetched) {
                sMinimumField = TextViewCompat.retrieveField("mMinimum");
                sMinimumFieldFetched = true;
            }
            if ((field = sMinimumField) != null) {
                return TextViewCompat.retrieveIntFromField(field, textView);
            }
        }
        return -1;
    }

    private static int getTextDirection(TextDirectionHeuristic textDirectionHeuristic) {
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            return 1;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            return 1;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.ANYRTL_LTR) {
            return 2;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.LTR) {
            return 3;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.RTL) {
            return 4;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.LOCALE) {
            return 5;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
            return 6;
        }
        if (textDirectionHeuristic == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
            return 7;
        }
        return 1;
    }

    private static TextDirectionHeuristic getTextDirectionHeuristic(TextView textView) {
        if (textView.getTransformationMethod() instanceof PasswordTransformationMethod) {
            return TextDirectionHeuristics.LTR;
        }
        int n = Build.VERSION.SDK_INT;
        byte by = 0;
        if (n >= 28 && (textView.getInputType() & 0xF) == 3) {
            by = Character.getDirectionality(DecimalFormatSymbols.getInstance((Locale)textView.getTextLocale()).getDigitStrings()[0].codePointAt(0));
            if (by != 1 && by != 2) {
                return TextDirectionHeuristics.LTR;
            }
            return TextDirectionHeuristics.RTL;
        }
        if (textView.getLayoutDirection() == 1) {
            by = 1;
        }
        switch (textView.getTextDirection()) {
            default: {
                textView = by != 0 ? TextDirectionHeuristics.FIRSTSTRONG_RTL : TextDirectionHeuristics.FIRSTSTRONG_LTR;
            }
            case 7: {
                return TextDirectionHeuristics.FIRSTSTRONG_RTL;
            }
            case 6: {
                return TextDirectionHeuristics.FIRSTSTRONG_LTR;
            }
            case 5: {
                return TextDirectionHeuristics.LOCALE;
            }
            case 4: {
                return TextDirectionHeuristics.RTL;
            }
            case 3: {
                return TextDirectionHeuristics.LTR;
            }
            case 2: {
                return TextDirectionHeuristics.ANYRTL_LTR;
            }
        }
        return textView;
    }

    public static PrecomputedTextCompat.Params getTextMetricsParams(TextView textView) {
        if (Build.VERSION.SDK_INT >= 28) {
            return new PrecomputedTextCompat.Params(textView.getTextMetricsParams());
        }
        PrecomputedTextCompat.Params.Builder builder = new PrecomputedTextCompat.Params.Builder(new TextPaint((Paint)textView.getPaint()));
        if (Build.VERSION.SDK_INT >= 23) {
            builder.setBreakStrategy(textView.getBreakStrategy());
            builder.setHyphenationFrequency(textView.getHyphenationFrequency());
        }
        if (Build.VERSION.SDK_INT >= 18) {
            builder.setTextDirection(TextViewCompat.getTextDirectionHeuristic(textView));
        }
        return builder.build();
    }

    private static Field retrieveField(String string2) {
        Field field;
        Field field2 = null;
        field2 = field = TextView.class.getDeclaredField(string2);
        try {
            field.setAccessible(true);
            field2 = field;
        }
        catch (NoSuchFieldException noSuchFieldException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not retrieve ");
            stringBuilder.append(string2);
            stringBuilder.append(" field.");
            Log.e((String)LOG_TAG, (String)stringBuilder.toString());
        }
        return field2;
    }

    private static int retrieveIntFromField(Field field, TextView textView) {
        try {
            int n = field.getInt(textView);
            return n;
        }
        catch (IllegalAccessException illegalAccessException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not retrieve value of ");
            stringBuilder.append(field.getName());
            stringBuilder.append(" field.");
            Log.d((String)LOG_TAG, (String)stringBuilder.toString());
            return -1;
        }
    }

    public static void setAutoSizeTextTypeUniformWithConfiguration(TextView textView, int n, int n2, int n3, int n4) throws IllegalArgumentException {
        if (Build.VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        } else if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithConfiguration(n, n2, n3, n4);
        }
    }

    public static void setAutoSizeTextTypeUniformWithPresetSizes(TextView textView, int[] nArray, int n) throws IllegalArgumentException {
        if (Build.VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeUniformWithPresetSizes(nArray, n);
        } else if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeUniformWithPresetSizes(nArray, n);
        }
    }

    public static void setAutoSizeTextTypeWithDefaults(TextView textView, int n) {
        if (Build.VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeWithDefaults(n);
        } else if (textView instanceof AutoSizeableTextView) {
            ((AutoSizeableTextView)textView).setAutoSizeTextTypeWithDefaults(n);
        }
    }

    public static void setCompoundDrawableTintList(TextView textView, ColorStateList colorStateList) {
        Preconditions.checkNotNull(textView);
        if (Build.VERSION.SDK_INT >= 24) {
            textView.setCompoundDrawableTintList(colorStateList);
        } else if (textView instanceof TintableCompoundDrawablesView) {
            ((TintableCompoundDrawablesView)textView).setSupportCompoundDrawablesTintList(colorStateList);
        }
    }

    public static void setCompoundDrawableTintMode(TextView textView, PorterDuff.Mode mode) {
        Preconditions.checkNotNull(textView);
        if (Build.VERSION.SDK_INT >= 24) {
            textView.setCompoundDrawableTintMode(mode);
        } else if (textView instanceof TintableCompoundDrawablesView) {
            ((TintableCompoundDrawablesView)textView).setSupportCompoundDrawablesTintMode(mode);
        }
    }

    public static void setCompoundDrawablesRelative(TextView textView, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5) {
        if (Build.VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelative(drawable2, drawable3, drawable4, drawable5);
        } else if (Build.VERSION.SDK_INT >= 17) {
            int n = textView.getLayoutDirection();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            Drawable drawable6 = bl ? drawable4 : drawable2;
            if (!bl) {
                drawable2 = drawable4;
            }
            textView.setCompoundDrawables(drawable6, drawable3, drawable2, drawable5);
        } else {
            textView.setCompoundDrawables(drawable2, drawable3, drawable4, drawable5);
        }
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, int n, int n2, int n3, int n4) {
        if (Build.VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(n, n2, n3, n4);
        } else if (Build.VERSION.SDK_INT >= 17) {
            int n5 = textView.getLayoutDirection();
            boolean bl = true;
            if (n5 != 1) {
                bl = false;
            }
            n5 = bl ? n3 : n;
            if (!bl) {
                n = n3;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(n5, n2, n, n4);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(n, n2, n3, n4);
        }
    }

    public static void setCompoundDrawablesRelativeWithIntrinsicBounds(TextView textView, Drawable drawable2, Drawable drawable3, Drawable drawable4, Drawable drawable5) {
        if (Build.VERSION.SDK_INT >= 18) {
            textView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        } else if (Build.VERSION.SDK_INT >= 17) {
            int n = textView.getLayoutDirection();
            boolean bl = true;
            if (n != 1) {
                bl = false;
            }
            Drawable drawable6 = bl ? drawable4 : drawable2;
            if (!bl) {
                drawable2 = drawable4;
            }
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable6, drawable3, drawable2, drawable5);
        } else {
            textView.setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable5);
        }
    }

    public static void setCustomSelectionActionModeCallback(TextView textView, ActionMode.Callback callback) {
        textView.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(textView, callback));
    }

    public static void setFirstBaselineToTopHeight(TextView textView, int n) {
        Preconditions.checkArgumentNonnegative(n);
        if (Build.VERSION.SDK_INT >= 28) {
            textView.setFirstBaselineToTopHeight(n);
            return;
        }
        Paint.FontMetricsInt fontMetricsInt = textView.getPaint().getFontMetricsInt();
        int n2 = Build.VERSION.SDK_INT >= 16 && !textView.getIncludeFontPadding() ? fontMetricsInt.ascent : fontMetricsInt.top;
        if (n > Math.abs(n2)) {
            textView.setPadding(textView.getPaddingLeft(), n + n2, textView.getPaddingRight(), textView.getPaddingBottom());
        }
    }

    public static void setLastBaselineToBottomHeight(TextView textView, int n) {
        Preconditions.checkArgumentNonnegative(n);
        Paint.FontMetricsInt fontMetricsInt = textView.getPaint().getFontMetricsInt();
        int n2 = Build.VERSION.SDK_INT >= 16 && !textView.getIncludeFontPadding() ? fontMetricsInt.descent : fontMetricsInt.bottom;
        if (n > Math.abs(n2)) {
            textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), n - n2);
        }
    }

    public static void setLineHeight(TextView textView, int n) {
        Preconditions.checkArgumentNonnegative(n);
        int n2 = textView.getPaint().getFontMetricsInt(null);
        if (n != n2) {
            textView.setLineSpacing((float)(n - n2), 1.0f);
        }
    }

    public static void setPrecomputedText(TextView textView, PrecomputedTextCompat precomputedTextCompat) {
        block4: {
            block3: {
                block2: {
                    if (Build.VERSION.SDK_INT < 29) break block2;
                    textView.setText((CharSequence)precomputedTextCompat.getPrecomputedText());
                    break block3;
                }
                if (!TextViewCompat.getTextMetricsParams(textView).equalsWithoutTextDirection(precomputedTextCompat.getParams())) break block4;
                textView.setText((CharSequence)((Object)precomputedTextCompat));
            }
            return;
        }
        throw new IllegalArgumentException("Given text can not be applied to TextView.");
    }

    public static void setTextAppearance(TextView textView, int n) {
        if (Build.VERSION.SDK_INT >= 23) {
            textView.setTextAppearance(n);
        } else {
            textView.setTextAppearance(textView.getContext(), n);
        }
    }

    public static void setTextMetricsParams(TextView textView, PrecomputedTextCompat.Params params) {
        if (Build.VERSION.SDK_INT >= 18) {
            textView.setTextDirection(TextViewCompat.getTextDirection(params.getTextDirection()));
        }
        if (Build.VERSION.SDK_INT < 23) {
            float f = params.getTextPaint().getTextScaleX();
            textView.getPaint().set(params.getTextPaint());
            if (f == textView.getTextScaleX()) {
                textView.setTextScaleX(f / 2.0f + 1.0f);
            }
            textView.setTextScaleX(f);
        } else {
            textView.getPaint().set(params.getTextPaint());
            textView.setBreakStrategy(params.getBreakStrategy());
            textView.setHyphenationFrequency(params.getHyphenationFrequency());
        }
    }

    public static ActionMode.Callback wrapCustomSelectionActionModeCallback(TextView textView, ActionMode.Callback callback) {
        if (Build.VERSION.SDK_INT >= 26 && Build.VERSION.SDK_INT <= 27 && !(callback instanceof OreoCallback)) {
            return new OreoCallback(callback, textView);
        }
        return callback;
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface AutoSizeTextType {
    }

    private static class OreoCallback
    implements ActionMode.Callback {
        private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;
        private final ActionMode.Callback mCallback;
        private boolean mCanUseMenuBuilderReferences;
        private boolean mInitializedMenuBuilderReferences;
        private Class<?> mMenuBuilderClass;
        private Method mMenuBuilderRemoveItemAtMethod;
        private final TextView mTextView;

        OreoCallback(ActionMode.Callback callback, TextView textView) {
            this.mCallback = callback;
            this.mTextView = textView;
            this.mInitializedMenuBuilderReferences = false;
        }

        private Intent createProcessTextIntent() {
            return new Intent().setAction("android.intent.action.PROCESS_TEXT").setType("text/plain");
        }

        private Intent createProcessTextIntentForResolveInfo(ResolveInfo resolveInfo, TextView textView) {
            return this.createProcessTextIntent().putExtra("android.intent.extra.PROCESS_TEXT_READONLY", this.isEditable(textView) ^ true).setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
        }

        private List<ResolveInfo> getSupportedActivities(Context context, PackageManager object) {
            ArrayList<ResolveInfo> arrayList = new ArrayList<ResolveInfo>();
            if (!(context instanceof Activity)) {
                return arrayList;
            }
            for (ResolveInfo resolveInfo : object.queryIntentActivities(this.createProcessTextIntent(), 0)) {
                if (!this.isSupportedActivity(resolveInfo, context)) continue;
                arrayList.add(resolveInfo);
            }
            return arrayList;
        }

        private boolean isEditable(TextView textView) {
            boolean bl = textView instanceof Editable && textView.onCheckIsTextEditor() && textView.isEnabled();
            return bl;
        }

        private boolean isSupportedActivity(ResolveInfo resolveInfo, Context context) {
            boolean bl;
            block2: {
                boolean bl2 = context.getPackageName().equals(resolveInfo.activityInfo.packageName);
                bl = true;
                if (bl2) {
                    return true;
                }
                if (!resolveInfo.activityInfo.exported) {
                    return false;
                }
                if (resolveInfo.activityInfo.permission == null || context.checkSelfPermission(resolveInfo.activityInfo.permission) == 0) break block2;
                bl = false;
            }
            return bl;
        }

        private void recomputeProcessTextMenuItems(Menu menu) {
            block12: {
                int n;
                Object object;
                PackageManager packageManager;
                Context context;
                block11: {
                    context = this.mTextView.getContext();
                    packageManager = context.getPackageManager();
                    if (!this.mInitializedMenuBuilderReferences) {
                        this.mInitializedMenuBuilderReferences = true;
                        try {
                            object = Class.forName("com.android.internal.view.menu.MenuBuilder");
                            this.mMenuBuilderClass = object;
                            this.mMenuBuilderRemoveItemAtMethod = ((Class)object).getDeclaredMethod("removeItemAt", Integer.TYPE);
                            this.mCanUseMenuBuilderReferences = true;
                            break block11;
                        }
                        catch (NoSuchMethodException noSuchMethodException) {
                        }
                        catch (ClassNotFoundException classNotFoundException) {
                            // empty catch block
                        }
                        this.mMenuBuilderClass = null;
                        this.mMenuBuilderRemoveItemAtMethod = null;
                        this.mCanUseMenuBuilderReferences = false;
                    }
                }
                try {
                    object = this.mCanUseMenuBuilderReferences && this.mMenuBuilderClass.isInstance(menu) ? this.mMenuBuilderRemoveItemAtMethod : menu.getClass().getDeclaredMethod("removeItemAt", Integer.TYPE);
                }
                catch (InvocationTargetException invocationTargetException) {
                    break block12;
                }
                catch (IllegalAccessException illegalAccessException) {
                    break block12;
                }
                catch (NoSuchMethodException noSuchMethodException) {
                    // empty catch block
                }
                for (n = menu.size() - 1; n >= 0; --n) {
                    MenuItem menuItem = menu.getItem(n);
                    if (menuItem.getIntent() == null || !"android.intent.action.PROCESS_TEXT".equals(menuItem.getIntent().getAction())) continue;
                    ((Method)object).invoke((Object)menu, n);
                    continue;
                }
                object = this.getSupportedActivities(context, packageManager);
                for (n = 0; n < object.size(); ++n) {
                    context = (ResolveInfo)object.get(n);
                    menu.add(0, 0, n + 100, context.loadLabel(packageManager)).setIntent(this.createProcessTextIntentForResolveInfo((ResolveInfo)context, this.mTextView)).setShowAsAction(1);
                }
                return;
            }
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            return this.mCallback.onActionItemClicked(actionMode, menuItem);
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            return this.mCallback.onCreateActionMode(actionMode, menu);
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            this.mCallback.onDestroyActionMode(actionMode);
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            this.recomputeProcessTextMenuItems(menu);
            return this.mCallback.onPrepareActionMode(actionMode, menu);
        }
    }
}

