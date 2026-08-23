/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.PropertyReference1;
import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

public class PropertyReference1Impl
extends PropertyReference1 {
    public PropertyReference1Impl(Class clazz, String string2, String string3, int n) {
        super(NO_RECEIVER, clazz, string2, string3, n);
    }

    public PropertyReference1Impl(Object object, Class clazz, String string2, String string3, int n) {
        super(object, clazz, string2, string3, n);
    }

    public PropertyReference1Impl(KDeclarationContainer kDeclarationContainer, String string2, String string3) {
        super(NO_RECEIVER, ((ClassBasedDeclarationContainer)kDeclarationContainer).getJClass(), string2, string3, kDeclarationContainer instanceof KClass ^ 1);
    }

    public Object get(Object object) {
        return this.getGetter().call(object);
    }
}

