/*
 * Decompiled with CFR 0.152.
 */
package retrofit2.converter.gson;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Converter;

final class GsonRequestBodyConverter<T>
implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");
    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private final TypeAdapter<T> adapter;
    private final Gson gson;

    GsonRequestBodyConverter(Gson gson, TypeAdapter<T> typeAdapter) {
        this.gson = gson;
        this.adapter = typeAdapter;
    }

    @Override
    public RequestBody convert(T t) throws IOException {
        Buffer buffer = new Buffer();
        Closeable closeable = new OutputStreamWriter(buffer.outputStream(), UTF_8);
        closeable = this.gson.newJsonWriter((Writer)closeable);
        this.adapter.write((JsonWriter)closeable, t);
        ((JsonWriter)closeable).close();
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString());
    }
}

