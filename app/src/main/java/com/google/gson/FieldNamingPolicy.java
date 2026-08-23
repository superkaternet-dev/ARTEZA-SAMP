/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.FieldNamingStrategy;
import java.lang.reflect.Field;
import java.util.Locale;

public abstract class FieldNamingPolicy
extends Enum<FieldNamingPolicy>
implements FieldNamingStrategy {
    private static final FieldNamingPolicy[] $VALUES;
    public static final /* enum */ FieldNamingPolicy IDENTITY;
    public static final /* enum */ FieldNamingPolicy LOWER_CASE_WITH_DASHES;
    public static final /* enum */ FieldNamingPolicy LOWER_CASE_WITH_UNDERSCORES;
    public static final /* enum */ FieldNamingPolicy UPPER_CAMEL_CASE;
    public static final /* enum */ FieldNamingPolicy UPPER_CAMEL_CASE_WITH_SPACES;

    static {
        FieldNamingPolicy fieldNamingPolicy;
        FieldNamingPolicy fieldNamingPolicy2;
        FieldNamingPolicy fieldNamingPolicy3;
        FieldNamingPolicy fieldNamingPolicy4;
        FieldNamingPolicy fieldNamingPolicy5;
        IDENTITY = fieldNamingPolicy5 = new FieldNamingPolicy(){

            @Override
            public String translateName(Field field) {
                return field.getName();
            }
        };
        UPPER_CAMEL_CASE = fieldNamingPolicy4 = new FieldNamingPolicy(){

            @Override
            public String translateName(Field field) {
                return 2.upperCaseFirstLetter(field.getName());
            }
        };
        UPPER_CAMEL_CASE_WITH_SPACES = fieldNamingPolicy3 = new FieldNamingPolicy(){

            @Override
            public String translateName(Field field) {
                return 3.upperCaseFirstLetter(3.separateCamelCase(field.getName(), " "));
            }
        };
        LOWER_CASE_WITH_UNDERSCORES = fieldNamingPolicy2 = new FieldNamingPolicy(){

            @Override
            public String translateName(Field field) {
                return 4.separateCamelCase(field.getName(), "_").toLowerCase(Locale.ENGLISH);
            }
        };
        LOWER_CASE_WITH_DASHES = fieldNamingPolicy = new FieldNamingPolicy(){

            @Override
            public String translateName(Field field) {
                return 5.separateCamelCase(field.getName(), "-").toLowerCase(Locale.ENGLISH);
            }
        };
        $VALUES = new FieldNamingPolicy[]{fieldNamingPolicy5, fieldNamingPolicy4, fieldNamingPolicy3, fieldNamingPolicy2, fieldNamingPolicy};
    }

    private static String modifyString(char c, String string2, int n) {
        if (n < string2.length()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(c);
            stringBuilder.append(string2.substring(n));
            string2 = stringBuilder.toString();
        } else {
            string2 = String.valueOf(c);
        }
        return string2;
    }

    static String separateCamelCase(String string2, String string3) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < string2.length(); ++i) {
            char c = string2.charAt(i);
            if (Character.isUpperCase(c) && stringBuilder.length() != 0) {
                stringBuilder.append(string3);
            }
            stringBuilder.append(c);
        }
        return stringBuilder.toString();
    }

    static String upperCaseFirstLetter(String string2) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        char c = string2.charAt(0);
        while (n < string2.length() - 1 && !Character.isLetter(c)) {
            stringBuilder.append(c);
            c = string2.charAt(++n);
        }
        if (n == string2.length()) {
            return stringBuilder.toString();
        }
        if (!Character.isUpperCase(c)) {
            stringBuilder.append(FieldNamingPolicy.modifyString(Character.toUpperCase(c), string2, n + 1));
            return stringBuilder.toString();
        }
        return string2;
    }

    public static FieldNamingPolicy valueOf(String string2) {
        return Enum.valueOf(FieldNamingPolicy.class, string2);
    }

    public static FieldNamingPolicy[] values() {
        return (FieldNamingPolicy[])$VALUES.clone();
    }
}

