/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.RouteDatabase;

public final class RouteSelector {
    private final Address address;
    private List<InetSocketAddress> inetSocketAddresses;
    private InetSocketAddress lastInetSocketAddress;
    private Proxy lastProxy;
    private int nextInetSocketAddressIndex;
    private int nextProxyIndex;
    private final List<Route> postponedRoutes;
    private List<Proxy> proxies = Collections.emptyList();
    private final RouteDatabase routeDatabase;

    public RouteSelector(Address address, RouteDatabase routeDatabase) {
        this.inetSocketAddresses = Collections.emptyList();
        this.postponedRoutes = new ArrayList<Route>();
        this.address = address;
        this.routeDatabase = routeDatabase;
        this.resetNextProxy(address.url(), address.proxy());
    }

    static String getHostString(InetSocketAddress inetSocketAddress) {
        InetAddress inetAddress = inetSocketAddress.getAddress();
        if (inetAddress == null) {
            return inetSocketAddress.getHostName();
        }
        return inetAddress.getHostAddress();
    }

    private boolean hasNextInetSocketAddress() {
        boolean bl = this.nextInetSocketAddressIndex < this.inetSocketAddresses.size();
        return bl;
    }

    private boolean hasNextPostponed() {
        return this.postponedRoutes.isEmpty() ^ true;
    }

    private boolean hasNextProxy() {
        boolean bl = this.nextProxyIndex < this.proxies.size();
        return bl;
    }

    private InetSocketAddress nextInetSocketAddress() throws IOException {
        if (this.hasNextInetSocketAddress()) {
            List<InetSocketAddress> list = this.inetSocketAddresses;
            int n = this.nextInetSocketAddressIndex;
            this.nextInetSocketAddressIndex = n + 1;
            return list.get(n);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No route to ");
        stringBuilder.append(this.address.url().host());
        stringBuilder.append("; exhausted inet socket addresses: ");
        stringBuilder.append(this.inetSocketAddresses);
        throw new SocketException(stringBuilder.toString());
    }

    private Route nextPostponed() {
        return this.postponedRoutes.remove(0);
    }

    private Proxy nextProxy() throws IOException {
        if (this.hasNextProxy()) {
            Object object = this.proxies;
            int n = this.nextProxyIndex;
            this.nextProxyIndex = n + 1;
            object = object.get(n);
            this.resetNextInetSocketAddress((Proxy)object);
            return object;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("No route to ");
        stringBuilder.append(this.address.url().host());
        stringBuilder.append("; exhausted proxy configurations: ");
        stringBuilder.append(this.proxies);
        throw new SocketException(stringBuilder.toString());
    }

    /*
     * Enabled aggressive block sorting
     */
    private void resetNextInetSocketAddress(Proxy object) throws IOException {
        int n;
        Object object2;
        this.inetSocketAddresses = new ArrayList<InetSocketAddress>();
        if (((Proxy)object).type() != Proxy.Type.DIRECT && ((Proxy)object).type() != Proxy.Type.SOCKS) {
            object2 = ((Proxy)object).address();
            if (!(object2 instanceof InetSocketAddress)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Proxy.address() is not an InetSocketAddress: ");
                ((StringBuilder)object).append(object2.getClass());
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            InetSocketAddress inetSocketAddress = (InetSocketAddress)object2;
            object2 = RouteSelector.getHostString(inetSocketAddress);
            n = inetSocketAddress.getPort();
        } else {
            object2 = this.address.url().host();
            n = this.address.url().port();
        }
        if (n >= 1 && n <= 65535) {
            if (((Proxy)object).type() == Proxy.Type.SOCKS) {
                this.inetSocketAddresses.add(InetSocketAddress.createUnresolved((String)object2, n));
            } else {
                object2 = this.address.dns().lookup((String)object2);
                int n2 = object2.size();
                for (int i = 0; i < n2; ++i) {
                    object = object2.get(i);
                    this.inetSocketAddresses.add(new InetSocketAddress((InetAddress)object, n));
                }
            }
            this.nextInetSocketAddressIndex = 0;
            return;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("No route to ");
        ((StringBuilder)object).append((String)object2);
        ((StringBuilder)object).append(":");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append("; port is out of range");
        object = new SocketException(((StringBuilder)object).toString());
        throw object;
    }

    private void resetNextProxy(HttpUrl list, Proxy proxy) {
        if (proxy != null) {
            this.proxies = Collections.singletonList(proxy);
        } else {
            this.proxies = new ArrayList<Proxy>();
            list = this.address.proxySelector().select(((HttpUrl)((Object)list)).uri());
            if (list != null) {
                this.proxies.addAll((Collection<Proxy>)list);
            }
            this.proxies.removeAll(Collections.singleton(Proxy.NO_PROXY));
            this.proxies.add(Proxy.NO_PROXY);
        }
        this.nextProxyIndex = 0;
    }

    public void connectFailed(Route route, IOException iOException) {
        if (route.proxy().type() != Proxy.Type.DIRECT && this.address.proxySelector() != null) {
            this.address.proxySelector().connectFailed(this.address.url().uri(), route.proxy().address(), iOException);
        }
        this.routeDatabase.failed(route);
    }

    public boolean hasNext() {
        boolean bl = this.hasNextInetSocketAddress() || this.hasNextProxy() || this.hasNextPostponed();
        return bl;
    }

    public Route next() throws IOException {
        if (!this.hasNextInetSocketAddress()) {
            if (!this.hasNextProxy()) {
                if (this.hasNextPostponed()) {
                    return this.nextPostponed();
                }
                throw new NoSuchElementException();
            }
            this.lastProxy = this.nextProxy();
        }
        Object object = this.nextInetSocketAddress();
        this.lastInetSocketAddress = object;
        if (this.routeDatabase.shouldPostpone((Route)(object = new Route(this.address, this.lastProxy, (InetSocketAddress)object)))) {
            this.postponedRoutes.add((Route)object);
            return this.next();
        }
        return object;
    }
}

