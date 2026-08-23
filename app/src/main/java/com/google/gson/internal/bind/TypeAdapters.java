/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;

public final class TypeAdapters {
    public static final TypeAdapter<AtomicBoolean> ATOMIC_BOOLEAN;
    public static final TypeAdapterFactory ATOMIC_BOOLEAN_FACTORY;
    public static final TypeAdapter<AtomicInteger> ATOMIC_INTEGER;
    public static final TypeAdapter<AtomicIntegerArray> ATOMIC_INTEGER_ARRAY;
    public static final TypeAdapterFactory ATOMIC_INTEGER_ARRAY_FACTORY;
    public static final TypeAdapterFactory ATOMIC_INTEGER_FACTORY;
    public static final TypeAdapter<BigDecimal> BIG_DECIMAL;
    public static final TypeAdapter<BigInteger> BIG_INTEGER;
    public static final TypeAdapter<BitSet> BIT_SET;
    public static final TypeAdapterFactory BIT_SET_FACTORY;
    public static final TypeAdapter<Boolean> BOOLEAN;
    public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING;
    public static final TypeAdapterFactory BOOLEAN_FACTORY;
    public static final TypeAdapter<Number> BYTE;
    public static final TypeAdapterFactory BYTE_FACTORY;
    public static final TypeAdapter<Calendar> CALENDAR;
    public static final TypeAdapterFactory CALENDAR_FACTORY;
    public static final TypeAdapter<Character> CHARACTER;
    public static final TypeAdapterFactory CHARACTER_FACTORY;
    public static final TypeAdapter<Class> CLASS;
    public static final TypeAdapterFactory CLASS_FACTORY;
    public static final TypeAdapter<Currency> CURRENCY;
    public static final TypeAdapterFactory CURRENCY_FACTORY;
    public static final TypeAdapter<Number> DOUBLE;
    public static final TypeAdapterFactory ENUM_FACTORY;
    public static final TypeAdapter<Number> FLOAT;
    public static final TypeAdapter<InetAddress> INET_ADDRESS;
    public static final TypeAdapterFactory INET_ADDRESS_FACTORY;
    public static final TypeAdapter<Number> INTEGER;
    public static final TypeAdapterFactory INTEGER_FACTORY;
    public static final TypeAdapter<JsonElement> JSON_ELEMENT;
    public static final TypeAdapterFactory JSON_ELEMENT_FACTORY;
    public static final TypeAdapter<Locale> LOCALE;
    public static final TypeAdapterFactory LOCALE_FACTORY;
    public static final TypeAdapter<Number> LONG;
    public static final TypeAdapter<Number> NUMBER;
    public static final TypeAdapterFactory NUMBER_FACTORY;
    public static final TypeAdapter<Number> SHORT;
    public static final TypeAdapterFactory SHORT_FACTORY;
    public static final TypeAdapter<String> STRING;
    public static final TypeAdapter<StringBuffer> STRING_BUFFER;
    public static final TypeAdapterFactory STRING_BUFFER_FACTORY;
    public static final TypeAdapter<StringBuilder> STRING_BUILDER;
    public static final TypeAdapterFactory STRING_BUILDER_FACTORY;
    public static final TypeAdapterFactory STRING_FACTORY;
    public static final TypeAdapterFactory TIMESTAMP_FACTORY;
    public static final TypeAdapter<URI> URI;
    public static final TypeAdapterFactory URI_FACTORY;
    public static final TypeAdapter<URL> URL;
    public static final TypeAdapterFactory URL_FACTORY;
    public static final TypeAdapter<UUID> UUID;
    public static final TypeAdapterFactory UUID_FACTORY;

