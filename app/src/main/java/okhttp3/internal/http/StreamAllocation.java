/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;
import okhttp3.Address;
import okhttp3.ConnectionPool;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.RouteDatabase;
import okhttp3.internal.Util;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.StreamResetException;
import okhttp3.internal.http.Http1xStream;
import okhttp3.internal.http.Http2xStream;
import okhttp3.internal.http.HttpStream;
import okhttp3.internal.http.RouteException;
import okhttp3.internal.http.RouteSelector;
import okhttp3.internal.io.RealConnection;

public final class StreamAllocation {
    public final Address address;
    private boolean canceled;
    private RealConnection connection;
    private final ConnectionPool connectionPool;
    private int refusedStreamCount;
    private boolean released;
    private Route route;
    private final RouteSelector routeSelector;
    private HttpStream stream;

    public StreamAllocation(ConnectionPool connectionPool, Address address) {
        this.connectionPool = connectionPool;
        this.address = address;
        this.routeSelector = new RouteSelector(address, this.routeDatabase());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void deallocate(boolean bl, boolean bl2, boolean bl3) {
        RealConnection realConnection;
        block12: {
            Object var5_5;
            block13: {
                RealConnection realConnection2 = null;
                var5_5 = null;
                ConnectionPool connectionPool = this.connectionPool;
                // MONITORENTER : connectionPool
                if (bl3) {
                    this.stream = null;
                }
                if (bl2) {
                    this.released = true;
                }
                RealConnection realConnection3 = this.connection;
                realConnection = realConnection2;
                if (realConnection3 == null) break block12;
                if (bl) {
                    realConnection3.noNewStreams = true;
                }
                realConnection = realConnection2;
                if (this.stream != null) break block12;
                if (this.released) break block13;
                realConnection = realConnection2;
                if (!this.connection.noNewStreams) break block12;
            }
            this.release(this.connection);
            realConnection = var5_5;
            if (this.connection.allocations.isEmpty()) {
                this.connection.idleAtNanos = System.nanoTime();
                realConnection = var5_5;
                if (Internal.instance.connectionBecameIdle(this.connectionPool, this.connection)) {
                    realConnection = this.connection;
                }
            }
            this.connection = null;
        }
        // MONITOREXIT : connectionPool
        if (realConnection == null) return;
        Util.closeQuietly(realConnection.socket());
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private RealConnection findConnection(int n, int n2, int n3, boolean bl) throws IOException, RouteException {
        Object object;
        Object object2 = this.connectionPool;
        synchronized (object2) {
            if (this.released) {
                IllegalStateException illegalStateException = new IllegalStateException("released");
                throw illegalStateException;
            }
            if (this.stream != null) {
                IllegalStateException illegalStateException = new IllegalStateException("stream != null");
                throw illegalStateException;
            }
            if (this.canceled) {
                IOException iOException = new IOException("Canceled");
                throw iOException;
            }
            object = this.connection;
            if (object != null && !((RealConnection)object).noNewStreams) {
                return object;
            }
            object = Internal.instance.get(this.connectionPool, this.address, this);
            if (object != null) {
                this.connection = object;
                return object;
            }
            object = this.route;
        }
        object2 = object;
        if (object == null) {
            object2 = this.routeSelector.next();
            object = this.connectionPool;
            synchronized (object) {
                this.route = object2;
                this.refusedStreamCount = 0;
            }
        }
        object = new RealConnection((Route)object2);
        this.acquire((RealConnection)object);
        object2 = this.connectionPool;
        synchronized (object2) {
            Internal.instance.put(this.connectionPool, (RealConnection)object);
            this.connection = object;
            if (!this.canceled) {
                // MONITOREXIT @DISABLED, blocks:[7, 8] lbl38 : MonitorExitStatement: MONITOREXIT : var5_5
                ((RealConnection)object).connect(n, n2, n3, this.address.connectionSpecs(), bl);
                this.routeDatabase().connected(((RealConnection)object).route());
                return object;
            }
            object = new IOException("Canceled");
            throw object;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private RealConnection findHealthyConnection(int n, int n2, int n3, boolean bl, boolean bl2) throws IOException, RouteException {
        while (true) {
            RealConnection realConnection = this.findConnection(n, n2, n3, bl);
            ConnectionPool connectionPool = this.connectionPool;
            synchronized (connectionPool) {
                if (realConnection.successCount == 0) {
                    return realConnection;
                }
                // MONITOREXIT @DISABLED, blocks:[0, 2, 4] lbl9 : MonitorExitStatement: MONITOREXIT : var6_6
                if (realConnection.isHealthy(bl2)) return realConnection;
                this.noNewStreams();
                continue;
                {
                    catch (Throwable throwable) {}
                    {
                        throw throwable;
                    }
                }
            }
        }
    }

    private void release(RealConnection object) {
        int n = ((RealConnection)object).allocations.size();
        for (int i = 0; i < n; ++i) {
            if (((RealConnection)object).allocations.get(i).get() != this) continue;
            ((RealConnection)object).allocations.remove(i);
            return;
        }
        object = new IllegalStateException();
        throw object;
    }

    private RouteDatabase routeDatabase() {
        return Internal.instance.routeDatabase(this.connectionPool);
    }

    public void acquire(RealConnection realConnection) {
        realConnection.allocations.add(new WeakReference<StreamAllocation>(this));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public void cancel() {
        ConnectionPool connectionPool = this.connectionPool;
        // MONITORENTER : connectionPool
        this.canceled = true;
        HttpStream httpStream = this.stream;
        RealConnection realConnection = this.connection;
        // MONITOREXIT : connectionPool
        if (httpStream != null) {
            httpStream.cancel();
            return;
        }
        if (realConnection == null) return;
        realConnection.cancel();
    }

    public RealConnection connection() {
        synchronized (this) {
            RealConnection realConnection = this.connection;
            return realConnection;
        }
    }

    public boolean hasMoreRoutes() {
        boolean bl = this.route != null || this.routeSelector.hasNext();
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public HttpStream newStream(int n, int n2, int n3, boolean bl, boolean bl2) throws RouteException, IOException {
        HttpStream httpStream;
        try {
            Object object = this.findHealthyConnection(n, n2, n3, bl, bl2);
            if (((RealConnection)object).framedConnection != null) {
                httpStream = new Http2xStream(this, ((RealConnection)object).framedConnection);
            } else {
                ((RealConnection)object).socket().setSoTimeout(n2);
                ((RealConnection)object).source.timeout().timeout(n2, TimeUnit.MILLISECONDS);
                ((RealConnection)object).sink.timeout().timeout(n3, TimeUnit.MILLISECONDS);
                httpStream = new Http1xStream(this, ((RealConnection)object).source, ((RealConnection)object).sink);
            }
            object = this.connectionPool;
            synchronized (object) {
                this.stream = httpStream;
            }
        }
        catch (IOException iOException) {
            throw new RouteException(iOException);
        }
        return httpStream;
    }

    public void noNewStreams() {
        this.deallocate(true, false, false);
    }

    public void release() {
        this.deallocate(false, true, false);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public HttpStream stream() {
        ConnectionPool connectionPool = this.connectionPool;
        synchronized (connectionPool) {
            return this.stream;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void streamFailed(IOException iOException) {
        boolean bl = false;
        ConnectionPool connectionPool = this.connectionPool;
        synchronized (connectionPool) {
            boolean bl2;
            block13: {
                block11: {
                    block12: {
                        if (!(iOException instanceof StreamResetException)) break block11;
                        iOException = (StreamResetException)iOException;
                        if (((StreamResetException)iOException).errorCode == ErrorCode.REFUSED_STREAM) {
                            ++this.refusedStreamCount;
                        }
                        if (((StreamResetException)iOException).errorCode != ErrorCode.REFUSED_STREAM) break block12;
                        bl2 = bl;
                        if (this.refusedStreamCount <= 1) break block13;
                    }
                    bl2 = true;
                    this.route = null;
                    break block13;
                }
                Object object = this.connection;
                bl2 = bl;
                if (object != null) {
                    bl2 = bl;
                    if (!((RealConnection)object).isMultiplexed()) {
                        bl2 = bl = true;
                        if (this.connection.successCount == 0) {
                            object = this.route;
                            if (object != null && iOException != null) {
                                this.routeSelector.connectFailed((Route)object, iOException);
                            }
                            this.route = null;
                            bl2 = bl;
                        }
                    }
                }
            }
            // MONITOREXIT @DISABLED, blocks:[0, 2] lbl29 : MonitorExitStatement: MONITOREXIT : var4_4
            this.deallocate(bl2, false, true);
            return;
            {
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void streamFinished(boolean bl, HttpStream object) {
        ConnectionPool connectionPool = this.connectionPool;
        synchronized (connectionPool) {
            if (object != null && object == this.stream) {
                if (!bl) {
                    object = this.connection;
                    ++((RealConnection)object).successCount;
                }
                // MONITOREXIT @DISABLED, blocks:[2, 4] lbl7 : MonitorExitStatement: MONITOREXIT : var3_3
                this.deallocate(bl, false, true);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("expected ");
            stringBuilder.append(this.stream);
            stringBuilder.append(" but was ");
            stringBuilder.append(object);
            IllegalStateException illegalStateException = new IllegalStateException(stringBuilder.toString());
            throw illegalStateException;
        }
    }

    public String toString() {
        return this.address.toString();
    }
}

