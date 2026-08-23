/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Utils;

final class ExecutorCallAdapterFactory
extends CallAdapter.Factory {
    final Executor callbackExecutor;

    ExecutorCallAdapterFactory(Executor executor) {
        this.callbackExecutor = executor;
    }

    public CallAdapter<Call<?>> get(Type type, Annotation[] annotationArray, Retrofit retrofit) {
        if (ExecutorCallAdapterFactory.getRawType(type) != Call.class) {
            return null;
        }
        return new CallAdapter<Call<?>>(this, Utils.getCallResponseType(type)){
            final ExecutorCallAdapterFactory this$0;
            final Type val$responseType;
            {
                this.this$0 = executorCallAdapterFactory;
                this.val$responseType = type;
            }

            @Override
            public <R> Call<R> adapt(Call<R> call) {
                return new ExecutorCallbackCall<R>(this.this$0.callbackExecutor, call);
            }

            @Override
            public Type responseType() {
                return this.val$responseType;
            }
        };
    }

    static final class ExecutorCallbackCall<T>
    implements Call<T> {
        final Executor callbackExecutor;
        final Call<T> delegate;

        ExecutorCallbackCall(Executor executor, Call<T> call) {
            this.callbackExecutor = executor;
            this.delegate = call;
        }

        @Override
        public void cancel() {
            this.delegate.cancel();
        }

        @Override
        public Call<T> clone() {
            return new ExecutorCallbackCall<T>(this.callbackExecutor, this.delegate.clone());
        }

        @Override
        public void enqueue(Callback<T> callback) {
            if (callback != null) {
                this.delegate.enqueue(new Callback<T>(this, callback){
                    final ExecutorCallbackCall this$0;
                    final Callback val$callback;
                    {
                        this.this$0 = executorCallbackCall;
                        this.val$callback = callback;
                    }

                    @Override
                    public void onFailure(Call<T> call, Throwable throwable) {
                        this.this$0.callbackExecutor.execute(new Runnable(this, throwable){
                            final 1 this$1;
                            final Throwable val$t;
                            {
                                this.this$1 = var1_1;
                                this.val$t = throwable;
                            }

                            @Override
                            public void run() {
                                this.this$1.val$callback.onFailure(this.this$1.this$0, this.val$t);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call<T> call, Response<T> response) {
                        this.this$0.callbackExecutor.execute(new Runnable(this, response){
                            final 1 this$1;
                            final Response val$response;
                            {
                                this.this$1 = var1_1;
                                this.val$response = response;
                            }

                            @Override
                            public void run() {
                                if (this.this$1.this$0.delegate.isCanceled()) {
                                    this.this$1.val$callback.onFailure(this.this$1.this$0, new IOException("Canceled"));
                                } else {
                                    this.this$1.val$callback.onResponse(this.this$1.this$0, this.val$response);
                                }
                            }
                        });
                    }
                });
                return;
            }
            throw new NullPointerException("callback == null");
        }

        @Override
        public Response<T> execute() throws IOException {
            return this.delegate.execute();
        }

        @Override
        public boolean isCanceled() {
            return this.delegate.isCanceled();
        }

        @Override
        public boolean isExecuted() {
            return this.delegate.isExecuted();
        }

        @Override
        public Request request() {
            return this.delegate.request();
        }
    }
}

