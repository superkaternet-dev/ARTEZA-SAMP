/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.net.ConnectivityManager
 *  android.os.Bundle
 *  android.os.Environment
 *  android.widget.Toast
 */
package com.blackrussia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Scanner;

public class Utils
extends AppCompatActivity {
    public static Integer INSTALL_TYPE_CLIENT;
    public static Integer INSTALL_TYPE_GRAPHICS;
    public static Integer INSTALL_TYPE_REINSTALL;
    public static Integer INSTALL_TYPE_UPDATE_GAMEFILES;
    static boolean downloading;
    static Integer typeInstall;

    static {
        downloading = false;
        typeInstall = 0;
        INSTALL_TYPE_CLIENT = 1;
        INSTALL_TYPE_REINSTALL = 2;
        INSTALL_TYPE_UPDATE_GAMEFILES = 3;
        INSTALL_TYPE_GRAPHICS = 4;
    }

    public static String convertStreamToString(InputStream object) {
        object = ((Scanner)(object = new Scanner((InputStream)object).useDelimiter("\\A"))).hasNext() ? ((Scanner)object).next().replace(",", ",\n") : "";
        return object;
    }

    public static String formatFileSize(long l) {
        double d = l;
        double d2 = l;
        Double.isNaN(d2);
        d2 /= 1024.0;
        double d3 = l;
        Double.isNaN(d3);
        d3 = d3 / 1024.0 / 1024.0;
        double d4 = l;
        Double.isNaN(d4);
        d4 = d4 / 1024.0 / 1024.0 / 1024.0;
        double d5 = l;
        Double.isNaN(d5);
        d5 = d5 / 1024.0 / 1024.0 / 1024.0 / 1024.0;
        Object object = new DecimalFormat("0.00");
        object = d5 > 1.0 ? ((NumberFormat)object).format(d5).concat(" \u0422\u0411") : (d4 > 1.0 ? ((NumberFormat)object).format(d4).concat(" \u0413\u0411") : (d3 > 1.0 ? ((NumberFormat)object).format(d3).concat(" \u041c\u0411") : (d2 > 1.0 ? ((NumberFormat)object).format(d2).concat(" \u041a\u0411") : ((NumberFormat)object).format(d).concat(" \u0411\u0430\u0439\u0442\u043e\u0432"))));
        return object;
    }

    public static boolean getDownloading() {
        return downloading;
    }

    public static Integer getInstallType() {
        return typeInstall;
    }

    public static boolean isGameInstalled() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append("/BlackRussia/texdb/gta3.img");
        return new File(stringBuilder.toString()).exists();
    }

    public static boolean isInternetConnected(Context context) {
        return (context = ((ConnectivityManager)context.getSystemService("connectivity")).getActiveNetworkInfo()) != null && context.isConnectedOrConnecting();
    }

    public static boolean setDownloading(boolean bl) {
        downloading = bl;
        return bl;
    }

    public static Integer setInstallType(int n) {
        Integer n2;
        typeInstall = n2 = Integer.valueOf(n);
        return n2;
    }

    public static void showMessage(String string2, Context context) {
        Toast.makeText((Context)context, (CharSequence)string2, (int)0).show();
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void writeFile(String object, String string2) {
        Throwable throwable2222222;
        Object object2;
        block12: {
            object2 = new File((String)object);
            try {
                if (!((File)object2).exists()) {
                    ((File)object2).createNewFile();
                }
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
            OutputStreamWriter outputStreamWriter = null;
            Object var6_9 = null;
            object2 = var6_9;
            Object object3 = outputStreamWriter;
            object2 = var6_9;
            object3 = outputStreamWriter;
            object2 = var6_9;
            object3 = outputStreamWriter;
            File file = new File((String)object);
            object2 = var6_9;
            object3 = outputStreamWriter;
            FileWriter fileWriter = new FileWriter(file, false);
            object2 = object = fileWriter;
            object3 = object;
            ((Writer)object).write(string2);
            object2 = object;
            object3 = object;
            ((OutputStreamWriter)object).flush();
            {
                catch (Throwable throwable2222222) {
                    break block12;
                }
                catch (IOException iOException) {}
                object2 = object3;
                {
                    iOException.printStackTrace();
                    if (object3 == null) return;
                }
                try {}
                catch (IOException iOException) {
                    iOException.printStackTrace();
                    return;
                }
                ((OutputStreamWriter)object3).close();
                return;
            }
            {
                ((OutputStreamWriter)object).close();
                return;
            }
        }
        if (object2 == null) throw throwable2222222;
        try {
            ((OutputStreamWriter)object2).close();
            throw throwable2222222;
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        throw throwable2222222;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }
}

