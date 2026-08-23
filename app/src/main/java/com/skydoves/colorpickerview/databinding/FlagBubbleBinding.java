/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.FrameLayout
 */
package com.skydoves.colorpickerview.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;
import com.skydoves.colorpickerview.R;

public final class FlagBubbleBinding
implements ViewBinding {
    public final AppCompatImageView bubble;
    public final FrameLayout layout;
    private final FrameLayout rootView;

    private FlagBubbleBinding(FrameLayout frameLayout, AppCompatImageView appCompatImageView, FrameLayout frameLayout2) {
        this.rootView = frameLayout;
        this.bubble = appCompatImageView;
        this.layout = frameLayout2;
    }

    public static FlagBubbleBinding bind(View object) {
        AppCompatImageView appCompatImageView = (AppCompatImageView)object.findViewById(R.id.bubble);
        if (appCompatImageView != null) {
            FrameLayout frameLayout = (FrameLayout)object.findViewById(R.id.layout);
            if (frameLayout != null) {
                return new FlagBubbleBinding((FrameLayout)object, appCompatImageView, frameLayout);
            }
            object = "layout";
        } else {
            object = "bubble";
        }
        throw new NullPointerException("Missing required view with ID: ".concat((String)object));
    }

    public static FlagBubbleBinding inflate(LayoutInflater layoutInflater) {
        return FlagBubbleBinding.inflate(layoutInflater, null, false);
    }

    public static FlagBubbleBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean bl) {
        layoutInflater = layoutInflater.inflate(R.layout.flag_bubble, viewGroup, false);
        if (bl) {
            viewGroup.addView((View)layoutInflater);
        }
        return FlagBubbleBinding.bind((View)layoutInflater);
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }
}

