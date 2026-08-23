/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.components;

import com.google.firebase.components.Preconditions;

public final class Dependency {
    private final Class<?> anInterface;
    private final int injection;
    private final int type;

    private Dependency(Class<?> clazz, int n, int n2) {
        this.anInterface = Preconditions.checkNotNull(clazz, "Null dependency anInterface.");
        this.type = n;
        this.injection = n2;
    }

    public static Dependency deferred(Class<?> clazz) {
        return new Dependency(clazz, 0, 2);
    }

    private static String describeInjection(int n) {
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unsupported injection: ");
                stringBuilder.append(n);
                throw new AssertionError((Object)stringBuilder.toString());
            }
            case 2: {
                return "deferred";
            }
            case 1: {
                return "provider";
            }
            case 0: 
        }
        return "direct";
    }

    @Deprecated
    public static Dependency optional(Class<?> clazz) {
        return new Dependency(clazz, 0, 0);
    }

    public static Dependency optionalProvider(Class<?> clazz) {
        return new Dependency(clazz, 0, 1);
    }

    public static Dependency required(Class<?> clazz) {
        return new Dependency(clazz, 1, 0);
    }

    public static Dependency requiredProvider(Class<?> clazz) {
        return new Dependency(clazz, 1, 1);
    }

    public static Dependency setOf(Class<?> clazz) {
        return new Dependency(clazz, 2, 0);
    }

    public static Dependency setOfProvider(Class<?> clazz) {
        return new Dependency(clazz, 2, 1);
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof Dependency;
        boolean bl2 = false;
        if (bl) {
            object = (Dependency)object;
            bl = bl2;
            if (this.anInterface == ((Dependency)object).anInterface) {
                bl = bl2;
                if (this.type == ((Dependency)object).type) {
                    bl = bl2;
                    if (this.injection == ((Dependency)object).injection) {
                        bl = true;
                    }
                }
            }
            return bl;
        }
        return false;
    }

    public Class<?> getInterface() {
        return this.anInterface;
    }

    public int hashCode() {
        return ((0xF4243 ^ this.anInterface.hashCode()) * 1000003 ^ this.type) * 1000003 ^ this.injection;
    }

    public boolean isDeferred() {
        boolean bl = this.injection == 2;
        return bl;
    }

    public boolean isDirectInjection() {
        boolean bl = this.injection == 0;
        return bl;
    }

    public boolean isRequired() {
        int n = this.type;
        boolean bl = true;
        if (n != 1) {
            bl = false;
        }
        return bl;
    }

    public boolean isSet() {
        boolean bl = this.type == 2;
        return bl;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Dependency{anInterface=");
        stringBuilder.append(this.anInterface);
        stringBuilder.append(", type=");
        int n = this.type;
        String string2 = n == 1 ? "required" : (n == 0 ? "optional" : "set");
        stringBuilder.append(string2);
        stringBuilder.append(", injection=");
        stringBuilder.append(Dependency.describeInjection(this.injection));
        return stringBuilder.append("}").toString();
    }
}

