/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.core.Path;
import com.google.firebase.database.snapshot.ChildKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ValidationPath {
    public static final int MAX_PATH_DEPTH = 32;
    public static final int MAX_PATH_LENGTH_BYTES = 768;
    private int byteLength = 0;
    private final List<String> parts = new ArrayList<String>();

    private ValidationPath(Path object) throws DatabaseException {
        object = ((Path)object).iterator();
        while (object.hasNext()) {
            ChildKey childKey = (ChildKey)object.next();
            this.parts.add(childKey.asString());
        }
        this.byteLength = Math.max(1, this.parts.size());
        for (int i = 0; i < this.parts.size(); ++i) {
            this.byteLength += ValidationPath.utf8Bytes(this.parts.get(i));
        }
        this.checkValid();
    }

    private void checkValid() throws DatabaseException {
        if (this.byteLength <= 768) {
            if (this.parts.size() <= 32) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Path specified exceeds the maximum depth that can be written (32) or object contains a cycle ");
            stringBuilder.append(this.toErrorString());
            throw new DatabaseException(stringBuilder.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Data has a key path longer than 768 bytes (");
        stringBuilder.append(this.byteLength);
        stringBuilder.append(").");
        throw new DatabaseException(stringBuilder.toString());
    }

    private static String joinStringList(String string2, List<String> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); ++i) {
            if (i > 0) {
                stringBuilder.append(string2);
            }
            stringBuilder.append(list.get(i));
        }
        return stringBuilder.toString();
    }

    private String pop() {
        Object object = this.parts;
        object = object.remove(object.size() - 1);
        this.byteLength -= ValidationPath.utf8Bytes((CharSequence)object);
        if (this.parts.size() > 0) {
            --this.byteLength;
        }
        return object;
    }

    private void push(String string2) throws DatabaseException {
        if (this.parts.size() > 0) {
            ++this.byteLength;
        }
        this.parts.add(string2);
        this.byteLength += ValidationPath.utf8Bytes(string2);
        this.checkValid();
    }

    private String toErrorString() {
        if (this.parts.size() == 0) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("in path '");
        stringBuilder.append(ValidationPath.joinStringList("/", this.parts));
        stringBuilder.append("'");
        return stringBuilder.toString();
    }

    private static int utf8Bytes(CharSequence charSequence) {
        int n = 0;
        int n2 = charSequence.length();
        for (int i = 0; i < n2; ++i) {
            char c = charSequence.charAt(i);
            if (c <= '\u007f') {
                ++n;
                continue;
            }
            if (c <= '\u07ff') {
                n += 2;
                continue;
            }
            if (Character.isHighSurrogate(c)) {
                n += 4;
                ++i;
                continue;
            }
            n += 3;
        }
        return n;
    }

    public static void validateWithObject(Path path, Object object) throws DatabaseException {
        new ValidationPath(path).withObject(object);
    }

    private void withObject(Object object) throws DatabaseException {
        if (object instanceof Map) {
            object = (Map)object;
            for (String string2 : object.keySet()) {
                if (string2.startsWith(".")) continue;
                this.push(string2);
                this.withObject(object.get(string2));
                this.pop();
            }
            return;
        }
        if (object instanceof List) {
            object = (List)object;
            for (int i = 0; i < object.size(); ++i) {
                this.push(Integer.toString(i));
                this.withObject(object.get(i));
                this.pop();
            }
        }
    }
}

