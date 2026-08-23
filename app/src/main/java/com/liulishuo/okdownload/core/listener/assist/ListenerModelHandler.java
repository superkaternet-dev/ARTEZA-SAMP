/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.okdownload.core.listener.assist;

import android.util.SparseArray;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.breakpoint.BreakpointInfo;
import com.liulishuo.okdownload.core.listener.assist.ListenerAssist;

public class ListenerModelHandler<T extends ListenerModel>
implements ListenerAssist {
    private Boolean alwaysRecoverModel;
    private final ModelCreator<T> creator;
    final SparseArray<T> modelList = new SparseArray();
    volatile T singleTaskModel;

    ListenerModelHandler(ModelCreator<T> modelCreator) {
        this.creator = modelCreator;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    T addAndGetModel(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
        T t = this.creator.create(downloadTask.getId());
        synchronized (this) {
            if (this.singleTaskModel == null) {
                this.singleTaskModel = t;
            } else {
                this.modelList.put(downloadTask.getId(), t);
            }
            if (breakpointInfo != null) {
                t.onInfoValid(breakpointInfo);
            }
            return t;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    T getOrRecoverModel(DownloadTask downloadTask, BreakpointInfo breakpointInfo) {
        ListenerModel listenerModel;
        int n = downloadTask.getId();
        ListenerModel listenerModel2 = null;
        synchronized (this) {
            listenerModel = listenerModel2;
            if (this.singleTaskModel != null) {
                listenerModel = listenerModel2;
                if (this.singleTaskModel.getId() == n) {
                    listenerModel = (ListenerModel)this.singleTaskModel;
                }
            }
        }
        listenerModel2 = listenerModel;
        if (listenerModel == null) {
            listenerModel2 = (ListenerModel)this.modelList.get(n);
        }
        if (listenerModel2 != null) return (T)listenerModel2;
        if (this.isAlwaysRecoverAssistModel()) return this.addAndGetModel(downloadTask, breakpointInfo);
        return (T)listenerModel2;
    }

    @Override
    public boolean isAlwaysRecoverAssistModel() {
        Boolean bl = this.alwaysRecoverModel;
        boolean bl2 = bl != null && bl != false;
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    T removeOrCreate(DownloadTask object, BreakpointInfo breakpointInfo) {
        int n = ((DownloadTask)object).getId();
        synchronized (this) {
            if (this.singleTaskModel != null && this.singleTaskModel.getId() == n) {
                object = this.singleTaskModel;
                this.singleTaskModel = null;
            } else {
                object = (ListenerModel)this.modelList.get(n);
                this.modelList.remove(n);
            }
        }
        Object object2 = object;
        if (object == null) {
            object2 = object = this.creator.create(n);
            if (breakpointInfo != null) {
                object.onInfoValid(breakpointInfo);
                object2 = object;
            }
        }
        return (T)object2;
    }

    @Override
    public void setAlwaysRecoverAssistModel(boolean bl) {
        this.alwaysRecoverModel = bl;
    }

    @Override
    public void setAlwaysRecoverAssistModelIfNotSet(boolean bl) {
        if (this.alwaysRecoverModel == null) {
            this.alwaysRecoverModel = bl;
        }
    }

    static interface ListenerModel {
        public int getId();

        public void onInfoValid(BreakpointInfo var1);
    }

    public static interface ModelCreator<T extends ListenerModel> {
        public T create(int var1);
    }
}

