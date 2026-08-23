/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.filedownloader.connection;

import com.liulishuo.filedownloader.connection.FileDownloadConnection;
import com.liulishuo.filedownloader.util.FileDownloadHelper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class FileDownloadUrlConnection
implements FileDownloadConnection {
    protected URLConnection mConnection;

    public FileDownloadUrlConnection(String string2) throws IOException {
        this(string2, null);
    }

    public FileDownloadUrlConnection(String string2, Configuration configuration) throws IOException {
        this(new URL(string2), configuration);
    }

    public FileDownloadUrlConnection(URL uRL, Configuration configuration) throws IOException {
        this.mConnection = configuration != null && configuration.proxy != null ? uRL.openConnection(configuration.proxy) : uRL.openConnection();
        if (configuration != null) {
            if (configuration.readTimeout != null) {
                this.mConnection.setReadTimeout(configuration.readTimeout);
            }
            if (configuration.connectTimeout != null) {
                this.mConnection.setConnectTimeout(configuration.connectTimeout);
            }
        }
    }

    @Override
    public void addHeader(String string2, String string3) {
        this.mConnection.addRequestProperty(string2, string3);
    }

    @Override
    public boolean dispatchAddResumeOffset(String string2, long l) {
        return false;
    }

    @Override
    public void ending() {
        try {
            this.mConnection.getInputStream().close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    @Override
    public void execute() throws IOException {
        this.mConnection.connect();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.mConnection.getInputStream();
    }

    @Override
    public Map<String, List<String>> getRequestHeaderFields() {
        return this.mConnection.getRequestProperties();
    }

    @Override
    public int getResponseCode() throws IOException {
        URLConnection uRLConnection = this.mConnection;
        if (uRLConnection instanceof HttpURLConnection) {
            return ((HttpURLConnection)uRLConnection).getResponseCode();
        }
        return 0;
    }

    @Override
    public String getResponseHeaderField(String string2) {
        return this.mConnection.getHeaderField(string2);
    }

    @Override
    public Map<String, List<String>> getResponseHeaderFields() {
        return this.mConnection.getHeaderFields();
    }

    @Override
    public boolean setRequestMethod(String string2) throws ProtocolException {
        URLConnection uRLConnection = this.mConnection;
        if (uRLConnection instanceof HttpURLConnection) {
            ((HttpURLConnection)uRLConnection).setRequestMethod(string2);
            return true;
        }
        return false;
    }

    public static class Configuration {
        private Integer connectTimeout;
        private Proxy proxy;
        private Integer readTimeout;

        public Configuration connectTimeout(int n) {
            this.connectTimeout = n;
            return this;
        }

        public Configuration proxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        public Configuration readTimeout(int n) {
            this.readTimeout = n;
            return this;
        }
    }

    public static class Creator
    implements FileDownloadHelper.ConnectionCreator {
        private final Configuration mConfiguration;

        public Creator() {
            this(null);
        }

        public Creator(Configuration configuration) {
            this.mConfiguration = configuration;
        }

        @Override
        public FileDownloadConnection create(String string2) throws IOException {
            return new FileDownloadUrlConnection(string2, this.mConfiguration);
        }

        FileDownloadConnection create(URL uRL) throws IOException {
            return new FileDownloadUrlConnection(uRL, this.mConfiguration);
        }
    }
}

