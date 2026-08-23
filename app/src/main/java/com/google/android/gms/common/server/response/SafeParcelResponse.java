/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.os.Parcel
 *  android.os.Parcelable$Creator
 *  android.util.SparseArray
 */
package com.google.android.gms.common.server.response;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader;
import com.google.android.gms.common.internal.safeparcel.SafeParcelWriter;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.common.server.response.FastSafeParcelableJsonResponse;
import com.google.android.gms.common.server.response.zan;
import com.google.android.gms.common.server.response.zaq;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.gms.common.util.Base64Utils;
import com.google.android.gms.common.util.JsonUtils;
import com.google.android.gms.common.util.MapUtils;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SafeParcelResponse
extends FastSafeParcelableJsonResponse {
    public static final Parcelable.Creator<SafeParcelResponse> CREATOR = new zaq();
    private final int zaa;
    private final Parcel zab;
    private final int zac;
    private final zan zad;
    private final String zae;
    private int zaf;
    private int zag;

    SafeParcelResponse(int n, Parcel object, zan zan2) {
        this.zaa = n;
        this.zab = Preconditions.checkNotNull(object);
        this.zac = 2;
        this.zad = zan2;
        object = zan2 == null ? null : zan2.zaa();
        this.zae = object;
        this.zaf = 2;
    }

    private SafeParcelResponse(SafeParcelable safeParcelable, zan zan2, String string2) {
        Parcel parcel;
        this.zaa = 1;
        this.zab = parcel = Parcel.obtain();
        safeParcelable.writeToParcel(parcel, 0);
        this.zac = 1;
        this.zad = Preconditions.checkNotNull(zan2);
        this.zae = Preconditions.checkNotNull(string2);
        this.zaf = 2;
    }

    public SafeParcelResponse(zan zan2, String string2) {
        this.zaa = 1;
        this.zab = Parcel.obtain();
        this.zac = 0;
        this.zad = Preconditions.checkNotNull(zan2);
        this.zae = Preconditions.checkNotNull(string2);
        this.zaf = 0;
    }

    public static <T extends FastJsonResponse> SafeParcelResponse from(T t) {
        String string2 = Preconditions.checkNotNull(t.getClass().getCanonicalName());
        zan zan2 = new zan(t.getClass());
        SafeParcelResponse.zaF(zan2, t);
        zan2.zac();
        zan2.zad();
        return new SafeParcelResponse((SafeParcelable)((Object)t), zan2, string2);
    }

    private static void zaF(zan object, FastJsonResponse object2) {
        Class<Object> clazz = object2.getClass();
        if (!((zan)object).zaf(clazz)) {
            Map<String, FastJsonResponse.Field<?, ?>> map = ((FastJsonResponse)object2).getFieldMappings();
            ((zan)object).zae(clazz, map);
            Iterator<String> iterator2 = map.keySet().iterator();
            while (iterator2.hasNext()) {
                object2 = map.get(iterator2.next());
                clazz = ((FastJsonResponse.Field)object2).zag;
                if (clazz == null) continue;
                try {
                    SafeParcelResponse.zaF((zan)object, (FastJsonResponse)clazz.newInstance());
                }
                catch (IllegalAccessException illegalAccessException) {
                    object = String.valueOf(Preconditions.checkNotNull(((FastJsonResponse.Field)object2).zag).getCanonicalName());
                    object = ((String)object).length() != 0 ? "Could not access object of type ".concat((String)object) : new String("Could not access object of type ");
                    throw new IllegalStateException((String)object, illegalAccessException);
                }
                catch (InstantiationException instantiationException) {
                    object = String.valueOf(Preconditions.checkNotNull(((FastJsonResponse.Field)object2).zag).getCanonicalName());
                    object = ((String)object).length() != 0 ? "Could not instantiate an object of type ".concat((String)object) : new String("Could not instantiate an object of type ");
                    throw new IllegalStateException((String)object, instantiationException);
                }
            }
        }
    }

    private final void zaG(FastJsonResponse.Field<?, ?> parcel) {
        if (parcel.zaf != -1) {
            parcel = this.zab;
            if (parcel != null) {
                switch (this.zaf) {
                    default: {
                        throw new IllegalStateException("Attempted to parse JSON with a SafeParcelResponse object that is already filled with data.");
                    }
                    case 1: {
                        return;
                    }
                    case 0: 
                }
                this.zag = SafeParcelWriter.beginObjectHeader(parcel);
                this.zaf = 1;
                return;
            }
            throw new IllegalStateException("Internal Parcel object is null.");
        }
        throw new IllegalStateException("Field does not have a valid safe parcelable field id.");
    }

    /*
     * WARNING - void declaration
     */
    private final void zaH(StringBuilder serializable, Map<String, FastJsonResponse.Field<?, ?>> object2, Parcel parcel) {
        void var3_12;
        SparseArray sparseArray = new SparseArray();
        for (Map.Entry entry : object2.entrySet()) {
            sparseArray.put(((FastJsonResponse.Field)entry.getValue()).getSafeParcelableFieldId(), entry);
        }
        ((StringBuilder)serializable).append('{');
        int n = SafeParcelReader.validateObjectHeader((Parcel)var3_12);
        int n2 = 0;
        block40: while (var3_12.dataPosition() < n) {
            Object object;
            int n3 = SafeParcelReader.readHeader((Parcel)var3_12);
            Map.Entry entry = (Map.Entry)sparseArray.get(SafeParcelReader.getFieldId(n3));
            if (entry == null) continue;
            if (n2 != 0) {
                ((StringBuilder)serializable).append(",");
            }
            Object object3 = (String)entry.getKey();
            FastJsonResponse.Field field = (FastJsonResponse.Field)entry.getValue();
            ((StringBuilder)serializable).append("\"");
            ((StringBuilder)serializable).append((String)object3);
            ((StringBuilder)serializable).append("\":");
            if (field.zaj()) {
                n2 = field.zac;
                switch (n2) {
                    default: {
                        serializable = new StringBuilder(36);
                        ((StringBuilder)serializable).append("Unknown field out type = ");
                        ((StringBuilder)serializable).append(n2);
                        throw new IllegalArgumentException(((StringBuilder)serializable).toString());
                    }
                    case 11: {
                        throw new IllegalArgumentException("Method does not accept concrete type.");
                    }
                    case 10: {
                        Bundle bundle = SafeParcelReader.createBundle((Parcel)var3_12, n3);
                        object = new HashMap();
                        for (String string2 : bundle.keySet()) {
                            ((HashMap)object).put(string2, Preconditions.checkNotNull(bundle.getString(string2)));
                        }
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, object));
                        n2 = 1;
                        continue block40;
                    }
                    case 8: 
                    case 9: {
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, SafeParcelReader.createByteArray((Parcel)var3_12, n3)));
                        n2 = 1;
                        continue block40;
                    }
                    case 7: {
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, SafeParcelReader.createString((Parcel)var3_12, n3)));
                        n2 = 1;
                        continue block40;
                    }
                    case 6: {
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, SafeParcelReader.readBoolean((Parcel)var3_12, n3)));
                        n2 = 1;
                        continue block40;
                    }
                    case 5: {
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, SafeParcelReader.createBigDecimal((Parcel)var3_12, n3)));
                        n2 = 1;
                        continue block40;
                    }
                    case 4: {
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, SafeParcelReader.readDouble((Parcel)var3_12, n3)));
                        n2 = 1;
                        continue block40;
                    }
                    case 3: {
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, Float.valueOf(SafeParcelReader.readFloat((Parcel)var3_12, n3))));
                        n2 = 1;
                        continue block40;
                    }
                    case 2: {
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, SafeParcelReader.readLong((Parcel)var3_12, n3)));
                        n2 = 1;
                        continue block40;
                    }
                    case 1: {
                        SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, SafeParcelReader.createBigInteger((Parcel)var3_12, n3)));
                        n2 = 1;
                        continue block40;
                    }
                    case 0: 
                }
                SafeParcelResponse.zaJ((StringBuilder)serializable, field, SafeParcelResponse.zaD(field, SafeParcelReader.readInt((Parcel)var3_12, n3)));
                n2 = 1;
                continue;
            }
            if (field.zad) {
                ((StringBuilder)serializable).append("[");
                switch (field.zac) {
                    default: {
                        throw new IllegalStateException("Unknown field type out.");
                    }
                    case 11: {
                        object3 = SafeParcelReader.createParcelArray((Parcel)var3_12, n3);
                        n3 = ((Parcel[])object3).length;
                        for (n2 = 0; n2 < n3; ++n2) {
                            if (n2 > 0) {
                                ((StringBuilder)serializable).append(",");
                            }
                            object3[n2].setDataPosition(0);
                            this.zaH((StringBuilder)serializable, field.zah(), (Parcel)object3[n2]);
                        }
                        break;
                    }
                    case 8: 
                    case 9: 
                    case 10: {
                        throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                    }
                    case 7: {
                        ArrayUtils.writeStringArray((StringBuilder)serializable, SafeParcelReader.createStringArray((Parcel)var3_12, n3));
                        break;
                    }
                    case 6: {
                        ArrayUtils.writeArray((StringBuilder)serializable, SafeParcelReader.createBooleanArray((Parcel)var3_12, n3));
                        break;
                    }
                    case 5: {
                        ArrayUtils.writeArray((StringBuilder)serializable, SafeParcelReader.createBigDecimalArray((Parcel)var3_12, n3));
                        break;
                    }
                    case 4: {
                        ArrayUtils.writeArray((StringBuilder)serializable, SafeParcelReader.createDoubleArray((Parcel)var3_12, n3));
                        break;
                    }
                    case 3: {
                        ArrayUtils.writeArray((StringBuilder)serializable, SafeParcelReader.createFloatArray((Parcel)var3_12, n3));
                        break;
                    }
                    case 2: {
                        ArrayUtils.writeArray((StringBuilder)serializable, SafeParcelReader.createLongArray((Parcel)var3_12, n3));
                        break;
                    }
                    case 1: {
                        ArrayUtils.writeArray((StringBuilder)serializable, SafeParcelReader.createBigIntegerArray((Parcel)var3_12, n3));
                        break;
                    }
                    case 0: {
                        ArrayUtils.writeArray((StringBuilder)serializable, SafeParcelReader.createIntArray((Parcel)var3_12, n3));
                    }
                }
                ((StringBuilder)serializable).append("]");
                n2 = 1;
                continue;
            }
            switch (field.zac) {
                default: {
                    throw new IllegalStateException("Unknown field type out");
                }
                case 11: {
                    object3 = SafeParcelReader.createParcel((Parcel)var3_12, n3);
                    object3.setDataPosition(0);
                    this.zaH((StringBuilder)serializable, field.zah(), (Parcel)object3);
                    n2 = 1;
                    continue block40;
                }
                case 10: {
                    Bundle bundle = SafeParcelReader.createBundle((Parcel)var3_12, n3);
                    object3 = bundle.keySet();
                    ((StringBuilder)serializable).append("{");
                    object = object3.iterator();
                    n2 = 1;
                    while (object.hasNext()) {
                        object3 = (String)object.next();
                        if (n2 == 0) {
                            ((StringBuilder)serializable).append(",");
                        }
                        ((StringBuilder)serializable).append("\"");
                        ((StringBuilder)serializable).append((String)object3);
                        ((StringBuilder)serializable).append("\":\"");
                        ((StringBuilder)serializable).append(JsonUtils.escapeString(bundle.getString((String)object3)));
                        ((StringBuilder)serializable).append("\"");
                        n2 = 0;
                    }
                    ((StringBuilder)serializable).append("}");
                    n2 = 1;
                    continue block40;
                }
                case 9: {
                    byte[] byArray = SafeParcelReader.createByteArray((Parcel)var3_12, n3);
                    ((StringBuilder)serializable).append("\"");
                    ((StringBuilder)serializable).append(Base64Utils.encodeUrlSafe(byArray));
                    ((StringBuilder)serializable).append("\"");
                    n2 = 1;
                    continue block40;
                }
                case 8: {
                    byte[] byArray = SafeParcelReader.createByteArray((Parcel)var3_12, n3);
                    ((StringBuilder)serializable).append("\"");
                    ((StringBuilder)serializable).append(Base64Utils.encode(byArray));
                    ((StringBuilder)serializable).append("\"");
                    n2 = 1;
                    continue block40;
                }
                case 7: {
                    String string3 = SafeParcelReader.createString((Parcel)var3_12, n3);
                    ((StringBuilder)serializable).append("\"");
                    ((StringBuilder)serializable).append(JsonUtils.escapeString(string3));
                    ((StringBuilder)serializable).append("\"");
                    n2 = 1;
                    continue block40;
                }
                case 6: {
                    ((StringBuilder)serializable).append(SafeParcelReader.readBoolean((Parcel)var3_12, n3));
                    n2 = 1;
                    continue block40;
                }
                case 5: {
                    ((StringBuilder)serializable).append(SafeParcelReader.createBigDecimal((Parcel)var3_12, n3));
                    n2 = 1;
                    continue block40;
                }
                case 4: {
                    ((StringBuilder)serializable).append(SafeParcelReader.readDouble((Parcel)var3_12, n3));
                    n2 = 1;
                    continue block40;
                }
                case 3: {
                    ((StringBuilder)serializable).append(SafeParcelReader.readFloat((Parcel)var3_12, n3));
                    n2 = 1;
                    continue block40;
                }
                case 2: {
                    ((StringBuilder)serializable).append(SafeParcelReader.readLong((Parcel)var3_12, n3));
                    n2 = 1;
                    continue block40;
                }
                case 1: {
                    ((StringBuilder)serializable).append(SafeParcelReader.createBigInteger((Parcel)var3_12, n3));
                    n2 = 1;
                    continue block40;
                }
                case 0: 
            }
            ((StringBuilder)serializable).append(SafeParcelReader.readInt((Parcel)var3_12, n3));
            n2 = 1;
        }
        if (var3_12.dataPosition() == n) {
            ((StringBuilder)serializable).append('}');
            return;
        }
        serializable = new StringBuilder(37);
        ((StringBuilder)serializable).append("Overread allowed size end=");
        ((StringBuilder)serializable).append(n);
        serializable = new SafeParcelReader.ParseException(((StringBuilder)serializable).toString(), (Parcel)var3_12);
        throw serializable;
    }

    private static final void zaI(StringBuilder stringBuilder, int n, Object object) {
        switch (n) {
            default: {
                stringBuilder = new StringBuilder(26);
                stringBuilder.append("Unknown type = ");
                stringBuilder.append(n);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            case 11: {
                throw new IllegalArgumentException("Method does not accept concrete type.");
            }
            case 10: {
                MapUtils.writeStringMapToJson(stringBuilder, (HashMap)Preconditions.checkNotNull(object));
                return;
            }
            case 9: {
                stringBuilder.append("\"");
                stringBuilder.append(Base64Utils.encodeUrlSafe((byte[])object));
                stringBuilder.append("\"");
                return;
            }
            case 8: {
                stringBuilder.append("\"");
                stringBuilder.append(Base64Utils.encode((byte[])object));
                stringBuilder.append("\"");
                return;
            }
            case 7: {
                stringBuilder.append("\"");
                stringBuilder.append(JsonUtils.escapeString(Preconditions.checkNotNull(object).toString()));
                stringBuilder.append("\"");
                return;
            }
            case 0: 
            case 1: 
            case 2: 
            case 3: 
            case 4: 
            case 5: 
            case 6: 
        }
        stringBuilder.append(object);
    }

    private static final void zaJ(StringBuilder stringBuilder, FastJsonResponse.Field<?, ?> field, Object object) {
        if (field.zab) {
            object = (ArrayList)object;
            stringBuilder.append("[");
            int n = ((ArrayList)object).size();
            for (int i = 0; i < n; ++i) {
                if (i != 0) {
                    stringBuilder.append(",");
                }
                SafeParcelResponse.zaI(stringBuilder, field.zaa, ((ArrayList)object).get(i));
            }
            stringBuilder.append("]");
            return;
        }
        SafeParcelResponse.zaI(stringBuilder, field.zaa, object);
    }

    @Override
    public final <T extends FastJsonResponse> void addConcreteTypeArrayInternal(FastJsonResponse.Field field, String object, ArrayList<T> arrayList) {
        this.zaG(field);
        object = new ArrayList();
        Preconditions.checkNotNull(arrayList).size();
        int n = arrayList.size();
        for (int i = 0; i < n; ++i) {
            ((ArrayList)object).add(((SafeParcelResponse)((FastJsonResponse)arrayList.get(i))).zaE());
        }
        SafeParcelWriter.writeParcelList(this.zab, field.getSafeParcelableFieldId(), (List<Parcel>)object, true);
    }

    @Override
    public final <T extends FastJsonResponse> void addConcreteTypeInternal(FastJsonResponse.Field field, String string2, T t) {
        this.zaG(field);
        string2 = ((SafeParcelResponse)t).zaE();
        SafeParcelWriter.writeParcel(this.zab, field.getSafeParcelableFieldId(), (Parcel)string2, true);
    }

    @Override
    public final Map<String, FastJsonResponse.Field<?, ?>> getFieldMappings() {
        zan zan2 = this.zad;
        if (zan2 == null) {
            return null;
        }
        return zan2.zab(Preconditions.checkNotNull(this.zae));
    }

    @Override
    public final Object getValueObject(String string2) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override
    public final boolean isPrimitiveFieldSet(String string2) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }

    @Override
    protected final void setBooleanInternal(FastJsonResponse.Field<?, ?> field, String string2, boolean bl) {
        this.zaG(field);
        SafeParcelWriter.writeBoolean(this.zab, field.getSafeParcelableFieldId(), bl);
    }

    @Override
    protected final void setDecodedBytesInternal(FastJsonResponse.Field<?, ?> field, String string2, byte[] byArray) {
        this.zaG(field);
        SafeParcelWriter.writeByteArray(this.zab, field.getSafeParcelableFieldId(), byArray, true);
    }

    @Override
    protected final void setIntegerInternal(FastJsonResponse.Field<?, ?> field, String string2, int n) {
        this.zaG(field);
        SafeParcelWriter.writeInt(this.zab, field.getSafeParcelableFieldId(), n);
    }

    @Override
    protected final void setLongInternal(FastJsonResponse.Field<?, ?> field, String string2, long l) {
        this.zaG(field);
        SafeParcelWriter.writeLong(this.zab, field.getSafeParcelableFieldId(), l);
    }

    @Override
    protected final void setStringInternal(FastJsonResponse.Field<?, ?> field, String string2, String string3) {
        this.zaG(field);
        SafeParcelWriter.writeString(this.zab, field.getSafeParcelableFieldId(), string3, true);
    }

    @Override
    protected final void setStringMapInternal(FastJsonResponse.Field<?, ?> field, String object, Map<String, String> map) {
        this.zaG(field);
        Bundle bundle = new Bundle();
        for (String string2 : Preconditions.checkNotNull(map).keySet()) {
            bundle.putString(string2, map.get(string2));
        }
        SafeParcelWriter.writeBundle(this.zab, field.getSafeParcelableFieldId(), bundle, true);
    }

    @Override
    protected final void setStringsInternal(FastJsonResponse.Field<?, ?> field, String stringArray, ArrayList<String> arrayList) {
        this.zaG(field);
        int n = Preconditions.checkNotNull(arrayList).size();
        stringArray = new String[n];
        for (int i = 0; i < n; ++i) {
            stringArray[i] = arrayList.get(i);
        }
        SafeParcelWriter.writeStringArray(this.zab, field.getSafeParcelableFieldId(), stringArray, true);
    }

    @Override
    public final String toString() {
        Preconditions.checkNotNull(this.zad, "Cannot convert to JSON on client side.");
        Parcel parcel = this.zaE();
        parcel.setDataPosition(0);
        StringBuilder stringBuilder = new StringBuilder(100);
        this.zaH(stringBuilder, Preconditions.checkNotNull(this.zad.zab(Preconditions.checkNotNull(this.zae))), parcel);
        return stringBuilder.toString();
    }

    public final void writeToParcel(Parcel parcel, int n) {
        zan zan2;
        int n2 = SafeParcelWriter.beginObjectHeader(parcel);
        SafeParcelWriter.writeInt(parcel, 1, this.zaa);
        SafeParcelWriter.writeParcel(parcel, 2, this.zaE(), false);
        switch (this.zac) {
            default: {
                zan2 = this.zad;
                break;
            }
            case 1: {
                zan2 = this.zad;
                break;
            }
            case 0: {
                zan2 = null;
            }
        }
        SafeParcelWriter.writeParcelable(parcel, 3, zan2, n, false);
        SafeParcelWriter.finishObjectHeader(parcel, n2);
    }

    public final Parcel zaE() {
        switch (this.zaf) {
            default: {
                break;
            }
            case 1: {
                SafeParcelWriter.finishObjectHeader(this.zab, this.zag);
                this.zaf = 2;
                break;
            }
            case 0: {
                int n;
                this.zag = n = SafeParcelWriter.beginObjectHeader(this.zab);
                SafeParcelWriter.finishObjectHeader(this.zab, n);
                this.zaf = 2;
            }
        }
        return this.zab;
    }

    @Override
    protected final void zab(FastJsonResponse.Field<?, ?> field, String string2, BigDecimal bigDecimal) {
        this.zaG(field);
        SafeParcelWriter.writeBigDecimal(this.zab, field.getSafeParcelableFieldId(), bigDecimal, true);
    }

    @Override
    protected final void zad(FastJsonResponse.Field<?, ?> field, String bigDecimalArray, ArrayList<BigDecimal> arrayList) {
        this.zaG(field);
        int n = Preconditions.checkNotNull(arrayList).size();
        bigDecimalArray = new BigDecimal[n];
        for (int i = 0; i < n; ++i) {
            bigDecimalArray[i] = arrayList.get(i);
        }
        SafeParcelWriter.writeBigDecimalArray(this.zab, field.getSafeParcelableFieldId(), bigDecimalArray, true);
    }

    @Override
    protected final void zaf(FastJsonResponse.Field<?, ?> field, String string2, BigInteger bigInteger) {
        this.zaG(field);
        SafeParcelWriter.writeBigInteger(this.zab, field.getSafeParcelableFieldId(), bigInteger, true);
    }

    @Override
    protected final void zah(FastJsonResponse.Field<?, ?> field, String bigIntegerArray, ArrayList<BigInteger> arrayList) {
        this.zaG(field);
        int n = Preconditions.checkNotNull(arrayList).size();
        bigIntegerArray = new BigInteger[n];
        for (int i = 0; i < n; ++i) {
            bigIntegerArray[i] = arrayList.get(i);
        }
        SafeParcelWriter.writeBigIntegerArray(this.zab, field.getSafeParcelableFieldId(), bigIntegerArray, true);
    }

    @Override
    protected final void zak(FastJsonResponse.Field<?, ?> field, String object, ArrayList<Boolean> arrayList) {
        this.zaG(field);
        int n = Preconditions.checkNotNull(arrayList).size();
        object = new boolean[n];
        for (int i = 0; i < n; ++i) {
            object[i] = arrayList.get(i);
        }
        SafeParcelWriter.writeBooleanArray(this.zab, field.getSafeParcelableFieldId(), (boolean[])object, true);
    }

    @Override
    protected final void zan(FastJsonResponse.Field<?, ?> field, String string2, double d) {
        this.zaG(field);
        SafeParcelWriter.writeDouble(this.zab, field.getSafeParcelableFieldId(), d);
    }

    @Override
    protected final void zap(FastJsonResponse.Field<?, ?> field, String object, ArrayList<Double> arrayList) {
        this.zaG(field);
        int n = Preconditions.checkNotNull(arrayList).size();
        object = new double[n];
        for (int i = 0; i < n; ++i) {
            object[i] = arrayList.get(i);
        }
        SafeParcelWriter.writeDoubleArray(this.zab, field.getSafeParcelableFieldId(), (double[])object, true);
    }

    @Override
    protected final void zar(FastJsonResponse.Field<?, ?> field, String string2, float f) {
        this.zaG(field);
        SafeParcelWriter.writeFloat(this.zab, field.getSafeParcelableFieldId(), f);
    }

    @Override
    protected final void zat(FastJsonResponse.Field<?, ?> field, String object, ArrayList<Float> arrayList) {
        this.zaG(field);
        int n = Preconditions.checkNotNull(arrayList).size();
        object = new float[n];
        for (int i = 0; i < n; ++i) {
            object[i] = arrayList.get(i).floatValue();
        }
        SafeParcelWriter.writeFloatArray(this.zab, field.getSafeParcelableFieldId(), (float[])object, true);
    }

    @Override
    protected final void zaw(FastJsonResponse.Field<?, ?> field, String object, ArrayList<Integer> arrayList) {
        this.zaG(field);
        int n = Preconditions.checkNotNull(arrayList).size();
        object = new int[n];
        for (int i = 0; i < n; ++i) {
            object[i] = arrayList.get(i);
        }
        SafeParcelWriter.writeIntArray(this.zab, field.getSafeParcelableFieldId(), (int[])object, true);
    }

    @Override
    protected final void zaz(FastJsonResponse.Field<?, ?> field, String object, ArrayList<Long> arrayList) {
        this.zaG(field);
        int n = Preconditions.checkNotNull(arrayList).size();
        object = new long[n];
        for (int i = 0; i < n; ++i) {
            object[i] = arrayList.get(i);
        }
        SafeParcelWriter.writeLongArray(this.zab, field.getSafeParcelableFieldId(), (long[])object, true);
    }
}

