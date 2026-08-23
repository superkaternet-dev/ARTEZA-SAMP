/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager$NameNotFoundException
 */
package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageManager;
import com.google.android.gms.common.wrappers.Wrappers;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AndroidUtilsLight {
    private static volatile int zza = -1;

    @Deprecated
    public static byte[] getPackageCertificateHashBytes(Context context, String object) throws PackageManager.NameNotFoundException {
        context = Wrappers.packageManager(context).getPackageInfo((String)object, 64);
        if (context.signatures != null && context.signatures.length == 1 && (object = AndroidUtilsLight.zza("SHA1")) != null) {
            return ((MessageDigest)object).digest(context.signatures[0].toByteArray());
        }
        return null;
    }

    public static MessageDigest zza(String string2) {
        for (int i = 0; i < 2; ++i) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(string2);
                if (messageDigest == null) continue;
                return messageDigest;
            }
            catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                continue;
            }
        }
        return null;
    }
}

