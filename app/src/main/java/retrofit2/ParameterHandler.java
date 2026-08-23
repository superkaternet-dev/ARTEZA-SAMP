/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Converter;
import retrofit2.RequestBuilder;
import retrofit2.Utils;

abstract class ParameterHandler<T> {
    ParameterHandler() {
    }

    abstract void apply(RequestBuilder var1, T var2) throws IOException;

    final ParameterHandler<Object> array() {
        return new ParameterHandler<Object>(this){
            final ParameterHandler this$0;
            {
                this.this$0 = parameterHandler;
            }

            @Override
            void apply(RequestBuilder requestBuilder, Object object) throws IOException {
                if (object == null) {
                    return;
                }
                int n = Array.getLength(object);
                for (int i = 0; i < n; ++i) {
                    this.this$0.apply(requestBuilder, Array.get(object, i));
                }
            }
        };
    }

    final ParameterHandler<Iterable<T>> iterable() {
        return new ParameterHandler<Iterable<T>>(this){
            final ParameterHandler this$0;
            {
                this.this$0 = parameterHandler;
            }

            @Override
            void apply(RequestBuilder requestBuilder, Iterable<T> iterable2) throws IOException {
                if (iterable2 == null) {
                    return;
                }
                for (Iterable iterable2 : iterable2) {
                    this.this$0.apply(requestBuilder, iterable2);
                }
            }
        };
    }

