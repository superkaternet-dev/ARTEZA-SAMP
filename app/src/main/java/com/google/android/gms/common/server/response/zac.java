/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import com.google.android.gms.common.server.response.zai;
import java.io.BufferedReader;
import java.io.IOException;

final class zac
implements zai<Float> {
    zac() {
    }

    @Override
    public final /* synthetic */ Object zaa(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        return Float.valueOf(FastParser.zab(fastParser, bufferedReader));
    }
}

