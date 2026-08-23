/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.provider;

import com.bumptech.glide.load.ResourceDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResourceDecoderRegistry {
    private final List<String> bucketPriorityList = new ArrayList<String>();
    private final Map<String, List<Entry<?, ?>>> decoders = new HashMap();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private List<Entry<?, ?>> getOrAddEntryList(String string2) {
        synchronized (this) {
            ArrayList arrayList;
            if (!this.bucketPriorityList.contains(string2)) {
                this.bucketPriorityList.add(string2);
            }
            ArrayList arrayList2 = arrayList = this.decoders.get(string2);
            if (arrayList == null) {
                arrayList2 = new ArrayList();
                this.decoders.put(string2, arrayList2);
            }
            return arrayList2;
        }
    }

    public <T, R> void append(String object, ResourceDecoder<T, R> resourceDecoder, Class<T> clazz, Class<R> clazz2) {
        synchronized (this) {
            object = this.getOrAddEntryList((String)object);
            Entry<T, R> entry = new Entry<T, R>(clazz, clazz2, resourceDecoder);
            object.add(entry);
            return;
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public <T, R> List<ResourceDecoder<T, R>> getDecoders(Class<T> clazz, Class<R> clazz2) {
        synchronized (this) {
            try {
                ArrayList<ResourceDecoder<T, R>> arrayList = new ArrayList<ResourceDecoder<T, R>>();
                for (String string2 : this.bucketPriorityList) {
                    List<Entry<?, ?>> list = this.decoders.get(string2);
                    if (list == null) continue;
                    for (Entry<T, T> entry : list) {
                        if (!entry.handles(clazz, clazz2)) continue;
                        arrayList.add(entry.decoder);
                    }
                }
                return arrayList;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    /*
     * Enabled aggressive exception aggregation
     */
    public <T, R> List<Class<R>> getResourceClasses(Class<T> clazz, Class<R> clazz2) {
        synchronized (this) {
            try {
                ArrayList<Class<R>> arrayList = new ArrayList<Class<R>>();
                for (String string2 : this.bucketPriorityList) {
                    List<Entry<?, ?>> list = this.decoders.get(string2);
                    if (list == null) continue;
                    for (Entry<T, T> entry : list) {
                        if (!entry.handles(clazz, clazz2) || arrayList.contains(entry.resourceClass)) continue;
                        arrayList.add(entry.resourceClass);
                    }
                }
                return arrayList;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    public <T, R> void prepend(String entry, ResourceDecoder<T, R> resourceDecoder, Class<T> clazz, Class<R> clazz2) {
        synchronized (this) {
            List<Entry<?, ?>> list = this.getOrAddEntryList((String)((Object)entry));
            entry = new Entry<T, R>(clazz, clazz2, resourceDecoder);
            list.add(0, entry);
            return;
        }
    }

    public void setBucketPriorityList(List<String> list) {
        synchronized (this) {
            try {
                Object object2 = new ArrayList(this.bucketPriorityList);
                this.bucketPriorityList.clear();
                for (String object3 : list) {
                    this.bucketPriorityList.add(object3);
                }
                Iterator iterator2 = object2.iterator();
                while (iterator2.hasNext()) {
                    object2 = (String)iterator2.next();
                    if (list.contains(object2)) continue;
                    this.bucketPriorityList.add((String)object2);
                }
                return;
            }
            catch (Throwable throwable) {
                throw throwable;
            }
        }
    }

    private static class Entry<T, R> {
        private final Class<T> dataClass;
        final ResourceDecoder<T, R> decoder;
        final Class<R> resourceClass;

        public Entry(Class<T> clazz, Class<R> clazz2, ResourceDecoder<T, R> resourceDecoder) {
            this.dataClass = clazz;
            this.resourceClass = clazz2;
            this.decoder = resourceDecoder;
        }

        public boolean handles(Class<?> clazz, Class<?> clazz2) {
            boolean bl = this.dataClass.isAssignableFrom(clazz) && clazz2.isAssignableFrom(this.resourceClass);
            return bl;
        }
    }
}

