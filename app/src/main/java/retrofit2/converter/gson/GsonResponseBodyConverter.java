/*
 * Decompiled with CFR 0.152.
 */
package retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T>
implements Converter<ResponseBody, T> {
    private final TypeAdapter<T> adapter;
    private final Gson gson;

    GsonResponseBodyConverter(Gson gson, TypeAdapter<T> typeAdapter) {
        this.gson = gson;
        this.adapter = typeAdapter;
    }

    @Override
    public T convert(ResponseBody responseBody) throws IOException {
        JsonReader jsonReader = this.gson.newJsonReader(responseBody.charStream());
        try {
            jsonReader = this.adapter.read(jsonReader);
            return (T)jsonReader;
        }
        finally {
            responseBody.close();
        }
    }
}

