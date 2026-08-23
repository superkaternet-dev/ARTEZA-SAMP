/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.ParcelFileDescriptor
 *  android.util.Log
 */
package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileLoader<Data>
implements ModelLoader<File, Data> {
    private static final String TAG = "FileLoader";
    private final FileOpener<Data> fileOpener;

    public FileLoader(FileOpener<Data> fileOpener) {
        this.fileOpener = fileOpener;
    }

    @Override
    public ModelLoader.LoadData<Data> buildLoadData(File file, int n, int n2, Options options) {
        return new ModelLoader.LoadData<Data>(new ObjectKey(file), new FileFetcher<Data>(file, this.fileOpener));
    }

    @Override
    public boolean handles(File file) {
        return true;
    }

    public static class Factory<Data>
    implements ModelLoaderFactory<File, Data> {
        private final FileOpener<Data> opener;

        public Factory(FileOpener<Data> fileOpener) {
            this.opener = fileOpener;
        }

        @Override
        public final ModelLoader<File, Data> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new FileLoader<Data>(this.opener);
        }

        @Override
        public final void teardown() {
        }
    }

    public static class FileDescriptorFactory
    extends Factory<ParcelFileDescriptor> {
        public FileDescriptorFactory() {
            super(new FileOpener<ParcelFileDescriptor>(){

                @Override
                public void close(ParcelFileDescriptor parcelFileDescriptor) throws IOException {
                    parcelFileDescriptor.close();
                }

                @Override
                public Class<ParcelFileDescriptor> getDataClass() {
                    return ParcelFileDescriptor.class;
                }

                @Override
                public ParcelFileDescriptor open(File file) throws FileNotFoundException {
                    return ParcelFileDescriptor.open((File)file, (int)0x10000000);
                }
            });
        }
    }

    private static final class FileFetcher<Data>
    implements DataFetcher<Data> {
        private Data data;
        private final File file;
        private final FileOpener<Data> opener;

        FileFetcher(File file, FileOpener<Data> fileOpener) {
            this.file = file;
            this.opener = fileOpener;
        }

        @Override
        public void cancel() {
        }

        @Override
        public void cleanup() {
            Data Data = this.data;
            if (Data != null) {
                try {
                    this.opener.close(Data);
                }
                catch (IOException iOException) {
                    // empty catch block
                }
            }
        }

        @Override
        public Class<Data> getDataClass() {
            return this.opener.getDataClass();
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Data> dataCallback) {
            try {
                priority = this.opener.open(this.file);
                this.data = priority;
                dataCallback.onDataReady((Data)((Object)priority));
            }
            catch (FileNotFoundException fileNotFoundException) {
                if (Log.isLoggable((String)FileLoader.TAG, (int)3)) {
                    Log.d((String)FileLoader.TAG, (String)"Failed to open file", (Throwable)fileNotFoundException);
                }
                dataCallback.onLoadFailed(fileNotFoundException);
            }
        }
    }

    public static interface FileOpener<Data> {
        public void close(Data var1) throws IOException;

        public Class<Data> getDataClass();

        public Data open(File var1) throws FileNotFoundException;
    }

    public static class StreamFactory
    extends Factory<InputStream> {
        public StreamFactory() {
            super(new FileOpener<InputStream>(){

                @Override
                public void close(InputStream inputStream) throws IOException {
                    inputStream.close();
                }

                @Override
                public Class<InputStream> getDataClass() {
                    return InputStream.class;
                }

                @Override
                public InputStream open(File file) throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        }
    }
}

