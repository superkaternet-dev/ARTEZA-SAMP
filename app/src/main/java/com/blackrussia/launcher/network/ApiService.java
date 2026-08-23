/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.launcher.network;

import com.blackrussia.launcher.network.Api;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static ApiService instance;
    public int ClientVersion;
    public Integer GameFilesVersion;
    public String URL_CLIENT;
    public String URL_GAME_FILES;
    private Retrofit retrofit = new Retrofit.Builder().baseUrl("https://brussia-new.reactnet.site/").addConverterFactory(GsonConverterFactory.create()).build();

    private ApiService() {
    }

    public static ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    public Api getApiService() {
        return this.retrofit.create(Api.class);
    }
}

