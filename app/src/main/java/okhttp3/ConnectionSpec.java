/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.util.Arrays;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.CipherSuite;
import okhttp3.TlsVersion;
import okhttp3.internal.Util;

public final class ConnectionSpec {
    private static final CipherSuite[] APPROVED_CIPHER_SUITES;
    public static final ConnectionSpec CLEARTEXT;
    public static final ConnectionSpec COMPATIBLE_TLS;
    public static final ConnectionSpec MODERN_TLS;
    private final String[] cipherSuites;
    private final boolean supportsTlsExtensions;
    private final boolean tls;
    private final String[] tlsVersions;

    static {
        Object object = new CipherSuite[]{CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_DHE_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256, CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA, CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA, CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA};
        APPROVED_CIPHER_SUITES = object;
        MODERN_TLS = object = new Builder(true).cipherSuites((CipherSuite)((Object)object)).tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
        COMPATIBLE_TLS = new Builder((ConnectionSpec)object).tlsVersions(TlsVersion.TLS_1_0).supportsTlsExtensions(true).build();
        CLEARTEXT = new Builder(false).build();
    }

    private ConnectionSpec(Builder builder) {
        this.tls = builder.tls;
        this.cipherSuites = builder.cipherSuites;
        this.tlsVersions = builder.tlsVersions;
        this.supportsTlsExtensions = builder.supportsTlsExtensions;
    }

    private static boolean nonEmptyIntersection(String[] stringArray, String[] stringArray2) {
        if (stringArray != null && stringArray2 != null && stringArray.length != 0 && stringArray2.length != 0) {
            int n = stringArray.length;
            for (int i = 0; i < n; ++i) {
                if (!Util.contains(stringArray2, stringArray[i])) continue;
                return true;
            }
            return false;
        }
        return false;
    }

    private ConnectionSpec supportedSpec(SSLSocket sSLSocket, boolean bl) {
        String[] stringArray = this.cipherSuites;
        stringArray = stringArray != null ? Util.intersect(String.class, stringArray, sSLSocket.getEnabledCipherSuites()) : sSLSocket.getEnabledCipherSuites();
        String[] stringArray2 = this.tlsVersions;
        stringArray2 = stringArray2 != null ? Util.intersect(String.class, stringArray2, sSLSocket.getEnabledProtocols()) : sSLSocket.getEnabledProtocols();
        String[] stringArray3 = stringArray;
        if (bl) {
            stringArray3 = stringArray;
            if (Util.contains(sSLSocket.getSupportedCipherSuites(), "TLS_FALLBACK_SCSV")) {
                stringArray3 = Util.concat(stringArray, "TLS_FALLBACK_SCSV");
            }
        }
        return new Builder(this).cipherSuites(stringArray3).tlsVersions(stringArray2).build();
    }

    void apply(SSLSocket sSLSocket, boolean bl) {
        String[] stringArray = this.supportedSpec(sSLSocket, bl);
        String[] stringArray2 = stringArray.tlsVersions;
        if (stringArray2 != null) {
            sSLSocket.setEnabledProtocols(stringArray2);
        }
        if ((stringArray = stringArray.cipherSuites) != null) {
            sSLSocket.setEnabledCipherSuites(stringArray);
        }
    }

    public List<CipherSuite> cipherSuites() {
        String[] stringArray;
        Object[] objectArray = this.cipherSuites;
        if (objectArray == null) {
            return null;
        }
        objectArray = new CipherSuite[objectArray.length];
        for (int i = 0; i < (stringArray = this.cipherSuites).length; ++i) {
            objectArray[i] = CipherSuite.forJavaName(stringArray[i]);
        }
        return Util.immutableList(objectArray);
    }

