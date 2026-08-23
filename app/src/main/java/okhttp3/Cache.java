/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.Closeable;
import java.io.File;
import java.io.Flushable;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.CipherSuite;
import okhttp3.Handshake;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.TlsVersion;
import okhttp3.internal.DiskLruCache;
import okhttp3.internal.InternalCache;
import okhttp3.internal.Util;
import okhttp3.internal.http.CacheRequest;
import okhttp3.internal.http.CacheStrategy;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.OkHeaders;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.io.FileSystem;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.ForwardingSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Cache
implements Closeable,
Flushable {
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private static final int ENTRY_METADATA = 0;
    private static final int VERSION = 201105;
    private final DiskLruCache cache;
    private int hitCount;
    final InternalCache internalCache = new InternalCache(this){
        final Cache this$0;
        {
            this.this$0 = cache;
        }

        @Override
        public Response get(Request request) throws IOException {
            return this.this$0.get(request);
        }

        @Override
        public CacheRequest put(Response response) throws IOException {
            return this.this$0.put(response);
        }

        @Override
        public void remove(Request request) throws IOException {
            this.this$0.remove(request);
        }

        @Override
        public void trackConditionalCacheHit() {
            this.this$0.trackConditionalCacheHit();
        }

        @Override
        public void trackResponse(CacheStrategy cacheStrategy) {
            this.this$0.trackResponse(cacheStrategy);
        }

        @Override
        public void update(Response response, Response response2) throws IOException {
            this.this$0.update(response, response2);
        }
    };
    private int networkCount;
    private int requestCount;
    private int writeAbortCount;
    private int writeSuccessCount;

    public Cache(File file, long l) {
        this(file, l, FileSystem.SYSTEM);
    }

    Cache(File file, long l, FileSystem fileSystem) {
        this.cache = DiskLruCache.create(fileSystem, file, 201105, 2, l);
    }

    private void abortQuietly(DiskLruCache.Editor editor) {
        block2: {
            if (editor == null) break block2;
            try {
                editor.abort();
            }
            catch (IOException iOException) {}
        }
    }

    static /* synthetic */ int access$808(Cache cache) {
        int n = cache.writeSuccessCount;
        cache.writeSuccessCount = n + 1;
        return n;
    }

    static /* synthetic */ int access$908(Cache cache) {
        int n = cache.writeAbortCount;
        cache.writeAbortCount = n + 1;
        return n;
    }

    private CacheRequest put(Response object) throws IOException {
        Object object2 = ((Response)object).request().method();
        if (HttpMethod.invalidatesCache(((Response)object).request().method())) {
            try {
                this.remove(((Response)object).request());
            }
            catch (IOException iOException) {
                // empty catch block
            }
            return null;
        }
        if (!((String)object2).equals("GET")) {
            return null;
        }
        if (OkHeaders.hasVaryAll((Response)object)) {
            return null;
        }
        Entry entry = new Entry((Response)object);
        object2 = null;
        try {
            object = this.cache.edit(Cache.urlToKey(((Response)object).request()));
            if (object == null) {
                return null;
            }
            object2 = object;
        }
        catch (IOException iOException) {
            this.abortQuietly((DiskLruCache.Editor)object2);
            return null;
        }
        entry.writeTo((DiskLruCache.Editor)object);
        object2 = object;
        object = new CacheRequestImpl(this, (DiskLruCache.Editor)object);
        return object;
    }

    private static int readInt(BufferedSource object) throws IOException {
        long l;
        block4: {
            try {
                l = object.readDecimalLong();
                object = object.readUtf8LineStrict();
                if (l < 0L || l > Integer.MAX_VALUE) break block4;
            }
            catch (NumberFormatException numberFormatException) {
                throw new IOException(numberFormatException.getMessage());
            }
            if (!((String)object).isEmpty()) break block4;
            return (int)l;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("expected an int but was \"");
        stringBuilder.append(l);
        stringBuilder.append((String)object);
        stringBuilder.append("\"");
        IOException iOException = new IOException(stringBuilder.toString());
        throw iOException;
    }

    private void remove(Request request) throws IOException {
        this.cache.remove(Cache.urlToKey(request));
    }

    private void trackConditionalCacheHit() {
        synchronized (this) {
            ++this.hitCount;
            return;
        }
    }

    private void trackResponse(CacheStrategy cacheStrategy) {
        synchronized (this) {
            ++this.requestCount;
            if (cacheStrategy.networkRequest != null) {
                ++this.networkCount;
            } else if (cacheStrategy.cacheResponse != null) {
                ++this.hitCount;
            }
            return;
        }
    }

    private void update(Response object, Response object2) {
        block4: {
            Entry entry = new Entry((Response)object2);
            object2 = ((CacheResponseBody)((Response)object).body()).snapshot;
            object = null;
            object2 = ((DiskLruCache.Snapshot)object2).edit();
            if (object2 == null) break block4;
            object = object2;
            entry.writeTo((DiskLruCache.Editor)object2);
            object = object2;
            try {
                ((DiskLruCache.Editor)object2).commit();
            }
            catch (IOException iOException) {
                this.abortQuietly((DiskLruCache.Editor)object);
            }
        }
    }

    private static String urlToKey(Request request) {
        return Util.md5Hex(request.url().toString());
    }

    @Override
    public void close() throws IOException {
        this.cache.close();
    }

    public void delete() throws IOException {
        this.cache.delete();
    }

    public File directory() {
        return this.cache.getDirectory();
    }

    public void evictAll() throws IOException {
        this.cache.evictAll();
    }

    @Override
    public void flush() throws IOException {
        this.cache.flush();
    }

    Response get(Request request) {
        Object object;
        block5: {
            block4: {
                object = Cache.urlToKey(request);
                try {
                    object = this.cache.get((String)object);
                    if (object != null) break block4;
                    return null;
                }
                catch (IOException iOException) {
                    return null;
                }
            }
            try {
                Entry entry = new Entry(((DiskLruCache.Snapshot)object).getSource(0));
                object = entry.response((DiskLruCache.Snapshot)object);
                if (entry.matches(request, (Response)object)) break block5;
            }
            catch (IOException iOException) {
                Util.closeQuietly((Closeable)object);
                return null;
            }
            Util.closeQuietly(((Response)object).body());
            return null;
        }
        return object;
    }

    public int hitCount() {
        synchronized (this) {
            int n = this.hitCount;
            return n;
        }
    }

    public void initialize() throws IOException {
        this.cache.initialize();
    }

    public boolean isClosed() {
        return this.cache.isClosed();
    }

    public long maxSize() {
        return this.cache.getMaxSize();
    }

    public int networkCount() {
        synchronized (this) {
            int n = this.networkCount;
            return n;
        }
    }

    public int requestCount() {
        synchronized (this) {
            int n = this.requestCount;
            return n;
        }
    }

    public long size() throws IOException {
        return this.cache.size();
    }

    public Iterator<String> urls() throws IOException {
        return new Iterator<String>(this){
            boolean canRemove;
            final Iterator<DiskLruCache.Snapshot> delegate;
            String nextUrl;
            final Cache this$0;
            {
                this.this$0 = cache;
                this.delegate = cache.cache.snapshots();
            }

            @Override
            public boolean hasNext() {
                if (this.nextUrl != null) {
                    return true;
                }
                this.canRemove = false;
                while (this.delegate.hasNext()) {
                    DiskLruCache.Snapshot snapshot = this.delegate.next();
                    try {
                        this.nextUrl = Okio.buffer(snapshot.getSource(0)).readUtf8LineStrict();
                        return true;
                    }
                    catch (IOException iOException) {}
                    continue;
                    finally {
                        snapshot.close();
                    }
                }
                return false;
            }

            @Override
            public String next() {
                if (this.hasNext()) {
                    String string2 = this.nextUrl;
                    this.nextUrl = null;
                    this.canRemove = true;
                    return string2;
                }
                throw new NoSuchElementException();
            }

            @Override
            public void remove() {
                if (this.canRemove) {
                    this.delegate.remove();
                    return;
                }
                throw new IllegalStateException("remove() before next()");
            }
        };
    }

    public int writeAbortCount() {
        synchronized (this) {
            int n = this.writeAbortCount;
            return n;
        }
    }

    public int writeSuccessCount() {
        synchronized (this) {
            int n = this.writeSuccessCount;
            return n;
        }
    }

    private final class CacheRequestImpl
    implements CacheRequest {
        private Sink body;
        private Sink cacheOut;
        private boolean done;
        private final DiskLruCache.Editor editor;
        final Cache this$0;

        public CacheRequestImpl(Cache cache, DiskLruCache.Editor editor) throws IOException {
            this.this$0 = cache;
            this.editor = editor;
            this.cacheOut = editor.newSink(1);
            this.body = new ForwardingSink(this, this.cacheOut, cache, editor){
                final CacheRequestImpl this$1;
                final DiskLruCache.Editor val$editor;
                final Cache val$this$0;
                {
                    this.this$1 = cacheRequestImpl;
                    this.val$this$0 = cache;
                    this.val$editor = editor;
                    super(sink);
                }

                /*
                 * Enabled aggressive block sorting
                 * Enabled unnecessary exception pruning
                 * Enabled aggressive exception aggregation
                 */
                @Override
                public void close() throws IOException {
                    Cache cache = this.this$1.this$0;
                    synchronized (cache) {
                        if (this.this$1.done) {
                            return;
                        }
                        CacheRequestImpl.access$702(this.this$1, true);
                        Cache.access$808(this.this$1.this$0);
                    }
                    super.close();
                    this.val$editor.commit();
                }
            };
        }

        static /* synthetic */ boolean access$702(CacheRequestImpl cacheRequestImpl, boolean bl) {
            cacheRequestImpl.done = bl;
            return bl;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void abort() {
            Cache cache = this.this$0;
            synchronized (cache) {
                if (this.done) {
                    return;
                }
                this.done = true;
                Cache.access$908(this.this$0);
            }
            Util.closeQuietly(this.cacheOut);
            try {
                this.editor.abort();
                return;
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        @Override
        public Sink body() {
            return this.body;
        }
    }

    private static class CacheResponseBody
    extends ResponseBody {
        private final BufferedSource bodySource;
        private final String contentLength;
        private final String contentType;
        private final DiskLruCache.Snapshot snapshot;

        public CacheResponseBody(DiskLruCache.Snapshot snapshot, String string2, String string3) {
            this.snapshot = snapshot;
            this.contentType = string2;
            this.contentLength = string3;
            this.bodySource = Okio.buffer(new ForwardingSource(this, snapshot.getSource(1), snapshot){
                final CacheResponseBody this$0;
                final DiskLruCache.Snapshot val$snapshot;
                {
                    this.this$0 = cacheResponseBody;
                    this.val$snapshot = snapshot;
                    super(source);
                }

                @Override
                public void close() throws IOException {
                    this.val$snapshot.close();
                    super.close();
                }
            });
        }

        @Override
        public long contentLength() {
            long l;
            block3: {
                String string2;
                l = -1L;
                try {
                    string2 = this.contentLength;
                    if (string2 == null) break block3;
                }
                catch (NumberFormatException numberFormatException) {
                    return -1L;
                }
                l = Long.parseLong(string2);
            }
            return l;
        }

        @Override
        public MediaType contentType() {
            Object object = this.contentType;
            object = object != null ? MediaType.parse((String)object) : null;
            return object;
        }

        @Override
        public BufferedSource source() {
            return this.bodySource;
        }
    }

    private static final class Entry {
        private final int code;
        private final Handshake handshake;
        private final String message;
        private final Protocol protocol;
        private final long receivedResponseMillis;
        private final String requestMethod;
        private final Headers responseHeaders;
        private final long sentRequestMillis;
        private final String url;
        private final Headers varyHeaders;

        public Entry(Response response) {
            this.url = response.request().url().toString();
            this.varyHeaders = OkHeaders.varyHeaders(response);
            this.requestMethod = response.request().method();
            this.protocol = response.protocol();
            this.code = response.code();
            this.message = response.message();
            this.responseHeaders = response.headers();
            this.handshake = response.handshake();
            this.sentRequestMillis = response.sentRequestAtMillis();
            this.receivedResponseMillis = response.receivedResponseAtMillis();
        }

        /*
         * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public Entry(Source source) throws IOException {
            block9: {
                Object object;
                int n;
                int n2;
                Object object2;
                Object object3;
                try {
                    object3 = Okio.buffer(source);
                    this.url = object3.readUtf8LineStrict();
                    this.requestMethod = object3.readUtf8LineStrict();
                    object2 = new Headers.Builder();
                    n2 = Cache.readInt((BufferedSource)object3);
                    for (n = 0; n < n2; ++n) {
                        ((Headers.Builder)object2).addLenient(object3.readUtf8LineStrict());
                    }
                }
                catch (Throwable throwable) {
                    source.close();
                    throw throwable;
                }
                {
                    this.varyHeaders = ((Headers.Builder)object2).build();
                    object2 = StatusLine.parse(object3.readUtf8LineStrict());
                    this.protocol = ((StatusLine)object2).protocol;
                    this.code = ((StatusLine)object2).code;
                    this.message = ((StatusLine)object2).message;
                    object = new Headers.Builder();
                    n2 = Cache.readInt((BufferedSource)object3);
                    for (n = 0; n < n2; ++n) {
                        ((Headers.Builder)object).addLenient(object3.readUtf8LineStrict());
                    }
                }
                {
                    Object object4 = ((Headers.Builder)object).get(OkHeaders.SENT_MILLIS);
                    object2 = ((Headers.Builder)object).get(OkHeaders.RECEIVED_MILLIS);
                    ((Headers.Builder)object).removeAll(OkHeaders.SENT_MILLIS);
                    ((Headers.Builder)object).removeAll(OkHeaders.RECEIVED_MILLIS);
                    long l = 0L;
                    long l2 = object4 != null ? Long.parseLong((String)object4) : 0L;
                    this.sentRequestMillis = l2;
                    l2 = object2 != null ? Long.parseLong((String)object2) : l;
                    this.receivedResponseMillis = l2;
                    this.responseHeaders = ((Headers.Builder)object).build();
                    boolean bl = this.isHttps();
                    object2 = null;
                    if (bl) {
                        object4 = object3.readUtf8LineStrict();
                        if (((String)object4).length() > 0) {
                            object2 = new StringBuilder();
                            ((StringBuilder)object2).append("expected \"\" but was \"");
                            ((StringBuilder)object2).append((String)object4);
                            ((StringBuilder)object2).append("\"");
                            object3 = new IOException(((StringBuilder)object2).toString());
                            throw object3;
                        }
                        CipherSuite cipherSuite = CipherSuite.forJavaName(object3.readUtf8LineStrict());
                        object = this.readCertificateList((BufferedSource)object3);
                        object4 = this.readCertificateList((BufferedSource)object3);
                        if (!object3.exhausted()) {
                            object2 = TlsVersion.forJavaName(object3.readUtf8LineStrict());
                        }
                        this.handshake = Handshake.get((TlsVersion)((Object)object2), cipherSuite, (List<Certificate>)object, (List<Certificate>)object4);
                        break block9;
                    }
                    this.handshake = null;
                }
            }
            source.close();
        }

        private boolean isHttps() {
            return this.url.startsWith("https://");
        }

        private List<Certificate> readCertificateList(BufferedSource bufferedSource) throws IOException {
            ArrayList<Certificate> arrayList;
            CertificateFactory certificateFactory;
            int n = Cache.readInt(bufferedSource);
            if (n == -1) {
                return Collections.emptyList();
            }
            try {
                certificateFactory = CertificateFactory.getInstance("X.509");
                arrayList = new ArrayList<Certificate>(n);
            }
            catch (CertificateException certificateException) {
                IOException iOException = new IOException(certificateException.getMessage());
                throw iOException;
            }
            for (int i = 0; i < n; ++i) {
                String string2 = bufferedSource.readUtf8LineStrict();
                Buffer buffer = new Buffer();
                buffer.write(ByteString.decodeBase64(string2));
                arrayList.add(certificateFactory.generateCertificate(buffer.inputStream()));
                continue;
            }
            return arrayList;
        }

        private void writeCertList(BufferedSink bufferedSink, List<Certificate> list) throws IOException {
            int n;
            bufferedSink.writeDecimalLong(list.size()).writeByte(10);
            try {
                n = list.size();
            }
            catch (CertificateEncodingException certificateEncodingException) {
                IOException iOException = new IOException(certificateEncodingException.getMessage());
                throw iOException;
            }
            for (int i = 0; i < n; ++i) {
                bufferedSink.writeUtf8(ByteString.of(list.get(i).getEncoded()).base64()).writeByte(10);
                continue;
            }
            return;
        }

        public boolean matches(Request request, Response response) {
            boolean bl = this.url.equals(request.url().toString()) && this.requestMethod.equals(request.method()) && OkHeaders.varyMatches(response, this.varyHeaders, request);
            return bl;
        }

        public Response response(DiskLruCache.Snapshot snapshot) {
            String string2 = this.responseHeaders.get("Content-Type");
            String string3 = this.responseHeaders.get("Content-Length");
            Request request = new Request.Builder().url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build();
            return new Response.Builder().request(request).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new CacheResponseBody(snapshot, string2, string3)).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
        }

        public void writeTo(DiskLruCache.Editor object) throws IOException {
            int n;
            object = Okio.buffer(((DiskLruCache.Editor)object).newSink(0));
            object.writeUtf8(this.url).writeByte(10);
            object.writeUtf8(this.requestMethod).writeByte(10);
            object.writeDecimalLong(this.varyHeaders.size()).writeByte(10);
            int n2 = this.varyHeaders.size();
            for (n = 0; n < n2; ++n) {
                object.writeUtf8(this.varyHeaders.name(n)).writeUtf8(": ").writeUtf8(this.varyHeaders.value(n)).writeByte(10);
            }
            object.writeUtf8(new StatusLine(this.protocol, this.code, this.message).toString()).writeByte(10);
            object.writeDecimalLong(this.responseHeaders.size() + 2).writeByte(10);
            n2 = this.responseHeaders.size();
            for (n = 0; n < n2; ++n) {
                object.writeUtf8(this.responseHeaders.name(n)).writeUtf8(": ").writeUtf8(this.responseHeaders.value(n)).writeByte(10);
            }
            object.writeUtf8(OkHeaders.SENT_MILLIS).writeUtf8(": ").writeDecimalLong(this.sentRequestMillis).writeByte(10);
            object.writeUtf8(OkHeaders.RECEIVED_MILLIS).writeUtf8(": ").writeDecimalLong(this.receivedResponseMillis).writeByte(10);
            if (this.isHttps()) {
                object.writeByte(10);
                object.writeUtf8(this.handshake.cipherSuite().javaName()).writeByte(10);
                this.writeCertList((BufferedSink)object, this.handshake.peerCertificates());
                this.writeCertList((BufferedSink)object, this.handshake.localCertificates());
                if (this.handshake.tlsVersion() != null) {
                    object.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
                }
            }
            object.close();
        }
    }
}

