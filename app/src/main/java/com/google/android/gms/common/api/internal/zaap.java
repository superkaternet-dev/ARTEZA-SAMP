/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.api.internal;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.internal.zaav;
import com.google.android.gms.common.api.internal.zaaw;
import java.util.ArrayList;

final class zaap
extends zaav {
    final zaaw zaa;
    private final ArrayList<Api.Client> zac;

    /*
     * Ignored method signature, as it can't be verified against descriptor
     */
    public zaap(zaaw zaaw2, ArrayList arrayList) {
        this.zaa = zaaw2;
        super(zaaw2, null);
        this.zac = arrayList;
    }

    @Override
    public final void zaa() {
        zaaw zaaw2 = this.zaa;
        zaaw.zak((zaaw)zaaw2).zag.zad = zaaw.zao(zaaw2);
        ArrayList<Api.Client> arrayList = this.zac;
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            Api.Client client = (Api.Client)arrayList.get(i);
            zaaw2 = this.zaa;
            client.getRemoteService(zaaw.zam(zaaw2), zaaw.zak((zaaw)zaaw2).zag.zad);
        }
    }
}

