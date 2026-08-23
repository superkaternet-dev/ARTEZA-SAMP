/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.View
 */
package androidx.preference;

import android.os.Bundle;
import android.view.View;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroupAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;

@Deprecated
public class PreferenceRecyclerViewAccessibilityDelegate
extends RecyclerViewAccessibilityDelegate {
    final AccessibilityDelegateCompat mDefaultItemDelegate = super.getItemDelegate();
    final AccessibilityDelegateCompat mItemDelegate = new AccessibilityDelegateCompat(this){
        final PreferenceRecyclerViewAccessibilityDelegate this$0;
        {
            this.this$0 = preferenceRecyclerViewAccessibilityDelegate;
        }

        @Override
        public void onInitializeAccessibilityNodeInfo(View object, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            this.this$0.mDefaultItemDelegate.onInitializeAccessibilityNodeInfo((View)object, accessibilityNodeInfoCompat);
            int n = this.this$0.mRecyclerView.getChildAdapterPosition((View)object);
            object = this.this$0.mRecyclerView.getAdapter();
            if (!(object instanceof PreferenceGroupAdapter)) {
                return;
            }
            if ((object = ((PreferenceGroupAdapter)object).getItem(n)) == null) {
                return;
            }
            ((Preference)object).onInitializeAccessibilityNodeInfo(accessibilityNodeInfoCompat);
        }

        @Override
        public boolean performAccessibilityAction(View view, int n, Bundle bundle) {
            return this.this$0.mDefaultItemDelegate.performAccessibilityAction(view, n, bundle);
        }
    };
    final RecyclerView mRecyclerView;

    public PreferenceRecyclerViewAccessibilityDelegate(RecyclerView recyclerView) {
        super(recyclerView);
        this.mRecyclerView = recyclerView;
    }

    @Override
    public AccessibilityDelegateCompat getItemDelegate() {
        return this.mItemDelegate;
    }
}

