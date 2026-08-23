/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.game.gui.models;

public class DataDialogMenu {
    private int id;
    private int imgDrawableButton;
    private String nameButton;

    public DataDialogMenu(int n, int n2, String string2) {
        this.id = n;
        this.imgDrawableButton = n2;
        this.nameButton = string2;
    }

    public int getId() {
        return this.id;
    }

    public int getImgDrawableButton() {
        return this.imgDrawableButton;
    }

    public String getNameButton() {
        return this.nameButton;
    }

    public void setId(int n) {
        this.id = n;
    }

    public void setImgDrawableButton(int n) {
        this.imgDrawableButton = n;
    }

    public void setNameButton(String string2) {
        this.nameButton = string2;
    }
}

