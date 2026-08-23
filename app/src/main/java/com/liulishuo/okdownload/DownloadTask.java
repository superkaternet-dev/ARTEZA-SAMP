/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.util.SparseArray
 */
package com.liulishuo.okdownload;

import android.net.Uri;
import android.util.SparseArray;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.IdentifiedTask;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.download.DownloadStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadTask
extends IdentifiedTask
implements Comparable<DownloadTask> {
    private final boolean autoCallbackToUIThread;
    private final Integer connectionCount;
    private final File directoryFile;
    private final boolean filenameFromResponse;
    private final DownloadStrategy.FilenameHolder filenameHolder;
    private final int flushBufferSize;
    private final Map<String, List<String>> headerMapFields;
    private final int id;
    private BreakpointInfo info;
    private final Boolean isPreAllocateLength;
    private volatile SparseArray<Object> keyTagMap;
    private final AtomicLong lastCallbackProcessTimestamp;
    private DownloadListener listener;
    private final int minIntervalMillisCallbackProcess;
    private final boolean passIfAlreadyCompleted;
    private final int priority;
    private final File providedPathFile;
    private final int readBufferSize;
    private final int syncBufferIntervalMills;
    private final int syncBufferSize;
    private Object tag;
    private File targetFile;
    private final Uri uri;
    private final String url;
    private final boolean wifiRequired;

    public DownloadTask(String object, Uri object2, int n, int n2, int n3, int n4, int n5, boolean bl, int n6, Map<String, List<String>> object3, String string2, boolean bl2, boolean bl3, Boolean bl4, Integer n7, Boolean bl5) {
        String string3 = string2;
        this.url = object;
        this.uri = object2;
        this.priority = n;
        this.readBufferSize = n2;
        this.flushBufferSize = n3;
        this.syncBufferSize = n4;
        this.syncBufferIntervalMills = n5;
        this.autoCallbackToUIThread = bl;
        this.minIntervalMillisCallbackProcess = n6;
        this.headerMapFields = object3;
        this.lastCallbackProcessTimestamp = new AtomicLong();
        this.passIfAlreadyCompleted = bl2;
        this.wifiRequired = bl3;
        this.connectionCount = n7;
        this.isPreAllocateLength = bl5;
        if (Util.isUriFileScheme(object2)) {
            object3 = new File(object2.getPath());
            if (bl4 != null) {
                if (bl4.booleanValue()) {
                    if (((File)object3).exists() && ((File)object3).isFile()) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("If you want filename from response please make sure you provide path is directory ");
                        ((StringBuilder)object).append(((File)object3).getPath());
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    object = string3;
                    if (!Util.isEmpty(string2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Discard filename[");
                        ((StringBuilder)object).append(string3);
                        ((StringBuilder)object).append("] because you set filenameFromResponse=true");
                        Util.w("DownloadTask", ((StringBuilder)object).toString());
                        object = null;
                    }
                    this.directoryFile = object3;
                    object2 = bl4;
                } else {
                    if (((File)object3).exists() && ((File)object3).isDirectory() && Util.isEmpty(string2)) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("If you don't want filename from response please make sure you have already provided valid filename or not directory path ");
                        ((StringBuilder)object).append(((File)object3).getPath());
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    if (Util.isEmpty(string2)) {
                        object = ((File)object3).getName();
                        this.directoryFile = Util.getParentFile((File)object3);
                        object2 = bl4;
                    } else {
                        this.directoryFile = object3;
                        object2 = bl4;
                        object = string3;
                    }
                }
            } else if (((File)object3).exists() && ((File)object3).isDirectory()) {
                object2 = true;
                this.directoryFile = object3;
                object = string3;
            } else {
                object2 = false;
                if (((File)object3).exists()) {
                    if (!Util.isEmpty(string2) && !((File)object3).getName().equals(string3)) {
                        throw new IllegalArgumentException("Uri already provided filename!");
                    }
                    object = ((File)object3).getName();
                    this.directoryFile = Util.getParentFile((File)object3);
                } else if (Util.isEmpty(string2)) {
                    object = ((File)object3).getName();
                    this.directoryFile = Util.getParentFile((File)object3);
                } else {
                    this.directoryFile = object3;
                    object = string3;
                }
            }
            this.filenameFromResponse = object2.booleanValue();
        } else {
            this.filenameFromResponse = false;
            this.directoryFile = new File(object2.getPath());
            object = string3;
        }
        if (Util.isEmpty((CharSequence)object)) {
            this.filenameHolder = new DownloadStrategy.FilenameHolder();
            this.providedPathFile = this.directoryFile;
        } else {
            this.filenameHolder = new DownloadStrategy.FilenameHolder((String)object);
            this.targetFile = object = new File(this.directoryFile, (String)object);
            this.providedPathFile = object;
        }
        this.id = OkDownload.with().breakpointStore().findOrCreateId(this);
    }

    public static void cancel(DownloadTask[] downloadTaskArray) {
        OkDownload.with().downloadDispatcher().cancel(downloadTaskArray);
    }

    public static void enqueue(DownloadTask[] downloadTaskArray, DownloadListener downloadListener) {
        int n = downloadTaskArray.length;
        for (int i = 0; i < n; ++i) {
            downloadTaskArray[i].listener = downloadListener;
        }
        OkDownload.with().downloadDispatcher().enqueue(downloadTaskArray);
    }

    public static MockTaskForCompare mockTaskForCompare(int n) {
        return new MockTaskForCompare(n);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public DownloadTask addTag(int n, Object object) {
        synchronized (this) {
            if (this.keyTagMap != null) break block7;
            synchronized (this) {
                if (this.keyTagMap != null) break block7;
            }
            {
                block7: {
                    SparseArray sparseArray;
                    this.keyTagMap = sparseArray = new SparseArray();
                }
                this.keyTagMap.put(n, object);
                return this;
            }
        }
    }

    public void cancel() {
        OkDownload.with().downloadDispatcher().cancel(this);
    }

    @Override
    public int compareTo(DownloadTask downloadTask) {
        return downloadTask.getPriority() - this.getPriority();
    }

    public void enqueue(DownloadListener downloadListener) {
        this.listener = downloadListener;
        OkDownload.with().downloadDispatcher().enqueue(this);
    }

    public boolean equals(Object object) {
        if (super.equals(object)) {
            return true;
        }
        if (object instanceof DownloadTask) {
            object = (DownloadTask)object;
            if (((DownloadTask)object).id == this.id) {
                return true;
            }
            return this.compareIgnoreId((IdentifiedTask)object);
        }
        return false;
    }

    public void execute(DownloadListener downloadListener) {
        this.listener = downloadListener;
        OkDownload.with().downloadDispatcher().execute(this);
    }

    public int getConnectionCount() {
        BreakpointInfo breakpointInfo = this.info;
        if (breakpointInfo == null) {
            return 0;
        }
        return breakpointInfo.getBlockCount();
    }

    public File getFile() {
        String string2 = this.filenameHolder.get();
        if (string2 == null) {
            return null;
        }
        if (this.targetFile == null) {
            this.targetFile = new File(this.directoryFile, string2);
        }
        return this.targetFile;
    }

    @Override
    public String getFilename() {
        return this.filenameHolder.get();
    }

    public DownloadStrategy.FilenameHolder getFilenameHolder() {
        return this.filenameHolder;
    }

    public int getFlushBufferSize() {
        return this.flushBufferSize;
    }

    public Map<String, List<String>> getHeaderMapFields() {
        return this.headerMapFields;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public BreakpointInfo getInfo() {
        if (this.info == null) {
            this.info = OkDownload.with().breakpointStore().get(this.id);
        }
        return this.info;
    }

    long getLastCallbackProcessTs() {
        return this.lastCallbackProcessTimestamp.get();
    }

    public DownloadListener getListener() {
        return this.listener;
    }

    public int getMinIntervalMillisCallbackProcess() {
        return this.minIntervalMillisCallbackProcess;
    }

    @Override
    public File getParentFile() {
        return this.directoryFile;
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    protected File getProvidedPathFile() {
        return this.providedPathFile;
    }

    public int getReadBufferSize() {
        return this.readBufferSize;
    }

    public Integer getSetConnectionCount() {
        return this.connectionCount;
    }

    public Boolean getSetPreAllocateLength() {
        return this.isPreAllocateLength;
    }

    public int getSyncBufferIntervalMills() {
        return this.syncBufferIntervalMills;
    }

    public int getSyncBufferSize() {
        return this.syncBufferSize;
    }

    public Object getTag() {
        return this.tag;
    }

    public Object getTag(int n) {
        Object object = this.keyTagMap == null ? null : this.keyTagMap.get(n);
        return object;
    }

    public Uri getUri() {
        return this.uri;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    public int hashCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.url);
        stringBuilder.append(this.providedPathFile.toString());
        stringBuilder.append(this.filenameHolder.get());
        return stringBuilder.toString().hashCode();
    }

    public boolean isAutoCallbackToUIThread() {
        return this.autoCallbackToUIThread;
    }

    public boolean isFilenameFromResponse() {
        return this.filenameFromResponse;
    }

    public boolean isPassIfAlreadyCompleted() {
        return this.passIfAlreadyCompleted;
    }

    public boolean isWifiRequired() {
        return this.wifiRequired;
    }

    public MockTaskForCompare mock(int n) {
        return new MockTaskForCompare(n, this);
    }

    public void removeTag() {
        synchronized (this) {
            this.tag = null;
            return;
        }
    }

    public void removeTag(int n) {
        synchronized (this) {
            if (this.keyTagMap != null) {
                this.keyTagMap.remove(n);
            }
            return;
        }
    }

    void setBreakpointInfo(BreakpointInfo breakpointInfo) {
        this.info = breakpointInfo;
    }

    void setLastCallbackProcessTs(long l) {
        this.lastCallbackProcessTimestamp.set(l);
    }

    public void setTag(Object object) {
        this.tag = object;
    }

    public void setTags(DownloadTask downloadTask) {
        this.tag = downloadTask.tag;
        this.keyTagMap = downloadTask.keyTagMap;
    }

    public Builder toBuilder() {
        return this.toBuilder(this.url, this.uri);
    }

    public Builder toBuilder(String object, Uri uri) {
        object = new Builder((String)object, uri).setPriority(this.priority).setReadBufferSize(this.readBufferSize).setFlushBufferSize(this.flushBufferSize).setSyncBufferSize(this.syncBufferSize).setSyncBufferIntervalMillis(this.syncBufferIntervalMills).setAutoCallbackToUIThread(this.autoCallbackToUIThread).setMinIntervalMillisCallbackProcess(this.minIntervalMillisCallbackProcess).setHeaderMapFields(this.headerMapFields).setPassIfAlreadyCompleted(this.passIfAlreadyCompleted);
        if (Util.isUriFileScheme(uri) && !new File(uri.getPath()).isFile() && Util.isUriFileScheme(this.uri) && this.filenameHolder.get() != null && !new File(this.uri.getPath()).getName().equals(this.filenameHolder.get())) {
            ((Builder)object).setFilename(this.filenameHolder.get());
        }
        return object;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append("@");
        stringBuilder.append(this.id);
        stringBuilder.append("@");
        stringBuilder.append(this.url);
        stringBuilder.append("@");
        stringBuilder.append(this.directoryFile.toString());
        stringBuilder.append("/");
        stringBuilder.append(this.filenameHolder.get());
        return stringBuilder.toString();
    }

    public static class Builder {
        public static final boolean DEFAULT_AUTO_CALLBACK_TO_UI_THREAD = true;
        public static final int DEFAULT_FLUSH_BUFFER_SIZE = 16384;
        public static final boolean DEFAULT_IS_WIFI_REQUIRED = false;
        public static final int DEFAULT_MIN_INTERVAL_MILLIS_CALLBACK_PROCESS = 3000;
        public static final boolean DEFAULT_PASS_IF_ALREADY_COMPLETED = true;
        public static final int DEFAULT_READ_BUFFER_SIZE = 4096;
        public static final int DEFAULT_SYNC_BUFFER_INTERVAL_MILLIS = 2000;
        public static final int DEFAULT_SYNC_BUFFER_SIZE = 65536;
        private boolean autoCallbackToUIThread = true;
        private Integer connectionCount;
        private String filename;
        private int flushBufferSize = 16384;
        private volatile Map<String, List<String>> headerMapFields;
        private Boolean isFilenameFromResponse;
        private Boolean isPreAllocateLength;
        private boolean isWifiRequired = false;
        private int minIntervalMillisCallbackProcess = 3000;
        private boolean passIfAlreadyCompleted = true;
        private int priority;
        private int readBufferSize = 4096;
        private int syncBufferIntervalMillis = 2000;
        private int syncBufferSize = 65536;
        final Uri uri;
        final String url;

        public Builder(String string2, Uri uri) {
            this.url = string2;
            this.uri = uri;
            if (Util.isUriContentScheme(uri)) {
                this.filename = Util.getFilenameFromContentUri(uri);
            }
        }

        public Builder(String string2, File file) {
            this.url = string2;
            this.uri = Uri.fromFile((File)file);
        }

        public Builder(String string2, String string3, String string4) {
            this(string2, Uri.fromFile((File)new File(string3)));
            if (Util.isEmpty(string4)) {
                this.isFilenameFromResponse = true;
            } else {
                this.filename = string4;
            }
        }

        /*
         * WARNING - void declaration
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void addHeader(String string2, String string3) {
            synchronized (this) {
                void var2_2;
                ArrayList<void> arrayList;
                if (this.headerMapFields == null) {
                    arrayList = new ArrayList<void>();
                    this.headerMapFields = arrayList;
                }
                List<String> list = this.headerMapFields.get(string2);
                arrayList = list;
                if (list == null) {
                    arrayList = new ArrayList<void>();
                    this.headerMapFields.put(string2, arrayList);
                }
                arrayList.add(var2_2);
                return;
            }
        }

        public DownloadTask build() {
            return new DownloadTask(this.url, this.uri, this.priority, this.readBufferSize, this.flushBufferSize, this.syncBufferSize, this.syncBufferIntervalMillis, this.autoCallbackToUIThread, this.minIntervalMillisCallbackProcess, this.headerMapFields, this.filename, this.passIfAlreadyCompleted, this.isWifiRequired, this.isFilenameFromResponse, this.connectionCount, this.isPreAllocateLength);
        }

        public Builder setAutoCallbackToUIThread(boolean bl) {
            this.autoCallbackToUIThread = bl;
            return this;
        }

        public Builder setConnectionCount(int n) {
            this.connectionCount = n;
            return this;
        }

        public Builder setFilename(String string2) {
            this.filename = string2;
            return this;
        }

        public Builder setFilenameFromResponse(Boolean bl) {
            if (Util.isUriFileScheme(this.uri)) {
                this.isFilenameFromResponse = bl;
                return this;
            }
            throw new IllegalArgumentException("Uri isn't file scheme we can't let filename from response");
        }

        public Builder setFlushBufferSize(int n) {
            if (n >= 0) {
                this.flushBufferSize = n;
                return this;
            }
            throw new IllegalArgumentException("Value must be positive!");
        }

        public Builder setHeaderMapFields(Map<String, List<String>> map) {
            this.headerMapFields = map;
            return this;
        }

        public Builder setMinIntervalMillisCallbackProcess(int n) {
            this.minIntervalMillisCallbackProcess = n;
            return this;
        }

        public Builder setPassIfAlreadyCompleted(boolean bl) {
            this.passIfAlreadyCompleted = bl;
            return this;
        }

        public Builder setPreAllocateLength(boolean bl) {
            this.isPreAllocateLength = bl;
            return this;
        }

        public Builder setPriority(int n) {
            this.priority = n;
            return this;
        }

        public Builder setReadBufferSize(int n) {
            if (n >= 0) {
                this.readBufferSize = n;
                return this;
            }
            throw new IllegalArgumentException("Value must be positive!");
        }

        public Builder setSyncBufferIntervalMillis(int n) {
            if (n >= 0) {
                this.syncBufferIntervalMillis = n;
                return this;
            }
            throw new IllegalArgumentException("Value must be positive!");
        }

        public Builder setSyncBufferSize(int n) {
            if (n >= 0) {
                this.syncBufferSize = n;
                return this;
            }
            throw new IllegalArgumentException("Value must be positive!");
        }

        public Builder setWifiRequired(boolean bl) {
            this.isWifiRequired = bl;
            return this;
        }
    }

    public static class MockTaskForCompare
    extends IdentifiedTask {
        final String filename;
        final int id;
        final File parentFile;
        final File providedPathFile;
        final String url;

        public MockTaskForCompare(int n) {
            this.id = n;
            this.url = "";
            this.providedPathFile = EMPTY_FILE;
            this.filename = null;
            this.parentFile = EMPTY_FILE;
        }

        public MockTaskForCompare(int n, DownloadTask downloadTask) {
            this.id = n;
            this.url = downloadTask.url;
            this.parentFile = downloadTask.getParentFile();
            this.providedPathFile = downloadTask.providedPathFile;
            this.filename = downloadTask.getFilename();
        }

        @Override
        public String getFilename() {
            return this.filename;
        }

        @Override
        public int getId() {
            return this.id;
        }

        @Override
        public File getParentFile() {
            return this.parentFile;
        }

        @Override
        protected File getProvidedPathFile() {
            return this.providedPathFile;
        }

        @Override
        public String getUrl() {
            return this.url;
        }
    }

    public static class TaskHideWrapper {
        public static long getLastCallbackProcessTs(DownloadTask downloadTask) {
            return downloadTask.getLastCallbackProcessTs();
        }

        public static void setBreakpointInfo(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
            downloadTask.setBreakpointInfo(breakpointInfo);
        }

        public static void setLastCallbackProcessTs(DownloadTask downloadTask, long l) {
            downloadTask.setLastCallbackProcessTs(l);
        }
    }
}

