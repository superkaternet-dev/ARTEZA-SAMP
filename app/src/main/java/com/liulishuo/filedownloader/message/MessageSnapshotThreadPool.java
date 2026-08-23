/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.message;

import com.liulishuo.filedownloader.message.MessageSnapshot;
import com.liulishuo.filedownloader.message.MessageSnapshotFlow;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;

public class MessageSnapshotThreadPool {
    private final List<FlowSingleExecutor> executorList;
    private final MessageSnapshotFlow.MessageReceiver receiver;

    MessageSnapshotThreadPool(int n, MessageSnapshotFlow.MessageReceiver messageReceiver) {
        this.receiver = messageReceiver;
        this.executorList = new ArrayList<FlowSingleExecutor>();
        for (int i = 0; i < n; ++i) {
            this.executorList.add(new FlowSingleExecutor(this, i));
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void execute(MessageSnapshot messageSnapshot) {
        Iterator<FlowSingleExecutor> iterator2;
        int n;
        FlowSingleExecutor flowSingleExecutor = null;
        FlowSingleExecutor flowSingleExecutor2 = null;
        FlowSingleExecutor flowSingleExecutor3 = null;
        FlowSingleExecutor flowSingleExecutor4 = flowSingleExecutor2;
        try {
            List<FlowSingleExecutor> list = this.executorList;
            flowSingleExecutor4 = flowSingleExecutor2;
            synchronized (list) {
                flowSingleExecutor4 = flowSingleExecutor;
                n = messageSnapshot.getId();
                flowSingleExecutor4 = flowSingleExecutor;
                iterator2 = this.executorList.iterator();
            }
        }
        catch (Throwable throwable) {
            flowSingleExecutor4.execute(messageSnapshot);
            throw throwable;
        }
        {
            block10: {
                do {
                    flowSingleExecutor2 = flowSingleExecutor3;
                    flowSingleExecutor4 = flowSingleExecutor;
                    if (!iterator2.hasNext()) break;
                    flowSingleExecutor4 = flowSingleExecutor;
                    flowSingleExecutor2 = iterator2.next();
                    flowSingleExecutor4 = flowSingleExecutor;
                } while (!flowSingleExecutor2.enQueueTaskIdList.contains(n));
                flowSingleExecutor3 = flowSingleExecutor2;
                if (flowSingleExecutor2 != null) break block10;
                int n2 = 0;
                flowSingleExecutor4 = flowSingleExecutor2;
                iterator2 = this.executorList.iterator();
                while (true) {
                    int n3;
                    block12: {
                        block11: {
                            flowSingleExecutor3 = flowSingleExecutor2;
                            flowSingleExecutor4 = flowSingleExecutor2;
                            if (!iterator2.hasNext()) break;
                            flowSingleExecutor4 = flowSingleExecutor2;
                            flowSingleExecutor3 = iterator2.next();
                            flowSingleExecutor4 = flowSingleExecutor2;
                            if (flowSingleExecutor3.enQueueTaskIdList.size() <= 0) break;
                            if (n2 == 0) break block11;
                            flowSingleExecutor = flowSingleExecutor2;
                            n3 = n2;
                            flowSingleExecutor4 = flowSingleExecutor2;
                            if (flowSingleExecutor3.enQueueTaskIdList.size() >= n2) break block12;
                        }
                        flowSingleExecutor4 = flowSingleExecutor2;
                        n3 = flowSingleExecutor3.enQueueTaskIdList.size();
                        flowSingleExecutor = flowSingleExecutor3;
                    }
                    flowSingleExecutor2 = flowSingleExecutor;
                    n2 = n3;
                }
            }
            flowSingleExecutor4 = flowSingleExecutor3;
            flowSingleExecutor3.enqueue(n);
            flowSingleExecutor4 = flowSingleExecutor3;
        }
        flowSingleExecutor3.execute(messageSnapshot);
    }

    public class FlowSingleExecutor {
        private final List<Integer> enQueueTaskIdList;
        private final Executor mExecutor;
        final MessageSnapshotThreadPool this$0;

        public FlowSingleExecutor(MessageSnapshotThreadPool object, int n) {
            this.this$0 = object;
            this.enQueueTaskIdList = new ArrayList<Integer>();
            object = new StringBuilder();
            ((StringBuilder)object).append("Flow-");
            ((StringBuilder)object).append(n);
            this.mExecutor = FileDownloadExecutors.newDefaultThreadPool(1, ((StringBuilder)object).toString());
        }

        public void enqueue(int n) {
            this.enQueueTaskIdList.add(n);
        }

        public void execute(MessageSnapshot messageSnapshot) {
            this.mExecutor.execute(new Runnable(this, messageSnapshot){
                final FlowSingleExecutor this$1;
                final MessageSnapshot val$snapshot;
                {
                    this.this$1 = flowSingleExecutor;
                    this.val$snapshot = messageSnapshot;
                }

                @Override
                public void run() {
                    this.this$1.this$0.receiver.receive(this.val$snapshot);
                    this.this$1.enQueueTaskIdList.remove((Object)this.val$snapshot.getId());
                }
            });
        }
    }
}

