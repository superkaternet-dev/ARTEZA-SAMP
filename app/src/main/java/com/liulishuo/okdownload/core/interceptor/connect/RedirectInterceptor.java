/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.interceptor.connect;

import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.download.DownloadChain;
import com.liulishuo.okdownload.core.exception.InterruptException;
import com.liulishuo.okdownload.core.interceptor.Interceptor;
import java.io.IOException;
import java.net.ProtocolException;

public class RedirectInterceptor
implements Interceptor.Connect {
    private static final int HTTP_PERMANENT_REDIRECT = 308;
    private static final int HTTP_TEMPORARY_REDIRECT = 307;
    static final int MAX_REDIRECT_TIMES = 10;

    private static boolean isRedirect(int n) {
        boolean bl = n == 301 || n == 302 || n == 303 || n == 300 || n == 307 || n == 308;
        return bl;
    }

    @Override
    public DownloadConnection.Connected interceptConnect(DownloadChain object) throws IOException {
        int n = 0;
        while (!((DownloadChain)object).getCache().isInterrupt()) {
            Object object2 = ((DownloadChain)object).processConnect();
            int n2 = object2.getResponseCode();
            if (!RedirectInterceptor.isRedirect(n2)) {
                return object2;
            }
            if (++n < 10) {
                if ((object2 = object2.getResponseHeaderField("Location")) != null) {
                    ((DownloadChain)object).releaseConnection();
                    ((DownloadChain)object).setConnection(OkDownload.with().connectionFactory().create((String)object2));
                    ((DownloadChain)object).setRedirectLocation((String)object2);
                    continue;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Response code is ");
                ((StringBuilder)object).append(n2);
                ((StringBuilder)object).append(" but can't find Location field");
                throw new ProtocolException(((StringBuilder)object).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Too many redirect requests: ");
            ((StringBuilder)object).append(n);
            throw new ProtocolException(((StringBuilder)object).toString());
        }
        object = InterruptException.SIGNAL;
        throw object;
    }
}

