/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.SystemClock
 */
package com.liulishuo.okdownload;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.liulishuo.okdownload.DownloadContextListener;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.listener.DownloadListener2;
import com.liulishuo.okdownload.core.listener.DownloadListenerBunch;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadContext {
    private static final Executor SERIAL_EXECUTOR = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkDownload Serial", false));
    private static final String TAG = "DownloadContext";
    final DownloadContextListener contextListener;
    private final QueueSet set;
    volatile boolean started = false;
    private final DownloadTask[] tasks;
    private Handler uiHandler;

    DownloadContext(DownloadTask[] downloadTaskArray, DownloadContextListener downloadContextListener, QueueSet queueSet) {
        this.tasks = downloadTaskArray;
        this.contextListener = downloadContextListener;
        this.set = queueSet;
    }

    DownloadContext(DownloadTask[] downloadTaskArray, DownloadContextListener downloadContextListener, QueueSet queueSet, Handler handler) {
        this(downloadTaskArray, downloadContextListener, queueSet);
        this.uiHandler = handler;
    }

    private void callbackQueueEndOnSerialLoop(boolean bl) {
        DownloadContextListener downloadContextListener = this.contextListener;
        if (downloadContextListener == null) {
            return;
        }
        if (bl) {
            if (this.uiHandler == null) {
                this.uiHandler = new Handler(Looper.getMainLooper());
            }
            this.uiHandler.post(new Runnable(this){
                final DownloadContext this$0;
                {
                    this.this$0 = downloadContext;
                }

                @Override
                public void run() {
                    this.this$0.contextListener.queueEnd(this.this$0);
                }
            });
        } else {
            downloadContextListener.queueEnd(this);
        }
    }

    public AlterContext alter() {
        return new AlterContext(this);
    }

    void executeOnSerialExecutor(Runnable runnable) {
        SERIAL_EXECUTOR.execute(runnable);
    }

    public DownloadTask[] getTasks() {
        return this.tasks;
    }

    public boolean isStarted() {
        return this.started;
    }

    public void start(DownloadListener object, boolean bl) {
        long l = SystemClock.uptimeMillis();
        Serializable serializable = new StringBuilder();
        serializable.append("start ");
        serializable.append(bl);
        Util.d(TAG, serializable.toString());
        this.started = true;
        if (this.contextListener != null) {
            object = new DownloadListenerBunch.Builder().append((DownloadListener)object).append(new QueueAttachListener(this, this.contextListener, this.tasks.length)).build();
        }
        if (bl) {
            serializable = new ArrayList();
            Collections.addAll(serializable, this.tasks);
            Collections.sort(serializable);
            this.executeOnSerialExecutor(new Runnable(this, (List)((Object)serializable), (DownloadListener)object){
                final DownloadContext this$0;
                final List val$scheduleTaskList;
                final DownloadListener val$targetListener;
                {
                    this.this$0 = downloadContext;
                    this.val$scheduleTaskList = list;
                    this.val$targetListener = downloadListener;
                }

                @Override
                public void run() {
                    for (DownloadTask downloadTask : this.val$scheduleTaskList) {
                        if (!this.this$0.isStarted()) {
                            this.this$0.callbackQueueEndOnSerialLoop(downloadTask.isAutoCallbackToUIThread());
                            break;
                        }
                        downloadTask.execute(this.val$targetListener);
                    }
                }
            });
        } else {
            DownloadTask.enqueue(this.tasks, (DownloadListener)object);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("start finish ");
        ((StringBuilder)object).append(bl);
        ((StringBuilder)object).append(" ");
        ((StringBuilder)object).append(SystemClock.uptimeMillis() - l);
        ((StringBuilder)object).append("ms");
        Util.d(TAG, ((StringBuilder)object).toString());
    }

    public void startOnParallel(DownloadListener downloadListener) {
        this.start(downloadListener, false);
    }

    public void startOnSerial(DownloadListener downloadListener) {
        this.start(downloadListener, true);
    }

    public void stop() {
        if (this.started) {
            OkDownload.with().downloadDispatcher().cancel(this.tasks);
        }
        this.started = false;
    }

    public Builder toBuilder() {
        return new Builder(this.set, new ArrayList<DownloadTask>(Arrays.asList(this.tasks))).setListener(this.contextListener);
    }

    public static class AlterContext {
        private final DownloadContext context;

        AlterContext(DownloadContext downloadContext) {
            this.context = downloadContext;
        }

        public AlterContext replaceTask(DownloadTask downloadTask, DownloadTask downloadTask2) {
            DownloadTask[] downloadTaskArray = this.context.tasks;
            for (int i = 0; i < downloadTaskArray.length; ++i) {
                if (downloadTaskArray[i] != downloadTask) continue;
                downloadTaskArray[i] = downloadTask2;
            }
            return this;
        }
    }

    public static class Builder {
        final ArrayList<DownloadTask> boundTaskList;
        private DownloadContextListener listener;
        private final QueueSet set;

        public Builder() {
            this(new QueueSet());
        }

        public Builder(QueueSet queueSet) {
            this(queueSet, new ArrayList<DownloadTask>());
        }

        public Builder(QueueSet queueSet, ArrayList<DownloadTask> arrayList) {
            this.set = queueSet;
            this.boundTaskList = arrayList;
        }

        public DownloadTask bind(DownloadTask.Builder object) {
            if (this.set.headerMapFields != null) {
                ((DownloadTask.Builder)object).setHeaderMapFields(this.set.headerMapFields);
            }
            if (this.set.readBufferSize != null) {
                ((DownloadTask.Builder)object).setReadBufferSize(this.set.readBufferSize);
            }
            if (this.set.flushBufferSize != null) {
                ((DownloadTask.Builder)object).setFlushBufferSize(this.set.flushBufferSize);
            }
            if (this.set.syncBufferSize != null) {
                ((DownloadTask.Builder)object).setSyncBufferSize(this.set.syncBufferSize);
            }
            if (this.set.wifiRequired != null) {
                ((DownloadTask.Builder)object).setWifiRequired(this.set.wifiRequired);
            }
            if (this.set.syncBufferIntervalMillis != null) {
                ((DownloadTask.Builder)object).setSyncBufferIntervalMillis(this.set.syncBufferIntervalMillis);
            }
            if (this.set.autoCallbackToUIThread != null) {
                ((DownloadTask.Builder)object).setAutoCallbackToUIThread(this.set.autoCallbackToUIThread);
            }
            if (this.set.minIntervalMillisCallbackProcess != null) {
                ((DownloadTask.Builder)object).setMinIntervalMillisCallbackProcess(this.set.minIntervalMillisCallbackProcess);
            }
            if (this.set.passIfAlreadyCompleted != null) {
                ((DownloadTask.Builder)object).setPassIfAlreadyCompleted(this.set.passIfAlreadyCompleted);
            }
            object = ((DownloadTask.Builder)object).build();
            if (this.set.tag != null) {
                ((DownloadTask)object).setTag(this.set.tag);
            }
            this.boundTaskList.add((DownloadTask)object);
            return object;
        }

        public DownloadTask bind(String string2) {
            if (this.set.uri != null) {
                return this.bind(new DownloadTask.Builder(string2, this.set.uri).setFilenameFromResponse(true));
            }
            throw new IllegalArgumentException("If you want to bind only with url, you have to provide parentPath on QueueSet!");
        }

        public Builder bindSetTask(DownloadTask downloadTask) {
            int n = this.boundTaskList.indexOf(downloadTask);
            if (n >= 0) {
                this.boundTaskList.set(n, downloadTask);
            } else {
                this.boundTaskList.add(downloadTask);
            }
            return this;
        }

        public DownloadContext build() {
            DownloadTask[] downloadTaskArray = new DownloadTask[this.boundTaskList.size()];
            return new DownloadContext(this.boundTaskList.toArray(downloadTaskArray), this.listener, this.set);
        }

        public Builder setListener(DownloadContextListener downloadContextListener) {
            this.listener = downloadContextListener;
            return this;
        }

        public void unbind(int n) {
            for (DownloadTask downloadTask : (List)this.boundTaskList.clone()) {
                if (downloadTask.getId() != n) continue;
                this.boundTaskList.remove(downloadTask);
            }
        }

        public void unbind(DownloadTask downloadTask) {
            this.boundTaskList.remove(downloadTask);
        }
    }

    static class QueueAttachListener
    extends DownloadListener2 {
        private final DownloadContextListener contextListener;
        private final DownloadContext hostContext;
        private final AtomicInteger remainCount;

        QueueAttachListener(DownloadContext downloadContext, DownloadContextListener downloadContextListener, int n) {
            this.remainCount = new AtomicInteger(n);
            this.contextListener = downloadContextListener;
            this.hostContext = downloadContext;
        }

        @Override
        public void taskEnd(DownloadTask comparable, EndCause endCause, Exception exception) {
            int n = this.remainCount.decrementAndGet();
            this.contextListener.taskEnd(this.hostContext, (DownloadTask)comparable, endCause, exception, n);
            if (n <= 0) {
                this.contextListener.queueEnd(this.hostContext);
                comparable = new StringBuilder();
                ((StringBuilder)comparable).append("taskEnd and remainCount ");
                ((StringBuilder)comparable).append(n);
                Util.d(DownloadContext.TAG, ((StringBuilder)comparable).toString());
            }
        }

        @Override
        public void taskStart(DownloadTask downloadTask) {
        }
    }

    public static class QueueSet {
        private Boolean autoCallbackToUIThread;
        private Integer flushBufferSize;
        private Map<String, List<String>> headerMapFields;
        private Integer minIntervalMillisCallbackProcess;
        private Boolean passIfAlreadyCompleted;
        private Integer readBufferSize;
        private Integer syncBufferIntervalMillis;
        private Integer syncBufferSize;
        private Object tag;
        private Uri uri;
        private Boolean wifiRequired;

        public Builder commit() {
            return new Builder(this);
        }

        public Uri getDirUri() {
            return this.uri;
        }

        public int getFlushBufferSize() {
            Integer n = this.flushBufferSize;
            int n2 = n == null ? 16384 : n;
            return n2;
        }

        public Map<String, List<String>> getHeaderMapFields() {
            return this.headerMapFields;
        }

        public int getMinIntervalMillisCallbackProcess() {
            Integer n = this.minIntervalMillisCallbackProcess;
            int n2 = n == null ? 3000 : n;
            return n2;
        }

        public int getReadBufferSize() {
            Integer n = this.readBufferSize;
            int n2 = n == null ? 4096 : n;
            return n2;
        }

        public int getSyncBufferIntervalMillis() {
            Integer n = this.syncBufferIntervalMillis;
            int n2 = n == null ? 2000 : n;
            return n2;
        }

        public int getSyncBufferSize() {
            Integer n = this.syncBufferSize;
            int n2 = n == null ? 65536 : n;
            return n2;
        }

        public Object getTag() {
            return this.tag;
        }

        public boolean isAutoCallbackToUIThread() {
            Boolean bl = this.autoCallbackToUIThread;
            boolean bl2 = bl == null ? true : bl;
            return bl2;
        }

        public boolean isPassIfAlreadyCompleted() {
            Boolean bl = this.passIfAlreadyCompleted;
            boolean bl2 = bl == null ? true : bl;
            return bl2;
        }

        public boolean isWifiRequired() {
            Boolean bl = this.wifiRequired;
            boolean bl2 = bl == null ? false : bl;
            return bl2;
        }

        public QueueSet setAutoCallbackToUIThread(Boolean bl) {
            this.autoCallbackToUIThread = bl;
            return this;
        }

        public QueueSet setFlushBufferSize(int n) {
            this.flushBufferSize = n;
            return this;
        }

        public void setHeaderMapFields(Map<String, List<String>> map) {
            this.headerMapFields = map;
        }

        public QueueSet setMinIntervalMillisCallbackProcess(Integer n) {
            this.minIntervalMillisCallbackProcess = n;
            return this;
        }

        public QueueSet setParentPath(String string2) {
            return this.setParentPathFile(new File(string2));
        }

        public QueueSet setParentPathFile(File file) {
            if (!file.isFile()) {
                this.uri = Uri.fromFile((File)file);
                return this;
            }
            throw new IllegalArgumentException("parent path only accept directory path");
        }

        public QueueSet setParentPathUri(Uri uri) {
            this.uri = uri;
            return this;
        }

        public QueueSet setPassIfAlreadyCompleted(boolean bl) {
            this.passIfAlreadyCompleted = bl;
            return this;
        }

        public QueueSet setReadBufferSize(int n) {
            this.readBufferSize = n;
            return this;
        }

        public QueueSet setSyncBufferIntervalMillis(int n) {
            this.syncBufferIntervalMillis = n;
            return this;
        }

        public QueueSet setSyncBufferSize(int n) {
            this.syncBufferSize = n;
            return this;
        }

        public QueueSet setTag(Object object) {
            this.tag = object;
            return this;
        }

        public QueueSet setWifiRequired(Boolean bl) {
            this.wifiRequired = bl;
            return this;
        }
    }
}

