/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.net.Uri
 *  android.os.Parcel
 *  android.os.Parcelable
 *  android.os.Parcelable$Creator
 *  android.text.TextUtils
 *  org.json.JSONArray
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.collection.ArraySet;
import com.google.android.gms.auth.api.signin.zaa;
import com.google.android.gms.auth.api.signin.zab;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.util.Clock;
import com.google.android.gms.common.util.DefaultClock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleSignInAccount
extends AbstractSafeParcelable
implements ReflectedParcelable {
    public static final Parcelable.Creator<GoogleSignInAccount> CREATOR = new zab();
    public static Clock zaa = DefaultClock.getInstance();
    final int zab;
    List<Scope> zac;
    private String zad;
    private String zae;
    private String zaf;
    private String zag;
    private Uri zah;
    private String zai;
    private long zaj;
    private String zak;
    private String zal;
    private String zam;
    private Set<Scope> zan = new HashSet<Scope>();

    GoogleSignInAccount(int n, String string2, String string3, String string4, String string5, Uri uri, String string6, long l, String string7, List<Scope> list, String string8, String string9) {
        this.zab = n;
        this.zad = string2;
        this.zae = string3;
        this.zaf = string4;
        this.zag = string5;
        this.zah = uri;
        this.zai = string6;
        this.zaj = l;
        this.zak = string7;
        this.zac = list;
        this.zal = string8;
        this.zam = string9;
    }

    public static GoogleSignInAccount createDefault() {
        return GoogleSignInAccount.zae(new Account("<<default account>>", "com.google"), new HashSet<Scope>());
    }

    public static GoogleSignInAccount fromAccount(Account account) {
        return GoogleSignInAccount.zae(account, new ArraySet<Scope>());
    }

    public static GoogleSignInAccount zaa(String string2, String string3, String string4, String string5, String string6, String string7, Uri uri, Long l, String string8, Set<Scope> set) {
        return new GoogleSignInAccount(3, string2, string3, string4, string5, uri, null, l, Preconditions.checkNotEmpty(string8), new ArrayList<Scope>((Collection)Preconditions.checkNotNull(set)), string6, string7);
    }

    public static GoogleSignInAccount zab(String string2) throws JSONException {
        boolean bl = TextUtils.isEmpty((CharSequence)string2);
        Object var11_2 = null;
        if (bl) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(string2);
        string2 = !TextUtils.isEmpty((CharSequence)(string2 = jSONObject.optString("photoUrl"))) ? Uri.parse((String)string2) : null;
        long l = Long.parseLong(jSONObject.getString("expirationTime"));
        HashSet<Scope> hashSet = new HashSet<Scope>();
        Object object = jSONObject.getJSONArray("grantedScopes");
        int n = object.length();
        for (int i = 0; i < n; ++i) {
            hashSet.add(new Scope(object.getString(i)));
        }
        String string3 = jSONObject.optString("id");
        object = jSONObject.has("tokenId") ? jSONObject.optString("tokenId") : null;
        String string4 = jSONObject.has("email") ? jSONObject.optString("email") : null;
        String string5 = jSONObject.has("displayName") ? jSONObject.optString("displayName") : null;
        String string6 = jSONObject.has("givenName") ? jSONObject.optString("givenName") : null;
        String string7 = jSONObject.has("familyName") ? jSONObject.optString("familyName") : null;
        object = GoogleSignInAccount.zaa(string3, (String)object, string4, string5, string6, string7, (Uri)string2, l, jSONObject.getString("obfuscatedIdentifier"), hashSet);
        string2 = var11_2;
        if (jSONObject.has("serverAuthCode")) {
            string2 = jSONObject.optString("serverAuthCode");
        }
        object.zai = string2;
        return object;
    }

    private static GoogleSignInAccount zae(Account account, Set<Scope> set) {
        return GoogleSignInAccount.zaa(null, null, account.name, null, null, null, null, 0L, account.name, set);
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (!(object instanceof GoogleSignInAccount)) {
            return false;
        }
        object = (GoogleSignInAccount)object;
        return ((GoogleSignInAccount)object).zak.equals(this.zak) && ((GoogleSignInAccount)object).getRequestedScopes().equals(this.getRequestedScopes());
    }

    public Account getAccount() {
        String string2 = this.zaf;
        if (string2 == null) {
            return null;
        }
        return new Account(string2, "com.google");
    }

    public String getDisplayName() {
        return this.zag;
    }

    public String getEmail() {
        return this.zaf;
    }

    public String getFamilyName() {
        return this.zam;
    }

    public String getGivenName() {
        return this.zal;
    }

    public Set<Scope> getGrantedScopes() {
        return new HashSet<Scope>(this.zac);
    }

    public String getId() {
        return this.zad;
    }

    public String getIdToken() {
        return this.zae;
    }

    public Uri getPhotoUrl() {
        return this.zah;
    }

    public Set<Scope> getRequestedScopes() {
        HashSet<Scope> hashSet = new HashSet<Scope>(this.zac);
        hashSet.addAll(this.zan);
        return hashSet;
    }

    public String getServerAuthCode() {
        return this.zai;
    }

    public int hashCode() {
        return (this.zak.hashCode() + 527) * 31 + this.getRequestedScopes().hashCode();
    }

    public boolean isExpired() {
        return zaa.currentTimeMillis() / 1000L >= this.zaj - 300L;
    }

    public GoogleSignInAccount requestExtraScopes(Scope ... scopeArray) {
        if (scopeArray != null) {
            Collections.addAll(this.zan, scopeArray);
        }
        return this;
    }

    public void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zab);
        SafeParcelWriter.writeString(parcel, 2, this.getId(), false);
        SafeParcelWriter.writeString(parcel, 3, this.getIdToken(), false);
        SafeParcelWriter.writeString(parcel, 4, this.getEmail(), false);
        SafeParcelWriter.writeString(parcel, 5, this.getDisplayName(), false);
        SafeParcelWriter.writeParcelable(parcel, 6, (Parcelable)this.getPhotoUrl(), n, false);
        SafeParcelWriter.writeString(parcel, 7, this.getServerAuthCode(), false);
        SafeParcelWriter.writeLong(parcel, 8, this.zaj);
        SafeParcelWriter.writeString(parcel, 9, this.zak, false);
        SafeParcelWriter.writeTypedList(parcel, 10, this.zac, false);
        SafeParcelWriter.writeString(parcel, 11, this.getGivenName(), false);
        SafeParcelWriter.writeString(parcel, 12, this.getFamilyName(), false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }

    public final String zac() {
        return this.zak;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final String zad() {
        JSONArray jSONArray;
        JSONObject jSONObject = new JSONObject();
        try {
            String string2;
            Uri uri;
            if (this.getId() != null) {
                jSONObject.put("id", (Object)this.getId());
            }
            if (this.getIdToken() != null) {
                jSONObject.put("tokenId", (Object)this.getIdToken());
            }
            if (this.getEmail() != null) {
                jSONObject.put("email", (Object)this.getEmail());
            }
            if (this.getDisplayName() != null) {
                jSONObject.put("displayName", (Object)this.getDisplayName());
            }
            if (this.getGivenName() != null) {
                jSONObject.put("givenName", (Object)this.getGivenName());
            }
            if (this.getFamilyName() != null) {
                jSONObject.put("familyName", (Object)this.getFamilyName());
            }
            if ((uri = this.getPhotoUrl()) != null) {
                jSONObject.put("photoUrl", (Object)uri.toString());
            }
            if ((string2 = this.getServerAuthCode()) != null) {
                jSONObject.put("serverAuthCode", (Object)this.getServerAuthCode());
            }
            jSONObject.put("expirationTime", this.zaj);
            jSONObject.put("obfuscatedIdentifier", (Object)this.zak);
            jSONArray = new JSONArray();
            Scope[] scopeArray = this.zac;
            scopeArray = scopeArray.toArray(new Scope[scopeArray.size()]);
            Arrays.sort(scopeArray, com.google.android.gms.auth.api.signin.zaa.zaa);
            int n = scopeArray.length;
            for (int i = 0; i < n; ++i) {
                jSONArray.put((Object)scopeArray[i].getScopeUri());
            }
        }
        catch (JSONException jSONException) {
            RuntimeException runtimeException = new RuntimeException(jSONException);
            throw runtimeException;
        }
        {
            jSONObject.put("grantedScopes", (Object)jSONArray);
        }
        jSONObject.remove("serverAuthCode");
        return jSONObject.toString();
    }
}

