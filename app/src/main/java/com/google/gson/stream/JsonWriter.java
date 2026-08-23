/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.Writer;

public class JsonWriter
implements Closeable,
Flushable {
    private static final String[] HTML_SAFE_REPLACEMENT_CHARS;
    private static final String[] REPLACEMENT_CHARS;
    private String deferredName;
    private boolean htmlSafe;
    private String indent;
    private boolean lenient;
    private final Writer out;
    private String separator;
    private boolean serializeNulls;
    private int[] stack = new int[32];
    private int stackSize = 0;

    static {
        REPLACEMENT_CHARS = new String[128];
        for (int i = 0; i <= 31; ++i) {
            JsonWriter.REPLACEMENT_CHARS[i] = String.format("\\u%04x", i);
        }
        String[] stringArray = REPLACEMENT_CHARS;
        stringArray[34] = "\\\"";
        stringArray[92] = "\\\\";
        stringArray[9] = "\\t";
        stringArray[8] = "\\b";
        stringArray[10] = "\\n";
        stringArray[13] = "\\r";
        stringArray[12] = "\\f";
        stringArray = (String[])stringArray.clone();
        HTML_SAFE_REPLACEMENT_CHARS = stringArray;
        stringArray[60] = "\\u003c";
        stringArray[62] = "\\u003e";
        stringArray[38] = "\\u0026";
        stringArray[61] = "\\u003d";
        stringArray[39] = "\\u0027";
    }

    public JsonWriter(Writer writer) {
        this.push(6);
        this.separator = ":";
        this.serializeNulls = true;
        if (writer != null) {
            this.out = writer;
            return;
        }
        throw new NullPointerException("out == null");
    }

    private void beforeName() throws IOException {
        block4: {
            block3: {
                int n;
                block2: {
                    n = this.peek();
                    if (n != 5) break block2;
                    this.out.write(44);
                    break block3;
                }
                if (n != 3) break block4;
            }
            this.newline();
            this.replaceTop(4);
            return;
        }
        throw new IllegalStateException("Nesting problem.");
    }

    private void beforeValue() throws IOException {
        switch (this.peek()) {
            default: {
                throw new IllegalStateException("Nesting problem.");
            }
            case 7: {
                if (!this.lenient) {
                    throw new IllegalStateException("JSON must have only one top-level value.");
                }
            }
            case 6: {
                this.replaceTop(7);
                break;
            }
            case 4: {
                this.out.append(this.separator);
                this.replaceTop(5);
                break;
            }
            case 2: {
                this.out.append(',');
                this.newline();
                break;
            }
            case 1: {
                this.replaceTop(2);
                this.newline();
            }
        }
    }

    private JsonWriter close(int n, int n2, String charSequence) throws IOException {
        int n3 = this.peek();
        if (n3 != n2 && n3 != n) {
            throw new IllegalStateException("Nesting problem.");
        }
        if (this.deferredName == null) {
            --this.stackSize;
            if (n3 == n2) {
                this.newline();
            }
            this.out.write((String)charSequence);
            return this;
        }
        charSequence = new StringBuilder();
        ((StringBuilder)charSequence).append("Dangling name: ");
        ((StringBuilder)charSequence).append(this.deferredName);
        throw new IllegalStateException(((StringBuilder)charSequence).toString());
    }

    private void newline() throws IOException {
        if (this.indent == null) {
            return;
        }
        this.out.write("\n");
        int n = this.stackSize;
        for (int i = 1; i < n; ++i) {
            this.out.write(this.indent);
        }
    }

    private JsonWriter open(int n, String string2) throws IOException {
        this.beforeValue();
        this.push(n);
        this.out.write(string2);
        return this;
    }

    private int peek() {
        int n = this.stackSize;
        if (n != 0) {
            return this.stack[n - 1];
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }

    private void push(int n) {
        int[] nArray;
        int n2 = this.stackSize;
        int[] nArray2 = this.stack;
        if (n2 == nArray2.length) {
            nArray = new int[n2 * 2];
            System.arraycopy(nArray2, 0, nArray, 0, n2);
            this.stack = nArray;
        }
        nArray = this.stack;
        n2 = this.stackSize;
        this.stackSize = n2 + 1;
        nArray[n2] = n;
    }

    private void replaceTop(int n) {
        this.stack[this.stackSize - 1] = n;
    }

    private void string(String string2) throws IOException {
        String[] stringArray = this.htmlSafe ? HTML_SAFE_REPLACEMENT_CHARS : REPLACEMENT_CHARS;
        this.out.write("\"");
        int n = 0;
        int n2 = string2.length();
        for (int i = 0; i < n2; ++i) {
            int n3;
            block8: {
                String string3;
                block7: {
                    char c;
                    block9: {
                        block6: {
                            String string4;
                            c = string2.charAt(i);
                            if (c >= '\u0080') break block6;
                            string3 = string4 = stringArray[c];
                            if (string4 != null) break block7;
                            n3 = n;
                            break block8;
                        }
                        if (c != '\u2028') break block9;
                        string3 = "\\u2028";
                        break block7;
                    }
                    n3 = n;
                    if (c != '\u2029') break block8;
                    string3 = "\\u2029";
                }
                if (n < i) {
                    this.out.write(string2, n, i - n);
                }
                this.out.write(string3);
                n3 = i + 1;
            }
            n = n3;
        }
        if (n < n2) {
            this.out.write(string2, n, n2 - n);
        }
        this.out.write("\"");
    }

    private void writeDeferredName() throws IOException {
        if (this.deferredName != null) {
            this.beforeName();
            this.string(this.deferredName);
            this.deferredName = null;
        }
    }

    public JsonWriter beginArray() throws IOException {
        this.writeDeferredName();
        return this.open(1, "[");
    }

    public JsonWriter beginObject() throws IOException {
        this.writeDeferredName();
        return this.open(3, "{");
    }

    @Override
    public void close() throws IOException {
        this.out.close();
        int n = this.stackSize;
        if (n <= 1 && (n != 1 || this.stack[n - 1] == 7)) {
            this.stackSize = 0;
            return;
        }
        throw new IOException("Incomplete document");
    }

    public JsonWriter endArray() throws IOException {
        return this.close(1, 2, "]");
    }

    public JsonWriter endObject() throws IOException {
        return this.close(3, 5, "}");
    }

    @Override
    public void flush() throws IOException {
        if (this.stackSize != 0) {
            this.out.flush();
            return;
        }
        throw new IllegalStateException("JsonWriter is closed.");
    }

    public final boolean getSerializeNulls() {
        return this.serializeNulls;
    }

    public final boolean isHtmlSafe() {
        return this.htmlSafe;
    }

    public boolean isLenient() {
        return this.lenient;
    }

    public JsonWriter jsonValue(String string2) throws IOException {
        if (string2 == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        this.out.append(string2);
        return this;
    }

    public JsonWriter name(String string2) throws IOException {
        if (string2 != null) {
            if (this.deferredName == null) {
                if (this.stackSize != 0) {
                    this.deferredName = string2;
                    return this;
                }
                throw new IllegalStateException("JsonWriter is closed.");
            }
            throw new IllegalStateException();
        }
        throw new NullPointerException("name == null");
    }

    public JsonWriter nullValue() throws IOException {
        if (this.deferredName != null) {
            if (this.serializeNulls) {
                this.writeDeferredName();
            } else {
                this.deferredName = null;
                return this;
            }
        }
        this.beforeValue();
        this.out.write("null");
        return this;
    }

    public final void setHtmlSafe(boolean bl) {
        this.htmlSafe = bl;
    }

    public final void setIndent(String string2) {
        if (string2.length() == 0) {
            this.indent = null;
            this.separator = ":";
        } else {
            this.indent = string2;
            this.separator = ": ";
        }
    }

    public final void setLenient(boolean bl) {
        this.lenient = bl;
    }

    public final void setSerializeNulls(boolean bl) {
        this.serializeNulls = bl;
    }

    public JsonWriter value(double d) throws IOException {
        if (!Double.isNaN(d) && !Double.isInfinite(d)) {
            this.writeDeferredName();
            this.beforeValue();
            this.out.append(Double.toString(d));
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Numeric values must be finite, but was ");
        stringBuilder.append(d);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public JsonWriter value(long l) throws IOException {
        this.writeDeferredName();
        this.beforeValue();
        this.out.write(Long.toString(l));
        return this;
    }

    public JsonWriter value(Boolean object) throws IOException {
        if (object == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        Writer writer = this.out;
        object = ((Boolean)object).booleanValue() ? "true" : "false";
        writer.write((String)object);
        return this;
    }

    public JsonWriter value(Number number) throws IOException {
        if (number == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        CharSequence charSequence = number.toString();
        if (!this.lenient && (((String)charSequence).equals("-Infinity") || ((String)charSequence).equals("Infinity") || ((String)charSequence).equals("NaN"))) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("Numeric values must be finite, but was ");
            ((StringBuilder)charSequence).append(number);
            throw new IllegalArgumentException(((StringBuilder)charSequence).toString());
        }
        this.beforeValue();
        this.out.append(charSequence);
        return this;
    }

    public JsonWriter value(String string2) throws IOException {
        if (string2 == null) {
            return this.nullValue();
        }
        this.writeDeferredName();
        this.beforeValue();
        this.string(string2);
        return this;
    }

    public JsonWriter value(boolean bl) throws IOException {
        this.writeDeferredName();
        this.beforeValue();
        Writer writer = this.out;
        String string2 = bl ? "true" : "false";
        writer.write(string2);
        return this;
    }
}

