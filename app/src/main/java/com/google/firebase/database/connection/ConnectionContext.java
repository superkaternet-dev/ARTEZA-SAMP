/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

import com.google.firebase.database.connection.ConnectionTokenProvider;
import com.google.firebase.database.logging.Logger;
import java.util.concurrent.ScheduledExecutorService;

public class ConnectionContext {
    private final ConnectionTokenProvider appCheckTokenProvider;
    private final String applicationId;
    private final ConnectionTokenProvider authTokenProvider;
    private final String clientSdkVersion;
    private final ScheduledExecutorService executorService;
    private final Logger logger;
    private final boolean persistenceEnabled;
    private final String sslCacheDirectory;
    private final String userAgent;

    public ConnectionContext(Logger logger, ConnectionTokenProvider connectionTokenProvider, ConnectionTokenProvider connectionTokenProvider2, ScheduledExecutorService scheduledExecutorService, boolean bl, String string2, String string3, String string4, String string5) {
        this.logger = logger;
        this.authTokenProvider = connectionTokenProvider;
        this.appCheckTokenProvider = connectionTokenProvider2;
        this.executorService = scheduledExecutorService;
        this.persistenceEnabled = bl;
        this.clientSdkVersion = string2;
        this.userAgent = string3;
        this.applicationId = string4;
        this.sslCacheDirectory = string5;
    }

    public ConnectionTokenProvider getAppCheckTokenProvider() {
        return this.appCheckTokenProvider;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public ConnectionTokenProvider getAuthTokenProvider() {
        return this.authTokenProvider;
    }

    public String getClientSdkVersion() {
        return this.clientSdkVersion;
    }

    public ScheduledExecutorService getExecutorService() {
        return this.executorService;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public String getSslCacheDirectory() {
        return this.sslCacheDirectory;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public boolean isPersistenceEnabled() {
        return this.persistenceEnabled;
    }
}

