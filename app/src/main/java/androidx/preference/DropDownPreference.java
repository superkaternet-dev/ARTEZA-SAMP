/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.AdapterView
 *  android.widget.AdapterView$OnItemSelectedListener
 *  android.widget.ArrayAdapter
 *  android.widget.Spinner
 *  android.widget.SpinnerAdapter
 */
package androidx.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.R;

public class DropDownPreference
extends ListPreference {
    private final ArrayAdapter mAdapter;
    private final Context mContext;
    private final AdapterView.OnItemSelectedListener mItemSelectedListener = new AdapterView.OnItemSelectedListener(this){
        final DropDownPreference this$0;
        {
            this.this$0 = dropDownPreference;
        }

        public void onItemSelected(AdapterView<?> object, View view, int n, long l) {
            if (n >= 0 && !((String)(object = this.this$0.getEntryValues()[n].toString())).equals(this.this$0.getValue()) && this.this$0.callChangeListener(object)) {
                this.this$0.setValue((String)object);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };
    private Spinner mSpinner;

    public DropDownPreference(Context context) {
        this(context, null);
    }

    public DropDownPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.dropdownPreferenceStyle);
    }

    public DropDownPreference(Context context, AttributeSet attributeSet, int n) {
        this(context, attributeSet, n, 0);
    }

    public DropDownPreference(Context context, AttributeSet attributeSet, int n, int n2) {
        super(context, attributeSet, n, n2);
        this.mContext = context;
        this.mAdapter = this.createAdapter();
        this.updateEntries();
    }

    private int findSpinnerIndexOfValue(String string2) {
        CharSequence[] charSequenceArray = this.getEntryValues();
        if (string2 != null && charSequenceArray != null) {
            for (int i = charSequenceArray.length - 1; i >= 0; --i) {
                if (!charSequenceArray[i].equals(string2)) continue;
                return i;
            }
        }
        return -1;
    }

    private void updateEntries() {
        this.mAdapter.clear();
        if (this.getEntries() != null) {
            for (CharSequence charSequence : this.getEntries()) {
                this.mAdapter.add((Object)charSequence.toString());
            }
        }
    }

    protected ArrayAdapter createAdapter() {
        return new ArrayAdapter(this.mContext, 0x1090009);
    }

    @Override
    protected void notifyChanged() {
        super.notifyChanged();
        ArrayAdapter arrayAdapter = this.mAdapter;
        if (arrayAdapter != null) {
            arrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        Spinner spinner;
        this.mSpinner = spinner = (Spinner)preferenceViewHolder.itemView.findViewById(R.id.spinner);
        spinner.setAdapter((SpinnerAdapter)this.mAdapter);
        this.mSpinner.setOnItemSelectedListener(this.mItemSelectedListener);
        this.mSpinner.setSelection(this.findSpinnerIndexOfValue(this.getValue()));
        super.onBindViewHolder(preferenceViewHolder);
    }

    @Override
    protected void onClick() {
        this.mSpinner.performClick();
    }

    @Override
    public void setEntries(CharSequence[] charSequenceArray) {
        super.setEntries(charSequenceArray);
        this.updateEntries();
    }

    @Override
    public void setValueIndex(int n) {
        this.setValue(this.getEntryValues()[n].toString());
    }
}

