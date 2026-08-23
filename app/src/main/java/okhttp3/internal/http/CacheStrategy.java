/*
 * Decompiled with CFR 0.152.
 */
package okhttp3.internal.http;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.http.HeaderParser;
import okhttp3.internal.http.HttpDate;

public final class CacheStrategy {
    public final Response cacheResponse;
    public final Request networkRequest;

    private CacheStrategy(Request request, Response response) {
        this.networkRequest = request;
        this.cacheResponse = response;
    }

    public static boolean isCacheable(Response response, Request request) {
        int n = response.code();
        boolean bl = false;
        switch (n) {
            default: {
                break;
            }
            case 302: 
            case 307: {
                if (response.header("Expires") == null && response.cacheControl().maxAgeSeconds() == -1 && !response.cacheControl().isPublic() && !response.cacheControl().isPrivate()) break;
            }
            case 200: 
            case 203: 
            case 204: 
            case 300: 
            case 301: 
            case 308: 
            case 404: 
            case 405: 
            case 410: 
            case 414: 
            case 501: {
                boolean bl2 = bl;
                if (!response.cacheControl().noStore()) {
                    bl2 = bl;
                    if (!request.cacheControl().noStore()) {
                        bl2 = true;
                    }
                }
                return bl2;
            }
        }
        return false;
    }

    public static class Factory {
        private int ageSeconds = -1;
        final Response cacheResponse;
        private String etag;
        private Date expires;
        private Date lastModified;
        private String lastModifiedString;
        final long nowMillis;
        private long receivedResponseMillis;
        final Request request;
        private long sentRequestMillis;
        private Date servedDate;
        private String servedDateString;

        public Factory(long l, Request object, Response object2) {
            this.nowMillis = l;
            this.request = object;
            this.cacheResponse = object2;
            if (object2 != null) {
                this.sentRequestMillis = ((Response)object2).sentRequestAtMillis();
                this.receivedResponseMillis = ((Response)object2).receivedResponseAtMillis();
                object2 = ((Response)object2).headers();
                int n = ((Headers)object2).size();
                for (int i = 0; i < n; ++i) {
                    String string2 = ((Headers)object2).name(i);
                    object = ((Headers)object2).value(i);
                    if ("Date".equalsIgnoreCase(string2)) {
                        this.servedDate = HttpDate.parse((String)object);
                        this.servedDateString = object;
                        continue;
                    }
                    if ("Expires".equalsIgnoreCase(string2)) {
                        this.expires = HttpDate.parse((String)object);
                        continue;
                    }
                    if ("Last-Modified".equalsIgnoreCase(string2)) {
                        this.lastModified = HttpDate.parse((String)object);
                        this.lastModifiedString = object;
                        continue;
                    }
                    if ("ETag".equalsIgnoreCase(string2)) {
                        this.etag = object;
                        continue;
                    }
                    if (!"Age".equalsIgnoreCase(string2)) continue;
                    this.ageSeconds = HeaderParser.parseSeconds((String)object, -1);
                }
            }
        }

        private long cacheResponseAge() {
            Date date = this.servedDate;
            long l = 0L;
            if (date != null) {
                l = Math.max(0L, this.receivedResponseMillis - date.getTime());
            }
            if (this.ageSeconds != -1) {
                l = Math.max(l, TimeUnit.SECONDS.toMillis(this.ageSeconds));
            }
            long l2 = this.receivedResponseMillis;
            return l + (l2 - this.sentRequestMillis) + (this.nowMillis - l2);
        }

