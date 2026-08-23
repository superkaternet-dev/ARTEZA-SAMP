/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.EditText
 */
package androidx.preference;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

public class EditTextPreferenceDialogFragmentCompat
extends PreferenceDialogFragmentCompat {
    private static final String SAVE_STATE_TEXT = "EditTextPreferenceDialogFragment.text";
    private EditText mEditText;
    private CharSequence mText;

    private EditTextPreference getEditTextPreference() {
        return (EditTextPreference)this.getPreference();
    }

    public static EditTextPreferenceDialogFragmentCompat newInstance(String string2) {
        EditTextPreferenceDialogFragmentCompat editTextPreferenceDialogFragmentCompat = new EditTextPreferenceDialogFragmentCompat();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", string2);
        editTextPreferenceDialogFragmentCompat.setArguments(bundle);
        return editTextPreferenceDialogFragmentCompat;
    }

    @Override
    protected boolean needInputMethod() {
        return true;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        view = (EditText)view.findViewById(16908291);
        this.mEditText = view;
        if (view != null) {
            view.requestFocus();
            this.mEditText.setText(this.mText);
            view = this.mEditText;
            view.setSelection(view.getText().length());
            if (this.getEditTextPreference().getOnBindEditTextListener() != null) {
                this.getEditTextPreference().getOnBindEditTextListener().onBindEditText(this.mEditText);
            }
            return;
        }
        throw new IllegalStateException("Dialog view must contain an EditText with id @android:id/edit");
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mText = bundle == null ? this.getEditTextPreference().getText() : bundle.getCharSequence(SAVE_STATE_TEXT);
    }

    @Override
    public void onDialogClosed(boolean bl) {
        if (bl) {
            String string2 = this.mEditText.getText().toString();
            EditTextPreference editTextPreference = this.getEditTextPreference();
            if (editTextPreference.callChangeListener(string2)) {
                editTextPreference.setText(string2);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence(SAVE_STATE_TEXT, this.mText);
    }
}

