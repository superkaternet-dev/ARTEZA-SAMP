/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.PropertyReference0;
import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

public class PropertyReference0Impl
extends PropertyReference0 {
    public PropertyReference0Impl(Class clazz, String string2, String string3, int n) {
        super(NO_RECEIVER, clazz, string2, string3, n);
    }

    public PropertyReference0Impl(Object object, Class clazz, String string2, String string3, int n) {
        super(object, clazz, string2, string3, n);
    }

    public PropertyReference0Impl(KDeclarationContainer kDeclarationContainer, String string2, String string3) {
        super(NO_RECEIVER, ((ClassBasedDeclarationContainer)kDeclarationContainer).getJClass(), string2, string3, kDeclarationContainer instanceof KClass ^ 1);
    }

    public Object get() {
        return this.getGetter().call(new Object[0]);
    }
}

