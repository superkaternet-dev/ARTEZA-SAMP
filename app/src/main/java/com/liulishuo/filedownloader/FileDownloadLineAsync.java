/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 */
package com.liulishuo.filedownloader;

import android.app.Notification;
import com.liulishuo.filedownloader.FileDownloader;

public class FileDownloadLineAsync {
    public boolean startForeground(int n, Notification notification) {
        if (FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().startForeground(n, notification);
            return true;
        }
        FileDownloader.getImpl().bindService(new Runnable(this, n, notification){
            final FileDownloadLineAsync this$0;
            final int val$id;
            final Notification val$notification;
            {
                this.this$0 = fileDownloadLineAsync;
                this.val$id = n;
                this.val$notification = notification;
            }

            @Override
            public void run() {
                FileDownloader.getImpl().startForeground(this.val$id, this.val$notification);
            }
        });
        return false;
    }
}

