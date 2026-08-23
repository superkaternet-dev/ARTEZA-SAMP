/*
 * Decompiled with CFR 0.152.
 */
package kotlin.coroutines.intrinsics;

import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0081\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2={"Lkotlin/coroutines/intrinsics/CoroutineSingletons;", "", "(Ljava/lang/String;I)V", "COROUTINE_SUSPENDED", "UNDECIDED", "RESUMED", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class CoroutineSingletons
extends Enum<CoroutineSingletons> {
    private static final CoroutineSingletons[] $VALUES;
    public static final /* enum */ CoroutineSingletons COROUTINE_SUSPENDED;
    public static final /* enum */ CoroutineSingletons RESUMED;
    public static final /* enum */ CoroutineSingletons UNDECIDED;

    static {
        CoroutineSingletons coroutineSingletons;
        CoroutineSingletons coroutineSingletons2;
        CoroutineSingletons coroutineSingletons3;
        COROUTINE_SUSPENDED = coroutineSingletons3 = new CoroutineSingletons();
        UNDECIDED = coroutineSingletons2 = new CoroutineSingletons();
        RESUMED = coroutineSingletons = new CoroutineSingletons();
        $VALUES = new CoroutineSingletons[]{coroutineSingletons3, coroutineSingletons2, coroutineSingletons};
    }

    public static CoroutineSingletons valueOf(String string2) {
        return Enum.valueOf(CoroutineSingletons.class, string2);
    }

    public static CoroutineSingletons[] values() {
        return (CoroutineSingletons[])$VALUES.clone();
    }
}

