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
import androidx.preference.PreferenceDialogFragment;

@Deprecated
public class EditTextPreferenceDialogFragment
extends PreferenceDialogFragment {
    private static final String SAVE_STATE_TEXT = "EditTextPreferenceDialogFragment.text";
    private EditText mEditText;
    private CharSequence mText;

    @Deprecated
    public EditTextPreferenceDialogFragment() {
    }

    private EditTextPreference getEditTextPreference() {
        return (EditTextPreference)this.getPreference();
    }

    @Deprecated
    public static EditTextPreferenceDialogFragment newInstance(String string2) {
        EditTextPreferenceDialogFragment editTextPreferenceDialogFragment = new EditTextPreferenceDialogFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("key", string2);
        editTextPreferenceDialogFragment.setArguments(bundle);
        return editTextPreferenceDialogFragment;
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
        view.requestFocus();
        view = this.mEditText;
        if (view != null) {
            view.setText(this.mText);
            view = this.mEditText;
            view.setSelection(view.getText().length());
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
    @Deprecated
    public void onDialogClosed(boolean bl) {
        if (bl) {
            String string2 = this.mEditText.getText().toString();
            if (this.getEditTextPreference().callChangeListener(string2)) {
                this.getEditTextPreference().setText(string2);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putCharSequence(SAVE_STATE_TEXT, this.mText);
    }
}

