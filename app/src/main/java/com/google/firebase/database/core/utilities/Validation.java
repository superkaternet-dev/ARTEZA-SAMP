/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core.utilities;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.core.ValidationPath;
import com.google.firebase.database.core.utilities.Utilities;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.database.snapshot.Node;
import com.google.firebase.database.snapshot.NodeUtilities;
import com.google.firebase.database.snapshot.PriorityUtilities;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Validation {
    private static final Pattern INVALID_KEY_REGEX;
    private static final Pattern INVALID_PATH_REGEX;

    static {
        INVALID_PATH_REGEX = Pattern.compile("[\\[\\]\\.#$]");
        INVALID_KEY_REGEX = Pattern.compile("[\\[\\]\\.#\\$\\/\\u0000-\\u001F\\u007F]");
    }

    private static boolean isValidKey(String string2) {
        boolean bl = string2.equals(".info") || !INVALID_KEY_REGEX.matcher(string2).find() || string2.equals(ChildKey.getMaxName().asString()) || string2.equals(ChildKey.getMinName().asString());
        return bl;
    }

    private static boolean isValidPathString(String string2) {
        return INVALID_PATH_REGEX.matcher(string2).find() ^ true;
    }

    private static boolean isWritableKey(String string2) {
        boolean bl = string2 != null && string2.length() > 0 && (string2.equals(".value") || string2.equals(".priority") || !string2.startsWith(".") && !INVALID_KEY_REGEX.matcher(string2).find());
        return bl;
    }

    private static boolean isWritablePath(Path comparable) {
        boolean bl = (comparable = ((Path)comparable).getFront()) == null || !((ChildKey)comparable).asString().startsWith(".");
        return bl;
    }

    /*
     * WARNING - void declaration
     */
    public static Map<Path, Node> parseAndValidateUpdate(Path object, Map<String, Object> object22) throws DatabaseException {
        Serializable serializable = new TreeMap<Path, Node>();
        for (Map.Entry entry : object22.entrySet()) {
            void var1_6;
            Path path = new Path((String)entry.getKey());
            Object v = entry.getValue();
            ValidationPath.validateWithObject(((Path)object).child(path), v);
            if (!path.isEmpty()) {
                String string2 = path.getBack().asString();
            } else {
                String string3 = "";
            }
            if (!var1_6.equals(".sv") && !var1_6.equals(".value")) {
                void var1_9;
                if (var1_6.equals(".priority")) {
                    Node node = PriorityUtilities.parsePriority(path, v);
                } else {
                    Node node = NodeUtilities.NodeFromJSON(v);
                }
                Validation.validateWritableObject(v);
                serializable.put(path, var1_9);
                continue;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Path '");
            ((StringBuilder)object).append(path);
            ((StringBuilder)object).append("' contains disallowed child name: ");
            ((StringBuilder)object).append((String)var1_6);
            throw new DatabaseException(((StringBuilder)object).toString());
        }
        object = null;
        for (Path path : serializable.keySet()) {
            boolean bl = object == null || ((Path)object).compareTo(path) < 0;
            Utilities.hardAssert(bl);
            if (object != null && ((Path)object).contains(path)) {
                serializable = new StringBuilder();
                ((StringBuilder)serializable).append("Path '");
                ((StringBuilder)serializable).append(object);
                ((StringBuilder)serializable).append("' is an ancestor of '");
                ((StringBuilder)serializable).append(path);
                ((StringBuilder)serializable).append("' in an update.");
                throw new DatabaseException(((StringBuilder)serializable).toString());
            }
            object = path;
        }
        return serializable;
    }

    private static void validateDoubleValue(double d) {
        if (!Double.isInfinite(d) && !Double.isNaN(d)) {
            return;
        }
        throw new DatabaseException("Invalid value: Value cannot be NaN, Inf or -Inf.");
    }

    public static void validateNullableKey(String string2) throws DatabaseException {
        if (string2 != null && !Validation.isValidKey(string2)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Invalid key: ");
            stringBuilder.append(string2);
            stringBuilder.append(". Keys must not contain '/', '.', '#', '$', '[', or ']'");
            throw new DatabaseException(stringBuilder.toString());
        }
    }

    public static void validatePathString(String string2) throws DatabaseException {
        if (Validation.isValidPathString(string2)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid Firebase Database path: ");
        stringBuilder.append(string2);
        stringBuilder.append(". Firebase Database paths must not contain '.', '#', '$', '[', or ']'");
        throw new DatabaseException(stringBuilder.toString());
    }

    public static void validateRootPathString(String string2) throws DatabaseException {
        if (string2.startsWith(".info")) {
            Validation.validatePathString(string2.substring(5));
        } else if (string2.startsWith("/.info")) {
            Validation.validatePathString(string2.substring(6));
        } else {
            Validation.validatePathString(string2);
        }
    }

    public static void validateWritableKey(String string2) throws DatabaseException {
        if (Validation.isWritableKey(string2)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid key: ");
        stringBuilder.append(string2);
        stringBuilder.append(". Keys must not contain '/', '.', '#', '$', '[', or ']'");
        throw new DatabaseException(stringBuilder.toString());
    }

    public static void validateWritableObject(Object object2) {
        if (object2 instanceof Map) {
            Map map = (Map)object2;
            if (map.containsKey(".sv")) {
                return;
            }
            for (Map.Entry entry : map.entrySet()) {
                Validation.validateWritableKey((String)entry.getKey());
                Validation.validateWritableObject(entry.getValue());
            }
        } else if (object2 instanceof List) {
            Iterator iterator2 = ((List)object2).iterator();
            while (iterator2.hasNext()) {
                Validation.validateWritableObject(iterator2.next());
            }
        } else if (object2 instanceof Double || object2 instanceof Float) {
            Validation.validateDoubleValue((Double)object2);
        }
    }

    public static void validateWritablePath(Path path) throws DatabaseException {
        if (Validation.isWritablePath(path)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid write location: ");
        stringBuilder.append(path.toString());
        throw new DatabaseException(stringBuilder.toString());
    }
}

