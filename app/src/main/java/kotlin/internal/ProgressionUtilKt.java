/*
 * Decompiled with CFR 0.152.
 */
package kotlin.internal;

import kotlin.Metadata;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0006\u001a \u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002\u001a \u0010\u0000\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u00052\u0006\u0010\u0004\u001a\u00020\u0005H\u0002\u001a \u0010\u0006\u001a\u00020\u00012\u0006\u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\u00012\u0006\u0010\t\u001a\u00020\u0001H\u0001\u001a \u0010\u0006\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\u00052\u0006\u0010\t\u001a\u00020\u0005H\u0001\u001a\u0018\u0010\n\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001H\u0002\u001a\u0018\u0010\n\u001a\u00020\u00052\u0006\u0010\u0002\u001a\u00020\u00052\u0006\u0010\u0003\u001a\u00020\u0005H\u0002\u00a8\u0006\u000b"}, d2={"differenceModulo", "", "a", "b", "c", "", "getProgressionLastElement", "start", "end", "step", "mod", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class ProgressionUtilKt {
    private static final int differenceModulo(int n, int n2, int n3) {
        return ProgressionUtilKt.mod(ProgressionUtilKt.mod(n, n3) - ProgressionUtilKt.mod(n2, n3), n3);
    }

    private static final long differenceModulo(long l, long l2, long l3) {
        return ProgressionUtilKt.mod(ProgressionUtilKt.mod(l, l3) - ProgressionUtilKt.mod(l2, l3), l3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final int getProgressionLastElement(int n, int n2, int n3) {
        if (n3 > 0) {
            if (n < n2) return n2 - ProgressionUtilKt.differenceModulo(n2, n, n3);
            return n2;
        } else {
            if (n3 >= 0) throw (Throwable)new IllegalArgumentException("Step is zero.");
            if (n > n2) return ProgressionUtilKt.differenceModulo(n, n2, -n3) + n2;
        }
        return n2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final long getProgressionLastElement(long l, long l2, long l3) {
        if (l3 > 0L) {
            if (l < l2) return l2 - ProgressionUtilKt.differenceModulo(l2, l, l3);
            return l2;
        } else {
            if (l3 >= 0L) throw (Throwable)new IllegalArgumentException("Step is zero.");
            if (l > l2) return ProgressionUtilKt.differenceModulo(l, l2, -l3) + l2;
        }
        return l2;
    }

    private static final int mod(int n, int n2) {
        if ((n %= n2) < 0) {
            n += n2;
        }
        return n;
    }

    private static final long mod(long l, long l2) {
        if ((l %= l2) < 0L) {
            l += l2;
        }
        return l;
    }
}

