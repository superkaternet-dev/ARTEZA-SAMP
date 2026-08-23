/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.MutablePropertyReference1;
import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

public class MutablePropertyReference1Impl
extends MutablePropertyReference1 {
    public MutablePropertyReference1Impl(Class clazz, String string2, String string3, int n) {
        super(NO_RECEIVER, clazz, string2, string3, n);
    }

    public MutablePropertyReference1Impl(Object object, Class clazz, String string2, String string3, int n) {
        super(object, clazz, string2, string3, n);
    }

    public MutablePropertyReference1Impl(KDeclarationContainer kDeclarationContainer, String string2, String string3) {
        super(NO_RECEIVER, ((ClassBasedDeclarationContainer)kDeclarationContainer).getJClass(), string2, string3, kDeclarationContainer instanceof KClass ^ 1);
    }

    @Override
    public Object get(Object object) {
        return this.getGetter().call(object);
    }

    public void set(Object object, Object object2) {
        this.getSetter().call(object, object2);
    }
}

