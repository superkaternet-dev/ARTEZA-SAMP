/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 */
package androidx.preference;

import android.content.Context;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class ExpandButton
extends Preference {
    private long mId;

    ExpandButton(Context context, List<Preference> list, long l) {
        super(context);
        this.initLayout();
        this.setSummary(list);
        this.mId = 1000000L + l;
    }

    private void initLayout() {
        this.setLayoutResource(R.layout.expand_button);
        this.setIcon(R.drawable.ic_arrow_down_24dp);
        this.setTitle(R.string.expand_button_title);
        this.setOrder(999);
    }

    private void setSummary(List<Preference> object) {
        Object object2 = null;
        ArrayList<PreferenceGroup> arrayList = new ArrayList<PreferenceGroup>();
        Iterator<Preference> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            CharSequence charSequence = ((Preference)object).getTitle();
            if (object instanceof PreferenceGroup && !TextUtils.isEmpty((CharSequence)charSequence)) {
                arrayList.add((PreferenceGroup)object);
            }
            if (arrayList.contains(((Preference)object).getParent())) {
                if (!(object instanceof PreferenceGroup)) continue;
                arrayList.add((PreferenceGroup)object);
                continue;
            }
            object = object2;
            if (!TextUtils.isEmpty((CharSequence)charSequence)) {
                object = object2 == null ? charSequence : this.getContext().getString(R.string.summary_collapsed_preference_list, new Object[]{object2, charSequence});
            }
            object2 = object;
        }
        this.setSummary((CharSequence)object2);
    }

    @Override
    long getId() {
        return this.mId;
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.setDividerAllowedAbove(false);
    }
}

