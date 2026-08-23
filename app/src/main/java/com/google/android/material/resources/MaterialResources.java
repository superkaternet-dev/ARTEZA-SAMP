/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 *  android.content.res.TypedArray
 *  android.graphics.drawable.Drawable
 */
package com.google.android.material.resources;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import androidx.appcompat.content.res.AppCompatResources;
import com.google.android.material.resources.TextAppearance;

public class MaterialResources {
    private MaterialResources() {
    }

    public static ColorStateList getColorStateList(Context context, TypedArray typedArray, int n) {
        int n2;
        if (typedArray.hasValue(n) && (n2 = typedArray.getResourceId(n, 0)) != 0 && (context = AppCompatResources.getColorStateList(context, n2)) != null) {
            return context;
        }
        return typedArray.getColorStateList(n);
    }

    public static Drawable getDrawable(Context context, TypedArray typedArray, int n) {
        int n2;
        if (typedArray.hasValue(n) && (n2 = typedArray.getResourceId(n, 0)) != 0 && (context = AppCompatResources.getDrawable(context, n2)) != null) {
            return context;
        }
        return typedArray.getDrawable(n);
    }

    static int getIndexWithValue(TypedArray typedArray, int n, int n2) {
        if (typedArray.hasValue(n)) {
            return n;
        }
        return n2;
    }

    public static TextAppearance getTextAppearance(Context context, TypedArray typedArray, int n) {
        if (typedArray.hasValue(n) && (n = typedArray.getResourceId(n, 0)) != 0) {
            return new TextAppearance(context, n);
        }
        return null;
    }
}

