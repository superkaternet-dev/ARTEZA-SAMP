/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import org.ini4j.Registry;

public class TypeValuesPair {
    private final Registry.Type _type;
    private final String[] _values;

    public TypeValuesPair(Registry.Type type, String[] stringArray) {
        this._type = type;
        this._values = stringArray;
    }

    public Registry.Type getType() {
        return this._type;
    }

    public String[] getValues() {
        return this._values;
    }
}

