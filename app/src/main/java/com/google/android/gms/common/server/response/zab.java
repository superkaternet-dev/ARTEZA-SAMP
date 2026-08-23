/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import com.google.android.gms.common.server.response.zai;
import java.io.BufferedReader;
import java.io.IOException;

final class zab
implements zai<Long> {
    zab() {
    }

    @Override
    public final /* synthetic */ Object zaa(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        return FastParser.zad(fastParser, bufferedReader);
    }
}

