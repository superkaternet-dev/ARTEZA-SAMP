/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.BuiltInConverters;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.OkHttpCall;
import retrofit2.Platform;
import retrofit2.ServiceMethod;
import retrofit2.Utils;

public final class Retrofit {
    private final List<CallAdapter.Factory> adapterFactories;
    private final HttpUrl baseUrl;
    private final Call.Factory callFactory;
    private final Executor callbackExecutor;
    private final List<Converter.Factory> converterFactories;
    private final Map<Method, ServiceMethod> serviceMethodCache = new LinkedHashMap<Method, ServiceMethod>();
    private final boolean validateEagerly;

    Retrofit(Call.Factory factory, HttpUrl httpUrl, List<Converter.Factory> list, List<CallAdapter.Factory> list2, Executor executor, boolean bl) {
        this.callFactory = factory;
        this.baseUrl = httpUrl;
        this.converterFactories = Collections.unmodifiableList(list);
        this.adapterFactories = Collections.unmodifiableList(list2);
        this.callbackExecutor = executor;
        this.validateEagerly = bl;
    }

    private void eagerlyValidateMethods(Class<?> methodArray) {
        Platform platform = Platform.get();
        for (Method method : methodArray.getDeclaredMethods()) {
            if (platform.isDefaultMethod(method)) continue;
            this.loadServiceMethod(method);
        }
    }

    public HttpUrl baseUrl() {
        return this.baseUrl;
    }

    public CallAdapter<?> callAdapter(Type type, Annotation[] annotationArray) {
        return this.nextCallAdapter(null, type, annotationArray);
    }

    public List<CallAdapter.Factory> callAdapterFactories() {
        return this.adapterFactories;
    }

    public Call.Factory callFactory() {
        return this.callFactory;
    }

    public Executor callbackExecutor() {
        return this.callbackExecutor;
    }

    public List<Converter.Factory> converterFactories() {
        return this.converterFactories;
    }

