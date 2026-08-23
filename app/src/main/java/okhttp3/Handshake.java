/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.security.Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.auth.x500.X500Principal;
import okhttp3.CipherSuite;
import okhttp3.TlsVersion;
import okhttp3.internal.Util;

public final class Handshake {
    private final CipherSuite cipherSuite;
    private final List<Certificate> localCertificates;
    private final List<Certificate> peerCertificates;
    private final TlsVersion tlsVersion;

    private Handshake(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> list, List<Certificate> list2) {
        this.tlsVersion = tlsVersion;
        this.cipherSuite = cipherSuite;
        this.peerCertificates = list;
        this.localCertificates = list2;
    }

    public static Handshake get(SSLSession object) {
        Object object2 = object.getCipherSuite();
        if (object2 != null) {
            CipherSuite cipherSuite = CipherSuite.forJavaName((String)object2);
            object2 = object.getProtocol();
            if (object2 != null) {
                TlsVersion tlsVersion = TlsVersion.forJavaName((String)object2);
                try {
                    object2 = object.getPeerCertificates();
                }
                catch (SSLPeerUnverifiedException sSLPeerUnverifiedException) {
                    object2 = null;
                }
                object2 = object2 != null ? Util.immutableList(object2) : Collections.emptyList();
                object = object.getLocalCertificates();
                object = object != null ? Util.immutableList(object) : Collections.emptyList();
                return new Handshake(tlsVersion, cipherSuite, (List<Certificate>)object2, (List<Certificate>)object);
            }
            throw new IllegalStateException("tlsVersion == null");
        }
        throw new IllegalStateException("cipherSuite == null");
    }

    public static Handshake get(TlsVersion tlsVersion, CipherSuite cipherSuite, List<Certificate> list, List<Certificate> list2) {
        if (cipherSuite != null) {
            return new Handshake(tlsVersion, cipherSuite, Util.immutableList(list), Util.immutableList(list2));
        }
        throw new NullPointerException("cipherSuite == null");
    }

    public CipherSuite cipherSuite() {
        return this.cipherSuite;
    }

    public boolean equals(Object object) {
        boolean bl;
        block1: {
            boolean bl2 = object instanceof Handshake;
            bl = false;
            if (!bl2) {
                return false;
            }
            object = (Handshake)object;
            if (!Util.equal((Object)this.cipherSuite, (Object)((Handshake)object).cipherSuite) || !this.cipherSuite.equals((Object)((Handshake)object).cipherSuite) || !this.peerCertificates.equals(((Handshake)object).peerCertificates) || !this.localCertificates.equals(((Handshake)object).localCertificates)) break block1;
            bl = true;
        }
        return bl;
    }

    public int hashCode() {
        TlsVersion tlsVersion = this.tlsVersion;
        int n = tlsVersion != null ? tlsVersion.hashCode() : 0;
        return (((17 * 31 + n) * 31 + this.cipherSuite.hashCode()) * 31 + this.peerCertificates.hashCode()) * 31 + this.localCertificates.hashCode();
    }

    public List<Certificate> localCertificates() {
        return this.localCertificates;
    }

    public Principal localPrincipal() {
        X500Principal x500Principal = !this.localCertificates.isEmpty() ? ((X509Certificate)this.localCertificates.get(0)).getSubjectX500Principal() : null;
        return x500Principal;
    }

    public List<Certificate> peerCertificates() {
        return this.peerCertificates;
    }

    public Principal peerPrincipal() {
        X500Principal x500Principal = !this.peerCertificates.isEmpty() ? ((X509Certificate)this.peerCertificates.get(0)).getSubjectX500Principal() : null;
        return x500Principal;
    }

    public TlsVersion tlsVersion() {
        return this.tlsVersion;
    }
}

