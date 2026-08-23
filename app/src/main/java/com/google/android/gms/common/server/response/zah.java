/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.server.response;

import com.google.android.gms.common.server.response.FastParser;
import com.google.android.gms.common.server.response.zai;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;

final class zah
implements zai<BigDecimal> {
    zah() {
    }

    @Override
    public final /* synthetic */ Object zaa(FastParser fastParser, BufferedReader bufferedReader) throws FastParser.ParseException, IOException {
        return FastParser.zaf(fastParser, bufferedReader);
    }
}

