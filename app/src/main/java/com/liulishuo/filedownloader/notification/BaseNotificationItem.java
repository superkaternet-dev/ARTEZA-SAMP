/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.NotificationManager
 */
package com.liulishuo.filedownloader.notification;

import android.app.NotificationManager;
import com.liulishuo.filedownloader.util.FileDownloadHelper;

public abstract class BaseNotificationItem {
    private String desc;
    private int id;
    private int lastStatus = 0;
    private NotificationManager manager;
    private int sofar;
    private int status = 0;
    private String title;
    private int total;

    public BaseNotificationItem(int n, String string2, String string3) {
        this.id = n;
        this.title = string2;
        this.desc = string3;
    }

    public void cancel() {
        this.getManager().cancel(this.id);
    }

    public String getDesc() {
        return this.desc;
    }

    public int getId() {
        return this.id;
    }

    public int getLastStatus() {
        return this.lastStatus;
    }

    protected NotificationManager getManager() {
        if (this.manager == null) {
            this.manager = (NotificationManager)FileDownloadHelper.getAppContext().getSystemService("notification");
        }
        return this.manager;
    }

    public int getSofar() {
        return this.sofar;
    }

    public int getStatus() {
        int n;
        this.lastStatus = n = this.status;
        return n;
    }

    public String getTitle() {
        return this.title;
    }

    public int getTotal() {
        return this.total;
    }

    public boolean isChanged() {
        boolean bl = this.lastStatus != this.status;
        return bl;
    }

    public void setDesc(String string2) {
        this.desc = string2;
    }

    public void setId(int n) {
        this.id = n;
    }

    public void setSofar(int n) {
        this.sofar = n;
    }

    public void setStatus(int n) {
        this.status = n;
    }

    public void setTitle(String string2) {
        this.title = string2;
    }

    public void setTotal(int n) {
        this.total = n;
    }

    public void show(boolean bl) {
        this.show(this.isChanged(), this.getStatus(), bl);
    }

    public abstract void show(boolean var1, int var2, boolean var3);

    public void update(int n, int n2) {
        this.sofar = n;
        this.total = n2;
        this.show(true);
    }

    public void updateStatus(int n) {
        this.status = n;
    }
}

