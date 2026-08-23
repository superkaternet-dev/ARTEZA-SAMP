/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.Utils;

public interface CallAdapter<T> {
    public <R> T adapt(Call<R> var1);

    public Type responseType();

    public static abstract class Factory {
        protected static Type getParameterUpperBound(int n, ParameterizedType parameterizedType) {
            return Utils.getParameterUpperBound(n, parameterizedType);
        }

        protected static Class<?> getRawType(Type type) {
            return Utils.getRawType(type);
        }

        public abstract CallAdapter<?> get(Type var1, Annotation[] var2, Retrofit var3);
    }
}

