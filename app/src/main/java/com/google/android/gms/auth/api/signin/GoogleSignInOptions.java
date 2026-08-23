/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
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
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable;
import com.google.android.gms.auth.api.signin.internal.HashAccumulator;
import com.google.android.gms.auth.api.signin.zac;
import com.google.android.gms.auth.api.signin.zad;
import com.google.android.gms.auth.api.signin.zae;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GoogleSignInOptions
extends AbstractSafeParcelable
implements Api.ApiOptions.Optional,
ReflectedParcelable {
    public static final Parcelable.Creator<GoogleSignInOptions> CREATOR;
    public static final GoogleSignInOptions DEFAULT_GAMES_SIGN_IN;
    public static final GoogleSignInOptions DEFAULT_SIGN_IN;
    public static final Scope zaa;
    public static final Scope zab;
    public static final Scope zac;
    public static final Scope zad;
    public static final Scope zae;
    private static Comparator<Scope> zag;
    final int zaf;
    private final ArrayList<Scope> zah;
    private Account zai;
    private boolean zaj;
    private final boolean zak;
    private final boolean zal;
    private String zam;
    private String zan;
    private ArrayList<GoogleSignInOptionsExtensionParcelable> zao;
    private String zap;
    private Map<Integer, GoogleSignInOptionsExtensionParcelable> zaq;

    static {
        Scope scope;
        zaa = new Scope("profile");
        zab = new Scope("email");
        zac = new Scope("openid");
        zad = scope = new Scope("https://www.googleapis.com/auth/games_lite");
        zae = new Scope("https://www.googleapis.com/auth/games");
        Builder builder = new Builder();
        builder.requestId();
        builder.requestProfile();
        DEFAULT_SIGN_IN = builder.build();
        builder = new Builder();
        builder.requestScopes(scope, new Scope[0]);
        DEFAULT_GAMES_SIGN_IN = builder.build();
        CREATOR = new zae();
        zag = new zac();
    }

    GoogleSignInOptions(int n, ArrayList<Scope> arrayList, Account account, boolean bl, boolean bl2, boolean bl3, String string2, String string3, ArrayList<GoogleSignInOptionsExtensionParcelable> arrayList2, String string4) {
        this(n, arrayList, account, bl, bl2, bl3, string2, string3, GoogleSignInOptions.zam(arrayList2), string4);
    }

    private GoogleSignInOptions(int n, ArrayList<Scope> arrayList, Account account, boolean bl, boolean bl2, boolean bl3, String string2, String string3, Map<Integer, GoogleSignInOptionsExtensionParcelable> map, String string4) {
        this.zaf = n;
        this.zah = arrayList;
        this.zai = account;
        this.zaj = bl;
        this.zak = bl2;
        this.zal = bl3;
        this.zam = string2;
        this.zan = string3;
        this.zao = new ArrayList<GoogleSignInOptionsExtensionParcelable>(map.values());
        this.zaq = map;
        this.zap = string4;
    }

    /* synthetic */ GoogleSignInOptions(int n, ArrayList arrayList, Account account, boolean bl, boolean bl2, boolean bl3, String string2, String string3, Map map, String string4, zad zad2) {
        this(3, (ArrayList<Scope>)arrayList, account, bl, bl2, bl3, string2, string3, map, string4);
    }

    public static GoogleSignInOptions zab(String string2) throws JSONException {
        boolean bl = TextUtils.isEmpty((CharSequence)string2);
        String string3 = null;
        if (bl) {
            return null;
        }
        JSONObject jSONObject = new JSONObject(string2);
        Object object = new HashSet<Scope>();
        string2 = jSONObject.getJSONArray("scopes");
        int n = string2.length();
        for (int i = 0; i < n; ++i) {
            object.add(new Scope(string2.getString(i)));
        }
        string2 = jSONObject.has("accountName") ? jSONObject.optString("accountName") : null;
        string2 = !TextUtils.isEmpty((CharSequence)string2) ? new Account(string2, "com.google") : null;
        ArrayList<Scope> arrayList = new ArrayList<Scope>((Collection<Scope>)object);
        bl = jSONObject.getBoolean("idTokenRequested");
        boolean bl2 = jSONObject.getBoolean("serverAuthRequested");
        boolean bl3 = jSONObject.getBoolean("forceCodeForRefreshToken");
        object = jSONObject.has("serverClientId") ? jSONObject.optString("serverClientId") : null;
        if (jSONObject.has("hostedDomain")) {
            string3 = jSONObject.optString("hostedDomain");
        }
        return new GoogleSignInOptions(3, arrayList, (Account)string2, bl, bl2, bl3, (String)object, string3, new HashMap<Integer, GoogleSignInOptionsExtensionParcelable>(), null);
    }

    private static Map<Integer, GoogleSignInOptionsExtensionParcelable> zam(List<GoogleSignInOptionsExtensionParcelable> object) {
        HashMap<Integer, GoogleSignInOptionsExtensionParcelable> hashMap = new HashMap<Integer, GoogleSignInOptionsExtensionParcelable>();
        if (object == null) {
            return hashMap;
        }
        Iterator<GoogleSignInOptionsExtensionParcelable> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            hashMap.put(((GoogleSignInOptionsExtensionParcelable)object).getType(), (GoogleSignInOptionsExtensionParcelable)object);
        }
        return hashMap;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        try {
            GoogleSignInOptions googleSignInOptions = (GoogleSignInOptions)object;
            if (this.zao.size() > 0) return false;
            if (googleSignInOptions.zao.size() > 0) {
                return false;
            }
            if (this.zah.size() != googleSignInOptions.getScopes().size()) return false;
            if (!this.zah.containsAll(googleSignInOptions.getScopes())) {
                return false;
            }
            object = this.zai;
            if (object == null) {
                if (googleSignInOptions.getAccount() != null) return false;
            } else if (!object.equals((Object)googleSignInOptions.getAccount())) return false;
            if (TextUtils.isEmpty((CharSequence)this.zam)) {
                if (!TextUtils.isEmpty((CharSequence)googleSignInOptions.getServerClientId())) return false;
            } else if (!this.zam.equals(googleSignInOptions.getServerClientId())) {
                return false;
            }
            if (this.zal != googleSignInOptions.isForceCodeForRefreshToken()) return false;
            if (this.zaj != googleSignInOptions.isIdTokenRequested()) return false;
            if (this.zak != googleSignInOptions.isServerAuthCodeRequested()) return false;
            boolean bl = TextUtils.equals((CharSequence)this.zap, (CharSequence)googleSignInOptions.getLogSessionId());
            if (!bl) return false;
            return true;
        }
        catch (ClassCastException classCastException) {
            return false;
        }
    }

    public Account getAccount() {
        return this.zai;
    }

    public ArrayList<GoogleSignInOptionsExtensionParcelable> getExtensions() {
        return this.zao;
    }

    public String getLogSessionId() {
        return this.zap;
    }

    public Scope[] getScopeArray() {
        ArrayList<Scope> arrayList = this.zah;
        return arrayList.toArray(new Scope[arrayList.size()]);
    }

    public ArrayList<Scope> getScopes() {
        return new ArrayList<Scope>(this.zah);
    }

    public String getServerClientId() {
        return this.zam;
    }

    public int hashCode() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Object object = this.zah;
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            arrayList.add(((Scope)object.get(i)).getScopeUri());
        }
        Collections.sort(arrayList);
        object = new HashAccumulator();
        ((HashAccumulator)object).addObject(arrayList);
        ((HashAccumulator)object).addObject(this.zai);
        ((HashAccumulator)object).addObject(this.zam);
        ((HashAccumulator)object).zaa(this.zal);
        ((HashAccumulator)object).zaa(this.zaj);
        ((HashAccumulator)object).zaa(this.zak);
        ((HashAccumulator)object).addObject(this.zap);
        return ((HashAccumulator)object).hash();
    }

    public boolean isForceCodeForRefreshToken() {
        return this.zal;
    }

    public boolean isIdTokenRequested() {
        return this.zaj;
    }

    public boolean isServerAuthCodeRequested() {
        return this.zak;
    }

    public void writeToParcel(Parcel parcel, int n) {
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaf);
        SafeParcelWriter.writeTypedList(parcel, 2, this.getScopes(), false);
        SafeParcelWriter.writeParcelable(parcel, 3, (Parcelable)this.getAccount(), n, false);
        SafeParcelWriter.writeBoolean(parcel, 4, this.isIdTokenRequested());
        SafeParcelWriter.writeBoolean(parcel, 5, this.isServerAuthCodeRequested());
        SafeParcelWriter.writeBoolean(parcel, 6, this.isForceCodeForRefreshToken());
        SafeParcelWriter.writeString(parcel, 7, this.getServerClientId(), false);
        SafeParcelWriter.writeString(parcel, 8, this.zan, false);
        SafeParcelWriter.writeTypedList(parcel, 9, this.getExtensions(), false);
        SafeParcelWriter.writeString(parcel, 10, this.getLogSessionId(), false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final String zaf() {
        JSONObject jSONObject = new JSONObject();
        try {
            JSONArray jSONArray = new JSONArray();
            Collections.sort(this.zah, zag);
            Iterator<Scope> iterator2 = this.zah.iterator();
            while (iterator2.hasNext()) {
                jSONArray.put((Object)iterator2.next().getScopeUri());
            }
            jSONObject.put("scopes", (Object)jSONArray);
            jSONArray = this.zai;
            if (jSONArray != null) {
                jSONObject.put("accountName", (Object)jSONArray.name);
            }
            jSONObject.put("idTokenRequested", this.zaj);
            jSONObject.put("forceCodeForRefreshToken", this.zal);
            jSONObject.put("serverAuthRequested", this.zak);
            if (!TextUtils.isEmpty((CharSequence)this.zam)) {
                jSONObject.put("serverClientId", (Object)this.zam);
            }
            if (TextUtils.isEmpty((CharSequence)this.zan)) return jSONObject.toString();
            jSONObject.put("hostedDomain", (Object)this.zan);
            return jSONObject.toString();
        }
        catch (JSONException jSONException) {
            RuntimeException runtimeException = new RuntimeException(jSONException);
            throw runtimeException;
        }
    }

    public static final class Builder {
        private Set<Scope> zaa = new HashSet<Scope>();
        private boolean zab;
        private boolean zac;
        private boolean zad;
        private String zae;
        private Account zaf;
        private String zag;
        private Map<Integer, GoogleSignInOptionsExtensionParcelable> zah = new HashMap<Integer, GoogleSignInOptionsExtensionParcelable>();
        private String zai;

        public Builder() {
        }

        public Builder(GoogleSignInOptions googleSignInOptions) {
            Preconditions.checkNotNull(googleSignInOptions);
            this.zaa = new HashSet<Scope>(googleSignInOptions.zah);
            this.zab = googleSignInOptions.zak;
            this.zac = googleSignInOptions.zal;
            this.zad = googleSignInOptions.zaj;
            this.zae = googleSignInOptions.zam;
            this.zaf = googleSignInOptions.zai;
            this.zag = googleSignInOptions.zan;
            this.zah = GoogleSignInOptions.zam(googleSignInOptions.zao);
            this.zai = googleSignInOptions.zap;
        }

        private final String zaa(String string2) {
            Preconditions.checkNotEmpty(string2);
            String string3 = this.zae;
            boolean bl = true;
            if (string3 != null && !string3.equals(string2)) {
                bl = false;
            }
            Preconditions.checkArgument(bl, "two different server client ids provided");
            return string2;
        }

        public Builder addExtension(GoogleSignInOptionsExtension googleSignInOptionsExtension) {
            if (!this.zah.containsKey(googleSignInOptionsExtension.getExtensionType())) {
                List<Scope> list = googleSignInOptionsExtension.getImpliedScopes();
                if (list != null) {
                    this.zaa.addAll(list);
                }
                this.zah.put(googleSignInOptionsExtension.getExtensionType(), new GoogleSignInOptionsExtensionParcelable(googleSignInOptionsExtension));
                return this;
            }
            throw new IllegalStateException("Only one extension per type may be added");
        }

        public GoogleSignInOptions build() {
            if (this.zaa.contains(zae) && this.zaa.contains(zad)) {
                this.zaa.remove(zad);
            }
            if (this.zad && (this.zaf == null || !this.zaa.isEmpty())) {
                this.requestId();
            }
            return new GoogleSignInOptions(3, new ArrayList<Scope>(this.zaa), this.zaf, this.zad, this.zab, this.zac, this.zae, this.zag, this.zah, this.zai, null);
        }

        public Builder requestEmail() {
            this.zaa.add(zab);
            return this;
        }

        public Builder requestId() {
            this.zaa.add(zac);
            return this;
        }

        public Builder requestIdToken(String string2) {
            this.zad = true;
            this.zaa(string2);
            this.zae = string2;
            return this;
        }

        public Builder requestProfile() {
            this.zaa.add(zaa);
            return this;
        }

        public Builder requestScopes(Scope scope, Scope ... scopeArray) {
            this.zaa.add(scope);
            this.zaa.addAll(Arrays.asList(scopeArray));
            return this;
        }

        public Builder requestServerAuthCode(String string2) {
            this.requestServerAuthCode(string2, false);
            return this;
        }

        public Builder requestServerAuthCode(String string2, boolean bl) {
            this.zab = true;
            this.zaa(string2);
            this.zae = string2;
            this.zac = bl;
            return this;
        }

        public Builder setAccountName(String string2) {
            this.zaf = new Account(Preconditions.checkNotEmpty(string2), "com.google");
            return this;
        }

        public Builder setHostedDomain(String string2) {
            this.zag = Preconditions.checkNotEmpty(string2);
            return this;
        }

        public Builder setLogSessionId(String string2) {
            this.zai = string2;
            return this;
        }
    }
}

