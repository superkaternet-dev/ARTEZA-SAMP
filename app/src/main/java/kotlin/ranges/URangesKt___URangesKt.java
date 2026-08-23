/*
 * Decompiled with CFR 0.152.
 */
package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.random.Random;
import kotlin.random.URandomKt;
import kotlin.ranges.ClosedFloatingPointRange;
import kotlin.ranges.ClosedRange;
import kotlin.ranges.RangesKt;
import kotlin.ranges.UIntProgression;
import kotlin.ranges.UIntRange;
import kotlin.ranges.ULongProgression;
import kotlin.ranges.ULongRange;
import kotlin.ranges.URangesKt;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0002\u0010\t\n\u0002\b\n\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0003\u0010\u0004\u001a\u001e\u0010\u0000\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u0005H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0007\u001a\u001e\u0010\u0000\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a\u001e\u0010\u0000\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a\u001e\u0010\u000e\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u0004\u001a\u001e\u0010\u000e\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0011\u0010\u0007\u001a\u001e\u0010\u000e\u001a\u00020\b*\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0012\u0010\n\u001a\u001e\u0010\u000e\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0013\u0010\r\u001a&\u0010\u0014\u001a\u00020\u0001*\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u000f\u001a\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016\u001a&\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u000f\u001a\u00020\u0005H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018\u001a$\u0010\u0014\u001a\u00020\u0005*\u00020\u00052\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00050\u001aH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001b\u0010\u001c\u001a&\u0010\u0014\u001a\u00020\b*\u00020\b2\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001d\u0010\u001e\u001a$\u0010\u0014\u001a\u00020\b*\u00020\b2\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\b0\u001aH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001f\u0010 \u001a&\u0010\u0014\u001a\u00020\u000b*\u00020\u000b2\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u0001H\u0087\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b'\u0010(\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\b\u0010)\u001a\u0004\u0018\u00010\u0005H\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0002\b*\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\bH\u0087\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b+\u0010,\u001a\u001f\u0010#\u001a\u00020$*\u00020%2\u0006\u0010&\u001a\u00020\u000bH\u0087\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b-\u0010.\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0001H\u0087\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b0\u00101\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u0005H\u0087\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b2\u00103\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\b\u0010)\u001a\u0004\u0018\u00010\bH\u0087\n\u00f8\u0001\u0000\u00a2\u0006\u0002\b4\u001a\u001f\u0010#\u001a\u00020$*\u00020/2\u0006\u0010&\u001a\u00020\u000bH\u0087\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b5\u00106\u001a\u001f\u00107\u001a\u000208*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b:\u0010;\u001a\u001f\u00107\u001a\u000208*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b<\u0010=\u001a\u001f\u00107\u001a\u00020>*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\b?\u0010@\u001a\u001f\u00107\u001a\u000208*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\bA\u0010B\u001a\u0015\u0010C\u001a\u00020\u0005*\u00020%H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010D\u001a\u001c\u0010C\u001a\u00020\u0005*\u00020%2\u0006\u0010C\u001a\u00020EH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010F\u001a\u0015\u0010C\u001a\u00020\b*\u00020/H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010G\u001a\u001c\u0010C\u001a\u00020\b*\u00020/2\u0006\u0010C\u001a\u00020EH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010H\u001a\u0012\u0010I\u001a\u0004\u0018\u00010\u0005*\u00020%H\u0087\b\u00f8\u0001\u0000\u001a\u0019\u0010I\u001a\u0004\u0018\u00010\u0005*\u00020%2\u0006\u0010C\u001a\u00020EH\u0007\u00f8\u0001\u0000\u001a\u0012\u0010I\u001a\u0004\u0018\u00010\b*\u00020/H\u0087\b\u00f8\u0001\u0000\u001a\u0019\u0010I\u001a\u0004\u0018\u00010\b*\u00020/2\u0006\u0010C\u001a\u00020EH\u0007\u00f8\u0001\u0000\u001a\f\u0010J\u001a\u000208*\u000208H\u0007\u001a\f\u0010J\u001a\u00020>*\u00020>H\u0007\u001a\u0015\u0010K\u001a\u000208*\u0002082\u0006\u0010K\u001a\u00020LH\u0087\u0004\u001a\u0015\u0010K\u001a\u00020>*\u00020>2\u0006\u0010K\u001a\u00020MH\u0087\u0004\u001a\u001f\u0010N\u001a\u00020%*\u00020\u00012\u0006\u00109\u001a\u00020\u0001H\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\bO\u0010P\u001a\u001f\u0010N\u001a\u00020%*\u00020\u00052\u0006\u00109\u001a\u00020\u0005H\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\bQ\u0010R\u001a\u001f\u0010N\u001a\u00020/*\u00020\b2\u0006\u00109\u001a\u00020\bH\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\bS\u0010T\u001a\u001f\u0010N\u001a\u00020%*\u00020\u000b2\u0006\u00109\u001a\u00020\u000bH\u0087\u0004\u00f8\u0001\u0000\u00a2\u0006\u0004\bU\u0010V\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006W"}, d2={"coerceAtLeast", "Lkotlin/UByte;", "minimumValue", "coerceAtLeast-Kr8caGY", "(BB)B", "Lkotlin/UInt;", "coerceAtLeast-J1ME1BU", "(II)I", "Lkotlin/ULong;", "coerceAtLeast-eb3DHEI", "(JJ)J", "Lkotlin/UShort;", "coerceAtLeast-5PvTz6A", "(SS)S", "coerceAtMost", "maximumValue", "coerceAtMost-Kr8caGY", "coerceAtMost-J1ME1BU", "coerceAtMost-eb3DHEI", "coerceAtMost-5PvTz6A", "coerceIn", "coerceIn-b33U2AM", "(BBB)B", "coerceIn-WZ9TVnA", "(III)I", "range", "Lkotlin/ranges/ClosedRange;", "coerceIn-wuiCnnA", "(ILkotlin/ranges/ClosedRange;)I", "coerceIn-sambcqE", "(JJJ)J", "coerceIn-JPwROB0", "(JLkotlin/ranges/ClosedRange;)J", "coerceIn-VKSA0NQ", "(SSS)S", "contains", "", "Lkotlin/ranges/UIntRange;", "value", "contains-68kG9v0", "(Lkotlin/ranges/UIntRange;B)Z", "element", "contains-biwQdVI", "contains-fz5IDCE", "(Lkotlin/ranges/UIntRange;J)Z", "contains-ZsK3CEQ", "(Lkotlin/ranges/UIntRange;S)Z", "Lkotlin/ranges/ULongRange;", "contains-ULb-yJY", "(Lkotlin/ranges/ULongRange;B)Z", "contains-Gab390E", "(Lkotlin/ranges/ULongRange;I)Z", "contains-GYNo2lE", "contains-uhHAxoY", "(Lkotlin/ranges/ULongRange;S)Z", "downTo", "Lkotlin/ranges/UIntProgression;", "to", "downTo-Kr8caGY", "(BB)Lkotlin/ranges/UIntProgression;", "downTo-J1ME1BU", "(II)Lkotlin/ranges/UIntProgression;", "Lkotlin/ranges/ULongProgression;", "downTo-eb3DHEI", "(JJ)Lkotlin/ranges/ULongProgression;", "downTo-5PvTz6A", "(SS)Lkotlin/ranges/UIntProgression;", "random", "(Lkotlin/ranges/UIntRange;)I", "Lkotlin/random/Random;", "(Lkotlin/ranges/UIntRange;Lkotlin/random/Random;)I", "(Lkotlin/ranges/ULongRange;)J", "(Lkotlin/ranges/ULongRange;Lkotlin/random/Random;)J", "randomOrNull", "reversed", "step", "", "", "until", "until-Kr8caGY", "(BB)Lkotlin/ranges/UIntRange;", "until-J1ME1BU", "(II)Lkotlin/ranges/UIntRange;", "until-eb3DHEI", "(JJ)Lkotlin/ranges/ULongRange;", "until-5PvTz6A", "(SS)Lkotlin/ranges/UIntRange;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/ranges/URangesKt")
class URangesKt___URangesKt {
    public static final short coerceAtLeast-5PvTz6A(short s, short s2) {
        block0: {
            if (Intrinsics.compare(s & 0xFFFF, 0xFFFF & s2) >= 0) break block0;
            s = s2;
        }
        return s;
    }

