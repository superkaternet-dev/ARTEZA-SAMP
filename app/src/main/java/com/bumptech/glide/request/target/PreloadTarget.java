/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.drawable.Drawable
 *  android.os.Handler
 *  android.os.Handler$Callback
 *  android.os.Looper
 *  android.os.Message
 */
package com.bumptech.glide.request.target;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

public final class PreloadTarget<Z>
extends CustomTarget<Z> {
    private static final Handler HANDLER = new Handler(Looper.getMainLooper(), new Handler.Callback(){

        public boolean handleMessage(Message message) {
            if (message.what == 1) {
                ((PreloadTarget)message.obj).clear();
                return true;
            }
            return false;
        }
    });
    private static final int MESSAGE_CLEAR = 1;
    private final RequestManager requestManager;

    private PreloadTarget(RequestManager requestManager, int n, int n2) {
        super(n, n2);
        this.requestManager = requestManager;
    }

    public static <Z> PreloadTarget<Z> obtain(RequestManager requestManager, int n, int n2) {
        return new PreloadTarget<Z>(requestManager, n, n2);
    }

    void clear() {
        this.requestManager.clear(this);
    }

    @Override
    public void onLoadCleared(Drawable drawable2) {
    }

    @Override
    public void onResourceReady(Z object, Transition<? super Z> transition) {
        object = this.getRequest();
        if (object != null && object.isComplete()) {
            HANDLER.obtainMessage(1, (Object)this).sendToTarget();
        }
    }
}

