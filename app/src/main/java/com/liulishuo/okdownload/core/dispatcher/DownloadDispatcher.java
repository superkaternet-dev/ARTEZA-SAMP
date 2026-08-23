/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.SystemClock
 */
package com.liulishuo.okdownload.core.dispatcher;

import android.os.SystemClock;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.StatusUtil;
import com.liulishuo.okdownload.core.IdentifiedTask;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.dispatcher.CallbackDispatcher;
import com.liulishuo.okdownload.core.download.DownloadCall;
import java.io.File;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadDispatcher {
    private static final String TAG = "DownloadDispatcher";
    private volatile ExecutorService executorService;
    private final AtomicInteger flyingCanceledAsyncCallCount = new AtomicInteger();
    int maxParallelRunningCount = 5;
    private final List<DownloadCall> readyAsyncCalls;
    private final List<DownloadCall> runningAsyncCalls;
    private final List<DownloadCall> runningSyncCalls;
    private final AtomicInteger skipProceedCallCount = new AtomicInteger();
    private DownloadStore store;

    public DownloadDispatcher() {
        this(new ArrayList<DownloadCall>(), new ArrayList<DownloadCall>(), new ArrayList<DownloadCall>());
    }

    DownloadDispatcher(List<DownloadCall> list, List<DownloadCall> list2, List<DownloadCall> list3) {
        this.readyAsyncCalls = list;
        this.runningAsyncCalls = list2;
        this.runningSyncCalls = list3;
    }

    /*
     * Loose catch block
     * WARNING - void declaration
     */
    private void cancelLocked(IdentifiedTask[] identifiedTaskArray) {
        synchronized (this) {
            void var6_7;
            ArrayList<DownloadCall> arrayList;
            Serializable serializable;
            long l;
            block10: {
                l = SystemClock.uptimeMillis();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("start cancel bunch task manually: ");
                stringBuilder.append(identifiedTaskArray.length);
                Util.d(TAG, stringBuilder.toString());
                serializable = new ArrayList();
                arrayList = new ArrayList<DownloadCall>();
                int n = identifiedTaskArray.length;
                for (int i = 0; i < n; ++i) {
                    try {
                        this.filterCanceledCalls(identifiedTaskArray[i], (List<DownloadCall>)((Object)serializable), arrayList);
                        continue;
                    }
                    catch (Throwable throwable) {
                        break block10;
                    }
                }
                try {
                    this.handleCanceledCalls((List<DownloadCall>)((Object)serializable), (List<DownloadCall>)arrayList);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("finish cancel bunch task manually: ");
                    stringBuilder.append(identifiedTaskArray.length);
                    stringBuilder.append(" consume ");
                    stringBuilder.append(SystemClock.uptimeMillis() - l);
                    stringBuilder.append("ms");
                    Util.d(TAG, stringBuilder.toString());
                    return;
                }
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
                catch (Throwable throwable) {
                    // empty catch block
                }
            }
            this.handleCanceledCalls((List<DownloadCall>)((Object)serializable), (List<DownloadCall>)arrayList);
            serializable = new StringBuilder();
            ((StringBuilder)serializable).append("finish cancel bunch task manually: ");
            ((StringBuilder)serializable).append(identifiedTaskArray.length);
            ((StringBuilder)serializable).append(" consume ");
            ((StringBuilder)serializable).append(SystemClock.uptimeMillis() - l);
            ((StringBuilder)serializable).append("ms");
            Util.d(TAG, ((StringBuilder)serializable).toString());
            throw var6_7;
        }
    }

    private void enqueueIgnorePriority(DownloadTask comparable) {
        synchronized (this) {
            comparable = DownloadCall.create(comparable, true, this.store);
            if (this.runningAsyncSize() < this.maxParallelRunningCount) {
                this.runningAsyncCalls.add((DownloadCall)comparable);
                this.getExecutorService().execute((Runnable)((Object)comparable));
            } else {
                this.readyAsyncCalls.add((DownloadCall)comparable);
            }
            return;
        }
    }

    private void enqueueLocked(DownloadTask downloadTask) {
        synchronized (this) {
            block7: {
                boolean bl;
                block6: {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("enqueueLocked for single task: ");
                    stringBuilder.append(downloadTask);
                    Util.d(TAG, stringBuilder.toString());
                    bl = this.inspectCompleted(downloadTask);
                    if (!bl) break block6;
                    return;
                }
                bl = this.inspectForConflict(downloadTask);
                if (!bl) break block7;
                return;
            }
            int n = this.readyAsyncCalls.size();
            this.enqueueIgnorePriority(downloadTask);
            if (n != this.readyAsyncCalls.size()) {
                Collections.sort(this.readyAsyncCalls);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void enqueueLocked(DownloadTask[] downloadTaskArray) {
        synchronized (this) {
            long l = SystemClock.uptimeMillis();
            Serializable serializable = new StringBuilder();
            ((StringBuilder)serializable).append("start enqueueLocked for bunch task: ");
            ((StringBuilder)serializable).append(downloadTaskArray.length);
            Util.d(TAG, ((StringBuilder)serializable).toString());
            serializable = new ArrayList();
            Collections.addAll(serializable, downloadTaskArray);
            if (serializable.size() > 1) {
                Collections.sort(serializable);
            }
            int n = this.readyAsyncCalls.size();
            try {
                OkDownload.with().downloadStrategy().inspectNetworkAvailable();
                ArrayList<DownloadTask> arrayList = new ArrayList<DownloadTask>();
                ArrayList<DownloadTask> arrayList2 = new ArrayList<DownloadTask>();
                ArrayList<DownloadTask> arrayList3 = new ArrayList<DownloadTask>();
                Iterator iterator2 = serializable.iterator();
                while (iterator2.hasNext()) {
                    DownloadTask downloadTask = (DownloadTask)iterator2.next();
                    if (this.inspectCompleted(downloadTask, arrayList) || this.inspectForConflict(downloadTask, arrayList2, arrayList3)) continue;
                    this.enqueueIgnorePriority(downloadTask);
                }
                OkDownload.with().callbackDispatcher().endTasks(arrayList, arrayList2, arrayList3);
            }
            catch (UnknownHostException unknownHostException) {
                ArrayList<DownloadTask> arrayList = new ArrayList<DownloadTask>((Collection<DownloadTask>)((Object)serializable));
                OkDownload.with().callbackDispatcher().endTasksWithError(arrayList, unknownHostException);
            }
            if (n != this.readyAsyncCalls.size()) {
                Collections.sort(this.readyAsyncCalls);
            }
            serializable = new StringBuilder();
            ((StringBuilder)serializable).append("end enqueueLocked for bunch task: ");
            ((StringBuilder)serializable).append(downloadTaskArray.length);
            ((StringBuilder)serializable).append(" consume ");
            ((StringBuilder)serializable).append(SystemClock.uptimeMillis() - l);
            ((StringBuilder)serializable).append("ms");
            Util.d(TAG, ((StringBuilder)serializable).toString());
            return;
        }
    }

    private void filterCanceledCalls(IdentifiedTask identifiedTask, List<DownloadCall> list, List<DownloadCall> list2) {
        synchronized (this) {
            try {
                Iterator<DownloadCall> iterator2 = this.readyAsyncCalls.iterator();
                while (iterator2.hasNext()) {
                    DownloadCall downloadCall = iterator2.next();
                    if (downloadCall.task != identifiedTask && downloadCall.task.getId() != identifiedTask.getId()) continue;
                    if (!downloadCall.isCanceled() && !downloadCall.isFinishing()) {
                        iterator2.remove();
                        list.add(downloadCall);
                        return;
                    }
                    return;
                }
                for (DownloadCall downloadCall : this.runningAsyncCalls) {
                    if (downloadCall.task != identifiedTask && downloadCall.task.getId() != identifiedTask.getId()) continue;
                    list.add(downloadCall);
                    list2.add(downloadCall);
                    return;
                }
                for (DownloadCall downloadCall : this.runningSyncCalls) {
                    if (downloadCall.task != identifiedTask && downloadCall.task.getId() != identifiedTask.getId()) continue;
                    list.add(downloadCall);
                    list2.add(downloadCall);
                    return;
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    private void handleCanceledCalls(List<DownloadCall> iterator2, List<DownloadCall> arrayList) {
        synchronized (this) {
            try {
                StringBuilder comparable2 = new StringBuilder();
                comparable2.append("handle cancel calls, cancel calls: ");
                comparable2.append(arrayList.size());
                Util.d(TAG, comparable2.toString());
                if (!arrayList.isEmpty()) {
                    for (DownloadCall downloadCall : arrayList) {
                        if (downloadCall.cancel()) continue;
                        iterator2.remove(downloadCall);
                    }
                }
                arrayList = new ArrayList<DownloadTask>();
                ((StringBuilder)((Object)arrayList)).append("handle cancel calls, callback cancel event: ");
                ((StringBuilder)((Object)arrayList)).append(iterator2.size());
                Util.d(TAG, ((StringBuilder)((Object)arrayList)).toString());
                if (!iterator2.isEmpty()) {
                    if (iterator2.size() <= 1) {
                        iterator2 = (DownloadCall)iterator2.get(0);
                        OkDownload.with().callbackDispatcher().dispatch().taskEnd(((DownloadCall)((Object)iterator2)).task, EndCause.CANCELED, null);
                    } else {
                        arrayList = new ArrayList<DownloadTask>();
                        iterator2 = iterator2.iterator();
                        while (iterator2.hasNext()) {
                            arrayList.add(((DownloadCall)iterator2.next()).task);
                        }
                        OkDownload.with().callbackDispatcher().endTasksWithCanceled(arrayList);
                    }
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    private boolean inspectForConflict(DownloadTask downloadTask) {
        return this.inspectForConflict(downloadTask, null, null);
    }

    private boolean inspectForConflict(DownloadTask downloadTask, Collection<DownloadTask> collection, Collection<DownloadTask> collection2) {
        boolean bl = this.inspectForConflict(downloadTask, this.readyAsyncCalls, collection, collection2) || this.inspectForConflict(downloadTask, this.runningAsyncCalls, collection, collection2) || this.inspectForConflict(downloadTask, this.runningSyncCalls, collection, collection2);
        return bl;
    }

    private void processCalls() {
        synchronized (this) {
            int n;
            int n2;
            block10: {
                block9: {
                    block8: {
                        n2 = this.skipProceedCallCount.get();
                        if (n2 <= 0) break block8;
                        return;
                    }
                    n2 = this.runningAsyncSize();
                    n = this.maxParallelRunningCount;
                    if (n2 < n) break block9;
                    return;
                }
                boolean bl = this.readyAsyncCalls.isEmpty();
                if (!bl) break block10;
                return;
            }
            try {
                Iterator<DownloadCall> iterator2 = this.readyAsyncCalls.iterator();
                while (iterator2.hasNext()) {
                    DownloadCall downloadCall = iterator2.next();
                    iterator2.remove();
                    DownloadTask downloadTask = downloadCall.task;
                    if (this.isFileConflictAfterRun(downloadTask)) {
                        OkDownload.with().callbackDispatcher().dispatch().taskEnd(downloadTask, EndCause.FILE_BUSY, null);
                        continue;
                    }
                    this.runningAsyncCalls.add(downloadCall);
                    this.getExecutorService().execute(downloadCall);
                    n2 = this.runningAsyncSize();
                    if (n2 < (n = this.maxParallelRunningCount)) continue;
                }
            }
            catch (Throwable throwable) {}
            {
                throw throwable;
            }
            return;
        }
    }

    private int runningAsyncSize() {
        return this.runningAsyncCalls.size() - this.flyingCanceledAsyncCallCount.get();
    }

    public static void setMaxParallelRunningCount(int n) {
        DownloadDispatcher downloadDispatcher = OkDownload.with().downloadDispatcher();
        if (downloadDispatcher.getClass() == DownloadDispatcher.class) {
            downloadDispatcher.maxParallelRunningCount = Math.max(1, n);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The current dispatcher is ");
        stringBuilder.append(downloadDispatcher);
        stringBuilder.append(" not DownloadDispatcher exactly!");
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void cancel(IdentifiedTask[] identifiedTaskArray) {
        this.skipProceedCallCount.incrementAndGet();
        this.cancelLocked(identifiedTaskArray);
        this.skipProceedCallCount.decrementAndGet();
        this.processCalls();
    }

    public boolean cancel(int n) {
        this.skipProceedCallCount.incrementAndGet();
        boolean bl = this.cancelLocked(DownloadTask.mockTaskForCompare(n));
        this.skipProceedCallCount.decrementAndGet();
        this.processCalls();
        return bl;
    }

    public boolean cancel(IdentifiedTask identifiedTask) {
        this.skipProceedCallCount.incrementAndGet();
        boolean bl = this.cancelLocked(identifiedTask);
        this.skipProceedCallCount.decrementAndGet();
        this.processCalls();
        return bl;
    }

    public void cancelAll() {
        this.skipProceedCallCount.incrementAndGet();
        ArrayList<DownloadTask> arrayList = new ArrayList<DownloadTask>();
        Iterator<DownloadCall> iterator2 = this.readyAsyncCalls.iterator();
        while (iterator2.hasNext()) {
            arrayList.add(iterator2.next().task);
        }
        iterator2 = this.runningAsyncCalls.iterator();
        while (iterator2.hasNext()) {
            arrayList.add(iterator2.next().task);
        }
        iterator2 = this.runningSyncCalls.iterator();
        while (iterator2.hasNext()) {
            arrayList.add(iterator2.next().task);
        }
        if (!arrayList.isEmpty()) {
            this.cancelLocked(arrayList.toArray(new DownloadTask[arrayList.size()]));
        }
        this.skipProceedCallCount.decrementAndGet();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    boolean cancelLocked(IdentifiedTask identifiedTask) {
        synchronized (this) {
            boolean bl;
            ArrayList<DownloadCall> arrayList;
            Serializable serializable;
            block8: {
                int n;
                serializable = new StringBuilder();
                ((StringBuilder)serializable).append("cancel manually: ");
                ((StringBuilder)serializable).append(identifiedTask.getId());
                Util.d(TAG, ((StringBuilder)serializable).toString());
                serializable = new ArrayList();
                arrayList = new ArrayList<DownloadCall>();
                this.filterCanceledCalls(identifiedTask, (List<DownloadCall>)((Object)serializable), arrayList);
                if (serializable.size() <= 0 && (n = arrayList.size()) <= 0) {
                    bl = false;
                    break block8;
                }
                bl = true;
            }
            return bl;
            finally {
                this.handleCanceledCalls((List<DownloadCall>)((Object)serializable), arrayList);
            }
        }
    }

    public void enqueue(DownloadTask downloadTask) {
        this.skipProceedCallCount.incrementAndGet();
        this.enqueueLocked(downloadTask);
        this.skipProceedCallCount.decrementAndGet();
    }

    public void enqueue(DownloadTask[] downloadTaskArray) {
        this.skipProceedCallCount.incrementAndGet();
        this.enqueueLocked(downloadTaskArray);
        this.skipProceedCallCount.decrementAndGet();
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void execute(DownloadTask comparable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("execute: ");
        stringBuilder.append(comparable);
        Util.d(TAG, stringBuilder.toString());
        synchronized (this) {
            if (this.inspectCompleted((DownloadTask)comparable)) {
                return;
            }
            if (this.inspectForConflict((DownloadTask)comparable)) {
                return;
            }
            comparable = DownloadCall.create(comparable, false, this.store);
            try {
                this.runningSyncCalls.add((DownloadCall)comparable);
                // MONITOREXIT @DISABLED, blocks:[1, 3] lbl18 : MonitorExitStatement: MONITOREXIT : this
                this.syncRunCall((DownloadCall)comparable);
                return;
            }
            catch (Throwable throwable) {}
            throw throwable;
        }
    }

    public DownloadTask findSameTask(DownloadTask downloadTask) {
        synchronized (this) {
            Iterator<DownloadCall> iterator2 = new Iterator<DownloadCall>();
            ((StringBuilder)((Object)iterator2)).append("findSameTask: ");
            ((StringBuilder)((Object)iterator2)).append(downloadTask.getId());
            Util.d(TAG, ((StringBuilder)((Object)iterator2)).toString());
            for (DownloadCall downloadCall : this.readyAsyncCalls) {
                if (downloadCall.isCanceled() || !downloadCall.equalsTask(downloadTask)) continue;
                downloadTask = downloadCall.task;
                return downloadTask;
            }
            for (DownloadCall downloadCall : this.runningAsyncCalls) {
                if (downloadCall.isCanceled() || !downloadCall.equalsTask(downloadTask)) continue;
                downloadTask = downloadCall.task;
                return downloadTask;
            }
            try {
                for (DownloadCall downloadCall : this.runningSyncCalls) {
                    if (downloadCall.isCanceled() || !downloadCall.equalsTask(downloadTask)) continue;
                    downloadTask = downloadCall.task;
                    return downloadTask;
                }
            }
            catch (Throwable throwable) {}
            {
                throw throwable;
            }
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void finish(DownloadCall object) {
        synchronized (this) {
            boolean bl = ((DownloadCall)object).asyncExecuted;
            List<DownloadCall> list = bl ? this.runningAsyncCalls : this.runningSyncCalls;
            if (!list.remove(object)) {
                object = new AssertionError((Object)"Call wasn't in-flight!");
                throw object;
            }
            if (bl && ((DownloadCall)object).isCanceled()) {
                this.flyingCanceledAsyncCallCount.decrementAndGet();
            }
            if (bl) {
                this.processCalls();
            }
            return;
        }
    }

    public void flyingCanceled(DownloadCall downloadCall) {
        synchronized (this) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("flying canceled: ");
            stringBuilder.append(downloadCall.task.getId());
            Util.d(TAG, stringBuilder.toString());
            if (downloadCall.asyncExecuted) {
                this.flyingCanceledAsyncCallCount.incrementAndGet();
            }
            return;
        }
    }

    ExecutorService getExecutorService() {
        synchronized (this) {
            ExecutorService executorService;
            if (this.executorService == null) {
                TimeUnit timeUnit = TimeUnit.SECONDS;
                SynchronousQueue<Runnable> synchronousQueue = new SynchronousQueue<Runnable>();
                executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, timeUnit, synchronousQueue, Util.threadFactory("OkDownload Download", false));
                this.executorService = executorService;
            }
            executorService = this.executorService;
            return executorService;
        }
    }

    boolean inspectCompleted(DownloadTask downloadTask) {
        return this.inspectCompleted(downloadTask, null);
    }

    boolean inspectCompleted(DownloadTask downloadTask, Collection<DownloadTask> collection) {
        if (downloadTask.isPassIfAlreadyCompleted() && StatusUtil.isCompleted(downloadTask)) {
            if (downloadTask.getFilename() == null && !OkDownload.with().downloadStrategy().validFilenameFromStore(downloadTask)) {
                return false;
            }
            OkDownload.with().downloadStrategy().validInfoOnCompleted(downloadTask, this.store);
            if (collection != null) {
                collection.add(downloadTask);
            } else {
                OkDownload.with().callbackDispatcher().dispatch().taskEnd(downloadTask, EndCause.COMPLETED, null);
            }
            return true;
        }
        return false;
    }

    boolean inspectForConflict(DownloadTask downloadTask, Collection<DownloadCall> object, Collection<DownloadTask> collection, Collection<DownloadTask> collection2) {
        CallbackDispatcher callbackDispatcher = OkDownload.with().callbackDispatcher();
        object = object.iterator();
        while (object.hasNext()) {
            Comparable<DownloadCall> comparable = (DownloadCall)object.next();
            if (comparable.isCanceled()) continue;
            if (comparable.equalsTask(downloadTask)) {
                if (collection != null) {
                    collection.add(downloadTask);
                } else {
                    callbackDispatcher.dispatch().taskEnd(downloadTask, EndCause.SAME_TASK_BUSY, null);
                }
                return true;
            }
            File file = comparable.getFile();
            comparable = downloadTask.getFile();
            if (file == null || comparable == null || !file.equals(comparable)) continue;
            if (collection2 != null) {
                collection2.add(downloadTask);
            } else {
                callbackDispatcher.dispatch().taskEnd(downloadTask, EndCause.FILE_BUSY, null);
            }
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isFileConflictAfterRun(DownloadTask downloadTask) {
        synchronized (this) {
            boolean bl;
            Comparable<DownloadCall> comparable3;
            Comparable<StringBuilder> comparable2 = new Comparable<StringBuilder>();
            ((StringBuilder)comparable2).append("is file conflict after run: ");
            ((StringBuilder)comparable2).append(downloadTask.getId());
            Util.d(TAG, ((StringBuilder)comparable2).toString());
            comparable2 = downloadTask.getFile();
            if (comparable2 == null) {
                return false;
            }
            for (Comparable<DownloadCall> comparable3 : this.runningSyncCalls) {
                if (comparable3.isCanceled() || comparable3.task == downloadTask || (comparable3 = comparable3.task.getFile()) == null || !(bl = ((File)comparable2).equals(comparable3))) continue;
                return true;
            }
            Iterator<DownloadCall> iterator2 = this.runningAsyncCalls.iterator();
            do {
                if (!iterator2.hasNext()) return false;
            } while ((comparable3 = iterator2.next()).isCanceled() || comparable3.task == downloadTask || (comparable3 = comparable3.task.getFile()) == null || !(bl = ((File)comparable2).equals(comparable3)));
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isPending(DownloadTask downloadTask) {
        synchronized (this) {
            boolean bl;
            DownloadCall downloadCall;
            Object object = new StringBuilder();
            ((StringBuilder)object).append("isPending: ");
            ((StringBuilder)object).append(downloadTask.getId());
            Util.d(TAG, ((StringBuilder)object).toString());
            object = this.readyAsyncCalls.iterator();
            do {
                if (!object.hasNext()) return false;
            } while ((downloadCall = (DownloadCall)object.next()).isCanceled() || !(bl = downloadCall.equalsTask(downloadTask)));
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isRunning(DownloadTask downloadTask) {
        synchronized (this) {
            DownloadCall downloadCall;
            boolean bl;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("isRunning: ");
            stringBuilder.append(downloadTask.getId());
            Util.d(TAG, stringBuilder.toString());
            for (DownloadCall downloadCall2 : this.runningSyncCalls) {
                if (downloadCall2.isCanceled() || !(bl = downloadCall2.equalsTask(downloadTask))) continue;
                return true;
            }
            Iterator<DownloadCall> iterator2 = this.runningAsyncCalls.iterator();
            do {
                if (!iterator2.hasNext()) return false;
            } while ((downloadCall = iterator2.next()).isCanceled() || !(bl = downloadCall.equalsTask(downloadTask)));
            return true;
        }
    }

    public void setDownloadStore(DownloadStore downloadStore) {
        this.store = downloadStore;
    }

    void syncRunCall(DownloadCall downloadCall) {
        downloadCall.run();
    }
}

