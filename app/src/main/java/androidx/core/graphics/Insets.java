/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Insets
 *  android.graphics.Rect
 */
package androidx.core.graphics;

import android.graphics.Rect;

public final class Insets {
    public static final Insets NONE = new Insets(0, 0, 0, 0);
    public final int bottom;
    public final int left;
    public final int right;
    public final int top;

    private Insets(int n, int n2, int n3, int n4) {
        this.left = n;
        this.top = n2;
        this.right = n3;
        this.bottom = n4;
    }

    public static Insets of(int n, int n2, int n3, int n4) {
        if (n == 0 && n2 == 0 && n3 == 0 && n4 == 0) {
            return NONE;
        }
        return new Insets(n, n2, n3, n4);
    }

    public static Insets of(Rect rect) {
        return Insets.of(rect.left, rect.top, rect.right, rect.bottom);
    }

    public static Insets toCompatInsets(android.graphics.Insets insets) {
        return Insets.of(insets.left, insets.top, insets.right, insets.bottom);
    }

    @Deprecated
    public static Insets wrap(android.graphics.Insets insets) {
        return Insets.toCompatInsets(insets);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (Insets)object;
            if (this.bottom != ((Insets)object).bottom) {
                return false;
            }
            if (this.left != ((Insets)object).left) {
                return false;
            }
            if (this.right != ((Insets)object).right) {
                return false;
            }
            return this.top == ((Insets)object).top;
        }
        return false;
    }

    public int hashCode() {
        return ((this.left * 31 + this.top) * 31 + this.right) * 31 + this.bottom;
    }

    public android.graphics.Insets toPlatformInsets() {
        return android.graphics.Insets.of((int)this.left, (int)this.top, (int)this.right, (int)this.bottom);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Insets{left=");
        stringBuilder.append(this.left);
        stringBuilder.append(", top=");
        stringBuilder.append(this.top);
        stringBuilder.append(", right=");
        stringBuilder.append(this.right);
        stringBuilder.append(", bottom=");
        stringBuilder.append(this.bottom);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