    static {
        TypeAdapter<Object> typeAdapter;
        CLASS = typeAdapter = new TypeAdapter<Class>(){

            @Override
            public Class read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
            }

            @Override
            public void write(JsonWriter object, Class clazz) throws IOException {
                if (clazz == null) {
                    ((JsonWriter)object).nullValue();
                    return;
                }
                object = new StringBuilder();
                ((StringBuilder)object).append("Attempted to serialize java.lang.Class: ");
                ((StringBuilder)object).append(clazz.getName());
                ((StringBuilder)object).append(". Forgot to register a type adapter?");
                throw new UnsupportedOperationException(((StringBuilder)object).toString());
            }
        };
        CLASS_FACTORY = TypeAdapters.newFactory(Class.class, typeAdapter);
        BIT_SET = typeAdapter = new TypeAdapter<BitSet>(){

            @Override
            public BitSet read(JsonReader object) throws IOException {
                if (((JsonReader)object).peek() == JsonToken.NULL) {
                    ((JsonReader)object).nextNull();
                    return null;
                }
                BitSet bitSet = new BitSet();
                ((JsonReader)object).beginArray();
                int n = 0;
                Object object2 = ((JsonReader)object).peek();
                while (object2 != JsonToken.END_ARRAY) {
                    int n2 = 36.$SwitchMap$com$google$gson$stream$JsonToken[object2.ordinal()];
                    boolean bl = false;
                    boolean bl2 = false;
                    switch (n2) {
                        default: {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Invalid bitset value type: ");
                            ((StringBuilder)object).append(object2);
                            throw new JsonSyntaxException(((StringBuilder)object).toString());
                        }
                        case 3: {
                            object2 = ((JsonReader)object).nextString();
                            try {
                                n2 = Integer.parseInt((String)object2);
                                bl = bl2;
                                if (n2 == 0) break;
                                bl = true;
                                break;
                            }
                            catch (NumberFormatException numberFormatException) {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Error: Expecting: bitset number value (1, 0), Found: ");
                                stringBuilder.append((String)object2);
                                throw new JsonSyntaxException(stringBuilder.toString());
                            }
                        }
                        case 2: {
                            bl = ((JsonReader)object).nextBoolean();
                            break;
                        }
                        case 1: {
                            if (((JsonReader)object).nextInt() == 0) break;
                            bl = true;
                        }
                    }
                    if (bl) {
                        bitSet.set(n);
                    }
                    ++n;
                    object2 = ((JsonReader)object).peek();
                }
                ((JsonReader)object).endArray();
                return bitSet;
            }

            @Override
            public void write(JsonWriter jsonWriter, BitSet bitSet) throws IOException {
                if (bitSet == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.beginArray();
                for (int i = 0; i < bitSet.length(); ++i) {
                    jsonWriter.value((long)bitSet.get(i));
                }
                jsonWriter.endArray();
            }
        };
        BIT_SET_FACTORY = TypeAdapters.newFactory(BitSet.class, typeAdapter);
        BOOLEAN = typeAdapter = new TypeAdapter<Boolean>(){

            @Override
            public Boolean read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                if (jsonReader.peek() == JsonToken.STRING) {
                    return Boolean.parseBoolean(jsonReader.nextString());
                }
                return jsonReader.nextBoolean();
            }

            @Override
            public void write(JsonWriter jsonWriter, Boolean bl) throws IOException {
                jsonWriter.value(bl);
            }
        };
        BOOLEAN_AS_STRING = new TypeAdapter<Boolean>(){

            @Override
            public Boolean read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return Boolean.valueOf(jsonReader.nextString());
            }

