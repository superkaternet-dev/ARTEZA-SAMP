/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.view.View
 */
package com.google.android.material.tabs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.appcompat.widget.TintTypedArray;
import com.google.android.material.R;

public class TabItem
extends View {
    public final int customLayout;
    public final Drawable icon;
    public final CharSequence text;

    public TabItem(Context context) {
        this(context, null);
    }

    public TabItem(Context object, AttributeSet attributeSet) {
        super((Context)object, attributeSet);
        object = TintTypedArray.obtainStyledAttributes((Context)object, attributeSet, R.styleable.TabItem);
        this.text = ((TintTypedArray)object).getText(R.styleable.TabItem_android_text);
        this.icon = ((TintTypedArray)object).getDrawable(R.styleable.TabItem_android_icon);
        this.customLayout = ((TintTypedArray)object).getResourceId(R.styleable.TabItem_android_layout, 0);
        ((TintTypedArray)object).recycle();
    }
}

