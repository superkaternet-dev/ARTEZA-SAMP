/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

public final class TreeTypeAdapter<T>
extends TypeAdapter<T> {
    private final GsonContextImpl context = new GsonContextImpl(this);
    private TypeAdapter<T> delegate;
    private final JsonDeserializer<T> deserializer;
    private final Gson gson;
    private final JsonSerializer<T> serializer;
    private final TypeAdapterFactory skipPast;
    private final TypeToken<T> typeToken;

    public TreeTypeAdapter(JsonSerializer<T> jsonSerializer, JsonDeserializer<T> jsonDeserializer, Gson gson, TypeToken<T> typeToken, TypeAdapterFactory typeAdapterFactory) {
        this.serializer = jsonSerializer;
        this.deserializer = jsonDeserializer;
        this.gson = gson;
        this.typeToken = typeToken;
        this.skipPast = typeAdapterFactory;
    }

    private TypeAdapter<T> delegate() {
        TypeAdapter<T> typeAdapter = this.delegate;
        if (typeAdapter == null) {
            this.delegate = typeAdapter = this.gson.getDelegateAdapter(this.skipPast, this.typeToken);
        }
        return typeAdapter;
    }

    public static TypeAdapterFactory newFactory(TypeToken<?> typeToken, Object object) {
        return new SingleTypeFactory(object, typeToken, false, null);
    }

    public static TypeAdapterFactory newFactoryWithMatchRawType(TypeToken<?> typeToken, Object object) {
        boolean bl = typeToken.getType() == typeToken.getRawType();
        return new SingleTypeFactory(object, typeToken, bl, null);
    }

    public static TypeAdapterFactory newTypeHierarchyFactory(Class<?> clazz, Object object) {
        return new SingleTypeFactory(object, null, false, clazz);
    }

    @Override
    public T read(JsonReader object) throws IOException {
        if (this.deserializer == null) {
            return this.delegate().read((JsonReader)object);
        }
        if (((JsonElement)(object = Streams.parse((JsonReader)object))).isJsonNull()) {
            return null;
        }
        return this.deserializer.deserialize((JsonElement)object, this.typeToken.getType(), this.context);
    }

    @Override
    public void write(JsonWriter jsonWriter, T t) throws IOException {
        JsonSerializer<T> jsonSerializer = this.serializer;
        if (jsonSerializer == null) {
            this.delegate().write(jsonWriter, t);
            return;
        }
        if (t == null) {
            jsonWriter.nullValue();
            return;
        }
        Streams.write(jsonSerializer.serialize(t, this.typeToken.getType(), this.context), jsonWriter);
    }

    private final class GsonContextImpl
    implements JsonSerializationContext,
    JsonDeserializationContext {
        final TreeTypeAdapter this$0;

        private GsonContextImpl(TreeTypeAdapter treeTypeAdapter) {
            this.this$0 = treeTypeAdapter;
        }

        public <R> R deserialize(JsonElement jsonElement, Type type) throws JsonParseException {
            return (R)this.this$0.gson.fromJson(jsonElement, type);
        }

        @Override
        public JsonElement serialize(Object object) {
            return this.this$0.gson.toJsonTree(object);
        }

        @Override
        public JsonElement serialize(Object object, Type type) {
            return this.this$0.gson.toJsonTree(object, type);
        }
    }

    private static final class SingleTypeFactory
    implements TypeAdapterFactory {
        private final JsonDeserializer<?> deserializer;
        private final TypeToken<?> exactType;
        private final Class<?> hierarchyType;
        private final boolean matchRawType;
        private final JsonSerializer<?> serializer;

        SingleTypeFactory(Object object, TypeToken<?> typeToken, boolean bl, Class<?> clazz) {
            boolean bl2 = object instanceof JsonSerializer;
            JsonDeserializer jsonDeserializer = null;
            JsonSerializer jsonSerializer = bl2 ? (JsonSerializer)object : null;
            this.serializer = jsonSerializer;
            if (object instanceof JsonDeserializer) {
                jsonDeserializer = (JsonDeserializer)object;
            }
            this.deserializer = jsonDeserializer;
            bl2 = jsonSerializer != null || jsonDeserializer != null;
            $Gson$Preconditions.checkArgument(bl2);
            this.exactType = typeToken;
            this.matchRawType = bl;
            this.hierarchyType = clazz;
        }

        @Override
        public <T> TypeAdapter<T> create(Gson treeTypeAdapter, TypeToken<T> typeToken) {
            TypeToken<?> typeToken2 = this.exactType;
            boolean bl = typeToken2 != null ? typeToken2.equals(typeToken) || this.matchRawType && this.exactType.getType() == typeToken.getRawType() : this.hierarchyType.isAssignableFrom(typeToken.getRawType());
            treeTypeAdapter = bl ? new TreeTypeAdapter(this.serializer, this.deserializer, (Gson)((Object)treeTypeAdapter), typeToken, this) : null;
            return treeTypeAdapter;
        }
    }
}

