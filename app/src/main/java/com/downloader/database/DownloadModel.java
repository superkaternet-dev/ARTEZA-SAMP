/*
 * Decompiled with CFR 0.152.
 */
package com.downloader.database;

public class DownloadModel {
    static final String DIR_PATH = "dir_path";
    static final String DOWNLOADED_BYTES = "downloaded_bytes";
    static final String ETAG = "etag";
    static final String FILE_NAME = "file_name";
    static final String ID = "id";
    static final String LAST_MODIFIED_AT = "last_modified_at";
    static final String TOTAL_BYTES = "total_bytes";
    static final String URL = "url";
    private String dirPath;
    private long downloadedBytes;
    private String eTag;
    private String fileName;
    private int id;
    private long lastModifiedAt;
    private long totalBytes;
    private String url;

    public String getDirPath() {
        return this.dirPath;
    }

    public long getDownloadedBytes() {
        return this.downloadedBytes;
    }

    public String getETag() {
        return this.eTag;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getId() {
        return this.id;
    }

    public long getLastModifiedAt() {
        return this.lastModifiedAt;
    }

    public long getTotalBytes() {
        return this.totalBytes;
    }

    public String getUrl() {
        return this.url;
    }

    public void setDirPath(String string2) {
        this.dirPath = string2;
    }

    public void setDownloadedBytes(long l) {
        this.downloadedBytes = l;
    }

    public void setETag(String string2) {
        this.eTag = string2;
    }

    public void setFileName(String string2) {
        this.fileName = string2;
    }

    public void setId(int n) {
        this.id = n;
    }

    public void setLastModifiedAt(long l) {
        this.lastModifiedAt = l;
    }

    public void setTotalBytes(long l) {
        this.totalBytes = l;
    }

    public void setUrl(String string2) {
        this.url = string2;
    }
}

