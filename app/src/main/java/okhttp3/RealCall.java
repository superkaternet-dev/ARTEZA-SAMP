/*
 * Decompiled with CFR 0.152.
 */
package okhttp3;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Connection;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.NamedRunnable;
import okhttp3.internal.Platform;
import okhttp3.internal.http.HttpEngine;

final class RealCall
implements Call {
    volatile boolean canceled;
    private final OkHttpClient client;
    HttpEngine engine;
    private boolean executed;
    Request originalRequest;

    protected RealCall(OkHttpClient okHttpClient, Request request) {
        this.client = okHttpClient;
        this.originalRequest = request;
    }

    private Response getResponseWithInterceptorChain(boolean bl) throws IOException {
        return new ApplicationInterceptorChain(this, 0, this.originalRequest, bl).proceed(this.originalRequest);
    }

    private String toLoggableString() {
        String string2 = this.canceled ? "canceled call" : "call";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(string2);
        stringBuilder.append(" to ");
        stringBuilder.append(this.redactedUrl());
        return stringBuilder.toString();
    }

    @Override
    public void cancel() {
        this.canceled = true;
        HttpEngine httpEngine = this.engine;
        if (httpEngine != null) {
            httpEngine.cancel();
        }
    }

    @Override
    public void enqueue(Callback callback) {
        this.enqueue(callback, false);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void enqueue(Callback object, boolean bl) {
        synchronized (this) {
            if (!this.executed) {
                this.executed = true;
                // MONITOREXIT @DISABLED, blocks:[2, 3] lbl4 : MonitorExitStatement: MONITOREXIT : this
                this.client.dispatcher().enqueue(new AsyncCall(this, (Callback)object, bl));
                return;
            }
            object = new IllegalStateException("Already Executed");
            throw object;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Response execute() throws IOException {
        Object object;
        synchronized (this) {
            if (this.executed) {
                IllegalStateException illegalStateException = new IllegalStateException("Already Executed");
                throw illegalStateException;
            }
            this.executed = true;
        }
        try {
            this.client.dispatcher().executed(this);
            object = this.getResponseWithInterceptorChain(false);
            if (object != null) {
                this.client.dispatcher().finished(this);
                return object;
            }
        }
        catch (Throwable throwable) {
            this.client.dispatcher().finished(this);
            throw throwable;
        }
        {
            object = new IOException("Canceled");
            throw object;
        }
    }

    /*
     * Exception decompiling
     */
    Response getResponse(Request var1_1, boolean var2_5) throws IOException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 2[TRYBLOCK] [8 : 375->387)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    @Override
    public boolean isCanceled() {
        return this.canceled;
    }

    @Override
    public boolean isExecuted() {
        synchronized (this) {
            boolean bl = this.executed;
            return bl;
        }
    }

    HttpUrl redactedUrl() {
        return this.originalRequest.url().resolve("/...");
    }

    @Override
    public Request request() {
        return this.originalRequest;
    }

    Object tag() {
        return this.originalRequest.tag();
    }

    class ApplicationInterceptorChain
    implements Interceptor.Chain {
        private final boolean forWebSocket;
        private final int index;
        private final Request request;
        final RealCall this$0;

        ApplicationInterceptorChain(RealCall realCall, int n, Request request, boolean bl) {
            this.this$0 = realCall;
            this.index = n;
            this.request = request;
            this.forWebSocket = bl;
        }

        @Override
        public Connection connection() {
            return null;
        }

        @Override
        public Response proceed(Request object) throws IOException {
            if (this.index < this.this$0.client.interceptors().size()) {
                Object object2 = new ApplicationInterceptorChain(this.this$0, this.index + 1, (Request)object, this.forWebSocket);
                object = this.this$0.client.interceptors().get(this.index);
                object2 = object.intercept((Interceptor.Chain)object2);
                if (object2 != null) {
                    return object2;
                }
                object2 = new StringBuilder();
                ((StringBuilder)object2).append("application interceptor ");
                ((StringBuilder)object2).append(object);
                ((StringBuilder)object2).append(" returned null");
                throw new NullPointerException(((StringBuilder)object2).toString());
            }
            return this.this$0.getResponse((Request)object, this.forWebSocket);
        }

        @Override
        public Request request() {
            return this.request;
        }
    }

    final class AsyncCall
    extends NamedRunnable {
        private final boolean forWebSocket;
        private final Callback responseCallback;
        final RealCall this$0;

        private AsyncCall(RealCall realCall, Callback callback, boolean bl) {
            this.this$0 = realCall;
            super("OkHttp %s", realCall.redactedUrl().toString());
            this.responseCallback = callback;
            this.forWebSocket = bl;
        }

        void cancel() {
            this.this$0.cancel();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        protected void execute() {
            Throwable throwable2;
            block7: {
                block6: {
                    boolean bl;
                    boolean bl2 = bl = false;
                    try {
                        try {
                            Object object = this.this$0.getResponseWithInterceptorChain(this.forWebSocket);
                            bl2 = bl;
                            if (this.this$0.canceled) {
                                bl2 = bl = true;
                                Callback callback = this.responseCallback;
                                bl2 = bl;
                                RealCall realCall = this.this$0;
                                bl2 = bl;
                                bl2 = bl;
                                object = new IOException("Canceled");
                                bl2 = bl;
                                callback.onFailure(realCall, (IOException)object);
                                break block6;
                            }
                            bl2 = true;
                            this.responseCallback.onResponse(this.this$0, (Response)object);
                        }
                        catch (IOException iOException) {
                            if (bl2) {
                                Platform platform = Platform.get();
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Callback failure for ");
                                stringBuilder.append(this.this$0.toLoggableString());
                                platform.log(4, stringBuilder.toString(), iOException);
                                break block6;
                            }
                            this.responseCallback.onFailure(this.this$0, iOException);
                        }
                    }
                    catch (Throwable throwable2) {
                        break block7;
                    }
                }
                this.this$0.client.dispatcher().finished(this);
                return;
            }
            this.this$0.client.dispatcher().finished(this);
            throw throwable2;
        }

        RealCall get() {
            return this.this$0;
        }

        String host() {
            return this.this$0.originalRequest.url().host();
        }

        Request request() {
            return this.this$0.originalRequest;
        }

        Object tag() {
            return this.this$0.originalRequest.tag();
        }
    }
}

