/*
 * Decompiled with CFR 0.152.
 */
package androidx.recyclerview.widget;

import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.AsyncDifferConfig;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public abstract class ListAdapter<T, VH extends RecyclerView.ViewHolder>
extends RecyclerView.Adapter<VH> {
    private final AsyncListDiffer<T> mHelper;

    protected ListAdapter(AsyncDifferConfig<T> asyncDifferConfig) {
        this.mHelper = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), asyncDifferConfig);
    }

    protected ListAdapter(DiffUtil.ItemCallback<T> itemCallback) {
        this.mHelper = new AsyncListDiffer<T>(new AdapterListUpdateCallback(this), new AsyncDifferConfig.Builder<T>(itemCallback).build());
    }

    protected T getItem(int n) {
        return this.mHelper.getCurrentList().get(n);
    }

    @Override
    public int getItemCount() {
        return this.mHelper.getCurrentList().size();
    }

    public void submitList(List<T> list) {
        this.mHelper.submitList(list);
    }
}

