/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.Util;
import okhttp3.internal.tls.CertificateChainCleaner;
import okio.ByteString;

public final class CertificatePinner {
    public static final CertificatePinner DEFAULT = new Builder().build();
    private final CertificateChainCleaner certificateChainCleaner;
    private final List<Pin> pins;

    private CertificatePinner(List<Pin> list, CertificateChainCleaner certificateChainCleaner) {
        this.pins = list;
        this.certificateChainCleaner = certificateChainCleaner;
    }

    public static String pin(Certificate certificate) {
        if (certificate instanceof X509Certificate) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("sha256/");
            stringBuilder.append(CertificatePinner.sha256((X509Certificate)certificate).base64());
            return stringBuilder.toString();
        }
        throw new IllegalArgumentException("Certificate pinning requires X509 certificates");
    }

    static ByteString sha1(X509Certificate x509Certificate) {
        return Util.sha1(ByteString.of(x509Certificate.getPublicKey().getEncoded()));
    }

    static ByteString sha256(X509Certificate x509Certificate) {
        return Util.sha256(ByteString.of(x509Certificate.getPublicKey().getEncoded()));
    }

    public void check(String object, List<Certificate> object2) throws SSLPeerUnverifiedException {
        int n;
        int n2;
        List<Pin> list = this.findMatchingPins((String)object);
        if (list.isEmpty()) {
            return;
        }
        Object object3 = this.certificateChainCleaner;
        List<Certificate> list2 = object2;
        if (object3 != null) {
            list2 = ((CertificateChainCleaner)object3).clean((List<Certificate>)object2, (String)object);
        }
        int n3 = list2.size();
        for (n2 = 0; n2 < n3; ++n2) {
            X509Certificate x509Certificate = (X509Certificate)list2.get(n2);
            object3 = null;
            object2 = null;
            int n4 = list.size();
            for (n = 0; n < n4; ++n) {
                Object object4;
                Pin pin = list.get(n);
                if (pin.hashAlgorithm.equals("sha256/")) {
                    object4 = object2;
                    if (object2 == null) {
                        object4 = CertificatePinner.sha256(x509Certificate);
                    }
                    object2 = object4;
                    if (!pin.hash.equals(object4)) continue;
                    return;
                }
                if (pin.hashAlgorithm.equals("sha1/")) {
                    object4 = object3;
                    if (object3 == null) {
                        object4 = CertificatePinner.sha1(x509Certificate);
                    }
                    object3 = object4;
                    if (!pin.hash.equals(object4)) continue;
                    return;
                }
                throw new AssertionError();
            }
        }
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("Certificate pinning failure!");
        object2 = ((StringBuilder)object2).append("\n  Peer certificate chain:");
        n = list2.size();
        for (n2 = 0; n2 < n; ++n2) {
            object3 = (X509Certificate)list2.get(n2);
            ((StringBuilder)object2).append("\n    ");
            ((StringBuilder)object2).append(CertificatePinner.pin((Certificate)object3));
            ((StringBuilder)object2).append(": ");
            ((StringBuilder)object2).append(((X509Certificate)object3).getSubjectDN().getName());
        }
        ((StringBuilder)object2).append("\n  Pinned certificates for ");
        ((StringBuilder)object2).append((String)object);
        ((StringBuilder)object2).append(":");
        n = list.size();
        for (n2 = 0; n2 < n; ++n2) {
            object = list.get(n2);
            ((StringBuilder)object2).append("\n    ");
            ((StringBuilder)object2).append(object);
        }
        object = new SSLPeerUnverifiedException(((StringBuilder)object2).toString());
        throw object;
    }

    public void check(String string2, Certificate ... certificateArray) throws SSLPeerUnverifiedException {
        this.check(string2, Arrays.asList(certificateArray));
    }

    List<Pin> findMatchingPins(String string2) {
        List<Pin> list = Collections.emptyList();
        for (Pin pin : this.pins) {
            List<Pin> list2 = list;
            if (pin.matches(string2)) {
                list2 = list;
                if (list.isEmpty()) {
                    list2 = new ArrayList<Pin>();
                }
                list2.add(pin);
            }
            list = list2;
        }
        return list;
    }

    CertificatePinner withCertificateChainCleaner(CertificateChainCleaner object) {
        object = this.certificateChainCleaner != object ? new CertificatePinner(this.pins, (CertificateChainCleaner)object) : this;
        return object;
    }

    public static final class Builder {
        private final List<Pin> pins = new ArrayList<Pin>();

        public Builder add(String object, String ... stringArray) {
            if (object != null) {
                for (String string2 : stringArray) {
                    this.pins.add(new Pin((String)object, string2));
                }
                return this;
            }
            object = new NullPointerException("pattern == null");
            throw object;
        }

        public CertificatePinner build() {
            return new CertificatePinner(Util.immutableList(this.pins), null);
        }
    }

    static final class Pin {
        final ByteString hash;
        final String hashAlgorithm;
        final String pattern;

        Pin(String charSequence, String string2) {
            block6: {
                block5: {
                    block4: {
                        this.pattern = charSequence;
                        if (!string2.startsWith("sha1/")) break block4;
                        this.hashAlgorithm = "sha1/";
                        this.hash = ByteString.decodeBase64(string2.substring("sha1/".length()));
                        break block5;
                    }
                    if (!string2.startsWith("sha256/")) break block6;
                    this.hashAlgorithm = "sha256/";
                    this.hash = ByteString.decodeBase64(string2.substring("sha256/".length()));
                }
                if (this.hash != null) {
                    return;
                }
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("pins must be base64: ");
                ((StringBuilder)charSequence).append(string2);
                throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
            }
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("pins must start with 'sha256/' or 'sha1/': ");
            ((StringBuilder)charSequence).append(string2);
            throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof Pin && this.pattern.equals(((Pin)object).pattern) && this.hashAlgorithm.equals(((Pin)object).hashAlgorithm) && this.hash.equals(((Pin)object).hash);
            return bl;
        }

        public int hashCode() {
            return ((17 * 31 + this.pattern.hashCode()) * 31 + this.hashAlgorithm.hashCode()) * 31 + this.hash.hashCode();
        }

        boolean matches(String string2) {
            String string3;
            boolean bl = this.pattern.equals(string2);
            boolean bl2 = true;
            if (bl) {
                return true;
            }
            int n = string2.indexOf(46);
            if (!this.pattern.startsWith("*.") || !string2.regionMatches(false, n + 1, string3 = this.pattern, 2, string3.length() - 2)) {
                bl2 = false;
            }
            return bl2;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.hashAlgorithm);
            stringBuilder.append(this.hash.base64());
            return stringBuilder.toString();
        }
    }
}

