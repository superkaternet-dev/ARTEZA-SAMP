/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package androidx.savedstate;

import android.os.Bundle;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.savedstate.SavedStateRegistry;
import androidx.savedstate.SavedStateRegistryOwner;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

final class Recreator
implements GenericLifecycleObserver {
    static final String CLASSES_KEY = "classes_to_restore";
    static final String COMPONENT_KEY = "androidx.savedstate.Restarter";
    private final SavedStateRegistryOwner mOwner;

    Recreator(SavedStateRegistryOwner savedStateRegistryOwner) {
        this.mOwner = savedStateRegistryOwner;
    }

    private void reflectiveNew(String charSequence) {
        Object object;
        Object object2;
        try {
            object2 = Class.forName((String)charSequence, false, Recreator.class.getClassLoader()).asSubclass(SavedStateRegistry.AutoRecreated.class);
        }
        catch (ClassNotFoundException classNotFoundException) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Class ");
            stringBuilder.append((String)charSequence);
            stringBuilder.append(" wasn't found");
            throw new RuntimeException(stringBuilder.toString(), classNotFoundException);
        }
        try {
            object = ((Class)object2).getDeclaredConstructor(new Class[0]);
            ((Constructor)object).setAccessible(true);
        }
        catch (NoSuchMethodException noSuchMethodException) {
            charSequence = new StringBuilder();
            ((StringBuilder)charSequence).append("Class");
            ((StringBuilder)charSequence).append(((Class)object2).getSimpleName());
            ((StringBuilder)charSequence).append(" must have default constructor in order to be automatically recreated");
            throw new IllegalStateException(((StringBuilder)charSequence).toString(), noSuchMethodException);
        }
        try {
            object2 = ((Constructor)object).newInstance(new Object[0]);
            object2.onRecreated(this.mOwner);
            return;
        }
        catch (Exception exception) {
            object = new StringBuilder();
            ((StringBuilder)object).append("Failed to instantiate ");
            ((StringBuilder)object).append((String)charSequence);
            throw new RuntimeException(((StringBuilder)object).toString(), exception);
        }
    }

    @Override
    public void onStateChanged(LifecycleOwner iterator2, Lifecycle.Event event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            iterator2.getLifecycle().removeObserver(this);
            iterator2 = this.mOwner.getSavedStateRegistry().consumeRestoredStateForKey(COMPONENT_KEY);
            if (iterator2 == null) {
                return;
            }
            if ((iterator2 = iterator2.getStringArrayList(CLASSES_KEY)) != null) {
                iterator2 = ((ArrayList)((Object)iterator2)).iterator();
                while (iterator2.hasNext()) {
                    this.reflectiveNew((String)iterator2.next());
                }
                return;
            }
            throw new IllegalStateException("Bundle with restored state for the component \"androidx.savedstate.Restarter\" must contain list of strings by the key \"classes_to_restore\"");
        }
        iterator2 = new AssertionError((Object)"Next event must be ON_CREATE");
        throw iterator2;
    }

    static final class SavedStateProvider
    implements SavedStateRegistry.SavedStateProvider {
        final Set<String> mClasses = new HashSet<String>();

        SavedStateProvider(SavedStateRegistry savedStateRegistry) {
            savedStateRegistry.registerSavedStateProvider(Recreator.COMPONENT_KEY, this);
        }

        void add(String string2) {
            this.mClasses.add(string2);
        }

        @Override
        public Bundle saveState() {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(Recreator.CLASSES_KEY, new ArrayList<String>(this.mClasses));
            return bundle;
        }
    }
}

