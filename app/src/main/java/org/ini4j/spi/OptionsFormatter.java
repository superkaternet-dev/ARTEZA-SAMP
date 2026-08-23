/*
 * Decompiled with CFR 0.152.
 */
package org.ini4j.spi;

import java.io.PrintWriter;
import java.io.Writer;
import org.ini4j.Config;
import org.ini4j.spi.AbstractFormatter;
import org.ini4j.spi.OptionsHandler;
import org.ini4j.spi.ServiceFinder;

public class OptionsFormatter
extends AbstractFormatter
implements OptionsHandler {
    private static OptionsFormatter newInstance() {
        return ServiceFinder.findService(OptionsFormatter.class);
    }

    public static OptionsFormatter newInstance(Writer writer, Config config) {
        OptionsFormatter optionsFormatter = OptionsFormatter.newInstance();
        writer = writer instanceof PrintWriter ? (PrintWriter)writer : new PrintWriter(writer);
        optionsFormatter.setOutput((PrintWriter)writer);
        optionsFormatter.setConfig(config);
        return optionsFormatter;
    }

    @Override
    public void endOptions() {
        this.getOutput().flush();
    }

    @Override
    public void startOptions() {
    }
}

