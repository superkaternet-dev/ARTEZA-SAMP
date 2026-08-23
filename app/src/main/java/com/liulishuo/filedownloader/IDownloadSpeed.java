/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader;

public interface IDownloadSpeed {

    public static interface Lookup {
        public int getSpeed();

        public void setMinIntervalUpdateSpeed(int var1);
    }

    public static interface Monitor {
        public void end(long var1);

        public void reset();

        public void start(long var1);

        public void update(long var1);
    }
}

