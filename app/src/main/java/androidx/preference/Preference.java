/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ClipData
 *  android.content.ClipboardManager
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  android.util.AttributeSet
 *  android.view.AbsSavedState
 *  android.view.ContextMenu
 *  android.view.ContextMenu$ContextMenuInfo
 *  android.view.MenuItem
 *  android.view.MenuItem$OnMenuItemClickListener
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.View$OnCreateContextMenuListener
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  android.widget.TextView
 *  android.widget.Toast
 */
package androidx.preference;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.AbsSavedState;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Preference
implements Comparable<Preference> {
    private static final String CLIPBOARD_ID = "Preference";
    public static final int DEFAULT_ORDER = Integer.MAX_VALUE;
    private boolean mAllowDividerAbove = true;
    private boolean mAllowDividerBelow = true;
    private boolean mBaseMethodCalled;
    private final View.OnClickListener mClickListener;
    private Context mContext;
    private boolean mCopyingEnabled;
    private Object mDefaultValue;
    private String mDependencyKey;
    private boolean mDependencyMet = true;
    private List<Preference> mDependents;
    private boolean mEnabled = true;
    private Bundle mExtras;
    private String mFragment;
    private boolean mHasId;
    private boolean mHasSingleLineTitleAttr;
    private Drawable mIcon;
    private int mIconResId;
    private boolean mIconSpaceReserved;
    private long mId;
    private Intent mIntent;
    private String mKey;
    private int mLayoutResId;
    private OnPreferenceChangeInternalListener mListener;
    private OnPreferenceChangeListener mOnChangeListener;
    private OnPreferenceClickListener mOnClickListener;
    private OnPreferenceCopyListener mOnCopyListener;
    private int mOrder = Integer.MAX_VALUE;
    private boolean mParentDependencyMet = true;
    private PreferenceGroup mParentGroup;
    private boolean mPersistent = true;
    private PreferenceDataStore mPreferenceDataStore;
    private PreferenceManager mPreferenceManager;
    private boolean mRequiresKey;
    private boolean mSelectable = true;
    private boolean mShouldDisableView = true;
    private boolean mSingleLineTitle = true;
    private CharSequence mSummary;
    private SummaryProvider mSummaryProvider;
    private CharSequence mTitle;
    private int mViewId = 0;
    private boolean mVisible = true;
    private boolean mWasDetached;
    private int mWidgetLayoutResId;

    public Preference(Context context) {
        this(context, null);
    }

    public Preference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, TypedArrayUtils.getAttr(context, R.attr.preferenceStyle, 16842894));
    }

    public Preference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public Preference(Context context, AttributeSet attributeSet, int n, int n2) {
        boolean bl;
        this.mLayoutResId = R.layout.preference;
        this.mClickListener = new View.OnClickListener(this){
            final Preference this$0;
            {
                this.this$0 = preference;
            }

            public void onClick(View view) {
                this.this$0.performClick(view);
            }
        };
        this.mContext = context;
        context = context.obtainStyledAttributes(attributeSet, R.styleable.Preference, n, n2);
        this.mIconResId = TypedArrayUtils.getResourceId((TypedArray)context, R.styleable.Preference_icon, R.styleable.Preference_android_icon, 0);
        this.mKey = TypedArrayUtils.getString((TypedArray)context, R.styleable.Preference_key, R.styleable.Preference_android_key);
        this.mTitle = TypedArrayUtils.getText((TypedArray)context, R.styleable.Preference_title, R.styleable.Preference_android_title);
        this.mSummary = TypedArrayUtils.getText((TypedArray)context, R.styleable.Preference_summary, R.styleable.Preference_android_summary);
        this.mOrder = TypedArrayUtils.getInt((TypedArray)context, R.styleable.Preference_order, R.styleable.Preference_android_order, Integer.MAX_VALUE);
        this.mFragment = TypedArrayUtils.getString((TypedArray)context, R.styleable.Preference_fragment, R.styleable.Preference_android_fragment);
        this.mLayoutResId = TypedArrayUtils.getResourceId((TypedArray)context, R.styleable.Preference_layout, R.styleable.Preference_android_layout, R.layout.preference);
        this.mWidgetLayoutResId = TypedArrayUtils.getResourceId((TypedArray)context, R.styleable.Preference_widgetLayout, R.styleable.Preference_android_widgetLayout, 0);
        this.mEnabled = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_enabled, R.styleable.Preference_android_enabled, true);
        this.mSelectable = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_selectable, R.styleable.Preference_android_selectable, true);
        this.mPersistent = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_persistent, R.styleable.Preference_android_persistent, true);
        this.mDependencyKey = TypedArrayUtils.getString((TypedArray)context, R.styleable.Preference_dependency, R.styleable.Preference_android_dependency);
        this.mAllowDividerAbove = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_allowDividerAbove, R.styleable.Preference_allowDividerAbove, this.mSelectable);
        this.mAllowDividerBelow = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_allowDividerBelow, R.styleable.Preference_allowDividerBelow, this.mSelectable);
        if (context.hasValue(R.styleable.Preference_defaultValue)) {
            this.mDefaultValue = this.onGetDefaultValue((TypedArray)context, R.styleable.Preference_defaultValue);
        } else if (context.hasValue(R.styleable.Preference_android_defaultValue)) {
            this.mDefaultValue = this.onGetDefaultValue((TypedArray)context, R.styleable.Preference_android_defaultValue);
        }
        this.mShouldDisableView = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_shouldDisableView, R.styleable.Preference_android_shouldDisableView, true);
        this.mHasSingleLineTitleAttr = bl = context.hasValue(R.styleable.Preference_singleLineTitle);
        if (bl) {
            this.mSingleLineTitle = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_singleLineTitle, R.styleable.Preference_android_singleLineTitle, true);
        }
        this.mIconSpaceReserved = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_iconSpaceReserved, R.styleable.Preference_android_iconSpaceReserved, false);
        this.mVisible = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_isPreferenceVisible, R.styleable.Preference_isPreferenceVisible, true);
        this.mCopyingEnabled = TypedArrayUtils.getBoolean((TypedArray)context, R.styleable.Preference_enableCopying, R.styleable.Preference_enableCopying, false);
        context.recycle();
    }

    private void dispatchSetInitialValue() {
        if (this.getPreferenceDataStore() != null) {
            this.onSetInitialValue(true, this.mDefaultValue);
            return;
        }
        if (this.shouldPersist() && this.getSharedPreferences().contains(this.mKey)) {
            this.onSetInitialValue(true, null);
        } else {
            Object object = this.mDefaultValue;
            if (object != null) {
                this.onSetInitialValue(false, object);
            }
        }
    }

    private void registerDependency() {
        if (TextUtils.isEmpty((CharSequence)this.mDependencyKey)) {
            return;
        }
        Object object = this.findPreferenceInHierarchy(this.mDependencyKey);
        if (object != null) {
            super.registerDependent(this);
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Dependency \"");
        ((StringBuilder)object).append(this.mDependencyKey);
        ((StringBuilder)object).append("\" not found for preference \"");
        ((StringBuilder)object).append(this.mKey);
        ((StringBuilder)object).append("\" (title: \"");
        ((StringBuilder)object).append((Object)this.mTitle);
        ((StringBuilder)object).append("\"");
        throw new IllegalStateException(((StringBuilder)object).toString());
    }

    private void registerDependent(Preference preference) {
        if (this.mDependents == null) {
            this.mDependents = new ArrayList<Preference>();
        }
        this.mDependents.add(preference);
        preference.onDependencyChanged(this, this.shouldDisableDependents());
    }

    private void setEnabledStateOnViews(View view, boolean bl) {
        view.setEnabled(bl);
        if (view instanceof ViewGroup) {
            view = (ViewGroup)view;
            for (int i = view.getChildCount() - 1; i >= 0; --i) {
                this.setEnabledStateOnViews(view.getChildAt(i), bl);
            }
        }
    }

    private void tryCommit(SharedPreferences.Editor editor) {
        if (this.mPreferenceManager.shouldCommit()) {
            editor.apply();
        }
    }

    private void unregisterDependency() {
        String string2 = this.mDependencyKey;
        if (string2 != null && (string2 = this.findPreferenceInHierarchy(string2)) != null) {
            super.unregisterDependent(this);
        }
    }

    private void unregisterDependent(Preference preference) {
        List<Preference> list = this.mDependents;
        if (list != null) {
            list.remove(preference);
        }
    }

    void assignParent(PreferenceGroup preferenceGroup) {
        if (preferenceGroup != null && this.mParentGroup != null) {
            throw new IllegalStateException("This preference already has a parent. You must remove the existing parent before assigning a new one.");
        }
        this.mParentGroup = preferenceGroup;
    }

    public boolean callChangeListener(Object object) {
        OnPreferenceChangeListener onPreferenceChangeListener = this.mOnChangeListener;
        boolean bl = onPreferenceChangeListener == null || onPreferenceChangeListener.onPreferenceChange(this, object);
        return bl;
    }

    final void clearWasDetached() {
        this.mWasDetached = false;
    }

    @Override
    public int compareTo(Preference preference) {
        int n = this.mOrder;
        int n2 = preference.mOrder;
        if (n != n2) {
            return n - n2;
        }
        CharSequence charSequence = this.mTitle;
        CharSequence charSequence2 = preference.mTitle;
        if (charSequence == charSequence2) {
            return 0;
        }
        if (charSequence == null) {
            return 1;
        }
        if (charSequence2 == null) {
            return -1;
        }
        return charSequence.toString().compareToIgnoreCase(preference.mTitle.toString());
    }

    void dispatchRestoreInstanceState(Bundle bundle) {
        if (this.hasKey() && (bundle = bundle.getParcelable(this.mKey)) != null) {
            this.mBaseMethodCalled = false;
            this.onRestoreInstanceState((Parcelable)bundle);
            if (!this.mBaseMethodCalled) {
                throw new IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
            }
        }
    }

    void dispatchSaveInstanceState(Bundle bundle) {
        if (this.hasKey()) {
            this.mBaseMethodCalled = false;
            Parcelable parcelable = this.onSaveInstanceState();
            if (this.mBaseMethodCalled) {
                if (parcelable != null) {
                    bundle.putParcelable(this.mKey, parcelable);
                }
            } else {
                throw new IllegalStateException("Derived class did not call super.onSaveInstanceState()");
            }
        }
    }

    protected <T extends Preference> T findPreferenceInHierarchy(String string2) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager == null) {
            return null;
        }
        return preferenceManager.findPreference(string2);
    }

    public Context getContext() {
        return this.mContext;
    }

    public String getDependency() {
        return this.mDependencyKey;
    }

    public Bundle getExtras() {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        return this.mExtras;
    }

    StringBuilder getFilterableStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        CharSequence charSequence = this.getTitle();
        if (!TextUtils.isEmpty((CharSequence)charSequence)) {
            stringBuilder.append(charSequence);
            stringBuilder.append(' ');
        }
        if (!TextUtils.isEmpty((CharSequence)(charSequence = this.getSummary()))) {
            stringBuilder.append(charSequence);
            stringBuilder.append(' ');
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.setLength(stringBuilder.length() - 1);
        }
        return stringBuilder;
    }

    public String getFragment() {
        return this.mFragment;
    }

    public Drawable getIcon() {
        int n;
        if (this.mIcon == null && (n = this.mIconResId) != 0) {
            this.mIcon = AppCompatResources.getDrawable(this.mContext, n);
        }
        return this.mIcon;
    }

    long getId() {
        return this.mId;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public String getKey() {
        return this.mKey;
    }

    public final int getLayoutResource() {
        return this.mLayoutResId;
    }

    public OnPreferenceChangeListener getOnPreferenceChangeListener() {
        return this.mOnChangeListener;
    }

    public OnPreferenceClickListener getOnPreferenceClickListener() {
        return this.mOnClickListener;
    }

    public int getOrder() {
        return this.mOrder;
    }

    public PreferenceGroup getParent() {
        return this.mParentGroup;
    }

    protected boolean getPersistedBoolean(boolean bl) {
        if (!this.shouldPersist()) {
            return bl;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            return preferenceDataStore.getBoolean(this.mKey, bl);
        }
        return this.mPreferenceManager.getSharedPreferences().getBoolean(this.mKey, bl);
    }

    protected float getPersistedFloat(float f) {
        if (!this.shouldPersist()) {
            return f;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            return preferenceDataStore.getFloat(this.mKey, f);
        }
        return this.mPreferenceManager.getSharedPreferences().getFloat(this.mKey, f);
    }

    protected int getPersistedInt(int n) {
        if (!this.shouldPersist()) {
            return n;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            return preferenceDataStore.getInt(this.mKey, n);
        }
        return this.mPreferenceManager.getSharedPreferences().getInt(this.mKey, n);
    }

    protected long getPersistedLong(long l) {
        if (!this.shouldPersist()) {
            return l;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            return preferenceDataStore.getLong(this.mKey, l);
        }
        return this.mPreferenceManager.getSharedPreferences().getLong(this.mKey, l);
    }

    protected String getPersistedString(String string2) {
        if (!this.shouldPersist()) {
            return string2;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            return preferenceDataStore.getString(this.mKey, string2);
        }
        return this.mPreferenceManager.getSharedPreferences().getString(this.mKey, string2);
    }

    public Set<String> getPersistedStringSet(Set<String> set) {
        if (!this.shouldPersist()) {
            return set;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            return preferenceDataStore.getStringSet(this.mKey, set);
        }
        return this.mPreferenceManager.getSharedPreferences().getStringSet(this.mKey, set);
    }

    public PreferenceDataStore getPreferenceDataStore() {
        Object object = this.mPreferenceDataStore;
        if (object != null) {
            return object;
        }
        object = this.mPreferenceManager;
        if (object != null) {
            return ((PreferenceManager)object).getPreferenceDataStore();
        }
        return null;
    }

    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    public SharedPreferences getSharedPreferences() {
        if (this.mPreferenceManager != null && this.getPreferenceDataStore() == null) {
            return this.mPreferenceManager.getSharedPreferences();
        }
        return null;
    }

    public boolean getShouldDisableView() {
        return this.mShouldDisableView;
    }

    public CharSequence getSummary() {
        if (this.getSummaryProvider() != null) {
            return this.getSummaryProvider().provideSummary(this);
        }
        return this.mSummary;
    }

    public final SummaryProvider getSummaryProvider() {
        return this.mSummaryProvider;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public final int getWidgetLayoutResource() {
        return this.mWidgetLayoutResId;
    }

    public boolean hasKey() {
        return TextUtils.isEmpty((CharSequence)this.mKey) ^ true;
    }

    public boolean isCopyingEnabled() {
        return this.mCopyingEnabled;
    }

    public boolean isEnabled() {
        boolean bl = this.mEnabled && this.mDependencyMet && this.mParentDependencyMet;
        return bl;
    }

    public boolean isIconSpaceReserved() {
        return this.mIconSpaceReserved;
    }

    public boolean isPersistent() {
        return this.mPersistent;
    }

    public boolean isSelectable() {
        return this.mSelectable;
    }

    public final boolean isShown() {
        if (!this.isVisible()) {
            return false;
        }
        if (this.getPreferenceManager() == null) {
            return false;
        }
        if (this == this.getPreferenceManager().getPreferenceScreen()) {
            return true;
        }
        PreferenceGroup preferenceGroup = this.getParent();
        if (preferenceGroup == null) {
            return false;
        }
        return preferenceGroup.isShown();
    }

    public boolean isSingleLineTitle() {
        return this.mSingleLineTitle;
    }

    public final boolean isVisible() {
        return this.mVisible;
    }

    protected void notifyChanged() {
        OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = this.mListener;
        if (onPreferenceChangeInternalListener != null) {
            onPreferenceChangeInternalListener.onPreferenceChange(this);
        }
    }

    public void notifyDependencyChange(boolean bl) {
        List<Preference> list = this.mDependents;
        if (list == null) {
            return;
        }
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            list.get(i).onDependencyChanged(this, bl);
        }
    }

    protected void notifyHierarchyChanged() {
        OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = this.mListener;
        if (onPreferenceChangeInternalListener != null) {
            onPreferenceChangeInternalListener.onPreferenceHierarchyChange(this);
        }
    }

    public void onAttached() {
        this.registerDependency();
    }

    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        this.mPreferenceManager = preferenceManager;
        if (!this.mHasId) {
            this.mId = preferenceManager.getNextId();
        }
        this.dispatchSetInitialValue();
    }

    protected void onAttachedToHierarchy(PreferenceManager preferenceManager, long l) {
        this.mId = l;
        this.mHasId = true;
        try {
            this.onAttachedToHierarchy(preferenceManager);
            return;
        }
        finally {
            this.mHasId = false;
        }
    }

    public void onBindViewHolder(PreferenceViewHolder object) {
        int n;
        View view = ((PreferenceViewHolder)object).itemView;
        CharSequence charSequence = null;
        view.setOnClickListener(this.mClickListener);
        view.setId(this.mViewId);
        Object object2 = (TextView)((PreferenceViewHolder)object).findViewById(0x1020010);
        int n2 = 8;
        Object object3 = charSequence;
        if (object2 != null) {
            object3 = this.getSummary();
            if (!TextUtils.isEmpty((CharSequence)object3)) {
                object2.setText((CharSequence)object3);
                object2.setVisibility(0);
                object3 = object2.getCurrentTextColor();
            } else {
                object2.setVisibility(8);
                object3 = charSequence;
            }
        }
        if ((charSequence = (TextView)((PreferenceViewHolder)object).findViewById(16908310)) != null) {
            object2 = this.getTitle();
            if (!TextUtils.isEmpty((CharSequence)object2)) {
                charSequence.setText((CharSequence)object2);
                charSequence.setVisibility(0);
                if (this.mHasSingleLineTitleAttr) {
                    charSequence.setSingleLine(this.mSingleLineTitle);
                }
                if (!this.isSelectable() && this.isEnabled() && object3 != null) {
                    charSequence.setTextColor((Integer)object3);
                }
            } else {
                charSequence.setVisibility(8);
            }
        }
        if ((object3 = (ImageView)((PreferenceViewHolder)object).findViewById(16908294)) != null) {
            n = this.mIconResId;
            if (n != 0 || this.mIcon != null) {
                if (this.mIcon == null) {
                    this.mIcon = AppCompatResources.getDrawable(this.mContext, n);
                }
                if ((charSequence = this.mIcon) != null) {
                    object3.setImageDrawable((Drawable)charSequence);
                }
            }
            if (this.mIcon != null) {
                object3.setVisibility(0);
            } else {
                n = this.mIconSpaceReserved ? 4 : 8;
                object3.setVisibility(n);
            }
        }
        charSequence = ((PreferenceViewHolder)object).findViewById(R.id.icon_frame);
        object3 = charSequence;
        if (charSequence == null) {
            object3 = ((PreferenceViewHolder)object).findViewById(16908350);
        }
        if (object3 != null) {
            if (this.mIcon != null) {
                object3.setVisibility(0);
            } else {
                n = n2;
                if (this.mIconSpaceReserved) {
                    n = 4;
                }
                object3.setVisibility(n);
            }
        }
        if (this.mShouldDisableView) {
            this.setEnabledStateOnViews(view, this.isEnabled());
        } else {
            this.setEnabledStateOnViews(view, true);
        }
        boolean bl = this.isSelectable();
        view.setFocusable(bl);
        view.setClickable(bl);
        ((PreferenceViewHolder)object).setDividerAllowedAbove(this.mAllowDividerAbove);
        ((PreferenceViewHolder)object).setDividerAllowedBelow(this.mAllowDividerBelow);
        boolean bl2 = this.isCopyingEnabled();
        if (bl2 && this.mOnCopyListener == null) {
            this.mOnCopyListener = new OnPreferenceCopyListener(this);
        }
        object = bl2 ? this.mOnCopyListener : null;
        view.setOnCreateContextMenuListener((View.OnCreateContextMenuListener)object);
        view.setLongClickable(bl2);
        if (bl2 && !bl) {
            ViewCompat.setBackground(view, null);
        }
    }

    protected void onClick() {
    }

    public void onDependencyChanged(Preference preference, boolean bl) {
        if (this.mDependencyMet == bl) {
            this.mDependencyMet = bl ^ true;
            this.notifyDependencyChange(this.shouldDisableDependents());
            this.notifyChanged();
        }
    }

    public void onDetached() {
        this.unregisterDependency();
        this.mWasDetached = true;
    }

    protected Object onGetDefaultValue(TypedArray typedArray, int n) {
        return null;
    }

    @Deprecated
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
    }

    public void onParentChanged(Preference preference, boolean bl) {
        if (this.mParentDependencyMet == bl) {
            this.mParentDependencyMet = bl ^ true;
            this.notifyDependencyChange(this.shouldDisableDependents());
            this.notifyChanged();
        }
    }

    protected void onPrepareForRemoval() {
        this.unregisterDependency();
    }

    protected void onRestoreInstanceState(Parcelable parcelable) {
        this.mBaseMethodCalled = true;
        if (parcelable != BaseSavedState.EMPTY_STATE && parcelable != null) {
            throw new IllegalArgumentException("Wrong state class -- expecting Preference State");
        }
    }

    protected Parcelable onSaveInstanceState() {
        this.mBaseMethodCalled = true;
        return BaseSavedState.EMPTY_STATE;
    }

    protected void onSetInitialValue(Object object) {
    }

    @Deprecated
    protected void onSetInitialValue(boolean bl, Object object) {
        this.onSetInitialValue(object);
    }

    public Bundle peekExtras() {
        return this.mExtras;
    }

    public void performClick() {
        if (this.isEnabled() && this.isSelectable()) {
            this.onClick();
            Object object = this.mOnClickListener;
            if (object != null && object.onPreferenceClick(this)) {
                return;
            }
            object = this.getPreferenceManager();
            if (object != null && (object = ((PreferenceManager)object).getOnPreferenceTreeClickListener()) != null && object.onPreferenceTreeClick(this)) {
                return;
            }
            if (this.mIntent != null) {
                this.getContext().startActivity(this.mIntent);
            }
            return;
        }
    }

    protected void performClick(View view) {
        this.performClick();
    }

    protected boolean persistBoolean(boolean bl) {
        if (!this.shouldPersist()) {
            return false;
        }
        if (bl == this.getPersistedBoolean(bl ^ true)) {
            return true;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            preferenceDataStore.putBoolean(this.mKey, bl);
        } else {
            preferenceDataStore = this.mPreferenceManager.getEditor();
            preferenceDataStore.putBoolean(this.mKey, bl);
            this.tryCommit((SharedPreferences.Editor)preferenceDataStore);
        }
        return true;
    }

    protected boolean persistFloat(float f) {
        if (!this.shouldPersist()) {
            return false;
        }
        if (f == this.getPersistedFloat(Float.NaN)) {
            return true;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            preferenceDataStore.putFloat(this.mKey, f);
        } else {
            preferenceDataStore = this.mPreferenceManager.getEditor();
            preferenceDataStore.putFloat(this.mKey, f);
            this.tryCommit((SharedPreferences.Editor)preferenceDataStore);
        }
        return true;
    }

    protected boolean persistInt(int n) {
        if (!this.shouldPersist()) {
            return false;
        }
        if (n == this.getPersistedInt(~n)) {
            return true;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            preferenceDataStore.putInt(this.mKey, n);
        } else {
            preferenceDataStore = this.mPreferenceManager.getEditor();
            preferenceDataStore.putInt(this.mKey, n);
            this.tryCommit((SharedPreferences.Editor)preferenceDataStore);
        }
        return true;
    }

    protected boolean persistLong(long l) {
        if (!this.shouldPersist()) {
            return false;
        }
        if (l == this.getPersistedLong(0xFFFFFFFFFFFFFFFFL ^ l)) {
            return true;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            preferenceDataStore.putLong(this.mKey, l);
        } else {
            preferenceDataStore = this.mPreferenceManager.getEditor();
            preferenceDataStore.putLong(this.mKey, l);
            this.tryCommit((SharedPreferences.Editor)preferenceDataStore);
        }
        return true;
    }

    protected boolean persistString(String string2) {
        if (!this.shouldPersist()) {
            return false;
        }
        if (TextUtils.equals((CharSequence)string2, (CharSequence)this.getPersistedString(null))) {
            return true;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            preferenceDataStore.putString(this.mKey, string2);
        } else {
            preferenceDataStore = this.mPreferenceManager.getEditor();
            preferenceDataStore.putString(this.mKey, string2);
            this.tryCommit((SharedPreferences.Editor)preferenceDataStore);
        }
        return true;
    }

    public boolean persistStringSet(Set<String> set) {
        if (!this.shouldPersist()) {
            return false;
        }
        if (set.equals(this.getPersistedStringSet(null))) {
            return true;
        }
        PreferenceDataStore preferenceDataStore = this.getPreferenceDataStore();
        if (preferenceDataStore != null) {
            preferenceDataStore.putStringSet(this.mKey, set);
        } else {
            preferenceDataStore = this.mPreferenceManager.getEditor();
            preferenceDataStore.putStringSet(this.mKey, set);
            this.tryCommit((SharedPreferences.Editor)preferenceDataStore);
        }
        return true;
    }

    void requireKey() {
        if (!TextUtils.isEmpty((CharSequence)this.mKey)) {
            this.mRequiresKey = true;
            return;
        }
        throw new IllegalStateException("Preference does not have a key assigned.");
    }

    public void restoreHierarchyState(Bundle bundle) {
        this.dispatchRestoreInstanceState(bundle);
    }

    public void saveHierarchyState(Bundle bundle) {
        this.dispatchSaveInstanceState(bundle);
    }

    public void setCopyingEnabled(boolean bl) {
        if (this.mCopyingEnabled != bl) {
            this.mCopyingEnabled = bl;
            this.notifyChanged();
        }
    }

    public void setDefaultValue(Object object) {
        this.mDefaultValue = object;
    }

    public void setDependency(String string2) {
        this.unregisterDependency();
        this.mDependencyKey = string2;
        this.registerDependency();
    }

    public void setEnabled(boolean bl) {
        if (this.mEnabled != bl) {
            this.mEnabled = bl;
            this.notifyDependencyChange(this.shouldDisableDependents());
            this.notifyChanged();
        }
    }

    public void setFragment(String string2) {
        this.mFragment = string2;
    }

    public void setIcon(int n) {
        this.setIcon(AppCompatResources.getDrawable(this.mContext, n));
        this.mIconResId = n;
    }

    public void setIcon(Drawable drawable2) {
        if (this.mIcon != drawable2) {
            this.mIcon = drawable2;
            this.mIconResId = 0;
            this.notifyChanged();
        }
    }

    public void setIconSpaceReserved(boolean bl) {
        if (this.mIconSpaceReserved != bl) {
            this.mIconSpaceReserved = bl;
            this.notifyChanged();
        }
    }

    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    public void setKey(String string2) {
        this.mKey = string2;
        if (this.mRequiresKey && !this.hasKey()) {
            this.requireKey();
        }
    }

    public void setLayoutResource(int n) {
        this.mLayoutResId = n;
    }

    final void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener) {
        this.mListener = onPreferenceChangeInternalListener;
    }

    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        this.mOnChangeListener = onPreferenceChangeListener;
    }

    public void setOnPreferenceClickListener(OnPreferenceClickListener onPreferenceClickListener) {
        this.mOnClickListener = onPreferenceClickListener;
    }

    public void setOrder(int n) {
        if (n != this.mOrder) {
            this.mOrder = n;
            this.notifyHierarchyChanged();
        }
    }

    public void setPersistent(boolean bl) {
        this.mPersistent = bl;
    }

    public void setPreferenceDataStore(PreferenceDataStore preferenceDataStore) {
        this.mPreferenceDataStore = preferenceDataStore;
    }

    public void setSelectable(boolean bl) {
        if (this.mSelectable != bl) {
            this.mSelectable = bl;
            this.notifyChanged();
        }
    }

    public void setShouldDisableView(boolean bl) {
        if (this.mShouldDisableView != bl) {
            this.mShouldDisableView = bl;
            this.notifyChanged();
        }
    }

    public void setSingleLineTitle(boolean bl) {
        this.mHasSingleLineTitleAttr = true;
        this.mSingleLineTitle = bl;
    }

    public void setSummary(int n) {
        this.setSummary(this.mContext.getString(n));
    }

    public void setSummary(CharSequence charSequence) {
        if (this.getSummaryProvider() == null) {
            if (!TextUtils.equals((CharSequence)this.mSummary, (CharSequence)charSequence)) {
                this.mSummary = charSequence;
                this.notifyChanged();
            }
            return;
        }
        throw new IllegalStateException("Preference already has a SummaryProvider set.");
    }

    public final void setSummaryProvider(SummaryProvider summaryProvider) {
        this.mSummaryProvider = summaryProvider;
        this.notifyChanged();
    }

    public void setTitle(int n) {
        this.setTitle(this.mContext.getString(n));
    }

    public void setTitle(CharSequence charSequence) {
        if (charSequence == null && this.mTitle != null || charSequence != null && !charSequence.equals(this.mTitle)) {
            this.mTitle = charSequence;
            this.notifyChanged();
        }
    }

    public void setViewId(int n) {
        this.mViewId = n;
    }

    public final void setVisible(boolean bl) {
        if (this.mVisible != bl) {
            this.mVisible = bl;
            OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = this.mListener;
            if (onPreferenceChangeInternalListener != null) {
                onPreferenceChangeInternalListener.onPreferenceVisibilityChange(this);
            }
        }
    }

    public void setWidgetLayoutResource(int n) {
        this.mWidgetLayoutResId = n;
    }

    public boolean shouldDisableDependents() {
        return this.isEnabled() ^ true;
    }

    protected boolean shouldPersist() {
        boolean bl = this.mPreferenceManager != null && this.isPersistent() && this.hasKey();
        return bl;
    }

    public String toString() {
        return this.getFilterableStringBuilder().toString();
    }

    final boolean wasDetached() {
        return this.mWasDetached;
    }

    public static class BaseSavedState
    extends AbsSavedState {
        public static final Parcelable.Creator<BaseSavedState> CREATOR = new Parcelable.Creator<BaseSavedState>(){

            public BaseSavedState createFromParcel(Parcel parcel) {
                return new BaseSavedState(parcel);
            }

            public BaseSavedState[] newArray(int n) {
                return new BaseSavedState[n];
            }
        };

        public BaseSavedState(Parcel parcel) {
            super(parcel);
        }

        public BaseSavedState(Parcelable parcelable) {
            super(parcelable);
        }
    }

    static interface OnPreferenceChangeInternalListener {
        public void onPreferenceChange(Preference var1);

        public void onPreferenceHierarchyChange(Preference var1);

        public void onPreferenceVisibilityChange(Preference var1);
    }

    public static interface OnPreferenceChangeListener {
        public boolean onPreferenceChange(Preference var1, Object var2);
    }

    public static interface OnPreferenceClickListener {
        public boolean onPreferenceClick(Preference var1);
    }

    private static class OnPreferenceCopyListener
    implements View.OnCreateContextMenuListener,
    MenuItem.OnMenuItemClickListener {
        private final Preference mPreference;

        OnPreferenceCopyListener(Preference preference) {
            this.mPreference = preference;
        }

        public void onCreateContextMenu(ContextMenu contextMenu, View object, ContextMenu.ContextMenuInfo contextMenuInfo) {
            object = this.mPreference.getSummary();
            if (this.mPreference.isCopyingEnabled() && !TextUtils.isEmpty((CharSequence)object)) {
                contextMenu.setHeaderTitle((CharSequence)object);
                contextMenu.add(0, 0, 0, R.string.copy).setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener)this);
                return;
            }
        }

        public boolean onMenuItemClick(MenuItem object) {
            ClipboardManager clipboardManager = (ClipboardManager)this.mPreference.getContext().getSystemService("clipboard");
            object = this.mPreference.getSummary();
            clipboardManager.setPrimaryClip(ClipData.newPlainText((CharSequence)Preference.CLIPBOARD_ID, (CharSequence)object));
            Toast.makeText((Context)this.mPreference.getContext(), (CharSequence)this.mPreference.getContext().getString(R.string.preference_copied, new Object[]{object}), (int)0).show();
            return true;
        }
    }

    public static interface SummaryProvider<T extends Preference> {
        public CharSequence provideSummary(T var1);
    }
}

