/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.Locale;

public final class DateTypeAdapter
extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson object, TypeToken<T> typeToken) {
            object = typeToken.getRawType() == Date.class ? new DateTypeAdapter() : null;
            return object;
        }
    };
    private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
    private final DateFormat localFormat = DateFormat.getDateTimeInstance(2, 2);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Date deserializeToDate(String string2) {
        synchronized (this) {
            try {
                try {
                    return this.localFormat.parse(string2);
                }
                catch (ParseException parseException) {
                    try {
                        return this.enUsFormat.parse(string2);
                    }
                    catch (ParseException parseException2) {
                        try {
                            Object object = new ParsePosition(0);
                            return ISO8601Utils.parse(string2, (ParsePosition)object);
                        }
                        catch (ParseException parseException3) {
                            JsonSyntaxException jsonSyntaxException = new JsonSyntaxException(string2, parseException3);
                            throw jsonSyntaxException;
                        }
                    }
                }
            }
            catch (Throwable throwable) {}
            throw throwable;
        }
    }

    @Override
    public Date read(JsonReader jsonReader) throws IOException {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        return this.deserializeToDate(jsonReader.nextString());
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        synchronized (this) {
            void var2_2;
            if (var2_2 == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.value(this.enUsFormat.format((Date)var2_2));
            return;
        }
    }
}

