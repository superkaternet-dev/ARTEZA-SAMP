/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import org.ini4j.Config;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.spi.WinEscapeTool;

public class Wini
extends Ini {
    public static final char PATH_SEPARATOR = '\\';
    private static final long serialVersionUID = -2781377824232440728L;

    public Wini() {
        Object object = Config.getGlobal().clone();
        ((Config)object).setEscape(false);
        ((Config)object).setEscapeNewline(false);
        ((Config)object).setGlobalSection(true);
        ((Config)object).setEmptyOption(true);
        ((Config)object).setMultiOption(false);
        ((Config)object).setPathSeparator('\\');
        this.setConfig((Config)object);
    }

    public Wini(File file) throws IOException, InvalidFileFormatException {
        this();
        this.setFile(file);
        this.load();
    }

    public Wini(InputStream inputStream) throws IOException, InvalidFileFormatException {
        this();
        this.load(inputStream);
    }

    public Wini(Reader reader) throws IOException, InvalidFileFormatException {
        this();
        this.load(reader);
    }

    public Wini(URL uRL) throws IOException, InvalidFileFormatException {
        this();
        this.load(uRL);
    }

    public String escape(String string2) {
        return WinEscapeTool.getInstance().escape(string2);
    }

    public String unescape(String string2) {
        return WinEscapeTool.getInstance().unescape(string2);
    }
}

