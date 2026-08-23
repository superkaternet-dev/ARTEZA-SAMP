/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.FrameLayout
 *  android.widget.ScrollView
 *  android.widget.Space
 */
package com.skydoves.colorpickerview.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.Space;
import androidx.viewbinding.ViewBinding;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.R;
import com.skydoves.colorpickerview.sliders.AlphaSlideBar;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

public final class DialogColorpickerBinding
implements ViewBinding {
    public final AlphaSlideBar alphaSlideBar;
    public final FrameLayout alphaSlideBarFrame;
    public final BrightnessSlideBar brightnessSlideBar;
    public final FrameLayout brightnessSlideBarFrame;
    public final ColorPickerView colorPickerView;
    public final FrameLayout colorPickerViewFrame;
    private final ScrollView rootView;
    public final Space spaceBottom;

    private DialogColorpickerBinding(ScrollView scrollView, AlphaSlideBar alphaSlideBar, FrameLayout frameLayout, BrightnessSlideBar brightnessSlideBar, FrameLayout frameLayout2, ColorPickerView colorPickerView, FrameLayout frameLayout3, Space space) {
        this.rootView = scrollView;
        this.alphaSlideBar = alphaSlideBar;
        this.alphaSlideBarFrame = frameLayout;
        this.brightnessSlideBar = brightnessSlideBar;
        this.brightnessSlideBarFrame = frameLayout2;
        this.colorPickerView = colorPickerView;
        this.colorPickerViewFrame = frameLayout3;
        this.spaceBottom = space;
    }

    public static DialogColorpickerBinding bind(View object) {
        AlphaSlideBar alphaSlideBar = (AlphaSlideBar)object.findViewById(R.id.alphaSlideBar);
        if (alphaSlideBar != null) {
            FrameLayout frameLayout = (FrameLayout)object.findViewById(R.id.alphaSlideBarFrame);
            if (frameLayout != null) {
                BrightnessSlideBar brightnessSlideBar = (BrightnessSlideBar)object.findViewById(R.id.brightnessSlideBar);
                if (brightnessSlideBar != null) {
                    FrameLayout frameLayout2 = (FrameLayout)object.findViewById(R.id.brightnessSlideBarFrame);
                    if (frameLayout2 != null) {
                        ColorPickerView colorPickerView = (ColorPickerView)object.findViewById(R.id.colorPickerView);
                        if (colorPickerView != null) {
                            FrameLayout frameLayout3 = (FrameLayout)object.findViewById(R.id.colorPickerViewFrame);
                            if (frameLayout3 != null) {
                                Space space = (Space)object.findViewById(R.id.space_bottom);
                                if (space != null) {
                                    return new DialogColorpickerBinding((ScrollView)object, alphaSlideBar, frameLayout, brightnessSlideBar, frameLayout2, colorPickerView, frameLayout3, space);
                                }
                                object = "spaceBottom";
                            } else {
                                object = "colorPickerViewFrame";
                            }
                        } else {
                            object = "colorPickerView";
                        }
                    } else {
                        object = "brightnessSlideBarFrame";
                    }
                } else {
                    object = "brightnessSlideBar";
                }
            } else {
                object = "alphaSlideBarFrame";
            }
        } else {
            object = "alphaSlideBar";
        }
        throw new NullPointerException("Missing required view with ID: ".concat((String)object));
    }

    public static DialogColorpickerBinding inflate(LayoutInflater layoutInflater) {
        return DialogColorpickerBinding.inflate(layoutInflater, null, false);
    }

    public static DialogColorpickerBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean bl) {
        layoutInflater = layoutInflater.inflate(R.layout.dialog_colorpicker, viewGroup, false);
        if (bl) {
            viewGroup.addView((View)layoutInflater);
        }
        return DialogColorpickerBinding.bind((View)layoutInflater);
    }

    public ScrollView getRoot() {
        return this.rootView;
    }
}

