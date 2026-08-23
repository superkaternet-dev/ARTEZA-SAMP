/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Cookie {
    private static final Pattern DAY_OF_MONTH_PATTERN;
    private static final Pattern MONTH_PATTERN;
    private static final Pattern TIME_PATTERN;
    private static final Pattern YEAR_PATTERN;
    private final String domain;
    private final long expiresAt;
    private final boolean hostOnly;
    private final boolean httpOnly;
    private final String name;
    private final String path;
    private final boolean persistent;
    private final boolean secure;
    private final String value;

    static {
        YEAR_PATTERN = Pattern.compile("(\\d{2,4})[^\\d]*");
        MONTH_PATTERN = Pattern.compile("(?i)(jan|feb|mar|apr|may|jun|jul|aug|sep|oct|nov|dec).*");
        DAY_OF_MONTH_PATTERN = Pattern.compile("(\\d{1,2})[^\\d]*");
        TIME_PATTERN = Pattern.compile("(\\d{1,2}):(\\d{1,2}):(\\d{1,2})[^\\d]*");
    }

    private Cookie(String string2, String string3, long l, String string4, String string5, boolean bl, boolean bl2, boolean bl3, boolean bl4) {
        this.name = string2;
        this.value = string3;
        this.expiresAt = l;
        this.domain = string4;
        this.path = string5;
        this.secure = bl;
        this.httpOnly = bl2;
        this.hostOnly = bl3;
        this.persistent = bl4;
    }

    private Cookie(Builder builder) {
        if (builder.name != null) {
            if (builder.value != null) {
                if (builder.domain != null) {
                    this.name = builder.name;
                    this.value = builder.value;
                    this.expiresAt = builder.expiresAt;
                    this.domain = builder.domain;
                    this.path = builder.path;
                    this.secure = builder.secure;
                    this.httpOnly = builder.httpOnly;
                    this.persistent = builder.persistent;
                    this.hostOnly = builder.hostOnly;
                    return;
                }
                throw new NullPointerException("builder.domain == null");
            }
            throw new NullPointerException("builder.value == null");
        }
        throw new NullPointerException("builder.name == null");
    }

    private static int dateCharacterOffset(String string2, int n, int n2, boolean bl) {
        while (n < n2) {
            char c = string2.charAt(n);
            c = !(c < ' ' && c != '\t' || c >= '\u007f' || c >= '0' && c <= '9' || c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == ':') ? (char)'\u0000' : '\u0001';
            if (c == (bl ^ '\u0001')) {
                return n;
            }
            ++n;
        }
        return n2;
    }

    private static boolean domainMatch(HttpUrl object, String string2) {
        if (((String)(object = ((HttpUrl)object).host())).equals(string2)) {
            return true;
        }
        return ((String)object).endsWith(string2) && ((String)object).charAt(((String)object).length() - string2.length() - 1) == '.' && !Util.verifyAsIpAddress((String)object);
    }

    static Cookie parse(long l, HttpUrl object, String string2) {
        int n = string2.length();
        int n2 = Util.delimiterOffset(string2, 0, n, ';');
        int n3 = Util.delimiterOffset(string2, 0, n2, '=');
        if (n3 == n2) {
            return null;
        }
        String string3 = Util.trimSubstring(string2, 0, n3);
        if (string3.isEmpty()) {
            return null;
        }
        String string4 = Util.trimSubstring(string2, n3 + 1, n2);
        long l2 = 253402300799999L;
        String string5 = null;
        String string6 = null;
        long l3 = -1L;
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = true;
        boolean bl4 = false;
        ++n2;
        while (n2 < n) {
            boolean bl5;
            boolean bl6;
            long l4;
            String string7;
            boolean bl7;
            long l5;
            int n4 = Util.delimiterOffset(string2, n2, n, ';');
            n3 = Util.delimiterOffset(string2, n2, n4, '=');
            String string8 = Util.trimSubstring(string2, n2, n3);
            String string9 = n3 < n4 ? Util.trimSubstring(string2, n3 + 1, n4) : "";
            if (string8.equalsIgnoreCase("expires")) {
                try {
                    l5 = Cookie.parseExpires(string9, 0, string9.length());
                    bl7 = true;
                    string9 = string5;
                    string7 = string6;
                    l4 = l3;
                    bl6 = bl;
                    bl5 = bl3;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    l5 = l2;
                    string9 = string5;
                    string7 = string6;
                    l4 = l3;
                    bl6 = bl;
                    bl5 = bl3;
                    bl7 = bl4;
                }
            } else if (string8.equalsIgnoreCase("max-age")) {
                try {
                    l4 = Cookie.parseMaxAge(string9);
                    bl7 = true;
                    l5 = l2;
                    string9 = string5;
                    string7 = string6;
                    bl6 = bl;
                    bl5 = bl3;
                }
                catch (NumberFormatException numberFormatException) {
                    l5 = l2;
                    string9 = string5;
                    string7 = string6;
                    l4 = l3;
                    bl6 = bl;
                    bl5 = bl3;
                    bl7 = bl4;
                }
            } else if (string8.equalsIgnoreCase("domain")) {
                try {
                    string9 = Cookie.parseDomain(string9);
                    bl5 = false;
                    l5 = l2;
                    string7 = string6;
                    l4 = l3;
                    bl6 = bl;
                    bl7 = bl4;
                }
                catch (IllegalArgumentException illegalArgumentException) {
                    l5 = l2;
                    string9 = string5;
                    string7 = string6;
                    l4 = l3;
                    bl6 = bl;
                    bl5 = bl3;
                    bl7 = bl4;
                }
            } else if (string8.equalsIgnoreCase("path")) {
                string7 = string9;
                l5 = l2;
                string9 = string5;
                l4 = l3;
                bl6 = bl;
                bl5 = bl3;
                bl7 = bl4;
            } else if (string8.equalsIgnoreCase("secure")) {
                bl6 = true;
                l5 = l2;
                string9 = string5;
                string7 = string6;
                l4 = l3;
                bl5 = bl3;
                bl7 = bl4;
            } else {
                l5 = l2;
                string9 = string5;
                string7 = string6;
                l4 = l3;
                bl6 = bl;
                bl5 = bl3;
                bl7 = bl4;
                if (string8.equalsIgnoreCase("httponly")) {
                    bl2 = true;
                    bl7 = bl4;
                    bl5 = bl3;
                    bl6 = bl;
                    l4 = l3;
                    string7 = string6;
                    string9 = string5;
                    l5 = l2;
                }
            }
            n2 = n4 + 1;
            l2 = l5;
            string5 = string9;
            string6 = string7;
            l3 = l4;
            bl = bl6;
            bl3 = bl5;
            bl4 = bl7;
        }
        if (l3 == Long.MIN_VALUE) {
            l = Long.MIN_VALUE;
        } else if (l3 != -1L) {
            l2 = l3 <= 9223372036854775L ? 1000L * l3 : Long.MAX_VALUE;
            l = (l2 = l + l2) >= l && l2 <= 253402300799999L ? l2 : 253402300799999L;
        } else {
            l = l2;
        }
        if (string5 == null) {
            string2 = ((HttpUrl)object).host();
        } else {
            if (!Cookie.domainMatch((HttpUrl)object, string5)) {
                return null;
            }
            string2 = string5;
        }
        string5 = "/";
        if (string6 != null && string6.startsWith("/")) {
            object = string6;
        } else {
            string6 = ((HttpUrl)object).encodedPath();
            n2 = string6.lastIndexOf(47);
            object = string5;
            if (n2 != 0) {
                object = string6.substring(0, n2);
            }
        }
        return new Cookie(string3, string4, l, string2, (String)object, bl, bl2, bl3, bl4);
    }

    public static Cookie parse(HttpUrl httpUrl, String string2) {
        return Cookie.parse(System.currentTimeMillis(), httpUrl, string2);
    }

    public static List<Cookie> parseAll(HttpUrl list, Headers object) {
        List<String> list2 = ((Headers)object).values("Set-Cookie");
        object = null;
        int n = list2.size();
        for (int i = 0; i < n; ++i) {
            Cookie cookie = Cookie.parse((HttpUrl)((Object)list), list2.get(i));
            if (cookie == null) continue;
            Object object2 = object;
            if (object == null) {
                object2 = new ArrayList();
            }
            object2.add(cookie);
            object = object2;
        }
        list = object != null ? Collections.unmodifiableList(object) : Collections.emptyList();
        return list;
    }

    private static String parseDomain(String string2) {
        if (!string2.endsWith(".")) {
            String string3 = string2;
            if (string2.startsWith(".")) {
                string3 = string2.substring(1);
            }
            if ((string2 = Util.domainToAscii(string3)) != null) {
                return string2;
            }
            throw new IllegalArgumentException();
        }
        throw new IllegalArgumentException();
    }

    private static long parseExpires(String object, int n, int n2) {
        int n3 = Cookie.dateCharacterOffset((String)object, n, n2, false);
        int n4 = -1;
        int n5 = -1;
        int n6 = -1;
        int n7 = -1;
        int n8 = -1;
        n = -1;
        Matcher matcher = TIME_PATTERN.matcher((CharSequence)object);
        while (n3 < n2) {
            Pattern pattern;
            int n9;
            int n10;
            int n11;
            int n12;
            int n13;
            int n14 = Cookie.dateCharacterOffset((String)object, n3 + 1, n2, true);
            matcher.region(n3, n14);
            if (n4 == -1 && matcher.usePattern(TIME_PATTERN).matches()) {
                n3 = Integer.parseInt(matcher.group(1));
                n13 = Integer.parseInt(matcher.group(2));
                n12 = Integer.parseInt(matcher.group(3));
                n11 = n7;
                n10 = n8;
                n9 = n;
            } else if (n7 == -1 && matcher.usePattern(DAY_OF_MONTH_PATTERN).matches()) {
                n11 = Integer.parseInt(matcher.group(1));
                n3 = n4;
                n13 = n5;
                n12 = n6;
                n10 = n8;
                n9 = n;
            } else if (n8 == -1 && matcher.usePattern(pattern = MONTH_PATTERN).matches()) {
                String string2 = matcher.group(1).toLowerCase(Locale.US);
                n10 = pattern.pattern().indexOf(string2) / 4;
                n3 = n4;
                n13 = n5;
                n12 = n6;
                n11 = n7;
                n9 = n;
            } else {
                n3 = n4;
                n13 = n5;
                n12 = n6;
                n11 = n7;
                n10 = n8;
                n9 = n;
                if (n == -1) {
                    n3 = n4;
                    n13 = n5;
                    n12 = n6;
                    n11 = n7;
                    n10 = n8;
                    n9 = n;
                    if (matcher.usePattern(YEAR_PATTERN).matches()) {
                        n9 = Integer.parseInt(matcher.group(1));
                        n10 = n8;
                        n11 = n7;
                        n12 = n6;
                        n13 = n5;
                        n3 = n4;
                    }
                }
            }
            n14 = Cookie.dateCharacterOffset((String)object, n14 + 1, n2, false);
            n4 = n3;
            n5 = n13;
            n6 = n12;
            n7 = n11;
            n8 = n10;
            n = n9;
            n3 = n14;
        }
        n2 = n;
        if (n >= 70) {
            n2 = n;
            if (n <= 99) {
                n2 = n + 1900;
            }
        }
        n = n2;
        if (n2 >= 0) {
            n = n2;
            if (n2 <= 69) {
                n = n2 + 2000;
            }
        }
        if (n >= 1601) {
            if (n8 != -1) {
                if (n7 >= 1 && n7 <= 31) {
                    if (n4 >= 0 && n4 <= 23) {
                        if (n5 >= 0 && n5 <= 59) {
                            if (n6 >= 0 && n6 <= 59) {
                                object = new GregorianCalendar(Util.UTC);
                                ((Calendar)object).setLenient(false);
                                ((Calendar)object).set(1, n);
                                ((Calendar)object).set(2, n8 - 1);
                                ((Calendar)object).set(5, n7);
                                ((Calendar)object).set(11, n4);
                                ((Calendar)object).set(12, n5);
                                ((Calendar)object).set(13, n6);
                                ((Calendar)object).set(14, 0);
                                return ((Calendar)object).getTimeInMillis();
                            }
                            throw new IllegalArgumentException();
                        }
                        throw new IllegalArgumentException();
                    }
                    throw new IllegalArgumentException();
                }
                throw new IllegalArgumentException();
            }
            throw new IllegalArgumentException();
        }
        object = new IllegalArgumentException();
        throw object;
    }

    private static long parseMaxAge(String string2) {
        long l = Long.MIN_VALUE;
        try {
            long l2 = Long.parseLong(string2);
            if (l2 > 0L) {
                l = l2;
            }
            return l;
        }
        catch (NumberFormatException numberFormatException) {
            if (string2.matches("-?\\d+")) {
                if (!string2.startsWith("-")) {
                    l = Long.MAX_VALUE;
                }
                return l;
            }
            throw numberFormatException;
        }
    }

    private static boolean pathMatch(HttpUrl object, String string2) {
        if (((String)(object = ((HttpUrl)object).encodedPath())).equals(string2)) {
            return true;
        }
        if (((String)object).startsWith(string2)) {
            if (string2.endsWith("/")) {
                return true;
            }
            if (((String)object).charAt(string2.length()) == '/') {
                return true;
            }
        }
        return false;
    }

    public String domain() {
        return this.domain;
    }

    public boolean equals(Object object) {
        boolean bl;
        block1: {
            boolean bl2 = object instanceof Cookie;
            bl = false;
            if (!bl2) {
                return false;
            }
            object = (Cookie)object;
            if (!((Cookie)object).name.equals(this.name) || !((Cookie)object).value.equals(this.value) || !((Cookie)object).domain.equals(this.domain) || !((Cookie)object).path.equals(this.path) || ((Cookie)object).expiresAt != this.expiresAt || ((Cookie)object).secure != this.secure || ((Cookie)object).httpOnly != this.httpOnly || ((Cookie)object).persistent != this.persistent || ((Cookie)object).hostOnly != this.hostOnly) break block1;
            bl = true;
        }
        return bl;
    }

    public long expiresAt() {
        return this.expiresAt;
    }

    public int hashCode() {
        int n = this.name.hashCode();
        int n2 = this.value.hashCode();
        int n3 = this.domain.hashCode();
        int n4 = this.path.hashCode();
        long l = this.expiresAt;
        return ((((((((17 * 31 + n) * 31 + n2) * 31 + n3) * 31 + n4) * 31 + (int)(l ^ l >>> 32)) * 31 + (this.secure ^ 1)) * 31 + (this.httpOnly ^ 1)) * 31 + (this.persistent ^ 1)) * 31 + (this.hostOnly ^ 1);
    }

    public boolean hostOnly() {
        return this.hostOnly;
    }

    public boolean httpOnly() {
        return this.httpOnly;
    }

    public boolean matches(HttpUrl httpUrl) {
        boolean bl = this.hostOnly ? httpUrl.host().equals(this.domain) : Cookie.domainMatch(httpUrl, this.domain);
        if (!bl) {
            return false;
        }
        if (!Cookie.pathMatch(httpUrl, this.path)) {
            return false;
        }
        return !this.secure || httpUrl.isHttps();
    }

    public String name() {
        return this.name;
    }

    public String path() {
        return this.path;
    }

    public boolean persistent() {
        return this.persistent;
    }

    public boolean secure() {
        return this.secure;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.name);
        stringBuilder.append('=');
        stringBuilder.append(this.value);
        if (this.persistent) {
            if (this.expiresAt == Long.MIN_VALUE) {
                stringBuilder.append("; max-age=0");
            } else {
                stringBuilder.append("; expires=");
                stringBuilder.append(HttpDate.format(new Date(this.expiresAt)));
            }
        }
        if (!this.hostOnly) {
            stringBuilder.append("; domain=");
            stringBuilder.append(this.domain);
        }
        stringBuilder.append("; path=");
        stringBuilder.append(this.path);
        if (this.secure) {
            stringBuilder.append("; secure");
        }
        if (this.httpOnly) {
            stringBuilder.append("; httponly");
        }
        return stringBuilder.toString();
    }

    public String value() {
        return this.value;
    }

    public static final class Builder {
        String domain;
        long expiresAt = 253402300799999L;
        boolean hostOnly;
        boolean httpOnly;
        String name;
        String path = "/";
        boolean persistent;
        boolean secure;
        String value;

        private Builder domain(String string2, boolean bl) {
            if (string2 != null) {
                CharSequence charSequence = Util.domainToAscii(string2);
                if (charSequence != null) {
                    this.domain = charSequence;
                    this.hostOnly = bl;
                    return this;
                }
                charSequence = new StringBuilder();
                ((StringBuilder)charSequence).append("unexpected domain: ");
                ((StringBuilder)charSequence).append(string2);
                throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
            }
            throw new NullPointerException("domain == null");
        }

        public Cookie build() {
            return new Cookie(this);
        }

        public Builder domain(String string2) {
            return this.domain(string2, false);
        }

        public Builder expiresAt(long l) {
            long l2 = l;
            if (l <= 0L) {
                l2 = Long.MIN_VALUE;
            }
            l = l2;
            if (l2 > 253402300799999L) {
                l = 253402300799999L;
            }
            this.expiresAt = l;
            this.persistent = true;
            return this;
        }

        public Builder hostOnlyDomain(String string2) {
            return this.domain(string2, true);
        }

        public Builder httpOnly() {
            this.httpOnly = true;
            return this;
        }

        public Builder name(String string2) {
            if (string2 != null) {
                if (string2.trim().equals(string2)) {
                    this.name = string2;
                    return this;
                }
                throw new IllegalArgumentException("name is not trimmed");
            }
            throw new NullPointerException("name == null");
        }

        public Builder path(String string2) {
            if (string2.startsWith("/")) {
                this.path = string2;
                return this;
            }
            throw new IllegalArgumentException("path must start with '/'");
        }

        public Builder secure() {
            this.secure = true;
            return this;
        }

        public Builder value(String string2) {
            if (string2 != null) {
                if (string2.trim().equals(string2)) {
                    this.value = string2;
                    return this;
                }
                throw new IllegalArgumentException("value is not trimmed");
            }
            throw new NullPointerException("value == null");
        }
    }
}

