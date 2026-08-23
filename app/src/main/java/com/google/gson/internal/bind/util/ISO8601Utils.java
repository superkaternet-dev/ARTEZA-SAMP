/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601Utils {
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
    private static final String UTC_ID = "UTC";

    private static boolean checkOffset(String string2, int n, char c) {
        boolean bl = n < string2.length() && string2.charAt(n) == c;
        return bl;
    }

    public static String format(Date date) {
        return ISO8601Utils.format(date, false, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean bl) {
        return ISO8601Utils.format(date, bl, TIMEZONE_UTC);
    }

    public static String format(Date object, boolean bl, TimeZone timeZone) {
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime((Date)object);
        int n = "yyyy-MM-ddThh:mm:ss".length();
        int n2 = bl ? ".sss".length() : 0;
        object = timeZone.getRawOffset() == 0 ? "Z" : "+hh:mm";
        object = new StringBuilder(n + n2 + ((String)object).length());
        ISO8601Utils.padInt((StringBuilder)object, gregorianCalendar.get(1), "yyyy".length());
        char c = '-';
        ((StringBuilder)object).append('-');
        ISO8601Utils.padInt((StringBuilder)object, gregorianCalendar.get(2) + 1, "MM".length());
        ((StringBuilder)object).append('-');
        ISO8601Utils.padInt((StringBuilder)object, gregorianCalendar.get(5), "dd".length());
        ((StringBuilder)object).append('T');
        ISO8601Utils.padInt((StringBuilder)object, gregorianCalendar.get(11), "hh".length());
        ((StringBuilder)object).append(':');
        ISO8601Utils.padInt((StringBuilder)object, gregorianCalendar.get(12), "mm".length());
        ((StringBuilder)object).append(':');
        ISO8601Utils.padInt((StringBuilder)object, gregorianCalendar.get(13), "ss".length());
        if (bl) {
            ((StringBuilder)object).append('.');
            ISO8601Utils.padInt((StringBuilder)object, gregorianCalendar.get(14), "sss".length());
        }
        if ((n2 = timeZone.getOffset(gregorianCalendar.getTimeInMillis())) != 0) {
            int n3 = Math.abs(n2 / 60000 / 60);
            n = Math.abs(n2 / 60000 % 60);
            if (n2 >= 0) {
                c = '+';
            }
            ((StringBuilder)object).append(c);
            ISO8601Utils.padInt((StringBuilder)object, n3, "hh".length());
            ((StringBuilder)object).append(':');
            ISO8601Utils.padInt((StringBuilder)object, n, "mm".length());
        } else {
            ((StringBuilder)object).append('Z');
        }
        return ((StringBuilder)object).toString();
    }

    private static int indexOfNonDigit(String string2, int n) {
        while (n < string2.length()) {
            char c = string2.charAt(n);
            if (c >= '0' && c <= '9') {
                ++n;
                continue;
            }
            return n;
        }
        return string2.length();
    }

    private static void padInt(StringBuilder stringBuilder, int n, int n2) {
        String string2 = Integer.toString(n);
        for (n = n2 - string2.length(); n > 0; --n) {
            stringBuilder.append('0');
        }
        stringBuilder.append(string2);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static Date parse(String var0, ParsePosition var1_1) throws ParseException {
        block41: {
            block40: {
                block37: {
                    block38: {
                        block39: {
                            block36: {
                                try {
                                    var3_2 = var1_1.getIndex();
                                    var4_3 = var3_2 + 4;
                                    var13_4 = ISO8601Utils.parseInt((String)var0, var3_2, var4_3);
                                    var3_2 = var4_3;
                                    if (ISO8601Utils.checkOffset((String)var0, var4_3, '-')) {
                                        var3_2 = var4_3 + 1;
                                    }
                                    var4_3 = var3_2 + 2;
                                    var14_5 = ISO8601Utils.parseInt((String)var0, var3_2, var4_3);
                                    var3_2 = var4_3;
                                    if (ISO8601Utils.checkOffset((String)var0, var4_3, '-')) {
                                        var3_2 = var4_3 + 1;
                                    }
                                    var4_3 = var3_2 + 2;
                                    var15_6 = ISO8601Utils.parseInt((String)var0, var3_2, var4_3);
                                    var6_7 = 0;
                                    var7_8 = 0;
                                    var12_9 = 0;
                                    var11_10 = 0;
                                    var17_11 = ISO8601Utils.checkOffset((String)var0, var4_3, 'T');
                                    if (var17_11) break block36;
                                }
                                catch (IllegalArgumentException var18_22) {
                                    break block37;
                                }
                                catch (NumberFormatException var18_24) {
                                    break block37;
                                }
                                catch (IndexOutOfBoundsException var18_25) {
                                    // empty catch block
                                    break block37;
                                }
                                try {
                                    if (var0.length() <= var4_3) {
                                        var18_12 = new GregorianCalendar(var13_4, var14_5 - 1, var15_6);
                                        var1_1.setIndex(var4_3);
                                        return var18_12.getTime();
                                    }
                                }
                                catch (IllegalArgumentException var18_13) {
                                    break block37;
                                }
                                catch (NumberFormatException var18_14) {
                                    break block37;
                                }
                                catch (IndexOutOfBoundsException var18_15) {
                                    break block37;
                                }
                            }
                            var3_2 = var4_3;
                            var5_26 = var12_9;
                            var8_27 = var11_10;
                            if (!var17_11) ** GOTO lbl-1000
                            var3_2 = var4_3 + 1;
                            var4_3 = var3_2 + 2;
                            var9_28 = ISO8601Utils.parseInt((String)var0, var3_2, var4_3);
                            var3_2 = var4_3;
                            if (ISO8601Utils.checkOffset((String)var0, var4_3, ':')) {
                                var3_2 = var4_3 + 1;
                            }
                            var4_3 = var3_2 + 2;
                            var10_29 = ISO8601Utils.parseInt((String)var0, var3_2, var4_3);
                            if (ISO8601Utils.checkOffset((String)var0, var4_3, ':')) {
                                ++var4_3;
                            }
                            var3_2 = var4_3;
                            var6_7 = var9_28;
                            var7_8 = var10_29;
                            var5_26 = var12_9;
                            var8_27 = var11_10;
                            if (var0.length() <= var4_3) ** GOTO lbl-1000
                            var16_30 = var0.charAt(var4_3);
                            var3_2 = var4_3;
                            var6_7 = var9_28;
                            var7_8 = var10_29;
                            var5_26 = var12_9;
                            var8_27 = var11_10;
                            if (var16_30 == 'Z') ** GOTO lbl-1000
                            var3_2 = var4_3;
                            var6_7 = var9_28;
                            var7_8 = var10_29;
                            var5_26 = var12_9;
                            var8_27 = var11_10;
                            if (var16_30 == '+') ** GOTO lbl-1000
                            var3_2 = var4_3;
                            var6_7 = var9_28;
                            var7_8 = var10_29;
                            var5_26 = var12_9;
                            var8_27 = var11_10;
                            if (var16_30 == '-') ** GOTO lbl-1000
                            var3_2 = var4_3 + 2;
                            var5_26 = ISO8601Utils.parseInt((String)var0, var4_3, var3_2);
                            if (var5_26 > 59 && var5_26 < 63) {
                                var5_26 = 59;
                            }
                            if (!ISO8601Utils.checkOffset((String)var0, var3_2, '.')) break block39;
                            var6_7 = var3_2 + 1;
                            var3_2 = ISO8601Utils.indexOfNonDigit((String)var0, var6_7 + 1);
                            var7_8 = Math.min(var3_2, var6_7 + 3);
                            var4_3 = ISO8601Utils.parseInt((String)var0, var6_7, var7_8);
                            switch (var7_8 - var6_7) {
                                default: {
                                    break;
                                }
                                case 2: {
                                    var4_3 *= 10;
                                    break;
                                }
                                case 1: {
                                    var4_3 *= 100;
                                }
                            }
                            var6_7 = var9_28;
                            var7_8 = var10_29;
                            var8_27 = var4_3;
                            ** GOTO lbl-1000
                        }
                        var8_27 = var11_10;
                        var7_8 = var10_29;
                        var6_7 = var9_28;
lbl-1000:
                        // 7 sources

                        {
                            if (var0.length() <= var3_2) ** GOTO lbl167
                            var2_31 = var0.charAt(var3_2);
                            if (var2_31 != 'Z') break block38;
                        }
                        {
                            var18_16 = ISO8601Utils.TIMEZONE_UTC;
                            ++var3_2;
                            ** GOTO lbl156
                        }
                    }
                    if (var2_31 == '+' || var2_31 == '-') ** GOTO lbl124
                    try {
                        var19_32 = new StringBuilder();
                        var19_32.append("Invalid time zone indicator '");
                        var19_32.append(var2_31);
                        var19_32.append("'");
                        var18_17 = new IndexOutOfBoundsException(var19_32.toString());
                        throw var18_17;
lbl124:
                        // 1 sources

                        var18_16 = var0.substring(var3_2);
                        if (var18_16.length() < 5) {
                            var19_33 = new StringBuilder();
                            var19_33.append((String)var18_16);
                            var19_33.append("00");
                            var18_16 = var19_33.toString();
                        }
                        var3_2 += var18_16.length();
                        if (!"+0000".equals(var18_16) && !"+00:00".equals(var18_16)) {
                            var19_33 = new StringBuilder();
                            var19_33.append("GMT");
                            var19_33.append((String)var18_16);
                            var19_33 = var19_33.toString();
                            var18_16 = TimeZone.getTimeZone((String)var19_33);
                            var20_35 = var18_16.getID();
                            if (!var20_35.equals(var19_33) && !var20_35.replace(":", "").equals(var19_33)) {
                                var21_37 = new StringBuilder();
                                var21_37.append("Mismatching time zone indicator: ");
                                var21_37.append((String)var19_33);
                                var21_37.append(" given, resolves to ");
                                var21_37.append(var18_16.getID());
                                var20_35 = new IndexOutOfBoundsException(var21_37.toString());
                                throw var20_35;
                            }
                        } else {
                            var18_16 = ISO8601Utils.TIMEZONE_UTC;
                        }
lbl156:
                        // 3 sources

                        var19_33 = new GregorianCalendar((TimeZone)var18_16);
                        var19_33.setLenient(false);
                        var19_33.set(1, var13_4);
                        var19_33.set(2, var14_5 - 1);
                        var19_33.set(5, var15_6);
                        var19_33.set(11, var6_7);
                        var19_33.set(12, var7_8);
                        var19_33.set(13, var5_26);
                        var19_33.set(14, var8_27);
                        var1_1.setIndex(var3_2);
                        return var19_33.getTime();
lbl167:
                        // 1 sources

                        var18_18 = new IllegalArgumentException("No time zone indicator");
                        throw var18_18;
                    }
                    catch (IllegalArgumentException var18_19) {
                    }
                    catch (NumberFormatException var18_20) {
                    }
                    catch (IndexOutOfBoundsException var18_21) {}
                }
                if (var0 == null) {
                    var0 = null;
                } else {
                    var19_34 = new StringBuilder();
                    var19_34.append('\"');
                    var19_34.append((String)var0);
                    var19_34.append("'");
                    var0 = var19_34.toString();
                }
                var20_36 = var18_23.getMessage();
                if (var20_36 == null) break block40;
                var19_34 = var20_36;
                if (!var20_36.isEmpty()) break block41;
            }
            var19_34 = new StringBuilder();
            var19_34.append("(");
            var19_34.append(var18_23.getClass().getName());
            var19_34.append(")");
            var19_34 = var19_34.toString();
        }
        var20_36 = new StringBuilder();
        var20_36.append("Failed to parse date [");
        var20_36.append((String)var0);
        var20_36.append("]: ");
        var20_36.append((String)var19_34);
        var0 = new ParseException(var20_36.toString(), var1_1.getIndex());
        var0.initCause((Throwable)var18_23);
        throw var0;
    }

    private static int parseInt(String object, int n, int n2) throws NumberFormatException {
        if (n >= 0 && n2 <= ((String)object).length() && n <= n2) {
            int n3 = n;
            int n4 = 0;
            int n5 = n3;
            if (n3 < n2) {
                n5 = Character.digit(((String)object).charAt(n3), 10);
                if (n5 >= 0) {
                    n4 = -n5;
                    n5 = n3 + 1;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Invalid number: ");
                    stringBuilder.append(((String)object).substring(n, n2));
                    throw new NumberFormatException(stringBuilder.toString());
                }
            }
            while (n5 < n2) {
                n3 = Character.digit(((String)object).charAt(n5), 10);
                if (n3 >= 0) {
                    n4 = n4 * 10 - n3;
                    ++n5;
                    continue;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid number: ");
                stringBuilder.append(((String)object).substring(n, n2));
                throw new NumberFormatException(stringBuilder.toString());
            }
            return -n4;
        }
        object = new NumberFormatException((String)object);
        throw object;
    }
}

