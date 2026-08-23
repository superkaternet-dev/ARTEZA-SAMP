/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.filedownloader.database;

import android.util.SparseArray;
import com.liulishuo.filedownloader.database.FileDownloadDatabase;
import com.liulishuo.filedownloader.model.ConnectionModel;
import com.liulishuo.filedownloader.model.FileDownloadModel;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class NoDatabaseImpl
implements FileDownloadDatabase {
    final SparseArray<List<ConnectionModel>> connectionModelListMap;
    final SparseArray<FileDownloadModel> downloaderModelMap = new SparseArray();

    public NoDatabaseImpl() {
        this.connectionModelListMap = new SparseArray();
    }

    public static Maker createMaker() {
        return new Maker();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void clear() {
        SparseArray<FileDownloadModel> sparseArray = this.downloaderModelMap;
        synchronized (sparseArray) {
            this.downloaderModelMap.clear();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public FileDownloadModel find(int n) {
        SparseArray<FileDownloadModel> sparseArray = this.downloaderModelMap;
        synchronized (sparseArray) {
            return (FileDownloadModel)this.downloaderModelMap.get(n);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public List<ConnectionModel> findConnectionModel(int n) {
        ArrayList<ConnectionModel> arrayList = new ArrayList<ConnectionModel>();
        SparseArray<List<ConnectionModel>> sparseArray = this.connectionModelListMap;
        // MONITORENTER : sparseArray
        List list = (List)this.connectionModelListMap.get(n);
        // MONITOREXIT : sparseArray
        if (list == null) return arrayList;
        arrayList.addAll(list);
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void insert(FileDownloadModel fileDownloadModel) {
        SparseArray<FileDownloadModel> sparseArray = this.downloaderModelMap;
        synchronized (sparseArray) {
            this.downloaderModelMap.put(fileDownloadModel.getId(), (Object)fileDownloadModel);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void insertConnectionModel(ConnectionModel connectionModel) {
        int n = connectionModel.getId();
        SparseArray<List<ConnectionModel>> sparseArray = this.connectionModelListMap;
        synchronized (sparseArray) {
            ArrayList<ConnectionModel> arrayList;
            ArrayList<ConnectionModel> arrayList2 = arrayList = (ArrayList<ConnectionModel>)this.connectionModelListMap.get(n);
            if (arrayList == null) {
                arrayList2 = new ArrayList<ConnectionModel>();
                this.connectionModelListMap.put(n, arrayList2);
            }
            arrayList2.add(connectionModel);
            return;
        }
    }

    @Override
    public FileDownloadDatabase.Maintainer maintainer() {
        return new Maintainer(this);
    }

    @Override
    public void onTaskStart(int n) {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean remove(int n) {
        SparseArray<FileDownloadModel> sparseArray = this.downloaderModelMap;
        synchronized (sparseArray) {
            this.downloaderModelMap.remove(n);
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void removeConnections(int n) {
        SparseArray<List<ConnectionModel>> sparseArray = this.connectionModelListMap;
        synchronized (sparseArray) {
            this.connectionModelListMap.remove(n);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void update(FileDownloadModel fileDownloadModel) {
        if (fileDownloadModel == null) {
            FileDownloadLog.w(this, "update but model == null!", new Object[0]);
            return;
        }
        if (this.find(fileDownloadModel.getId()) != null) {
            SparseArray<FileDownloadModel> sparseArray = this.downloaderModelMap;
            synchronized (sparseArray) {
                this.downloaderModelMap.remove(fileDownloadModel.getId());
                this.downloaderModelMap.put(fileDownloadModel.getId(), (Object)fileDownloadModel);
                return;
            }
        }
        this.insert(fileDownloadModel);
    }

    @Override
    public void updateCompleted(int n, long l) {
        this.remove(n);
    }

    @Override
    public void updateConnected(int n, long l, String string2, String string3) {
    }

    @Override
    public void updateConnectionCount(int n, int n2) {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void updateConnectionModel(int n, int n2, long l) {
        SparseArray<List<ConnectionModel>> sparseArray = this.connectionModelListMap;
        synchronized (sparseArray) {
            ConnectionModel connectionModel;
            Object object = (List)this.connectionModelListMap.get(n);
            if (object == null) {
                return;
            }
            object = object.iterator();
            do {
                if (!object.hasNext()) return;
            } while ((connectionModel = (ConnectionModel)object.next()).getIndex() != n2);
            connectionModel.setCurrentOffset(l);
            return;
        }
    }

    @Override
    public void updateError(int n, Throwable throwable, long l) {
    }

    @Override
    public void updateOldEtagOverdue(int n, String string2, long l, long l2, int n2) {
    }

    @Override
    public void updatePause(int n, long l) {
    }

    @Override
    public void updatePending(int n) {
    }

    @Override
    public void updateProgress(int n, long l) {
    }

    @Override
    public void updateRetry(int n, Throwable throwable) {
    }

    class Maintainer
    implements FileDownloadDatabase.Maintainer {
        final NoDatabaseImpl this$0;

        Maintainer(NoDatabaseImpl noDatabaseImpl) {
            this.this$0 = noDatabaseImpl;
        }

        @Override
        public void changeFileDownloadModelId(int n, FileDownloadModel fileDownloadModel) {
        }

        @Override
        public Iterator<FileDownloadModel> iterator() {
            return new MaintainerIterator(this.this$0);
        }

        @Override
        public void onFinishMaintain() {
        }

        @Override
        public void onRefreshedValidData(FileDownloadModel fileDownloadModel) {
        }

        @Override
        public void onRemovedInvalidData(FileDownloadModel fileDownloadModel) {
        }
    }

    class MaintainerIterator
    implements Iterator<FileDownloadModel> {
        final NoDatabaseImpl this$0;

        MaintainerIterator(NoDatabaseImpl noDatabaseImpl) {
            this.this$0 = noDatabaseImpl;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public FileDownloadModel next() {
            return null;
        }

        @Override
        public void remove() {
        }
    }

    public static class Maker
    implements FileDownloadHelper.DatabaseCustomMaker {
        @Override
        public FileDownloadDatabase customMake() {
            return new NoDatabaseImpl();
        }
    }
}

