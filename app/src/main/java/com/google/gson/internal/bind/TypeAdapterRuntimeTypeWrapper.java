/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

final class TypeAdapterRuntimeTypeWrapper<T>
extends TypeAdapter<T> {
    private final Gson context;
    private final TypeAdapter<T> delegate;
    private final Type type;

    TypeAdapterRuntimeTypeWrapper(Gson gson, TypeAdapter<T> typeAdapter, Type type) {
        this.context = gson;
        this.delegate = typeAdapter;
        this.type = type;
    }

    private Type getRuntimeTypeIfMoreSpecific(Type clazz, Object object) {
        Class<?> clazz2;
        block2: {
            block3: {
                clazz2 = clazz;
                if (object == null) break block2;
                if (clazz == Object.class || clazz instanceof TypeVariable) break block3;
                clazz2 = clazz;
                if (!(clazz instanceof Class)) break block2;
            }
            clazz2 = object.getClass();
        }
        return clazz2;
    }

    @Override
    public T read(JsonReader jsonReader) throws IOException {
        return this.delegate.read(jsonReader);
    }

    @Override
    public void write(JsonWriter jsonWriter, T t) throws IOException {
        TypeAdapter<Object> typeAdapter = this.delegate;
        Type type = this.getRuntimeTypeIfMoreSpecific(this.type, t);
        if (type != this.type && (typeAdapter = this.context.getAdapter(TypeToken.get(type))) instanceof ReflectiveTypeAdapterFactory.Adapter && !(this.delegate instanceof ReflectiveTypeAdapterFactory.Adapter)) {
            typeAdapter = this.delegate;
        }
        typeAdapter.write(jsonWriter, t);
    }
}

