/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteArrayLoader<Data>
implements ModelLoader<byte[], Data> {
    private final Converter<Data> converter;

    public ByteArrayLoader(Converter<Data> converter) {
        this.converter = converter;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(byte[] byArray, int n, int n2, Options options) {
        return new ModelLoader.LoadData<Data>(new ObjectKey(byArray), new Fetcher<Data>(byArray, this.converter));
    }

    @Override
    public boolean handles(byte[] byArray) {
        return true;
    }

    public static class ByteBufferFactory
    implements ModelLoaderFactory<byte[], ByteBuffer> {
        @Override
        public ModelLoader<byte[], ByteBuffer> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader<ByteBuffer>(new Converter<ByteBuffer>(this){
                final ByteBufferFactory this$0;
                {
                    this.this$0 = byteBufferFactory;
                }

                @Override
                public ByteBuffer convert(byte[] byArray) {
                    return ByteBuffer.wrap(byArray);
                }

                @Override
                public Class<ByteBuffer> getDataClass() {
                    return ByteBuffer.class;
                }
            });
        }

        @Override
        public void teardown() {
        }
    }

    public static interface Converter<Data> {
        public Data convert(byte[] var1);

        public Class<Data> getDataClass();
    }

    private static class Fetcher<Data>
    implements DataFetcher<Data> {
        private final Converter<Data> converter;
        private final byte[] model;

        Fetcher(byte[] byArray, Converter<Data> converter) {
            this.model = byArray;
            this.converter = converter;
        }

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
        }

        @Override
        public Class<Data> getDataClass() {
            return this.converter.getDataClass();
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Data> dataCallback) {
            dataCallback.onDataReady(this.converter.convert(this.model));
        }
    }

    public static class StreamFactory
    implements ModelLoaderFactory<byte[], InputStream> {
        @Override
        public ModelLoader<byte[], InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteArrayLoader<InputStream>(new Converter<InputStream>(this){
                final StreamFactory this$0;
                {
                    this.this$0 = streamFactory;
                }

                @Override
                public InputStream convert(byte[] byArray) {
                    return new ByteArrayInputStream(byArray);
                }

                @Override
                public Class<InputStream> getDataClass() {
                    return InputStream.class;
                }
            });
        }

        @Override
        public void teardown() {
        }
    }
}

