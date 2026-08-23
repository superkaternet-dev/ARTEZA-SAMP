/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.resource.transcode;

import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.UnitTranscoder;
import java.util.ArrayList;
import java.util.List;

public class TranscoderRegistry {
    private final List<Entry<?, ?>> transcoders = new ArrayList();

    public <Z, R> ResourceTranscoder<Z, R> get(Class<Z> resourceTranscoder, Class<R> clazz) {
        synchronized (this) {
            block6: {
                if (!clazz.isAssignableFrom((Class<?>)((Object)resourceTranscoder))) break block6;
                resourceTranscoder = UnitTranscoder.get();
                return resourceTranscoder;
            }
            try {
                for (Entry<R, R> entry2 : this.transcoders) {
                    if (!entry2.handles((Class<?>)((Object)resourceTranscoder), clazz)) continue;
                    resourceTranscoder = entry2.transcoder;
                    return resourceTranscoder;
                }
            }
            catch (Throwable throwable) {}
            {
                throw throwable;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("No transcoder registered to transcode from ");
            stringBuilder.append(resourceTranscoder);
            stringBuilder.append(" to ");
            stringBuilder.append(clazz);
            Object object = new IllegalArgumentException(stringBuilder.toString());
            throw object;
        }
    }

    public <Z, R> List<Class<R>> getTranscodeClasses(Class<Z> clazz, Class<R> clazz2) {
        synchronized (this) {
            ArrayList<Class<R>> arrayList;
            block5: {
                arrayList = new ArrayList<Class<R>>();
                if (!clazz2.isAssignableFrom(clazz)) break block5;
                arrayList.add(clazz2);
                return arrayList;
            }
            try {
                for (Entry<Z, Z> entry : this.transcoders) {
                    if (!entry.handles(clazz, clazz2) || arrayList.contains(entry.toClass)) continue;
                    arrayList.add(entry.toClass);
                }
                return arrayList;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public <Z, R> void register(Class<Z> clazz, Class<R> clazz2, ResourceTranscoder<Z, R> resourceTranscoder) {
        synchronized (this) {
            List<Entry<?, ?>> list = this.transcoders;
            Entry<Z, R> entry = new Entry<Z, R>(clazz, clazz2, resourceTranscoder);
            list.add(entry);
            return;
        }
    }

    private static final class Entry<Z, R> {
        final Class<Z> fromClass;
        final Class<R> toClass;
        final ResourceTranscoder<Z, R> transcoder;

        Entry(Class<Z> clazz, Class<R> clazz2, ResourceTranscoder<Z, R> resourceTranscoder) {
            this.fromClass = clazz;
            this.toClass = clazz2;
            this.transcoder = resourceTranscoder;
        }

        public boolean handles(Class<?> clazz, Class<?> clazz2) {
            boolean bl = this.fromClass.isAssignableFrom(clazz) && clazz2.isAssignableFrom(this.toClass);
            return bl;
        }
    }
}

