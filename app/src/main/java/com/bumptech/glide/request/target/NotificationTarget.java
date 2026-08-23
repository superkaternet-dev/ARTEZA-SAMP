/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Notification
 *  android.app.NotificationManager
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.widget.RemoteViews
 */
package com.bumptech.glide.request.target;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Preconditions;

public class NotificationTarget
extends CustomTarget<Bitmap> {
    private final Context context;
    private final Notification notification;
    private final int notificationId;
    private final String notificationTag;
    private final RemoteViews remoteViews;
    private final int viewId;

    public NotificationTarget(Context context, int n, int n2, int n3, RemoteViews remoteViews, Notification notification, int n4, String string2) {
        super(n, n2);
        this.context = Preconditions.checkNotNull(context, "Context must not be null!");
        this.notification = Preconditions.checkNotNull(notification, "Notification object can not be null!");
        this.remoteViews = Preconditions.checkNotNull(remoteViews, "RemoteViews object can not be null!");
        this.viewId = n3;
        this.notificationId = n4;
        this.notificationTag = string2;
    }

    public NotificationTarget(Context context, int n, RemoteViews remoteViews, Notification notification, int n2) {
        this(context, n, remoteViews, notification, n2, null);
    }

    public NotificationTarget(Context context, int n, RemoteViews remoteViews, Notification notification, int n2, String string2) {
        this(context, Integer.MIN_VALUE, Integer.MIN_VALUE, n, remoteViews, notification, n2, string2);
    }

    private void setBitmap(Bitmap bitmap) {
        this.remoteViews.setImageViewBitmap(this.viewId, bitmap);
        this.update();
    }

    private void update() {
        Preconditions.checkNotNull((NotificationManager)this.context.getSystemService("notification")).notify(this.notificationTag, this.notificationId, this.notification);
    }

    @Override
    public void onLoadCleared(Drawable drawable2) {
        this.setBitmap(null);
    }

    @Override
    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
        this.setBitmap(bitmap);
    }
}

