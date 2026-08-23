/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.IBinder
 */
package com.liulishuo.filedownloader.services;

import android.content.Intent;
import android.os.IBinder;

interface IFileDownloadServiceHandler {
    public IBinder onBind(Intent var1);

    public void onDestroy();

    public void onStartCommand(Intent var1, int var2, int var3);
}

