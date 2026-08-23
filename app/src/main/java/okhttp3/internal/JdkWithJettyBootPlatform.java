/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import javax.net.ssl.SSLSocket;
import okhttp3.Protocol;
import okhttp3.internal.Platform;
import okhttp3.internal.Util;

class JdkWithJettyBootPlatform
extends Platform {
    private final Class<?> clientProviderClass;
    private final Method getMethod;
    private final Method putMethod;
    private final Method removeMethod;
    private final Class<?> serverProviderClass;

    public JdkWithJettyBootPlatform(Method method, Method method2, Method method3, Class<?> clazz, Class<?> clazz2) {
        this.putMethod = method;
        this.getMethod = method2;
        this.removeMethod = method3;
        this.clientProviderClass = clazz;
        this.serverProviderClass = clazz2;
    }

    public static Platform buildIfSupported() {
        try {
            Object object = Class.forName("org.eclipse.jetty.alpn.ALPN");
            Serializable serializable = new StringBuilder();
            serializable.append("org.eclipse.jetty.alpn.ALPN");
            serializable.append("$Provider");
            serializable = Class.forName(serializable.toString());
            Serializable serializable2 = new StringBuilder();
            serializable2.append("org.eclipse.jetty.alpn.ALPN");
            serializable2.append("$ClientProvider");
            serializable2 = Class.forName(serializable2.toString());
            Serializable serializable3 = new StringBuilder();
            serializable3.append("org.eclipse.jetty.alpn.ALPN");
            serializable3.append("$ServerProvider");
            serializable3 = Class.forName(serializable3.toString());
            object = new JdkWithJettyBootPlatform(((Class)object).getMethod("put", new Class[]{SSLSocket.class, serializable}), ((Class)object).getMethod("get", SSLSocket.class), ((Class)object).getMethod("remove", SSLSocket.class), (Class<?>)serializable2, (Class<?>)serializable3);
            return object;
        }
        catch (NoSuchMethodException noSuchMethodException) {
        }
        catch (ClassNotFoundException classNotFoundException) {
            // empty catch block
        }
        return null;
    }

    @Override
    public void afterHandshake(SSLSocket sSLSocket) {
        try {
            this.removeMethod.invoke(null, sSLSocket);
            return;
        }
        catch (InvocationTargetException invocationTargetException) {
        }
        catch (IllegalAccessException illegalAccessException) {
            // empty catch block
        }
        throw new AssertionError();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public void configureTlsExtensions(SSLSocket sSLSocket, String object, List<Protocol> object2) {
        void var1_4;
        List<String> list = JdkWithJettyBootPlatform.alpnProtocolNames(object2);
        try {
            ClassLoader classLoader = Platform.class.getClassLoader();
            object2 = this.clientProviderClass;
            object = this.serverProviderClass;
            JettyNegoProvider jettyNegoProvider = new JettyNegoProvider(list);
            object = Proxy.newProxyInstance(classLoader, new Class[]{object2, object}, (InvocationHandler)jettyNegoProvider);
            this.putMethod.invoke(null, sSLSocket, object);
            return;
        }
        catch (IllegalAccessException illegalAccessException) {
        }
        catch (InvocationTargetException invocationTargetException) {
            // empty catch block
        }
        throw new AssertionError(var1_4);
    }

    @Override
    public String getSelectedProtocol(SSLSocket object) {
        block9: {
            block8: {
                block7: {
                    Object var2_5;
                    block6: {
                        Method method = this.getMethod;
                        var2_5 = null;
                        object = (JettyNegoProvider)Proxy.getInvocationHandler(method.invoke(null, object));
                        if (((JettyNegoProvider)object).unsupported || ((JettyNegoProvider)object).selected != null) break block6;
                        Platform.get().log(4, "ALPN callback dropped: SPDY and HTTP/2 are disabled. Is alpn-boot on the boot class path?", null);
                        return null;
                    }
                    if (!((JettyNegoProvider)object).unsupported) break block7;
                    object = var2_5;
                    break block8;
                }
                try {
                    object = ((JettyNegoProvider)object).selected;
                }
                catch (IllegalAccessException illegalAccessException) {
                    break block9;
                }
                catch (InvocationTargetException invocationTargetException) {
                    // empty catch block
                }
            }
            return object;
        }
        throw new AssertionError();
    }

    private static class JettyNegoProvider
    implements InvocationHandler {
        private final List<String> protocols;
        private String selected;
        private boolean unsupported;

        public JettyNegoProvider(List<String> list) {
            this.protocols = list;
        }

        @Override
        public Object invoke(Object object, Method method, Object[] objectArray) throws Throwable {
            String string2 = method.getName();
            Class<?> clazz = method.getReturnType();
            object = objectArray;
            if (objectArray == null) {
                object = Util.EMPTY_STRING_ARRAY;
            }
            if (string2.equals("supports") && Boolean.TYPE == clazz) {
                return true;
            }
            if (string2.equals("unsupported") && Void.TYPE == clazz) {
                this.unsupported = true;
                return null;
            }
            if (string2.equals("protocols") && ((Object[])object).length == 0) {
                return this.protocols;
            }
            if ((string2.equals("selectProtocol") || string2.equals("select")) && String.class == clazz && ((Object[])object).length == 1 && object[0] instanceof List) {
                object = (List)object[0];
                int n = object.size();
                for (int i = 0; i < n; ++i) {
                    if (!this.protocols.contains(object.get(i))) continue;
                    this.selected = object = (String)object.get(i);
                    return object;
                }
                this.selected = object = this.protocols.get(0);
                return object;
            }
            if ((string2.equals("protocolSelected") || string2.equals("selected")) && ((Object[])object).length == 1) {
                this.selected = (String)object[0];
                return null;
            }
            return method.invoke((Object)this, (Object[])object);
        }
    }
}

