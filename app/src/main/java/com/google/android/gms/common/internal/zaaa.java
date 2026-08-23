/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.Typeface
 *  android.graphics.drawable.Drawable
 *  android.util.AttributeSet
 *  android.widget.Button
 */
package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.gms.base.R;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.util.DeviceProperties;

public final class zaaa
extends Button {
    public zaaa(Context context, AttributeSet attributeSet) {
        super(context, null, 16842824);
    }

    private static final int zab(int n, int n2, int n3, int n4) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder(33);
                stringBuilder.append("Unknown color scheme: ");
                stringBuilder.append(n);
                throw new IllegalStateException(stringBuilder.toString());
            }
            case 2: {
                return n4;
            }
            case 1: {
                return n3;
            }
            case 0: 
        }
        return n2;
    }

    public final void zaa(Resources object, int n, int n2) {
        this.setTypeface(Typeface.DEFAULT_BOLD);
        this.setTextSize(14.0f);
        int n3 = (int)(object.getDisplayMetrics().density * 48.0f + 0.5f);
        this.setMinHeight(n3);
        this.setMinWidth(n3);
        n3 = zaaa.zab(n2, R.drawable.common_google_signin_btn_icon_dark, R.drawable.common_google_signin_btn_icon_light, R.drawable.common_google_signin_btn_icon_light);
        int n4 = zaaa.zab(n2, R.drawable.common_google_signin_btn_text_dark, R.drawable.common_google_signin_btn_text_light, R.drawable.common_google_signin_btn_text_light);
        switch (n) {
            default: {
                object = new StringBuilder(32);
                ((StringBuilder)object).append("Unknown button size: ");
                ((StringBuilder)object).append(n);
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
            case 0: 
            case 1: {
                n3 = n4;
            }
            case 2: 
        }
        Drawable drawable2 = DrawableCompat.wrap(object.getDrawable(n3));
        DrawableCompat.setTintList(drawable2, object.getColorStateList(R.color.common_google_signin_btn_tint));
        DrawableCompat.setTintMode(drawable2, PorterDuff.Mode.SRC_ATOP);
        this.setBackgroundDrawable(drawable2);
        this.setTextColor(Preconditions.checkNotNull(object.getColorStateList(zaaa.zab(n2, R.color.common_google_signin_btn_text_dark, R.color.common_google_signin_btn_text_light, R.color.common_google_signin_btn_text_light))));
        switch (n) {
            default: {
                object = new StringBuilder(32);
                ((StringBuilder)object).append("Unknown button size: ");
                ((StringBuilder)object).append(n);
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
            case 2: {
                this.setText(null);
                break;
            }
            case 1: {
                this.setText(object.getString(R.string.common_signin_button_text_long));
                break;
            }
            case 0: {
                this.setText(object.getString(R.string.common_signin_button_text));
            }
        }
        this.setTransformationMethod(null);
        if (DeviceProperties.isWearable(this.getContext())) {
            this.setGravity(19);
        }
    }
}

