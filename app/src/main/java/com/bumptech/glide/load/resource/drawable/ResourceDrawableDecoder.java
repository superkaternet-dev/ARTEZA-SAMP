/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.pm.PackageManager$NameNotFoundException
 *  android.content.res.Resources
 *  android.graphics.drawable.Drawable
 *  android.net.Uri
 */
package com.bumptech.glide.load.resource.drawable;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.drawable.DrawableDecoderCompat;
import com.bumptech.glide.load.resource.drawable.NonOwnedDrawableResource;
import java.util.List;

public class ResourceDrawableDecoder
implements ResourceDecoder<Uri, Drawable> {
    private static final String ANDROID_PACKAGE_NAME = "android";
    private static final int ID_PATH_SEGMENTS = 1;
    private static final int MISSING_RESOURCE_ID = 0;
    private static final int NAME_PATH_SEGMENT_INDEX = 1;
    private static final int NAME_URI_PATH_SEGMENTS = 2;
    private static final int RESOURCE_ID_SEGMENT_INDEX = 0;
    private static final int TYPE_PATH_SEGMENT_INDEX = 0;
    private final Context context;

    public ResourceDrawableDecoder(Context context) {
        this.context = context.getApplicationContext();
    }

    private Context findContextForPackage(Uri uri, String charSequence) {
        if (((String)charSequence).equals(this.context.getPackageName())) {
            return this.context;
        }
        try {
            Context context = this.context.createPackageContext((String)charSequence, 0);
            return context;
        }
        catch (PackageManager.NameNotFoundException nameNotFoundException) {
            if (((String)charSequence).contains(this.context.getPackageName())) {
                return this.context;
            }
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("Failed to obtain context or unrecognized Uri format for: ");
            ((StringBuilder)charSequence).append(uri);
            throw new IllegalArgumentException(((StringBuilder)charSequence).toString(), nameNotFoundException);
        }
    }

    private int findResourceIdFromResourceIdUri(Uri uri) {
        Object object = uri.getPathSegments();
        try {
            int n = Integer.parseInt((String)object.get(0));
            return n;
        }
        catch (NumberFormatException numberFormatException) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Unrecognized Uri format: ");
            ((StringBuilder)object).append(uri);
            throw new IllegalArgumentException(((StringBuilder)object).toString(), numberFormatException);
        }
    }

    private int findResourceIdFromTypeAndNameResourceUri(Context object, Uri uri) {
        int n;
        Object object2 = uri.getPathSegments();
        String string2 = uri.getAuthority();
        String string3 = (String)object2.get(0);
        object2 = (String)object2.get(1);
        int n2 = n = object.getResources().getIdentifier((String)object2, string3, string2);
        if (n == 0) {
            n2 = Resources.getSystem().getIdentifier((String)object2, string3, ANDROID_PACKAGE_NAME);
        }
        if (n2 != 0) {
            return n2;
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Failed to find resource id for: ");
        ((StringBuilder)object).append(uri);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    private int findResourceIdFromUri(Context object, Uri uri) {
        List list = uri.getPathSegments();
        if (list.size() == 2) {
            return this.findResourceIdFromTypeAndNameResourceUri((Context)object, uri);
        }
        if (list.size() == 1) {
            return this.findResourceIdFromResourceIdUri(uri);
        }
        object = new StringBuilder();
        ((StringBuilder)object).append("Unrecognized Uri format: ");
        ((StringBuilder)object).append(uri);
        throw new IllegalArgumentException(((StringBuilder)object).toString());
    }

    @Override
    public Resource<Drawable> decode(Uri uri, int n, int n2, Options options) {
        options = this.findContextForPackage(uri, uri.getAuthority());
        n = this.findResourceIdFromUri((Context)options, uri);
        return NonOwnedDrawableResource.newInstance(DrawableDecoderCompat.getDrawable(this.context, (Context)options, n));
    }

    @Override
    public boolean handles(Uri uri, Options options) {
        return uri.getScheme().equals("android.resource");
    }
}

