/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.interceptor;

import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.download.DownloadChain;
import java.io.IOException;

public interface Interceptor {

    public static interface Connect {
        public DownloadConnection.Connected interceptConnect(DownloadChain var1) throws IOException;
    }

    public static interface Fetch {
        public long interceptFetch(DownloadChain var1) throws IOException;
    }
}

