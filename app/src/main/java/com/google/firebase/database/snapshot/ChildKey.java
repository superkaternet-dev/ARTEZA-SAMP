/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.snapshot;

import com.google.firebase.database.core.utilities.Utilities;

public class ChildKey
implements Comparable<ChildKey> {
    private static final ChildKey INFO_CHILD_KEY;
    private static final ChildKey MAX_KEY;
    public static final String MAX_KEY_NAME = "[MAX_KEY]";
    private static final ChildKey MIN_KEY;
    public static final String MIN_KEY_NAME = "[MIN_NAME]";
    private static final ChildKey PRIORITY_CHILD_KEY;
    private final String key;

    static {
        MIN_KEY = new ChildKey(MIN_KEY_NAME);
        MAX_KEY = new ChildKey(MAX_KEY_NAME);
        PRIORITY_CHILD_KEY = new ChildKey(".priority");
        INFO_CHILD_KEY = new ChildKey(".info");
    }

    private ChildKey(String string2) {
        this.key = string2;
    }

    public static ChildKey fromString(String string2) {
        Integer n = Utilities.tryParseInt(string2);
        if (n != null) {
            return new IntegerChildKey(string2, n);
        }
        if (string2.equals(".priority")) {
            return PRIORITY_CHILD_KEY;
        }
        Utilities.hardAssert(string2.contains("/") ^ true);
        return new ChildKey(string2);
    }

    public static ChildKey getInfoKey() {
        return INFO_CHILD_KEY;
    }

    public static ChildKey getMaxName() {
        return MAX_KEY;
    }

    public static ChildKey getMinName() {
        return MIN_KEY;
    }

    public static ChildKey getPriorityKey() {
        return PRIORITY_CHILD_KEY;
    }

    public String asString() {
        return this.key;
    }

    @Override
    public int compareTo(ChildKey childKey) {
        if (this == childKey) {
            return 0;
        }
        if (!this.key.equals(MIN_KEY_NAME) && !childKey.key.equals(MAX_KEY_NAME)) {
            if (!childKey.key.equals(MIN_KEY_NAME) && !this.key.equals(MAX_KEY_NAME)) {
                if (this.isInt()) {
                    if (childKey.isInt()) {
                        int n = Utilities.compareInts(this.intValue(), childKey.intValue());
                        if (n == 0) {
                            n = Utilities.compareInts(this.key.length(), childKey.key.length());
                        }
                        return n;
                    }
                    return -1;
                }
                if (childKey.isInt()) {
                    return 1;
                }
                return this.key.compareTo(childKey.key);
            }
            return 1;
        }
        return -1;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ChildKey)) {
            return false;
        }
        if (this == object) {
            return true;
        }
        object = (ChildKey)object;
        return this.key.equals(((ChildKey)object).key);
    }

    public int hashCode() {
        return this.key.hashCode();
    }

    protected int intValue() {
        return 0;
    }

    protected boolean isInt() {
        return false;
    }

    public boolean isPriorityChildName() {
        return this.equals(PRIORITY_CHILD_KEY);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ChildKey(\"");
        stringBuilder.append(this.key);
        stringBuilder.append("\")");
        return stringBuilder.toString();
    }

    private static class IntegerChildKey
    extends ChildKey {
        private final int intValue;

        IntegerChildKey(String string2, int n) {
            super(string2);
            this.intValue = n;
        }

        @Override
        protected int intValue() {
            return this.intValue;
        }

        @Override
        protected boolean isInt() {
            return true;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("IntegerChildName(\"");
            stringBuilder.append(((ChildKey)this).key);
            stringBuilder.append("\")");
            return stringBuilder.toString();
        }
    }
}

