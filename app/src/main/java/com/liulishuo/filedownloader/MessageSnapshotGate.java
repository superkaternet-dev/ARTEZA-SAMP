/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadList;
import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.List;

public class MessageSnapshotGate
implements MessageSnapshotFlow.MessageReceiver {
    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean transmitMessage(List<BaseDownloadTask.IRunningTask> object, MessageSnapshot messageSnapshot) {
        Object object2;
        Object object3;
        Object object4;
        if (object.size() > 1 && messageSnapshot.getStatus() == -3) {
            object4 = object.iterator();
            while (object4.hasNext()) {
                object3 = (BaseDownloadTask.IRunningTask)object4.next();
                object2 = object3.getPauseLock();
                synchronized (object2) {
                    if (object3.getMessageHandler().updateMoreLikelyCompleted(messageSnapshot)) {
                        FileDownloadLog.d(this, "updateMoreLikelyCompleted", new Object[0]);
                        return true;
                    }
                }
            }
        }
        object3 = object.iterator();
        while (object3.hasNext()) {
            object4 = object3.next();
            object2 = object4.getPauseLock();
            synchronized (object2) {
                if (object4.getMessageHandler().updateKeepFlow(messageSnapshot)) {
                    FileDownloadLog.d(this, "updateKeepFlow", new Object[0]);
                    return true;
                }
            }
        }
        if (-4 == messageSnapshot.getStatus()) {
            object3 = object.iterator();
            while (object3.hasNext()) {
                object4 = (BaseDownloadTask.IRunningTask)object3.next();
                object2 = object4.getPauseLock();
                synchronized (object2) {
                    if (object4.getMessageHandler().updateSameFilePathTaskRunning(messageSnapshot)) {
                        FileDownloadLog.d(this, "updateSampleFilePathTaskRunning", new Object[0]);
                        return true;
                    }
                }
            }
        }
        if (object.size() != 1) return false;
        object2 = object.get(0);
        object = object2.getPauseLock();
        synchronized (object) {
            FileDownloadLog.d(this, "updateKeepAhead", new Object[0]);
            return object2.getMessageHandler().updateKeepAhead(messageSnapshot);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void receive(MessageSnapshot object) {
        String string2 = Integer.toString(((MessageSnapshot)object).getId()).intern();
        synchronized (string2) {
            Object object2 = FileDownloadList.getImpl().getReceiveServiceTaskList(((MessageSnapshot)object).getId());
            if (object2.size() > 0) {
                Object object3 = object2.get(0).getOrigin();
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "~~~callback %s old[%s] new[%s] %d", ((MessageSnapshot)object).getId(), object3.getStatus(), object.getStatus(), object2.size());
                }
                if (this.transmitMessage((List<BaseDownloadTask.IRunningTask>)object2, (MessageSnapshot)object)) return;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("The event isn't consumed, id:");
                stringBuilder.append(((MessageSnapshot)object).getId());
                stringBuilder.append(" status:");
                stringBuilder.append(object.getStatus());
                stringBuilder.append(" task-count:");
                stringBuilder.append(object2.size());
                object3 = new StringBuilder(stringBuilder.toString());
                object = object2.iterator();
                while (object.hasNext()) {
                    object2 = (BaseDownloadTask.IRunningTask)object.next();
                    ((StringBuilder)object3).append(" | ");
                    ((StringBuilder)object3).append(object2.getOrigin().getStatus());
                }
                FileDownloadLog.i(this, ((StringBuilder)object3).toString(), new Object[0]);
            } else {
                FileDownloadLog.i(this, "Receive the event %d, but there isn't any running task in the upper layer", object.getStatus());
            }
            return;
        }
    }
}

