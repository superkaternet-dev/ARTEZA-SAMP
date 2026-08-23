/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.ContentResolver
 *  android.net.Uri
 *  android.util.Log
 */
package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class LocalUriFetcher<T>
implements DataFetcher<T> {
    private static final String TAG = "LocalUriFetcher";
    private final ContentResolver contentResolver;
    private T data;
    private final Uri uri;

    public LocalUriFetcher(ContentResolver contentResolver, Uri uri) {
        this.contentResolver = contentResolver;
        this.uri = uri;
    }

    @Override
    public void cancel() {
    }

    @Override
    public void cleanup() {
        T t = this.data;
        if (t != null) {
            try {
                this.close(t);
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
    }

    protected abstract void close(T var1) throws IOException;

    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

    @Override
    public final void loadData(Priority priority, DataFetcher.DataCallback<? super T> dataCallback) {
        try {
            priority = this.loadResource(this.uri, this.contentResolver);
            this.data = priority;
            dataCallback.onDataReady(priority);
        }
        catch (FileNotFoundException fileNotFoundException) {
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"Failed to open Uri", (Throwable)fileNotFoundException);
            }
            dataCallback.onLoadFailed(fileNotFoundException);
        }
    }

    protected abstract T loadResource(Uri var1, ContentResolver var2) throws FileNotFoundException;
}

