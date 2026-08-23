/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.internal.AndroidPlatform;
import okhttp3.internal.Jdk9Platform;
import okhttp3.internal.JdkWithJettyBootPlatform;
import okio.Buffer;

public class Platform {
    public static final int INFO = 4;
    private static final Platform PLATFORM = Platform.findPlatform();
    public static final int WARN = 5;
    private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());

    public static List<String> alpnProtocolNames(List<Protocol> list) {
        ArrayList<String> arrayList = new ArrayList<String>(list.size());
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            Protocol protocol = list.get(i);
            if (protocol == Protocol.HTTP_1_0) continue;
            arrayList.add(protocol.toString());
        }
        return arrayList;
    }

    static byte[] concatLengthPrefixed(List<Protocol> list) {
        Buffer buffer = new Buffer();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            Protocol protocol = list.get(i);
            if (protocol == Protocol.HTTP_1_0) continue;
            buffer.writeByte(protocol.toString().length());
            buffer.writeUtf8(protocol.toString());
        }
        return buffer.readByteArray();
    }

    private static Platform findPlatform() {
        Platform platform = AndroidPlatform.buildIfSupported();
        if (platform != null) {
            return platform;
        }
        platform = Jdk9Platform.buildIfSupported();
        if (platform != null) {
            return platform;
        }
        platform = JdkWithJettyBootPlatform.buildIfSupported();
        if (platform != null) {
            return platform;
        }
        return new Platform();
    }

    public static Platform get() {
        return PLATFORM;
    }

    static <T> T readFieldOrNull(Object object, Class<T> clazz, String string2) {
        for (Class<?> clazz2 = object.getClass(); clazz2 != Object.class; clazz2 = clazz2.getSuperclass()) {
            block6: {
                Object object2 = clazz2.getDeclaredField(string2);
                ((Field)object2).setAccessible(true);
                object2 = ((Field)object2).get(object);
                if (object2 == null) break block6;
                try {
                    if (!clazz.isInstance(object2)) break block6;
                    object2 = clazz.cast(object2);
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw new AssertionError();
                }
                catch (NoSuchFieldException noSuchFieldException) {
                    continue;
                }
                return (T)object2;
            }
            return null;
        }
        if (!string2.equals("delegate") && (object = Platform.readFieldOrNull(object, Object.class, "delegate")) != null) {
            return Platform.readFieldOrNull(object, clazz, string2);
        }
        return null;
    }

    public void afterHandshake(SSLSocket sSLSocket) {
    }

    public void configureTlsExtensions(SSLSocket sSLSocket, String string2, List<Protocol> list) {
    }

    public void connectSocket(Socket socket, InetSocketAddress inetSocketAddress, int n) throws IOException {
        socket.connect(inetSocketAddress, n);
    }

    public String getPrefix() {
        return "OkHttp";
    }

    public String getSelectedProtocol(SSLSocket sSLSocket) {
        return null;
    }

    public boolean isCleartextTrafficPermitted() {
        return true;
    }

    public void log(int n, String string2, Throwable throwable) {
        Level level = n == 5 ? Level.WARNING : Level.INFO;
        logger.log(level, string2, throwable);
    }

    public X509TrustManager trustManager(SSLSocketFactory object) {
        block3: {
            try {
                object = Platform.readFieldOrNull(object, Class.forName("sun.security.ssl.SSLContextImpl"), "context");
                if (object != null) break block3;
                return null;
            }
            catch (ClassNotFoundException classNotFoundException) {
                return null;
            }
        }
        object = Platform.readFieldOrNull(object, X509TrustManager.class, "trustManager");
        return object;
    }
}

