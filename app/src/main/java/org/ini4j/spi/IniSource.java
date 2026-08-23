/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.URL;
import org.ini4j.Config;
import org.ini4j.spi.HandlerBase;
import org.ini4j.spi.UnicodeInputStreamReader;

class IniSource {
    private static final char ESCAPE_CHAR = '\\';
    public static final char INCLUDE_BEGIN = '<';
    public static final char INCLUDE_END = '>';
    public static final char INCLUDE_OPTIONAL = '?';
    private URL _base;
    private IniSource _chain;
    private final String _commentChars;
    private final Config _config;
    private final HandlerBase _handler;
    private final LineNumberReader _reader;

    IniSource(InputStream inputStream, HandlerBase handlerBase, String string2, Config config) {
        this(new UnicodeInputStreamReader(inputStream, config.getFileEncoding()), handlerBase, string2, config);
    }

    IniSource(Reader reader, HandlerBase handlerBase, String string2, Config config) {
        this._reader = new LineNumberReader(reader);
        this._handler = handlerBase;
        this._commentChars = string2;
        this._config = config;
    }

    IniSource(URL uRL, HandlerBase handlerBase, String string2, Config config) throws IOException {
        this(new UnicodeInputStreamReader(uRL.openStream(), config.getFileEncoding()), handlerBase, string2, config);
        this._base = uRL;
    }

    private void close() throws IOException {
        this._reader.close();
    }

    private int countEndingEscapes(String string2) {
        int n = 0;
        for (int i = string2.length() - 1; i >= 0 && string2.charAt(i) == '\\'; --i) {
            ++n;
        }
        return n;
    }

    private void handleComment(StringBuilder stringBuilder) {
        if (stringBuilder.length() != 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            this._handler.handleComment(stringBuilder.toString());
            stringBuilder.delete(0, stringBuilder.length());
        }
    }

    private String handleInclude(String object) throws IOException {
        Object object2;
        object = object2 = object;
        if (this._config.isInclude()) {
            object = object2;
            if (((String)object2).length() > 2) {
                boolean bl = false;
                object = object2;
                if (((String)object2).charAt(0) == '<') {
                    object = object2;
                    if (((String)object2).charAt(((String)object2).length() - 1) == '>') {
                        if (((String)(object2 = ((String)object2).substring(1, ((String)object2).length() - 1).trim())).charAt(0) == '?') {
                            bl = true;
                        }
                        object = object2;
                        if (bl) {
                            object = ((String)object2).substring(1).trim();
                        }
                        object = this._base == null ? new URL((String)object) : new URL(this._base, (String)object);
                        if (bl) {
                            try {
                                this._chain = object2 = new IniSource((URL)object, this._handler, this._commentChars, this._config);
                            }
                            catch (Throwable throwable) {
                                this.readLine();
                                throw throwable;
                            }
                            catch (IOException iOException) {
                                // empty catch block
                            }
                            object = this.readLine();
                        } else {
                            this._chain = new IniSource((URL)object, this._handler, this._commentChars, this._config);
                            object = this.readLine();
                        }
                    }
                }
            }
        }
        return object;
    }

    private String readLineLocal() throws IOException {
        String string2 = this.readLineSkipComments();
        if (string2 == null) {
            this.close();
        } else {
            string2 = this.handleInclude(string2);
        }
        return string2;
    }

    private String readLineSkipComments() throws IOException {
        String string2;
        StringBuilder stringBuilder;
        block6: {
            stringBuilder = new StringBuilder();
            StringBuilder stringBuilder2 = new StringBuilder();
            String string3 = this._reader.readLine();
            while (true) {
                string2 = string3;
                if (string3 == null) break block6;
                if ((string3 = string3.trim()).length() == 0) {
                    this.handleComment(stringBuilder);
                } else if (this._commentChars.indexOf(string3.charAt(0)) >= 0 && stringBuilder2.length() == 0) {
                    stringBuilder.append(string3.substring(1));
                    stringBuilder.append(this._config.getLineSeparator());
                } else {
                    this.handleComment(stringBuilder);
                    if (!this._config.isEscapeNewline() || (this.countEndingEscapes(string3) & 1) == 0) break;
                    stringBuilder2.append(string3.subSequence(0, string3.length() - 1));
                }
                string3 = this._reader.readLine();
            }
            stringBuilder2.append(string3);
            string2 = stringBuilder2.toString();
        }
        if (string2 == null && stringBuilder.length() != 0) {
            this.handleComment(stringBuilder);
        }
        return string2;
    }

    int getLineNumber() {
        IniSource iniSource = this._chain;
        int n = iniSource == null ? this._reader.getLineNumber() : iniSource.getLineNumber();
        return n;
    }

    String readLine() throws IOException {
        Object object = this._chain;
        if (object == null) {
            object = this.readLineLocal();
        } else {
            String string2 = ((IniSource)object).readLine();
            object = string2;
            if (string2 == null) {
                this._chain = null;
                object = this.readLine();
            }
        }
        return object;
    }
}

