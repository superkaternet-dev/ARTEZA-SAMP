/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentCallbacks2
 *  android.content.res.Configuration
 */
package com.bumptech.glide.manager;

import android.app.Activity;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import com.bumptech.glide.manager.FrameWaiter;

final class FirstFrameAndAfterTrimMemoryWaiter
implements FrameWaiter,
ComponentCallbacks2 {
    FirstFrameAndAfterTrimMemoryWaiter() {
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public void onLowMemory() {
        this.onTrimMemory(20);
    }

    public void onTrimMemory(int n) {
    }

    @Override
    public void registerSelf(Activity activity) {
    }
}

