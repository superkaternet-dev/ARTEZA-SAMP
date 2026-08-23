/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Locale;
import org.ini4j.Config;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.spi.EscapeTool;
import org.ini4j.spi.HandlerBase;
import org.ini4j.spi.IniSource;

abstract class AbstractParser {
    private final String _comments;
    private Config _config = Config.getGlobal();
    private final String _operators;

    protected AbstractParser(String string2, String string3) {
        this._operators = string2;
        this._comments = string3;
    }

    private int indexOfOperator(String string2) {
        int n = -1;
        for (char c : this._operators.toCharArray()) {
            int n2;
            int n3 = string2.indexOf(c);
            while (true) {
                n2 = n;
                if (n3 < 0) break;
                n2 = -1;
                if (!(n3 < 0 || n3 != 0 && string2.charAt(n3 - 1) == '\\' || n != -1 && n3 >= n)) {
                    n2 = n3;
                    break;
                }
                if (n3 == string2.length() - 1) {
                    n3 = n2;
                    continue;
                }
                n3 = string2.indexOf(c, n3 + 1);
            }
            n = n2;
        }
        return n;
    }

    protected Config getConfig() {
        return this._config;
    }

    IniSource newIniSource(InputStream inputStream, HandlerBase handlerBase) {
        return new IniSource(inputStream, handlerBase, this._comments, this.getConfig());
    }

    IniSource newIniSource(Reader reader, HandlerBase handlerBase) {
        return new IniSource(reader, handlerBase, this._comments, this.getConfig());
    }

    IniSource newIniSource(URL uRL, HandlerBase handlerBase) throws IOException {
        return new IniSource(uRL, handlerBase, this._comments, this.getConfig());
    }

    protected void parseError(String string2, int n) throws InvalidFileFormatException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("parse error (at line: ");
        stringBuilder.append(n);
        stringBuilder.append("): ");
        stringBuilder.append(string2);
        throw new InvalidFileFormatException(stringBuilder.toString());
    }

    void parseOptionLine(String string2, HandlerBase handlerBase, int n) throws InvalidFileFormatException {
        int n2 = this.indexOfOperator(string2);
        String string3 = null;
        String string4 = null;
        if (n2 < 0) {
            if (this.getConfig().isEmptyOption()) {
                string3 = string2;
            } else {
                this.parseError(string2, n);
            }
        } else {
            string3 = this.unescapeKey(string2.substring(0, n2)).trim();
            string4 = this.unescapeValue(string2.substring(n2 + 1)).trim();
        }
        if (string3.length() == 0) {
            this.parseError(string2, n);
        }
        string2 = string3;
        if (this.getConfig().isLowerCaseOption()) {
            string2 = string3.toLowerCase(Locale.getDefault());
        }
        handlerBase.handleOption(string2, string4);
    }

    protected void setConfig(Config config) {
        this._config = config;
    }

    String unescapeKey(String string2) {
        block0: {
            if (!this.getConfig().isEscape()) break block0;
            string2 = EscapeTool.getInstance().unescape(string2);
        }
        return string2;
    }

    String unescapeValue(String string2) {
        block0: {
            if (!this.getConfig().isEscape() || this.getConfig().isEscapeKeyOnly()) break block0;
            string2 = EscapeTool.getInstance().unescape(string2);
        }
        return string2;
    }
}

