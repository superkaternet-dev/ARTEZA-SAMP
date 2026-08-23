/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide;

import androidx.core.util.Pools;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.data.DataRewinderRegistry;
import com.bumptech.glide.load.engine.DecodePath;
import com.bumptech.glide.load.engine.LoadPath;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.ModelLoaderRegistry;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry;
import com.bumptech.glide.provider.EncoderRegistry;
import com.bumptech.glide.provider.ImageHeaderParserRegistry;
import com.bumptech.glide.provider.LoadPathCache;
import com.bumptech.glide.provider.ModelToResourceClassCache;
import com.bumptech.glide.provider.ResourceDecoderRegistry;
import com.bumptech.glide.provider.ResourceEncoderRegistry;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Registry {
    public static final String BUCKET_ANIMATION = "Animation";
    private static final String BUCKET_APPEND_ALL = "legacy_append";
    public static final String BUCKET_BITMAP = "Bitmap";
    public static final String BUCKET_BITMAP_DRAWABLE = "BitmapDrawable";
    @Deprecated
    public static final String BUCKET_GIF = "Animation";
    private static final String BUCKET_PREPEND_ALL = "legacy_prepend_all";
    private final DataRewinderRegistry dataRewinderRegistry;
    private final ResourceDecoderRegistry decoderRegistry;
    private final EncoderRegistry encoderRegistry;
    private final ImageHeaderParserRegistry imageHeaderParserRegistry;
    private final LoadPathCache loadPathCache;
    private final ModelLoaderRegistry modelLoaderRegistry;
    private final ModelToResourceClassCache modelToResourceClassCache = new ModelToResourceClassCache();
    private final ResourceEncoderRegistry resourceEncoderRegistry;
    private final Pools.Pool<List<Throwable>> throwableListPool;
    private final TranscoderRegistry transcoderRegistry;

    public Registry() {
        this.loadPathCache = new LoadPathCache();
        Pools.Pool<List<Throwable>> pool = FactoryPools.threadSafeList();
        this.throwableListPool = pool;
        this.modelLoaderRegistry = new ModelLoaderRegistry(pool);
        this.encoderRegistry = new EncoderRegistry();
        this.decoderRegistry = new ResourceDecoderRegistry();
        this.resourceEncoderRegistry = new ResourceEncoderRegistry();
        this.dataRewinderRegistry = new DataRewinderRegistry();
        this.transcoderRegistry = new TranscoderRegistry();
        this.imageHeaderParserRegistry = new ImageHeaderParserRegistry();
        this.setResourceDecoderBucketPriorityList(Arrays.asList("Animation", BUCKET_BITMAP, BUCKET_BITMAP_DRAWABLE));
    }

    private <Data, TResource, Transcode> List<DecodePath<Data, TResource, Transcode>> getDecodePaths(Class<Data> clazz, Class<TResource> object, Class<Transcode> clazz2) {
        ArrayList<DecodePath<Data, TResource, Transcode>> arrayList = new ArrayList<DecodePath<Data, TResource, Transcode>>();
        for (Class clazz3 : this.decoderRegistry.getResourceClasses(clazz, object)) {
            for (Class<Transcode> clazz4 : this.transcoderRegistry.getTranscodeClasses(clazz3, clazz2)) {
                arrayList.add(new DecodePath(clazz, clazz3, clazz4, this.decoderRegistry.getDecoders(clazz, clazz3), this.transcoderRegistry.get(clazz3, clazz4), this.throwableListPool));
            }
        }
        return arrayList;
    }

    public <Data> Registry append(Class<Data> clazz, Encoder<Data> encoder) {
        this.encoderRegistry.append(clazz, encoder);
        return this;
    }

    public <TResource> Registry append(Class<TResource> clazz, ResourceEncoder<TResource> resourceEncoder) {
        this.resourceEncoderRegistry.append(clazz, resourceEncoder);
        return this;
    }

    public <Data, TResource> Registry append(Class<Data> clazz, Class<TResource> clazz2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.append(BUCKET_APPEND_ALL, clazz, clazz2, resourceDecoder);
        return this;
    }

    public <Model, Data> Registry append(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.modelLoaderRegistry.append(clazz, clazz2, modelLoaderFactory);
        return this;
    }

    public <Data, TResource> Registry append(String string2, Class<Data> clazz, Class<TResource> clazz2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.decoderRegistry.append(string2, resourceDecoder, clazz, clazz2);
        return this;
    }

    public List<ImageHeaderParser> getImageHeaderParsers() {
        List<ImageHeaderParser> list = this.imageHeaderParserRegistry.getParsers();
        if (!list.isEmpty()) {
            return list;
        }
        throw new NoImageHeaderParserException();
    }

    public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> getLoadPath(Class<Data> clazz, Class<TResource> clazz2, Class<Transcode> clazz3) {
        LoadPath<Data, TResource, Transcode> loadPath = this.loadPathCache.get(clazz, clazz2, clazz3);
        if (this.loadPathCache.isEmptyLoadPath(loadPath)) {
            return null;
        }
        Object object = loadPath;
        if (loadPath == null) {
            object = this.getDecodePaths(clazz, clazz2, clazz3);
            object = object.isEmpty() ? null : new LoadPath<Data, TResource, Transcode>(clazz, clazz2, clazz3, object, this.throwableListPool);
            this.loadPathCache.put(clazz, clazz2, clazz3, (LoadPath<?, ?, ?>)object);
        }
        return object;
    }

    public <Model> List<ModelLoader<Model, ?>> getModelLoaders(Model Model2) {
        return this.modelLoaderRegistry.getModelLoaders(Model2);
    }

    public <Model, TResource, Transcode> List<Class<?>> getRegisteredResourceClasses(Class<Model> clazz, Class<TResource> clazz2, Class<Transcode> clazz3) {
        List<Class<?>> list = this.modelToResourceClassCache.get(clazz, clazz2, clazz3);
        List<Class<?>> list2 = list;
        if (list == null) {
            list2 = new ArrayList();
            for (Class clazz4 : this.modelLoaderRegistry.getDataClasses(clazz)) {
                for (Class<TResource> clazz5 : this.decoderRegistry.getResourceClasses(clazz4, clazz2)) {
                    if (this.transcoderRegistry.getTranscodeClasses(clazz5, clazz3).isEmpty() || list2.contains(clazz5)) continue;
                    list2.add(clazz5);
                }
            }
            this.modelToResourceClassCache.put(clazz, clazz2, clazz3, Collections.unmodifiableList(list2));
        }
        return list2;
    }

    public <X> ResourceEncoder<X> getResultEncoder(Resource<X> resource) throws NoResultEncoderAvailableException {
        ResourceEncoder<X> resourceEncoder = this.resourceEncoderRegistry.get(resource.getResourceClass());
        if (resourceEncoder != null) {
            return resourceEncoder;
        }
        throw new NoResultEncoderAvailableException(resource.getResourceClass());
    }

    public <X> DataRewinder<X> getRewinder(X x) {
        return this.dataRewinderRegistry.build(x);
    }

    public <X> Encoder<X> getSourceEncoder(X x) throws NoSourceEncoderAvailableException {
        Encoder<?> encoder = this.encoderRegistry.getEncoder(x.getClass());
        if (encoder != null) {
            return encoder;
        }
        throw new NoSourceEncoderAvailableException(x.getClass());
    }

    public boolean isResourceEncoderAvailable(Resource<?> resource) {
        boolean bl = this.resourceEncoderRegistry.get(resource.getResourceClass()) != null;
        return bl;
    }

    public <Data> Registry prepend(Class<Data> clazz, Encoder<Data> encoder) {
        this.encoderRegistry.prepend(clazz, encoder);
        return this;
    }

    public <TResource> Registry prepend(Class<TResource> clazz, ResourceEncoder<TResource> resourceEncoder) {
        this.resourceEncoderRegistry.prepend(clazz, resourceEncoder);
        return this;
    }

    public <Data, TResource> Registry prepend(Class<Data> clazz, Class<TResource> clazz2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.prepend(BUCKET_PREPEND_ALL, clazz, clazz2, resourceDecoder);
        return this;
    }

    public <Model, Data> Registry prepend(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<Model, Data> modelLoaderFactory) {
        this.modelLoaderRegistry.prepend(clazz, clazz2, modelLoaderFactory);
        return this;
    }

    public <Data, TResource> Registry prepend(String string2, Class<Data> clazz, Class<TResource> clazz2, ResourceDecoder<Data, TResource> resourceDecoder) {
        this.decoderRegistry.prepend(string2, resourceDecoder, clazz, clazz2);
        return this;
    }

    public Registry register(ImageHeaderParser imageHeaderParser) {
        this.imageHeaderParserRegistry.add(imageHeaderParser);
        return this;
    }

    public Registry register(DataRewinder.Factory<?> factory) {
        this.dataRewinderRegistry.register(factory);
        return this;
    }

    @Deprecated
    public <Data> Registry register(Class<Data> clazz, Encoder<Data> encoder) {
        return this.append(clazz, encoder);
    }

    @Deprecated
    public <TResource> Registry register(Class<TResource> clazz, ResourceEncoder<TResource> resourceEncoder) {
        return this.append(clazz, resourceEncoder);
    }

    public <TResource, Transcode> Registry register(Class<TResource> clazz, Class<Transcode> clazz2, ResourceTranscoder<TResource, Transcode> resourceTranscoder) {
        this.transcoderRegistry.register(clazz, clazz2, resourceTranscoder);
        return this;
    }

    public <Model, Data> Registry replace(Class<Model> clazz, Class<Data> clazz2, ModelLoaderFactory<? extends Model, ? extends Data> modelLoaderFactory) {
        this.modelLoaderRegistry.replace(clazz, clazz2, modelLoaderFactory);
        return this;
    }

    public final Registry setResourceDecoderBucketPriorityList(List<String> object) {
        ArrayList<String> arrayList = new ArrayList<String>(object.size());
        arrayList.add(BUCKET_PREPEND_ALL);
        object = object.iterator();
        while (object.hasNext()) {
            arrayList.add((String)object.next());
        }
        arrayList.add(BUCKET_APPEND_ALL);
        this.decoderRegistry.setBucketPriorityList(arrayList);
        return this;
    }

    public static class MissingComponentException
    extends RuntimeException {
        public MissingComponentException(String string2) {
            super(string2);
        }
    }

    public static final class NoImageHeaderParserException
    extends MissingComponentException {
        public NoImageHeaderParserException() {
            super("Failed to find image header parser.");
        }
    }

    public static class NoModelLoaderAvailableException
    extends MissingComponentException {
        public NoModelLoaderAvailableException(Class<?> clazz, Class<?> clazz2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find any ModelLoaders for model: ");
            stringBuilder.append(clazz);
            stringBuilder.append(" and data: ");
            stringBuilder.append(clazz2);
            super(stringBuilder.toString());
        }

        public NoModelLoaderAvailableException(Object object) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find any ModelLoaders registered for model class: ");
            stringBuilder.append(object.getClass());
            super(stringBuilder.toString());
        }

        public <M> NoModelLoaderAvailableException(M m, List<ModelLoader<M, ?>> list) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Found ModelLoaders for model class: ");
            stringBuilder.append(list);
            stringBuilder.append(", but none that handle this specific model instance: ");
            stringBuilder.append(m);
            super(stringBuilder.toString());
        }
    }

    public static class NoResultEncoderAvailableException
    extends MissingComponentException {
        public NoResultEncoderAvailableException(Class<?> clazz) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find result encoder for resource class: ");
            stringBuilder.append(clazz);
            stringBuilder.append(", you may need to consider registering a new Encoder for the requested type or DiskCacheStrategy.DATA/DiskCacheStrategy.NONE if caching your transformed resource is unnecessary.");
            super(stringBuilder.toString());
        }
    }

    public static class NoSourceEncoderAvailableException
    extends MissingComponentException {
        public NoSourceEncoderAvailableException(Class<?> clazz) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find source encoder for data class: ");
            stringBuilder.append(clazz);
            super(stringBuilder.toString());
        }
    }
}

