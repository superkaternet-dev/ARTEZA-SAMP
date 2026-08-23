/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Resources
 *  android.content.res.Resources$NotFoundException
 *  android.util.AttributeSet
 *  android.util.Log
 *  android.util.TypedValue
 */
package com.google.android.gms.common.internal;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;

public class ViewUtils {
    private ViewUtils() {
    }

    public static String getXmlAttributeString(String charSequence, String string2, Context object, AttributeSet object2, boolean bl, boolean bl2, String string3) {
        charSequence = object2 == null ? null : object2.getAttributeValue(charSequence, string2);
        object2 = charSequence;
        if (charSequence != null) {
            object2 = charSequence;
            if (charSequence.startsWith("@string/")) {
                object2 = charSequence;
                if (bl) {
                    String string4 = charSequence.substring(8);
                    String string5 = object.getPackageName();
                    object2 = new TypedValue();
                    try {
                        Resources resources = object.getResources();
                        int n = String.valueOf(string5).length();
                        int n2 = String.valueOf(string4).length();
                        object = new StringBuilder(n + 8 + n2);
                        ((StringBuilder)object).append(string5);
                        ((StringBuilder)object).append(":string/");
                        ((StringBuilder)object).append(string4);
                        resources.getValue(((StringBuilder)object).toString(), (TypedValue)object2, true);
                    }
                    catch (Resources.NotFoundException notFoundException) {
                        object = new StringBuilder(String.valueOf(string2).length() + 30 + charSequence.length());
                        ((StringBuilder)object).append("Could not find resource for ");
                        ((StringBuilder)object).append(string2);
                        ((StringBuilder)object).append(": ");
                        ((StringBuilder)object).append((String)charSequence);
                        Log.w((String)string3, (String)((StringBuilder)object).toString());
                    }
                    if (((TypedValue)object2).string != null) {
                        object2 = ((TypedValue)object2).string.toString();
                    } else {
                        object = object2.toString();
                        object2 = new StringBuilder(String.valueOf(string2).length() + 28 + ((String)object).length());
                        ((StringBuilder)object2).append("Resource ");
                        ((StringBuilder)object2).append(string2);
                        ((StringBuilder)object2).append(" was not a string: ");
                        ((StringBuilder)object2).append((String)object);
                        Log.w((String)string3, (String)((StringBuilder)object2).toString());
                        object2 = charSequence;
                    }
                }
            }
        }
        if (bl2 && object2 == null) {
            charSequence = new StringBuilder(String.valueOf(string2).length() + 33);
            ((StringBuilder)charSequence).append("Required XML attribute \"");
            ((StringBuilder)charSequence).append(string2);
            ((StringBuilder)charSequence).append("\" missing");
            Log.w((String)string3, (String)((StringBuilder)charSequence).toString());
        }
        return object2;
    }
}

