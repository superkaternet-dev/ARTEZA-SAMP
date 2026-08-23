/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.util.SparseArray
 *  android.view.View
 */
package androidx.transition;

import android.util.SparseArray;
import android.view.View;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.transition.TransitionValues;

class TransitionValuesMaps {
    final SparseArray<View> mIdValues;
    final LongSparseArray<View> mItemIdValues;
    final ArrayMap<String, View> mNameValues;
    final ArrayMap<View, TransitionValues> mViewValues = new ArrayMap();

    TransitionValuesMaps() {
        this.mIdValues = new SparseArray();
        this.mItemIdValues = new LongSparseArray();
        this.mNameValues = new ArrayMap();
    }
}