    public static final int coerceAtLeast-J1ME1BU(int n, int n2) {
        if (UnsignedKt.uintCompare(n, n2) >= 0) {
            n2 = n;
        }
        return n2;
    }

    public static final byte coerceAtLeast-Kr8caGY(byte by, byte by2) {
        block0: {
            if (Intrinsics.compare(by & 0xFF, by2 & 0xFF) >= 0) break block0;
            by = by2;
        }
        return by;
    }

    public static final long coerceAtLeast-eb3DHEI(long l, long l2) {
        block0: {
            if (UnsignedKt.ulongCompare(l, l2) >= 0) break block0;
            l = l2;
        }
        return l;
    }

    public static final short coerceAtMost-5PvTz6A(short s, short s2) {
        block0: {
            if (Intrinsics.compare(s & 0xFFFF, 0xFFFF & s2) <= 0) break block0;
            s = s2;
        }
        return s;
    }

    public static final int coerceAtMost-J1ME1BU(int n, int n2) {
        block0: {
            if (UnsignedKt.uintCompare(n, n2) <= 0) break block0;
            n = n2;
        }
        return n;
    }

    public static final byte coerceAtMost-Kr8caGY(byte by, byte by2) {
        block0: {
            if (Intrinsics.compare(by & 0xFF, by2 & 0xFF) <= 0) break block0;
            by = by2;
        }
        return by;
    }

