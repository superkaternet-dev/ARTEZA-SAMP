/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.os.Environment
 */
package com.nvidia.devtech;

import android.app.Activity;
import android.os.Environment;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

public class NvUtil {
    private static NvUtil instance = new NvUtil();
    private Activity activity = null;
    private HashMap<String, String> appLocalValues;

    private NvUtil() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        this.appLocalValues = hashMap;
        hashMap.put("STORAGE_ROOT", Environment.getExternalStorageDirectory().getAbsolutePath());
    }

    public static NvUtil getInstance() {
        return instance;
    }

    public void appendLog(String string2) {
        Object object = this.getAppLocalValue("STORAGE_ROOT");
        Comparable<StringBuilder> comparable = new StringBuilder();
        ((StringBuilder)comparable).append((String)object);
        ((StringBuilder)comparable).append("SAMP/javalog.txt");
        comparable = new File(((StringBuilder)comparable).toString());
        if (!((File)comparable).exists()) {
            try {
                ((File)comparable).createNewFile();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        try {
            object = new FileWriter((File)comparable, true);
            BufferedWriter bufferedWriter = new BufferedWriter((Writer)object);
            bufferedWriter.append(string2);
            bufferedWriter.newLine();
            bufferedWriter.close();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
    }

    public String getAppLocalValue(String string2) {
        return this.appLocalValues.get(string2);
    }

    public String getParameter(String string2) {
        return this.activity.getIntent().getStringExtra(string2);
    }

    public boolean hasAppLocalValue(String string2) {
        return this.appLocalValues.containsKey(string2);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setAppLocalValue(String string2, String string3) {
        this.appLocalValues.put(string2, string3);
    }
}

