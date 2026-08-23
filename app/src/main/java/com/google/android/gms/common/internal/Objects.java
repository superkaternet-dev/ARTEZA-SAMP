/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.google.android.gms.common.internal;

import android.os.Bundle;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.zzah;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class Objects {
    private Objects() {
        throw new AssertionError((Object)"Uninstantiable");
    }

    public static boolean checkBundlesEquality(Bundle bundle, Bundle bundle2) {
        if (bundle != null && bundle2 != null) {
            if (bundle.size() != bundle2.size()) {
                return false;
            }
            Object object = bundle.keySet();
            if (!object.containsAll(bundle2.keySet())) {
                return false;
            }
            Iterator iterator2 = object.iterator();
            while (iterator2.hasNext()) {
                object = (String)iterator2.next();
                if (Objects.equal(bundle.get((String)object), bundle2.get((String)object))) continue;
                return false;
            }
            return true;
        }
        return bundle == bundle2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean equal(Object object, Object object2) {
        boolean bl = false;
        if (object == object2) return true;
        if (object == null) return bl;
        if (!object.equals(object2)) return false;
        return true;
    }

    public static int hashCode(Object ... objectArray) {
        return Arrays.hashCode(objectArray);
    }

    public static ToStringHelper toStringHelper(Object object) {
        return new ToStringHelper(object, null);
    }

    public static final class ToStringHelper {
        private final List<String> zza;
        private final Object zzb;

        /* synthetic */ ToStringHelper(Object object, zzah zzah2) {
            Preconditions.checkNotNull(object);
            this.zzb = object;
            this.zza = new ArrayList<String>();
        }

        public ToStringHelper add(String string2, Object object) {
            List<String> list = this.zza;
            Preconditions.checkNotNull(string2);
            object = String.valueOf(object);
            int n = String.valueOf(object).length();
            StringBuilder stringBuilder = new StringBuilder(string2.length() + 1 + n);
            stringBuilder.append(string2);
            stringBuilder.append("=");
            stringBuilder.append((String)object);
            list.add(stringBuilder.toString());
            return this;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(100);
            stringBuilder.append(this.zzb.getClass().getSimpleName());
            stringBuilder.append('{');
            int n = this.zza.size();
            for (int i = 0; i < n; ++i) {
                stringBuilder.append(this.zza.get(i));
                if (i >= n - 1) continue;
                stringBuilder.append(", ");
            }
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }
}

