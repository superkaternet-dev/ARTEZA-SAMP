/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.database.DataSetObserver
 *  android.os.Parcelable
 *  android.view.View
 *  android.view.ViewGroup
 */
package com.smarteist.autoimageslider.InfiniteAdapter;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class InfinitePagerAdapter
extends PagerAdapter {
    public static final int INFINITE_SCROLL_LIMIT = 32400;
    private static final String TAG = "InfinitePagerAdapter";
    private SliderViewAdapter adapter;

    public InfinitePagerAdapter(SliderViewAdapter sliderViewAdapter) {
        this.adapter = sliderViewAdapter;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int n, Object object) {
        if (this.getRealCount() < 1) {
            this.adapter.destroyItem(viewGroup, 0, object);
            return;
        }
        this.adapter.destroyItem(viewGroup, this.getRealPosition(n), object);
    }

    @Override
    public void finishUpdate(ViewGroup viewGroup) {
        this.adapter.finishUpdate(viewGroup);
    }

    @Override
    public int getCount() {
        if (this.getRealCount() < 1) {
            return 0;
        }
        return this.getRealCount() * 32400;
    }

    @Override
    public int getItemPosition(Object object) {
        return this.adapter.getItemPosition(object);
    }

    public int getMiddlePosition(int n) {
        return n + Math.max(0, this.getRealCount()) * 16200;
    }

    @Override
    public CharSequence getPageTitle(int n) {
        return this.adapter.getPageTitle(this.getRealPosition(n));
    }

    @Override
    public float getPageWidth(int n) {
        return this.adapter.getPageWidth(n);
    }

    public PagerAdapter getRealAdapter() {
        return this.adapter;
    }

    public int getRealCount() {
        try {
            int n = this.getRealAdapter().getCount();
            return n;
        }
        catch (Exception exception) {
            return 0;
        }
    }

    public int getRealPosition(int n) {
        if (this.getRealCount() > 0) {
            return n % this.getRealCount();
        }
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int n) {
        if (this.getRealCount() < 1) {
            return this.adapter.instantiateItem(viewGroup, 0);
        }
        return this.adapter.instantiateItem(viewGroup, this.getRealPosition(n));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return this.adapter.isViewFromObject(view, object);
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        this.adapter.registerDataSetObserver(dataSetObserver);
    }

    @Override
    public void restoreState(Parcelable parcelable, ClassLoader classLoader) {
        this.adapter.restoreState(parcelable, classLoader);
    }

    @Override
    public Parcelable saveState() {
        return this.adapter.saveState();
    }

    @Override
    public void setPrimaryItem(ViewGroup viewGroup, int n, Object object) {
        this.adapter.setPrimaryItem(viewGroup, n, object);
    }

    @Override
    public void startUpdate(ViewGroup viewGroup) {
        this.adapter.startUpdate(viewGroup);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        this.adapter.unregisterDataSetObserver(dataSetObserver);
    }
}

