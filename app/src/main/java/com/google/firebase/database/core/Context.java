/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.android.AndroidPlatform;
import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.connection.ConnectionTokenProvider;
import com.google.firebase.database.connection.HostInfo;
import com.google.firebase.database.connection.PersistentConnection;
import com.google.firebase.database.core.Context$$ExternalSyntheticLambda0;
import com.google.firebase.database.core.Context$1$$ExternalSyntheticLambda0;
import com.google.firebase.database.core.Context$1$$ExternalSyntheticLambda1;
import com.google.firebase.database.core.EventTarget;
import com.google.firebase.database.core.Platform;
import com.google.firebase.database.core.RunLoop;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.database.core.persistence.NoopPersistenceManager;
import com.google.firebase.database.core.persistence.PersistenceManager;
import com.google.firebase.database.core.utilities.DefaultRunLoop;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.logging.Logger;
import java.io.File;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class Context {
    private static final long DEFAULT_CACHE_SIZE = 0xA00000L;
    protected TokenProvider appCheckTokenProvider;
    protected TokenProvider authTokenProvider;
    protected long cacheSize = 0xA00000L;
    protected EventTarget eventTarget;
    protected FirebaseApp firebaseApp;
    private PersistenceManager forcedPersistenceManager;
    private boolean frozen = false;
    protected Logger.Level logLevel = Logger.Level.INFO;
    protected List<String> loggedComponents;
    protected Logger logger;
    protected boolean persistenceEnabled;
    protected String persistenceKey;
    private Platform platform;
    protected RunLoop runLoop;
    private boolean stopped = false;
    protected String userAgent;

    private String buildUserAgent(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Firebase/");
        stringBuilder.append("5");
        stringBuilder.append("/");
        stringBuilder.append(FirebaseDatabase.getSdkVersion());
        stringBuilder.append("/");
        return stringBuilder.append(string2).toString();
    }

    private void ensureAppTokenProvider() {
        Preconditions.checkNotNull(this.appCheckTokenProvider, "You must register an appCheckTokenProvider before initializing Context.");
    }

    private void ensureAuthTokenProvider() {
        Preconditions.checkNotNull(this.authTokenProvider, "You must register an authTokenProvider before initializing Context.");
    }

    private void ensureEventTarget() {
        if (this.eventTarget == null) {
            this.eventTarget = this.getPlatform().newEventTarget(this);
        }
    }

    private void ensureLogger() {
        if (this.logger == null) {
            this.logger = this.getPlatform().newLogger(this, this.logLevel, this.loggedComponents);
        }
    }

    private void ensureRunLoop() {
        if (this.runLoop == null) {
            this.runLoop = this.platform.newRunLoop(this);
        }
    }

    private void ensureSessionIdentifier() {
        if (this.persistenceKey == null) {
            this.persistenceKey = "default";
        }
    }

    private void ensureUserAgent() {
        if (this.userAgent == null) {
            this.userAgent = this.buildUserAgent(this.getPlatform().getUserAgent(this));
        }
    }

    private ScheduledExecutorService getExecutorService() {
        RunLoop runLoop = this.getRunLoop();
        if (runLoop instanceof DefaultRunLoop) {
            return ((DefaultRunLoop)runLoop).getExecutorService();
        }
        throw new RuntimeException("Custom run loops are not supported!");
    }

    private Platform getPlatform() {
        if (this.platform == null) {
            this.initializeAndroidPlatform();
        }
        return this.platform;
    }

    private void initServices() {
        this.ensureLogger();
        this.getPlatform();
        this.ensureUserAgent();
        this.ensureEventTarget();
        this.ensureRunLoop();
        this.ensureSessionIdentifier();
        this.ensureAuthTokenProvider();
        this.ensureAppTokenProvider();
    }

    private void initializeAndroidPlatform() {
        synchronized (this) {
            AndroidPlatform androidPlatform = new AndroidPlatform(this.firebaseApp);
            this.platform = androidPlatform;
            return;
        }
    }

    static /* synthetic */ void lambda$wrapTokenProvider$0(TokenProvider tokenProvider, ScheduledExecutorService scheduledExecutorService, boolean bl, ConnectionTokenProvider.GetTokenCallback getTokenCallback) {
        tokenProvider.getToken(bl, new TokenProvider.GetTokenCompletionListener(scheduledExecutorService, getTokenCallback){
            final ConnectionTokenProvider.GetTokenCallback val$callback;
            final ScheduledExecutorService val$executorService;
            {
                this.val$executorService = scheduledExecutorService;
                this.val$callback = getTokenCallback;
            }

            static /* synthetic */ void lambda$onError$1(ConnectionTokenProvider.GetTokenCallback getTokenCallback, String string2) {
                getTokenCallback.onError(string2);
            }

            static /* synthetic */ void lambda$onSuccess$0(ConnectionTokenProvider.GetTokenCallback getTokenCallback, String string2) {
                getTokenCallback.onSuccess(string2);
            }

            @Override
            public void onError(String string2) {
                this.val$executorService.execute(new Context$1$$ExternalSyntheticLambda0(this.val$callback, string2));
            }

            @Override
            public void onSuccess(String string2) {
                this.val$executorService.execute(new Context$1$$ExternalSyntheticLambda1(this.val$callback, string2));
            }
        });
    }

    private void restartServices() {
        this.eventTarget.restart();
        this.runLoop.restart();
    }

    private static ConnectionTokenProvider wrapTokenProvider(TokenProvider tokenProvider, ScheduledExecutorService scheduledExecutorService) {
        return new Context$$ExternalSyntheticLambda0(tokenProvider, scheduledExecutorService);
    }

    protected void assertUnfrozen() {
        if (!this.isFrozen()) {
            return;
        }
        throw new DatabaseException("Modifications to DatabaseConfig objects must occur before they are in use");
    }

    void forcePersistenceManager(PersistenceManager persistenceManager) {
        this.forcedPersistenceManager = persistenceManager;
    }

    void freeze() {
        synchronized (this) {
            if (!this.frozen) {
                this.frozen = true;
                this.initServices();
            }
            return;
        }
    }

    public TokenProvider getAppCheckTokenProvider() {
        return this.appCheckTokenProvider;
    }

    public TokenProvider getAuthTokenProvider() {
        return this.authTokenProvider;
    }

    public ConnectionContext getConnectionContext() {
        return new ConnectionContext(this.getLogger(), Context.wrapTokenProvider(this.getAuthTokenProvider(), this.getExecutorService()), Context.wrapTokenProvider(this.getAppCheckTokenProvider(), this.getExecutorService()), this.getExecutorService(), this.isPersistenceEnabled(), FirebaseDatabase.getSdkVersion(), this.getUserAgent(), this.firebaseApp.getOptions().getApplicationId(), this.getSSLCacheDirectory().getAbsolutePath());
    }

    public EventTarget getEventTarget() {
        return this.eventTarget;
    }

    public Logger.Level getLogLevel() {
        return this.logLevel;
    }

    public LogWrapper getLogger(String string2) {
        return new LogWrapper(this.logger, string2);
    }

    public LogWrapper getLogger(String string2, String string3) {
        return new LogWrapper(this.logger, string2, string3);
    }

    public Logger getLogger() {
        return this.logger;
    }

    public List<String> getOptDebugLogComponents() {
        return this.loggedComponents;
    }

    public long getPersistenceCacheSizeBytes() {
        return this.cacheSize;
    }

    PersistenceManager getPersistenceManager(String object) {
        PersistenceManager persistenceManager = this.forcedPersistenceManager;
        if (persistenceManager != null) {
            return persistenceManager;
        }
        if (this.persistenceEnabled) {
            if ((object = this.platform.createPersistenceManager(this, (String)object)) != null) {
                return object;
            }
            throw new IllegalArgumentException("You have enabled persistence, but persistence is not supported on this platform.");
        }
        return new NoopPersistenceManager();
    }

    public String getPlatformVersion() {
        return this.getPlatform().getPlatformVersion();
    }

    public RunLoop getRunLoop() {
        return this.runLoop;
    }

    public File getSSLCacheDirectory() {
        return this.getPlatform().getSSLCacheDirectory();
    }

    public String getSessionPersistenceKey() {
        return this.persistenceKey;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    public boolean isPersistenceEnabled() {
        return this.persistenceEnabled;
    }

    public boolean isStopped() {
        return this.stopped;
    }

    public PersistentConnection newPersistentConnection(HostInfo hostInfo, PersistentConnection.Delegate delegate) {
        return this.getPlatform().newPersistentConnection(this, this.getConnectionContext(), hostInfo, delegate);
    }

    public void requireStarted() {
        if (this.stopped) {
            this.restartServices();
            this.stopped = false;
        }
    }

    void stop() {
        this.stopped = true;
        this.eventTarget.shutdown();
        this.runLoop.shutdown();
    }
}

