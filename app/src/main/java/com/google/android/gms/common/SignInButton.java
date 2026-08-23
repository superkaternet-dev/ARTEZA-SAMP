/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.FrameLayout
 */
package com.google.android.gms.common;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.base.R;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zaaa;
import com.google.android.gms.common.internal.zaz;
import com.google.android.gms.dynamic.RemoteCreator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class SignInButton
extends FrameLayout
implements View.OnClickListener {
    public static final int COLOR_AUTO = 2;
    public static final int COLOR_DARK = 0;
    public static final int COLOR_LIGHT = 1;
    public static final int SIZE_ICON_ONLY = 2;
    public static final int SIZE_STANDARD = 0;
    public static final int SIZE_WIDE = 1;
    private int zaa;
    private int zab;
    private View zac;
    private View.OnClickListener zad = null;

    public SignInButton(Context context) {
        this(context, null);
    }

    public SignInButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SignInButton(Context context, AttributeSet attributeSet, int n) {
        super(context, attributeSet, n);
        context = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.SignInButton, 0, 0);
        try {
            this.zaa = context.getInt(R.styleable.SignInButton_buttonSize, 0);
            this.zab = context.getInt(R.styleable.SignInButton_colorScheme, 2);
            this.setStyle(this.zaa, this.zab);
            return;
        }
        finally {
            context.recycle();
        }
    }

    private final void zaa(Context context) {
        View view = this.zac;
        if (view != null) {
            this.removeView(view);
        }
        try {
            this.zac = zaz.zaa(context, this.zaa, this.zab);
        }
        catch (RemoteCreator.RemoteCreatorException remoteCreatorException) {
            Log.w((String)"SignInButton", (String)"Sign in button not found, using placeholder instead");
            int n = this.zaa;
            int n2 = this.zab;
            zaaa zaaa2 = new zaaa(context, null);
            zaaa2.zaa(context.getResources(), n, n2);
            this.zac = zaaa2;
        }
        this.addView(this.zac);
        this.zac.setEnabled(this.isEnabled());
        this.zac.setOnClickListener((View.OnClickListener)this);
    }

    public void onClick(View view) {
        View.OnClickListener onClickListener = this.zad;
        if (onClickListener != null && view == this.zac) {
            onClickListener.onClick((View)this);
        }
    }

    public void setColorScheme(int n) {
        this.setStyle(this.zaa, n);
    }

    public void setEnabled(boolean bl) {
        super.setEnabled(bl);
        this.zac.setEnabled(bl);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.zad = onClickListener;
        onClickListener = this.zac;
        if (onClickListener != null) {
            onClickListener.setOnClickListener((View.OnClickListener)this);
        }
    }

    @Deprecated
    public void setScopes(Scope[] scopeArray) {
        this.setStyle(this.zaa, this.zab);
    }

    public void setSize(int n) {
        this.setStyle(n, this.zab);
    }

    public void setStyle(int n, int n2) {
        this.zaa = n;
        this.zab = n2;
        this.zaa(this.getContext());
    }

    @Deprecated
    public void setStyle(int n, int n2, Scope[] scopeArray) {
        this.setStyle(n, n2);
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ButtonSize {
    }

    @Retention(value=RetentionPolicy.SOURCE)
    public static @interface ColorScheme {
    }
}

