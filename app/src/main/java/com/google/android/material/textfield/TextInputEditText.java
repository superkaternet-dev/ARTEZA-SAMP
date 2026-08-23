/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.view.ViewParent
 *  android.view.inputmethod.EditorInfo
 *  android.view.inputmethod.InputConnection
 */
package com.google.android.material.textfield;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import androidx.appcompat.widget.AppCompatEditText;
import com.google.android.material.R;
import com.google.android.material.textfield.TextInputLayout;

public class TextInputEditText
extends AppCompatEditText {
    public TextInputEditText(Context context) {
        this(context, null);
    }

    public TextInputEditText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.editTextStyle);
    }

    public TextInputEditText(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
    }

    private CharSequence getHintFromLayout() {
        Object object = this.getTextInputLayout();
        object = object != null ? object.getHint() : null;
        return object;
    }

    private TextInputLayout getTextInputLayout() {
        ViewParent viewParent = this.getParent();
        while (viewParent instanceof View) {
            if (viewParent instanceof TextInputLayout) {
                return (TextInputLayout)viewParent;
            }
            viewParent = viewParent.getParent();
        }
        return null;
    }

    public CharSequence getHint() {
        TextInputLayout textInputLayout = this.getTextInputLayout();
        if (textInputLayout != null && textInputLayout.isProvidingHint()) {
            return textInputLayout.getHint();
        }
        return super.getHint();
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection inputConnection = super.onCreateInputConnection(editorInfo);
        if (inputConnection != null && editorInfo.hintText == null) {
            editorInfo.hintText = this.getHintFromLayout();
        }
        return inputConnection;
    }
}

