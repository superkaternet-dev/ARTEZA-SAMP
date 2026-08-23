/*
 * Decompiled with CFR 0.152.
 */
package kotlin.io;

import java.io.BufferedReader;
import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010(\n\u0000\b\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u000f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00020\u0007H\u0096\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2={"Lkotlin/io/LinesSequence;", "Lkotlin/sequences/Sequence;", "", "reader", "Ljava/io/BufferedReader;", "(Ljava/io/BufferedReader;)V", "iterator", "", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
final class LinesSequence
implements Sequence<String> {
    private final BufferedReader reader;

    public LinesSequence(BufferedReader bufferedReader) {
        Intrinsics.checkNotNullParameter(bufferedReader, "reader");
        this.reader = bufferedReader;
    }

    public static final /* synthetic */ BufferedReader access$getReader$p(LinesSequence linesSequence) {
        return linesSequence.reader;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>(this){
            private boolean done;
            private String nextValue;
            final LinesSequence this$0;
            {
                this.this$0 = linesSequence;
            }

            public boolean hasNext() {
                String string2 = this.nextValue;
                boolean bl = true;
                if (string2 == null && !this.done) {
                    this.nextValue = string2 = LinesSequence.access$getReader$p(this.this$0).readLine();
                    if (string2 == null) {
                        this.done = true;
                    }
                }
                if (this.nextValue == null) {
                    bl = false;
                }
                return bl;
            }

            public String next() {
                if (this.hasNext()) {
                    String string2 = this.nextValue;
                    this.nextValue = null;
                    Intrinsics.checkNotNull(string2);
                    return string2;
                }
                throw (Throwable)new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }
        };
    }
}

