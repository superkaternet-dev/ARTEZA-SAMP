/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.bumptech.glide.load.model;

import android.text.TextUtils;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class LazyHeaders
implements Headers {
    private volatile Map<String, String> combinedHeaders;
    private final Map<String, List<LazyHeaderFactory>> headers;

    LazyHeaders(Map<String, List<LazyHeaderFactory>> map) {
        this.headers = Collections.unmodifiableMap(map);
    }

    private String buildHeaderValue(List<LazyHeaderFactory> list) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = list.size();
        for (int i = 0; i < n; ++i) {
            String string2 = list.get(i).buildHeader();
            if (TextUtils.isEmpty((CharSequence)string2)) continue;
            stringBuilder.append(string2);
            if (i == list.size() - 1) continue;
            stringBuilder.append(',');
        }
        return stringBuilder.toString();
    }

    private Map<String, String> generateHeaders() {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        for (Map.Entry<String, List<LazyHeaderFactory>> entry : this.headers.entrySet()) {
            String string2 = this.buildHeaderValue(entry.getValue());
            if (TextUtils.isEmpty((CharSequence)string2)) continue;
            hashMap.put(entry.getKey(), string2);
        }
        return hashMap;
    }

    public boolean equals(Object object) {
        if (object instanceof LazyHeaders) {
            object = (LazyHeaders)object;
            return this.headers.equals(((LazyHeaders)object).headers);
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Map<String, String> getHeaders() {
        if (this.combinedHeaders != null) return this.combinedHeaders;
        synchronized (this) {
            if (this.combinedHeaders != null) return this.combinedHeaders;
            this.combinedHeaders = Collections.unmodifiableMap(this.generateHeaders());
            return this.combinedHeaders;
        }
    }

    public int hashCode() {
        return this.headers.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LazyHeaders{headers=");
        stringBuilder.append(this.headers);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static final class Builder {
        private static final Map<String, List<LazyHeaderFactory>> DEFAULT_HEADERS;
        private static final String DEFAULT_USER_AGENT;
        private static final String USER_AGENT_HEADER = "User-Agent";
        private boolean copyOnModify = true;
        private Map<String, List<LazyHeaderFactory>> headers = DEFAULT_HEADERS;
        private boolean isUserAgentDefault = true;

        static {
            String string2;
            DEFAULT_USER_AGENT = string2 = Builder.getSanitizedUserAgent();
            HashMap<String, List<StringHeaderFactory>> hashMap = new HashMap<String, List<StringHeaderFactory>>(2);
            if (!TextUtils.isEmpty((CharSequence)string2)) {
                hashMap.put(USER_AGENT_HEADER, Collections.singletonList(new StringHeaderFactory(string2)));
            }
            DEFAULT_HEADERS = Collections.unmodifiableMap(hashMap);
        }

        private Map<String, List<LazyHeaderFactory>> copyHeaders() {
            HashMap<String, List<LazyHeaderFactory>> hashMap = new HashMap<String, List<LazyHeaderFactory>>(this.headers.size());
            for (Map.Entry<String, List<LazyHeaderFactory>> entry : this.headers.entrySet()) {
                ArrayList arrayList = new ArrayList(entry.getValue());
                hashMap.put(entry.getKey(), arrayList);
            }
            return hashMap;
        }

        private void copyIfNecessary() {
            if (this.copyOnModify) {
                this.copyOnModify = false;
                this.headers = this.copyHeaders();
            }
        }

        private List<LazyHeaderFactory> getFactories(String string2) {
            List<LazyHeaderFactory> list;
            List<LazyHeaderFactory> list2 = list = this.headers.get(string2);
            if (list == null) {
                list2 = new ArrayList<LazyHeaderFactory>();
                this.headers.put(string2, list2);
            }
            return list2;
        }

        static String getSanitizedUserAgent() {
            String string2 = System.getProperty("http.agent");
            if (TextUtils.isEmpty((CharSequence)string2)) {
                return string2;
            }
            int n = string2.length();
            StringBuilder stringBuilder = new StringBuilder(string2.length());
            for (int i = 0; i < n; ++i) {
                char c = string2.charAt(i);
                if ((c > '\u001f' || c == '\t') && c < '\u007f') {
                    stringBuilder.append(c);
                    continue;
                }
                stringBuilder.append('?');
            }
            return stringBuilder.toString();
        }

        public Builder addHeader(String string2, LazyHeaderFactory lazyHeaderFactory) {
            if (this.isUserAgentDefault && USER_AGENT_HEADER.equalsIgnoreCase(string2)) {
                return this.setHeader(string2, lazyHeaderFactory);
            }
            this.copyIfNecessary();
            this.getFactories(string2).add(lazyHeaderFactory);
            return this;
        }

        public Builder addHeader(String string2, String string3) {
            return this.addHeader(string2, new StringHeaderFactory(string3));
        }

        public LazyHeaders build() {
            this.copyOnModify = true;
            return new LazyHeaders(this.headers);
        }

        public Builder setHeader(String string2, LazyHeaderFactory lazyHeaderFactory) {
            this.copyIfNecessary();
            if (lazyHeaderFactory == null) {
                this.headers.remove(string2);
            } else {
                List<LazyHeaderFactory> list = this.getFactories(string2);
                list.clear();
                list.add(lazyHeaderFactory);
            }
            if (this.isUserAgentDefault && USER_AGENT_HEADER.equalsIgnoreCase(string2)) {
                this.isUserAgentDefault = false;
            }
            return this;
        }

        public Builder setHeader(String string2, String object) {
            object = object == null ? null : new StringHeaderFactory((String)object);
            return this.setHeader(string2, (LazyHeaderFactory)object);
        }
    }

    static final class StringHeaderFactory
    implements LazyHeaderFactory {
        private final String value;

        StringHeaderFactory(String string2) {
            this.value = string2;
        }

        @Override
        public String buildHeader() {
            return this.value;
        }

        public boolean equals(Object object) {
            if (object instanceof StringHeaderFactory) {
                object = (StringHeaderFactory)object;
                return this.value.equals(((StringHeaderFactory)object).value);
            }
            return false;
        }

        public int hashCode() {
            return this.value.hashCode();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("StringHeaderFactory{value='");
            stringBuilder.append(this.value);
            stringBuilder.append('\'');
            stringBuilder.append('}');
            return stringBuilder.toString();
        }
    }
}

