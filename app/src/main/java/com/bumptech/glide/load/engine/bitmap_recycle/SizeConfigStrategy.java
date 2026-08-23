/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$Config
 *  android.os.Build$VERSION
 */
package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.os.Build;
import com.bumptech.glide.load.engine.bitmap_recycle.BaseKeyPool;
import com.bumptech.glide.load.engine.bitmap_recycle.GroupedLinkedMap;
import com.bumptech.glide.load.engine.bitmap_recycle.LruPoolStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.Poolable;
import com.bumptech.glide.util.Util;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class SizeConfigStrategy
implements LruPoolStrategy {
    private static final Bitmap.Config[] ALPHA_8_IN_CONFIGS;
    private static final Bitmap.Config[] ARGB_4444_IN_CONFIGS;
    private static final Bitmap.Config[] ARGB_8888_IN_CONFIGS;
    private static final int MAX_SIZE_MULTIPLE = 8;
    private static final Bitmap.Config[] RGBA_F16_IN_CONFIGS;
    private static final Bitmap.Config[] RGB_565_IN_CONFIGS;
    private final GroupedLinkedMap<Key, Bitmap> groupedMap;
    private final KeyPool keyPool = new KeyPool();
    private final Map<Bitmap.Config, NavigableMap<Integer, Integer>> sortedSizes;

    static {
        Bitmap.Config[] configArray = new Bitmap.Config[]{Bitmap.Config.ARGB_8888, null};
        Bitmap.Config[] configArray2 = configArray;
        if (Build.VERSION.SDK_INT >= 26) {
            configArray2 = Arrays.copyOf(configArray, configArray.length + 1);
            configArray2[configArray2.length - 1] = Bitmap.Config.RGBA_F16;
        }
        ARGB_8888_IN_CONFIGS = configArray2;
        RGBA_F16_IN_CONFIGS = configArray2;
        RGB_565_IN_CONFIGS = new Bitmap.Config[]{Bitmap.Config.RGB_565};
        ARGB_4444_IN_CONFIGS = new Bitmap.Config[]{Bitmap.Config.ARGB_4444};
        ALPHA_8_IN_CONFIGS = new Bitmap.Config[]{Bitmap.Config.ALPHA_8};
    }

    public SizeConfigStrategy() {
        this.groupedMap = new GroupedLinkedMap();
        this.sortedSizes = new HashMap<Bitmap.Config, NavigableMap<Integer, Integer>>();
    }

    private void decrementBitmapOfSize(Integer n, Bitmap bitmap) {
        NavigableMap<Integer, Integer> navigableMap = this.getSizesForConfig(bitmap.getConfig());
        Integer n2 = (Integer)navigableMap.get(n);
        if (n2 != null) {
            if (n2 == 1) {
                navigableMap.remove(n);
            } else {
                navigableMap.put(n, n2 - 1);
            }
            return;
        }
        navigableMap = new StringBuilder();
        ((StringBuilder)((Object)navigableMap)).append("Tried to decrement empty size, size: ");
        ((StringBuilder)((Object)navigableMap)).append(n);
        ((StringBuilder)((Object)navigableMap)).append(", removed: ");
        ((StringBuilder)((Object)navigableMap)).append(this.logBitmap(bitmap));
        ((StringBuilder)((Object)navigableMap)).append(", this: ");
        ((StringBuilder)((Object)navigableMap)).append(this);
        throw new NullPointerException(((StringBuilder)((Object)navigableMap)).toString());
    }

    private Key findBestKey(int n, Bitmap.Config config) {
        Key key;
        Key key2 = this.keyPool.get(n, config);
        Bitmap.Config[] configArray = SizeConfigStrategy.getInConfigs(config);
        int n2 = configArray.length;
        int n3 = 0;
        while (true) {
            key = key2;
            if (n3 >= n2) break;
            Bitmap.Config config2 = configArray[n3];
            Integer n4 = this.getSizesForConfig(config2).ceilingKey(n);
            if (n4 != null && n4 <= n * 8) {
                if (n4 == n) {
                    if (config2 == null) {
                        key = key2;
                        if (config == null) break;
                    } else {
                        key = key2;
                        if (config2.equals((Object)config)) break;
                    }
                }
                this.keyPool.offer(key2);
                key = this.keyPool.get(n4, config2);
                break;
            }
            ++n3;
        }
        return key;
    }

    static String getBitmapString(int n, Bitmap.Config config) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append(n);
        stringBuilder.append("](");
        stringBuilder.append(config);
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private static Bitmap.Config[] getInConfigs(Bitmap.Config config) {
        if (Build.VERSION.SDK_INT >= 26 && Bitmap.Config.RGBA_F16.equals((Object)config)) {
            return RGBA_F16_IN_CONFIGS;
        }
        switch (1.$SwitchMap$android$graphics$Bitmap$Config[config.ordinal()]) {
            default: {
                return new Bitmap.Config[]{config};
            }
            case 4: {
                return ALPHA_8_IN_CONFIGS;
            }
            case 3: {
                return ARGB_4444_IN_CONFIGS;
            }
            case 2: {
                return RGB_565_IN_CONFIGS;
            }
            case 1: 
        }
        return ARGB_8888_IN_CONFIGS;
    }

    private NavigableMap<Integer, Integer> getSizesForConfig(Bitmap.Config config) {
        NavigableMap<Integer, Integer> navigableMap;
        NavigableMap<Integer, Integer> navigableMap2 = navigableMap = this.sortedSizes.get(config);
        if (navigableMap == null) {
            navigableMap2 = new TreeMap<Integer, Integer>();
            this.sortedSizes.put(config, navigableMap2);
        }
        return navigableMap2;
    }

    @Override
    public Bitmap get(int n, int n2, Bitmap.Config config) {
        Key key = this.findBestKey(Util.getBitmapByteSize(n, n2, config), config);
        Bitmap bitmap = this.groupedMap.get(key);
        if (bitmap != null) {
            this.decrementBitmapOfSize(key.size, bitmap);
            bitmap.reconfigure(n, n2, config);
        }
        return bitmap;
    }

    @Override
    public int getSize(Bitmap bitmap) {
        return Util.getBitmapByteSize(bitmap);
    }

    @Override
    public String logBitmap(int n, int n2, Bitmap.Config config) {
        return SizeConfigStrategy.getBitmapString(Util.getBitmapByteSize(n, n2, config), config);
    }

    @Override
    public String logBitmap(Bitmap bitmap) {
        return SizeConfigStrategy.getBitmapString(Util.getBitmapByteSize(bitmap), bitmap.getConfig());
    }

    @Override
    public void put(Bitmap object) {
        int n = Util.getBitmapByteSize((Bitmap)object);
        Key key = this.keyPool.get(n, object.getConfig());
        this.groupedMap.put(key, (Bitmap)object);
        NavigableMap<Integer, Integer> navigableMap = this.getSizesForConfig(object.getConfig());
        object = (Integer)navigableMap.get(key.size);
        int n2 = key.size;
        n = 1;
        if (object != null) {
            n = 1 + (Integer)object;
        }
        navigableMap.put(n2, n);
    }

    @Override
    public Bitmap removeLast() {
        Bitmap bitmap = this.groupedMap.removeLast();
        if (bitmap != null) {
            this.decrementBitmapOfSize(Util.getBitmapByteSize(bitmap), bitmap);
        }
        return bitmap;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SizeConfigStrategy{groupedMap=");
        stringBuilder.append(this.groupedMap);
        stringBuilder = stringBuilder.append(", sortedSizes=(");
        for (Map.Entry<Bitmap.Config, NavigableMap<Integer, Integer>> entry : this.sortedSizes.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append('[');
            stringBuilder.append(entry.getValue());
            stringBuilder.append("], ");
        }
        if (!this.sortedSizes.isEmpty()) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        }
        stringBuilder.append(")}");
        return stringBuilder.toString();
    }

    static final class Key
    implements Poolable {
        private Bitmap.Config config;
        private final KeyPool pool;
        int size;

        public Key(KeyPool keyPool) {
            this.pool = keyPool;
        }

        Key(KeyPool keyPool, int n, Bitmap.Config config) {
            this(keyPool);
            this.init(n, config);
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof Key;
            boolean bl2 = false;
            if (bl) {
                object = (Key)object;
                bl = bl2;
                if (this.size == ((Key)object).size) {
                    bl = bl2;
                    if (Util.bothNullOrEqual(this.config, ((Key)object).config)) {
                        bl = true;
                    }
                }
                return bl;
            }
            return false;
        }

        public int hashCode() {
            int n = this.size;
            Bitmap.Config config = this.config;
            int n2 = config != null ? config.hashCode() : 0;
            return n * 31 + n2;
        }

        public void init(int n, Bitmap.Config config) {
            this.size = n;
            this.config = config;
        }

        @Override
        public void offer() {
            this.pool.offer(this);
        }

        public String toString() {
            return SizeConfigStrategy.getBitmapString(this.size, this.config);
        }
    }

    static class KeyPool
    extends BaseKeyPool<Key> {
        KeyPool() {
        }

        @Override
        protected Key create() {
            return new Key(this);
        }

        public Key get(int n, Bitmap.Config config) {
            Key key = (Key)this.get();
            key.init(n, config);
            return key;
        }
    }
}

