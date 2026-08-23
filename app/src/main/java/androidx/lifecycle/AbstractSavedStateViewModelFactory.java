/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package androidx.lifecycle;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleController;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;

public abstract class AbstractSavedStateViewModelFactory
extends ViewModelProvider.KeyedFactory {
    static final String TAG_SAVED_STATE_HANDLE_CONTROLLER = "androidx.lifecycle.savedstate.vm.tag";
    private final Bundle mDefaultArgs;
    private final Lifecycle mLifecycle;
    private final SavedStateRegistry mSavedStateRegistry;

    public AbstractSavedStateViewModelFactory(SavedStateRegistryOwner savedStateRegistryOwner, Bundle bundle) {
        this.mSavedStateRegistry = savedStateRegistryOwner.getSavedStateRegistry();
        this.mLifecycle = savedStateRegistryOwner.getLifecycle();
        this.mDefaultArgs = bundle;
    }

    @Override
    public final <T extends ViewModel> T create(Class<T> clazz) {
        String string2 = clazz.getCanonicalName();
        if (string2 != null) {
            return this.create(string2, clazz);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    @Override
    public final <T extends ViewModel> T create(String string2, Class<T> clazz) {
        SavedStateHandleController savedStateHandleController = SavedStateHandleController.create(this.mSavedStateRegistry, this.mLifecycle, string2, this.mDefaultArgs);
        string2 = this.create(string2, clazz, savedStateHandleController.getHandle());
        ((ViewModel)((Object)string2)).setTagIfAbsent(TAG_SAVED_STATE_HANDLE_CONTROLLER, savedStateHandleController);
        return (T)string2;
    }

    protected abstract <T extends ViewModel> T create(String var1, Class<T> var2, SavedStateHandle var3);

    @Override
    void onRequery(ViewModel viewModel) {
        SavedStateHandleController.attachHandleIfNeeded(viewModel, this.mSavedStateRegistry, this.mLifecycle);
    }
}

