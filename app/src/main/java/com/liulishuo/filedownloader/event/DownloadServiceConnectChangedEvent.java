/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.event;

import com.liulishuo.filedownloader.event.IDownloadEvent;

public class DownloadServiceConnectChangedEvent
extends IDownloadEvent {
    public static final String ID = "event.service.connect.changed";
    private final Class<?> serviceClass;
    private final ConnectStatus status;

    public DownloadServiceConnectChangedEvent(ConnectStatus connectStatus, Class<?> clazz) {
        super(ID);
        this.status = connectStatus;
        this.serviceClass = clazz;
    }

    public ConnectStatus getStatus() {
        return this.status;
    }

    public boolean isSuchService(Class<?> clazz) {
        Class<?> clazz2 = this.serviceClass;
        boolean bl = clazz2 != null && clazz2.getName().equals(clazz.getName());
        return bl;
    }

    public static final class ConnectStatus
    extends Enum<ConnectStatus> {
        private static final ConnectStatus[] $VALUES;
        public static final /* enum */ ConnectStatus connected;
        public static final /* enum */ ConnectStatus disconnected;
        public static final /* enum */ ConnectStatus lost;

        static {
            ConnectStatus connectStatus;
            ConnectStatus connectStatus2;
            ConnectStatus connectStatus3;
            connected = connectStatus3 = new ConnectStatus();
            disconnected = connectStatus2 = new ConnectStatus();
            lost = connectStatus = new ConnectStatus();
            $VALUES = new ConnectStatus[]{connectStatus3, connectStatus2, connectStatus};
        }

        public static ConnectStatus valueOf(String string2) {
            return Enum.valueOf(ConnectStatus.class, string2);
        }

        public static ConnectStatus[] values() {
            return (ConnectStatus[])$VALUES.clone();
        }
    }
}

