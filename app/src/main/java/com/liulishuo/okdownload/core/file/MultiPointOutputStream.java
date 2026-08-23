/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.os.StatFs
 *  android.os.SystemClock
 *  android.util.SparseArray
 */
package com.liulishuo.okdownload.core.file;

import android.net.Uri;
import android.os.StatFs;
import android.os.SystemClock;
import android.util.SparseArray;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.OkDownload;
import com.liulishuo.okdownload.core.Util;
import com.liulishuo.okdownload.core.breakpoint.BlockInfo;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.breakpoint.DownloadStore;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.exception.PreAllocateException;
import com.liulishuo.okdownload.core.file.DownloadOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.LockSupport;

public class MultiPointOutputStream {
    private static final ExecutorService FILE_IO_EXECUTOR = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), Util.threadFactory("OkDownload file io", false));
    private static final String TAG = "MultiPointOutputStream";
    final AtomicLong allNoSyncLength;
    final StreamsState doneState;
    private volatile boolean firstOutputStream = true;
    private final int flushBufferSize;
    private final BreakpointInfo info;
    private final boolean isPreAllocateLength;
    final AtomicLong lastSyncTimestamp;
    List<Integer> noMoreStreamList;
    final SparseArray<AtomicLong> noSyncLengthMap;
    final SparseArray<DownloadOutputStream> outputStreamMap = new SparseArray();
    final SparseArray<Thread> parkedRunBlockThreadMap;
    private String path;
    volatile Thread runSyncThread;
    StreamsState state;
    private final DownloadStore store;
    private final boolean supportSeek;
    private final int syncBufferIntervalMills;
    private final int syncBufferSize;
    IOException syncException;
    volatile Future syncFuture;
    private final Runnable syncRunnable;
    private final DownloadTask task;

    public MultiPointOutputStream(DownloadTask downloadTask, BreakpointInfo breakpointInfo, DownloadStore downloadStore) {
        this(downloadTask, breakpointInfo, downloadStore, null);
    }

    MultiPointOutputStream(DownloadTask comparable, BreakpointInfo breakpointInfo, DownloadStore downloadStore, Runnable runnable) {
        this.noSyncLengthMap = new SparseArray();
        this.allNoSyncLength = new AtomicLong();
        this.lastSyncTimestamp = new AtomicLong();
        this.parkedRunBlockThreadMap = new SparseArray();
        this.doneState = new StreamsState();
        this.state = new StreamsState();
        this.task = comparable;
        this.flushBufferSize = ((DownloadTask)comparable).getFlushBufferSize();
        this.syncBufferSize = ((DownloadTask)comparable).getSyncBufferSize();
        this.syncBufferIntervalMills = ((DownloadTask)comparable).getSyncBufferIntervalMills();
        this.info = breakpointInfo;
        this.store = downloadStore;
        this.supportSeek = OkDownload.with().outputStreamFactory().supportSeek();
        this.isPreAllocateLength = OkDownload.with().processFileStrategy().isPreAllocateLength((DownloadTask)comparable);
        this.noMoreStreamList = new ArrayList<Integer>();
        this.syncRunnable = runnable == null ? new Runnable(this){
            final MultiPointOutputStream this$0;
            {
                this.this$0 = multiPointOutputStream;
            }

            @Override
            public void run() {
                this.this$0.runSyncDelayException();
            }
        } : runnable;
        comparable = ((DownloadTask)comparable).getFile();
        if (comparable != null) {
            this.path = ((File)comparable).getAbsolutePath();
        }
    }

    private void inspectValidPath() {
        if (this.path == null && this.task.getFile() != null) {
            this.path = this.task.getFile().getAbsolutePath();
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void cancel() {
        var3_1 = 0;
        var2_2 = 0;
        var1_3 = 0;
        try {
            var5_4 = this.allNoSyncLength.get();
            if (var5_4 > 0L) ** GOTO lbl-1000
            ** GOTO lbl15
        }
        catch (Throwable var8_7) {
            block31: {
                synchronized (this) {
                    var9_15 = this.outputStreamMap.clone();
                    {
                        catch (Throwable var7_12) {}
                        {
                            throw var7_12;
                        }
                    }
                }
lbl15:
                // 1 sources

                synchronized (this) {
                    var8_5 = this.outputStreamMap.clone();
                }
                var2_2 = var8_5.size();
                while (true) {
                    if (var1_3 >= var2_2) {
                        this.store.onTaskEnd(this.task.getId(), EndCause.CANCELED, null);
                        return;
                    }
                    try {
                        this.close(var8_5.keyAt(var1_3));
                    }
                    catch (IOException var9_13) {
                        var7_8 = new StringBuilder();
                        var7_8.append("OutputStream close failed task[");
                        var7_8.append(this.task.getId());
                        var7_8.append("] block[");
                        var7_8.append(var1_3);
                        var7_8.append("]");
                        var7_8.append(var9_13);
                        Util.d("MultiPointOutputStream", var7_8.toString());
                    }
                    ++var1_3;
                }
lbl-1000:
                // 1 sources

                {
                    var7_9 = this.outputStreamMap.clone();
                    var4_16 = var7_9.size();
                    for (var1_3 = 0; var1_3 < var4_16; ++var1_3) {
                        this.noMoreStreamList.add(var7_9.keyAt(var1_3));
                    }
                }
                {
                    if (this.syncFuture == null || this.syncFuture.isDone()) break block31;
                    this.inspectValidPath();
                    OkDownload.with().processFileStrategy().getFileLock().increaseLock(this.path);
                }
                try {
                    this.ensureSync(true, -1);
                }
                finally {
                    OkDownload.with().processFileStrategy().getFileLock().decreaseLock(this.path);
                }
            }
            synchronized (this) {
                var9_14 = this.outputStreamMap.clone();
            }
            var2_2 = var9_14.size();
            var1_3 = var3_1;
            while (true) {
                if (var1_3 >= var2_2) {
                    this.store.onTaskEnd(this.task.getId(), EndCause.CANCELED, null);
                    return;
                }
                try {
                    this.close(var9_14.keyAt(var1_3));
                }
                catch (IOException var8_6) {
                    var7_9 = new StringBuilder();
                    var7_9.append("OutputStream close failed task[");
                    var7_9.append(this.task.getId());
                    var7_9.append("] block[");
                    var7_9.append(var1_3);
                    var7_9.append("]");
                    var7_9.append(var8_6);
                    Util.d("MultiPointOutputStream", var7_9.toString());
                }
                ++var1_3;
            }
            var3_1 = var9_15.size();
            var1_3 = var2_2;
            while (true) {
                if (var1_3 >= var3_1) {
                    this.store.onTaskEnd(this.task.getId(), EndCause.CANCELED, null);
                    throw var8_7;
                }
                try {
                    this.close(var9_15.keyAt(var1_3));
                }
                catch (IOException var7_11) {
                    var10_17 = new StringBuilder();
                    var10_17.append("OutputStream close failed task[");
                    var10_17.append(this.task.getId());
                    var10_17.append("] block[");
                    var10_17.append(var1_3);
                    var10_17.append("]");
                    var10_17.append(var7_11);
                    Util.d("MultiPointOutputStream", var10_17.toString());
                }
                ++var1_3;
            }
        }
    }

    public void cancelAsync() {
        FILE_IO_EXECUTOR.execute(new Runnable(this){
            final MultiPointOutputStream this$0;
            {
                this.this$0 = multiPointOutputStream;
            }

            @Override
            public void run() {
                this.this$0.cancel();
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void close(int n) throws IOException {
        synchronized (this) {
            Object object = (DownloadOutputStream)this.outputStreamMap.get(n);
            if (object != null) {
                object.close();
                this.outputStreamMap.remove(n);
                object = new StringBuilder();
                ((StringBuilder)object).append("OutputStream close task[");
                ((StringBuilder)object).append(this.task.getId());
                ((StringBuilder)object).append("] block[");
                ((StringBuilder)object).append(n);
                ((StringBuilder)object).append("]");
                Util.d(TAG, ((StringBuilder)object).toString());
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void done(int n) throws IOException {
        this.noMoreStreamList.add(n);
        try {
            Object object = this.syncException;
            if (object != null) throw object;
            if (this.syncFuture != null && !this.syncFuture.isDone()) {
                object = (AtomicLong)this.noSyncLengthMap.get(n);
                if (object == null) return;
                if (((AtomicLong)object).get() <= 0L) return;
                this.inspectStreamState(this.doneState);
                this.ensureSync(this.doneState.isNoMoreStream, n);
                return;
            }
            object = this.syncFuture;
            if (object == null) {
                object = new StringBuilder();
                ((StringBuilder)object).append("OutputStream done but no need to ensure sync, because the sync job not run yet. task[");
                ((StringBuilder)object).append(this.task.getId());
                ((StringBuilder)object).append("] block[");
                ((StringBuilder)object).append(n);
                ((StringBuilder)object).append("]");
                Util.d(TAG, ((StringBuilder)object).toString());
                return;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("OutputStream done but no need to ensure sync, because the syncFuture.isDone[");
            ((StringBuilder)object).append(this.syncFuture.isDone());
            ((StringBuilder)object).append("] task[");
            ((StringBuilder)object).append(this.task.getId());
            ((StringBuilder)object).append("] block[");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append("]");
            Util.d(TAG, ((StringBuilder)object).toString());
            return;
        }
        finally {
            this.close(n);
        }
    }

    /*
     * Unable to fully structure code
     */
    void ensureSync(boolean var1_1, int var2_2) {
        block10: {
            block11: {
                if (this.syncFuture == null || this.syncFuture.isDone()) break block10;
                if (!var1_1) {
                    this.parkedRunBlockThreadMap.put(var2_2, (Object)Thread.currentThread());
                }
                if (this.runSyncThread == null) break block11;
                this.unparkThread(this.runSyncThread);
                ** GOTO lbl11
            }
            while (true) {
                block12: {
                    if (!this.isRunSyncThreadValid()) break block12;
                    this.unparkThread(this.runSyncThread);
lbl11:
                    // 2 sources

                    if (var1_1) {
                        this.unparkThread(this.runSyncThread);
                        try {
                            this.syncFuture.get();
                        }
                        catch (ExecutionException var3_3) {
                        }
                        catch (InterruptedException var3_4) {}
                    } else {
                        this.parkThread();
                    }
                    return;
                }
                this.parkThread(25L);
            }
        }
    }

    Future executeSyncRunnableAsync() {
        return FILE_IO_EXECUTOR.submit(this.syncRunnable);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void flushProcess() throws IOException {
        long l;
        long l2;
        int n;
        int n2;
        int n3;
        Object object = this.noSyncLengthMap;
        synchronized (object) {
            n3 = this.noSyncLengthMap.size();
        }
        SparseArray sparseArray = new SparseArray(n3);
        for (n2 = 0; n2 < n3; ++n2) {
            try {
                n = this.outputStreamMap.keyAt(n2);
                l2 = ((AtomicLong)this.noSyncLengthMap.get(n)).get();
                if (l2 <= 0L) continue;
                sparseArray.put(n, (Object)l2);
                ((DownloadOutputStream)this.outputStreamMap.get(n)).flushAndSync();
                continue;
            }
            catch (IOException iOException) {
                object = new StringBuilder();
                ((StringBuilder)object).append("OutputStream flush and sync data to filesystem failed ");
                ((StringBuilder)object).append(iOException);
                Util.w(TAG, ((StringBuilder)object).toString());
                return;
            }
        }
        n2 = 1;
        if (n2 == 0) return;
        n3 = sparseArray.size();
        l2 = 0L;
        for (n2 = 0; n2 < n3; l2 += l, ++n2) {
            n = sparseArray.keyAt(n2);
            l = (Long)sparseArray.valueAt(n2);
            this.store.onSyncToFilesystemSuccess(this.info, n, l);
            ((AtomicLong)this.noSyncLengthMap.get(n)).addAndGet(-l);
            object = new StringBuilder();
            ((StringBuilder)object).append("OutputStream sync success (");
            ((StringBuilder)object).append(this.task.getId());
            ((StringBuilder)object).append(") ");
            ((StringBuilder)object).append("block(");
            ((StringBuilder)object).append(n);
            ((StringBuilder)object).append(") ");
            ((StringBuilder)object).append(" syncLength(");
            ((StringBuilder)object).append(l);
            ((StringBuilder)object).append(")");
            ((StringBuilder)object).append(" currentOffset(");
            ((StringBuilder)object).append(this.info.getBlock(n).getCurrentOffset());
            ((StringBuilder)object).append(")");
            Util.d(TAG, ((StringBuilder)object).toString());
        }
        this.allNoSyncLength.addAndGet(-l2);
        this.lastSyncTimestamp.set(SystemClock.uptimeMillis());
    }

    long getNextParkMillisecond() {
        long l = this.now();
        long l2 = this.lastSyncTimestamp.get();
        return (long)this.syncBufferIntervalMills - (l - l2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void inspectAndPersist() throws IOException {
        Object object = this.syncException;
        if (object != null) {
            throw object;
        }
        if (this.syncFuture == null) {
            object = this.syncRunnable;
            synchronized (object) {
                if (this.syncFuture == null) {
                    this.syncFuture = this.executeSyncRunnableAsync();
                }
            }
        }
    }

    public void inspectComplete(int n) throws IOException {
        BlockInfo blockInfo = this.info.getBlock(n);
        if (Util.isCorrectFull(blockInfo.getCurrentOffset(), blockInfo.getContentLength())) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("The current offset on block-info isn't update correct, ");
        stringBuilder.append(blockInfo.getCurrentOffset());
        stringBuilder.append(" != ");
        stringBuilder.append(blockInfo.getContentLength());
        stringBuilder.append(" on ");
        stringBuilder.append(n);
        throw new IOException(stringBuilder.toString());
    }

    void inspectFreeSpace(StatFs statFs, long l) throws PreAllocateException {
        long l2 = Util.getFreeSpaceBytes(statFs);
        if (l2 >= l) {
            return;
        }
        throw new PreAllocateException(l, l2);
    }

    void inspectStreamState(StreamsState streamsState) {
        boolean bl = true;
        streamsState.newNoMoreStreamBlockList.clear();
        SparseArray sparseArray = this.outputStreamMap.clone();
        int n = sparseArray.size();
        for (int i = 0; i < n; ++i) {
            boolean bl2;
            int n2 = sparseArray.keyAt(i);
            if (this.noMoreStreamList.contains(n2)) {
                bl2 = bl;
                if (!streamsState.noMoreStreamBlockList.contains(n2)) {
                    streamsState.noMoreStreamBlockList.add(n2);
                    streamsState.newNoMoreStreamBlockList.add(n2);
                    bl2 = bl;
                }
            } else {
                bl2 = false;
            }
            bl = bl2;
        }
        streamsState.isNoMoreStream = bl;
    }

    boolean isNoNeedFlushForLength() {
        boolean bl = this.allNoSyncLength.get() < (long)this.syncBufferSize;
        return bl;
    }

    boolean isRunSyncThreadValid() {
        boolean bl = this.runSyncThread != null;
        return bl;
    }

    long now() {
        return SystemClock.uptimeMillis();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    DownloadOutputStream outputStream(int n) throws IOException {
        synchronized (this) {
            Object object;
            Object object2 = object = (DownloadOutputStream)this.outputStreamMap.get(n);
            if (object == null) {
                AtomicLong atomicLong;
                long l;
                boolean bl = Util.isUriFileScheme(this.task.getUri());
                if (bl) {
                    object2 = this.task.getFile();
                    if (object2 == null) {
                        object2 = new FileNotFoundException("Filename is not ready!");
                        throw object2;
                    }
                    object = this.task.getParentFile();
                    if (!((File)object).exists() && !((File)object).mkdirs()) {
                        object2 = new IOException("Create parent folder failed!");
                        throw object2;
                    }
                    if (((File)object2).createNewFile()) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Create new file: ");
                        ((StringBuilder)object).append(((File)object2).getName());
                        Util.d(TAG, ((StringBuilder)object).toString());
                    }
                    object2 = Uri.fromFile((File)object2);
                } else {
                    object2 = this.task.getUri();
                }
                object2 = OkDownload.with().outputStreamFactory().create(OkDownload.with().context(), (Uri)object2, this.flushBufferSize);
                if (this.supportSeek && (l = this.info.getBlock(n).getRangeLeft()) > 0L) {
                    object2.seek(l);
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Create output stream write from (");
                    ((StringBuilder)object).append(this.task.getId());
                    ((StringBuilder)object).append(") block(");
                    ((StringBuilder)object).append(n);
                    ((StringBuilder)object).append(") ");
                    ((StringBuilder)object).append(l);
                    Util.d(TAG, ((StringBuilder)object).toString());
                }
                if (!this.info.isChunked() && this.firstOutputStream && this.isPreAllocateLength) {
                    l = this.info.getTotalLength();
                    if (bl) {
                        object = this.task.getFile();
                        long l2 = l - ((File)object).length();
                        if (l2 > 0L) {
                            atomicLong = new StatFs(((File)object).getAbsolutePath());
                            this.inspectFreeSpace((StatFs)atomicLong, l2);
                            object2.setLength(l);
                        }
                    } else {
                        object2.setLength(l);
                    }
                }
                object = this.noSyncLengthMap;
                synchronized (object) {
                    this.outputStreamMap.put(n, object2);
                    SparseArray<AtomicLong> sparseArray = this.noSyncLengthMap;
                    atomicLong = new AtomicLong();
                    sparseArray.put(n, (Object)atomicLong);
                }
                this.firstOutputStream = false;
            }
            return object2;
        }
    }

    void parkThread() {
        LockSupport.park();
    }

    void parkThread(long l) {
        LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(l));
    }

    void runSync() throws IOException {
        StringBuilder object2 = new StringBuilder();
        object2.append("OutputStream start flush looper task[");
        object2.append(this.task.getId());
        object2.append("] with ");
        object2.append("syncBufferIntervalMills[");
        object2.append(this.syncBufferIntervalMills);
        object2.append("] ");
        object2.append("syncBufferSize[");
        object2.append(this.syncBufferSize);
        object2.append("]");
        Util.d(TAG, object2.toString());
        this.runSyncThread = Thread.currentThread();
        long l = this.syncBufferIntervalMills;
        this.flushProcess();
        while (true) {
            this.parkThread(l);
            this.inspectStreamState(this.state);
            if (this.state.isStreamsEndOrChanged()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("runSync state change isNoMoreStream[");
                stringBuilder.append(this.state.isNoMoreStream);
                stringBuilder.append("]");
                stringBuilder.append(" newNoMoreStreamBlockList[");
                stringBuilder.append(this.state.newNoMoreStreamBlockList);
                stringBuilder.append("]");
                Util.d(TAG, stringBuilder.toString());
                if (this.allNoSyncLength.get() > 0L) {
                    this.flushProcess();
                }
                for (Integer n : this.state.newNoMoreStreamBlockList) {
                    Thread thread2 = (Thread)this.parkedRunBlockThreadMap.get(n.intValue());
                    this.parkedRunBlockThreadMap.remove(n.intValue());
                    if (thread2 == null) continue;
                    this.unparkThread(thread2);
                }
                if (!this.state.isNoMoreStream) continue;
                int n = this.parkedRunBlockThreadMap.size();
                for (int i = 0; i < n; ++i) {
                    Thread thread3 = (Thread)this.parkedRunBlockThreadMap.valueAt(i);
                    if (thread3 == null) continue;
                    this.unparkThread(thread3);
                }
                this.parkedRunBlockThreadMap.clear();
                return;
            }
            if (this.isNoNeedFlushForLength()) {
                l = this.syncBufferIntervalMills;
                continue;
            }
            l = this.getNextParkMillisecond();
            if (l > 0L) continue;
            this.flushProcess();
            l = this.syncBufferIntervalMills;
        }
    }

    void runSyncDelayException() {
        try {
            this.runSync();
        }
        catch (IOException iOException) {
            this.syncException = iOException;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Sync to breakpoint-store for task[");
            stringBuilder.append(this.task.getId());
            stringBuilder.append("] ");
            stringBuilder.append("failed with cause: ");
            stringBuilder.append(iOException);
            Util.w(TAG, stringBuilder.toString());
        }
    }

    void unparkThread(Thread thread2) {
        LockSupport.unpark(thread2);
    }

    public void write(int n, byte[] byArray, int n2) throws IOException {
        this.outputStream(n).write(byArray, 0, n2);
        this.allNoSyncLength.addAndGet(n2);
        ((AtomicLong)this.noSyncLengthMap.get(n)).addAndGet(n2);
        this.inspectAndPersist();
    }

    static class StreamsState {
        boolean isNoMoreStream;
        List<Integer> newNoMoreStreamBlockList;
        List<Integer> noMoreStreamBlockList = new ArrayList<Integer>();

        StreamsState() {
            this.newNoMoreStreamBlockList = new ArrayList<Integer>();
        }

        boolean isStreamsEndOrChanged() {
            boolean bl = this.isNoMoreStream || this.newNoMoreStreamBlockList.size() > 0;
            return bl;
        }
    }
}

