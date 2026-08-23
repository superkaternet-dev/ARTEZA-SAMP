/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewTreeObserver$OnGlobalLayoutListener
 *  android.widget.SeekBar$OnSeekBarChangeListener
 */
package com.blackrussia.game.core;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import com.nvidia.devtech.NvEventQueueActivity;
import java.util.HashMap;

public class DialogClientSettingsOld
extends Dialog {
    static final int mSettingsHudCount = 12;
    private AppCompatButton mButtonAmmoColor;
    private AppCompatButton mButtonArmorColor;
    private AppCompatButton mButtonArmorTextColor;
    private AppCompatButton mButtonHPColor;
    private AppCompatButton mButtonHpTextColor;
    private AppCompatButton mButtonMoneyColor;
    private AppCompatButton mButtonRadarColor;
    private AppCompatButton mButtonWantedColor;
    private boolean mChangingAllowed = false;
    private NvEventQueueActivity mContext;
    private SeekBar.OnSeekBarChangeListener mListenerSeekBars;
    private HashMap<ViewGroup, Drawable> mOldDrawables;
    private SwitchCompat mSwitchCutout;
    private SwitchCompat mSwitchFPSCounter;
    private SwitchCompat mSwitchHpArmour;
    private SwitchCompat mSwitchKeyboard;
    private SwitchCompat mSwitchOutfit;
    private SwitchCompat mSwitchPCMoney;
    private SwitchCompat mSwitchRadarrect;
    private SwitchCompat mSwitchSkyBox;

    public DialogClientSettingsOld(Context context) {
        super(context);
        this.mContext = (NvEventQueueActivity)context;
    }

    static /* synthetic */ boolean access$002(DialogClientSettingsOld dialogClientSettingsOld, boolean bl) {
        dialogClientSettingsOld.mChangingAllowed = bl;
        return bl;
    }

    private void getColors() {
    }

    private void getValues() {
        this.mChangingAllowed = false;
        this.getColors();
        this.mChangingAllowed = true;
    }

    private void makeAllElementsInvisible(ViewGroup viewGroup, View view, boolean bl) {
        View view2;
        if (bl) {
            view2 = new HashMap();
            this.mOldDrawables = view2;
            view2.put(viewGroup, viewGroup.getBackground());
            viewGroup.setBackground((Drawable)new ColorDrawable(0));
        }
        if (viewGroup == null) {
            return;
        }
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            view2 = viewGroup.getChildAt(i);
            if (view2 instanceof ViewGroup) {
                this.makeAllElementsInvisible((ViewGroup)view2, view, false);
                this.mOldDrawables.put((ViewGroup)view2, ((ViewGroup)view2).getBackground());
                view2.setBackground((Drawable)new ColorDrawable(0));
                continue;
            }
            if (view2 == view) continue;
            view2.setAlpha(0.0f);
        }
    }

    private void makeAllElementsVisible(ViewGroup viewGroup, View view, boolean bl) {
        if (bl) {
            for (ViewGroup viewGroup2 : this.mOldDrawables.keySet()) {
                viewGroup2.setBackground(this.mOldDrawables.get(viewGroup2));
            }
        }
        if (viewGroup == null) {
            return;
        }
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            ViewGroup viewGroup2;
            viewGroup2 = viewGroup.getChildAt(i);
            if (viewGroup2 instanceof ViewGroup) {
                this.makeAllElementsVisible(viewGroup2, view, false);
                continue;
            }
            if (viewGroup2 == view) continue;
            viewGroup2.setAlpha(1.0f);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.passValuesToNative();
        this.mContext.onSettingsWindowSave();
        this.dismiss();
    }

    protected void onCreate(Bundle bundle) {
        this.mChangingAllowed = false;
        super.onCreate(bundle);
        this.setContentView(2131558461);
        this.findViewById(2131362380).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(this){
            final DialogClientSettingsOld this$0;
            {
                this.this$0 = dialogClientSettingsOld;
            }

            public void onGlobalLayout() {
                DialogClientSettingsOld.access$002(this.this$0, true);
            }
        });
        this.getWindow().setDimAmount(0.0f);
        this.getWindow().setGravity(17);
        this.getWindow().setLayout(-1, -1);
        this.getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
        if (Build.VERSION.SDK_INT < 28) {
            this.findViewById(2131362435).setVisibility(8);
        }
        this.setCancelable(false);
    }

    public void passValuesToNative() {
    }
}

