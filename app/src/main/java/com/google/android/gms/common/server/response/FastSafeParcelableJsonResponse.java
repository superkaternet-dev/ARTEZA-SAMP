/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package com.google.android.gms.common.server.response;

import android.os.Parcel;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.Iterator;

public abstract class FastSafeParcelableJsonResponse
extends FastJsonResponse
implements SafeParcelable {
    public final int describeContents() {
        return 0;
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!this.getClass().isInstance(object)) {
            return false;
        }
        object = (FastJsonResponse)object;
        for (FastJsonResponse.Field<?, ?> field : this.getFieldMappings().values()) {
            if (!(this.isFieldSet(field) ? !((FastJsonResponse)object).isFieldSet(field) || !Objects.equal(this.getFieldValue(field), ((FastJsonResponse)object).getFieldValue(field)) : ((FastJsonResponse)object).isFieldSet(field))) continue;
            return false;
        }
        return true;
    }

    @Override
    public Object getValueObject(String string2) {
        return null;
    }

    public int hashCode() {
        Iterator<FastJsonResponse.Field<?, ?>> iterator2 = this.getFieldMappings().values().iterator();
        int n = 0;
        while (iterator2.hasNext()) {
            FastJsonResponse.Field<?, ?> field = iterator2.next();
            if (!this.isFieldSet(field)) continue;
            n = n * 31 + Preconditions.checkNotNull(this.getFieldValue(field)).hashCode();
        }
        return n;
    }

    @Override
    public boolean isPrimitiveFieldSet(String string2) {
        return false;
    }

    public byte[] toByteArray() {
        Parcel parcel = Parcel.obtain();
        this.writeToParcel(parcel, 0);
        byte[] byArray = parcel.marshall();
        parcel.recycle();
        return byArray;
    }
}

