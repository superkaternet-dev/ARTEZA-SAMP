/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.ClassBasedDeclarationContainer;
import kotlin.jvm.internal.FunctionReference;
import kotlin.reflect.KClass;
import kotlin.reflect.KDeclarationContainer;

public class FunctionReferenceImpl
extends FunctionReference {
    public FunctionReferenceImpl(int n, Class clazz, String string2, String string3, int n2) {
        super(n, NO_RECEIVER, clazz, string2, string3, n2);
    }

    public FunctionReferenceImpl(int n, Object object, Class clazz, String string2, String string3, int n2) {
        super(n, object, clazz, string2, string3, n2);
    }

    public FunctionReferenceImpl(int n, KDeclarationContainer kDeclarationContainer, String string2, String string3) {
        super(n, NO_RECEIVER, ((ClassBasedDeclarationContainer)kDeclarationContainer).getJClass(), string2, string3, kDeclarationContainer instanceof KClass ^ 1);
    }
}

