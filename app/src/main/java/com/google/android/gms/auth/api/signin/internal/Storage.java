/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.text.TextUtils
 *  org.json.JSONException
 */
package com.google.android.gms.auth.api.signin.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.internal.Preconditions;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.json.JSONException;

public class Storage {
    private static final Lock zaa = new ReentrantLock();
    private static Storage zab;
    private final Lock zac = new ReentrantLock();
    private final SharedPreferences zad;

    Storage(Context context) {
        this.zad = context.getSharedPreferences("com.google.android.gms.signin", 0);
    }

    public static Storage getInstance(Context object) {
        Preconditions.checkNotNull(object);
        Lock lock = zaa;
        lock.lock();
        try {
            if (zab == null) {
                Storage storage;
                zab = storage = new Storage(object.getApplicationContext());
            }
            object = zab;
            lock.unlock();
            return object;
        }
        catch (Throwable throwable) {
            zaa.unlock();
            throw throwable;
        }
    }

    private static final String zae(String string2, String string3) {
        int n = String.valueOf(string3).length();
        StringBuilder stringBuilder = new StringBuilder(string2.length() + 1 + n);
        stringBuilder.append(string2);
        stringBuilder.append(":");
        stringBuilder.append(string3);
        return stringBuilder.toString();
    }

    public void clear() {
        this.zac.lock();
        try {
            this.zad.edit().clear().apply();
            return;
        }
        finally {
            this.zac.unlock();
        }
    }

    public GoogleSignInAccount getSavedDefaultGoogleSignInAccount() {
        String string2 = this.zaa("defaultGoogleSignInAccount");
        boolean bl = TextUtils.isEmpty((CharSequence)string2);
        GoogleSignInAccount googleSignInAccount = null;
        GoogleSignInAccount googleSignInAccount2 = null;
        if (!bl) {
            string2 = this.zaa(Storage.zae("googleSignInAccount", string2));
            googleSignInAccount = googleSignInAccount2;
            if (string2 != null) {
                try {
                    googleSignInAccount = GoogleSignInAccount.zab(string2);
                }
                catch (JSONException jSONException) {
                    googleSignInAccount = googleSignInAccount2;
                }
            }
        }
        return googleSignInAccount;
    }

    public GoogleSignInOptions getSavedDefaultGoogleSignInOptions() {
        String string2 = this.zaa("defaultGoogleSignInAccount");
        boolean bl = TextUtils.isEmpty((CharSequence)string2);
        GoogleSignInOptions googleSignInOptions = null;
        GoogleSignInOptions googleSignInOptions2 = null;
        if (!bl) {
            string2 = this.zaa(Storage.zae("googleSignInOptions", string2));
            googleSignInOptions = googleSignInOptions2;
            if (string2 != null) {
                try {
                    googleSignInOptions = GoogleSignInOptions.zab(string2);
                }
                catch (JSONException jSONException) {
                    googleSignInOptions = googleSignInOptions2;
                }
            }
        }
        return googleSignInOptions;
    }

    public String getSavedRefreshToken() {
        return this.zaa("refreshToken");
    }

    public void saveDefaultGoogleSignInAccount(GoogleSignInAccount googleSignInAccount, GoogleSignInOptions googleSignInOptions) {
        Preconditions.checkNotNull(googleSignInAccount);
        Preconditions.checkNotNull(googleSignInOptions);
        this.zad("defaultGoogleSignInAccount", googleSignInAccount.zac());
        Preconditions.checkNotNull(googleSignInAccount);
        Preconditions.checkNotNull(googleSignInOptions);
        String string2 = googleSignInAccount.zac();
        this.zad(Storage.zae("googleSignInAccount", string2), googleSignInAccount.zad());
        this.zad(Storage.zae("googleSignInOptions", string2), googleSignInOptions.zaf());
    }

    protected final String zaa(String string2) {
        this.zac.lock();
        try {
            string2 = this.zad.getString(string2, null);
            return string2;
        }
        finally {
            this.zac.unlock();
        }
    }

    protected final void zab(String string2) {
        this.zac.lock();
        try {
            this.zad.edit().remove(string2).apply();
            return;
        }
        finally {
            this.zac.unlock();
        }
    }

    public final void zac() {
        String string2 = this.zaa("defaultGoogleSignInAccount");
        this.zab("defaultGoogleSignInAccount");
        if (TextUtils.isEmpty((CharSequence)string2)) {
            return;
        }
        this.zab(Storage.zae("googleSignInAccount", string2));
        this.zab(Storage.zae("googleSignInOptions", string2));
    }

    protected final void zad(String string2, String string3) {
        this.zac.lock();
        try {
            this.zad.edit().putString(string2, string3).apply();
            return;
        }
        finally {
            this.zac.unlock();
        }
    }
}

