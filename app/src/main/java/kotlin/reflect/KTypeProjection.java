/*
 * Decompiled with CFR 0.152.
 */
package kotlin.reflect;

import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KType;
import kotlin.reflect.KTypeProjection$WhenMappings;
import kotlin.reflect.KVariance;

@Metadata(bv={1, 0, 3}, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0087\b\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B\u0019\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\u0002\u0010\u0006J\u000b\u0010\u000b\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\f\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J!\u0010\r\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\b\u0010\u0013\u001a\u00020\u0014H\u0016R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0016"}, d2={"Lkotlin/reflect/KTypeProjection;", "", "variance", "Lkotlin/reflect/KVariance;", "type", "Lkotlin/reflect/KType;", "(Lkotlin/reflect/KVariance;Lkotlin/reflect/KType;)V", "getType", "()Lkotlin/reflect/KType;", "getVariance", "()Lkotlin/reflect/KVariance;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Companion", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class KTypeProjection {
    public static final Companion Companion = new Companion(null);
    public static final KTypeProjection star = new KTypeProjection(null, null);
    private final KType type;
    private final KVariance variance;

    public KTypeProjection(KVariance object, KType object2) {
        this.variance = object;
        this.type = object2;
        boolean bl = true;
        boolean bl2 = object == null;
        boolean bl3 = object2 == null;
        bl2 = bl2 == bl3 ? bl : false;
        if (!bl2) {
            if (object == null) {
                object = "Star projection must have no type specified.";
            } else {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("The projection variance ");
                ((StringBuilder)object2).append(object);
                ((StringBuilder)object2).append(" requires type to be specified.");
                object = ((StringBuilder)object2).toString();
            }
            throw (Throwable)new IllegalArgumentException(object.toString());
        }
    }

    @JvmStatic
    public static final KTypeProjection contravariant(KType kType) {
        return Companion.contravariant(kType);
    }

    public static /* synthetic */ KTypeProjection copy$default(KTypeProjection kTypeProjection, KVariance kVariance, KType kType, int n, Object object) {
        if ((n & 1) != 0) {
            kVariance = kTypeProjection.variance;
        }
        if ((n & 2) != 0) {
            kType = kTypeProjection.type;
        }
        return kTypeProjection.copy(kVariance, kType);
    }

    @JvmStatic
    public static final KTypeProjection covariant(KType kType) {
        return Companion.covariant(kType);
    }

    @JvmStatic
    public static final KTypeProjection invariant(KType kType) {
        return Companion.invariant(kType);
    }

    public final KVariance component1() {
        return this.variance;
    }

    public final KType component2() {
        return this.type;
    }

    public final KTypeProjection copy(KVariance kVariance, KType kType) {
        return new KTypeProjection(kVariance, kType);
    }

    public boolean equals(Object object) {
        block2: {
            block3: {
                if (this == object) break block2;
                if (!(object instanceof KTypeProjection)) break block3;
                object = (KTypeProjection)object;
                if (Intrinsics.areEqual((Object)this.variance, (Object)((KTypeProjection)object).variance) && Intrinsics.areEqual(this.type, ((KTypeProjection)object).type)) break block2;
            }
            return false;
        }
        return true;
    }

    public final KType getType() {
        return this.type;
    }

    public final KVariance getVariance() {
        return this.variance;
    }

    public int hashCode() {
        Object object = this.variance;
        int n = 0;
        int n2 = object != null ? object.hashCode() : 0;
        object = this.type;
        if (object != null) {
            n = object.hashCode();
        }
        return n2 * 31 + n;
    }

    public String toString() {
        Object object = this.variance;
        if (object == null) {
            object = "*";
        } else {
            switch (KTypeProjection$WhenMappings.$EnumSwitchMapping$0[object.ordinal()]) {
                default: {
                    throw new NoWhenBranchMatchedException();
                }
                case 3: {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("out ");
                    ((StringBuilder)object).append(this.type);
                    object = ((StringBuilder)object).toString();
                    break;
                }
                case 2: {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("in ");
                    ((StringBuilder)object).append(this.type);
                    object = ((StringBuilder)object).toString();
                    break;
                }
                case 1: {
                    object = String.valueOf(this.type);
                }
            }
        }
        return object;
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\t\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\f\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007J\u0010\u0010\r\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000bH\u0007R\u0011\u0010\u0003\u001a\u00020\u00048F\u00a2\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0016\u0010\u0007\u001a\u00020\u00048\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\b\u0010\u0002\u00a8\u0006\u000e"}, d2={"Lkotlin/reflect/KTypeProjection$Companion;", "", "()V", "STAR", "Lkotlin/reflect/KTypeProjection;", "getSTAR", "()Lkotlin/reflect/KTypeProjection;", "star", "getStar$annotations", "contravariant", "type", "Lkotlin/reflect/KType;", "covariant", "invariant", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getStar$annotations() {
        }

        @JvmStatic
        public final KTypeProjection contravariant(KType kType) {
            Intrinsics.checkNotNullParameter(kType, "type");
            return new KTypeProjection(KVariance.IN, kType);
        }

        @JvmStatic
        public final KTypeProjection covariant(KType kType) {
            Intrinsics.checkNotNullParameter(kType, "type");
            return new KTypeProjection(KVariance.OUT, kType);
        }

        public final KTypeProjection getSTAR() {
            return star;
        }

        @JvmStatic
        public final KTypeProjection invariant(KType kType) {
            Intrinsics.checkNotNullParameter(kType, "type");
            return new KTypeProjection(KVariance.INVARIANT, kType);
        }
    }
}

