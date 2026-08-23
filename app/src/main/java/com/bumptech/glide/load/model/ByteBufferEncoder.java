/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferEncoder
implements Encoder<ByteBuffer> {
    private static final String TAG = "ByteBufferEncoder";

    @Override
    public boolean encode(ByteBuffer byteBuffer, File file, Options options) {
        boolean bl;
        block2: {
            boolean bl2 = false;
            try {
                ByteBufferUtil.toFile(byteBuffer, file);
                bl = true;
            }
            catch (IOException iOException) {
                bl = bl2;
                if (!Log.isLoggable((String)TAG, (int)3)) break block2;
                Log.d((String)TAG, (String)"Failed to write data", (Throwable)iOException);
                bl = bl2;
            }
        }
        return bl;
    }
}

