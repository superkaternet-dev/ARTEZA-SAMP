/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Authenticator;
import okhttp3.CertificatePinner;
import okhttp3.ConnectionSpec;
import okhttp3.Dns;
import okhttp3.HttpUrl;
import okhttp3.Protocol;
import okhttp3.internal.Util;

public final class Address {
    final CertificatePinner certificatePinner;
    final List<ConnectionSpec> connectionSpecs;
    final Dns dns;
    final HostnameVerifier hostnameVerifier;
    final List<Protocol> protocols;
    final Proxy proxy;
    final Authenticator proxyAuthenticator;
    final ProxySelector proxySelector;
    final SocketFactory socketFactory;
    final SSLSocketFactory sslSocketFactory;
    final HttpUrl url;

    public Address(String string2, int n, Dns dns, SocketFactory socketFactory, SSLSocketFactory sSLSocketFactory, HostnameVerifier hostnameVerifier, CertificatePinner certificatePinner, Authenticator authenticator, Proxy proxy, List<Protocol> list, List<ConnectionSpec> list2, ProxySelector proxySelector) {
        HttpUrl.Builder builder = new HttpUrl.Builder();
        String string3 = sSLSocketFactory != null ? "https" : "http";
        this.url = builder.scheme(string3).host(string2).port(n).build();
        if (dns != null) {
            this.dns = dns;
            if (socketFactory != null) {
                this.socketFactory = socketFactory;
                if (authenticator != null) {
                    this.proxyAuthenticator = authenticator;
                    if (list != null) {
                        this.protocols = Util.immutableList(list);
                        if (list2 != null) {
                            this.connectionSpecs = Util.immutableList(list2);
                            if (proxySelector != null) {
                                this.proxySelector = proxySelector;
                                this.proxy = proxy;
                                this.sslSocketFactory = sSLSocketFactory;
                                this.hostnameVerifier = hostnameVerifier;
                                this.certificatePinner = certificatePinner;
                                return;
                            }
                            throw new NullPointerException("proxySelector == null");
                        }
                        throw new NullPointerException("connectionSpecs == null");
                    }
                    throw new NullPointerException("protocols == null");
                }
                throw new NullPointerException("proxyAuthenticator == null");
            }
            throw new NullPointerException("socketFactory == null");
        }
        throw new NullPointerException("dns == null");
    }

    public CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }

    public List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }

    public Dns dns() {
        return this.dns;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof Address;
        boolean bl2 = false;
        if (bl) {
            object = (Address)object;
            if (this.url.equals(((Address)object).url) && this.dns.equals(((Address)object).dns) && this.proxyAuthenticator.equals(((Address)object).proxyAuthenticator) && this.protocols.equals(((Address)object).protocols) && this.connectionSpecs.equals(((Address)object).connectionSpecs) && this.proxySelector.equals(((Address)object).proxySelector) && Util.equal(this.proxy, ((Address)object).proxy) && Util.equal(this.sslSocketFactory, ((Address)object).sslSocketFactory) && Util.equal(this.hostnameVerifier, ((Address)object).hostnameVerifier) && Util.equal(this.certificatePinner, ((Address)object).certificatePinner)) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    public int hashCode() {
        int n = this.url.hashCode();
        int n2 = this.dns.hashCode();
        int n3 = this.proxyAuthenticator.hashCode();
        int n4 = this.protocols.hashCode();
        int n5 = this.connectionSpecs.hashCode();
        int n6 = this.proxySelector.hashCode();
        Object object = this.proxy;
        int n7 = 0;
        int n8 = object != null ? ((Proxy)object).hashCode() : 0;
        object = this.sslSocketFactory;
        int n9 = object != null ? object.hashCode() : 0;
        object = this.hostnameVerifier;
        int n10 = object != null ? object.hashCode() : 0;
        object = this.certificatePinner;
        if (object != null) {
            n7 = object.hashCode();
        }
        return (((((((((17 * 31 + n) * 31 + n2) * 31 + n3) * 31 + n4) * 31 + n5) * 31 + n6) * 31 + n8) * 31 + n9) * 31 + n10) * 31 + n7;
    }

    public HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }

    public List<Protocol> protocols() {
        return this.protocols;
    }

    public Proxy proxy() {
        return this.proxy;
    }

    public Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }

    public ProxySelector proxySelector() {
        return this.proxySelector;
    }

    public SocketFactory socketFactory() {
        return this.socketFactory;
    }

    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }

    public HttpUrl url() {
        return this.url;
    }
}

