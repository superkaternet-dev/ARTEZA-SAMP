/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.Bitmap$CompressFormat
 *  android.util.Log
 */
package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.data.BufferedOutputStream;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Util;
import com.bumptech.glide.util.pool.GlideTrace;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BitmapEncoder
implements ResourceEncoder<Bitmap> {
    public static final Option<Bitmap.CompressFormat> COMPRESSION_FORMAT;
    public static final Option<Integer> COMPRESSION_QUALITY;
    private static final String TAG = "BitmapEncoder";
    private final ArrayPool arrayPool;

    static {
        COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
        COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");
    }

    @Deprecated
    public BitmapEncoder() {
        this.arrayPool = null;
    }

    public BitmapEncoder(ArrayPool arrayPool) {
        this.arrayPool = arrayPool;
    }

    private Bitmap.CompressFormat getFormat(Bitmap bitmap, Options options) {
        if ((options = options.get(COMPRESSION_FORMAT)) != null) {
            return options;
        }
        if (bitmap.hasAlpha()) {
            return Bitmap.CompressFormat.PNG;
        }
        return Bitmap.CompressFormat.JPEG;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean encode(Resource<Bitmap> var1_1, File var2_5, Options var3_8) {
        block15: {
            block17: {
                var14_9 = var1_1.get();
                var15_10 = this.getFormat(var14_9, var3_8);
                GlideTrace.beginSectionFormat("encode: [%dx%d] %s", var14_9.getWidth(), var14_9.getHeight(), var15_10);
                var8_11 = LogTime.getLogTime();
                var4_12 = var3_8.get(BitmapEncoder.COMPRESSION_QUALITY);
                var7_13 = false;
                var5_14 = false;
                var6_15 = false;
                var12_16 = null;
                var13_17 = null;
                var1_1 = var13_17;
                var10_18 = var12_16;
                {
                    catch (Throwable var1_4) {
                        GlideTrace.endSection();
                        throw var1_4;
                    }
                    var1_1 = var13_17;
                    var10_18 = var12_16;
                    var11_19 = var2_5 = (var11_19 = new FileOutputStream((File)var2_5));
                    var1_1 = var2_5;
                    var10_18 = var2_5;
                    if (this.arrayPool != null) {
                        var1_1 = var2_5;
                        var10_18 = var2_5;
                        var1_1 = var2_5;
                        var10_18 = var2_5;
                        var11_19 = new BufferedOutputStream((OutputStream)var2_5, this.arrayPool);
                    }
                    var1_1 = var11_19;
                    var10_18 = var11_19;
                    var14_9.compress(var15_10, var4_12, (OutputStream)var11_19);
                    var1_1 = var11_19;
                    var10_18 = var11_19;
                    var11_19.close();
                    var5_14 = true;
                    var6_15 = true;
                    var11_19.close();
                    var5_14 = var6_15;
                    ** GOTO lbl57
                    {
                        block16: {
                            catch (IOException var1_2) {}
                            break block16;
                            catch (Throwable var2_6) {
                                break block15;
                            }
                            catch (IOException var2_7) {}
                            var1_1 = var10_18;
                            {
                                if (Log.isLoggable((String)"BitmapEncoder", (int)3)) {
                                    var1_1 = var10_18;
                                    Log.d((String)"BitmapEncoder", (String)"Failed to encode Bitmap", (Throwable)var2_7);
                                }
                                if (var10_18 == null) break block16;
                                var5_14 = var7_13;
                            }
                            {
                                var10_18.close();
                                var5_14 = var6_15;
                            }
                        }
                        if (!Log.isLoggable((String)"BitmapEncoder", (int)2)) break block17;
                        var1_1 = new StringBuilder();
                        var1_1.append("Compressed with type: ");
                        var1_1.append(var15_10);
                        var1_1.append(" of size ");
                        var1_1.append(Util.getBitmapByteSize(var14_9));
                        var1_1.append(" in ");
                        var1_1.append(LogTime.getElapsedMillis(var8_11));
                        var1_1.append(", options format: ");
                        var1_1.append(var3_8.get(BitmapEncoder.COMPRESSION_FORMAT));
                        var1_1.append(", hasAlpha: ");
                        var1_1.append(var14_9.hasAlpha());
                        Log.v((String)"BitmapEncoder", (String)var1_1.toString());
                    }
                }
            }
            GlideTrace.endSection();
            return var5_14;
        }
        if (var1_1 == null) throw var2_6;
        {
            try {
                var1_1.close();
                throw var2_6;
            }
            catch (IOException var1_3) {
                // empty catch block
            }
            throw var2_6;
        }
    }

    @Override
    public EncodeStrategy getEncodeStrategy(Options options) {
        return EncodeStrategy.TRANSFORMED;
    }
}

