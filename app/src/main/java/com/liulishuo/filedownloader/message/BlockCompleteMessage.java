/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.message;

import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

public interface BlockCompleteMessage {
    public MessageSnapshot transmitToCompleted();

    public static class BlockCompleteMessageImpl
    extends MessageSnapshot
    implements BlockCompleteMessage {
        private final MessageSnapshot mCompletedSnapshot;

        public BlockCompleteMessageImpl(MessageSnapshot messageSnapshot) {
            super(messageSnapshot.getId());
            if (messageSnapshot.getStatus() == -3) {
                this.mCompletedSnapshot = messageSnapshot;
                return;
            }
            throw new IllegalArgumentException(FileDownloadUtils.formatString("can't create the block complete message for id[%d], status[%d]", messageSnapshot.getId(), messageSnapshot.getStatus()));
        }

        @Override
        public byte getStatus() {
            return 4;
        }

        @Override
        public MessageSnapshot transmitToCompleted() {
            return this.mCompletedSnapshot;
        }
    }
}

