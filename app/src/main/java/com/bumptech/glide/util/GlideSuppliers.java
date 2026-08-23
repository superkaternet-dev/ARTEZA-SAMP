/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.util;

import com.bumptech.glide.util.Preconditions;

public final class GlideSuppliers {
    private GlideSuppliers() {
    }

    public static <T> GlideSupplier<T> memorize(GlideSupplier<T> glideSupplier) {
        return new GlideSupplier<T>(glideSupplier){
            private volatile T instance;
            final GlideSupplier val$supplier;
            {
                this.val$supplier = glideSupplier;
            }

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public T get() {
                if (this.instance != null) return this.instance;
                synchronized (this) {
                    if (this.instance != null) return this.instance;
                    this.instance = Preconditions.checkNotNull(this.val$supplier.get());
                    return this.instance;
                }
            }
        };
    }

    public static interface GlideSupplier<T> {
        public T get();
    }
}

