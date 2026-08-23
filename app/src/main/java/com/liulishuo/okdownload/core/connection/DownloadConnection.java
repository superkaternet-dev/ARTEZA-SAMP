/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.util.List;
import java.util.Map;

public interface DownloadConnection {
    public static final int NO_RESPONSE_CODE = 0;

    public void addHeader(String var1, String var2);

    public Connected execute() throws IOException;

    public Map<String, List<String>> getRequestProperties();

    public String getRequestProperty(String var1);

    public void release();

    public boolean setRequestMethod(String var1) throws ProtocolException;

    public static interface Connected {
        public InputStream getInputStream() throws IOException;

        public int getResponseCode() throws IOException;

        public String getResponseHeaderField(String var1);

        public Map<String, List<String>> getResponseHeaderFields();
    }

    public static interface Factory {
        public DownloadConnection create(String var1) throws IOException;
    }
}

