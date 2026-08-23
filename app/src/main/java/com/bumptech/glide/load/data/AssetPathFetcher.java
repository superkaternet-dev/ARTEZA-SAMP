/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetManager
 *  android.util.Log
 */
package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import java.io.IOException;

public abstract class AssetPathFetcher<T>
implements DataFetcher<T> {
    private static final String TAG = "AssetPathFetcher";
    private final AssetManager assetManager;
    private final String assetPath;
    private T data;

    public AssetPathFetcher(AssetManager assetManager, String string2) {
        this.assetManager = assetManager;
        this.assetPath = string2;
    }

    @Override
    public void cancel() {
    }

    @Override
    public void cleanup() {
        T t = this.data;
        if (t == null) {
            return;
        }
        try {
            this.close(t);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    protected abstract void close(T var1) throws IOException;

    @Override
    public DataSource getDataSource() {
        return DataSource.LOCAL;
    }

    @Override
    public void loadData(Priority priority, DataFetcher.DataCallback<? super T> dataCallback) {
        try {
            priority = this.loadResource(this.assetManager, this.assetPath);
            this.data = priority;
            dataCallback.onDataReady(priority);
        }
        catch (IOException iOException) {
            if (Log.isLoggable((String)TAG, (int)3)) {
                Log.d((String)TAG, (String)"Failed to load data from asset manager", (Throwable)iOException);
            }
            dataCallback.onLoadFailed(iOException);
        }
    }

    protected abstract T loadResource(AssetManager var1, String var2) throws IOException;
}

