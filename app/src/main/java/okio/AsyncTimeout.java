/*
 * Decompiled with CFR 0.152.
 */
package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import okio.Buffer;
import okio.Segment;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;

public class AsyncTimeout
extends Timeout {
    private static final int TIMEOUT_WRITE_SIZE = 65536;
    private static AsyncTimeout head;
    private boolean inQueue;
    private AsyncTimeout next;
    private long timeoutAt;

    static AsyncTimeout awaitTimeout() throws InterruptedException {
        synchronized (AsyncTimeout.class) {
            AsyncTimeout asyncTimeout;
            block10: {
                block9: {
                    asyncTimeout = AsyncTimeout.head.next;
                    if (asyncTimeout != null) break block9;
                    AsyncTimeout.class.wait();
                    return null;
                }
                long l = asyncTimeout.remainingNanos(System.nanoTime());
                if (l <= 0L) break block10;
                long l2 = l / 1000000L;
                Long.signum(l2);
                int n = (int)(l - 1000000L * l2);
                AsyncTimeout.class.wait(l2, n);
                return null;
            }
            AsyncTimeout.head.next = asyncTimeout.next;
            asyncTimeout.next = null;
            return asyncTimeout;
            finally {
            }
        }
    }

    private static boolean cancelScheduledTimeout(AsyncTimeout asyncTimeout) {
        synchronized (AsyncTimeout.class) {
            AsyncTimeout asyncTimeout2;
            try {
                asyncTimeout2 = head;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
            while (asyncTimeout2 != null) {
                AsyncTimeout asyncTimeout3;
                block6: {
                    asyncTimeout3 = asyncTimeout2.next;
                    if (asyncTimeout3 != asyncTimeout) break block6;
                    asyncTimeout2.next = asyncTimeout.next;
                    asyncTimeout.next = null;
                    return false;
                }
                asyncTimeout2 = asyncTimeout3;
            }
            return true;
        }
    }

    private long remainingNanos(long l) {
        return this.timeoutAt - l;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void scheduleTimeout(AsyncTimeout object, long l, boolean bl) {
        synchronized (AsyncTimeout.class) {
            block13: {
                AsyncTimeout asyncTimeout;
                long l2;
                Object object2;
                block11: {
                    block12: {
                        block10: {
                            if (head == null) {
                                object2 = new AsyncTimeout();
                                head = object2;
                                object2 = new Watchdog();
                                ((Thread)object2).start();
                            }
                            l2 = System.nanoTime();
                            if (l == 0L || !bl) break block10;
                            ((AsyncTimeout)object).timeoutAt = Math.min(l, ((Timeout)object).deadlineNanoTime() - l2) + l2;
                            break block11;
                        }
                        if (l == 0L) break block12;
                        ((AsyncTimeout)object).timeoutAt = l2 + l;
                        break block11;
                    }
                    if (!bl) break block13;
                    ((AsyncTimeout)object).timeoutAt = ((Timeout)object).deadlineNanoTime();
                }
                l = super.remainingNanos(l2);
                object2 = head;
                while ((asyncTimeout = ((AsyncTimeout)object2).next) != null && l >= asyncTimeout.remainingNanos(l2)) {
                    object2 = ((AsyncTimeout)object2).next;
                }
                ((AsyncTimeout)object).next = ((AsyncTimeout)object2).next;
                ((AsyncTimeout)object2).next = object;
                if (object2 != head) return;
                AsyncTimeout.class.notify();
                return;
            }
            object = new AssertionError();
            throw object;
        }
    }

    public final void enter() {
        if (!this.inQueue) {
            long l = this.timeoutNanos();
            boolean bl = this.hasDeadline();
            if (l == 0L && !bl) {
                return;
            }
            this.inQueue = true;
            AsyncTimeout.scheduleTimeout(this, l, bl);
            return;
        }
        throw new IllegalStateException("Unbalanced enter/exit");
    }

    final IOException exit(IOException iOException) throws IOException {
        if (!this.exit()) {
            return iOException;
        }
        return this.newTimeoutException(iOException);
    }

    final void exit(boolean bl) throws IOException {
        if (this.exit() && bl) {
            throw this.newTimeoutException(null);
        }
    }

    public final boolean exit() {
        if (!this.inQueue) {
            return false;
        }
        this.inQueue = false;
        return AsyncTimeout.cancelScheduledTimeout(this);
    }

    protected IOException newTimeoutException(IOException iOException) {
        InterruptedIOException interruptedIOException = new InterruptedIOException("timeout");
        if (iOException != null) {
            interruptedIOException.initCause(iOException);
        }
        return interruptedIOException;
    }

    public final Sink sink(Sink sink) {
        return new Sink(this, sink){
            final AsyncTimeout this$0;
            final Sink val$sink;
            {
                this.this$0 = asyncTimeout;
                this.val$sink = sink;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void close() throws IOException {
                Throwable throwable2222222;
                block4: {
                    this.this$0.enter();
                    this.val$sink.close();
                    {
                        catch (Throwable throwable2222222) {
                            break block4;
                        }
                        catch (IOException iOException) {}
                        {
                            throw this.this$0.exit(iOException);
                        }
                    }
                    this.this$0.exit(true);
                    return;
                }
                this.this$0.exit(false);
                throw throwable2222222;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void flush() throws IOException {
                Throwable throwable2222222;
                block4: {
                    this.this$0.enter();
                    this.val$sink.flush();
                    {
                        catch (Throwable throwable2222222) {
                            break block4;
                        }
                        catch (IOException iOException) {}
                        {
                            throw this.this$0.exit(iOException);
                        }
                    }
                    this.this$0.exit(true);
                    return;
                }
                this.this$0.exit(false);
                throw throwable2222222;
            }

            @Override
            public Timeout timeout() {
                return this.this$0;
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("AsyncTimeout.sink(");
                stringBuilder.append(this.val$sink);
                stringBuilder.append(")");
                return stringBuilder.toString();
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void write(Buffer buffer, long l) throws IOException {
                Throwable throwable2222222;
                Util.checkOffsetAndCount(buffer.size, 0L, l);
                while (true) {
                    long l2;
                    if (l <= 0L) {
                        return;
                    }
                    long l3 = 0L;
                    Segment segment = buffer.head;
                    while (true) {
                        l2 = l3;
                        if (l3 >= 65536L) break;
                        if ((l3 += (long)(buffer.head.limit - buffer.head.pos)) >= l) {
                            l2 = l;
                            break;
                        }
                        segment = segment.next;
                    }
                    this.this$0.enter();
                    this.val$sink.write(buffer, l2);
                    l -= l2;
                    {
                        catch (Throwable throwable2222222) {
                            break;
                        }
                        catch (IOException iOException) {}
                        {
                            throw this.this$0.exit(iOException);
                        }
                    }
                    this.this$0.exit(true);
                }
                this.this$0.exit(false);
                throw throwable2222222;
            }
        };
    }

    public final Source source(Source source) {
        return new Source(this, source){
            final AsyncTimeout this$0;
            final Source val$source;
            {
                this.this$0 = asyncTimeout;
                this.val$source = source;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void close() throws IOException {
                Throwable throwable2222222;
                block4: {
                    this.val$source.close();
                    {
                        catch (Throwable throwable2222222) {
                            break block4;
                        }
                        catch (IOException iOException) {}
                        {
                            throw this.this$0.exit(iOException);
                        }
                    }
                    this.this$0.exit(true);
                    return;
                }
                this.this$0.exit(false);
                throw throwable2222222;
            }

            /*
             * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public long read(Buffer buffer, long l) throws IOException {
                Throwable throwable2222222;
                block4: {
                    this.this$0.enter();
                    l = this.val$source.read(buffer, l);
                    {
                        catch (Throwable throwable2222222) {
                            break block4;
                        }
                        catch (IOException iOException) {}
                        {
                            throw this.this$0.exit(iOException);
                        }
                    }
                    this.this$0.exit(true);
                    return l;
                }
                this.this$0.exit(false);
                throw throwable2222222;
            }

            @Override
            public Timeout timeout() {
                return this.this$0;
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("AsyncTimeout.source(");
                stringBuilder.append(this.val$source);
                stringBuilder.append(")");
                return stringBuilder.toString();
            }
        };
    }

    protected void timedOut() {
    }

    private static final class Watchdog
    extends Thread {
        public Watchdog() {
            super("Okio Watchdog");
            this.setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                AsyncTimeout asyncTimeout = AsyncTimeout.awaitTimeout();
                if (asyncTimeout == null) continue;
                try {
                    asyncTimeout.timedOut();
                }
                catch (InterruptedException interruptedException) {
                }
            }
        }
    }
}

