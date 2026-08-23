/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.CallableReference;
import kotlin.jvm.internal.FunctionBase;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KCallable;
import kotlin.reflect.KFunction;

public class FunctionReference
extends CallableReference
implements FunctionBase,
KFunction {
    private final int arity;
    private final int flags;

    public FunctionReference(int n) {
        this(n, NO_RECEIVER, null, null, null, 0);
    }

    public FunctionReference(int n, Object object) {
        this(n, object, null, null, null, 0);
    }

    public FunctionReference(int n, Object object, Class clazz, String string2, String string3, int n2) {
        boolean bl = (n2 & 1) == 1;
        super(object, clazz, string2, string3, bl);
        this.arity = n;
        this.flags = n2 >> 1;
    }

    @Override
    protected KCallable computeReflected() {
        return Reflection.function(this);
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (object == this) {
            return true;
        }
        if (object instanceof FunctionReference) {
            object = (FunctionReference)object;
            if (!(Intrinsics.areEqual(this.getOwner(), ((CallableReference)object).getOwner()) && this.getName().equals(((CallableReference)object).getName()) && this.getSignature().equals(((CallableReference)object).getSignature()) && this.flags == ((FunctionReference)object).flags && this.arity == ((FunctionReference)object).arity && Intrinsics.areEqual(this.getBoundReceiver(), ((CallableReference)object).getBoundReceiver()))) {
                bl = false;
            }
            return bl;
        }
        if (object instanceof KFunction) {
            return object.equals(this.compute());
        }
        return false;
    }

    @Override
    public int getArity() {
        return this.arity;
    }

    @Override
    protected KFunction getReflected() {
        return (KFunction)super.getReflected();
    }

    public int hashCode() {
        int n = this.getOwner() == null ? 0 : this.getOwner().hashCode() * 31;
        return (n + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }

    @Override
    public boolean isExternal() {
        return this.getReflected().isExternal();
    }

    @Override
    public boolean isInfix() {
        return this.getReflected().isInfix();
    }

    @Override
    public boolean isInline() {
        return this.getReflected().isInline();
    }

    @Override
    public boolean isOperator() {
        return this.getReflected().isOperator();
    }

    @Override
    public boolean isSuspend() {
        return this.getReflected().isSuspend();
    }

    public String toString() {
        Object object = this.compute();
        if (object != this) {
            return object.toString();
        }
        if ("<init>".equals(this.getName())) {
            object = "constructor (Kotlin reflection is not available)";
        } else {
            object = new StringBuilder();
            ((StringBuilder)object).append("function ");
            ((StringBuilder)object).append(this.getName());
            ((StringBuilder)object).append(" (Kotlin reflection is not available)");
            object = ((StringBuilder)object).toString();
        }
        return object;
    }
}

