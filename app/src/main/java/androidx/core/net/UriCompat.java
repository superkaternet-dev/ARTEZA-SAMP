/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 */
package androidx.core.net;

import android.net.Uri;

public final class UriCompat {
    private UriCompat() {
    }

    public static String toSafeString(Uri object) {
        CharSequence charSequence;
        String string2;
        block8: {
            String string3;
            block9: {
                block10: {
                    string2 = object.getScheme();
                    string3 = object.getSchemeSpecificPart();
                    charSequence = string3;
                    if (string2 == null) break block8;
                    if (string2.equalsIgnoreCase("tel") || string2.equalsIgnoreCase("sip") || string2.equalsIgnoreCase("sms") || string2.equalsIgnoreCase("smsto") || string2.equalsIgnoreCase("mailto") || string2.equalsIgnoreCase("nfc")) break block9;
                    if (string2.equalsIgnoreCase("http") || string2.equalsIgnoreCase("https") || string2.equalsIgnoreCase("ftp")) break block10;
                    charSequence = string3;
                    if (!string2.equalsIgnoreCase("rtsp")) break block8;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("//");
                charSequence = object.getHost();
                string3 = "";
                charSequence = charSequence != null ? object.getHost() : "";
                stringBuilder.append((String)charSequence);
                charSequence = string3;
                if (object.getPort() != -1) {
                    charSequence = new StringBuilder();
                    ((StringBuilder)charSequence).append(":");
                    ((StringBuilder)charSequence).append(object.getPort());
                    charSequence = ((StringBuilder)charSequence).toString();
                }
                stringBuilder.append((String)charSequence);
                stringBuilder.append("/...");
                charSequence = stringBuilder.toString();
                break block8;
            }
            object = new StringBuilder(64);
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(':');
            if (string3 != null) {
                for (int i = 0; i < string3.length(); ++i) {
                    char c = string3.charAt(i);
                    if (c != '-' && c != '@' && c != '.') {
                        ((StringBuilder)object).append('x');
                        continue;
                    }
                    ((StringBuilder)object).append(c);
                }
            }
            return ((StringBuilder)object).toString();
        }
        object = new StringBuilder(64);
        if (string2 != null) {
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append(':');
        }
        if (charSequence != null) {
            ((StringBuilder)object).append((String)charSequence);
        }
        return ((StringBuilder)object).toString();
    }
}

