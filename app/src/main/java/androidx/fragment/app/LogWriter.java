/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package androidx.fragment.app;

import android.util.Log;
import java.io.Writer;

final class LogWriter
extends Writer {
    private StringBuilder mBuilder = new StringBuilder(128);
    private final String mTag;

    LogWriter(String string2) {
        this.mTag = string2;
    }

    private void flushBuilder() {
        if (this.mBuilder.length() > 0) {
            Log.d((String)this.mTag, (String)this.mBuilder.toString());
            StringBuilder stringBuilder = this.mBuilder;
            stringBuilder.delete(0, stringBuilder.length());
        }
    }

    @Override
    public void close() {
        this.flushBuilder();
    }

    @Override
    public void flush() {
        this.flushBuilder();
    }

    @Override
    public void write(char[] cArray, int n, int n2) {
        for (int i = 0; i < n2; ++i) {
            char c = cArray[n + i];
            if (c == '\n') {
                this.flushBuilder();
                continue;
            }
            this.mBuilder.append(c);
        }
    }
}

