/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build$VERSION
 *  android.os.Handler
 *  android.util.Log
 */
package com.google.firebase.database.android;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.android.AndroidEventTarget;
import com.google.firebase.database.android.SqlPersistenceStorageEngine;
import com.google.firebase.database.connection.ConnectionContext;
import com.google.firebase.database.connection.HostInfo;
import com.google.firebase.database.connection.PersistentConnection;
import com.google.firebase.database.connection.PersistentConnectionImpl;
import com.google.firebase.database.core.EventTarget;
import com.google.firebase.database.core.Platform;
import com.google.firebase.database.core.RunLoop;
import com.google.firebase.database.core.persistence.DefaultPersistenceManager;
import com.google.firebase.database.core.persistence.LRUCachePolicy;
import com.google.firebase.database.core.persistence.PersistenceManager;
import com.google.firebase.database.core.utilities.DefaultRunLoop;
import com.google.firebase.database.logging.AndroidLogger;
import com.google.firebase.database.logging.LogWrapper;
import com.google.firebase.database.logging.Logger;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AndroidPlatform
implements Platform {
    private static final String APP_IN_BACKGROUND_INTERRUPT_REASON = "app_in_background";
    private final Context applicationContext;
    private final Set<String> createdPersistenceCaches = new HashSet<String>();
    private final FirebaseApp firebaseApp;

    public AndroidPlatform(FirebaseApp firebaseApp) {
        this.firebaseApp = firebaseApp;
        if (firebaseApp != null) {
            this.applicationContext = firebaseApp.getApplicationContext();
            return;
        }
        Log.e((String)"FirebaseDatabase", (String)"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        Log.e((String)"FirebaseDatabase", (String)"ERROR: You must call FirebaseApp.initializeApp() before using Firebase Database.");
        Log.e((String)"FirebaseDatabase", (String)"!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        throw new RuntimeException("You need to call FirebaseApp.initializeApp() before using Firebase Database.");
    }

    @Override
    public PersistenceManager createPersistenceManager(com.google.firebase.database.core.Context object, String string2) {
        String string3 = ((com.google.firebase.database.core.Context)object).getSessionPersistenceKey();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append("_");
        stringBuilder.append(string3);
        string2 = stringBuilder.toString();
        if (!this.createdPersistenceCaches.contains(string2)) {
            this.createdPersistenceCaches.add(string2);
            return new DefaultPersistenceManager((com.google.firebase.database.core.Context)object, new SqlPersistenceStorageEngine(this.applicationContext, (com.google.firebase.database.core.Context)object, string2), new LRUCachePolicy(((com.google.firebase.database.core.Context)object).getPersistenceCacheSizeBytes()));
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("SessionPersistenceKey '");
        ((StringBuilder)object).append(string3);
        ((StringBuilder)object).append("' has already been used.");
        throw new DatabaseException(((StringBuilder)object).toString());
    }

    @Override
    public String getPlatformVersion() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("android-");
        stringBuilder.append(FirebaseDatabase.getSdkVersion());
        return stringBuilder.toString();
    }

    @Override
    public File getSSLCacheDirectory() {
        return this.applicationContext.getApplicationContext().getDir("sslcache", 0);
    }

    @Override
    public String getUserAgent(com.google.firebase.database.core.Context object) {
        object = new StringBuilder();
        ((StringBuilder)object).append(Build.VERSION.SDK_INT);
        ((StringBuilder)object).append("/Android");
        return ((StringBuilder)object).toString();
    }

    @Override
    public EventTarget newEventTarget(com.google.firebase.database.core.Context context) {
        return new AndroidEventTarget();
    }

    @Override
    public Logger newLogger(com.google.firebase.database.core.Context context, Logger.Level level, List<String> list) {
        return new AndroidLogger(level, list);
    }

    @Override
    public PersistentConnection newPersistentConnection(com.google.firebase.database.core.Context object, ConnectionContext connectionContext, HostInfo hostInfo, PersistentConnection.Delegate delegate) {
        object = new PersistentConnectionImpl(connectionContext, hostInfo, delegate);
        this.firebaseApp.addBackgroundStateChangeListener(new FirebaseApp.BackgroundStateChangeListener(this, (PersistentConnection)object){
            final AndroidPlatform this$0;
            final PersistentConnection val$connection;
            {
                this.this$0 = androidPlatform;
                this.val$connection = persistentConnection;
            }

            @Override
            public void onBackgroundStateChanged(boolean bl) {
                if (bl) {
                    this.val$connection.interrupt(AndroidPlatform.APP_IN_BACKGROUND_INTERRUPT_REASON);
                } else {
                    this.val$connection.resume(AndroidPlatform.APP_IN_BACKGROUND_INTERRUPT_REASON);
                }
            }
        });
        return object;
    }

    @Override
    public RunLoop newRunLoop(com.google.firebase.database.core.Context context) {
        return new DefaultRunLoop(this, context.getLogger("RunLoop")){
            final AndroidPlatform this$0;
            final LogWrapper val$logger;
            {
                this.this$0 = androidPlatform;
                this.val$logger = logWrapper;
            }

            @Override
            public void handleException(Throwable throwable) {
                String string2 = DefaultRunLoop.messageForException(throwable);
                this.val$logger.error(string2, throwable);
                new Handler(this.this$0.applicationContext.getMainLooper()).post(new Runnable(this, string2, throwable){
                    final 1 this$1;
                    final Throwable val$e;
                    final String val$message;
                    {
                        this.this$1 = var1_1;
                        this.val$message = string2;
                        this.val$e = throwable;
                    }

                    @Override
                    public void run() {
                        throw new RuntimeException(this.val$message, this.val$e);
                    }
                });
                this.getExecutorService().shutdownNow();
            }
        };
    }
}

