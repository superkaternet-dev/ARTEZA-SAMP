/*
 * Decompiled with CFR 0.152.
 */
package br.com.sapereaude.maskedEditText;

import br.com.sapereaude.maskedEditText.Range;

public class RawText {
    private String text = "";

    public int addToString(String string2, int n, int n2) {
        String string3 = "";
        String string4 = "";
        if (string2 != null && !string2.equals("")) {
            if (n >= 0) {
                if (n <= this.text.length()) {
                    int n3 = string2.length();
                    if (n > 0) {
                        string3 = this.text.substring(0, n);
                    }
                    String string5 = string4;
                    if (n >= 0) {
                        string5 = string4;
                        if (n < this.text.length()) {
                            string5 = this.text;
                            string5 = string5.substring(n, string5.length());
                        }
                    }
                    n = n3;
                    string4 = string2;
                    if (this.text.length() + string2.length() > n2) {
                        n = n2 - this.text.length();
                        string4 = string2.substring(0, n);
                    }
                    this.text = string3.concat(string4).concat(string5);
                    return n;
                }
                throw new IllegalArgumentException("Start position must be less than the actual text length");
            }
            throw new IllegalArgumentException("Start position must be non-negative");
        }
        return 0;
    }

    public char charAt(int n) {
        return this.text.charAt(n);
    }

    public String getText() {
        return this.text;
    }

    public int length() {
        return this.text.length();
    }

    public void subtractFromString(Range range) {
        String string2 = "";
        String string3 = "";
        String string4 = string2;
        if (range.getStart() > 0) {
            string4 = string2;
            if (range.getStart() <= this.text.length()) {
                string4 = this.text.substring(0, range.getStart());
            }
        }
        string2 = string3;
        if (range.getEnd() >= 0) {
            string2 = string3;
            if (range.getEnd() < this.text.length()) {
                string2 = this.text.substring(range.getEnd(), this.text.length());
            }
        }
        this.text = string4.concat(string2);
    }
}

