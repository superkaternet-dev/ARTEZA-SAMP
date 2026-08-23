/*
 * Decompiled with CFR 0.152.
 */
package kotlin;

import java.io.Serializable;
import kotlin.InitializedLazyImpl;
import kotlin.Lazy;
import kotlin.Metadata;
import kotlin.UNINITIALIZED_VALUE;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u001f\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b\u00a2\u0006\u0002\u0010\tJ\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\bH\u0002R\u0010\u0010\n\u001a\u0004\u0018\u00010\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0006X\u0088\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00028\u00008VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\r\u00a8\u0006\u0013"}, d2={"Lkotlin/SynchronizedLazyImpl;", "T", "Lkotlin/Lazy;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "initializer", "Lkotlin/Function0;", "lock", "", "(Lkotlin/jvm/functions/Function0;Ljava/lang/Object;)V", "_value", "value", "getValue", "()Ljava/lang/Object;", "isInitialized", "", "toString", "", "writeReplace", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class SynchronizedLazyImpl<T>
implements Lazy<T>,
Serializable {
    private volatile Object _value;
    private Function0<? extends T> initializer;
    private final Object lock;

    public SynchronizedLazyImpl(Function0<? extends T> object, Object object2) {
        Intrinsics.checkNotNullParameter(object, "initializer");
        this.initializer = object;
        this._value = UNINITIALIZED_VALUE.INSTANCE;
        object = object2 != null ? object2 : this;
        this.lock = object;
    }

    public /* synthetic */ SynchronizedLazyImpl(Function0 function0, Object object, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 2) != 0) {
            object = null;
        }
        this(function0, object);
    }

    private final Object writeReplace() {
        return new InitializedLazyImpl<T>(this.getValue());
    }

    @Override
    public T getValue() {
        Function0<T> function0 = this._value;
        if (function0 != UNINITIALIZED_VALUE.INSTANCE) {
            return (T)function0;
        }
        Object object = this.lock;
        synchronized (object) {
            function0 = this._value;
            if (function0 == UNINITIALIZED_VALUE.INSTANCE) {
                function0 = this.initializer;
                Intrinsics.checkNotNull(function0);
                this._value = function0 = function0.invoke();
                this.initializer = null;
            }
            return (T)function0;
        }
    }

    @Override
    public boolean isInitialized() {
        boolean bl = this._value != UNINITIALIZED_VALUE.INSTANCE;
        return bl;
    }

    public String toString() {
        String string2 = this.isInitialized() ? String.valueOf(this.getValue()) : "Lazy value not initialized yet.";
        return string2;
    }
}

