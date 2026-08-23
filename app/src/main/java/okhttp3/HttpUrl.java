/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import okhttp3.internal.Util;
import okio.Buffer;

public final class HttpUrl {
    static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
    static final String FRAGMENT_ENCODE_SET = "";
    static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
    static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
    static final String QUERY_COMPONENT_ENCODE_SET = " \"'<>#&=";
    static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
    static final String QUERY_ENCODE_SET = " \"'<>#";
    static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
    private final String fragment;
    private final String host;
    private final String password;
    private final List<String> pathSegments;
    private final int port;
    private final List<String> queryNamesAndValues;
    private final String scheme;
    private final String url;
    private final String username;

    private HttpUrl(Builder builder) {
        this.scheme = builder.scheme;
        this.username = HttpUrl.percentDecode(builder.encodedUsername, false);
        this.password = HttpUrl.percentDecode(builder.encodedPassword, false);
        this.host = builder.host;
        this.port = builder.effectivePort();
        this.pathSegments = this.percentDecode(builder.encodedPathSegments, false);
        Object object = builder.encodedQueryNamesAndValues;
        Object var3_3 = null;
        object = object != null ? this.percentDecode(builder.encodedQueryNamesAndValues, true) : null;
        this.queryNamesAndValues = object;
        object = builder.encodedFragment != null ? HttpUrl.percentDecode(builder.encodedFragment, false) : var3_3;
        this.fragment = object;
        this.url = builder.toString();
    }

    static String canonicalize(String string2, int n, int n2, String string3, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        int n3;
        for (int i = n; i < n2; i += Character.charCount(n3)) {
            n3 = string2.codePointAt(i);
            if (!(n3 < 32 || n3 == 127 || n3 >= 128 && bl4)) {
                if (!(string3.indexOf(n3) != -1 || n3 == 37 && (!bl || bl2 && !HttpUrl.percentEncoded(string2, i, n2)) || n3 == 43 && bl3)) {
                    continue;
                }
            }
            Buffer buffer = new Buffer();
            buffer.writeUtf8(string2, n, i);
            HttpUrl.canonicalize(buffer, string2, i, n2, string3, bl, bl2, bl3, bl4);
            return buffer.readUtf8();
        }
        return string2.substring(n, n2);
    }

    static String canonicalize(String string2, String string3, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        return HttpUrl.canonicalize(string2, 0, string2.length(), string3, bl, bl2, bl3, bl4);
    }

    static void canonicalize(Buffer buffer, String string2, int n, int n2, String string3, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        Object object = null;
        while (n < n2) {
            int n3 = string2.codePointAt(n);
            if (!bl || n3 != 9 && n3 != 10 && n3 != 12 && n3 != 13) {
                Object object2;
                if (n3 == 43 && bl3) {
                    object2 = bl ? "+" : "%2B";
                    buffer.writeUtf8((String)object2);
                } else if (!(n3 < 32 || n3 == 127 || n3 >= 128 && bl4 || string3.indexOf(n3) != -1 || n3 == 37 && (!bl || bl2 && !HttpUrl.percentEncoded(string2, n, n2)))) {
                    buffer.writeUtf8CodePoint(n3);
                } else {
                    object2 = object;
                    if (object == null) {
                        object2 = new Buffer();
                    }
                    ((Buffer)object2).writeUtf8CodePoint(n3);
                    while (true) {
                        object = object2;
                        if (((Buffer)object2).exhausted()) break;
                        int n4 = ((Buffer)object2).readByte() & 0xFF;
                        buffer.writeByte(37);
                        object = HEX_DIGITS;
                        buffer.writeByte((int)object[n4 >> 4 & 0xF]);
                        buffer.writeByte((int)object[n4 & 0xF]);
                    }
                }
            }
            n += Character.charCount(n3);
        }
    }

