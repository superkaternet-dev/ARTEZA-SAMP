/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import okhttp3.internal.Util;
import okhttp3.internal.http.HttpDate;

public final class Headers {
    private final String[] namesAndValues;

    private Headers(Builder builder) {
        this.namesAndValues = builder.namesAndValues.toArray(new String[builder.namesAndValues.size()]);
    }

    private Headers(String[] stringArray) {
        this.namesAndValues = stringArray;
    }

    private static String get(String[] stringArray, String string2) {
        for (int i = stringArray.length - 2; i >= 0; i -= 2) {
            if (!string2.equalsIgnoreCase(stringArray[i])) continue;
            return stringArray[i + 1];
        }
        return null;
    }

    public static Headers of(Map<String, String> object) {
        if (object != null) {
            Object object2 = new String[object.size() * 2];
            int n = 0;
            for (Map.Entry entry : object.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    object = ((String)entry.getKey()).trim();
                    entry = ((String)entry.getValue()).trim();
                    if (((String)object).length() != 0 && ((String)object).indexOf(0) == -1 && ((String)((Object)entry)).indexOf(0) == -1) {
                        object2[n] = object;
                        object2[n + 1] = entry;
                        n += 2;
                        continue;
                    }
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("Unexpected header: ");
                    ((StringBuilder)object2).append((String)object);
                    ((StringBuilder)object2).append(": ");
                    ((StringBuilder)object2).append((String)((Object)entry));
                    throw new IllegalArgumentException(((StringBuilder)object2).toString());
                }
                throw new IllegalArgumentException("Headers cannot be null");
            }
            return new Headers((String[])object2);
        }
        object = new NullPointerException("headers == null");
        throw object;
    }

    public static Headers of(String ... object) {
        if (object != null) {
            if (((String[])object).length % 2 == 0) {
                int n;
                Object object2 = (String[])object.clone();
                for (n = 0; n < ((String[])object2).length; ++n) {
                    if (object2[n] != null) {
                        object2[n] = object2[n].trim();
                        continue;
                    }
                    throw new IllegalArgumentException("Headers cannot be null");
                }
                for (n = 0; n < ((String[])object2).length; n += 2) {
                    String string2 = object2[n];
                    object = object2[n + 1];
                    if (string2.length() != 0 && string2.indexOf(0) == -1 && ((String)object).indexOf(0) == -1) {
                        continue;
                    }
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("Unexpected header: ");
                    ((StringBuilder)object2).append(string2);
                    ((StringBuilder)object2).append(": ");
                    ((StringBuilder)object2).append((String)object);
                    throw new IllegalArgumentException(((StringBuilder)object2).toString());
                }
                return new Headers((String[])object2);
            }
            throw new IllegalArgumentException("Expected alternating header names and values");
        }
        object = new NullPointerException("namesAndValues == null");
        throw object;
    }

    public boolean equals(Object object) {
        boolean bl = object instanceof Headers && Arrays.equals(((Headers)object).namesAndValues, this.namesAndValues);
        return bl;
    }

    public String get(String string2) {
        return Headers.get(this.namesAndValues, string2);
    }

    public Date getDate(String object) {
        object = (object = this.get((String)object)) != null ? HttpDate.parse((String)object) : null;
        return object;
    }

    public int hashCode() {
        return Arrays.hashCode(this.namesAndValues);
    }

    public String name(int n) {
        return this.namesAndValues[n * 2];
    }

    public Set<String> names() {
        TreeSet<String> treeSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            treeSet.add(this.name(i));
        }
        return Collections.unmodifiableSet(treeSet);
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        Collections.addAll(builder.namesAndValues, this.namesAndValues);
        return builder;
    }

    public int size() {
        return this.namesAndValues.length / 2;
    }

    public Map<String, List<String>> toMultimap() {
        LinkedHashMap<String, List<String>> linkedHashMap = new LinkedHashMap<String, List<String>>();
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            ArrayList<String> arrayList;
            String string2 = this.name(i).toLowerCase(Locale.US);
            ArrayList<String> arrayList2 = arrayList = (ArrayList<String>)linkedHashMap.get(string2);
            if (arrayList == null) {
                arrayList2 = new ArrayList<String>(2);
                linkedHashMap.put(string2, arrayList2);
            }
            arrayList2.add(this.value(i));
        }
        return linkedHashMap;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            stringBuilder.append(this.name(i));
            stringBuilder.append(": ");
            stringBuilder.append(this.value(i));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    public String value(int n) {
        return this.namesAndValues[n * 2 + 1];
    }

    public List<String> values(String list) {
        ArrayList<String> arrayList = null;
        int n = this.size();
        for (int i = 0; i < n; ++i) {
            ArrayList<String> arrayList2 = arrayList;
            if (((String)((Object)list)).equalsIgnoreCase(this.name(i))) {
                arrayList2 = arrayList;
                if (arrayList == null) {
                    arrayList2 = new ArrayList<String>(2);
                }
                arrayList2.add(this.value(i));
            }
            arrayList = arrayList2;
        }
        list = arrayList != null ? Collections.unmodifiableList(arrayList) : Collections.emptyList();
        return list;
    }

    public static final class Builder {
        private final List<String> namesAndValues = new ArrayList<String>(20);

        private void checkNameAndValue(String object, String string2) {
            if (object != null) {
                if (!((String)object).isEmpty()) {
                    char c;
                    int n;
                    int n2 = ((String)object).length();
                    for (n = 0; n < n2; ++n) {
                        c = ((String)object).charAt(n);
                        if (c > '\u001f' && c < '\u007f') {
                            continue;
                        }
                        throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", c, n, object));
                    }
                    if (string2 != null) {
                        n2 = string2.length();
                        for (n = 0; n < n2; ++n) {
                            c = string2.charAt(n);
                            if (c > '\u001f' && c < '\u007f') {
                                continue;
                            }
                            throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", c, n, object, string2));
                        }
                        return;
                    }
                    throw new NullPointerException("value == null");
                }
                throw new IllegalArgumentException("name is empty");
            }
            object = new NullPointerException("name == null");
            throw object;
        }

        public Builder add(String string2) {
            int n = string2.indexOf(":");
            if (n != -1) {
                return this.add(string2.substring(0, n).trim(), string2.substring(n + 1));
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected header: ");
            stringBuilder.append(string2);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public Builder add(String string2, String string3) {
            this.checkNameAndValue(string2, string3);
            return this.addLenient(string2, string3);
        }

        Builder addLenient(String string2) {
            int n = string2.indexOf(":", 1);
            if (n != -1) {
                return this.addLenient(string2.substring(0, n), string2.substring(n + 1));
            }
            if (string2.startsWith(":")) {
                return this.addLenient("", string2.substring(1));
            }
            return this.addLenient("", string2);
        }

        Builder addLenient(String string2, String string3) {
            this.namesAndValues.add(string2);
            this.namesAndValues.add(string3.trim());
            return this;
        }

        public Headers build() {
            return new Headers(this);
        }

        public String get(String string2) {
            for (int i = this.namesAndValues.size() - 2; i >= 0; i -= 2) {
                if (!string2.equalsIgnoreCase(this.namesAndValues.get(i))) continue;
                return this.namesAndValues.get(i + 1);
            }
            return null;
        }

        public Builder removeAll(String string2) {
            int n = 0;
            while (n < this.namesAndValues.size()) {
                int n2 = n;
                if (string2.equalsIgnoreCase(this.namesAndValues.get(n))) {
                    this.namesAndValues.remove(n);
                    this.namesAndValues.remove(n);
                    n2 = n - 2;
                }
                n = n2 + 2;
            }
            return this;
        }

        public Builder set(String string2, String string3) {
            this.checkNameAndValue(string2, string3);
            this.removeAll(string2);
            this.addLenient(string2, string3);
            return this;
        }
    }
}

