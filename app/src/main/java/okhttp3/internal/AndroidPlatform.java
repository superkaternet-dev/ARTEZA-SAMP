/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package okhttp3.internal;

import android.util.Log;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Protocol;
import okhttp3.internal.OptionalMethod;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;

class AndroidPlatform
extends Platform {
    private static final int MAX_LOG_LENGTH = 4000;
    private final OptionalMethod<Socket> getAlpnSelectedProtocol;
    private final OptionalMethod<Socket> setAlpnProtocols;
    private final OptionalMethod<Socket> setHostname;
    private final OptionalMethod<Socket> setUseSessionTickets;
    private final Class<?> sslParametersClass;

    public AndroidPlatform(Class<?> clazz, OptionalMethod<Socket> optionalMethod, OptionalMethod<Socket> optionalMethod2, OptionalMethod<Socket> optionalMethod3, OptionalMethod<Socket> optionalMethod4) {
        this.sslParametersClass = clazz;
        this.setUseSessionTickets = optionalMethod;
        this.setHostname = optionalMethod2;
        this.getAlpnSelectedProtocol = optionalMethod3;
        this.setAlpnProtocols = optionalMethod4;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Platform buildIfSupported() {
        try {
            var2 = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
            ** GOTO lbl7
        }
        catch (ClassNotFoundException var0_1) {
            try {
                var2 = Class.forName("org.apache.harmony.xnet.provider.jsse.SSLParametersImpl");
lbl7:
                // 2 sources

                var5_4 = new OptionalMethod<Socket>(null, "setUseSessionTickets", new Class[]{Boolean.TYPE});
                var4_5 = new OptionalMethod<Socket>(null, "setHostname", new Class[]{String.class});
            }
            catch (ClassNotFoundException var0_3) {
                return null;
            }
        }
        var1_6 = null;
        var0_2 = var1_6;
        try {
            Class.forName("android.net.Network");
            var0_2 = var1_6;
            var0_2 = var1_6;
            var3_8 = new OptionalMethod<T>(byte[].class, "getAlpnSelectedProtocol", new Class[0]);
            var1_6 = var3_8;
            var0_2 = var1_6;
            var0_2 = var1_6;
            var0_2 = var3_8 = new OptionalMethod<T>(null, "setAlpnProtocols", new Class[]{byte[].class});
            return new AndroidPlatform(var2, var5_4, var4_5, (OptionalMethod<Socket>)var1_6, (OptionalMethod<Socket>)var0_2);
        }
        catch (ClassNotFoundException var1_7) {
            var1_6 = var0_2;
            var0_2 = null;
        }
        return new AndroidPlatform(var2, var5_4, var4_5, (OptionalMethod<Socket>)var1_6, (OptionalMethod<Socket>)var0_2);
    }

    @Override
    public void configureTlsExtensions(SSLSocket sSLSocket, String object, List<Protocol> list) {
        if (object != null) {
            this.setUseSessionTickets.invokeOptionalWithoutCheckedException(sSLSocket, true);
            this.setHostname.invokeOptionalWithoutCheckedException(sSLSocket, object);
        }
        if ((object = this.setAlpnProtocols) != null && ((OptionalMethod)object).isSupported(sSLSocket)) {
            object = AndroidPlatform.concatLengthPrefixed(list);
            this.setAlpnProtocols.invokeWithoutCheckedException(sSLSocket, object);
        }
    }

    @Override
    public void connectSocket(Socket socket, InetSocketAddress serializable, int n) throws IOException {
        try {
            socket.connect((SocketAddress)serializable, n);
            return;
        }
        catch (SecurityException securityException) {
            serializable = new IOException("Exception in connect");
            ((Throwable)serializable).initCause(securityException);
            throw serializable;
        }
        catch (AssertionError assertionError) {
            if (Util.isAndroidGetsocknameError(assertionError)) {
                throw new IOException((Throwable)((Object)assertionError));
            }
            throw assertionError;
        }
    }

    @Override
    public String getSelectedProtocol(SSLSocket object) {
        Object object2 = this.getAlpnSelectedProtocol;
        Object var2_3 = null;
        if (object2 == null) {
            return null;
        }
        if (!((OptionalMethod)object2).isSupported((Socket)object)) {
            return null;
        }
        object2 = (byte[])this.getAlpnSelectedProtocol.invokeWithoutCheckedException((Socket)object, new Object[0]);
        object = var2_3;
        if (object2 != null) {
            object = new String((byte[])object2, Util.UTF_8);
        }
        return object;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isCleartextTrafficPermitted() {
        try {
            Class<?> clazz = Class.forName("android.security.NetworkSecurityPolicy");
            Object object = clazz.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
            return (Boolean)clazz.getMethod("isCleartextTrafficPermitted", new Class[0]).invoke(object, new Object[0]);
        }
        catch (InvocationTargetException invocationTargetException) {
            throw new AssertionError();
        }
        catch (IllegalArgumentException illegalArgumentException) {
            throw new AssertionError();
        }
        catch (IllegalAccessException illegalAccessException) {
            throw new AssertionError();
        }
        catch (NoSuchMethodException noSuchMethodException) {
            // empty catch block
            throw new AssertionError();
        }
        catch (ClassNotFoundException classNotFoundException) {
            return super.isCleartextTrafficPermitted();
        }
    }

    @Override
    public void log(int n, String string2, Throwable throwable) {
        int n2 = 5;
        if (n != 5) {
            n2 = 3;
        }
        CharSequence charSequence = string2;
        if (throwable != null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string2);
            ((StringBuilder)charSequence).append('\n');
            ((StringBuilder)charSequence).append(Log.getStackTraceString((Throwable)throwable));
            charSequence = ((StringBuilder)charSequence).toString();
        }
        n = 0;
        int n3 = ((String)charSequence).length();
        while (n < n3) {
            int n4;
            int n5 = ((String)charSequence).indexOf(10, n);
            if (n5 == -1) {
                n5 = n3;
            }
            do {
                n4 = Math.min(n5, n + 4000);
                Log.println((int)n2, (String)"OkHttp", (String)((String)charSequence).substring(n, n4));
                n = n4;
            } while (n4 < n5);
            n = n4 + 1;
        }
    }

    @Override
    public X509TrustManager trustManager(SSLSocketFactory object) {
        Object obj;
        Object obj2 = obj = AndroidPlatform.readFieldOrNull(object, this.sslParametersClass, "sslParameters");
        if (obj == null) {
            try {
                obj2 = AndroidPlatform.readFieldOrNull(object, Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, object.getClass().getClassLoader()), "sslParameters");
            }
            catch (ClassNotFoundException classNotFoundException) {
                return super.trustManager((SSLSocketFactory)object);
            }
        }
        if ((object = AndroidPlatform.readFieldOrNull(obj2, X509TrustManager.class, "x509TrustManager")) != null) {
            return object;
        }
        return AndroidPlatform.readFieldOrNull(obj2, X509TrustManager.class, "trustManager");
    }
}

