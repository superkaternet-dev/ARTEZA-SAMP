/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import org.ini4j.Profile;

public interface Registry
extends Profile {
    public static final char ESCAPE_CHAR = '\\';
    public static final Charset FILE_ENCODING = Charset.forName("UnicodeLittle");
    public static final char KEY_SEPARATOR = '\\';
    public static final String LINE_SEPARATOR = "\r\n";
    public static final char TYPE_SEPARATOR = ':';
    public static final String VERSION = "Windows Registry Editor Version 5.00";

    @Override
    public Key get(Object var1);

    @Override
    public Key get(Object var1, int var2);

    public String getVersion();

    @Override
    public Key put(String var1, Profile.Section var2);

    @Override
    public Key put(String var1, Profile.Section var2, int var3);

    @Override
    public Key remove(Object var1);

    @Override
    public Key remove(Object var1, int var2);

    public void setVersion(String var1);

    public static final class Hive
    extends Enum<Hive> {
        private static final Hive[] $VALUES;
        public static final /* enum */ Hive HKEY_CLASSES_ROOT;
        public static final /* enum */ Hive HKEY_CURRENT_CONFIG;
        public static final /* enum */ Hive HKEY_CURRENT_USER;
        public static final /* enum */ Hive HKEY_LOCAL_MACHINE;
        public static final /* enum */ Hive HKEY_USERS;

        static {
            Hive hive;
            Hive hive2;
            Hive hive3;
            Hive hive4;
            Hive hive5;
            HKEY_CLASSES_ROOT = hive5 = new Hive();
            HKEY_CURRENT_CONFIG = hive4 = new Hive();
            HKEY_CURRENT_USER = hive3 = new Hive();
            HKEY_LOCAL_MACHINE = hive2 = new Hive();
            HKEY_USERS = hive = new Hive();
            $VALUES = new Hive[]{hive5, hive4, hive3, hive2, hive};
        }

        public static Hive valueOf(String string2) {
            return Enum.valueOf(Hive.class, string2);
        }

        public static Hive[] values() {
            return (Hive[])$VALUES.clone();
        }
    }

    public static interface Key
    extends Profile.Section {
        public static final String DEFAULT_NAME = "@";

        @Override
        public Key addChild(String var1);

        @Override
        public Key getChild(String var1);

        @Override
        public Key getParent();

        public Type getType(Object var1);

        public Type getType(Object var1, Type var2);

        @Override
        public Key lookup(String ... var1);

        public Type putType(String var1, Type var2);

        public Type removeType(Object var1);
    }

    public static final class Type
    extends Enum<Type> {
        private static final Type[] $VALUES;
        private static final Map<String, Type> MAPPING;
        public static final /* enum */ Type REG_BINARY;
        public static final /* enum */ Type REG_DWORD;
        public static final /* enum */ Type REG_DWORD_BIG_ENDIAN;
        public static final /* enum */ Type REG_EXPAND_SZ;
        public static final /* enum */ Type REG_FULL_RESOURCE_DESCRIPTOR;
        public static final /* enum */ Type REG_LINK;
        public static final /* enum */ Type REG_MULTI_SZ;
        public static final /* enum */ Type REG_NONE;
        public static final /* enum */ Type REG_QWORD;
        public static final /* enum */ Type REG_RESOURCE_LIST;
        public static final /* enum */ Type REG_RESOURCE_REQUIREMENTS_LIST;
        public static final /* enum */ Type REG_SZ;
        public static final String REMOVE;
        public static final char REMOVE_CHAR = '-';
        public static final String SEPARATOR;
        public static final char SEPARATOR_CHAR = ':';
        private final String _prefix;

        static {
            Type type;
            Type type2;
            Type type3;
            Type type4;
            Type type5;
            Type type6;
            Type type7;
            Type type82;
            Type type9;
            Type type10;
            Type type11;
            REG_NONE = type11 = new Type("hex(0)");
            REG_SZ = type10 = new Type("");
            REG_EXPAND_SZ = type9 = new Type("hex(2)");
            Type[] typeArray = new Type("hex");
            REG_BINARY = typeArray;
            REG_DWORD = type82 = new Type("dword");
            REG_DWORD_BIG_ENDIAN = type7 = new Type("hex(5)");
            REG_LINK = type6 = new Type("hex(6)");
            REG_MULTI_SZ = type5 = new Type("hex(7)");
            REG_RESOURCE_LIST = type4 = new Type("hex(8)");
            REG_FULL_RESOURCE_DESCRIPTOR = type3 = new Type("hex(9)");
            REG_RESOURCE_REQUIREMENTS_LIST = type2 = new Type("hex(a)");
            REG_QWORD = type = new Type("hex(b)");
            $VALUES = new Type[]{type11, type10, type9, typeArray, type82, type7, type6, type5, type4, type3, type2, type};
            MAPPING = new HashMap<String, Type>();
            for (Type type82 : Type.values()) {
                MAPPING.put(type82.toString(), type82);
            }
            SEPARATOR = String.valueOf(':');
            REMOVE = String.valueOf('-');
        }

        private Type(String string3) {
            this._prefix = string3;
        }

        public static Type fromString(String string2) {
            return MAPPING.get(string2);
        }

        public static Type valueOf(String string2) {
            return Enum.valueOf(Type.class, string2);
        }

        public static Type[] values() {
            return (Type[])$VALUES.clone();
        }

        public String toString() {
            return this._prefix;
        }
    }
}

