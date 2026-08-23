/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.exception;

import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileDownloadHttpException
extends IOException {
    private final int mCode;
    private final Map<String, List<String>> mRequestHeaderMap;
    private final Map<String, List<String>> mResponseHeaderMap;

    public FileDownloadHttpException(int n, Map<String, List<String>> map, Map<String, List<String>> map2) {
        super(FileDownloadUtils.formatString("response code error: %d, \n request headers: %s \n response headers: %s", n, map, map2));
        this.mCode = n;
        this.mRequestHeaderMap = FileDownloadHttpException.cloneSerializableMap(map);
        this.mResponseHeaderMap = FileDownloadHttpException.cloneSerializableMap(map);
    }

    private static Map<String, List<String>> cloneSerializableMap(Map<String, List<String>> object2) {
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();
        for (Map.Entry entry : object2.entrySet()) {
            hashMap.put((String)entry.getKey(), new ArrayList((Collection)entry.getValue()));
        }
        return hashMap;
    }

    public int getCode() {
        return this.mCode;
    }

    public Map<String, List<String>> getRequestHeader() {
        return this.mRequestHeaderMap;
    }

    public Map<String, List<String>> getResponseHeader() {
        return this.mResponseHeaderMap;
    }
}

