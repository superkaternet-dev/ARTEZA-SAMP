/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.UIntRange;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000n\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\b\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0015\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 m2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001mB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000e\u0010\u000fJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0000H\u0097\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u000bJ\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016J\u0016\u0010\u0017\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0018\u0010\u0005J\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001a\u0010\u000fJ\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001b\u0010\u000bJ\u001b\u0010\u0019\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001dJ\u001b\u0010\u0019\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u0016J\u001a\u0010\u001f\u001a\u00020 2\b\u0010\t\u001a\u0004\u0018\u00010!H\u00d6\u0003\u00a2\u0006\u0004\b\"\u0010#J\u0010\u0010$\u001a\u00020\u0003H\u00d6\u0001\u00a2\u0006\u0004\b%\u0010\u0005J\u0016\u0010&\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b'\u0010\u0005J\u0016\u0010(\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b)\u0010\u0005J\u001b\u0010*\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b+\u0010\u000fJ\u001b\u0010*\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b,\u0010\u000bJ\u001b\u0010*\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b-\u0010\u001dJ\u001b\u0010*\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b.\u0010\u0016J\u001b\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\b0\u0010\u000bJ\u001b\u00101\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b2\u0010\u000fJ\u001b\u00101\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b3\u0010\u000bJ\u001b\u00101\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b4\u0010\u001dJ\u001b\u00101\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b5\u0010\u0016J\u001b\u00106\u001a\u0002072\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b8\u00109J\u001b\u0010:\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b;\u0010\u000fJ\u001b\u0010:\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b<\u0010\u000bJ\u001b\u0010:\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b=\u0010\u001dJ\u001b\u0010:\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b>\u0010\u0016J\u001e\u0010?\u001a\u00020\u00002\u0006\u0010@\u001a\u00020\u0003H\u0087\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bA\u0010\u000bJ\u001e\u0010B\u001a\u00020\u00002\u0006\u0010@\u001a\u00020\u0003H\u0087\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bC\u0010\u000bJ\u001b\u0010D\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\rH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bE\u0010\u000fJ\u001b\u0010D\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bF\u0010\u000bJ\u001b\u0010D\u001a\u00020\u00112\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bG\u0010\u001dJ\u001b\u0010D\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0014H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bH\u0010\u0016J\u0010\u0010I\u001a\u00020JH\u0087\b\u00a2\u0006\u0004\bK\u0010LJ\u0010\u0010M\u001a\u00020NH\u0087\b\u00a2\u0006\u0004\bO\u0010PJ\u0010\u0010Q\u001a\u00020RH\u0087\b\u00a2\u0006\u0004\bS\u0010TJ\u0010\u0010U\u001a\u00020\u0003H\u0087\b\u00a2\u0006\u0004\bV\u0010\u0005J\u0010\u0010W\u001a\u00020XH\u0087\b\u00a2\u0006\u0004\bY\u0010ZJ\u0010\u0010[\u001a\u00020\\H\u0087\b\u00a2\u0006\u0004\b]\u0010^J\u000f\u0010_\u001a\u00020`H\u0016\u00a2\u0006\u0004\ba\u0010bJ\u0016\u0010c\u001a\u00020\rH\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bd\u0010LJ\u0016\u0010e\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bf\u0010\u0005J\u0016\u0010g\u001a\u00020\u0011H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bh\u0010ZJ\u0016\u0010i\u001a\u00020\u0014H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bj\u0010^J\u001b\u0010k\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\bl\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006n"}, d2={"Lkotlin/UInt;", "", "data", "", "constructor-impl", "(I)I", "getData$annotations", "()V", "and", "other", "and-WZ4Q5Ns", "(II)I", "compareTo", "Lkotlin/UByte;", "compareTo-7apg3OU", "(IB)I", "compareTo-WZ4Q5Ns", "Lkotlin/ULong;", "compareTo-VKZWuLQ", "(IJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(IS)I", "dec", "dec-pVg5ArA", "div", "div-7apg3OU", "div-WZ4Q5Ns", "div-VKZWuLQ", "(IJ)J", "div-xj2QHRw", "equals", "", "", "equals-impl", "(ILjava/lang/Object;)Z", "hashCode", "hashCode-impl", "inc", "inc-pVg5ArA", "inv", "inv-pVg5ArA", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "or", "or-WZ4Q5Ns", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/UIntRange;", "rangeTo-WZ4Q5Ns", "(II)Lkotlin/ranges/UIntRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-pVg5ArA", "shr", "shr-pVg5ArA", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(I)B", "toDouble", "", "toDouble-impl", "(I)D", "toFloat", "", "toFloat-impl", "(I)F", "toInt", "toInt-impl", "toLong", "", "toLong-impl", "(I)J", "toShort", "", "toShort-impl", "(I)S", "toString", "", "toString-impl", "(I)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-WZ4Q5Ns", "Companion", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class UInt
implements Comparable<UInt> {
    public static final Companion Companion = new Companion(null);
    public static final int MAX_VALUE = -1;
    public static final int MIN_VALUE = 0;
    public static final int SIZE_BITS = 32;
    public static final int SIZE_BYTES = 4;
    private final int data;

    private /* synthetic */ UInt(int n) {
        this.data = n;
    }

    private static final int and-WZ4Q5Ns(int n, int n2) {
        return UInt.constructor-impl(n & n2);
    }

    public static final /* synthetic */ UInt box-impl(int n) {
        return new UInt(n);
    }

    private static final int compareTo-7apg3OU(int n, byte by) {
        return UnsignedKt.uintCompare(n, UInt.constructor-impl(by & 0xFF));
    }

    private static final int compareTo-VKZWuLQ(int n, long l) {
        return UnsignedKt.ulongCompare(ULong.constructor-impl((long)n & 0xFFFFFFFFL), l);
    }

    private int compareTo-WZ4Q5Ns(int n) {
        return UInt.compareTo-WZ4Q5Ns(this.data, n);
    }

    private static int compareTo-WZ4Q5Ns(int n, int n2) {
        return UnsignedKt.uintCompare(n, n2);
    }

    private static final int compareTo-xj2QHRw(int n, short s) {
        return UnsignedKt.uintCompare(n, UInt.constructor-impl(0xFFFF & s));
    }

    public static int constructor-impl(int n) {
        return n;
    }

    private static final int dec-pVg5ArA(int n) {
        return UInt.constructor-impl(n - 1);
    }

    private static final int div-7apg3OU(int n, byte by) {
        return UnsignedKt.uintDivide-J1ME1BU(n, UInt.constructor-impl(by & 0xFF));
    }

    private static final long div-VKZWuLQ(int n, long l) {
        return UnsignedKt.ulongDivide-eb3DHEI(ULong.constructor-impl((long)n & 0xFFFFFFFFL), l);
    }

    private static final int div-WZ4Q5Ns(int n, int n2) {
        return UnsignedKt.uintDivide-J1ME1BU(n, n2);
    }

    private static final int div-xj2QHRw(int n, short s) {
        return UnsignedKt.uintDivide-J1ME1BU(n, UInt.constructor-impl(0xFFFF & s));
    }

    public static boolean equals-impl(int n, Object object) {
        return object instanceof UInt && n == ((UInt)object).unbox-impl();
    }

    public static final boolean equals-impl0(int n, int n2) {
        boolean bl = n == n2;
        return bl;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    public static int hashCode-impl(int n) {
        return n;
    }

    private static final int inc-pVg5ArA(int n) {
        return UInt.constructor-impl(n + 1);
    }

    private static final int inv-pVg5ArA(int n) {
        return UInt.constructor-impl(~n);
    }

    private static final int minus-7apg3OU(int n, byte by) {
        return UInt.constructor-impl(n - UInt.constructor-impl(by & 0xFF));
    }

    private static final long minus-VKZWuLQ(int n, long l) {
        return ULong.constructor-impl(ULong.constructor-impl((long)n & 0xFFFFFFFFL) - l);
    }

    private static final int minus-WZ4Q5Ns(int n, int n2) {
        return UInt.constructor-impl(n - n2);
    }

    private static final int minus-xj2QHRw(int n, short s) {
        return UInt.constructor-impl(n - UInt.constructor-impl(0xFFFF & s));
    }

    private static final int or-WZ4Q5Ns(int n, int n2) {
        return UInt.constructor-impl(n | n2);
    }

    private static final int plus-7apg3OU(int n, byte by) {
        return UInt.constructor-impl(UInt.constructor-impl(by & 0xFF) + n);
    }

    private static final long plus-VKZWuLQ(int n, long l) {
        return ULong.constructor-impl(ULong.constructor-impl((long)n & 0xFFFFFFFFL) + l);
    }

    private static final int plus-WZ4Q5Ns(int n, int n2) {
        return UInt.constructor-impl(n + n2);
    }

    private static final int plus-xj2QHRw(int n, short s) {
        return UInt.constructor-impl(UInt.constructor-impl(0xFFFF & s) + n);
    }

    private static final UIntRange rangeTo-WZ4Q5Ns(int n, int n2) {
        return new UIntRange(n, n2, null);
    }

    private static final int rem-7apg3OU(int n, byte by) {
        return UnsignedKt.uintRemainder-J1ME1BU(n, UInt.constructor-impl(by & 0xFF));
    }

    private static final long rem-VKZWuLQ(int n, long l) {
        return UnsignedKt.ulongRemainder-eb3DHEI(ULong.constructor-impl((long)n & 0xFFFFFFFFL), l);
    }

    private static final int rem-WZ4Q5Ns(int n, int n2) {
        return UnsignedKt.uintRemainder-J1ME1BU(n, n2);
    }

    private static final int rem-xj2QHRw(int n, short s) {
        return UnsignedKt.uintRemainder-J1ME1BU(n, UInt.constructor-impl(0xFFFF & s));
    }

    private static final int shl-pVg5ArA(int n, int n2) {
        return UInt.constructor-impl(n << n2);
    }

    private static final int shr-pVg5ArA(int n, int n2) {
        return UInt.constructor-impl(n >>> n2);
    }

    private static final int times-7apg3OU(int n, byte by) {
        return UInt.constructor-impl(UInt.constructor-impl(by & 0xFF) * n);
    }

    private static final long times-VKZWuLQ(int n, long l) {
        return ULong.constructor-impl(ULong.constructor-impl((long)n & 0xFFFFFFFFL) * l);
    }

    private static final int times-WZ4Q5Ns(int n, int n2) {
        return UInt.constructor-impl(n * n2);
    }

    private static final int times-xj2QHRw(int n, short s) {
        return UInt.constructor-impl(UInt.constructor-impl(0xFFFF & s) * n);
    }

    private static final byte toByte-impl(int n) {
        return (byte)n;
    }

    private static final double toDouble-impl(int n) {
        return UnsignedKt.uintToDouble(n);
    }

    private static final float toFloat-impl(int n) {
        return (float)UnsignedKt.uintToDouble(n);
    }

    private static final int toInt-impl(int n) {
        return n;
    }

    private static final long toLong-impl(int n) {
        return (long)n & 0xFFFFFFFFL;
    }

    private static final short toShort-impl(int n) {
        return (short)n;
    }

    public static String toString-impl(int n) {
        return String.valueOf((long)n & 0xFFFFFFFFL);
    }

    private static final byte toUByte-w2LRezQ(int n) {
        return UByte.constructor-impl((byte)n);
    }

    private static final int toUInt-pVg5ArA(int n) {
        return n;
    }

    private static final long toULong-s-VKNKU(int n) {
        return ULong.constructor-impl((long)n & 0xFFFFFFFFL);
    }

    private static final short toUShort-Mh2AYeg(int n) {
        return UShort.constructor-impl((short)n);
    }

    private static final int xor-WZ4Q5Ns(int n, int n2) {
        return UInt.constructor-impl(n ^ n2);
    }

    public boolean equals(Object object) {
        return UInt.equals-impl(this.data, object);
    }

    public int hashCode() {
        return UInt.hashCode-impl(this.data);
    }

    public String toString() {
        return UInt.toString-impl(this.data);
    }

    public final /* synthetic */ int unbox-impl() {
        return this.data;
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u0016\u0010\u0006\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\n"}, d2={"Lkotlin/UInt$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/UInt;", "I", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

