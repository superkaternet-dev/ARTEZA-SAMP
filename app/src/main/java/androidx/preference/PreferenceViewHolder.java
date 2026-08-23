/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 *  android.view.View
 */
package androidx.preference;

import android.util.SparseArray;
import android.view.View;
import androidx.preference.R;
import androidx.recyclerview.widget.RecyclerView;

public class PreferenceViewHolder
extends RecyclerView.ViewHolder {
    private final SparseArray<View> mCachedViews;
    private boolean mDividerAllowedAbove;
    private boolean mDividerAllowedBelow;

    PreferenceViewHolder(View view) {
        super(view);
        SparseArray sparseArray;
        this.mCachedViews = sparseArray = new SparseArray(4);
        sparseArray.put(16908310, (Object)view.findViewById(16908310));
        sparseArray.put(0x1020010, (Object)view.findViewById(0x1020010));
        sparseArray.put(16908294, (Object)view.findViewById(16908294));
        sparseArray.put(R.id.icon_frame, (Object)view.findViewById(R.id.icon_frame));
        sparseArray.put(16908350, (Object)view.findViewById(16908350));
    }

    public static PreferenceViewHolder createInstanceForTests(View view) {
        return new PreferenceViewHolder(view);
    }

    public View findViewById(int n) {
        View view = (View)this.mCachedViews.get(n);
        if (view != null) {
            return view;
        }
        view = this.itemView.findViewById(n);
        if (view != null) {
            this.mCachedViews.put(n, (Object)view);
        }
        return view;
    }

    public boolean isDividerAllowedAbove() {
        return this.mDividerAllowedAbove;
    }

    public boolean isDividerAllowedBelow() {
        return this.mDividerAllowedBelow;
    }

    public void setDividerAllowedAbove(boolean bl) {
        this.mDividerAllowedAbove = bl;
    }

    public void setDividerAllowedBelow(boolean bl) {
        this.mDividerAllowedBelow = bl;
    }
}

