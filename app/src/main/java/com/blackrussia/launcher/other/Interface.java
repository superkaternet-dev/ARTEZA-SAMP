/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.launcher.other;

import com.blackrussia.launcher.model.News;
import com.blackrussia.launcher.model.Servers;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Interface {
    public static final String nwulink = "http://wh3606.web3.maze-host.ru/stories.json";
    public static final String sulink = "http://wh3606.web3.maze-host.ru/servers.json";

    @GET(value="http://wh3606.web3.maze-host.ru/stories.json")
    public Call<List<News>> getNews();

    @GET(value="http://wh3606.web3.maze-host.ru/servers.json")
    public Call<List<Servers>> getServers();
}

