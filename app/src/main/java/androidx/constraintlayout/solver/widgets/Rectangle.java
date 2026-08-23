/*
 * Decompiled with CFR 0.152.
 */
package androidx.constraintlayout.solver.widgets;

public class Rectangle {
    public int height;
    public int width;
    public int x;
    public int y;

    public boolean contains(int n, int n2) {
        int n3 = this.x;
        boolean bl = n >= n3 && n < n3 + this.width && n2 >= (n = this.y) && n2 < n + this.height;
        return bl;
    }

    public int getCenterX() {
        return (this.x + this.width) / 2;
    }

    public int getCenterY() {
        return (this.y + this.height) / 2;
    }

    void grow(int n, int n2) {
        this.x -= n;
        this.y -= n2;
        this.width += n * 2;
        this.height += n2 * 2;
    }

    boolean intersects(Rectangle rectangle) {
        int n = this.x;
        int n2 = rectangle.x;
        boolean bl = n >= n2 && n < n2 + rectangle.width && (n2 = this.y) >= (n = rectangle.y) && n2 < n + rectangle.height;
        return bl;
    }

    public void setBounds(int n, int n2, int n3, int n4) {
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
    }
}

