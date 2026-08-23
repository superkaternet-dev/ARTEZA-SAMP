/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database;

import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.interop.InternalAppCheckTokenProvider;
import com.google.firebase.auth.internal.InternalAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.android.AndroidAppCheckTokenProvider;
import com.google.firebase.database.android.AndroidAuthTokenProvider;
import com.google.firebase.database.core.DatabaseConfig;
import com.google.firebase.database.core.RepoInfo;
import com.google.firebase.database.core.TokenProvider;
import com.google.firebase.inject.Deferred;
import java.util.HashMap;
import java.util.Map;

class FirebaseDatabaseComponent {
    private final FirebaseApp app;
    private final TokenProvider appCheckProvider;
    private final TokenProvider authProvider;
    private final Map<RepoInfo, FirebaseDatabase> instances = new HashMap<RepoInfo, FirebaseDatabase>();

    FirebaseDatabaseComponent(FirebaseApp firebaseApp, Deferred<InternalAuthProvider> deferred, Deferred<InternalAppCheckTokenProvider> deferred2) {
        this.app = firebaseApp;
        this.authProvider = new AndroidAuthTokenProvider(deferred);
        this.appCheckProvider = new AndroidAppCheckTokenProvider(deferred2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    FirebaseDatabase get(RepoInfo repoInfo) {
        synchronized (this) {
            Object object = this.instances.get(repoInfo);
            FirebaseDatabase firebaseDatabase = object;
            if (object == null) {
                object = new DatabaseConfig();
                if (!this.app.isDefaultApp()) {
                    ((DatabaseConfig)object).setSessionPersistenceKey(this.app.getName());
                }
                ((DatabaseConfig)object).setFirebaseApp(this.app);
                ((DatabaseConfig)object).setAuthTokenProvider(this.authProvider);
                ((DatabaseConfig)object).setAppCheckTokenProvider(this.appCheckProvider);
                firebaseDatabase = new FirebaseDatabase(this.app, repoInfo, (DatabaseConfig)object);
                this.instances.put(repoInfo, firebaseDatabase);
            }
            return firebaseDatabase;
        }
    }
}

