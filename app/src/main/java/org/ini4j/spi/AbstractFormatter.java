/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.PrintWriter;
import org.ini4j.Config;
import org.ini4j.spi.EscapeTool;
import org.ini4j.spi.HandlerBase;

abstract class AbstractFormatter
implements HandlerBase {
    private static final char COMMENT = '#';
    private static final char OPERATOR = '=';
    private static final char SPACE = ' ';
    private Config _config = Config.getGlobal();
    private boolean _header = true;
    private PrintWriter _output;

    AbstractFormatter() {
    }

    String escapeKey(String string2) {
        block0: {
            if (!this.getConfig().isEscape()) break block0;
            string2 = EscapeTool.getInstance().escape(string2);
        }
        return string2;
    }

    String escapeValue(String string2) {
        block0: {
            if (!this.getConfig().isEscape() || this.getConfig().isEscapeKeyOnly()) break block0;
            string2 = EscapeTool.getInstance().escape(string2);
        }
        return string2;
    }

    protected Config getConfig() {
        return this._config;
    }

    protected PrintWriter getOutput() {
        return this._output;
    }

    @Override
    public void handleComment(String stringArray) {
        if (this.getConfig().isComment() && (!this._header || this.getConfig().isHeaderComment()) && stringArray != null && stringArray.length() != 0) {
            for (String string2 : stringArray.split(this.getConfig().getLineSeparator())) {
                this.getOutput().print('#');
                this.getOutput().print(string2);
                this.getOutput().print(this.getConfig().getLineSeparator());
            }
            if (this._header) {
                this.getOutput().print(this.getConfig().getLineSeparator());
            }
        }
        this._header = false;
    }

    @Override
    public void handleOption(String string2, String string3) {
        if (this.getConfig().isStrictOperator()) {
            if (this.getConfig().isEmptyOption() || string3 != null) {
                this.getOutput().print(this.escapeKey(string2));
                this.getOutput().print('=');
            }
            if (string3 != null) {
                this.getOutput().print(this.escapeValue(string3));
            }
            if (this.getConfig().isEmptyOption() || string3 != null) {
                this.getOutput().print(this.getConfig().getLineSeparator());
            }
        } else {
            if (string3 == null && this.getConfig().isEmptyOption()) {
                string3 = "";
            }
            if (string3 != null) {
                this.getOutput().print(this.escapeKey(string2));
                this.getOutput().print(' ');
                this.getOutput().print('=');
                this.getOutput().print(' ');
                this.getOutput().print(this.escapeValue(string3));
                this.getOutput().print(this.getConfig().getLineSeparator());
            }
        }
        this.setHeader(false);
    }

    protected void setConfig(Config config) {
        this._config = config;
    }

    void setHeader(boolean bl) {
        this._header = bl;
    }

    protected void setOutput(PrintWriter printWriter) {
        this._output = printWriter;
    }
}

