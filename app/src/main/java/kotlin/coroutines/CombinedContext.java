/*
 * Decompiled with CFR 0.152.
 */
package kotlin.coroutines;

import java.io.Serializable;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.CombinedContext;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

@Metadata(bv={1, 0, 3}, d1={"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0000\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0001\u0018\u00002\u00020\u00012\u00060\u0002j\u0002`\u0003:\u0001!B\u0015\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0007J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0005\u001a\u00020\u0006H\u0002J\u0010\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\u0000H\u0002J\u0013\u0010\f\u001a\u00020\t2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J5\u0010\u000f\u001a\u0002H\u0010\"\u0004\b\u0000\u0010\u00102\u0006\u0010\u0011\u001a\u0002H\u00102\u0018\u0010\u0012\u001a\u0014\u0012\u0004\u0012\u0002H\u0010\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u0002H\u00100\u0013H\u0016\u00a2\u0006\u0002\u0010\u0014J(\u0010\u0015\u001a\u0004\u0018\u0001H\u0016\"\b\b\u0000\u0010\u0016*\u00020\u00062\f\u0010\u0017\u001a\b\u0012\u0004\u0012\u0002H\u00160\u0018H\u0096\u0002\u00a2\u0006\u0002\u0010\u0019J\b\u0010\u001a\u001a\u00020\u001bH\u0016J\u0014\u0010\u001c\u001a\u00020\u00012\n\u0010\u0017\u001a\u0006\u0012\u0002\b\u00030\u0018H\u0016J\b\u0010\u001d\u001a\u00020\u001bH\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016J\b\u0010 \u001a\u00020\u000eH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\""}, d2={"Lkotlin/coroutines/CombinedContext;", "Lkotlin/coroutines/CoroutineContext;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "left", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "(Lkotlin/coroutines/CoroutineContext;Lkotlin/coroutines/CoroutineContext$Element;)V", "contains", "", "containsAll", "context", "equals", "other", "", "fold", "R", "initial", "operation", "Lkotlin/Function2;", "(Ljava/lang/Object;Lkotlin/jvm/functions/Function2;)Ljava/lang/Object;", "get", "E", "key", "Lkotlin/coroutines/CoroutineContext$Key;", "(Lkotlin/coroutines/CoroutineContext$Key;)Lkotlin/coroutines/CoroutineContext$Element;", "hashCode", "", "minusKey", "size", "toString", "", "writeReplace", "Serialized", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class CombinedContext
implements CoroutineContext,
Serializable {
    private final CoroutineContext.Element element;
    private final CoroutineContext left;

    public CombinedContext(CoroutineContext coroutineContext, CoroutineContext.Element element) {
        Intrinsics.checkNotNullParameter(coroutineContext, "left");
        Intrinsics.checkNotNullParameter(element, "element");
        this.left = coroutineContext;
        this.element = element;
    }

    private final boolean contains(CoroutineContext.Element element) {
        return Intrinsics.areEqual(this.get(element.getKey()), element);
    }

    private final boolean containsAll(CombinedContext object) {
        do {
            if (this.contains(((CombinedContext)object).element)) continue;
            return false;
        } while ((object = ((CombinedContext)object).left) instanceof CombinedContext);
        if (object != null) {
            return this.contains((CoroutineContext.Element)object);
        }
        object = new NullPointerException("null cannot be cast to non-null type kotlin.coroutines.CoroutineContext.Element");
        throw object;
    }

    private final int size() {
        CoroutineContext coroutineContext = this;
        int n = 2;
        while (true) {
            CoroutineContext coroutineContext2 = coroutineContext.left;
            coroutineContext = coroutineContext2;
            if (!(coroutineContext2 instanceof CombinedContext)) {
                coroutineContext = null;
            }
            if (coroutineContext == null) break;
            ++n;
        }
        return n;
    }

    private final Object writeReplace() {
        int n = this.size();
        CoroutineContext[] coroutineContextArray = new CoroutineContext[n];
        Ref.IntRef intRef = new Ref.IntRef();
        boolean bl = false;
        intRef.element = 0;
        this.fold(Unit.INSTANCE, (Function2)new Function2<Unit, CoroutineContext.Element, Unit>(coroutineContextArray, intRef){
            final CoroutineContext[] $elements;
            final Ref.IntRef $index;
            {
                this.$elements = coroutineContextArray;
                this.$index = intRef;
                super(2);
            }

            public final void invoke(Unit coroutineContextArray, CoroutineContext.Element element) {
                Intrinsics.checkNotNullParameter(coroutineContextArray, "<anonymous parameter 0>");
                Intrinsics.checkNotNullParameter(element, "element");
                coroutineContextArray = this.$elements;
                Ref.IntRef intRef = this.$index;
                int n = intRef.element;
                intRef.element = n + 1;
                coroutineContextArray[n] = element;
            }
        });
        if (intRef.element == n) {
            bl = true;
        }
        if (bl) {
            return new Serialized(coroutineContextArray);
        }
        throw (Throwable)new IllegalStateException("Check failed.".toString());
    }

    public boolean equals(Object object) {
        boolean bl = this == object || object instanceof CombinedContext && ((CombinedContext)object).size() == this.size() && ((CombinedContext)object).containsAll(this);
        return bl;
    }

    @Override
    public <R> R fold(R r, Function2<? super R, ? super CoroutineContext.Element, ? extends R> function2) {
        Intrinsics.checkNotNullParameter(function2, "operation");
        return function2.invoke(this.left.fold(r, function2), this.element);
    }

    @Override
    public <E extends CoroutineContext.Element> E get(CoroutineContext.Key<E> key) {
        Intrinsics.checkNotNullParameter(key, "key");
        CoroutineContext coroutineContext = this;
        do {
            E e;
            if ((e = ((CombinedContext)coroutineContext).element.get(key)) == null) continue;
            return e;
        } while ((coroutineContext = ((CombinedContext)coroutineContext).left) instanceof CombinedContext);
        return coroutineContext.get(key);
    }

    public int hashCode() {
        return this.left.hashCode() + this.element.hashCode();
    }

    @Override
    public CoroutineContext minusKey(CoroutineContext.Key<?> object) {
        Intrinsics.checkNotNullParameter(object, "key");
        if (this.element.get(object) != null) {
            return this.left;
        }
        object = (object = this.left.minusKey((CoroutineContext.Key<?>)object)) == this.left ? (CoroutineContext)this : (object == EmptyCoroutineContext.INSTANCE ? (CoroutineContext)this.element : (CoroutineContext)new CombinedContext((CoroutineContext)object, this.element));
        return object;
    }

    @Override
    public CoroutineContext plus(CoroutineContext coroutineContext) {
        Intrinsics.checkNotNullParameter(coroutineContext, "context");
        return CoroutineContext.DefaultImpls.plus(this, coroutineContext);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(this.fold("", toString.1.INSTANCE));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Metadata(bv={1, 0, 3}, d1={"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u0002\b\u0002\u0018\u0000 \f2\u00060\u0001j\u0002`\u0002:\u0001\fB\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\u0002\u0010\u0006J\b\u0010\n\u001a\u00020\u000bH\u0002R\u0019\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\n\n\u0002\u0010\t\u001a\u0004\b\u0007\u0010\b\u00a8\u0006\r"}, d2={"Lkotlin/coroutines/CombinedContext$Serialized;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "elements", "", "Lkotlin/coroutines/CoroutineContext;", "([Lkotlin/coroutines/CoroutineContext;)V", "getElements", "()[Lkotlin/coroutines/CoroutineContext;", "[Lkotlin/coroutines/CoroutineContext;", "readResolve", "", "Companion", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
    private static final class Serialized
    implements Serializable {
        public static final Companion Companion = new Companion(null);
        private static final long serialVersionUID = 0L;
        private final CoroutineContext[] elements;

        public Serialized(CoroutineContext[] coroutineContextArray) {
            Intrinsics.checkNotNullParameter(coroutineContextArray, "elements");
            this.elements = coroutineContextArray;
        }

        private final Object readResolve() {
            CoroutineContext[] coroutineContextArray = this.elements;
            CoroutineContext coroutineContext = EmptyCoroutineContext.INSTANCE;
            for (CoroutineContext coroutineContext2 : coroutineContextArray) {
                coroutineContext = ((CoroutineContext)coroutineContext).plus(coroutineContext2);
            }
            return coroutineContext;
        }

        public final CoroutineContext[] getElements() {
            return this.elements;
        }

        @Metadata(bv={1, 0, 3}, d1={"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2={"Lkotlin/coroutines/CombinedContext$Serialized$Companion;", "", "()V", "serialVersionUID", "", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }
        }
    }
}

