/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.request;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

@Deprecated
public abstract class ExperimentalRequestListener<ResourceT>
implements RequestListener<ResourceT> {
    public void onRequestStarted(Object object) {
    }

    public abstract boolean onResourceReady(ResourceT var1, Object var2, Target<ResourceT> var3, DataSource var4, boolean var5, boolean var6);
}

