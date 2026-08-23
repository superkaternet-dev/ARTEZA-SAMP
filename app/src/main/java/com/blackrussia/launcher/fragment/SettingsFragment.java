/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Bundle
 *  android.os.Environment
 *  android.os.Handler
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 *  android.widget.EditText
 *  android.widget.ImageView
 *  android.widget.TextView
 *  android.widget.TextView$OnEditorActionListener
 *  android.widget.Toast
 */
package com.blackrussia.launcher.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.blackrussia.launcher.activity.InstallActivity;
import com.blackrussia.launcher.other.Utils;
import java.io.File;
import java.io.IOException;
import org.ini4j.BasicProfile;
import org.ini4j.Ini;
import org.ini4j.Wini;

public class SettingsFragment
extends Fragment {
    Animation animation;
    public EditText editTextnicka;
    private Handler handler;
    String nickName;
    public EditText nickname;

    private void InitLogic() {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(Environment.getExternalStorageDirectory());
            stringBuilder.append("/BlackRussia/SAMP/settings.ini");
            File file = new File(stringBuilder.toString());
            Wini wini = new Wini(file);
            this.nickname.setText((CharSequence)wini.get((Object)"client", "name"));
            wini.store();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public void SaveNick() throws IOException {
        CharSequence charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append(Environment.getExternalStorageDirectory());
        ((StringBuilder)charSequence).append("/BlackRussia/SAMP/settings.ini");
        Object object = new Wini(new File(((StringBuilder)charSequence).toString()));
        charSequence = this.editTextnicka.getText().toString();
        this.nickName = charSequence;
        ((BasicProfile)object).put("client", "name", charSequence);
        ((Ini)object).store();
        object = this.getActivity();
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("\u0412\u0430\u0448 \u043d\u0438\u043a: ");
        ((StringBuilder)charSequence).append(this.nickName);
        ((StringBuilder)charSequence).append("   \u0423\u0441\u043f\u0435\u0448\u043d\u043e \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d!");
        charSequence = Toast.makeText((Context)object, (CharSequence)((StringBuilder)charSequence).toString(), (int)0);
        charSequence.setGravity(17, 0, 0);
        charSequence.show();
    }

    public boolean checkValidNick(View view) {
        if ((view = (EditText)view.findViewById(2131362045)).getText().toString().isEmpty()) {
            Toast.makeText((Context)this.getActivity(), (CharSequence)"\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u0438\u043a", (int)0).show();
            return false;
        }
        if (!view.getText().toString().contains("_")) {
            Toast.makeText((Context)this.getActivity(), (CharSequence)"\u041d\u0438\u043a \u0434\u043e\u043b\u0436\u0435\u043d \u0441\u043e\u0434\u0435\u0440\u0436\u0430\u0442\u044c \u0441\u0438\u043c\u0432\u043e\u043b \"_\"", (int)0).show();
            return false;
        }
        if (view.getText().toString().length() < 4) {
            Toast.makeText((Context)this.getActivity(), (CharSequence)"\u0414\u043b\u0438\u043d\u0430 \u043d\u0438\u043a\u0430 \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043d\u0435 \u043c\u0435\u043d\u0435\u0435 4 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432", (int)0).show();
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        viewGroup = layoutInflater.inflate(2131558469, viewGroup, false);
        layoutInflater = AnimationUtils.loadAnimation((Context)this.getContext(), (int)2130771992);
        this.nickname = (EditText)viewGroup.findViewById(2131362045);
        this.editTextnicka = (EditText)viewGroup.findViewById(2131362046);
        this.handler = new Handler();
        this.InitLogic();
        ((TextView)viewGroup.findViewById(2131362308)).setOnClickListener(new View.OnClickListener(this, (Animation)layoutInflater){
            final SettingsFragment this$0;
            final Animation val$animation;
            {
                this.this$0 = settingsFragment;
                this.val$animation = animation;
            }

            public void onClick(View object) {
                object.startAnimation(this.val$animation);
                object = new StringBuilder();
                ((StringBuilder)object).append(Environment.getExternalStorageDirectory());
                ((StringBuilder)object).append("/BlackRussia");
                Utils.delete(new File(((StringBuilder)object).toString()));
                this.this$0.startActivity(new Intent((Context)this.this$0.getActivity(), InstallActivity.class));
            }
        });
        ((TextView)viewGroup.findViewById(2131362310)).setOnClickListener(new View.OnClickListener(this, (Animation)layoutInflater){
            final SettingsFragment this$0;
            final Animation val$animation;
            {
                this.this$0 = settingsFragment;
                this.val$animation = animation;
            }

            public void onClick(View view) {
                view.startAnimation(this.val$animation);
            }
        });
        ((ImageView)viewGroup.findViewById(2131362454)).setOnClickListener(new View.OnClickListener(this, (Animation)layoutInflater){
            final SettingsFragment this$0;
            final Animation val$animation;
            {
                this.this$0 = settingsFragment;
                this.val$animation = animation;
            }

            public void onClick(View view) {
                view.startAnimation(this.val$animation);
                this.this$0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"t.me/weikton")));
            }
        });
        ((ImageView)viewGroup.findViewById(2131362177)).setOnClickListener(new View.OnClickListener(this, (Animation)layoutInflater){
            final SettingsFragment this$0;
            final Animation val$animation;
            {
                this.this$0 = settingsFragment;
                this.val$animation = animation;
            }

            public void onClick(View view) {
                view.startAnimation(this.val$animation);
                this.this$0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"")));
            }
        });
        ((ImageView)viewGroup.findViewById(2131362514)).setOnClickListener(new View.OnClickListener(this, (Animation)layoutInflater){
            final SettingsFragment this$0;
            final Animation val$animation;
            {
                this.this$0 = settingsFragment;
                this.val$animation = animation;
            }

            public void onClick(View view) {
                view.startAnimation(this.val$animation);
                this.this$0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"https://vk.com/ne.weikton")));
            }
        });
        ((ImageView)viewGroup.findViewById(2131362013)).setOnClickListener(new View.OnClickListener(this, (Animation)layoutInflater){
            final SettingsFragment this$0;
            final Animation val$animation;
            {
                this.this$0 = settingsFragment;
                this.val$animation = animation;
            }

            public void onClick(View view) {
                view.startAnimation(this.val$animation);
                this.this$0.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"")));
            }
        });
        this.nickname.setOnEditorActionListener(new TextView.OnEditorActionListener(this, (View)viewGroup){
            final SettingsFragment this$0;
            final View val$inflate;
            {
                this.this$0 = settingsFragment;
                this.val$inflate = view;
            }

            public boolean onEditorAction(TextView object, int n, KeyEvent object2) {
                if (n == 3 || n == 6 || object2.getAction() == 0 && object2.getKeyCode() == 66) {
                    try {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append(Environment.getExternalStorageDirectory());
                        ((StringBuilder)object2).append("/BlackRussia/SAMP/settings.ini");
                        object = new File(((StringBuilder)object2).toString());
                        if (!((File)object).exists()) {
                            ((File)object).createNewFile();
                            ((File)object).mkdirs();
                        }
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append(Environment.getExternalStorageDirectory());
                        ((StringBuilder)object2).append("/BlackRussia/SAMP/settings.ini");
                        object = new File(((StringBuilder)object2).toString());
                        Wini wini = new Wini((File)object);
                        if (this.this$0.checkValidNick(this.val$inflate)) {
                            wini.put("client", "name", this.this$0.nickname.getText().toString());
                            Toast.makeText((Context)this.this$0.getActivity(), (CharSequence)"\u0412\u0430\u0448 \u043d\u043e\u0432\u044b\u0439 \u043d\u0438\u043a\u043d\u0435\u0439\u043c \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u0441\u043e\u0445\u0440\u0430\u043d\u0435\u043d!", (int)0).show();
                        } else {
                            this.this$0.checkValidNick(this.val$inflate);
                        }
                        wini.store();
                    }
                    catch (IOException iOException) {
                        iOException.printStackTrace();
                        Toast.makeText((Context)this.this$0.getActivity(), (CharSequence)"\u0423\u0441\u0442\u0430\u043d\u043e\u0432\u0438\u0442\u0435 \u0438\u0433\u0440\u0443!", (int)0).show();
                    }
                }
                return false;
            }
        });
        return viewGroup;
    }
}

