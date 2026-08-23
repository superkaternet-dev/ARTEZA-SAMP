/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.logging;

public interface Logger {
    public Level getLogLevel();

    public void onLogMessage(Level var1, String var2, String var3, long var4);

    public static final class Level
    extends Enum<Level> {
        private static final Level[] $VALUES;
        public static final /* enum */ Level DEBUG;
        public static final /* enum */ Level ERROR;
        public static final /* enum */ Level INFO;
        public static final /* enum */ Level NONE;
        public static final /* enum */ Level WARN;

        static {
            Level level;
            Level level2;
            Level level3;
            Level level4;
            Level level5;
            DEBUG = level5 = new Level();
            INFO = level4 = new Level();
            WARN = level3 = new Level();
            ERROR = level2 = new Level();
            NONE = level = new Level();
            $VALUES = new Level[]{level5, level4, level3, level2, level};
        }

        public static Level valueOf(String string2) {
            return Enum.valueOf(Level.class, string2);
        }

        public static Level[] values() {
            return (Level[])$VALUES.clone();
        }
    }
}

