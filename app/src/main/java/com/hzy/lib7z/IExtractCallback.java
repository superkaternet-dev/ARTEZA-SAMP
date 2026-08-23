/*
 * Decompiled with CFR 0.152.
 */
package com.hzy.lib7z;

public interface IExtractCallback {
    public void onError(int var1, String var2);

    public void onGetFileNum(int var1);

    public void onProgress(String var1, long var2);

    public void onStart();

    public void onSucceed();
}

