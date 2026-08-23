/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.PropertyReference;
import kotlin.reflect.KMutableProperty;

public abstract class MutablePropertyReference
extends PropertyReference
implements KMutableProperty {
    public MutablePropertyReference() {
    }

    public MutablePropertyReference(Object object) {
        super(object);
    }

    public MutablePropertyReference(Object object, Class clazz, String string2, String string3, int n) {
        super(object, clazz, string2, string3, n);
    }
}

