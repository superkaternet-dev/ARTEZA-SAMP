/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.ColorDrawable
 *  android.graphics.drawable.Drawable
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.widget.SeekBar
 *  android.widget.SeekBar$OnSeekBarChangeListener
 */
package com.blackrussia.game.core;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.fragment.app.Fragment;
import com.blackrussia.game.core.ISaveableFragment;
import com.nvidia.devtech.NvEventQueueActivity;
import java.util.HashMap;

public class DialogClientSettingsFPSFragment
extends Fragment
implements ISaveableFragment {
    private boolean bChangeAllowed = true;
    private NvEventQueueActivity mContext = null;
    private SeekBar.OnSeekBarChangeListener mListenerSeekBars;
    private HashMap<ViewGroup, Drawable> mOldDrawables;
    private ViewGroup mParentView = null;
    private View mRootView = null;

    public static DialogClientSettingsFPSFragment createInstance(String string2) {
        return new DialogClientSettingsFPSFragment();
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
            View view2 = viewGroup.getChildAt(i);
            if (view2 instanceof ViewGroup) {
                this.makeAllElementsVisible((ViewGroup)view2, view, false);
                continue;
            }
            if (view2 == view) continue;
            view2.setAlpha(1.0f);
        }
    }

    private void setSeekBarListeners() {
        int n;
        int n2;
        CharSequence charSequence;
        CharSequence charSequence2;
        int n3;
        this.mListenerSeekBars = new SeekBar.OnSeekBarChangeListener(this){
            final DialogClientSettingsFPSFragment this$0;
            {
                this.this$0 = dialogClientSettingsFPSFragment;
            }

            public void onProgressChanged(SeekBar seekBar, int n, boolean bl) {
                if (this.this$0.bChangeAllowed) {
                    this.this$0.passValuesToNative();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                DialogClientSettingsFPSFragment dialogClientSettingsFPSFragment = this.this$0;
                dialogClientSettingsFPSFragment.makeAllElementsInvisible(dialogClientSettingsFPSFragment.mParentView, (View)seekBar, true);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                DialogClientSettingsFPSFragment dialogClientSettingsFPSFragment = this.this$0;
                dialogClientSettingsFPSFragment.makeAllElementsVisible(dialogClientSettingsFPSFragment.mParentView, (View)seekBar, true);
                this.this$0.mContext.onSettingsWindowSave();
            }
        };
        for (n3 = 10; n3 < 12; ++n3) {
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
            if (charSequence2 != null) {
                charSequence2.setOnSeekBarChangeListener(this.mListenerSeekBars);
            }
            if (charSequence == null) continue;
            charSequence.setOnSeekBarChangeListener(this.mListenerSeekBars);
        }
        for (n3 = 10; n3 < 12; ++n3) {
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
            charSequence2 = (SeekBar)this.mRootView.findViewById(n);
            charSequence = (SeekBar)this.mRootView.findViewById(n2);
            if (charSequence2 != null) {
                charSequence2.setOnSeekBarChangeListener(this.mListenerSeekBars);
            }
            if (charSequence == null) continue;
            charSequence.setOnSeekBarChangeListener(this.mListenerSeekBars);
        }
    }

    @Override
    public void getValues() {
        SeekBar seekBar;
        int n;
        int n2;
        CharSequence charSequence;
        Object object;
        int n3;
        this.bChangeAllowed = false;
        for (n3 = 10; n3 < 12; ++n3) {
            object = new StringBuilder();
            ((StringBuilder)object).append("hud_element_pos_x_");
            ((StringBuilder)object).append(n3);
            object = ((StringBuilder)object).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("hud_element_pos_y_");
            ((StringBuilder)charSequence).append(n3);
            charSequence = ((StringBuilder)charSequence).toString();
            n2 = this.mContext.getResources().getIdentifier((String)object, "id", this.mContext.getPackageName());
            n = this.mContext.getResources().getIdentifier((String)charSequence, "id", this.mContext.getPackageName());
            charSequence = (SeekBar)this.mRootView.findViewById(n2);
            seekBar = (SeekBar)this.mRootView.findViewById(n);
            object = this.mContext.getNativeHudElementPosition(n3);
            if (object[0] == -1) {
                object[0] = true;
            }
            if (object[1] == -1) {
                object[1] = true;
            }
            if (charSequence != null) {
                charSequence.setProgress((int)object[0]);
            }
            if (seekBar == null) continue;
            seekBar.setProgress((int)object[1]);
        }
        for (n3 = 10; n3 < 12; ++n3) {
            object = new StringBuilder();
            ((StringBuilder)object).append("hud_element_scale_x_");
            ((StringBuilder)object).append(n3);
            object = ((StringBuilder)object).toString();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("hud_element_scale_y_");
            ((StringBuilder)charSequence).append(n3);
            charSequence = ((StringBuilder)charSequence).toString();
            n2 = this.mContext.getResources().getIdentifier((String)object, "id", this.mContext.getPackageName());
            n = this.mContext.getResources().getIdentifier((String)charSequence, "id", this.mContext.getPackageName());
            charSequence = (SeekBar)this.mRootView.findViewById(n2);
            seekBar = (SeekBar)this.mRootView.findViewById(n);
            object = this.mContext.getNativeHudElementScale(n3);
            if (object[0] == -1) {
                object[0] = true;
            }
            if (object[1] == -1) {
                object[1] = true;
            }
            if (charSequence != null && object[0] != -1) {
                charSequence.setProgress((int)object[0]);
            }
            if (seekBar == null || object[1] == -1) continue;
            seekBar.setProgress((int)object[1]);
        }
        this.bChangeAllowed = true;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mRootView = layoutInflater.inflate(2131558459, viewGroup, false);
        this.mContext = (NvEventQueueActivity)this.getActivity();
        this.getValues();
        this.setSeekBarListeners();
        return this.mRootView;
    }

    public void passValuesToNative() {
        int n;
        int n2;
        CharSequence charSequence;
        CharSequence charSequence2;
        int n3;
        for (n3 = 10; n3 < 12; ++n3) {
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
            n2 = -1;
            n = -1;
            if (charSequence != null) {
                n2 = charSequence.getProgress();
            }
            if (charSequence2 != null) {
                n = charSequence2.getProgress();
            }
            NvEventQueueActivity.setNativeHudElementPosition(n3, n2, n);
        }
        for (n3 = 10; n3 < 12; ++n3) {
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

    public DialogClientSettingsFPSFragment setRoot(ViewGroup viewGroup) {
        this.mParentView = viewGroup;
        return this;
    }
}

