/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.connection;

public interface ConnectionTokenProvider {
    public void getToken(boolean var1, GetTokenCallback var2);

    public static interface GetTokenCallback {
        public void onError(String var1);

        public void onSuccess(String var1);
    }
}

