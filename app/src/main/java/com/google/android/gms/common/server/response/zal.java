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
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.server.response.zam;
import com.google.android.gms.common.server.response.zap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public final class zal
extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zal> CREATOR = new zap();
    final int zaa;
    final String zab;
    final ArrayList<zam> zac;

    zal(int n, String string2, ArrayList<zam> arrayList) {
        this.zaa = n;
        this.zab = string2;
        this.zac = arrayList;
    }

    zal(String object, Map<String, FastJsonResponse.Field<?, ?>> map) {
        this.zaa = 1;
        this.zab = object;
        if (map == null) {
            object = null;
        } else {
            ArrayList<zam> arrayList = new ArrayList<zam>();
            Iterator<String> iterator2 = map.keySet().iterator();
            while (true) {
                object = arrayList;
                if (!iterator2.hasNext()) break;
                object = iterator2.next();
                arrayList.add(new zam((String)object, map.get(object)));
            }
        }
        this.zac = object;
    }

    public final void writeToParcel(Parcel parcel, int n) {
        n = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        SafeParcelWriter.writeString(parcel, 2, this.zab, false);
        SafeParcelWriter.writeTypedList(parcel, 3, this.zac, false);
        SafeParcelWriter.finishObjectHeader(parcel, n);
    }
}

