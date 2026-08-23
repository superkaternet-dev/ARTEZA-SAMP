/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.HandlerThread
 *  android.os.Message
 */
package com.liulishuo.filedownloader.database;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.database.NoDatabaseImpl;
import com.liulishuo.filedownloader.database.SqliteDatabaseImpl;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadProperties;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class RemitDatabase
implements FileDownloadDatabase {
    private static final int WHAT_CLEAN_LOCK = 0;
    private final NoDatabaseImpl cachedDatabase;
    private final List<Integer> freeToDBIdList = new ArrayList<Integer>();
    private Handler handler;
    private AtomicInteger handlingId = new AtomicInteger();
    private final long minInterval;
    private volatile Thread parkThread;
    private final SqliteDatabaseImpl realDatabase;

    public RemitDatabase() {
        this.cachedDatabase = new NoDatabaseImpl();
        this.realDatabase = new SqliteDatabaseImpl();
        this.minInterval = FileDownloadProperties.getImpl().downloadMinProgressTime;
        HandlerThread handlerThread = new HandlerThread(FileDownloadUtils.getThreadPoolName("RemitHandoverToDB"));
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper(), new Handler.Callback(this){
            final RemitDatabase this$0;
            {
                this.this$0 = remitDatabase;
            }

            public boolean handleMessage(Message message) {
                int n = message.what;
                if (n == 0) {
                    if (this.this$0.parkThread != null) {
                        LockSupport.unpark(this.this$0.parkThread);
                        RemitDatabase.access$002(this.this$0, null);
                    }
                    return false;
                }
                try {
                    this.this$0.handlingId.set(n);
                    this.this$0.syncCacheToDB(n);
                    this.this$0.freeToDBIdList.add(n);
                    return false;
                }
                finally {
                    this.this$0.handlingId.set(0);
                    if (this.this$0.parkThread != null) {
                        LockSupport.unpark(this.this$0.parkThread);
                        RemitDatabase.access$002(this.this$0, null);
                    }
                }
            }
        });
    }

    static /* synthetic */ Thread access$002(RemitDatabase remitDatabase, Thread thread2) {
        remitDatabase.parkThread = thread2;
        return thread2;
    }

    private void ensureCacheToDB(int n) {
        this.handler.removeMessages(n);
        if (this.handlingId.get() == n) {
            this.parkThread = Thread.currentThread();
            this.handler.sendEmptyMessage(0);
            LockSupport.park();
        } else {
            this.syncCacheToDB(n);
        }
    }

    private boolean isNoNeedUpdateToRealDB(int n) {
        return this.freeToDBIdList.contains(n) ^ true;
    }

    private void syncCacheToDB(int n) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "sync cache to db %d", n);
        }
        this.realDatabase.update(this.cachedDatabase.find(n));
        Object object = this.cachedDatabase.findConnectionModel(n);
        this.realDatabase.removeConnections(n);
        Iterator<ConnectionModel> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            this.realDatabase.insertConnectionModel((ConnectionModel)object);
        }
    }

    @Override
    public void clear() {
        this.cachedDatabase.clear();
        this.realDatabase.clear();
    }

    @Override
    public FileDownloadModel find(int n) {
        return this.cachedDatabase.find(n);
    }

    @Override
    public List<ConnectionModel> findConnectionModel(int n) {
        return this.cachedDatabase.findConnectionModel(n);
    }

    @Override
    public void insert(FileDownloadModel fileDownloadModel) {
        this.cachedDatabase.insert(fileDownloadModel);
        if (this.isNoNeedUpdateToRealDB(fileDownloadModel.getId())) {
            return;
        }
        this.realDatabase.insert(fileDownloadModel);
    }

    @Override
    public void insertConnectionModel(ConnectionModel connectionModel) {
        this.cachedDatabase.insertConnectionModel(connectionModel);
        if (this.isNoNeedUpdateToRealDB(connectionModel.getId())) {
            return;
        }
        this.realDatabase.insertConnectionModel(connectionModel);
    }

    @Override
    public FileDownloadDatabase.Maintainer maintainer() {
        return this.realDatabase.maintainer(this.cachedDatabase.downloaderModelMap, this.cachedDatabase.connectionModelListMap);
    }

    @Override
    public void onTaskStart(int n) {
        this.handler.sendEmptyMessageDelayed(n, this.minInterval);
    }

    @Override
    public boolean remove(int n) {
        this.realDatabase.remove(n);
        return this.cachedDatabase.remove(n);
    }

    @Override
    public void removeConnections(int n) {
        this.cachedDatabase.removeConnections(n);
        if (this.isNoNeedUpdateToRealDB(n)) {
            return;
        }
        this.realDatabase.removeConnections(n);
    }

    @Override
    public void update(FileDownloadModel fileDownloadModel) {
        this.cachedDatabase.update(fileDownloadModel);
        if (this.isNoNeedUpdateToRealDB(fileDownloadModel.getId())) {
            return;
        }
        this.realDatabase.update(fileDownloadModel);
    }

    @Override
    public void updateCompleted(int n, long l) {
        this.cachedDatabase.updateCompleted(n, l);
        if (this.isNoNeedUpdateToRealDB(n)) {
            this.handler.removeMessages(n);
            if (this.handlingId.get() == n) {
                this.parkThread = Thread.currentThread();
                this.handler.sendEmptyMessage(0);
                LockSupport.park();
                this.realDatabase.updateCompleted(n, l);
            }
        } else {
            this.realDatabase.updateCompleted(n, l);
        }
        this.freeToDBIdList.remove((Object)n);
    }

    @Override
    public void updateConnected(int n, long l, String string2, String string3) {
        this.cachedDatabase.updateConnected(n, l, string2, string3);
        if (this.isNoNeedUpdateToRealDB(n)) {
            return;
        }
        this.realDatabase.updateConnected(n, l, string2, string3);
    }

    @Override
    public void updateConnectionCount(int n, int n2) {
        this.cachedDatabase.updateConnectionCount(n, n2);
        if (this.isNoNeedUpdateToRealDB(n)) {
            return;
        }
        this.realDatabase.updateConnectionCount(n, n2);
    }

    @Override
    public void updateConnectionModel(int n, int n2, long l) {
        this.cachedDatabase.updateConnectionModel(n, n2, l);
        if (this.isNoNeedUpdateToRealDB(n)) {
            return;
        }
        this.realDatabase.updateConnectionModel(n, n2, l);
    }

    @Override
    public void updateError(int n, Throwable throwable, long l) {
        this.cachedDatabase.updateError(n, throwable, l);
        if (this.isNoNeedUpdateToRealDB(n)) {
            this.ensureCacheToDB(n);
        }
        this.realDatabase.updateError(n, throwable, l);
        this.freeToDBIdList.remove((Object)n);
    }

    @Override
    public void updateOldEtagOverdue(int n, String string2, long l, long l2, int n2) {
        this.cachedDatabase.updateOldEtagOverdue(n, string2, l, l2, n2);
        if (this.isNoNeedUpdateToRealDB(n)) {
            return;
        }
        this.realDatabase.updateOldEtagOverdue(n, string2, l, l2, n2);
    }

    @Override
    public void updatePause(int n, long l) {
        this.cachedDatabase.updatePause(n, l);
        if (this.isNoNeedUpdateToRealDB(n)) {
            this.ensureCacheToDB(n);
        }
        this.realDatabase.updatePause(n, l);
        this.freeToDBIdList.remove((Object)n);
    }

    @Override
    public void updatePending(int n) {
        this.cachedDatabase.updatePending(n);
        if (this.isNoNeedUpdateToRealDB(n)) {
            return;
        }
        this.realDatabase.updatePending(n);
    }

    @Override
    public void updateProgress(int n, long l) {
        this.cachedDatabase.updateProgress(n, l);
        if (this.isNoNeedUpdateToRealDB(n)) {
            return;
        }
        this.realDatabase.updateProgress(n, l);
    }

    @Override
    public void updateRetry(int n, Throwable throwable) {
        this.cachedDatabase.updateRetry(n, throwable);
        if (this.isNoNeedUpdateToRealDB(n)) {
            return;
        }
        this.realDatabase.updateRetry(n, throwable);
    }

    public static class Maker
    implements FileDownloadHelper.DatabaseCustomMaker {
        @Override
        public FileDownloadDatabase customMake() {
            return new RemitDatabase();
        }
    }
}

