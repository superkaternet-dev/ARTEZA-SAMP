/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.net.Uri
 */
package com.bumptech.glide.load.data.mediastore;

import android.net.Uri;

public final class MediaStoreUtil {
    private static final int MINI_THUMB_HEIGHT = 384;
    private static final int MINI_THUMB_WIDTH = 512;

    private MediaStoreUtil() {
    }

    public static boolean isMediaStoreImageUri(Uri uri) {
        boolean bl = MediaStoreUtil.isMediaStoreUri(uri) && !MediaStoreUtil.isVideoUri(uri);
        return bl;
    }

    public static boolean isMediaStoreUri(Uri uri) {
        boolean bl = uri != null && "content".equals(uri.getScheme()) && "media".equals(uri.getAuthority());
        return bl;
    }

    public static boolean isMediaStoreVideoUri(Uri uri) {
        boolean bl = MediaStoreUtil.isMediaStoreUri(uri) && MediaStoreUtil.isVideoUri(uri);
        return bl;
    }

    public static boolean isThumbnailSize(int n, int n2) {
        boolean bl = n != Integer.MIN_VALUE && n2 != Integer.MIN_VALUE && n <= 512 && n2 <= 384;
        return bl;
    }

    private static boolean isVideoUri(Uri uri) {
        return uri.getPathSegments().contains("video");
    }
}

