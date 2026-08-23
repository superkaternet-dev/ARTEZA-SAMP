/*
 * Decompiled with CFR 0.152.
 */
package kotlin.contracts;

import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, d1={"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0087\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2={"Lkotlin/contracts/InvocationKind;", "", "(Ljava/lang/String;I)V", "AT_MOST_ONCE", "AT_LEAST_ONCE", "EXACTLY_ONCE", "UNKNOWN", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class InvocationKind
extends Enum<InvocationKind> {
    private static final InvocationKind[] $VALUES;
    public static final /* enum */ InvocationKind AT_LEAST_ONCE;
    public static final /* enum */ InvocationKind AT_MOST_ONCE;
    public static final /* enum */ InvocationKind EXACTLY_ONCE;
    public static final /* enum */ InvocationKind UNKNOWN;

    static {
        InvocationKind invocationKind;
        InvocationKind invocationKind2;
        InvocationKind invocationKind3;
        InvocationKind invocationKind4;
        AT_MOST_ONCE = invocationKind4 = new InvocationKind();
        AT_LEAST_ONCE = invocationKind3 = new InvocationKind();
        EXACTLY_ONCE = invocationKind2 = new InvocationKind();
        UNKNOWN = invocationKind = new InvocationKind();
        $VALUES = new InvocationKind[]{invocationKind4, invocationKind3, invocationKind2, invocationKind};
    }

    public static InvocationKind valueOf(String string2) {
        return Enum.valueOf(InvocationKind.class, string2);
    }

    public static InvocationKind[] values() {
        return (InvocationKind[])$VALUES.clone();
    }
}

