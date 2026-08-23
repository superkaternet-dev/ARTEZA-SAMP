/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.NumbersKt;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000&\n\u0000\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b)\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0003\u0010\u0004\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0007\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a\u0017\u0010\u0000\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0004\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u0007\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\n\u001a\u0017\u0010\u000e\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\r\u001a\u0017\u0010\u0013\u001a\u00020\u0001*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0004\u001a\u0017\u0010\u0013\u001a\u00020\u0001*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0007\u001a\u0017\u0010\u0013\u001a\u00020\u0001*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0016\u0010\n\u001a\u0017\u0010\u0013\u001a\u00020\u0001*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\r\u001a\u001f\u0010\u0018\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\u001b\u001a\u001f\u0010\u0018\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001d\u001a\u001f\u0010\u0018\u001a\u00020\b*\u00020\b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001f\u001a\u001f\u0010\u0018\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010!\u001a\u001f\u0010\"\u001a\u00020\u0002*\u00020\u00022\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010\u001b\u001a\u001f\u0010\"\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b$\u0010\u001d\u001a\u001f\u0010\"\u001a\u00020\b*\u00020\b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b%\u0010\u001f\u001a\u001f\u0010\"\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0019\u001a\u00020\u0001H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b&\u0010!\u001a\u0017\u0010'\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b(\u0010)\u001a\u0017\u0010'\u001a\u00020\u0005*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b*\u0010\u0007\u001a\u0017\u0010'\u001a\u00020\b*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b+\u0010,\u001a\u0017\u0010'\u001a\u00020\u000b*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b-\u0010.\u001a\u0017\u0010/\u001a\u00020\u0002*\u00020\u0002H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b0\u0010)\u001a\u0017\u0010/\u001a\u00020\u0005*\u00020\u0005H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b1\u0010\u0007\u001a\u0017\u0010/\u001a\u00020\b*\u00020\bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b2\u0010,\u001a\u0017\u0010/\u001a\u00020\u000b*\u00020\u000bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b3\u0010.\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00064"}, d2={"countLeadingZeroBits", "", "Lkotlin/UByte;", "countLeadingZeroBits-7apg3OU", "(B)I", "Lkotlin/UInt;", "countLeadingZeroBits-WZ4Q5Ns", "(I)I", "Lkotlin/ULong;", "countLeadingZeroBits-VKZWuLQ", "(J)I", "Lkotlin/UShort;", "countLeadingZeroBits-xj2QHRw", "(S)I", "countOneBits", "countOneBits-7apg3OU", "countOneBits-WZ4Q5Ns", "countOneBits-VKZWuLQ", "countOneBits-xj2QHRw", "countTrailingZeroBits", "countTrailingZeroBits-7apg3OU", "countTrailingZeroBits-WZ4Q5Ns", "countTrailingZeroBits-VKZWuLQ", "countTrailingZeroBits-xj2QHRw", "rotateLeft", "bitCount", "rotateLeft-LxnNnR4", "(BI)B", "rotateLeft-V7xB4Y4", "(II)I", "rotateLeft-JSWoG40", "(JI)J", "rotateLeft-olVBNx4", "(SI)S", "rotateRight", "rotateRight-LxnNnR4", "rotateRight-V7xB4Y4", "rotateRight-JSWoG40", "rotateRight-olVBNx4", "takeHighestOneBit", "takeHighestOneBit-7apg3OU", "(B)B", "takeHighestOneBit-WZ4Q5Ns", "takeHighestOneBit-VKZWuLQ", "(J)J", "takeHighestOneBit-xj2QHRw", "(S)S", "takeLowestOneBit", "takeLowestOneBit-7apg3OU", "takeLowestOneBit-WZ4Q5Ns", "takeLowestOneBit-VKZWuLQ", "takeLowestOneBit-xj2QHRw", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class UNumbersKt {
    private static final int countLeadingZeroBits-7apg3OU(byte by) {
        return Integer.numberOfLeadingZeros(by & 0xFF) - 24;
    }

    private static final int countLeadingZeroBits-VKZWuLQ(long l) {
        return Long.numberOfLeadingZeros(l);
    }

    private static final int countLeadingZeroBits-WZ4Q5Ns(int n) {
        return Integer.numberOfLeadingZeros(n);
    }

    private static final int countLeadingZeroBits-xj2QHRw(short s) {
        return Integer.numberOfLeadingZeros(0xFFFF & s) - 16;
    }

    private static final int countOneBits-7apg3OU(byte by) {
        return Integer.bitCount(UInt.constructor-impl(by & 0xFF));
    }

    private static final int countOneBits-VKZWuLQ(long l) {
        return Long.bitCount(l);
    }

    private static final int countOneBits-WZ4Q5Ns(int n) {
        return Integer.bitCount(n);
    }

    private static final int countOneBits-xj2QHRw(short s) {
        return Integer.bitCount(UInt.constructor-impl(0xFFFF & s));
    }

    private static final int countTrailingZeroBits-7apg3OU(byte by) {
        return Integer.numberOfTrailingZeros(by | 0x100);
    }

    private static final int countTrailingZeroBits-VKZWuLQ(long l) {
        return Long.numberOfTrailingZeros(l);
    }

    private static final int countTrailingZeroBits-WZ4Q5Ns(int n) {
        return Integer.numberOfTrailingZeros(n);
    }

    private static final int countTrailingZeroBits-xj2QHRw(short s) {
        return Integer.numberOfTrailingZeros(0x10000 | s);
    }

    private static final long rotateLeft-JSWoG40(long l, int n) {
        return ULong.constructor-impl(Long.rotateLeft(l, n));
    }

    private static final byte rotateLeft-LxnNnR4(byte by, int n) {
        return UByte.constructor-impl(NumbersKt.rotateLeft(by, n));
    }

    private static final int rotateLeft-V7xB4Y4(int n, int n2) {
        return UInt.constructor-impl(Integer.rotateLeft(n, n2));
    }

    private static final short rotateLeft-olVBNx4(short s, int n) {
        return UShort.constructor-impl(NumbersKt.rotateLeft(s, n));
    }

    private static final long rotateRight-JSWoG40(long l, int n) {
        return ULong.constructor-impl(Long.rotateRight(l, n));
    }

    private static final byte rotateRight-LxnNnR4(byte by, int n) {
        return UByte.constructor-impl(NumbersKt.rotateRight(by, n));
    }

    private static final int rotateRight-V7xB4Y4(int n, int n2) {
        return UInt.constructor-impl(Integer.rotateRight(n, n2));
    }

    private static final short rotateRight-olVBNx4(short s, int n) {
        return UShort.constructor-impl(NumbersKt.rotateRight(s, n));
    }

    private static final byte takeHighestOneBit-7apg3OU(byte by) {
        return UByte.constructor-impl((byte)Integer.highestOneBit(by & 0xFF));
    }

    private static final long takeHighestOneBit-VKZWuLQ(long l) {
        return ULong.constructor-impl(Long.highestOneBit(l));
    }

    private static final int takeHighestOneBit-WZ4Q5Ns(int n) {
        return UInt.constructor-impl(Integer.highestOneBit(n));
    }

    private static final short takeHighestOneBit-xj2QHRw(short s) {
        return UShort.constructor-impl((short)Integer.highestOneBit(0xFFFF & s));
    }

    private static final byte takeLowestOneBit-7apg3OU(byte by) {
        return UByte.constructor-impl((byte)Integer.lowestOneBit(by & 0xFF));
    }

    private static final long takeLowestOneBit-VKZWuLQ(long l) {
        return ULong.constructor-impl(Long.lowestOneBit(l));
    }

    private static final int takeLowestOneBit-WZ4Q5Ns(int n) {
        return UInt.constructor-impl(Integer.lowestOneBit(n));
    }

    private static final short takeLowestOneBit-xj2QHRw(short s) {
        return UShort.constructor-impl((short)Integer.lowestOneBit(0xFFFF & s));
    }
}

