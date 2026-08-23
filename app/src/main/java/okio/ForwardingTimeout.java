/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okio.Timeout;

public class ForwardingTimeout
extends Timeout {
    private Timeout delegate;

    public ForwardingTimeout(Timeout timeout) {
        if (timeout != null) {
            this.delegate = timeout;
            return;
        }
        throw new IllegalArgumentException("delegate == null");
    }

    @Override
    public Timeout clearDeadline() {
        return this.delegate.clearDeadline();
    }

    @Override
    public Timeout clearTimeout() {
        return this.delegate.clearTimeout();
    }

    @Override
    public long deadlineNanoTime() {
        return this.delegate.deadlineNanoTime();
    }

    @Override
    public Timeout deadlineNanoTime(long l) {
        return this.delegate.deadlineNanoTime(l);
    }

    public final Timeout delegate() {
        return this.delegate;
    }

    @Override
    public boolean hasDeadline() {
        return this.delegate.hasDeadline();
    }

    public final ForwardingTimeout setDelegate(Timeout timeout) {
        if (timeout != null) {
            this.delegate = timeout;
            return this;
        }
        throw new IllegalArgumentException("delegate == null");
    }

    @Override
    public void throwIfReached() throws IOException {
        this.delegate.throwIfReached();
    }

    @Override
    public Timeout timeout(long l, TimeUnit timeUnit) {
        return this.delegate.timeout(l, timeUnit);
    }

    @Override
    public long timeoutNanos() {
        return this.delegate.timeoutNanos();
    }
}

