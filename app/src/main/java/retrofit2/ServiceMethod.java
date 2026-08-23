/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.ParameterHandler;
import retrofit2.RequestBuilder;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.Utils;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

final class ServiceMethod<T> {
    static final String PARAM = "[a-zA-Z][a-zA-Z0-9_-]*";
    static final Pattern PARAM_NAME_REGEX;
    static final Pattern PARAM_URL_REGEX;
    private final HttpUrl baseUrl;
    final CallAdapter<?> callAdapter;
    final Call.Factory callFactory;
    private final MediaType contentType;
    private final boolean hasBody;
    private final okhttp3.Headers headers;
    private final String httpMethod;
    private final boolean isFormEncoded;
    private final boolean isMultipart;
    private final ParameterHandler<?>[] parameterHandlers;
    private final String relativeUrl;
    private final Converter<ResponseBody, T> responseConverter;

    static {
        PARAM_URL_REGEX = Pattern.compile("\\{([a-zA-Z][a-zA-Z0-9_-]*)\\}");
        PARAM_NAME_REGEX = Pattern.compile(PARAM);
    }

    ServiceMethod(Builder<T> builder) {
        this.callFactory = builder.retrofit.callFactory();
        this.callAdapter = builder.callAdapter;
        this.baseUrl = builder.retrofit.baseUrl();
        this.responseConverter = builder.responseConverter;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.headers = builder.headers;
        this.contentType = builder.contentType;
        this.hasBody = builder.hasBody;
        this.isFormEncoded = builder.isFormEncoded;
        this.isMultipart = builder.isMultipart;
        this.parameterHandlers = builder.parameterHandlers;
    }

    static Class<?> boxIfPrimitive(Class<?> clazz) {
        if (Boolean.TYPE == clazz) {
            return Boolean.class;
        }
        if (Byte.TYPE == clazz) {
            return Byte.class;
        }
        if (Character.TYPE == clazz) {
            return Character.class;
        }
        if (Double.TYPE == clazz) {
            return Double.class;
        }
        if (Float.TYPE == clazz) {
            return Float.class;
        }
        if (Integer.TYPE == clazz) {
            return Integer.class;
        }
        if (Long.TYPE == clazz) {
            return Long.class;
        }
        if (Short.TYPE == clazz) {
            return Short.class;
        }
        return clazz;
    }

    static Set<String> parsePathParameters(String object) {
        object = PARAM_URL_REGEX.matcher((CharSequence)object);
        LinkedHashSet<String> linkedHashSet = new LinkedHashSet<String>();
        while (((Matcher)object).find()) {
            linkedHashSet.add(((Matcher)object).group(1));
        }
        return linkedHashSet;
    }

