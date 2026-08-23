/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.gif;

import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;

public class GifDrawableEncoder
implements ResourceEncoder<GifDrawable> {
    private static final String TAG = "GifEncoder";

    @Override
    public boolean encode(Resource<GifDrawable> object, File file, Options options) {
        boolean bl;
        block2: {
            object = object.get();
            boolean bl2 = false;
            try {
                ByteBufferUtil.toFile(((GifDrawable)object).getBuffer(), file);
                bl = true;
            }
            catch (IOException iOException) {
                bl = bl2;
                if (!Log.isLoggable((String)TAG, (int)5)) break block2;
                Log.w((String)TAG, (String)"Failed to encode GIF drawable data", (Throwable)iOException);
                bl = bl2;
            }
        }
        return bl;
    }

    @Override
    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.SOURCE;
    }
}

