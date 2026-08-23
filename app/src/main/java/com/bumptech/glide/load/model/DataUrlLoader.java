/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Base64
 */
package com.bumptech.glide.load.model;

import android.util.Base64;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class DataUrlLoader<Model, Data>
implements ModelLoader<Model, Data> {
    private static final String BASE64_TAG = ";base64";
    private static final String DATA_SCHEME_IMAGE = "data:image";
    private final DataDecoder<Data> dataDecoder;

    public DataUrlLoader(DataDecoder<Data> dataDecoder) {
        this.dataDecoder = dataDecoder;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(Model Model2, int n, int n2, Options options) {
        return new ModelLoader.LoadData<Data>(new ObjectKey(Model2), new DataUriFetcher<Data>(Model2.toString(), this.dataDecoder));
    }

    @Override
    public boolean handles(Model Model2) {
        return Model2.toString().startsWith(DATA_SCHEME_IMAGE);
    }

    public static interface DataDecoder<Data> {
        public void close(Data var1) throws IOException;

        public Data decode(String var1) throws IllegalArgumentException;

        public Class<Data> getDataClass();
    }

    private static final class DataUriFetcher<Data>
    implements DataFetcher<Data> {
        private Data data;
        private final String dataUri;
        private final DataDecoder<Data> reader;

        DataUriFetcher(String string2, DataDecoder<Data> dataDecoder) {
            this.dataUri = string2;
            this.reader = dataDecoder;
        }

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
            try {
                this.reader.close(this.data);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }

        @Override
        public Class<Data> getDataClass() {
            return this.reader.getDataClass();
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Data> dataCallback) {
            try {
                priority = this.reader.decode(this.dataUri);
                this.data = priority;
                dataCallback.onDataReady((Data)((Object)priority));
            }
            catch (IllegalArgumentException illegalArgumentException) {
                dataCallback.onLoadFailed(illegalArgumentException);
            }
        }
    }

    public static final class StreamFactory<Model>
    implements ModelLoaderFactory<Model, InputStream> {
        private final DataDecoder<InputStream> opener = new DataDecoder<InputStream>(this){
            final StreamFactory this$0;
            {
                this.this$0 = streamFactory;
            }

            @Override
            public void close(InputStream inputStream) throws IOException {
                inputStream.close();
            }

            @Override
            public InputStream decode(String string2) {
                if (string2.startsWith(DataUrlLoader.DATA_SCHEME_IMAGE)) {
                    int n = string2.indexOf(44);
                    if (n != -1) {
                        if (string2.substring(0, n).endsWith(DataUrlLoader.BASE64_TAG)) {
                            return new ByteArrayInputStream(Base64.decode((String)string2.substring(n + 1), (int)0));
                        }
                        throw new IllegalArgumentException("Not a base64 image data URL.");
                    }
                    throw new IllegalArgumentException("Missing comma in data URL.");
                }
                throw new IllegalArgumentException("Not a valid image data URL.");
            }

            @Override
            public Class<InputStream> getDataClass() {
                return InputStream.class;
            }
        };

        @Override
        public ModelLoader<Model, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new DataUrlLoader(this.opener);
        }

        @Override
        public void teardown() {
        }
    }
}

