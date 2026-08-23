/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.tls;

import java.io.Serializable;
import javax.security.auth.x500.X500Principal;

final class DistinguishedNameParser {
    private int beg;
    private char[] chars;
    private int cur;
    private final String dn;
    private int end;
    private final int length;
    private int pos;

    public DistinguishedNameParser(X500Principal object) {
        this.dn = object = ((X500Principal)object).getName("RFC2253");
        this.length = ((String)object).length();
    }

    private String escapedAV() {
        char[] cArray;
        int n;
        this.beg = n = this.pos;
        this.end = n;
        block5: while (true) {
            int n2;
            if ((n = this.pos) >= this.length) {
                cArray = this.chars;
                n = this.beg;
                return new String(cArray, n, this.end - n);
            }
            cArray = this.chars;
            switch (cArray[n]) {
                default: {
                    n2 = this.end;
                    this.end = n2 + 1;
                    cArray[n2] = cArray[n];
                    this.pos = n + 1;
                    continue block5;
                }
                case '\\': {
                    n = this.end;
                    this.end = n + 1;
                    cArray[n] = this.getEscaped();
                    ++this.pos;
                    continue block5;
                }
                case '+': 
                case ',': 
                case ';': {
                    n = this.beg;
                    return new String(cArray, n, this.end - n);
                }
                case ' ': 
            }
            this.cur = n2 = this.end;
            this.pos = n + 1;
            this.end = n2 + 1;
            cArray[n2] = 32;
            while ((n = this.pos) < (n2 = this.length) && (cArray = this.chars)[n] == ' ') {
                n2 = this.end;
                this.end = n2 + 1;
                cArray[n2] = 32;
                this.pos = n + 1;
            }
            if (n == n2 || (cArray = this.chars)[n] == ',' || cArray[n] == '+' || cArray[n] == ';') break;
        }
        cArray = this.chars;
        n = this.beg;
        return new String(cArray, n, this.cur - n);
    }

