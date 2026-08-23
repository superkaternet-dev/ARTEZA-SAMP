/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.connection.HostInfo;
import com.google.firebase.database.connection.PersistentConnection;
import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.EventTarget;
import com.google.firebase.database.core.RunLoop;
import com.google.firebase.database.core.persistence.PersistenceManager;
import com.google.firebase.database.logging.Logger;
import java.io.File;
import java.util.List;

public interface Platform {
    public PersistenceManager createPersistenceManager(Context var1, String var2);

    public String getPlatformVersion();

    public File getSSLCacheDirectory();

    public String getUserAgent(Context var1);

    public EventTarget newEventTarget(Context var1);

    public Logger newLogger(Context var1, Logger.Level var2, List<String> var3);

    public PersistentConnection newPersistentConnection(Context var1, ConnectionContext var2, HostInfo var3, PersistentConnection.Delegate var4);

    public RunLoop newRunLoop(Context var1);
}

