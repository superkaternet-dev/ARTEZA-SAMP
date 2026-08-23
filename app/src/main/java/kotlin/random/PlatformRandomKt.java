/*
 * Decompiled with CFR 0.152.
 */
package kotlin.random;

import kotlin.Metadata;
import kotlin.internal.PlatformImplementationsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.AbstractPlatformRandom;
import kotlin.random.KotlinRandom;
import kotlin.random.PlatformRandom;
import kotlin.random.Random;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\t\u0010\u0000\u001a\u00020\u0001H\u0081\b\u001a\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0005H\u0000\u001a\f\u0010\u0007\u001a\u00020\b*\u00020\u0001H\u0007\u001a\f\u0010\t\u001a\u00020\u0001*\u00020\bH\u0007\u00a8\u0006\n"}, d2={"defaultPlatformRandom", "Lkotlin/random/Random;", "doubleFromParts", "", "hi26", "", "low27", "asJavaRandom", "Ljava/util/Random;", "asKotlinRandom", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class PlatformRandomKt {
    public static final java.util.Random asJavaRandom(Random object) {
        Intrinsics.checkNotNullParameter(object, "$this$asJavaRandom");
        Object object2 = !(object instanceof AbstractPlatformRandom) ? null : object;
        object2 = (AbstractPlatformRandom)object2;
        object = object2 != null && (object2 = ((AbstractPlatformRandom)object2).getImpl()) != null ? object2 : (java.util.Random)new KotlinRandom((Random)object);
        return object;
    }

    public static final Random asKotlinRandom(java.util.Random object) {
        Intrinsics.checkNotNullParameter(object, "$this$asKotlinRandom");
        Object object2 = !(object instanceof KotlinRandom) ? null : object;
        object2 = (KotlinRandom)object2;
        object = object2 != null && (object2 = ((KotlinRandom)object2).getImpl()) != null ? object2 : (Random)new PlatformRandom((java.util.Random)object);
        return object;
    }

    private static final Random defaultPlatformRandom() {
        return PlatformImplementationsKt.IMPLEMENTATIONS.defaultPlatformRandom();
    }

    public static final double doubleFromParts(int n, int n2) {
        double d = ((long)n << 27) + (long)n2;
        double d2 = 0x20000000000000L;
        Double.isNaN(d);
        Double.isNaN(d2);
        return d / d2;
    }
}

