/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import java.io.File;

public final class InternalCacheDiskCacheFactory
extends DiskLruCacheFactory {
    public InternalCacheDiskCacheFactory(Context context) {
        this(context, "image_manager_disk_cache", 0xFA00000L);
    }

    public InternalCacheDiskCacheFactory(Context context, long l) {
        this(context, "image_manager_disk_cache", l);
    }

    public InternalCacheDiskCacheFactory(Context context, String string2, long l) {
        super(new DiskLruCacheFactory.CacheDirectoryGetter(context, string2){
            final Context val$context;
            final String val$diskCacheName;
            {
                this.val$context = context;
                this.val$diskCacheName = string2;
            }

            @Override
            public File getCacheDirectory() {
                File file = this.val$context.getCacheDir();
                if (file == null) {
                    return null;
                }
                if (this.val$diskCacheName != null) {
                    return new File(file, this.val$diskCacheName);
                }
                return file;
            }
        }, l);
    }
}

