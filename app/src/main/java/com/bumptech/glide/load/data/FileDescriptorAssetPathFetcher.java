/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetFileDescriptor
 *  android.content.res.AssetManager
 */
package com.bumptech.glide.load.data;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import com.bumptech.glide.load.data.AssetPathFetcher;
import java.io.IOException;

public class FileDescriptorAssetPathFetcher
extends AssetPathFetcher<AssetFileDescriptor> {
    public FileDescriptorAssetPathFetcher(AssetManager assetManager, String string2) {
        super(assetManager, string2);
    }

    @Override
    protected void close(AssetFileDescriptor assetFileDescriptor) throws IOException {
        assetFileDescriptor.close();
    }

    @Override
    public Class<AssetFileDescriptor> getDataClass() {
        return AssetFileDescriptor.class;
    }

    @Override
    protected AssetFileDescriptor loadResource(AssetManager assetManager, String string2) throws IOException {
        return assetManager.openFd(string2);
    }
}

