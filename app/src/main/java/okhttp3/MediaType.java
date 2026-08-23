/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.nio.charset.Charset;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MediaType {
    private static final Pattern PARAMETER;
    private static final String QUOTED = "\"([^\"]*)\"";
    private static final String TOKEN = "([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)";
    private static final Pattern TYPE_SUBTYPE;
    private final String charset;
    private final String mediaType;
    private final String subtype;
    private final String type;

    static {
        TYPE_SUBTYPE = Pattern.compile("([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)/([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)");
        PARAMETER = Pattern.compile(";\\s*(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)=(?:([a-zA-Z0-9-!#$%&'*+.^_`{|}~]+)|\"([^\"]*)\"))?");
    }

    private MediaType(String string2, String string3, String string4, String string5) {
        this.mediaType = string2;
        this.type = string3;
        this.subtype = string4;
        this.charset = string5;
    }

    public static MediaType parse(String string2) {
        Object object = TYPE_SUBTYPE.matcher(string2);
        if (!((Matcher)object).lookingAt()) {
            return null;
        }
        String string3 = ((Matcher)object).group(1).toLowerCase(Locale.US);
        String string4 = ((Matcher)object).group(2).toLowerCase(Locale.US);
        Matcher matcher = null;
        Matcher matcher2 = PARAMETER.matcher(string2);
        int n = ((Matcher)object).end();
        while (n < string2.length()) {
            matcher2.region(n, string2.length());
            if (!matcher2.lookingAt()) {
                return null;
            }
            String string5 = matcher2.group(1);
            object = matcher;
            if (string5 != null) {
                if (!string5.equalsIgnoreCase("charset")) {
                    object = matcher;
                } else {
                    object = matcher2.group(2) != null ? matcher2.group(2) : matcher2.group(3);
                    if (matcher != null && !((String)object).equalsIgnoreCase((String)((Object)matcher))) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Multiple different charsets: ");
                        ((StringBuilder)object).append(string2);
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                }
            }
            n = matcher2.end();
            matcher = object;
        }
        return new MediaType(string2, string3, string4, (String)((Object)matcher));
    }

    public Charset charset() {
        Object object = this.charset;
        object = object != null ? Charset.forName((String)object) : null;
        return object;
    }

    public Charset charset(Charset charset) {
        block0: {
            String string2 = this.charset;
            if (string2 == null) break block0;
            charset = Charset.forName(string2);
        }
        return charset;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof MediaType && ((MediaType)object).mediaType.equals(this.mediaType);
        return bl;
    }

    public int hashCode() {
        return this.mediaType.hashCode();
    }

    public String subtype() {
        return this.subtype;
    }

    public String toString() {
        return this.mediaType;
    }

    public String type() {
        return this.type;
    }
}

