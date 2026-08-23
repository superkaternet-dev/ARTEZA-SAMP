/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection.util;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringListReader
extends Reader {
    private int charPos;
    private boolean closed = false;
    private boolean frozen = false;
    private int markedCharPos = this.charPos;
    private int markedStringListPos = this.stringListPos;
    private int stringListPos;
    private List<String> strings = new ArrayList<String>();

    private long advance(long l) {
        long l2 = 0L;
        while (this.stringListPos < this.strings.size() && l2 < l) {
            long l3 = l - l2;
            int n = this.currentStringRemainingChars();
            if (l3 < (long)n) {
                this.charPos = (int)((long)this.charPos + l3);
                l2 += l3;
                continue;
            }
            l2 += (long)n;
            this.charPos = 0;
            ++this.stringListPos;
        }
        return l2;
    }

    private void checkState() throws IOException {
        if (!this.closed) {
            if (this.frozen) {
                return;
            }
            throw new IOException("Reader needs to be frozen before read operations can be called");
        }
        throw new IOException("Stream already closed");
    }

    private String currentString() {
        String string2 = this.stringListPos < this.strings.size() ? this.strings.get(this.stringListPos) : null;
        return string2;
    }

    private int currentStringRemainingChars() {
        String string2 = this.currentString();
        int n = string2 == null ? 0 : string2.length() - this.charPos;
        return n;
    }

    public void addString(String string2) {
        if (!this.frozen) {
            if (string2.length() > 0) {
                this.strings.add(string2);
            }
            return;
        }
        throw new IllegalStateException("Trying to add string after reading");
    }

    @Override
    public void close() throws IOException {
        this.checkState();
        this.closed = true;
    }

    public void freeze() {
        if (!this.frozen) {
            this.frozen = true;
            return;
        }
        throw new IllegalStateException("Trying to freeze frozen StringListReader");
    }

    @Override
    public void mark(int n) throws IOException {
        this.checkState();
        this.markedCharPos = this.charPos;
        this.markedStringListPos = this.stringListPos;
    }

    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() throws IOException {
        this.checkState();
        String string2 = this.currentString();
        if (string2 == null) {
            return -1;
        }
        char c = string2.charAt(this.charPos);
        this.advance(1L);
        return c;
    }

    @Override
    public int read(CharBuffer charBuffer) throws IOException {
        this.checkState();
        int n = charBuffer.remaining();
        int n2 = 0;
        String string2 = this.currentString();
        while (n > 0 && string2 != null) {
            int n3 = Math.min(string2.length() - this.charPos, n);
            string2 = this.strings.get(this.stringListPos);
            int n4 = this.charPos;
            charBuffer.put(string2, n4, n4 + n3);
            n -= n3;
            n2 += n3;
            this.advance(n3);
            string2 = this.currentString();
        }
        if (n2 <= 0 && string2 == null) {
            return -1;
        }
        return n2;
    }

    @Override
    public int read(char[] cArray, int n, int n2) throws IOException {
        int n3;
        int n4;
        this.checkState();
        String string2 = this.currentString();
        for (n3 = 0; string2 != null && n3 < n2; n3 += n4) {
            n4 = Math.min(this.currentStringRemainingChars(), n2 - n3);
            int n5 = this.charPos;
            string2.getChars(n5, n5 + n4, cArray, n + n3);
            this.advance(n4);
            string2 = this.currentString();
        }
        if (n3 <= 0 && string2 == null) {
            return -1;
        }
        return n3;
    }

    @Override
    public boolean ready() throws IOException {
        this.checkState();
        return true;
    }

    @Override
    public void reset() throws IOException {
        this.charPos = this.markedCharPos;
        this.stringListPos = this.markedStringListPos;
    }

    @Override
    public long skip(long l) throws IOException {
        this.checkState();
        return this.advance(l);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<String> iterator2 = this.strings.iterator();
        while (iterator2.hasNext()) {
            stringBuilder.append(iterator2.next());
        }
        return stringBuilder.toString();
    }
}

