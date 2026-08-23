/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.listener.assist;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.assist.ListenerAssist;
import com.liulishuo.okdownload.core.listener.assist.ListenerModelHandler;
import java.util.concurrent.atomic.AtomicLong;

public class Listener1Assist
implements ListenerAssist,
ListenerModelHandler.ModelCreator<Listener1Model> {
    private Listener1Callback callback;
    private final ListenerModelHandler<Listener1Model> modelHandler;

    public Listener1Assist() {
        this.modelHandler = new ListenerModelHandler<Listener1Model>(this);
    }

    Listener1Assist(ListenerModelHandler<Listener1Model> listenerModelHandler) {
        this.modelHandler = listenerModelHandler;
    }

    public void connectEnd(DownloadTask downloadTask) {
        Listener1Callback listener1Callback;
        Listener1Model listener1Model = this.modelHandler.getOrRecoverModel(downloadTask, downloadTask.getInfo());
        if (listener1Model == null) {
            return;
        }
        if (listener1Model.isFromResumed.booleanValue() && listener1Model.isFirstConnect.booleanValue()) {
            listener1Model.isFirstConnect = false;
        }
        if ((listener1Callback = this.callback) != null) {
            listener1Callback.connected(downloadTask, listener1Model.blockCount, listener1Model.currentOffset.get(), listener1Model.totalLength);
        }
    }

    @Override
    public Listener1Model create(int n) {
        return new Listener1Model(n);
    }

    public void downloadFromBeginning(DownloadTask downloadTask, BreakpointInfo object, ResumeFailedCause resumeFailedCause) {
        Listener1Model listener1Model = this.modelHandler.getOrRecoverModel(downloadTask, (BreakpointInfo)object);
        if (listener1Model == null) {
            return;
        }
        listener1Model.onInfoValid((BreakpointInfo)object);
        if (listener1Model.isStarted.booleanValue() && (object = this.callback) != null) {
            object.retry(downloadTask, resumeFailedCause);
        }
        listener1Model.isStarted = true;
        listener1Model.isFromResumed = false;
        listener1Model.isFirstConnect = true;
    }

    public void downloadFromBreakpoint(DownloadTask object, BreakpointInfo breakpointInfo) {
        if ((object = this.modelHandler.getOrRecoverModel((DownloadTask)object, breakpointInfo)) == null) {
            return;
        }
        ((Listener1Model)object).onInfoValid(breakpointInfo);
        ((Listener1Model)object).isStarted = true;
        ((Listener1Model)object).isFromResumed = true;
        ((Listener1Model)object).isFirstConnect = true;
    }

    public void fetchProgress(DownloadTask downloadTask, long l) {
        Listener1Model listener1Model = this.modelHandler.getOrRecoverModel(downloadTask, downloadTask.getInfo());
        if (listener1Model == null) {
            return;
        }
        listener1Model.currentOffset.addAndGet(l);
        Listener1Callback listener1Callback = this.callback;
        if (listener1Callback != null) {
            listener1Callback.progress(downloadTask, listener1Model.currentOffset.get(), listener1Model.totalLength);
        }
    }

    @Override
    public boolean isAlwaysRecoverAssistModel() {
        return this.modelHandler.isAlwaysRecoverAssistModel();
    }

    @Override
    public void setAlwaysRecoverAssistModel(boolean bl) {
        this.modelHandler.setAlwaysRecoverAssistModel(bl);
    }

    @Override
    public void setAlwaysRecoverAssistModelIfNotSet(boolean bl) {
        this.modelHandler.setAlwaysRecoverAssistModelIfNotSet(bl);
    }

    public void setCallback(Listener1Callback listener1Callback) {
        this.callback = listener1Callback;
    }

    public void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
        Listener1Model listener1Model = this.modelHandler.removeOrCreate(downloadTask, downloadTask.getInfo());
        Listener1Callback listener1Callback = this.callback;
        if (listener1Callback != null) {
            listener1Callback.taskEnd(downloadTask, endCause, exception, listener1Model);
        }
    }

    public void taskStart(DownloadTask downloadTask) {
        Listener1Model listener1Model = this.modelHandler.addAndGetModel(downloadTask, null);
        Listener1Callback listener1Callback = this.callback;
        if (listener1Callback != null) {
            listener1Callback.taskStart(downloadTask, listener1Model);
        }
    }

    public static interface Listener1Callback {
        public void connected(DownloadTask var1, int var2, long var3, long var5);

        public void progress(DownloadTask var1, long var2, long var4);

        public void retry(DownloadTask var1, ResumeFailedCause var2);

        public void taskEnd(DownloadTask var1, EndCause var2, Exception var3, Listener1Model var4);

        public void taskStart(DownloadTask var1, Listener1Model var2);
    }

    public static class Listener1Model
    implements ListenerModelHandler.ListenerModel {
        int blockCount;
        final AtomicLong currentOffset = new AtomicLong();
        final int id;
        volatile Boolean isFirstConnect;
        Boolean isFromResumed;
        Boolean isStarted;
        long totalLength;

        Listener1Model(int n) {
            this.id = n;
        }

        @Override
        public int getId() {
            return this.id;
        }

        public long getTotalLength() {
            return this.totalLength;
        }

        @Override
        public void onInfoValid(BreakpointInfo object) {
            this.blockCount = ((BreakpointInfo)object).getBlockCount();
            this.totalLength = ((BreakpointInfo)object).getTotalLength();
            this.currentOffset.set(((BreakpointInfo)object).getTotalOffset());
            object = this.isStarted;
            boolean bl = false;
            if (object == null) {
                this.isStarted = false;
            }
            if (this.isFromResumed == null) {
                if (this.currentOffset.get() > 0L) {
                    bl = true;
                }
                this.isFromResumed = bl;
            }
            if (this.isFirstConnect == null) {
                this.isFirstConnect = true;
            }
        }
    }
}

