/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeTypeAdapter
extends TypeAdapter<Time> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson object, TypeToken<T> typeToken) {
            object = typeToken.getRawType() == Time.class ? new TimeTypeAdapter() : null;
            return object;
        }
    };
    private final DateFormat format = new SimpleDateFormat("hh:mm:ss a");

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public Time read(JsonReader object) throws IOException {
        synchronized (this) {
            if (((JsonReader)object).peek() == JsonToken.NULL) {
                ((JsonReader)object).nextNull();
                return null;
            }
            try {
                return new Time(this.format.parse(((JsonReader)object).nextString()).getTime());
            }
            catch (ParseException parseException) {
                JsonSyntaxException jsonSyntaxException = new JsonSyntaxException(parseException);
                throw jsonSyntaxException;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void write(JsonWriter jsonWriter, Time object) throws IOException {
        synchronized (this) {
            String string2;
            string2 = string2 == null ? null : this.format.format((Date)((Object)string2));
            jsonWriter.value(string2);
            return;
        }
    }
}

