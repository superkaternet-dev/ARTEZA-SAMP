/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.Service
 *  android.content.Intent
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.os.RemoteException
 */
package androidx.core.app;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.app.INotificationSideChannel;

public abstract class NotificationCompatSideChannelService
extends Service {
    public abstract void cancel(String var1, int var2, String var3);

    public abstract void cancelAll(String var1);

    void checkPermission(int n, String object) {
        Object object2 = this.getPackageManager().getPackagesForUid(n);
        int n2 = ((String[])object2).length;
        for (int i = 0; i < n2; ++i) {
            if (!object2[i].equals(object)) continue;
            return;
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("NotificationSideChannelService: Uid ");
        ((StringBuilder)object2).append(n);
        ((StringBuilder)object2).append(" is not authorized for package ");
        ((StringBuilder)object2).append((String)object);
        object = new SecurityException(((StringBuilder)object2).toString());
        throw object;
    }

    public abstract void notify(String var1, int var2, String var3, Notification var4);

    public IBinder onBind(Intent intent) {
        if (intent.getAction().equals("android.support.BIND_NOTIFICATION_SIDE_CHANNEL")) {
            if (Build.VERSION.SDK_INT > 19) {
                return null;
            }
            return new NotificationSideChannelStub(this);
        }
        return null;
    }

    private class NotificationSideChannelStub
    extends INotificationSideChannel.Stub {
        final NotificationCompatSideChannelService this$0;

        NotificationSideChannelStub(NotificationCompatSideChannelService notificationCompatSideChannelService) {
            this.this$0 = notificationCompatSideChannelService;
        }

        @Override
        public void cancel(String string2, int n, String string3) throws RemoteException {
            this.this$0.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l = NotificationSideChannelStub.clearCallingIdentity();
            try {
                this.this$0.cancel(string2, n, string3);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l);
            }
        }

        @Override
        public void cancelAll(String string2) {
            this.this$0.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l = NotificationSideChannelStub.clearCallingIdentity();
            try {
                this.this$0.cancelAll(string2);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l);
            }
        }

        @Override
        public void notify(String string2, int n, String string3, Notification notification) throws RemoteException {
            this.this$0.checkPermission(NotificationSideChannelStub.getCallingUid(), string2);
            long l = NotificationSideChannelStub.clearCallingIdentity();
            try {
                this.this$0.notify(string2, n, string3, notification);
                return;
            }
            finally {
                NotificationSideChannelStub.restoreCallingIdentity((long)l);
            }
        }
    }
}

