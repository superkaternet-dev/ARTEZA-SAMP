/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Collection;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000F\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u001c\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u001e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0006\u0010\u0005\u001a\u001c\u0010\u0000\u001a\u00020\u0007*\b\u0012\u0004\u0012\u00020\u00070\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\n0\u0002H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u000b\u0010\u0005\u001a\u001a\u0010\f\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00030\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000f\u001a\u001a\u0010\u0010\u001a\u00020\u0011*\b\u0012\u0004\u0012\u00020\u00010\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0012\u001a\u001a\u0010\u0013\u001a\u00020\u0014*\b\u0012\u0004\u0012\u00020\u00070\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0015\u001a\u001a\u0010\u0016\u001a\u00020\u0017*\b\u0012\u0004\u0012\u00020\n0\u000eH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0018\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u0019"}, d2={"sum", "Lkotlin/UInt;", "", "Lkotlin/UByte;", "sumOfUByte", "(Ljava/lang/Iterable;)I", "sumOfUInt", "Lkotlin/ULong;", "sumOfULong", "(Ljava/lang/Iterable;)J", "Lkotlin/UShort;", "sumOfUShort", "toUByteArray", "Lkotlin/UByteArray;", "", "(Ljava/util/Collection;)[B", "toUIntArray", "Lkotlin/UIntArray;", "(Ljava/util/Collection;)[I", "toULongArray", "Lkotlin/ULongArray;", "(Ljava/util/Collection;)[J", "toUShortArray", "Lkotlin/UShortArray;", "(Ljava/util/Collection;)[S", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/collections/UCollectionsKt")
class UCollectionsKt___UCollectionsKt {
    public static final int sumOfUByte(Iterable<UByte> object) {
        Intrinsics.checkNotNullParameter(object, "$this$sum");
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            n = UInt.constructor-impl(UInt.constructor-impl(((UByte)object.next()).unbox-impl() & 0xFF) + n);
        }
        return n;
    }

    public static final int sumOfUInt(Iterable<UInt> object) {
        Intrinsics.checkNotNullParameter(object, "$this$sum");
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            n = UInt.constructor-impl(n + ((UInt)object.next()).unbox-impl());
        }
        return n;
    }

    public static final long sumOfULong(Iterable<ULong> object) {
        Intrinsics.checkNotNullParameter(object, "$this$sum");
        long l = 0L;
        object = object.iterator();
        while (object.hasNext()) {
            l = ULong.constructor-impl(l + ((ULong)object.next()).unbox-impl());
        }
        return l;
    }

    public static final int sumOfUShort(Iterable<UShort> object) {
        Intrinsics.checkNotNullParameter(object, "$this$sum");
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            n = UInt.constructor-impl(UInt.constructor-impl(0xFFFF & ((UShort)object.next()).unbox-impl()) + n);
        }
        return n;
    }

    public static final byte[] toUByteArray(Collection<UByte> object) {
        Intrinsics.checkNotNullParameter(object, "$this$toUByteArray");
        byte[] byArray = UByteArray.constructor-impl(object.size());
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            UByteArray.set-VurrAj0(byArray, n, ((UByte)object.next()).unbox-impl());
            ++n;
        }
        return byArray;
    }

    public static final int[] toUIntArray(Collection<UInt> object) {
        Intrinsics.checkNotNullParameter(object, "$this$toUIntArray");
        int[] nArray = UIntArray.constructor-impl(object.size());
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            UIntArray.set-VXSXFK8(nArray, n, ((UInt)object.next()).unbox-impl());
            ++n;
        }
        return nArray;
    }

    public static final long[] toULongArray(Collection<ULong> object) {
        Intrinsics.checkNotNullParameter(object, "$this$toULongArray");
        long[] lArray = ULongArray.constructor-impl(object.size());
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            ULongArray.set-k8EXiF4(lArray, n, ((ULong)object.next()).unbox-impl());
            ++n;
        }
        return lArray;
    }

    public static final short[] toUShortArray(Collection<UShort> object) {
        Intrinsics.checkNotNullParameter(object, "$this$toUShortArray");
        short[] sArray = UShortArray.constructor-impl(object.size());
        int n = 0;
        object = object.iterator();
        while (object.hasNext()) {
            UShortArray.set-01HTLdE(sArray, n, ((UShort)object.next()).unbox-impl());
            ++n;
        }
        return sArray;
    }
}

