/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonReader
implements Closeable {
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_EOF = 17;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private final char[] buffer = new char[1024];
    private final Reader in;
    private boolean lenient = false;
    private int limit = 0;
    private int lineNumber = 0;
    private int lineStart = 0;
    private int[] pathIndices;
    private String[] pathNames;
    int peeked = 0;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int pos = 0;
    private int[] stack;
    private int stackSize;

    static {
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess(){

            @Override
            public void promoteNameToValue(JsonReader jsonReader) throws IOException {
                block8: {
                    block6: {
                        int n;
                        block7: {
                            block5: {
                                int n2;
                                if (jsonReader instanceof JsonTreeReader) {
                                    ((JsonTreeReader)jsonReader).promoteNameToValue();
                                    return;
                                }
                                n = n2 = jsonReader.peeked;
                                if (n2 == 0) {
                                    n = jsonReader.doPeek();
                                }
                                if (n != 13) break block5;
                                jsonReader.peeked = 9;
                                break block6;
                            }
                            if (n != 12) break block7;
                            jsonReader.peeked = 8;
                            break block6;
                        }
                        if (n != 14) break block8;
                        jsonReader.peeked = 10;
                    }
                    return;
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Expected a name but was ");
                stringBuilder.append((Object)jsonReader.peek());
                stringBuilder.append(jsonReader.locationString());
                throw new IllegalStateException(stringBuilder.toString());
            }
        };
    }

    public JsonReader(Reader reader) {
        int[] nArray = new int[32];
        this.stack = nArray;
        this.stackSize = 0;
        this.stackSize = 0 + 1;
        nArray[0] = 6;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        if (reader != null) {
            this.in = reader;
            return;
        }
        throw new NullPointerException("in == null");
    }

    private void checkLenient() throws IOException {
        if (this.lenient) {
            return;
        }
        throw this.syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
    }

    private void consumeNonExecutePrefix() throws IOException {
        int n;
        this.nextNonWhitespace(true);
        this.pos = n = this.pos - 1;
        char[] cArray = NON_EXECUTE_PREFIX;
        if (n + cArray.length > this.limit && !this.fillBuffer(cArray.length)) {
            return;
        }
        for (n = 0; n < (cArray = NON_EXECUTE_PREFIX).length; ++n) {
            if (this.buffer[this.pos + n] == cArray[n]) continue;
            return;
        }
        this.pos += cArray.length;
    }

    private boolean fillBuffer(int n) throws IOException {
        block7: {
            char[] cArray = this.buffer;
            int n2 = this.lineStart;
            int n3 = this.pos;
            this.lineStart = n2 - n3;
            n2 = this.limit;
            if (n2 != n3) {
                this.limit = n2 -= n3;
                System.arraycopy(cArray, n3, cArray, 0, n2);
            } else {
                this.limit = 0;
            }
            this.pos = 0;
            do {
                Reader reader = this.in;
                n3 = this.limit;
                if ((n3 = reader.read(cArray, n3, cArray.length - n3)) == -1) break block7;
                this.limit = n2 = this.limit + n3;
                n3 = n;
                if (this.lineNumber == 0) {
                    int n4 = this.lineStart;
                    n3 = n;
                    if (n4 == 0) {
                        n3 = n;
                        if (n2 > 0) {
                            n3 = n;
                            if (cArray[0] == '\ufeff') {
                                ++this.pos;
                                this.lineStart = n4 + 1;
                                n3 = n + 1;
                            }
                        }
                    }
                }
                n = n3;
            } while (n2 < n3);
            return true;
        }
        return false;
    }

    private boolean isLiteral(char c) throws IOException {
        switch (c) {
            default: {
                return true;
            }
            case '#': 
            case '/': 
            case ';': 
            case '=': 
            case '\\': {
                this.checkLenient();
            }
            case '\t': 
            case '\n': 
            case '\f': 
            case '\r': 
            case ' ': 
            case ',': 
            case ':': 
            case '[': 
            case ']': 
            case '{': 
            case '}': 
        }
        return false;
    }

    private String locationString() {
        int n = this.lineNumber;
        int n2 = this.pos;
        int n3 = this.lineStart;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" at line ");
        stringBuilder.append(n + 1);
        stringBuilder.append(" column ");
        stringBuilder.append(n2 - n3 + 1);
        stringBuilder.append(" path ");
        stringBuilder.append(this.getPath());
        return stringBuilder.toString();
    }

    private int nextNonWhitespace(boolean bl) throws IOException {
        Object object = this.buffer;
        int n = this.pos;
        int n2 = this.limit;
        block4: while (true) {
            int n3 = n;
            int n4 = n2;
            if (n == n2) {
                this.pos = n;
                if (!this.fillBuffer(1)) {
                    if (!bl) {
                        return -1;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("End of input");
                    ((StringBuilder)object).append(this.locationString());
                    throw new EOFException(((StringBuilder)object).toString());
                }
                n3 = this.pos;
                n4 = this.limit;
            }
            n = n3 + 1;
            n2 = object[n3];
            if (n2 == 10) {
                ++this.lineNumber;
                this.lineStart = n;
            } else if (n2 != 32 && n2 != 13 && n2 != 9) {
                if (n2 == 47) {
                    this.pos = n;
                    if (n == n4) {
                        this.pos = n - 1;
                        boolean bl2 = this.fillBuffer(2);
                        ++this.pos;
                        if (!bl2) {
                            return n2;
                        }
                    }
                    this.checkLenient();
                    n = this.pos;
                    switch (object[n]) {
                        default: {
                            return n2;
                        }
                        case 47: {
                            this.pos = n + 1;
                            this.skipToEndOfLine();
                            n = this.pos;
                            n2 = this.limit;
                            continue block4;
                        }
                        case 42: 
                    }
                    this.pos = n + 1;
                    if (this.skipTo("*/")) {
                        n = this.pos;
                        n2 = this.limit;
                        n += 2;
                        continue;
                    }
                    throw this.syntaxError("Unterminated comment");
                }
                if (n2 == 35) {
                    this.pos = n;
                    this.checkLenient();
                    this.skipToEndOfLine();
                    n = this.pos;
                    n2 = this.limit;
                    continue;
                }
                this.pos = n;
                return n2;
            }
            n2 = n4;
        }
    }

    private String nextQuotedValue(char c) throws IOException {
        Object object = this.buffer;
        StringBuilder stringBuilder = new StringBuilder();
        do {
            int n = this.pos;
            int n2 = this.limit;
            int n3 = n;
            while (n < n2) {
                int n4 = n + 1;
                if ((n = object[n]) == c) {
                    this.pos = n4;
                    stringBuilder.append((char[])object, n3, n4 - n3 - 1);
                    return stringBuilder.toString();
                }
                if (n == 92) {
                    this.pos = n4;
                    stringBuilder.append((char[])object, n3, n4 - n3 - 1);
                    stringBuilder.append(this.readEscapeCharacter());
                    n2 = this.pos;
                    n3 = this.limit;
                    n4 = n2;
                } else {
                    if (n == 10) {
                        ++this.lineNumber;
                        this.lineStart = n4;
                    }
                    n = n4;
                    n4 = n3;
                    n3 = n2;
                    n2 = n;
                }
                n = n2;
                n2 = n3;
                n3 = n4;
            }
            stringBuilder.append((char[])object, n3, n - n3);
            this.pos = n;
        } while (this.fillBuffer(1));
        object = this.syntaxError("Unterminated string");
        throw object;
    }

    private String nextUnquotedValue() throws IOException {
        CharSequence charSequence = null;
        int n = 0;
        block4: while (true) {
            StringBuilder stringBuilder;
            block16: {
                int n2;
                block14: {
                    block15: {
                        block13: {
                            if ((n2 = this.pos) + n >= this.limit) break block13;
                            switch (this.buffer[n2 + n]) {
                                default: {
                                    ++n;
                                    continue block4;
                                }
                                case '#': 
                                case '/': 
                                case ';': 
                                case '=': 
                                case '\\': {
                                    this.checkLenient();
                                }
                                case '\t': 
                                case '\n': 
                                case '\f': 
                                case '\r': 
                                case ' ': 
                                case ',': 
                                case ':': 
                                case '[': 
                                case ']': 
                                case '{': 
                                case '}': 
                            }
                            stringBuilder = charSequence;
                            n2 = n;
                            break block14;
                        }
                        if (n >= this.buffer.length) break block15;
                        stringBuilder = charSequence;
                        n2 = n;
                        if (this.fillBuffer(n + 1)) {
                            continue;
                        }
                        break block14;
                    }
                    stringBuilder = charSequence;
                    if (charSequence == null) {
                        stringBuilder = new StringBuilder();
                    }
                    stringBuilder.append(this.buffer, this.pos, n);
                    this.pos += n;
                    n2 = 0;
                    n = 0;
                    if (this.fillBuffer(1)) break block16;
                }
                if (stringBuilder == null) {
                    charSequence = new String(this.buffer, this.pos, n2);
                } else {
                    stringBuilder.append(this.buffer, this.pos, n2);
                    charSequence = stringBuilder.toString();
                }
                this.pos += n2;
                return charSequence;
            }
            charSequence = stringBuilder;
        }
    }

    private int peekKeyword() throws IOException {
        String string2;
        String string3;
        int n = this.buffer[this.pos];
        if (n != 116 && n != 84) {
            if (n != 102 && n != 70) {
                if (n != 110 && n != 78) {
                    return 0;
                }
                string3 = "null";
                string2 = "NULL";
                n = 7;
            } else {
                string3 = "false";
                string2 = "FALSE";
                n = 6;
            }
        } else {
            string3 = "true";
            string2 = "TRUE";
            n = 5;
        }
        int n2 = string3.length();
        for (int i = 1; i < n2; ++i) {
            if (this.pos + i >= this.limit && !this.fillBuffer(i + 1)) {
                return 0;
            }
            char c = this.buffer[this.pos + i];
            if (c == string3.charAt(i) || c == string2.charAt(i)) continue;
            return 0;
        }
        if ((this.pos + n2 < this.limit || this.fillBuffer(n2 + 1)) && this.isLiteral(this.buffer[this.pos + n2])) {
            return 0;
        }
        this.pos += n2;
        this.peeked = n;
        return n;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int peekNumber() throws IOException {
        int n;
        int n2;
        int n3;
        int n4;
        long l;
        block22: {
            char c;
            char[] cArray = this.buffer;
            int n5 = this.pos;
            int n6 = this.limit;
            l = 0L;
            n4 = 0;
            n3 = 1;
            n2 = 0;
            n = 0;
            block6: while (true) {
                int n7;
                long l2;
                int n8;
                int n9;
                int n10;
                int n11;
                block23: {
                    block24: {
                        block29: {
                            block28: {
                                block25: {
                                    block27: {
                                        block26: {
                                            n11 = 0;
                                            n10 = n5;
                                            n9 = n6;
                                            if (n5 + n == n6) {
                                                if (n == cArray.length) {
                                                    return 0;
                                                }
                                                if (!this.fillBuffer(n + 1)) break block22;
                                                n10 = this.pos;
                                                n9 = this.limit;
                                            }
                                            c = cArray[n10 + n];
                                            switch (c) {
                                                default: {
                                                    if (c >= '0' && c <= '9') break;
                                                    break block6;
                                                }
                                                case 'E': 
                                                case 'e': {
                                                    if (n2 != 2 && n2 != 4) {
                                                        return 0;
                                                    }
                                                    n8 = 5;
                                                    l2 = l;
                                                    n11 = n4;
                                                    n7 = n3;
                                                    break block23;
                                                }
                                                case '.': {
                                                    if (n2 != 2) return 0;
                                                    n8 = 3;
                                                    l2 = l;
                                                    n11 = n4;
                                                    n7 = n3;
                                                    break block23;
                                                }
                                                case '-': {
                                                    if (n2 == 0) {
                                                        n11 = 1;
                                                        n8 = 1;
                                                        l2 = l;
                                                        n7 = n3;
                                                    } else {
                                                        if (n2 != 5) return 0;
                                                        n8 = 6;
                                                        l2 = l;
                                                        n11 = n4;
                                                        n7 = n3;
                                                    }
                                                    break block23;
                                                }
                                                case '+': {
                                                    if (n2 != 5) return 0;
                                                    n8 = 6;
                                                    l2 = l;
                                                    n11 = n4;
                                                    n7 = n3;
                                                    break block23;
                                                }
                                            }
                                            if (n2 == 1 || n2 == 0) break block24;
                                            if (n2 != 2) break block25;
                                            if (l == 0L) {
                                                return 0;
                                            }
                                            l2 = 10L * l - (long)(c - 48);
                                            if (l > -922337203685477580L) break block26;
                                            n8 = n11;
                                            if (l != -922337203685477580L) break block27;
                                            n8 = n11;
                                            if (l2 >= l) break block27;
                                        }
                                        n8 = 1;
                                    }
                                    n7 = n3 & n8;
                                    n11 = n4;
                                    n8 = n2;
                                    break block23;
                                }
                                if (n2 != 3) break block28;
                                n8 = 4;
                                l2 = l;
                                n11 = n4;
                                n7 = n3;
                                break block23;
                            }
                            if (n2 == 5) break block29;
                            l2 = l;
                            n11 = n4;
                            n7 = n3;
                            n8 = n2;
                            if (n2 != 6) break block23;
                        }
                        n8 = 7;
                        l2 = l;
                        n11 = n4;
                        n7 = n3;
                        break block23;
                    }
                    l2 = -(c - 48);
                    n8 = 2;
                    n7 = n3;
                    n11 = n4;
                }
                ++n;
                n5 = n10;
                n6 = n9;
                l = l2;
                n4 = n11;
                n3 = n7;
                n2 = n8;
            }
            if (this.isLiteral(c)) return 0;
        }
        if (n2 == 2 && n3 != 0 && (l != Long.MIN_VALUE || n4 != 0)) {
            if (n4 == 0) {
                l = -l;
            }
            this.peekedLong = l;
            this.pos += n;
            this.peeked = 15;
            return 15;
        }
        if (n2 != 2 && n2 != 4 && n2 != 7) {
            return 0;
        }
        this.peekedNumberLength = n;
        this.peeked = 16;
        return 16;
    }

    private void push(int n) {
        Object[] objectArray;
        int n2 = this.stackSize;
        int[] nArray = this.stack;
        if (n2 == nArray.length) {
            int[] nArray2 = new int[n2 * 2];
            int[] nArray3 = new int[n2 * 2];
            objectArray = new String[n2 * 2];
            System.arraycopy(nArray, 0, nArray2, 0, n2);
            System.arraycopy(this.pathIndices, 0, nArray3, 0, this.stackSize);
            System.arraycopy(this.pathNames, 0, objectArray, 0, this.stackSize);
            this.stack = nArray2;
            this.pathIndices = nArray3;
            this.pathNames = (String[])objectArray;
        }
        objectArray = this.stack;
        n2 = this.stackSize;
        this.stackSize = n2 + 1;
        objectArray[n2] = n;
    }

    private char readEscapeCharacter() throws IOException {
        int n;
        if (this.pos == this.limit && !this.fillBuffer(1)) {
            throw this.syntaxError("Unterminated escape sequence");
        }
        Object object = this.buffer;
        int n2 = this.pos;
        this.pos = n = n2 + 1;
        char c = object[n2];
        switch (c) {
            default: {
                throw this.syntaxError("Invalid escape sequence");
            }
            case 'u': {
                if (n + 4 > this.limit && !this.fillBuffer(4)) {
                    throw this.syntaxError("Unterminated escape sequence");
                }
                c = '\u0000';
                for (n2 = n = this.pos; n2 < n + 4; ++n2) {
                    char c2 = this.buffer[n2];
                    char c3 = (char)(c << 4);
                    if (c2 >= '0' && c2 <= '9') {
                        c = (char)(c2 - 48 + c3);
                        continue;
                    }
                    if (c2 >= 'a' && c2 <= 'f') {
                        c = (char)(c2 - 97 + 10 + c3);
                        continue;
                    }
                    if (c2 >= 'A' && c2 <= 'F') {
                        c = (char)(c2 - 65 + 10 + c3);
                        continue;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("\\u");
                    ((StringBuilder)object).append(new String(this.buffer, this.pos, 4));
                    throw new NumberFormatException(((StringBuilder)object).toString());
                }
                this.pos += 4;
                return c;
            }
            case 't': {
                return '\t';
            }
            case 'r': {
                return '\r';
            }
            case 'n': {
                return '\n';
            }
            case 'f': {
                return '\f';
            }
            case 'b': {
                return '\b';
            }
            case '\n': {
                ++this.lineNumber;
                this.lineStart = n;
            }
            case '\"': 
            case '\'': 
            case '/': 
            case '\\': 
        }
        return c;
    }

    private void skipQuotedValue(char c) throws IOException {
        Object object = this.buffer;
        do {
            int n = this.pos;
            int n2 = this.limit;
            while (n < n2) {
                int n3 = n + 1;
                if ((n = object[n]) == c) {
                    this.pos = n3;
                    return;
                }
                if (n == 92) {
                    this.pos = n3;
                    this.readEscapeCharacter();
                    n2 = this.pos;
                    n3 = this.limit;
                } else {
                    if (n == 10) {
                        ++this.lineNumber;
                        this.lineStart = n3;
                    }
                    n = n3;
                    n3 = n2;
                    n2 = n;
                }
                n = n2;
                n2 = n3;
            }
            this.pos = n;
        } while (this.fillBuffer(1));
        object = this.syntaxError("Unterminated string");
        throw object;
    }

    /*
     * Unable to fully structure code
     */
    private boolean skipTo(String var1_1) throws IOException {
        block0: while (true) {
            block3: {
                if (this.pos + var1_1.length() > this.limit && !this.fillBuffer(var1_1.length())) {
                    return false;
                }
                var3_3 = this.buffer;
                var2_2 = this.pos;
                if (var3_3[var2_2] != '\n') break block3;
                ++this.lineNumber;
                this.lineStart = var2_2 + 1;
                ** GOTO lbl13
            }
            for (var2_2 = 0; var2_2 < var1_1.length(); ++var2_2) {
                if (this.buffer[this.pos + var2_2] == var1_1.charAt(var2_2)) continue;
lbl13:
                // 2 sources

                ++this.pos;
                continue block0;
            }
            break;
        }
        return true;
    }

    private void skipToEndOfLine() throws IOException {
        while (this.pos < this.limit || this.fillBuffer(1)) {
            int n;
            char[] cArray = this.buffer;
            int n2 = this.pos;
            this.pos = n = n2 + 1;
            if ((n2 = cArray[n2]) == 10) {
                ++this.lineNumber;
                this.lineStart = n;
                break;
            }
            if (n2 != 13) continue;
        }
    }

    private void skipUnquotedValue() throws IOException {
        do {
            int n;
            int n2 = 0;
            block5: while ((n = this.pos) + n2 < this.limit) {
                switch (this.buffer[n + n2]) {
                    default: {
                        ++n2;
                        continue block5;
                    }
                    case '#': 
                    case '/': 
                    case ';': 
                    case '=': 
                    case '\\': {
                        this.checkLenient();
                    }
                    case '\t': 
                    case '\n': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                    case ',': 
                    case ':': 
                    case '[': 
                    case ']': 
                    case '{': 
                    case '}': 
                }
                this.pos += n2;
                return;
            }
            this.pos = n + n2;
        } while (this.fillBuffer(1));
    }

    private IOException syntaxError(String string2) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(this.locationString());
        throw new MalformedJsonException(stringBuilder.toString());
    }

    public void beginArray() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 3) {
            this.push(1);
            this.pathIndices[this.stackSize - 1] = 0;
            this.peeked = 0;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected BEGIN_ARRAY but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void beginObject() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 1) {
            this.push(3);
            this.peeked = 0;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected BEGIN_OBJECT but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    @Override
    public void close() throws IOException {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }

    /*
     * Handled duff style switch with additional control
     * Enabled aggressive block sorting
     */
    int doPeek() throws IOException {
        int n;
        int n2;
        block44: {
            block42: {
                block45: {
                    block43: {
                        block41: {
                            int[] nArray = this.stack;
                            n2 = this.stackSize;
                            n = nArray[n2 - 1];
                            if (n != 1) break block41;
                            nArray[n2 - 1] = 2;
                            break block42;
                        }
                        if (n != 2) break block43;
                        switch (this.nextNonWhitespace(true)) {
                            default: {
                                throw this.syntaxError("Unterminated array");
                            }
                            case 93: {
                                this.peeked = 4;
                                return 4;
                            }
                            case 59: {
                                this.checkLenient();
                                break;
                            }
                            case 44: {
                                break;
                            }
                        }
                        break block42;
                    }
                    if (n == 3 || n == 5) break block44;
                    if (n != 4) break block45;
                    nArray[n2 - 1] = 5;
                    switch (this.nextNonWhitespace(true)) {
                        default: {
                            throw this.syntaxError("Expected ':'");
                        }
                        case 61: {
                            char[] cArray;
                            this.checkLenient();
                            if ((this.pos < this.limit || this.fillBuffer(1)) && (cArray = this.buffer)[n2 = this.pos] == '>') {
                                this.pos = n2 + 1;
                                break;
                            }
                            break block42;
                        }
                        case 58: {
                            break;
                        }
                    }
                    break block42;
                }
                if (n == 6) {
                    if (this.lenient) {
                        this.consumeNonExecutePrefix();
                    }
                    this.stack[this.stackSize - 1] = 7;
                } else if (n == 7) {
                    if (this.nextNonWhitespace(false) == -1) {
                        this.peeked = 17;
                        return 17;
                    }
                    this.checkLenient();
                    --this.pos;
                } else if (n == 8) {
                    throw new IllegalStateException("JsonReader is closed");
                }
            }
            int n3 = 0;
            block27: do {
                switch (n3 == 0 ? this.nextNonWhitespace(true) : n3) {
                    default: {
                        --this.pos;
                        n = this.peekKeyword();
                        if (n == 0) break;
                        return n;
                    }
                    case 123: {
                        this.peeked = 1;
                        return 1;
                    }
                    case 93: {
                        n3 = 44;
                        if (n != 1) continue block27;
                        this.peeked = 4;
                        return 4;
                    }
                    case 91: {
                        this.peeked = 3;
                        return 3;
                    }
                    case 44: 
                    case 59: {
                        if (n != 1 && n != 2) {
                            throw this.syntaxError("Unexpected value");
                        }
                        this.checkLenient();
                        --this.pos;
                        this.peeked = 7;
                        return 7;
                    }
                    case 39: {
                        this.checkLenient();
                        this.peeked = 8;
                        return 8;
                    }
                    case 34: {
                        this.peeked = 9;
                        return 9;
                    }
                }
                break;
            } while (true);
            if ((n = this.peekNumber()) != 0) {
                return n;
            }
            if (this.isLiteral(this.buffer[this.pos])) {
                this.checkLenient();
                this.peeked = 10;
                return 10;
            }
            throw this.syntaxError("Expected value");
        }
        nArray[n2 - 1] = 4;
        if (n == 5) {
            switch (this.nextNonWhitespace(true)) {
                default: {
                    throw this.syntaxError("Unterminated object");
                }
                case 125: {
                    this.peeked = 2;
                    return 2;
                }
                case 59: {
                    this.checkLenient();
                    break;
                }
                case 44: 
            }
        }
        n2 = this.nextNonWhitespace(true);
        switch (n2) {
            default: {
                this.checkLenient();
                --this.pos;
                if (!this.isLiteral((char)n2)) break;
                this.peeked = 14;
                return 14;
            }
            case 125: {
                if (n != 5) {
                    this.peeked = 2;
                    return 2;
                }
                throw this.syntaxError("Expected name");
            }
            case 39: {
                this.checkLenient();
                this.peeked = 12;
                return 12;
            }
            case 34: {
                this.peeked = 13;
                return 13;
            }
        }
        throw this.syntaxError("Expected name");
    }

    public void endArray() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 4) {
            n2 = this.stackSize - 1;
            this.stackSize = n2--;
            int[] nArray = this.pathIndices;
            nArray[n2] = nArray[n2] + 1;
            this.peeked = 0;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected END_ARRAY but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void endObject() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 2) {
            this.stackSize = n2 = this.stackSize - 1;
            this.pathNames[n2] = null;
            int[] nArray = this.pathIndices;
            nArray[--n2] = nArray[n2] + 1;
            this.peeked = 0;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected END_OBJECT but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder().append('$');
        int n = this.stackSize;
        block4: for (int i = 0; i < n; ++i) {
            switch (this.stack[i]) {
                default: {
                    continue block4;
                }
                case 3: 
                case 4: 
                case 5: {
                    stringBuilder.append('.');
                    String[] stringArray = this.pathNames;
                    if (stringArray[i] == null) continue block4;
                    stringBuilder.append(stringArray[i]);
                    continue block4;
                }
                case 1: 
                case 2: {
                    stringBuilder.append('[');
                    stringBuilder.append(this.pathIndices[i]);
                    stringBuilder.append(']');
                }
            }
        }
        return stringBuilder.toString();
    }

    public boolean hasNext() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        boolean bl = n2 != 2 && n2 != 4;
        return bl;
    }

    public final boolean isLenient() {
        return this.lenient;
    }

    public boolean nextBoolean() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 5) {
            this.peeked = 0;
            int[] nArray = this.pathIndices;
            n2 = this.stackSize - 1;
            nArray[n2] = nArray[n2] + 1;
            return true;
        }
        if (n2 == 6) {
            this.peeked = 0;
            int[] nArray = this.pathIndices;
            n2 = this.stackSize - 1;
            nArray[n2] = nArray[n2] + 1;
            return false;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a boolean but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public double nextDouble() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 15) {
            this.peeked = 0;
            int[] nArray = this.pathIndices;
            n2 = this.stackSize - 1;
            nArray[n2] = nArray[n2] + 1;
            return this.peekedLong;
        }
        if (n2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (n2 != 8 && n2 != 9) {
            if (n2 == 10) {
                this.peekedString = this.nextUnquotedValue();
            } else if (n2 != 11) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Expected a double but was ");
                stringBuilder.append((Object)this.peek());
                stringBuilder.append(this.locationString());
                throw new IllegalStateException(stringBuilder.toString());
            }
        } else {
            char c = n2 == 8 ? (char)'\'' : '\"';
            this.peekedString = this.nextQuotedValue(c);
        }
        this.peeked = 11;
        double d = Double.parseDouble(this.peekedString);
        if (!this.lenient && (Double.isNaN(d) || Double.isInfinite(d))) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("JSON forbids NaN and infinities: ");
            stringBuilder.append(d);
            stringBuilder.append(this.locationString());
            throw new MalformedJsonException(stringBuilder.toString());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] nArray = this.pathIndices;
        n2 = this.stackSize - 1;
        nArray[n2] = nArray[n2] + 1;
        return d;
    }

    public int nextInt() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 15) {
            long l = this.peekedLong;
            n = (int)l;
            if (l == (long)n) {
                this.peeked = 0;
                int[] nArray = this.pathIndices;
                n2 = this.stackSize - 1;
                nArray[n2] = nArray[n2] + 1;
                return n;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected an int but was ");
            stringBuilder.append(this.peekedLong);
            stringBuilder.append(this.locationString());
            throw new NumberFormatException(stringBuilder.toString());
        }
        if (n2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            int[] nArray;
            if (n2 != 8 && n2 != 9 && n2 != 10) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Expected an int but was ");
                stringBuilder.append((Object)this.peek());
                stringBuilder.append(this.locationString());
                throw new IllegalStateException(stringBuilder.toString());
            }
            if (n2 == 10) {
                this.peekedString = this.nextUnquotedValue();
            } else {
                char c = n2 == 8 ? (char)'\'' : '\"';
                this.peekedString = this.nextQuotedValue(c);
            }
            try {
                n = Integer.parseInt(this.peekedString);
                this.peeked = 0;
                nArray = this.pathIndices;
                n2 = this.stackSize - 1;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            nArray[n2] = nArray[n2] + 1;
            return n;
        }
        this.peeked = 11;
        double d = Double.parseDouble(this.peekedString);
        n = (int)d;
        if ((double)n == d) {
            this.peekedString = null;
            this.peeked = 0;
            int[] nArray = this.pathIndices;
            n2 = this.stackSize - 1;
            nArray[n2] = nArray[n2] + 1;
            return n;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected an int but was ");
        stringBuilder.append(this.peekedString);
        stringBuilder.append(this.locationString());
        throw new NumberFormatException(stringBuilder.toString());
    }

    public long nextLong() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 15) {
            this.peeked = 0;
            int[] nArray = this.pathIndices;
            n2 = this.stackSize - 1;
            nArray[n2] = nArray[n2] + 1;
            return this.peekedLong;
        }
        if (n2 == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            int[] nArray;
            long l;
            if (n2 != 8 && n2 != 9 && n2 != 10) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Expected a long but was ");
                stringBuilder.append((Object)this.peek());
                stringBuilder.append(this.locationString());
                throw new IllegalStateException(stringBuilder.toString());
            }
            if (n2 == 10) {
                this.peekedString = this.nextUnquotedValue();
            } else {
                char c = n2 == 8 ? (char)'\'' : '\"';
                this.peekedString = this.nextQuotedValue(c);
            }
            try {
                l = Long.parseLong(this.peekedString);
                this.peeked = 0;
                nArray = this.pathIndices;
                n2 = this.stackSize - 1;
            }
            catch (NumberFormatException numberFormatException) {
                // empty catch block
            }
            nArray[n2] = nArray[n2] + 1;
            return l;
        }
        this.peeked = 11;
        double d = Double.parseDouble(this.peekedString);
        long l = (long)d;
        if ((double)l == d) {
            this.peekedString = null;
            this.peeked = 0;
            int[] nArray = this.pathIndices;
            n2 = this.stackSize - 1;
            nArray[n2] = nArray[n2] + 1;
            return l;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a long but was ");
        stringBuilder.append(this.peekedString);
        stringBuilder.append(this.locationString());
        throw new NumberFormatException(stringBuilder.toString());
    }

    public String nextName() throws IOException {
        block7: {
            String string2;
            block5: {
                int n;
                block6: {
                    block4: {
                        int n2;
                        n = n2 = this.peeked;
                        if (n2 == 0) {
                            n = this.doPeek();
                        }
                        if (n != 14) break block4;
                        string2 = this.nextUnquotedValue();
                        break block5;
                    }
                    if (n != 12) break block6;
                    string2 = this.nextQuotedValue('\'');
                    break block5;
                }
                if (n != 13) break block7;
                string2 = this.nextQuotedValue('\"');
            }
            this.peeked = 0;
            this.pathNames[this.stackSize - 1] = string2;
            return string2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a name but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void nextNull() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        if (n2 == 7) {
            this.peeked = 0;
            int[] nArray = this.pathIndices;
            n2 = this.stackSize - 1;
            nArray[n2] = nArray[n2] + 1;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected null but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public String nextString() throws IOException {
        block10: {
            String string2;
            int n;
            block5: {
                block9: {
                    block8: {
                        block7: {
                            block6: {
                                block4: {
                                    int n2;
                                    n = n2 = this.peeked;
                                    if (n2 == 0) {
                                        n = this.doPeek();
                                    }
                                    if (n != 10) break block4;
                                    string2 = this.nextUnquotedValue();
                                    break block5;
                                }
                                if (n != 8) break block6;
                                string2 = this.nextQuotedValue('\'');
                                break block5;
                            }
                            if (n != 9) break block7;
                            string2 = this.nextQuotedValue('\"');
                            break block5;
                        }
                        if (n != 11) break block8;
                        string2 = this.peekedString;
                        this.peekedString = null;
                        break block5;
                    }
                    if (n != 15) break block9;
                    string2 = Long.toString(this.peekedLong);
                    break block5;
                }
                if (n != 16) break block10;
                string2 = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            }
            this.peeked = 0;
            int[] nArray = this.pathIndices;
            n = this.stackSize - 1;
            nArray[n] = nArray[n] + 1;
            return string2;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected a string but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public JsonToken peek() throws IOException {
        int n;
        int n2 = n = this.peeked;
        if (n == 0) {
            n2 = this.doPeek();
        }
        switch (n2) {
            default: {
                throw new AssertionError();
            }
            case 17: {
                return JsonToken.END_DOCUMENT;
            }
            case 15: 
            case 16: {
                return JsonToken.NUMBER;
            }
            case 12: 
            case 13: 
            case 14: {
                return JsonToken.NAME;
            }
            case 8: 
            case 9: 
            case 10: 
            case 11: {
                return JsonToken.STRING;
            }
            case 7: {
                return JsonToken.NULL;
            }
            case 5: 
            case 6: {
                return JsonToken.BOOLEAN;
            }
            case 4: {
                return JsonToken.END_ARRAY;
            }
            case 3: {
                return JsonToken.BEGIN_ARRAY;
            }
            case 2: {
                return JsonToken.END_OBJECT;
            }
            case 1: 
        }
        return JsonToken.BEGIN_OBJECT;
    }

    public final void setLenient(boolean bl) {
        this.lenient = bl;
    }

    public void skipValue() throws IOException {
        int n = 0;
        while (true) {
            int n2;
            int n3 = n2 = this.peeked;
            if (n2 == 0) {
                n3 = this.doPeek();
            }
            if (n3 == 3) {
                this.push(1);
                n2 = n + 1;
            } else if (n3 == 1) {
                this.push(3);
                n2 = n + 1;
            } else if (n3 == 4) {
                --this.stackSize;
                n2 = n - 1;
            } else if (n3 == 2) {
                --this.stackSize;
                n2 = n - 1;
            } else if (n3 != 14 && n3 != 10) {
                if (n3 != 8 && n3 != 12) {
                    if (n3 != 9 && n3 != 13) {
                        n2 = n;
                        if (n3 == 16) {
                            this.pos += this.peekedNumberLength;
                            n2 = n;
                        }
                    } else {
                        this.skipQuotedValue('\"');
                        n2 = n;
                    }
                } else {
                    this.skipQuotedValue('\'');
                    n2 = n;
                }
            } else {
                this.skipUnquotedValue();
                n2 = n;
            }
            this.peeked = 0;
            if (n2 == 0) {
                int[] nArray = this.pathIndices;
                n = this.stackSize;
                n2 = n - 1;
                nArray[n2] = nArray[n2] + 1;
                this.pathNames[n - 1] = "null";
                return;
            }
            n = n2;
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getClass().getSimpleName());
        stringBuilder.append(this.locationString());
        return stringBuilder.toString();
    }
}

