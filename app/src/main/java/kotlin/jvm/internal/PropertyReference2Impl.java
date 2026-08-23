/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.PropertyReference2;
import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

public class PropertyReference2Impl
extends PropertyReference2 {
    public PropertyReference2Impl(Class clazz, String string2, String string3, int n) {
        super(clazz, string2, string3, n);
    }

    public PropertyReference2Impl(KDeclarationContainer kDeclarationContainer, String string2, String string3) {
        super(((ClassBasedDeclarationContainer)kDeclarationContainer).getJClass(), string2, string3, kDeclarationContainer instanceof KClass ^ 1);
    }

    public Object get(Object object, Object object2) {
        return this.getGetter().call(object, object2);
    }
}

