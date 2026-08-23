/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  android.util.Log
 */
package com.bumptech.glide.load.data;

import android.text.TextUtils;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.HttpException;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.util.ContentLengthInputStream;
import com.bumptech.glide.util.LogTime;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class HttpUrlFetcher
implements DataFetcher<InputStream> {
    static final HttpUrlConnectionFactory DEFAULT_CONNECTION_FACTORY = new DefaultHttpUrlConnectionFactory();
    static final int INVALID_STATUS_CODE = -1;
    private static final int MAXIMUM_REDIRECTS = 5;
    static final String REDIRECT_HEADER_FIELD = "Location";
    private static final String TAG = "HttpUrlFetcher";
    private final HttpUrlConnectionFactory connectionFactory;
    private final GlideUrl glideUrl;
    private volatile boolean isCancelled;
    private InputStream stream;
    private final int timeout;
    private HttpURLConnection urlConnection;

    public HttpUrlFetcher(GlideUrl glideUrl, int n) {
        this(glideUrl, n, DEFAULT_CONNECTION_FACTORY);
    }

    HttpUrlFetcher(GlideUrl glideUrl, int n, HttpUrlConnectionFactory httpUrlConnectionFactory) {
        this.glideUrl = glideUrl;
        this.timeout = n;
        this.connectionFactory = httpUrlConnectionFactory;
    }

    private HttpURLConnection buildAndConfigureConnection(URL object, Map<String, String> object2) throws HttpException {
        try {
            object = this.connectionFactory.build((URL)object);
        }
        catch (IOException iOException) {
            HttpException httpException = new HttpException("URL.openConnection threw", 0, iOException);
            throw httpException;
        }
        for (Map.Entry entry : object2.entrySet()) {
            ((URLConnection)object).addRequestProperty((String)entry.getKey(), (String)entry.getValue());
        }
        ((URLConnection)object).setConnectTimeout(this.timeout);
        ((URLConnection)object).setReadTimeout(this.timeout);
        ((URLConnection)object).setUseCaches(false);
        ((URLConnection)object).setDoInput(true);
        ((HttpURLConnection)object).setInstanceFollowRedirects(false);
        return object;
    }

    private static int getHttpStatusCodeOrInvalid(HttpURLConnection httpURLConnection) {
        try {
            int n = httpURLConnection.getResponseCode();
            return n;
        }
        catch (IOException iOException) {
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"Failed to get a response code", (Throwable)iOException);
            }
            return -1;
        }
    }

    private InputStream getStreamForSuccessfulRequest(HttpURLConnection httpURLConnection) throws HttpException {
        try {
            if (TextUtils.isEmpty((CharSequence)httpURLConnection.getContentEncoding())) {
                int n = httpURLConnection.getContentLength();
                this.stream = ContentLengthInputStream.obtain(httpURLConnection.getInputStream(), n);
            } else {
                if (Log.isLoggable((String)TAG, (int)3)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Got non empty content encoding: ");
                    stringBuilder.append(httpURLConnection.getContentEncoding());
                    Log.d((String)TAG, (String)stringBuilder.toString());
                }
                this.stream = httpURLConnection.getInputStream();
            }
            return this.stream;
        }
        catch (IOException iOException) {
            throw new HttpException("Failed to obtain InputStream", HttpUrlFetcher.getHttpStatusCodeOrInvalid(httpURLConnection), iOException);
        }
    }

    private static boolean isHttpOk(int n) {
        boolean bl = n / 100 == 2;
        return bl;
    }

    private static boolean isHttpRedirect(int n) {
        boolean bl = n / 100 == 3;
        return bl;
    }

    private InputStream loadDataWithRedirects(URL serializable, int n, URL object, Map<String, String> object2) throws HttpException {
        if (n < 5) {
            block15: {
                if (object != null) {
                    try {
                        if (serializable.toURI().equals(((URL)object).toURI())) {
                            object = new HttpException("In re-direct loop", -1);
                            throw object;
                        }
                    }
                    catch (URISyntaxException uRISyntaxException) {
                        // empty catch block
                    }
                }
                this.urlConnection = object = this.buildAndConfigureConnection((URL)serializable, (Map<String, String>)object2);
                try {
                    ((URLConnection)object).connect();
                    this.stream = this.urlConnection.getInputStream();
                    if (!this.isCancelled) break block15;
                    return null;
                }
                catch (IOException iOException) {
                    throw new HttpException("Failed to connect or obtain data", HttpUrlFetcher.getHttpStatusCodeOrInvalid(this.urlConnection), iOException);
                }
            }
            int n2 = HttpUrlFetcher.getHttpStatusCodeOrInvalid(this.urlConnection);
            if (HttpUrlFetcher.isHttpOk(n2)) {
                return this.getStreamForSuccessfulRequest(this.urlConnection);
            }
            if (HttpUrlFetcher.isHttpRedirect(n2)) {
                object = this.urlConnection.getHeaderField(REDIRECT_HEADER_FIELD);
                if (!TextUtils.isEmpty((CharSequence)object)) {
                    try {
                        URL uRL = new URL((URL)serializable, (String)object);
                        this.cleanup();
                        return this.loadDataWithRedirects(uRL, n + 1, (URL)serializable, (Map<String, String>)object2);
                    }
                    catch (MalformedURLException malformedURLException) {
                        object2 = new StringBuilder();
                        ((StringBuilder)object2).append("Bad redirect url: ");
                        ((StringBuilder)object2).append((String)object);
                        throw new HttpException(((StringBuilder)object2).toString(), n2, malformedURLException);
                    }
                }
                throw new HttpException("Received empty or null redirect url", n2);
            }
            if (n2 == -1) {
                throw new HttpException(n2);
            }
            try {
                serializable = new HttpException(this.urlConnection.getResponseMessage(), n2);
                throw serializable;
            }
            catch (IOException iOException) {
                throw new HttpException("Failed to get a response message", n2, iOException);
            }
        }
        throw new HttpException("Too many (> 5) redirects!", -1);
    }

    @Override
    public void cancel() {
        this.isCancelled = true;
    }

    @Override
    public void cleanup() {
        Object object = this.stream;
        if (object != null) {
            try {
                ((InputStream)object).close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        if ((object = this.urlConnection) != null) {
            ((HttpURLConnection)object).disconnect();
        }
        this.urlConnection = null;
    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void loadData(Priority object, DataFetcher.DataCallback<? super InputStream> dataCallback) {
        Throwable throwable2222222;
        long l;
        block9: {
            block10: {
                block11: {
                    l = LogTime.getLogTime();
                    dataCallback.onDataReady(this.loadDataWithRedirects(this.glideUrl.toURL(), 0, null, this.glideUrl.getHeaders()));
                    {
                        catch (Throwable throwable2222222) {
                            break block9;
                        }
                        catch (IOException iOException) {}
                        {
                            if (Log.isLoggable((String)TAG, (int)3)) {
                                Log.d((String)TAG, (String)"Failed to load data for url", (Throwable)iOException);
                            }
                            dataCallback.onLoadFailed(iOException);
                        }
                        if (!Log.isLoggable((String)TAG, (int)2)) break block10;
                        object = new StringBuilder();
                        break block11;
                    }
                    if (!Log.isLoggable((String)TAG, (int)2)) break block10;
                    object = new StringBuilder();
                }
                ((StringBuilder)object).append("Finished http url fetcher fetch in ");
                ((StringBuilder)object).append(LogTime.getElapsedMillis(l));
                Log.v((String)TAG, (String)((StringBuilder)object).toString());
            }
            return;
        }
        if (Log.isLoggable((String)TAG, (int)2)) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Finished http url fetcher fetch in ");
            ((StringBuilder)object).append(LogTime.getElapsedMillis(l));
            Log.v((String)TAG, (String)((StringBuilder)object).toString());
        }
        throw throwable2222222;
    }

    private static class DefaultHttpUrlConnectionFactory
    implements HttpUrlConnectionFactory {
        DefaultHttpUrlConnectionFactory() {
        }

        @Override
        public HttpURLConnection build(URL uRL) throws IOException {
            return (HttpURLConnection)uRL.openConnection();
        }
    }

    static interface HttpUrlConnectionFactory {
        public HttpURLConnection build(URL var1) throws IOException;
    }
}

