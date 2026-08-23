/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import org.ini4j.Config;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.spi.AbstractParser;
import org.ini4j.spi.HandlerBase;
import org.ini4j.spi.IniSource;
import org.ini4j.spi.OptionsHandler;
import org.ini4j.spi.ServiceFinder;

public class OptionsParser
extends AbstractParser {
    private static final String COMMENTS = "!#";
    private static final String OPERATORS = ":=";

    public OptionsParser() {
        super(OPERATORS, COMMENTS);
    }

    public static OptionsParser newInstance() {
        return ServiceFinder.findService(OptionsParser.class);
    }

    public static OptionsParser newInstance(Config config) {
        OptionsParser optionsParser = OptionsParser.newInstance();
        optionsParser.setConfig(config);
        return optionsParser;
    }

    private void parse(IniSource iniSource, OptionsHandler optionsHandler) throws IOException, InvalidFileFormatException {
        optionsHandler.startOptions();
        String string2 = iniSource.readLine();
        while (string2 != null) {
            this.parseOptionLine(string2, optionsHandler, iniSource.getLineNumber());
            string2 = iniSource.readLine();
        }
        optionsHandler.endOptions();
    }

    public void parse(InputStream inputStream, OptionsHandler optionsHandler) throws IOException, InvalidFileFormatException {
        this.parse(this.newIniSource(inputStream, (HandlerBase)optionsHandler), optionsHandler);
    }

    public void parse(Reader reader, OptionsHandler optionsHandler) throws IOException, InvalidFileFormatException {
        this.parse(this.newIniSource(reader, (HandlerBase)optionsHandler), optionsHandler);
    }

    public void parse(URL uRL, OptionsHandler optionsHandler) throws IOException, InvalidFileFormatException {
        this.parse(this.newIniSource(uRL, (HandlerBase)optionsHandler), optionsHandler);
    }
}

