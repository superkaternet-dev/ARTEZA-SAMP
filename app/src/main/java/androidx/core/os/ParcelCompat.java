/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 */
package androidx.core.os;

import android.os.Parcel;

public final class ParcelCompat {
    private ParcelCompat() {
    }

    public static boolean readBoolean(Parcel parcel) {
        boolean bl = parcel.readInt() != 0;
        return bl;
    }

    public static void writeBoolean(Parcel parcel, boolean bl) {
        parcel.writeInt(bl ? 1 : 0);
    }
}

