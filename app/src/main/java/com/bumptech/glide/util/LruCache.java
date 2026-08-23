/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<T, Y> {
    private final Map<T, Entry<Y>> cache = new LinkedHashMap<T, Entry<Y>>(100, 0.75f, true);
    private long currentSize;
    private final long initialMaxSize;
    private long maxSize;

    public LruCache(long l) {
        this.initialMaxSize = l;
        this.maxSize = l;
    }

    private void evict() {
        this.trimToSize(this.maxSize);
    }

    public void clearMemory() {
        this.trimToSize(0L);
    }

    public boolean contains(T t) {
        synchronized (this) {
            boolean bl = this.cache.containsKey(t);
            return bl;
        }
    }

    public Y get(T object) {
        synchronized (this) {
            block6: {
                block5: {
                    object = this.cache.get(object);
                    if (object == null) break block5;
                    object = ((Entry)object).value;
                    break block6;
                }
                object = null;
            }
            return (Y)object;
            finally {
            }
        }
    }

    protected int getCount() {
        synchronized (this) {
            int n = this.cache.size();
            return n;
        }
    }

    public long getCurrentSize() {
        synchronized (this) {
            long l = this.currentSize;
            return l;
        }
    }

    public long getMaxSize() {
        synchronized (this) {
            long l = this.maxSize;
            return l;
        }
    }

    protected int getSize(Y y) {
        return 1;
    }

    protected void onItemEvicted(T t, Y y) {
    }

    /*
     * WARNING - void declaration
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Y put(T object, Y y) {
        synchronized (this) {
            void var1_4;
            void var2_5;
            int n = this.getSize(var2_5);
            long l = n;
            long l2 = this.maxSize;
            Object var9_9 = null;
            if (l >= l2) {
                this.onItemEvicted(object, var2_5);
                return null;
            }
            if (var2_5 != null) {
                this.currentSize += (long)n;
            }
            Map<T, Entry<Y>> map = this.cache;
            Entry<void> entry = var2_5 == null ? null : new Entry<void>(var2_5, n);
            if ((entry = map.put(object, entry)) != null) {
                this.currentSize -= (long)entry.size;
                if (!entry.value.equals(var2_5)) {
                    this.onItemEvicted(object, entry.value);
                }
            }
            this.evict();
            Object var1_2 = var9_9;
            if (entry != null) {
                Object y2 = entry.value;
            }
            return var1_4;
        }
    }

    public Y remove(T object) {
        synchronized (this) {
            block4: {
                object = this.cache.remove(object);
                if (object != null) break block4;
                return null;
            }
            this.currentSize -= (long)((Entry)object).size;
            object = ((Entry)object).value;
            return (Y)object;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setSizeMultiplier(float f) {
        synchronized (this) {
            Throwable throwable2;
            if (!(f < 0.0f)) {
                try {
                    this.maxSize = Math.round((float)this.initialMaxSize * f);
                    this.evict();
                    return;
                }
                catch (Throwable throwable2) {}
            } else {
                IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Multiplier must be >= 0");
                throw illegalArgumentException;
            }
            throw throwable2;
        }
    }

    protected void trimToSize(long l) {
        synchronized (this) {
            try {
                while (this.currentSize > l) {
                    Iterator<Map.Entry<T, Entry<Y>>> iterator2 = this.cache.entrySet().iterator();
                    Map.Entry<Object, Entry<Object>> entry = iterator2.next();
                    Entry<Y> entry2 = entry.getValue();
                    this.currentSize -= (long)entry2.size;
                    entry = entry.getKey();
                    iterator2.remove();
                    this.onItemEvicted(entry, entry2.value);
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    static final class Entry<Y> {
        final int size;
        final Y value;

        Entry(Y y, int n) {
            this.value = y;
            this.size = n;
        }
    }
}