    static int decodeHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 97 + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 65 + 10;
        }
        return -1;
    }

    public static int defaultPort(String string2) {
        if (string2.equals("http")) {
            return 80;
        }
        if (string2.equals("https")) {
            return 443;
        }
        return -1;
    }

    public static HttpUrl get(URI uRI) {
        return HttpUrl.parse(uRI.toString());
    }

    public static HttpUrl get(URL uRL) {
        return HttpUrl.parse(uRL.toString());
    }

    static HttpUrl getChecked(String string2) throws MalformedURLException, UnknownHostException {
        Object object = new Builder();
        Object object2 = ((Builder)object).parse(null, string2);
        switch (1.$SwitchMap$okhttp3$HttpUrl$Builder$ParseResult[((Enum)object2).ordinal()]) {
            default: {
                object = new StringBuilder();
                ((StringBuilder)object).append("Invalid URL: ");
                ((StringBuilder)object).append(object2);
                ((StringBuilder)object).append(" for ");
                ((StringBuilder)object).append(string2);
                throw new MalformedURLException(((StringBuilder)object).toString());
            }
            case 2: {
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("Invalid host: ");
                ((StringBuilder)object2).append(string2);
                throw new UnknownHostException(((StringBuilder)object2).toString());
            }
            case 1: 
        }
        return ((Builder)object).build();
    }

    static void namesAndValuesToQueryString(StringBuilder stringBuilder, List<String> list) {
        int n = list.size();
        for (int i = 0; i < n; i += 2) {
            String string2 = list.get(i);
            String string3 = list.get(i + 1);
            if (i > 0) {
                stringBuilder.append('&');
            }
            stringBuilder.append(string2);
            if (string3 == null) continue;
            stringBuilder.append('=');
            stringBuilder.append(string3);
        }
    }

    public static HttpUrl parse(String string2) {
        Builder builder = new Builder();
        HttpUrl httpUrl = null;
        if (builder.parse(null, string2) == Builder.ParseResult.SUCCESS) {
            httpUrl = builder.build();
        }
        return httpUrl;
    }

    static void pathSegmentsToString(StringBuilder stringBuilder, List<String> list) {
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            stringBuilder.append('/');
            stringBuilder.append(list.get(i));
        }
    }

    static String percentDecode(String string2, int n, int n2, boolean bl) {
        for (int i = n; i < n2; ++i) {
            char c = string2.charAt(i);
            if (!(c == '%' || c == '+' && bl)) {
                continue;
            }
            Buffer buffer = new Buffer();
            buffer.writeUtf8(string2, n, i);
            HttpUrl.percentDecode(buffer, string2, i, n2, bl);
            return buffer.readUtf8();
        }
        return string2.substring(n, n2);
    }

    static String percentDecode(String string2, boolean bl) {
        return HttpUrl.percentDecode(string2, 0, string2.length(), bl);
    }

    private List<String> percentDecode(List<String> object, boolean bl) {
        ArrayList<Object> arrayList = new ArrayList<Object>(object.size());
        Iterator<String> iterator2 = object.iterator();
        while (iterator2.hasNext()) {
            object = iterator2.next();
            object = object != null ? HttpUrl.percentDecode((String)object, bl) : null;
            arrayList.add(object);
        }
        return Collections.unmodifiableList(arrayList);
    }

    /*
     * Unable to fully structure code
     */
    static void percentDecode(Buffer var0, String var1_1, int var2_2, int var3_3, boolean var4_4) {
        while (var2_2 < var3_3) {
            block4: {
                block3: {
                    var5_5 = var1_1.codePointAt(var2_2);
                    if (var5_5 != 37 || var2_2 + 2 >= var3_3) break block3;
                    var6_6 = HttpUrl.decodeHexDigit(var1_1.charAt(var2_2 + 1));
                    var7_7 = HttpUrl.decodeHexDigit(var1_1.charAt(var2_2 + 2));
                    if (var6_6 == -1 || var7_7 == -1) ** GOTO lbl-1000
                    var0.writeByte((var6_6 << 4) + var7_7);
                    var2_2 += 2;
                    break block4;
                }
                if (var5_5 == 43 && var4_4) {
                    var0.writeByte(32);
                } else lbl-1000:
                // 2 sources

                {
                    var0.writeUtf8CodePoint(var5_5);
                }
            }
            var2_2 += Character.charCount(var5_5);
        }
    }

    static boolean percentEncoded(String string2, int n, int n2) {
        boolean bl = n + 2 < n2 && string2.charAt(n) == '%' && HttpUrl.decodeHexDigit(string2.charAt(n + 1)) != -1 && HttpUrl.decodeHexDigit(string2.charAt(n + 2)) != -1;
        return bl;
    }

    static List<String> queryStringToNamesAndValues(String string2) {
        ArrayList<String> arrayList = new ArrayList<String>();
        int n = 0;
        while (n <= string2.length()) {
            int n2;
            int n3 = n2 = string2.indexOf(38, n);
            if (n2 == -1) {
                n3 = string2.length();
            }
            if ((n2 = string2.indexOf(61, n)) != -1 && n2 <= n3) {
                arrayList.add(string2.substring(n, n2));
                arrayList.add(string2.substring(n2 + 1, n3));
            } else {
                arrayList.add(string2.substring(n, n3));
                arrayList.add(null);
            }
            n = n3 + 1;
        }
        return arrayList;
    }

    public String encodedFragment() {
        if (this.fragment == null) {
            return null;
        }
        int n = this.url.indexOf(35);
        return this.url.substring(n + 1);
    }

    public String encodedPassword() {
        if (this.password.isEmpty()) {
            return FRAGMENT_ENCODE_SET;
        }
        int n = this.url.indexOf(58, this.scheme.length() + 3);
        int n2 = this.url.indexOf(64);
        return this.url.substring(n + 1, n2);
    }

    public String encodedPath() {
        int n = this.url.indexOf(47, this.scheme.length() + 3);
        String string2 = this.url;
        int n2 = Util.delimiterOffset(string2, n, string2.length(), "?#");
        return this.url.substring(n, n2);
    }

    public List<String> encodedPathSegments() {
        int n = this.url.indexOf(47, this.scheme.length() + 3);
        Object object = this.url;
        int n2 = Util.delimiterOffset((String)object, n, ((String)object).length(), "?#");
        object = new ArrayList();
        while (n < n2) {
            int n3 = n + 1;
            n = Util.delimiterOffset(this.url, n3, n2, '/');
            object.add(this.url.substring(n3, n));
        }
        return object;
    }

    public String encodedQuery() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        int n = this.url.indexOf(63) + 1;
        String string2 = this.url;
        int n2 = Util.delimiterOffset(string2, n + 1, string2.length(), '#');
        return this.url.substring(n, n2);
    }

    public String encodedUsername() {
        if (this.username.isEmpty()) {
            return FRAGMENT_ENCODE_SET;
        }
        int n = this.scheme.length() + 3;
        String string2 = this.url;
        int n2 = Util.delimiterOffset(string2, n, string2.length(), ":@");
        return this.url.substring(n, n2);
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof HttpUrl && ((HttpUrl)object).url.equals(this.url);
        return bl;
    }

    public String fragment() {
        return this.fragment;
    }

    public int hashCode() {
        return this.url.hashCode();
    }

    public String host() {
        return this.host;
    }

    public boolean isHttps() {
        return this.scheme.equals("https");
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.scheme = this.scheme;
        builder.encodedUsername = this.encodedUsername();
        builder.encodedPassword = this.encodedPassword();
        builder.host = this.host;
        int n = this.port != HttpUrl.defaultPort(this.scheme) ? this.port : -1;
        builder.port = n;
        builder.encodedPathSegments.clear();
        builder.encodedPathSegments.addAll(this.encodedPathSegments());
        builder.encodedQuery(this.encodedQuery());
        builder.encodedFragment = this.encodedFragment();
        return builder;
    }

    public Builder newBuilder(String object) {
        Builder builder = new Builder();
        object = builder.parse(this, (String)object) == Builder.ParseResult.SUCCESS ? builder : null;
        return object;
    }

    public String password() {
        return this.password;
    }

    public List<String> pathSegments() {
        return this.pathSegments;
    }

    public int pathSize() {
        return this.pathSegments.size();
    }

    public int port() {
        return this.port;
    }

    public String query() {
        if (this.queryNamesAndValues == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        HttpUrl.namesAndValuesToQueryString(stringBuilder, this.queryNamesAndValues);
        return stringBuilder.toString();
    }

    public String queryParameter(String string2) {
        List<String> list = this.queryNamesAndValues;
        if (list == null) {
            return null;
        }
        int n = list.size();
        for (int i = 0; i < n; i += 2) {
            if (!string2.equals(this.queryNamesAndValues.get(i))) continue;
            return this.queryNamesAndValues.get(i + 1);
        }
        return null;
    }

    public String queryParameterName(int n) {
        return this.queryNamesAndValues.get(n * 2);
    }

    public Set<String> queryParameterNames() {
        if (this.queryNamesAndValues == null) {
            return Collections.emptySet();
        }
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        int n = this.queryNamesAndValues.size();
        for (int i = 0; i < n; i += 2) {
            linkedHashSet.add(this.queryNamesAndValues.get(i));
        }
        return Collections.unmodifiableSet(linkedHashSet);
    }

    public String queryParameterValue(int n) {
        return this.queryNamesAndValues.get(n * 2 + 1);
    }

    public List<String> queryParameterValues(String string2) {
        if (this.queryNamesAndValues == null) {
            return Collections.emptyList();
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        int n = this.queryNamesAndValues.size();
        for (int i = 0; i < n; i += 2) {
            if (!string2.equals(this.queryNamesAndValues.get(i))) continue;
            arrayList.add(this.queryNamesAndValues.get(i + 1));
        }
        return Collections.unmodifiableList(arrayList);
    }

    public int querySize() {
        List<String> list = this.queryNamesAndValues;
        int n = list != null ? list.size() / 2 : 0;
        return n;
    }

    public HttpUrl resolve(String object) {
        object = (object = this.newBuilder((String)object)) != null ? ((Builder)object).build() : null;
        return object;
    }

    public String scheme() {
        return this.scheme;
    }

    public String toString() {
        return this.url;
    }

    public URI uri() {
        Object object = this.newBuilder().reencodeForUri().toString();
        try {
            URI uRI = new URI((String)object);
            return uRI;
        }
        catch (URISyntaxException uRISyntaxException) {
            try {
                object = URI.create(((String)object).replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", FRAGMENT_ENCODE_SET));
                return object;
            }
            catch (Exception exception) {
                throw new RuntimeException(uRISyntaxException);
            }
        }
    }

    public URL url() {
        try {
            URL uRL = new URL(this.url);
            return uRL;
        }
        catch (MalformedURLException malformedURLException) {
            throw new RuntimeException(malformedURLException);
        }
    }

    public String username() {
        return this.username;
    }

    public static final class Builder {
        String encodedFragment;
        String encodedPassword = "";
        final List<String> encodedPathSegments;
        List<String> encodedQueryNamesAndValues;
        String encodedUsername = "";
        String host;
        int port = -1;
        String scheme;

        public Builder() {
            ArrayList<String> arrayList = new ArrayList<String>();
            this.encodedPathSegments = arrayList;
            arrayList.add(HttpUrl.FRAGMENT_ENCODE_SET);
        }

        private Builder addPathSegments(String string2, boolean bl) {
            int n;
            int n2 = 0;
            do {
                boolean bl2 = (n = Util.delimiterOffset(string2, n2, string2.length(), "/\\")) < string2.length();
                this.push(string2, n2, n, bl2, bl);
            } while ((n2 = n + 1) <= string2.length());
            return this;
        }

        private static String canonicalizeHost(String object, int n, int n2) {
            if (((String)(object = HttpUrl.percentDecode((String)object, n, n2, false))).contains(":")) {
                if ((object = ((String)object).startsWith("[") && ((String)object).endsWith("]") ? Builder.decodeIpv6((String)object, 1, ((String)object).length() - 1) : Builder.decodeIpv6((String)object, 0, ((String)object).length())) == null) {
                    return null;
                }
                if (((Object)(object = (Object)((InetAddress)object).getAddress())).length == 16) {
                    return Builder.inet6AddressToAscii((byte[])object);
                }
                throw new AssertionError();
            }
            return Util.domainToAscii((String)object);
        }

        private static boolean decodeIpv4Suffix(String string2, int n, int n2, byte[] byArray, int n3) {
            int n4 = n3;
            int n5 = n;
            while (n5 < n2) {
                char c;
                if (n4 == byArray.length) {
                    return false;
                }
                n = n5;
                if (n4 != n3) {
                    if (string2.charAt(n5) != '.') {
                        return false;
                    }
                    n = n5 + 1;
                }
                int n6 = 0;
                for (n5 = n; n5 < n2 && (c = string2.charAt(n5)) >= '0' && c <= '9'; ++n5) {
                    if (n6 == 0 && n != n5) {
                        return false;
                    }
                    if ((n6 = n6 * 10 + c - 48) <= 255) continue;
                    return false;
                }
                if (n5 - n == 0) {
                    return false;
                }
                byArray[n4] = (byte)n6;
                ++n4;
            }
            return n4 == n3 + 4;
        }

        private static InetAddress decodeIpv6(String object, int n, int n2) {
            int n3;
            byte[] byArray;
            block16: {
                byArray = new byte[16];
                int n4 = 0;
                int n5 = -1;
                int n6 = -1;
                int n7 = n;
                while (true) {
                    int n8;
                    n = n4;
                    n3 = n5;
                    if (n7 >= n2) break block16;
                    if (n4 == byArray.length) {
                        return null;
                    }
                    if (n7 + 2 <= n2 && ((String)object).regionMatches(n7, "::", 0, 2)) {
                        if (n5 != -1) {
                            return null;
                        }
                        n4 = n5 = n4 + 2;
                        n8 = n5;
                        n3 = n4;
                        n = n7 += 2;
                        if (n7 == n2) {
                            n = n5;
                            n3 = n4;
                            break block16;
                        }
                    } else {
                        n8 = n4;
                        n3 = n5;
                        n = n7;
                        if (n4 != 0) {
                            if (((String)object).regionMatches(n7, ":", 0, 1)) {
                                n = n7 + 1;
                                n8 = n4;
                                n3 = n5;
                            } else {
                                if (((String)object).regionMatches(n7, ".", 0, 1)) {
                                    if (!Builder.decodeIpv4Suffix((String)object, n6, n2, byArray, n4 - 2)) {
                                        return null;
                                    }
                                    n = n4 + 2;
                                    n3 = n5;
                                    break block16;
                                }
                                return null;
                            }
                        }
                    }
                    n4 = 0;
                    n7 = n;
                    while (n < n2 && (n5 = HttpUrl.decodeHexDigit(((String)object).charAt(n))) != -1) {
                        n4 = (n4 << 4) + n5;
                        ++n;
                    }
                    n5 = n - n7;
                    if (n5 == 0 || n5 > 4) break;
                    n6 = n8 + 1;
                    byArray[n8] = (byte)(n4 >>> 8 & 0xFF);
                    n5 = n6 + 1;
                    byArray[n6] = (byte)(n4 & 0xFF);
                    n4 = n5;
                    n5 = n3;
                    n6 = n7;
                    n7 = n;
                }
                return null;
            }
            if (n != byArray.length) {
                if (n3 == -1) {
                    return null;
                }
                System.arraycopy(byArray, n3, byArray, byArray.length - (n - n3), n - n3);
                Arrays.fill(byArray, n3, byArray.length - n + n3, (byte)0);
            }
            try {
                object = InetAddress.getByAddress(byArray);
                return object;
            }
            catch (UnknownHostException unknownHostException) {
                AssertionError assertionError = new AssertionError();
                throw assertionError;
            }
        }

        private static String inet6AddressToAscii(byte[] byArray) {
            int n;
            int n2 = -1;
            int n3 = 0;
            int n4 = 0;
            while (n4 < byArray.length) {
                int n5;
                n = n4;
                while ((n5 = n) < 16 && byArray[n5] == 0 && byArray[n5 + 1] == 0) {
                    n = n5 + 2;
                }
                int n6 = n5 - n4;
                n = n3;
                if (n6 > n3) {
                    n = n6;
                    n2 = n4;
                }
                n4 = n5 + 2;
                n3 = n;
            }
            Buffer buffer = new Buffer();
            n4 = 0;
            while (n4 < byArray.length) {
                if (n4 == n2) {
                    buffer.writeByte(58);
                    n4 = n = n4 + n3;
                    if (n != 16) continue;
                    buffer.writeByte(58);
                    n4 = n;
                    continue;
                }
                if (n4 > 0) {
                    buffer.writeByte(58);
                }
                buffer.writeHexadecimalUnsignedLong((byArray[n4] & 0xFF) << 8 | byArray[n4 + 1] & 0xFF);
                n4 += 2;
            }
            return buffer.readUtf8();
        }

        private boolean isDot(String string2) {
            boolean bl = string2.equals(".") || string2.equalsIgnoreCase("%2e");
            return bl;
        }

        private boolean isDotDot(String string2) {
            boolean bl = string2.equals("..") || string2.equalsIgnoreCase("%2e.") || string2.equalsIgnoreCase(".%2e") || string2.equalsIgnoreCase("%2e%2e");
            return bl;
        }

        private static int parsePort(String string2, int n, int n2) {
            try {
                n = Integer.parseInt(HttpUrl.canonicalize(string2, n, n2, HttpUrl.FRAGMENT_ENCODE_SET, false, false, false, true));
                if (n > 0 && n <= 65535) {
                    return n;
                }
                return -1;
            }
            catch (NumberFormatException numberFormatException) {
                return -1;
            }
        }

        private void pop() {
            List<String> list = this.encodedPathSegments;
            if (list.remove(list.size() - 1).isEmpty() && !this.encodedPathSegments.isEmpty()) {
                list = this.encodedPathSegments;
                list.set(list.size() - 1, HttpUrl.FRAGMENT_ENCODE_SET);
            } else {
                this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
            }
        }

        private static int portColonOffset(String string2, int n, int n2) {
            while (n < n2) {
                int n3 = n;
                block0 : switch (string2.charAt(n)) {
                    default: {
                        n3 = n;
                        break;
                    }
                    case '[': {
                        do {
                            n3 = n = n3 + 1;
                            if (n >= n2) break block0;
                            n3 = n;
                        } while (string2.charAt(n) != ']');
                        n3 = n;
                        break;
                    }
                    case ':': {
                        return n;
                    }
                }
                n = n3 + 1;
            }
            return n2;
        }

        private void push(String string2, int n, int n2, boolean bl, boolean bl2) {
            if (this.isDot(string2 = HttpUrl.canonicalize(string2, n, n2, HttpUrl.PATH_SEGMENT_ENCODE_SET, bl2, false, false, true))) {
                return;
            }
            if (this.isDotDot(string2)) {
                this.pop();
                return;
            }
            List<String> list = this.encodedPathSegments;
            if (list.get(list.size() - 1).isEmpty()) {
                list = this.encodedPathSegments;
                list.set(list.size() - 1, string2);
            } else {
                this.encodedPathSegments.add(string2);
            }
            if (bl) {
                this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
            }
        }

        private void removeAllCanonicalQueryParameters(String string2) {
            for (int i = this.encodedQueryNamesAndValues.size() - 2; i >= 0; i -= 2) {
                if (!string2.equals(this.encodedQueryNamesAndValues.get(i))) continue;
                this.encodedQueryNamesAndValues.remove(i + 1);
                this.encodedQueryNamesAndValues.remove(i);
                if (!this.encodedQueryNamesAndValues.isEmpty()) continue;
                this.encodedQueryNamesAndValues = null;
                return;
            }
        }

        private void resolvePath(String string2, int n, int n2) {
            if (n == n2) {
                return;
            }
            int n3 = string2.charAt(n);
            if (n3 != 47 && n3 != 92) {
                List<String> list = this.encodedPathSegments;
                list.set(list.size() - 1, HttpUrl.FRAGMENT_ENCODE_SET);
            } else {
                this.encodedPathSegments.clear();
                this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
                ++n;
            }
            while (n < n2) {
                n3 = Util.delimiterOffset(string2, n, n2, "/\\");
                boolean bl = n3 < n2;
                this.push(string2, n, n3, bl, true);
                n = n3;
                if (!bl) continue;
                n = n3 + 1;
            }
        }

        private static int schemeDelimiterOffset(String string2, int n, int n2) {
            if (n2 - n < 2) {
                return -1;
            }
            char c = string2.charAt(n);
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                ++n;
                while (n < n2) {
                    c = string2.charAt(n);
                    if (!(c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '+' || c == '-' || c == '.')) {
                        if (c == ':') {
                            return n;
                        }
                        return -1;
                    }
                    ++n;
                }
                return -1;
            }
            return -1;
        }

        private static int slashCount(String string2, int n, int n2) {
            char c;
            int n3 = 0;
            while (n < n2 && ((c = string2.charAt(n)) == '\\' || c == '/')) {
                ++n3;
                ++n;
            }
            return n3;
        }

        public Builder addEncodedPathSegment(String string2) {
            if (string2 != null) {
                this.push(string2, 0, string2.length(), false, true);
                return this;
            }
            throw new NullPointerException("encodedPathSegment == null");
        }

        public Builder addEncodedPathSegments(String string2) {
            if (string2 != null) {
                return this.addPathSegments(string2, true);
            }
            throw new NullPointerException("encodedPathSegments == null");
        }

        public Builder addEncodedQueryParameter(String string2, String string3) {
            if (string2 != null) {
                if (this.encodedQueryNamesAndValues == null) {
                    this.encodedQueryNamesAndValues = new ArrayList<String>();
                }
                this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(string2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, false, true, true));
                List<String> list = this.encodedQueryNamesAndValues;
                string2 = string3 != null ? HttpUrl.canonicalize(string3, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, false, true, true) : null;
                list.add(string2);
                return this;
            }
            throw new NullPointerException("encodedName == null");
        }

        public Builder addPathSegment(String string2) {
            if (string2 != null) {
                this.push(string2, 0, string2.length(), false, false);
                return this;
            }
            throw new NullPointerException("pathSegment == null");
        }

        public Builder addPathSegments(String string2) {
            if (string2 != null) {
                return this.addPathSegments(string2, false);
            }
            throw new NullPointerException("pathSegments == null");
        }

        public Builder addQueryParameter(String string2, String string3) {
            if (string2 != null) {
                if (this.encodedQueryNamesAndValues == null) {
                    this.encodedQueryNamesAndValues = new ArrayList<String>();
                }
                this.encodedQueryNamesAndValues.add(HttpUrl.canonicalize(string2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true));
                List<String> list = this.encodedQueryNamesAndValues;
                string2 = string3 != null ? HttpUrl.canonicalize(string3, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true) : null;
                list.add(string2);
                return this;
            }
            throw new NullPointerException("name == null");
        }

        public HttpUrl build() {
            if (this.scheme != null) {
                if (this.host != null) {
                    return new HttpUrl(this);
                }
                throw new IllegalStateException("host == null");
            }
            throw new IllegalStateException("scheme == null");
        }

        int effectivePort() {
            int n = this.port;
            if (n == -1) {
                n = HttpUrl.defaultPort(this.scheme);
            }
            return n;
        }

        public Builder encodedFragment(String string2) {
            string2 = string2 != null ? HttpUrl.canonicalize(string2, HttpUrl.FRAGMENT_ENCODE_SET, true, false, false, false) : null;
            this.encodedFragment = string2;
            return this;
        }

        public Builder encodedPassword(String string2) {
            if (string2 != null) {
                this.encodedPassword = HttpUrl.canonicalize(string2, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                return this;
            }
            throw new NullPointerException("encodedPassword == null");
        }

        public Builder encodedPath(String string2) {
            if (string2 != null) {
                if (string2.startsWith("/")) {
                    this.resolvePath(string2, 0, string2.length());
                    return this;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unexpected encodedPath: ");
                stringBuilder.append(string2);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            throw new NullPointerException("encodedPath == null");
        }

        public Builder encodedQuery(String list) {
            list = list != null ? HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize((String)((Object)list), HttpUrl.QUERY_ENCODE_SET, true, false, true, true)) : null;
            this.encodedQueryNamesAndValues = list;
            return this;
        }

        public Builder encodedUsername(String string2) {
            if (string2 != null) {
                this.encodedUsername = HttpUrl.canonicalize(string2, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                return this;
            }
            throw new NullPointerException("encodedUsername == null");
        }

        public Builder fragment(String string2) {
            string2 = string2 != null ? HttpUrl.canonicalize(string2, HttpUrl.FRAGMENT_ENCODE_SET, false, false, false, false) : null;
            this.encodedFragment = string2;
            return this;
        }

        public Builder host(String string2) {
            if (string2 != null) {
                CharSequence charSequence = Builder.canonicalizeHost(string2, 0, string2.length());
                if (charSequence != null) {
                    this.host = charSequence;
                    return this;
                }
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("unexpected host: ");
                ((StringBuilder)charSequence).append(string2);
                throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
            }
            throw new NullPointerException("host == null");
        }

        ParseResult parse(HttpUrl object, String string2) {
            block26: {
                int n;
                int n2;
                int n3;
                block24: {
                    block22: {
                        block25: {
                            block23: {
                                n3 = Util.skipLeadingAsciiWhitespace(string2, 0, string2.length());
                                int n4 = Builder.schemeDelimiterOffset(string2, n3, n2 = Util.skipTrailingAsciiWhitespace(string2, n3, string2.length()));
                                if (n4 == -1) break block22;
                                if (!string2.regionMatches(true, n3, "https:", 0, 6)) break block23;
                                this.scheme = "https";
                                n3 += "https:".length();
                                break block24;
                            }
                            if (!string2.regionMatches(true, n3, "http:", 0, 5)) break block25;
                            this.scheme = "http";
                            n3 += "http:".length();
                            break block24;
                        }
                        return ParseResult.UNSUPPORTED_SCHEME;
                    }
                    if (object == null) break block26;
                    this.scheme = ((HttpUrl)object).scheme;
                }
                int n5 = Builder.slashCount(string2, n3, n2);
                if (n5 < 2 && object != null && ((HttpUrl)object).scheme.equals(this.scheme)) {
                    this.encodedUsername = ((HttpUrl)object).encodedUsername();
                    this.encodedPassword = ((HttpUrl)object).encodedPassword();
                    this.host = ((HttpUrl)object).host;
                    this.port = ((HttpUrl)object).port;
                    this.encodedPathSegments.clear();
                    this.encodedPathSegments.addAll(((HttpUrl)object).encodedPathSegments());
                    if (n3 == n2 || string2.charAt(n3) == '#') {
                        this.encodedQuery(((HttpUrl)object).encodedQuery());
                    }
                } else {
                    int n6;
                    n = 0;
                    int n7 = 0;
                    n5 = n3 + n5;
                    n3 = n7;
                    block4: while (true) {
                        n7 = (n6 = Util.delimiterOffset(string2, n5, n2, "@/\\?#")) != n2 ? (int)string2.charAt(n6) : -1;
                        switch (n7) {
                            default: {
                                continue block4;
                            }
                            case 64: {
                                if (n3 == 0) {
                                    n7 = Util.delimiterOffset(string2, n5, n6, ':');
                                    object = HttpUrl.canonicalize(string2, n5, n7, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                                    if (n != 0) {
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append(this.encodedUsername);
                                        stringBuilder.append("%40");
                                        stringBuilder.append((String)object);
                                        object = stringBuilder.toString();
                                    }
                                    this.encodedUsername = object;
                                    if (n7 != n6) {
                                        n3 = 1;
                                        this.encodedPassword = HttpUrl.canonicalize(string2, n7 + 1, n6, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
                                    }
                                    n = 1;
                                } else {
                                    object = new StringBuilder();
                                    ((StringBuilder)object).append(this.encodedPassword);
                                    ((StringBuilder)object).append("%40");
                                    ((StringBuilder)object).append(HttpUrl.canonicalize(string2, n5, n6, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true));
                                    this.encodedPassword = ((StringBuilder)object).toString();
                                }
                                n5 = n6 + 1;
                                continue block4;
                            }
                            case -1: 
                            case 35: 
                            case 47: 
                            case 63: 
                            case 92: 
                        }
                        break;
                    }
                    n3 = Builder.portColonOffset(string2, n5, n6);
                    if (n3 + 1 < n6) {
                        this.host = Builder.canonicalizeHost(string2, n5, n3);
                        this.port = n3 = Builder.parsePort(string2, n3 + 1, n6);
                        if (n3 == -1) {
                            return ParseResult.INVALID_PORT;
                        }
                    } else {
                        this.host = Builder.canonicalizeHost(string2, n5, n3);
                        this.port = HttpUrl.defaultPort(this.scheme);
                    }
                    if (this.host == null) {
                        return ParseResult.INVALID_HOST;
                    }
                    n3 = n6;
                }
                n = Util.delimiterOffset(string2, n3, n2, "?#");
                this.resolvePath(string2, n3, n);
                n = n3 = n;
                if (n3 < n2) {
                    n = n3;
                    if (string2.charAt(n3) == '?') {
                        n = Util.delimiterOffset(string2, n3, n2, '#');
                        this.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(string2, n3 + 1, n, HttpUrl.QUERY_ENCODE_SET, true, false, true, true));
                    }
                }
                if (n < n2 && string2.charAt(n) == '#') {
                    this.encodedFragment = HttpUrl.canonicalize(string2, n + 1, n2, HttpUrl.FRAGMENT_ENCODE_SET, true, false, false, false);
                }
                return ParseResult.SUCCESS;
            }
            return ParseResult.MISSING_SCHEME;
        }

        public Builder password(String string2) {
            if (string2 != null) {
                this.encodedPassword = HttpUrl.canonicalize(string2, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
                return this;
            }
            throw new NullPointerException("password == null");
        }

        public Builder port(int n) {
            if (n > 0 && n <= 65535) {
                this.port = n;
                return this;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("unexpected port: ");
            stringBuilder.append(n);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder query(String list) {
            list = list != null ? HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize((String)((Object)list), HttpUrl.QUERY_ENCODE_SET, false, false, true, true)) : null;
            this.encodedQueryNamesAndValues = list;
            return this;
        }

        Builder reencodeForUri() {
            Object object;
            int n;
            int n2 = this.encodedPathSegments.size();
            for (n = 0; n < n2; ++n) {
                object = this.encodedPathSegments.get(n);
                this.encodedPathSegments.set(n, HttpUrl.canonicalize((String)object, HttpUrl.PATH_SEGMENT_ENCODE_SET_URI, true, true, false, true));
            }
            object = this.encodedQueryNamesAndValues;
            if (object != null) {
                n2 = object.size();
                for (n = 0; n < n2; ++n) {
                    object = this.encodedQueryNamesAndValues.get(n);
                    if (object == null) continue;
                    this.encodedQueryNamesAndValues.set(n, HttpUrl.canonicalize((String)object, HttpUrl.QUERY_COMPONENT_ENCODE_SET_URI, true, true, true, true));
                }
            }
            if ((object = this.encodedFragment) != null) {
                this.encodedFragment = HttpUrl.canonicalize((String)object, HttpUrl.FRAGMENT_ENCODE_SET_URI, true, true, false, false);
            }
            return this;
        }

        public Builder removeAllEncodedQueryParameters(String string2) {
            if (string2 != null) {
                if (this.encodedQueryNamesAndValues == null) {
                    return this;
                }
                this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(string2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, true, false, true, true));
                return this;
            }
            throw new NullPointerException("encodedName == null");
        }

        public Builder removeAllQueryParameters(String string2) {
            if (string2 != null) {
                if (this.encodedQueryNamesAndValues == null) {
                    return this;
                }
                this.removeAllCanonicalQueryParameters(HttpUrl.canonicalize(string2, HttpUrl.QUERY_COMPONENT_ENCODE_SET, false, false, true, true));
                return this;
            }
            throw new NullPointerException("name == null");
        }

        public Builder removePathSegment(int n) {
            this.encodedPathSegments.remove(n);
            if (this.encodedPathSegments.isEmpty()) {
                this.encodedPathSegments.add(HttpUrl.FRAGMENT_ENCODE_SET);
            }
            return this;
        }

        public Builder scheme(String string2) {
            block2: {
                block5: {
                    block4: {
                        block3: {
                            if (string2 == null) break block2;
                            if (!string2.equalsIgnoreCase("http")) break block3;
                            this.scheme = "http";
                            break block4;
                        }
                        if (!string2.equalsIgnoreCase("https")) break block5;
                        this.scheme = "https";
                    }
                    return this;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unexpected scheme: ");
                stringBuilder.append(string2);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            throw new NullPointerException("scheme == null");
        }

        public Builder setEncodedPathSegment(int n, String string2) {
            if (string2 != null) {
                CharSequence charSequence = HttpUrl.canonicalize(string2, 0, string2.length(), HttpUrl.PATH_SEGMENT_ENCODE_SET, true, false, false, true);
                this.encodedPathSegments.set(n, (String)charSequence);
                if (!this.isDot((String)charSequence) && !this.isDotDot((String)charSequence)) {
                    return this;
                }
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("unexpected path segment: ");
                ((StringBuilder)charSequence).append(string2);
                throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
            }
            throw new NullPointerException("encodedPathSegment == null");
        }

        public Builder setEncodedQueryParameter(String string2, String string3) {
            this.removeAllEncodedQueryParameters(string2);
            this.addEncodedQueryParameter(string2, string3);
            return this;
        }

        public Builder setPathSegment(int n, String string2) {
            if (string2 != null) {
                CharSequence charSequence = HttpUrl.canonicalize(string2, 0, string2.length(), HttpUrl.PATH_SEGMENT_ENCODE_SET, false, false, false, true);
                if (!this.isDot((String)charSequence) && !this.isDotDot((String)charSequence)) {
                    this.encodedPathSegments.set(n, (String)charSequence);
                    return this;
                }
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("unexpected path segment: ");
                ((StringBuilder)charSequence).append(string2);
                throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
            }
            throw new NullPointerException("pathSegment == null");
        }

        public Builder setQueryParameter(String string2, String string3) {
            this.removeAllQueryParameters(string2);
            this.addQueryParameter(string2, string3);
            return this;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.scheme);
            stringBuilder.append("://");
            if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
                stringBuilder.append(this.encodedUsername);
                if (!this.encodedPassword.isEmpty()) {
                    stringBuilder.append(':');
                    stringBuilder.append(this.encodedPassword);
                }
                stringBuilder.append('@');
            }
            if (this.host.indexOf(58) != -1) {
                stringBuilder.append('[');
                stringBuilder.append(this.host);
                stringBuilder.append(']');
            } else {
                stringBuilder.append(this.host);
            }
            int n = this.effectivePort();
            if (n != HttpUrl.defaultPort(this.scheme)) {
                stringBuilder.append(':');
                stringBuilder.append(n);
            }
            HttpUrl.pathSegmentsToString(stringBuilder, this.encodedPathSegments);
            if (this.encodedQueryNamesAndValues != null) {
                stringBuilder.append('?');
                HttpUrl.namesAndValuesToQueryString(stringBuilder, this.encodedQueryNamesAndValues);
            }
            if (this.encodedFragment != null) {
                stringBuilder.append('#');
                stringBuilder.append(this.encodedFragment);
            }
            return stringBuilder.toString();
        }

        public Builder username(String string2) {
            if (string2 != null) {
                this.encodedUsername = HttpUrl.canonicalize(string2, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
                return this;
            }
            throw new NullPointerException("username == null");
        }

        static final class ParseResult
        extends Enum<ParseResult> {
            private static final ParseResult[] $VALUES;
            public static final /* enum */ ParseResult INVALID_HOST;
            public static final /* enum */ ParseResult INVALID_PORT;
            public static final /* enum */ ParseResult MISSING_SCHEME;
            public static final /* enum */ ParseResult SUCCESS;
            public static final /* enum */ ParseResult UNSUPPORTED_SCHEME;

            static {
                ParseResult parseResult;
                ParseResult parseResult2;
                ParseResult parseResult3;
                ParseResult parseResult4;
                ParseResult parseResult5;
                SUCCESS = parseResult5 = new ParseResult();
                MISSING_SCHEME = parseResult4 = new ParseResult();
                UNSUPPORTED_SCHEME = parseResult3 = new ParseResult();
                INVALID_PORT = parseResult2 = new ParseResult();
                INVALID_HOST = parseResult = new ParseResult();
                $VALUES = new ParseResult[]{parseResult5, parseResult4, parseResult3, parseResult2, parseResult};
            }

            public static ParseResult valueOf(String string2) {
                return Enum.valueOf(ParseResult.class, string2);
            }

            public static ParseResult[] values() {
                return (ParseResult[])$VALUES.clone();
            }
        }
    }
}