    public boolean equals(Object object) {
        if (!(object instanceof ConnectionSpec)) {
            return false;
        }
        if (object == this) {
            return true;
        }
        object = (ConnectionSpec)object;
        boolean bl = this.tls;
        if (bl != ((ConnectionSpec)object).tls) {
            return false;
        }
        if (bl) {
            if (!Arrays.equals(this.cipherSuites, ((ConnectionSpec)object).cipherSuites)) {
                return false;
            }
            if (!Arrays.equals(this.tlsVersions, ((ConnectionSpec)object).tlsVersions)) {
                return false;
            }
            if (this.supportsTlsExtensions != ((ConnectionSpec)object).supportsTlsExtensions) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int n = 17;
        if (this.tls) {
            n = ((17 * 31 + Arrays.hashCode(this.cipherSuites)) * 31 + Arrays.hashCode(this.tlsVersions)) * 31 + (this.supportsTlsExtensions ^ 1);
        }
        return n;
    }

    public boolean isCompatible(SSLSocket sSLSocket) {
        if (!this.tls) {
            return false;
        }
        String[] stringArray = this.tlsVersions;
        if (stringArray != null && !ConnectionSpec.nonEmptyIntersection(stringArray, sSLSocket.getEnabledProtocols())) {
            return false;
        }
        stringArray = this.cipherSuites;
        return stringArray == null || ConnectionSpec.nonEmptyIntersection(stringArray, sSLSocket.getEnabledCipherSuites());
    }

    public boolean isTls() {
        return this.tls;
    }

    public boolean supportsTlsExtensions() {
        return this.supportsTlsExtensions;
    }

    public List<TlsVersion> tlsVersions() {
        String[] stringArray;
        Object[] objectArray = this.tlsVersions;
        if (objectArray == null) {
            return null;
        }
        objectArray = new TlsVersion[objectArray.length];
        for (int i = 0; i < (stringArray = this.tlsVersions).length; ++i) {
            objectArray[i] = TlsVersion.forJavaName(stringArray[i]);
        }
        return Util.immutableList(objectArray);
    }

    public String toString() {
        if (!this.tls) {
            return "ConnectionSpec()";
        }
        Object object = this.cipherSuites;
        String string2 = "[all enabled]";
        object = object != null ? this.cipherSuites().toString() : "[all enabled]";
        if (this.tlsVersions != null) {
            string2 = this.tlsVersions().toString();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ConnectionSpec(cipherSuites=");
        stringBuilder.append((String)object);
        stringBuilder.append(", tlsVersions=");
        stringBuilder.append(string2);
        stringBuilder.append(", supportsTlsExtensions=");
        stringBuilder.append(this.supportsTlsExtensions);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static final class Builder {
        private String[] cipherSuites;
        private boolean supportsTlsExtensions;
        private boolean tls;
        private String[] tlsVersions;

        public Builder(ConnectionSpec connectionSpec) {
            this.tls = connectionSpec.tls;
            this.cipherSuites = connectionSpec.cipherSuites;
            this.tlsVersions = connectionSpec.tlsVersions;
            this.supportsTlsExtensions = connectionSpec.supportsTlsExtensions;
        }

        Builder(boolean bl) {
            this.tls = bl;
        }

        public Builder allEnabledCipherSuites() {
            if (this.tls) {
                this.cipherSuites = null;
                return this;
            }
            throw new IllegalStateException("no cipher suites for cleartext connections");
        }

        public Builder allEnabledTlsVersions() {
            if (this.tls) {
                this.tlsVersions = null;
                return this;
            }
            throw new IllegalStateException("no TLS versions for cleartext connections");
        }

        public ConnectionSpec build() {
            return new ConnectionSpec(this);
        }

        public Builder cipherSuites(String ... stringArray) {
            if (this.tls) {
                if (stringArray.length != 0) {
                    this.cipherSuites = (String[])stringArray.clone();
                    return this;
                }
                throw new IllegalArgumentException("At least one cipher suite is required");
            }
            throw new IllegalStateException("no cipher suites for cleartext connections");
        }

        public Builder cipherSuites(CipherSuite ... object) {
            if (this.tls) {
                String[] stringArray = new String[((CipherSuite[])object).length];
                for (int i = 0; i < ((CipherSuite[])object).length; ++i) {
                    stringArray[i] = object[i].javaName;
                }
                return this.cipherSuites(stringArray);
            }
            object = new IllegalStateException("no cipher suites for cleartext connections");
            throw object;
        }

        public Builder supportsTlsExtensions(boolean bl) {
            if (this.tls) {
                this.supportsTlsExtensions = bl;
                return this;
            }
            throw new IllegalStateException("no TLS extensions for cleartext connections");
        }

        public Builder tlsVersions(String ... stringArray) {
            if (this.tls) {
                if (stringArray.length != 0) {
                    this.tlsVersions = (String[])stringArray.clone();
                    return this;
                }
                throw new IllegalArgumentException("At least one TLS version is required");
            }
            throw new IllegalStateException("no TLS versions for cleartext connections");
        }

        public Builder tlsVersions(TlsVersion ... object) {
            if (this.tls) {
                String[] stringArray = new String[((TlsVersion[])object).length];
                for (int i = 0; i < ((TlsVersion[])object).length; ++i) {
                    stringArray[i] = object[i].javaName;
                }
                return this.tlsVersions(stringArray);
            }
            object = new IllegalStateException("no TLS versions for cleartext connections");
            throw object;
        }
    }
}

