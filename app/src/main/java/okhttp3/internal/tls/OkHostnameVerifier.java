/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.tls;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import okhttp3.internal.Util;
import okhttp3.internal.tls.DistinguishedNameParser;

public final class OkHostnameVerifier
implements HostnameVerifier {
    private static final int ALT_DNS_NAME = 2;
    private static final int ALT_IPA_NAME = 7;
    public static final OkHostnameVerifier INSTANCE = new OkHostnameVerifier();

    private OkHostnameVerifier() {
    }

    public static List<String> allSubjectAltNames(X509Certificate object) {
        List<String> list = OkHostnameVerifier.getSubjectAltNames((X509Certificate)object, 7);
        object = OkHostnameVerifier.getSubjectAltNames((X509Certificate)object, 2);
        ArrayList<String> arrayList = new ArrayList<String>(list.size() + object.size());
        arrayList.addAll(list);
        arrayList.addAll((Collection<String>)object);
        return arrayList;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static List<String> getSubjectAltNames(X509Certificate iterator2, int n) {
        ArrayList<String> arrayList = new ArrayList<String>();
        try {
            iterator2 = ((X509Certificate)((Object)iterator2)).getSubjectAlternativeNames();
            if (iterator2 == null) {
                return Collections.emptyList();
            }
            iterator2 = iterator2.iterator();
            while (true) {
                Object object;
                if (!iterator2.hasNext()) {
                    return arrayList;
                }
                List list = (List)iterator2.next();
                if (list == null || list.size() < 2 || (object = (Integer)list.get(0)) == null || (Integer)object != n || (object = (String)list.get(1)) == null) continue;
                arrayList.add((String)object);
            }
        }
        catch (CertificateParsingException certificateParsingException) {
            return Collections.emptyList();
        }
    }

    private boolean verifyHostname(String charSequence, String string2) {
        if (charSequence != null && ((String)charSequence).length() != 0 && !((String)charSequence).startsWith(".") && !((String)charSequence).endsWith("..")) {
            if (string2 != null && string2.length() != 0 && !string2.startsWith(".") && !string2.endsWith("..")) {
                CharSequence charSequence2 = charSequence;
                if (!((String)charSequence).endsWith(".")) {
                    charSequence2 = new StringBuilder();
                    ((StringBuilder)charSequence2).append((String)charSequence);
                    ((StringBuilder)charSequence2).append('.');
                    charSequence2 = ((StringBuilder)charSequence2).toString();
                }
                charSequence = string2;
                if (!string2.endsWith(".")) {
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append(string2);
                    ((StringBuilder)charSequence).append('.');
                    charSequence = ((StringBuilder)charSequence).toString();
                }
                if (!((String)(charSequence = ((String)charSequence).toLowerCase(Locale.US))).contains("*")) {
                    return ((String)charSequence2).equals(charSequence);
                }
                if (((String)charSequence).startsWith("*.") && ((String)charSequence).indexOf(42, 1) == -1) {
                    if (((String)charSequence2).length() < ((String)charSequence).length()) {
                        return false;
                    }
                    if ("*.".equals(charSequence)) {
                        return false;
                    }
                    if (!((String)charSequence2).endsWith((String)(charSequence = ((String)charSequence).substring(1)))) {
                        return false;
                    }
                    int n = ((String)charSequence2).length() - ((String)charSequence).length();
                    return n <= 0 || ((String)charSequence2).lastIndexOf(46, n - 1) == -1;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    private boolean verifyHostname(String string2, X509Certificate object) {
        string2 = string2.toLowerCase(Locale.US);
        boolean bl = false;
        List<String> list = OkHostnameVerifier.getSubjectAltNames((X509Certificate)object, 2);
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            bl = true;
            if (!this.verifyHostname(string2, list.get(i))) continue;
            return true;
        }
        if (!bl && (object = new DistinguishedNameParser(((X509Certificate)object).getSubjectX500Principal()).findMostSpecific("cn")) != null) {
            return this.verifyHostname(string2, (String)object);
        }
        return false;
    }

    private boolean verifyIpAddress(String string2, X509Certificate object) {
        object = OkHostnameVerifier.getSubjectAltNames((X509Certificate)object, 7);
        int n = object.size();
        for (int i = 0; i < n; ++i) {
            if (!string2.equalsIgnoreCase((String)object.get(i))) continue;
            return true;
        }
        return false;
    }

    public boolean verify(String string2, X509Certificate x509Certificate) {
        boolean bl = Util.verifyAsIpAddress(string2) ? this.verifyIpAddress(string2, x509Certificate) : this.verifyHostname(string2, x509Certificate);
        return bl;
    }

    @Override
    public boolean verify(String string2, SSLSession sSLSession) {
        try {
            boolean bl = this.verify(string2, (X509Certificate)sSLSession.getPeerCertificates()[0]);
            return bl;
        }
        catch (SSLException sSLException) {
            return false;
        }
    }
}

