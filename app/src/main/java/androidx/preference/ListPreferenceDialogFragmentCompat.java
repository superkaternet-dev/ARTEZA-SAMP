/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.os.Bundle
 */
package androidx.preference;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

public class ListPreferenceDialogFragmentCompat
extends PreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_ENTRIES = "ListPreferenceDialogFragment.entries";
    private static final String SAVE_STATE_ENTRY_VALUES = "ListPreferenceDialogFragment.entryValues";
    private static final String SAVE_STATE_INDEX = "ListPreferenceDialogFragment.index";
    int mClickedDialogEntryIndex;
    private CharSequence[] mEntries;
    private CharSequence[] mEntryValues;

    private ListPreference getListPreference() {
        return (ListPreference)this.getPreference();
    }

    public static ListPreferenceDialogFragmentCompat newInstance(String string2) {
        ListPreferenceDialogFragmentCompat listPreferenceDialogFragmentCompat = new ListPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", string2);
        listPreferenceDialogFragmentCompat.setArguments(bundle);
        return listPreferenceDialogFragmentCompat;
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
    public void onDialogClosed(boolean bl) {
        int n;
        if (bl && (n = this.mClickedDialogEntryIndex) >= 0) {
            String string2 = this.mEntryValues[n].toString();
            ListPreference listPreference = this.getListPreference();
            if (listPreference.callChangeListener(string2)) {
                listPreference.setValue(string2);
            }
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setSingleChoiceItems(this.mEntries, this.mClickedDialogEntryIndex, new DialogInterface.OnClickListener(this){
            final ListPreferenceDialogFragmentCompat this$0;
            {
                this.this$0 = listPreferenceDialogFragmentCompat;
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

