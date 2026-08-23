/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Handler
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.ImageView
 *  android.widget.ProgressBar
 *  android.widget.TextView
 */
package com.blackrussia.game.gui;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.blackrussia.game.gui.HudManager$$ExternalSyntheticLambda0;
import com.blackrussia.game.gui.HudManager$$ExternalSyntheticLambda1;
import com.blackrussia.game.gui.HudManager$$ExternalSyntheticLambda2;
import com.blackrussia.game.gui.util.Utils;
import com.nvidia.devtech.NvEventQueueActivity;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Formatter;

public class HudManager {
    public Activity activity;
    private Handler handler;
    public ImageView hud_gps;
    public ConstraintLayout hud_layout;
    public ImageView hud_menu;
    public ImageView hud_micro;
    public TextView hud_money;
    public ImageView hud_radar;
    public ArrayList<ImageView> hud_wanted;
    public ImageView hud_weapon;
    public ImageView hud_x2;
    public ImageView hud_zona;
    private final NvEventQueueActivity mContext = null;
    public ProgressBar progressArmor;
    public ProgressBar progressHP;

    public HudManager(Activity object) {
        ConstraintLayout constraintLayout;
        this.activity = object;
        this.hud_layout = constraintLayout = (ConstraintLayout)object.findViewById(2131362130);
        constraintLayout.setVisibility(8);
        this.handler = new Handler();
        this.progressArmor = (ProgressBar)object.findViewById(2131362084);
        this.progressHP = (ProgressBar)object.findViewById(2131362129);
        this.hud_radar = (ImageView)object.findViewById(2131362302);
        this.hud_micro = (ImageView)object.findViewById(2131362157);
        this.hud_gps = (ImageView)object.findViewById(2131362159);
        this.hud_zona = (ImageView)object.findViewById(2131362075);
        this.hud_x2 = (ImageView)object.findViewById(2131362160);
        this.hud_money = (TextView)object.findViewById(2131362132);
        this.hud_weapon = (ImageView)object.findViewById(2131362139);
        this.hud_menu = (ImageView)object.findViewById(2131362131);
        this.hud_wanted = object = new ArrayList();
        ((ArrayList)object).add((ImageView)this.activity.findViewById(2131362133));
        this.hud_wanted.add((ImageView)this.activity.findViewById(2131362134));
        this.hud_wanted.add((ImageView)this.activity.findViewById(2131362135));
        this.hud_wanted.add((ImageView)this.activity.findViewById(2131362136));
        this.hud_wanted.add((ImageView)this.activity.findViewById(2131362137));
        this.hud_wanted.add((ImageView)this.activity.findViewById(2131362138));
        this.hud_micro.setOnClickListener((View.OnClickListener)HudManager$$ExternalSyntheticLambda1.INSTANCE);
        this.hud_menu.setOnClickListener((View.OnClickListener)HudManager$$ExternalSyntheticLambda2.INSTANCE);
    }

    static /* synthetic */ void lambda$UpdateHudInfo$2(View view) {
        NvEventQueueActivity.getInstance().onWeaponChanged();
    }

    static /* synthetic */ void lambda$new$0(View view) {
    }

    static /* synthetic */ void lambda$new$1(View view) {
        NvEventQueueActivity.getInstance().showMenu();
        NvEventQueueActivity.getInstance().togglePlayer(1);
    }

    public void HideGps() {
        Utils.HideLayout((View)this.hud_gps, false);
    }

    public void HideHud() {
        Utils.HideLayout((View)this.hud_layout, false);
    }

    public void HideRadar() {
        Utils.HideLayout((View)this.hud_radar, false);
    }

    public void HideX2() {
        Utils.HideLayout((View)this.hud_x2, false);
    }

    public void HideZona() {
        Utils.HideLayout((View)this.hud_zona, false);
    }

    public void ShowClient() {
    }

    public void ShowGps() {
        Utils.ShowLayout((View)this.hud_gps, false);
    }

    public void ShowHud() {
        Utils.ShowLayout((View)this.hud_layout, false);
        Utils.HideLayout((View)this.hud_micro, false);
    }

    public void ShowRadar() {
        Utils.ShowLayout((View)this.hud_radar, false);
    }

    public void ShowX2() {
        Utils.ShowLayout((View)this.hud_x2, false);
    }

    public void ShowZona() {
        Utils.ShowLayout((View)this.hud_zona, false);
    }

    public void UpdateHudInfo(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.progressHP.setProgress(n);
        this.progressArmor.setProgress(n2);
        DecimalFormat decimalFormat = new DecimalFormat();
        Object object = DecimalFormatSymbols.getInstance();
        ((DecimalFormatSymbols)object).setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols((DecimalFormatSymbols)object);
        object = decimalFormat.format(n7);
        this.hud_money.setText((CharSequence)object);
        n = this.activity.getResources().getIdentifier(new Formatter().format("weapon_%d", n4).toString(), "drawable", this.activity.getPackageName());
        this.hud_weapon.setImageResource(n);
        this.hud_weapon.setOnClickListener((View.OnClickListener)HudManager$$ExternalSyntheticLambda0.INSTANCE);
        n = n8 > 6 ? 6 : n8;
        for (n2 = 0; n2 < n; ++n2) {
            this.hud_wanted.get(n2).setBackgroundResource(2131231046);
        }
    }
}

