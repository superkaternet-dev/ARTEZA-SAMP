/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.zaal;
import com.google.android.gms.common.api.internal.zaam;
import com.google.android.gms.common.api.internal.zaan;
import com.google.android.gms.common.api.internal.zaav;
import com.google.android.gms.common.api.internal.zaaw;
import com.google.android.gms.common.api.internal.zabf;
import com.google.android.gms.common.internal.BaseGmsClient;
import com.google.android.gms.common.internal.zal;
import java.util.ArrayList;
import java.util.Map;

final class zaao
extends zaav {
    final zaaw zaa;
    private final Map<Api.Client, zaal> zac;

    /*
     * Ignored method signature, as it can't be verified against descriptor
     */
    public zaao(zaaw zaaw2, Map map) {
        this.zaa = zaaw2;
        super(zaaw2, null);
        this.zac = map;
    }

    @Override
    public final void zaa() {
        int n;
        int n2;
        Object object = new zal(zaaw.zaf(this.zaa));
        Object object2 = new ArrayList<Api.Client>();
        Object object3 = new ArrayList<Api.Client>();
        for (Api.Client object4 : this.zac.keySet()) {
            if (object4.requiresGooglePlayServices() && !zaal.zaa(this.zac.get(object4))) {
                object2.add(object4);
                continue;
            }
            object3.add(object4);
        }
        boolean bl = object2.isEmpty();
        int n3 = -1;
        int n4 = 0;
        if (bl) {
            n2 = object3.size();
            for (n = n4; n < n2; ++n) {
                object2 = (Api.Client)object3.get(n);
                n4 = ((zal)object).zab(zaaw.zac(this.zaa), (Api.Client)object2);
                n3 = n4;
                if (n4 != 0) continue;
                n3 = n4;
                break;
            }
        } else {
            n2 = object2.size();
            for (n = 0; n < n2; ++n) {
                object3 = (Api.Client)object2.get(n);
                n4 = ((zal)object).zab(zaaw.zac(this.zaa), (Api.Client)object3);
                n3 = n4;
                if (n4 == 0) continue;
                n3 = n4;
                break;
            }
        }
        if (n3 != 0) {
            object2 = new ConnectionResult(n3, null);
            object = this.zaa;
            zaaw.zak((zaaw)object).zal(new zaam(this, (zabf)object, (ConnectionResult)object2));
            return;
        }
        object2 = this.zaa;
        if (zaaw.zav((zaaw)object2) && zaaw.zan((zaaw)object2) != null) {
            zaaw.zan((zaaw)object2).zab();
        }
        for (Api.Client client : this.zac.keySet()) {
            object3 = this.zac.get(client);
            if (client.requiresGooglePlayServices() && ((zal)object).zab(zaaw.zac(this.zaa), client) != 0) {
                zaaw zaaw2 = this.zaa;
                zaaw.zak(zaaw2).zal(new zaan(this, zaaw2, (BaseGmsClient.ConnectionProgressReportCallbacks)object3));
                continue;
            }
            client.connect((BaseGmsClient.ConnectionProgressReportCallbacks)object3);
        }
    }
}

