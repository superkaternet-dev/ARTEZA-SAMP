/*
 * Decompiled with CFR 0.152.
 */
package kotlin.experimental;

import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0010\n\u0000\n\u0002\u0010\u0005\n\u0000\n\u0002\u0010\n\n\u0002\b\u0004\u001a\u0015\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0000\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0001H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0003*\u00020\u0003H\u0087\b\u001a\u0015\u0010\u0005\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0005\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u001a\u0015\u0010\u0006\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0087\f\u001a\u0015\u0010\u0006\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0002\u001a\u00020\u0003H\u0087\f\u00a8\u0006\u0007"}, d2={"and", "", "other", "", "inv", "or", "xor", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class BitwiseOperationsKt {
    private static final byte and(byte by, byte by2) {
        return (byte)(by & by2);
    }

    private static final short and(short s, short s2) {
        return (short)(s & s2);
    }

    private static final byte inv(byte by) {
        return ~by;
    }

    private static final short inv(short s) {
        return ~s;
    }

    private static final byte or(byte by, byte by2) {
        return (byte)(by | by2);
    }

    private static final short or(short s, short s2) {
        return (short)(s | s2);
    }

    private static final byte xor(byte by, byte by2) {
        return (byte)(by ^ by2);
    }

    private static final short xor(short s, short s2) {
        return (short)(s ^ s2);
    }
}

