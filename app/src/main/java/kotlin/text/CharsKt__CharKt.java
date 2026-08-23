/*
 * Decompiled with CFR 0.152.
 */
package kotlin.text;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.CharsKt;
import kotlin.text.CharsKt__CharJVMKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001c\n\u0000\n\u0002\u0010\f\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0007\u001a\u0014\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0002H\u0007\u001a\f\u0010\u0004\u001a\u00020\u0002*\u00020\u0001H\u0007\u001a\u0014\u0010\u0004\u001a\u00020\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0007\u001a\u0013\u0010\u0005\u001a\u0004\u0018\u00010\u0002*\u00020\u0001H\u0007\u00a2\u0006\u0002\u0010\u0006\u001a\u001b\u0010\u0005\u001a\u0004\u0018\u00010\u0002*\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0002H\u0007\u00a2\u0006\u0002\u0010\u0007\u001a\u001c\u0010\b\u001a\u00020\t*\u00020\u00012\u0006\u0010\n\u001a\u00020\u00012\b\b\u0002\u0010\u000b\u001a\u00020\t\u001a\n\u0010\f\u001a\u00020\t*\u00020\u0001\u001a\u0015\u0010\r\u001a\u00020\u000e*\u00020\u00012\u0006\u0010\n\u001a\u00020\u000eH\u0087\n\u00a8\u0006\u000f"}, d2={"digitToChar", "", "", "radix", "digitToInt", "digitToIntOrNull", "(C)Ljava/lang/Integer;", "(CI)Ljava/lang/Integer;", "equals", "", "other", "ignoreCase", "isSurrogate", "plus", "", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/text/CharsKt")
class CharsKt__CharKt
extends CharsKt__CharJVMKt {
    public static final char digitToChar(int n) {
        if (n >= 0 && 9 >= n) {
            return (char)(n + 48);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Int ");
        stringBuilder.append(n);
        stringBuilder.append(" is not a decimal digit");
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final char digitToChar(int n, int n2) {
        if (2 <= n2 && 36 >= n2) {
            if (n >= 0 && n < n2) {
                char c = n < 10 ? (char)(n + 48) : (char)((char)(n + 65) - 10);
                return c;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Digit ");
            stringBuilder.append(n);
            stringBuilder.append(" does not represent a valid digit in radix ");
            stringBuilder.append(n2);
            throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid radix: ");
        stringBuilder.append(n2);
        stringBuilder.append(". Valid radix values are in range 2..36");
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final int digitToInt(char c) {
        if ('0' <= c && '9' >= c) {
            return c - 48;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Char ");
        stringBuilder.append(c);
        stringBuilder.append(" is not a decimal digit");
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final int digitToInt(char c, int n) {
        Comparable<Integer> comparable = CharsKt.digitToIntOrNull(c, n);
        if (comparable != null) {
            return (Integer)comparable;
        }
        comparable = new StringBuilder();
        ((StringBuilder)comparable).append("Char ");
        ((StringBuilder)comparable).append(c);
        ((StringBuilder)comparable).append(" is not a digit in the given radix=");
        ((StringBuilder)comparable).append(n);
        throw (Throwable)new IllegalArgumentException(((StringBuilder)comparable).toString());
    }

    public static final Integer digitToIntOrNull(char c) {
        if ('0' <= c && '9' >= c) {
            return c - 48;
        }
        return null;
    }

    public static final Integer digitToIntOrNull(char c, int n) {
        if (2 <= n && 36 >= n) {
            Object var4_2 = null;
            Integer n2 = null;
            if ('0' <= c && '9' >= c) {
                if ((c = (char)(c - 48)) < n) {
                    n2 = c;
                }
                return n2;
            }
            int n3 = Intrinsics.compare(c, 90) <= 0 ? 65 : 97;
            if ('\n' > (c = (char)(c - n3 + 10))) {
                n2 = var4_2;
            } else {
                n2 = var4_2;
                if (n > c) {
                    n2 = c;
                }
            }
            return n2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid radix: ");
        stringBuilder.append(n);
        stringBuilder.append(". Valid radix values are in range 2..36");
        throw (Throwable)new IllegalArgumentException(stringBuilder.toString());
    }

    public static final boolean equals(char c, char c2, boolean bl) {
        if (c == c2) {
            return true;
        }
        if (!bl) {
            return false;
        }
        if (Character.toUpperCase(c) == Character.toUpperCase(c2)) {
            return true;
        }
        return Character.toLowerCase(c) == Character.toLowerCase(c2);
    }

    public static /* synthetic */ boolean equals$default(char c, char c2, boolean bl, int n, Object object) {
        if ((n & 2) != 0) {
            bl = false;
        }
        return CharsKt.equals(c, c2, bl);
    }

    public static final boolean isSurrogate(char c) {
        boolean bl = '\ud800' <= c && '\udfff' >= c;
        return bl;
    }

    private static final String plus(char c, String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(c));
        stringBuilder.append(string2);
        return stringBuilder.toString();
    }
}

