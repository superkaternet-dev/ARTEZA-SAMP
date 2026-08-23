/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.logging;

import com.google.firebase.database.logging.Logger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DefaultLogger
implements Logger {
    private final Set<String> enabledComponents;
    private final Logger.Level minLevel;

    public DefaultLogger(Logger.Level level, List<String> list) {
        this.enabledComponents = list != null ? new HashSet<String>(list) : null;
        this.minLevel = level;
    }

    protected String buildLogMessage(Logger.Level level, String string2, String string3, long l) {
        Date date = new Date(l);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(date.toString());
        stringBuilder.append(" [");
        stringBuilder.append((Object)level);
        stringBuilder.append("] ");
        stringBuilder.append(string2);
        stringBuilder.append(": ");
        stringBuilder.append(string3);
        return stringBuilder.toString();
    }

    protected void debug(String string2, String string3) {
        System.out.println(string3);
    }

    protected void error(String string2, String string3) {
        System.err.println(string3);
    }

    @Override
    public Logger.Level getLogLevel() {
        return this.minLevel;
    }

    protected void info(String string2, String string3) {
        System.out.println(string3);
    }

    @Override
    public void onLogMessage(Logger.Level level, String string2, String string3, long l) {
        if (this.shouldLog(level, string2)) {
            string3 = this.buildLogMessage(level, string2, string3, l);
            switch (1.$SwitchMap$com$google$firebase$database$logging$Logger$Level[level.ordinal()]) {
                default: {
                    throw new RuntimeException("Should not reach here!");
                }
                case 4: {
                    this.debug(string2, string3);
                    break;
                }
                case 3: {
                    this.info(string2, string3);
                    break;
                }
                case 2: {
                    this.warn(string2, string3);
                    break;
                }
                case 1: {
                    this.error(string2, string3);
                }
            }
        }
    }

    protected boolean shouldLog(Logger.Level level, String string2) {
        boolean bl = level.ordinal() >= this.minLevel.ordinal() && (this.enabledComponents == null || level.ordinal() > Logger.Level.DEBUG.ordinal() || this.enabledComponents.contains(string2));
        return bl;
    }

    protected void warn(String string2, String string3) {
        System.out.println(string3);
    }
}

