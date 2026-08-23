/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.NumbersKt__NumbersJVMKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0005\n\u0002\u0010\n\n\u0002\b\b\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0003H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0004\u001a\u00020\u0001*\u00020\u0003H\u0087\b\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u001a\r\u0010\u0005\u001a\u00020\u0001*\u00020\u0003H\u0087\b\u001a\u0014\u0010\u0006\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\u0014\u0010\u0006\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\u0014\u0010\b\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\u0014\u0010\b\u001a\u00020\u0003*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0001H\u0007\u001a\r\u0010\t\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\t\u001a\u00020\u0003*\u00020\u0003H\u0087\b\u001a\r\u0010\n\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u001a\r\u0010\n\u001a\u00020\u0003*\u00020\u0003H\u0087\b\u00a8\u0006\u000b"}, d2={"countLeadingZeroBits", "", "", "", "countOneBits", "countTrailingZeroBits", "rotateLeft", "bitCount", "rotateRight", "takeHighestOneBit", "takeLowestOneBit", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/NumbersKt")
class NumbersKt__NumbersKt
extends NumbersKt__NumbersJVMKt {
    private static final int countLeadingZeroBits(byte by) {
        return Integer.numberOfLeadingZeros(by & 0xFF) - 24;
    }

    private static final int countLeadingZeroBits(short s) {
        return Integer.numberOfLeadingZeros(0xFFFF & s) - 16;
    }

    private static final int countOneBits(byte by) {
        return Integer.bitCount(by & 0xFF);
    }

    private static final int countOneBits(short s) {
        return Integer.bitCount(0xFFFF & s);
    }

    private static final int countTrailingZeroBits(byte by) {
        return Integer.numberOfTrailingZeros(by | 0x100);
    }

    private static final int countTrailingZeroBits(short s) {
        return Integer.numberOfTrailingZeros(0x10000 | s);
    }

    public static final byte rotateLeft(byte by, int n) {
        return (byte)(by << (n & 7) | (by & 0xFF) >>> 8 - (n & 7));
    }

    public static final short rotateLeft(short s, int n) {
        return (short)(s << (n & 0xF) | (0xFFFF & s) >>> 16 - (n & 0xF));
    }

    public static final byte rotateRight(byte by, int n) {
        return (byte)(by << 8 - (n & 7) | (by & 0xFF) >>> (n & 7));
    }

    public static final short rotateRight(short s, int n) {
        return (short)(s << 16 - (n & 0xF) | (0xFFFF & s) >>> (n & 0xF));
    }

    private static final byte takeHighestOneBit(byte by) {
        return (byte)Integer.highestOneBit(by & 0xFF);
    }

    private static final short takeHighestOneBit(short s) {
        return (short)Integer.highestOneBit(0xFFFF & s);
    }

    private static final byte takeLowestOneBit(byte by) {
        return (byte)Integer.lowestOneBit(by);
    }

    private static final short takeLowestOneBit(short s) {
        return (short)Integer.lowestOneBit(s);
    }
}

