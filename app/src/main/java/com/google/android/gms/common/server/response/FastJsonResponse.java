/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Parcel
 *  android.util.Log
 */
package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.util.Log;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.server.converter.zaa;
import com.google.android.gms.common.server.response.SafeParcelResponse;
import com.google.android.gms.common.server.response.zaj;
import com.google.android.gms.common.server.response.zan;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.MapUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class FastJsonResponse {
    protected static final <O, I> I zaD(Field<I, O> field, Object object) {
        if (((Field)field).zak != null) {
            return field.zaf(object);
        }
        return (I)object;
    }

    private final <I, O> void zaE(Field<I, O> object, I object2) {
        String string2 = ((Field)object).zae;
        object2 = ((Field)object).zae(object2);
        int n = ((Field)object).zac;
        switch (n) {
            default: {
                object = new StringBuilder(44);
                ((StringBuilder)object).append("Unsupported type for conversion: ");
                ((StringBuilder)object).append(n);
                throw new IllegalStateException(((StringBuilder)object).toString());
            }
            case 8: 
            case 9: {
                if (object2 != null) {
                    this.setDecodedBytesInternal((Field<?, ?>)object, string2, (byte[])object2);
                    return;
                }
                FastJsonResponse.zaG(string2);
                return;
            }
            case 7: {
                this.setStringInternal((Field<?, ?>)object, string2, (String)object2);
                return;
            }
            case 6: {
                if (object2 != null) {
                    this.setBooleanInternal((Field<?, ?>)object, string2, (Boolean)object2);
                    return;
                }
                FastJsonResponse.zaG(string2);
                return;
            }
            case 5: {
                this.zab((Field<?, ?>)object, string2, (BigDecimal)object2);
                return;
            }
            case 4: {
                if (object2 != null) {
                    this.zan((Field<?, ?>)object, string2, (Double)object2);
                    return;
                }
                FastJsonResponse.zaG(string2);
                return;
            }
            case 2: {
                if (object2 != null) {
                    this.setLongInternal((Field<?, ?>)object, string2, (Long)object2);
                    return;
                }
                FastJsonResponse.zaG(string2);
                return;
            }
            case 1: {
                this.zaf((Field<?, ?>)object, string2, (BigInteger)object2);
                return;
            }
            case 0: 
        }
        if (object2 != null) {
            this.setIntegerInternal((Field<?, ?>)object, string2, (Integer)object2);
            return;
        }
        FastJsonResponse.zaG(string2);
    }

    private static final void zaF(StringBuilder stringBuilder, Field object, Object object2) {
        int n = ((Field)object).zaa;
        if (n != 11) {
            if (n == 7) {
                stringBuilder.append("\"");
                stringBuilder.append(JsonUtils.escapeString((String)object2));
                stringBuilder.append("\"");
                return;
            }
            stringBuilder.append(object2);
            return;
        }
        object = ((Field)object).zag;
        Preconditions.checkNotNull(object);
        stringBuilder.append(((FastJsonResponse)((Class)object).cast(object2)).toString());
    }

    private static final <O> void zaG(String string2) {
        if (Log.isLoggable((String)"FastJsonResponse", (int)6)) {
            StringBuilder stringBuilder = new StringBuilder(String.valueOf(string2).length() + 58);
            stringBuilder.append("Output field (");
            stringBuilder.append(string2);
            stringBuilder.append(") has a null value, but expected a primitive");
            Log.e((String)"FastJsonResponse", (String)stringBuilder.toString());
        }
    }

    public <T extends FastJsonResponse> void addConcreteTypeArrayInternal(Field field, String string2, ArrayList<T> arrayList) {
        throw new UnsupportedOperationException("Concrete type array not supported");
    }

    public <T extends FastJsonResponse> void addConcreteTypeInternal(Field field, String string2, T t) {
        throw new UnsupportedOperationException("Concrete type not supported");
    }

    public abstract Map<String, Field<?, ?>> getFieldMappings();

    protected Object getFieldValue(Field object) {
        CharSequence charSequence = ((Field)object).zae;
        if (((Field)object).zag != null) {
            boolean bl = this.getValueObject((String)charSequence) == null;
            Preconditions.checkState(bl, "Concrete field shouldn't be value object: %s", ((Field)object).zae);
            bl = ((Field)object).zad;
            try {
                char c = Character.toUpperCase(((String)charSequence).charAt(0));
                object = ((String)charSequence).substring(1);
                int n = String.valueOf(object).length();
                charSequence = new StringBuilder(n + 4);
                ((StringBuilder)charSequence).append("get");
                ((StringBuilder)charSequence).append(c);
                ((StringBuilder)charSequence).append((String)object);
                object = this.getClass().getMethod(((StringBuilder)charSequence).toString(), new Class[0]).invoke((Object)this, new Object[0]);
                return object;
            }
            catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
        return this.getValueObject((String)charSequence);
    }

    protected abstract Object getValueObject(String var1);

    protected boolean isFieldSet(Field object) {
        if (((Field)object).zac == 11) {
            boolean bl = ((Field)object).zad;
            object = ((Field)object).zae;
            if (bl) {
                object = new UnsupportedOperationException("Concrete type arrays not supported");
                throw object;
            }
            object = new UnsupportedOperationException("Concrete types not supported");
            throw object;
        }
        return this.isPrimitiveFieldSet(((Field)object).zae);
    }

    protected abstract boolean isPrimitiveFieldSet(String var1);

    protected void setBooleanInternal(Field<?, ?> field, String string2, boolean bl) {
        throw new UnsupportedOperationException("Boolean not supported");
    }

    protected void setDecodedBytesInternal(Field<?, ?> field, String string2, byte[] byArray) {
        throw new UnsupportedOperationException("byte[] not supported");
    }

    protected void setIntegerInternal(Field<?, ?> field, String string2, int n) {
        throw new UnsupportedOperationException("Integer not supported");
    }

    protected void setLongInternal(Field<?, ?> field, String string2, long l) {
        throw new UnsupportedOperationException("Long not supported");
    }

    protected void setStringInternal(Field<?, ?> field, String string2, String string3) {
        throw new UnsupportedOperationException("String not supported");
    }

    protected void setStringMapInternal(Field<?, ?> field, String string2, Map<String, String> map) {
        throw new UnsupportedOperationException("String map not supported");
    }

    protected void setStringsInternal(Field<?, ?> field, String string2, ArrayList<String> arrayList) {
        throw new UnsupportedOperationException("String list not supported");
    }

    public String toString() {
        Map<String, Field<?, ?>> map = this.getFieldMappings();
        StringBuilder stringBuilder = new StringBuilder(100);
        block5: for (String string2 : map.keySet()) {
            Object object;
            Field<?, ?> field;
            block14: {
                int n;
                ArrayList arrayList;
                field = map.get(string2);
                if (!this.isFieldSet(field)) continue;
                object = FastJsonResponse.zaD(field, this.getFieldValue(field));
                if (stringBuilder.length() == 0) {
                    stringBuilder.append("{");
                } else {
                    stringBuilder.append(",");
                }
                stringBuilder.append("\"");
                stringBuilder.append(string2);
                stringBuilder.append("\":");
                if (object == null) {
                    stringBuilder.append("null");
                    continue;
                }
                switch (field.zac) {
                    default: {
                        if (field.zab) {
                            arrayList = (ArrayList)object;
                            stringBuilder.append("[");
                            n = arrayList.size();
                            break;
                        }
                        break block14;
                    }
                    case 10: {
                        MapUtils.writeStringMapToJson(stringBuilder, (HashMap)object);
                        continue block5;
                    }
                    case 9: {
                        stringBuilder.append("\"");
                        stringBuilder.append(Base64Utils.encodeUrlSafe((byte[])object));
                        stringBuilder.append("\"");
                        continue block5;
                    }
                    case 8: {
                        stringBuilder.append("\"");
                        stringBuilder.append(Base64Utils.encode((byte[])object));
                        stringBuilder.append("\"");
                        continue block5;
                    }
                }
                for (int i = 0; i < n; ++i) {
                    if (i > 0) {
                        stringBuilder.append(",");
                    }
                    if ((object = arrayList.get(i)) == null) continue;
                    FastJsonResponse.zaF(stringBuilder, field, object);
                }
                stringBuilder.append("]");
                continue;
            }
            FastJsonResponse.zaF(stringBuilder, field, object);
        }
        if (stringBuilder.length() > 0) {
            stringBuilder.append("}");
        } else {
            stringBuilder.append("{}");
        }
        return stringBuilder.toString();
    }

    public final <O> void zaA(Field<String, O> field, String string2) {
        if (((Field)field).zak != null) {
            this.zaE(field, string2);
            return;
        }
        this.setStringInternal(field, field.zae, string2);
    }

    public final <O> void zaB(Field<Map<String, String>, O> field, Map<String, String> map) {
        if (((Field)field).zak != null) {
            this.zaE(field, map);
            return;
        }
        this.setStringMapInternal(field, field.zae, map);
    }

    public final <O> void zaC(Field<ArrayList<String>, O> field, ArrayList<String> arrayList) {
        if (((Field)field).zak != null) {
            this.zaE(field, arrayList);
            return;
        }
        this.setStringsInternal(field, field.zae, arrayList);
    }

    public final <O> void zaa(Field<BigDecimal, O> field, BigDecimal bigDecimal) {
        if (((Field)field).zak != null) {
            this.zaE(field, bigDecimal);
            return;
        }
        this.zab(field, field.zae, bigDecimal);
    }

    protected void zab(Field<?, ?> field, String string2, BigDecimal bigDecimal) {
        throw new UnsupportedOperationException("BigDecimal not supported");
    }

    public final <O> void zac(Field<ArrayList<BigDecimal>, O> field, ArrayList<BigDecimal> arrayList) {
        if (((Field)field).zak != null) {
            this.zaE(field, arrayList);
            return;
        }
        this.zad(field, field.zae, arrayList);
    }

    protected void zad(Field<?, ?> field, String string2, ArrayList<BigDecimal> arrayList) {
        throw new UnsupportedOperationException("BigDecimal list not supported");
    }

    public final <O> void zae(Field<BigInteger, O> field, BigInteger bigInteger) {
        if (((Field)field).zak != null) {
            this.zaE(field, bigInteger);
            return;
        }
        this.zaf(field, field.zae, bigInteger);
    }

    protected void zaf(Field<?, ?> field, String string2, BigInteger bigInteger) {
        throw new UnsupportedOperationException("BigInteger not supported");
    }

    public final <O> void zag(Field<ArrayList<BigInteger>, O> field, ArrayList<BigInteger> arrayList) {
        if (((Field)field).zak != null) {
            this.zaE(field, arrayList);
            return;
        }
        this.zah(field, field.zae, arrayList);
    }

    protected void zah(Field<?, ?> field, String string2, ArrayList<BigInteger> arrayList) {
        throw new UnsupportedOperationException("BigInteger list not supported");
    }

    public final <O> void zai(Field<Boolean, O> field, boolean bl) {
        if (((Field)field).zak != null) {
            this.zaE(field, bl);
            return;
        }
        this.setBooleanInternal(field, field.zae, bl);
    }

    public final <O> void zaj(Field<ArrayList<Boolean>, O> field, ArrayList<Boolean> arrayList) {
        if (((Field)field).zak != null) {
            this.zaE(field, arrayList);
            return;
        }
        this.zak(field, field.zae, arrayList);
    }

    protected void zak(Field<?, ?> field, String string2, ArrayList<Boolean> arrayList) {
        throw new UnsupportedOperationException("Boolean list not supported");
    }

    public final <O> void zal(Field<byte[], O> field, byte[] byArray) {
        if (((Field)field).zak != null) {
            this.zaE(field, byArray);
            return;
        }
        this.setDecodedBytesInternal(field, field.zae, byArray);
    }

    public final <O> void zam(Field<Double, O> field, double d) {
        if (((Field)field).zak != null) {
            this.zaE(field, d);
            return;
        }
        this.zan(field, field.zae, d);
    }

    protected void zan(Field<?, ?> field, String string2, double d) {
        throw new UnsupportedOperationException("Double not supported");
    }

    public final <O> void zao(Field<ArrayList<Double>, O> field, ArrayList<Double> arrayList) {
        if (((Field)field).zak != null) {
            this.zaE(field, arrayList);
            return;
        }
        this.zap(field, field.zae, arrayList);
    }

    protected void zap(Field<?, ?> field, String string2, ArrayList<Double> arrayList) {
        throw new UnsupportedOperationException("Double list not supported");
    }

    public final <O> void zaq(Field<Float, O> field, float f) {
        if (((Field)field).zak != null) {
            this.zaE(field, Float.valueOf(f));
            return;
        }
        this.zar(field, field.zae, f);
    }

    protected void zar(Field<?, ?> field, String string2, float f) {
        throw new UnsupportedOperationException("Float not supported");
    }

    public final <O> void zas(Field<ArrayList<Float>, O> field, ArrayList<Float> arrayList) {
        if (((Field)field).zak != null) {
            this.zaE(field, arrayList);
            return;
        }
        this.zat(field, field.zae, arrayList);
    }

    protected void zat(Field<?, ?> field, String string2, ArrayList<Float> arrayList) {
        throw new UnsupportedOperationException("Float list not supported");
    }

    public final <O> void zau(Field<Integer, O> field, int n) {
        if (((Field)field).zak != null) {
            this.zaE(field, n);
            return;
        }
        this.setIntegerInternal(field, field.zae, n);
    }

    public final <O> void zav(Field<ArrayList<Integer>, O> field, ArrayList<Integer> arrayList) {
        if (((Field)field).zak != null) {
            this.zaE(field, arrayList);
            return;
        }
        this.zaw(field, field.zae, arrayList);
    }

    protected void zaw(Field<?, ?> field, String string2, ArrayList<Integer> arrayList) {
        throw new UnsupportedOperationException("Integer list not supported");
    }

    public final <O> void zax(Field<Long, O> field, long l) {
        if (((Field)field).zak != null) {
            this.zaE(field, l);
            return;
        }
        this.setLongInternal(field, field.zae, l);
    }

    public final <O> void zay(Field<ArrayList<Long>, O> field, ArrayList<Long> arrayList) {
        if (((Field)field).zak != null) {
            this.zaE(field, arrayList);
            return;
        }
        this.zaz(field, field.zae, arrayList);
    }

    protected void zaz(Field<?, ?> field, String string2, ArrayList<Long> arrayList) {
        throw new UnsupportedOperationException("Long list not supported");
    }

    public static class Field<I, O>
    extends AbstractSafeParcelable {
        public static final zaj CREATOR = new zaj();
        protected final int zaa;
        protected final boolean zab;
        protected final int zac;
        protected final boolean zad;
        protected final String zae;
        protected final int zaf;
        protected final Class<? extends FastJsonResponse> zag;
        protected final String zah;
        private final int zai;
        private zan zaj;
        private FieldConverter<I, O> zak;

        Field(int n, int n2, boolean bl, int n3, boolean bl2, String string2, int n4, String string3, zaa zaa2) {
            this.zai = n;
            this.zaa = n2;
            this.zab = bl;
            this.zac = n3;
            this.zad = bl2;
            this.zae = string2;
            this.zaf = n4;
            if (string3 == null) {
                this.zag = null;
                this.zah = null;
            } else {
                this.zag = SafeParcelResponse.class;
                this.zah = string3;
            }
            if (zaa2 == null) {
                this.zak = null;
                return;
            }
            this.zak = zaa2.zab();
        }

        protected Field(int n, boolean bl, int n2, boolean bl2, String string2, int n3, Class<? extends FastJsonResponse> clazz, FieldConverter<I, O> fieldConverter) {
            this.zai = 1;
            this.zaa = n;
            this.zab = bl;
            this.zac = n2;
            this.zad = bl2;
            this.zae = string2;
            this.zaf = n3;
            this.zag = clazz;
            this.zah = clazz == null ? null : clazz.getCanonicalName();
            this.zak = fieldConverter;
        }

        public static Field<byte[], byte[]> forBase64(String string2, int n) {
            return new Field<byte[], byte[]>(8, false, 8, false, string2, n, null, null);
        }

        public static Field<Boolean, Boolean> forBoolean(String string2, int n) {
            return new Field<Boolean, Boolean>(6, false, 6, false, string2, n, null, null);
        }

        public static <T extends FastJsonResponse> Field<T, T> forConcreteType(String string2, int n, Class<T> clazz) {
            return new Field(11, false, 11, false, string2, n, clazz, null);
        }

        public static <T extends FastJsonResponse> Field<ArrayList<T>, ArrayList<T>> forConcreteTypeArray(String string2, int n, Class<T> clazz) {
            return new Field<ArrayList<T>, ArrayList<T>>(11, true, 11, true, string2, n, clazz, null);
        }

        public static Field<Double, Double> forDouble(String string2, int n) {
            return new Field<Double, Double>(4, false, 4, false, string2, n, null, null);
        }

        public static Field<Float, Float> forFloat(String string2, int n) {
            return new Field<Float, Float>(3, false, 3, false, string2, n, null, null);
        }

        public static Field<Integer, Integer> forInteger(String string2, int n) {
            return new Field<Integer, Integer>(0, false, 0, false, string2, n, null, null);
        }

        public static Field<Long, Long> forLong(String string2, int n) {
            return new Field<Long, Long>(2, false, 2, false, string2, n, null, null);
        }

        public static Field<String, String> forString(String string2, int n) {
            return new Field<String, String>(7, false, 7, false, string2, n, null, null);
        }

        public static Field<HashMap<String, String>, HashMap<String, String>> forStringMap(String string2, int n) {
            return new Field<HashMap<String, String>, HashMap<String, String>>(10, false, 10, false, string2, n, null, null);
        }

        public static Field<ArrayList<String>, ArrayList<String>> forStrings(String string2, int n) {
            return new Field<ArrayList<String>, ArrayList<String>>(7, true, 7, true, string2, n, null, null);
        }

        public static Field withConverter(String string2, int n, FieldConverter<?, ?> fieldConverter, boolean bl) {
            fieldConverter.zaa();
            fieldConverter.zab();
            return new Field(7, bl, 0, false, string2, n, null, fieldConverter);
        }

        public int getSafeParcelableFieldId() {
            return this.zaf;
        }

        public final String toString() {
            Objects.ToStringHelper toStringHelper = Objects.toStringHelper(this).add("versionCode", this.zai).add("typeIn", this.zaa).add("typeInArray", this.zab).add("typeOut", this.zac).add("typeOutArray", this.zad).add("outputFieldName", this.zae).add("safeParcelFieldId", this.zaf).add("concreteTypeName", this.zag());
            Class<? extends FastJsonResponse> clazz = this.zag;
            if (clazz != null) {
                toStringHelper.add("concreteType.class", clazz.getCanonicalName());
            }
            if ((clazz = this.zak) != null) {
                toStringHelper.add("converterName", clazz.getClass().getCanonicalName());
            }
            return toStringHelper.toString();
        }

        public final void writeToParcel(Parcel parcel, int n) {
            int n2 = SafeParcelWriter.beginObjectHeader(parcel);
            SafeParcelWriter.writeInt(parcel, 1, this.zai);
            SafeParcelWriter.writeInt(parcel, 2, this.zaa);
            SafeParcelWriter.writeBoolean(parcel, 3, this.zab);
            SafeParcelWriter.writeInt(parcel, 4, this.zac);
            SafeParcelWriter.writeBoolean(parcel, 5, this.zad);
            SafeParcelWriter.writeString(parcel, 6, this.zae, false);
            SafeParcelWriter.writeInt(parcel, 7, this.getSafeParcelableFieldId());
            SafeParcelWriter.writeString(parcel, 8, this.zag(), false);
            SafeParcelWriter.writeParcelable(parcel, 9, this.zaa(), n, false);
            SafeParcelWriter.finishObjectHeader(parcel, n2);
        }

        final zaa zaa() {
            FieldConverter<I, O> fieldConverter = this.zak;
            if (fieldConverter == null) {
                return null;
            }
            return com.google.android.gms.common.server.converter.zaa.zaa(fieldConverter);
        }

        public final Field<I, O> zab() {
            return new Field<I, O>(this.zai, this.zaa, this.zab, this.zac, this.zad, this.zae, this.zaf, this.zah, this.zaa());
        }

        public final FastJsonResponse zad() throws InstantiationException, IllegalAccessException {
            Preconditions.checkNotNull(this.zag);
            Class<? extends FastJsonResponse> clazz = this.zag;
            if (clazz == SafeParcelResponse.class) {
                Preconditions.checkNotNull(this.zah);
                Preconditions.checkNotNull(this.zaj, "The field mapping dictionary must be set if the concrete type is a SafeParcelResponse object.");
                return new SafeParcelResponse(this.zaj, this.zah);
            }
            return clazz.newInstance();
        }

        public final O zae(I i) {
            Preconditions.checkNotNull(this.zak);
            return Preconditions.checkNotNull(this.zak.zac(i));
        }

        public final I zaf(O o) {
            Preconditions.checkNotNull(this.zak);
            return this.zak.zad(o);
        }

        final String zag() {
            String string2;
            String string3 = string2 = this.zah;
            if (string2 == null) {
                string3 = null;
            }
            return string3;
        }

        public final Map<String, Field<?, ?>> zah() {
            Preconditions.checkNotNull(this.zah);
            Preconditions.checkNotNull(this.zaj);
            return Preconditions.checkNotNull(this.zaj.zab(this.zah));
        }

        public final void zai(zan zan2) {
            this.zaj = zan2;
        }

        public final boolean zaj() {
            return this.zak != null;
        }
    }

    public static interface FieldConverter<I, O> {
        public int zaa();

        public int zab();

        public O zac(I var1);

        public I zad(O var1);
    }
}

