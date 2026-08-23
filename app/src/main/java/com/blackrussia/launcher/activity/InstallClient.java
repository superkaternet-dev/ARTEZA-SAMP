/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.Environment
 *  android.util.Log
 *  android.widget.ProgressBar
 *  android.widget.TextView
 */
package com.blackrussia.launcher.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import com.blackrussia.launcher.network.ApiService;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class InstallClient
extends AppCompatActivity {
    private final String link;
    public final String path_zip;
    ProgressBar progressBar;
    ProgressBar progressBarInstall;
    TextView textView6;
    TextView textView7;
    TextView textview5;

    public InstallClient() {
        this.link = String.valueOf(ApiService.getInstance().URL_CLIENT);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStoragePublicDirectory((String)Environment.DIRECTORY_DOWNLOADS));
        stringBuilder.append("/launcher.apk");
        this.path_zip = stringBuilder.toString();
    }

    private void installAPK(String string2) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(".apk");
            File file = new File("/storage/emulated/0/Download/", stringBuilder.toString());
            if (file.exists()) {
                if (Build.VERSION.SDK_INT >= 24) {
                    stringBuilder = FileProvider.getUriForFile((Context)this, "com.blackrussia.game.provider", file);
                    string2 = new Intent("android.intent.action.INSTALL_PACKAGE");
                    string2.setFlags(1);
                    string2.setData((Uri)stringBuilder);
                } else {
                    stringBuilder = Uri.fromFile((File)file);
                    string2 = new Intent("android.intent.action.VIEW");
                    string2.setDataAndType((Uri)stringBuilder, "application/vnd.android.package-archive");
                    string2.setFlags(0x10000000);
                }
                this.startActivity((Intent)string2);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
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
        this.textview5.setText((CharSequence)"\u041f\u043e\u0434\u0442\u0432\u0435\u0440\u0434\u0438\u0442\u0435 \u0443\u0441\u0442\u0430\u043d\u043e\u0432\u043a\u0443");
        this.textView6.setVisibility(4);
        this.textView7.setVisibility(4);
        this.progressBarInstall.setVisibility(4);
        this.progressBar.setVisibility(0);
        this.progressBar.setProgress(100);
    }

    public void getFileSize(String string2) throws IOException {
        new Thread(new Runnable(this, string2){
            InputStream f115in;
            FileOutputStream file;
            final InstallClient this$0;
            URLConnection urlConnection;
            final String val$string;
            {
                this.this$0 = installClient;
                this.val$string = string2;
                this.file = new FileOutputStream(installClient.path_zip);
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
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("\u041e\u0448\u0438\u0431\u043a\u0430 ");
                        stringBuilder.append(exception);
                        Log.e((String)"Downn", (String)stringBuilder.toString());
                        Closeable closeable = this.file;
                        if (closeable != null) {
                            try {
                                ((FileOutputStream)closeable).close();
                            }
                            catch (IOException iOException) {
                                iOException.printStackTrace();
                            }
                        }
                        if ((closeable = this.f115in) == null) break block21;
                        try {
                            ((InputStream)closeable).close();
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
                        this.this$1.this$0.installAPK("launcher");
                    }
                });
                this.this$0.installAPK("launcher");
            }
        }).start();
    }

    @Override
    public void onCreate(Bundle object) {
        super.onCreate((Bundle)object);
        this.setContentView(2131558429);
        this.textview5 = (TextView)this.findViewById(2131362470);
        this.textView7 = (TextView)this.findViewById(2131362474);
        this.textView6 = (TextView)this.findViewById(2131362472);
        this.progressBar = (ProgressBar)this.findViewById(2131362294);
        object = new StringBuilder();
        ((StringBuilder)object).append(Environment.getExternalStoragePublicDirectory((String)Environment.DIRECTORY_DOWNLOADS));
        ((StringBuilder)object).append("/launcher.apk");
        object = new File(((StringBuilder)object).toString());
        if (((File)object).exists()) {
            ((File)object).delete();
        }
        this.progressBarInstall = (ProgressBar)this.findViewById(2131362291);
        try {
            this.getFileSize(String.valueOf(ApiService.getInstance().URL_CLIENT));
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }
}

