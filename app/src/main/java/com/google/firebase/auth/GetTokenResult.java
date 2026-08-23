/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.auth;

import java.util.Map;

public class GetTokenResult {
    private String zza;
    private Map<String, Object> zzb;

    public GetTokenResult(String string2, Map<String, Object> map) {
        this.zza = string2;
        this.zzb = map;
    }

    private final long zza(String object) {
        if ((object = (Integer)this.zzb.get(object)) == null) {
            return 0L;
        }
        return ((Integer)object).longValue();
    }

    public long getAuthTimestamp() {
        return this.zza("auth_time");
    }

    public Map<String, Object> getClaims() {
        return this.zzb;
    }

    public long getExpirationTimestamp() {
        return this.zza("exp");
    }

    public long getIssuedAtTimestamp() {
        return this.zza("iat");
    }

    public String getSignInProvider() {
        Map map = (Map)this.zzb.get("firebase");
        if (map != null) {
            return (String)map.get("sign_in_provider");
        }
        return null;
    }

    public String getSignInSecondFactor() {
        Map map = (Map)this.zzb.get("firebase");
        if (map != null) {
            return (String)map.get("sign_in_second_factor");
        }
        return null;
    }

    public String getToken() {
        return this.zza;
    }
}

