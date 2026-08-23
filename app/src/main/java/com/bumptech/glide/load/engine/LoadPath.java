/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoadPath<Data, ResourceType, Transcode> {
    private final Class<Data> dataClass;
    private final List<? extends DecodePath<Data, ResourceType, Transcode>> decodePaths;
    private final String failureMessage;
    private final Pools.Pool<List<Throwable>> listPool;

    public LoadPath(Class<Data> clazz, Class<ResourceType> clazz2, Class<Transcode> clazz3, List<DecodePath<Data, ResourceType, Transcode>> object, Pools.Pool<List<Throwable>> pool) {
        this.dataClass = clazz;
        this.listPool = pool;
        this.decodePaths = Preconditions.checkNotEmpty(object);
        object = new StringBuilder();
        ((StringBuilder)object).append("Failed LoadPath{");
        ((StringBuilder)object).append(clazz.getSimpleName());
        ((StringBuilder)object).append("->");
        ((StringBuilder)object).append(clazz2.getSimpleName());
        ((StringBuilder)object).append("->");
        ((StringBuilder)object).append(clazz3.getSimpleName());
        ((StringBuilder)object).append("}");
        this.failureMessage = ((StringBuilder)object).toString();
    }

    private Resource<Transcode> loadWithExceptionList(DataRewinder<Data> object, Options options, int n, int n2, DecodePath.DecodeCallback<ResourceType> decodeCallback, List<Throwable> list) throws GlideException {
        DecodePath<Data, ResourceType, Transcode> decodePath;
        int n3 = this.decodePaths.size();
        int n4 = 0;
        Object object2 = null;
        while (true) {
            decodePath = object2;
            if (n4 >= n3) break;
            decodePath = this.decodePaths.get(n4);
            try {
                decodePath = decodePath.decode((DataRewinder<Data>)object, n, n2, options, decodeCallback);
                object2 = decodePath;
            }
            catch (GlideException glideException) {
                list.add(glideException);
            }
            if (object2 != null) {
                decodePath = object2;
                break;
            }
            ++n4;
        }
        if (decodePath != null) {
            return decodePath;
        }
        object = new GlideException(this.failureMessage, new ArrayList<Throwable>(list));
        throw object;
    }

    public Class<Data> getDataClass() {
        return this.dataClass;
    }

    public Resource<Transcode> load(DataRewinder<Data> object, Options options, int n, int n2, DecodePath.DecodeCallback<ResourceType> decodeCallback) throws GlideException {
        List<Throwable> list = Preconditions.checkNotNull(this.listPool.acquire());
        try {
            object = this.loadWithExceptionList((DataRewinder<Data>)object, options, n, n2, decodeCallback, list);
            return object;
        }
        finally {
            this.listPool.release(list);
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("LoadPath{decodePaths=");
        stringBuilder.append(Arrays.toString(this.decodePaths.toArray()));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}

