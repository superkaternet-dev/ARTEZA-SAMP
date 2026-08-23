/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 */
package com.bumptech.glide.load.model.stream;

import android.text.TextUtils;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class BaseGlideUrlLoader<Model>
implements ModelLoader<Model, InputStream> {
    private final ModelLoader<GlideUrl, InputStream> concreteLoader;
    private final ModelCache<Model, GlideUrl> modelCache;

    protected BaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> modelLoader) {
        this(modelLoader, null);
    }

    protected BaseGlideUrlLoader(ModelLoader<GlideUrl, InputStream> modelLoader, ModelCache<Model, GlideUrl> modelCache) {
        this.concreteLoader = modelLoader;
        this.modelCache = modelCache;
    }

    private static List<Key> getAlternateKeys(Collection<String> object) {
        ArrayList<Key> arrayList = new ArrayList<Key>(object.size());
        object = object.iterator();
        while (object.hasNext()) {
            arrayList.add(new GlideUrl((String)object.next()));
        }
        return arrayList;
    }

    @Override
    public ModelLoader.LoadData<InputStream> buildLoadData(Model object, int n, int n2, Options object2) {
        Object object3 = null;
        ModelCache<Model, GlideUrl> modelCache = this.modelCache;
        if (modelCache != null) {
            object3 = modelCache.get(object, n, n2);
        }
        modelCache = object3;
        if (object3 == null) {
            object3 = this.getUrl(object, n, n2, (Options)object2);
            if (TextUtils.isEmpty((CharSequence)object3)) {
                return null;
            }
            object3 = new GlideUrl((String)object3, this.getHeaders(object, n, n2, (Options)object2));
            ModelCache<Model, GlideUrl> modelCache2 = this.modelCache;
            modelCache = object3;
            if (modelCache2 != null) {
                modelCache2.put(object, n, n2, (GlideUrl)object3);
                modelCache = object3;
            }
        }
        object = this.getAlternateUrls(object, n, n2, (Options)object2);
        if ((object2 = this.concreteLoader.buildLoadData((GlideUrl)((Object)modelCache), n, n2, (Options)object2)) != null && !object.isEmpty()) {
            return new ModelLoader.LoadData<InputStream>(((ModelLoader.LoadData)object2).sourceKey, BaseGlideUrlLoader.getAlternateKeys((Collection<String>)object), ((ModelLoader.LoadData)object2).fetcher);
        }
        return object2;
    }

    protected List<String> getAlternateUrls(Model Model2, int n, int n2, Options options) {
        return Collections.emptyList();
    }

    protected Headers getHeaders(Model Model2, int n, int n2, Options options) {
        return Headers.DEFAULT;
    }

    protected abstract String getUrl(Model var1, int var2, int var3, Options var4);
}

