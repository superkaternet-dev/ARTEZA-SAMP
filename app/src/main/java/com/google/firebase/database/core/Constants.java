/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.snapshot.ChildKey;

public class Constants {
    public static final ChildKey DOT_INFO = ChildKey.fromString(".info");
    public static final ChildKey DOT_INFO_AUTHENTICATED;
    public static final ChildKey DOT_INFO_CONNECTED;
    public static final ChildKey DOT_INFO_SERVERTIME_OFFSET;
    public static final String WIRE_PROTOCOL_VERSION = "5";

    static {
        DOT_INFO_SERVERTIME_OFFSET = ChildKey.fromString("serverTimeOffset");
        DOT_INFO_AUTHENTICATED = ChildKey.fromString("authenticated");
        DOT_INFO_CONNECTED = ChildKey.fromString("connected");
    }
}

