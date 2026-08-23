/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.launcher.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Servers {
    @Expose
    @SerializedName(value="color")
    private String color;
    @Expose
    @SerializedName(value="dopname")
    private String dopname;
    @Expose
    @SerializedName(value="maxonline")
    private int maxonline;
    @Expose
    @SerializedName(value="name")
    private String name;
    @Expose
    @SerializedName(value="online")
    private int online;

    public Servers(String string2, String string3, String string4, int n, int n2) {
        this.color = string2;
        this.dopname = string3;
        this.name = string4;
        this.online = n;
        this.maxonline = n2;
    }

    public String getColor() {
        return this.color;
    }

    public String getDopname() {
        return this.dopname;
    }

    public int getOnline() {
        return this.online;
    }

    public int getmaxOnline() {
        return this.maxonline;
    }

    public String getname() {
        return this.name;
    }
}

