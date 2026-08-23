/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import kotlin.Metadata;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.jvm.internal.Intrinsics;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u00000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0010\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0007\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\n\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000f\u0010\u0010\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0013\u0010\u0014\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0015\u0010\u0016\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018\u001a*\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0005\u001a\u00020\u0001H\u0003\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\u001a\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001e\u0010\u0014\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001f\u0010\u0016\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b \u0010\u0018\u001a*\u0010\u001b\u001a\u00020\u00122\u0006\u0010\u0002\u001a\u00020\u000e2\u0006\u0010\u001c\u001a\u00020\u00012\u0006\u0010\u001d\u001a\u00020\u0001H\u0001\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\u001a\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\""}, d2={"partition", "", "array", "Lkotlin/UByteArray;", "left", "right", "partition-4UcCI2c", "([BII)I", "Lkotlin/UIntArray;", "partition-oBK06Vg", "([III)I", "Lkotlin/ULongArray;", "partition--nroSd4", "([JII)I", "Lkotlin/UShortArray;", "partition-Aa5vz7o", "([SII)I", "quickSort", "", "quickSort-4UcCI2c", "([BII)V", "quickSort-oBK06Vg", "([III)V", "quickSort--nroSd4", "([JII)V", "quickSort-Aa5vz7o", "([SII)V", "sortArray", "fromIndex", "toIndex", "sortArray-4UcCI2c", "sortArray-oBK06Vg", "sortArray--nroSd4", "sortArray-Aa5vz7o", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class UArraySortingKt {
    private static final int partition--nroSd4(long[] lArray, int n, int n2) {
        int n3 = n;
        int n4 = n2;
        long l = ULongArray.get-s-VKNKU(lArray, (n + n2) / 2);
        n = n3;
        while (n <= n4) {
            while (true) {
                n2 = n4;
                if (UnsignedKt.ulongCompare(ULongArray.get-s-VKNKU(lArray, n), l) >= 0) break;
                ++n;
            }
            while (UnsignedKt.ulongCompare(ULongArray.get-s-VKNKU(lArray, n2), l) > 0) {
                --n2;
            }
            n3 = n;
            n4 = n2;
            if (n <= n2) {
                long l2 = ULongArray.get-s-VKNKU(lArray, n);
                ULongArray.set-k8EXiF4(lArray, n, ULongArray.get-s-VKNKU(lArray, n2));
                ULongArray.set-k8EXiF4(lArray, n2, l2);
                n3 = n + 1;
                n4 = n2 - 1;
            }
            n = n3;
        }
        return n;
    }

    private static final int partition-4UcCI2c(byte[] byArray, int n, int n2) {
        int n3 = n;
        int n4 = n2;
        byte by = UByteArray.get-w2LRezQ(byArray, (n + n2) / 2);
        n2 = n4;
        n = n3;
        while (n <= n2) {
            while (true) {
                n3 = n2;
                if (Intrinsics.compare(UByteArray.get-w2LRezQ(byArray, n) & 0xFF, by & 0xFF) >= 0) break;
                ++n;
            }
            while (Intrinsics.compare(UByteArray.get-w2LRezQ(byArray, n3) & 0xFF, by & 0xFF) > 0) {
                --n3;
            }
            n4 = n;
            n2 = n3;
            if (n <= n3) {
                byte by2 = UByteArray.get-w2LRezQ(byArray, n);
                UByteArray.set-VurrAj0(byArray, n, UByteArray.get-w2LRezQ(byArray, n3));
                UByteArray.set-VurrAj0(byArray, n3, by2);
                n4 = n + 1;
                n2 = n3 - 1;
            }
            n = n4;
        }
        return n;
    }

    private static final int partition-Aa5vz7o(short[] sArray, int n, int n2) {
        int n3 = n;
        int n4 = n2;
        short s = UShortArray.get-Mh2AYeg(sArray, (n + n2) / 2);
        n2 = n4;
        while (n3 <= n2) {
            while (true) {
                n = n2;
                if (Intrinsics.compare(UShortArray.get-Mh2AYeg(sArray, n3) & 0xFFFF, s & 0xFFFF) >= 0) break;
                ++n3;
            }
            while (Intrinsics.compare(UShortArray.get-Mh2AYeg(sArray, n) & 0xFFFF, s & 0xFFFF) > 0) {
                --n;
            }
            n4 = n3;
            n2 = n;
            if (n3 <= n) {
                short s2 = UShortArray.get-Mh2AYeg(sArray, n3);
                UShortArray.set-01HTLdE(sArray, n3, UShortArray.get-Mh2AYeg(sArray, n));
                UShortArray.set-01HTLdE(sArray, n, s2);
                n4 = n3 + 1;
                n2 = n - 1;
            }
            n3 = n4;
        }
        return n3;
    }

    private static final int partition-oBK06Vg(int[] nArray, int n, int n2) {
        int n3 = n;
        int n4 = n2;
        int n5 = UIntArray.get-pVg5ArA(nArray, (n + n2) / 2);
        n2 = n3;
        while (n2 <= n4) {
            while (true) {
                n = n4;
                if (UnsignedKt.uintCompare(UIntArray.get-pVg5ArA(nArray, n2), n5) >= 0) break;
                ++n2;
            }
            while (UnsignedKt.uintCompare(UIntArray.get-pVg5ArA(nArray, n), n5) > 0) {
                --n;
            }
            n3 = n2;
            n4 = n;
            if (n2 <= n) {
                n4 = UIntArray.get-pVg5ArA(nArray, n2);
                UIntArray.set-VXSXFK8(nArray, n2, UIntArray.get-pVg5ArA(nArray, n));
                UIntArray.set-VXSXFK8(nArray, n, n4);
                n3 = n2 + 1;
                n4 = n - 1;
            }
            n2 = n3;
        }
        return n2;
    }

    private static final void quickSort--nroSd4(long[] lArray, int n, int n2) {
        int n3 = UArraySortingKt.partition--nroSd4(lArray, n, n2);
        if (n < n3 - 1) {
            UArraySortingKt.quickSort--nroSd4(lArray, n, n3 - 1);
        }
        if (n3 < n2) {
            UArraySortingKt.quickSort--nroSd4(lArray, n3, n2);
        }
    }

    private static final void quickSort-4UcCI2c(byte[] byArray, int n, int n2) {
        int n3 = UArraySortingKt.partition-4UcCI2c(byArray, n, n2);
        if (n < n3 - 1) {
            UArraySortingKt.quickSort-4UcCI2c(byArray, n, n3 - 1);
        }
        if (n3 < n2) {
            UArraySortingKt.quickSort-4UcCI2c(byArray, n3, n2);
        }
    }

    private static final void quickSort-Aa5vz7o(short[] sArray, int n, int n2) {
        int n3 = UArraySortingKt.partition-Aa5vz7o(sArray, n, n2);
        if (n < n3 - 1) {
            UArraySortingKt.quickSort-Aa5vz7o(sArray, n, n3 - 1);
        }
        if (n3 < n2) {
            UArraySortingKt.quickSort-Aa5vz7o(sArray, n3, n2);
        }
    }

    private static final void quickSort-oBK06Vg(int[] nArray, int n, int n2) {
        int n3 = UArraySortingKt.partition-oBK06Vg(nArray, n, n2);
        if (n < n3 - 1) {
            UArraySortingKt.quickSort-oBK06Vg(nArray, n, n3 - 1);
        }
        if (n3 < n2) {
            UArraySortingKt.quickSort-oBK06Vg(nArray, n3, n2);
        }
    }

    public static final void sortArray--nroSd4(long[] lArray, int n, int n2) {
        Intrinsics.checkNotNullParameter(lArray, "array");
        UArraySortingKt.quickSort--nroSd4(lArray, n, n2 - 1);
    }

    public static final void sortArray-4UcCI2c(byte[] byArray, int n, int n2) {
        Intrinsics.checkNotNullParameter(byArray, "array");
        UArraySortingKt.quickSort-4UcCI2c(byArray, n, n2 - 1);
    }

    public static final void sortArray-Aa5vz7o(short[] sArray, int n, int n2) {
        Intrinsics.checkNotNullParameter(sArray, "array");
        UArraySortingKt.quickSort-Aa5vz7o(sArray, n, n2 - 1);
    }

    public static final void sortArray-oBK06Vg(int[] nArray, int n, int n2) {
        Intrinsics.checkNotNullParameter(nArray, "array");
        UArraySortingKt.quickSort-oBK06Vg(nArray, n, n2 - 1);
    }
}

