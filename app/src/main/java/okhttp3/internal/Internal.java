/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.net.MalformedURLException;
import java.net.UnknownHostException;
import javax.net.ssl.SSLSocket;
import okhttp3.Address;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.ConnectionSpec;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.internal.InternalCache;
import okhttp3.internal.RouteDatabase;
import okhttp3.internal.http.StreamAllocation;
import okhttp3.internal.io.RealConnection;

public abstract class Internal {
    public static Internal instance;

    public static void initializeInstanceForTests() {
        new OkHttpClient();
    }

    public abstract void addLenient(Headers.Builder var1, String var2);

    public abstract void addLenient(Headers.Builder var1, String var2, String var3);

    public abstract void apply(ConnectionSpec var1, SSLSocket var2, boolean var3);

    public abstract StreamAllocation callEngineGetStreamAllocation(Call var1);

    public abstract void callEnqueue(Call var1, Callback var2, boolean var3);

    public abstract boolean connectionBecameIdle(ConnectionPool var1, RealConnection var2);

    public abstract RealConnection get(ConnectionPool var1, Address var2, StreamAllocation var3);

    public abstract HttpUrl getHttpUrlChecked(String var1) throws MalformedURLException, UnknownHostException;

    public abstract InternalCache internalCache(OkHttpClient var1);

    public abstract void put(ConnectionPool var1, RealConnection var2);

    public abstract RouteDatabase routeDatabase(ConnectionPool var1);

    public abstract void setCache(OkHttpClient.Builder var1, InternalCache var2);
}

