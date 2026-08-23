/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.Logger;
import com.google.firebase.database.core.Context;
import com.google.firebase.database.core.EventTarget;
import com.google.firebase.database.core.RunLoop;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.database.logging.Logger;
import java.util.List;

public class DatabaseConfig
extends Context {
    public void setAppCheckTokenProvider(TokenProvider tokenProvider) {
        this.appCheckTokenProvider = tokenProvider;
    }

    public void setAuthTokenProvider(TokenProvider tokenProvider) {
        this.authTokenProvider = tokenProvider;
    }

    public void setDebugLogComponents(List<String> list) {
        synchronized (this) {
            this.assertUnfrozen();
            this.setLogLevel(Logger.Level.DEBUG);
            this.loggedComponents = list;
            return;
        }
    }

    public void setEventTarget(EventTarget eventTarget) {
        synchronized (this) {
            this.assertUnfrozen();
            this.eventTarget = eventTarget;
            return;
        }
    }

    public void setFirebaseApp(FirebaseApp firebaseApp) {
        synchronized (this) {
            this.firebaseApp = firebaseApp;
            return;
        }
    }

    public void setLogLevel(Logger.Level level) {
        synchronized (this) {
            block10: {
                this.assertUnfrozen();
                switch (1.$SwitchMap$com$google$firebase$database$Logger$Level[level.ordinal()]) {
                    default: {
                        break block10;
                    }
                    case 5: {
                        this.logLevel = Logger.Level.NONE;
                        break;
                    }
                    case 4: {
                        this.logLevel = Logger.Level.ERROR;
                        break;
                    }
                    case 3: {
                        this.logLevel = Logger.Level.WARN;
                        break;
                    }
                    case 2: {
                        this.logLevel = Logger.Level.INFO;
                        break;
                    }
                    case 1: {
                        this.logLevel = Logger.Level.DEBUG;
                    }
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unknown log level: ");
            stringBuilder.append((Object)level);
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException(stringBuilder.toString());
            throw illegalArgumentException;
        }
    }

    public void setLogger(Logger logger) {
        synchronized (this) {
            this.assertUnfrozen();
            this.logger = logger;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setPersistenceCacheSizeBytes(long l) {
        synchronized (this) {
            this.assertUnfrozen();
            if (l < 0x100000L) {
                DatabaseException databaseException = new DatabaseException("The minimum cache size must be at least 1MB");
                throw databaseException;
            }
            if (l <= 0x6400000L) {
                this.cacheSize = l;
                return;
            }
            DatabaseException databaseException = new DatabaseException("Firebase Database currently doesn't support a cache size larger than 100MB");
            throw databaseException;
        }
    }

    public void setPersistenceEnabled(boolean bl) {
        synchronized (this) {
            this.assertUnfrozen();
            this.persistenceEnabled = bl;
            return;
        }
    }

    public void setRunLoop(RunLoop runLoop) {
        this.runLoop = runLoop;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setSessionPersistenceKey(String object) {
        synchronized (this) {
            this.assertUnfrozen();
            if (object != null && !((String)object).isEmpty()) {
                this.persistenceKey = object;
                return;
            }
            object = new IllegalArgumentException("Session identifier is not allowed to be empty or null!");
            throw object;
        }
    }
}

