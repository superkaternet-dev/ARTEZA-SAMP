/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.net.InetSocketAddress;
import java.net.Proxy;
import okhttp3.Address;

public final class Route {
    final Address address;
    final InetSocketAddress inetSocketAddress;
    final Proxy proxy;

    public Route(Address address, Proxy proxy, InetSocketAddress inetSocketAddress) {
        if (address != null) {
            if (proxy != null) {
                if (inetSocketAddress != null) {
                    this.address = address;
                    this.proxy = proxy;
                    this.inetSocketAddress = inetSocketAddress;
                    return;
                }
                throw new NullPointerException("inetSocketAddress == null");
            }
            throw new NullPointerException("proxy == null");
        }
        throw new NullPointerException("address == null");
    }

    public Address address() {
        return this.address;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof Route;
        boolean bl2 = false;
        if (bl) {
            object = (Route)object;
            if (this.address.equals(((Route)object).address) && this.proxy.equals(((Route)object).proxy) && this.inetSocketAddress.equals(((Route)object).inetSocketAddress)) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    public int hashCode() {
        return ((17 * 31 + this.address.hashCode()) * 31 + this.proxy.hashCode()) * 31 + this.inetSocketAddress.hashCode();
    }

    public Proxy proxy() {
        return this.proxy;
    }

    public boolean requiresTunnel() {
        boolean bl = this.address.sslSocketFactory != null && this.proxy.type() == Proxy.Type.HTTP;
        return bl;
    }

    public InetSocketAddress socketAddress() {
        return this.inetSocketAddress;
    }
}

