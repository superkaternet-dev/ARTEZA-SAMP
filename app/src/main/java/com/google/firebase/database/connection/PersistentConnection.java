/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.connection.ListenHashProvider;
import com.google.firebase.database.connection.RangeMerge;
import com.google.firebase.database.connection.RequestResultCallback;
import java.util.List;
import java.util.Map;

public interface PersistentConnection {
    public void compareAndPut(List<String> var1, Object var2, String var3, RequestResultCallback var4);

    public Task<Object> get(List<String> var1, Map<String, Object> var2);

    public void initialize();

    public void interrupt(String var1);

    public boolean isInterrupted(String var1);

    public void listen(List<String> var1, Map<String, Object> var2, ListenHashProvider var3, Long var4, RequestResultCallback var5);

    public void merge(List<String> var1, Map<String, Object> var2, RequestResultCallback var3);

    public void onDisconnectCancel(List<String> var1, RequestResultCallback var2);

    public void onDisconnectMerge(List<String> var1, Map<String, Object> var2, RequestResultCallback var3);

    public void onDisconnectPut(List<String> var1, Object var2, RequestResultCallback var3);

    public void purgeOutstandingWrites();

    public void put(List<String> var1, Object var2, RequestResultCallback var3);

    public void refreshAppCheckToken();

    public void refreshAppCheckToken(String var1);

    public void refreshAuthToken();

    public void refreshAuthToken(String var1);

    public void resume(String var1);

    public void shutdown();

    public void unlisten(List<String> var1, Map<String, Object> var2);

    public static interface Delegate {
        public void onConnect();

        public void onConnectionStatus(boolean var1);

        public void onDataUpdate(List<String> var1, Object var2, boolean var3, Long var4);

        public void onDisconnect();

        public void onRangeMergeUpdate(List<String> var1, List<RangeMerge> var2, Long var3);

        public void onServerInfoUpdate(Map<String, Object> var1);
    }
}

