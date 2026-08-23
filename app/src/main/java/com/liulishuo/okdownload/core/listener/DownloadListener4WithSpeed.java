/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.listener;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener4;
import com.liulishuo.okdownload.core.listener.assist.Listener4Assist;
import com.liulishuo.okdownload.core.listener.assist.Listener4SpeedAssistExtend;
import com.liulishuo.okdownload.core.listener.assist.ListenerModelHandler;

public abstract class DownloadListener4WithSpeed
extends DownloadListener4
implements Listener4SpeedAssistExtend.Listener4SpeedCallback {
    public DownloadListener4WithSpeed() {
        this(new Listener4SpeedAssistExtend());
    }

    private DownloadListener4WithSpeed(Listener4SpeedAssistExtend listener4SpeedAssistExtend) {
        super(new Listener4Assist<Listener4SpeedAssistExtend.Listener4SpeedModel>(new Listener4WithSpeedModelCreator()));
        listener4SpeedAssistExtend.setCallback(this);
        this.setAssistExtend(listener4SpeedAssistExtend);
    }

    @Override
    public final void blockEnd(DownloadTask downloadTask, int n, BlockInfo blockInfo) {
    }

    @Override
    public final void infoReady(DownloadTask downloadTask, BreakpointInfo breakpointInfo, boolean bl, Listener4Assist.Listener4Model listener4Model) {
    }

    @Override
    public final void progress(DownloadTask downloadTask, long l) {
    }

    @Override
    public final void progressBlock(DownloadTask downloadTask, int n, long l) {
    }

    @Override
    public final void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception, Listener4Assist.Listener4Model listener4Model) {
    }

    private static class Listener4WithSpeedModelCreator
    implements ListenerModelHandler.ModelCreator<Listener4SpeedAssistExtend.Listener4SpeedModel> {
        private Listener4WithSpeedModelCreator() {
        }

        @Override
        public Listener4SpeedAssistExtend.Listener4SpeedModel create(int n) {
            return new Listener4SpeedAssistExtend.Listener4SpeedModel(n);
        }
    }
}

