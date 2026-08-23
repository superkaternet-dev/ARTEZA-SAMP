/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.io.Serializable;
import kotlin.jvm.internal.CallableReference;
import kotlin.jvm.internal.FunctionBase;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KDeclarationContainer;

public class AdaptedFunctionReference
implements FunctionBase,
Serializable {
    private final int arity;
    private final int flags;
    private final boolean isTopLevel;
    private final String name;
    private final Class owner;
    protected final Object receiver;
    private final String signature;

    public AdaptedFunctionReference(int n, Class clazz, String string2, String string3, int n2) {
        this(n, CallableReference.NO_RECEIVER, clazz, string2, string3, n2);
    }

    public AdaptedFunctionReference(int n, Object object, Class clazz, String string2, String string3, int n2) {
        this.receiver = object;
        this.owner = clazz;
        this.name = string2;
        this.signature = string3;
        boolean bl = true;
        if ((n2 & 1) != 1) {
            bl = false;
        }
        this.isTopLevel = bl;
        this.arity = n;
        this.flags = n2 >> 1;
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (this == object) {
            return true;
        }
        if (!(object instanceof AdaptedFunctionReference)) {
            return false;
        }
        object = (AdaptedFunctionReference)object;
        if (!(this.isTopLevel == ((AdaptedFunctionReference)object).isTopLevel && this.arity == ((AdaptedFunctionReference)object).arity && this.flags == ((AdaptedFunctionReference)object).flags && Intrinsics.areEqual(this.receiver, ((AdaptedFunctionReference)object).receiver) && Intrinsics.areEqual(this.owner, ((AdaptedFunctionReference)object).owner) && this.name.equals(((AdaptedFunctionReference)object).name) && this.signature.equals(((AdaptedFunctionReference)object).signature))) {
            bl = false;
        }
        return bl;
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    public KDeclarationContainer getOwner() {
        Object object = this.owner;
        object = object == null ? null : (this.isTopLevel ? Reflection.getOrCreateKotlinPackage((Class)object) : Reflection.getOrCreateKotlinClass((Class)object));
        return object;
    }

    public int hashCode() {
        Object object = this.receiver;
        int n = 0;
        int n2 = object != null ? object.hashCode() : 0;
        object = this.owner;
        if (object != null) {
            n = object.hashCode();
        }
        int n3 = this.name.hashCode();
        int n4 = this.signature.hashCode();
        int n5 = this.isTopLevel ? 1231 : 1237;
        return (((((n2 * 31 + n) * 31 + n3) * 31 + n4) * 31 + n5) * 31 + this.arity) * 31 + this.flags;
    }

    public String toString() {
        return Reflection.renderLambdaToString(this);
    }
}

