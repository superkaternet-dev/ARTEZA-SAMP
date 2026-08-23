/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.tls;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PublicKey;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.x500.X500Principal;

public abstract class TrustRootIndex {
    public static TrustRootIndex get(X509TrustManager x509TrustManager) {
        try {
            Object object = x509TrustManager.getClass().getDeclaredMethod("findTrustAnchorByIssuerAndSignature", X509Certificate.class);
            ((Method)object).setAccessible(true);
            object = new AndroidTrustRootIndex(x509TrustManager, (Method)object);
            return object;
        }
        catch (NoSuchMethodException noSuchMethodException) {
            return TrustRootIndex.get(x509TrustManager.getAcceptedIssuers());
        }
    }

    public static TrustRootIndex get(X509Certificate ... x509CertificateArray) {
        return new BasicTrustRootIndex(x509CertificateArray);
    }

    abstract X509Certificate findByIssuerAndSignature(X509Certificate var1);

    static final class AndroidTrustRootIndex
    extends TrustRootIndex {
        private final Method findByIssuerAndSignatureMethod;
        private final X509TrustManager trustManager;

        AndroidTrustRootIndex(X509TrustManager x509TrustManager, Method method) {
            this.findByIssuerAndSignatureMethod = method;
            this.trustManager = x509TrustManager;
        }

        @Override
        public X509Certificate findByIssuerAndSignature(X509Certificate x509Certificate) {
            block4: {
                Object var2_4 = null;
                TrustAnchor trustAnchor = (TrustAnchor)this.findByIssuerAndSignatureMethod.invoke((Object)this.trustManager, x509Certificate);
                x509Certificate = var2_4;
                if (trustAnchor == null) break block4;
                try {
                    x509Certificate = trustAnchor.getTrustedCert();
                }
                catch (InvocationTargetException invocationTargetException) {
                    return null;
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw new AssertionError();
                }
            }
            return x509Certificate;
        }
    }

    static final class BasicTrustRootIndex
    extends TrustRootIndex {
        private final Map<X500Principal, List<X509Certificate>> subjectToCaCerts = new LinkedHashMap<X500Principal, List<X509Certificate>>();

        public BasicTrustRootIndex(X509Certificate ... x509CertificateArray) {
            for (X509Certificate x509Certificate : x509CertificateArray) {
                List<X509Certificate> list;
                X500Principal x500Principal = x509Certificate.getSubjectX500Principal();
                List<X509Certificate> list2 = list = this.subjectToCaCerts.get(x500Principal);
                if (list == null) {
                    list2 = new ArrayList<X509Certificate>(1);
                    this.subjectToCaCerts.put(x500Principal, list2);
                }
                list2.add(x509Certificate);
            }
        }

        @Override
        public X509Certificate findByIssuerAndSignature(X509Certificate x509Certificate) {
            Iterator iterator2 = x509Certificate.getIssuerX500Principal();
            if ((iterator2 = this.subjectToCaCerts.get(iterator2)) == null) {
                return null;
            }
            iterator2 = iterator2.iterator();
            while (iterator2.hasNext()) {
                X509Certificate x509Certificate2 = (X509Certificate)iterator2.next();
                PublicKey publicKey = x509Certificate2.getPublicKey();
                try {
                    x509Certificate.verify(publicKey);
                    return x509Certificate2;
                }
                catch (Exception exception) {
                }
            }
            return null;
        }
    }
}

