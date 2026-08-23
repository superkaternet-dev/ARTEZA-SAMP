/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dalvik.system.PathClassLoader
 */
package com.google.android.gms.dynamite;

import dalvik.system.PathClassLoader;

final class zzc
extends PathClassLoader {
    zzc(String string2, ClassLoader classLoader) {
        super(string2, classLoader);
    }

    protected final Class<?> loadClass(String string2, boolean bl) throws ClassNotFoundException {
        if (!string2.startsWith("java.") && !string2.startsWith("android.")) {
            try {
                Class clazz = this.findClass(string2);
                return clazz;
            }
            catch (ClassNotFoundException classNotFoundException) {
                // empty catch block
            }
        }
        return super.loadClass(string2, bl);
    }
}

