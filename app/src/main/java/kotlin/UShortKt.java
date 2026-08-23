/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.UShort;

@Metadata(bv={1, 0, 3}, d1={"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\n\n\u0002\b\u0002\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0003\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0004H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0005\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u0006H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\n"}, d2={"toUShort", "Lkotlin/UShort;", "", "(B)S", "", "(I)S", "", "(J)S", "", "(S)S", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class UShortKt {
    private static final short toUShort(byte by) {
        return UShort.constructor-impl(by);
    }

    private static final short toUShort(int n) {
        return UShort.constructor-impl((short)n);
    }

    private static final short toUShort(long l) {
        return UShort.constructor-impl((short)l);
    }

    private static final short toUShort(short s) {
        return UShort.constructor-impl(s);
    }
}

