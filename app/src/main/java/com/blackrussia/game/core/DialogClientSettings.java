/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 */
package com.blackrussia.game.core;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.blackrussia.game.core.DialogClientSettingsAdapter;
import com.blackrussia.game.core.DialogClientSettingsColorFragment;
import com.blackrussia.game.core.DialogClientSettingsCommonFragment;
import com.blackrussia.game.core.DialogClientSettingsFPSFragment;
import com.blackrussia.game.core.DialogClientSettingsHUDFragment;
import com.blackrussia.game.core.DialogClientSettingsWeaponsFragment;
import com.google.android.material.tabs.TabLayout;
import com.nvidia.devtech.NvEventQueueActivity;

public class DialogClientSettings
extends DialogFragment {
    static final int mSettingsComonEnd = 15;
    static final int mSettingsComonStart = 14;
    static final int mSettingsHudCount = 10;
    static final int mSettingsHudFPSEnd = 12;
    static final int mSettingsHudFPSStart = 10;
    static final int mSettingsWeaponsEnd = 14;
    static final int mSettingsWeaponsStart = 12;
    NvEventQueueActivity mContext = null;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater object, ViewGroup viewGroup, Bundle bundle) {
        viewGroup = object.inflate(2131558456, null, false);
        this.tabLayout = (TabLayout)viewGroup.findViewById(2131362444);
        this.viewPager = (ViewPager)viewGroup.findViewById(2131362219);
        object = new DialogClientSettingsAdapter(this.getChildFragmentManager(), 0);
        ((DialogClientSettingsAdapter)object).addFragment("\u041e\u0441\u043d\u043e\u0432\u043d\u043e\u0435", DialogClientSettingsCommonFragment.createInstance("common"));
        ((DialogClientSettingsAdapter)object).addFragment("\u0426\u0432\u0435\u0442\u0430", DialogClientSettingsColorFragment.createInstance("colors"));
        ((DialogClientSettingsAdapter)object).addFragment("\u041f\u0435\u0440\u0432\u043e\u0435 \u043b\u0438\u0446\u043e", DialogClientSettingsFPSFragment.createInstance("fps").setRoot((ViewGroup)viewGroup.findViewById(2131362207)));
        ((DialogClientSettingsAdapter)object).addFragment("HUD", DialogClientSettingsHUDFragment.createInstance("hud").setRoot((ViewGroup)viewGroup.findViewById(2131362207)));
        ((DialogClientSettingsAdapter)object).addFragment("\u041e\u0440\u0443\u0436\u0438\u0435", DialogClientSettingsWeaponsFragment.createInstance("weapons").setRoot((ViewGroup)viewGroup.findViewById(2131362207)));
        this.viewPager.setAdapter((PagerAdapter)object);
        this.tabLayout.setupWithViewPager(this.viewPager);
        this.getDialog().getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
        this.getDialog().getWindow().setDimAmount(0.0f);
        this.mContext = (NvEventQueueActivity)this.getActivity();
        ((AppCompatButton)viewGroup.findViewById(2131362004)).setOnClickListener(new View.OnClickListener(this){
            final DialogClientSettings this$0;
            {
                this.this$0 = dialogClientSettings;
            }

            public void onClick(View view) {
                this.this$0.mContext.onSettingsWindowSave();
                this.this$0.getDialog().dismiss();
            }
        });
        this.setCancelable(false);
        return viewGroup;
    }
}

