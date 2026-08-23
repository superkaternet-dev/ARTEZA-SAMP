/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import kotlin.KotlinNothingValueException;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UShort;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000,\n\u0000\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0013\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\n2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\f\u001a\u001e\u0010\u0000\u001a\u00020\u0001*\u00020\r2\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000e\u0010\u000f\u001a\u0014\u0010\u0010\u001a\u00020\u0002*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0011\u001a\u001c\u0010\u0010\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001a\u0011\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u0013\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u0014\u001a\u00020\u0007*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001a\u001c\u0010\u0014\u001a\u00020\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0016\u001a\u0011\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u0017\u001a\u0004\u0018\u00010\u0007*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u0018\u001a\u00020\n*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0019\u001a\u001c\u0010\u0018\u001a\u00020\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001a\u001a\u0011\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u001b\u001a\u0004\u0018\u00010\n*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u001a\u0014\u0010\u001c\u001a\u00020\r*\u00020\u0001H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001d\u001a\u001c\u0010\u001c\u001a\u00020\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001e\u001a\u0011\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u0001H\u0007\u00f8\u0001\u0000\u001a\u0019\u0010\u001f\u001a\u0004\u0018\u00010\r*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0004H\u0007\u00f8\u0001\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006 "}, d2={"toString", "", "Lkotlin/UByte;", "radix", "", "toString-LxnNnR4", "(BI)Ljava/lang/String;", "Lkotlin/UInt;", "toString-V7xB4Y4", "(II)Ljava/lang/String;", "Lkotlin/ULong;", "toString-JSWoG40", "(JI)Ljava/lang/String;", "Lkotlin/UShort;", "toString-olVBNx4", "(SI)Ljava/lang/String;", "toUByte", "(Ljava/lang/String;)B", "(Ljava/lang/String;I)B", "toUByteOrNull", "toUInt", "(Ljava/lang/String;)I", "(Ljava/lang/String;I)I", "toUIntOrNull", "toULong", "(Ljava/lang/String;)J", "(Ljava/lang/String;I)J", "toULongOrNull", "toUShort", "(Ljava/lang/String;)S", "(Ljava/lang/String;I)S", "toUShortOrNull", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class UStringsKt {
    public static final String toString-JSWoG40(long l, int n) {
        return UnsignedKt.ulongToString(l, CharsKt.checkRadix(n));
    }

    public static final String toString-LxnNnR4(byte by, int n) {
        String string2 = Integer.toString(by & 0xFF, CharsKt.checkRadix(n));
        Intrinsics.checkNotNullExpressionValue(string2, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string2;
    }

    public static final String toString-V7xB4Y4(int n, int n2) {
        String string2 = Long.toString((long)n & 0xFFFFFFFFL, CharsKt.checkRadix(n2));
        Intrinsics.checkNotNullExpressionValue(string2, "java.lang.Long.toString(this, checkRadix(radix))");
        return string2;
    }

    public static final String toString-olVBNx4(short s, int n) {
        String string2 = Integer.toString(0xFFFF & s, CharsKt.checkRadix(n));
        Intrinsics.checkNotNullExpressionValue(string2, "java.lang.Integer.toStri\u2026(this, checkRadix(radix))");
        return string2;
    }

    public static final byte toUByte(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUByte");
        UByte uByte = UStringsKt.toUByteOrNull(string2);
        if (uByte != null) {
            return uByte.unbox-impl();
        }
        StringsKt.numberFormatError(string2);
        throw new KotlinNothingValueException();
    }

    public static final byte toUByte(String string2, int n) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUByte");
        UByte uByte = UStringsKt.toUByteOrNull(string2, n);
        if (uByte != null) {
            return uByte.unbox-impl();
        }
        StringsKt.numberFormatError(string2);
        throw new KotlinNothingValueException();
    }

    public static final UByte toUByteOrNull(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUByteOrNull");
        return UStringsKt.toUByteOrNull(string2, 10);
    }

    public static final UByte toUByteOrNull(String object, int n) {
        Intrinsics.checkNotNullParameter(object, "$this$toUByteOrNull");
        object = UStringsKt.toUIntOrNull((String)object, n);
        if (object != null) {
            n = ((UInt)object).unbox-impl();
            if (UnsignedKt.uintCompare(n, UInt.constructor-impl(255)) > 0) {
                return null;
            }
            return UByte.box-impl(UByte.constructor-impl((byte)n));
        }
        return null;
    }

    public static final int toUInt(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUInt");
        UInt uInt = UStringsKt.toUIntOrNull(string2);
        if (uInt != null) {
            return uInt.unbox-impl();
        }
        StringsKt.numberFormatError(string2);
        throw new KotlinNothingValueException();
    }

    public static final int toUInt(String string2, int n) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUInt");
        UInt uInt = UStringsKt.toUIntOrNull(string2, n);
        if (uInt != null) {
            return uInt.unbox-impl();
        }
        StringsKt.numberFormatError(string2);
        throw new KotlinNothingValueException();
    }

    public static final UInt toUIntOrNull(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUIntOrNull");
        return UStringsKt.toUIntOrNull(string2, 10);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final UInt toUIntOrNull(String string2, int n) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUIntOrNull");
        CharsKt.checkRadix(n);
        int n2 = string2.length();
        if (n2 == 0) {
            return null;
        }
        int n3 = string2.charAt(0);
        if (Intrinsics.compare(n3, 48) < 0) {
            if (n2 == 1 || n3 != 43) return null;
            n3 = 1;
        } else {
            n3 = 0;
        }
        int n4 = 0x71C71C7;
        int n5 = UInt.constructor-impl(n);
        int n6 = 0;
        while (n3 < n2) {
            int n7 = CharsKt.digitOf(string2.charAt(n3), n);
            if (n7 < 0) {
                return null;
            }
            int n8 = n4;
            if (UnsignedKt.uintCompare(n6, n4) > 0) {
                if (n4 != 0x71C71C7) return null;
                n8 = n4 = UnsignedKt.uintDivide-J1ME1BU(-1, n5);
                if (UnsignedKt.uintCompare(n6, n4) > 0) {
                    return null;
                }
            }
            n4 = UInt.constructor-impl(n6 * n5);
            n6 = UInt.constructor-impl(UInt.constructor-impl(n7) + n4);
            if (UnsignedKt.uintCompare(n6, n4) < 0) {
                return null;
            }
            ++n3;
            n4 = n8;
        }
        return UInt.box-impl(n6);
    }

    public static final long toULong(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toULong");
        ULong uLong = UStringsKt.toULongOrNull(string2);
        if (uLong != null) {
            return uLong.unbox-impl();
        }
        StringsKt.numberFormatError(string2);
        throw new KotlinNothingValueException();
    }

    public static final long toULong(String string2, int n) {
        Intrinsics.checkNotNullParameter(string2, "$this$toULong");
        ULong uLong = UStringsKt.toULongOrNull(string2, n);
        if (uLong != null) {
            return uLong.unbox-impl();
        }
        StringsKt.numberFormatError(string2);
        throw new KotlinNothingValueException();
    }

    public static final ULong toULongOrNull(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toULongOrNull");
        return UStringsKt.toULongOrNull(string2, 10);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final ULong toULongOrNull(String string2, int n) {
        Intrinsics.checkNotNullParameter(string2, "$this$toULongOrNull");
        CharsKt.checkRadix(n);
        int n2 = string2.length();
        if (n2 == 0) {
            return null;
        }
        int n3 = string2.charAt(0);
        if (Intrinsics.compare(n3, 48) < 0) {
            if (n2 == 1 || n3 != 43) return null;
            n3 = 1;
        } else {
            n3 = 0;
        }
        long l = 0x71C71C71C71C71CL;
        long l2 = ULong.constructor-impl(n);
        long l3 = 0L;
        int n4 = n3;
        n3 = n2;
        while (n4 < n3) {
            n2 = CharsKt.digitOf(string2.charAt(n4), n);
            if (n2 < 0) {
                return null;
            }
            long l4 = l;
            if (UnsignedKt.ulongCompare(l3, l) > 0) {
                if (l != 0x71C71C71C71C71CL) return null;
                l4 = UnsignedKt.ulongDivide-eb3DHEI(-1L, l2);
                if (UnsignedKt.ulongCompare(l3, l4) > 0) {
                    return null;
                }
            }
            l = ULong.constructor-impl(l3 * l2);
            l3 = ULong.constructor-impl(ULong.constructor-impl((long)UInt.constructor-impl(n2) & 0xFFFFFFFFL) + l);
            if (UnsignedKt.ulongCompare(l3, l) < 0) {
                return null;
            }
            ++n4;
            l = l4;
        }
        return ULong.box-impl(l3);
    }

    public static final short toUShort(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUShort");
        UShort uShort = UStringsKt.toUShortOrNull(string2);
        if (uShort != null) {
            return uShort.unbox-impl();
        }
        StringsKt.numberFormatError(string2);
        throw new KotlinNothingValueException();
    }

    public static final short toUShort(String string2, int n) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUShort");
        UShort uShort = UStringsKt.toUShortOrNull(string2, n);
        if (uShort != null) {
            return uShort.unbox-impl();
        }
        StringsKt.numberFormatError(string2);
        throw new KotlinNothingValueException();
    }

    public static final UShort toUShortOrNull(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toUShortOrNull");
        return UStringsKt.toUShortOrNull(string2, 10);
    }

    public static final UShort toUShortOrNull(String object, int n) {
        Intrinsics.checkNotNullParameter(object, "$this$toUShortOrNull");
        object = UStringsKt.toUIntOrNull((String)object, n);
        if (object != null) {
            n = ((UInt)object).unbox-impl();
            if (UnsignedKt.uintCompare(n, UInt.constructor-impl(65535)) > 0) {
                return null;
            }
            return UShort.box-impl(UShort.constructor-impl((short)n));
        }
        return null;
    }
}

