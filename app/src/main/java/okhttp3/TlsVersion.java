/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

public final class TlsVersion
extends Enum<TlsVersion> {
    private static final TlsVersion[] $VALUES;
    public static final /* enum */ TlsVersion SSL_3_0;
    public static final /* enum */ TlsVersion TLS_1_0;
    public static final /* enum */ TlsVersion TLS_1_1;
    public static final /* enum */ TlsVersion TLS_1_2;
    final String javaName;

    static {
        TlsVersion tlsVersion;
        TlsVersion tlsVersion2;
        TlsVersion tlsVersion3;
        TlsVersion tlsVersion4;
        TLS_1_2 = tlsVersion4 = new TlsVersion("TLSv1.2");
        TLS_1_1 = tlsVersion3 = new TlsVersion("TLSv1.1");
        TLS_1_0 = tlsVersion2 = new TlsVersion("TLSv1");
        SSL_3_0 = tlsVersion = new TlsVersion("SSLv3");
        $VALUES = new TlsVersion[]{tlsVersion4, tlsVersion3, tlsVersion2, tlsVersion};
    }

    private TlsVersion(String string3) {
        this.javaName = string3;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static TlsVersion forJavaName(String string2) {
        int n;
        block12: {
            switch (string2.hashCode()) {
                case 79923350: {
                    if (!string2.equals("TLSv1")) break;
                    n = 2;
                    break block12;
                }
                case 79201641: {
                    if (!string2.equals("SSLv3")) break;
                    n = 3;
                    break block12;
                }
                case -503070502: {
                    if (!string2.equals("TLSv1.2")) break;
                    n = 0;
                    break block12;
                }
                case -503070503: {
                    if (!string2.equals("TLSv1.1")) break;
                    n = 1;
                    break block12;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected TLS version: ");
                stringBuilder.append(string2);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            case 3: {
                return SSL_3_0;
            }
            case 2: {
                return TLS_1_0;
            }
            case 1: {
                return TLS_1_1;
            }
            case 0: 
        }
        return TLS_1_2;
    }

    public static TlsVersion valueOf(String string2) {
        return Enum.valueOf(TlsVersion.class, string2);
    }

    public static TlsVersion[] values() {
        return (TlsVersion[])$VALUES.clone();
    }

    public String javaName() {
        return this.javaName;
    }
}

