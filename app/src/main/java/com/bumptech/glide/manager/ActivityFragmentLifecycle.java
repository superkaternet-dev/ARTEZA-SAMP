/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.manager;

import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

class ActivityFragmentLifecycle
implements Lifecycle {
    private boolean isDestroyed;
    private boolean isStarted;
    private final Set<LifecycleListener> lifecycleListeners = Collections.newSetFromMap(new WeakHashMap());

    ActivityFragmentLifecycle() {
    }

    @Override
    public void addListener(LifecycleListener lifecycleListener) {
        this.lifecycleListeners.add(lifecycleListener);
        if (this.isDestroyed) {
            lifecycleListener.onDestroy();
        } else if (this.isStarted) {
            lifecycleListener.onStart();
        } else {
            lifecycleListener.onStop();
        }
    }

    void onDestroy() {
        this.isDestroyed = true;
        Iterator<LifecycleListener> iterator2 = Util.getSnapshot(this.lifecycleListeners).iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDestroy();
        }
    }

    void onStart() {
        this.isStarted = true;
        Iterator<LifecycleListener> iterator2 = Util.getSnapshot(this.lifecycleListeners).iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onStart();
        }
    }

    void onStop() {
        this.isStarted = false;
        Iterator<LifecycleListener> iterator2 = Util.getSnapshot(this.lifecycleListeners).iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onStop();
        }
    }

    @Override
    public void removeListener(LifecycleListener lifecycleListener) {
        this.lifecycleListeners.remove(lifecycleListener);
    }
}