        private long computeFreshnessLifetime() {
            Object object = this.cacheResponse.cacheControl();
            if (((CacheControl)object).maxAgeSeconds() != -1) {
                return TimeUnit.SECONDS.toMillis(((CacheControl)object).maxAgeSeconds());
            }
            object = this.expires;
            long l = 0L;
            if (object != null) {
                object = this.servedDate;
                long l2 = object != null ? ((Date)object).getTime() : this.receivedResponseMillis;
                l2 = this.expires.getTime() - l2;
                if (l2 > 0L) {
                    l = l2;
                }
                return l;
            }
            if (this.lastModified != null && this.cacheResponse.request().url().query() == null) {
                object = this.servedDate;
                long l3 = object != null ? ((Date)object).getTime() : this.sentRequestMillis;
                if ((l3 -= this.lastModified.getTime()) > 0L) {
                    l = l3 / 10L;
                }
                return l;
            }
            return 0L;
        }

        private CacheStrategy getCandidate() {
            if (this.cacheResponse == null) {
                return new CacheStrategy(this.request, null);
            }
            if (this.request.isHttps() && this.cacheResponse.handshake() == null) {
                return new CacheStrategy(this.request, null);
            }
            if (!CacheStrategy.isCacheable(this.cacheResponse, this.request)) {
                return new CacheStrategy(this.request, null);
            }
            Object object = this.request.cacheControl();
            if (!((CacheControl)object).noCache() && !Factory.hasConditions(this.request)) {
                long l;
                long l2 = this.cacheResponseAge();
                long l3 = l = this.computeFreshnessLifetime();
                if (((CacheControl)object).maxAgeSeconds() != -1) {
                    l3 = Math.min(l, TimeUnit.SECONDS.toMillis(((CacheControl)object).maxAgeSeconds()));
                }
                l = 0L;
                if (((CacheControl)object).minFreshSeconds() != -1) {
                    l = TimeUnit.SECONDS.toMillis(((CacheControl)object).minFreshSeconds());
                }
                long l4 = 0L;
                Object object2 = this.cacheResponse.cacheControl();
                long l5 = l4;
                if (!((CacheControl)object2).mustRevalidate()) {
                    l5 = l4;
                    if (((CacheControl)object).maxStaleSeconds() != -1) {
                        l5 = TimeUnit.SECONDS.toMillis(((CacheControl)object).maxStaleSeconds());
                    }
                }
                if (!((CacheControl)object2).noCache() && l2 + l < l3 + l5) {
                    object2 = this.cacheResponse.newBuilder();
                    if (l2 + l >= l3) {
                        ((Response.Builder)object2).addHeader("Warning", "110 HttpURLConnection \"Response is stale\"");
                    }
                    if (l2 > 86400000L && this.isFreshnessLifetimeHeuristic()) {
                        ((Response.Builder)object2).addHeader("Warning", "113 HttpURLConnection \"Heuristic expiration\"");
                    }
                    return new CacheStrategy(null, ((Response.Builder)object2).build());
                }
                object = this.request.newBuilder();
                object2 = this.etag;
                if (object2 != null) {
                    ((Request.Builder)object).header("If-None-Match", (String)object2);
                } else if (this.lastModified != null) {
                    ((Request.Builder)object).header("If-Modified-Since", this.lastModifiedString);
                } else if (this.servedDate != null) {
                    ((Request.Builder)object).header("If-Modified-Since", this.servedDateString);
                }
                object2 = ((Request.Builder)object).build();
                object2 = Factory.hasConditions((Request)object2) ? new CacheStrategy((Request)object2, this.cacheResponse) : new CacheStrategy((Request)object2, null);
                return object2;
            }
            return new CacheStrategy(this.request, null);
        }

        private static boolean hasConditions(Request request) {
            boolean bl = request.header("If-Modified-Since") != null || request.header("If-None-Match") != null;
            return bl;
        }

        private boolean isFreshnessLifetimeHeuristic() {
            boolean bl = this.cacheResponse.cacheControl().maxAgeSeconds() == -1 && this.expires == null;
            return bl;
        }

        public CacheStrategy get() {
            CacheStrategy cacheStrategy = this.getCandidate();
            if (cacheStrategy.networkRequest != null && this.request.cacheControl().onlyIfCached()) {
                return new CacheStrategy(null, null);
            }
            return cacheStrategy;
        }
    }
}

