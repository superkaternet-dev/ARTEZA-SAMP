/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.okdownload.core.listener.assist;

import android.util.SparseArray;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.assist.ListenerAssist;
import com.liulishuo.okdownload.core.listener.assist.ListenerModelHandler;

public class Listener4Assist<T extends Listener4Model>
implements ListenerAssist {
    private AssistExtend assistExtend;
    Listener4Callback callback;
    private final ListenerModelHandler<T> modelHandler;

    public Listener4Assist(ListenerModelHandler.ModelCreator<T> modelCreator) {
        this.modelHandler = new ListenerModelHandler<T>(modelCreator);
    }

    Listener4Assist(ListenerModelHandler<T> listenerModelHandler) {
        this.modelHandler = listenerModelHandler;
    }

    public void fetchEnd(DownloadTask downloadTask, int n) {
        Listener4Model listener4Model = (Listener4Model)this.modelHandler.getOrRecoverModel(downloadTask, downloadTask.getInfo());
        if (listener4Model == null) {
            return;
        }
        Object object = this.assistExtend;
        if (object != null && object.dispatchBlockEnd(downloadTask, n, listener4Model)) {
            return;
        }
        object = this.callback;
        if (object != null) {
            object.blockEnd(downloadTask, n, listener4Model.info.getBlock(n));
        }
    }

    public void fetchProgress(DownloadTask downloadTask, int n, long l) {
        Listener4Model listener4Model = (Listener4Model)this.modelHandler.getOrRecoverModel(downloadTask, downloadTask.getInfo());
        if (listener4Model == null) {
            return;
        }
        long l2 = (Long)listener4Model.blockCurrentOffsetMap.get(n) + l;
        listener4Model.blockCurrentOffsetMap.put(n, (Object)l2);
        listener4Model.currentOffset += l;
        Object object = this.assistExtend;
        if (object != null && object.dispatchFetchProgress(downloadTask, n, l, listener4Model)) {
            return;
        }
        object = this.callback;
        if (object != null) {
            object.progressBlock(downloadTask, n, l2);
            this.callback.progress(downloadTask, listener4Model.currentOffset);
        }
    }

    public AssistExtend getAssistExtend() {
        return this.assistExtend;
    }

    public void infoReady(DownloadTask downloadTask, BreakpointInfo breakpointInfo, boolean bl) {
        Listener4Model listener4Model = (Listener4Model)this.modelHandler.addAndGetModel(downloadTask, breakpointInfo);
        Object object = this.assistExtend;
        if (object != null && object.dispatchInfoReady(downloadTask, breakpointInfo, bl, listener4Model)) {
            return;
        }
        object = this.callback;
        if (object != null) {
            object.infoReady(downloadTask, breakpointInfo, bl, listener4Model);
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

    public void setAssistExtend(AssistExtend assistExtend) {
        this.assistExtend = assistExtend;
    }

    public void setCallback(Listener4Callback listener4Callback) {
        this.callback = listener4Callback;
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
        synchronized (this) {
            void var3_3;
            void var2_2;
            boolean bl;
            Listener4Model listener4Model = (Listener4Model)this.modelHandler.removeOrCreate(downloadTask, downloadTask.getInfo());
            Object object = this.assistExtend;
            if (object != null && (bl = object.dispatchTaskEnd(downloadTask, (EndCause)var2_2, (Exception)var3_3, listener4Model))) {
                return;
            }
            object = this.callback;
            if (object != null) {
                object.taskEnd(downloadTask, (EndCause)var2_2, (Exception)var3_3, listener4Model);
            }
            return;
        }
    }

    public static interface AssistExtend {
        public boolean dispatchBlockEnd(DownloadTask var1, int var2, Listener4Model var3);

        public boolean dispatchFetchProgress(DownloadTask var1, int var2, long var3, Listener4Model var5);

        public boolean dispatchInfoReady(DownloadTask var1, BreakpointInfo var2, boolean var3, Listener4Model var4);

        public boolean dispatchTaskEnd(DownloadTask var1, EndCause var2, Exception var3, Listener4Model var4);
    }

    public static interface Listener4Callback {
        public void blockEnd(DownloadTask var1, int var2, BlockInfo var3);

        public void infoReady(DownloadTask var1, BreakpointInfo var2, boolean var3, Listener4Model var4);

        public void progress(DownloadTask var1, long var2);

        public void progressBlock(DownloadTask var1, int var2, long var3);

        public void taskEnd(DownloadTask var1, EndCause var2, Exception var3, Listener4Model var4);
    }

    public static class Listener4Model
    implements ListenerModelHandler.ListenerModel {
        SparseArray<Long> blockCurrentOffsetMap;
        long currentOffset;
        private final int id;
        BreakpointInfo info;

        public Listener4Model(int n) {
            this.id = n;
        }

        public SparseArray<Long> cloneBlockCurrentOffsetMap() {
            return this.blockCurrentOffsetMap.clone();
        }

        public long getBlockCurrentOffset(int n) {
            return (Long)this.blockCurrentOffsetMap.get(n);
        }

        SparseArray<Long> getBlockCurrentOffsetMap() {
            return this.blockCurrentOffsetMap;
        }

        public long getCurrentOffset() {
            return this.currentOffset;
        }

        @Override
        public int getId() {
            return this.id;
        }

        public BreakpointInfo getInfo() {
            return this.info;
        }

        @Override
        public void onInfoValid(BreakpointInfo breakpointInfo) {
            this.info = breakpointInfo;
            this.currentOffset = breakpointInfo.getTotalOffset();
            SparseArray sparseArray = new SparseArray();
            int n = breakpointInfo.getBlockCount();
            for (int i = 0; i < n; ++i) {
                sparseArray.put(i, (Object)breakpointInfo.getBlock(i).getCurrentOffset());
            }
            this.blockCurrentOffsetMap = sparseArray;
        }
    }
}

