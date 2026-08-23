/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferFileLoader
implements ModelLoader<File, ByteBuffer> {
    private static final String TAG = "ByteBufferFileLoader";

    @Override
    public ModelLoader.LoadData<ByteBuffer> buildLoadData(File file, int n, int n2, Options options) {
        return new ModelLoader.LoadData<ByteBuffer>(new ObjectKey(file), new ByteBufferFetcher(file));
    }

    @Override
    public boolean handles(File file) {
        return true;
    }

    private static final class ByteBufferFetcher
    implements DataFetcher<ByteBuffer> {
        private final File file;

        ByteBufferFetcher(File file) {
            this.file = file;
        }

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
        }

        @Override
        public Class<ByteBuffer> getDataClass() {
            return ByteBuffer.class;
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super ByteBuffer> dataCallback) {
            try {
                dataCallback.onDataReady(ByteBufferUtil.fromFile(this.file));
            }
            catch (IOException iOException) {
                if (Log.isLoggable((String)ByteBufferFileLoader.TAG, (int)3)) {
                    Log.d((String)ByteBufferFileLoader.TAG, (String)"Failed to obtain ByteBuffer for file", (Throwable)iOException);
                }
                dataCallback.onLoadFailed(iOException);
            }
        }
    }

    public static class Factory
    implements ModelLoaderFactory<File, ByteBuffer> {
        @Override
        public ModelLoader<File, ByteBuffer> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ByteBufferFileLoader();
        }

        @Override
        public void teardown() {
        }
    }
}

