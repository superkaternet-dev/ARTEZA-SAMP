/*
 * Decompiled with CFR 0.152.
 */
package androidx.core.util;

public class DebugUtils {
    private DebugUtils() {
    }

    public static void buildShortClassTag(Object object, StringBuilder stringBuilder) {
        block5: {
            String string2;
            block7: {
                String string3;
                block6: {
                    block4: {
                        if (object != null) break block4;
                        stringBuilder.append("null");
                        break block5;
                    }
                    string3 = object.getClass().getSimpleName();
                    if (string3 == null) break block6;
                    string2 = string3;
                    if (string3.length() > 0) break block7;
                }
                string3 = object.getClass().getName();
                int n = string3.lastIndexOf(46);
                string2 = string3;
                if (n > 0) {
                    string2 = string3.substring(n + 1);
                }
            }
            stringBuilder.append(string2);
            stringBuilder.append('{');
            stringBuilder.append(Integer.toHexString(System.identityHashCode(object)));
        }
    }
}

