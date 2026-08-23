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
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamEncoder
implements Encoder<InputStream> {
    private static final String TAG = "StreamEncoder";
    private final ArrayPool byteArrayPool;

    public StreamEncoder(ArrayPool arrayPool) {
        this.byteArrayPool = arrayPool;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean encode(InputStream inputStream, File object, Options object2) {
        Throwable throwable222222;
        byte[] byArray;
        block13: {
            boolean bl;
            block14: {
                byArray = this.byteArrayPool.get(65536, byte[].class);
                boolean bl2 = false;
                boolean bl3 = false;
                Object object3 = null;
                Object var10_13 = null;
                object2 = var10_13;
                Object object4 = object3;
                object2 = var10_13;
                object4 = object3;
                FileOutputStream fileOutputStream = new FileOutputStream((File)object);
                object = fileOutputStream;
                while (true) {
                    object2 = object;
                    object4 = object;
                    int n = inputStream.read(byArray);
                    if (n == -1) break;
                    object2 = object;
                    object4 = object;
                    ((OutputStream)object).write(byArray, 0, n);
                }
                object2 = object;
                object4 = object;
                ((OutputStream)object).close();
                bl3 = true;
                bl = true;
                try {
                    ((OutputStream)object).close();
                }
                catch (IOException iOException) {
                    bl = bl3;
                }
                break block14;
                {
                    catch (Throwable throwable222222) {
                        break block13;
                    }
                    catch (IOException iOException) {}
                    object2 = object4;
                    {
                        if (Log.isLoggable((String)TAG, (int)3)) {
                            object2 = object4;
                            Log.d((String)TAG, (String)"Failed to encode data onto the OutputStream", (Throwable)iOException);
                        }
                        bl = bl2;
                        if (object4 == null) break block14;
                    }
                    try {
                        ((OutputStream)object4).close();
                        bl = bl3;
                    }
                    catch (IOException iOException) {
                        bl = bl2;
                    }
                }
            }
            this.byteArrayPool.put(byArray);
            return bl;
        }
        if (object2 != null) {
            try {
                ((OutputStream)object2).close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        this.byteArrayPool.put(byArray);
        throw throwable222222;
    }
}

