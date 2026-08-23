/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import kotlin.jvm.internal.CallableReference;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KProperty;

public abstract class PropertyReference
extends CallableReference
implements KProperty {
    public PropertyReference() {
    }

    public PropertyReference(Object object) {
        super(object);
    }

    public PropertyReference(Object object, Class clazz, String string2, String string3, int n) {
        boolean bl = (n & 1) == 1;
        super(object, clazz, string2, string3, bl);
    }

    public boolean equals(Object object) {
        boolean bl = true;
        if (object == this) {
            return true;
        }
        if (object instanceof PropertyReference) {
            object = (PropertyReference)object;
            if (!(this.getOwner().equals(((CallableReference)object).getOwner()) && this.getName().equals(((CallableReference)object).getName()) && this.getSignature().equals(((CallableReference)object).getSignature()) && Intrinsics.areEqual(this.getBoundReceiver(), ((CallableReference)object).getBoundReceiver()))) {
                bl = false;
            }
            return bl;
        }
        if (object instanceof KProperty) {
            return object.equals(this.compute());
        }
        return false;
    }

    @Override
    protected KProperty getReflected() {
        return (KProperty)super.getReflected();
    }

    public int hashCode() {
        return (this.getOwner().hashCode() * 31 + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }

    @Override
    public boolean isConst() {
        return this.getReflected().isConst();
    }

    @Override
    public boolean isLateinit() {
        return this.getReflected().isLateinit();
    }

    public String toString() {
        Object object = this.compute();
        if (object != this) {
            return object.toString();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("property ");
        ((StringBuilder)object).append(this.getName());
        ((StringBuilder)object).append(" (Kotlin reflection is not available)");
        return ((StringBuilder)object).toString();
    }
}

