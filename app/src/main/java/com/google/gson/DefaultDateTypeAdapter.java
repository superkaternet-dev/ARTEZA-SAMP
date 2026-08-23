/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

final class DefaultDateTypeAdapter
implements JsonSerializer<java.util.Date>,
JsonDeserializer<java.util.Date> {
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;

    DefaultDateTypeAdapter() {
        this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
    }

    DefaultDateTypeAdapter(int n) {
        this(DateFormat.getDateInstance(n, Locale.US), DateFormat.getDateInstance(n));
    }

    public DefaultDateTypeAdapter(int n, int n2) {
        this(DateFormat.getDateTimeInstance(n, n2, Locale.US), DateFormat.getDateTimeInstance(n, n2));
    }

    DefaultDateTypeAdapter(String string2) {
        this(new SimpleDateFormat(string2, Locale.US), new SimpleDateFormat(string2));
    }

    DefaultDateTypeAdapter(DateFormat dateFormat, DateFormat dateFormat2) {
        this.enUsFormat = dateFormat;
        this.localFormat = dateFormat2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private java.util.Date deserializeToDate(JsonElement jsonElement) {
        DateFormat dateFormat = this.localFormat;
        synchronized (dateFormat) {
            try {
                try {
                    return this.localFormat.parse(jsonElement.getAsString());
                }
                catch (ParseException parseException) {
                    try {
                        return this.enUsFormat.parse(jsonElement.getAsString());
                    }
                    catch (ParseException parseException2) {
                        try {
                            String string2 = jsonElement.getAsString();
                            Object object = new ParsePosition(0);
                            return ISO8601Utils.parse(string2, (ParsePosition)object);
                        }
                        catch (ParseException parseException3) {
                            JsonSyntaxException jsonSyntaxException = new JsonSyntaxException(jsonElement.getAsString(), parseException3);
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
    public java.util.Date deserialize(JsonElement object, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (object instanceof JsonPrimitive) {
            object = this.deserializeToDate((JsonElement)object);
            if (type == java.util.Date.class) {
                return object;
            }
            if (type == Timestamp.class) {
                return new Timestamp(((java.util.Date)object).getTime());
            }
            if (type == Date.class) {
                return new Date(((java.util.Date)object).getTime());
            }
            object = new StringBuilder();
            ((StringBuilder)object).append(this.getClass());
            ((StringBuilder)object).append(" cannot deserialize to ");
            ((StringBuilder)object).append(type);
            throw new IllegalArgumentException(((StringBuilder)object).toString());
        }
        throw new JsonParseException("The date should be a string value");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public JsonElement serialize(java.util.Date object, Type object2, JsonSerializationContext object3) {
        object2 = this.localFormat;
        synchronized (object2) {
            object3 = this.enUsFormat.format((java.util.Date)object);
            return new JsonPrimitive((String)object3);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DefaultDateTypeAdapter.class.getSimpleName());
        stringBuilder.append('(');
        stringBuilder.append(this.localFormat.getClass().getSimpleName());
        stringBuilder.append(')');
        return stringBuilder.toString();
    }
}

