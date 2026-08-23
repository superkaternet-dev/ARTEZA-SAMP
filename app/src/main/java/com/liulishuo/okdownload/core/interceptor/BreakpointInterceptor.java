/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.interceptor;

import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.download.DownloadChain;
import com.liulishuo.okdownload.core.exception.InterruptException;
import com.liulishuo.okdownload.core.exception.RetryException;
import com.liulishuo.okdownload.core.file.MultiPointOutputStream;
import com.liulishuo.okdownload.core.interceptor.Interceptor;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BreakpointInterceptor
implements Interceptor.Connect,
Interceptor.Fetch {
    private static final Pattern CONTENT_RANGE_RIGHT_VALUE = Pattern.compile(".*\\d+ *- *(\\d+) */ *\\d+");
    private static final String TAG = "BreakpointInterceptor";

    static long getRangeRightFromContentRange(String object) {
        if (((Matcher)(object = CONTENT_RANGE_RIGHT_VALUE.matcher((CharSequence)object))).find()) {
            return Long.parseLong(((Matcher)object).group(1));
        }
        return -1L;
    }

    long getExactContentLengthRangeFrom0(DownloadConnection.Connected object) {
        long l;
        String string2 = object.getResponseHeaderField("Content-Range");
        long l2 = l = -1L;
        if (!Util.isEmpty(string2)) {
            long l3 = BreakpointInterceptor.getRangeRightFromContentRange(string2);
            l2 = l;
            if (l3 > 0L) {
                l2 = l3 + 1L;
            }
        }
        l = l2;
        if (l2 < 0L) {
            object = object.getResponseHeaderField("Content-Length");
            l = l2;
            if (!Util.isEmpty((CharSequence)object)) {
                l = Long.parseLong((String)object);
            }
        }
        return l;
    }

    @Override
    public DownloadConnection.Connected interceptConnect(DownloadChain object) throws IOException {
        DownloadConnection.Connected connected = ((DownloadChain)object).processConnect();
        BreakpointInfo breakpointInfo = ((DownloadChain)object).getInfo();
        if (!((DownloadChain)object).getCache().isInterrupt()) {
            block9: {
                int n = breakpointInfo.getBlockCount();
                boolean bl = true;
                if (n == 1 && !breakpointInfo.isChunked()) {
                    long l = this.getExactContentLengthRangeFrom0(connected);
                    long l2 = breakpointInfo.getTotalLength();
                    if (l > 0L && l != l2) {
                        Object object2 = new StringBuilder();
                        ((StringBuilder)object2).append("SingleBlock special check: the response instance-length[");
                        ((StringBuilder)object2).append(l);
                        ((StringBuilder)object2).append("] isn't equal to the instance length from trial-");
                        ((StringBuilder)object2).append("connection[");
                        ((StringBuilder)object2).append(l2);
                        ((StringBuilder)object2).append("]");
                        Util.d(TAG, ((StringBuilder)object2).toString());
                        if (breakpointInfo.getBlock(0).getRangeLeft() == 0L) {
                            bl = false;
                        }
                        object2 = new BlockInfo(0L, l);
                        breakpointInfo.resetBlockInfos();
                        breakpointInfo.addBlock((BlockInfo)object2);
                        if (!bl) {
                            OkDownload.with().callbackDispatcher().dispatch().downloadFromBeginning(((DownloadChain)object).getTask(), breakpointInfo, ResumeFailedCause.CONTENT_LENGTH_CHANGED);
                        } else {
                            Util.w(TAG, "Discard breakpoint because of on this special case, we have to download from beginning");
                            throw new RetryException("Discard breakpoint because of on this special case, we have to download from beginning");
                        }
                    }
                }
                object = ((DownloadChain)object).getDownloadStore();
                try {
                    if (!object.update(breakpointInfo)) break block9;
                    return connected;
                }
                catch (Exception exception) {
                    throw new IOException("Update store failed!", exception);
                }
            }
            object = new IOException("Update store failed!");
            throw object;
        }
        throw InterruptException.SIGNAL;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public long interceptFetch(DownloadChain object) throws IOException {
        long l = ((DownloadChain)object).getResponseContentLength();
        int n = ((DownloadChain)object).getBlockIndex();
        boolean bl = l != -1L;
        long l2 = 0L;
        MultiPointOutputStream multiPointOutputStream = ((DownloadChain)object).getOutputStream();
        while (true) {
            long l3;
            block4: {
                block5: {
                    try {
                        l3 = ((DownloadChain)object).loopFetch();
                        if (l3 != -1L) break block4;
                        ((DownloadChain)object).flushNoCallbackIncreaseBytes();
                        if (((DownloadChain)object).getCache().isUserCanceled()) break block5;
                    }
                    catch (Throwable throwable) {
                        ((DownloadChain)object).flushNoCallbackIncreaseBytes();
                        if (((DownloadChain)object).getCache().isUserCanceled()) throw throwable;
                        multiPointOutputStream.done(n);
                        throw throwable;
                    }
                    multiPointOutputStream.done(n);
                }
                if (!bl) return l2;
                multiPointOutputStream.inspectComplete(n);
                if (l2 == l) {
                    return l2;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Fetch-length isn't equal to the response content-length, ");
                ((StringBuilder)object).append(l2);
                ((StringBuilder)object).append("!= ");
                ((StringBuilder)object).append(l);
                throw new IOException(((StringBuilder)object).toString());
            }
            l2 += l3;
        }
    }
}

