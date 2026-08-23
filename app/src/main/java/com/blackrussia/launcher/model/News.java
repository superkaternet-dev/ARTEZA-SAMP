/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.launcher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class News {
    @Expose
    @SerializedName(value="imageurl")
    private String imageurl;
    @Expose
    @SerializedName(value="title")
    private String title;
    @Expose
    @SerializedName(value="url")
    private String url;

    public News(String string2, String string3, String string4) {
        this.imageurl = string2;
        this.title = string3;
        this.url = string4;
    }

    public String getImageUrl() {
        return this.imageurl;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }
}

