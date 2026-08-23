/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.bumptech.glide.module;

import android.content.Context;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.AppliesOptions;
import com.bumptech.glide.module.LibraryGlideModule;

public abstract class AppGlideModule
extends LibraryGlideModule
implements AppliesOptions {
    @Override
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
    }

    public boolean isManifestParsingEnabled() {
        return true;
    }
}

