/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public final class Gson {
    static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
    static final boolean DEFAULT_ESCAPE_HTML = true;
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    static final boolean DEFAULT_LENIENT = false;
    static final boolean DEFAULT_PRETTY_PRINT = false;
    static final boolean DEFAULT_SERIALIZE_NULLS = false;
    static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private static final TypeToken<?> NULL_KEY_SURROGATE = new TypeToken<Object>(){};
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls = new ThreadLocal();
    private final ConstructorConstructor constructorConstructor;
    private final Excluder excluder;
    private final List<TypeAdapterFactory> factories;
    private final FieldNamingStrategy fieldNamingStrategy;
    private final boolean generateNonExecutableJson;
    private final boolean htmlSafe;
    private final JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory;
    private final boolean lenient;
    private final boolean prettyPrinting;
    private final boolean serializeNulls;
    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache = new ConcurrentHashMap();

    public Gson() {
        this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, false, LongSerializationPolicy.DEFAULT, Collections.emptyList());
    }

    Gson(Excluder excluder, FieldNamingStrategy fieldNamingStrategy, Map<Type, InstanceCreator<?>> object, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6, boolean bl7, LongSerializationPolicy object2, List<TypeAdapterFactory> list) {
        this.constructorConstructor = object = new ConstructorConstructor((Map<Type, InstanceCreator<?>>)object);
        this.excluder = excluder;
        this.fieldNamingStrategy = fieldNamingStrategy;
        this.serializeNulls = bl;
        this.generateNonExecutableJson = bl3;
        this.htmlSafe = bl4;
        this.prettyPrinting = bl5;
        this.lenient = bl6;
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        arrayList.add(ObjectTypeAdapter.FACTORY);
        arrayList.add(excluder);
        arrayList.addAll(list);
        arrayList.add(TypeAdapters.STRING_FACTORY);
        arrayList.add(TypeAdapters.INTEGER_FACTORY);
        arrayList.add(TypeAdapters.BOOLEAN_FACTORY);
        arrayList.add(TypeAdapters.BYTE_FACTORY);
        arrayList.add(TypeAdapters.SHORT_FACTORY);
        object2 = Gson.longAdapter(object2);
        arrayList.add(TypeAdapters.newFactory(Long.TYPE, Long.class, object2));
        arrayList.add(TypeAdapters.newFactory(Double.TYPE, Double.class, this.doubleAdapter(bl7)));
        arrayList.add(TypeAdapters.newFactory(Float.TYPE, Float.class, this.floatAdapter(bl7)));
        arrayList.add(TypeAdapters.NUMBER_FACTORY);
        arrayList.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        arrayList.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        arrayList.add(TypeAdapters.newFactory(AtomicLong.class, Gson.atomicLongAdapter((TypeAdapter<Number>)object2)));
        arrayList.add(TypeAdapters.newFactory(AtomicLongArray.class, Gson.atomicLongArrayAdapter((TypeAdapter<Number>)object2)));
        arrayList.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        arrayList.add(TypeAdapters.CHARACTER_FACTORY);
        arrayList.add(TypeAdapters.STRING_BUILDER_FACTORY);
        arrayList.add(TypeAdapters.STRING_BUFFER_FACTORY);
        arrayList.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        arrayList.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        arrayList.add(TypeAdapters.URL_FACTORY);
        arrayList.add(TypeAdapters.URI_FACTORY);
        arrayList.add(TypeAdapters.UUID_FACTORY);
        arrayList.add(TypeAdapters.CURRENCY_FACTORY);
        arrayList.add(TypeAdapters.LOCALE_FACTORY);
        arrayList.add(TypeAdapters.INET_ADDRESS_FACTORY);
        arrayList.add(TypeAdapters.BIT_SET_FACTORY);
        arrayList.add(DateTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.CALENDAR_FACTORY);
        arrayList.add(TimeTypeAdapter.FACTORY);
        arrayList.add(SqlDateTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.TIMESTAMP_FACTORY);
        arrayList.add(ArrayTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.CLASS_FACTORY);
        arrayList.add(new CollectionTypeAdapterFactory((ConstructorConstructor)object));
        arrayList.add(new MapTypeAdapterFactory((ConstructorConstructor)object, bl2));
        object2 = new JsonAdapterAnnotationTypeAdapterFactory((ConstructorConstructor)object);
        this.jsonAdapterFactory = object2;
        arrayList.add(object2);
        arrayList.add(TypeAdapters.ENUM_FACTORY);
        arrayList.add(new ReflectiveTypeAdapterFactory((ConstructorConstructor)object, fieldNamingStrategy, excluder, (JsonAdapterAnnotationTypeAdapterFactory)object2));
        this.factories = Collections.unmodifiableList(arrayList);
    }

    private static void assertFullConsumption(Object object, JsonReader jsonReader) {
        if (object != null) {
            try {
                if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                    object = new JsonIOException("JSON document was not fully consumed.");
                    throw object;
                }
            }
            catch (IOException iOException) {
                throw new JsonIOException(iOException);
            }
            catch (MalformedJsonException malformedJsonException) {
                throw new JsonSyntaxException(malformedJsonException);
            }
        }
    }

    private static TypeAdapter<AtomicLong> atomicLongAdapter(TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLong>(typeAdapter){
            final TypeAdapter val$longAdapter;
            {
                this.val$longAdapter = typeAdapter;
            }

            @Override
            public AtomicLong read(JsonReader jsonReader) throws IOException {
                return new AtomicLong(((Number)this.val$longAdapter.read(jsonReader)).longValue());
            }

            @Override
            public void write(JsonWriter jsonWriter, AtomicLong atomicLong) throws IOException {
                this.val$longAdapter.write(jsonWriter, atomicLong.get());
            }
        }.nullSafe();
    }

    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLongArray>(typeAdapter){
            final TypeAdapter val$longAdapter;
            {
                this.val$longAdapter = typeAdapter;
            }

            @Override
            public AtomicLongArray read(JsonReader object) throws IOException {
                ArrayList<Long> arrayList = new ArrayList<Long>();
                ((JsonReader)object).beginArray();
                while (((JsonReader)object).hasNext()) {
                    arrayList.add(((Number)this.val$longAdapter.read((JsonReader)object)).longValue());
                }
                ((JsonReader)object).endArray();
                int n = arrayList.size();
                object = new AtomicLongArray(n);
                for (int i = 0; i < n; ++i) {
                    ((AtomicLongArray)object).set(i, (Long)arrayList.get(i));
                }
                return object;
            }

            @Override
            public void write(JsonWriter jsonWriter, AtomicLongArray atomicLongArray) throws IOException {
                jsonWriter.beginArray();
                int n = atomicLongArray.length();
                for (int i = 0; i < n; ++i) {
                    this.val$longAdapter.write(jsonWriter, atomicLongArray.get(i));
                }
                jsonWriter.endArray();
            }
        }.nullSafe();
    }

    static void checkValidFloatingPoint(double d) {
        if (!Double.isNaN(d) && !Double.isInfinite(d)) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(d);
        stringBuilder.append(" is not a valid double value as per JSON specification. To override this");
        stringBuilder.append(" behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private TypeAdapter<Number> doubleAdapter(boolean bl) {
        if (bl) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>(this){
            final Gson this$0;
            {
                this.this$0 = gson;
            }

            @Override
            public Double read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextDouble();
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                Gson.checkValidFloatingPoint(number.doubleValue());
                jsonWriter.value(number);
            }
        };
    }

    private TypeAdapter<Number> floatAdapter(boolean bl) {
        if (bl) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>(this){
            final Gson this$0;
            {
                this.this$0 = gson;
            }

            @Override
            public Float read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return Float.valueOf((float)jsonReader.nextDouble());
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                Gson.checkValidFloatingPoint(number.floatValue());
                jsonWriter.value(number);
            }
        };
    }

    private static TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextLong();
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.value(number.toString());
            }
        };
    }

    public Excluder excluder() {
        return this.excluder;
    }

    public FieldNamingStrategy fieldNamingStrategy() {
        return this.fieldNamingStrategy;
    }

    public <T> T fromJson(JsonElement jsonElement, Class<T> clazz) throws JsonSyntaxException {
        jsonElement = this.fromJson(jsonElement, (Type)clazz);
        return Primitives.wrap(clazz).cast(jsonElement);
    }

    public <T> T fromJson(JsonElement jsonElement, Type type) throws JsonSyntaxException {
        if (jsonElement == null) {
            return null;
        }
        return this.fromJson(new JsonTreeReader(jsonElement), type);
    }

    /*
     * Exception decompiling
     */
    public <T> T fromJson(JsonReader var1_1, Type var2_2) throws JsonIOException, JsonSyntaxException {
        /*
         * This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
         * 
         * org.benf.cfr.reader.util.ConfusedCFRException: Back jump on a try block [egrp 2[TRYBLOCK] [8 : 46->60)] java.lang.Throwable
         *     at org.benf.cfr.reader.bytecode.analysis.opgraph.Op02WithProcessedDataAndRefs.insertExceptionBlocks(Op02WithProcessedDataAndRefs.java:2283)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:415)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:278)
         *     at org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:201)
         *     at org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:94)
         *     at org.benf.cfr.reader.entities.Method.analyse(Method.java:531)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:1055)
         *     at org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:942)
         *     at org.benf.cfr.reader.Driver.doJarVersionTypes(Driver.java:257)
         *     at org.benf.cfr.reader.Driver.doJar(Driver.java:139)
         *     at org.benf.cfr.reader.CfrDriverImpl.analyse(CfrDriverImpl.java:76)
         *     at org.benf.cfr.reader.Main.main(Main.java:54)
         */
        throw new IllegalStateException("Decompilation failed");
    }

    public <T> T fromJson(Reader reader, Class<T> clazz) throws JsonSyntaxException, JsonIOException {
        JsonReader jsonReader = this.newJsonReader(reader);
        reader = this.fromJson(jsonReader, clazz);
        Gson.assertFullConsumption(reader, jsonReader);
        return Primitives.wrap(clazz).cast(reader);
    }

    public <T> T fromJson(Reader closeable, Type type) throws JsonIOException, JsonSyntaxException {
        closeable = this.newJsonReader((Reader)closeable);
        type = this.fromJson((JsonReader)closeable, type);
        Gson.assertFullConsumption(type, (JsonReader)closeable);
        return (T)type;
    }

    public <T> T fromJson(String string2, Class<T> clazz) throws JsonSyntaxException {
        string2 = this.fromJson(string2, (Type)clazz);
        return Primitives.wrap(clazz).cast(string2);
    }

    public <T> T fromJson(String string2, Type type) throws JsonSyntaxException {
        if (string2 == null) {
            return null;
        }
        return this.fromJson((Reader)new StringReader(string2), type);
    }

    public <T> TypeAdapter<T> getAdapter(TypeToken<T> typeToken) {
        TypeAdapter<T> typeAdapter;
        Object object = this.typeTokenCache;
        Object object2 = typeToken == null ? NULL_KEY_SURROGATE : typeToken;
        if ((object2 = object.get(object2)) != null) {
            return object2;
        }
        object = this.calls.get();
        boolean bl = false;
        object2 = object;
        if (object == null) {
            object2 = new HashMap();
            this.calls.set((Map<TypeToken<?>, FutureTypeAdapter<?>>)object2);
            bl = true;
        }
        if ((object = (FutureTypeAdapter)object2.get(typeToken)) != null) {
            return object;
        }
        try {
            object = new FutureTypeAdapter();
            object2.put(typeToken, object);
            Iterator<TypeAdapterFactory> iterator2 = this.factories.iterator();
            while (iterator2.hasNext()) {
                typeAdapter = iterator2.next().create(this, typeToken);
                if (typeAdapter == null) continue;
            }
        }
        catch (Throwable throwable) {
            block9: {
                object2.remove(typeToken);
                if (!bl) break block9;
                this.calls.remove();
            }
            throw throwable;
        }
        {
            ((FutureTypeAdapter)object).setDelegate(typeAdapter);
            this.typeTokenCache.put(typeToken, typeAdapter);
            object2.remove(typeToken);
            if (bl) {
                this.calls.remove();
            }
            return typeAdapter;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("GSON cannot handle ");
        ((StringBuilder)object).append(typeToken);
        typeAdapter = new IllegalArgumentException(((StringBuilder)object).toString());
        throw typeAdapter;
    }

    public <T> TypeAdapter<T> getAdapter(Class<T> clazz) {
        return this.getAdapter(TypeToken.get(clazz));
    }

    public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory object, TypeToken<T> typeToken) {
        Iterator<TypeAdapterFactory> typeAdapterFactory = object;
        if (!this.factories.contains(object)) {
            typeAdapterFactory = this.jsonAdapterFactory;
        }
        boolean bl = false;
        for (TypeAdapterFactory typeAdapterFactory2 : this.factories) {
            if (!bl) {
                if (typeAdapterFactory2 != typeAdapterFactory) continue;
                bl = true;
                continue;
            }
            TypeAdapter<T> typeAdapter = typeAdapterFactory2.create(this, typeToken);
            if (typeAdapter == null) continue;
            return typeAdapter;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("GSON cannot serialize ");
        ((StringBuilder)object).append(typeToken);
        object = new IllegalArgumentException(((StringBuilder)object).toString());
        throw object;
    }

    public boolean htmlSafe() {
        return this.htmlSafe;
    }

    public JsonReader newJsonReader(Reader closeable) {
        closeable = new JsonReader((Reader)closeable);
        ((JsonReader)closeable).setLenient(this.lenient);
        return closeable;
    }

    public JsonWriter newJsonWriter(Writer closeable) throws IOException {
        if (this.generateNonExecutableJson) {
            ((Writer)closeable).write(JSON_NON_EXECUTABLE_PREFIX);
        }
        closeable = new JsonWriter((Writer)closeable);
        if (this.prettyPrinting) {
            ((JsonWriter)closeable).setIndent("  ");
        }
        ((JsonWriter)closeable).setSerializeNulls(this.serializeNulls);
        return closeable;
    }

    public boolean serializeNulls() {
        return this.serializeNulls;
    }

    public String toJson(JsonElement jsonElement) {
        StringWriter stringWriter = new StringWriter();
        this.toJson(jsonElement, (Appendable)stringWriter);
        return stringWriter.toString();
    }

    public String toJson(Object object) {
        if (object == null) {
            return this.toJson(JsonNull.INSTANCE);
        }
        return this.toJson(object, object.getClass());
    }

    public String toJson(Object object, Type type) {
        StringWriter stringWriter = new StringWriter();
        this.toJson(object, type, stringWriter);
        return stringWriter.toString();
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void toJson(JsonElement jsonElement, JsonWriter jsonWriter) throws JsonIOException {
        Throwable throwable2222222;
        boolean bl = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        boolean bl2 = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        boolean bl3 = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        Streams.write(jsonElement, jsonWriter);
        jsonWriter.setLenient(bl);
        jsonWriter.setHtmlSafe(bl2);
        jsonWriter.setSerializeNulls(bl3);
        return;
        {
            catch (Throwable throwable2222222) {
            }
            catch (IOException iOException) {}
            {
                JsonIOException jsonIOException = new JsonIOException(iOException);
                throw jsonIOException;
            }
        }
        jsonWriter.setLenient(bl);
        jsonWriter.setHtmlSafe(bl2);
        jsonWriter.setSerializeNulls(bl3);
        throw throwable2222222;
    }

    public void toJson(JsonElement jsonElement, Appendable appendable) throws JsonIOException {
        try {
            this.toJson(jsonElement, this.newJsonWriter(Streams.writerForAppendable(appendable)));
            return;
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
    }

    public void toJson(Object object, Appendable appendable) throws JsonIOException {
        if (object != null) {
            this.toJson(object, object.getClass(), appendable);
        } else {
            this.toJson((JsonElement)JsonNull.INSTANCE, appendable);
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void toJson(Object object, Type object2, JsonWriter jsonWriter) throws JsonIOException {
        Throwable throwable2222222;
        object2 = this.getAdapter(TypeToken.get((Type)object2));
        boolean bl = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        boolean bl2 = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        boolean bl3 = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        ((TypeAdapter)object2).write(jsonWriter, object);
        jsonWriter.setLenient(bl);
        jsonWriter.setHtmlSafe(bl2);
        jsonWriter.setSerializeNulls(bl3);
        return;
        {
            catch (Throwable throwable2222222) {
            }
            catch (IOException iOException) {}
            {
                object = new JsonIOException(iOException);
                throw object;
            }
        }
        jsonWriter.setLenient(bl);
        jsonWriter.setHtmlSafe(bl2);
        jsonWriter.setSerializeNulls(bl3);
        throw throwable2222222;
    }

    public void toJson(Object object, Type type, Appendable appendable) throws JsonIOException {
        try {
            this.toJson(object, type, this.newJsonWriter(Streams.writerForAppendable(appendable)));
            return;
        }
        catch (IOException iOException) {
            throw new JsonIOException(iOException);
        }
    }

    public JsonElement toJsonTree(Object object) {
        if (object == null) {
            return JsonNull.INSTANCE;
        }
        return this.toJsonTree(object, object.getClass());
    }

    public JsonElement toJsonTree(Object object, Type type) {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        this.toJson(object, type, jsonTreeWriter);
        return jsonTreeWriter.get();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("{serializeNulls:");
        stringBuilder.append(this.serializeNulls);
        stringBuilder.append("factories:");
        stringBuilder.append(this.factories);
        stringBuilder.append(",instanceCreators:");
        stringBuilder.append(this.constructorConstructor);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    static class FutureTypeAdapter<T>
    extends TypeAdapter<T> {
        private TypeAdapter<T> delegate;

        FutureTypeAdapter() {
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
            TypeAdapter<T> typeAdapter = this.delegate;
            if (typeAdapter != null) {
                return typeAdapter.read(jsonReader);
            }
            throw new IllegalStateException();
        }

        public void setDelegate(TypeAdapter<T> typeAdapter) {
            if (this.delegate == null) {
                this.delegate = typeAdapter;
                return;
            }
            throw new AssertionError();
        }

        @Override
        public void write(JsonWriter jsonWriter, T t) throws IOException {
            TypeAdapter<T> typeAdapter = this.delegate;
            if (typeAdapter != null) {
                typeAdapter.write(jsonWriter, t);
                return;
            }
            throw new IllegalStateException();
        }
    }
}

