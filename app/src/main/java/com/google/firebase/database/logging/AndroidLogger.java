/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.google.firebase.database.logging;

import android.util.Log;
import com.google.firebase.database.logging.DefaultLogger;
import com.google.firebase.database.logging.Logger;
import java.util.List;

public class AndroidLogger
extends DefaultLogger {
    public AndroidLogger(Logger.Level level, List<String> list) {
        super(level, list);
    }

    @Override
    protected String buildLogMessage(Logger.Level level, String string2, String string3, long l) {
        return string3;
    }

    @Override
    protected void debug(String string2, String string3) {
        Log.d((String)string2, (String)string3);
    }

    @Override
    protected void error(String string2, String string3) {
        Log.e((String)string2, (String)string3);
    }

    @Override
    protected void info(String string2, String string3) {
        Log.i((String)string2, (String)string3);
    }

    @Override
    protected void warn(String string2, String string3) {
        Log.w((String)string2, (String)string3);
    }
}

