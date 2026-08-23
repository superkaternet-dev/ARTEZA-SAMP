/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.os.Build$VERSION
 *  android.util.Log
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import com.bumptech.glide.load.engine.bitmap_recycle.AttributeStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruPoolStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.SizeConfigStrategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LruBitmapPool
implements BitmapPool {
    private static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.ARGB_8888;
    private static final String TAG = "LruBitmapPool";
    private final Set<Bitmap.Config> allowedConfigs;
    private long currentSize;
    private int evictions;
    private int hits;
    private final long initialMaxSize;
    private long maxSize;
    private int misses;
    private int puts;
    private final LruPoolStrategy strategy;
    private final BitmapTracker tracker;

    public LruBitmapPool(long l) {
        this(l, LruBitmapPool.getDefaultStrategy(), LruBitmapPool.getDefaultAllowedConfigs());
    }

    LruBitmapPool(long l, LruPoolStrategy lruPoolStrategy, Set<Bitmap.Config> set) {
        this.initialMaxSize = l;
        this.maxSize = l;
        this.strategy = lruPoolStrategy;
        this.allowedConfigs = set;
        this.tracker = new NullBitmapTracker();
    }

    public LruBitmapPool(long l, Set<Bitmap.Config> set) {
        this(l, LruBitmapPool.getDefaultStrategy(), set);
    }

    private static void assertNotHardwareConfig(Bitmap.Config config) {
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        if (config != Bitmap.Config.HARDWARE) {
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Cannot create a mutable Bitmap with config: ");
        stringBuilder.append(config);
        stringBuilder.append(". Consider setting Downsampler#ALLOW_HARDWARE_CONFIG to false in your RequestOptions and/or in GlideBuilder.setDefaultRequestOptions");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    private static Bitmap createBitmap(int n, int n2, Bitmap.Config config) {
        if (config == null) {
            config = DEFAULT_CONFIG;
        }
        return Bitmap.createBitmap((int)n, (int)n2, (Bitmap.Config)config);
    }

    private void dump() {
        if (Log.isLoggable((String)TAG, (int)2)) {
            this.dumpUnchecked();
        }
    }

    private void dumpUnchecked() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Hits=");
        stringBuilder.append(this.hits);
        stringBuilder.append(", misses=");
        stringBuilder.append(this.misses);
        stringBuilder.append(", puts=");
        stringBuilder.append(this.puts);
        stringBuilder.append(", evictions=");
        stringBuilder.append(this.evictions);
        stringBuilder.append(", currentSize=");
        stringBuilder.append(this.currentSize);
        stringBuilder.append(", maxSize=");
        stringBuilder.append(this.maxSize);
        stringBuilder.append("\nStrategy=");
        stringBuilder.append(this.strategy);
        Log.v((String)TAG, (String)stringBuilder.toString());
    }

    private void evict() {
        this.trimToSize(this.maxSize);
    }

    private static Set<Bitmap.Config> getDefaultAllowedConfigs() {
        HashSet<Bitmap.Config> hashSet = new HashSet<Bitmap.Config>(Arrays.asList(Bitmap.Config.values()));
        if (Build.VERSION.SDK_INT >= 19) {
            hashSet.add(null);
        }
        if (Build.VERSION.SDK_INT >= 26) {
            hashSet.remove(Bitmap.Config.HARDWARE);
        }
        return Collections.unmodifiableSet(hashSet);
    }

    private static LruPoolStrategy getDefaultStrategy() {
        LruPoolStrategy lruPoolStrategy = Build.VERSION.SDK_INT >= 19 ? new SizeConfigStrategy() : new AttributeStrategy();
        return lruPoolStrategy;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private Bitmap getDirtyOrNull(int n, int n2, Bitmap.Config config) {
        synchronized (this) {
            LruBitmapPool.assertNotHardwareConfig(config);
            Object object = this.strategy;
            Bitmap.Config config2 = config != null ? config : DEFAULT_CONFIG;
            config2 = object.get(n, n2, config2);
            if (config2 == null) {
                if (Log.isLoggable((String)TAG, (int)3)) {
                    object = new StringBuilder();
                    ((StringBuilder)object).append("Missing bitmap=");
                    ((StringBuilder)object).append(this.strategy.logBitmap(n, n2, config));
                    Log.d((String)TAG, (String)((StringBuilder)object).toString());
                }
                ++this.misses;
            } else {
                ++this.hits;
                this.currentSize -= (long)this.strategy.getSize((Bitmap)config2);
                this.tracker.remove((Bitmap)config2);
                LruBitmapPool.normalize((Bitmap)config2);
            }
            if (Log.isLoggable((String)TAG, (int)2)) {
                object = new StringBuilder();
                ((StringBuilder)object).append("Get bitmap=");
                ((StringBuilder)object).append(this.strategy.logBitmap(n, n2, config));
                Log.v((String)TAG, (String)((StringBuilder)object).toString());
            }
            this.dump();
            return config2;
        }
    }

    private static void maybeSetPreMultiplied(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 19) {
            bitmap.setPremultiplied(true);
        }
    }

    private static void normalize(Bitmap bitmap) {
        bitmap.setHasAlpha(true);
        LruBitmapPool.maybeSetPreMultiplied(bitmap);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void trimToSize(long l) {
        synchronized (this) {
            while (this.currentSize > l) {
                Bitmap bitmap = this.strategy.removeLast();
                if (bitmap == null) {
                    if (Log.isLoggable((String)TAG, (int)5)) {
                        Log.w((String)TAG, (String)"Size mismatch, resetting");
                        this.dumpUnchecked();
                    }
                    this.currentSize = 0L;
                    return;
                }
                this.tracker.remove(bitmap);
                this.currentSize -= (long)this.strategy.getSize(bitmap);
                ++this.evictions;
                if (Log.isLoggable((String)TAG, (int)3)) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Evicting bitmap=");
                    stringBuilder.append(this.strategy.logBitmap(bitmap));
                    Log.d((String)TAG, (String)stringBuilder.toString());
                }
                this.dump();
                bitmap.recycle();
            }
            return;
        }
    }

    @Override
    public void clearMemory() {
        if (Log.isLoggable((String)TAG, (int)3)) {
            Log.d((String)TAG, (String)"clearMemory");
        }
        this.trimToSize(0L);
    }

    public long evictionCount() {
        return this.evictions;
    }

    @Override
    public Bitmap get(int n, int n2, Bitmap.Config config) {
        Bitmap bitmap = this.getDirtyOrNull(n, n2, config);
        if (bitmap != null) {
            bitmap.eraseColor(0);
            config = bitmap;
        } else {
            config = LruBitmapPool.createBitmap(n, n2, config);
        }
        return config;
    }

    public long getCurrentSize() {
        return this.currentSize;
    }

    @Override
    public Bitmap getDirty(int n, int n2, Bitmap.Config config) {
        Bitmap bitmap;
        Bitmap bitmap2 = bitmap = this.getDirtyOrNull(n, n2, config);
        if (bitmap == null) {
            bitmap2 = LruBitmapPool.createBitmap(n, n2, config);
        }
        return bitmap2;
    }

    @Override
    public long getMaxSize() {
        return this.maxSize;
    }

    public long hitCount() {
        return this.hits;
    }

    public long missCount() {
        return this.misses;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void put(Bitmap object) {
        synchronized (this) {
            Throwable throwable2;
            if (object != null) {
                try {
                    if (object.isRecycled()) {
                        object = new IllegalStateException("Cannot pool recycled bitmap");
                        throw object;
                    }
                    if (object.isMutable() && (long)this.strategy.getSize((Bitmap)object) <= this.maxSize && this.allowedConfigs.contains(object.getConfig())) {
                        int n = this.strategy.getSize((Bitmap)object);
                        this.strategy.put((Bitmap)object);
                        this.tracker.add((Bitmap)object);
                        ++this.puts;
                        this.currentSize += (long)n;
                        if (Log.isLoggable((String)TAG, (int)2)) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Put bitmap in pool=");
                            stringBuilder.append(this.strategy.logBitmap((Bitmap)object));
                            Log.v((String)TAG, (String)stringBuilder.toString());
                        }
                        this.dump();
                        this.evict();
                        return;
                    }
                    if (Log.isLoggable((String)TAG, (int)2)) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Reject bitmap from pool, bitmap: ");
                        stringBuilder.append(this.strategy.logBitmap((Bitmap)object));
                        stringBuilder.append(", is mutable: ");
                        stringBuilder.append(object.isMutable());
                        stringBuilder.append(", is allowed config: ");
                        stringBuilder.append(this.allowedConfigs.contains(object.getConfig()));
                        Log.v((String)TAG, (String)stringBuilder.toString());
                    }
                    object.recycle();
                    return;
                }
                catch (Throwable throwable2) {}
            } else {
                object = new NullPointerException("Bitmap must not be null");
                throw object;
            }
            throw throwable2;
        }
    }

    @Override
    public void setSizeMultiplier(float f) {
        synchronized (this) {
            this.maxSize = Math.round((float)this.initialMaxSize * f);
            this.evict();
            return;
        }
    }

    @Override
    public void trimMemory(int n) {
        if (Log.isLoggable((String)TAG, (int)3)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("trimMemory, level=");
            stringBuilder.append(n);
            Log.d((String)TAG, (String)stringBuilder.toString());
        }
        if (n < 40 && (Build.VERSION.SDK_INT < 23 || n < 20)) {
            if (n >= 20 || n == 15) {
                this.trimToSize(this.getMaxSize() / 2L);
            }
        } else {
            this.clearMemory();
        }
    }

    private static interface BitmapTracker {
        public void add(Bitmap var1);

        public void remove(Bitmap var1);
    }

    private static final class NullBitmapTracker
    implements BitmapTracker {
        NullBitmapTracker() {
        }

        @Override
        public void add(Bitmap bitmap) {
        }

        @Override
        public void remove(Bitmap bitmap) {
        }
    }

    private static class ThrowingBitmapTracker
    implements BitmapTracker {
        private final Set<Bitmap> bitmaps = Collections.synchronizedSet(new HashSet());

        private ThrowingBitmapTracker() {
        }

        @Override
        public void add(Bitmap bitmap) {
            if (!this.bitmaps.contains(bitmap)) {
                this.bitmaps.add(bitmap);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't add already added bitmap: ");
            stringBuilder.append(bitmap);
            stringBuilder.append(" [");
            stringBuilder.append(bitmap.getWidth());
            stringBuilder.append("x");
            stringBuilder.append(bitmap.getHeight());
            stringBuilder.append("]");
            throw new IllegalStateException(stringBuilder.toString());
        }

        @Override
        public void remove(Bitmap bitmap) {
            if (this.bitmaps.contains(bitmap)) {
                this.bitmaps.remove(bitmap);
                return;
            }
            throw new IllegalStateException("Cannot remove bitmap not in tracker");
        }
    }
}

