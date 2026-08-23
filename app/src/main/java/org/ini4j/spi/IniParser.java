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
import org.ini4j.spi.AbstractParser;
import org.ini4j.spi.HandlerBase;
import org.ini4j.spi.IniHandler;
import org.ini4j.spi.IniSource;
import org.ini4j.spi.ServiceFinder;

public class IniParser
extends AbstractParser {
    private static final String COMMENTS = ";#";
    private static final String OPERATORS = ":=";
    static final char SECTION_BEGIN = '[';
    static final char SECTION_END = ']';

    public IniParser() {
        super(OPERATORS, COMMENTS);
    }

    public static IniParser newInstance() {
        return ServiceFinder.findService(IniParser.class);
    }

    public static IniParser newInstance(Config config) {
        IniParser iniParser = IniParser.newInstance();
        iniParser.setConfig(config);
        return iniParser;
    }

    private void parse(IniSource iniSource, IniHandler iniHandler) throws IOException, InvalidFileFormatException {
        iniHandler.startIni();
        String string2 = null;
        String string3 = iniSource.readLine();
        while (string3 != null) {
            if (string3.charAt(0) == '[') {
                if (string2 != null) {
                    iniHandler.endSection();
                }
                string2 = this.parseSectionLine(string3, iniSource, iniHandler);
            } else {
                String string4 = string2;
                if (string2 == null) {
                    if (this.getConfig().isGlobalSection()) {
                        string4 = this.getConfig().getGlobalSectionName();
                        iniHandler.startSection(string4);
                    } else {
                        this.parseError(string3, iniSource.getLineNumber());
                        string4 = string2;
                    }
                }
                this.parseOptionLine(string3, iniHandler, iniSource.getLineNumber());
                string2 = string4;
            }
            string3 = iniSource.readLine();
        }
        if (string2 != null) {
            iniHandler.endSection();
        }
        iniHandler.endIni();
    }

    private String parseSectionLine(String string2, IniSource iniSource, IniHandler iniHandler) throws InvalidFileFormatException {
        String string3;
        if (string2.charAt(string2.length() - 1) != ']') {
            this.parseError(string2, iniSource.getLineNumber());
        }
        if ((string3 = this.unescapeKey(string2.substring(1, string2.length() - 1).trim())).length() == 0 && !this.getConfig().isUnnamedSection()) {
            this.parseError(string2, iniSource.getLineNumber());
        }
        string2 = string3;
        if (this.getConfig().isLowerCaseSection()) {
            string2 = string3.toLowerCase(Locale.getDefault());
        }
        iniHandler.startSection(string2);
        return string2;
    }

    public void parse(InputStream inputStream, IniHandler iniHandler) throws IOException, InvalidFileFormatException {
        this.parse(this.newIniSource(inputStream, (HandlerBase)iniHandler), iniHandler);
    }

    public void parse(Reader reader, IniHandler iniHandler) throws IOException, InvalidFileFormatException {
        this.parse(this.newIniSource(reader, (HandlerBase)iniHandler), iniHandler);
    }

    public void parse(URL uRL, IniHandler iniHandler) throws IOException, InvalidFileFormatException {
        this.parse(this.newIniSource(uRL, (HandlerBase)iniHandler), iniHandler);
    }
}

