/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.accounts.Account
 *  android.content.Context
 *  android.view.View
 *  javax.annotation.Nullable
 */
package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.content.Context;
import android.view.View;
import androidx.collection.ArraySet;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zab;
import com.google.android.gms.signin.SignInOptions;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

public final class ClientSettings {
    @Nullable
    private final Account zaa;
    private final Set<Scope> zab;
    private final Set<Scope> zac;
    private final Map<Api<?>, zab> zad;
    private final int zae;
    @Nullable
    private final View zaf;
    private final String zag;
    private final String zah;
    private final SignInOptions zai;
    private Integer zaj;

    public ClientSettings(Account account, Set<Scope> set, Map<Api<?>, zab> map, int n, @Nullable View view, String string2, String string3, @Nullable SignInOptions signInOptions) {
        this(account, set, map, n, view, string2, string3, signInOptions, false);
    }

    public ClientSettings(@Nullable Account object, Set<Scope> object2, Map<Api<?>, zab> map, int n, @Nullable View view, String string2, String string3, @Nullable SignInOptions signInOptions, boolean bl) {
        this.zaa = object;
        object = object2 == null ? Collections.emptySet() : Collections.unmodifiableSet(object2);
        this.zab = object;
        if (map == null) {
            map = Collections.emptyMap();
        }
        this.zad = map;
        this.zaf = view;
        this.zae = n;
        this.zag = string2;
        this.zah = string3;
        if (signInOptions == null) {
            signInOptions = SignInOptions.zaa;
        }
        this.zai = signInOptions;
        object = new HashSet(object);
        object2 = map.values().iterator();
        while (object2.hasNext()) {
            object.addAll(((zab)object2.next()).zaa);
        }
        this.zac = Collections.unmodifiableSet(object);
    }

    public static ClientSettings createDefault(Context context) {
        return new GoogleApiClient.Builder(context).zaa();
    }

    public Account getAccount() {
        return this.zaa;
    }

    @Deprecated
    public String getAccountName() {
        Account account = this.zaa;
        if (account != null) {
            return account.name;
        }
        return null;
    }

    public Account getAccountOrDefault() {
        Account account = this.zaa;
        if (account != null) {
            return account;
        }
        return new Account("<<default account>>", "com.google");
    }

    public Set<Scope> getAllRequestedScopes() {
        return this.zac;
    }

    public Set<Scope> getApplicableScopes(Api<?> object) {
        if ((object = this.zad.get(object)) != null && !((zab)object).zaa.isEmpty()) {
            HashSet<Scope> hashSet = new HashSet<Scope>(this.zab);
            hashSet.addAll(((zab)object).zaa);
            return hashSet;
        }
        return this.zab;
    }

    public int getGravityForPopups() {
        return this.zae;
    }

    public String getRealClientPackageName() {
        return this.zag;
    }

    public Set<Scope> getRequiredScopes() {
        return this.zab;
    }

    public View getViewForPopups() {
        return this.zaf;
    }

    public final SignInOptions zaa() {
        return this.zai;
    }

    public final Integer zab() {
        return this.zaj;
    }

    public final String zac() {
        return this.zah;
    }

    public final Map<Api<?>, zab> zad() {
        return this.zad;
    }

    public final void zae(Integer n) {
        this.zaj = n;
    }

    public static final class Builder {
        @Nullable
        private Account zaa;
        private ArraySet<Scope> zab;
        private String zac;
        private String zad;
        private SignInOptions zae = SignInOptions.zaa;

        public ClientSettings build() {
            return new ClientSettings(this.zaa, this.zab, null, 0, null, this.zac, this.zad, this.zae, false);
        }

        public Builder setRealClientPackageName(String string2) {
            this.zac = string2;
            return this;
        }

        public final Builder zaa(Collection<Scope> collection) {
            if (this.zab == null) {
                this.zab = new ArraySet();
            }
            this.zab.addAll(collection);
            return this;
        }

        public final Builder zab(@Nullable Account account) {
            this.zaa = account;
            return this;
        }

        public final Builder zac(String string2) {
            this.zad = string2;
            return this;
        }
    }
}

