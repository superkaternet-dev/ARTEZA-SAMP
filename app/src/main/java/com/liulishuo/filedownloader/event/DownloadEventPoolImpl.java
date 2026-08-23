/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.event;

import com.liulishuo.filedownloader.event.IDownloadEvent;
import com.liulishuo.filedownloader.event.IDownloadEventPool;
import com.liulishuo.filedownloader.event.IDownloadListener;
import com.liulishuo.filedownloader.util.FileDownloadExecutors;
import com.liulishuo.filedownloader.util.FileDownloadLog;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executor;

public class DownloadEventPoolImpl
implements IDownloadEventPool {
    private final HashMap<String, LinkedList<IDownloadListener>> listenersMap;
    private final Executor threadPool = FileDownloadExecutors.newDefaultThreadPool(10, "EventPool");

    public DownloadEventPoolImpl() {
        this.listenersMap = new HashMap();
    }

    private void trigger(LinkedList<IDownloadListener> object, IDownloadEvent iDownloadEvent) {
        Object[] objectArray = ((LinkedList)object).toArray();
        int n = objectArray.length;
        for (int i = 0; !(i >= n || (object = objectArray[i]) != null && ((IDownloadListener)object).callback(iDownloadEvent)); ++i) {
        }
        if (iDownloadEvent.callback != null) {
            iDownloadEvent.callback.run();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean addListener(String string2, IDownloadListener iDownloadListener) {
        LinkedList<IDownloadListener> linkedList;
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "setListener %s", string2);
        }
        if (iDownloadListener == null) {
            throw new IllegalArgumentException("listener must not be null!");
        }
        LinkedList<IDownloadListener> linkedList2 = linkedList = this.listenersMap.get(string2);
        if (linkedList == null) {
            String string3 = string2.intern();
            synchronized (string3) {
                linkedList2 = linkedList = this.listenersMap.get(string2);
                if (linkedList == null) {
                    HashMap<String, LinkedList<IDownloadListener>> hashMap = this.listenersMap;
                    linkedList2 = linkedList = new LinkedList<IDownloadListener>();
                    hashMap.put(string2, linkedList);
                }
            }
        }
        string2 = string2.intern();
        synchronized (string2) {
            return linkedList2.add(iDownloadListener);
        }
    }

    @Override
    public void asyncPublishInNewThread(IDownloadEvent iDownloadEvent) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "asyncPublishInNewThread %s", iDownloadEvent.getId());
        }
        if (iDownloadEvent != null) {
            this.threadPool.execute(new Runnable(this, iDownloadEvent){
                final DownloadEventPoolImpl this$0;
                final IDownloadEvent val$event;
                {
                    this.this$0 = downloadEventPoolImpl;
                    this.val$event = iDownloadEvent;
                }

                @Override
                public void run() {
                    this.this$0.publish(this.val$event);
                }
            });
            return;
        }
        throw new IllegalArgumentException("event must not be null!");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean publish(IDownloadEvent iDownloadEvent) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "publish %s", iDownloadEvent.getId());
        }
        if (iDownloadEvent == null) {
            throw new IllegalArgumentException("event must not be null!");
        }
        String string2 = iDownloadEvent.getId();
        Object object = this.listenersMap.get(string2);
        LinkedList<IDownloadListener> linkedList = object;
        if (object == null) {
            object = string2.intern();
            synchronized (object) {
                linkedList = this.listenersMap.get(string2);
                if (linkedList == null) {
                    if (FileDownloadLog.NEED_LOG) {
                        FileDownloadLog.d(this, "No listener for this event %s", string2);
                    }
                    return false;
                }
            }
        }
        this.trigger(linkedList, iDownloadEvent);
        return true;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean removeListener(String string2, IDownloadListener iDownloadListener) {
        if (FileDownloadLog.NEED_LOG) {
            FileDownloadLog.v(this, "removeListener %s", string2);
        }
        Object object = this.listenersMap.get(string2);
        LinkedList<IDownloadListener> linkedList = object;
        if (object == null) {
            object = string2.intern();
            synchronized (object) {
                linkedList = this.listenersMap.get(string2);
            }
        }
        if (linkedList == null) return false;
        if (iDownloadListener == null) {
            return false;
        }
        object = string2.intern();
        synchronized (object) {
            boolean bl = linkedList.remove(iDownloadListener);
            if (linkedList.size() > 0) return bl;
            this.listenersMap.remove(string2);
            return bl;
        }
    }
}

