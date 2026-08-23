/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLProtocolException;
import javax.net.ssl.SSLSocket;
import okhttp3.ConnectionSpec;
import okhttp3.internal.Internal;

public final class ConnectionSpecSelector {
    private final List<ConnectionSpec> connectionSpecs;
    private boolean isFallback;
    private boolean isFallbackPossible;
    private int nextModeIndex = 0;

    public ConnectionSpecSelector(List<ConnectionSpec> list) {
        this.connectionSpecs = list;
    }

    private boolean isFallbackPossible(SSLSocket sSLSocket) {
        for (int i = this.nextModeIndex; i < this.connectionSpecs.size(); ++i) {
            if (!this.connectionSpecs.get(i).isCompatible(sSLSocket)) continue;
            return true;
        }
        return false;
    }

    public ConnectionSpec configureSecureSocket(SSLSocket object) throws IOException {
        Object object2;
        ConnectionSpec connectionSpec = null;
        int n = this.nextModeIndex;
        int n2 = this.connectionSpecs.size();
        while (true) {
            object2 = connectionSpec;
            if (n >= n2) break;
            object2 = this.connectionSpecs.get(n);
            if (((ConnectionSpec)object2).isCompatible((SSLSocket)object)) {
                this.nextModeIndex = n + 1;
                break;
            }
            ++n;
        }
        if (object2 != null) {
            this.isFallbackPossible = this.isFallbackPossible((SSLSocket)object);
            Internal.instance.apply((ConnectionSpec)object2, (SSLSocket)object, this.isFallback);
            return object2;
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("Unable to find acceptable protocols. isFallback=");
        ((StringBuilder)object2).append(this.isFallback);
        ((StringBuilder)object2).append(", modes=");
        ((StringBuilder)object2).append(this.connectionSpecs);
        ((StringBuilder)object2).append(", supported protocols=");
        ((StringBuilder)object2).append(Arrays.toString(((SSLSocket)object).getEnabledProtocols()));
        object = new UnknownServiceException(((StringBuilder)object2).toString());
        throw object;
    }

    public boolean connectionFailed(IOException iOException) {
        boolean bl = true;
        this.isFallback = true;
        if (!this.isFallbackPossible) {
            return false;
        }
        if (iOException instanceof ProtocolException) {
            return false;
        }
        if (iOException instanceof InterruptedIOException) {
            return false;
        }
        if (iOException instanceof SSLHandshakeException && iOException.getCause() instanceof CertificateException) {
            return false;
        }
        if (iOException instanceof SSLPeerUnverifiedException) {
            return false;
        }
        boolean bl2 = bl;
        if (!(iOException instanceof SSLHandshakeException)) {
            bl2 = iOException instanceof SSLProtocolException ? bl : false;
        }
        return bl2;
    }
}