    static final class Body<T>
    extends ParameterHandler<T> {
        private final Converter<T, RequestBody> converter;

        Body(Converter<T, RequestBody> converter) {
            this.converter = converter;
        }

        @Override
        void apply(RequestBuilder requestBuilder, T t) {
            if (t != null) {
                try {
                    RequestBody requestBody = this.converter.convert(t);
                    requestBuilder.setBody(requestBody);
                    return;
                }
                catch (IOException iOException) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unable to convert ");
                    stringBuilder.append(t);
                    stringBuilder.append(" to RequestBody");
                    throw new RuntimeException(stringBuilder.toString(), iOException);
                }
            }
            throw new IllegalArgumentException("Body parameter value must not be null.");
        }
    }

    static final class Field<T>
    extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Field(String string2, Converter<T, String> converter, boolean bl) {
            this.name = Utils.checkNotNull(string2, "name == null");
            this.valueConverter = converter;
            this.encoded = bl;
        }

        @Override
        void apply(RequestBuilder requestBuilder, T t) throws IOException {
            if (t == null) {
                return;
            }
            requestBuilder.addFormField(this.name, this.valueConverter.convert(t), this.encoded);
        }
    }

    static final class FieldMap<T>
    extends ParameterHandler<Map<String, T>> {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        FieldMap(Converter<T, String> converter, boolean bl) {
            this.valueConverter = converter;
            this.encoded = bl;
        }

        @Override
        void apply(RequestBuilder object, Map<String, T> object2) throws IOException {
            if (object2 != null) {
                for (Map.Entry<String, Object> entry : object2.entrySet()) {
                    object2 = entry.getKey();
                    if (object2 != null) {
                        if ((entry = entry.getValue()) != null) {
                            ((RequestBuilder)object).addFormField((String)object2, this.valueConverter.convert(entry), this.encoded);
                            continue;
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Field map contained null value for key '");
                        ((StringBuilder)object).append((String)object2);
                        ((StringBuilder)object).append("'.");
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    throw new IllegalArgumentException("Field map contained null key.");
                }
                return;
            }
            object = new IllegalArgumentException("Field map was null.");
            throw object;
        }
    }

    static final class Header<T>
    extends ParameterHandler<T> {
        private final String name;
        private final Converter<T, String> valueConverter;

        Header(String string2, Converter<T, String> converter) {
            this.name = Utils.checkNotNull(string2, "name == null");
            this.valueConverter = converter;
        }

        @Override
        void apply(RequestBuilder requestBuilder, T t) throws IOException {
            if (t == null) {
                return;
            }
            requestBuilder.addHeader(this.name, this.valueConverter.convert(t));
        }
    }

    static final class HeaderMap<T>
    extends ParameterHandler<Map<String, T>> {
        private final Converter<T, String> valueConverter;

        HeaderMap(Converter<T, String> converter) {
            this.valueConverter = converter;
        }

        @Override
        void apply(RequestBuilder object, Map<String, T> object2) throws IOException {
            if (object2 != null) {
                for (Map.Entry<String, Object> entry : object2.entrySet()) {
                    object2 = entry.getKey();
                    if (object2 != null) {
                        if ((entry = entry.getValue()) != null) {
                            ((RequestBuilder)object).addHeader((String)object2, this.valueConverter.convert(entry));
                            continue;
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Header map contained null value for key '");
                        ((StringBuilder)object).append((String)object2);
                        ((StringBuilder)object).append("'.");
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    throw new IllegalArgumentException("Header map contained null key.");
                }
                return;
            }
            object = new IllegalArgumentException("Header map was null.");
            throw object;
        }
    }

    static final class Part<T>
    extends ParameterHandler<T> {
        private final Converter<T, RequestBody> converter;
        private final Headers headers;

        Part(Headers headers, Converter<T, RequestBody> converter) {
            this.headers = headers;
            this.converter = converter;
        }

        @Override
        void apply(RequestBuilder requestBuilder, T t) {
            if (t == null) {
                return;
            }
            try {
                RequestBody requestBody = this.converter.convert(t);
                requestBuilder.addPart(this.headers, requestBody);
                return;
            }
            catch (IOException iOException) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to convert ");
                stringBuilder.append(t);
                stringBuilder.append(" to RequestBody");
                throw new RuntimeException(stringBuilder.toString(), iOException);
            }
        }
    }

    static final class PartMap<T>
    extends ParameterHandler<Map<String, T>> {
        private final String transferEncoding;
        private final Converter<T, RequestBody> valueConverter;

        PartMap(Converter<T, RequestBody> converter, String string2) {
            this.valueConverter = converter;
            this.transferEncoding = string2;
        }

        @Override
        void apply(RequestBuilder object, Map<String, T> object2) throws IOException {
            if (object2 != null) {
                for (Map.Entry<String, T> entry : object2.entrySet()) {
                    object2 = entry.getKey();
                    if (object2 != null) {
                        T t = entry.getValue();
                        if (t != null) {
                            entry = new StringBuilder();
                            ((StringBuilder)((Object)entry)).append("form-data; name=\"");
                            ((StringBuilder)((Object)entry)).append((String)object2);
                            ((StringBuilder)((Object)entry)).append("\"");
                            ((RequestBuilder)object).addPart(Headers.of("Content-Disposition", ((StringBuilder)((Object)entry)).toString(), "Content-Transfer-Encoding", this.transferEncoding), this.valueConverter.convert(t));
                            continue;
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Part map contained null value for key '");
                        ((StringBuilder)object).append((String)object2);
                        ((StringBuilder)object).append("'.");
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    throw new IllegalArgumentException("Part map contained null key.");
                }
                return;
            }
            object = new IllegalArgumentException("Part map was null.");
            throw object;
        }
    }

    static final class Path<T>
    extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Path(String string2, Converter<T, String> converter, boolean bl) {
            this.name = Utils.checkNotNull(string2, "name == null");
            this.valueConverter = converter;
            this.encoded = bl;
        }

        @Override
        void apply(RequestBuilder object, T t) throws IOException {
            if (t != null) {
                ((RequestBuilder)object).addPathParam(this.name, this.valueConverter.convert(t), this.encoded);
                return;
            }
            object = new StringBuilder();
            ((StringBuilder)object).append("Path parameter \"");
            ((StringBuilder)object).append(this.name);
            ((StringBuilder)object).append("\" value must not be null.");
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
    }

    static final class Query<T>
    extends ParameterHandler<T> {
        private final boolean encoded;
        private final String name;
        private final Converter<T, String> valueConverter;

        Query(String string2, Converter<T, String> converter, boolean bl) {
            this.name = Utils.checkNotNull(string2, "name == null");
            this.valueConverter = converter;
            this.encoded = bl;
        }

        @Override
        void apply(RequestBuilder requestBuilder, T t) throws IOException {
            if (t == null) {
                return;
            }
            requestBuilder.addQueryParam(this.name, this.valueConverter.convert(t), this.encoded);
        }
    }

    static final class QueryMap<T>
    extends ParameterHandler<Map<String, T>> {
        private final boolean encoded;
        private final Converter<T, String> valueConverter;

        QueryMap(Converter<T, String> converter, boolean bl) {
            this.valueConverter = converter;
            this.encoded = bl;
        }

        @Override
        void apply(RequestBuilder object, Map<String, T> object2) throws IOException {
            if (object2 != null) {
                for (Map.Entry<String, Object> entry : object2.entrySet()) {
                    object2 = entry.getKey();
                    if (object2 != null) {
                        if ((entry = entry.getValue()) != null) {
                            ((RequestBuilder)object).addQueryParam((String)object2, this.valueConverter.convert(entry), this.encoded);
                            continue;
                        }
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Query map contained null value for key '");
                        ((StringBuilder)object).append((String)object2);
                        ((StringBuilder)object).append("'.");
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    throw new IllegalArgumentException("Query map contained null key.");
                }
                return;
            }
            object = new IllegalArgumentException("Query map was null.");
            throw object;
        }
    }

    static final class RawPart
    extends ParameterHandler<MultipartBody.Part> {
        static final RawPart INSTANCE = new RawPart();

        private RawPart() {
        }

        @Override
        void apply(RequestBuilder requestBuilder, MultipartBody.Part part) throws IOException {
            if (part != null) {
                requestBuilder.addPart(part);
            }
        }
    }

    static final class RelativeUrl
    extends ParameterHandler<Object> {
        RelativeUrl() {
        }

        @Override
        void apply(RequestBuilder requestBuilder, Object object) {
            requestBuilder.setRelativeUrl(object);
        }
    }
}

