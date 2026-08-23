/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.util.Iterator;

final class SavedStateHandleController
implements LifecycleEventObserver {
    static final String TAG_SAVED_STATE_HANDLE_CONTROLLER = "androidx.lifecycle.savedstate.vm.tag";
    private final SavedStateHandle mHandle;
    private boolean mIsAttached = false;
    private final String mKey;

    SavedStateHandleController(String string2, SavedStateHandle savedStateHandle) {
        this.mKey = string2;
        this.mHandle = savedStateHandle;
    }

    static void attachHandleIfNeeded(ViewModel object, SavedStateRegistry savedStateRegistry, Lifecycle lifecycle) {
        if ((object = (SavedStateHandleController)((ViewModel)object).getTag(TAG_SAVED_STATE_HANDLE_CONTROLLER)) != null && !((SavedStateHandleController)object).isAttached()) {
            ((SavedStateHandleController)object).attachToLifecycle(savedStateRegistry, lifecycle);
            SavedStateHandleController.tryToAddRecreator(savedStateRegistry, lifecycle);
        }
    }

    static SavedStateHandleController create(SavedStateRegistry savedStateRegistry, Lifecycle lifecycle, String object, Bundle bundle) {
        object = new SavedStateHandleController((String)object, SavedStateHandle.createHandle(savedStateRegistry.consumeRestoredStateForKey((String)object), bundle));
        ((SavedStateHandleController)object).attachToLifecycle(savedStateRegistry, lifecycle);
        SavedStateHandleController.tryToAddRecreator(savedStateRegistry, lifecycle);
        return object;
    }

    private static void tryToAddRecreator(SavedStateRegistry savedStateRegistry, Lifecycle lifecycle) {
        Lifecycle.State state = lifecycle.getCurrentState();
        if (state != Lifecycle.State.INITIALIZED && !state.isAtLeast(Lifecycle.State.STARTED)) {
            lifecycle.addObserver(new LifecycleEventObserver(lifecycle, savedStateRegistry){
                final Lifecycle val$lifecycle;
                final SavedStateRegistry val$registry;
                {
                    this.val$lifecycle = lifecycle;
                    this.val$registry = savedStateRegistry;
                }

                @Override
                public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_START) {
                        this.val$lifecycle.removeObserver(this);
                        this.val$registry.runOnNextRecreation(OnRecreation.class);
                    }
                }
            });
        } else {
            savedStateRegistry.runOnNextRecreation(OnRecreation.class);
        }
    }

    void attachToLifecycle(SavedStateRegistry savedStateRegistry, Lifecycle lifecycle) {
        if (!this.mIsAttached) {
            this.mIsAttached = true;
            lifecycle.addObserver(this);
            savedStateRegistry.registerSavedStateProvider(this.mKey, this.mHandle.savedStateProvider());
            return;
        }
        throw new IllegalStateException("Already attached to lifecycleOwner");
    }

    SavedStateHandle getHandle() {
        return this.mHandle;
    }

    boolean isAttached() {
        return this.mIsAttached;
    }

    @Override
    public void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            this.mIsAttached = false;
            lifecycleOwner.getLifecycle().removeObserver(this);
        }
    }

    static final class OnRecreation
    implements SavedStateRegistry.AutoRecreated {
        OnRecreation() {
        }

        @Override
        public void onRecreated(SavedStateRegistryOwner object) {
            if (object instanceof ViewModelStoreOwner) {
                ViewModelStore viewModelStore = ((ViewModelStoreOwner)object).getViewModelStore();
                SavedStateRegistry savedStateRegistry = object.getSavedStateRegistry();
                Iterator<String> iterator2 = viewModelStore.keys().iterator();
                while (iterator2.hasNext()) {
                    SavedStateHandleController.attachHandleIfNeeded(viewModelStore.get(iterator2.next()), savedStateRegistry, object.getLifecycle());
                }
                if (!viewModelStore.keys().isEmpty()) {
                    savedStateRegistry.runOnNextRecreation(OnRecreation.class);
                }
                return;
            }
            object = new IllegalStateException("Internal error: OnRecreation should be registered only on componentsthat implement ViewModelStoreOwner");
            throw object;
        }
    }
}

