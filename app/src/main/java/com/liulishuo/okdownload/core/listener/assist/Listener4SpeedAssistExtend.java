/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.okdownload.core.listener.assist;

import android.util.SparseArray;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.SpeedCalculator;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.assist.Listener4Assist;
import com.liulishuo.okdownload.core.listener.assist.ListenerModelHandler;

public class Listener4SpeedAssistExtend
implements Listener4Assist.AssistExtend,
ListenerModelHandler.ModelCreator<Listener4SpeedModel> {
    private Listener4SpeedCallback callback;

    @Override
    public Listener4SpeedModel create(int n) {
        return new Listener4SpeedModel(n);
    }

    @Override
    public boolean dispatchBlockEnd(DownloadTask downloadTask, int n, Listener4Assist.Listener4Model listener4Model) {
        Listener4SpeedModel listener4SpeedModel = (Listener4SpeedModel)listener4Model;
        ((SpeedCalculator)listener4SpeedModel.blockSpeeds.get(n)).endTask();
        Listener4SpeedCallback listener4SpeedCallback = this.callback;
        if (listener4SpeedCallback != null) {
            listener4SpeedCallback.blockEnd(downloadTask, n, listener4Model.info.getBlock(n), listener4SpeedModel.getBlockSpeed(n));
        }
        return true;
    }

    @Override
    public boolean dispatchFetchProgress(DownloadTask downloadTask, int n, long l, Listener4Assist.Listener4Model listener4Model) {
        Listener4SpeedModel listener4SpeedModel = (Listener4SpeedModel)listener4Model;
        ((SpeedCalculator)listener4SpeedModel.blockSpeeds.get(n)).downloading(l);
        listener4SpeedModel.taskSpeed.downloading(l);
        Listener4SpeedCallback listener4SpeedCallback = this.callback;
        if (listener4SpeedCallback != null) {
            listener4SpeedCallback.progressBlock(downloadTask, n, (Long)listener4Model.blockCurrentOffsetMap.get(n), listener4SpeedModel.getBlockSpeed(n));
            this.callback.progress(downloadTask, listener4Model.currentOffset, listener4SpeedModel.taskSpeed);
        }
        return true;
    }

    @Override
    public boolean dispatchInfoReady(DownloadTask downloadTask, BreakpointInfo breakpointInfo, boolean bl, Listener4Assist.Listener4Model listener4Model) {
        Listener4SpeedCallback listener4SpeedCallback = this.callback;
        if (listener4SpeedCallback != null) {
            listener4SpeedCallback.infoReady(downloadTask, breakpointInfo, bl, (Listener4SpeedModel)listener4Model);
        }
        return true;
    }

    @Override
    public boolean dispatchTaskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception, Listener4Assist.Listener4Model object) {
        Listener4SpeedModel listener4SpeedModel = (Listener4SpeedModel)object;
        listener4SpeedModel.taskSpeed.endTask();
        object = this.callback;
        if (object != null) {
            object.taskEnd(downloadTask, endCause, exception, listener4SpeedModel.taskSpeed);
        }
        return true;
    }

    public void setCallback(Listener4SpeedCallback listener4SpeedCallback) {
        this.callback = listener4SpeedCallback;
    }

    public static interface Listener4SpeedCallback {
        public void blockEnd(DownloadTask var1, int var2, BlockInfo var3, SpeedCalculator var4);

        public void infoReady(DownloadTask var1, BreakpointInfo var2, boolean var3, Listener4SpeedModel var4);

        public void progress(DownloadTask var1, long var2, SpeedCalculator var4);

        public void progressBlock(DownloadTask var1, int var2, long var3, SpeedCalculator var5);

        public void taskEnd(DownloadTask var1, EndCause var2, Exception var3, SpeedCalculator var4);
    }

    public static class Listener4SpeedModel
    extends Listener4Assist.Listener4Model {
        SparseArray<SpeedCalculator> blockSpeeds;
        SpeedCalculator taskSpeed;

        public Listener4SpeedModel(int n) {
            super(n);
        }

        public SpeedCalculator getBlockSpeed(int n) {
            return (SpeedCalculator)this.blockSpeeds.get(n);
        }

        public SpeedCalculator getTaskSpeed() {
            return this.taskSpeed;
        }

        @Override
        public void onInfoValid(BreakpointInfo breakpointInfo) {
            super.onInfoValid(breakpointInfo);
            this.taskSpeed = new SpeedCalculator();
            this.blockSpeeds = new SparseArray();
            int n = breakpointInfo.getBlockCount();
            for (int i = 0; i < n; ++i) {
                this.blockSpeeds.put(i, (Object)new SpeedCalculator());
            }
        }
    }
}

