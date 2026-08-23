/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 */
package com.bumptech.glide.manager;

import android.app.Activity;
import com.bumptech.glide.manager.FrameWaiter;

final class DoNothingFirstFrameWaiter
implements FrameWaiter {
    DoNothingFirstFrameWaiter() {
    }

    @Override
    public void registerSelf(Activity activity) {
    }
}

