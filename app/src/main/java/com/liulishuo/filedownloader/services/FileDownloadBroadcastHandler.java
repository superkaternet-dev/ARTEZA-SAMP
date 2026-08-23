/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Parcelable
 */
package com.liulishuo.filedownloader.services;

import android.content.Intent;
import android.os.Parcelable;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public class FileDownloadBroadcastHandler {
    public static final String ACTION_COMPLETED = "filedownloader.intent.action.completed";
    public static final String KEY_MODEL = "model";

    public static FileDownloadModel parseIntent(Intent intent) {
        if (ACTION_COMPLETED.equals(intent.getAction())) {
            return (FileDownloadModel)intent.getParcelableExtra(KEY_MODEL);
        }
        throw new IllegalArgumentException(FileDownloadUtils.formatString("can't recognize the intent with action %s, on the current version we only support action [%s]", intent.getAction(), ACTION_COMPLETED));
    }

    public static void sendCompletedBroadcast(FileDownloadModel fileDownloadModel) {
        if (fileDownloadModel != null) {
            if (fileDownloadModel.getStatus() == -3) {
                Intent intent = new Intent(ACTION_COMPLETED);
                intent.putExtra(KEY_MODEL, (Parcelable)fileDownloadModel);
                FileDownloadHelper.getAppContext().sendBroadcast(intent);
                return;
            }
            throw new IllegalStateException();
        }
        throw new IllegalArgumentException();
    }
}

