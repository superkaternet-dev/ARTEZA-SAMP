/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.MutablePropertyReference0;
import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

public class MutablePropertyReference0Impl
extends MutablePropertyReference0 {
    public MutablePropertyReference0Impl(Class clazz, String string2, String string3, int n) {
        super(NO_RECEIVER, clazz, string2, string3, n);
    }

    public MutablePropertyReference0Impl(Object object, Class clazz, String string2, String string3, int n) {
        super(object, clazz, string2, string3, n);
    }

    public MutablePropertyReference0Impl(KDeclarationContainer kDeclarationContainer, String string2, String string3) {
        super(NO_RECEIVER, ((ClassBasedDeclarationContainer)kDeclarationContainer).getJClass(), string2, string3, kDeclarationContainer instanceof KClass ^ 1);
    }

    @Override
    public Object get() {
        return this.getGetter().call(new Object[0]);
    }

    public void set(Object object) {
        this.getSetter().call(object);
    }
}

