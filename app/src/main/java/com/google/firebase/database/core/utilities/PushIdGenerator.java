/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.core.utilities.Validation;
import java.util.Random;

public class PushIdGenerator {
    private static final int MAX_KEY_LEN = 786;
    private static final char MAX_PUSH_CHAR = 'z';
    private static final char MIN_PUSH_CHAR = '-';
    private static final String PUSH_CHARS = "-0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ_abcdefghijklmnopqrstuvwxyz";
    private static long lastPushTime;
    private static final int[] lastRandChars;
    private static final Random randGen;

    static {
        randGen = new Random();
        lastPushTime = 0L;
        lastRandChars = new int[12];
    }

    public static String generatePushChildName(long l) {
        synchronized (PushIdGenerator.class) {
            boolean bl;
            StringBuilder stringBuilder;
            Object object;
            block15: {
                int n;
                boolean bl2;
                block16: {
                    block14: {
                        long l2 = lastPushTime;
                        bl2 = false;
                        n = l == l2 ? 1 : 0;
                        lastPushTime = l;
                        object = new char[8];
                        stringBuilder = new StringBuilder(20);
                        for (int i = 7; i >= 0; --i) {
                            object[i] = PUSH_CHARS.charAt((int)(l % 64L));
                            l /= 64L;
                            continue;
                        }
                        bl = l == 0L;
                        Utilities.hardAssert(bl);
                        stringBuilder.append((char[])object);
                        if (n != 0) break block14;
                        for (n = 0; n < 12; ++n) {
                            PushIdGenerator.lastRandChars[n] = randGen.nextInt(64);
                            continue;
                        }
                        break block16;
                    }
                    PushIdGenerator.incrementArray();
                }
                for (n = 0; n < 12; ++n) {
                    stringBuilder.append(PUSH_CHARS.charAt(lastRandChars[n]));
                    continue;
                }
                bl = bl2;
                try {
                    if (stringBuilder.length() != 20) break block15;
                    bl = true;
                }
                catch (Throwable throwable) {}
                {
                    throw throwable;
                }
            }
            Utilities.hardAssert(bl);
            object = stringBuilder.toString();
            return object;
        }
    }

    private static void incrementArray() {
        for (int i = 11; i >= 0; --i) {
            int[] nArray = lastRandChars;
            if (nArray[i] != 63) {
                nArray[i] = nArray[i] + 1;
                return;
            }
            nArray[i] = 0;
        }
    }

    public static final String predecessor(String charSequence) {
        Validation.validateNullableKey((String)charSequence);
        Integer n = Utilities.tryParseInt((String)charSequence);
        if (n != null) {
            if (n == Integer.MIN_VALUE) {
                return "[MIN_NAME]";
            }
            return String.valueOf(n - 1);
        }
        if (((StringBuilder)(charSequence = new StringBuilder((String)charSequence))).charAt(((StringBuilder)charSequence).length() - 1) == '-') {
            if (((StringBuilder)charSequence).length() == 1) {
                return String.valueOf(Integer.MAX_VALUE);
            }
            return ((StringBuilder)charSequence).substring(0, ((StringBuilder)charSequence).length() - 1);
        }
        ((StringBuilder)charSequence).setCharAt(((StringBuilder)charSequence).length() - 1, PUSH_CHARS.charAt(PUSH_CHARS.indexOf(((StringBuilder)charSequence).charAt(((StringBuilder)charSequence).length() - 1)) - 1));
        ((StringBuilder)charSequence).append(new String(new char[786 - ((StringBuilder)charSequence).length()]).replace("\u0000", "z"));
        return ((StringBuilder)charSequence).toString();
    }

    public static final String successor(String charSequence) {
        int n;
        Validation.validateNullableKey((String)charSequence);
        Integer n2 = Utilities.tryParseInt((String)charSequence);
        if (n2 != null) {
            if (n2 == Integer.MAX_VALUE) {
                return String.valueOf('-');
            }
            return String.valueOf(n2 + 1);
        }
        if (((StringBuilder)(charSequence = new StringBuilder((String)charSequence))).length() < 786) {
            ((StringBuilder)charSequence).append('-');
            return ((StringBuilder)charSequence).toString();
        }
        for (n = ((StringBuilder)charSequence).length() - 1; n >= 0 && ((StringBuilder)charSequence).charAt(n) == 'z'; --n) {
        }
        if (n == -1) {
            return "[MAX_KEY]";
        }
        ((StringBuilder)charSequence).replace(n, n + 1, String.valueOf(PUSH_CHARS.charAt(PUSH_CHARS.indexOf(((StringBuilder)charSequence).charAt(n)) + 1)));
        return ((StringBuilder)charSequence).substring(0, n + 1);
    }
}

