/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.request;

import com.downloader.Priority;
import com.downloader.request.DownloadRequest;
import com.downloader.request.RequestBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DownloadRequestBuilder
implements RequestBuilder {
    int connectTimeout;
    String dirPath;
    String fileName;
    HashMap<String, List<String>> headerMap;
    Priority priority = Priority.MEDIUM;
    int readTimeout;
    Object tag;
    String url;
    String userAgent;

    public DownloadRequestBuilder(String string2, String string3, String string4) {
        this.url = string2;
        this.dirPath = string3;
        this.fileName = string4;
    }

    public DownloadRequest build() {
        return new DownloadRequest(this);
    }

    @Override
    public DownloadRequestBuilder setConnectTimeout(int n) {
        this.connectTimeout = n;
        return this;
    }

    @Override
    public DownloadRequestBuilder setHeader(String string2, String string3) {
        List<String> list;
        if (this.headerMap == null) {
            this.headerMap = new HashMap();
        }
        List<String> list2 = list = this.headerMap.get(string2);
        if (list == null) {
            list2 = new ArrayList<String>();
            this.headerMap.put(string2, list2);
        }
        if (!list2.contains(string3)) {
            list2.add(string3);
        }
        return this;
    }

    @Override
    public DownloadRequestBuilder setPriority(Priority priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public DownloadRequestBuilder setReadTimeout(int n) {
        this.readTimeout = n;
        return this;
    }

    @Override
    public DownloadRequestBuilder setTag(Object object) {
        this.tag = object;
        return this;
    }

    @Override
    public DownloadRequestBuilder setUserAgent(String string2) {
        this.userAgent = string2;
        return this;
    }
}