    Request toRequest(Object ... object) throws IOException {
        ParameterHandler<?>[] parameterHandlerArray;
        RequestBuilder requestBuilder = new RequestBuilder(this.httpMethod, this.baseUrl, this.relativeUrl, this.headers, this.contentType, this.hasBody, this.isFormEncoded, this.isMultipart);
        int n = object != null ? ((Object[])object).length : 0;
        if (n == (parameterHandlerArray = this.parameterHandlers).length) {
            for (int i = 0; i < n; ++i) {
                parameterHandlerArray[i].apply(requestBuilder, object[i]);
            }
            return requestBuilder.build();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Argument count (");
        ((StringBuilder)object).append(n);
        ((StringBuilder)object).append(") doesn't match expected count (");
        ((StringBuilder)object).append(parameterHandlerArray.length);
        ((StringBuilder)object).append(")");
        object = new IllegalArgumentException(((StringBuilder)object).toString());
        throw object;
    }

    T toResponse(ResponseBody responseBody) throws IOException {
        return this.responseConverter.convert(responseBody);
    }

    static final class Builder<T> {
        CallAdapter<?> callAdapter;
        MediaType contentType;
        boolean gotBody;
        boolean gotField;
        boolean gotPart;
        boolean gotPath;
        boolean gotQuery;
        boolean gotUrl;
        boolean hasBody;
        okhttp3.Headers headers;
        String httpMethod;
        boolean isFormEncoded;
        boolean isMultipart;
        final Method method;
        final Annotation[] methodAnnotations;
        final Annotation[][] parameterAnnotationsArray;
        ParameterHandler<?>[] parameterHandlers;
        final Type[] parameterTypes;
        String relativeUrl;
        Set<String> relativeUrlParamNames;
        Converter<ResponseBody, T> responseConverter;
        Type responseType;
        final Retrofit retrofit;

        public Builder(Retrofit retrofit, Method method) {
            this.retrofit = retrofit;
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
            this.parameterTypes = method.getGenericParameterTypes();
            this.parameterAnnotationsArray = method.getParameterAnnotations();
        }

        private CallAdapter<?> createCallAdapter() {
            Type type = this.method.getGenericReturnType();
            if (!Utils.hasUnresolvableType(type)) {
                if (type != Void.TYPE) {
                    Object object = this.method.getAnnotations();
                    try {
                        object = this.retrofit.callAdapter(type, (Annotation[])object);
                        return object;
                    }
                    catch (RuntimeException runtimeException) {
                        throw this.methodError(runtimeException, "Unable to create call adapter for %s", type);
                    }
                }
                throw this.methodError("Service methods cannot return void.", new Object[0]);
            }
            throw this.methodError("Method return type must not include a type variable or wildcard: %s", type);
        }

        private Converter<ResponseBody, T> createResponseConverter() {
            Object object = this.method.getAnnotations();
            try {
                object = this.retrofit.responseBodyConverter(this.responseType, (Annotation[])object);
                return object;
            }
            catch (RuntimeException runtimeException) {
                throw this.methodError(runtimeException, "Unable to create converter for %s", this.responseType);
            }
        }

        private RuntimeException methodError(String string2, Object ... objectArray) {
            return this.methodError(null, string2, objectArray);
        }

        private RuntimeException methodError(Throwable throwable, String string2, Object ... object) {
            string2 = String.format(string2, (Object[])object);
            object = new StringBuilder();
            ((StringBuilder)object).append(string2);
            ((StringBuilder)object).append("\n    for method ");
            ((StringBuilder)object).append(this.method.getDeclaringClass().getSimpleName());
            ((StringBuilder)object).append(".");
            ((StringBuilder)object).append(this.method.getName());
            return new IllegalArgumentException(((StringBuilder)object).toString(), throwable);
        }

        private RuntimeException parameterError(int n, String string2, Object ... objectArray) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(" (parameter #");
            stringBuilder.append(n + 1);
            stringBuilder.append(")");
            return this.methodError(stringBuilder.toString(), objectArray);
        }

        private RuntimeException parameterError(Throwable throwable, int n, String string2, Object ... objectArray) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string2);
            stringBuilder.append(" (parameter #");
            stringBuilder.append(n + 1);
            stringBuilder.append(")");
            return this.methodError(throwable, stringBuilder.toString(), objectArray);
        }

        private okhttp3.Headers parseHeaders(String[] stringArray) {
            Headers.Builder builder = new Headers.Builder();
            for (String string2 : stringArray) {
                int n = string2.indexOf(58);
                if (n != -1 && n != 0 && n != string2.length() - 1) {
                    Object object = string2.substring(0, n);
                    string2 = string2.substring(n + 1).trim();
                    if ("Content-Type".equalsIgnoreCase((String)object)) {
                        object = MediaType.parse(string2);
                        if (object != null) {
                            this.contentType = object;
                            continue;
                        }
                        throw this.methodError("Malformed content type: %s", string2);
                    }
                    builder.add((String)object, string2);
                    continue;
                }
                throw this.methodError("@Headers value must be in the form \"Name: Value\". Found: \"%s\"", string2);
            }
            return builder.build();
        }

