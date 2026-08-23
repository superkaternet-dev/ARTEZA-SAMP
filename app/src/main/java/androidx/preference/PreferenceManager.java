/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.text.TextUtils
 */
package androidx.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import androidx.preference.DropDownPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceInflater;
import androidx.preference.PreferenceScreen;
import androidx.preference.TwoStatePreference;

public class PreferenceManager {
    public static final String KEY_HAS_SET_DEFAULT_VALUES = "_has_set_default_values";
    private static final int STORAGE_DEFAULT = 0;
    private static final int STORAGE_DEVICE_PROTECTED = 1;
    private Context mContext;
    private SharedPreferences.Editor mEditor;
    private long mNextId = 0L;
    private boolean mNoCommit;
    private OnDisplayPreferenceDialogListener mOnDisplayPreferenceDialogListener;
    private OnNavigateToScreenListener mOnNavigateToScreenListener;
    private OnPreferenceTreeClickListener mOnPreferenceTreeClickListener;
    private PreferenceComparisonCallback mPreferenceComparisonCallback;
    private PreferenceDataStore mPreferenceDataStore;
    private PreferenceScreen mPreferenceScreen;
    private SharedPreferences mSharedPreferences;
    private int mSharedPreferencesMode;
    private String mSharedPreferencesName;
    private int mStorage = 0;

    public PreferenceManager(Context context) {
        this.mContext = context;
        this.setSharedPreferencesName(PreferenceManager.getDefaultSharedPreferencesName(context));
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(PreferenceManager.getDefaultSharedPreferencesName(context), PreferenceManager.getDefaultSharedPreferencesMode());
    }

