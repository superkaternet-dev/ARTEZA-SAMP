/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.heartbeatinfo;

import com.google.firebase.components.Component;
import com.google.firebase.heartbeatinfo.HeartBeatConsumer;

public class HeartBeatConsumerComponent {
    private HeartBeatConsumerComponent() {
    }

    public static Component<?> create() {
        return Component.intoSet(new HeartBeatConsumer(){}, HeartBeatConsumer.class);
    }
}

