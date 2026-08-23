/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.graphics.Point
 */
package com.skydoves.colorpickerview.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import com.skydoves.colorpickerview.ColorPickerView;

public class ColorPickerPreferenceManager {
    protected static final String AlphaSlider = "_SLIDER_ALPHA";
    protected static final String BrightnessSlider = "_SLIDER_BRIGHTNESS";
    protected static final String COLOR = "_COLOR";
    protected static final String SelectorX = "_SELECTOR_X";
    protected static final String SelectorY = "_SELECTOR_Y";
    private static ColorPickerPreferenceManager colorPickerPreferenceManager;
    private SharedPreferences sharedPreferences;

    private ColorPickerPreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
    }

    public static ColorPickerPreferenceManager getInstance(Context context) {
        if (colorPickerPreferenceManager == null) {
            colorPickerPreferenceManager = new ColorPickerPreferenceManager(context);
        }
        return colorPickerPreferenceManager;
    }

    public ColorPickerPreferenceManager clearSavedAllData() {
        this.sharedPreferences.edit().clear().apply();
        return colorPickerPreferenceManager;
    }

    public ColorPickerPreferenceManager clearSavedAlphaSliderPosition(String string2) {
        this.sharedPreferences.edit().remove(this.getAlphaSliderName(string2)).apply();
        return colorPickerPreferenceManager;
    }

    public ColorPickerPreferenceManager clearSavedBrightnessSlider(String string2) {
        this.sharedPreferences.edit().remove(this.getBrightnessSliderName(string2)).apply();
        return colorPickerPreferenceManager;
    }

    public ColorPickerPreferenceManager clearSavedColor(String string2) {
        this.sharedPreferences.edit().remove(this.getColorName(string2)).apply();
        return colorPickerPreferenceManager;
    }

    public ColorPickerPreferenceManager clearSavedSelectorPosition(String string2) {
        this.sharedPreferences.edit().remove(this.getSelectorXName(string2)).apply();
        this.sharedPreferences.edit().remove(this.getSelectorYName(string2)).apply();
        return colorPickerPreferenceManager;
    }

    protected String getAlphaSliderName(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(AlphaSlider);
        return stringBuilder.toString();
    }

    public int getAlphaSliderPosition(String string2, int n) {
        return this.sharedPreferences.getInt(this.getAlphaSliderName(string2), n);
    }

    protected String getBrightnessSliderName(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(BrightnessSlider);
        return stringBuilder.toString();
    }

    public int getBrightnessSliderPosition(String string2, int n) {
        return this.sharedPreferences.getInt(this.getBrightnessSliderName(string2), n);
    }

    public int getColor(String string2, int n) {
        return this.sharedPreferences.getInt(this.getColorName(string2), n);
    }

    protected String getColorName(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(COLOR);
        return stringBuilder.toString();
    }

    public Point getSelectorPosition(String string2, Point point) {
        return new Point(this.sharedPreferences.getInt(this.getSelectorXName(string2), point.x), this.sharedPreferences.getInt(this.getSelectorYName(string2), point.y));
    }

    protected String getSelectorXName(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(SelectorX);
        return stringBuilder.toString();
    }

    protected String getSelectorYName(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(SelectorY);
        return stringBuilder.toString();
    }

    public void restoreColorPickerData(ColorPickerView colorPickerView) {
        if (colorPickerView != null && colorPickerView.getPreferenceName() != null) {
            String string2 = colorPickerView.getPreferenceName();
            colorPickerView.setPureColor(this.getColor(string2, -1));
            Point point = new Point(colorPickerView.getMeasuredWidth() / 2, colorPickerView.getMeasuredHeight() / 2);
            colorPickerView.moveSelectorPoint(this.getSelectorPosition((String)string2, (Point)point).x, this.getSelectorPosition((String)string2, (Point)point).y, this.getColor(string2, -1));
        }
    }

    public void saveColorPickerData(ColorPickerView colorPickerView) {
        if (colorPickerView != null && colorPickerView.getPreferenceName() != null) {
            String string2 = colorPickerView.getPreferenceName();
            this.setColor(string2, colorPickerView.getColor());
            this.setSelectorPosition(string2, colorPickerView.getSelectedPoint());
            if (colorPickerView.getAlphaSlideBar() != null) {
                this.setAlphaSliderPosition(string2, colorPickerView.getAlphaSlideBar().getSelectedX());
            }
            if (colorPickerView.getBrightnessSlider() != null) {
                this.setBrightnessSliderPosition(string2, colorPickerView.getBrightnessSlider().getSelectedX());
            }
        }
    }

    public ColorPickerPreferenceManager setAlphaSliderPosition(String string2, int n) {
        this.sharedPreferences.edit().putInt(this.getAlphaSliderName(string2), n).apply();
        return colorPickerPreferenceManager;
    }

    public ColorPickerPreferenceManager setBrightnessSliderPosition(String string2, int n) {
        this.sharedPreferences.edit().putInt(this.getBrightnessSliderName(string2), n).apply();
        return colorPickerPreferenceManager;
    }

    public ColorPickerPreferenceManager setColor(String string2, int n) {
        this.sharedPreferences.edit().putInt(this.getColorName(string2), n).apply();
        return colorPickerPreferenceManager;
    }

    public ColorPickerPreferenceManager setSelectorPosition(String string2, Point point) {
        this.sharedPreferences.edit().putInt(this.getSelectorXName(string2), point.x).apply();
        this.sharedPreferences.edit().putInt(this.getSelectorYName(string2), point.y).apply();
        return colorPickerPreferenceManager;
    }
}

