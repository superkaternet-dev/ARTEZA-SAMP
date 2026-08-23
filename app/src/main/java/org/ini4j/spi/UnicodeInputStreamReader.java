/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.Reader;
import java.nio.charset.Charset;

class UnicodeInputStreamReader
extends Reader {
    private static final int BOM_SIZE = 4;
    private final Charset _defaultEncoding;
    private InputStreamReader _reader;
    private final PushbackInputStream _stream;

    UnicodeInputStreamReader(InputStream inputStream, Charset charset) {
        this._stream = new PushbackInputStream(inputStream, 4);
        this._defaultEncoding = charset;
    }

    @Override
    public void close() throws IOException {
        this.init();
        this._reader.close();
    }

    protected void init() throws IOException {
        int n;
        Charset charset;
        if (this._reader != null) {
            return;
        }
        byte[] byArray = new byte[4];
        int n2 = this._stream.read(byArray, 0, byArray.length);
        Bom bom = Bom.find(byArray);
        if (bom == null) {
            charset = this._defaultEncoding;
            n = n2;
        } else {
            charset = bom._charset;
            n = byArray.length - bom._bytes.length;
        }
        if (n > 0) {
            this._stream.unread(byArray, n2 - n, n);
        }
        this._reader = new InputStreamReader((InputStream)this._stream, charset);
    }

    @Override
    public int read(char[] cArray, int n, int n2) throws IOException {
        this.init();
        return this._reader.read(cArray, n, n2);
    }

    private static final class Bom
    extends Enum<Bom> {
        private static final Bom[] $VALUES;
        public static final /* enum */ Bom UTF16BE;
        public static final /* enum */ Bom UTF16LE;
        public static final /* enum */ Bom UTF32BE;
        public static final /* enum */ Bom UTF32LE;
        public static final /* enum */ Bom UTF8;
        private final byte[] _bytes;
        private Charset _charset;

        static {
            Bom bom;
            Bom bom2;
            Bom bom3;
            Bom bom4;
            Bom bom5;
            UTF32BE = bom5 = new Bom("UTF-32BE", new byte[]{0, 0, -2, -1});
            UTF32LE = bom4 = new Bom("UTF-32LE", new byte[]{-1, -2, 0, 0});
            UTF16BE = bom3 = new Bom("UTF-16BE", new byte[]{-2, -1});
            UTF16LE = bom2 = new Bom("UTF-16LE", new byte[]{-1, -2});
            UTF8 = bom = new Bom("UTF-8", new byte[]{-17, -69, -65});
            $VALUES = new Bom[]{bom5, bom4, bom3, bom2, bom};
        }

        private Bom(String string3, byte[] byArray) {
            try {
                this._charset = Charset.forName(string3);
            }
            catch (Exception exception) {
                this._charset = null;
            }
            this._bytes = byArray;
        }

        private static Bom find(byte[] byArray) {
            Bom bom;
            Bom bom2 = null;
            Bom[] bomArray = Bom.values();
            int n = bomArray.length;
            int n2 = 0;
            while (true) {
                bom = bom2;
                if (n2 >= n || (bom = bomArray[n2]).supported() && bom.match(byArray)) break;
                ++n2;
            }
            return bom;
        }

        private boolean match(byte[] byArray) {
            boolean bl;
            boolean bl2 = true;
            int n = 0;
            while (true) {
                byte[] byArray2 = this._bytes;
                bl = bl2;
                if (n >= byArray2.length) break;
                if (byArray[n] != byArray2[n]) {
                    bl = false;
                    break;
                }
                ++n;
            }
            return bl;
        }

        private boolean supported() {
            boolean bl = this._charset != null;
            return bl;
        }

        public static Bom valueOf(String string2) {
            return Enum.valueOf(Bom.class, string2);
        }

        public static Bom[] values() {
            return (Bom[])$VALUES.clone();
        }
    }
}

