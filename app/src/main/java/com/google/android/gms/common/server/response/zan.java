/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 */
package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.server.response.zal;
import com.google.android.gms.common.server.response.zam;
import com.google.android.gms.common.server.response.zao;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class zan
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zan> CREATOR = new zao();
    final int zaa;
    private final HashMap<String, Map<String, FastJsonResponse.Field<?, ?>>> zab;
    private final String zac;

    zan(int n, ArrayList<zal> arrayList, String string2) {
        this.zaa = n;
        HashMap hashMap = new HashMap();
        int n2 = arrayList.size();
        for (n = 0; n < n2; ++n) {
            zal zal2 = arrayList.get(n);
            String string3 = zal2.zab;
            HashMap hashMap2 = new HashMap();
            int n3 = Preconditions.checkNotNull(zal2.zac).size();
            for (int i = 0; i < n3; ++i) {
                zam zam2 = zal2.zac.get(i);
                hashMap2.put(zam2.zab, zam2.zac);
            }
            hashMap.put(string3, hashMap2);
        }
        this.zab = hashMap;
        this.zac = Preconditions.checkNotNull(string2);
        this.zad();
    }

    public zan(Class<? extends FastJsonResponse> clazz) {
        this.zaa = 1;
        this.zab = new HashMap();
        this.zac = Preconditions.checkNotNull(clazz.getCanonicalName());
    }

    public final String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String string2 : this.zab.keySet()) {
            stringBuilder.append(string2);
            stringBuilder.append(":\n");
            Map<String, FastJsonResponse.Field<?, ?>> object = this.zab.get(string2);
            for (String string3 : object.keySet()) {
                stringBuilder.append("  ");
                stringBuilder.append(string3);
                stringBuilder.append(": ");
                stringBuilder.append(object.get(string3));
            }
        }
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int n) {
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        ArrayList<zal> arrayList = new ArrayList<zal>();
        for (String string2 : this.zab.keySet()) {
            arrayList.add(new zal(string2, this.zab.get(string2)));
        }
        SafeParcelWriter.writeTypedList(parcel, 2, arrayList, false);
        SafeParcelWriter.writeString(parcel, 3, this.zac, false);
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }

    public final String zaa() {
        return this.zac;
    }

    public final Map<String, FastJsonResponse.Field<?, ?>> zab(String string2) {
        return this.zab.get(string2);
    }

    public final void zac() {
        for (String string2 : this.zab.keySet()) {
            Map<String, FastJsonResponse.Field<?, ?>> map = this.zab.get(string2);
            HashMap hashMap = new HashMap();
            for (String string3 : map.keySet()) {
                hashMap.put(string3, map.get(string3).zab());
            }
            this.zab.put(string2, hashMap);
        }
    }

    public final void zad() {
        for (String string2 : this.zab.keySet()) {
            Map<String, FastJsonResponse.Field<?, ?>> map = this.zab.get(string2);
            Iterator<String> object = map.keySet().iterator();
            while (object.hasNext()) {
                map.get(object.next()).zai(this);
            }
        }
    }

    public final void zae(Class<? extends FastJsonResponse> clazz, Map<String, FastJsonResponse.Field<?, ?>> map) {
        this.zab.put(Preconditions.checkNotNull(clazz.getCanonicalName()), map);
    }

    public final boolean zaf(Class<? extends FastJsonResponse> clazz) {
        return this.zab.containsKey(Preconditions.checkNotNull(clazz.getCanonicalName()));
    }
}

