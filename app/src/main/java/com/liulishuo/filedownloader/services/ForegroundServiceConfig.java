/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Notification$Builder
 *  android.content.Context
 */
package com.liulishuo.filedownloader.services;

import android.app.Notification;
import android.content.Context;
import com.liulishuo.filedownloader.R;
import com.liulishuo.filedownloader.util.FileDownloadLog;

public class ForegroundServiceConfig {
    private static final String DEFAULT_NOTIFICATION_CHANNEL_ID = "filedownloader_channel";
    private static final String DEFAULT_NOTIFICATION_CHANNEL_NAME = "Filedownloader";
    private static final int DEFAULT_NOTIFICATION_ID = 17301506;
    private boolean needRecreateChannelId;
    private Notification notification;
    private String notificationChannelId;
    private String notificationChannelName;
    private int notificationId;

    private ForegroundServiceConfig() {
    }

    private Notification buildDefaultNotification(Context context) {
        String string2 = context.getString(R.string.default_filedownloader_notification_title);
        String string3 = context.getString(R.string.default_filedownloader_notification_content);
        context = new Notification.Builder(context, this.notificationChannelId);
        context.setContentTitle((CharSequence)string2).setContentText((CharSequence)string3).setSmallIcon(17301506);
        return context.build();
    }

    public Notification getNotification(Context context) {
        if (this.notification == null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "build default notification", new Object[0]);
            }
            this.notification = this.buildDefaultNotification(context);
        }
        return this.notification;
    }

    public String getNotificationChannelId() {
        return this.notificationChannelId;
    }

    public String getNotificationChannelName() {
        return this.notificationChannelName;
    }

    public int getNotificationId() {
        return this.notificationId;
    }

    public boolean isNeedRecreateChannelId() {
        return this.needRecreateChannelId;
    }

    public void setNeedRecreateChannelId(boolean bl) {
        this.needRecreateChannelId = bl;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public void setNotificationChannelId(String string2) {
        this.notificationChannelId = string2;
    }

    public void setNotificationChannelName(String string2) {
        this.notificationChannelName = string2;
    }

    public void setNotificationId(int n) {
        this.notificationId = n;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ForegroundServiceConfig{notificationId=");
        stringBuilder.append(this.notificationId);
        stringBuilder.append(", notificationChannelId='");
        stringBuilder.append(this.notificationChannelId);
        stringBuilder.append('\'');
        stringBuilder.append(", notificationChannelName='");
        stringBuilder.append(this.notificationChannelName);
        stringBuilder.append('\'');
        stringBuilder.append(", notification=");
        stringBuilder.append(this.notification);
        stringBuilder.append(", needRecreateChannelId=");
        stringBuilder.append(this.needRecreateChannelId);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static class Builder {
        private boolean needRecreateChannelId;
        private Notification notification;
        private String notificationChannelId;
        private String notificationChannelName;
        private int notificationId;

        public ForegroundServiceConfig build() {
            int n;
            String string2;
            ForegroundServiceConfig foregroundServiceConfig = new ForegroundServiceConfig();
            String string3 = string2 = this.notificationChannelId;
            if (string2 == null) {
                string3 = ForegroundServiceConfig.DEFAULT_NOTIFICATION_CHANNEL_ID;
            }
            foregroundServiceConfig.setNotificationChannelId(string3);
            string3 = string2 = this.notificationChannelName;
            if (string2 == null) {
                string3 = ForegroundServiceConfig.DEFAULT_NOTIFICATION_CHANNEL_NAME;
            }
            foregroundServiceConfig.setNotificationChannelName(string3);
            int n2 = n = this.notificationId;
            if (n == 0) {
                n2 = 17301506;
            }
            foregroundServiceConfig.setNotificationId(n2);
            foregroundServiceConfig.setNeedRecreateChannelId(this.needRecreateChannelId);
            foregroundServiceConfig.setNotification(this.notification);
            return foregroundServiceConfig;
        }

        public Builder needRecreateChannelId(boolean bl) {
            this.needRecreateChannelId = bl;
            return this;
        }

        public Builder notification(Notification notification) {
            this.notification = notification;
            return this;
        }

        public Builder notificationChannelId(String string2) {
            this.notificationChannelId = string2;
            return this;
        }

        public Builder notificationChannelName(String string2) {
            this.notificationChannelName = string2;
            return this;
        }

        public Builder notificationId(int n) {
            this.notificationId = n;
            return this;
        }
    }
}

