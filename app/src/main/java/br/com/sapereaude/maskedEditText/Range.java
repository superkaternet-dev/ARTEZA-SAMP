/*
 * Decompiled with CFR 0.152.
 */
package br.com.sapereaude.maskedEditText;

public class Range {
    private int end = -1;
    private int start = -1;

    Range() {
    }

    public int getEnd() {
        return this.end;
    }

    public int getStart() {
        return this.start;
    }

    public void setEnd(int n) {
        this.end = n;
    }

    public void setStart(int n) {
        this.start = n;
    }
}

