/*
 * Decompiled with CFR 0.152.
 */
package retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonRequestBodyConverter;
import retrofit2.converter.gson.GsonResponseBodyConverter;

public final class GsonConverterFactory
extends Converter.Factory {
    private final Gson gson;

    private GsonConverterFactory(Gson gson) {
        if (gson != null) {
            this.gson = gson;
            return;
        }
        throw new NullPointerException("gson == null");
    }

    public static GsonConverterFactory create() {
        return GsonConverterFactory.create(new Gson());
    }

    public static GsonConverterFactory create(Gson gson) {
        return new GsonConverterFactory(gson);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type object, Annotation[] annotationArray, Annotation[] annotationArray2, Retrofit retrofit) {
        object = this.gson.getAdapter(TypeToken.get((Type)object));
        return new GsonRequestBodyConverter(this.gson, object);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type object, Annotation[] annotationArray, Retrofit retrofit) {
        object = this.gson.getAdapter(TypeToken.get((Type)object));
        return new GsonResponseBodyConverter(this.gson, object);
    }
}

