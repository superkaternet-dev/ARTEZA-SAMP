/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.View
 */
package androidx.fragment.app;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import java.util.concurrent.CopyOnWriteArrayList;

class FragmentLifecycleCallbacksDispatcher {
    private final FragmentManager mFragmentManager;
    private final CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> mLifecycleCallbacks = new CopyOnWriteArrayList();

    FragmentLifecycleCallbacksDispatcher(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }

    void dispatchOnFragmentActivityCreated(Fragment fragment, Bundle bundle, boolean bl) {
        Fragment object2 = this.mFragmentManager.getParent();
        if (object2 != null) {
            object2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentActivityCreated(fragment, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentActivityCreated(this.mFragmentManager, fragment, bundle);
        }
    }

    void dispatchOnFragmentAttached(Fragment fragment, boolean bl) {
        Context context = this.mFragmentManager.getHost().getContext();
        Fragment fragment2 = this.mFragmentManager.getParent();
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentAttached(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentAttached(this.mFragmentManager, fragment, context);
        }
    }

    void dispatchOnFragmentCreated(Fragment fragment, Bundle bundle, boolean bl) {
        Fragment object2 = this.mFragmentManager.getParent();
        if (object2 != null) {
            object2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentCreated(fragment, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentCreated(this.mFragmentManager, fragment, bundle);
        }
    }

    void dispatchOnFragmentDestroyed(Fragment fragment, boolean bl) {
        Fragment object2 = this.mFragmentManager.getParent();
        if (object2 != null) {
            object2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentDestroyed(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentDestroyed(this.mFragmentManager, fragment);
        }
    }

    void dispatchOnFragmentDetached(Fragment fragment, boolean bl) {
        Fragment fragment2 = this.mFragmentManager.getParent();
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentDetached(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentDetached(this.mFragmentManager, fragment);
        }
    }

    void dispatchOnFragmentPaused(Fragment fragment, boolean bl) {
        Fragment fragment2 = this.mFragmentManager.getParent();
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentPaused(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentPaused(this.mFragmentManager, fragment);
        }
    }

    void dispatchOnFragmentPreAttached(Fragment fragment, boolean bl) {
        Context context = this.mFragmentManager.getHost().getContext();
        Fragment fragment2 = this.mFragmentManager.getParent();
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentPreAttached(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreAttached(this.mFragmentManager, fragment, context);
        }
    }

    void dispatchOnFragmentPreCreated(Fragment fragment, Bundle bundle, boolean bl) {
        Fragment fragment2 = this.mFragmentManager.getParent();
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentPreCreated(fragment, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentPreCreated(this.mFragmentManager, fragment, bundle);
        }
    }

    void dispatchOnFragmentResumed(Fragment fragment, boolean bl) {
        Fragment fragment2 = this.mFragmentManager.getParent();
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentResumed(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentResumed(this.mFragmentManager, fragment);
        }
    }

    void dispatchOnFragmentSaveInstanceState(Fragment fragment, Bundle bundle, boolean bl) {
        Fragment fragment2 = this.mFragmentManager.getParent();
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentSaveInstanceState(fragment, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentSaveInstanceState(this.mFragmentManager, fragment, bundle);
        }
    }

    void dispatchOnFragmentStarted(Fragment fragment, boolean bl) {
        Fragment object2 = this.mFragmentManager.getParent();
        if (object2 != null) {
            object2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentStarted(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentStarted(this.mFragmentManager, fragment);
        }
    }

    void dispatchOnFragmentStopped(Fragment fragment, boolean bl) {
        Fragment fragment2 = this.mFragmentManager.getParent();
        if (fragment2 != null) {
            fragment2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentStopped(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentStopped(this.mFragmentManager, fragment);
        }
    }

    void dispatchOnFragmentViewCreated(Fragment fragment, View view, Bundle bundle, boolean bl) {
        Fragment object2 = this.mFragmentManager.getParent();
        if (object2 != null) {
            object2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentViewCreated(fragment, view, bundle, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewCreated(this.mFragmentManager, fragment, view, bundle);
        }
    }

    void dispatchOnFragmentViewDestroyed(Fragment fragment, boolean bl) {
        Fragment object2 = this.mFragmentManager.getParent();
        if (object2 != null) {
            object2.getParentFragmentManager().getLifecycleCallbacksDispatcher().dispatchOnFragmentViewDestroyed(fragment, true);
        }
        for (FragmentLifecycleCallbacksHolder fragmentLifecycleCallbacksHolder : this.mLifecycleCallbacks) {
            if (bl && !fragmentLifecycleCallbacksHolder.mRecursive) continue;
            fragmentLifecycleCallbacksHolder.mCallback.onFragmentViewDestroyed(this.mFragmentManager, fragment);
        }
    }

    public void registerFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean bl) {
        this.mLifecycleCallbacks.add(new FragmentLifecycleCallbacksHolder(fragmentLifecycleCallbacks, bl));
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void unregisterFragmentLifecycleCallbacks(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks) {
        CopyOnWriteArrayList<FragmentLifecycleCallbacksHolder> copyOnWriteArrayList = this.mLifecycleCallbacks;
        synchronized (copyOnWriteArrayList) {
            int n = this.mLifecycleCallbacks.size();
            for (int i = 0; i < n; ++i) {
                if (this.mLifecycleCallbacks.get((int)i).mCallback != fragmentLifecycleCallbacks) continue;
                this.mLifecycleCallbacks.remove(i);
                return;
            }
            return;
        }
    }

    private static final class FragmentLifecycleCallbacksHolder {
        final FragmentManager.FragmentLifecycleCallbacks mCallback;
        final boolean mRecursive;

        FragmentLifecycleCallbacksHolder(FragmentManager.FragmentLifecycleCallbacks fragmentLifecycleCallbacks, boolean bl) {
            this.mCallback = fragmentLifecycleCallbacks;
            this.mRecursive = bl;
        }
    }
}

