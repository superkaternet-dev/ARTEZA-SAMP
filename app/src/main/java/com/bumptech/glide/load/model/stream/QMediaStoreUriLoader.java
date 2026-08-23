/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.Cursor
 *  android.net.Uri
 *  android.os.Build$VERSION
 *  android.os.Environment
 *  android.os.ParcelFileDescriptor
 *  android.provider.MediaStore
 *  android.text.TextUtils
 */
package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class QMediaStoreUriLoader<DataT>
implements ModelLoader<Uri, DataT> {
    private final Context context;
    private final Class<DataT> dataClass;
    private final ModelLoader<File, DataT> fileDelegate;
    private final ModelLoader<Uri, DataT> uriDelegate;

    QMediaStoreUriLoader(Context context, ModelLoader<File, DataT> modelLoader, ModelLoader<Uri, DataT> modelLoader2, Class<DataT> clazz) {
        this.context = context.getApplicationContext();
        this.fileDelegate = modelLoader;
        this.uriDelegate = modelLoader2;
        this.dataClass = clazz;
    }

    @Override
    public ModelLoader.LoadData<DataT> buildLoadData(Uri uri, int n, int n2, Options options) {
        return new ModelLoader.LoadData<DataT>(new ObjectKey(uri), new QMediaStoreUriFetcher<DataT>(this.context, this.fileDelegate, this.uriDelegate, uri, n, n2, options, this.dataClass));
    }

    @Override
    public boolean handles(Uri uri) {
        boolean bl = Build.VERSION.SDK_INT >= 29 && MediaStoreUtil.isMediaStoreUri(uri);
        return bl;
    }

    private static abstract class Factory<DataT>
    implements ModelLoaderFactory<Uri, DataT> {
        private final Context context;
        private final Class<DataT> dataClass;

        Factory(Context context, Class<DataT> clazz) {
            this.context = context;
            this.dataClass = clazz;
        }

        @Override
        public final ModelLoader<Uri, DataT> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new QMediaStoreUriLoader<DataT>(this.context, multiModelLoaderFactory.build(File.class, this.dataClass), multiModelLoaderFactory.build(Uri.class, this.dataClass), this.dataClass);
        }

        @Override
        public final void teardown() {
        }
    }

    public static final class FileDescriptorFactory
    extends Factory<ParcelFileDescriptor> {
        public FileDescriptorFactory(Context context) {
            super(context, ParcelFileDescriptor.class);
        }
    }

    public static final class InputStreamFactory
    extends Factory<InputStream> {
        public InputStreamFactory(Context context) {
            super(context, InputStream.class);
        }
    }

    private static final class QMediaStoreUriFetcher<DataT>
    implements DataFetcher<DataT> {
        private static final String[] PROJECTION = new String[]{"_data"};
        private final Context context;
        private final Class<DataT> dataClass;
        private volatile DataFetcher<DataT> delegate;
        private final ModelLoader<File, DataT> fileDelegate;
        private final int height;
        private volatile boolean isCancelled;
        private final Options options;
        private final Uri uri;
        private final ModelLoader<Uri, DataT> uriDelegate;
        private final int width;

        QMediaStoreUriFetcher(Context context, ModelLoader<File, DataT> modelLoader, ModelLoader<Uri, DataT> modelLoader2, Uri uri, int n, int n2, Options options, Class<DataT> clazz) {
            this.context = context.getApplicationContext();
            this.fileDelegate = modelLoader;
            this.uriDelegate = modelLoader2;
            this.uri = uri;
            this.width = n;
            this.height = n2;
            this.options = options;
            this.dataClass = clazz;
        }

        private ModelLoader.LoadData<DataT> buildDelegateData() throws FileNotFoundException {
            if (Environment.isExternalStorageLegacy()) {
                return this.fileDelegate.buildLoadData(this.queryForFilePath(this.uri), this.width, this.height, this.options);
            }
            Uri uri = this.isAccessMediaLocationGranted() ? MediaStore.setRequireOriginal((Uri)this.uri) : this.uri;
            return this.uriDelegate.buildLoadData(uri, this.width, this.height, this.options);
        }

        private DataFetcher<DataT> buildDelegateFetcher() throws FileNotFoundException {
            Object object = this.buildDelegateData();
            object = object != null ? ((ModelLoader.LoadData)object).fetcher : null;
            return object;
        }

        private boolean isAccessMediaLocationGranted() {
            boolean bl = this.context.checkSelfPermission("android.permission.ACCESS_MEDIA_LOCATION") == 0;
            return bl;
        }

        private File queryForFilePath(Uri object) throws FileNotFoundException {
            Cursor cursor;
            Cursor cursor2;
            block21: {
                Object object2;
                block22: {
                    block23: {
                        cursor2 = null;
                        try {
                            cursor = this.context.getContentResolver().query(object, PROJECTION, null, null, null);
                            if (cursor == null) break block21;
                            cursor2 = cursor;
                        }
                        catch (Throwable throwable) {
                            if (cursor2 != null) {
                                cursor2.close();
                            }
                            throw throwable;
                        }
                        if (!cursor.moveToFirst()) break block21;
                        cursor2 = cursor;
                        object2 = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                        cursor2 = cursor;
                        if (TextUtils.isEmpty((CharSequence)object2)) break block22;
                        cursor2 = cursor;
                        object = new File((String)object2);
                        if (cursor == null) break block23;
                        cursor.close();
                    }
                    return object;
                }
                cursor2 = cursor;
                cursor2 = cursor;
                cursor2 = cursor;
                StringBuilder stringBuilder = new StringBuilder();
                cursor2 = cursor;
                stringBuilder.append("File path was empty in media store for: ");
                cursor2 = cursor;
                stringBuilder.append(object);
                cursor2 = cursor;
                object2 = new FileNotFoundException(stringBuilder.toString());
                cursor2 = cursor;
                throw object2;
            }
            cursor2 = cursor;
            cursor2 = cursor;
            cursor2 = cursor;
            StringBuilder stringBuilder = new StringBuilder();
            cursor2 = cursor;
            stringBuilder.append("Failed to media store entry for: ");
            cursor2 = cursor;
            stringBuilder.append(object);
            cursor2 = cursor;
            FileNotFoundException fileNotFoundException = new FileNotFoundException(stringBuilder.toString());
            cursor2 = cursor;
            throw fileNotFoundException;
        }

        @Override
        public void cancel() {
            this.isCancelled = true;
            DataFetcher<DataT> dataFetcher = this.delegate;
            if (dataFetcher != null) {
                dataFetcher.cancel();
            }
        }

        @Override
        public void cleanup() {
            DataFetcher<DataT> dataFetcher = this.delegate;
            if (dataFetcher != null) {
                dataFetcher.cleanup();
            }
        }

        @Override
        public Class<DataT> getDataClass() {
            return this.dataClass;
        }

        @Override
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        /*
         * WARNING - void declaration
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void loadData(Priority object, DataFetcher.DataCallback<? super DataT> dataCallback) {
            void var2_4;
            try {
                Object object2 = this.buildDelegateFetcher();
                if (object2 == null) {
                    object2 = new StringBuilder();
                    ((StringBuilder)object2).append("Failed to build fetcher for: ");
                    ((StringBuilder)object2).append(this.uri);
                    IllegalArgumentException illegalArgumentException = new IllegalArgumentException(((StringBuilder)object2).toString());
                    var2_4.onLoadFailed(illegalArgumentException);
                    return;
                }
                this.delegate = object2;
                if (this.isCancelled) {
                    this.cancel();
                    return;
                }
                object2.loadData((Priority)((Object)object), var2_4);
                return;
            }
            catch (FileNotFoundException fileNotFoundException) {
                var2_4.onLoadFailed(fileNotFoundException);
            }
        }
    }
}

