/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 */
package com.liulishuo.filedownloader.notification;

import android.util.SparseArray;
import com.liulishuo.filedownloader.notification.BaseNotificationItem;

public class FileDownloadNotificationHelper<T extends BaseNotificationItem> {
    private final SparseArray<T> notificationArray = new SparseArray();

    public void add(T t) {
        this.notificationArray.remove(((BaseNotificationItem)t).getId());
        this.notificationArray.put(((BaseNotificationItem)t).getId(), t);
    }

    public void cancel(int n) {
        T t = this.remove(n);
        if (t == null) {
            return;
        }
        ((BaseNotificationItem)t).cancel();
    }

    public void clear() {
        SparseArray sparseArray = this.notificationArray.clone();
        this.notificationArray.clear();
        for (int i = 0; i < sparseArray.size(); ++i) {
            ((BaseNotificationItem)sparseArray.get(sparseArray.keyAt(i))).cancel();
        }
    }

    public boolean contains(int n) {
        boolean bl = this.get(n) != null;
        return bl;
    }

    public T get(int n) {
        return (T)((BaseNotificationItem)this.notificationArray.get(n));
    }

    public T remove(int n) {
        T t = this.get(n);
        if (t != null) {
            this.notificationArray.remove(n);
            return t;
        }
        return null;
    }

    public void showIndeterminate(int n, int n2) {
        T t = this.get(n);
        if (t == null) {
            return;
        }
        ((BaseNotificationItem)t).updateStatus(n2);
        ((BaseNotificationItem)t).show(false);
    }

    public void showProgress(int n, int n2, int n3) {
        T t = this.get(n);
        if (t == null) {
            return;
        }
        ((BaseNotificationItem)t).updateStatus(3);
        ((BaseNotificationItem)t).update(n2, n3);
    }
}

