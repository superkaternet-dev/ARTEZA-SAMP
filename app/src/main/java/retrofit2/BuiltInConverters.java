/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.Utils;
import retrofit2.http.Streaming;

final class BuiltInConverters
extends Converter.Factory {
    BuiltInConverters() {
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] annotationArray, Annotation[] annotationArray2, Retrofit retrofit) {
        if (RequestBody.class.isAssignableFrom(Utils.getRawType(type))) {
            return RequestBodyConverter.INSTANCE;
        }
        return null;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotationArray, Retrofit retrofit) {
        if (type == ResponseBody.class) {
            if (Utils.isAnnotationPresent(annotationArray, Streaming.class)) {
                return StreamingResponseBodyConverter.INSTANCE;
            }
            return BufferingResponseBodyConverter.INSTANCE;
        }
        if (type == Void.class) {
            return VoidResponseBodyConverter.INSTANCE;
        }
        return null;
    }

    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotationArray, Retrofit retrofit) {
        if (type == String.class) {
            return StringConverter.INSTANCE;
        }
        return null;
    }

    static final class BufferingResponseBodyConverter
    implements Converter<ResponseBody, ResponseBody> {
        static final BufferingResponseBodyConverter INSTANCE = new BufferingResponseBodyConverter();

        BufferingResponseBodyConverter() {
        }

        @Override
        public ResponseBody convert(ResponseBody responseBody) throws IOException {
            try {
                ResponseBody responseBody2 = Utils.buffer(responseBody);
                return responseBody2;
            }
            finally {
                responseBody.close();
            }
        }
    }

    static final class RequestBodyConverter
    implements Converter<RequestBody, RequestBody> {
        static final RequestBodyConverter INSTANCE = new RequestBodyConverter();

        RequestBodyConverter() {
        }

        @Override
        public RequestBody convert(RequestBody requestBody) throws IOException {
            return requestBody;
        }
    }

    static final class StreamingResponseBodyConverter
    implements Converter<ResponseBody, ResponseBody> {
        static final StreamingResponseBodyConverter INSTANCE = new StreamingResponseBodyConverter();

        StreamingResponseBodyConverter() {
        }

        @Override
        public ResponseBody convert(ResponseBody responseBody) throws IOException {
            return responseBody;
        }
    }

    static final class StringConverter
    implements Converter<String, String> {
        static final StringConverter INSTANCE = new StringConverter();

        StringConverter() {
        }

        @Override
        public String convert(String string2) throws IOException {
            return string2;
        }
    }

    static final class ToStringConverter
    implements Converter<Object, String> {
        static final ToStringConverter INSTANCE = new ToStringConverter();

        ToStringConverter() {
        }

        @Override
        public String convert(Object object) {
            return object.toString();
        }
    }

    static final class VoidResponseBodyConverter
    implements Converter<ResponseBody, Void> {
        static final VoidResponseBodyConverter INSTANCE = new VoidResponseBodyConverter();

        VoidResponseBodyConverter() {
        }

        @Override
        public Void convert(ResponseBody responseBody) throws IOException {
            responseBody.close();
            return null;
        }
    }
}

