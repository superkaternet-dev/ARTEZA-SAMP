/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;

public final class JsonAdapterAnnotationTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        JsonAdapter jsonAdapter = typeToken.getRawType().getAnnotation(JsonAdapter.class);
        if (jsonAdapter == null) {
            return null;
        }
        return this.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
    }

    TypeAdapter<?> getTypeAdapter(ConstructorConstructor object, Gson typeAdapter, TypeToken<?> typeToken, JsonAdapter object2) {
        Object obj = ((ConstructorConstructor)object).get(TypeToken.get(object2.value())).construct();
        if (obj instanceof TypeAdapter) {
            object = (TypeAdapter)obj;
        } else if (obj instanceof TypeAdapterFactory) {
            object = ((TypeAdapterFactory)obj).create((Gson)((Object)typeAdapter), typeToken);
        } else {
            if (!(obj instanceof JsonSerializer) && !(obj instanceof JsonDeserializer)) {
                throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter, TypeAdapterFactory, JsonSerializer or JsonDeserializer reference.");
            }
            boolean bl = obj instanceof JsonSerializer;
            object2 = null;
            object = bl ? (JsonSerializer)obj : null;
            if (obj instanceof JsonDeserializer) {
                object2 = (JsonDeserializer)obj;
            }
            object = new TreeTypeAdapter((JsonSerializer<?>)object, (JsonDeserializer<?>)object2, (Gson)((Object)typeAdapter), typeToken, null);
        }
        typeAdapter = object;
        if (object != null) {
            typeAdapter = ((TypeAdapter)object).nullSafe();
        }
        return typeAdapter;
    }
}

