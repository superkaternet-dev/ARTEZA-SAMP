/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.logging;

import com.google.firebase.database.logging.Logger;
import java.io.PrintWriter;
import java.io.StringWriter;

public class LogWrapper {
    private final String component;
    private final Logger logger;
    private final String prefix;

    public LogWrapper(Logger logger, String string2) {
        this(logger, string2, null);
    }

    public LogWrapper(Logger logger, String string2, String string3) {
        this.logger = logger;
        this.component = string2;
        this.prefix = string3;
    }

    private static String exceptionStacktrace(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private long now() {
        return System.currentTimeMillis();
    }

    private String toLog(String string2, Object ... object) {
        if (((Object[])object).length > 0) {
            string2 = String.format(string2, (Object[])object);
        }
        if (this.prefix != null) {
            object = new StringBuilder();
            ((StringBuilder)object).append(this.prefix);
            ((StringBuilder)object).append(" - ");
            ((StringBuilder)object).append(string2);
            string2 = ((StringBuilder)object).toString();
        }
        return string2;
    }

    public void debug(String object, Throwable throwable, Object ... object2) {
        if (this.logsDebug()) {
            object2 = this.toLog((String)object, object2);
            object = object2;
            if (throwable != null) {
                object = new StringBuilder();
                ((StringBuilder)object).append((String)object2);
                ((StringBuilder)object).append("\n");
                ((StringBuilder)object).append(LogWrapper.exceptionStacktrace(throwable));
                object = ((StringBuilder)object).toString();
            }
            this.logger.onLogMessage(Logger.Level.DEBUG, this.component, (String)object, this.now());
        }
    }

    public void debug(String string2, Object ... objectArray) {
        this.debug(string2, null, objectArray);
    }

    public void error(String string2, Throwable throwable) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.toLog(string2, new Object[0]));
        stringBuilder.append("\n");
        stringBuilder.append(LogWrapper.exceptionStacktrace(throwable));
        string2 = stringBuilder.toString();
        this.logger.onLogMessage(Logger.Level.ERROR, this.component, string2, this.now());
    }

    public void info(String string2) {
        this.logger.onLogMessage(Logger.Level.INFO, this.component, this.toLog(string2, new Object[0]), this.now());
    }

    public boolean logsDebug() {
        boolean bl = this.logger.getLogLevel().ordinal() <= Logger.Level.DEBUG.ordinal();
        return bl;
    }

    public void warn(String string2) {
        this.warn(string2, null);
    }

    public void warn(String charSequence, Throwable throwable) {
        String string2 = this.toLog((String)charSequence, new Object[0]);
        charSequence = string2;
        if (throwable != null) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append(string2);
            ((StringBuilder)charSequence).append("\n");
            ((StringBuilder)charSequence).append(LogWrapper.exceptionStacktrace(throwable));
            charSequence = ((StringBuilder)charSequence).toString();
        }
        this.logger.onLogMessage(Logger.Level.WARN, this.component, (String)charSequence, this.now());
    }
}

