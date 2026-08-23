/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.IBinder
 */
package com.google.android.gms.dynamic;

import android.os.IBinder;
import com.google.android.gms.common.internal.Preconditions;
import com.google.android.gms.dynamic.IObjectWrapper;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public final class ObjectWrapper<T>
extends IObjectWrapper.Stub {
    private final T zza;

    private ObjectWrapper(T t) {
        this.zza = t;
    }

    public static <T> T unwrap(IObjectWrapper object) {
        int n;
        if (object instanceof ObjectWrapper) {
            return ((ObjectWrapper)object).zza;
        }
        IBinder iBinder = object.asBinder();
        Field[] fieldArray = iBinder.getClass().getDeclaredFields();
        int n2 = fieldArray.length;
        object = null;
        int n3 = 0;
        for (n = 0; n < n2; ++n) {
            Field field = fieldArray[n];
            int n4 = n3;
            if (!field.isSynthetic()) {
                n4 = n3 + 1;
                object = field;
            }
            n3 = n4;
        }
        if (n3 == 1) {
            Preconditions.checkNotNull(object);
            if (!((AccessibleObject)object).isAccessible()) {
                ((Field)object).setAccessible(true);
                try {
                    object = ((Field)object).get(iBinder);
                }
                catch (IllegalAccessException illegalAccessException) {
                    throw new IllegalArgumentException("Could not access the field in remoteBinder.", illegalAccessException);
                }
                catch (NullPointerException nullPointerException) {
                    throw new IllegalArgumentException("Binder object is null.", nullPointerException);
                }
                return (T)object;
            }
            throw new IllegalArgumentException("IObjectWrapper declared field not private!");
        }
        n = fieldArray.length;
        object = new StringBuilder(64);
        ((StringBuilder)object).append("Unexpected number of IObjectWrapper declared fields: ");
        ((StringBuilder)object).append(n);
        object = new IllegalArgumentException(((StringBuilder)object).toString());
        throw object;
    }

    public static <T> IObjectWrapper wrap(T t) {
        return new ObjectWrapper<T>(t);
    }
}

