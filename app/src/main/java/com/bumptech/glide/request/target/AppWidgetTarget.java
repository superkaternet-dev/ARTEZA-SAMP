/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.appwidget.AppWidgetManager
 *  android.content.ComponentName
 *  android.content.Context
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.widget.RemoteViews
 */
package com.bumptech.glide.request.target;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.RemoteViews;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.util.Preconditions;

public class AppWidgetTarget
extends CustomTarget<Bitmap> {
    private final ComponentName componentName;
    private final Context context;
    private final RemoteViews remoteViews;
    private final int viewId;
    private final int[] widgetIds;

    public AppWidgetTarget(Context context, int n, int n2, int n3, RemoteViews remoteViews, ComponentName componentName) {
        super(n, n2);
        this.context = Preconditions.checkNotNull(context, "Context can not be null!");
        this.remoteViews = Preconditions.checkNotNull(remoteViews, "RemoteViews object can not be null!");
        this.componentName = Preconditions.checkNotNull(componentName, "ComponentName can not be null!");
        this.viewId = n3;
        this.widgetIds = null;
    }

    public AppWidgetTarget(Context context, int n, int n2, int n3, RemoteViews remoteViews, int ... nArray) {
        super(n, n2);
        if (nArray.length != 0) {
            this.context = Preconditions.checkNotNull(context, "Context can not be null!");
            this.remoteViews = Preconditions.checkNotNull(remoteViews, "RemoteViews object can not be null!");
            this.widgetIds = Preconditions.checkNotNull(nArray, "WidgetIds can not be null!");
            this.viewId = n3;
            this.componentName = null;
            return;
        }
        throw new IllegalArgumentException("WidgetIds must have length > 0");
    }

    public AppWidgetTarget(Context context, int n, RemoteViews remoteViews, ComponentName componentName) {
        this(context, Integer.MIN_VALUE, Integer.MIN_VALUE, n, remoteViews, componentName);
    }

    public AppWidgetTarget(Context context, int n, RemoteViews remoteViews, int ... nArray) {
        this(context, Integer.MIN_VALUE, Integer.MIN_VALUE, n, remoteViews, nArray);
    }

    private void setBitmap(Bitmap bitmap) {
        this.remoteViews.setImageViewBitmap(this.viewId, bitmap);
        this.update();
    }

    private void update() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance((Context)this.context);
        ComponentName componentName = this.componentName;
        if (componentName != null) {
            appWidgetManager.updateAppWidget(componentName, this.remoteViews);
        } else {
            appWidgetManager.updateAppWidget(this.widgetIds, this.remoteViews);
        }
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