    private static int getDefaultSharedPreferencesMode() {
        return 0;
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getPackageName());
        stringBuilder.append("_preferences");
        return stringBuilder.toString();
    }

    public static void setDefaultValues(Context context, int n, boolean bl) {
        PreferenceManager.setDefaultValues(context, PreferenceManager.getDefaultSharedPreferencesName(context), PreferenceManager.getDefaultSharedPreferencesMode(), n, bl);
    }

    public static void setDefaultValues(Context context, String string2, int n, int n2, boolean bl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(KEY_HAS_SET_DEFAULT_VALUES, 0);
        if (bl || !sharedPreferences.getBoolean(KEY_HAS_SET_DEFAULT_VALUES, false)) {
            PreferenceManager preferenceManager = new PreferenceManager(context);
            preferenceManager.setSharedPreferencesName(string2);
            preferenceManager.setSharedPreferencesMode(n);
            preferenceManager.inflateFromResource(context, n2, null);
            sharedPreferences.edit().putBoolean(KEY_HAS_SET_DEFAULT_VALUES, true).apply();
        }
    }

    private void setNoCommit(boolean bl) {
        SharedPreferences.Editor editor;
        if (!bl && (editor = this.mEditor) != null) {
            editor.apply();
        }
        this.mNoCommit = bl;
    }

    public PreferenceScreen createPreferenceScreen(Context object) {
        object = new PreferenceScreen((Context)object, null);
        ((Preference)object).onAttachedToHierarchy(this);
        return object;
    }

    public <T extends Preference> T findPreference(CharSequence charSequence) {
        PreferenceScreen preferenceScreen = this.mPreferenceScreen;
        if (preferenceScreen == null) {
            return null;
        }
        return preferenceScreen.findPreference(charSequence);
    }

    public Context getContext() {
        return this.mContext;
    }

    SharedPreferences.Editor getEditor() {
        if (this.mPreferenceDataStore != null) {
            return null;
        }
        if (this.mNoCommit) {
            if (this.mEditor == null) {
                this.mEditor = this.getSharedPreferences().edit();
            }
            return this.mEditor;
        }
        return this.getSharedPreferences().edit();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    long getNextId() {
        synchronized (this) {
            long l = this.mNextId;
            this.mNextId = 1L + l;
            return l;
        }
    }

    public OnDisplayPreferenceDialogListener getOnDisplayPreferenceDialogListener() {
        return this.mOnDisplayPreferenceDialogListener;
    }

    public OnNavigateToScreenListener getOnNavigateToScreenListener() {
        return this.mOnNavigateToScreenListener;
    }

    public OnPreferenceTreeClickListener getOnPreferenceTreeClickListener() {
        return this.mOnPreferenceTreeClickListener;
    }

    public PreferenceComparisonCallback getPreferenceComparisonCallback() {
        return this.mPreferenceComparisonCallback;
    }

    public PreferenceDataStore getPreferenceDataStore() {
        return this.mPreferenceDataStore;
    }

    public PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceScreen;
    }

    public SharedPreferences getSharedPreferences() {
        if (this.getPreferenceDataStore() != null) {
            return null;
        }
        if (this.mSharedPreferences == null) {
            Context context;
            switch (this.mStorage) {
                default: {
                    context = this.mContext;
                    break;
                }
                case 1: {
                    context = ContextCompat.createDeviceProtectedStorageContext(this.mContext);
                }
            }
            this.mSharedPreferences = context.getSharedPreferences(this.mSharedPreferencesName, this.mSharedPreferencesMode);
        }
        return this.mSharedPreferences;
    }

    public int getSharedPreferencesMode() {
        return this.mSharedPreferencesMode;
    }

    public String getSharedPreferencesName() {
        return this.mSharedPreferencesName;
    }

    public PreferenceScreen inflateFromResource(Context object, int n, PreferenceScreen preferenceScreen) {
        this.setNoCommit(true);
        object = (PreferenceScreen)new PreferenceInflater((Context)object, this).inflate(n, (PreferenceGroup)preferenceScreen);
        ((Preference)object).onAttachedToHierarchy(this);
        this.setNoCommit(false);
        return object;
    }

    public boolean isStorageDefault() {
        int n = Build.VERSION.SDK_INT;
        boolean bl = true;
        if (n >= 24) {
            if (this.mStorage != 0) {
                bl = false;
            }
            return bl;
        }
        return true;
    }

    public boolean isStorageDeviceProtected() {
        int n = Build.VERSION.SDK_INT;
        boolean bl = false;
        if (n >= 24) {
            if (this.mStorage == 1) {
                bl = true;
            }
            return bl;
        }
        return false;
    }

    public void setOnDisplayPreferenceDialogListener(OnDisplayPreferenceDialogListener onDisplayPreferenceDialogListener) {
        this.mOnDisplayPreferenceDialogListener = onDisplayPreferenceDialogListener;
    }

    public void setOnNavigateToScreenListener(OnNavigateToScreenListener onNavigateToScreenListener) {
        this.mOnNavigateToScreenListener = onNavigateToScreenListener;
    }

    public void setOnPreferenceTreeClickListener(OnPreferenceTreeClickListener onPreferenceTreeClickListener) {
        this.mOnPreferenceTreeClickListener = onPreferenceTreeClickListener;
    }

    public void setPreferenceComparisonCallback(PreferenceComparisonCallback preferenceComparisonCallback) {
        this.mPreferenceComparisonCallback = preferenceComparisonCallback;
    }

    public void setPreferenceDataStore(PreferenceDataStore preferenceDataStore) {
        this.mPreferenceDataStore = preferenceDataStore;
    }

    public boolean setPreferences(PreferenceScreen preferenceScreen) {
        PreferenceScreen preferenceScreen2 = this.mPreferenceScreen;
        if (preferenceScreen != preferenceScreen2) {
            if (preferenceScreen2 != null) {
                preferenceScreen2.onDetached();
            }
            this.mPreferenceScreen = preferenceScreen;
            return true;
        }
        return false;
    }

    public void setSharedPreferencesMode(int n) {
        this.mSharedPreferencesMode = n;
        this.mSharedPreferences = null;
    }

    public void setSharedPreferencesName(String string2) {
        this.mSharedPreferencesName = string2;
        this.mSharedPreferences = null;
    }

    public void setStorageDefault() {
        if (Build.VERSION.SDK_INT >= 24) {
            this.mStorage = 0;
            this.mSharedPreferences = null;
        }
    }

    public void setStorageDeviceProtected() {
        if (Build.VERSION.SDK_INT >= 24) {
            this.mStorage = 1;
            this.mSharedPreferences = null;
        }
    }

    boolean shouldCommit() {
        return this.mNoCommit ^ true;
    }

    public void showDialog(Preference preference) {
        OnDisplayPreferenceDialogListener onDisplayPreferenceDialogListener = this.mOnDisplayPreferenceDialogListener;
        if (onDisplayPreferenceDialogListener != null) {
            onDisplayPreferenceDialogListener.onDisplayPreferenceDialog(preference);
        }
    }

    public static interface OnDisplayPreferenceDialogListener {
        public void onDisplayPreferenceDialog(Preference var1);
    }

    public static interface OnNavigateToScreenListener {
        public void onNavigateToScreen(PreferenceScreen var1);
    }

    public static interface OnPreferenceTreeClickListener {
        public boolean onPreferenceTreeClick(Preference var1);
    }

    public static abstract class PreferenceComparisonCallback {
        public abstract boolean arePreferenceContentsTheSame(Preference var1, Preference var2);

        public abstract boolean arePreferenceItemsTheSame(Preference var1, Preference var2);
    }

    public static class SimplePreferenceComparisonCallback
    extends PreferenceComparisonCallback {
        @Override
        public boolean arePreferenceContentsTheSame(Preference preference, Preference preference2) {
            Drawable drawable2;
            if (preference.getClass() != preference2.getClass()) {
                return false;
            }
            if (preference == preference2 && preference.wasDetached()) {
                return false;
            }
            if (!TextUtils.equals((CharSequence)preference.getTitle(), (CharSequence)preference2.getTitle())) {
                return false;
            }
            if (!TextUtils.equals((CharSequence)preference.getSummary(), (CharSequence)preference2.getSummary())) {
                return false;
            }
            Drawable drawable3 = preference.getIcon();
            if (!(drawable3 == (drawable2 = preference2.getIcon()) || drawable3 != null && drawable3.equals(drawable2))) {
                return false;
            }
            if (preference.isEnabled() != preference2.isEnabled()) {
                return false;
            }
            if (preference.isSelectable() != preference2.isSelectable()) {
                return false;
            }
            if (preference instanceof TwoStatePreference && ((TwoStatePreference)preference).isChecked() != ((TwoStatePreference)preference2).isChecked()) {
                return false;
            }
            return !(preference instanceof DropDownPreference) || preference == preference2;
        }

        @Override
        public boolean arePreferenceItemsTheSame(Preference preference, Preference preference2) {
            boolean bl = preference.getId() == preference2.getId();
            return bl;
        }
    }
}

