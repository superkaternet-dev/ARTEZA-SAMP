/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map;

public final class JsonTreeReader
extends JsonReader {
    private static final Object SENTINEL_CLOSED;
    private static final Reader UNREADABLE_READER;
    private int[] pathIndices;
    private String[] pathNames;
    private Object[] stack = new Object[32];
    private int stackSize = 0;

    static {
        UNREADABLE_READER = new Reader(){

            @Override
            public void close() throws IOException {
                throw new AssertionError();
            }

            @Override
            public int read(char[] cArray, int n, int n2) throws IOException {
                throw new AssertionError();
            }
        };
        SENTINEL_CLOSED = new Object();
    }

    public JsonTreeReader(JsonElement jsonElement) {
        super(UNREADABLE_READER);
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        this.push(jsonElement);
    }

    private void expect(JsonToken jsonToken) throws IOException {
        if (this.peek() == jsonToken) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Expected ");
        stringBuilder.append((Object)jsonToken);
        stringBuilder.append(" but was ");
        stringBuilder.append((Object)this.peek());
        stringBuilder.append(this.locationString());
        throw new IllegalStateException(stringBuilder.toString());
    }

    private String locationString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" at path ");
        stringBuilder.append(this.getPath());
        return stringBuilder.toString();
    }

    private Object peekStack() {
        return this.stack[this.stackSize - 1];
    }

    private Object popStack() {
        int n;
        Object[] objectArray = this.stack;
        this.stackSize = n = this.stackSize - 1;
        Object object = objectArray[n];
        objectArray[n] = null;
        return object;
    }

    private void push(Object object) {
        Object[] objectArray;
        int n = this.stackSize;
        Object[] objectArray2 = this.stack;
        if (n == objectArray2.length) {
            objectArray = new Object[n * 2];
            int[] nArray = new int[n * 2];
            String[] stringArray = new String[n * 2];
            System.arraycopy(objectArray2, 0, objectArray, 0, n);
            System.arraycopy(this.pathIndices, 0, nArray, 0, this.stackSize);
            System.arraycopy(this.pathNames, 0, stringArray, 0, this.stackSize);
            this.stack = objectArray;
            this.pathIndices = nArray;
            this.pathNames = stringArray;
        }
        objectArray = this.stack;
        n = this.stackSize;
        this.stackSize = n + 1;
        objectArray[n] = object;
    }

    @Override
    public void beginArray() throws IOException {
        this.expect(JsonToken.BEGIN_ARRAY);
        this.push(((JsonArray)this.peekStack()).iterator());
        this.pathIndices[this.stackSize - 1] = 0;
    }

    @Override
    public void beginObject() throws IOException {
        this.expect(JsonToken.BEGIN_OBJECT);
        this.push(((JsonObject)this.peekStack()).entrySet().iterator());
    }

    @Override
    public void close() throws IOException {
        this.stack = new Object[]{SENTINEL_CLOSED};
        this.stackSize = 1;
    }

    @Override
    public void endArray() throws IOException {
        this.expect(JsonToken.END_ARRAY);
        this.popStack();
        this.popStack();
        int n = this.stackSize;
        if (n > 0) {
            int[] nArray = this.pathIndices;
            nArray[--n] = nArray[n] + 1;
        }
    }

    @Override
    public void endObject() throws IOException {
        this.expect(JsonToken.END_OBJECT);
        this.popStack();
        this.popStack();
        int n = this.stackSize;
        if (n > 0) {
            int[] nArray = this.pathIndices;
            nArray[--n] = nArray[n] + 1;
        }
    }

    @Override
    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder().append('$');
        int n = 0;
        while (n < this.stackSize) {
            int n2;
            Object[] objectArray = this.stack;
            if (objectArray[n] instanceof JsonArray) {
                n2 = ++n;
                if (objectArray[n] instanceof Iterator) {
                    stringBuilder.append('[');
                    stringBuilder.append(this.pathIndices[n]);
                    stringBuilder.append(']');
                    n2 = n;
                }
            } else {
                n2 = n;
                if (objectArray[n] instanceof JsonObject) {
                    n2 = ++n;
                    if (objectArray[n] instanceof Iterator) {
                        stringBuilder.append('.');
                        objectArray = this.pathNames;
                        n2 = n;
                        if (objectArray[n] != null) {
                            stringBuilder.append((String)objectArray[n]);
                            n2 = n;
                        }
                    }
                }
            }
            n = n2 + 1;
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean hasNext() throws IOException {
        JsonToken jsonToken = this.peek();
        boolean bl = jsonToken != JsonToken.END_OBJECT && jsonToken != JsonToken.END_ARRAY;
        return bl;
    }

    @Override
    public boolean nextBoolean() throws IOException {
        this.expect(JsonToken.BOOLEAN);
        boolean bl = ((JsonPrimitive)this.popStack()).getAsBoolean();
        int n = this.stackSize;
        if (n > 0) {
            int[] nArray = this.pathIndices;
            nArray[--n] = nArray[n] + 1;
        }
        return bl;
    }

    @Override
    public double nextDouble() throws IOException {
        Object object = this.peek();
        if (object != JsonToken.NUMBER && object != JsonToken.STRING) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected ");
            stringBuilder.append((Object)JsonToken.NUMBER);
            stringBuilder.append(" but was ");
            stringBuilder.append(object);
            stringBuilder.append(this.locationString());
            throw new IllegalStateException(stringBuilder.toString());
        }
        double d = ((JsonPrimitive)this.peekStack()).getAsDouble();
        if (!this.isLenient() && (Double.isNaN(d) || Double.isInfinite(d))) {
            object = new StringBuilder();
            ((StringBuilder)object).append("JSON forbids NaN and infinities: ");
            ((StringBuilder)object).append(d);
            throw new NumberFormatException(((StringBuilder)object).toString());
        }
        this.popStack();
        int n = this.stackSize;
        if (n > 0) {
            object = this.pathIndices;
            object[--n] = object[n] + true;
        }
        return d;
    }

    @Override
    public int nextInt() throws IOException {
        Object object = this.peek();
        if (object != JsonToken.NUMBER && object != JsonToken.STRING) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected ");
            stringBuilder.append((Object)JsonToken.NUMBER);
            stringBuilder.append(" but was ");
            stringBuilder.append(object);
            stringBuilder.append(this.locationString());
            throw new IllegalStateException(stringBuilder.toString());
        }
        int n = ((JsonPrimitive)this.peekStack()).getAsInt();
        this.popStack();
        int n2 = this.stackSize;
        if (n2 > 0) {
            object = this.pathIndices;
            object[--n2] = object[n2] + true;
        }
        return n;
    }

    @Override
    public long nextLong() throws IOException {
        Object object = this.peek();
        if (object != JsonToken.NUMBER && object != JsonToken.STRING) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected ");
            stringBuilder.append((Object)JsonToken.NUMBER);
            stringBuilder.append(" but was ");
            stringBuilder.append(object);
            stringBuilder.append(this.locationString());
            throw new IllegalStateException(stringBuilder.toString());
        }
        long l = ((JsonPrimitive)this.peekStack()).getAsLong();
        this.popStack();
        int n = this.stackSize;
        if (n > 0) {
            object = this.pathIndices;
            object[--n] = object[n] + true;
        }
        return l;
    }

    @Override
    public String nextName() throws IOException {
        String string2;
        this.expect(JsonToken.NAME);
        Map.Entry entry = (Map.Entry)((Iterator)this.peekStack()).next();
        this.pathNames[this.stackSize - 1] = string2 = (String)entry.getKey();
        this.push(entry.getValue());
        return string2;
    }

    @Override
    public void nextNull() throws IOException {
        this.expect(JsonToken.NULL);
        this.popStack();
        int n = this.stackSize;
        if (n > 0) {
            int[] nArray = this.pathIndices;
            nArray[--n] = nArray[n] + 1;
        }
    }

    @Override
    public String nextString() throws IOException {
        Object object = this.peek();
        if (object != JsonToken.STRING && object != JsonToken.NUMBER) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Expected ");
            stringBuilder.append((Object)JsonToken.STRING);
            stringBuilder.append(" but was ");
            stringBuilder.append(object);
            stringBuilder.append(this.locationString());
            throw new IllegalStateException(stringBuilder.toString());
        }
        String string2 = ((JsonPrimitive)this.popStack()).getAsString();
        int n = this.stackSize;
        if (n > 0) {
            object = this.pathIndices;
            object[--n] = object[n] + true;
        }
        return string2;
    }

    @Override
    public JsonToken peek() throws IOException {
        if (this.stackSize == 0) {
            return JsonToken.END_DOCUMENT;
        }
        Object object = this.peekStack();
        if (object instanceof Iterator) {
            boolean bl = this.stack[this.stackSize - 2] instanceof JsonObject;
            if ((object = (Iterator)object).hasNext()) {
                if (bl) {
                    return JsonToken.NAME;
                }
                this.push(object.next());
                return this.peek();
            }
            object = bl ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
            return object;
        }
        if (object instanceof JsonObject) {
            return JsonToken.BEGIN_OBJECT;
        }
        if (object instanceof JsonArray) {
            return JsonToken.BEGIN_ARRAY;
        }
        if (object instanceof JsonPrimitive) {
            if (((JsonPrimitive)(object = (JsonPrimitive)object)).isString()) {
                return JsonToken.STRING;
            }
            if (((JsonPrimitive)object).isBoolean()) {
                return JsonToken.BOOLEAN;
            }
            if (((JsonPrimitive)object).isNumber()) {
                return JsonToken.NUMBER;
            }
            throw new AssertionError();
        }
        if (object instanceof JsonNull) {
            return JsonToken.NULL;
        }
        if (object == SENTINEL_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        throw new AssertionError();
    }

    public void promoteNameToValue() throws IOException {
        this.expect(JsonToken.NAME);
        Map.Entry entry = (Map.Entry)((Iterator)this.peekStack()).next();
        this.push(entry.getValue());
        this.push(new JsonPrimitive((String)entry.getKey()));
    }

    @Override
    public void skipValue() throws IOException {
        if (this.peek() == JsonToken.NAME) {
            this.nextName();
            this.pathNames[this.stackSize - 2] = "null";
        } else {
            this.popStack();
            this.pathNames[this.stackSize - 1] = "null";
        }
        int[] nArray = this.pathIndices;
        int n = this.stackSize - 1;
        nArray[n] = nArray[n] + 1;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

