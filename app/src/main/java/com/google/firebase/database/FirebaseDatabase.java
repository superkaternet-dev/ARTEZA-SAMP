/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.google.firebase.database;

import android.text.TextUtils;
import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabaseComponent;
import com.google.firebase.database.Logger;
import com.google.firebase.database.core.DatabaseConfig;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.Repo;
import com.google.firebase.database.core.RepoInfo;
import com.google.firebase.database.core.RepoManager;
import com.google.firebase.database.core.utilities.ParsedUrl;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.utilities.Validation;
import com.google.firebase.emulators.EmulatedServiceSettings;

public class FirebaseDatabase {
    private static final String SDK_VERSION = "20.0.4";
    private final FirebaseApp app;
    private final DatabaseConfig config;
    private EmulatedServiceSettings emulatorSettings;
    private Repo repo;
    private final RepoInfo repoInfo;

    FirebaseDatabase(FirebaseApp firebaseApp, RepoInfo repoInfo, DatabaseConfig databaseConfig) {
        this.app = firebaseApp;
        this.repoInfo = repoInfo;
        this.config = databaseConfig;
    }

    private void assertUnfrozen(String string2) {
        if (this.repo == null) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Calls to ");
        stringBuilder.append(string2);
        stringBuilder.append("() must be made before any other usage of FirebaseDatabase instance.");
        throw new DatabaseException(stringBuilder.toString());
    }

    static FirebaseDatabase createForTests(FirebaseApp object, RepoInfo repoInfo, DatabaseConfig databaseConfig) {
        object = new FirebaseDatabase((FirebaseApp)object, repoInfo, databaseConfig);
        super.ensureRepo();
        return object;
    }

    private void ensureRepo() {
        synchronized (this) {
            if (this.repo == null) {
                this.repoInfo.applyEmulatorSettings(this.emulatorSettings);
                this.repo = RepoManager.createRepo(this.config, this.repoInfo, this);
            }
            return;
        }
    }

    public static FirebaseDatabase getInstance() {
        FirebaseApp firebaseApp = FirebaseApp.getInstance();
        if (firebaseApp != null) {
            return FirebaseDatabase.getInstance(firebaseApp);
        }
        throw new DatabaseException("You must call FirebaseApp.initialize() first.");
    }

    public static FirebaseDatabase getInstance(FirebaseApp firebaseApp) {
        String string2 = firebaseApp.getOptions().getDatabaseUrl();
        CharSequence charSequence = string2;
        if (string2 == null) {
            if (firebaseApp.getOptions().getProjectId() != null) {
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("https://");
                ((StringBuilder)charSequence).append(firebaseApp.getOptions().getProjectId());
                ((StringBuilder)charSequence).append("-default-rtdb.firebaseio.com");
                charSequence = ((StringBuilder)charSequence).toString();
            } else {
                throw new DatabaseException("Failed to get FirebaseDatabase instance: Can't determine Firebase Database URL. Be sure to include a Project ID in your configuration.");
            }
        }
        return FirebaseDatabase.getInstance(firebaseApp, (String)charSequence);
    }

    public static FirebaseDatabase getInstance(FirebaseApp object, String string2) {
        synchronized (FirebaseDatabase.class) {
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                Preconditions.checkNotNull(object, "Provided FirebaseApp must not be null.");
                Object object2 = ((FirebaseApp)object).get(FirebaseDatabaseComponent.class);
                Preconditions.checkNotNull(object2, "Firebase Database component is not present.");
                object = Utilities.parseUrl(string2);
                if (((ParsedUrl)object).path.isEmpty()) {
                    object = ((FirebaseDatabaseComponent)object2).get(((ParsedUrl)object).repoInfo);
                    return object;
                }
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Specified Database URL '");
                ((StringBuilder)object2).append(string2);
                ((StringBuilder)object2).append("' is invalid. It should point to the root of a Firebase Database but it includes a path: ");
                ((StringBuilder)object2).append(((ParsedUrl)object).path.toString());
                DatabaseException databaseException = new DatabaseException(((StringBuilder)object2).toString());
                throw databaseException;
            }
            object = new DatabaseException("Failed to get FirebaseDatabase instance: Specify DatabaseURL within FirebaseApp or from your getInstance() call.");
            throw object;
        }
    }

    public static FirebaseDatabase getInstance(String string2) {
        FirebaseApp firebaseApp = FirebaseApp.getInstance();
        if (firebaseApp != null) {
            return FirebaseDatabase.getInstance(firebaseApp, string2);
        }
        throw new DatabaseException("You must call FirebaseApp.initialize() first.");
    }

    public static String getSdkVersion() {
        return SDK_VERSION;
    }

    public FirebaseApp getApp() {
        return this.app;
    }

    DatabaseConfig getConfig() {
        return this.config;
    }

    public DatabaseReference getReference() {
        this.ensureRepo();
        return new DatabaseReference(this.repo, Path.getEmptyPath());
    }

    public DatabaseReference getReference(String object) {
        this.ensureRepo();
        if (object != null) {
            Validation.validateRootPathString((String)object);
            object = new Path((String)object);
            return new DatabaseReference(this.repo, (Path)object);
        }
        throw new NullPointerException("Can't pass null for argument 'pathString' in FirebaseDatabase.getReference()");
    }

    public DatabaseReference getReferenceFromUrl(String string2) {
        this.ensureRepo();
        if (string2 != null) {
            Object object = Utilities.parseUrl(string2);
            ((ParsedUrl)object).repoInfo.applyEmulatorSettings(this.emulatorSettings);
            if (((ParsedUrl)object).repoInfo.host.equals(this.repo.getRepoInfo().host)) {
                return new DatabaseReference(this.repo, ((ParsedUrl)object).path);
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Invalid URL (");
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(") passed to getReference().  URL was expected to match configured Database URL: ");
            ((StringBuilder)object).append(this.getReference().toString());
            throw new DatabaseException(((StringBuilder)object).toString());
        }
        throw new NullPointerException("Can't pass null for argument 'url' in FirebaseDatabase.getReferenceFromUrl()");
    }

    public void goOffline() {
        this.ensureRepo();
        RepoManager.interrupt(this.repo);
    }

    public void goOnline() {
        this.ensureRepo();
        RepoManager.resume(this.repo);
    }

    public void purgeOutstandingWrites() {
        this.ensureRepo();
        this.repo.scheduleNow(new Runnable(this){
            final FirebaseDatabase this$0;
            {
                this.this$0 = firebaseDatabase;
            }

            @Override
            public void run() {
                this.this$0.repo.purgeOutstandingWrites();
            }
        });
    }

    public void setLogLevel(Logger.Level level) {
        synchronized (this) {
            this.assertUnfrozen("setLogLevel");
            this.config.setLogLevel(level);
            return;
        }
    }

    public void setPersistenceCacheSizeBytes(long l) {
        synchronized (this) {
            this.assertUnfrozen("setPersistenceCacheSizeBytes");
            this.config.setPersistenceCacheSizeBytes(l);
            return;
        }
    }

    public void setPersistenceEnabled(boolean bl) {
        synchronized (this) {
            this.assertUnfrozen("setPersistenceEnabled");
            this.config.setPersistenceEnabled(bl);
            return;
        }
    }

    public void useEmulator(String string2, int n) {
        if (this.repo == null) {
            this.emulatorSettings = new EmulatedServiceSettings(string2, n);
            return;
        }
        throw new IllegalStateException("Cannot call useEmulator() after instance has already been initialized.");
    }
}

