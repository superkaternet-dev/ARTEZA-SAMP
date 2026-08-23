/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.collections.ArraysKt;
import kotlin.collections.ArraysKt__ArraysJVMKt;
import kotlin.collections.CollectionsKt;
import kotlin.collections.unsigned.UArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.ranges.RangesKt;

@Metadata(bv={1, 0, 3}, d1={"\u0000H\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a5\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u00032\u0010\u0010\u0004\u001a\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001a#\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001\u00a2\u0006\u0004\b\t\u0010\n\u001a?\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\u0010\u0010\u0010\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00030\u0011H\u0002\u00a2\u0006\u0004\b\u0012\u0010\u0013\u001a+\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0015\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00030\u0003\u00a2\u0006\u0002\u0010\u0016\u001a;\u0010\u0017\u001a\u0002H\u0018\"\u0010\b\u0000\u0010\u0019*\u0006\u0012\u0002\b\u00030\u0003*\u0002H\u0018\"\u0004\b\u0001\u0010\u0018*\u0002H\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00180\u001bH\u0087\b\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001c\u001a)\u0010\u001d\u001a\u00020\u0001*\b\u0012\u0002\b\u0003\u0018\u00010\u0003H\u0087\b\u0082\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000\u00a2\u0006\u0002\u0010\u001e\u001aG\u0010\u001f\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\u00150 \"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0018*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00180 0\u0003\u00a2\u0006\u0002\u0010!\u0082\u0002\u0007\n\u0005\b\u009920\u0001\u00a8\u0006\""}, d2={"contentDeepEqualsImpl", "", "T", "", "other", "contentDeepEquals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepToStringImpl", "", "contentDeepToString", "([Ljava/lang/Object;)Ljava/lang/String;", "contentDeepToStringInternal", "", "result", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "processed", "", "contentDeepToStringInternal$ArraysKt__ArraysKt", "([Ljava/lang/Object;Ljava/lang/StringBuilder;Ljava/util/List;)V", "flatten", "", "([[Ljava/lang/Object;)Ljava/util/List;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNullOrEmpty", "([Ljava/lang/Object;)Z", "unzip", "Lkotlin/Pair;", "([Lkotlin/Pair;)Lkotlin/Pair;", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/collections/ArraysKt")
class ArraysKt__ArraysKt
extends ArraysKt__ArraysJVMKt {
    public static final <T> boolean contentDeepEquals(T[] TArray, T[] TArray2) {
        if (TArray == TArray2) {
            return true;
        }
        if (TArray != null && TArray2 != null && TArray.length == TArray2.length) {
            int n = TArray.length;
            for (int i = 0; i < n; ++i) {
                T t = TArray[i];
                T t2 = TArray2[i];
                if (t == t2) continue;
                if (t != null && t2 != null) {
                    if (!(t instanceof Object[] && t2 instanceof Object[] ? !ArraysKt.contentDeepEquals((Object[])t, (Object[])t2) : (t instanceof byte[] && t2 instanceof byte[] ? !Arrays.equals((byte[])t, (byte[])t2) : (t instanceof short[] && t2 instanceof short[] ? !Arrays.equals((short[])t, (short[])t2) : (t instanceof int[] && t2 instanceof int[] ? !Arrays.equals((int[])t, (int[])t2) : (t instanceof long[] && t2 instanceof long[] ? !Arrays.equals((long[])t, (long[])t2) : (t instanceof float[] && t2 instanceof float[] ? !Arrays.equals((float[])t, (float[])t2) : (t instanceof double[] && t2 instanceof double[] ? !Arrays.equals((double[])t, (double[])t2) : (t instanceof char[] && t2 instanceof char[] ? !Arrays.equals((char[])t, (char[])t2) : (t instanceof boolean[] && t2 instanceof boolean[] ? !Arrays.equals((boolean[])t, (boolean[])t2) : (t instanceof UByteArray && t2 instanceof UByteArray ? !UArraysKt.contentEquals-kV0jMPg(((UByteArray)t).unbox-impl(), ((UByteArray)t2).unbox-impl()) : (t instanceof UShortArray && t2 instanceof UShortArray ? !UArraysKt.contentEquals-FGO6Aew(((UShortArray)t).unbox-impl(), ((UShortArray)t2).unbox-impl()) : (t instanceof UIntArray && t2 instanceof UIntArray ? !UArraysKt.contentEquals-KJPZfPQ(((UIntArray)t).unbox-impl(), ((UIntArray)t2).unbox-impl()) : (t instanceof ULongArray && t2 instanceof ULongArray ? !UArraysKt.contentEquals-lec5QzE(((ULongArray)t).unbox-impl(), ((ULongArray)t2).unbox-impl()) : Intrinsics.areEqual(t, t2) ^ true)))))))))))))) continue;
                    return false;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    public static final <T> String contentDeepToString(T[] object) {
        if (object == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder(RangesKt.coerceAtMost(((T[])object).length, 0x19999999) * 5 + 2);
        ArraysKt__ArraysKt.contentDeepToStringInternal$ArraysKt__ArraysKt(object, stringBuilder, new ArrayList());
        object = stringBuilder.toString();
        Intrinsics.checkNotNullExpressionValue(object, "StringBuilder(capacity).\u2026builderAction).toString()");
        return object;
    }

    private static final <T> void contentDeepToStringInternal$ArraysKt__ArraysKt(T[] TArray, StringBuilder stringBuilder, List<Object[]> list) {
        if (list.contains(TArray)) {
            stringBuilder.append("[...]");
            return;
        }
        list.add(TArray);
        stringBuilder.append('[');
        int n = TArray.length;
        for (int i = 0; i < n; ++i) {
            Object object;
            T t;
            if (i != 0) {
                stringBuilder.append(", ");
            }
            if ((t = TArray[i]) == null) {
                stringBuilder.append("null");
                continue;
            }
            if (t instanceof Object[]) {
                ArraysKt__ArraysKt.contentDeepToStringInternal$ArraysKt__ArraysKt((Object[])t, stringBuilder, list);
                continue;
            }
            if (t instanceof byte[]) {
                object = Arrays.toString((byte[])t);
                Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.toString(this)");
                stringBuilder.append((String)object);
                continue;
            }
            if (t instanceof short[]) {
                object = Arrays.toString((short[])t);
                Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.toString(this)");
                stringBuilder.append((String)object);
                continue;
            }
            if (t instanceof int[]) {
                object = Arrays.toString((int[])t);
                Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.toString(this)");
                stringBuilder.append((String)object);
                continue;
            }
            if (t instanceof long[]) {
                object = Arrays.toString((long[])t);
                Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.toString(this)");
                stringBuilder.append((String)object);
                continue;
            }
            if (t instanceof float[]) {
                object = Arrays.toString((float[])t);
                Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.toString(this)");
                stringBuilder.append((String)object);
                continue;
            }
            if (t instanceof double[]) {
                object = Arrays.toString((double[])t);
                Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.toString(this)");
                stringBuilder.append((String)object);
                continue;
            }
            if (t instanceof char[]) {
                object = Arrays.toString((char[])t);
                Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.toString(this)");
                stringBuilder.append((String)object);
                continue;
            }
            if (t instanceof boolean[]) {
                object = Arrays.toString((boolean[])t);
                Intrinsics.checkNotNullExpressionValue(object, "java.util.Arrays.toString(this)");
                stringBuilder.append((String)object);
                continue;
            }
            boolean bl = t instanceof UByteArray;
            object = null;
            KMappedMarker kMappedMarker = null;
            Object var9_9 = null;
            UIntArray uIntArray = null;
            if (bl) {
                kMappedMarker = (UByteArray)t;
                object = uIntArray;
                if (kMappedMarker != null) {
                    object = ((UByteArray)kMappedMarker).unbox-impl();
                }
                stringBuilder.append(UArraysKt.contentToString-2csIQuQ((byte[])object));
                continue;
            }
            if (t instanceof UShortArray) {
                kMappedMarker = (UShortArray)t;
                if (kMappedMarker != null) {
                    object = ((UShortArray)kMappedMarker).unbox-impl();
                }
                stringBuilder.append(UArraysKt.contentToString-d-6D3K8((short[])object));
                continue;
            }
            if (t instanceof UIntArray) {
                uIntArray = (UIntArray)t;
                object = kMappedMarker;
                if (uIntArray != null) {
                    object = uIntArray.unbox-impl();
                }
                stringBuilder.append(UArraysKt.contentToString-XUkPCBk((int[])object));
                continue;
            }
            if (t instanceof ULongArray) {
                kMappedMarker = (ULongArray)t;
                object = var9_9;
                if (kMappedMarker != null) {
                    object = ((ULongArray)kMappedMarker).unbox-impl();
                }
                stringBuilder.append(UArraysKt.contentToString-uLth9ew((long[])object));
                continue;
            }
            stringBuilder.append(t.toString());
        }
        stringBuilder.append(']');
        list.remove(CollectionsKt.getLastIndex(list));
    }

    public static final <T> List<T> flatten(T[][] TArray) {
        int n;
        Intrinsics.checkNotNullParameter(TArray, "$this$flatten");
        Object object = (Object[])TArray;
        int n2 = 0;
        int n3 = ((Object[])object).length;
        int n4 = 0;
        for (n = 0; n < n3; ++n) {
            n2 += ((Object[])object[n]).length;
        }
        object = new ArrayList(n2);
        n2 = TArray.length;
        for (n = n4; n < n2; ++n) {
            T[] TArray2 = TArray[n];
            CollectionsKt.addAll((Collection)object, TArray2);
        }
        return (List)object;
    }

    private static final <C extends Object[], R> R ifEmpty(C object, Function0<? extends R> function0) {
        block0: {
            boolean bl = ((C)object).length == 0;
            if (!bl) break block0;
            object = function0.invoke();
        }
        return (R)object;
    }

    private static final boolean isNullOrEmpty(Object[] objectArray) {
        boolean bl;
        boolean bl2 = false;
        if (objectArray == null || (bl = objectArray.length == 0)) {
            bl2 = true;
        }
        return bl2;
    }

    public static final <T, R> Pair<List<T>, List<R>> unzip(Pair<? extends T, ? extends R>[] pairArray) {
        Intrinsics.checkNotNullParameter(pairArray, "$this$unzip");
        ArrayList<T> arrayList = new ArrayList<T>(pairArray.length);
        ArrayList<R> arrayList2 = new ArrayList<R>(pairArray.length);
        for (Pair<T, R> pair : pairArray) {
            arrayList.add(pair.getFirst());
            arrayList2.add(pair.getSecond());
        }
        return TuplesKt.to(arrayList, arrayList2);
    }
}

