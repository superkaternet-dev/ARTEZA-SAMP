/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.view.View
 *  android.widget.TextView
 *  android.widget.ToggleButton
 */
package androidx.appcompat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.widget.AppCompatTextHelper;
import androidx.appcompat.widget.ThemeUtils;

public class AppCompatToggleButton
extends ToggleButton {
    private final AppCompatTextHelper mTextHelper;

    public AppCompatToggleButton(Context context) {
        this(context, null);
    }

    public AppCompatToggleButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842827);
    }

    public AppCompatToggleButton(Context object, AttributeSet attributeSet, int n) {
        super((Context)object, attributeSet, n);
        ThemeUtils.checkAppCompatTheme((View)this, this.getContext());
        object = new AppCompatTextHelper((TextView)this);
        this.mTextHelper = object;
        ((AppCompatTextHelper)object).loadFromAttributes(attributeSet, n);
    }
}

