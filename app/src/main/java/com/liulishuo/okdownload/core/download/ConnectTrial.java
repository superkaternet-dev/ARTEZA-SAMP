/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.download;

import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConnectTrial {
    private static final Pattern CONTENT_DISPOSITION_NON_QUOTED_PATTERN;
    private static final Pattern CONTENT_DISPOSITION_QUOTED_PATTERN;
    private static final String TAG = "ConnectTrial";
    private boolean acceptRange;
    private final BreakpointInfo info;
    private long instanceLength;
    private int responseCode;
    private String responseEtag;
    private String responseFilename;
    private final DownloadTask task;

    static {
        CONTENT_DISPOSITION_QUOTED_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*\"([^\"]*)\"");
        CONTENT_DISPOSITION_NON_QUOTED_PATTERN = Pattern.compile("attachment;\\s*filename\\s*=\\s*(.*)");
    }

    public ConnectTrial(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
        this.task = downloadTask;
        this.info = breakpointInfo;
    }

    private static String findEtag(DownloadConnection.Connected connected) {
        return connected.getResponseHeaderField("Etag");
    }

    private static String findFilename(DownloadConnection.Connected connected) {
        return ConnectTrial.parseContentDisposition(connected.getResponseHeaderField("Content-Disposition"));
    }

    private static long findInstanceLength(DownloadConnection.Connected connected) {
        long l = ConnectTrial.parseContentRangeFoInstanceLength(connected.getResponseHeaderField("Content-Range"));
        if (l != -1L) {
            return l;
        }
        if (!ConnectTrial.parseTransferEncoding(connected.getResponseHeaderField("Transfer-Encoding"))) {
            Util.w(TAG, "Transfer-Encoding isn't chunked but there is no valid instance length found either!");
        }
        return -1L;
    }

    private static boolean isAcceptRange(DownloadConnection.Connected connected) throws IOException {
        if (connected.getResponseCode() == 206) {
            return true;
        }
        return "bytes".equals(connected.getResponseHeaderField("Accept-Ranges"));
    }

    private static String parseContentDisposition(String object) {
        if (object == null) {
            return null;
        }
        try {
            Matcher matcher = CONTENT_DISPOSITION_QUOTED_PATTERN.matcher((CharSequence)object);
            if (matcher.find()) {
                return matcher.group(1);
            }
            if (((Matcher)(object = CONTENT_DISPOSITION_NON_QUOTED_PATTERN.matcher((CharSequence)object))).find()) {
                object = ((Matcher)object).group(1);
                return object;
            }
        }
        catch (IllegalStateException illegalStateException) {
            // empty catch block
        }
        return null;
    }

    private static long parseContentRangeFoInstanceLength(String string2) {
        if (string2 == null) {
            return -1L;
        }
        String[] stringArray = string2.split("/");
        if (stringArray.length >= 2) {
            try {
                long l = Long.parseLong(stringArray[1]);
                return l;
            }
            catch (NumberFormatException numberFormatException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("parse instance length failed with ");
                stringBuilder.append(string2);
                Util.w(TAG, stringBuilder.toString());
            }
        }
        return -1L;
    }

    private static boolean parseTransferEncoding(String string2) {
        boolean bl = string2 != null && string2.equals("chunked");
        return bl;
    }

    public void executeTrial() throws IOException {
        OkDownload.with().downloadStrategy().inspectNetworkOnWifi(this.task);
        OkDownload.with().downloadStrategy().inspectNetworkAvailable();
        DownloadConnection downloadConnection = OkDownload.with().connectionFactory().create(this.task.getUrl());
        try {
            if (!Util.isEmpty(this.info.getEtag())) {
                downloadConnection.addHeader("If-Match", this.info.getEtag());
            }
            downloadConnection.addHeader("Range", "bytes=0-0");
            Object object = this.task.getHeaderMapFields();
            if (object != null) {
                Util.addUserRequestHeaderField(object, downloadConnection);
            }
            object = OkDownload.with().callbackDispatcher().dispatch();
            Map<String, List<String>> map = downloadConnection.getRequestProperties();
            object.connectTrialStart(this.task, map);
            DownloadConnection.Connected connected = downloadConnection.execute();
            this.responseCode = connected.getResponseCode();
            this.acceptRange = ConnectTrial.isAcceptRange(connected);
            this.instanceLength = ConnectTrial.findInstanceLength(connected);
            this.responseEtag = ConnectTrial.findEtag(connected);
            this.responseFilename = ConnectTrial.findFilename(connected);
            map = connected.getResponseHeaderFields();
            object.connectTrialEnd(this.task, this.responseCode, map);
            boolean bl = this.isNeedTrialHeadMethodForInstanceLength(this.instanceLength, connected);
            if (bl) {
                this.trialHeadMethodForInstanceLength();
            }
            return;
        }
        finally {
            downloadConnection.release();
        }
    }

    public long getInstanceLength() {
        return this.instanceLength;
    }

    public int getResponseCode() {
        return this.responseCode;
    }

    public String getResponseEtag() {
        return this.responseEtag;
    }

    public String getResponseFilename() {
        return this.responseFilename;
    }

    public boolean isAcceptRange() {
        return this.acceptRange;
    }

    public boolean isChunked() {
        boolean bl = this.instanceLength == -1L;
        return bl;
    }

    public boolean isEtagOverdue() {
        boolean bl = this.info.getEtag() != null && !this.info.getEtag().equals(this.responseEtag);
        return bl;
    }

    boolean isNeedTrialHeadMethodForInstanceLength(long l, DownloadConnection.Connected object) {
        if (l != -1L) {
            return false;
        }
        String string2 = object.getResponseHeaderField("Content-Range");
        if (string2 != null && string2.length() > 0) {
            return false;
        }
        if (ConnectTrial.parseTransferEncoding(object.getResponseHeaderField("Transfer-Encoding"))) {
            return false;
        }
        return (object = object.getResponseHeaderField("Content-Length")) != null && ((String)object).length() > 0;
        {
        }
    }

    void trialHeadMethodForInstanceLength() throws IOException {
        DownloadConnection downloadConnection = OkDownload.with().connectionFactory().create(this.task.getUrl());
        DownloadListener downloadListener = OkDownload.with().callbackDispatcher().dispatch();
        try {
            downloadConnection.setRequestMethod("HEAD");
            Object object = this.task.getHeaderMapFields();
            if (object != null) {
                Util.addUserRequestHeaderField(object, downloadConnection);
            }
            downloadListener.connectTrialStart(this.task, downloadConnection.getRequestProperties());
            object = downloadConnection.execute();
            downloadListener.connectTrialEnd(this.task, object.getResponseCode(), object.getResponseHeaderFields());
            this.instanceLength = Util.parseContentLength(object.getResponseHeaderField("Content-Length"));
            return;
        }
        finally {
            downloadConnection.release();
        }
    }
}

