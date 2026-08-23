/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Dialog
 *  android.content.Context
 *  android.content.Intent
 *  android.graphics.PorterDuff$Mode
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  android.os.Handler
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 *  android.widget.EditText
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.TextView
 *  android.widget.Toast
 */
package com.blackrussia.launcher.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.blackrussia.game.core.GTASA;
import com.blackrussia.game.gui.ChooseServer;
import com.blackrussia.launcher.activity.InstallActivity;
import com.blackrussia.launcher.activity.InstallClient;
import com.blackrussia.launcher.activity.MainActivity$$ExternalSyntheticLambda0;
import com.blackrussia.launcher.activity.MainActivity$$ExternalSyntheticLambda1;
import com.blackrussia.launcher.activity.MainActivity$$ExternalSyntheticLambda2;
import com.blackrussia.launcher.activity.MainActivity$$ExternalSyntheticLambda3;
import com.blackrussia.launcher.activity.MainActivity$$ExternalSyntheticLambda4;
import com.blackrussia.launcher.activity.MainActivity$$ExternalSyntheticLambda5;
import com.blackrussia.launcher.activity.MainActivity$$ExternalSyntheticLambda6;
import com.blackrussia.launcher.activity.MainActivity$$ExternalSyntheticLambda7;
import com.blackrussia.launcher.adapter.NewsAdapter;
import com.blackrussia.launcher.adapter.ServersAdapter;
import com.blackrussia.launcher.fragment.DonateFragment;
import com.blackrussia.launcher.fragment.ForumFragment;
import com.blackrussia.launcher.fragment.MonitoringFragment;
import com.blackrussia.launcher.fragment.SettingsFragment;
import com.blackrussia.launcher.model.News;
import com.blackrussia.launcher.model.Servers;
import com.blackrussia.launcher.network.ApiService;
import com.blackrussia.launcher.other.Interface;
import com.blackrussia.launcher.other.Lists;
import com.google.firebase.database.DatabaseReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.ini4j.Wini;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class MainActivity
extends AppCompatActivity {
    public static ArrayList<News> nlist;
    public static ArrayList<Servers> slist;
    DatabaseReference databaseNews;
    DatabaseReference databaseServers;
    public LinearLayout donateButton;
    public DonateFragment donateFragment;
    public ImageView donateImage;
    public TextView donateTV;
    public LinearLayout forumButton;
    public ForumFragment forumFragment;
    public ImageView forumImage;
    public TextView forumTV;
    private Handler handler;
    private ChooseServer mContexta = null;
    public LinearLayout monitoringButton;
    public MonitoringFragment monitoringFragment;
    public ImageView monitoringImage;
    public TextView monitoringTV;
    NewsAdapter newsAdapter;
    public LinearLayout playButton;
    public ImageView playImage;
    ServersAdapter serversAdapter;
    public LinearLayout settingsButton;
    public SettingsFragment settingsFragment;
    public ImageView settingsImage;
    public TextView settingsTV;

    private boolean IsGameInstalled() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/BlackRussia/texdb/gta3.img");
        return new File(stringBuilder.toString()).exists();
    }

    static /* synthetic */ void lambda$onCreate$0(Dialog dialog) {
        dialog.dismiss();
    }

    static /* synthetic */ void lambda$onCreate$2(Dialog dialog) {
        dialog.dismiss();
    }

    static /* synthetic */ void lambda$onCreate$4(Dialog dialog) {
        dialog.dismiss();
    }

    private void startErar() {
        this.onDestroy();
        this.finish();
    }

    private void startTimer() {
        new Timer().schedule(new TimerTask(this){
            final MainActivity this$0;
            {
                this.this$0 = mainActivity;
            }

            @Override
            public void run() {
                this.this$0.onClickPlay();
            }
        }, 100L);
    }

    public boolean checkValidNick() {
        EditText editText = (EditText)this.findViewById(2131362045);
        if (editText.getText().toString().isEmpty()) {
            Toast.makeText((Context)this, (CharSequence)"\u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u043d\u0438\u043a", (int)0).show();
            return false;
        }
        if (!editText.getText().toString().contains("_")) {
            Toast.makeText((Context)this, (CharSequence)"\u041d\u0438\u043a \u0434\u043e\u043b\u0436\u0435\u043d \u0441\u043e\u0434\u0435\u0440\u0436\u0430\u0442\u044c \u0441\u0438\u043c\u0432\u043e\u043b \"_\"", (int)0).show();
            return false;
        }
        if (editText.getText().toString().length() < 4) {
            Toast.makeText((Context)this, (CharSequence)"\u0414\u043b\u0438\u043d\u0430 \u043d\u0438\u043a\u0430 \u0434\u043e\u043b\u0436\u043d\u0430 \u0431\u044b\u0442\u044c \u043d\u0435 \u043c\u0435\u043d\u0435\u0435 4 \u0441\u0438\u043c\u0432\u043e\u043b\u043e\u0432", (int)0).show();
            return false;
        }
        return true;
    }

    public boolean isRecordAudioPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.RECORD_AUDIO") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO"}, 2);
            return false;
        }
        return true;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            return false;
        }
        return true;
    }

    public /* synthetic */ void lambda$onCreate$1$com-blackrussia-launcher-activity-MainActivity(Dialog dialog, View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this, (int)2130771992));
        this.handler.postDelayed((Runnable)new MainActivity$$ExternalSyntheticLambda4(dialog), 200L);
    }

    public /* synthetic */ void lambda$onCreate$3$com-blackrussia-launcher-activity-MainActivity(Dialog dialog, View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this, (int)2130771992));
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"https://youtube.com/@KALCOR20")));
        this.handler.postDelayed((Runnable)new MainActivity$$ExternalSyntheticLambda5(dialog), 200L);
    }

    public /* synthetic */ void lambda$onCreate$5$com-blackrussia-launcher-activity-MainActivity(Dialog dialog, View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this, (int)2130771992));
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)"https://youtube.com/@weikton")));
        this.handler.postDelayed((Runnable)new MainActivity$$ExternalSyntheticLambda6(dialog), 200L);
    }

    public /* synthetic */ void lambda$onCreate$6$com-blackrussia-launcher-activity-MainActivity(View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this, (int)2130771992));
        Toast.makeText((Context)this.getApplicationContext(), (CharSequence)"\u041d\u0430\u0436\u043c\u0438\u0442\u0435 \u043d\u0430 \u043e\u0434\u043d\u043e\u0433\u043e \u0438\u0437 \u0430\u0432\u0442\u043e\u0440\u043e\u0432", (int)0).show();
    }

    public /* synthetic */ void lambda$onCreate$7$com-blackrussia-launcher-activity-MainActivity() {
        Dialog dialog = new Dialog((Context)this);
        dialog.setContentView(2131558477);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(2131230927);
        dialog.getWindow().setLayout(-1, -2);
        ((TextView)dialog.findViewById(2131362223)).setText((CharSequence)"\u0410\u0432\u0442\u043e\u0440\u044b \u043f\u0440\u043e\u0435\u043a\u0442\u0430");
        ((TextView)dialog.findViewById(2131362224)).setText((CharSequence)"Kalcor 2.0 X Weikton");
        ((TextView)dialog.findViewById(2131361938)).setOnClickListener((View.OnClickListener)new MainActivity$$ExternalSyntheticLambda1(this, dialog));
        ((ImageView)dialog.findViewById(2131362162)).setOnClickListener((View.OnClickListener)new MainActivity$$ExternalSyntheticLambda2(this, dialog));
        ((ImageView)dialog.findViewById(2131362164)).setOnClickListener((View.OnClickListener)new MainActivity$$ExternalSyntheticLambda3(this, dialog));
        ((TextView)dialog.findViewById(2131362250)).setOnClickListener((View.OnClickListener)new MainActivity$$ExternalSyntheticLambda0(this));
        dialog.show();
    }

    public void onClickDonate() {
    }

    public void onClickForum() {
    }

    public void onClickMonitoring() {
        this.setTextColor(this.monitoringButton, this.monitoringTV, this.monitoringImage);
        this.replaceFragment(this.monitoringFragment);
    }

    public void onClickPlay() {
        Comparable<StringBuilder> comparable = new StringBuilder();
        ((StringBuilder)comparable).append(Environment.getExternalStorageDirectory());
        ((StringBuilder)comparable).append("/BlackRussia/SAMP/settings.ini");
        File file = new File(((StringBuilder)comparable).toString());
        Integer n = 0;
        comparable = n;
        if (file.exists()) {
            comparable = n;
            comparable = n;
            Wini wini = new Wini(file);
            comparable = n;
            n = Integer.valueOf(wini.get((Object)"versions", "gameFilesVersion"));
            comparable = n;
            try {
                wini.store();
                comparable = n;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        if (ApiService.getInstance().ClientVersion != 45) {
            this.monitoringFragment.getContext().startActivity(new Intent(this.monitoringFragment.getContext(), InstallClient.class));
            this.monitoringFragment.getActivity().finish();
        } else if (ApiService.getInstance().GameFilesVersion != comparable) {
            comparable = new StringBuilder();
            ((StringBuilder)comparable).append(Environment.getExternalStorageDirectory());
            ((StringBuilder)comparable).append("/BlackRussia");
            comparable = new File(((StringBuilder)comparable).toString());
            if (((File)comparable).exists()) {
                ((File)comparable).delete();
            }
            this.monitoringFragment.getContext().startActivity(new Intent(this.monitoringFragment.getContext(), InstallActivity.class));
            this.monitoringFragment.getActivity().finish();
        } else if (this.IsGameInstalled()) {
            this.startActivity(new Intent(this.getApplicationContext(), GTASA.class));
        } else {
            this.startActivity(new Intent(this.getApplicationContext(), InstallActivity.class));
        }
    }

    public void onClickSettings() {
        this.setTextColor(this.settingsButton, this.settingsTV, this.settingsImage);
        this.replaceFragment(this.settingsFragment);
    }

    @Override
    protected void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        this.setContentView(2131558431);
        Lists.slist = new ArrayList();
        Lists.nlist = new ArrayList();
        object = new Retrofit.Builder().baseUrl("https://brussia-new.reactnet.site/").addConverterFactory(GsonConverterFactory.create()).build().create(Interface.class);
        object.getServers().enqueue(new Callback<List<Servers>>(this){
            final MainActivity this$0;
            {
                this.this$0 = mainActivity;
            }

            @Override
            public void onFailure(Call<List<Servers>> call, Throwable throwable) {
                Toast.makeText((Context)this.this$0.getApplicationContext(), (CharSequence)"\u0421\u0435\u0440\u0432\u0435\u0440\u0430 WEIKTON GAMES \u043d\u0435 \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b", (int)0).show();
                this.this$0.startErar();
            }

            @Override
            public void onResponse(Call<List<Servers>> object3, Response<List<Servers>> object2) {
                Iterator iterator2;
                for (Servers servers : (List)((Response)((Object)iterator2)).body()) {
                    Lists.slist.add(new Servers(servers.getColor(), servers.getDopname(), servers.getname(), servers.getOnline(), servers.getmaxOnline()));
                }
            }
        });
        object.getNews().enqueue(new Callback<List<News>>(this){
            final MainActivity this$0;
            {
                this.this$0 = mainActivity;
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable throwable) {
                Toast.makeText((Context)this.this$0.getApplicationContext(), (CharSequence)"\u0421\u0435\u0440\u0432\u0435\u0440\u0430 WEIKTON GAMES \u043d\u0435 \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b", (int)0).show();
                this.this$0.startErar();
            }

            @Override
            public void onResponse(Call<List<News>> object3, Response<List<News>> object2) {
                Iterator iterator2;
                for (News news : (List)((Response)((Object)iterator2)).body()) {
                    Lists.nlist.add(new News(news.getImageUrl(), news.getTitle(), news.getUrl()));
                }
            }
        });
        this.handler = new Handler();
        object = AnimationUtils.loadAnimation((Context)this, (int)2130771992);
        this.monitoringTV = (TextView)this.findViewById(2131362231);
        this.settingsTV = (TextView)this.findViewById(2131362379);
        this.forumTV = (TextView)this.findViewById(2131362064);
        this.donateTV = (TextView)this.findViewById(2131362016);
        this.monitoringImage = (ImageView)this.findViewById(2131362230);
        this.settingsImage = (ImageView)this.findViewById(2131362378);
        this.forumImage = (ImageView)this.findViewById(2131362063);
        this.donateImage = (ImageView)this.findViewById(2131362015);
        this.playImage = (ImageView)this.findViewById(2131362271);
        this.monitoringButton = (LinearLayout)this.findViewById(2131362229);
        this.settingsButton = (LinearLayout)this.findViewById(2131362377);
        this.forumButton = (LinearLayout)this.findViewById(2131362318);
        this.donateButton = (LinearLayout)this.findViewById(2131362014);
        this.playButton = (LinearLayout)this.findViewById(2131362270);
        this.monitoringFragment = new MonitoringFragment();
        this.settingsFragment = new SettingsFragment();
        this.forumFragment = new ForumFragment();
        this.donateFragment = new DonateFragment();
        this.replaceFragment(this.monitoringFragment);
        this.handler.postDelayed((Runnable)new MainActivity$$ExternalSyntheticLambda7(this), 200L);
        this.monitoringButton.setOnClickListener(new View.OnClickListener(this){
            final MainActivity this$0;
            {
                this.this$0 = mainActivity;
            }

            public void onClick(View view) {
                this.this$0.onClickMonitoring();
            }
        });
        this.settingsButton.setOnClickListener(new View.OnClickListener(this){
            final MainActivity this$0;
            {
                this.this$0 = mainActivity;
            }

            public void onClick(View view) {
                this.this$0.onClickSettings();
            }
        });
        this.forumButton.setOnClickListener(new View.OnClickListener(this){
            final MainActivity this$0;
            {
                this.this$0 = mainActivity;
            }

            public void onClick(View view) {
                this.this$0.onClickForum();
            }
        });
        this.donateButton.setOnClickListener(new View.OnClickListener(this){
            final MainActivity this$0;
            {
                this.this$0 = mainActivity;
            }

            public void onClick(View view) {
                this.this$0.onClickDonate();
            }
        });
        this.playButton.setOnClickListener(new View.OnClickListener(this, (Animation)object){
            final MainActivity this$0;
            final Animation val$animation;
            {
                this.this$0 = mainActivity;
                this.val$animation = animation;
            }

            public void onClick(View view) {
                view.startAnimation(this.val$animation);
                this.this$0.startTimer();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void onRestart() {
        super.onRestart();
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(2131361965, fragment);
        fragmentTransaction.commit();
    }

    public void setTextColor(LinearLayout linearLayout, TextView textView, ImageView imageView) {
        this.monitoringButton.setAlpha(0.45f);
        this.settingsButton.setAlpha(0.45f);
        this.forumButton.setAlpha(0.45f);
        this.donateButton.setAlpha(0.45f);
        this.monitoringTV.setTextColor(this.getResources().getColor(2131099764));
        this.settingsTV.setTextColor(this.getResources().getColor(2131099764));
        this.forumTV.setTextColor(this.getResources().getColor(2131099764));
        this.donateTV.setTextColor(this.getResources().getColor(2131099764));
        this.monitoringImage.setColorFilter(this.getResources().getColor(2131099764), PorterDuff.Mode.SRC_IN);
        this.settingsImage.setColorFilter(this.getResources().getColor(2131099764), PorterDuff.Mode.SRC_IN);
        this.forumImage.setColorFilter(this.getResources().getColor(2131099764), PorterDuff.Mode.SRC_IN);
        this.donateImage.setColorFilter(this.getResources().getColor(2131099764), PorterDuff.Mode.SRC_IN);
        linearLayout.setAlpha(1.0f);
        textView.setTextColor(this.getResources().getColor(2131099765));
        imageView.setColorFilter(this.getResources().getColor(2131099765), PorterDuff.Mode.SRC_IN);
    }
}

