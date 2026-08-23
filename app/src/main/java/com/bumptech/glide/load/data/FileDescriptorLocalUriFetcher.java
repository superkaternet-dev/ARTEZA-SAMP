/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.net.Uri
 *  android.os.ParcelFileDescriptor
 */
package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.data.LocalUriFetcher;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileDescriptorLocalUriFetcher
extends LocalUriFetcher<ParcelFileDescriptor> {
    public FileDescriptorLocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        super(contentResolver, uri);
    }

    @Override
    protected void close(ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        parcelFileDescriptor.close();
    }

    @Override
    public Class<ParcelFileDescriptor> getDataClass() {
        return ParcelFileDescriptor.class;
    }

    @Override
    protected ParcelFileDescriptor loadResource(Uri uri, ContentResolver object) throws FileNotFoundException {
        if ((object = object.openAssetFileDescriptor(uri, "r")) != null) {
            return object.getParcelFileDescriptor();
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("FileDescriptor is null for: ");
        ((StringBuilder)object).append(uri);
        throw new FileNotFoundException(((StringBuilder)object).toString());
    }
}

