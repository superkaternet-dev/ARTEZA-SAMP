/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.content.res.AssetFileDescriptor
 *  android.net.Uri
 */
package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import com.bumptech.glide.load.data.LocalUriFetcher;
import java.io.FileNotFoundException;
import java.io.IOException;

public final class AssetFileDescriptorLocalUriFetcher
extends LocalUriFetcher<AssetFileDescriptor> {
    public AssetFileDescriptorLocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        super(contentResolver, uri);
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
    protected AssetFileDescriptor loadResource(Uri uri, ContentResolver object) throws FileNotFoundException {
        if ((object = object.openAssetFileDescriptor(uri, "r")) != null) {
            return object;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("FileDescriptor is null for: ");
        ((StringBuilder)object).append(uri);
        throw new FileNotFoundException(((StringBuilder)object).toString());
    }
}

