/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.connection;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.util.List;
import java.util.Map;

public interface FileDownloadConnection {
    public static final int NO_RESPONSE_CODE = 0;
    public static final int RESPONSE_CODE_FROM_OFFSET = 1;

    public void addHeader(String var1, String var2);

    public boolean dispatchAddResumeOffset(String var1, long var2);

    public void ending();

    public void execute() throws IOException;

    public InputStream getInputStream() throws IOException;

    public Map<String, List<String>> getRequestHeaderFields();

    public int getResponseCode() throws IOException;

    public String getResponseHeaderField(String var1);

    public Map<String, List<String>> getResponseHeaderFields();

    public boolean setRequestMethod(String var1) throws ProtocolException;
}

