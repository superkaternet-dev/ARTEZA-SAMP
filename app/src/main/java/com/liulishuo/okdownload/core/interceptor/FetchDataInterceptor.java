/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.interceptor;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.dispatcher.CallbackDispatcher;
import com.liulishuo.okdownload.core.download.DownloadChain;
import com.liulishuo.okdownload.core.exception.InterruptException;
import com.liulishuo.okdownload.core.file.MultiPointOutputStream;
import com.liulishuo.okdownload.core.interceptor.Interceptor;
import java.io.IOException;
import java.io.InputStream;

public class FetchDataInterceptor
implements Interceptor.Fetch {
    private final int blockIndex;
    private final CallbackDispatcher dispatcher;
    private final InputStream inputStream;
    private final MultiPointOutputStream outputStream;
    private final byte[] readBuffer;
    private final DownloadTask task;

    public FetchDataInterceptor(int n, InputStream inputStream, MultiPointOutputStream multiPointOutputStream, DownloadTask downloadTask) {
        this.blockIndex = n;
        this.inputStream = inputStream;
        this.readBuffer = new byte[downloadTask.getReadBufferSize()];
        this.outputStream = multiPointOutputStream;
        this.task = downloadTask;
        this.dispatcher = OkDownload.with().callbackDispatcher();
    }

    @Override
    public long interceptFetch(DownloadChain downloadChain) throws IOException {
        if (!downloadChain.getCache().isInterrupt()) {
            OkDownload.with().downloadStrategy().inspectNetworkOnWifi(downloadChain.getTask());
            int n = this.inputStream.read(this.readBuffer);
            if (n == -1) {
                return n;
            }
            this.outputStream.write(this.blockIndex, this.readBuffer, n);
            downloadChain.increaseCallbackBytes(n);
            if (this.dispatcher.isFetchProcessMoment(this.task)) {
                downloadChain.flushNoCallbackIncreaseBytes();
            }
            return n;
        }
        throw InterruptException.SIGNAL;
    }
}

