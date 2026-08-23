/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.DialogInterface
 *  android.content.DialogInterface$OnClickListener
 *  android.graphics.Color
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.widget.FrameLayout$LayoutParams
 */
package com.blackrussia.game.core;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import com.blackrussia.game.core.ISaveableFragment;
import com.nvidia.devtech.NvEventQueueActivity;
import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.listeners.ColorPickerViewListener;

public class DialogClientSettingsColorFragment
extends Fragment
implements ISaveableFragment {
    private AppCompatButton mButtonAmmoColor;
    private AppCompatButton mButtonArmorColor;
    private AppCompatButton mButtonArmorTextColor;
    private AppCompatButton mButtonHPColor;
    private AppCompatButton mButtonHpTextColor;
    private AppCompatButton mButtonMoneyColor;
    private AppCompatButton mButtonRadarColor;
    private AppCompatButton mButtonWantedColor;
    private NvEventQueueActivity mContext = null;

    public static DialogClientSettingsColorFragment createInstance(String string2) {
        return new DialogClientSettingsColorFragment();
    }

    @Override
    public void getValues() {
        String string2 = this.mContext.getHudElementColor(0);
        this.mButtonHPColor.setBackgroundColor(Color.parseColor((String)string2));
        string2 = this.mContext.getHudElementColor(1);
        this.mButtonArmorColor.setBackgroundColor(Color.parseColor((String)string2));
        string2 = this.mContext.getHudElementColor(2);
        this.mButtonMoneyColor.setBackgroundColor(Color.parseColor((String)string2));
        string2 = this.mContext.getHudElementColor(3);
        this.mButtonWantedColor.setBackgroundColor(Color.parseColor((String)string2));
        string2 = this.mContext.getHudElementColor(4);
        this.mButtonHpTextColor.setBackgroundColor(Color.parseColor((String)string2));
        string2 = this.mContext.getHudElementColor(5);
        this.mButtonArmorTextColor.setBackgroundColor(Color.parseColor((String)string2));
        string2 = this.mContext.getHudElementColor(6);
        this.mButtonRadarColor.setBackgroundColor(Color.parseColor((String)string2));
        string2 = this.mContext.getHudElementColor(9);
        this.mButtonAmmoColor.setBackgroundColor(Color.parseColor((String)string2));
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContext = (NvEventQueueActivity)this.getActivity();
        layoutInflater = layoutInflater.inflate(2131558457, viewGroup, false);
        this.mButtonHPColor = (AppCompatButton)layoutInflater.findViewById(2131361928);
        this.mButtonArmorColor = (AppCompatButton)layoutInflater.findViewById(2131361926);
        this.mButtonMoneyColor = (AppCompatButton)layoutInflater.findViewById(2131361930);
        this.mButtonWantedColor = (AppCompatButton)layoutInflater.findViewById(2131361932);
        this.mButtonHpTextColor = (AppCompatButton)layoutInflater.findViewById(2131361929);
        this.mButtonArmorTextColor = (AppCompatButton)layoutInflater.findViewById(2131361927);
        this.mButtonRadarColor = (AppCompatButton)layoutInflater.findViewById(2131361931);
        this.mButtonAmmoColor = (AppCompatButton)layoutInflater.findViewById(2131361925);
        this.getValues();
        this.mButtonRadarColor.setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettingsColorFragment this$0;
            {
                this.this$0 = dialogClientSettingsColorFragment;
            }

            public void onClick(View view) {
                ColorPickerDialog.Builder builder = ((ColorPickerDialog.Builder)new ColorPickerDialog.Builder((Context)this.this$0.mContext).setPreferenceName("color6").setPositiveButton((CharSequence)"\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c", (ColorPickerViewListener)new ColorEnvelopeListener(this){
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void onColorSelected(ColorEnvelope object, boolean bl) {
                        object = ((ColorEnvelope)object).getArgb();
                        this.this$1.this$0.mContext.setNativeHudElementColor(6, (int)object[0], (int)object[1], (int)object[2], (int)object[3]);
                        this.this$1.this$0.getValues();
                    }
                }).setNegativeButton("\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    public void onClick(DialogInterface dialogInterface, int n) {
                        dialogInterface.dismiss();
                    }
                })).attachAlphaSlideBar(true).attachBrightnessSlideBar(true).setBottomSpace(2);
                view = (FrameLayout.LayoutParams)builder.getColorPickerView().getLayoutParams();
                view.height = (int)((float)view.height * 0.25f);
                view.width = (int)((float)view.width * 0.25f);
                view.topMargin = 10;
                view.bottomMargin = 0;
                builder.getColorPickerView().setLayoutParams((ViewGroup.LayoutParams)view);
                builder.show();
            }
        });
        this.mButtonAmmoColor.setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettingsColorFragment this$0;
            {
                this.this$0 = dialogClientSettingsColorFragment;
            }

            public void onClick(View object) {
                object = ((ColorPickerDialog.Builder)new ColorPickerDialog.Builder((Context)this.this$0.mContext).setPreferenceName("color9").setPositiveButton((CharSequence)"\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c", (ColorPickerViewListener)new ColorEnvelopeListener(this){
                    final 2 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void onColorSelected(ColorEnvelope object, boolean bl) {
                        object = ((ColorEnvelope)object).getArgb();
                        this.this$1.this$0.mContext.setNativeHudElementColor(9, (int)object[0], (int)object[1], (int)object[2], (int)object[3]);
                        this.this$1.this$0.getValues();
                    }
                }).setNegativeButton("\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
                    final 2 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    public void onClick(DialogInterface dialogInterface, int n) {
                        dialogInterface.dismiss();
                    }
                })).attachAlphaSlideBar(true).attachBrightnessSlideBar(true).setBottomSpace(2);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)((ColorPickerDialog.Builder)object).getColorPickerView().getLayoutParams();
                layoutParams.height = (int)((float)layoutParams.height * 0.25f);
                layoutParams.width = (int)((float)layoutParams.width * 0.25f);
                layoutParams.topMargin = 10;
                layoutParams.bottomMargin = 0;
                ((ColorPickerDialog.Builder)object).getColorPickerView().setLayoutParams((ViewGroup.LayoutParams)layoutParams);
                ((AlertDialog.Builder)object).show();
            }
        });
        this.mButtonHPColor.setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettingsColorFragment this$0;
            {
                this.this$0 = dialogClientSettingsColorFragment;
            }

            public void onClick(View view) {
                ColorPickerDialog.Builder builder = ((ColorPickerDialog.Builder)new ColorPickerDialog.Builder((Context)this.this$0.mContext).setPreferenceName("color0").setPositiveButton((CharSequence)"\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c", (ColorPickerViewListener)new ColorEnvelopeListener(this){
                    final 3 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void onColorSelected(ColorEnvelope object, boolean bl) {
                        object = ((ColorEnvelope)object).getArgb();
                        this.this$1.this$0.mContext.setNativeHudElementColor(0, (int)object[0], (int)object[1], (int)object[2], (int)object[3]);
                        this.this$1.this$0.getValues();
                    }
                }).setNegativeButton("\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
                    final 3 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    public void onClick(DialogInterface dialogInterface, int n) {
                        dialogInterface.dismiss();
                    }
                })).attachAlphaSlideBar(true).attachBrightnessSlideBar(true).setBottomSpace(2);
                view = (FrameLayout.LayoutParams)builder.getColorPickerView().getLayoutParams();
                view.height = (int)((float)view.height * 0.25f);
                view.width = (int)((float)view.width * 0.25f);
                view.topMargin = 10;
                view.bottomMargin = 0;
                builder.getColorPickerView().setLayoutParams((ViewGroup.LayoutParams)view);
                builder.show();
            }
        });
        this.mButtonArmorColor.setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettingsColorFragment this$0;
            {
                this.this$0 = dialogClientSettingsColorFragment;
            }

            public void onClick(View object) {
                object = ((ColorPickerDialog.Builder)new ColorPickerDialog.Builder((Context)this.this$0.mContext).setPreferenceName("color1").setPositiveButton((CharSequence)"\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c", (ColorPickerViewListener)new ColorEnvelopeListener(this){
                    final 4 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void onColorSelected(ColorEnvelope object, boolean bl) {
                        object = ((ColorEnvelope)object).getArgb();
                        this.this$1.this$0.mContext.setNativeHudElementColor(1, (int)object[0], (int)object[1], (int)object[2], (int)object[3]);
                        this.this$1.this$0.getValues();
                    }
                }).setNegativeButton("\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
                    final 4 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    public void onClick(DialogInterface dialogInterface, int n) {
                        dialogInterface.dismiss();
                    }
                })).attachAlphaSlideBar(true).attachBrightnessSlideBar(true).setBottomSpace(2);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)((ColorPickerDialog.Builder)object).getColorPickerView().getLayoutParams();
                layoutParams.height = (int)((float)layoutParams.height * 0.25f);
                layoutParams.width = (int)((float)layoutParams.width * 0.25f);
                layoutParams.topMargin = 10;
                layoutParams.bottomMargin = 0;
                ((ColorPickerDialog.Builder)object).getColorPickerView().setLayoutParams((ViewGroup.LayoutParams)layoutParams);
                ((AlertDialog.Builder)object).show();
            }
        });
        this.mButtonMoneyColor.setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettingsColorFragment this$0;
            {
                this.this$0 = dialogClientSettingsColorFragment;
            }

            public void onClick(View view) {
                ColorPickerDialog.Builder builder = ((ColorPickerDialog.Builder)new ColorPickerDialog.Builder((Context)this.this$0.mContext).setPreferenceName("color2").setPositiveButton((CharSequence)"\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c", (ColorPickerViewListener)new ColorEnvelopeListener(this){
                    final 5 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void onColorSelected(ColorEnvelope object, boolean bl) {
                        object = ((ColorEnvelope)object).getArgb();
                        this.this$1.this$0.mContext.setNativeHudElementColor(2, (int)object[0], (int)object[1], (int)object[2], (int)object[3]);
                        this.this$1.this$0.getValues();
                    }
                }).setNegativeButton("\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
                    final 5 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    public void onClick(DialogInterface dialogInterface, int n) {
                        dialogInterface.dismiss();
                    }
                })).attachAlphaSlideBar(true).attachBrightnessSlideBar(true).setBottomSpace(2);
                view = (FrameLayout.LayoutParams)builder.getColorPickerView().getLayoutParams();
                view.height = (int)((float)view.height * 0.25f);
                view.width = (int)((float)view.width * 0.25f);
                view.topMargin = 10;
                view.bottomMargin = 0;
                builder.getColorPickerView().setLayoutParams((ViewGroup.LayoutParams)view);
                builder.show();
            }
        });
        this.mButtonWantedColor.setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettingsColorFragment this$0;
            {
                this.this$0 = dialogClientSettingsColorFragment;
            }

            public void onClick(View view) {
                ColorPickerDialog.Builder builder = ((ColorPickerDialog.Builder)new ColorPickerDialog.Builder((Context)this.this$0.mContext).setPreferenceName("color3").setPositiveButton((CharSequence)"\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c", (ColorPickerViewListener)new ColorEnvelopeListener(this){
                    final 6 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void onColorSelected(ColorEnvelope object, boolean bl) {
                        object = ((ColorEnvelope)object).getArgb();
                        this.this$1.this$0.mContext.setNativeHudElementColor(3, (int)object[0], (int)object[1], (int)object[2], (int)object[3]);
                        this.this$1.this$0.getValues();
                    }
                }).setNegativeButton("\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
                    final 6 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    public void onClick(DialogInterface dialogInterface, int n) {
                        dialogInterface.dismiss();
                    }
                })).attachAlphaSlideBar(true).attachBrightnessSlideBar(true).setBottomSpace(2);
                view = (FrameLayout.LayoutParams)builder.getColorPickerView().getLayoutParams();
                view.height = (int)((float)view.height * 0.25f);
                view.width = (int)((float)view.width * 0.25f);
                view.topMargin = 10;
                view.bottomMargin = 0;
                builder.getColorPickerView().setLayoutParams((ViewGroup.LayoutParams)view);
                builder.show();
            }
        });
        this.mButtonHpTextColor.setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettingsColorFragment this$0;
            {
                this.this$0 = dialogClientSettingsColorFragment;
            }

            public void onClick(View object) {
                object = ((ColorPickerDialog.Builder)new ColorPickerDialog.Builder((Context)this.this$0.mContext).setPreferenceName("color4").setPositiveButton((CharSequence)"\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c", (ColorPickerViewListener)new ColorEnvelopeListener(this){
                    final 7 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void onColorSelected(ColorEnvelope object, boolean bl) {
                        object = ((ColorEnvelope)object).getArgb();
                        this.this$1.this$0.mContext.setNativeHudElementColor(4, (int)object[0], (int)object[1], (int)object[2], (int)object[3]);
                        this.this$1.this$0.getValues();
                    }
                }).setNegativeButton("\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
                    final 7 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    public void onClick(DialogInterface dialogInterface, int n) {
                        dialogInterface.dismiss();
                    }
                })).attachAlphaSlideBar(true).attachBrightnessSlideBar(true).setBottomSpace(2);
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)((ColorPickerDialog.Builder)object).getColorPickerView().getLayoutParams();
                layoutParams.height = (int)((float)layoutParams.height * 0.25f);
                layoutParams.width = (int)((float)layoutParams.width * 0.25f);
                layoutParams.topMargin = 10;
                layoutParams.bottomMargin = 0;
                ((ColorPickerDialog.Builder)object).getColorPickerView().setLayoutParams((ViewGroup.LayoutParams)layoutParams);
                ((AlertDialog.Builder)object).show();
            }
        });
        this.mButtonArmorTextColor.setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettingsColorFragment this$0;
            {
                this.this$0 = dialogClientSettingsColorFragment;
            }

            public void onClick(View view) {
                ColorPickerDialog.Builder builder = ((ColorPickerDialog.Builder)new ColorPickerDialog.Builder((Context)this.this$0.mContext).setPreferenceName("color5").setPositiveButton((CharSequence)"\u041f\u0440\u0438\u043c\u0435\u043d\u0438\u0442\u044c", (ColorPickerViewListener)new ColorEnvelopeListener(this){
                    final 8 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void onColorSelected(ColorEnvelope object, boolean bl) {
                        object = ((ColorEnvelope)object).getArgb();
                        this.this$1.this$0.mContext.setNativeHudElementColor(5, (int)object[0], (int)object[1], (int)object[2], (int)object[3]);
                        this.this$1.this$0.getValues();
                    }
                }).setNegativeButton("\u0417\u0430\u043a\u0440\u044b\u0442\u044c", new DialogInterface.OnClickListener(this){
                    final 8 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    public void onClick(DialogInterface dialogInterface, int n) {
                        dialogInterface.dismiss();
                    }
                })).attachAlphaSlideBar(true).attachBrightnessSlideBar(true).setBottomSpace(0);
                view = (FrameLayout.LayoutParams)builder.getColorPickerView().getLayoutParams();
                view.height = (int)((float)view.height * 0.25f);
                view.width = (int)((float)view.width * 0.25f);
                view.topMargin = 10;
                view.bottomMargin = 0;
                builder.getColorPickerView().setLayoutParams((ViewGroup.LayoutParams)view);
                builder.show();
            }
        });
        return layoutInflater;
    }

    @Override
    public void save() {
    }
}

