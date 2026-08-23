/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.io;

import java.io.IOException;
import java.io.Serializable;
import java.lang.ref.Reference;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownServiceException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.CertificatePinner;
import okhttp3.Connection;
import okhttp3.ConnectionSpec;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.ConnectionSpecSelector;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.framed.ErrorCode;
import okhttp3.internal.framed.FramedConnection;
import okhttp3.internal.framed.FramedStream;
import okhttp3.internal.http.Http1xStream;
import okhttp3.internal.http.OkHeaders;
import okhttp3.internal.http.RouteException;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.tls.OkHostnameVerifier;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public final class RealConnection
extends FramedConnection.Listener
implements Connection {
    public int allocationLimit;
    public final List<Reference<StreamAllocation>> allocations = new ArrayList<Reference<StreamAllocation>>();
    public volatile FramedConnection framedConnection;
    private Handshake handshake;
    public long idleAtNanos = Long.MAX_VALUE;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    public BufferedSink sink;
    public Socket socket;
    public BufferedSource source;
    public int successCount;

    public RealConnection(Route route) {
        this.route = route;
    }

    private void buildConnection(int n, int n2, int n3, ConnectionSpecSelector connectionSpecSelector) throws IOException {
        this.connectSocket(n, n2, n3, connectionSpecSelector);
        this.establishProtocol(n2, n3, connectionSpecSelector);
    }

    private void buildTunneledConnection(int n, int n2, int n3, ConnectionSpecSelector object) throws IOException {
        Request request = this.createTunnelRequest();
        HttpUrl httpUrl = request.url();
        int n4 = 0;
        while (++n4 <= 21) {
            this.connectSocket(n, n2, n3, (ConnectionSpecSelector)object);
            request = this.createTunnel(n2, n3, request, httpUrl);
            if (request == null) {
                this.establishProtocol(n2, n3, (ConnectionSpecSelector)object);
                return;
            }
            Util.closeQuietly(this.rawSocket);
            this.rawSocket = null;
            this.sink = null;
            this.source = null;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Too many tunnel connections attempted: ");
        ((StringBuilder)object).append(21);
        object = new ProtocolException(((StringBuilder)object).toString());
        throw object;
    }

    private void connectSocket(int n, int n2, int n3, ConnectionSpecSelector object) throws IOException {
        object = this.route.proxy();
        Address address = this.route.address();
        object = ((Proxy)object).type() != Proxy.Type.DIRECT && ((Proxy)object).type() != Proxy.Type.HTTP ? new Socket((Proxy)object) : address.socketFactory().createSocket();
        this.rawSocket = object;
        ((Socket)object).setSoTimeout(n2);
        try {
            Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), n);
        }
        catch (ConnectException connectException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to connect to ");
            stringBuilder.append(this.route.socketAddress());
            throw new ConnectException(stringBuilder.toString());
        }
        this.source = Okio.buffer(Okio.source(this.rawSocket));
        this.sink = Okio.buffer(Okio.sink(this.rawSocket));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void connectTls(int n, int n2, ConnectionSpecSelector object) throws IOException {
        Address address = this.route.address();
        Object object3 = address.sslSocketFactory();
        Object object4 = null;
        Object object5 = null;
        try {
            object5 = object3 = (SSLSocket)((SSLSocketFactory)object3).createSocket(this.rawSocket, address.url().host(), address.url().port(), true);
            object4 = object3;
            object = ((ConnectionSpecSelector)object).configureSecureSocket((SSLSocket)object3);
            object5 = object3;
            object4 = object3;
            if (((ConnectionSpec)object).supportsTlsExtensions()) {
                object5 = object3;
                object4 = object3;
                Platform.get().configureTlsExtensions((SSLSocket)object3, address.url().host(), address.protocols());
            }
            object5 = object3;
            object4 = object3;
            ((SSLSocket)object3).startHandshake();
            object5 = object3;
            object4 = object3;
            Object object2 = Handshake.get(((SSLSocket)object3).getSession());
            object5 = object3;
            object4 = object3;
            if (address.hostnameVerifier().verify(address.url().host(), ((SSLSocket)object3).getSession())) {
                object5 = object3;
                object4 = object3;
                address.certificatePinner().check(address.url().host(), ((Handshake)object2).peerCertificates());
                object5 = object3;
                object4 = object3;
                if (((ConnectionSpec)object).supportsTlsExtensions()) {
                    object5 = object3;
                    object4 = object3;
                    object = Platform.get().getSelectedProtocol((SSLSocket)object3);
                } else {
                    object = null;
                }
                object5 = object3;
                object4 = object3;
                this.socket = object3;
                object5 = object3;
                object4 = object3;
                this.source = Okio.buffer(Okio.source((Socket)object3));
                object5 = object3;
                object4 = object3;
                this.sink = Okio.buffer(Okio.sink(this.socket));
                object5 = object3;
                object4 = object3;
                this.handshake = object2;
                if (object != null) {
                    object5 = object3;
                    object4 = object3;
                    object = Protocol.get((String)object);
                } else {
                    object5 = object3;
                    object4 = object3;
                    object = Protocol.HTTP_1_1;
                }
                object5 = object3;
                object4 = object3;
                this.protocol = object;
                if (object3 != null) {
                    Platform.get().afterHandshake((SSLSocket)object3);
                }
                return;
            }
            object5 = object3;
            object4 = object3;
            object2 = (X509Certificate)((Handshake)object2).peerCertificates().get(0);
            object5 = object3;
            object4 = object3;
            object5 = object3;
            object4 = object3;
            object5 = object3;
            object4 = object3;
            object = new StringBuilder();
            object5 = object3;
            object4 = object3;
            ((StringBuilder)object).append("Hostname ");
            object5 = object3;
            object4 = object3;
            ((StringBuilder)object).append(address.url().host());
            object5 = object3;
            object4 = object3;
            ((StringBuilder)object).append(" not verified:\n    certificate: ");
            object5 = object3;
            object4 = object3;
            ((StringBuilder)object).append(CertificatePinner.pin((Certificate)object2));
            object5 = object3;
            object4 = object3;
            ((StringBuilder)object).append("\n    DN: ");
            object5 = object3;
            object4 = object3;
            ((StringBuilder)object).append(((X509Certificate)object2).getSubjectDN().getName());
            object5 = object3;
            object4 = object3;
            ((StringBuilder)object).append("\n    subjectAltNames: ");
            object5 = object3;
            object4 = object3;
            ((StringBuilder)object).append(OkHostnameVerifier.allSubjectAltNames((X509Certificate)object2));
            object5 = object3;
            object4 = object3;
            SSLPeerUnverifiedException sSLPeerUnverifiedException = new SSLPeerUnverifiedException(((StringBuilder)object).toString());
            object5 = object3;
            object4 = object3;
            throw sSLPeerUnverifiedException;
        }
        catch (Throwable throwable) {
        }
        catch (AssertionError assertionError) {
            object5 = object4;
            if (Util.isAndroidGetsocknameError(assertionError)) {
                object5 = object4;
                object5 = object4;
                object3 = new IOException((Throwable)((Object)assertionError));
                object5 = object4;
                throw object3;
            }
            object5 = object4;
            throw assertionError;
        }
        if (object5 != null) {
            Platform.get().afterHandshake((SSLSocket)object5);
        }
        Util.closeQuietly(object5);
        throw throwable;
    }

    private Request createTunnel(int n, int n2, Request object, HttpUrl object2) throws IOException {
        Object object3 = new StringBuilder();
        ((StringBuilder)object3).append("CONNECT ");
        ((StringBuilder)object3).append(Util.hostHeader((HttpUrl)object2, true));
        ((StringBuilder)object3).append(" HTTP/1.1");
        object2 = ((StringBuilder)object3).toString();
        block4: while (true) {
            long l;
            Http1xStream http1xStream = new Http1xStream(null, this.source, this.sink);
            this.source.timeout().timeout(n, TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout(n2, TimeUnit.MILLISECONDS);
            http1xStream.writeRequest(((Request)object).headers(), (String)object2);
            http1xStream.finishRequest();
            object3 = http1xStream.readResponse().request((Request)object).build();
            long l2 = l = OkHeaders.contentLength((Response)object3);
            if (l == -1L) {
                l2 = 0L;
            }
            object = http1xStream.newFixedLengthSource(l2);
            Util.skipAll((Source)object, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            object.close();
            switch (((Response)object3).code()) {
                default: {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Unexpected response code for CONNECT: ");
                    ((StringBuilder)object).append(((Response)object3).code());
                    throw new IOException(((StringBuilder)object).toString());
                }
                case 407: {
                    object = this.route.address().proxyAuthenticator().authenticate(this.route, (Response)object3);
                    if (object != null) {
                        if (!"close".equalsIgnoreCase(((Response)object3).header("Connection"))) continue block4;
                        return object;
                    }
                    throw new IOException("Failed to authenticate with proxy");
                }
                case 200: 
            }
            break;
        }
        if (this.source.buffer().exhausted() && this.sink.buffer().exhausted()) {
            return null;
        }
        object = new IOException("TLS tunnel buffered too many bytes!");
        throw object;
    }

    private Request createTunnelRequest() throws IOException {
        return new Request.Builder().url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
    }

    private void establishProtocol(int n, int n2, ConnectionSpecSelector object) throws IOException {
        if (this.route.address().sslSocketFactory() != null) {
            this.connectTls(n, n2, (ConnectionSpecSelector)object);
        } else {
            this.protocol = Protocol.HTTP_1_1;
            this.socket = this.rawSocket;
        }
        if (this.protocol != Protocol.SPDY_3 && this.protocol != Protocol.HTTP_2) {
            this.allocationLimit = 1;
        } else {
            this.socket.setSoTimeout(0);
            object = new FramedConnection.Builder(true).socket(this.socket, this.route.address().url().host(), this.source, this.sink).protocol(this.protocol).listener(this).build();
            ((FramedConnection)object).start();
            this.allocationLimit = ((FramedConnection)object).maxConcurrentStreams();
            this.framedConnection = object;
        }
    }

    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }

    public void connect(int n, int n2, int n3, List<ConnectionSpec> object, boolean bl) throws RouteException {
        if (this.protocol == null) {
            StringBuilder stringBuilder = null;
            ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector((List<ConnectionSpec>)object);
            Serializable serializable = stringBuilder;
            if (this.route.address().sslSocketFactory() == null) {
                if (object.contains(ConnectionSpec.CLEARTEXT)) {
                    serializable = stringBuilder;
                } else {
                    serializable = new StringBuilder();
                    serializable.append("CLEARTEXT communication not supported: ");
                    serializable.append(object);
                    throw new RouteException(new UnknownServiceException(serializable.toString()));
                }
            }
            while (this.protocol == null) {
                try {
                    if (this.route.requiresTunnel()) {
                        this.buildTunneledConnection(n, n2, n3, connectionSpecSelector);
                        continue;
                    }
                    this.buildConnection(n, n2, n3, connectionSpecSelector);
                }
                catch (IOException iOException) {
                    Util.closeQuietly(this.socket);
                    Util.closeQuietly(this.rawSocket);
                    this.socket = null;
                    this.rawSocket = null;
                    this.source = null;
                    this.sink = null;
                    this.handshake = null;
                    this.protocol = null;
                    if (serializable == null) {
                        serializable = new RouteException(iOException);
                    } else {
                        ((RouteException)serializable).addConnectException(iOException);
                    }
                    if (bl && connectionSpecSelector.connectionFailed(iOException)) continue;
                    throw serializable;
                }
            }
            return;
        }
        object = new IllegalStateException("already connected");
        throw object;
    }

    @Override
    public Handshake handshake() {
        return this.handshake;
    }

    boolean isConnected() {
        boolean bl = this.protocol != null;
        return bl;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean isHealthy(boolean bl) {
        int n;
        if (this.socket.isClosed()) return false;
        if (this.socket.isInputShutdown()) return false;
        if (this.socket.isOutputShutdown()) {
            return false;
        }
        if (this.framedConnection != null) {
            return true;
        }
        if (!bl) return true;
        try {
            n = this.socket.getSoTimeout();
        }
        catch (IOException iOException) {
            return false;
        }
        catch (SocketTimeoutException socketTimeoutException) {
            // empty catch block
            return true;
        }
        try {
            this.socket.setSoTimeout(1);
            bl = this.source.exhausted();
            if (!bl) return true;
            return false;
        }
        finally {
            this.socket.setSoTimeout(n);
        }
    }

    public boolean isMultiplexed() {
        boolean bl = this.framedConnection != null;
        return bl;
    }

    @Override
    public void onSettings(FramedConnection framedConnection) {
        this.allocationLimit = framedConnection.maxConcurrentStreams();
    }

    @Override
    public void onStream(FramedStream framedStream) throws IOException {
        framedStream.close(ErrorCode.REFUSED_STREAM);
    }

    @Override
    public Protocol protocol() {
        if (this.framedConnection == null) {
            Protocol protocol = this.protocol;
            if (protocol == null) {
                protocol = Protocol.HTTP_1_1;
            }
            return protocol;
        }
        return this.framedConnection.getProtocol();
    }

    @Override
    public Route route() {
        return this.route;
    }

    @Override
    public Socket socket() {
        return this.socket;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Connection{");
        stringBuilder.append(this.route.address().url().host());
        stringBuilder.append(":");
        stringBuilder.append(this.route.address().url().port());
        stringBuilder.append(", proxy=");
        stringBuilder.append(this.route.proxy());
        stringBuilder.append(" hostAddress=");
        stringBuilder.append(this.route.socketAddress());
        stringBuilder.append(" cipherSuite=");
        Object object = this.handshake;
        object = object != null ? ((Handshake)object).cipherSuite() : "none";
        stringBuilder.append(object);
        stringBuilder.append(" protocol=");
        stringBuilder.append((Object)this.protocol);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

