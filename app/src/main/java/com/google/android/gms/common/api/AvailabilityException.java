/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.google.android.gms.common.api;

import android.text.TextUtils;
import androidx.collection.ArrayMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.HasApiKey;
import com.google.android.gms.common.api.internal.ApiKey;
import com.google.android.gms.common.internal.Preconditions;
import java.util.ArrayList;

public class AvailabilityException
extends Exception {
    private final ArrayMap<ApiKey<?>, ConnectionResult> zaa;

    public AvailabilityException(ArrayMap<ApiKey<?>, ConnectionResult> arrayMap) {
        this.zaa = arrayMap;
    }

    public ConnectionResult getConnectionResult(GoogleApi<? extends Api.ApiOptions> object) {
        ApiKey<? extends Api.ApiOptions> apiKey = ((GoogleApi)object).getApiKey();
        boolean bl = this.zaa.get(apiKey) != null;
        object = apiKey.zab();
        StringBuilder stringBuilder = new StringBuilder(String.valueOf(object).length() + 58);
        stringBuilder.append("The given API (");
        stringBuilder.append((String)object);
        stringBuilder.append(") was not part of the availability request.");
        Preconditions.checkArgument(bl, stringBuilder.toString());
        return Preconditions.checkNotNull((ConnectionResult)this.zaa.get(apiKey));
    }

    public ConnectionResult getConnectionResult(HasApiKey<? extends Api.ApiOptions> object) {
        ApiKey<? extends Api.ApiOptions> apiKey = object.getApiKey();
        boolean bl = this.zaa.get(apiKey) != null;
        String string2 = apiKey.zab();
        object = new StringBuilder(String.valueOf(string2).length() + 58);
        ((StringBuilder)object).append("The given API (");
        ((StringBuilder)object).append(string2);
        ((StringBuilder)object).append(") was not part of the availability request.");
        Preconditions.checkArgument(bl, ((StringBuilder)object).toString());
        return Preconditions.checkNotNull((ConnectionResult)this.zaa.get(apiKey));
    }

    @Override
    public String getMessage() {
        ArrayList<String> arrayList = new ArrayList<String>();
        Object object = this.zaa.keySet().iterator();
        boolean bl = true;
        while (object.hasNext()) {
            Object object2 = object.next();
            Object object3 = Preconditions.checkNotNull((ConnectionResult)this.zaa.get(object2));
            bl &= ((ConnectionResult)object3).isSuccess() ^ true;
            object2 = ((ApiKey)object2).zab();
            String string2 = String.valueOf(object3);
            object3 = new StringBuilder(String.valueOf(object2).length() + 2 + String.valueOf(string2).length());
            ((StringBuilder)object3).append((String)object2);
            ((StringBuilder)object3).append(": ");
            ((StringBuilder)object3).append(string2);
            arrayList.add(((StringBuilder)object3).toString());
        }
        object = new StringBuilder();
        if (bl) {
            ((StringBuilder)object).append("None of the queried APIs are available. ");
        } else {
            ((StringBuilder)object).append("Some of the queried APIs are unavailable. ");
        }
        ((StringBuilder)object).append(TextUtils.join((CharSequence)"; ", arrayList));
        return ((StringBuilder)object).toString();
    }
}

