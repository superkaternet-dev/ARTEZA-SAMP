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

public class DialogClientSettingsWeaponsFragment
extends Fragment
implements ISaveableFragment {
    private boolean bChangeAllowed = true;
    private NvEventQueueActivity mContext = null;
    private SeekBar.OnSeekBarChangeListener mListenerSeekBars;
    private HashMap<ViewGroup, Drawable> mOldDrawables;
    private ViewGroup mParentView = null;
    private View mRootView = null;

    public static DialogClientSettingsWeaponsFragment createInstance(String string2) {
        return new DialogClientSettingsWeaponsFragment();
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

    private void setSeekBarListeners() {
        int n;
        int n2;
        CharSequence charSequence;
        CharSequence charSequence2;
        int n3;
        this.mListenerSeekBars = new SeekBar.OnSeekBarChangeListener(this){
            final DialogClientSettingsWeaponsFragment this$0;
            {
                this.this$0 = dialogClientSettingsWeaponsFragment;
            }

            public void onProgressChanged(SeekBar seekBar, int n, boolean bl) {
                if (this.this$0.bChangeAllowed) {
                    this.this$0.passValuesToNative();
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                DialogClientSettingsWeaponsFragment dialogClientSettingsWeaponsFragment = this.this$0;
                dialogClientSettingsWeaponsFragment.makeAllElementsInvisible(dialogClientSettingsWeaponsFragment.mParentView, (View)seekBar, true);
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                DialogClientSettingsWeaponsFragment dialogClientSettingsWeaponsFragment = this.this$0;
                dialogClientSettingsWeaponsFragment.makeAllElementsVisible(dialogClientSettingsWeaponsFragment.mParentView, (View)seekBar, true);
                this.this$0.mContext.onSettingsWindowSave();
            }
        };
        for (n3 = 12; n3 < 14; ++n3) {
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
        for (n3 = 12; n3 < 14; ++n3) {
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
            if (charSequence != null) {
                charSequence.setOnSeekBarChangeListener(this.mListenerSeekBars);
            }
            if (charSequence2 == null) continue;
            charSequence2.setOnSeekBarChangeListener(this.mListenerSeekBars);
        }
    }

    @Override
    public void getValues() {
        Object object;
        int n;
        int n2;
        Object object2;
        CharSequence charSequence;
        int n3;
        this.bChangeAllowed = false;
        for (n3 = 12; n3 < 14; ++n3) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("hud_element_pos_x_");
            ((StringBuilder)charSequence).append(n3);
            charSequence = ((StringBuilder)charSequence).toString();
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("hud_element_pos_y_");
            ((StringBuilder)object2).append(n3);
            object2 = ((StringBuilder)object2).toString();
            n2 = this.mContext.getResources().getIdentifier((String)charSequence, "id", this.mContext.getPackageName());
            n = this.mContext.getResources().getIdentifier((String)object2, "id", this.mContext.getPackageName());
            charSequence = (SeekBar)this.mRootView.findViewById(n2);
            object2 = (SeekBar)this.mRootView.findViewById(n);
            object = this.mContext.getNativeHudElementPosition(n3);
            if (object[0] == -1) {
                object[0] = 1;
            }
            if (object[1] == -1) {
                object[1] = 1;
            }
            if (charSequence != null) {
                charSequence.setProgress(object[0]);
            }
            if (object2 == null) continue;
            object2.setProgress(object[1]);
        }
        for (n3 = 12; n3 < 14; ++n3) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("hud_element_scale_x_");
            ((StringBuilder)charSequence).append(n3);
            charSequence = ((StringBuilder)charSequence).toString();
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("hud_element_scale_y_");
            ((StringBuilder)object2).append(n3);
            object2 = ((StringBuilder)object2).toString();
            n = this.mContext.getResources().getIdentifier((String)charSequence, "id", this.mContext.getPackageName());
            n2 = this.mContext.getResources().getIdentifier((String)object2, "id", this.mContext.getPackageName());
            charSequence = (SeekBar)this.mRootView.findViewById(n);
            object = (SeekBar)this.mRootView.findViewById(n2);
            object2 = this.mContext.getNativeHudElementScale(n3);
            if (object2[0] == -1) {
                object2[0] = true;
            }
            if (object2[1] == -1) {
                object2[1] = true;
            }
            if (charSequence != null && object2[0] != -1) {
                charSequence.setProgress((int)object2[0]);
            }
            if (object == null || object2[1] == -1) continue;
            object.setProgress((int)object2[1]);
        }
        this.bChangeAllowed = true;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.mRootView = layoutInflater.inflate(2131558462, viewGroup, false);
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
        for (n3 = 12; n3 < 14; ++n3) {
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
            n = -1;
            n2 = -1;
            if (charSequence2 != null) {
                n = charSequence2.getProgress();
            }
            if (charSequence != null) {
                n2 = charSequence.getProgress();
            }
            NvEventQueueActivity.setNativeHudElementPosition(n3, n, n2);
        }
        for (n3 = 12; n3 < 14; ++n3) {
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
            n = -1;
            n2 = -1;
            if (charSequence != null) {
                n = charSequence.getProgress();
            }
            if (charSequence2 != null) {
                n2 = charSequence2.getProgress();
            }
            NvEventQueueActivity.setNativeHudElementScale(n3, n, n2);
        }
    }

    @Override
    public void save() {
    }

    public DialogClientSettingsWeaponsFragment setRoot(ViewGroup viewGroup) {
        this.mParentView = viewGroup;
        return this;
    }
}