    public static final long coerceAtMost-eb3DHEI(long l, long l2) {
        block0: {
            if (UnsignedKt.ulongCompare(l, l2) <= 0) break block0;
            l = l2;
        }
        return l;
    }

    public static final long coerceIn-JPwROB0(long l, ClosedRange<ULong> closedRange) {
        Intrinsics.checkNotNullParameter(closedRange, "range");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return RangesKt.coerceIn(ULong.box-impl(l), (ClosedFloatingPointRange)closedRange).unbox-impl();
        }
        if (!closedRange.isEmpty()) {
            if (UnsignedKt.ulongCompare(l, closedRange.getStart().unbox-impl()) < 0) {
                l = closedRange.getStart().unbox-impl();
            } else if (UnsignedKt.ulongCompare(l, closedRange.getEndInclusive().unbox-impl()) > 0) {
                l = closedRange.getEndInclusive().unbox-impl();
            }
            return l;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: ");
        stringBuilder.append(closedRange);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final short coerceIn-VKSA0NQ(short s, short s2, short s3) {
        if (Intrinsics.compare(s2 & 0xFFFF, s3 & 0xFFFF) <= 0) {
            if (Intrinsics.compare(s & 0xFFFF, s2 & 0xFFFF) < 0) {
                return s2;
            }
            if (Intrinsics.compare(s & 0xFFFF, 0xFFFF & s3) > 0) {
                return s3;
            }
            return s;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(UShort.toString-impl(s3));
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(UShort.toString-impl(s2));
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final int coerceIn-WZ9TVnA(int n, int n2, int n3) {
        if (UnsignedKt.uintCompare(n2, n3) <= 0) {
            if (UnsignedKt.uintCompare(n, n2) < 0) {
                return n2;
            }
            if (UnsignedKt.uintCompare(n, n3) > 0) {
                return n3;
            }
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(UInt.toString-impl(n3));
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(UInt.toString-impl(n2));
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final byte coerceIn-b33U2AM(byte by, byte by2, byte by3) {
        if (Intrinsics.compare(by2 & 0xFF, by3 & 0xFF) <= 0) {
            if (Intrinsics.compare(by & 0xFF, by2 & 0xFF) < 0) {
                return by2;
            }
            if (Intrinsics.compare(by & 0xFF, by3 & 0xFF) > 0) {
                return by3;
            }
            return by;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(UByte.toString-impl(by3));
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(UByte.toString-impl(by2));
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final long coerceIn-sambcqE(long l, long l2, long l3) {
        if (UnsignedKt.ulongCompare(l2, l3) <= 0) {
            if (UnsignedKt.ulongCompare(l, l2) < 0) {
                return l2;
            }
            if (UnsignedKt.ulongCompare(l, l3) > 0) {
                return l3;
            }
            return l;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: maximum ");
        stringBuilder.append(ULong.toString-impl(l3));
        stringBuilder.append(" is less than minimum ");
        stringBuilder.append(ULong.toString-impl(l2));
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final int coerceIn-wuiCnnA(int n, ClosedRange<UInt> closedRange) {
        Intrinsics.checkNotNullParameter(closedRange, "range");
        if (closedRange instanceof ClosedFloatingPointRange) {
            return RangesKt.coerceIn(UInt.box-impl(n), (ClosedFloatingPointRange)closedRange).unbox-impl();
        }
        if (!closedRange.isEmpty()) {
            if (UnsignedKt.uintCompare(n, closedRange.getStart().unbox-impl()) < 0) {
                n = closedRange.getStart().unbox-impl();
            } else if (UnsignedKt.uintCompare(n, closedRange.getEndInclusive().unbox-impl()) > 0) {
                n = closedRange.getEndInclusive().unbox-impl();
            }
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot coerce value to an empty range: ");
        stringBuilder.append(closedRange);
        stringBuilder.append('.');
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final boolean contains-68kG9v0(UIntRange uIntRange, byte by) {
        Intrinsics.checkNotNullParameter(uIntRange, "$this$contains");
        return uIntRange.contains-WZ4Q5Ns(UInt.constructor-impl(by & 0xFF));
    }

    private static final boolean contains-GYNo2lE(ULongRange uLongRange, ULong uLong) {
        Intrinsics.checkNotNullParameter(uLongRange, "$this$contains");
        boolean bl = uLong != null && uLongRange.contains-VKZWuLQ(uLong.unbox-impl());
        return bl;
    }

    public static final boolean contains-Gab390E(ULongRange uLongRange, int n) {
        Intrinsics.checkNotNullParameter(uLongRange, "$this$contains");
        return uLongRange.contains-VKZWuLQ(ULong.constructor-impl((long)n & 0xFFFFFFFFL));
    }

    public static final boolean contains-ULb-yJY(ULongRange uLongRange, byte by) {
        Intrinsics.checkNotNullParameter(uLongRange, "$this$contains");
        return uLongRange.contains-VKZWuLQ(ULong.constructor-impl((long)by & 0xFFL));
    }

    public static final boolean contains-ZsK3CEQ(UIntRange uIntRange, short s) {
        Intrinsics.checkNotNullParameter(uIntRange, "$this$contains");
        return uIntRange.contains-WZ4Q5Ns(UInt.constructor-impl(0xFFFF & s));
    }

    private static final boolean contains-biwQdVI(UIntRange uIntRange, UInt uInt) {
        Intrinsics.checkNotNullParameter(uIntRange, "$this$contains");
        boolean bl = uInt != null && uIntRange.contains-WZ4Q5Ns(uInt.unbox-impl());
        return bl;
    }

    public static final boolean contains-fz5IDCE(UIntRange uIntRange, long l) {
        Intrinsics.checkNotNullParameter(uIntRange, "$this$contains");
        boolean bl = ULong.constructor-impl(l >>> 32) == 0L && uIntRange.contains-WZ4Q5Ns(UInt.constructor-impl((int)l));
        return bl;
    }

    public static final boolean contains-uhHAxoY(ULongRange uLongRange, short s) {
        Intrinsics.checkNotNullParameter(uLongRange, "$this$contains");
        return uLongRange.contains-VKZWuLQ(ULong.constructor-impl((long)s & 0xFFFFL));
    }

    public static final UIntProgression downTo-5PvTz6A(short s, short s2) {
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs(UInt.constructor-impl(s & 0xFFFF), UInt.constructor-impl(0xFFFF & s2), -1);
    }

    public static final UIntProgression downTo-J1ME1BU(int n, int n2) {
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs(n, n2, -1);
    }

    public static final UIntProgression downTo-Kr8caGY(byte by, byte by2) {
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs(UInt.constructor-impl(by & 0xFF), UInt.constructor-impl(by2 & 0xFF), -1);
    }

    public static final ULongProgression downTo-eb3DHEI(long l, long l2) {
        return ULongProgression.Companion.fromClosedRange-7ftBX0g(l, l2, -1L);
    }

    private static final int random(UIntRange uIntRange) {
        return URangesKt.random(uIntRange, (Random)Random.Default);
    }

    public static final int random(UIntRange uIntRange, Random random) {
        Intrinsics.checkNotNullParameter(uIntRange, "$this$random");
        Intrinsics.checkNotNullParameter(random, "random");
        try {
            int n = URandomKt.nextUInt(random, uIntRange);
            return n;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (Throwable)new NoSuchElementException(illegalArgumentException.getMessage());
        }
    }

    private static final long random(ULongRange uLongRange) {
        return URangesKt.random(uLongRange, (Random)Random.Default);
    }

    public static final long random(ULongRange uLongRange, Random random) {
        Intrinsics.checkNotNullParameter(uLongRange, "$this$random");
        Intrinsics.checkNotNullParameter(random, "random");
        try {
            long l = URandomKt.nextULong(random, uLongRange);
            return l;
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw (Throwable)new NoSuchElementException(illegalArgumentException.getMessage());
        }
    }

    private static final UInt randomOrNull(UIntRange uIntRange) {
        return URangesKt.randomOrNull(uIntRange, (Random)Random.Default);
    }

    public static final UInt randomOrNull(UIntRange uIntRange, Random random) {
        Intrinsics.checkNotNullParameter(uIntRange, "$this$randomOrNull");
        Intrinsics.checkNotNullParameter(random, "random");
        if (uIntRange.isEmpty()) {
            return null;
        }
        return UInt.box-impl(URandomKt.nextUInt(random, uIntRange));
    }

    private static final ULong randomOrNull(ULongRange uLongRange) {
        return URangesKt.randomOrNull(uLongRange, (Random)Random.Default);
    }

    public static final ULong randomOrNull(ULongRange uLongRange, Random random) {
        Intrinsics.checkNotNullParameter(uLongRange, "$this$randomOrNull");
        Intrinsics.checkNotNullParameter(random, "random");
        if (uLongRange.isEmpty()) {
            return null;
        }
        return ULong.box-impl(URandomKt.nextULong(random, uLongRange));
    }

    public static final UIntProgression reversed(UIntProgression uIntProgression) {
        Intrinsics.checkNotNullParameter(uIntProgression, "$this$reversed");
        return UIntProgression.Companion.fromClosedRange-Nkh28Cs(uIntProgression.getLast-pVg5ArA(), uIntProgression.getFirst-pVg5ArA(), -uIntProgression.getStep());
    }

    public static final ULongProgression reversed(ULongProgression uLongProgression) {
        Intrinsics.checkNotNullParameter(uLongProgression, "$this$reversed");
        return ULongProgression.Companion.fromClosedRange-7ftBX0g(uLongProgression.getLast-s-VKNKU(), uLongProgression.getFirst-s-VKNKU(), -uLongProgression.getStep());
    }

    public static final UIntProgression step(UIntProgression uIntProgression, int n) {
        Intrinsics.checkNotNullParameter(uIntProgression, "$this$step");
        boolean bl = n > 0;
        RangesKt.checkStepIsPositive(bl, n);
        UIntProgression.Companion companion = UIntProgression.Companion;
        int n2 = uIntProgression.getFirst-pVg5ArA();
        int n3 = uIntProgression.getLast-pVg5ArA();
        if (uIntProgression.getStep() <= 0) {
            n = -n;
        }
        return companion.fromClosedRange-Nkh28Cs(n2, n3, n);
    }

    public static final ULongProgression step(ULongProgression uLongProgression, long l) {
        Intrinsics.checkNotNullParameter(uLongProgression, "$this$step");
        boolean bl = l > 0L;
        RangesKt.checkStepIsPositive(bl, l);
        ULongProgression.Companion companion = ULongProgression.Companion;
        long l2 = uLongProgression.getFirst-s-VKNKU();
        long l3 = uLongProgression.getLast-s-VKNKU();
        if (uLongProgression.getStep() <= 0L) {
            l = -l;
        }
        return companion.fromClosedRange-7ftBX0g(l2, l3, l);
    }

    public static final UIntRange until-5PvTz6A(short s, short s2) {
        if (Intrinsics.compare(s2 & 0xFFFF, 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange(UInt.constructor-impl(s & 0xFFFF), UInt.constructor-impl(UInt.constructor-impl(0xFFFF & s2) - 1), null);
    }

    public static final UIntRange until-J1ME1BU(int n, int n2) {
        if (UnsignedKt.uintCompare(n2, 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange(n, UInt.constructor-impl(n2 - 1), null);
    }

    public static final UIntRange until-Kr8caGY(byte by, byte by2) {
        if (Intrinsics.compare(by2 & 0xFF, 0) <= 0) {
            return UIntRange.Companion.getEMPTY();
        }
        return new UIntRange(UInt.constructor-impl(by & 0xFF), UInt.constructor-impl(UInt.constructor-impl(by2 & 0xFF) - 1), null);
    }

    public static final ULongRange until-eb3DHEI(long l, long l2) {
        if (UnsignedKt.ulongCompare(l2, 0L) <= 0) {
            return ULongRange.Companion.getEMPTY();
        }
        return new ULongRange(l, ULong.constructor-impl(l2 - ULong.constructor-impl((long)1 & 0xFFFFFFFFL)), null);
    }
}