    public <T> T create(Class<T> clazz) {
        Utils.validateServiceInterface(clazz);
        if (this.validateEagerly) {
            this.eagerlyValidateMethods(clazz);
        }
        ClassLoader classLoader = clazz.getClassLoader();
        InvocationHandler invocationHandler = new InvocationHandler(this, clazz){
            private final Platform platform;
            final Retrofit this$0;
            final Class val$service;
            {
                this.this$0 = retrofit;
                this.val$service = clazz;
                this.platform = Platform.get();
            }

            @Override
            public Object invoke(Object object, Method object2, Object ... objectArray) throws Throwable {
                if (((Method)object2).getDeclaringClass() == Object.class) {
                    return ((Method)object2).invoke((Object)this, objectArray);
                }
                if (this.platform.isDefaultMethod((Method)object2)) {
                    return this.platform.invokeDefaultMethod((Method)object2, this.val$service, object, objectArray);
                }
                object = this.this$0.loadServiceMethod((Method)object2);
                object2 = new OkHttpCall(object, objectArray);
                return ((ServiceMethod)object).callAdapter.adapt(object2);
            }
        };
        return (T)Proxy.newProxyInstance(classLoader, new Class[]{clazz}, invocationHandler);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    ServiceMethod loadServiceMethod(Method method) {
        Map<Method, ServiceMethod> map = this.serviceMethodCache;
        synchronized (map) {
            ServiceMethod serviceMethod = this.serviceMethodCache.get(method);
            Object object = serviceMethod;
            if (serviceMethod == null) {
                object = new ServiceMethod.Builder(this, method);
                object = ((ServiceMethod.Builder)object).build();
                this.serviceMethodCache.put(method, (ServiceMethod)object);
            }
            return object;
        }
    }

    public CallAdapter<?> nextCallAdapter(CallAdapter.Factory object, Type object2, Annotation[] object3) {
        int n;
        int n2;
        Utils.checkNotNull(object2, "returnType == null");
        Utils.checkNotNull(object3, "annotations == null");
        int n3 = this.adapterFactories.size();
        for (n2 = n = this.adapterFactories.indexOf(object) + 1; n2 < n3; ++n2) {
            CallAdapter<?> callAdapter = this.adapterFactories.get(n2).get((Type)object2, (Annotation[])object3, this);
            if (callAdapter == null) continue;
            return callAdapter;
        }
        object3 = new StringBuilder("Could not locate call adapter for ");
        ((StringBuilder)object3).append(object2);
        object2 = ((StringBuilder)object3).append(".\n");
        if (object != null) {
            ((StringBuilder)object2).append("  Skipped:");
            for (n2 = 0; n2 < n; ++n2) {
                ((StringBuilder)object2).append("\n   * ");
                ((StringBuilder)object2).append(this.adapterFactories.get(n2).getClass().getName());
            }
            ((StringBuilder)object2).append('\n');
        }
        ((StringBuilder)object2).append("  Tried:");
        n2 = this.adapterFactories.size();
        while (n < n2) {
            ((StringBuilder)object2).append("\n   * ");
            ((StringBuilder)object2).append(this.adapterFactories.get(n).getClass().getName());
            ++n;
        }
        object = new IllegalArgumentException(((StringBuilder)object2).toString());
        throw object;
    }

    public <T> Converter<T, RequestBody> nextRequestBodyConverter(Converter.Factory object, Type object2, Annotation[] object3, Annotation[] annotationArray) {
        int n;
        int n2;
        Utils.checkNotNull(object2, "type == null");
        Utils.checkNotNull(object3, "parameterAnnotations == null");
        Utils.checkNotNull(annotationArray, "methodAnnotations == null");
        int n3 = this.converterFactories.size();
        for (n2 = n = this.converterFactories.indexOf(object) + 1; n2 < n3; ++n2) {
            Object object4 = this.converterFactories.get(n2);
            if ((object4 = ((Converter.Factory)object4).requestBodyConverter((Type)object2, (Annotation[])object3, annotationArray, this)) == null) continue;
            return object4;
        }
        object3 = new StringBuilder("Could not locate RequestBody converter for ");
        ((StringBuilder)object3).append(object2);
        object2 = ((StringBuilder)object3).append(".\n");
        if (object != null) {
            ((StringBuilder)object2).append("  Skipped:");
            for (n2 = 0; n2 < n; ++n2) {
                ((StringBuilder)object2).append("\n   * ");
                ((StringBuilder)object2).append(this.converterFactories.get(n2).getClass().getName());
            }
            ((StringBuilder)object2).append('\n');
        }
        ((StringBuilder)object2).append("  Tried:");
        n2 = this.converterFactories.size();
        while (n < n2) {
            ((StringBuilder)object2).append("\n   * ");
            ((StringBuilder)object2).append(this.converterFactories.get(n).getClass().getName());
            ++n;
        }
        object = new IllegalArgumentException(((StringBuilder)object2).toString());
        throw object;
    }

    public <T> Converter<ResponseBody, T> nextResponseBodyConverter(Converter.Factory object, Type object2, Annotation[] object3) {
        int n;
        int n2;
        Utils.checkNotNull(object2, "type == null");
        Utils.checkNotNull(object3, "annotations == null");
        int n3 = this.converterFactories.size();
        for (n2 = n = this.converterFactories.indexOf(object) + 1; n2 < n3; ++n2) {
            Converter<ResponseBody, ?> converter = this.converterFactories.get(n2).responseBodyConverter((Type)object2, (Annotation[])object3, this);
            if (converter == null) continue;
            return converter;
        }
        object3 = new StringBuilder("Could not locate ResponseBody converter for ");
        ((StringBuilder)object3).append(object2);
        object2 = ((StringBuilder)object3).append(".\n");
        if (object != null) {
            ((StringBuilder)object2).append("  Skipped:");
            for (n2 = 0; n2 < n; ++n2) {
                ((StringBuilder)object2).append("\n   * ");
                ((StringBuilder)object2).append(this.converterFactories.get(n2).getClass().getName());
            }
            ((StringBuilder)object2).append('\n');
        }
        ((StringBuilder)object2).append("  Tried:");
        n2 = this.converterFactories.size();
        while (n < n2) {
            ((StringBuilder)object2).append("\n   * ");
            ((StringBuilder)object2).append(this.converterFactories.get(n).getClass().getName());
            ++n;
        }
        object = new IllegalArgumentException(((StringBuilder)object2).toString());
        throw object;
    }

    public <T> Converter<T, RequestBody> requestBodyConverter(Type type, Annotation[] annotationArray, Annotation[] annotationArray2) {
        return this.nextRequestBodyConverter(null, type, annotationArray, annotationArray2);
    }

    public <T> Converter<ResponseBody, T> responseBodyConverter(Type type, Annotation[] annotationArray) {
        return this.nextResponseBodyConverter(null, type, annotationArray);
    }

    public <T> Converter<T, String> stringConverter(Type type, Annotation[] annotationArray) {
        Utils.checkNotNull(type, "type == null");
        Utils.checkNotNull(annotationArray, "annotations == null");
        int n = this.converterFactories.size();
        for (int i = 0; i < n; ++i) {
            Converter<?, String> converter = this.converterFactories.get(i).stringConverter(type, annotationArray, this);
            if (converter == null) continue;
            return converter;
        }
        return BuiltInConverters.ToStringConverter.INSTANCE;
    }

    public static final class Builder {
        private List<CallAdapter.Factory> adapterFactories;
        private HttpUrl baseUrl;
        private Call.Factory callFactory;
        private Executor callbackExecutor;
        private List<Converter.Factory> converterFactories = new ArrayList<Converter.Factory>();
        private Platform platform;
        private boolean validateEagerly;

        public Builder() {
            this(Platform.get());
        }

        Builder(Platform platform) {
            this.adapterFactories = new ArrayList<CallAdapter.Factory>();
            this.platform = platform;
            this.converterFactories.add(new BuiltInConverters());
        }

        public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
            this.adapterFactories.add(Utils.checkNotNull(factory, "factory == null"));
            return this;
        }

        public Builder addConverterFactory(Converter.Factory factory) {
            this.converterFactories.add(Utils.checkNotNull(factory, "factory == null"));
            return this;
        }

        public Builder baseUrl(String string2) {
            Utils.checkNotNull(string2, "baseUrl == null");
            Object object = HttpUrl.parse(string2);
            if (object != null) {
                return this.baseUrl((HttpUrl)object);
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Illegal URL: ");
            ((StringBuilder)object).append(string2);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }

        public Builder baseUrl(HttpUrl httpUrl) {
            Utils.checkNotNull(httpUrl, "baseUrl == null");
            Object object = httpUrl.pathSegments();
            if ("".equals(object.get(object.size() - 1))) {
                this.baseUrl = httpUrl;
                return this;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("baseUrl must end in /: ");
            ((StringBuilder)object).append(httpUrl);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }

        public Retrofit build() {
            if (this.baseUrl != null) {
                Object object = this.callFactory;
                Call.Factory factory = object;
                if (object == null) {
                    factory = new OkHttpClient();
                }
                if ((object = this.callbackExecutor) == null) {
                    object = this.platform.defaultCallbackExecutor();
                }
                ArrayList<CallAdapter.Factory> arrayList = new ArrayList<CallAdapter.Factory>(this.adapterFactories);
                arrayList.add(this.platform.defaultCallAdapterFactory((Executor)object));
                ArrayList<Converter.Factory> arrayList2 = new ArrayList<Converter.Factory>(this.converterFactories);
                return new Retrofit(factory, this.baseUrl, arrayList2, arrayList, (Executor)object, this.validateEagerly);
            }
            throw new IllegalStateException("Base URL required.");
        }

        public Builder callFactory(Call.Factory factory) {
            this.callFactory = Utils.checkNotNull(factory, "factory == null");
            return this;
        }

        public Builder callbackExecutor(Executor executor) {
            this.callbackExecutor = Utils.checkNotNull(executor, "executor == null");
            return this;
        }

        public Builder client(OkHttpClient okHttpClient) {
            return this.callFactory(Utils.checkNotNull(okHttpClient, "client == null"));
        }

        public Builder validateEagerly(boolean bl) {
            this.validateEagerly = bl;
            return this;
        }
    }
}

