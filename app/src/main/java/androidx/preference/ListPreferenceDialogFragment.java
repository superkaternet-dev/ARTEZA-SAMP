/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.AlertDialog$Builder
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.os.Bundle
 */
package androidx.preference;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceDialogFragment;

@Deprecated
public class ListPreferenceDialogFragment
extends PreferenceDialogFragment {
    private static final String SAVE_STATE_ENTRIES = "ListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "ListPreferenceDialogFragment.entryValues";
    private static final String SAVE_STATE_INDEX = "ListPreferenceDialogFragment.index";
    int mClickedDialogEntryIndex;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    @Deprecated
    public ListPreferenceDialogFragment() {
    }

    private ListPreference getListPreference() {
        return (ListPreference)this.getPreference();
    }

    @Deprecated
    public static ListPreferenceDialogFragment newInstance(String string2) {
        ListPreferenceDialogFragment listPreferenceDialogFragment = new ListPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", string2);
        listPreferenceDialogFragment.setArguments(bundle);
        return listPreferenceDialogFragment;
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
            if (((ListPreference)object).getEntries() == null || ((ListPreference)object).getEntryValues() == null) throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
            this.mClickedDialogEntryIndex = ((ListPreference)object).findIndexOfValue(((ListPreference)object).getValue());
            this.mEntries = ((ListPreference)object).getEntries();
            this.mEntryValues = ((ListPreference)object).getEntryValues();
            return;
        } else {
            this.mClickedDialogEntryIndex = object.getInt(SAVE_STATE_INDEX, 0);
            this.mEntries = object.getCharSequenceArray(SAVE_STATE_ENTRIES);
            this.mEntryValues = object.getCharSequenceArray(SAVE_STATE_ENTRY_VALUES);
        }
    }

    @Override
    @Deprecated
    public void onDialogClosed(boolean bl) {
        String string2;
        int n;
        ListPreference listPreference = this.getListPreference();
        if (bl && (n = this.mClickedDialogEntryIndex) >= 0 && listPreference.callChangeListener(string2 = this.mEntryValues[n].toString())) {
            listPreference.setValue(string2);
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setSingleChoiceItems(this.mEntries, this.mClickedDialogEntryIndex, new DialogInterface.OnClickListener(this){
            final ListPreferenceDialogFragment this$0;
            {
                this.this$0 = listPreferenceDialogFragment;
            }

            public void onClick(DialogInterface dialogInterface, int n) {
                this.this$0.mClickedDialogEntryIndex = n;
                this.this$0.onClick(dialogInterface, -1);
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(null, null);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt(SAVE_STATE_INDEX, this.mClickedDialogEntryIndex);
        bundle.putCharSequenceArray(SAVE_STATE_ENTRIES, this.mEntries);
        bundle.putCharSequenceArray(SAVE_STATE_ENTRY_VALUES, this.mEntryValues);
    }
}

