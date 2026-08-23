/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 *  android.widget.EditText
 *  android.widget.TextView
 *  android.widget.Toast
 */
package com.blackrussia.launcher.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class DonateFragment
extends Fragment {
    Animation animation;
    public Dialog dialog;
    public EditText nik;
    public EditText sum;

    public void onClickDeposit() {
        if (this.nik.getText().toString().isEmpty()) {
            Toast.makeText((Context)this.getActivity(), (CharSequence)"\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u0438\u043a!", (int)1).show();
        } else if (!this.nik.getText().toString().contains("_")) {
            Toast.makeText((Context)this.getActivity(), (CharSequence)"\u041d\u0438\u043a \u0434\u043e\u043b\u0436\u0435\u043d \u0438\u043c\u0435\u0442\u044c \"_\"!", (int)1).show();
        } else if (this.nik.getText().toString().length() < 4) {
            Toast.makeText((Context)this.getActivity(), (CharSequence)"\u041c\u0438\u043d\u0438\u043c\u0430\u043b\u044c\u043d\u0430\u044f \u0434\u043b\u0438\u043d\u0430 \u043d\u0438\u043a\u0430 4 \u0441\u0438\u043c\u0432\u043e\u043b\u0430!", (int)1).show();
        } else if (this.sum.getText().toString().isEmpty()) {
            Toast.makeText((Context)this.getActivity(), (CharSequence)"\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443!", (int)1).show();
        } else {
            this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"t.me/weikton")));
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        viewGroup = layoutInflater.inflate(2131558466, viewGroup, false);
        bundle = (TextView)viewGroup.findViewById(2131361989);
        layoutInflater = AnimationUtils.loadAnimation((Context)this.getContext(), (int)2130771992);
        this.nik = (EditText)viewGroup.findViewById(2131362242);
        this.sum = (EditText)viewGroup.findViewById(2131362428);
        bundle.setOnClickListener(new View.OnClickListener(this, (Animation)layoutInflater){
            final DonateFragment this$0;
            final Animation val$animation;
            {
                this.this$0 = donateFragment;
                this.val$animation = animation;
            }

            public void onClick(View view) {
                view.startAnimation(this.val$animation);
                this.this$0.onClickDeposit();
            }
        });
        return viewGroup;
    }
}

