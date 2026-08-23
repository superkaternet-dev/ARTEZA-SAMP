/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.ServiceConnection
 *  android.os.Binder
 *  android.os.Build$VERSION
 *  android.os.IBinder
 *  android.os.IInterface
 *  android.os.RemoteException
 */
package com.liulishuo.filedownloader.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import com.liulishuo.filedownloader.FileDownloadEventPool;
import com.liulishuo.filedownloader.IFileDownloadServiceProxy;
import com.liulishuo.filedownloader.event.DownloadServiceConnectChangedEvent;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class BaseFileServiceUIGuard<CALLBACK extends Binder, INTERFACE extends IInterface>
implements IFileDownloadServiceProxy,
ServiceConnection {
    private final List<Context> bindContexts;
    private final CALLBACK callback;
    private final ArrayList<Runnable> connectedRunnableList;
    protected boolean runServiceForeground = false;
    private volatile INTERFACE service;
    private final Class<?> serviceClass;
    private final HashMap<String, Object> uiCacheMap = new HashMap();

    protected BaseFileServiceUIGuard(Class<?> clazz) {
        this.bindContexts = new ArrayList<Context>();
        this.connectedRunnableList = new ArrayList();
        this.serviceClass = clazz;
        this.callback = this.createCallback();
    }

    private void releaseConnect(boolean bl) {
        if (!bl && this.service != null) {
            try {
                this.unregisterCallback(this.service, this.callback);
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "release connect resources %s", this.service);
        }
        this.service = null;
        FileDownloadEventPool fileDownloadEventPool = FileDownloadEventPool.getImpl();
        DownloadServiceConnectChangedEvent.ConnectStatus connectStatus = bl ? DownloadServiceConnectChangedEvent.ConnectStatus.lost : DownloadServiceConnectChangedEvent.ConnectStatus.disconnected;
        fileDownloadEventPool.asyncPublishInNewThread(new DownloadServiceConnectChangedEvent(connectStatus, this.serviceClass));
    }

    protected abstract INTERFACE asInterface(IBinder var1);

    @Override
    public void bindStartByContext(Context context) {
        this.bindStartByContext(context, null);
    }

    @Override
    public void bindStartByContext(Context context, Runnable runnable) {
        if (!FileDownloadUtils.isDownloaderProcess(context)) {
            boolean bl;
            if (FileDownloadLog.NEED_LOG) {
                FileDownloadLog.d(this, "bindStartByContext %s", context.getClass().getSimpleName());
            }
            Intent intent = new Intent(context, this.serviceClass);
            if (runnable != null && !this.connectedRunnableList.contains(runnable)) {
                this.connectedRunnableList.add(runnable);
            }
            if (!this.bindContexts.contains(context)) {
                this.bindContexts.add(context);
            }
            this.runServiceForeground = bl = FileDownloadUtils.needMakeServiceForeground(context);
            intent.putExtra("is_foreground", bl);
            context.bindService(intent, (ServiceConnection)this, 1);
            if (this.runServiceForeground) {
                if (FileDownloadLog.NEED_LOG) {
                    FileDownloadLog.d(this, "start foreground service", new Object[0]);
                }
                if (Build.VERSION.SDK_INT >= 26) {
                    context.startForegroundService(intent);
                }
            } else {
                context.startService(intent);
            }
            return;
        }
        throw new IllegalStateException("Fatal-Exception: You can't bind the FileDownloadService in :filedownloader process.\n It's the invalid operation and is likely to cause unexpected problems.\n Maybe you want to use non-separate process mode for FileDownloader, More detail about non-separate mode, please move to wiki manually: https://github.com/lingochamp/FileDownloader/wiki/filedownloader.properties");
    }

    protected abstract CALLBACK createCallback();

    protected CALLBACK getCallback() {
        return this.callback;
    }

    protected INTERFACE getService() {
        return this.service;
    }

    @Override
    public boolean isConnected() {
        boolean bl = this.getService() != null;
        return bl;
    }

    @Override
    public boolean isRunServiceForeground() {
        return this.runServiceForeground;
    }

    public void onServiceConnected(ComponentName object, IBinder iBinder) {
        this.service = this.asInterface(iBinder);
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "onServiceConnected %s %s", object, this.service);
        }
        try {
            this.registerCallback(this.service, this.callback);
        }
        catch (RemoteException remoteException) {
            remoteException.printStackTrace();
        }
        object = (List)this.connectedRunnableList.clone();
        this.connectedRunnableList.clear();
        object = object.iterator();
        while (object.hasNext()) {
            ((Runnable)object.next()).run();
        }
        FileDownloadEventPool.getImpl().asyncPublishInNewThread(new DownloadServiceConnectChangedEvent(DownloadServiceConnectChangedEvent.ConnectStatus.connected, this.serviceClass));
    }

    public void onServiceDisconnected(ComponentName componentName) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "onServiceDisconnected %s %s", componentName, this.service);
        }
        this.releaseConnect(true);
    }

    protected Object popCache(String string2) {
        return this.uiCacheMap.remove(string2);
    }

    protected String putCache(Object object) {
        if (object == null) {
            return null;
        }
        String string2 = object.toString();
        this.uiCacheMap.put(string2, object);
        return string2;
    }

    protected abstract void registerCallback(INTERFACE var1, CALLBACK var2) throws RemoteException;

    @Override
    public void unbindByContext(Context context) {
        if (!this.bindContexts.contains(context)) {
            return;
        }
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.d(this, "unbindByContext %s", context);
        }
        this.bindContexts.remove(context);
        if (this.bindContexts.isEmpty()) {
            this.releaseConnect(false);
        }
        Intent intent = new Intent(context, this.serviceClass);
        context.unbindService((ServiceConnection)this);
        context.stopService(intent);
    }

    protected abstract void unregisterCallback(INTERFACE var1, CALLBACK var2) throws RemoteException;
}

