/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.view.View
 *  android.view.ViewGroup
 */
package com.smarteist.autoimageslider;

import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import java.util.LinkedList;
import java.util.Queue;

public abstract class SliderViewAdapter<VH extends ViewHolder>
extends PagerAdapter {
    private DataSetListener dataSetListener;
    private Queue<VH> destroyedItems = new LinkedList<VH>();

    void dataSetChangedListener(DataSetListener dataSetListener) {
        this.dataSetListener = dataSetListener;
    }

    @Override
    public final void destroyItem(ViewGroup viewGroup, int n, Object object) {
        viewGroup.removeView(((ViewHolder)object).itemView);
        this.destroyedItems.add((ViewHolder)object);
    }

    @Override
    public int getItemPosition(Object object) {
        return -2;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int n) {
        ViewHolder viewHolder;
        ViewHolder viewHolder2 = viewHolder = (ViewHolder)this.destroyedItems.poll();
        if (viewHolder == null) {
            viewHolder2 = this.onCreateViewHolder(viewGroup);
        }
        viewGroup.addView(viewHolder2.itemView);
        this.onBindViewHolder(viewHolder2, n);
        return viewHolder2;
    }

    @Override
    public final boolean isViewFromObject(View view, Object object) {
        boolean bl = ((ViewHolder)object).itemView == view;
        return bl;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        DataSetListener dataSetListener = this.dataSetListener;
        if (dataSetListener != null) {
            dataSetListener.dataSetChanged();
        }
    }

    public abstract void onBindViewHolder(VH var1, int var2);

    public abstract VH onCreateViewHolder(ViewGroup var1);

    static interface DataSetListener {
        public void dataSetChanged();
    }

    public static abstract class ViewHolder {
        public final View itemView;

        public ViewHolder(View view) {
            this.itemView = view;
        }
    }
}

