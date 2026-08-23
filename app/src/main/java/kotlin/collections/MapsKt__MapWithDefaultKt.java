/*
 * Decompiled with CFR 0.152.
 */
package kotlin.collections;

import java.util.Map;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.MapWithDefault;
import kotlin.collections.MapWithDefaultImpl;
import kotlin.collections.MapsKt;
import kotlin.collections.MutableMapWithDefault;
import kotlin.collections.MutableMapWithDefaultImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001e\n\u0002\b\u0003\n\u0002\u0010$\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0002\u001a3\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0001*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\u00032\u0006\u0010\u0004\u001a\u0002H\u0002H\u0001\u00a2\u0006\u0004\b\u0005\u0010\u0006\u001aQ\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\u0003\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0001*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\u00032!\u0010\b\u001a\u001d\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u0004\u0012\u0004\u0012\u0002H\u00010\t\u001aX\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\f\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0001*\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00010\f2!\u0010\b\u001a\u001d\u0012\u0013\u0012\u0011H\u0002\u00a2\u0006\f\b\n\u0012\b\b\u000b\u0012\u0004\b\b(\u0004\u0012\u0004\u0012\u0002H\u00010\tH\u0007\u00a2\u0006\u0002\b\r\u00a8\u0006\u000e"}, d2={"getOrImplicitDefault", "V", "K", "", "key", "getOrImplicitDefaultNullable", "(Ljava/util/Map;Ljava/lang/Object;)Ljava/lang/Object;", "withDefault", "defaultValue", "Lkotlin/Function1;", "Lkotlin/ParameterName;", "name", "", "withDefaultMutable", "kotlin-stdlib"}, k=5, mv={1, 4, 1}, xi=1, xs="kotlin/collections/MapsKt")
class MapsKt__MapWithDefaultKt {
    public static final <K, V> V getOrImplicitDefaultNullable(Map<K, ? extends V> object, K k) {
        Intrinsics.checkNotNullParameter(object, "$this$getOrImplicitDefault");
        if (object instanceof MapWithDefault) {
            return ((MapWithDefault)object).getOrImplicitDefault(k);
        }
        V v = object.get(k);
        if (v == null && !object.containsKey(k)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Key ");
            ((StringBuilder)object).append(k);
            ((StringBuilder)object).append(" is missing in the map.");
            throw (Throwable)new NoSuchElementException(((StringBuilder)object).toString());
        }
        return v;
    }

    public static final <K, V> Map<K, V> withDefault(Map<K, ? extends V> map, Function1<? super K, ? extends V> function1) {
        Intrinsics.checkNotNullParameter(map, "$this$withDefault");
        Intrinsics.checkNotNullParameter(function1, "defaultValue");
        map = map instanceof MapWithDefault ? MapsKt.withDefault(((MapWithDefault)map).getMap(), function1) : (Map<Object, Object>)new MapWithDefaultImpl<K, V>(map, function1);
        return map;
    }

    public static final <K, V> Map<K, V> withDefaultMutable(Map<K, V> map, Function1<? super K, ? extends V> function1) {
        Intrinsics.checkNotNullParameter(map, "$this$withDefault");
        Intrinsics.checkNotNullParameter(function1, "defaultValue");
        map = map instanceof MutableMapWithDefault ? MapsKt.withDefaultMutable(((MutableMapWithDefault)map).getMap(), function1) : (Map<Object, Object>)new MutableMapWithDefaultImpl<K, V>(map, function1);
        return map;
    }
}

