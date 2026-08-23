/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingStrategy, Excluder excluder, JsonAdapterAnnotationTypeAdapterFactory jsonAdapterAnnotationTypeAdapterFactory) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingStrategy;
        this.excluder = excluder;
        this.jsonAdapterFactory = jsonAdapterAnnotationTypeAdapterFactory;
    }

    private BoundField createBoundField(Gson gson, Field field, String string2, TypeToken<?> typeToken, boolean bl, boolean bl2) {
        boolean bl3 = Primitives.isPrimitive(typeToken.getRawType());
        Object object = field.getAnnotation(JsonAdapter.class);
        TypeAdapter<?> typeAdapter = null;
        if (object != null) {
            typeAdapter = this.jsonAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, (JsonAdapter)object);
        }
        boolean bl4 = typeAdapter != null;
        object = typeAdapter;
        if (typeAdapter == null) {
            object = gson.getAdapter(typeToken);
        }
        return new BoundField(this, string2, bl, bl2, field, bl4, (TypeAdapter)object, gson, typeToken, bl3){
            final ReflectiveTypeAdapterFactory this$0;
            final Gson val$context;
            final Field val$field;
            final TypeToken val$fieldType;
            final boolean val$isPrimitive;
            final boolean val$jsonAdapterPresent;
            final TypeAdapter val$typeAdapter;
            {
                this.this$0 = reflectiveTypeAdapterFactory;
                this.val$field = field;
                this.val$jsonAdapterPresent = bl3;
                this.val$typeAdapter = typeAdapter;
                this.val$context = gson;
                this.val$fieldType = typeToken;
                this.val$isPrimitive = bl4;
                super(string2, bl, bl2);
            }

            @Override
            void read(JsonReader jsonReader, Object object) throws IOException, IllegalAccessException {
                if ((jsonReader = this.val$typeAdapter.read(jsonReader)) != null || !this.val$isPrimitive) {
                    this.val$field.set(object, jsonReader);
                }
            }

            @Override
            void write(JsonWriter jsonWriter, Object typeAdapter) throws IOException, IllegalAccessException {
                Object object = this.val$field.get(typeAdapter);
                typeAdapter = this.val$jsonAdapterPresent ? this.val$typeAdapter : new TypeAdapterRuntimeTypeWrapper<Object>(this.val$context, this.val$typeAdapter, this.val$fieldType.getType());
                ((TypeAdapter)typeAdapter).write(jsonWriter, object);
            }

            @Override
            public boolean writeField(Object object) throws IOException, IllegalAccessException {
                boolean bl = this.serialized;
                boolean bl2 = false;
                if (!bl) {
                    return false;
                }
                if (this.val$field.get(object) != object) {
                    bl2 = true;
                }
                return bl2;
            }
        };
    }

    static boolean excludeField(Field field, boolean bl, Excluder excluder) {
        bl = !excluder.excludeClass(field.getType(), bl) && !excluder.excludeField(field, bl);
        return bl;
    }

    private Map<String, BoundField> getBoundFields(Gson object, TypeToken<?> object2, Class<?> clazz) {
        LinkedHashMap<String, BoundField> linkedHashMap = new LinkedHashMap<String, BoundField>();
        if (clazz.isInterface()) {
            return linkedHashMap;
        }
        Type type = ((TypeToken)object2).getType();
        TypeToken<?> typeToken = object2;
        while (clazz != Object.class) {
            for (Field field : clazz.getDeclaredFields()) {
                boolean bl = this.excludeField(field, true);
                boolean bl2 = this.excludeField(field, false);
                if (!bl && !bl2) continue;
                field.setAccessible(true);
                Type type2 = $Gson$Types.resolve(typeToken.getType(), clazz, field.getGenericType());
                List<String> list = this.getFieldNames(field);
                object2 = null;
                for (int i = 0; i < list.size(); ++i) {
                    Object object3 = list.get(i);
                    if (i != 0) {
                        bl = false;
                    }
                    object3 = linkedHashMap.put((String)object3, this.createBoundField((Gson)object, field, (String)object3, TypeToken.get(type2), bl, bl2));
                    if (object2 != null) continue;
                    object2 = object3;
                }
                if (object2 == null) {
                    continue;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append(type);
                ((StringBuilder)object).append(" declares multiple JSON fields named ");
                ((StringBuilder)object).append(((BoundField)object2).name);
                throw new IllegalArgumentException(((StringBuilder)object).toString());
            }
            typeToken = TypeToken.get($Gson$Types.resolve(typeToken.getType(), clazz, clazz.getGenericSuperclass()));
            clazz = typeToken.getRawType();
        }
        return linkedHashMap;
    }

    private List<String> getFieldNames(Field object) {
        Object object2 = ((Field)object).getAnnotation(SerializedName.class);
        if (object2 == null) {
            return Collections.singletonList(this.fieldNamingPolicy.translateName((Field)object));
        }
        object = object2.value();
        String[] stringArray = object2.alternate();
        if (stringArray.length == 0) {
            return Collections.singletonList(object);
        }
        object2 = new ArrayList(stringArray.length + 1);
        object2.add(object);
        int n = stringArray.length;
        for (int i = 0; i < n; ++i) {
            object2.add(stringArray[i]);
        }
        return object2;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<T> clazz = typeToken.getRawType();
        if (!Object.class.isAssignableFrom(clazz)) {
            return null;
        }
        return new Adapter<T>(this.constructorConstructor.get(typeToken), this.getBoundFields(gson, typeToken, clazz));
    }

    public boolean excludeField(Field field, boolean bl) {
        return ReflectiveTypeAdapterFactory.excludeField(field, bl, this.excluder);
    }

    public static final class Adapter<T>
    extends TypeAdapter<T> {
        private final Map<String, BoundField> boundFields;
        private final ObjectConstructor<T> constructor;

        Adapter(ObjectConstructor<T> objectConstructor, Map<String, BoundField> map) {
            this.constructor = objectConstructor;
            this.boundFields = map;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public T read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            T t = this.constructor.construct();
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    Object object = jsonReader.nextName();
                    if ((object = this.boundFields.get(object)) != null && ((BoundField)object).deserialized) {
                        ((BoundField)object).read(jsonReader, t);
                        continue;
                    }
                    jsonReader.skipValue();
                }
            }
            catch (IllegalAccessException illegalAccessException) {
                throw new AssertionError((Object)illegalAccessException);
            }
            catch (IllegalStateException illegalStateException) {
                JsonSyntaxException jsonSyntaxException = new JsonSyntaxException(illegalStateException);
                throw jsonSyntaxException;
            }
            jsonReader.endObject();
            return t;
        }

        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
            if (t == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    if (!boundField.writeField(t)) continue;
                    jsonWriter.name(boundField.name);
                    boundField.write(jsonWriter, t);
                }
                jsonWriter.endObject();
            }
            catch (IllegalAccessException illegalAccessException) {
                AssertionError assertionError = new AssertionError((Object)illegalAccessException);
                throw assertionError;
            }
        }
    }

    static abstract class BoundField {
        final boolean deserialized;
        final String name;
        final boolean serialized;

        protected BoundField(String string2, boolean bl, boolean bl2) {
            this.name = string2;
            this.serialized = bl;
            this.deserialized = bl2;
        }

        abstract void read(JsonReader var1, Object var2) throws IOException, IllegalAccessException;

        abstract void write(JsonWriter var1, Object var2) throws IOException, IllegalAccessException;

        abstract boolean writeField(Object var1) throws IOException, IllegalAccessException;
    }
}

