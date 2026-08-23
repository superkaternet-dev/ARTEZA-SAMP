/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 *  android.text.TextUtils
 */
package com.bumptech.glide.load.model;

import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.util.Preconditions;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Map;

public class GlideUrl
implements Key {
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%;$";
    private volatile byte[] cacheKeyBytes;
    private int hashCode;
    private final Headers headers;
    private String safeStringUrl;
    private URL safeUrl;
    private final String stringUrl;
    private final URL url;

    public GlideUrl(String string2) {
        this(string2, Headers.DEFAULT);
    }

    public GlideUrl(String string2, Headers headers) {
        this.url = null;
        this.stringUrl = Preconditions.checkNotEmpty(string2);
        this.headers = Preconditions.checkNotNull(headers);
    }

    public GlideUrl(URL uRL) {
        this(uRL, Headers.DEFAULT);
    }

    public GlideUrl(URL uRL, Headers headers) {
        this.url = Preconditions.checkNotNull(uRL);
        this.stringUrl = null;
        this.headers = Preconditions.checkNotNull(headers);
    }

    private byte[] getCacheKeyBytes() {
        if (this.cacheKeyBytes == null) {
            this.cacheKeyBytes = this.getCacheKey().getBytes(CHARSET);
        }
        return this.cacheKeyBytes;
    }

    private String getSafeStringUrl() {
        if (TextUtils.isEmpty((CharSequence)this.safeStringUrl)) {
            String string2;
            String string3 = string2 = this.stringUrl;
            if (TextUtils.isEmpty((CharSequence)string2)) {
                string3 = Preconditions.checkNotNull(this.url).toString();
            }
            this.safeStringUrl = Uri.encode((String)string3, (String)ALLOWED_URI_CHARS);
        }
        return this.safeStringUrl;
    }

    private URL getSafeUrl() throws MalformedURLException {
        if (this.safeUrl == null) {
            this.safeUrl = new URL(this.getSafeStringUrl());
        }
        return this.safeUrl;
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof GlideUrl;
        boolean bl2 = false;
        if (bl) {
            object = (GlideUrl)object;
            bl = bl2;
            if (this.getCacheKey().equals(((GlideUrl)object).getCacheKey())) {
                bl = bl2;
                if (this.headers.equals(((GlideUrl)object).headers)) {
                    bl = true;
                }
            }
            return bl;
        }
        return false;
    }

    public String getCacheKey() {
        String string2 = this.stringUrl;
        if (string2 == null) {
            string2 = Preconditions.checkNotNull(this.url).toString();
        }
        return string2;
    }

    public Map<String, String> getHeaders() {
        return this.headers.getHeaders();
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            int n;
            this.hashCode = n = this.getCacheKey().hashCode();
            this.hashCode = n * 31 + this.headers.hashCode();
        }
        return this.hashCode;
    }

    public String toString() {
        return this.getCacheKey();
    }

    public String toStringUrl() {
        return this.getSafeStringUrl();
    }

    public URL toURL() throws MalformedURLException {
        return this.getSafeUrl();
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        messageDigest.update(this.getCacheKeyBytes());
    }
}

