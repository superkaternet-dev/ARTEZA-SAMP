/*
 * Decompiled with CFR 0.152.
 */
package kotlin.jvm.internal;

import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.collections.BooleanIterator;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv={1, 0, 3}, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0018\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\t\u0010\u0007\u001a\u00020\bH\u0096\u0002J\b\u0010\t\u001a\u00020\bH\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2={"Lkotlin/jvm/internal/ArrayBooleanIterator;", "Lkotlin/collections/BooleanIterator;", "array", "", "([Z)V", "index", "", "hasNext", "", "nextBoolean", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class ArrayBooleanIterator
extends BooleanIterator {
    private final boolean[] array;
    private int index;

    public ArrayBooleanIterator(boolean[] blArray) {
        Intrinsics.checkNotNullParameter(blArray, "array");
        this.array = blArray;
    }

    @Override
    public boolean hasNext() {
        boolean bl = this.index < this.array.length;
        return bl;
    }

    @Override
    public boolean nextBoolean() {
        int n;
        boolean[] blArray;
        try {
            blArray = this.array;
            n = this.index;
            this.index = n + 1;
        }
        catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
            --this.index;
            throw (Throwable)new NoSuchElementException(arrayIndexOutOfBoundsException.getMessage());
        }
        boolean bl = blArray[n];
        return bl;
    }
}

