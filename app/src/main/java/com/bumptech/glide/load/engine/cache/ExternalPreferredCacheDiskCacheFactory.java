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

public final class ExternalPreferredCacheDiskCacheFactory
extends DiskLruCacheFactory {
    public ExternalPreferredCacheDiskCacheFactory(Context context) {
        this(context, "image_manager_disk_cache", 0xFA00000L);
    }

    public ExternalPreferredCacheDiskCacheFactory(Context context, long l) {
        this(context, "image_manager_disk_cache", l);
    }

    public ExternalPreferredCacheDiskCacheFactory(Context context, String string2, long l) {
        super(new DiskLruCacheFactory.CacheDirectoryGetter(context, string2){
            final Context val$context;
            final String val$diskCacheName;
            {
                this.val$context = context;
                this.val$diskCacheName = string2;
            }

            private File getInternalCacheDirectory() {
                File file = this.val$context.getCacheDir();
                if (file == null) {
                    return null;
                }
                if (this.val$diskCacheName != null) {
                    return new File(file, this.val$diskCacheName);
                }
                return file;
            }

            @Override
            public File getCacheDirectory() {
                File file = this.getInternalCacheDirectory();
                if (file != null && file.exists()) {
                    return file;
                }
                File file2 = this.val$context.getExternalCacheDir();
                if (file2 != null && file2.canWrite()) {
                    if (this.val$diskCacheName != null) {
                        return new File(file2, this.val$diskCacheName);
                    }
                    return file2;
                }
                return file;
            }
        }, l);
    }
}

