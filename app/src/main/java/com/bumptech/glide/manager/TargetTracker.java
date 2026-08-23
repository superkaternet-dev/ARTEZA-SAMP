/*
 * Decompiled with CFR 0.152.
 */
package com.bumptech.glide.manager;

import com.bumptech.glide.manager.LifecycleListener;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.util.Util;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

public final class TargetTracker
implements LifecycleListener {
    private final Set<Target<?>> targets = Collections.newSetFromMap(new WeakHashMap());

    public void clear() {
        this.targets.clear();
    }

    public List<Target<?>> getAll() {
        return Util.getSnapshot(this.targets);
    }

    @Override
    public void onDestroy() {
        Iterator<Target<?>> iterator2 = Util.getSnapshot(this.targets).iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onDestroy();
        }
    }

    @Override
    public void onStart() {
        Iterator<Target<?>> iterator2 = Util.getSnapshot(this.targets).iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onStart();
        }
    }

    @Override
    public void onStop() {
        Iterator<Target<?>> iterator2 = Util.getSnapshot(this.targets).iterator();
        while (iterator2.hasNext()) {
            iterator2.next().onStop();
        }
    }

    public void track(Target<?> target) {
        this.targets.add(target);
    }

    public void untrack(Target<?> target) {
        this.targets.remove(target);
    }
}

