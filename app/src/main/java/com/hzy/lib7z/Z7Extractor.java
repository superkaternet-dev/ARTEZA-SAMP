/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.res.AssetManager
 *  android.text.TextUtils
 */
package com.hzy.lib7z;

import android.content.res.AssetManager;
import android.text.TextUtils;
import com.hzy.lib7z.IExtractCallback;
import java.io.File;

public class Z7Extractor {
    public static final long DEFAULT_IN_BUF_SIZE = 0x1000000L;
    private static final String lib7z = "un7zip";
    private static boolean mLibLoaded = false;

    public static int extractAsset(AssetManager assetManager, String string2, String string3, IExtractCallback iExtractCallback) {
        if (!mLibLoaded) {
            Z7Extractor.init();
        }
        if (!TextUtils.isEmpty((CharSequence)string2) && !TextUtils.isEmpty((CharSequence)string3) && Z7Extractor.prepareOutPath(string3)) {
            return Z7Extractor.nExtractAsset(assetManager, string2, string3, iExtractCallback, 0x1000000L);
        }
        if (iExtractCallback != null) {
            iExtractCallback.onError(999, "File Path Error!");
        }
        return 999;
    }

    public static int extractFile(String string2, String string3, IExtractCallback iExtractCallback) {
        if (!mLibLoaded) {
            Z7Extractor.init();
        }
        File file = new File(string2);
        if (!TextUtils.isEmpty((CharSequence)string2) && file.exists() && !TextUtils.isEmpty((CharSequence)string3) && Z7Extractor.prepareOutPath(string3)) {
            return Z7Extractor.nExtractFile(string2, string3, iExtractCallback, 0x1000000L);
        }
        if (iExtractCallback != null) {
            iExtractCallback.onError(999, "File Path Error!");
        }
        return 999;
    }

    public static String getLzmaVersion() {
        if (!mLibLoaded) {
            Z7Extractor.init();
        }
        return Z7Extractor.nGetLzmaVersion();
    }

    public static void init() {
        Z7Extractor.init(null);
    }

    public static void init(LibLoader libLoader) {
        if (!mLibLoaded) {
            if (libLoader != null) {
                libLoader.loadLibrary(lib7z);
            } else {
                System.loadLibrary(lib7z);
            }
            mLibLoaded = true;
        }
    }

    public static native int nExtractAsset(AssetManager var0, String var1, String var2, IExtractCallback var3, long var4);

    public static native int nExtractFile(String var0, String var1, IExtractCallback var2, long var3);

    public static native String nGetLzmaVersion();

    private static boolean prepareOutPath(String object) {
        object = new File((String)object);
        boolean bl = ((File)object).exists();
        boolean bl2 = true;
        if (!bl && ((File)object).mkdirs()) {
            return true;
        }
        if (!((File)object).exists() || !((File)object).isDirectory()) {
            bl2 = false;
        }
        return bl2;
    }

    public static interface LibLoader {
        public void loadLibrary(String var1);
    }
}

