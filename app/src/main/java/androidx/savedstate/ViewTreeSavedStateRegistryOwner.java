/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewParent
 */
package androidx.savedstate;

import android.view.View;
import android.view.ViewParent;
import androidx.savedstate.R;
import androidx.savedstate.SavedStateRegistryOwner;

public final class ViewTreeSavedStateRegistryOwner {
    private ViewTreeSavedStateRegistryOwner() {
    }

    public static SavedStateRegistryOwner get(View object) {
        SavedStateRegistryOwner savedStateRegistryOwner = (SavedStateRegistryOwner)object.getTag(R.id.view_tree_saved_state_registry_owner);
        if (savedStateRegistryOwner != null) {
            return savedStateRegistryOwner;
        }
        ViewParent viewParent = object.getParent();
        object = savedStateRegistryOwner;
        while (object == null && viewParent instanceof View) {
            viewParent = (View)viewParent;
            object = (SavedStateRegistryOwner)viewParent.getTag(R.id.view_tree_saved_state_registry_owner);
            viewParent = viewParent.getParent();
        }
        return object;
    }

    public static void set(View view, SavedStateRegistryOwner savedStateRegistryOwner) {
        view.setTag(R.id.view_tree_saved_state_registry_owner, (Object)savedStateRegistryOwner);
    }
}

