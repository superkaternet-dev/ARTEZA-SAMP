/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.os.Looper
 */
package com.liulishuo.filedownloader;

import android.app.Notification;
import android.os.Looper;
import com.liulishuo.filedownloader.FileDownloader;
import java.io.File;

public class FileDownloadLine {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void wait(ConnectSubscriber object) {
        object = new ConnectListener((ConnectSubscriber)object);
        synchronized (object) {
            FileDownloader.getImpl().bindService((Runnable)object);
            if (!((ConnectListener)object).isFinished()) {
                Thread thread2;
                Object object2 = Thread.currentThread();
                if (object2 == (thread2 = Looper.getMainLooper().getThread())) {
                    object2 = new IllegalThreadStateException("Sorry, FileDownloader can not block the main thread, because the system is also  callbacks ServiceConnection#onServiceConnected method in the main thread.");
                    throw object2;
                }
                try {
                    object.wait(200000L);
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            return;
        }
    }

    public long getSoFar(int n) {
        if (FileDownloader.getImpl().isServiceConnected()) {
            return FileDownloader.getImpl().getSoFar(n);
        }
        ConnectSubscriber connectSubscriber = new ConnectSubscriber(this, n){
            private long mValue;
            final FileDownloadLine this$0;
            final int val$id;
            {
                this.this$0 = fileDownloadLine;
                this.val$id = n;
            }

            @Override
            public void connected() {
                this.mValue = FileDownloader.getImpl().getSoFar(this.val$id);
            }

            @Override
            public Object getValue() {
                return this.mValue;
            }
        };
        this.wait(connectSubscriber);
        return (Long)connectSubscriber.getValue();
    }

    public byte getStatus(int n, String object) {
        if (FileDownloader.getImpl().isServiceConnected()) {
            return FileDownloader.getImpl().getStatus(n, (String)object);
        }
        if (object != null && new File((String)object).exists()) {
            return -3;
        }
        object = new ConnectSubscriber(this, n, (String)object){
            private byte mValue;
            final FileDownloadLine this$0;
            final int val$id;
            final String val$path;
            {
                this.this$0 = fileDownloadLine;
                this.val$id = n;
                this.val$path = string2;
            }

            @Override
            public void connected() {
                this.mValue = FileDownloader.getImpl().getStatus(this.val$id, this.val$path);
            }

            @Override
            public Object getValue() {
                return this.mValue;
            }
        };
        this.wait((ConnectSubscriber)object);
        return (Byte)object.getValue();
    }

    public long getTotal(int n) {
        if (FileDownloader.getImpl().isServiceConnected()) {
            return FileDownloader.getImpl().getTotal(n);
        }
        ConnectSubscriber connectSubscriber = new ConnectSubscriber(this, n){
            private long mValue;
            final FileDownloadLine this$0;
            final int val$id;
            {
                this.this$0 = fileDownloadLine;
                this.val$id = n;
            }

            @Override
            public void connected() {
                this.mValue = FileDownloader.getImpl().getTotal(this.val$id);
            }

            @Override
            public Object getValue() {
                return this.mValue;
            }
        };
        this.wait(connectSubscriber);
        return (Long)connectSubscriber.getValue();
    }

    public void startForeground(int n, Notification notification) {
        if (FileDownloader.getImpl().isServiceConnected()) {
            FileDownloader.getImpl().startForeground(n, notification);
            return;
        }
        this.wait(new ConnectSubscriber(this, n, notification){
            final FileDownloadLine this$0;
            final int val$id;
            final Notification val$notification;
            {
                this.this$0 = fileDownloadLine;
                this.val$id = n;
                this.val$notification = notification;
            }

            @Override
            public void connected() {
                FileDownloader.getImpl().startForeground(this.val$id, this.val$notification);
            }

            @Override
            public Object getValue() {
                return null;
            }
        });
    }

    static class ConnectListener
    implements Runnable {
        private boolean mIsFinished = false;
        private final ConnectSubscriber mSubscriber;

        ConnectListener(ConnectSubscriber connectSubscriber) {
            this.mSubscriber = connectSubscriber;
        }

        public boolean isFinished() {
            return this.mIsFinished;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void run() {
            synchronized (this) {
                this.mSubscriber.connected();
                this.mIsFinished = true;
                this.notifyAll();
                return;
            }
        }
    }

    static interface ConnectSubscriber {
        public void connected();

        public Object getValue();
    }
}

