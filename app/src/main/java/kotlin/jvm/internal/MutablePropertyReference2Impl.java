/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.MutablePropertyReference2;
import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

public class MutablePropertyReference2Impl
extends MutablePropertyReference2 {
    public MutablePropertyReference2Impl(Class clazz, String string2, String string3, int n) {
        super(clazz, string2, string3, n);
    }

    public MutablePropertyReference2Impl(KDeclarationContainer kDeclarationContainer, String string2, String string3) {
        super(((ClassBasedDeclarationContainer)kDeclarationContainer).getJClass(), string2, string3, kDeclarationContainer instanceof KClass ^ 1);
    }

    @Override
    public Object get(Object object, Object object2) {
        return this.getGetter().call(object, object2);
    }

    public void set(Object object, Object object2, Object object3) {
        this.getSetter().call(object, object2, object3);
    }
}

