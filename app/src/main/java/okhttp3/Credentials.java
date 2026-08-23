/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.UnsupportedEncodingException;
import okio.ByteString;

public final class Credentials {
    private Credentials() {
    }

    public static String basic(String charSequence, String string2) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append((String)charSequence);
            stringBuilder.append(":");
            stringBuilder.append(string2);
            string2 = ByteString.of(stringBuilder.toString().getBytes("ISO-8859-1")).base64();
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("Basic ");
            ((StringBuilder)charSequence).append(string2);
            charSequence = ((StringBuilder)charSequence).toString();
            return charSequence;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError();
        }
    }
}

