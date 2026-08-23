/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.PrintWriter;
import java.io.Writer;
import org.ini4j.Config;
import org.ini4j.spi.AbstractFormatter;
import org.ini4j.spi.IniHandler;
import org.ini4j.spi.ServiceFinder;

public class IniFormatter
extends AbstractFormatter
implements IniHandler {
    private static IniFormatter newInstance() {
        return ServiceFinder.findService(IniFormatter.class);
    }

    public static IniFormatter newInstance(Writer writer, Config config) {
        IniFormatter iniFormatter = IniFormatter.newInstance();
        writer = writer instanceof PrintWriter ? (PrintWriter)writer : new PrintWriter(writer);
        iniFormatter.setOutput((PrintWriter)writer);
        iniFormatter.setConfig(config);
        return iniFormatter;
    }

    @Override
    public void endIni() {
        this.getOutput().flush();
    }

    @Override
    public void endSection() {
        this.getOutput().print(this.getConfig().getLineSeparator());
    }

    @Override
    public void startIni() {
    }

    @Override
    public void startSection(String string2) {
        this.setHeader(false);
        if (!this.getConfig().isGlobalSection() || !string2.equals(this.getConfig().getGlobalSectionName())) {
            this.getOutput().print('[');
            this.getOutput().print(this.escapeKey(string2));
            this.getOutput().print(']');
            this.getOutput().print(this.getConfig().getLineSeparator());
        }
    }
}

