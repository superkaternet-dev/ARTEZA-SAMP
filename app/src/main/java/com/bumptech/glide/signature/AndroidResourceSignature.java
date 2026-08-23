/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.bumptech.glide.signature;

import android.content.Context;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.signature.ApplicationVersionSignature;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class AndroidResourceSignature
implements Key {
    private final Key applicationVersion;
    private final int nightMode;

    private AndroidResourceSignature(int n, Key key) {
        this.nightMode = n;
        this.applicationVersion = key;
    }

    public static Key obtain(Context context) {
        Key key = ApplicationVersionSignature.obtain(context);
        return new AndroidResourceSignature(context.getResources().getConfiguration().uiMode & 0x30, key);
    }

    @Override
    public boolean equals(Object object) {
        boolean bl = object instanceof AndroidResourceSignature;
        boolean bl2 = false;
        if (bl) {
            object = (AndroidResourceSignature)object;
            bl = bl2;
            if (this.nightMode == ((AndroidResourceSignature)object).nightMode) {
                bl = bl2;
                if (this.applicationVersion.equals(((AndroidResourceSignature)object).applicationVersion)) {
                    bl = true;
                }
            }
            return bl;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(this.applicationVersion, this.nightMode);
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {
        this.applicationVersion.updateDiskCacheKey(messageDigest);
        messageDigest.update(ByteBuffer.allocate(4).putInt(this.nightMode).array());
    }
}

