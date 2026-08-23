/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.message;

import com.liulishuo.filedownloader.message.IFlowDirectly;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotThreadPool;

public class MessageSnapshotFlow {
    private volatile MessageSnapshotThreadPool flowThreadPool;
    private volatile MessageReceiver receiver;

    public static MessageSnapshotFlow getImpl() {
        return HolderClass.INSTANCE;
    }

    public void inflow(MessageSnapshot messageSnapshot) {
        if (messageSnapshot instanceof IFlowDirectly) {
            if (this.receiver != null) {
                this.receiver.receive(messageSnapshot);
            }
        } else if (this.flowThreadPool != null) {
            this.flowThreadPool.execute(messageSnapshot);
        }
    }

    public void setReceiver(MessageReceiver messageReceiver) {
        this.receiver = messageReceiver;
        this.flowThreadPool = messageReceiver == null ? null : new MessageSnapshotThreadPool(5, messageReceiver);
    }

    public static final class HolderClass {
        private static final MessageSnapshotFlow INSTANCE = new MessageSnapshotFlow();
    }

    public static interface MessageReceiver {
        public void receive(MessageSnapshot var1);
    }
}

