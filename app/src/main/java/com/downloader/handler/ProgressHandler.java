/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.Message
 */
package com.downloader.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.downloader.OnProgressListener;
import com.downloader.Progress;

public class ProgressHandler
extends Handler {
    private final OnProgressListener listener;

    public ProgressHandler(OnProgressListener onProgressListener) {
        super(Looper.getMainLooper());
        this.listener = onProgressListener;
    }

    public void handleMessage(Message object) {
        switch (object.what) {
            default: {
                super.handleMessage(object);
                break;
            }
            case 1: {
                if (this.listener == null) break;
                object = (Progress)object.obj;
                this.listener.onProgress((Progress)object);
            }
        }
    }
}

