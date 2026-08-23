/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;

public final class JsonPrimitive
extends JsonElement {
    private static final Class<?>[] PRIMITIVE_TYPES = new Class[]{Integer.TYPE, Long.TYPE, Short.TYPE, Float.TYPE, Double.TYPE, Byte.TYPE, Boolean.TYPE, Character.TYPE, Integer.class, Long.class, Short.class, Float.class, Double.class, Byte.class, Boolean.class, Character.class};
    private Object value;

    public JsonPrimitive(Boolean bl) {
        this.setValue(bl);
    }

    public JsonPrimitive(Character c) {
        this.setValue(c);
    }

    public JsonPrimitive(Number number) {
        this.setValue(number);
    }

    JsonPrimitive(Object object) {
        this.setValue(object);
    }

    public JsonPrimitive(String string2) {
        this.setValue(string2);
    }

    private static boolean isIntegral(JsonPrimitive object) {
        object = ((JsonPrimitive)object).value;
        boolean bl = object instanceof Number;
        boolean bl2 = false;
        if (bl) {
            if ((object = (Number)object) instanceof BigInteger || object instanceof Long || object instanceof Integer || object instanceof Short || object instanceof Byte) {
                bl2 = true;
            }
            return bl2;
        }
        return false;
    }

    private static boolean isPrimitiveOrString(Object classArray) {
        if (classArray instanceof String) {
            return true;
        }
        Class<?> clazz = classArray.getClass();
        classArray = PRIMITIVE_TYPES;
        int n = classArray.length;
        for (int i = 0; i < n; ++i) {
            if (!classArray[i].isAssignableFrom(clazz)) continue;
            return true;
        }
        return false;
    }

    @Override
    JsonPrimitive deepCopy() {
        return this;
    }

    public boolean equals(Object object) {
        boolean bl = true;
        boolean bl2 = true;
        boolean bl3 = true;
        if (this == object) {
            return true;
        }
        if (object != null && this.getClass() == object.getClass()) {
            object = (JsonPrimitive)object;
            if (this.value == null) {
                if (((JsonPrimitive)object).value != null) {
                    bl3 = false;
                }
                return bl3;
            }
            if (JsonPrimitive.isIntegral(this) && JsonPrimitive.isIntegral((JsonPrimitive)object)) {
                bl3 = this.getAsNumber().longValue() == ((JsonPrimitive)object).getAsNumber().longValue() ? bl : false;
                return bl3;
            }
            Object object2 = this.value;
            if (object2 instanceof Number && ((JsonPrimitive)object).value instanceof Number) {
                double d = this.getAsNumber().doubleValue();
                double d2 = ((JsonPrimitive)object).getAsNumber().doubleValue();
                bl3 = bl2;
                if (d != d2) {
                    bl3 = Double.isNaN(d) && Double.isNaN(d2) ? bl2 : false;
                }
                return bl3;
            }
            return object2.equals(((JsonPrimitive)object).value);
        }
        return false;
    }

    @Override
    public BigDecimal getAsBigDecimal() {
        Object object = this.value;
        object = object instanceof BigDecimal ? (BigDecimal)object : new BigDecimal(this.value.toString());
        return object;
    }

    @Override
    public BigInteger getAsBigInteger() {
        Object object = this.value;
        object = object instanceof BigInteger ? (BigInteger)object : new BigInteger(this.value.toString());
        return object;
    }

    @Override
    public boolean getAsBoolean() {
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper();
        }
        return Boolean.parseBoolean(this.getAsString());
    }

    @Override
    Boolean getAsBooleanWrapper() {
        return (Boolean)this.value;
    }

    @Override
    public byte getAsByte() {
        byte by = this.isNumber() ? this.getAsNumber().byteValue() : Byte.parseByte(this.getAsString());
        return by;
    }

    @Override
    public char getAsCharacter() {
        return this.getAsString().charAt(0);
    }

    @Override
    public double getAsDouble() {
        double d = this.isNumber() ? this.getAsNumber().doubleValue() : Double.parseDouble(this.getAsString());
        return d;
    }

    @Override
    public float getAsFloat() {
        float f = this.isNumber() ? this.getAsNumber().floatValue() : Float.parseFloat(this.getAsString());
        return f;
    }

    @Override
    public int getAsInt() {
        int n = this.isNumber() ? this.getAsNumber().intValue() : Integer.parseInt(this.getAsString());
        return n;
    }

    @Override
    public long getAsLong() {
        long l = this.isNumber() ? this.getAsNumber().longValue() : Long.parseLong(this.getAsString());
        return l;
    }

    @Override
    public Number getAsNumber() {
        Object object = this.value;
        object = object instanceof String ? new LazilyParsedNumber((String)object) : (Number)object;
        return object;
    }

    @Override
    public short getAsShort() {
        short s = this.isNumber() ? this.getAsNumber().shortValue() : Short.parseShort(this.getAsString());
        return s;
    }

    @Override
    public String getAsString() {
        if (this.isNumber()) {
            return this.getAsNumber().toString();
        }
        if (this.isBoolean()) {
            return this.getAsBooleanWrapper().toString();
        }
        return (String)this.value;
    }

    public int hashCode() {
        if (this.value == null) {
            return 31;
        }
        if (JsonPrimitive.isIntegral(this)) {
            long l = this.getAsNumber().longValue();
            return (int)(l >>> 32 ^ l);
        }
        Object object = this.value;
        if (object instanceof Number) {
            long l = Double.doubleToLongBits(this.getAsNumber().doubleValue());
            return (int)(l >>> 32 ^ l);
        }
        return object.hashCode();
    }

    public boolean isBoolean() {
        return this.value instanceof Boolean;
    }

    public boolean isNumber() {
        return this.value instanceof Number;
    }

    public boolean isString() {
        return this.value instanceof String;
    }

    void setValue(Object object) {
        if (object instanceof Character) {
            this.value = String.valueOf(((Character)object).charValue());
        } else {
            boolean bl = object instanceof Number || JsonPrimitive.isPrimitiveOrString(object);
            $Gson$Preconditions.checkArgument(bl);
            this.value = object;
        }
    }
}

