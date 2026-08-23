/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import java.io.File;

public class DiskLruCacheFactory
implements DiskCache.Factory {
    private final CacheDirectoryGetter cacheDirectoryGetter;
    private final long diskCacheSize;

    public DiskLruCacheFactory(CacheDirectoryGetter cacheDirectoryGetter, long l) {
        this.diskCacheSize = l;
        this.cacheDirectoryGetter = cacheDirectoryGetter;
    }

    public DiskLruCacheFactory(String string2, long l) {
        this(new CacheDirectoryGetter(string2){
            final String val$diskCacheFolder;
            {
                this.val$diskCacheFolder = string2;
            }

            @Override
            public File getCacheDirectory() {
                return new File(this.val$diskCacheFolder);
            }
        }, l);
    }

    public DiskLruCacheFactory(String string2, String string3, long l) {
        this(new CacheDirectoryGetter(string2, string3){
            final String val$diskCacheFolder;
            final String val$diskCacheName;
            {
                this.val$diskCacheFolder = string2;
                this.val$diskCacheName = string3;
            }

            @Override
            public File getCacheDirectory() {
                return new File(this.val$diskCacheFolder, this.val$diskCacheName);
            }
        }, l);
    }

    @Override
    public DiskCache build() {
        File file = this.cacheDirectoryGetter.getCacheDirectory();
        if (file == null) {
            return null;
        }
        if (!file.isDirectory() && !file.mkdirs()) {
            return null;
        }
        return DiskLruCacheWrapper.create(file, this.diskCacheSize);
    }

    public static interface CacheDirectoryGetter {
        public File getCacheDirectory();
    }
}

