/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.PropertyReference;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty0;

public abstract class PropertyReference0
extends PropertyReference
implements KProperty0 {
    public PropertyReference0() {
    }

    public PropertyReference0(Object object) {
        super(object);
    }

    public PropertyReference0(Object object, Class clazz, String string2, String string3, int n) {
        super(object, clazz, string2, string3, n);
    }

    @Override
    protected KCallable computeReflected() {
        return Reflection.property0(this);
    }

    @Override
    public Object getDelegate() {
        return ((KProperty0)this.getReflected()).getDelegate();
    }

    public KProperty0.Getter getGetter() {
        return ((KProperty0)this.getReflected()).getGetter();
    }

    @Override
    public Object invoke() {
        return this.get();
    }
}

