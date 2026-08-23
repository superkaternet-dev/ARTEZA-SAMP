/*
 * Decompiled with CFR 0.152.
 */
package com.google.firebase.database.core;

import java.util.concurrent.ExecutorService;

public interface TokenProvider {
    public void addTokenChangeListener(ExecutorService var1, TokenChangeListener var2);

    public void getToken(boolean var1, GetTokenCompletionListener var2);

    public void removeTokenChangeListener(TokenChangeListener var1);

    public static interface GetTokenCompletionListener {
        public void onError(String var1);

        public void onSuccess(String var1);
    }

    public static interface TokenChangeListener {
        public void onTokenChange();

        public void onTokenChange(String var1);
    }
}

