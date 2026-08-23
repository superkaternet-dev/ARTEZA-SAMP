/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Rect
 *  android.text.method.TransformationMethod
 *  android.view.View
 */
package androidx.appcompat.text;

import android.content.Context;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.view.View;
import java.util.Locale;

public class AllCapsTransformationMethod
implements TransformationMethod {
    private Locale mLocale;

    public AllCapsTransformationMethod(Context context) {
        this.mLocale = context.getResources().getConfiguration().locale;
    }

    public CharSequence getTransformation(CharSequence charSequence, View view) {
        charSequence = charSequence != null ? charSequence.toString().toUpperCase(this.mLocale) : null;
        return charSequence;
    }

    public void onFocusChanged(View view, CharSequence charSequence, boolean bl, int n, Rect rect) {
    }
}

