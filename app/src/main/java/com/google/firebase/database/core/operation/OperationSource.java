/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.operation;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.view.QueryParams;

public class OperationSource {
    public static final OperationSource SERVER;
    public static final OperationSource USER;
    private final QueryParams queryParams;
    private final Source source;
    private final boolean tagged;

    static {
        USER = new OperationSource(Source.User, null, false);
        SERVER = new OperationSource(Source.Server, null, false);
    }

    public OperationSource(Source source, QueryParams queryParams, boolean bl) {
        this.source = source;
        this.queryParams = queryParams;
        this.tagged = bl;
        bl = !bl || this.isFromServer();
        Utilities.hardAssert(bl);
    }

    public static OperationSource forServerTaggedQuery(QueryParams queryParams) {
        return new OperationSource(Source.Server, queryParams, true);
    }

    public QueryParams getQueryParams() {
        return this.queryParams;
    }

    public boolean isFromServer() {
        boolean bl = this.source == Source.Server;
        return bl;
    }

    public boolean isFromUser() {
        boolean bl = this.source == Source.User;
        return bl;
    }

    public boolean isTagged() {
        return this.tagged;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OperationSource{source=");
        stringBuilder.append((Object)this.source);
        stringBuilder.append(", queryParams=");
        stringBuilder.append(this.queryParams);
        stringBuilder.append(", tagged=");
        stringBuilder.append(this.tagged);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    private static final class Source
    extends Enum<Source> {
        private static final Source[] $VALUES;
        public static final /* enum */ Source Server;
        public static final /* enum */ Source User;

        static {
            Source source;
            Source source2;
            User = source2 = new Source();
            Server = source = new Source();
            $VALUES = new Source[]{source2, source};
        }

        public static Source valueOf(String string2) {
            return Enum.valueOf(Source.class, string2);
        }

        public static Source[] values() {
            return (Source[])$VALUES.clone();
        }
    }
}

