/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.ConnectivityManager
 */
package com.liulishuo.okdownload.core.download;

import android.net.ConnectivityManager;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointStore;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.connection.DownloadConnection;
import com.liulishuo.okdownload.core.exception.NetworkPolicyException;
import com.liulishuo.okdownload.core.exception.ResumeFailedException;
import com.liulishuo.okdownload.core.exception.ServerCanceledException;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DownloadStrategy {
    private static final long FOUR_CONNECTION_UPPER_LIMIT = 0x6400000L;
    private static final long ONE_CONNECTION_UPPER_LIMIT = 0x100000L;
    private static final String TAG = "DownloadStrategy";
    private static final long THREE_CONNECTION_UPPER_LIMIT = 0x3200000L;
    private static final Pattern TMP_FILE_NAME_PATTERN = Pattern.compile(".*\\\\|/([^\\\\|/|?]*)\\??");
    private static final long TWO_CONNECTION_UPPER_LIMIT = 0x500000L;
    Boolean isHasAccessNetworkStatePermission = null;
    private ConnectivityManager manager = null;

    public int determineBlockCount(DownloadTask downloadTask, long l) {
        if (downloadTask.getSetConnectionCount() != null) {
            return downloadTask.getSetConnectionCount();
        }
        if (l < 0x100000L) {
            return 1;
        }
        if (l < 0x500000L) {
            return 2;
        }
        if (l < 0x3200000L) {
            return 3;
        }
        if (l < 0x6400000L) {
            return 4;
        }
        return 5;
    }

    protected String determineFilename(String string2, DownloadTask object) throws IOException {
        if (Util.isEmpty(string2)) {
            String string3 = ((DownloadTask)object).getUrl();
            object = TMP_FILE_NAME_PATTERN.matcher(string3);
            string2 = null;
            while (((Matcher)object).find()) {
                string2 = ((Matcher)object).group(1);
            }
            object = string2;
            if (Util.isEmpty(string2)) {
                object = Util.md5(string3);
            }
            if (object != null) {
                return object;
            }
            throw new IOException("Can't find valid filename.");
        }
        return string2;
    }

    public ResumeFailedCause getPreconditionFailedCause(int n, boolean bl, BreakpointInfo object, String string2) {
        object = ((BreakpointInfo)object).getEtag();
        if (n == 412) {
            return ResumeFailedCause.RESPONSE_PRECONDITION_FAILED;
        }
        if (!(Util.isEmpty((CharSequence)object) || Util.isEmpty(string2) || string2.equals(object))) {
            return ResumeFailedCause.RESPONSE_ETAG_CHANGED;
        }
        if (n == 201 && bl) {
            return ResumeFailedCause.RESPONSE_CREATED_RANGE_NOT_FROM_0;
        }
        if (n == 205 && bl) {
            return ResumeFailedCause.RESPONSE_RESET_RANGE_NOT_FROM_0;
        }
        return null;
    }

    public boolean inspectAnotherSameInfo(DownloadTask object, BreakpointInfo breakpointInfo, long l) {
        if (!((DownloadTask)object).isFilenameFromResponse()) {
            return false;
        }
        BreakpointStore breakpointStore = OkDownload.with().breakpointStore();
        object = breakpointStore.findAnotherInfoFromCompare((DownloadTask)object, breakpointInfo);
        if (object == null) {
            return false;
        }
        breakpointStore.remove(((BreakpointInfo)object).getId());
        if (((BreakpointInfo)object).getTotalOffset() <= OkDownload.with().downloadStrategy().reuseIdledSameInfoThresholdBytes()) {
            return false;
        }
        if (((BreakpointInfo)object).getEtag() != null && !((BreakpointInfo)object).getEtag().equals(breakpointInfo.getEtag())) {
            return false;
        }
        if (((BreakpointInfo)object).getTotalLength() != l) {
            return false;
        }
        if (((BreakpointInfo)object).getFile() != null && ((BreakpointInfo)object).getFile().exists()) {
            breakpointInfo.reuseBlocks((BreakpointInfo)object);
            object = new StringBuilder();
            ((StringBuilder)object).append("Reuse another same info: ");
            ((StringBuilder)object).append(breakpointInfo);
            Util.d(TAG, ((StringBuilder)object).toString());
            return true;
        }
        return false;
    }

    public void inspectFilenameFromResume(String string2, DownloadTask downloadTask) {
        if (Util.isEmpty(downloadTask.getFilename())) {
            downloadTask.getFilenameHolder().set(string2);
        }
    }

    public void inspectNetworkAvailable() throws UnknownHostException {
        if (this.isHasAccessNetworkStatePermission == null) {
            this.isHasAccessNetworkStatePermission = Util.checkPermission("android.permission.ACCESS_NETWORK_STATE");
        }
        if (!this.isHasAccessNetworkStatePermission.booleanValue()) {
            return;
        }
        if (this.manager == null) {
            this.manager = (ConnectivityManager)OkDownload.with().context().getSystemService("connectivity");
        }
        if (Util.isNetworkAvailable(this.manager)) {
            return;
        }
        throw new UnknownHostException("network is not available!");
    }

    public void inspectNetworkOnWifi(DownloadTask downloadTask) throws IOException {
        if (this.isHasAccessNetworkStatePermission == null) {
            this.isHasAccessNetworkStatePermission = Util.checkPermission("android.permission.ACCESS_NETWORK_STATE");
        }
        if (!downloadTask.isWifiRequired()) {
            return;
        }
        if (this.isHasAccessNetworkStatePermission.booleanValue()) {
            if (this.manager == null) {
                this.manager = (ConnectivityManager)OkDownload.with().context().getSystemService("connectivity");
            }
            if (!Util.isNetworkNotOnWifiType(this.manager)) {
                return;
            }
            throw new NetworkPolicyException();
        }
        throw new IOException("required for access network state but don't have the permission of Manifest.permission.ACCESS_NETWORK_STATE, please declare this permission first on your AndroidManifest, so we can handle the case of downloading required wifi state.");
    }

    public boolean isServerCanceled(int n, boolean bl) {
        if (n != 206 && n != 200) {
            return true;
        }
        return n == 200 && bl;
    }

    public boolean isUseMultiBlock(boolean bl) {
        if (!OkDownload.with().outputStreamFactory().supportSeek()) {
            return false;
        }
        return bl;
    }

    public ResumeAvailableResponseCheck resumeAvailableResponseCheck(DownloadConnection.Connected connected, int n, BreakpointInfo breakpointInfo) {
        return new ResumeAvailableResponseCheck(connected, n, breakpointInfo);
    }

    public long reuseIdledSameInfoThresholdBytes() {
        return 10240L;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void validFilenameFromResponse(String string2, DownloadTask downloadTask, BreakpointInfo breakpointInfo) throws IOException {
        if (!Util.isEmpty(downloadTask.getFilename())) return;
        string2 = this.determineFilename(string2, downloadTask);
        if (!Util.isEmpty(downloadTask.getFilename())) return;
        synchronized (downloadTask) {
            if (!Util.isEmpty(downloadTask.getFilename())) return;
            downloadTask.getFilenameHolder().set(string2);
            breakpointInfo.getFilenameHolder().set(string2);
            return;
        }
    }

    public boolean validFilenameFromStore(DownloadTask downloadTask) {
        String string2 = OkDownload.with().breakpointStore().getResponseFilename(downloadTask.getUrl());
        if (string2 == null) {
            return false;
        }
        downloadTask.getFilenameHolder().set(string2);
        return true;
    }

    public void validInfoOnCompleted(DownloadTask downloadTask, DownloadStore object) {
        Object object2;
        object = object2 = object.getAfterCompleted(downloadTask.getId());
        if (object2 == null) {
            long l;
            object = new BreakpointInfo(downloadTask.getId(), downloadTask.getUrl(), downloadTask.getParentFile(), downloadTask.getFilename());
            if (Util.isUriContentScheme(downloadTask.getUri())) {
                l = Util.getSizeFromContentUri(downloadTask.getUri());
            } else {
                object2 = downloadTask.getFile();
                if (object2 == null) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("file is not ready on valid info for task on complete state ");
                    ((StringBuilder)object2).append(downloadTask);
                    Util.w(TAG, ((StringBuilder)object2).toString());
                    l = 0L;
                } else {
                    l = ((File)object2).length();
                }
            }
            ((BreakpointInfo)object).addBlock(new BlockInfo(0L, l, l));
        }
        DownloadTask.TaskHideWrapper.setBreakpointInfo(downloadTask, (BreakpointInfo)object);
    }

    public static class FilenameHolder {
        private volatile String filename;
        private final boolean filenameProvidedByConstruct;

        public FilenameHolder() {
            this.filenameProvidedByConstruct = false;
        }

        public FilenameHolder(String string2) {
            this.filename = string2;
            this.filenameProvidedByConstruct = true;
        }

        public boolean equals(Object object) {
            boolean bl = super.equals(object);
            boolean bl2 = true;
            if (bl) {
                return true;
            }
            if (object instanceof FilenameHolder) {
                if (this.filename == null) {
                    if (((FilenameHolder)object).filename != null) {
                        bl2 = false;
                    }
                    return bl2;
                }
                return this.filename.equals(((FilenameHolder)object).filename);
            }
            return false;
        }

        public String get() {
            return this.filename;
        }

        public int hashCode() {
            int n = this.filename == null ? 0 : this.filename.hashCode();
            return n;
        }

        public boolean isFilenameProvidedByConstruct() {
            return this.filenameProvidedByConstruct;
        }

        void set(String string2) {
            this.filename = string2;
        }
    }

    public static class ResumeAvailableResponseCheck {
        private int blockIndex;
        private DownloadConnection.Connected connected;
        private BreakpointInfo info;

        protected ResumeAvailableResponseCheck(DownloadConnection.Connected connected, int n, BreakpointInfo breakpointInfo) {
            this.connected = connected;
            this.info = breakpointInfo;
            this.blockIndex = n;
        }

        public void inspect() throws IOException {
            BlockInfo blockInfo = this.info.getBlock(this.blockIndex);
            int n = this.connected.getResponseCode();
            Object object = this.connected.getResponseHeaderField("Etag");
            DownloadStrategy downloadStrategy = OkDownload.with().downloadStrategy();
            long l = blockInfo.getCurrentOffset();
            boolean bl = true;
            boolean bl2 = l != 0L;
            object = downloadStrategy.getPreconditionFailedCause(n, bl2, this.info, (String)object);
            if (object == null) {
                object = OkDownload.with().downloadStrategy();
                if (!((DownloadStrategy)object).isServerCanceled(n, bl2 = blockInfo.getCurrentOffset() != 0L ? bl : false)) {
                    return;
                }
                throw new ServerCanceledException(n, blockInfo.getCurrentOffset());
            }
            throw new ResumeFailedException((ResumeFailedCause)((Object)object));
        }
    }
}

