/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.launcher.network;

import com.blackrussia.launcher.network.Links;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    public static final String apulink = "http://wh3606.web3.maze-host.ru/tutor/api.json";

    @GET(value="http://wh3606.web3.maze-host.ru/tutor/api.json")
    public Call<Links> getLinks();
}

