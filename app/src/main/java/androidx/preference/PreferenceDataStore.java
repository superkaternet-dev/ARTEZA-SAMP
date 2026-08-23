/*
 * Decompiled with CFR 0.152.
 */
package androidx.preference;

import java.util.Set;

public abstract class PreferenceDataStore {
    public boolean getBoolean(String string2, boolean bl) {
        return bl;
    }

    public float getFloat(String string2, float f) {
        return f;
    }

    public int getInt(String string2, int n) {
        return n;
    }

    public long getLong(String string2, long l) {
        return l;
    }

    public String getString(String string2, String string3) {
        return string3;
    }

    public Set<String> getStringSet(String string2, Set<String> set) {
        return set;
    }

    public void putBoolean(String string2, boolean bl) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putFloat(String string2, float f) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putInt(String string2, int n) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putLong(String string2, long l) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putString(String string2, String string3) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }

    public void putStringSet(String string2, Set<String> set) {
        throw new UnsupportedOperationException("Not implemented on this data store");
    }
}

