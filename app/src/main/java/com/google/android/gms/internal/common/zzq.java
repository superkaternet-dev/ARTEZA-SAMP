/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.CheckForNull
 */
package com.google.android.gms.internal.common;

import javax.annotation.CheckForNull;

public final class zzq {
    static final CharSequence zza(@CheckForNull Object object, String string2) {
        object.getClass();
        object = object instanceof CharSequence ? (CharSequence)object : object.toString();
        return object;
    }
}

