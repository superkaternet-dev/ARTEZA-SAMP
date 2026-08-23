/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Canvas
 *  android.graphics.PointF
 */
package com.tuyenmonkey.mkloader.type;

import android.graphics.Canvas;
import android.graphics.PointF;
import com.tuyenmonkey.mkloader.callback.InvalidateListener;

public abstract class LoaderView {
    protected PointF center;
    protected int color;
    protected int desiredHeight = 150;
    protected int desiredWidth = 150;
    protected int height;
    protected InvalidateListener invalidateListener;
    protected int width;

    public abstract void draw(Canvas var1);

    public int getDesiredHeight() {
        return this.desiredHeight;
    }

    public int getDesiredWidth() {
        return this.desiredWidth;
    }

    public abstract void initializeObjects();

    public boolean isDetached() {
        boolean bl = this.invalidateListener == null;
        return bl;
    }

    public void onDetach() {
        if (this.invalidateListener != null) {
            this.invalidateListener = null;
        }
    }

    public void setColor(int n) {
        this.color = n;
    }

    public void setInvalidateListener(InvalidateListener invalidateListener) {
        this.invalidateListener = invalidateListener;
    }

    public void setSize(int n, int n2) {
        this.width = n;
        this.height = n2;
        this.center = new PointF((float)n / 2.0f, (float)n2 / 2.0f);
    }

    public abstract void setUpAnimation();
}

