/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.content.Intent
 *  android.net.ConnectivityManager
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  android.os.Handler
 *  android.widget.Toast
 */
package com.blackrussia.launcher.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.blackrussia.launcher.activity.MainActivity;
import com.blackrussia.launcher.network.ApiService;
import com.blackrussia.launcher.network.Links;
import java.util.Timer;
import java.util.TimerTask;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity
extends AppCompatActivity {
    private static final int PERMISSION_STORAGE = 101;
    private Handler handler;

    public static boolean isOnline(Context context) {
        return (context = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo()) != null && context.isConnectedOrConnecting();
    }

    private void loadAPI() {
        ApiService.getInstance().getApiService().getLinks().enqueue(new Callback<Links>(this){
            final SplashActivity this$0;
            {
                this.this$0 = splashActivity;
            }

            @Override
            public void onFailure(Call<Links> call, Throwable throwable) {
                Toast.makeText((Context)this.this$0.getApplicationContext(), (CharSequence)"\u0421\u0435\u0440\u0432\u0435\u0440\u0430 \u043d\u0435 \u0434\u043e\u0441\u0442\u0443\u043f\u043d\u044b", (int)0).show();
                this.this$0.startErar();
            }

            @Override
            public void onResponse(Call<Links> call, Response<Links> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            ApiService.getInstance().ClientVersion = response.body().getTargetClientVersion();
                            ApiService.getInstance().GameFilesVersion = response.body().getTargetGameFilesVersion();
                            ApiService.getInstance().URL_GAME_FILES = response.body().getUrlFiles();
                            ApiService.getInstance().URL_CLIENT = response.body().getUrlClient();
                        }
                    }
                    catch (Exception exception) {
                        // empty catch block
                    }
                }
            }
        });
    }

    private void startErar() {
        this.onDestroy();
        this.finish();
    }

    private void startLauncher() {
        this.startActivity(new Intent((Context)this, MainActivity.class));
        this.finish();
    }

    private void startTimer() {
        new Timer().schedule(new TimerTask(this){
            final SplashActivity this$0;
            {
                this.this$0 = splashActivity;
            }

            @Override
            public void run() {
                this.this$0.startLauncher();
            }
        }, 800L);
    }

    @Override
    public void onActivityResult(int n, int n2, Intent intent) {
        if (n == 101 && Build.VERSION.SDK_INT >= 30 && !PermissionUtils.hasPermissions((Context)this)) {
            Toast.makeText((Context)this.getApplicationContext(), (CharSequence)"\u0414\u0430\u0439\u0442\u0435 \u0440\u0430\u0437\u0440\u0435\u0448\u0435\u043d\u0438\u0435!", (int)0).show();
        } else {
            this.startLauncher();
        }
        super.onActivityResult(n, n2, intent);
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2131558432);
        new Retrofit.Builder().baseUrl("https://brussia-new.reactnet.site/").addConverterFactory(GsonConverterFactory.create()).build();
        this.loadAPI();
        this.handler = new Handler();
        if (Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") != -1 && this.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != -1 && this.checkSelfPermission("android.permission.RECORD_AUDIO") != -1) {
                this.startTimer();
            } else {
                this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.RECORD_AUDIO"}, 1000);
            }
        } else {
            this.startTimer();
        }
        if (PermissionUtils.hasPermissions((Context)this)) {
            try {
                this.startTimer();
                this.overridePendingTransition(0, 0);
            }
            catch (Exception exception) {}
        } else if (!PermissionUtils.hasPermissions((Context)this)) {
            PermissionUtils.requestPermissions(this, 101);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int n, String[] stringArray, int[] nArray) {
        if (n == 101 && (nArray.length <= 0 || nArray[0] != 0)) {
            Toast.makeText((Context)this.getApplicationContext(), (CharSequence)"\u0414\u0430\u0439\u0442\u0435 \u0440\u0430\u0437\u0440\u0435\u0448\u0435\u043d\u0438\u0435!", (int)0).show();
        } else {
            this.startLauncher();
        }
        super.onRequestPermissionsResult(n, stringArray, nArray);
    }

    public void onRequestPermissionsResultBr(int n, String[] stringArray, int[] nArray) {
        super.onRequestPermissionsResult(n, stringArray, nArray);
        if (n == 1000) {
            this.startLauncher();
        }
    }

    public void onRestart() {
        super.onRestart();
    }

    public static class PermissionUtils {
        public static boolean hasPermissions(Context context) {
            if (Build.VERSION.SDK_INT >= 30) {
                return Environment.isExternalStorageManager();
            }
            return Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
            {
            }
        }

        public static void requestPermissions(Activity activity, int n) {
            if (Build.VERSION.SDK_INT >= 30) {
                try {
                    Intent intent = new Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse((String)String.format("package:%s", activity.getPackageName())));
                    activity.startActivityForResult(intent, n);
                }
                catch (Exception exception) {
                    Intent intent = new Intent();
                    intent.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION");
                    activity.startActivityForResult(intent, n);
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, n);
            }
        }
    }
}

