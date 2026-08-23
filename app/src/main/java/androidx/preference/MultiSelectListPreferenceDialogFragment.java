/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AlertDialog$Builder
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnMultiChoiceClickListener
 *  android.os.Bundle
 */
package androidx.preference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.PreferenceDialogFragment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Deprecated
public class MultiSelectListPreferenceDialogFragment
extends PreferenceDialogFragment {
    private static final String SAVE_STATE_CHANGED = "MultiSelectListPreferenceDialogFragment.changed";
    private static final String SAVE_STATE_ENTRIES = "MultiSelectListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "MultiSelectListPreferenceDialogFragment.entryValues";
    private static final String SAVE_STATE_VALUES = "MultiSelectListPreferenceDialogFragment.values";
    CharSequence[] mEntries;
    CharSequence[] mEntryValues;
    Set<String> mNewValues = new HashSet<String>();
    boolean mPreferenceChanged;

    @Deprecated
    public MultiSelectListPreferenceDialogFragment() {
    }

    private MultiSelectListPreference getListPreference() {
        return (MultiSelectListPreference)this.getPreference();
    }

    @Deprecated
    public static MultiSelectListPreferenceDialogFragment newInstance(String string2) {
        MultiSelectListPreferenceDialogFragment multiSelectListPreferenceDialogFragment = new MultiSelectListPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", string2);
        multiSelectListPreferenceDialogFragment.setArguments(bundle);
        return multiSelectListPreferenceDialogFragment;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        if (object == null) {
            object = this.getListPreference();
            if (((MultiSelectListPreference)object).getEntries() == null || ((MultiSelectListPreference)object).getEntryValues() == null) throw new IllegalStateException("MultiSelectListPreference requires an entries array and an entryValues array.");
            this.mNewValues.clear();
            this.mNewValues.addAll(((MultiSelectListPreference)object).getValues());
            this.mPreferenceChanged = false;
            this.mEntries = ((MultiSelectListPreference)object).getEntries();
            this.mEntryValues = ((MultiSelectListPreference)object).getEntryValues();
            return;
        } else {
            this.mNewValues.clear();
            this.mNewValues.addAll(object.getStringArrayList(SAVE_STATE_VALUES));
            this.mPreferenceChanged = object.getBoolean(SAVE_STATE_CHANGED, false);
            this.mEntries = object.getCharSequenceArray(SAVE_STATE_ENTRIES);
            this.mEntryValues = object.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
        }
    }

    @Override
    @Deprecated
    public void onDialogClosed(boolean bl) {
        Set<String> set;
        MultiSelectListPreference multiSelectListPreference = this.getListPreference();
        if (bl && this.mPreferenceChanged && multiSelectListPreference.callChangeListener(set = this.mNewValues)) {
            multiSelectListPreference.setValues(set);
        }
        this.mPreferenceChanged = false;
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        int n = this.mEntryValues.length;
        boolean[] blArray = new boolean[n];
        for (int i = 0; i < n; ++i) {
            blArray[i] = this.mNewValues.contains(this.mEntryValues[i].toString());
        }
        builder.setMultiChoiceItems(this.mEntries, blArray, new DialogInterface.OnMultiChoiceClickListener(this){
            final MultiSelectListPreferenceDialogFragment this$0;
            {
                this.this$0 = multiSelectListPreferenceDialogFragment;
            }

            public void onClick(DialogInterface object, int n, boolean bl) {
                if (bl) {
                    object = this.this$0;
                    object.mPreferenceChanged |= this.this$0.mNewValues.add(this.this$0.mEntryValues[n].toString());
                } else {
                    object = this.this$0;
                    object.mPreferenceChanged |= this.this$0.mNewValues.remove(this.this$0.mEntryValues[n].toString());
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putStringArrayList(SAVE_STATE_VALUES, new ArrayList<String>(this.mNewValues));
        bundle.putBoolean(SAVE_STATE_CHANGED, this.mPreferenceChanged);
        bundle.putCharSequenceArray(SAVE_STATE_ENTRIES, this.mEntries);
        bundle.putCharSequenceArray(SAVE_STATE_ENTRY_VALUES, this.mEntryValues);
    }
}

