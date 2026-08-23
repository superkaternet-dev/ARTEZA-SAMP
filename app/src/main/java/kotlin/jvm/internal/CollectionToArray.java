/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import kotlin.Metadata;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u00002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u001e\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a#\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\bH\u0007\u00a2\u0006\u0004\b\t\u0010\n\u001a5\u0010\u0006\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0010\u0010\u000b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0002\u0018\u00010\u0001H\u0007\u00a2\u0006\u0004\b\t\u0010\f\u001a~\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00012\n\u0010\u0007\u001a\u0006\u0012\u0002\b\u00030\b2\u0014\u0010\u000e\u001a\u0010\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u000f2\u001a\u0010\u0010\u001a\u0016\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u00112(\u0010\u0012\u001a$\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001\u0012\u0004\u0012\u00020\u0005\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u00010\u0013H\u0082\b\u00a2\u0006\u0002\u0010\u0014\"\u0018\u0010\u0000\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00020\u0001X\u0082\u0004\u00a2\u0006\u0004\n\u0002\u0010\u0003\"\u000e\u0010\u0004\u001a\u00020\u0005X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2={"EMPTY", "", "", "[Ljava/lang/Object;", "MAX_SIZE", "", "collectionToArray", "collection", "", "toArray", "(Ljava/util/Collection;)[Ljava/lang/Object;", "a", "(Ljava/util/Collection;[Ljava/lang/Object;)[Ljava/lang/Object;", "toArrayImpl", "empty", "Lkotlin/Function0;", "alloc", "Lkotlin/Function1;", "trim", "Lkotlin/Function2;", "(Ljava/util/Collection;Lkotlin/jvm/functions/Function0;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)[Ljava/lang/Object;", "kotlin-stdlib"}, k=2, mv={1, 4, 1})
public final class CollectionToArray {
    private static final Object[] EMPTY = new Object[0];
    private static final int MAX_SIZE = 0x7FFFFFFD;

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final Object[] toArray(Collection<?> objectArray) {
        Intrinsics.checkNotNullParameter(objectArray, "collection");
        int n = objectArray.size();
        if (n == 0) {
            return EMPTY;
        }
        Iterator iterator2 = objectArray.iterator();
        if (!iterator2.hasNext()) {
            return EMPTY;
        }
        objectArray = new Object[n];
        n = 0;
        while (true) {
            Object[] objectArray2;
            int n2 = n + 1;
            objectArray[n] = iterator2.next();
            if (n2 >= objectArray.length) {
                int n3;
                if (!iterator2.hasNext()) return objectArray;
                n = n3 = n2 * 3 + 1 >>> 1;
                if (n3 <= n2) {
                    if (n2 >= 0x7FFFFFFD) throw (Throwable)new OutOfMemoryError();
                    n = 0x7FFFFFFD;
                }
                objectArray2 = Arrays.copyOf(objectArray, n);
                Intrinsics.checkNotNullExpressionValue(objectArray2, "Arrays.copyOf(result, newSize)");
            } else {
                objectArray2 = objectArray;
                if (!iterator2.hasNext()) {
                    objectArray = Arrays.copyOf(objectArray, n2);
                    Intrinsics.checkNotNullExpressionValue(objectArray, "Arrays.copyOf(result, size)");
                    return objectArray;
                }
            }
            n = n2;
            objectArray = objectArray2;
        }
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     */
    public static final Object[] toArray(Collection<?> object, Object[] objectArray) {
        void var0_9;
        void var1_12;
        Intrinsics.checkNotNullParameter(object, "collection");
        if (var1_12 == null) {
            Throwable throwable = new NullPointerException();
            throw throwable;
        }
        int n = object.size();
        if (n == 0) {
            if (((void)var1_12).length > 0) {
                var1_12[0] = null;
            }
        } else {
            Iterator iterator2 = object.iterator();
            if (!iterator2.hasNext()) {
                if (((void)var1_12).length > 0) {
                    var1_12[0] = null;
                }
            } else {
                if (n <= ((void)var1_12).length) {
                    void var0_1 = var1_12;
                } else {
                    Object object2 = Array.newInstance(var1_12.getClass().getComponentType(), n);
                    if (object2 == null) throw new NullPointerException("null cannot be cast to non-null type kotlin.Array<kotlin.Any?>");
                    Object[] objectArray2 = (Object[])object2;
                }
                n = 0;
                while (true) {
                    void var5_17;
                    void var0_5;
                    int n2 = n + 1;
                    var0_5[n] = iterator2.next();
                    if (n2 >= ((void)var0_5).length) {
                        int n3;
                        if (!iterator2.hasNext()) {
                            return var0_9;
                        }
                        n = n3 = n2 * 3 + 1 >>> 1;
                        if (n3 <= n2) {
                            if (n2 >= 0x7FFFFFFD) throw (Throwable)new OutOfMemoryError();
                            n = 0x7FFFFFFD;
                        }
                        T[] TArray = Arrays.copyOf(var0_5, n);
                        Intrinsics.checkNotNullExpressionValue(TArray, "Arrays.copyOf(result, newSize)");
                    } else {
                        void var5_19 = var0_5;
                        if (!iterator2.hasNext()) {
                            if (var0_5 != var1_12) {
                                T[] TArray = Arrays.copyOf(var0_5, n2);
                                Intrinsics.checkNotNullExpressionValue(TArray, "Arrays.copyOf(result, size)");
                                return var0_9;
                            }
                            var1_12[n2] = null;
                            break;
                        }
                    }
                    n = n2;
                    void var0_10 = var5_17;
                }
            }
        }
        void var0_7 = var1_12;
        return var0_9;
    }

    private static final Object[] toArrayImpl(Collection<?> objectArray, Function0<Object[]> objectArray2, Function1<? super Integer, Object[]> function1, Function2<? super Object[], ? super Integer, Object[]> function2) {
        int n = objectArray.size();
        if (n == 0) {
            return objectArray2.invoke();
        }
        Iterator<?> iterator2 = objectArray.iterator();
        if (!iterator2.hasNext()) {
            return objectArray2.invoke();
        }
        objectArray2 = function1.invoke((Integer)n);
        n = 0;
        while (true) {
            int n2 = n + 1;
            objectArray2[n] = iterator2.next();
            if (n2 >= objectArray2.length) {
                int n3;
                if (!iterator2.hasNext()) {
                    return objectArray2;
                }
                n = n3 = n2 * 3 + 1 >>> 1;
                if (n3 <= n2) {
                    if (n2 < 0x7FFFFFFD) {
                        n = 0x7FFFFFFD;
                    } else {
                        throw (Throwable)new OutOfMemoryError();
                    }
                }
                objectArray = Arrays.copyOf(objectArray2, n);
                Intrinsics.checkNotNullExpressionValue(objectArray, "Arrays.copyOf(result, newSize)");
            } else {
                objectArray = objectArray2;
                if (!iterator2.hasNext()) {
                    return function2.invoke((Object[])objectArray2, (Integer)n2);
                }
            }
            n = n2;
            objectArray2 = objectArray;
        }
    }
}

