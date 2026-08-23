/*
 * Decompiled with CFR 0.152.
 */
package com.google.android.gms.common.images;

public final class Size {
    private final int zaa;
    private final int zab;

    public Size(int n, int n2) {
        this.zaa = n;
        this.zab = n2;
    }

    public static Size parseSize(String string2) throws NumberFormatException {
        if (string2 != null) {
            int n;
            int n2 = n = string2.indexOf(42);
            if (n < 0) {
                n2 = string2.indexOf(120);
            }
            if (n2 >= 0) {
                try {
                    Size size = new Size(Integer.parseInt(string2.substring(0, n2)), Integer.parseInt(string2.substring(n2 + 1)));
                    return size;
                }
                catch (NumberFormatException numberFormatException) {
                    throw Size.zaa(string2);
                }
            }
            throw Size.zaa(string2);
        }
        throw new IllegalArgumentException("string must not be null");
    }

    private static NumberFormatException zaa(String string2) {
        StringBuilder stringBuilder = new StringBuilder(string2.length() + 16);
        stringBuilder.append("Invalid Size: \"");
        stringBuilder.append(string2);
        stringBuilder.append("\"");
        throw new NumberFormatException(stringBuilder.toString());
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (object instanceof Size) {
            object = (Size)object;
            if (this.zaa == ((Size)object).zaa && this.zab == ((Size)object).zab) {
                return true;
            }
        }
        return false;
    }

    public int getHeight() {
        return this.zab;
    }

    public int getWidth() {
        return this.zaa;
    }

    public int hashCode() {
        int n = this.zab;
        int n2 = this.zaa;
        return n ^ (n2 >>> 16 | n2 << 16);
    }

    public String toString() {
        int n = this.zaa;
        int n2 = this.zab;
        StringBuilder stringBuilder = new StringBuilder(23);
        stringBuilder.append(n);
        stringBuilder.append("x");
        stringBuilder.append(n2);
        return stringBuilder.toString();
    }
}

