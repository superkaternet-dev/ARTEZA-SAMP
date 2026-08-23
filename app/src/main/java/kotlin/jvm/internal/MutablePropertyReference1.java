/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.MutablePropertyReference;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KCallable;
import kotlin.reflect.KMutableProperty1;
import kotlin.reflect.KProperty1;

public abstract class MutablePropertyReference1
extends MutablePropertyReference
implements KMutableProperty1 {
    public MutablePropertyReference1() {
    }

    public MutablePropertyReference1(Object object) {
        super(object);
    }

    public MutablePropertyReference1(Object object, Class clazz, String string2, String string3, int n) {
        super(object, clazz, string2, string3, n);
    }

    @Override
    protected KCallable computeReflected() {
        return Reflection.mutableProperty1(this);
    }

    @Override
    public Object getDelegate(Object object) {
        return ((KMutableProperty1)this.getReflected()).getDelegate(object);
    }

    @Override
    public KProperty1.Getter getGetter() {
        return ((KMutableProperty1)this.getReflected()).getGetter();
    }

    public KMutableProperty1.Setter getSetter() {
        return ((KMutableProperty1)this.getReflected()).getSetter();
    }

    @Override
    public Object invoke(Object object) {
        return this.get(object);
    }
}

