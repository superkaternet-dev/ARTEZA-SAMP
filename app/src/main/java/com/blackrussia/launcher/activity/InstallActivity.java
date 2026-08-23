/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Environment
 *  android.util.Log
 *  android.widget.ProgressBar
 *  android.widget.TextView
 */
package com.blackrussia.launcher.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.blackrussia.launcher.activity.MainActivity;
import com.blackrussia.launcher.network.ApiService;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class InstallActivity
extends AppCompatActivity {
    private final String link;
    public final String path_zip;
    ProgressBar progressBar;
    ProgressBar progressBarInstall;
    TextView stanictop;
    TextView textView6;
    TextView textView7;
    TextView textview5;

    public InstallActivity() {
        this.link = String.valueOf(ApiService.getInstance().URL_GAME_FILES);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStoragePublicDirectory((String)Environment.DIRECTORY_DOWNLOADS));
        stringBuilder.append("/cache.zip");
        this.path_zip = stringBuilder.toString();
    }

    private void lambda$run$0$InstallActivity$1(int n, int n2) {
        TextView textView = this.textView7;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(n / 0x100000);
        stringBuilder.append(" MB \u0438\u0437 ");
        stringBuilder.append(n2 / 0x100000);
        stringBuilder.append(" MB");
        textView.setText((CharSequence)stringBuilder.toString());
        textView = this.textView6;
        stringBuilder = new StringBuilder();
        stringBuilder.append(n / 0x100000 * 100 / (n2 / 0x100000));
        stringBuilder.append("%");
        textView.setText((CharSequence)stringBuilder.toString());
        this.progressBarInstall.setProgress(n / 0x100000 * 100 / (n2 / 0x100000));
    }

    private void lambda$run$1$InstallActivity$1() {
        this.textview5.setText((CharSequence)"\u0420\u0430\u0441\u043f\u0430\u043a\u043e\u0432\u043a\u0430 \u0444\u0430\u0439\u043b\u043e\u0432 \u0438\u0433\u0440\u044b...");
        this.textView6.setVisibility(4);
        this.textView7.setVisibility(4);
        this.progressBarInstall.setVisibility(4);
        this.progressBar.setVisibility(0);
    }

    public void UnZip() {
        try {
            Object object = new ZipFile(this.path_zip);
            ((ZipFile)object).extractAll(String.valueOf(Environment.getExternalStorageDirectory()));
            object = new File(this.path_zip);
            ((File)object).delete();
        }
        catch (ZipException zipException) {
            zipException.printStackTrace();
        }
        this.startActivity(new Intent((Context)this, MainActivity.class));
        this.overridePendingTransition(0, 0);
    }

    public void getFileSize(String string2) throws IOException {
        new Thread(new Runnable(this, string2){
            InputStream f115in;
            FileOutputStream file;
            final InstallActivity this$0;
            URLConnection urlConnection;
            final String val$string;
            {
                this.this$0 = installActivity;
                this.val$string = string2;
                this.file = new FileOutputStream(installActivity.path_zip);
                this.urlConnection = null;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void run() {
                block21: {
                    try {
                        Object object = new URL(this.val$string);
                        this.urlConnection = object = ((URL)object).openConnection();
                        ((URLConnection)object).connect();
                        int n = this.urlConnection.getContentLength();
                        this.f115in = this.urlConnection.getInputStream();
                        byte[] byArray = new byte[4096];
                        int n2 = 0;
                        while (true) {
                            int n3;
                            if ((n3 = this.f115in.read(byArray, 0, 4096)) == -1) {
                                object = this.file;
                                if (object != null) {
                                    try {
                                        ((FileOutputStream)object).close();
                                    }
                                    catch (IOException iOException) {
                                        iOException.printStackTrace();
                                    }
                                }
                                if ((object = this.f115in) != null) {
                                    try {
                                        ((InputStream)object).close();
                                    }
                                    catch (IOException iOException) {
                                        iOException.printStackTrace();
                                    }
                                }
                                break;
                            }
                            this.file.write(byArray, 0, n3);
                            object = this.this$0;
                            Runnable runnable = new Runnable(this, n2 += n3, n){
                                final 1 this$1;
                                final int val$finalChet;
                                final int val$length;
                                {
                                    this.this$1 = var1_1;
                                    this.val$finalChet = n;
                                    this.val$length = n2;
                                }

                                @Override
                                public final void run() {
                                    this.this$1.this$0.lambda$run$0$InstallActivity$1(this.val$finalChet, this.val$length);
                                }
                            };
                            object.runOnUiThread(runnable);
                        }
                    }
                    catch (Throwable throwable) {
                        Closeable closeable = this.file;
                        if (closeable != null) {
                            try {
                                ((FileOutputStream)closeable).close();
                            }
                            catch (IOException iOException) {
                                iOException.printStackTrace();
                            }
                        }
                        if ((closeable = this.f115in) == null) throw throwable;
                        try {
                            ((InputStream)closeable).close();
                            throw throwable;
                        }
                        catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                        throw throwable;
                    }
                    catch (Exception exception) {
                        Object object = new StringBuilder();
                        ((StringBuilder)object).append("\u041e\u0448\u0438\u0431\u043a\u0430 ");
                        ((StringBuilder)object).append(exception);
                        Log.e((String)"Downn", (String)((StringBuilder)object).toString());
                        object = this.file;
                        if (object != null) {
                            try {
                                ((FileOutputStream)object).close();
                            }
                            catch (IOException iOException) {
                                iOException.printStackTrace();
                            }
                        }
                        if ((object = this.f115in) == null) break block21;
                        try {
                            ((InputStream)object).close();
                        }
                        catch (IOException iOException) {
                            iOException.printStackTrace();
                        }
                    }
                }
                this.this$0.runOnUiThread(new Runnable(this){
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public final void run() {
                        this.this$1.this$0.lambda$run$1$InstallActivity$1();
                    }
                });
                this.this$0.UnZip();
            }
        }).start();
    }

    @Override
    public void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        this.setContentView(2131558428);
        this.textview5 = (TextView)this.findViewById(2131362471);
        this.textView7 = (TextView)this.findViewById(2131362475);
        this.textView6 = (TextView)this.findViewById(2131362473);
        this.stanictop = (TextView)this.findViewById(2131361813);
        this.progressBar = (ProgressBar)this.findViewById(2131362295);
        object = new StringBuilder();
        ((StringBuilder)object).append(Environment.getExternalStorageDirectory());
        ((StringBuilder)object).append("/BlackRussia");
        object = new File(((StringBuilder)object).toString());
        if (((File)object).exists()) {
            ((File)object).delete();
        }
        this.progressBarInstall = (ProgressBar)this.findViewById(2131362292);
        try {
            this.getFileSize(String.valueOf(ApiService.getInstance().URL_GAME_FILES));
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}

