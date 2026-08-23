/*
 * Decompiled with CFR 0.152.
 */
package com.liulishuo.okdownload.core.connection;

import com.liulishuo.okdownload.core.connection.DownloadConnection;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class DownloadUrlConnection
implements DownloadConnection,
DownloadConnection.Connected {
    protected URLConnection connection;

    public DownloadUrlConnection(String string2) throws IOException {
        this(string2, null);
    }

    public DownloadUrlConnection(String string2, Configuration configuration) throws IOException {
        this(new URL(string2), configuration);
    }

    public DownloadUrlConnection(URL uRL, Configuration configuration) throws IOException {
        this.connection = configuration != null && configuration.proxy != null ? uRL.openConnection(configuration.proxy) : uRL.openConnection();
        if (configuration != null) {
            if (configuration.readTimeout != null) {
                this.connection.setReadTimeout(configuration.readTimeout);
            }
            if (configuration.connectTimeout != null) {
                this.connection.setConnectTimeout(configuration.connectTimeout);
            }
        }
    }

    DownloadUrlConnection(URLConnection uRLConnection) {
        this.connection = uRLConnection;
    }

    @Override
    public void addHeader(String string2, String string3) {
        this.connection.addRequestProperty(string2, string3);
    }

    @Override
    public DownloadConnection.Connected execute() throws IOException {
        this.connection.connect();
        return this;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.connection.getInputStream();
    }

    @Override
    public Map<String, List<String>> getRequestProperties() {
        return this.connection.getRequestProperties();
    }

    @Override
    public String getRequestProperty(String string2) {
        return this.connection.getRequestProperty(string2);
    }

    @Override
    public int getResponseCode() throws IOException {
        URLConnection uRLConnection = this.connection;
        if (uRLConnection instanceof HttpURLConnection) {
            return ((HttpURLConnection)uRLConnection).getResponseCode();
        }
        return 0;
    }

    @Override
    public String getResponseHeaderField(String string2) {
        return this.connection.getHeaderField(string2);
    }

    @Override
    public Map<String, List<String>> getResponseHeaderFields() {
        return this.connection.getHeaderFields();
    }

    @Override
    public void release() {
        try {
            this.connection.getInputStream().close();
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    @Override
    public boolean setRequestMethod(String string2) throws ProtocolException {
        URLConnection uRLConnection = this.connection;
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

    public static class Factory
    implements DownloadConnection.Factory {
        private final Configuration configuration;

        public Factory() {
            this(null);
        }

        public Factory(Configuration configuration) {
            this.configuration = configuration;
        }

        @Override
        public DownloadConnection create(String string2) throws IOException {
            return new DownloadUrlConnection(string2, this.configuration);
        }

        DownloadConnection create(URL uRL) throws IOException {
            return new DownloadUrlConnection(uRL, this.configuration);
        }
    }
}