        private void parseHttpMethodAndPath(String string2, String string3, boolean bl) {
            String string4 = this.httpMethod;
            if (string4 == null) {
                this.httpMethod = string2;
                this.hasBody = bl;
                if (string3.isEmpty()) {
                    return;
                }
                int n = string3.indexOf(63);
                if (n != -1 && n < string3.length() - 1 && PARAM_URL_REGEX.matcher(string2 = string3.substring(n + 1)).find()) {
                    throw this.methodError("URL query string \"%s\" must not have replace block. For dynamic query parameters use @Query.", string2);
                }
                this.relativeUrl = string3;
                this.relativeUrlParamNames = ServiceMethod.parsePathParameters(string3);
                return;
            }
            throw this.methodError("Only one HTTP method is allowed. Found: %s and %s.", string4, string2);
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        private void parseMethodAnnotation(Annotation object) {
            if (object instanceof DELETE) {
                this.parseHttpMethodAndPath("DELETE", ((DELETE)object).value(), false);
                return;
            } else if (object instanceof GET) {
                this.parseHttpMethodAndPath("GET", ((GET)object).value(), false);
                return;
            } else {
                if (object instanceof HEAD) {
                    this.parseHttpMethodAndPath("HEAD", ((HEAD)object).value(), false);
                    if (Void.class.equals((Object)this.responseType)) return;
                    throw this.methodError("HEAD method must use Void as response type.", new Object[0]);
                }
                if (object instanceof PATCH) {
                    this.parseHttpMethodAndPath("PATCH", ((PATCH)object).value(), true);
                    return;
                } else if (object instanceof POST) {
                    this.parseHttpMethodAndPath("POST", ((POST)object).value(), true);
                    return;
                } else if (object instanceof PUT) {
                    this.parseHttpMethodAndPath("PUT", ((PUT)object).value(), true);
                    return;
                } else if (object instanceof OPTIONS) {
                    this.parseHttpMethodAndPath("OPTIONS", ((OPTIONS)object).value(), false);
                    return;
                } else if (object instanceof HTTP) {
                    object = (HTTP)object;
                    this.parseHttpMethodAndPath(object.method(), object.path(), object.hasBody());
                    return;
                } else if (object instanceof Headers) {
                    if (((Object)(object = ((Headers)object).value())).length == 0) throw this.methodError("@Headers annotation is empty.", new Object[0]);
                    this.headers = this.parseHeaders((String[])object);
                    return;
                } else if (object instanceof Multipart) {
                    if (this.isFormEncoded) throw this.methodError("Only one encoding annotation is allowed.", new Object[0]);
                    this.isMultipart = true;
                    return;
                } else {
                    if (!(object instanceof FormUrlEncoded)) return;
                    if (this.isMultipart) throw this.methodError("Only one encoding annotation is allowed.", new Object[0]);
                    this.isFormEncoded = true;
                }
            }
        }

        private ParameterHandler<?> parseParameter(int n, Type object, Annotation[] annotationArray) {
            ParameterHandler<?> parameterHandler = null;
            int n2 = annotationArray.length;
            for (int i = 0; i < n2; ++i) {
                ParameterHandler<?> parameterHandler2 = this.parseParameterAnnotation(n, (Type)object, annotationArray, annotationArray[i]);
                if (parameterHandler2 == null) continue;
                if (parameterHandler == null) {
                    parameterHandler = parameterHandler2;
                    continue;
                }
                throw this.parameterError(n, "Multiple Retrofit annotations found, only one allowed.", new Object[0]);
            }
            if (parameterHandler != null) {
                return parameterHandler;
            }
            object = this.parameterError(n, "No Retrofit annotation found.", new Object[0]);
            throw object;
        }

        private ParameterHandler<?> parseParameterAnnotation(int n, Type type, Annotation[] object, Annotation type2) {
            if (type2 instanceof Url) {
                if (!this.gotUrl) {
                    if (!this.gotPath) {
                        if (!this.gotQuery) {
                            if (this.relativeUrl == null) {
                                this.gotUrl = true;
                                if (!(type == HttpUrl.class || type == String.class || type == URI.class || type instanceof Class && "android.net.Uri".equals(((Class)type).getName()))) {
                                    throw this.parameterError(n, "@Url must be okhttp3.HttpUrl, String, java.net.URI, or android.net.Uri type.", new Object[0]);
                                }
                                return new ParameterHandler.RelativeUrl();
                            }
                            throw this.parameterError(n, "@Url cannot be used with @%s URL", this.httpMethod);
                        }
                        throw this.parameterError(n, "A @Url parameter must not come after a @Query", new Object[0]);
                    }
                    throw this.parameterError(n, "@Path parameters may not be used with @Url.", new Object[0]);
                }
                throw this.parameterError(n, "Multiple @Url method annotations found.", new Object[0]);
            }
            if (type2 instanceof Path) {
                if (!this.gotQuery) {
                    if (!this.gotUrl) {
                        if (this.relativeUrl != null) {
                            this.gotPath = true;
                            Path path = (Path)((Object)type2);
                            type2 = path.value();
                            this.validatePathName(n, (String)((Object)type2));
                            return new ParameterHandler.Path((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object), path.encoded());
                        }
                        throw this.parameterError(n, "@Path can only be used with relative url on @%s", this.httpMethod);
                    }
                    throw this.parameterError(n, "@Path parameters may not be used with @Url.", new Object[0]);
                }
                throw this.parameterError(n, "A @Path parameter must not come after a @Query.", new Object[0]);
            }
            if (type2 instanceof Query) {
                Object object2 = (Query)((Object)type2);
                type2 = object2.value();
                boolean bl = object2.encoded();
                object2 = Utils.getRawType(type);
                this.gotQuery = true;
                if (Iterable.class.isAssignableFrom((Class<?>)object2)) {
                    if (type instanceof ParameterizedType) {
                        type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
                        return new ParameterHandler.Query((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object), bl).iterable();
                    }
                    type = new StringBuilder();
                    ((StringBuilder)((Object)type)).append(((Class)object2).getSimpleName());
                    ((StringBuilder)((Object)type)).append(" must include generic type (e.g., ");
                    ((StringBuilder)((Object)type)).append(((Class)object2).getSimpleName());
                    ((StringBuilder)((Object)type)).append("<String>)");
                    throw this.parameterError(n, ((StringBuilder)((Object)type)).toString(), new Object[0]);
                }
                if (((Class)object2).isArray()) {
                    type = ServiceMethod.boxIfPrimitive(((Class)object2).getComponentType());
                    return new ParameterHandler.Query((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object), bl).array();
                }
                return new ParameterHandler.Query((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object), bl);
            }
            if (type2 instanceof QueryMap) {
                Type type3 = Utils.getRawType(type);
                if (Map.class.isAssignableFrom((Class<?>)type3)) {
                    if ((type = Utils.getSupertype(type, type3, Map.class)) instanceof ParameterizedType) {
                        type3 = (ParameterizedType)type;
                        if (String.class == (type = Utils.getParameterUpperBound(0, type3))) {
                            type = Utils.getParameterUpperBound(1, type3);
                            return new ParameterHandler.QueryMap(this.retrofit.stringConverter(type, (Annotation[])object), ((QueryMap)((Object)type2)).encoded());
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append("@QueryMap keys must be of type String: ");
                        ((StringBuilder)object).append(type);
                        throw this.parameterError(n, ((StringBuilder)object).toString(), new Object[0]);
                    }
                    throw this.parameterError(n, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                throw this.parameterError(n, "@QueryMap parameter type must be Map.", new Object[0]);
            }
            if (type2 instanceof Header) {
                type2 = ((Header)((Object)type2)).value();
                Class<?> clazz = Utils.getRawType(type);
                if (Iterable.class.isAssignableFrom(clazz)) {
                    if (type instanceof ParameterizedType) {
                        type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
                        return new ParameterHandler.Header((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object)).iterable();
                    }
                    type = new StringBuilder();
                    ((StringBuilder)((Object)type)).append(clazz.getSimpleName());
                    ((StringBuilder)((Object)type)).append(" must include generic type (e.g., ");
                    ((StringBuilder)((Object)type)).append(clazz.getSimpleName());
                    ((StringBuilder)((Object)type)).append("<String>)");
                    throw this.parameterError(n, ((StringBuilder)((Object)type)).toString(), new Object[0]);
                }
                if (clazz.isArray()) {
                    type = ServiceMethod.boxIfPrimitive(clazz.getComponentType());
                    return new ParameterHandler.Header((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object)).array();
                }
                return new ParameterHandler.Header((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object));
            }
            if (type2 instanceof HeaderMap) {
                type2 = Utils.getRawType(type);
                if (Map.class.isAssignableFrom((Class<?>)type2)) {
                    if ((type = Utils.getSupertype(type, type2, Map.class)) instanceof ParameterizedType) {
                        type2 = (ParameterizedType)type;
                        if (String.class == (type = Utils.getParameterUpperBound(0, (ParameterizedType)type2))) {
                            type = Utils.getParameterUpperBound(1, type2);
                            return new ParameterHandler.HeaderMap(this.retrofit.stringConverter(type, (Annotation[])object));
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append("@HeaderMap keys must be of type String: ");
                        ((StringBuilder)object).append(type);
                        throw this.parameterError(n, ((StringBuilder)object).toString(), new Object[0]);
                    }
                    throw this.parameterError(n, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                }
                throw this.parameterError(n, "@HeaderMap parameter type must be Map.", new Object[0]);
            }
            if (type2 instanceof Field) {
                if (this.isFormEncoded) {
                    Class<?> clazz = (Field)((Object)type2);
                    type2 = clazz.value();
                    boolean bl = clazz.encoded();
                    this.gotField = true;
                    clazz = Utils.getRawType(type);
                    if (Iterable.class.isAssignableFrom(clazz)) {
                        if (type instanceof ParameterizedType) {
                            type = Utils.getParameterUpperBound(0, (ParameterizedType)type);
                            return new ParameterHandler.Field((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object), bl).iterable();
                        }
                        type = new StringBuilder();
                        ((StringBuilder)((Object)type)).append(clazz.getSimpleName());
                        ((StringBuilder)((Object)type)).append(" must include generic type (e.g., ");
                        ((StringBuilder)((Object)type)).append(clazz.getSimpleName());
                        ((StringBuilder)((Object)type)).append("<String>)");
                        throw this.parameterError(n, ((StringBuilder)((Object)type)).toString(), new Object[0]);
                    }
                    if (clazz.isArray()) {
                        type = ServiceMethod.boxIfPrimitive(clazz.getComponentType());
                        return new ParameterHandler.Field((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object), bl).array();
                    }
                    return new ParameterHandler.Field((String)((Object)type2), this.retrofit.stringConverter(type, (Annotation[])object), bl);
                }
                throw this.parameterError(n, "@Field parameters can only be used with form encoding.", new Object[0]);
            }
            if (type2 instanceof FieldMap) {
                if (this.isFormEncoded) {
                    Type type4 = Utils.getRawType(type);
                    if (Map.class.isAssignableFrom((Class<?>)type4)) {
                        if ((type = Utils.getSupertype(type, type4, Map.class)) instanceof ParameterizedType) {
                            type4 = (ParameterizedType)type;
                            if (String.class == (type = Utils.getParameterUpperBound(0, type4))) {
                                type = Utils.getParameterUpperBound(1, type4);
                                type = this.retrofit.stringConverter(type, (Annotation[])object);
                                this.gotField = true;
                                return new ParameterHandler.FieldMap(type, ((FieldMap)((Object)type2)).encoded());
                            }
                            object = new StringBuilder();
                            ((StringBuilder)object).append("@FieldMap keys must be of type String: ");
                            ((StringBuilder)object).append(type);
                            throw this.parameterError(n, ((StringBuilder)object).toString(), new Object[0]);
                        }
                        throw this.parameterError(n, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                    }
                    throw this.parameterError(n, "@FieldMap parameter type must be Map.", new Object[0]);
                }
                throw this.parameterError(n, "@FieldMap parameters can only be used with form encoding.", new Object[0]);
            }
            if (type2 instanceof Part) {
                if (this.isMultipart) {
                    Object object3 = (Part)((Object)type2);
                    this.gotPart = true;
                    String string2 = object3.value();
                    type2 = Utils.getRawType(type);
                    if (string2.isEmpty()) {
                        if (Iterable.class.isAssignableFrom((Class<?>)type2)) {
                            if (type instanceof ParameterizedType) {
                                if (MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(Utils.getParameterUpperBound(0, (ParameterizedType)type)))) {
                                    return ParameterHandler.RawPart.INSTANCE.iterable();
                                }
                                throw this.parameterError(n, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                            }
                            type = new StringBuilder();
                            ((StringBuilder)((Object)type)).append(type2.getSimpleName());
                            ((StringBuilder)((Object)type)).append(" must include generic type (e.g., ");
                            ((StringBuilder)((Object)type)).append(type2.getSimpleName());
                            ((StringBuilder)((Object)type)).append("<String>)");
                            throw this.parameterError(n, ((StringBuilder)((Object)type)).toString(), new Object[0]);
                        }
                        if (type2.isArray()) {
                            if (MultipartBody.Part.class.isAssignableFrom(type2.getComponentType())) {
                                return ParameterHandler.RawPart.INSTANCE.array();
                            }
                            throw this.parameterError(n, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                        }
                        if (MultipartBody.Part.class.isAssignableFrom((Class<?>)type2)) {
                            return ParameterHandler.RawPart.INSTANCE;
                        }
                        throw this.parameterError(n, "@Part annotation must supply a name or use MultipartBody.Part parameter type.", new Object[0]);
                    }
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("form-data; name=\"");
                    stringBuilder.append(string2);
                    stringBuilder.append("\"");
                    object3 = okhttp3.Headers.of("Content-Disposition", stringBuilder.toString(), "Content-Transfer-Encoding", object3.encoding());
                    if (Iterable.class.isAssignableFrom((Class<?>)type2)) {
                        if (type instanceof ParameterizedType) {
                            if (!MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(type = Utils.getParameterUpperBound(0, (ParameterizedType)type)))) {
                                return new ParameterHandler.Part((okhttp3.Headers)object3, this.retrofit.requestBodyConverter(type, (Annotation[])object, this.methodAnnotations)).iterable();
                            }
                            throw this.parameterError(n, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                        }
                        type = new StringBuilder();
                        ((StringBuilder)((Object)type)).append(type2.getSimpleName());
                        ((StringBuilder)((Object)type)).append(" must include generic type (e.g., ");
                        ((StringBuilder)((Object)type)).append(type2.getSimpleName());
                        ((StringBuilder)((Object)type)).append("<String>)");
                        throw this.parameterError(n, ((StringBuilder)((Object)type)).toString(), new Object[0]);
                    }
                    if (type2.isArray()) {
                        type = ServiceMethod.boxIfPrimitive(type2.getComponentType());
                        if (!MultipartBody.Part.class.isAssignableFrom((Class<?>)type)) {
                            return new ParameterHandler.Part((okhttp3.Headers)object3, this.retrofit.requestBodyConverter(type, (Annotation[])object, this.methodAnnotations)).array();
                        }
                        throw this.parameterError(n, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                    }
                    if (!MultipartBody.Part.class.isAssignableFrom((Class<?>)type2)) {
                        return new ParameterHandler.Part((okhttp3.Headers)object3, this.retrofit.requestBodyConverter(type, (Annotation[])object, this.methodAnnotations));
                    }
                    throw this.parameterError(n, "@Part parameters using the MultipartBody.Part must not include a part name in the annotation.", new Object[0]);
                }
                throw this.parameterError(n, "@Part parameters can only be used with multipart encoding.", new Object[0]);
            }
            if (type2 instanceof PartMap) {
                if (this.isMultipart) {
                    this.gotPart = true;
                    Type type5 = Utils.getRawType(type);
                    if (Map.class.isAssignableFrom((Class<?>)type5)) {
                        if ((type = Utils.getSupertype(type, type5, Map.class)) instanceof ParameterizedType) {
                            type5 = (ParameterizedType)type;
                            if (String.class == (type = Utils.getParameterUpperBound(0, type5))) {
                                type = Utils.getParameterUpperBound(1, type5);
                                if (!MultipartBody.Part.class.isAssignableFrom(Utils.getRawType(type))) {
                                    return new ParameterHandler.PartMap(this.retrofit.requestBodyConverter(type, (Annotation[])object, this.methodAnnotations), ((PartMap)((Object)type2)).encoding());
                                }
                                throw this.parameterError(n, "@PartMap values cannot be MultipartBody.Part. Use @Part List<Part> or a different value type instead.", new Object[0]);
                            }
                            object = new StringBuilder();
                            ((StringBuilder)object).append("@PartMap keys must be of type String: ");
                            ((StringBuilder)object).append(type);
                            throw this.parameterError(n, ((StringBuilder)object).toString(), new Object[0]);
                        }
                        throw this.parameterError(n, "Map must include generic types (e.g., Map<String, String>)", new Object[0]);
                    }
                    throw this.parameterError(n, "@PartMap parameter type must be Map.", new Object[0]);
                }
                throw this.parameterError(n, "@PartMap parameters can only be used with multipart encoding.", new Object[0]);
            }
            if (type2 instanceof Body) {
                if (!this.isFormEncoded && !this.isMultipart) {
                    if (!this.gotBody) {
                        try {
                            object = this.retrofit.requestBodyConverter(type, (Annotation[])object, this.methodAnnotations);
                            this.gotBody = true;
                        }
                        catch (RuntimeException runtimeException) {
                            throw this.parameterError(runtimeException, n, "Unable to create @Body converter for %s", type);
                        }
                        return new ParameterHandler.Body(object);
                    }
                    throw this.parameterError(n, "Multiple @Body method annotations found.", new Object[0]);
                }
                throw this.parameterError(n, "@Body parameters cannot be used with form or multi-part encoding.", new Object[0]);
            }
            return null;
        }

        private void validatePathName(int n, String string2) {
            if (PARAM_NAME_REGEX.matcher(string2).matches()) {
                if (this.relativeUrlParamNames.contains(string2)) {
                    return;
                }
                throw this.parameterError(n, "URL \"%s\" does not contain \"{%s}\".", this.relativeUrl, string2);
            }
            throw this.parameterError(n, "@Path parameter name must match %s. Found: %s", PARAM_URL_REGEX.pattern(), string2);
        }

        public ServiceMethod build() {
            Object object = this.createCallAdapter();
            this.callAdapter = object;
            object = object.responseType();
            this.responseType = object;
            if (object != Response.class && object != okhttp3.Response.class) {
                int n;
                this.responseConverter = this.createResponseConverter();
                object = this.methodAnnotations;
                int n2 = ((Annotation[])object).length;
                for (n = 0; n < n2; ++n) {
                    this.parseMethodAnnotation(object[n]);
                }
                if (this.httpMethod != null) {
                    if (!this.hasBody) {
                        if (!this.isMultipart) {
                            if (this.isFormEncoded) {
                                throw this.methodError("FormUrlEncoded can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
                            }
                        } else {
                            throw this.methodError("Multipart can only be specified on HTTP methods with request body (e.g., @POST).", new Object[0]);
                        }
                    }
                    n2 = this.parameterAnnotationsArray.length;
                    this.parameterHandlers = new ParameterHandler[n2];
                    for (n = 0; n < n2; ++n) {
                        object = this.parameterTypes[n];
                        if (!Utils.hasUnresolvableType((Type)object)) {
                            Annotation[] annotationArray = this.parameterAnnotationsArray[n];
                            if (annotationArray != null) {
                                this.parameterHandlers[n] = this.parseParameter(n, (Type)object, annotationArray);
                                continue;
                            }
                            throw this.parameterError(n, "No Retrofit annotation found.", new Object[0]);
                        }
                        throw this.parameterError(n, "Parameter type must not include a type variable or wildcard: %s", object);
                    }
                    if (this.relativeUrl == null && !this.gotUrl) {
                        throw this.methodError("Missing either @%s URL or @Url parameter.", this.httpMethod);
                    }
                    boolean bl = this.isFormEncoded;
                    if (!bl && !this.isMultipart && !this.hasBody && this.gotBody) {
                        throw this.methodError("Non-body HTTP method cannot contain @Body.", new Object[0]);
                    }
                    if (bl && !this.gotField) {
                        throw this.methodError("Form-encoded method must contain at least one @Field.", new Object[0]);
                    }
                    if (this.isMultipart && !this.gotPart) {
                        throw this.methodError("Multipart method must contain at least one @Part.", new Object[0]);
                    }
                    return new ServiceMethod(this);
                }
                throw this.methodError("HTTP method annotation is required (e.g., @GET, @POST, etc.).", new Object[0]);
            }
            object = new StringBuilder();
            object.append("'");
            object.append(Utils.getRawType(this.responseType).getName());
            object.append("' is not a valid response body type. Did you mean ResponseBody?");
            object = this.methodError(object.toString(), new Object[0]);
            throw object;
        }
    }
}

