/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 */
package com.google.android.gms.internal.common;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.CheckForNull;

public final class zzy {
    public static String zza(@CheckForNull String string2, Object ... objectArray) {
        int n;
        CharSequence charSequence;
        int n2;
        int n3;
        int n4 = 0;
        for (n3 = 0; n3 < (n2 = objectArray.length); ++n3) {
            Object object = objectArray[n3];
            if (object == null) {
                charSequence = "null";
            } else {
                try {
                    charSequence = object.toString();
                }
                catch (Exception exception) {
                    charSequence = object.getClass().getName();
                    Object object2 = Integer.toHexString(System.identityHashCode(object));
                    object = new StringBuilder(String.valueOf(charSequence).length() + 1 + String.valueOf(object2).length());
                    ((StringBuilder)object).append((String)charSequence);
                    ((StringBuilder)object).append('@');
                    ((StringBuilder)object).append((String)object2);
                    object = ((StringBuilder)object).toString();
                    Logger logger = Logger.getLogger("com.google.common.base.Strings");
                    object2 = Level.WARNING;
                    charSequence = ((String)object).length() != 0 ? "Exception during lenientFormat for ".concat((String)object) : new String("Exception during lenientFormat for ");
                    logger.logp((Level)object2, "com.google.common.base.Strings", "lenientToString", (String)charSequence, exception);
                    charSequence = exception.getClass().getName();
                    n2 = String.valueOf(charSequence).length();
                    StringBuilder stringBuilder = new StringBuilder(((String)object).length() + 9 + n2);
                    stringBuilder.append("<");
                    stringBuilder.append((String)object);
                    stringBuilder.append(" threw ");
                    stringBuilder.append((String)charSequence);
                    stringBuilder.append(">");
                    charSequence = stringBuilder.toString();
                }
            }
            objectArray[n3] = charSequence;
        }
        charSequence = new StringBuilder(string2.length() + n2 * 16);
        n2 = 0;
        n3 = n4;
        while (n3 < (n4 = objectArray.length) && (n = string2.indexOf("%s", n2)) != -1) {
            ((StringBuilder)charSequence).append(string2, n2, n);
            ((StringBuilder)charSequence).append(objectArray[n3]);
            n2 = n + 2;
            ++n3;
        }
        ((StringBuilder)charSequence).append(string2, n2, string2.length());
        if (n3 < n4) {
            ((StringBuilder)charSequence).append(" [");
            n2 = n3 + 1;
            ((StringBuilder)charSequence).append(objectArray[n3]);
            for (n3 = n2; n3 < objectArray.length; ++n3) {
                ((StringBuilder)charSequence).append(", ");
                ((StringBuilder)charSequence).append(objectArray[n3]);
            }
            ((StringBuilder)charSequence).append(']');
        }
        return ((StringBuilder)charSequence).toString();
    }
}

