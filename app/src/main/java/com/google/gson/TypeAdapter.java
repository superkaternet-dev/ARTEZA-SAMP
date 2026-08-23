/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

public abstract class TypeAdapter<T> {
    public final T fromJson(Reader reader) throws IOException {
        return this.read(new JsonReader(reader));
    }

    public final T fromJson(String string2) throws IOException {
        return this.fromJson(new StringReader(string2));
    }

    public final T fromJsonTree(JsonElement jsonElement) {
        try {
            JsonTreeReader jsonTreeReader = new JsonTreeReader(jsonElement);
            jsonElement = this.read(jsonTreeReader);
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
        return (T)jsonElement;
    }

    public final TypeAdapter<T> nullSafe() {
        return new TypeAdapter<T>(this){
            final TypeAdapter this$0;
            {
                this.this$0 = typeAdapter;
            }

            @Override
            public T read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return this.this$0.read(jsonReader);
            }

            @Override
            public void write(JsonWriter jsonWriter, T t) throws IOException {
                if (t == null) {
                    jsonWriter.nullValue();
                } else {
                    this.this$0.write(jsonWriter, t);
                }
            }
        };
    }

    public abstract T read(JsonReader var1) throws IOException;

    public final String toJson(T t) {
        StringWriter stringWriter = new StringWriter();
        try {
            this.toJson(stringWriter, t);
            return stringWriter.toString();
        }
        catch (IOException iOException) {
            throw new AssertionError((Object)iOException);
        }
    }

    public final void toJson(Writer writer, T t) throws IOException {
        this.write(new JsonWriter(writer), t);
    }

    public final JsonElement toJsonTree(T object) {
        try {
            JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
            this.write(jsonTreeWriter, object);
            object = jsonTreeWriter.get();
            return object;
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
    }

    public abstract void write(JsonWriter var1, T var2) throws IOException;
}

