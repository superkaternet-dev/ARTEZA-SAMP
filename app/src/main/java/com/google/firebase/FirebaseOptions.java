/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 */
package com.google.firebase;

import android.content.Context;
import android.text.TextUtils;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.StringResourceValueReader;
import com.google.android.gms.common.util.Strings;

public final class FirebaseOptions {
    private static final String API_KEY_RESOURCE_NAME = "google_api_key";
    private static final String APP_ID_RESOURCE_NAME = "google_app_id";
    private static final String DATABASE_URL_RESOURCE_NAME = "firebase_database_url";
    private static final String GA_TRACKING_ID_RESOURCE_NAME = "ga_trackingId";
    private static final String GCM_SENDER_ID_RESOURCE_NAME = "gcm_defaultSenderId";
    private static final String PROJECT_ID_RESOURCE_NAME = "project_id";
    private static final String STORAGE_BUCKET_RESOURCE_NAME = "google_storage_bucket";
    private final String apiKey;
    private final String applicationId;
    private final String databaseUrl;
    private final String gaTrackingId;
    private final String gcmSenderId;
    private final String projectId;
    private final String storageBucket;

    private FirebaseOptions(String string2, String string3, String string4, String string5, String string6, String string7, String string8) {
        Preconditions.checkState(Strings.isEmptyOrWhitespace(string2) ^ true, "ApplicationId must be set.");
        this.applicationId = string2;
        this.apiKey = string3;
        this.databaseUrl = string4;
        this.gaTrackingId = string5;
        this.gcmSenderId = string6;
        this.storageBucket = string7;
        this.projectId = string8;
    }

    public static FirebaseOptions fromResource(Context object) {
        StringResourceValueReader stringResourceValueReader = new StringResourceValueReader((Context)object);
        if (TextUtils.isEmpty((CharSequence)(object = stringResourceValueReader.getString(APP_ID_RESOURCE_NAME)))) {
            return null;
        }
        return new FirebaseOptions((String)object, stringResourceValueReader.getString(API_KEY_RESOURCE_NAME), stringResourceValueReader.getString(DATABASE_URL_RESOURCE_NAME), stringResourceValueReader.getString(GA_TRACKING_ID_RESOURCE_NAME), stringResourceValueReader.getString(GCM_SENDER_ID_RESOURCE_NAME), stringResourceValueReader.getString(STORAGE_BUCKET_RESOURCE_NAME), stringResourceValueReader.getString(PROJECT_ID_RESOURCE_NAME));
    }

    public boolean equals(Object object) {
        boolean bl;
        block1: {
            boolean bl2 = object instanceof FirebaseOptions;
            bl = false;
            if (!bl2) {
                return false;
            }
            object = (FirebaseOptions)object;
            if (!Objects.equal(this.applicationId, ((FirebaseOptions)object).applicationId) || !Objects.equal(this.apiKey, ((FirebaseOptions)object).apiKey) || !Objects.equal(this.databaseUrl, ((FirebaseOptions)object).databaseUrl) || !Objects.equal(this.gaTrackingId, ((FirebaseOptions)object).gaTrackingId) || !Objects.equal(this.gcmSenderId, ((FirebaseOptions)object).gcmSenderId) || !Objects.equal(this.storageBucket, ((FirebaseOptions)object).storageBucket) || !Objects.equal(this.projectId, ((FirebaseOptions)object).projectId)) break block1;
            bl = true;
        }
        return bl;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public String getApplicationId() {
        return this.applicationId;
    }

    public String getDatabaseUrl() {
        return this.databaseUrl;
    }

    public String getGaTrackingId() {
        return this.gaTrackingId;
    }

    public String getGcmSenderId() {
        return this.gcmSenderId;
    }

    public String getProjectId() {
        return this.projectId;
    }

    public String getStorageBucket() {
        return this.storageBucket;
    }

    public int hashCode() {
        return Objects.hashCode(this.applicationId, this.apiKey, this.databaseUrl, this.gaTrackingId, this.gcmSenderId, this.storageBucket, this.projectId);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("applicationId", this.applicationId).add("apiKey", this.apiKey).add("databaseUrl", this.databaseUrl).add("gcmSenderId", this.gcmSenderId).add("storageBucket", this.storageBucket).add("projectId", this.projectId).toString();
    }

    public static final class Builder {
        private String apiKey;
        private String applicationId;
        private String databaseUrl;
        private String gaTrackingId;
        private String gcmSenderId;
        private String projectId;
        private String storageBucket;

        public Builder() {
        }

        public Builder(FirebaseOptions firebaseOptions) {
            this.applicationId = firebaseOptions.applicationId;
            this.apiKey = firebaseOptions.apiKey;
            this.databaseUrl = firebaseOptions.databaseUrl;
            this.gaTrackingId = firebaseOptions.gaTrackingId;
            this.gcmSenderId = firebaseOptions.gcmSenderId;
            this.storageBucket = firebaseOptions.storageBucket;
            this.projectId = firebaseOptions.projectId;
        }

        public FirebaseOptions build() {
            return new FirebaseOptions(this.applicationId, this.apiKey, this.databaseUrl, this.gaTrackingId, this.gcmSenderId, this.storageBucket, this.projectId);
        }

        public Builder setApiKey(String string2) {
            this.apiKey = Preconditions.checkNotEmpty(string2, "ApiKey must be set.");
            return this;
        }

        public Builder setApplicationId(String string2) {
            this.applicationId = Preconditions.checkNotEmpty(string2, "ApplicationId must be set.");
            return this;
        }

        public Builder setDatabaseUrl(String string2) {
            this.databaseUrl = string2;
            return this;
        }

        public Builder setGaTrackingId(String string2) {
            this.gaTrackingId = string2;
            return this;
        }

        public Builder setGcmSenderId(String string2) {
            this.gcmSenderId = string2;
            return this;
        }

        public Builder setProjectId(String string2) {
            this.projectId = string2;
            return this;
        }

        public Builder setStorageBucket(String string2) {
            this.storageBucket = string2;
            return this;
        }
    }
}

