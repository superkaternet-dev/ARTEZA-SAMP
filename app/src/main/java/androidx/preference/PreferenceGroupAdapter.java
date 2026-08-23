/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 *  android.os.Handler
 *  android.text.TextUtils
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 */
package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.preference.ExpandButton;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PreferenceGroupAdapter
extends RecyclerView.Adapter<PreferenceViewHolder>
implements Preference.OnPreferenceChangeInternalListener,
PreferenceGroup.PreferencePositionCallback {
    private Handler mHandler;
    private PreferenceGroup mPreferenceGroup;
    private List<PreferenceResourceDescriptor> mPreferenceResourceDescriptors;
    private List<Preference> mPreferences;
    private Runnable mSyncRunnable = new Runnable(this){
        final PreferenceGroupAdapter this$0;
        {
            this.this$0 = preferenceGroupAdapter;
        }

        @Override
        public void run() {
            this.this$0.updatePreferences();
        }
    };
    private List<Preference> mVisiblePreferences;

    public PreferenceGroupAdapter(PreferenceGroup preferenceGroup) {
        this.mPreferenceGroup = preferenceGroup;
        this.mHandler = new Handler();
        this.mPreferenceGroup.setOnPreferenceChangeInternalListener(this);
        this.mPreferences = new ArrayList<Preference>();
        this.mVisiblePreferences = new ArrayList<Preference>();
        this.mPreferenceResourceDescriptors = new ArrayList<PreferenceResourceDescriptor>();
        preferenceGroup = this.mPreferenceGroup;
        if (preferenceGroup instanceof PreferenceScreen) {
            this.setHasStableIds(((PreferenceScreen)preferenceGroup).shouldUseGeneratedIds());
        } else {
            this.setHasStableIds(true);
        }
        this.updatePreferences();
    }

    private ExpandButton createExpandButton(PreferenceGroup preferenceGroup, List<Preference> object) {
        object = new ExpandButton(preferenceGroup.getContext(), (List<Preference>)object, preferenceGroup.getId());
        ((Preference)object).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(this, preferenceGroup){
            final PreferenceGroupAdapter this$0;
            final PreferenceGroup val$group;
            {
                this.this$0 = preferenceGroupAdapter;
                this.val$group = preferenceGroup;
            }

            @Override
            public boolean onPreferenceClick(Preference object) {
                this.val$group.setInitialExpandedChildrenCount(Integer.MAX_VALUE);
                this.this$0.onPreferenceHierarchyChange((Preference)object);
                object = this.val$group.getOnExpandButtonClickListener();
                if (object != null) {
                    object.onExpandButtonClick();
                }
                return true;
            }
        });
        return object;
    }

    private List<Preference> createVisiblePreferencesList(PreferenceGroup preferenceGroup) {
        int n = 0;
        ArrayList<Preference> arrayList = new ArrayList<Preference>();
        ArrayList<Preference> arrayList2 = new ArrayList<Preference>();
        int n2 = preferenceGroup.getPreferenceCount();
        block0: for (int i = 0; i < n2; ++i) {
            Preference preference = preferenceGroup.getPreference(i);
            if (!preference.isVisible()) continue;
            if (this.isGroupExpandable(preferenceGroup) && n >= preferenceGroup.getInitialExpandedChildrenCount()) {
                arrayList2.add(preference);
            } else {
                arrayList.add(preference);
            }
            if (!(preference instanceof PreferenceGroup)) {
                ++n;
                continue;
            }
            if (!((PreferenceGroup)(preference = (PreferenceGroup)preference)).isOnSameScreenAsChildren()) continue;
            if (this.isGroupExpandable(preferenceGroup) && this.isGroupExpandable((PreferenceGroup)preference)) {
                throw new IllegalStateException("Nesting an expandable group inside of another expandable group is not supported!");
            }
            Iterator<Preference> iterator2 = this.createVisiblePreferencesList((PreferenceGroup)preference).iterator();
            int n3 = n;
            while (true) {
                n = ++n3;
                if (!iterator2.hasNext()) continue block0;
                preference = iterator2.next();
                if (this.isGroupExpandable(preferenceGroup) && n3 >= preferenceGroup.getInitialExpandedChildrenCount()) {
                    arrayList2.add(preference);
                    continue;
                }
                arrayList.add(preference);
            }
        }
        if (this.isGroupExpandable(preferenceGroup) && n > preferenceGroup.getInitialExpandedChildrenCount()) {
            arrayList.add(this.createExpandButton(preferenceGroup, arrayList2));
        }
        return arrayList;
    }

    private void flattenPreferenceGroup(List<Preference> list, PreferenceGroup preferenceGroup) {
        preferenceGroup.sortPreferences();
        int n = preferenceGroup.getPreferenceCount();
        for (int i = 0; i < n; ++i) {
            Preference preference = preferenceGroup.getPreference(i);
            list.add(preference);
            Object object = new PreferenceResourceDescriptor(preference);
            if (!this.mPreferenceResourceDescriptors.contains(object)) {
                this.mPreferenceResourceDescriptors.add((PreferenceResourceDescriptor)object);
            }
            if (preference instanceof PreferenceGroup && ((PreferenceGroup)(object = (PreferenceGroup)preference)).isOnSameScreenAsChildren()) {
                this.flattenPreferenceGroup(list, (PreferenceGroup)object);
            }
            preference.setOnPreferenceChangeInternalListener(this);
        }
    }

    private boolean isGroupExpandable(PreferenceGroup preferenceGroup) {
        boolean bl = preferenceGroup.getInitialExpandedChildrenCount() != Integer.MAX_VALUE;
        return bl;
    }

    public Preference getItem(int n) {
        if (n >= 0 && n < this.getItemCount()) {
            return this.mVisiblePreferences.get(n);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return this.mVisiblePreferences.size();
    }

    @Override
    public long getItemId(int n) {
        if (!this.hasStableIds()) {
            return -1L;
        }
        return this.getItem(n).getId();
    }

    @Override
    public int getItemViewType(int n) {
        PreferenceResourceDescriptor preferenceResourceDescriptor = new PreferenceResourceDescriptor(this.getItem(n));
        if ((n = this.mPreferenceResourceDescriptors.indexOf(preferenceResourceDescriptor)) != -1) {
            return n;
        }
        n = this.mPreferenceResourceDescriptors.size();
        this.mPreferenceResourceDescriptors.add(preferenceResourceDescriptor);
        return n;
    }

    @Override
    public int getPreferenceAdapterPosition(Preference preference) {
        int n = this.mVisiblePreferences.size();
        for (int i = 0; i < n; ++i) {
            Preference preference2 = this.mVisiblePreferences.get(i);
            if (preference2 == null || !preference2.equals(preference)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public int getPreferenceAdapterPosition(String string2) {
        int n = this.mVisiblePreferences.size();
        for (int i = 0; i < n; ++i) {
            if (!TextUtils.equals((CharSequence)string2, (CharSequence)this.mVisiblePreferences.get(i).getKey())) continue;
            return i;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder, int n) {
        this.getItem(n).onBindViewHolder(preferenceViewHolder);
    }

    @Override
    public PreferenceViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
        Drawable drawable2;
        PreferenceResourceDescriptor preferenceResourceDescriptor = this.mPreferenceResourceDescriptors.get(n);
        LayoutInflater layoutInflater = LayoutInflater.from((Context)viewGroup.getContext());
        TypedArray typedArray = viewGroup.getContext().obtainStyledAttributes(null, R.styleable.BackgroundStyle);
        Drawable drawable3 = drawable2 = typedArray.getDrawable(R.styleable.BackgroundStyle_android_selectableItemBackground);
        if (drawable2 == null) {
            drawable3 = AppCompatResources.getDrawable(viewGroup.getContext(), 17301602);
        }
        typedArray.recycle();
        viewGroup = layoutInflater.inflate(preferenceResourceDescriptor.mLayoutResId, viewGroup, false);
        if (viewGroup.getBackground() == null) {
            ViewCompat.setBackground((View)viewGroup, drawable3);
        }
        if ((drawable3 = (ViewGroup)viewGroup.findViewById(16908312)) != null) {
            if (preferenceResourceDescriptor.mWidgetLayoutResId != 0) {
                layoutInflater.inflate(preferenceResourceDescriptor.mWidgetLayoutResId, (ViewGroup)drawable3);
            } else {
                drawable3.setVisibility(8);
            }
        }
        return new PreferenceViewHolder((View)viewGroup);
    }

    @Override
    public void onPreferenceChange(Preference preference) {
        int n = this.mVisiblePreferences.indexOf(preference);
        if (n != -1) {
            this.notifyItemChanged(n, preference);
        }
    }

    @Override
    public void onPreferenceHierarchyChange(Preference preference) {
        this.mHandler.removeCallbacks(this.mSyncRunnable);
        this.mHandler.post(this.mSyncRunnable);
    }

    @Override
    public void onPreferenceVisibilityChange(Preference preference) {
        this.onPreferenceHierarchyChange(preference);
    }

    void updatePreferences() {
        Iterator<Preference> iterator2 = this.mPreferences.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().setOnPreferenceChangeInternalListener(null);
        }
        iterator2 = new ArrayList(this.mPreferences.size());
        this.mPreferences = iterator2;
        this.flattenPreferenceGroup((List<Preference>)((Object)iterator2), this.mPreferenceGroup);
        List<Preference> list = this.mVisiblePreferences;
        List<Preference> list2 = this.createVisiblePreferencesList(this.mPreferenceGroup);
        this.mVisiblePreferences = list2;
        iterator2 = this.mPreferenceGroup.getPreferenceManager();
        if (iterator2 != null && ((PreferenceManager)((Object)iterator2)).getPreferenceComparisonCallback() != null) {
            DiffUtil.calculateDiff(new DiffUtil.Callback(this, list, list2, ((PreferenceManager)((Object)iterator2)).getPreferenceComparisonCallback()){
                final PreferenceGroupAdapter this$0;
                final PreferenceManager.PreferenceComparisonCallback val$comparisonCallback;
                final List val$oldVisibleList;
                final List val$visiblePreferenceList;
                {
                    this.this$0 = preferenceGroupAdapter;
                    this.val$oldVisibleList = list;
                    this.val$visiblePreferenceList = list2;
                    this.val$comparisonCallback = preferenceComparisonCallback;
                }

                @Override
                public boolean areContentsTheSame(int n, int n2) {
                    return this.val$comparisonCallback.arePreferenceContentsTheSame((Preference)this.val$oldVisibleList.get(n), (Preference)this.val$visiblePreferenceList.get(n2));
                }

                @Override
                public boolean areItemsTheSame(int n, int n2) {
                    return this.val$comparisonCallback.arePreferenceItemsTheSame((Preference)this.val$oldVisibleList.get(n), (Preference)this.val$visiblePreferenceList.get(n2));
                }

                @Override
                public int getNewListSize() {
                    return this.val$visiblePreferenceList.size();
                }

                @Override
                public int getOldListSize() {
                    return this.val$oldVisibleList.size();
                }
            }).dispatchUpdatesTo(this);
        } else {
            this.notifyDataSetChanged();
        }
        iterator2 = this.mPreferences.iterator();
        while (iterator2.hasNext()) {
            iterator2.next().clearWasDetached();
        }
    }

    private static class PreferenceResourceDescriptor {
        String mClassName;
        int mLayoutResId;
        int mWidgetLayoutResId;

        PreferenceResourceDescriptor(Preference preference) {
            this.mClassName = preference.getClass().getName();
            this.mLayoutResId = preference.getLayoutResource();
            this.mWidgetLayoutResId = preference.getWidgetLayoutResource();
        }

        public boolean equals(Object object) {
            boolean bl;
            block1: {
                boolean bl2 = object instanceof PreferenceResourceDescriptor;
                bl = false;
                if (!bl2) {
                    return false;
                }
                object = (PreferenceResourceDescriptor)object;
                if (this.mLayoutResId != ((PreferenceResourceDescriptor)object).mLayoutResId || this.mWidgetLayoutResId != ((PreferenceResourceDescriptor)object).mWidgetLayoutResId || !TextUtils.equals((CharSequence)this.mClassName, (CharSequence)((PreferenceResourceDescriptor)object).mClassName)) break block1;
                bl = true;
            }
            return bl;
        }

        public int hashCode() {
            return ((17 * 31 + this.mLayoutResId) * 31 + this.mWidgetLayoutResId) * 31 + this.mClassName.hashCode();
        }
    }
}

