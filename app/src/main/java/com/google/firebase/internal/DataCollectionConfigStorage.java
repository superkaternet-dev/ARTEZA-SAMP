/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.pm.PackageManager
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.os.Build$VERSION
 */
package com.google.firebase.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.content.ContextCompat;
import com.google.firebase.DataCollectionDefaultChange;
import com.google.firebase.events.Event;
import com.google.firebase.events.Publisher;

public class DataCollectionConfigStorage {
    public static final String DATA_COLLECTION_DEFAULT_ENABLED = "firebase_data_collection_default_enabled";
    private static final String FIREBASE_APP_PREFS = "com.google.firebase.common.prefs:";
    private boolean dataCollectionDefaultEnabled;
    private final Context deviceProtectedContext;
    private final Publisher publisher;
    private final SharedPreferences sharedPreferences;

    public DataCollectionConfigStorage(Context object, String string2, Publisher publisher) {
        Context context;
        this.deviceProtectedContext = context = DataCollectionConfigStorage.directBootSafe((Context)object);
        object = new StringBuilder();
        ((StringBuilder)object).append(FIREBASE_APP_PREFS);
        ((StringBuilder)object).append(string2);
        this.sharedPreferences = context.getSharedPreferences(((StringBuilder)object).toString(), 0);
        this.publisher = publisher;
        this.dataCollectionDefaultEnabled = this.readAutoDataCollectionEnabled();
    }

    private static Context directBootSafe(Context context) {
        if (Build.VERSION.SDK_INT < 24) {
            return context;
        }
        return ContextCompat.createDeviceProtectedStorageContext(context);
    }

    private boolean readAutoDataCollectionEnabled() {
        if (this.sharedPreferences.contains(DATA_COLLECTION_DEFAULT_ENABLED)) {
            return this.sharedPreferences.getBoolean(DATA_COLLECTION_DEFAULT_ENABLED, true);
        }
        return this.readManifestDataCollectionEnabled();
    }

    private boolean readManifestDataCollectionEnabled() {
        block5: {
            PackageManager packageManager = this.deviceProtectedContext.getPackageManager();
            if (packageManager == null) break block5;
            packageManager = packageManager.getApplicationInfo(this.deviceProtectedContext.getPackageName(), 128);
            if (packageManager == null) break block5;
            try {
                if (packageManager.metaData != null && packageManager.metaData.containsKey(DATA_COLLECTION_DEFAULT_ENABLED)) {
                    boolean bl = packageManager.metaData.getBoolean(DATA_COLLECTION_DEFAULT_ENABLED);
                    return bl;
                }
            }
            catch (PackageManager.NameNotFoundException nameNotFoundException) {
                // empty catch block
            }
        }
        return true;
    }

    private void updateDataCollectionDefaultEnabled(boolean bl) {
        synchronized (this) {
            if (this.dataCollectionDefaultEnabled != bl) {
                this.dataCollectionDefaultEnabled = bl;
                Publisher publisher = this.publisher;
                DataCollectionDefaultChange dataCollectionDefaultChange = new DataCollectionDefaultChange(bl);
                Event<DataCollectionDefaultChange> event = new Event<DataCollectionDefaultChange>(DataCollectionDefaultChange.class, dataCollectionDefaultChange);
                publisher.publish(event);
            }
            return;
        }
    }

    public boolean isEnabled() {
        synchronized (this) {
            boolean bl = this.dataCollectionDefaultEnabled;
            return bl;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setEnabled(Boolean bl) {
        synchronized (this) {
            if (bl == null) {
                this.sharedPreferences.edit().remove(DATA_COLLECTION_DEFAULT_ENABLED).apply();
                this.updateDataCollectionDefaultEnabled(this.readManifestDataCollectionEnabled());
            } else {
                boolean bl2 = Boolean.TRUE.equals(bl);
                this.sharedPreferences.edit().putBoolean(DATA_COLLECTION_DEFAULT_ENABLED, bl2).apply();
                this.updateDataCollectionDefaultEnabled(bl2);
            }
            return;
        }
    }
}

