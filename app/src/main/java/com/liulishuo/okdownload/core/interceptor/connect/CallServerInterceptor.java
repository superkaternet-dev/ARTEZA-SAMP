/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.interceptor.connect;

import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.download.DownloadChain;
import com.liulishuo.okdownload.core.interceptor.Interceptor;
import java.io.IOException;

public class CallServerInterceptor
implements Interceptor.Connect {
    @Override
    public DownloadConnection.Connected interceptConnect(DownloadChain downloadChain) throws IOException {
        OkDownload.with().downloadStrategy().inspectNetworkOnWifi(downloadChain.getTask());
        OkDownload.with().downloadStrategy().inspectNetworkAvailable();
        return downloadChain.getConnectionOrCreate().execute();
    }
}

