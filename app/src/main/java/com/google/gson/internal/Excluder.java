/*
 * Decompiled with CFR 0.152.
 */
package com.google.gson.internal;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Excluder
implements TypeAdapterFactory,
Cloneable {
    public static final Excluder DEFAULT = new Excluder();
    private static final double IGNORE_VERSIONS = -1.0;
    private List<ExclusionStrategy> deserializationStrategies;
    private int modifiers = 136;
    private boolean requireExpose;
    private List<ExclusionStrategy> serializationStrategies = Collections.emptyList();
    private boolean serializeInnerClasses = true;
    private double version = -1.0;

    public Excluder() {
        this.deserializationStrategies = Collections.emptyList();
    }

    private boolean isAnonymousOrLocal(Class<?> clazz) {
        boolean bl = !Enum.class.isAssignableFrom(clazz) && (clazz.isAnonymousClass() || clazz.isLocalClass());
        return bl;
    }

    private boolean isInnerClass(Class<?> clazz) {
        boolean bl = clazz.isMemberClass() && !this.isStatic(clazz);
        return bl;
    }

    private boolean isStatic(Class<?> clazz) {
        boolean bl = (clazz.getModifiers() & 8) != 0;
        return bl;
    }

    private boolean isValidSince(Since since) {
        return since == null || !(since.value() > this.version);
    }

    private boolean isValidUntil(Until until) {
        return until == null || !(until.value() <= this.version);
    }

    private boolean isValidVersion(Since since, Until until) {
        boolean bl = this.isValidSince(since) && this.isValidUntil(until);
        return bl;
    }

    protected Excluder clone() {
        try {
            Excluder excluder = (Excluder)super.clone();
            return excluder;
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new AssertionError((Object)cloneNotSupportedException);
        }
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<T> clazz = typeToken.getRawType();
        boolean bl = this.excludeClass(clazz, true);
        boolean bl2 = this.excludeClass(clazz, false);
        if (!bl && !bl2) {
            return null;
        }
        return new TypeAdapter<T>(this, bl2, bl, gson, typeToken){
            private TypeAdapter<T> delegate;
            final Excluder this$0;
            final Gson val$gson;
            final boolean val$skipDeserialize;
            final boolean val$skipSerialize;
            final TypeToken val$type;
            {
                this.this$0 = excluder;
                this.val$skipDeserialize = bl;
                this.val$skipSerialize = bl2;
                this.val$gson = gson;
                this.val$type = typeToken;
            }

            private TypeAdapter<T> delegate() {
                TypeAdapter typeAdapter = this.delegate;
                if (typeAdapter == null) {
                    this.delegate = typeAdapter = this.val$gson.getDelegateAdapter(this.this$0, this.val$type);
                }
                return typeAdapter;
            }

            @Override
            public T read(JsonReader jsonReader) throws IOException {
                if (this.val$skipDeserialize) {
                    jsonReader.skipValue();
                    return null;
                }
                return this.delegate().read(jsonReader);
            }

            @Override
            public void write(JsonWriter jsonWriter, T t) throws IOException {
                if (this.val$skipSerialize) {
                    jsonWriter.nullValue();
                    return;
                }
                this.delegate().write(jsonWriter, t);
            }
        };
    }

    public Excluder disableInnerClassSerialization() {
        Excluder excluder = this.clone();
        excluder.serializeInnerClasses = false;
        return excluder;
    }

    public boolean excludeClass(Class<?> clazz, boolean bl) {
        if (this.version != -1.0 && !this.isValidVersion(clazz.getAnnotation(Since.class), clazz.getAnnotation(Until.class))) {
            return true;
        }
        if (!this.serializeInnerClasses && this.isInnerClass(clazz)) {
            return true;
        }
        if (this.isAnonymousOrLocal(clazz)) {
            return true;
        }
        List<ExclusionStrategy> list = bl ? this.serializationStrategies : this.deserializationStrategies;
        list = list.iterator();
        while (list.hasNext()) {
            if (!((ExclusionStrategy)list.next()).shouldSkipClass(clazz)) continue;
            return true;
        }
        return false;
    }

    public boolean excludeField(Field object, boolean bl) {
        List<ExclusionStrategy> list;
        if ((this.modifiers & ((Field)object).getModifiers()) != 0) {
            return true;
        }
        if (this.version != -1.0 && !this.isValidVersion(((Field)object).getAnnotation(Since.class), ((Field)object).getAnnotation(Until.class))) {
            return true;
        }
        if (((Field)object).isSynthetic()) {
            return true;
        }
        if (this.requireExpose && ((list = ((Field)object).getAnnotation(Expose.class)) == null || (bl ? !list.serialize() : !list.deserialize()))) {
            return true;
        }
        if (!this.serializeInnerClasses && this.isInnerClass(((Field)object).getType())) {
            return true;
        }
        if (this.isAnonymousOrLocal(((Field)object).getType())) {
            return true;
        }
        list = bl ? this.serializationStrategies : this.deserializationStrategies;
        if (!list.isEmpty()) {
            object = new FieldAttributes((Field)object);
            list = list.iterator();
            while (list.hasNext()) {
                if (!((ExclusionStrategy)list.next()).shouldSkipField((FieldAttributes)object)) continue;
                return true;
            }
        }
        return false;
    }

    public Excluder excludeFieldsWithoutExposeAnnotation() {
        Excluder excluder = this.clone();
        excluder.requireExpose = true;
        return excluder;
    }

    public Excluder withExclusionStrategy(ExclusionStrategy exclusionStrategy, boolean bl, boolean bl2) {
        ArrayList<ExclusionStrategy> arrayList;
        Excluder excluder = this.clone();
        if (bl) {
            arrayList = new ArrayList<ExclusionStrategy>(this.serializationStrategies);
            excluder.serializationStrategies = arrayList;
            arrayList.add(exclusionStrategy);
        }
        if (bl2) {
            arrayList = new ArrayList<ExclusionStrategy>(this.deserializationStrategies);
            excluder.deserializationStrategies = arrayList;
            arrayList.add(exclusionStrategy);
        }
        return excluder;
    }

    public Excluder withModifiers(int ... nArray) {
        Excluder excluder = this.clone();
        excluder.modifiers = 0;
        for (int n : nArray) {
            excluder.modifiers |= n;
        }
        return excluder;
    }

    public Excluder withVersion(double d) {
        Excluder excluder = this.clone();
        excluder.version = d;
        return excluder;
    }
}

