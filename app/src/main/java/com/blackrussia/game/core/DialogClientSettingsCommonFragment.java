/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.CompoundButton
 *  android.widget.CompoundButton$OnCheckedChangeListener
 *  android.widget.SeekBar
 *  android.widget.SeekBar$OnSeekBarChangeListener
 */
package com.blackrussia.game.core;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import com.blackrussia.game.core.ISaveableFragment;
import com.blackrussia.game.core.NotificationDialogFragment;
import com.nvidia.devtech.NvEventQueueActivity;
import java.util.HashMap;

public class DialogClientSettingsCommonFragment
extends Fragment
implements ISaveableFragment {
    private boolean bChangeAllowed = true;
    private NvEventQueueActivity mContext = null;
    private SeekBar.OnSeekBarChangeListener mListenerSeekBars;
    private HashMap<ViewGroup, Drawable> mOldDrawables;
    private ViewGroup mParentView = null;
    private View mRootView = null;
    private SwitchCompat mSwitchCutout;
    private SwitchCompat mSwitchDialog;
    private SwitchCompat mSwitchFPSCounter;
    private SwitchCompat mSwitchHpArmour;
    private SwitchCompat mSwitchHud;
    private SwitchCompat mSwitchKeyboard;
    private SwitchCompat mSwitchOutfit;
    private SwitchCompat mSwitchPCMoney;
    private SwitchCompat mSwitchRadarrect;
    private SwitchCompat mSwitchSkyBox;

    public static DialogClientSettingsCommonFragment createInstance(String string2) {
        return new DialogClientSettingsCommonFragment();
    }

    private void setSeekBarListeners() {
        int n;
        int n2;
        CharSequence charSequence;
        CharSequence charSequence2;
        int n3;
        this.mListenerSeekBars = new SeekBar.OnSeekBarChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onProgressChanged(SeekBar seekBar, int n, boolean bl) {
                if (this.this$0.bChangeAllowed) {
                    this.this$0.passValuesToNative();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                this.this$0.mContext.onSettingsWindowSave();
            }
        };
        for (n3 = 14; n3 < 15; ++n3) {
            charSequence2 = new StringBuilder();
            ((StringBuilder)charSequence2).append("hud_element_pos_x_");
            ((StringBuilder)charSequence2).append(n3);
            charSequence2 = ((StringBuilder)charSequence2).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("hud_element_pos_y_");
            ((StringBuilder)charSequence).append(n3);
            charSequence = ((StringBuilder)charSequence).toString();
            n2 = this.mContext.getResources().getIdentifier((String)charSequence2, "id", this.mContext.getPackageName());
            n = this.mContext.getResources().getIdentifier((String)charSequence, "id", this.mContext.getPackageName());
            charSequence = (SeekBar)this.mRootView.findViewById(n2);
            charSequence2 = (SeekBar)this.mRootView.findViewById(n);
            if (charSequence != null) {
                charSequence.setOnSeekBarChangeListener(this.mListenerSeekBars);
            }
            if (charSequence2 == null) continue;
            charSequence2.setOnSeekBarChangeListener(this.mListenerSeekBars);
        }
        for (n3 = 14; n3 < 15; ++n3) {
            charSequence2 = new StringBuilder();
            ((StringBuilder)charSequence2).append("hud_element_scale_x_");
            ((StringBuilder)charSequence2).append(n3);
            charSequence2 = ((StringBuilder)charSequence2).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("hud_element_scale_y_");
            ((StringBuilder)charSequence).append(n3);
            charSequence = ((StringBuilder)charSequence).toString();
            n = this.mContext.getResources().getIdentifier((String)charSequence2, "id", this.mContext.getPackageName());
            n2 = this.mContext.getResources().getIdentifier((String)charSequence, "id", this.mContext.getPackageName());
            charSequence = (SeekBar)this.mRootView.findViewById(n);
            charSequence2 = (SeekBar)this.mRootView.findViewById(n2);
            if (charSequence != null) {
                charSequence.setOnSeekBarChangeListener(this.mListenerSeekBars);
            }
            if (charSequence2 == null) continue;
            charSequence2.setOnSeekBarChangeListener(this.mListenerSeekBars);
        }
    }

    @Override
    public void getValues() {
        SeekBar seekBar;
        int n;
        int n2;
        Object object;
        Object object2;
        int n3;
        this.mSwitchKeyboard.setChecked(this.mContext.getNativeKeyboardSettings());
        this.mSwitchCutout.setChecked(this.mContext.getNativeCutoutSettings());
        this.mSwitchFPSCounter.setChecked(this.mContext.getNativeFpsCounterSettings());
        this.mSwitchHpArmour.setChecked(this.mContext.getNativeHpArmourText());
        this.mSwitchOutfit.setChecked(this.mContext.getNativeOutfitGunsSettings());
        this.mSwitchPCMoney.setChecked(this.mContext.getNativePcMoney());
        this.mSwitchRadarrect.setChecked(this.mContext.getNativeRadarrect());
        this.mSwitchSkyBox.setChecked(this.mContext.getNativeSkyBox());
        this.mSwitchDialog.setChecked(this.mContext.getNativeDialog());
        this.mSwitchHud.setChecked(this.mContext.getNativeHud());
        this.bChangeAllowed = false;
        for (n3 = 14; n3 < 15; ++n3) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("hud_element_pos_x_");
            ((StringBuilder)object2).append(n3);
            object2 = ((StringBuilder)object2).toString();
            object = new StringBuilder();
            ((StringBuilder)object).append("hud_element_pos_y_");
            ((StringBuilder)object).append(n3);
            object = ((StringBuilder)object).toString();
            n2 = this.mContext.getResources().getIdentifier((String)object2, "id", this.mContext.getPackageName());
            n = this.mContext.getResources().getIdentifier((String)object, "id", this.mContext.getPackageName());
            seekBar = (SeekBar)this.mRootView.findViewById(n2);
            object = (SeekBar)this.mRootView.findViewById(n);
            object2 = this.mContext.getNativeHudElementPosition(n3);
            if (object2[0] == -1) {
                object2[0] = true;
            }
            if (object2[1] == -1) {
                object2[1] = true;
            }
            if (seekBar != null) {
                seekBar.setProgress((int)object2[0]);
            }
            if (object == null) continue;
            object.setProgress((int)object2[1]);
        }
        for (n3 = 14; n3 < 15; ++n3) {
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("hud_element_scale_x_");
            ((StringBuilder)object2).append(n3);
            object2 = ((StringBuilder)object2).toString();
            object = new StringBuilder();
            ((StringBuilder)object).append("hud_element_scale_y_");
            ((StringBuilder)object).append(n3);
            object = ((StringBuilder)object).toString();
            n2 = this.mContext.getResources().getIdentifier((String)object2, "id", this.mContext.getPackageName());
            n = this.mContext.getResources().getIdentifier((String)object, "id", this.mContext.getPackageName());
            seekBar = (SeekBar)this.mRootView.findViewById(n2);
            object2 = (SeekBar)this.mRootView.findViewById(n);
            object = this.mContext.getNativeHudElementScale(n3);
            if (object[0] == -1) {
                object[0] = true;
            }
            if (object[1] == -1) {
                object[1] = true;
            }
            if (seekBar != null && object[0] != -1) {
                seekBar.setProgress((int)object[0]);
            }
            if (object2 == null || object[1] == -1) continue;
            object2.setProgress((int)object[1]);
        }
        this.bChangeAllowed = true;
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mContext = (NvEventQueueActivity)this.getActivity();
        layoutInflater = layoutInflater.inflate(2131558458, viewGroup, false);
        this.mRootView = layoutInflater;
        this.mSwitchKeyboard = (SwitchCompat)layoutInflater.findViewById(2131362434);
        this.mSwitchCutout = (SwitchCompat)this.mRootView.findViewById(2131362435);
        this.mSwitchFPSCounter = (SwitchCompat)this.mRootView.findViewById(2131362437);
        this.mSwitchHpArmour = (SwitchCompat)this.mRootView.findViewById(2131362439);
        this.mSwitchOutfit = (SwitchCompat)this.mRootView.findViewById(2131362440);
        this.mSwitchRadarrect = (SwitchCompat)this.mRootView.findViewById(2131362442);
        this.mSwitchPCMoney = (SwitchCompat)this.mRootView.findViewById(2131362441);
        this.mSwitchSkyBox = (SwitchCompat)this.mRootView.findViewById(2131362443);
        this.mSwitchDialog = (SwitchCompat)this.mRootView.findViewById(2131362436);
        this.mSwitchHud = (SwitchCompat)this.mRootView.findViewById(2131362438);
        this.getValues();
        this.setSeekBarListeners();
        this.mSwitchCutout.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeCutoutSettings(bl);
                new NotificationDialogFragment().show(this.this$0.mContext.getSupportFragmentManager(), "missiles");
            }
        });
        this.mSwitchSkyBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeSkyBox(bl);
            }
        });
        this.mSwitchDialog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeDialog(bl);
            }
        });
        this.mSwitchHud.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeHud(bl);
            }
        });
        this.mSwitchKeyboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeKeyboardSettings(bl);
            }
        });
        this.mSwitchRadarrect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeRadarrect(bl);
            }
        });
        this.mSwitchPCMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativePcMoney(bl);
            }
        });
        this.mSwitchOutfit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeOutfitGunsSettings(bl);
            }
        });
        this.mSwitchHpArmour.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeHpArmourText(bl);
            }
        });
        this.mSwitchFPSCounter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(this){
            final DialogClientSettingsCommonFragment this$0;
            {
                this.this$0 = dialogClientSettingsCommonFragment;
            }

            public void onCheckedChanged(CompoundButton compoundButton, boolean bl) {
                this.this$0.mContext.setNativeFpsCounterSettings(bl);
            }
        });
        return this.mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putSerializable("android:support:fragments", null);
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("android:support:fragments", null);
    }

    @Override
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(null);
    }

    public void passValuesToNative() {
        int n;
        int n2;
        CharSequence charSequence;
        CharSequence charSequence2;
        int n3;
        for (n3 = 14; n3 < 15; ++n3) {
            charSequence2 = new StringBuilder();
            ((StringBuilder)charSequence2).append("hud_element_pos_x_");
            ((StringBuilder)charSequence2).append(n3);
            charSequence2 = ((StringBuilder)charSequence2).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("hud_element_pos_y_");
            ((StringBuilder)charSequence).append(n3);
            charSequence = ((StringBuilder)charSequence).toString();
            n2 = this.mContext.getResources().getIdentifier((String)charSequence2, "id", this.mContext.getPackageName());
            n = this.mContext.getResources().getIdentifier((String)charSequence, "id", this.mContext.getPackageName());
            charSequence2 = (SeekBar)this.mRootView.findViewById(n2);
            charSequence = (SeekBar)this.mRootView.findViewById(n);
            n2 = -1;
            n = -1;
            if (charSequence2 != null) {
                n2 = charSequence2.getProgress();
            }
            if (charSequence != null) {
                n = charSequence.getProgress();
            }
            NvEventQueueActivity.setNativeHudElementPosition(n3, n2, n);
        }
        for (n3 = 14; n3 < 15; ++n3) {
            charSequence2 = new StringBuilder();
            ((StringBuilder)charSequence2).append("hud_element_scale_x_");
            ((StringBuilder)charSequence2).append(n3);
            charSequence2 = ((StringBuilder)charSequence2).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("hud_element_scale_y_");
            ((StringBuilder)charSequence).append(n3);
            charSequence = ((StringBuilder)charSequence).toString();
            n2 = this.mContext.getResources().getIdentifier((String)charSequence2, "id", this.mContext.getPackageName());
            n = this.mContext.getResources().getIdentifier((String)charSequence, "id", this.mContext.getPackageName());
            charSequence = (SeekBar)this.mRootView.findViewById(n2);
            charSequence2 = (SeekBar)this.mRootView.findViewById(n);
            n2 = -1;
            n = -1;
            if (charSequence != null) {
                n2 = charSequence.getProgress();
            }
            if (charSequence2 != null) {
                n = charSequence2.getProgress();
            }
            NvEventQueueActivity.setNativeHudElementScale(n3, n2, n);
        }
    }

    @Override
    public void save() {
    }
}

