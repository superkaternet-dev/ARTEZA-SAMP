/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentProvider
 *  android.content.ContentValues
 *  android.content.Context
 *  android.content.pm.ProviderInfo
 *  android.database.Cursor
 *  android.net.Uri
 *  android.util.Log
 */
package com.google.firebase.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.google.android.gms.common.internal.Preconditions;
import com.google.firebase.FirebaseApp;

public class FirebaseInitProvider
extends ContentProvider {
    static final String EMPTY_APPLICATION_ID_PROVIDER_AUTHORITY = "com.google.firebase.firebaseinitprovider";
    private static final String TAG = "FirebaseInitProvider";

    private static void checkContentProviderAuthority(ProviderInfo providerInfo) {
        Preconditions.checkNotNull(providerInfo, "FirebaseInitProvider ProviderInfo cannot be null.");
        if (!EMPTY_APPLICATION_ID_PROVIDER_AUTHORITY.equals(providerInfo.authority)) {
            return;
        }
        throw new IllegalStateException("Incorrect provider authority in manifest. Most likely due to a missing applicationId variable in application's build.gradle.");
    }

    public void attachInfo(Context context, ProviderInfo providerInfo) {
        FirebaseInitProvider.checkContentProviderAuthority(providerInfo);
        super.attachInfo(context, providerInfo);
    }

    public int delete(Uri uri, String string2, String[] stringArray) {
        return 0;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        if (FirebaseApp.initializeApp(this.getContext()) == null) {
            Log.i((String)TAG, (String)"FirebaseApp initialization unsuccessful");
        } else {
            Log.i((String)TAG, (String)"FirebaseApp initialization successful");
        }
        return false;
    }

    public Cursor query(Uri uri, String[] stringArray, String string2, String[] stringArray2, String string3) {
        return null;
    }

    public int update(Uri uri, ContentValues contentValues, String string2, String[] stringArray) {
        return 0;
    }
}

