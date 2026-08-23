/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.os.Bundle
 */
package androidx.lifecycle;

import android.app.Application;
import android.os.Bundle;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateHandleController;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

public final class SavedStateViewModelFactory
extends ViewModelProvider.KeyedFactory {
    private static final Class<?>[] ANDROID_VIEWMODEL_SIGNATURE = new Class[]{Application.class, SavedStateHandle.class};
    private static final Class<?>[] VIEWMODEL_SIGNATURE = new Class[]{SavedStateHandle.class};
    private final Application mApplication;
    private final Bundle mDefaultArgs;
    private final ViewModelProvider.Factory mFactory;
    private final Lifecycle mLifecycle;
    private final SavedStateRegistry mSavedStateRegistry;

    public SavedStateViewModelFactory(Application application, SavedStateRegistryOwner savedStateRegistryOwner) {
        this(application, savedStateRegistryOwner, null);
    }

    public SavedStateViewModelFactory(Application object, SavedStateRegistryOwner savedStateRegistryOwner, Bundle bundle) {
        this.mSavedStateRegistry = savedStateRegistryOwner.getSavedStateRegistry();
        this.mLifecycle = savedStateRegistryOwner.getLifecycle();
        this.mDefaultArgs = bundle;
        this.mApplication = object;
        object = object != null ? ViewModelProvider.AndroidViewModelFactory.getInstance(object) : ViewModelProvider.NewInstanceFactory.getInstance();
        this.mFactory = object;
    }

    private static <T> Constructor<T> findMatchingConstructor(Class<T> constructorArray, Class<?>[] classArray) {
        for (Constructor<?> constructor : constructorArray.getConstructors()) {
            if (!Arrays.equals(classArray, constructor.getParameterTypes())) continue;
            return constructor;
        }
        return null;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> clazz) {
        String string2 = clazz.getCanonicalName();
        if (string2 != null) {
            return this.create(string2, clazz);
        }
        throw new IllegalArgumentException("Local and anonymous classes can not be ViewModels");
    }

    /*
     * Unable to fully structure code
     */
    @Override
    public <T extends ViewModel> T create(String var1_1, Class<T> var2_5) {
        var3_6 = AndroidViewModel.class.isAssignableFrom(var2_5);
        var4_7 = var3_6 != false && this.mApplication != null ? SavedStateViewModelFactory.findMatchingConstructor(var2_5, SavedStateViewModelFactory.ANDROID_VIEWMODEL_SIGNATURE) : SavedStateViewModelFactory.findMatchingConstructor(var2_5, SavedStateViewModelFactory.VIEWMODEL_SIGNATURE);
        if (var4_7 == null) {
            return this.mFactory.create(var2_5);
        }
        var5_8 = SavedStateHandleController.create(this.mSavedStateRegistry, this.mLifecycle, (String)var1_1, this.mDefaultArgs);
        if (!var3_6) ** GOTO lbl13
        var1_1 = this.mApplication;
        if (var1_1 == null) ** GOTO lbl13
        try {
            block6: {
                var1_1 = (ViewModel)var4_7.newInstance(new Object[]{var1_1, var5_8.getHandle()});
                break block6;
lbl13:
                // 2 sources

                var1_1 = (ViewModel)var4_7.newInstance(new Object[]{var5_8.getHandle()});
            }
            var1_1.setTagIfAbsent("androidx.lifecycle.savedstate.vm.tag", var5_8);
        }
        catch (InvocationTargetException var1_2) {
            var4_7 = new StringBuilder();
            var4_7.append("An exception happened in constructor of ");
            var4_7.append(var2_5);
            throw new RuntimeException(var4_7.toString(), var1_2.getCause());
        }
        catch (InstantiationException var1_3) {
            var4_7 = new StringBuilder();
            var4_7.append("A ");
            var4_7.append(var2_5);
            var4_7.append(" cannot be instantiated.");
            throw new RuntimeException(var4_7.toString(), var1_3);
        }
        catch (IllegalAccessException var1_4) {
            var4_7 = new StringBuilder();
            var4_7.append("Failed to access ");
            var4_7.append(var2_5);
            throw new RuntimeException(var4_7.toString(), var1_4);
        }
        return (T)var1_1;
    }

    @Override
    void onRequery(ViewModel viewModel) {
        SavedStateHandleController.attachHandleIfNeeded(viewModel, this.mSavedStateRegistry, this.mLifecycle);
    }
}

