/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package androidx.lifecycle;

import android.view.View;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.lifecycle.viewmodel.R;

public class ViewTreeViewModelStoreOwner {
    private ViewTreeViewModelStoreOwner() {
    }

    public static ViewModelStoreOwner get(View view) {
        ViewModelStoreOwner viewModelStoreOwner = (ViewModelStoreOwner)view.getTag(R.id.view_tree_view_model_store_owner);
        if (viewModelStoreOwner != null) {
            return viewModelStoreOwner;
        }
        view = view.getParent();
        while (viewModelStoreOwner == null && view instanceof View) {
            viewModelStoreOwner = (ViewModelStoreOwner)view.getTag(R.id.view_tree_view_model_store_owner);
            view = view.getParent();
        }
        return viewModelStoreOwner;
    }

    public static void set(View view, ViewModelStoreOwner viewModelStoreOwner) {
        view.setTag(R.id.view_tree_view_model_store_owner, (Object)viewModelStoreOwner);
    }
}

