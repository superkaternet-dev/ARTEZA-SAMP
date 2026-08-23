/*
 * Decompiled with CFR 0.152.
 */
package com.blackrussia.game.core;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class DialogClientSettingsAdapter
extends FragmentPagerAdapter {
    List<Fragment> mFragmentCollection = new ArrayList<Fragment>();
    List<String> mTitleCollection = new ArrayList<String>();

    public DialogClientSettingsAdapter(FragmentManager fragmentManager, int n) {
        super(fragmentManager, n);
    }

    public void addFragment(String string2, Fragment fragment) {
        this.mTitleCollection.add(string2);
        this.mFragmentCollection.add(fragment);
    }

    @Override
    public int getCount() {
        return this.mFragmentCollection.size();
    }

    @Override
    public Fragment getItem(int n) {
        return this.mFragmentCollection.get(n);
    }

    @Override
    public CharSequence getPageTitle(int n) {
        return this.mTitleCollection.get(n);
    }
}

