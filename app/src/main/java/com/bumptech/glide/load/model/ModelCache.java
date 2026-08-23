/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.model;

import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.util.Queue;

public class ModelCache<A, B> {
    private static final int DEFAULT_SIZE = 250;
    private final LruCache<ModelKey<A>, B> cache;

    public ModelCache() {
        this(250L);
    }

    public ModelCache(long l) {
        this.cache = new LruCache<ModelKey<A>, B>(this, l){
            final ModelCache this$0;
            {
                this.this$0 = modelCache;
                super(l);
            }

            @Override
            protected void onItemEvicted(ModelKey<A> modelKey, B b) {
                modelKey.release();
            }
        };
    }

    public void clear() {
        this.cache.clearMemory();
    }

    public B get(A object, int n, int n2) {
        object = ModelKey.get(object, n, n2);
        B b = this.cache.get((ModelKey<A>)object);
        ((ModelKey)object).release();
        return b;
    }

    public void put(A object, int n, int n2, B b) {
        object = ModelKey.get(object, n, n2);
        this.cache.put((ModelKey<A>)object, b);
    }

    static final class ModelKey<A> {
        private static final Queue<ModelKey<?>> KEY_QUEUE = Util.createQueue(0);
        private int height;
        private A model;
        private int width;

        private ModelKey() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        static <A> ModelKey<A> get(A a, int n, int n2) {
            ModelKey<?> modelKey;
            ModelKey<A> modelKey2 = KEY_QUEUE;
            synchronized (modelKey2) {
                modelKey = modelKey2.poll();
            }
            modelKey2 = modelKey;
            if (modelKey == null) {
                modelKey2 = new ModelKey<A>();
            }
            super.init(a, n, n2);
            return modelKey2;
        }

        private void init(A a, int n, int n2) {
            this.model = a;
            this.width = n;
            this.height = n2;
        }

        public boolean equals(Object object) {
            boolean bl = object instanceof ModelKey;
            boolean bl2 = false;
            if (bl) {
                object = (ModelKey)object;
                bl = bl2;
                if (this.width == ((ModelKey)object).width) {
                    bl = bl2;
                    if (this.height == ((ModelKey)object).height) {
                        bl = bl2;
                        if (this.model.equals(((ModelKey)object).model)) {
                            bl = true;
                        }
                    }
                }
                return bl;
            }
            return false;
        }

        public int hashCode() {
            return (this.height * 31 + this.width) * 31 + this.model.hashCode();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void release() {
            Queue<ModelKey<?>> queue = KEY_QUEUE;
            synchronized (queue) {
                queue.offer(this);
                return;
            }
        }
    }
}

