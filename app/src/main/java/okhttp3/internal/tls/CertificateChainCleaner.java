/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.tls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.X509TrustManager;
import okhttp3.internal.tls.TrustRootIndex;

public abstract class CertificateChainCleaner {
    public static CertificateChainCleaner get(X509TrustManager x509TrustManager) {
        try {
            Object object = Class.forName("android.net.http.X509TrustManagerExtensions");
            object = new AndroidCertificateChainCleaner(((Class)object).getConstructor(X509TrustManager.class).newInstance(x509TrustManager), ((Class)object).getMethod("checkServerTrusted", X509Certificate[].class, String.class, String.class));
            return object;
        }
        catch (Exception exception) {
            return new BasicCertificateChainCleaner(TrustRootIndex.get(x509TrustManager));
        }
    }

    public static CertificateChainCleaner get(X509Certificate ... x509CertificateArray) {
        return new BasicCertificateChainCleaner(TrustRootIndex.get(x509CertificateArray));
    }

    public abstract List<Certificate> clean(List<Certificate> var1, String var2) throws SSLPeerUnverifiedException;

    static final class AndroidCertificateChainCleaner
    extends CertificateChainCleaner {
        private final Method checkServerTrusted;
        private final Object x509TrustManagerExtensions;

        AndroidCertificateChainCleaner(Object object, Method method) {
            this.x509TrustManagerExtensions = object;
            this.checkServerTrusted = method;
        }

        @Override
        public List<Certificate> clean(List<Certificate> object, String string2) throws SSLPeerUnverifiedException {
            try {
                object = object.toArray(new X509Certificate[object.size()]);
                object = (List)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, object, "RSA", string2);
                return object;
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError((Object)illegalAccessException);
            }
            catch (InvocationTargetException invocationTargetException) {
                object = new SSLPeerUnverifiedException(invocationTargetException.getMessage());
                ((Throwable)object).initCause(invocationTargetException);
                throw object;
            }
        }
    }

    static final class BasicCertificateChainCleaner
    extends CertificateChainCleaner {
        private static final int MAX_SIGNERS = 9;
        private final TrustRootIndex trustRootIndex;

        public BasicCertificateChainCleaner(TrustRootIndex trustRootIndex) {
            this.trustRootIndex = trustRootIndex;
        }

        private boolean verifySignature(X509Certificate x509Certificate, X509Certificate x509Certificate2) {
            if (!x509Certificate.getIssuerDN().equals(x509Certificate2.getSubjectDN())) {
                return false;
            }
            try {
                x509Certificate.verify(x509Certificate2.getPublicKey());
                return true;
            }
            catch (GeneralSecurityException generalSecurityException) {
                return false;
            }
        }

        @Override
        public List<Certificate> clean(List<Certificate> object, String object2) throws SSLPeerUnverifiedException {
            ArrayDeque<Certificate> arrayDeque = new ArrayDeque<Certificate>((Collection<Certificate>)object);
            object2 = new ArrayList();
            object2.add(arrayDeque.removeFirst());
            boolean bl = false;
            block0: for (int i = 0; i < 9; ++i) {
                object = (X509Certificate)object2.get(object2.size() - 1);
                X509Certificate x509Certificate = this.trustRootIndex.findByIssuerAndSignature((X509Certificate)object);
                if (x509Certificate != null) {
                    if (object2.size() > 1 || !((Certificate)object).equals(x509Certificate)) {
                        object2.add(x509Certificate);
                    }
                    if (this.verifySignature(x509Certificate, x509Certificate)) {
                        return object2;
                    }
                    bl = true;
                    continue;
                }
                Iterator iterator2 = arrayDeque.iterator();
                while (iterator2.hasNext()) {
                    x509Certificate = (X509Certificate)iterator2.next();
                    if (!this.verifySignature((X509Certificate)object, x509Certificate)) continue;
                    iterator2.remove();
                    object2.add(x509Certificate);
                    continue block0;
                }
                if (bl) {
                    return object2;
                }
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Failed to find a trusted cert that signed ");
                ((StringBuilder)object2).append(object);
                throw new SSLPeerUnverifiedException(((StringBuilder)object2).toString());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Certificate chain too long: ");
            ((StringBuilder)object).append(object2);
            object = new SSLPeerUnverifiedException(((StringBuilder)object).toString());
            throw object;
        }
    }
}

