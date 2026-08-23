/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.ColorStateList
 */
package com.skydoves.colorpickerview.flag;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.R;
import com.skydoves.colorpickerview.flag.FlagView;

public class BubbleFlag
extends FlagView {
    private AppCompatImageView bubble = (AppCompatImageView)this.findViewById(R.id.bubble);

    public BubbleFlag(Context context) {
        super(context, R.layout.flag_bubble);
    }

    @Override
    public void onRefresh(ColorEnvelope colorEnvelope) {
        ImageViewCompat.setImageTintList(this.bubble, ColorStateList.valueOf((int)colorEnvelope.getColor()));
    }
}