            @Override
            public void write(JsonWriter jsonWriter, Boolean object) throws IOException {
                object = object == null ? "null" : ((Boolean)object).toString();
                jsonWriter.value((String)object);
            }
        };
        BOOLEAN_FACTORY = TypeAdapters.newFactory(Boolean.TYPE, Boolean.class, typeAdapter);
        BYTE = typeAdapter = new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) throws IOException {
                byte by;
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    by = (byte)jsonReader.nextInt();
                }
                catch (NumberFormatException numberFormatException) {
                    throw new JsonSyntaxException(numberFormatException);
                }
                return by;
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        BYTE_FACTORY = TypeAdapters.newFactory(Byte.TYPE, Byte.class, typeAdapter);
        SHORT = typeAdapter = new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) throws IOException {
                short s;
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    s = (short)jsonReader.nextInt();
                }
                catch (NumberFormatException numberFormatException) {
                    throw new JsonSyntaxException(numberFormatException);
                }
                return s;
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        SHORT_FACTORY = TypeAdapters.newFactory(Short.TYPE, Short.class, typeAdapter);
        INTEGER = typeAdapter = new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) throws IOException {
                int n;
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    n = jsonReader.nextInt();
                }
                catch (NumberFormatException numberFormatException) {
                    throw new JsonSyntaxException(numberFormatException);
                }
                return n;
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        INTEGER_FACTORY = TypeAdapters.newFactory(Integer.TYPE, Integer.class, typeAdapter);
        ATOMIC_INTEGER = typeAdapter = new TypeAdapter<AtomicInteger>(){

            @Override
            public AtomicInteger read(JsonReader object) throws IOException {
                try {
                    object = new AtomicInteger(((JsonReader)object).nextInt());
                    return object;
                }
                catch (NumberFormatException numberFormatException) {
                    throw new JsonSyntaxException(numberFormatException);
                }
            }

            @Override
            public void write(JsonWriter jsonWriter, AtomicInteger atomicInteger) throws IOException {
                jsonWriter.value(atomicInteger.get());
            }
        }.nullSafe();
        ATOMIC_INTEGER_FACTORY = TypeAdapters.newFactory(AtomicInteger.class, typeAdapter);
        ATOMIC_BOOLEAN = typeAdapter = new TypeAdapter<AtomicBoolean>(){

            @Override
            public AtomicBoolean read(JsonReader jsonReader) throws IOException {
                return new AtomicBoolean(jsonReader.nextBoolean());
            }

            @Override
            public void write(JsonWriter jsonWriter, AtomicBoolean atomicBoolean) throws IOException {
                jsonWriter.value(atomicBoolean.get());
            }
        }.nullSafe();
        ATOMIC_BOOLEAN_FACTORY = TypeAdapters.newFactory(AtomicBoolean.class, typeAdapter);
        ATOMIC_INTEGER_ARRAY = typeAdapter = new TypeAdapter<AtomicIntegerArray>(){

            @Override
            public AtomicIntegerArray read(JsonReader object) throws IOException {
                ArrayList<Integer> arrayList = new ArrayList<Integer>();
                ((JsonReader)object).beginArray();
                while (((JsonReader)object).hasNext()) {
                    try {
                        arrayList.add(((JsonReader)object).nextInt());
                    }
                    catch (NumberFormatException numberFormatException) {
                        throw new JsonSyntaxException(numberFormatException);
                    }
                }
                ((JsonReader)object).endArray();
                int n = arrayList.size();
                object = new AtomicIntegerArray(n);
                for (int i = 0; i < n; ++i) {
                    ((AtomicIntegerArray)object).set(i, (Integer)arrayList.get(i));
                }
                return object;
            }

            @Override
            public void write(JsonWriter jsonWriter, AtomicIntegerArray atomicIntegerArray) throws IOException {
                jsonWriter.beginArray();
                int n = atomicIntegerArray.length();
                for (int i = 0; i < n; ++i) {
                    jsonWriter.value(atomicIntegerArray.get(i));
                }
                jsonWriter.endArray();
            }
        }.nullSafe();
        ATOMIC_INTEGER_ARRAY_FACTORY = TypeAdapters.newFactory(AtomicIntegerArray.class, typeAdapter);
        LONG = new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) throws IOException {
                long l;
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                try {
                    l = jsonReader.nextLong();
                }
                catch (NumberFormatException numberFormatException) {
                    throw new JsonSyntaxException(numberFormatException);
                }
                return l;
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        FLOAT = new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return Float.valueOf((float)jsonReader.nextDouble());
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        DOUBLE = new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextDouble();
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        NUMBER = typeAdapter = new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader object) throws IOException {
                JsonToken jsonToken = ((JsonReader)object).peek();
                switch (36.$SwitchMap$com$google$gson$stream$JsonToken[jsonToken.ordinal()]) {
                    default: {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Expecting number, got: ");
                        ((StringBuilder)object).append((Object)jsonToken);
                        throw new JsonSyntaxException(((StringBuilder)object).toString());
                    }
                    case 4: {
                        ((JsonReader)object).nextNull();
                        return null;
                    }
                    case 1: 
                }
                return new LazilyParsedNumber(((JsonReader)object).nextString());
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) throws IOException {
                jsonWriter.value(number);
            }
        };
        NUMBER_FACTORY = TypeAdapters.newFactory(Number.class, typeAdapter);
        CHARACTER = typeAdapter = new TypeAdapter<Character>(){

            @Override
            public Character read(JsonReader object) throws IOException {
                if (((JsonReader)object).peek() == JsonToken.NULL) {
                    ((JsonReader)object).nextNull();
                    return null;
                }
                if (((String)(object = ((JsonReader)object).nextString())).length() == 1) {
                    return Character.valueOf(((String)object).charAt(0));
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Expecting character, got: ");
                stringBuilder.append((String)object);
                throw new JsonSyntaxException(stringBuilder.toString());
            }

            @Override
            public void write(JsonWriter jsonWriter, Character object) throws IOException {
                object = object == null ? null : String.valueOf(object);
                jsonWriter.value((String)object);
            }
        };
        CHARACTER_FACTORY = TypeAdapters.newFactory(Character.TYPE, Character.class, typeAdapter);
        STRING = typeAdapter = new TypeAdapter<String>(){

            @Override
            public String read(JsonReader jsonReader) throws IOException {
                JsonToken jsonToken = jsonReader.peek();
                if (jsonToken == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                if (jsonToken == JsonToken.BOOLEAN) {
                    return Boolean.toString(jsonReader.nextBoolean());
                }
                return jsonReader.nextString();
            }

            @Override
            public void write(JsonWriter jsonWriter, String string2) throws IOException {
                jsonWriter.value(string2);
            }
        };
        BIG_DECIMAL = new TypeAdapter<BigDecimal>(){

            @Override
            public BigDecimal read(JsonReader object) throws IOException {
                if (((JsonReader)object).peek() == JsonToken.NULL) {
                    ((JsonReader)object).nextNull();
                    return null;
                }
                try {
                    object = new BigDecimal(((JsonReader)object).nextString());
                    return object;
                }
                catch (NumberFormatException numberFormatException) {
                    throw new JsonSyntaxException(numberFormatException);
                }
            }

            @Override
            public void write(JsonWriter jsonWriter, BigDecimal bigDecimal) throws IOException {
                jsonWriter.value(bigDecimal);
            }
        };
        BIG_INTEGER = new TypeAdapter<BigInteger>(){

            @Override
            public BigInteger read(JsonReader object) throws IOException {
                if (((JsonReader)object).peek() == JsonToken.NULL) {
                    ((JsonReader)object).nextNull();
                    return null;
                }
                try {
                    object = new BigInteger(((JsonReader)object).nextString());
                    return object;
                }
                catch (NumberFormatException numberFormatException) {
                    throw new JsonSyntaxException(numberFormatException);
                }
            }

            @Override
            public void write(JsonWriter jsonWriter, BigInteger bigInteger) throws IOException {
                jsonWriter.value(bigInteger);
            }
        };
        STRING_FACTORY = TypeAdapters.newFactory(String.class, typeAdapter);
        STRING_BUILDER = typeAdapter = new TypeAdapter<StringBuilder>(){

            @Override
            public StringBuilder read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return new StringBuilder(jsonReader.nextString());
            }

            @Override
            public void write(JsonWriter jsonWriter, StringBuilder charSequence) throws IOException {
                charSequence = charSequence == null ? null : charSequence.toString();
                jsonWriter.value((String)charSequence);
            }
        };
        STRING_BUILDER_FACTORY = TypeAdapters.newFactory(StringBuilder.class, typeAdapter);
        STRING_BUFFER = typeAdapter = new TypeAdapter<StringBuffer>(){

            @Override
            public StringBuffer read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return new StringBuffer(jsonReader.nextString());
            }

            @Override
            public void write(JsonWriter jsonWriter, StringBuffer charSequence) throws IOException {
                charSequence = charSequence == null ? null : charSequence.toString();
                jsonWriter.value((String)charSequence);
            }
        };
        STRING_BUFFER_FACTORY = TypeAdapters.newFactory(StringBuffer.class, typeAdapter);
        URL = typeAdapter = new TypeAdapter<URL>(){

            @Override
            public URL read(JsonReader object) throws IOException {
                JsonToken jsonToken = ((JsonReader)object).peek();
                JsonToken jsonToken2 = JsonToken.NULL;
                Object var2_4 = null;
                if (jsonToken == jsonToken2) {
                    ((JsonReader)object).nextNull();
                    return null;
                }
                object = "null".equals(object = ((JsonReader)object).nextString()) ? var2_4 : new URL((String)object);
                return object;
            }

            @Override
            public void write(JsonWriter jsonWriter, URL object) throws IOException {
                object = object == null ? null : ((URL)object).toExternalForm();
                jsonWriter.value((String)object);
            }
        };
        URL_FACTORY = TypeAdapters.newFactory(URL.class, typeAdapter);
        URI = typeAdapter = new TypeAdapter<URI>(){

            @Override
            public URI read(JsonReader object) throws IOException {
                block5: {
                    block4: {
                        JsonToken jsonToken = ((JsonReader)object).peek();
                        JsonToken jsonToken2 = JsonToken.NULL;
                        Object var2_6 = null;
                        if (jsonToken == jsonToken2) {
                            ((JsonReader)object).nextNull();
                            return null;
                        }
                        try {
                            object = ((JsonReader)object).nextString();
                            if (!"null".equals(object)) break block4;
                            object = var2_6;
                            break block5;
                        }
                        catch (URISyntaxException uRISyntaxException) {
                            JsonIOException jsonIOException = new JsonIOException(uRISyntaxException);
                            throw jsonIOException;
                        }
                    }
                    object = new URI((String)object);
                }
                return object;
            }

            @Override
            public void write(JsonWriter jsonWriter, URI object) throws IOException {
                object = object == null ? null : ((URI)object).toASCIIString();
                jsonWriter.value((String)object);
            }
        };
        URI_FACTORY = TypeAdapters.newFactory(URI.class, typeAdapter);
        INET_ADDRESS = typeAdapter = new TypeAdapter<InetAddress>(){

            @Override
            public InetAddress read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return InetAddress.getByName(jsonReader.nextString());
            }

            @Override
            public void write(JsonWriter jsonWriter, InetAddress object) throws IOException {
                object = object == null ? null : ((InetAddress)object).getHostAddress();
                jsonWriter.value((String)object);
            }
        };
        INET_ADDRESS_FACTORY = TypeAdapters.newTypeHierarchyFactory(InetAddress.class, typeAdapter);
        UUID = typeAdapter = new TypeAdapter<UUID>(){

            @Override
            public UUID read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return java.util.UUID.fromString(jsonReader.nextString());
            }

            @Override
            public void write(JsonWriter jsonWriter, UUID object) throws IOException {
                object = object == null ? null : ((UUID)object).toString();
                jsonWriter.value((String)object);
            }
        };
        UUID_FACTORY = TypeAdapters.newFactory(UUID.class, typeAdapter);
        CURRENCY = typeAdapter = new TypeAdapter<Currency>(){

            @Override
            public Currency read(JsonReader jsonReader) throws IOException {
                return Currency.getInstance(jsonReader.nextString());
            }

            @Override
            public void write(JsonWriter jsonWriter, Currency currency) throws IOException {
                jsonWriter.value(currency.getCurrencyCode());
            }
        }.nullSafe();
        CURRENCY_FACTORY = TypeAdapters.newFactory(Currency.class, typeAdapter);
        TIMESTAMP_FACTORY = new TypeAdapterFactory(){

            @Override
            public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
                if (typeToken.getRawType() != Timestamp.class) {
                    return null;
                }
                return new TypeAdapter<Timestamp>(this, gson.getAdapter(Date.class)){
                    final 26 this$0;
                    final TypeAdapter val$dateTypeAdapter;
                    {
                        this.this$0 = var1_1;
                        this.val$dateTypeAdapter = typeAdapter;
                    }

                    @Override
                    public Timestamp read(JsonReader object) throws IOException {
                        object = (object = (Date)this.val$dateTypeAdapter.read((JsonReader)object)) != null ? new Timestamp(((Date)object).getTime()) : null;
                        return object;
                    }

                    @Override
                    public void write(JsonWriter jsonWriter, Timestamp timestamp) throws IOException {
                        this.val$dateTypeAdapter.write(jsonWriter, timestamp);
                    }
                };
            }
        };
        CALENDAR = typeAdapter = new TypeAdapter<Calendar>(){
            private static final String DAY_OF_MONTH = "dayOfMonth";
            private static final String HOUR_OF_DAY = "hourOfDay";
            private static final String MINUTE = "minute";
            private static final String MONTH = "month";
            private static final String SECOND = "second";
            private static final String YEAR = "year";

            @Override
            public Calendar read(JsonReader jsonReader) throws IOException {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                jsonReader.beginObject();
                int n = 0;
                int n2 = 0;
                int n3 = 0;
                int n4 = 0;
                int n5 = 0;
                int n6 = 0;
                while (jsonReader.peek() != JsonToken.END_OBJECT) {
                    int n7;
                    int n8;
                    int n9;
                    int n10;
                    int n11;
                    String string2 = jsonReader.nextName();
                    int n12 = jsonReader.nextInt();
                    if (YEAR.equals(string2)) {
                        n11 = n12;
                        n10 = n2;
                        n9 = n3;
                        n8 = n4;
                        n7 = n5;
                    } else if (MONTH.equals(string2)) {
                        n11 = n;
                        n10 = n12;
                        n9 = n3;
                        n8 = n4;
                        n7 = n5;
                    } else if (DAY_OF_MONTH.equals(string2)) {
                        n11 = n;
                        n10 = n2;
                        n9 = n12;
                        n8 = n4;
                        n7 = n5;
                    } else if (HOUR_OF_DAY.equals(string2)) {
                        n11 = n;
                        n10 = n2;
                        n9 = n3;
                        n8 = n12;
                        n7 = n5;
                    } else if (MINUTE.equals(string2)) {
                        n11 = n;
                        n10 = n2;
                        n9 = n3;
                        n8 = n4;
                        n7 = n12;
                    } else {
                        n11 = n;
                        n10 = n2;
                        n9 = n3;
                        n8 = n4;
                        n7 = n5;
                        if (SECOND.equals(string2)) {
                            n6 = n12;
                            n7 = n5;
                            n8 = n4;
                            n9 = n3;
                            n10 = n2;
                            n11 = n;
                        }
                    }
                    n = n11;
                    n2 = n10;
                    n3 = n9;
                    n4 = n8;
                    n5 = n7;
                }
                jsonReader.endObject();
                return new GregorianCalendar(n, n2, n3, n4, n5, n6);
            }

            @Override
            public void write(JsonWriter jsonWriter, Calendar calendar) throws IOException {
                if (calendar == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.beginObject();
                jsonWriter.name(YEAR);
                jsonWriter.value(calendar.get(1));
                jsonWriter.name(MONTH);
                jsonWriter.value(calendar.get(2));
                jsonWriter.name(DAY_OF_MONTH);
                jsonWriter.value(calendar.get(5));
                jsonWriter.name(HOUR_OF_DAY);
                jsonWriter.value(calendar.get(11));
                jsonWriter.name(MINUTE);
                jsonWriter.value(calendar.get(12));
                jsonWriter.name(SECOND);
                jsonWriter.value(calendar.get(13));
                jsonWriter.endObject();
            }
        };
        CALENDAR_FACTORY = TypeAdapters.newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, typeAdapter);
        LOCALE = typeAdapter = new TypeAdapter<Locale>(){

            @Override
            public Locale read(JsonReader object) throws IOException {
                if (((JsonReader)object).peek() == JsonToken.NULL) {
                    ((JsonReader)object).nextNull();
                    return null;
                }
                StringTokenizer stringTokenizer = new StringTokenizer(((JsonReader)object).nextString(), "_");
                object = null;
                String string2 = null;
                String string3 = null;
                if (stringTokenizer.hasMoreElements()) {
                    object = stringTokenizer.nextToken();
                }
                if (stringTokenizer.hasMoreElements()) {
                    string2 = stringTokenizer.nextToken();
                }
                if (stringTokenizer.hasMoreElements()) {
                    string3 = stringTokenizer.nextToken();
                }
                if (string2 == null && string3 == null) {
                    return new Locale((String)object);
                }
                if (string3 == null) {
                    return new Locale((String)object, string2);
                }
                return new Locale((String)object, string2, string3);
            }

            @Override
            public void write(JsonWriter jsonWriter, Locale object) throws IOException {
                object = object == null ? null : ((Locale)object).toString();
                jsonWriter.value((String)object);
            }
        };
        LOCALE_FACTORY = TypeAdapters.newFactory(Locale.class, typeAdapter);
        JSON_ELEMENT = typeAdapter = new TypeAdapter<JsonElement>(){

            @Override
            public JsonElement read(JsonReader jsonReader) throws IOException {
                switch (36.$SwitchMap$com$google$gson$stream$JsonToken[jsonReader.peek().ordinal()]) {
                    default: {
                        throw new IllegalArgumentException();
                    }
                    case 6: {
                        JsonObject jsonObject = new JsonObject();
                        jsonReader.beginObject();
                        while (jsonReader.hasNext()) {
                            jsonObject.add(jsonReader.nextName(), this.read(jsonReader));
                        }
                        jsonReader.endObject();
                        return jsonObject;
                    }
                    case 5: {
                        JsonArray jsonArray = new JsonArray();
                        jsonReader.beginArray();
                        while (jsonReader.hasNext()) {
                            jsonArray.add(this.read(jsonReader));
                        }
                        jsonReader.endArray();
                        return jsonArray;
                    }
                    case 4: {
                        jsonReader.nextNull();
                        return JsonNull.INSTANCE;
                    }
                    case 3: {
                        return new JsonPrimitive(jsonReader.nextString());
                    }
                    case 2: {
                        return new JsonPrimitive(jsonReader.nextBoolean());
                    }
                    case 1: 
                }
                return new JsonPrimitive(new LazilyParsedNumber(jsonReader.nextString()));
            }

            /*
             * Enabled aggressive block sorting
             */
            @Override
            public void write(JsonWriter object, JsonElement iterator2) throws IOException {
                if (iterator2 != null && !((JsonElement)((Object)iterator2)).isJsonNull()) {
                    if (((JsonElement)((Object)iterator2)).isJsonPrimitive()) {
                        if (((JsonPrimitive)((Object)(iterator2 = ((JsonElement)((Object)iterator2)).getAsJsonPrimitive()))).isNumber()) {
                            ((JsonWriter)object).value(((JsonPrimitive)((Object)iterator2)).getAsNumber());
                            return;
                        }
                        if (((JsonPrimitive)((Object)iterator2)).isBoolean()) {
                            ((JsonWriter)object).value(((JsonPrimitive)((Object)iterator2)).getAsBoolean());
                            return;
                        }
                        ((JsonWriter)object).value(((JsonPrimitive)((Object)iterator2)).getAsString());
                        return;
                    }
                    if (((JsonElement)((Object)iterator2)).isJsonArray()) {
                        ((JsonWriter)object).beginArray();
                        iterator2 = ((JsonElement)((Object)iterator2)).getAsJsonArray().iterator();
                        while (true) {
                            if (!iterator2.hasNext()) {
                                ((JsonWriter)object).endArray();
                                return;
                            }
                            this.write((JsonWriter)object, (JsonElement)iterator2.next());
                        }
                    }
                    if (!((JsonElement)((Object)iterator2)).isJsonObject()) {
                        object = new StringBuilder();
                        ((StringBuilder)object).append("Couldn't write ");
                        ((StringBuilder)object).append(iterator2.getClass());
                        throw new IllegalArgumentException(((StringBuilder)object).toString());
                    }
                    ((JsonWriter)object).beginObject();
                    iterator2 = ((JsonElement)((Object)iterator2)).getAsJsonObject().entrySet().iterator();
                    while (true) {
                        if (!iterator2.hasNext()) {
                            ((JsonWriter)object).endObject();
                            return;
                        }
                        Map.Entry entry = (Map.Entry)iterator2.next();
                        ((JsonWriter)object).name((String)entry.getKey());
                        this.write((JsonWriter)object, (JsonElement)entry.getValue());
                    }
                }
                ((JsonWriter)object).nullValue();
            }
        };
        JSON_ELEMENT_FACTORY = TypeAdapters.newTypeHierarchyFactory(JsonElement.class, typeAdapter);
        ENUM_FACTORY = new TypeAdapterFactory(){

            @Override
            public <T> TypeAdapter<T> create(Gson clazz, TypeToken<T> object) {
                if (Enum.class.isAssignableFrom((Class<?>)(object = ((TypeToken)object).getRawType())) && object != Enum.class) {
                    clazz = object;
                    if (!((Class)object).isEnum()) {
                        clazz = ((Class)object).getSuperclass();
                    }
                    return new EnumTypeAdapter(clazz);
                }
                return null;
            }
        };
    }

    private TypeAdapters() {
        throw new UnsupportedOperationException();
    }

    public static <TT> TypeAdapterFactory newFactory(TypeToken<TT> typeToken, TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(typeToken, typeAdapter){
            final TypeToken val$type;
            final TypeAdapter val$typeAdapter;
            {
                this.val$type = typeToken;
                this.val$typeAdapter = typeAdapter;
            }

            @Override
            public <T> TypeAdapter<T> create(Gson typeAdapter, TypeToken<T> typeToken) {
                typeAdapter = typeToken.equals(this.val$type) ? this.val$typeAdapter : null;
                return typeAdapter;
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(Class<TT> clazz, TypeAdapter<TT> typeAdapter) {
        return new TypeAdapterFactory(clazz, typeAdapter){
            final Class val$type;
            final TypeAdapter val$typeAdapter;
            {
                this.val$type = clazz;
                this.val$typeAdapter = typeAdapter;
            }

            @Override
            public <T> TypeAdapter<T> create(Gson typeAdapter, TypeToken<T> typeToken) {
                typeAdapter = typeToken.getRawType() == this.val$type ? this.val$typeAdapter : null;
                return typeAdapter;
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Factory[type=");
                stringBuilder.append(this.val$type.getName());
                stringBuilder.append(",adapter=");
                stringBuilder.append(this.val$typeAdapter);
                stringBuilder.append("]");
                return stringBuilder.toString();
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactory(Class<TT> clazz, Class<TT> clazz2, TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(clazz, clazz2, typeAdapter){
            final Class val$boxed;
            final TypeAdapter val$typeAdapter;
            final Class val$unboxed;
            {
                this.val$unboxed = clazz;
                this.val$boxed = clazz2;
                this.val$typeAdapter = typeAdapter;
            }

            @Override
            public <T> TypeAdapter<T> create(Gson clazz, TypeToken<T> typeToken) {
                clazz = typeToken.getRawType();
                clazz = clazz != this.val$unboxed && clazz != this.val$boxed ? null : this.val$typeAdapter;
                return clazz;
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Factory[type=");
                stringBuilder.append(this.val$boxed.getName());
                stringBuilder.append("+");
                stringBuilder.append(this.val$unboxed.getName());
                stringBuilder.append(",adapter=");
                stringBuilder.append(this.val$typeAdapter);
                stringBuilder.append("]");
                return stringBuilder.toString();
            }
        };
    }

    public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(Class<TT> clazz, Class<? extends TT> clazz2, TypeAdapter<? super TT> typeAdapter) {
        return new TypeAdapterFactory(clazz, clazz2, typeAdapter){
            final Class val$base;
            final Class val$sub;
            final TypeAdapter val$typeAdapter;
            {
                this.val$base = clazz;
                this.val$sub = clazz2;
                this.val$typeAdapter = typeAdapter;
            }

            @Override
            public <T> TypeAdapter<T> create(Gson clazz, TypeToken<T> typeToken) {
                clazz = typeToken.getRawType();
                clazz = clazz != this.val$base && clazz != this.val$sub ? null : this.val$typeAdapter;
                return clazz;
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Factory[type=");
                stringBuilder.append(this.val$base.getName());
                stringBuilder.append("+");
                stringBuilder.append(this.val$sub.getName());
                stringBuilder.append(",adapter=");
                stringBuilder.append(this.val$typeAdapter);
                stringBuilder.append("]");
                return stringBuilder.toString();
            }
        };
    }

    public static <T1> TypeAdapterFactory newTypeHierarchyFactory(Class<T1> clazz, TypeAdapter<T1> typeAdapter) {
        return new TypeAdapterFactory(clazz, typeAdapter){
            final Class val$clazz;
            final TypeAdapter val$typeAdapter;
            {
                this.val$clazz = clazz;
                this.val$typeAdapter = typeAdapter;
            }

            public <T2> TypeAdapter<T2> create(Gson clazz, TypeToken<T2> typeToken) {
                clazz = typeToken.getRawType();
                if (!this.val$clazz.isAssignableFrom(clazz)) {
                    return null;
                }
                return new TypeAdapter<T1>(this, clazz){
                    final 35 this$0;
                    final Class val$requestedType;
                    {
                        this.this$0 = var1_1;
                        this.val$requestedType = clazz;
                    }

                    @Override
                    public T1 read(JsonReader object) throws IOException {
                        Object t = this.this$0.val$typeAdapter.read((JsonReader)object);
                        if (t != null && !this.val$requestedType.isInstance(t)) {
                            object = new StringBuilder();
                            ((StringBuilder)object).append("Expected a ");
                            ((StringBuilder)object).append(this.val$requestedType.getName());
                            ((StringBuilder)object).append(" but was ");
                            ((StringBuilder)object).append(t.getClass().getName());
                            throw new JsonSyntaxException(((StringBuilder)object).toString());
                        }
                        return t;
                    }

                    @Override
                    public void write(JsonWriter jsonWriter, T1 T1) throws IOException {
                        this.this$0.val$typeAdapter.write(jsonWriter, T1);
                    }
                };
            }

            public String toString() {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Factory[typeHierarchy=");
                stringBuilder.append(this.val$clazz.getName());
                stringBuilder.append(",adapter=");
                stringBuilder.append(this.val$typeAdapter);
                stringBuilder.append("]");
                return stringBuilder.toString();
            }
        };
    }

    private static final class EnumTypeAdapter<T extends Enum<T>>
    extends TypeAdapter<T> {
        private final Map<T, String> constantToName;
        private final Map<String, T> nameToConstant = new HashMap<String, T>();

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public EnumTypeAdapter(Class<T> clazz) {
            int n;
            int n2;
            Enum[] enumArray;
            this.constantToName = new HashMap<T, String>();
            try {
                enumArray = (Enum[])clazz.getEnumConstants();
                n2 = enumArray.length;
                n = 0;
            }
            catch (NoSuchFieldException noSuchFieldException) {
                AssertionError assertionError = new AssertionError((Object)noSuchFieldException);
                throw assertionError;
            }
            while (n < n2) {
                String string2;
                Enum enum_;
                block7: {
                    enum_ = enumArray[n];
                    string2 = enum_.name();
                    String[] stringArray = clazz.getField(string2).getAnnotation(SerializedName.class);
                    if (stringArray == null) break block7;
                    String string3 = stringArray.value();
                    stringArray = stringArray.alternate();
                    int n3 = stringArray.length;
                    int n4 = 0;
                    while (true) {
                        string2 = string3;
                        if (n4 >= n3) break;
                        string2 = stringArray[n4];
                        this.nameToConstant.put(string2, enum_);
                        ++n4;
                        continue;
                        break;
                    }
                }
                this.nameToConstant.put(string2, enum_);
                this.constantToName.put(enum_, string2);
                ++n;
            }
            return;
        }

        @Override
        public T read(JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            return (T)((Enum)this.nameToConstant.get(jsonReader.nextString()));
        }

        @Override
        public void write(JsonWriter jsonWriter, T object) throws IOException {
            object = object == null ? null : this.constantToName.get(object);
            jsonWriter.value((String)object);
        }
    }
}

