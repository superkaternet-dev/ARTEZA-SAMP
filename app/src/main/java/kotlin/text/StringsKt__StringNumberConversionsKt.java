/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.StringsKt;
import kotlin.text.StringsKt__StringNumberConversionsJVMKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000.\n\u0000\n\u0002\u0010\u0001\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0005\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\n\n\u0002\b\u0003\u001a\u0010\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\u0000\u001a\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u0003H\u0007\u00a2\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0004\u001a\u0004\u0018\u00010\u0005*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a2\u0006\u0002\u0010\t\u001a\u0013\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u0003H\u0007\u00a2\u0006\u0002\u0010\u000b\u001a\u001b\u0010\n\u001a\u0004\u0018\u00010\b*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a2\u0006\u0002\u0010\f\u001a\u0013\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u0003H\u0007\u00a2\u0006\u0002\u0010\u000f\u001a\u001b\u0010\r\u001a\u0004\u0018\u00010\u000e*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a2\u0006\u0002\u0010\u0010\u001a\u0013\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u0003H\u0007\u00a2\u0006\u0002\u0010\u0013\u001a\u001b\u0010\u0011\u001a\u0004\u0018\u00010\u0012*\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0007\u00a2\u0006\u0002\u0010\u0014\u00a8\u0006\u0015"}, d2={"numberFormatError", "", "input", "", "toByteOrNull", "", "(Ljava/lang/String;)Ljava/lang/Byte;", "radix", "", "(Ljava/lang/String;I)Ljava/lang/Byte;", "toIntOrNull", "(Ljava/lang/String;)Ljava/lang/Integer;", "(Ljava/lang/String;I)Ljava/lang/Integer;", "toLongOrNull", "", "(Ljava/lang/String;)Ljava/lang/Long;", "(Ljava/lang/String;I)Ljava/lang/Long;", "toShortOrNull", "", "(Ljava/lang/String;)Ljava/lang/Short;", "(Ljava/lang/String;I)Ljava/lang/Short;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/text/StringsKt")
class StringsKt__StringNumberConversionsKt
extends StringsKt__StringNumberConversionsJVMKt {
    public static final Void numberFormatError(String string2) {
        Intrinsics.checkNotNullParameter(string2, "input");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid number format: '");
        stringBuilder.append(string2);
        stringBuilder.append('\'');
        throw (Throwable)new NumberFormatException(stringBuilder.toString());
    }

    public static final Byte toByteOrNull(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toByteOrNull");
        return StringsKt.toByteOrNull(string2, 10);
    }

    public static final Byte toByteOrNull(String object, int n) {
        Intrinsics.checkNotNullParameter(object, "$this$toByteOrNull");
        object = StringsKt.toIntOrNull((String)object, n);
        if (object != null) {
            n = (Integer)object;
            if (n >= -128 && n <= 127) {
                return (byte)n;
            }
            return null;
        }
        return null;
    }

    public static final Integer toIntOrNull(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toIntOrNull");
        return StringsKt.toIntOrNull(string2, 10);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final Integer toIntOrNull(String object, int n) {
        int n2;
        int n3;
        Intrinsics.checkNotNullParameter(object, "$this$toIntOrNull");
        CharsKt.checkRadix(n);
        int n4 = ((String)object).length();
        if (n4 == 0) {
            return null;
        }
        char c = ((String)object).charAt(0);
        if (Intrinsics.compare(c, 48) < 0) {
            if (n4 == 1) {
                return null;
            }
            n3 = 1;
            if (c == '-') {
                c = '\u0001';
                n2 = Integer.MIN_VALUE;
            } else {
                if (c != '+') return null;
                c = '\u0000';
                n2 = -2147483647;
            }
        } else {
            n3 = 0;
            c = '\u0000';
            n2 = -2147483647;
        }
        int n5 = -59652323;
        int n6 = 0;
        while (n3 < n4) {
            int n7 = CharsKt.digitOf(((String)object).charAt(n3), n);
            if (n7 < 0) {
                return null;
            }
            int n8 = n5;
            if (n6 < n5) {
                if (n5 != -59652323) return null;
                n8 = n5 = n2 / n;
                if (n6 < n5) {
                    return null;
                }
            }
            if ((n6 *= n) < n2 + n7) {
                return null;
            }
            n6 -= n7;
            ++n3;
            n5 = n8;
        }
        if (c == '\u0000') return -n6;
        return n6;
    }

    public static final Long toLongOrNull(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toLongOrNull");
        return StringsKt.toLongOrNull(string2, 10);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final Long toLongOrNull(String object, int n) {
        long l;
        boolean bl;
        int n2;
        Intrinsics.checkNotNullParameter(object, "$this$toLongOrNull");
        CharsKt.checkRadix(n);
        int n3 = ((String)object).length();
        if (n3 == 0) {
            return null;
        }
        char c = ((String)object).charAt(0);
        if (Intrinsics.compare(c, 48) < 0) {
            if (n3 == 1) {
                return null;
            }
            n2 = 1;
            if (c == '-') {
                bl = true;
                l = Long.MIN_VALUE;
            } else {
                if (c != '+') return null;
                bl = false;
                l = -9223372036854775807L;
            }
        } else {
            n2 = 0;
            bl = false;
            l = -9223372036854775807L;
        }
        long l2 = -256204778801521550L;
        long l3 = -256204778801521550L;
        long l4 = 0L;
        while (n2 < n3) {
            int n4 = CharsKt.digitOf(((String)object).charAt(n2), n);
            if (n4 < 0) {
                return null;
            }
            if (l4 < l3) {
                if (l3 != l2) return null;
                l3 = l / (long)n;
                if (l4 < l3) {
                    return null;
                }
            }
            if ((l4 *= (long)n) < (long)n4 + l) {
                return null;
            }
            l4 -= (long)n4;
            ++n2;
        }
        if (!bl) return -l4;
        return l4;
    }

    public static final Short toShortOrNull(String string2) {
        Intrinsics.checkNotNullParameter(string2, "$this$toShortOrNull");
        return StringsKt.toShortOrNull(string2, 10);
    }

    public static final Short toShortOrNull(String object, int n) {
        Intrinsics.checkNotNullParameter(object, "$this$toShortOrNull");
        object = StringsKt.toIntOrNull((String)object, n);
        if (object != null) {
            n = (Integer)object;
            if (n >= Short.MIN_VALUE && n <= Short.MAX_VALUE) {
                return (short)n;
            }
            return null;
        }
        return null;
    }
}

