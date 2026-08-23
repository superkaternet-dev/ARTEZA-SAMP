/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;
import org.ini4j.Registry;
import org.ini4j.spi.EscapeTool;
import org.ini4j.spi.ServiceFinder;
import org.ini4j.spi.TypeValuesPair;

public class RegEscapeTool
extends EscapeTool {
    private static final int DIGIT_SIZE = 4;
    private static final Charset HEX_CHARSET;
    private static final RegEscapeTool INSTANCE;
    private static final int LOWER_DIGIT = 15;
    private static final int UPPER_DIGIT = 240;

    static {
        INSTANCE = ServiceFinder.findService(RegEscapeTool.class);
        HEX_CHARSET = Charset.forName("UTF-16LE");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private String bytes2string(byte[] object) {
        try {
            String string2 = new String((byte[])object, 0, ((Object)object).length - 2, HEX_CHARSET);
            return string2;
        }
        catch (NoSuchMethodError noSuchMethodError) {
            try {
                return new String((byte[])object, 0, ((Object)object).length, HEX_CHARSET.name());
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new IllegalStateException(unsupportedEncodingException);
            }
        }
    }

    public static final RegEscapeTool getInstance() {
        return INSTANCE;
    }

    private String[] splitMulti(String string2) {
        int n;
        int n2 = string2.length();
        int n3 = 0;
        int n4 = string2.indexOf(0, 0);
        while (true) {
            n = n3++;
            if (n4 < 0) break;
            n = n4 + 1;
            if (n >= n2) {
                n = n3;
                break;
            }
            n4 = string2.indexOf(0, n);
        }
        String[] stringArray = new String[n];
        n4 = 0;
        for (n3 = 0; n3 < n; ++n3) {
            n2 = string2.indexOf(0, n4);
            stringArray[n3] = string2.substring(n4, n2);
            n4 = n2 + 1;
        }
        return stringArray;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private byte[] string2bytes(String object) {
        byte[] byArray;
        try {
            byArray = ((String)object).getBytes(HEX_CHARSET);
        }
        catch (NoSuchMethodError noSuchMethodError) {
            try {
                return ((String)object).getBytes(HEX_CHARSET.name());
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                throw new IllegalStateException(unsupportedEncodingException);
            }
        }
        return byArray;
    }

    byte[] binary(String string2) {
        byte[] byArray = new byte[string2.length()];
        int n = 0;
        int n2 = 4;
        for (int i = 0; i < string2.length(); ++i) {
            int n3;
            int n4;
            char c = string2.charAt(i);
            if (Character.isWhitespace(c)) {
                n4 = n;
                n3 = n2;
            } else if (c == ',') {
                n4 = n + 1;
                n3 = 4;
            } else {
                int n5 = Character.digit(c, 16);
                n4 = n;
                n3 = n2;
                if (n5 >= 0) {
                    byArray[n] = (byte)(byArray[n] | n5 << n2);
                    n3 = 0;
                    n4 = n;
                }
            }
            n = n4;
            n2 = n3;
        }
        return Arrays.copyOfRange(byArray, 0, n + 1);
    }

    public TypeValuesPair decode(String object) {
        Registry.Type type = this.type((String)object);
        object = type == Registry.Type.REG_SZ ? this.unquote((String)object) : object.substring(type.toString().length() + 1);
        switch (1.$SwitchMap$org$ini4j$Registry$Type[type.ordinal()]) {
            default: {
                break;
            }
            case 4: {
                break;
            }
            case 3: {
                object = String.valueOf(Long.parseLong((String)object, 16));
                break;
            }
            case 1: 
            case 2: {
                object = this.bytes2string(this.binary((String)object));
            }
        }
        if (type == Registry.Type.REG_MULTI_SZ) {
            object = this.splitMulti((String)object);
        } else {
            String[] stringArray = new String[]{object};
            object = stringArray;
        }
        return new TypeValuesPair(type, (String[])object);
    }

    String encode(Registry.Type type, String[] stringArray) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(type.toString());
        stringBuilder.append(':');
        switch (1.$SwitchMap$org$ini4j$Registry$Type[type.ordinal()]) {
            default: {
                stringBuilder.append(stringArray[0]);
                break;
            }
            case 3: {
                stringBuilder.append(String.format("%08x", Long.parseLong(stringArray[0])));
                break;
            }
            case 2: {
                int n = stringArray.length;
                for (int i = 0; i < n; ++i) {
                    stringBuilder.append(this.hexadecimal(stringArray[i]));
                    stringBuilder.append(',');
                }
                stringBuilder.append("00,00");
                break;
            }
            case 1: {
                stringBuilder.append(this.hexadecimal(stringArray[0]));
            }
        }
        return stringBuilder.toString();
    }

    public String encode(TypeValuesPair typeValuesPair) {
        String string2 = null;
        if (typeValuesPair.getType() == Registry.Type.REG_SZ) {
            string2 = this.quote(typeValuesPair.getValues()[0]);
        } else if (typeValuesPair.getValues()[0] != null) {
            string2 = this.encode(typeValuesPair.getType(), typeValuesPair.getValues());
        }
        return string2;
    }

    String hexadecimal(String object) {
        StringBuilder stringBuilder = new StringBuilder();
        if (object != null && ((String)object).length() != 0) {
            object = this.string2bytes((String)object);
            for (int i = 0; i < ((Object)object).length; ++i) {
                stringBuilder.append(Character.forDigit((object[i] & 0xF0) >> 4, 16));
                stringBuilder.append(Character.forDigit(object[i] & 0xF, 16));
                stringBuilder.append(',');
            }
            stringBuilder.append("00,00");
        }
        return stringBuilder.toString();
    }

    Registry.Type type(String object) {
        int n;
        object = object.charAt(0) == '\"' ? Registry.Type.REG_SZ : ((n = object.indexOf(58)) < 0 ? Registry.Type.REG_SZ : Registry.Type.fromString(object.substring(0, n)));
        return object;
    }
}

