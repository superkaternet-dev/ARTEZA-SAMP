/*
 * Decompiled with CFR 0.152.
 */
package retrofit2;

import retrofit2.Call;
import retrofit2.Response;

public interface Callback<T> {
    public void onFailure(Call<T> var1, Throwable var2);

    public void onResponse(Call<T> var1, Response<T> var2);
}

