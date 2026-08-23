/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.interceptor.connect;

import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.download.DownloadChain;
import com.liulishuo.okdownload.core.download.DownloadStrategy;
import com.liulishuo.okdownload.core.exception.InterruptException;
import com.liulishuo.okdownload.core.interceptor.Interceptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderInterceptor
implements Interceptor.Connect {
    private static final String TAG = "HeaderInterceptor";

    @Override
    public DownloadConnection.Connected interceptConnect(DownloadChain object) throws IOException {
        int n;
        BreakpointInfo breakpointInfo = ((DownloadChain)object).getInfo();
        Object object2 = ((DownloadChain)object).getConnectionOrCreate();
        DownloadTask downloadTask = ((DownloadChain)object).getTask();
        Map<String, List<String>> map = downloadTask.getHeaderMapFields();
        if (map != null) {
            Util.addUserRequestHeaderField(map, (DownloadConnection)object2);
        }
        if (map == null || !map.containsKey("User-Agent")) {
            Util.addDefaultUserAgent((DownloadConnection)object2);
        }
        if ((map = breakpointInfo.getBlock(n = ((DownloadChain)object).getBlockIndex())) != null) {
            Object object3 = new StringBuilder();
            ((StringBuilder)object3).append("bytes=");
            ((StringBuilder)object3).append(((BlockInfo)((Object)map)).getRangeLeft());
            ((StringBuilder)object3).append("-");
            String string2 = ((StringBuilder)object3).toString();
            object3 = new StringBuilder();
            ((StringBuilder)object3).append(string2);
            ((StringBuilder)object3).append(((BlockInfo)((Object)map)).getRangeRight());
            object2.addHeader("Range", ((StringBuilder)object3).toString());
            object3 = new StringBuilder();
            ((StringBuilder)object3).append("AssembleHeaderRange (");
            ((StringBuilder)object3).append(downloadTask.getId());
            ((StringBuilder)object3).append(") block(");
            ((StringBuilder)object3).append(n);
            ((StringBuilder)object3).append(") ");
            ((StringBuilder)object3).append("downloadFrom(");
            ((StringBuilder)object3).append(((BlockInfo)((Object)map)).getRangeLeft());
            ((StringBuilder)object3).append(") currentOffset(");
            ((StringBuilder)object3).append(((BlockInfo)((Object)map)).getCurrentOffset());
            ((StringBuilder)object3).append(")");
            Util.d(TAG, ((StringBuilder)object3).toString());
            map = breakpointInfo.getEtag();
            if (!Util.isEmpty((CharSequence)((Object)map))) {
                object2.addHeader("If-Match", (String)((Object)map));
            }
            if (!((DownloadChain)object).getCache().isInterrupt()) {
                OkDownload.with().callbackDispatcher().dispatch().connectStart(downloadTask, n, object2.getRequestProperties());
                object3 = ((DownloadChain)object).processConnect();
                map = object3.getResponseHeaderFields();
                object2 = map;
                if (map == null) {
                    object2 = new HashMap<String, List<String>>();
                }
                OkDownload.with().callbackDispatcher().dispatch().connectEnd(downloadTask, n, object3.getResponseCode(), (Map<String, List<String>>)object2);
                if (!((DownloadChain)object).getCache().isInterrupt()) {
                    object2 = OkDownload.with().downloadStrategy();
                    ((DownloadStrategy)object2).resumeAvailableResponseCheck((DownloadConnection.Connected)object3, n, breakpointInfo).inspect();
                    object2 = object3.getResponseHeaderField("Content-Length");
                    long l = object2 != null && ((String)object2).length() != 0 ? Util.parseContentLength((String)object2) : Util.parseContentLengthFromContentRange(object3.getResponseHeaderField("Content-Range"));
                    ((DownloadChain)object).setResponseContentLength(l);
                    return object3;
                }
                throw InterruptException.SIGNAL;
            }
            throw InterruptException.SIGNAL;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("No block-info found on ");
        ((StringBuilder)object).append(n);
        throw new IOException(((StringBuilder)object).toString());
    }
}

