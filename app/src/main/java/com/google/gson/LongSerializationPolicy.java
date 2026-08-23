/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public abstract class LongSerializationPolicy
extends Enum<LongSerializationPolicy> {
    private static final LongSerializationPolicy[] $VALUES;
    public static final /* enum */ LongSerializationPolicy DEFAULT;
    public static final /* enum */ LongSerializationPolicy STRING;

    static {
        LongSerializationPolicy longSerializationPolicy;
        LongSerializationPolicy longSerializationPolicy2;
        DEFAULT = longSerializationPolicy2 = new LongSerializationPolicy(){

            @Override
            public JsonElement serialize(Long l) {
                return new JsonPrimitive(l);
            }
        };
        STRING = longSerializationPolicy = new LongSerializationPolicy(){

            @Override
            public JsonElement serialize(Long l) {
                return new JsonPrimitive(String.valueOf(l));
            }
        };
        $VALUES = new LongSerializationPolicy[]{longSerializationPolicy2, longSerializationPolicy};
    }

    public static LongSerializationPolicy valueOf(String string2) {
        return Enum.valueOf(LongSerializationPolicy.class, string2);
    }

    public static LongSerializationPolicy[] values() {
        return (LongSerializationPolicy[])$VALUES.clone();
    }

    public abstract JsonElement serialize(Long var1);
}

