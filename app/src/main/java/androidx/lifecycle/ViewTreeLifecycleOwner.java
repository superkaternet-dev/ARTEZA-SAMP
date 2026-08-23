/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 */
package androidx.lifecycle;

import android.view.View;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.runtime.R;

public class ViewTreeLifecycleOwner {
    private ViewTreeLifecycleOwner() {
    }

    public static LifecycleOwner get(View view) {
        LifecycleOwner lifecycleOwner = (LifecycleOwner)view.getTag(R.id.view_tree_lifecycle_owner);
        if (lifecycleOwner != null) {
            return lifecycleOwner;
        }
        view = view.getParent();
        while (lifecycleOwner == null && view instanceof View) {
            lifecycleOwner = (LifecycleOwner)view.getTag(R.id.view_tree_lifecycle_owner);
            view = view.getParent();
        }
        return lifecycleOwner;
    }

    public static void set(View view, LifecycleOwner lifecycleOwner) {
        view.setTag(R.id.view_tree_lifecycle_owner, (Object)lifecycleOwner);
    }
}