    private int getByte(int n) {
        block2: {
            Object object;
            block6: {
                block10: {
                    int n2;
                    block8: {
                        block9: {
                            block7: {
                                block4: {
                                    block5: {
                                        block3: {
                                            if (n + 1 >= this.length) break block2;
                                            object = this.chars;
                                            n2 = object[n];
                                            if (n2 < 48 || n2 > 57) break block3;
                                            n2 -= 48;
                                            break block4;
                                        }
                                        if (n2 < 97 || n2 > 102) break block5;
                                        n2 -= 87;
                                        break block4;
                                    }
                                    if (n2 < 65 || n2 > 70) break block6;
                                    n2 -= 55;
                                }
                                n = object[n + 1];
                                if (n < 48 || n > 57) break block7;
                                n -= 48;
                                break block8;
                            }
                            if (n < 97 || n > 102) break block9;
                            n -= 87;
                            break block8;
                        }
                        if (n < 65 || n > 70) break block10;
                        n -= 55;
                    }
                    return (n2 << 4) + n;
                }
                object = new StringBuilder();
                object.append("Malformed DN: ");
                object.append(this.dn);
                throw new IllegalStateException(object.toString());
            }
            object = new StringBuilder();
            object.append("Malformed DN: ");
            object.append(this.dn);
            throw new IllegalStateException(object.toString());
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Malformed DN: ");
        stringBuilder.append(this.dn);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private char getEscaped() {
        int n;
        this.pos = n = this.pos + 1;
        if (n != this.length) {
            char[] cArray = this.chars;
            switch (cArray[n]) {
                default: {
                    return this.getUTF8();
                }
                case ' ': 
                case '\"': 
                case '#': 
                case '%': 
                case '*': 
                case '+': 
                case ',': 
                case ';': 
                case '<': 
                case '=': 
                case '>': 
                case '\\': 
                case '_': 
            }
            return cArray[n];
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unexpected end of DN: ");
        stringBuilder.append(this.dn);
        throw new IllegalStateException(stringBuilder.toString());
    }

    private char getUTF8() {
        int n = this.getByte(this.pos);
        ++this.pos;
        if (n < 128) {
            return (char)n;
        }
        if (n >= 192 && n <= 247) {
            int n2;
            if (n <= 223) {
                n2 = 1;
                n &= 0x1F;
            } else if (n <= 239) {
                n2 = 2;
                n &= 0xF;
            } else {
                n2 = 3;
                n &= 7;
            }
            for (int i = 0; i < n2; ++i) {
                int n3;
                this.pos = n3 = this.pos + 1;
                if (n3 != this.length && this.chars[n3] == '\\') {
                    this.pos = ++n3;
                    n3 = this.getByte(n3);
                    ++this.pos;
                    if ((n3 & 0xC0) != 128) {
                        return '?';
                    }
                    n = (n << 6) + (n3 & 0x3F);
                    continue;
                }
                return '?';
            }
            return (char)n;
        }
        return '?';
    }

    private String hexAV() {
        int n = this.pos;
        if (n + 4 < this.length) {
            int n2;
            int n3;
            Object object;
            block7: {
                this.beg = n;
                this.pos = n + 1;
                while ((n = this.pos) != this.length && (object = this.chars)[n] != '+' && object[n] != ',' && object[n] != ';') {
                    if (object[n] == ' ') {
                        this.end = n;
                        this.pos = n + 1;
                        while ((n = this.pos) < this.length && this.chars[n] == ' ') {
                            this.pos = n + 1;
                        }
                        break block7;
                    }
                    if (object[n] >= 65 && object[n] <= 70) {
                        object[n] = (char)(object[n] + 32);
                    }
                    this.pos = n + 1;
                }
                this.end = n;
            }
            if ((n3 = (n = this.end) - (n2 = this.beg)) >= 5 && (n3 & 1) != 0) {
                object = new byte[n3 / 2];
                ++n2;
                for (n = 0; n < ((Object)object).length; ++n) {
                    object[n] = (byte)this.getByte(n2);
                    n2 += 2;
                }
                return new String(this.chars, this.beg, n3);
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Unexpected end of DN: ");
            ((StringBuilder)object).append(this.dn);
            throw new IllegalStateException(((StringBuilder)object).toString());
        }
        Serializable serializable = new StringBuilder();
        serializable.append("Unexpected end of DN: ");
        serializable.append(this.dn);
        serializable = new IllegalStateException(serializable.toString());
        throw serializable;
    }

    private String nextAT() {
        Object object;
        int n;
        int n2;
        while ((n2 = this.pos) < (n = this.length) && this.chars[n2] == ' ') {
            this.pos = n2 + 1;
        }
        if (n2 == n) {
            return null;
        }
        this.beg = n2;
        this.pos = n2 + 1;
        while ((n = this.pos) < (n2 = this.length) && (object = this.chars)[n] != '=' && object[n] != 32) {
            this.pos = n + 1;
        }
        if (n < n2) {
            this.end = n;
            if (this.chars[n] == ' ') {
                while ((n = this.pos) < (n2 = this.length) && (object = (Object)this.chars)[n] != '=' && object[n] == ' ') {
                    this.pos = n + 1;
                }
                if (this.chars[n] != '=' || n == n2) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Unexpected end of DN: ");
                    ((StringBuilder)object).append(this.dn);
                    throw new IllegalStateException(((StringBuilder)object).toString());
                }
            }
            ++this.pos;
            while ((n2 = this.pos) < this.length && this.chars[n2] == ' ') {
                this.pos = n2 + 1;
            }
            n2 = this.end;
            n = this.beg;
            if (!(n2 - n <= 4 || (object = (Object)this.chars)[n + 3] != 46 || object[n] != 79 && object[n] != 111 || object[n + 1] != 73 && object[n + 1] != 105 || object[n + 2] != 68 && object[n + 2] != 100)) {
                this.beg = n + 4;
            }
            object = this.chars;
            n = this.beg;
            return new String((char[])object, n, n2 - n);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Unexpected end of DN: ");
        ((StringBuilder)object).append(this.dn);
        object = new IllegalStateException(((StringBuilder)object).toString());
        throw object;
    }

    private String quotedAV() {
        Object object;
        int n;
        this.pos = n = this.pos + 1;
        this.beg = n;
        this.end = n;
        while ((n = ++this.pos) != this.length) {
            object = this.chars;
            if (object[n] == '\"') {
                this.pos = n + 1;
                while ((n = this.pos) < this.length && this.chars[n] == ' ') {
                    this.pos = n + 1;
                }
                object = this.chars;
                n = this.beg;
                return new String((char[])object, n, this.end - n);
            }
            object[this.end] = object[n] == 92 ? (Object)this.getEscaped() : object[n];
            ++this.end;
        }
        object = new StringBuilder();
        object.append("Unexpected end of DN: ");
        object.append(this.dn);
        object = new IllegalStateException(object.toString());
        throw object;
    }

    public String findMostSpecific(String object) {
        this.pos = 0;
        this.beg = 0;
        this.end = 0;
        this.cur = 0;
        this.chars = this.dn.toCharArray();
        Object object2 = this.nextAT();
        String string2 = object2;
        if (object2 == null) {
            return null;
        }
        do {
            object2 = "";
            int n = this.pos;
            if (n == this.length) {
                return null;
            }
            switch (this.chars[n]) {
                default: {
                    object2 = this.escapedAV();
                    break;
                }
                case '+': 
                case ',': 
                case ';': {
                    break;
                }
                case '#': {
                    object2 = this.hexAV();
                    break;
                }
                case '\"': {
                    object2 = this.quotedAV();
                }
            }
            if (((String)object).equalsIgnoreCase(string2)) {
                return object2;
            }
            n = this.pos;
            if (n >= this.length) {
                return null;
            }
            object2 = this.chars;
            if (object2[n] != 44 && object2[n] != 59 && object2[n] != 43) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Malformed DN: ");
                ((StringBuilder)object).append(this.dn);
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
            this.pos = n + 1;
        } while ((string2 = this.nextAT()) != null);
        object = new StringBuilder();
        ((StringBuilder)object).append("Malformed DN: ");
        ((StringBuilder)object).append(this.dn);
        object = new IllegalStateException(((StringBuilder)object).toString());
        throw object;
    }
}

