/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.ranges.ULongRange;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000j\n\u0002\u0018\u0002\n\u0002\u0010\u000f\n\u0000\n\u0002\u0010\t\n\u0002\b\t\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\u0010\u0000\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0010\u0005\n\u0002\b\u0003\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\n\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u000e\b\u0087@\u0018\u0000 p2\b\u0012\u0004\u0012\u00020\u00000\u0001:\u0001pB\u0014\b\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005J\u001b\u0010\b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\b\n\u0010\u000bJ\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\u0013J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0000H\u0097\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0014\u0010\u0015J\u001b\u0010\f\u001a\u00020\r2\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018J\u0016\u0010\u0019\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u001a\u0010\u0005J\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001c\u0010\u001dJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u001fJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010\u000bJ\u001b\u0010\u001b\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"J\u001a\u0010#\u001a\u00020$2\b\u0010\t\u001a\u0004\u0018\u00010%H\u00d6\u0003\u00a2\u0006\u0004\b&\u0010'J\u0010\u0010(\u001a\u00020\rH\u00d6\u0001\u00a2\u0006\u0004\b)\u0010*J\u0016\u0010+\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b,\u0010\u0005J\u0016\u0010-\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b.\u0010\u0005J\u001b\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b0\u0010\u001dJ\u001b\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b1\u0010\u001fJ\u001b\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b2\u0010\u000bJ\u001b\u0010/\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b3\u0010\"J\u001b\u00104\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\b5\u0010\u000bJ\u001b\u00106\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b7\u0010\u001dJ\u001b\u00106\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b8\u0010\u001fJ\u001b\u00106\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b9\u0010\u000bJ\u001b\u00106\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b:\u0010\"J\u001b\u0010;\u001a\u00020<2\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b=\u0010>J\u001b\u0010?\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\b@\u0010\u001dJ\u001b\u0010?\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bA\u0010\u001fJ\u001b\u0010?\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bB\u0010\u000bJ\u001b\u0010?\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bC\u0010\"J\u001e\u0010D\u001a\u00020\u00002\u0006\u0010E\u001a\u00020\rH\u0087\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bF\u0010\u001fJ\u001e\u0010G\u001a\u00020\u00002\u0006\u0010E\u001a\u00020\rH\u0087\f\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bH\u0010\u001fJ\u001b\u0010I\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u000eH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bJ\u0010\u001dJ\u001b\u0010I\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0011H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bK\u0010\u001fJ\u001b\u0010I\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bL\u0010\u000bJ\u001b\u0010I\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0016H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0004\bM\u0010\"J\u0010\u0010N\u001a\u00020OH\u0087\b\u00a2\u0006\u0004\bP\u0010QJ\u0010\u0010R\u001a\u00020SH\u0087\b\u00a2\u0006\u0004\bT\u0010UJ\u0010\u0010V\u001a\u00020WH\u0087\b\u00a2\u0006\u0004\bX\u0010YJ\u0010\u0010Z\u001a\u00020\rH\u0087\b\u00a2\u0006\u0004\b[\u0010*J\u0010\u0010\\\u001a\u00020\u0003H\u0087\b\u00a2\u0006\u0004\b]\u0010\u0005J\u0010\u0010^\u001a\u00020_H\u0087\b\u00a2\u0006\u0004\b`\u0010aJ\u000f\u0010b\u001a\u00020cH\u0016\u00a2\u0006\u0004\bd\u0010eJ\u0016\u0010f\u001a\u00020\u000eH\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bg\u0010QJ\u0016\u0010h\u001a\u00020\u0011H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bi\u0010*J\u0016\u0010j\u001a\u00020\u0000H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bk\u0010\u0005J\u0016\u0010l\u001a\u00020\u0016H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\bm\u0010aJ\u001b\u0010n\u001a\u00020\u00002\u0006\u0010\t\u001a\u00020\u0000H\u0087\f\u00f8\u0001\u0000\u00a2\u0006\u0004\bo\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0000X\u0081\u0004\u00a2\u0006\b\n\u0000\u0012\u0004\b\u0006\u0010\u0007\u00f8\u0001\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006q"}, d2={"Lkotlin/ULong;", "", "data", "", "constructor-impl", "(J)J", "getData$annotations", "()V", "and", "other", "and-VKZWuLQ", "(JJ)J", "compareTo", "", "Lkotlin/UByte;", "compareTo-7apg3OU", "(JB)I", "Lkotlin/UInt;", "compareTo-WZ4Q5Ns", "(JI)I", "compareTo-VKZWuLQ", "(JJ)I", "Lkotlin/UShort;", "compareTo-xj2QHRw", "(JS)I", "dec", "dec-s-VKNKU", "div", "div-7apg3OU", "(JB)J", "div-WZ4Q5Ns", "(JI)J", "div-VKZWuLQ", "div-xj2QHRw", "(JS)J", "equals", "", "", "equals-impl", "(JLjava/lang/Object;)Z", "hashCode", "hashCode-impl", "(J)I", "inc", "inc-s-VKNKU", "inv", "inv-s-VKNKU", "minus", "minus-7apg3OU", "minus-WZ4Q5Ns", "minus-VKZWuLQ", "minus-xj2QHRw", "or", "or-VKZWuLQ", "plus", "plus-7apg3OU", "plus-WZ4Q5Ns", "plus-VKZWuLQ", "plus-xj2QHRw", "rangeTo", "Lkotlin/ranges/ULongRange;", "rangeTo-VKZWuLQ", "(JJ)Lkotlin/ranges/ULongRange;", "rem", "rem-7apg3OU", "rem-WZ4Q5Ns", "rem-VKZWuLQ", "rem-xj2QHRw", "shl", "bitCount", "shl-s-VKNKU", "shr", "shr-s-VKNKU", "times", "times-7apg3OU", "times-WZ4Q5Ns", "times-VKZWuLQ", "times-xj2QHRw", "toByte", "", "toByte-impl", "(J)B", "toDouble", "", "toDouble-impl", "(J)D", "toFloat", "", "toFloat-impl", "(J)F", "toInt", "toInt-impl", "toLong", "toLong-impl", "toShort", "", "toShort-impl", "(J)S", "toString", "", "toString-impl", "(J)Ljava/lang/String;", "toUByte", "toUByte-w2LRezQ", "toUInt", "toUInt-pVg5ArA", "toULong", "toULong-s-VKNKU", "toUShort", "toUShort-Mh2AYeg", "xor", "xor-VKZWuLQ", "Companion", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class ULong
implements Comparable<ULong> {
    public static final Companion Companion = new Companion(null);
    public static final long MAX_VALUE = -1L;
    public static final long MIN_VALUE = 0L;
    public static final int SIZE_BITS = 64;
    public static final int SIZE_BYTES = 8;
    private final long data;

    private /* synthetic */ ULong(long l) {
        this.data = l;
    }

    private static final long and-VKZWuLQ(long l, long l2) {
        return ULong.constructor-impl(l & l2);
    }

    public static final /* synthetic */ ULong box-impl(long l) {
        return new ULong(l);
    }

    private static final int compareTo-7apg3OU(long l, byte by) {
        return UnsignedKt.ulongCompare(l, ULong.constructor-impl((long)by & 0xFFL));
    }

    private int compareTo-VKZWuLQ(long l) {
        return ULong.compareTo-VKZWuLQ(this.data, l);
    }

    private static int compareTo-VKZWuLQ(long l, long l2) {
        return UnsignedKt.ulongCompare(l, l2);
    }

    private static final int compareTo-WZ4Q5Ns(long l, int n) {
        return UnsignedKt.ulongCompare(l, ULong.constructor-impl((long)n & 0xFFFFFFFFL));
    }

    private static final int compareTo-xj2QHRw(long l, short s) {
        return UnsignedKt.ulongCompare(l, ULong.constructor-impl((long)s & 0xFFFFL));
    }

    public static long constructor-impl(long l) {
        return l;
    }

    private static final long dec-s-VKNKU(long l) {
        return ULong.constructor-impl(-1L + l);
    }

    private static final long div-7apg3OU(long l, byte by) {
        return UnsignedKt.ulongDivide-eb3DHEI(l, ULong.constructor-impl((long)by & 0xFFL));
    }

    private static final long div-VKZWuLQ(long l, long l2) {
        return UnsignedKt.ulongDivide-eb3DHEI(l, l2);
    }

    private static final long div-WZ4Q5Ns(long l, int n) {
        return UnsignedKt.ulongDivide-eb3DHEI(l, ULong.constructor-impl((long)n & 0xFFFFFFFFL));
    }

    private static final long div-xj2QHRw(long l, short s) {
        return UnsignedKt.ulongDivide-eb3DHEI(l, ULong.constructor-impl((long)s & 0xFFFFL));
    }

    public static boolean equals-impl(long l, Object object) {
        return object instanceof ULong && l == ((ULong)object).unbox-impl();
    }

    public static final boolean equals-impl0(long l, long l2) {
        boolean bl = l == l2;
        return bl;
    }

    public static /* synthetic */ void getData$annotations() {
    }

    public static int hashCode-impl(long l) {
        return (int)(l ^ l >>> 32);
    }

    private static final long inc-s-VKNKU(long l) {
        return ULong.constructor-impl(1L + l);
    }

    private static final long inv-s-VKNKU(long l) {
        return ULong.constructor-impl(0xFFFFFFFFFFFFFFFFL ^ l);
    }

    private static final long minus-7apg3OU(long l, byte by) {
        return ULong.constructor-impl(l - ULong.constructor-impl((long)by & 0xFFL));
    }

    private static final long minus-VKZWuLQ(long l, long l2) {
        return ULong.constructor-impl(l - l2);
    }

    private static final long minus-WZ4Q5Ns(long l, int n) {
        return ULong.constructor-impl(l - ULong.constructor-impl((long)n & 0xFFFFFFFFL));
    }

    private static final long minus-xj2QHRw(long l, short s) {
        return ULong.constructor-impl(l - ULong.constructor-impl((long)s & 0xFFFFL));
    }

    private static final long or-VKZWuLQ(long l, long l2) {
        return ULong.constructor-impl(l | l2);
    }

    private static final long plus-7apg3OU(long l, byte by) {
        return ULong.constructor-impl(ULong.constructor-impl((long)by & 0xFFL) + l);
    }

    private static final long plus-VKZWuLQ(long l, long l2) {
        return ULong.constructor-impl(l + l2);
    }

    private static final long plus-WZ4Q5Ns(long l, int n) {
        return ULong.constructor-impl(ULong.constructor-impl((long)n & 0xFFFFFFFFL) + l);
    }

    private static final long plus-xj2QHRw(long l, short s) {
        return ULong.constructor-impl(ULong.constructor-impl((long)s & 0xFFFFL) + l);
    }

    private static final ULongRange rangeTo-VKZWuLQ(long l, long l2) {
        return new ULongRange(l, l2, null);
    }

    private static final long rem-7apg3OU(long l, byte by) {
        return UnsignedKt.ulongRemainder-eb3DHEI(l, ULong.constructor-impl((long)by & 0xFFL));
    }

    private static final long rem-VKZWuLQ(long l, long l2) {
        return UnsignedKt.ulongRemainder-eb3DHEI(l, l2);
    }

    private static final long rem-WZ4Q5Ns(long l, int n) {
        return UnsignedKt.ulongRemainder-eb3DHEI(l, ULong.constructor-impl((long)n & 0xFFFFFFFFL));
    }

    private static final long rem-xj2QHRw(long l, short s) {
        return UnsignedKt.ulongRemainder-eb3DHEI(l, ULong.constructor-impl((long)s & 0xFFFFL));
    }

    private static final long shl-s-VKNKU(long l, int n) {
        return ULong.constructor-impl(l << n);
    }

    private static final long shr-s-VKNKU(long l, int n) {
        return ULong.constructor-impl(l >>> n);
    }

    private static final long times-7apg3OU(long l, byte by) {
        return ULong.constructor-impl(ULong.constructor-impl((long)by & 0xFFL) * l);
    }

    private static final long times-VKZWuLQ(long l, long l2) {
        return ULong.constructor-impl(l * l2);
    }

    private static final long times-WZ4Q5Ns(long l, int n) {
        return ULong.constructor-impl(ULong.constructor-impl((long)n & 0xFFFFFFFFL) * l);
    }

    private static final long times-xj2QHRw(long l, short s) {
        return ULong.constructor-impl(ULong.constructor-impl((long)s & 0xFFFFL) * l);
    }

    private static final byte toByte-impl(long l) {
        return (byte)l;
    }

    private static final double toDouble-impl(long l) {
        return UnsignedKt.ulongToDouble(l);
    }

    private static final float toFloat-impl(long l) {
        return (float)UnsignedKt.ulongToDouble(l);
    }

    private static final int toInt-impl(long l) {
        return (int)l;
    }

    private static final long toLong-impl(long l) {
        return l;
    }

    private static final short toShort-impl(long l) {
        return (short)l;
    }

    public static String toString-impl(long l) {
        return UnsignedKt.ulongToString(l);
    }

    private static final byte toUByte-w2LRezQ(long l) {
        return UByte.constructor-impl((byte)l);
    }

    private static final int toUInt-pVg5ArA(long l) {
        return UInt.constructor-impl((int)l);
    }

    private static final long toULong-s-VKNKU(long l) {
        return l;
    }

    private static final short toUShort-Mh2AYeg(long l) {
        return UShort.constructor-impl((short)l);
    }

    private static final long xor-VKZWuLQ(long l, long l2) {
        return ULong.constructor-impl(l ^ l2);
    }

    public boolean equals(Object object) {
        return ULong.equals-impl(this.data, object);
    }

    public int hashCode() {
        return ULong.hashCode-impl(this.data);
    }

    public String toString() {
        return ULong.toString-impl(this.data);
    }

    public final /* synthetic */ long unbox-impl() {
        return this.data;
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u0016\u0010\u0003\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u0016\u0010\u0006\u001a\u00020\u0004X\u0086T\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\n\u0002\u0010\u0005R\u000e\u0010\u0007\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0086T\u00a2\u0006\u0002\n\u0000\u0082\u0002\b\n\u0002\b\u0019\n\u0002\b!\u00a8\u0006\n"}, d2={"Lkotlin/ULong$Companion;", "", "()V", "MAX_VALUE", "Lkotlin/ULong;", "J", "MIN_VALUE", "SIZE_BITS", "", "SIZE_BYTES", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }
}

