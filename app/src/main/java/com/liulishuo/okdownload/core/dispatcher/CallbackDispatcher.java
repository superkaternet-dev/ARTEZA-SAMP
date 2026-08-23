/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.SystemClock
 */
package com.liulishuo.okdownload.core.dispatcher;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadMonitor;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CallbackDispatcher {
    private static final String TAG = "CallbackDispatcher";
    private final DownloadListener transmit;
    private final Handler uiHandler;

    public CallbackDispatcher() {
        Handler handler;
        this.uiHandler = handler = new Handler(Looper.getMainLooper());
        this.transmit = new DefaultTransmitListener(handler);
    }

    CallbackDispatcher(Handler handler, DownloadListener downloadListener) {
        this.uiHandler = handler;
        this.transmit = downloadListener;
    }

    public DownloadListener dispatch() {
        return this.transmit;
    }

    public void endTasks(Collection<DownloadTask> collection, Collection<DownloadTask> collection2, Collection<DownloadTask> collection3) {
        Object object;
        if (collection.size() == 0 && collection2.size() == 0 && collection3.size() == 0) {
            return;
        }
        Object object2 = new StringBuilder();
        ((StringBuilder)object2).append("endTasks completed[");
        ((StringBuilder)object2).append(collection.size());
        ((StringBuilder)object2).append("] sameTask[");
        ((StringBuilder)object2).append(collection2.size());
        ((StringBuilder)object2).append("] fileBusy[");
        ((StringBuilder)object2).append(collection3.size());
        ((StringBuilder)object2).append("]");
        Util.d(TAG, ((StringBuilder)object2).toString());
        if (collection.size() > 0) {
            object = collection.iterator();
            while (object.hasNext()) {
                object2 = object.next();
                if (((DownloadTask)object2).isAutoCallbackToUIThread()) continue;
                ((DownloadTask)object2).getListener().taskEnd((DownloadTask)object2, EndCause.COMPLETED, null);
                object.remove();
            }
        }
        if (collection2.size() > 0) {
            object = collection2.iterator();
            while (object.hasNext()) {
                object2 = object.next();
                if (((DownloadTask)object2).isAutoCallbackToUIThread()) continue;
                ((DownloadTask)object2).getListener().taskEnd((DownloadTask)object2, EndCause.SAME_TASK_BUSY, null);
                object.remove();
            }
        }
        if (collection3.size() > 0) {
            object2 = collection3.iterator();
            while (object2.hasNext()) {
                object = (DownloadTask)object2.next();
                if (((DownloadTask)object).isAutoCallbackToUIThread()) continue;
                ((DownloadTask)object).getListener().taskEnd((DownloadTask)object, EndCause.FILE_BUSY, null);
                object2.remove();
            }
        }
        if (collection.size() == 0 && collection2.size() == 0 && collection3.size() == 0) {
            return;
        }
        this.uiHandler.post(new Runnable(this, collection, collection2, collection3){
            final CallbackDispatcher this$0;
            final Collection val$completedTaskCollection;
            final Collection val$fileBusyCollection;
            final Collection val$sameTaskConflictCollection;
            {
                this.this$0 = callbackDispatcher;
                this.val$completedTaskCollection = collection;
                this.val$sameTaskConflictCollection = collection2;
                this.val$fileBusyCollection = collection3;
            }

            @Override
            public void run() {
                for (Object object : this.val$completedTaskCollection) {
                    ((DownloadTask)object).getListener().taskEnd((DownloadTask)object, EndCause.COMPLETED, null);
                }
                for (Object object : this.val$sameTaskConflictCollection) {
                    ((DownloadTask)object).getListener().taskEnd((DownloadTask)object, EndCause.SAME_TASK_BUSY, null);
                }
                for (Object object : this.val$fileBusyCollection) {
                    ((DownloadTask)object).getListener().taskEnd((DownloadTask)object, EndCause.FILE_BUSY, null);
                }
            }
        });
    }

    public void endTasksWithCanceled(Collection<DownloadTask> collection) {
        if (collection.size() <= 0) {
            return;
        }
        Comparable<StringBuilder> comparable = new StringBuilder();
        ((StringBuilder)comparable).append("endTasksWithCanceled canceled[");
        ((StringBuilder)comparable).append(collection.size());
        ((StringBuilder)comparable).append("]");
        Util.d(TAG, ((StringBuilder)comparable).toString());
        Iterator<DownloadTask> iterator2 = collection.iterator();
        while (iterator2.hasNext()) {
            comparable = iterator2.next();
            if (((DownloadTask)comparable).isAutoCallbackToUIThread()) continue;
            ((DownloadTask)comparable).getListener().taskEnd((DownloadTask)comparable, EndCause.CANCELED, null);
            iterator2.remove();
        }
        this.uiHandler.post(new Runnable(this, collection){
            final CallbackDispatcher this$0;
            final Collection val$canceledCollection;
            {
                this.this$0 = callbackDispatcher;
                this.val$canceledCollection = collection;
            }

            @Override
            public void run() {
                for (DownloadTask downloadTask : this.val$canceledCollection) {
                    downloadTask.getListener().taskEnd(downloadTask, EndCause.CANCELED, null);
                }
            }
        });
    }

    public void endTasksWithError(Collection<DownloadTask> collection, Exception exception) {
        if (collection.size() <= 0) {
            return;
        }
        Object object = new StringBuilder();
        ((StringBuilder)object).append("endTasksWithError error[");
        ((StringBuilder)object).append(collection.size());
        ((StringBuilder)object).append("] realCause: ");
        ((StringBuilder)object).append(exception);
        Util.d(TAG, ((StringBuilder)object).toString());
        object = collection.iterator();
        while (object.hasNext()) {
            DownloadTask downloadTask = (DownloadTask)object.next();
            if (downloadTask.isAutoCallbackToUIThread()) continue;
            downloadTask.getListener().taskEnd(downloadTask, EndCause.ERROR, exception);
            object.remove();
        }
        this.uiHandler.post(new Runnable(this, collection, exception){
            final CallbackDispatcher this$0;
            final Collection val$errorCollection;
            final Exception val$realCause;
            {
                this.this$0 = callbackDispatcher;
                this.val$errorCollection = collection;
                this.val$realCause = exception;
            }

            @Override
            public void run() {
                for (DownloadTask downloadTask : this.val$errorCollection) {
                    downloadTask.getListener().taskEnd(downloadTask, EndCause.ERROR, this.val$realCause);
                }
            }
        });
    }

    public boolean isFetchProcessMoment(DownloadTask downloadTask) {
        long l = downloadTask.getMinIntervalMillisCallbackProcess();
        long l2 = SystemClock.uptimeMillis();
        boolean bl = l <= 0L || l2 - DownloadTask.TaskHideWrapper.getLastCallbackProcessTs(downloadTask) >= l;
        return bl;
    }

    static class DefaultTransmitListener
    implements DownloadListener {
        private final Handler uiHandler;

        DefaultTransmitListener(Handler handler) {
            this.uiHandler = handler;
        }

        @Override
        public void connectEnd(DownloadTask downloadTask, int n, int n2, Map<String, List<String>> map) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<----- finish connection task(");
            stringBuilder.append(downloadTask.getId());
            stringBuilder.append(") block(");
            stringBuilder.append(n);
            stringBuilder.append(") code[");
            stringBuilder.append(n2);
            stringBuilder.append("]");
            stringBuilder.append(map);
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, n, n2, map){
                    final DefaultTransmitListener this$0;
                    final int val$blockIndex;
                    final Map val$requestHeaderFields;
                    final int val$responseCode;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$blockIndex = n;
                        this.val$responseCode = n2;
                        this.val$requestHeaderFields = map;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().connectEnd(this.val$task, this.val$blockIndex, this.val$responseCode, this.val$requestHeaderFields);
                    }
                });
            } else {
                downloadTask.getListener().connectEnd(downloadTask, n, n2, map);
            }
        }

        @Override
        public void connectStart(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("-----> start connection task(");
            stringBuilder.append(downloadTask.getId());
            stringBuilder.append(") block(");
            stringBuilder.append(n);
            stringBuilder.append(") ");
            stringBuilder.append(map);
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, n, map){
                    final DefaultTransmitListener this$0;
                    final int val$blockIndex;
                    final Map val$requestHeaderFields;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$blockIndex = n;
                        this.val$requestHeaderFields = map;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().connectStart(this.val$task, this.val$blockIndex, this.val$requestHeaderFields);
                    }
                });
            } else {
                downloadTask.getListener().connectStart(downloadTask, n, map);
            }
        }

        @Override
        public void connectTrialEnd(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<----- finish trial task(");
            stringBuilder.append(downloadTask.getId());
            stringBuilder.append(") code[");
            stringBuilder.append(n);
            stringBuilder.append("]");
            stringBuilder.append(map);
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, n, map){
                    final DefaultTransmitListener this$0;
                    final Map val$headerFields;
                    final int val$responseCode;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$responseCode = n;
                        this.val$headerFields = map;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().connectTrialEnd(this.val$task, this.val$responseCode, this.val$headerFields);
                    }
                });
            } else {
                downloadTask.getListener().connectTrialEnd(downloadTask, n, map);
            }
        }

        @Override
        public void connectTrialStart(DownloadTask downloadTask, Map<String, List<String>> map) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("-----> start trial task(");
            stringBuilder.append(downloadTask.getId());
            stringBuilder.append(") ");
            stringBuilder.append(map);
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, map){
                    final DefaultTransmitListener this$0;
                    final Map val$headerFields;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$headerFields = map;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().connectTrialStart(this.val$task, this.val$headerFields);
                    }
                });
            } else {
                downloadTask.getListener().connectTrialStart(downloadTask, map);
            }
        }

        @Override
        public void downloadFromBeginning(DownloadTask downloadTask, BreakpointInfo breakpointInfo, ResumeFailedCause resumeFailedCause) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("downloadFromBeginning: ");
            stringBuilder.append(downloadTask.getId());
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            this.inspectDownloadFromBeginning(downloadTask, breakpointInfo, resumeFailedCause);
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, breakpointInfo, resumeFailedCause){
                    final DefaultTransmitListener this$0;
                    final ResumeFailedCause val$cause;
                    final BreakpointInfo val$info;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$info = breakpointInfo;
                        this.val$cause = resumeFailedCause;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().downloadFromBeginning(this.val$task, this.val$info, this.val$cause);
                    }
                });
            } else {
                downloadTask.getListener().downloadFromBeginning(downloadTask, breakpointInfo, resumeFailedCause);
            }
        }

        @Override
        public void downloadFromBreakpoint(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("downloadFromBreakpoint: ");
            stringBuilder.append(downloadTask.getId());
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            this.inspectDownloadFromBreakpoint(downloadTask, breakpointInfo);
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, breakpointInfo){
                    final DefaultTransmitListener this$0;
                    final BreakpointInfo val$info;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$info = breakpointInfo;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().downloadFromBreakpoint(this.val$task, this.val$info);
                    }
                });
            } else {
                downloadTask.getListener().downloadFromBreakpoint(downloadTask, breakpointInfo);
            }
        }

        @Override
        public void fetchEnd(DownloadTask downloadTask, int n, long l) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fetchEnd: ");
            stringBuilder.append(downloadTask.getId());
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, n, l){
                    final DefaultTransmitListener this$0;
                    final int val$blockIndex;
                    final long val$contentLength;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$blockIndex = n;
                        this.val$contentLength = l;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().fetchEnd(this.val$task, this.val$blockIndex, this.val$contentLength);
                    }
                });
            } else {
                downloadTask.getListener().fetchEnd(downloadTask, n, l);
            }
        }

        @Override
        public void fetchProgress(DownloadTask downloadTask, int n, long l) {
            if (downloadTask.getMinIntervalMillisCallbackProcess() > 0) {
                DownloadTask.TaskHideWrapper.setLastCallbackProcessTs(downloadTask, SystemClock.uptimeMillis());
            }
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, n, l){
                    final DefaultTransmitListener this$0;
                    final int val$blockIndex;
                    final long val$increaseBytes;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$blockIndex = n;
                        this.val$increaseBytes = l;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().fetchProgress(this.val$task, this.val$blockIndex, this.val$increaseBytes);
                    }
                });
            } else {
                downloadTask.getListener().fetchProgress(downloadTask, n, l);
            }
        }

        @Override
        public void fetchStart(DownloadTask downloadTask, int n, long l) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("fetchStart: ");
            stringBuilder.append(downloadTask.getId());
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, n, l){
                    final DefaultTransmitListener this$0;
                    final int val$blockIndex;
                    final long val$contentLength;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$blockIndex = n;
                        this.val$contentLength = l;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().fetchStart(this.val$task, this.val$blockIndex, this.val$contentLength);
                    }
                });
            } else {
                downloadTask.getListener().fetchStart(downloadTask, n, l);
            }
        }

        void inspectDownloadFromBeginning(DownloadTask downloadTask, BreakpointInfo breakpointInfo, ResumeFailedCause resumeFailedCause) {
            DownloadMonitor downloadMonitor = OkDownload.with().getMonitor();
            if (downloadMonitor != null) {
                downloadMonitor.taskDownloadFromBeginning(downloadTask, breakpointInfo, resumeFailedCause);
            }
        }

        void inspectDownloadFromBreakpoint(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
            DownloadMonitor downloadMonitor = OkDownload.with().getMonitor();
            if (downloadMonitor != null) {
                downloadMonitor.taskDownloadFromBreakpoint(downloadTask, breakpointInfo);
            }
        }

        void inspectTaskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
            DownloadMonitor downloadMonitor = OkDownload.with().getMonitor();
            if (downloadMonitor != null) {
                downloadMonitor.taskEnd(downloadTask, endCause, exception);
            }
        }

        void inspectTaskStart(DownloadTask downloadTask) {
            DownloadMonitor downloadMonitor = OkDownload.with().getMonitor();
            if (downloadMonitor != null) {
                downloadMonitor.taskStart(downloadTask);
            }
        }

        @Override
        public void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
            if (endCause == EndCause.ERROR) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("taskEnd: ");
                stringBuilder.append(downloadTask.getId());
                stringBuilder.append(" ");
                stringBuilder.append((Object)endCause);
                stringBuilder.append(" ");
                stringBuilder.append(exception);
                Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            }
            this.inspectTaskEnd(downloadTask, endCause, exception);
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask, endCause, exception){
                    final DefaultTransmitListener this$0;
                    final EndCause val$cause;
                    final Exception val$realCause;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                        this.val$cause = endCause;
                        this.val$realCause = exception;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().taskEnd(this.val$task, this.val$cause, this.val$realCause);
                    }
                });
            } else {
                downloadTask.getListener().taskEnd(downloadTask, endCause, exception);
            }
        }

        @Override
        public void taskStart(DownloadTask downloadTask) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("taskStart: ");
            stringBuilder.append(downloadTask.getId());
            Util.d(CallbackDispatcher.TAG, stringBuilder.toString());
            this.inspectTaskStart(downloadTask);
            if (downloadTask.isAutoCallbackToUIThread()) {
                this.uiHandler.post(new Runnable(this, downloadTask){
                    final DefaultTransmitListener this$0;
                    final DownloadTask val$task;
                    {
                        this.this$0 = defaultTransmitListener;
                        this.val$task = downloadTask;
                    }

                    @Override
                    public void run() {
                        this.val$task.getListener().taskStart(this.val$task);
                    }
                });
            } else {
                downloadTask.getListener().taskStart(downloadTask);
            }
        }
    }
}

