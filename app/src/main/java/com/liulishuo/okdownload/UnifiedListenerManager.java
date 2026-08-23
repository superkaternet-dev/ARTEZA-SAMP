/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.okdownload;

import android.util.SparseArray;
import com.liulishuo.okdownload.DownloadListener;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.assist.ListenerAssist;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnifiedListenerManager {
    final List<Integer> autoRemoveListenerIdList = new ArrayList<Integer>();
    final DownloadListener hostListener = new DownloadListener(this){
        final UnifiedListenerManager this$0;
        {
            this.this$0 = unifiedListenerManager;
        }

        @Override
        public void connectEnd(DownloadTask downloadTask, int n, int n2, Map<String, List<String>> map) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.connectEnd(downloadTask, n, n2, map);
            }
        }

        @Override
        public void connectStart(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.connectStart(downloadTask, n, map);
            }
        }

        @Override
        public void connectTrialEnd(DownloadTask downloadTask, int n, Map<String, List<String>> map) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.connectTrialEnd(downloadTask, n, map);
            }
        }

        @Override
        public void connectTrialStart(DownloadTask downloadTask, Map<String, List<String>> map) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.connectTrialStart(downloadTask, map);
            }
        }

        @Override
        public void downloadFromBeginning(DownloadTask downloadTask, BreakpointInfo breakpointInfo, ResumeFailedCause resumeFailedCause) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.downloadFromBeginning(downloadTask, breakpointInfo, resumeFailedCause);
            }
        }

        @Override
        public void downloadFromBreakpoint(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.downloadFromBreakpoint(downloadTask, breakpointInfo);
            }
        }

        @Override
        public void fetchEnd(DownloadTask downloadTask, int n, long l) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.fetchEnd(downloadTask, n, l);
            }
        }

        @Override
        public void fetchProgress(DownloadTask downloadTask, int n, long l) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.fetchProgress(downloadTask, n, l);
            }
        }

        @Override
        public void fetchStart(DownloadTask downloadTask, int n, long l) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.fetchStart(downloadTask, n, l);
            }
        }

        @Override
        public void taskEnd(DownloadTask downloadTask, EndCause endCause, Exception exception) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.taskEnd(downloadTask, endCause, exception);
            }
            if (this.this$0.autoRemoveListenerIdList.contains(downloadTask.getId())) {
                this.this$0.detachListener(downloadTask.getId());
            }
        }

        @Override
        public void taskStart(DownloadTask downloadTask) {
            DownloadListener[] downloadListenerArray = UnifiedListenerManager.getThreadSafeArray(downloadTask, (SparseArray<ArrayList<DownloadListener>>)this.this$0.realListenerMap);
            if (downloadListenerArray == null) {
                return;
            }
            for (DownloadListener downloadListener : downloadListenerArray) {
                if (downloadListener == null) continue;
                downloadListener.taskStart(downloadTask);
            }
        }
    };
    final SparseArray<ArrayList<DownloadListener>> realListenerMap = new SparseArray();

    private static DownloadListener[] getThreadSafeArray(DownloadTask downloadListenerArray, SparseArray<ArrayList<DownloadListener>> object) {
        if ((object = (ArrayList)object.get(downloadListenerArray.getId())) != null && ((ArrayList)object).size() > 0) {
            downloadListenerArray = new DownloadListener[((ArrayList)object).size()];
            ((ArrayList)object).toArray(downloadListenerArray);
            return downloadListenerArray;
        }
        return null;
    }

    public void addAutoRemoveListenersWhenTaskEnd(int n) {
        synchronized (this) {
            block4: {
                boolean bl = this.autoRemoveListenerIdList.contains(n);
                if (!bl) break block4;
                return;
            }
            this.autoRemoveListenerIdList.add(n);
            return;
        }
    }

    public void attachAndEnqueueIfNotRun(DownloadTask downloadTask, DownloadListener downloadListener) {
        synchronized (this) {
            this.attachListener(downloadTask, downloadListener);
            if (!this.isTaskPendingOrRunning(downloadTask)) {
                downloadTask.enqueue(this.hostListener);
            }
            return;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void attachListener(DownloadTask arrayList, DownloadListener downloadListener) {
        synchronized (this) {
            void var2_2;
            ArrayList<void> arrayList2;
            int n = ((DownloadTask)((Object)arrayList)).getId();
            arrayList = arrayList2 = (ArrayList<void>)this.realListenerMap.get(n);
            if (arrayList2 == null) {
                arrayList = new ArrayList<void>();
                this.realListenerMap.put(n, arrayList);
            }
            if (!arrayList.contains(var2_2)) {
                arrayList.add(var2_2);
                if (var2_2 instanceof ListenerAssist) {
                    ((ListenerAssist)var2_2).setAlwaysRecoverAssistModelIfNotSet(true);
                }
            }
            return;
        }
    }

    public void detachListener(int n) {
        synchronized (this) {
            this.realListenerMap.remove(n);
            return;
        }
    }

    public void detachListener(DownloadListener object) {
        synchronized (this) {
            int n;
            int n2 = this.realListenerMap.size();
            ArrayList<Integer> arrayList = new ArrayList<Integer>();
            for (n = 0; n < n2; ++n) {
                List list = (List)this.realListenerMap.valueAt(n);
                if (list == null) continue;
                list.remove(object);
                if (!list.isEmpty()) continue;
                arrayList.add(this.realListenerMap.keyAt(n));
            }
            try {
                object = arrayList.iterator();
                while (object.hasNext()) {
                    n = (Integer)object.next();
                    this.realListenerMap.remove(n);
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public boolean detachListener(DownloadTask object, DownloadListener downloadListener) {
        synchronized (this) {
            int n;
            block5: {
                n = ((DownloadTask)object).getId();
                object = (List)this.realListenerMap.get(n);
                if (object != null) break block5;
                return false;
            }
            boolean bl = object.remove(downloadListener);
            if (object.isEmpty()) {
                this.realListenerMap.remove(n);
            }
            return bl;
        }
    }

    public void enqueueTaskWithUnifiedListener(DownloadTask downloadTask, DownloadListener downloadListener) {
        synchronized (this) {
            this.attachListener(downloadTask, downloadListener);
            downloadTask.enqueue(this.hostListener);
            return;
        }
    }

    public void executeTaskWithUnifiedListener(DownloadTask downloadTask, DownloadListener downloadListener) {
        synchronized (this) {
            this.attachListener(downloadTask, downloadListener);
            downloadTask.execute(this.hostListener);
            return;
        }
    }

    boolean isTaskPendingOrRunning(DownloadTask downloadTask) {
        return StatusUtil.isSameTaskPendingOrRunning(downloadTask);
    }

    public void removeAutoRemoveListenersWhenTaskEnd(int n) {
        synchronized (this) {
            this.autoRemoveListenerIdList.remove((Object)n);
            return;
        }
    }
}

