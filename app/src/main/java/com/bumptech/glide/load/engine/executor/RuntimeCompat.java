/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Build$VERSION
 *  android.os.StrictMode
 *  android.os.StrictMode$ThreadPolicy
 *  android.util.Log
 */
package com.bumptech.glide.load.engine.executor;

import android.os.Build;
import android.os.StrictMode;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;

final class RuntimeCompat {
    private static final String CPU_LOCATION = "/sys/devices/system/cpu/";
    private static final String CPU_NAME_REGEX = "cpu[0-9]+";
    private static final String TAG = "GlideRuntimeCompat";

    private RuntimeCompat() {
    }

    static int availableProcessors() {
        int n;
        int n2 = n = Runtime.getRuntime().availableProcessors();
        if (Build.VERSION.SDK_INT < 17) {
            n2 = Math.max(RuntimeCompat.getCoreCountPre17(), n);
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static int getCoreCountPre17() {
        int n;
        FilenameFilter filenameFilter;
        FilenameFilter filenameFilter2 = null;
        StrictMode.ThreadPolicy threadPolicy = StrictMode.allowThreadDiskReads();
        try {
            File file = new File(CPU_LOCATION);
            Pattern pattern = Pattern.compile(CPU_NAME_REGEX);
            filenameFilter = new FilenameFilter(pattern){
                final Pattern val$cpuNamePattern;
                {
                    this.val$cpuNamePattern = pattern;
                }

                @Override
                public boolean accept(File file, String string2) {
                    return this.val$cpuNamePattern.matcher(string2).matches();
                }
            };
            filenameFilter = file.listFiles(filenameFilter);
        }
        catch (Throwable throwable) {
            filenameFilter = filenameFilter2;
            try {
                if (Log.isLoggable((String)TAG, (int)6)) {
                    Log.e((String)TAG, (String)"Failed to calculate accurate cpu count", (Throwable)throwable);
                    filenameFilter = filenameFilter2;
                }
            }
            finally {
                StrictMode.setThreadPolicy((StrictMode.ThreadPolicy)threadPolicy);
            }
        }
        if (filenameFilter != null) {
            n = (filenameFilter).length;
            return Math.max(1, n);
        }
        n = 0;
        return Math.max(1, n);
    }
}

