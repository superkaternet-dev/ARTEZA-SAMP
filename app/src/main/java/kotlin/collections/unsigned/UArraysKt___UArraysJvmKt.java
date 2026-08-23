/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections.unsigned;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.RandomAccess;
import kotlin.Metadata;
import kotlin.UByte;
import kotlin.UByteArray;
import kotlin.UInt;
import kotlin.UIntArray;
import kotlin.ULong;
import kotlin.ULongArray;
import kotlin.UShort;
import kotlin.UShortArray;
import kotlin.UnsignedKt;
import kotlin.collections.AbstractList;
import kotlin.collections.ArraysKt;
import kotlin.collections.unsigned.UArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000T\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0016\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u0001*\u00020\u0003H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0004\u0010\u0005\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00060\u0001*\u00020\u0007H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\b\u0010\t\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\n0\u0001*\u00020\u000bH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\r\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0001*\u00020\u000fH\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0010\u0010\u0011\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00032\u0006\u0010\u0014\u001a\u00020\u00022\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0017\u0010\u0018\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u00072\u0006\u0010\u0014\u001a\u00020\u00062\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u0019\u0010\u001a\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000b2\u0006\u0010\u0014\u001a\u00020\n2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001b\u0010\u001c\u001a2\u0010\u0012\u001a\u00020\u0013*\u00020\u000f2\u0006\u0010\u0014\u001a\u00020\u000e2\b\b\u0002\u0010\u0015\u001a\u00020\u00132\b\b\u0002\u0010\u0016\u001a\u00020\u0013H\u0007\u00f8\u0001\u0000\u00a2\u0006\u0004\b\u001d\u0010\u001e\u001a\u001f\u0010\u001f\u001a\u00020\u0002*\u00020\u00032\u0006\u0010 \u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b!\u0010\"\u001a\u001f\u0010\u001f\u001a\u00020\u0006*\u00020\u00072\u0006\u0010 \u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b#\u0010$\u001a\u001f\u0010\u001f\u001a\u00020\n*\u00020\u000b2\u0006\u0010 \u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b%\u0010&\u001a\u001f\u0010\u001f\u001a\u00020\u000e*\u00020\u000f2\u0006\u0010 \u001a\u00020\u0013H\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0004\b'\u0010(\u001a.\u0010)\u001a\u00020**\u00020\u00032\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020*0,H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b-\u0010.\u001a.\u0010)\u001a\u00020/*\u00020\u00032\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0002\u0012\u0004\u0012\u00020/0,H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u00101\u001a.\u0010)\u001a\u00020**\u00020\u00072\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020*0,H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b-\u00102\u001a.\u0010)\u001a\u00020/*\u00020\u00072\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020/0,H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u00103\u001a.\u0010)\u001a\u00020**\u00020\u000b2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020*0,H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b-\u00104\u001a.\u0010)\u001a\u00020/*\u00020\u000b2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\n\u0012\u0004\u0012\u00020/0,H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u00105\u001a.\u0010)\u001a\u00020**\u00020\u000f2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020*0,H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b-\u00106\u001a.\u0010)\u001a\u00020/*\u00020\u000f2\u0012\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020/0,H\u0087\b\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u00107\u0082\u0002\u000b\n\u0002\b\u0019\n\u0005\b\u009920\u0001\u00a8\u00068"}, d2={"asList", "", "Lkotlin/UByte;", "Lkotlin/UByteArray;", "asList-GBYM_sE", "([B)Ljava/util/List;", "Lkotlin/UInt;", "Lkotlin/UIntArray;", "asList--ajY-9A", "([I)Ljava/util/List;", "Lkotlin/ULong;", "Lkotlin/ULongArray;", "asList-QwZRm1k", "([J)Ljava/util/List;", "Lkotlin/UShort;", "Lkotlin/UShortArray;", "asList-rL5Bavg", "([S)Ljava/util/List;", "binarySearch", "", "element", "fromIndex", "toIndex", "binarySearch-WpHrYlw", "([BBII)I", "binarySearch-2fe2U9s", "([IIII)I", "binarySearch-K6DWlUc", "([JJII)I", "binarySearch-EtDCXyQ", "([SSII)I", "elementAt", "index", "elementAt-PpDY95g", "([BI)B", "elementAt-qFRl0hI", "([II)I", "elementAt-r7IrZao", "([JI)J", "elementAt-nggk6HY", "([SI)S", "sumOf", "Ljava/math/BigDecimal;", "selector", "Lkotlin/Function1;", "sumOfBigDecimal", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "Ljava/math/BigInteger;", "sumOfBigInteger", "([BLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([ILkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([JLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigDecimal;", "([SLkotlin/jvm/functions/Function1;)Ljava/math/BigInteger;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, pn="kotlin.collections", xi=1, xs="kotlin/collections/unsigned/UArraysKt")
class UArraysKt___UArraysJvmKt {
    public static final List<UInt> asList--ajY-9A(int[] nArray) {
        Intrinsics.checkNotNullParameter(nArray, "$this$asList");
        return (List)((Object)new RandomAccess(nArray){
            final int[] $this_asList;
            {
                this.$this_asList = nArray;
            }

            public boolean contains-WZ4Q5Ns(int n) {
                return UIntArray.contains-WZ4Q5Ns(this.$this_asList, n);
            }

            public int get-pVg5ArA(int n) {
                return UIntArray.get-pVg5ArA(this.$this_asList, n);
            }

            public int getSize() {
                return UIntArray.getSize-impl(this.$this_asList);
            }

            public int indexOf-WZ4Q5Ns(int n) {
                return ArraysKt.indexOf(this.$this_asList, n);
            }

            public boolean isEmpty() {
                return UIntArray.isEmpty-impl(this.$this_asList);
            }

            public int lastIndexOf-WZ4Q5Ns(int n) {
                return ArraysKt.lastIndexOf(this.$this_asList, n);
            }
        });
    }

    public static final List<UByte> asList-GBYM_sE(byte[] byArray) {
        Intrinsics.checkNotNullParameter(byArray, "$this$asList");
        return (List)((Object)new RandomAccess(byArray){
            final byte[] $this_asList;
            {
                this.$this_asList = byArray;
            }

            public boolean contains-7apg3OU(byte by) {
                return UByteArray.contains-7apg3OU(this.$this_asList, by);
            }

            public byte get-w2LRezQ(int n) {
                return UByteArray.get-w2LRezQ(this.$this_asList, n);
            }

            public int getSize() {
                return UByteArray.getSize-impl(this.$this_asList);
            }

            public int indexOf-7apg3OU(byte by) {
                return ArraysKt.indexOf(this.$this_asList, by);
            }

            public boolean isEmpty() {
                return UByteArray.isEmpty-impl(this.$this_asList);
            }

            public int lastIndexOf-7apg3OU(byte by) {
                return ArraysKt.lastIndexOf(this.$this_asList, by);
            }
        });
    }

    public static final List<ULong> asList-QwZRm1k(long[] lArray) {
        Intrinsics.checkNotNullParameter(lArray, "$this$asList");
        return (List)((Object)new RandomAccess(lArray){
            final long[] $this_asList;
            {
                this.$this_asList = lArray;
            }

            public boolean contains-VKZWuLQ(long l) {
                return ULongArray.contains-VKZWuLQ(this.$this_asList, l);
            }

            public long get-s-VKNKU(int n) {
                return ULongArray.get-s-VKNKU(this.$this_asList, n);
            }

            public int getSize() {
                return ULongArray.getSize-impl(this.$this_asList);
            }

            public int indexOf-VKZWuLQ(long l) {
                return ArraysKt.indexOf(this.$this_asList, l);
            }

            public boolean isEmpty() {
                return ULongArray.isEmpty-impl(this.$this_asList);
            }

            public int lastIndexOf-VKZWuLQ(long l) {
                return ArraysKt.lastIndexOf(this.$this_asList, l);
            }
        });
    }

    public static final List<UShort> asList-rL5Bavg(short[] sArray) {
        Intrinsics.checkNotNullParameter(sArray, "$this$asList");
        return (List)((Object)new RandomAccess(sArray){
            final short[] $this_asList;
            {
                this.$this_asList = sArray;
            }

            public boolean contains-xj2QHRw(short s) {
                return UShortArray.contains-xj2QHRw(this.$this_asList, s);
            }

            public short get-Mh2AYeg(int n) {
                return UShortArray.get-Mh2AYeg(this.$this_asList, n);
            }

            public int getSize() {
                return UShortArray.getSize-impl(this.$this_asList);
            }

            public int indexOf-xj2QHRw(short s) {
                return ArraysKt.indexOf(this.$this_asList, s);
            }

            public boolean isEmpty() {
                return UShortArray.isEmpty-impl(this.$this_asList);
            }

            public int lastIndexOf-xj2QHRw(short s) {
                return ArraysKt.lastIndexOf(this.$this_asList, s);
            }
        });
    }

    public static final int binarySearch-2fe2U9s(int[] nArray, int n, int n2, int n3) {
        Intrinsics.checkNotNullParameter(nArray, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n2, n3, UIntArray.getSize-impl(nArray));
        int n4 = n2;
        n2 = n3 - 1;
        n3 = n4;
        while (n3 <= n2) {
            n4 = n3 + n2 >>> 1;
            int n5 = UnsignedKt.uintCompare(nArray[n4], n);
            if (n5 < 0) {
                n3 = n4 + 1;
                continue;
            }
            if (n5 > 0) {
                n2 = n4 - 1;
                continue;
            }
            return n4;
        }
        return -(n3 + 1);
    }

    public static /* synthetic */ int binarySearch-2fe2U9s$default(int[] nArray, int n, int n2, int n3, int n4, Object object) {
        if ((n4 & 2) != 0) {
            n2 = 0;
        }
        if ((n4 & 4) != 0) {
            n3 = UIntArray.getSize-impl(nArray);
        }
        return UArraysKt.binarySearch-2fe2U9s(nArray, n, n2, n3);
    }

    public static final int binarySearch-EtDCXyQ(short[] sArray, short s, int n, int n2) {
        Intrinsics.checkNotNullParameter(sArray, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n, n2, UShortArray.getSize-impl(sArray));
        int n3 = n;
        n = n2 - 1;
        n2 = n3;
        while (n2 <= n) {
            int n4 = n2 + n >>> 1;
            n3 = UnsignedKt.uintCompare(sArray[n4], 0xFFFF & s);
            if (n3 < 0) {
                n2 = n4 + 1;
                continue;
            }
            if (n3 > 0) {
                n = n4 - 1;
                continue;
            }
            return n4;
        }
        return -(n2 + 1);
    }

    public static /* synthetic */ int binarySearch-EtDCXyQ$default(short[] sArray, short s, int n, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = UShortArray.getSize-impl(sArray);
        }
        return UArraysKt.binarySearch-EtDCXyQ(sArray, s, n, n2);
    }

    public static final int binarySearch-K6DWlUc(long[] lArray, long l, int n, int n2) {
        Intrinsics.checkNotNullParameter(lArray, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n, n2, ULongArray.getSize-impl(lArray));
        int n3 = n;
        n = n2 - 1;
        n2 = n3;
        while (n2 <= n) {
            n3 = n2 + n >>> 1;
            int n4 = UnsignedKt.ulongCompare(lArray[n3], l);
            if (n4 < 0) {
                n2 = n3 + 1;
                continue;
            }
            if (n4 > 0) {
                n = n3 - 1;
                continue;
            }
            return n3;
        }
        return -(n2 + 1);
    }

    public static /* synthetic */ int binarySearch-K6DWlUc$default(long[] lArray, long l, int n, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = ULongArray.getSize-impl(lArray);
        }
        return UArraysKt.binarySearch-K6DWlUc(lArray, l, n, n2);
    }

    public static final int binarySearch-WpHrYlw(byte[] byArray, byte by, int n, int n2) {
        Intrinsics.checkNotNullParameter(byArray, "$this$binarySearch");
        AbstractList.Companion.checkRangeIndexes$kotlin_stdlib(n, n2, UByteArray.getSize-impl(byArray));
        int n3 = n;
        n = n2 - 1;
        n2 = n3;
        while (n2 <= n) {
            int n4 = n2 + n >>> 1;
            n3 = UnsignedKt.uintCompare(byArray[n4], by & 0xFF);
            if (n3 < 0) {
                n2 = n4 + 1;
                continue;
            }
            if (n3 > 0) {
                n = n4 - 1;
                continue;
            }
            return n4;
        }
        return -(n2 + 1);
    }

    public static /* synthetic */ int binarySearch-WpHrYlw$default(byte[] byArray, byte by, int n, int n2, int n3, Object object) {
        if ((n3 & 2) != 0) {
            n = 0;
        }
        if ((n3 & 4) != 0) {
            n2 = UByteArray.getSize-impl(byArray);
        }
        return UArraysKt.binarySearch-WpHrYlw(byArray, by, n, n2);
    }

    private static final byte elementAt-PpDY95g(byte[] byArray, int n) {
        return UByteArray.get-w2LRezQ(byArray, n);
    }

    private static final short elementAt-nggk6HY(short[] sArray, int n) {
        return UShortArray.get-Mh2AYeg(sArray, n);
    }

    private static final int elementAt-qFRl0hI(int[] nArray, int n) {
        return UIntArray.get-pVg5ArA(nArray, n);
    }

    private static final long elementAt-r7IrZao(long[] lArray, int n) {
        return ULongArray.get-s-VKNKU(lArray, n);
    }

    private static final BigDecimal sumOfBigDecimal(byte[] byArray, Function1<? super UByte, ? extends BigDecimal> function1) {
        BigDecimal bigDecimal = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "BigDecimal.valueOf(this.toLong())");
        int n = byArray.length;
        for (int i = 0; i < n; ++i) {
            bigDecimal = bigDecimal.add(function1.invoke(UByte.box-impl(byArray[i])));
            Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.add(other)");
        }
        return bigDecimal;
    }

    private static final BigDecimal sumOfBigDecimal(int[] nArray, Function1<? super UInt, ? extends BigDecimal> function1) {
        BigDecimal bigDecimal = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "BigDecimal.valueOf(this.toLong())");
        int n = nArray.length;
        for (int i = 0; i < n; ++i) {
            bigDecimal = bigDecimal.add(function1.invoke(UInt.box-impl(nArray[i])));
            Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.add(other)");
        }
        return bigDecimal;
    }

    private static final BigDecimal sumOfBigDecimal(long[] lArray, Function1<? super ULong, ? extends BigDecimal> function1) {
        BigDecimal bigDecimal = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "BigDecimal.valueOf(this.toLong())");
        int n = lArray.length;
        for (int i = 0; i < n; ++i) {
            bigDecimal = bigDecimal.add(function1.invoke(ULong.box-impl(lArray[i])));
            Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.add(other)");
        }
        return bigDecimal;
    }

    private static final BigDecimal sumOfBigDecimal(short[] sArray, Function1<? super UShort, ? extends BigDecimal> function1) {
        BigDecimal bigDecimal = BigDecimal.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigDecimal, "BigDecimal.valueOf(this.toLong())");
        int n = sArray.length;
        for (int i = 0; i < n; ++i) {
            bigDecimal = bigDecimal.add(function1.invoke(UShort.box-impl(sArray[i])));
            Intrinsics.checkNotNullExpressionValue(bigDecimal, "this.add(other)");
        }
        return bigDecimal;
    }

    private static final BigInteger sumOfBigInteger(byte[] byArray, Function1<? super UByte, ? extends BigInteger> function1) {
        BigInteger bigInteger = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "BigInteger.valueOf(this.toLong())");
        int n = byArray.length;
        for (int i = 0; i < n; ++i) {
            bigInteger = bigInteger.add(function1.invoke(UByte.box-impl(byArray[i])));
            Intrinsics.checkNotNullExpressionValue(bigInteger, "this.add(other)");
        }
        return bigInteger;
    }

    private static final BigInteger sumOfBigInteger(int[] nArray, Function1<? super UInt, ? extends BigInteger> function1) {
        BigInteger bigInteger = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "BigInteger.valueOf(this.toLong())");
        int n = nArray.length;
        for (int i = 0; i < n; ++i) {
            bigInteger = bigInteger.add(function1.invoke(UInt.box-impl(nArray[i])));
            Intrinsics.checkNotNullExpressionValue(bigInteger, "this.add(other)");
        }
        return bigInteger;
    }

    private static final BigInteger sumOfBigInteger(long[] lArray, Function1<? super ULong, ? extends BigInteger> function1) {
        BigInteger bigInteger = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "BigInteger.valueOf(this.toLong())");
        int n = lArray.length;
        for (int i = 0; i < n; ++i) {
            bigInteger = bigInteger.add(function1.invoke(ULong.box-impl(lArray[i])));
            Intrinsics.checkNotNullExpressionValue(bigInteger, "this.add(other)");
        }
        return bigInteger;
    }

    private static final BigInteger sumOfBigInteger(short[] sArray, Function1<? super UShort, ? extends BigInteger> function1) {
        BigInteger bigInteger = BigInteger.valueOf(0);
        Intrinsics.checkNotNullExpressionValue(bigInteger, "BigInteger.valueOf(this.toLong())");
        int n = sArray.length;
        for (int i = 0; i < n; ++i) {
            bigInteger = bigInteger.add(function1.invoke(UShort.box-impl(sArray[i])));
            Intrinsics.checkNotNullExpressionValue(bigInteger, "this.add(other)");
        }
        return bigInteger;
    }
}

