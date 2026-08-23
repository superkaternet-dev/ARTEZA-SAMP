/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.launcher.network;

import com.google.gson.annotations.SerializedName;

public class Links {
    @SerializedName(value="clientVersionCode")
    private int ClientVersion = 45;
    @SerializedName(value="gameFilesVersionCode")
    private Integer GameFilesVersion;
    @SerializedName(value="URL_CLIENT")
    private String URL_CLIENT;
    @SerializedName(value="URL_GAME_FILES")
    private String URL_GAME_FILES;

    public final int getTargetClientVersion() {
        return this.ClientVersion;
    }

    public final Integer getTargetGameFilesVersion() {
        return this.GameFilesVersion;
    }

    public final String getUrlClient() {
        return this.URL_CLIENT;
    }

    public final String getUrlFiles() {
        return this.URL_GAME_FILES;
    }
}

