/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.interceptor;

import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.download.DownloadCache;
import com.liulishuo.okdownload.core.download.DownloadChain;
import com.liulishuo.okdownload.core.exception.InterruptException;
import com.liulishuo.okdownload.core.exception.RetryException;
import com.liulishuo.okdownload.core.interceptor.Interceptor;
import java.io.IOException;

public class RetryInterceptor
implements Interceptor.Connect,
Interceptor.Fetch {
    @Override
    public DownloadConnection.Connected interceptConnect(DownloadChain downloadChain) throws IOException {
        DownloadCache downloadCache = downloadChain.getCache();
        while (true) {
            try {
                if (!downloadCache.isInterrupt()) {
                    return downloadChain.processConnect();
                }
                throw InterruptException.SIGNAL;
            }
            catch (IOException iOException) {
                if (iOException instanceof RetryException) {
                    downloadChain.resetConnectForRetry();
                    continue;
                }
                downloadChain.getCache().catchException(iOException);
                throw iOException;
            }
            break;
        }
    }

    @Override
    public long interceptFetch(DownloadChain downloadChain) throws IOException {
        try {
            long l = downloadChain.processFetch();
            return l;
        }
        catch (IOException iOException) {
            downloadChain.getCache().catchException(iOException);
            throw iOException;
        }
    }
}

