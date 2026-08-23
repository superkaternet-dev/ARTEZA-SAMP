/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.ServiceMethod;
import retrofit2.Utils;

final class OkHttpCall<T>
implements Call<T> {
    private final Object[] args;
    private volatile boolean canceled;
    private Throwable creationFailure;
    private boolean executed;
    private okhttp3.Call rawCall;
    private final ServiceMethod<T> serviceMethod;

    OkHttpCall(ServiceMethod<T> serviceMethod, Object[] objectArray) {
        this.serviceMethod = serviceMethod;
        this.args = objectArray;
    }

    private okhttp3.Call createRawCall() throws IOException {
        Object object = this.serviceMethod.toRequest(this.args);
        if ((object = this.serviceMethod.callFactory.newCall((Request)object)) != null) {
            return object;
        }
        throw new NullPointerException("Call.Factory returned null.");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void cancel() {
        this.canceled = true;
        // MONITORENTER : this
        okhttp3.Call call = this.rawCall;
        // MONITOREXIT : this
        if (call == null) return;
        call.cancel();
    }

    @Override
    public OkHttpCall<T> clone() {
        return new OkHttpCall<T>(this.serviceMethod, this.args);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public void enqueue(Callback<T> object) {
        if (object == null) throw new NullPointerException("callback == null");
        // MONITORENTER : this
        if (this.executed) {
            object = new IllegalStateException("Already executed.");
            throw object;
        }
        this.executed = true;
        okhttp3.Call call = this.rawCall;
        Throwable throwable = this.creationFailure;
        okhttp3.Call call2 = call;
        Throwable throwable2 = throwable;
        if (call == null) {
            call2 = call;
            throwable2 = throwable;
            if (throwable == null) {
                try {
                    this.rawCall = call2 = this.createRawCall();
                    throwable2 = throwable;
                }
                catch (Throwable throwable3) {
                    this.creationFailure = throwable3;
                    call2 = call;
                }
            }
        }
        // MONITOREXIT : this
        if (throwable2 != null) {
            object.onFailure(this, throwable2);
            return;
        }
        if (this.canceled) {
            call2.cancel();
        }
        call2.enqueue(new okhttp3.Callback(this, (Callback)object){
            final OkHttpCall this$0;
            final Callback val$callback;
            {
                this.this$0 = okHttpCall;
                this.val$callback = callback;
            }

            private void callFailure(Throwable throwable) {
                try {
                    this.val$callback.onFailure(this.this$0, throwable);
                }
                catch (Throwable throwable2) {
                    throwable2.printStackTrace();
                }
            }

            private void callSuccess(Response<T> response) {
                try {
                    this.val$callback.onResponse(this.this$0, response);
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onFailure(okhttp3.Call call, IOException iOException) {
                try {
                    this.val$callback.onFailure(this.this$0, iOException);
                }
                catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void onResponse(okhttp3.Call object, okhttp3.Response response) throws IOException {
                try {
                    object = this.this$0.parseResponse(response);
                    this.callSuccess((Response)object);
                    return;
                }
                catch (Throwable throwable) {
                    this.callFailure(throwable);
                    return;
                }
            }
        });
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    @Override
    public Response<T> execute() throws IOException {
        Object object;
        block9: {
            // MONITORENTER : this
            if (this.executed) {
                IllegalStateException illegalStateException = new IllegalStateException("Already executed.");
                throw illegalStateException;
            }
            this.executed = true;
            object = this.creationFailure;
            if (object != null) {
                if (!(object instanceof IOException)) throw (RuntimeException)object;
                throw (IOException)object;
            }
            okhttp3.Call call = this.rawCall;
            object = call;
            if (call == null) {
                void var1_4;
                try {
                    this.rawCall = object = this.createRawCall();
                    break block9;
                }
                catch (RuntimeException runtimeException) {
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                this.creationFailure = var1_4;
                throw var1_4;
            }
        }
        // MONITOREXIT : this
        if (!this.canceled) return this.parseResponse(object.execute());
        object.cancel();
        return this.parseResponse(object.execute());
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

    Response<T> parseResponse(okhttp3.Response object) throws IOException {
        Object object2 = ((okhttp3.Response)object).body();
        okhttp3.Response response = ((okhttp3.Response)object).newBuilder().body(new NoContentResponseBody(((ResponseBody)object2).contentType(), ((ResponseBody)object2).contentLength())).build();
        int n = response.code();
        if (n >= 200 && n < 300) {
            if (n != 204 && n != 205) {
                object = new ExceptionCatchingRequestBody((ResponseBody)object2);
                try {
                    object2 = Response.success(this.serviceMethod.toResponse((ResponseBody)object), response);
                    return object2;
                }
                catch (RuntimeException runtimeException) {
                    ((ExceptionCatchingRequestBody)object).throwIfCaught();
                    throw runtimeException;
                }
            }
            return Response.success(null, response);
        }
        try {
            object = Response.error(Utils.buffer((ResponseBody)object2), response);
            return object;
        }
        finally {
            ((ResponseBody)object2).close();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Request request() {
        synchronized (this) {
            Object object = this.rawCall;
            if (object != null) {
                return object.request();
            }
            object = this.creationFailure;
            if (object != null) {
                if (!(object instanceof IOException)) throw (RuntimeException)object;
                object = new RuntimeException("Unable to create request.", this.creationFailure);
                throw object;
            }
            try {
                this.rawCall = object = this.createRawCall();
                return object.request();
            }
            catch (IOException iOException) {
                this.creationFailure = iOException;
                object = new RuntimeException("Unable to create request.", iOException);
                throw object;
            }
            catch (RuntimeException runtimeException) {
                this.creationFailure = runtimeException;
                throw runtimeException;
            }
        }
    }

    static final class ExceptionCatchingRequestBody
    extends ResponseBody {
        private final ResponseBody delegate;
        IOException thrownException;

        ExceptionCatchingRequestBody(ResponseBody responseBody) {
            this.delegate = responseBody;
        }

        @Override
        public void close() {
            this.delegate.close();
        }

        @Override
        public long contentLength() {
            return this.delegate.contentLength();
        }

        @Override
        public MediaType contentType() {
            return this.delegate.contentType();
        }

        @Override
        public BufferedSource source() {
            return Okio.buffer(new ForwardingSource(this, this.delegate.source()){
                final ExceptionCatchingRequestBody this$0;
                {
                    this.this$0 = exceptionCatchingRequestBody;
                    super(source);
                }

                @Override
                public long read(Buffer buffer, long l) throws IOException {
                    try {
                        l = super.read(buffer, l);
                        return l;
                    }
                    catch (IOException iOException) {
                        this.this$0.thrownException = iOException;
                        throw iOException;
                    }
                }
            });
        }

        void throwIfCaught() throws IOException {
            IOException iOException = this.thrownException;
            if (iOException == null) {
                return;
            }
            throw iOException;
        }
    }

    static final class NoContentResponseBody
    extends ResponseBody {
        private final long contentLength;
        private final MediaType contentType;

        NoContentResponseBody(MediaType mediaType, long l) {
            this.contentType = mediaType;
            this.contentLength = l;
        }

        @Override
        public long contentLength() {
            return this.contentLength;
        }

        @Override
        public MediaType contentType() {
            return this.contentType;
        }

        @Override
        public BufferedSource source() {
            throw new IllegalStateException("Cannot read raw response body of a converted body.");
        }
    }
}

