/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import org.ini4j.Config;
import org.ini4j.Options;
import org.ini4j.spi.OptionsHandler;
import org.ini4j.spi.ServiceFinder;

public class OptionsBuilder
implements OptionsHandler {
    private boolean _header;
    private String _lastComment;
    private Options _options;

    private Config getConfig() {
        return this._options.getConfig();
    }

    protected static OptionsBuilder newInstance() {
        return ServiceFinder.findService(OptionsBuilder.class);
    }

    public static OptionsBuilder newInstance(Options options) {
        OptionsBuilder optionsBuilder = OptionsBuilder.newInstance();
        optionsBuilder.setOptions(options);
        return optionsBuilder;
    }

    private void putComment(String string2) {
        if (this.getConfig().isComment()) {
            this._options.putComment(string2, this._lastComment);
        }
    }

    private void setHeaderComment() {
        if (this.getConfig().isComment()) {
            this._options.setComment(this._lastComment);
        }
    }

    @Override
    public void endOptions() {
        if (this._lastComment != null && this._header) {
            this.setHeaderComment();
        }
    }

    @Override
    public void handleComment(String string2) {
        if (this._lastComment != null && this._header) {
            this.setHeaderComment();
            this._header = false;
        }
        this._lastComment = string2;
    }

    @Override
    public void handleOption(String string2, String string3) {
        if (this.getConfig().isMultiOption()) {
            this._options.add(string2, string3);
        } else {
            this._options.put(string2, string3);
        }
        if (this._lastComment != null) {
            if (this._header) {
                this.setHeaderComment();
            } else {
                this.putComment(string2);
            }
            this._lastComment = null;
        }
        this._header = false;
    }

    public void setOptions(Options options) {
        this._options = options;
    }

    @Override
    public void startOptions() {
        if (this.getConfig().isHeaderComment()) {
            this._header = true;
        }
    }
}

