/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.liulishuo.filedownloader.download;

import android.text.TextUtils;
import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.connection.RedirectHandler;
import com.liulishuo.filedownloader.download.ConnectionProfile;
import com.liulishuo.filedownloader.download.CustomComponentHolder;
import com.liulishuo.filedownloader.model.FileDownloadHeader;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ConnectTask {
    final int downloadId;
    private String etag;
    final FileDownloadHeader header;
    private ConnectionProfile profile;
    private List<String> redirectedUrlList;
    private Map<String, List<String>> requestHeader;
    final String url;

    private ConnectTask(ConnectionProfile connectionProfile, int n, String string2, String string3, FileDownloadHeader fileDownloadHeader) {
        this.downloadId = n;
        this.url = string2;
        this.etag = string3;
        this.header = fileDownloadHeader;
        this.profile = connectionProfile;
    }

    private void addRangeHeader(FileDownloadConnection fileDownloadConnection) throws ProtocolException {
        if (fileDownloadConnection.dispatchAddResumeOffset(this.etag, this.profile.startOffset)) {
            return;
        }
        if (!TextUtils.isEmpty((CharSequence)this.etag)) {
            fileDownloadConnection.addHeader("If-Match", this.etag);
        }
        this.profile.processProfile(fileDownloadConnection);
    }

    private void addUserRequiredHeader(FileDownloadConnection fileDownloadConnection) {
        Object object = this.header;
        if (object != null && (object = ((FileDownloadHeader)object).getHeaders()) != null) {
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.v(this, "%d add outside header: %s", this.downloadId, object);
            }
            for (Map.Entry entry : ((HashMap)object).entrySet()) {
                object = (String)entry.getKey();
                List list = (List)entry.getValue();
                if (list == null) continue;
                Iterator iterator2 = list.iterator();
                while (iterator2.hasNext()) {
                    fileDownloadConnection.addHeader((String)object, (String)iterator2.next());
                }
            }
        }
    }

    private void fixNeededHeader(FileDownloadConnection fileDownloadConnection) {
        FileDownloadHeader fileDownloadHeader = this.header;
        if (fileDownloadHeader == null || fileDownloadHeader.getHeaders().get("User-Agent") == null) {
            fileDownloadConnection.addHeader("User-Agent", FileDownloadUtils.defaultUserAgent());
        }
    }

    FileDownloadConnection connect() throws IOException, IllegalAccessException {
        FileDownloadConnection fileDownloadConnection = CustomComponentHolder.getImpl().createConnection(this.url);
        this.addUserRequiredHeader(fileDownloadConnection);
        this.addRangeHeader(fileDownloadConnection);
        this.fixNeededHeader(fileDownloadConnection);
        this.requestHeader = fileDownloadConnection.getRequestHeaderFields();
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "<---- %s request header %s", this.downloadId, this.requestHeader);
        }
        fileDownloadConnection.execute();
        Object object = new ArrayList<String>();
        this.redirectedUrlList = object;
        object = RedirectHandler.process(this.requestHeader, fileDownloadConnection, object);
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "----> %s response header %s", this.downloadId, object.getResponseHeaderFields());
        }
        return object;
    }

    String getFinalRedirectedUrl() {
        List<String> list = this.redirectedUrlList;
        if (list != null && !list.isEmpty()) {
            list = this.redirectedUrlList;
            return list.get(list.size() - 1);
        }
        return null;
    }

    public ConnectionProfile getProfile() {
        return this.profile;
    }

    public Map<String, List<String>> getRequestHeader() {
        return this.requestHeader;
    }

    boolean isRangeNotFromBeginning() {
        boolean bl = this.profile.currentOffset > 0L;
        return bl;
    }

    public void retryOnConnectedWithNewParam(ConnectionProfile connectionProfile, String string2) throws Reconnect {
        if (connectionProfile == null) {
            throw new IllegalArgumentException();
        }
        this.profile = connectionProfile;
        this.etag = string2;
        throw new Reconnect(this);
    }

    void updateConnectionProfile(long l) {
        if (l == this.profile.currentOffset) {
            FileDownloadLog.w(this, "no data download, no need to update", new Object[0]);
            return;
        }
        long l2 = this.profile.contentLength;
        long l3 = this.profile.currentOffset;
        this.profile = ConnectionProfile.ConnectionProfileBuild.buildConnectionProfile(this.profile.startOffset, l, this.profile.endOffset, l2 - (l - l3));
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.i(this, "after update profile:%s", this.profile);
        }
    }

    static class Builder {
        private ConnectionProfile connectionProfile;
        private Integer downloadId;
        private String etag;
        private FileDownloadHeader header;
        private String url;

        Builder() {
        }

        ConnectTask build() {
            ConnectionProfile connectionProfile;
            Integer n = this.downloadId;
            if (n != null && (connectionProfile = this.connectionProfile) != null && this.url != null) {
                return new ConnectTask(connectionProfile, n, this.url, this.etag, this.header);
            }
            throw new IllegalArgumentException();
        }

        public Builder setConnectionProfile(ConnectionProfile connectionProfile) {
            this.connectionProfile = connectionProfile;
            return this;
        }

        public Builder setDownloadId(int n) {
            this.downloadId = n;
            return this;
        }

        public Builder setEtag(String string2) {
            this.etag = string2;
            return this;
        }

        public Builder setHeader(FileDownloadHeader fileDownloadHeader) {
            this.header = fileDownloadHeader;
            return this;
        }

        public Builder setUrl(String string2) {
            this.url = string2;
            return this;
        }
    }

    class Reconnect
    extends Throwable {
        final ConnectTask this$0;

        Reconnect(ConnectTask connectTask) {
            this.this$0 = connectTask;
        }
    }
}

