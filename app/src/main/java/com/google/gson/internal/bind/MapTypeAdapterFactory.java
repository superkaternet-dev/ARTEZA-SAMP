/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public final class MapTypeAdapterFactory
implements TypeAdapterFactory {
    final boolean complexMapKeySerialization;
    private final ConstructorConstructor constructorConstructor;

    public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean bl) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = bl;
    }

    private TypeAdapter<?> getKeyAdapter(Gson typeAdapter, Type type) {
        typeAdapter = type != Boolean.TYPE && type != Boolean.class ? ((Gson)((Object)typeAdapter)).getAdapter(TypeToken.get(type)) : TypeAdapters.BOOLEAN_AS_STRING;
        return typeAdapter;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> object) {
        Object object2 = ((TypeToken)object).getType();
        if (!Map.class.isAssignableFrom(((TypeToken)object).getRawType())) {
            return null;
        }
        Type[] typeArray = $Gson$Types.getMapKeyAndValueTypes((Type)object2, $Gson$Types.getRawType((Type)object2));
        object2 = this.getKeyAdapter(gson, typeArray[0]);
        TypeAdapter<?> typeAdapter = gson.getAdapter(TypeToken.get(typeArray[1]));
        object = this.constructorConstructor.get(object);
        return new Adapter(this, gson, typeArray[0], object2, typeArray[1], typeAdapter, object);
    }

    private final class Adapter<K, V>
    extends TypeAdapter<Map<K, V>> {
        private final ObjectConstructor<? extends Map<K, V>> constructor;
        private final TypeAdapter<K> keyTypeAdapter;
        final MapTypeAdapterFactory this$0;
        private final TypeAdapter<V> valueTypeAdapter;

        public Adapter(MapTypeAdapterFactory mapTypeAdapterFactory, Gson gson, Type type, TypeAdapter<K> typeAdapter, Type type2, TypeAdapter<V> typeAdapter2, ObjectConstructor<? extends Map<K, V>> objectConstructor) {
            this.this$0 = mapTypeAdapterFactory;
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(gson, typeAdapter, type);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(gson, typeAdapter2, type2);
            this.constructor = objectConstructor;
        }

        private String keyToString(JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive()) {
                if (((JsonPrimitive)(jsonElement = jsonElement.getAsJsonPrimitive())).isNumber()) {
                    return String.valueOf(((JsonPrimitive)jsonElement).getAsNumber());
                }
                if (((JsonPrimitive)jsonElement).isBoolean()) {
                    return Boolean.toString(((JsonPrimitive)jsonElement).getAsBoolean());
                }
                if (((JsonPrimitive)jsonElement).isString()) {
                    return ((JsonPrimitive)jsonElement).getAsString();
                }
                throw new AssertionError();
            }
            if (jsonElement.isJsonNull()) {
                return "null";
            }
            throw new AssertionError();
        }

        @Override
        public Map<K, V> read(JsonReader object) throws IOException {
            JsonToken jsonToken = ((JsonReader)object).peek();
            if (jsonToken == JsonToken.NULL) {
                ((JsonReader)object).nextNull();
                return null;
            }
            Map<JsonToken, V> map = this.constructor.construct();
            if (jsonToken == JsonToken.BEGIN_ARRAY) {
                ((JsonReader)object).beginArray();
                while (((JsonReader)object).hasNext()) {
                    ((JsonReader)object).beginArray();
                    jsonToken = this.keyTypeAdapter.read((JsonReader)object);
                    if (map.put(jsonToken, this.valueTypeAdapter.read((JsonReader)object)) == null) {
                        ((JsonReader)object).endArray();
                        continue;
                    }
                    object = new StringBuilder();
                    ((StringBuilder)object).append("duplicate key: ");
                    ((StringBuilder)object).append((Object)jsonToken);
                    throw new JsonSyntaxException(((StringBuilder)object).toString());
                }
                ((JsonReader)object).endArray();
            } else {
                ((JsonReader)object).beginObject();
                while (((JsonReader)object).hasNext()) {
                    JsonReaderInternalAccess.INSTANCE.promoteNameToValue((JsonReader)object);
                    jsonToken = this.keyTypeAdapter.read((JsonReader)object);
                    if (map.put(jsonToken, this.valueTypeAdapter.read((JsonReader)object)) == null) continue;
                    object = new StringBuilder();
                    ((StringBuilder)object).append("duplicate key: ");
                    ((StringBuilder)object).append((Object)jsonToken);
                    throw new JsonSyntaxException(((StringBuilder)object).toString());
                }
                ((JsonReader)object).endObject();
            }
            return map;
        }

        @Override
        public void write(JsonWriter jsonWriter, Map<K, V> iterator2) throws IOException {
            if (iterator2 == null) {
                jsonWriter.nullValue();
                return;
            }
            if (!this.this$0.complexMapKeySerialization) {
                jsonWriter.beginObject();
                for (Map.Entry entry : iterator2.entrySet()) {
                    jsonWriter.name(String.valueOf(entry.getKey()));
                    this.valueTypeAdapter.write(jsonWriter, entry.getValue());
                }
                jsonWriter.endObject();
                return;
            }
            int n = 0;
            ArrayList<JsonElement> arrayList = new ArrayList<JsonElement>(iterator2.size());
            ArrayList arrayList2 = new ArrayList(iterator2.size());
            for (Map.Entry entry : iterator2.entrySet()) {
                JsonElement jsonElement = this.keyTypeAdapter.toJsonTree(entry.getKey());
                arrayList.add(jsonElement);
                arrayList2.add(entry.getValue());
                int n2 = !jsonElement.isJsonArray() && !jsonElement.isJsonObject() ? 0 : 1;
                n |= n2;
            }
            if (n != 0) {
                jsonWriter.beginArray();
                for (n = 0; n < arrayList.size(); ++n) {
                    jsonWriter.beginArray();
                    Streams.write((JsonElement)arrayList.get(n), jsonWriter);
                    this.valueTypeAdapter.write(jsonWriter, arrayList2.get(n));
                    jsonWriter.endArray();
                }
                jsonWriter.endArray();
            } else {
                jsonWriter.beginObject();
                for (n = 0; n < arrayList.size(); ++n) {
                    jsonWriter.name(this.keyToString((JsonElement)arrayList.get(n)));
                    this.valueTypeAdapter.write(jsonWriter, arrayList2.get(n));
                }
                jsonWriter.endObject();
            }
        }
    }
}

