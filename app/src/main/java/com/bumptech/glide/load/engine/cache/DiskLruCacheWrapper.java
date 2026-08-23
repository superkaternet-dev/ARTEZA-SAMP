/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheWriteLocker;
import com.bumptech.glide.load.engine.cache.SafeKeyGenerator;
import java.io.File;
import java.io.IOException;

public class DiskLruCacheWrapper
implements DiskCache {
    private static final int APP_VERSION = 1;
    private static final String TAG = "DiskLruCacheWrapper";
    private static final int VALUE_COUNT = 1;
    private static DiskLruCacheWrapper wrapper;
    private final File directory;
    private DiskLruCache diskLruCache;
    private final long maxSize;
    private final SafeKeyGenerator safeKeyGenerator;
    private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();

    @Deprecated
    protected DiskLruCacheWrapper(File file, long l) {
        this.directory = file;
        this.maxSize = l;
        this.safeKeyGenerator = new SafeKeyGenerator();
    }

    public static DiskCache create(File file, long l) {
        return new DiskLruCacheWrapper(file, l);
    }

    @Deprecated
    public static DiskCache get(File object, long l) {
        synchronized (DiskLruCacheWrapper.class) {
            if (wrapper == null) {
                DiskLruCacheWrapper diskLruCacheWrapper;
                wrapper = diskLruCacheWrapper = new DiskLruCacheWrapper((File)object, l);
            }
            object = wrapper;
            return object;
        }
    }

    private DiskLruCache getDiskCache() throws IOException {
        synchronized (this) {
            if (this.diskLruCache == null) {
                this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, this.maxSize);
            }
            DiskLruCache diskLruCache = this.diskLruCache;
            return diskLruCache;
        }
    }

    private void resetDiskCache() {
        synchronized (this) {
            this.diskLruCache = null;
            return;
        }
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void clear() {
        synchronized (this) {
            Throwable throwable2222222;
            block8: {
                this.getDiskCache().delete();
                this.resetDiskCache();
                {
                    catch (Throwable throwable2222222) {
                        break block8;
                    }
                    catch (IOException iOException) {}
                    {
                        if (Log.isLoggable((String)TAG, (int)5)) {
                            Log.w((String)TAG, (String)"Unable to clear disk cache or disk cache cleared externally", (Throwable)iOException);
                        }
                        this.resetDiskCache();
                    }
                }
                return;
            }
            this.resetDiskCache();
            throw throwable2222222;
        }
    }

    @Override
    public void delete(Key object) {
        block2: {
            object = this.safeKeyGenerator.getSafeKey((Key)object);
            try {
                this.getDiskCache().remove((String)object);
            }
            catch (IOException iOException) {
                if (!Log.isLoggable((String)TAG, (int)5)) break block2;
                Log.w((String)TAG, (String)"Unable to delete from disk cache", (Throwable)iOException);
            }
        }
    }

    @Override
    public File get(Key object) {
        block4: {
            StringBuilder stringBuilder;
            Object object2 = this.safeKeyGenerator.getSafeKey((Key)object);
            if (Log.isLoggable((String)TAG, (int)2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Get: Obtained: ");
                stringBuilder.append((String)object2);
                stringBuilder.append(" for for Key: ");
                stringBuilder.append(object);
                Log.v((String)TAG, (String)stringBuilder.toString());
            }
            stringBuilder = null;
            object = null;
            object2 = this.getDiskCache().get((String)object2);
            if (object2 == null) break block4;
            try {
                object = ((DiskLruCache.Value)object2).getFile(0);
            }
            catch (IOException iOException) {
                object = stringBuilder;
                if (!Log.isLoggable((String)TAG, (int)5)) break block4;
                Log.w((String)TAG, (String)"Unable to get from disk cache", (Throwable)iOException);
                object = stringBuilder;
            }
        }
        return object;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void put(Key var1_1, DiskCache.Writer var2_4) {
        var3_6 = this.safeKeyGenerator.getSafeKey((Key)var1_1);
        this.writeLocker.acquire(var3_6);
        if (Log.isLoggable((String)"DiskLruCacheWrapper", (int)2)) {
            var4_7 = new StringBuilder();
            var4_7.append("Put: Obtained: ");
            var4_7.append(var3_6);
            var4_7.append(" for for Key: ");
            var4_7.append(var1_1);
            Log.v((String)"DiskLruCacheWrapper", (String)var4_7.toString());
        }
        try {
            var1_1 = this.getDiskCache();
            var4_7 = var1_1.get(var3_6);
            if (var4_7 != null) {
                this.writeLocker.release(var3_6);
                return;
            }
        }
        catch (IOException var1_2) {
            if (Log.isLoggable((String)"DiskLruCacheWrapper", (int)5) == false) return;
            Log.w((String)"DiskLruCacheWrapper", (String)"Unable to put to disk cache", (Throwable)var1_2);
            return;
        }
        {
            var1_1 = var1_1.edit(var3_6);
            if (var1_1 == null) ** GOTO lbl-1000
        }
        try {
            if (!var2_4.write(var1_1.getFile(0))) return;
            var1_1.commit();
            return;
        }
        finally {
            var1_1.abortUnlessCommitted();
        }
lbl-1000:
        // 1 sources

        {
            var2_4 = new StringBuilder();
            var2_4.append("Had two simultaneous puts for: ");
            var2_4.append(var3_6);
            var1_1 = new IllegalStateException(var2_4.toString());
            throw var1_1;
        }
    }
}

