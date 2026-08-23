/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.time.AbstractLongTimeSource;
import kotlin.time.TimeSource;

@Metadata(bv={1, 0, 3}, d1={"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u00c1\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u0004\u001a\u00020\u0005H\u0014J\b\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\b"}, d2={"Lkotlin/time/MonotonicTimeSource;", "Lkotlin/time/AbstractLongTimeSource;", "Lkotlin/time/TimeSource;", "()V", "read", "", "toString", "", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class MonotonicTimeSource
extends AbstractLongTimeSource
implements TimeSource {
    public static final MonotonicTimeSource INSTANCE = new MonotonicTimeSource();

    private MonotonicTimeSource() {
        super(TimeUnit.NANOSECONDS);
    }

    @Override
    protected long read() {
        return System.nanoTime();
    }

    public String toString() {
        return "TimeSource(System.nanoTime())";
    }
}

