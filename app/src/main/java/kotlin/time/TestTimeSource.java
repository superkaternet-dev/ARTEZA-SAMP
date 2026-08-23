/*
 * Decompiled with CFR 0.152.
 */
package kotlin.time;

import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.time.AbstractLongTimeSource;
import kotlin.time.Duration;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@Metadata(bv={1, 0, 3}, d1={"\u0000 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0007\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\t\u0010\nJ\u001b\u0010\u000b\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0086\u0002\u00f8\u0001\u0000\u00a2\u0006\u0004\b\f\u0010\nJ\b\u0010\r\u001a\u00020\u0004H\u0014R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\u000e"}, d2={"Lkotlin/time/TestTimeSource;", "Lkotlin/time/AbstractLongTimeSource;", "()V", "reading", "", "overflow", "", "duration", "Lkotlin/time/Duration;", "overflow-LRDsOJo", "(D)V", "plusAssign", "plusAssign-LRDsOJo", "read", "kotlin-stdlib"}, k=1, mv={1, 4, 1})
public final class TestTimeSource
extends AbstractLongTimeSource {
    private long reading;

    public TestTimeSource() {
        super(TimeUnit.NANOSECONDS);
    }

    private final void overflow-LRDsOJo(double d) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TestTimeSource will overflow if its reading ");
        stringBuilder.append(this.reading);
        stringBuilder.append("ns is advanced by ");
        stringBuilder.append(Duration.toString-impl(d));
        stringBuilder.append('.');
        throw (Throwable)new IllegalStateException(stringBuilder.toString());
    }

    public final void plusAssign-LRDsOJo(double d) {
        long l;
        double d2 = Duration.toDouble-impl(d, this.getUnit());
        long l2 = (long)d2;
        if (l2 != Long.MIN_VALUE && l2 != Long.MAX_VALUE) {
            long l3 = this.reading;
            l = l3 + l2;
            if ((l3 ^ l2) >= 0L && (l3 ^ l) < 0L) {
                this.overflow-LRDsOJo(d);
            }
        } else {
            double d3 = this.reading;
            Double.isNaN(d3);
            d2 = d3 + d2;
            if (d2 > (double)Long.MAX_VALUE || d2 < (double)Long.MIN_VALUE) {
                this.overflow-LRDsOJo(d);
            }
            l = (long)d2;
        }
        this.reading = l;
    }

    @Override
    protected long read() {
        return this.reading;
    }
}

