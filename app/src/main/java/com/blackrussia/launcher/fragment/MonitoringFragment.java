/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.animation.AnimationUtils
 */
package com.blackrussia.launcher.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blackrussia.launcher.adapter.NewsAdapter;
import com.blackrussia.launcher.adapter.ServersAdapter;
import com.blackrussia.launcher.model.News;
import com.blackrussia.launcher.model.Servers;
import com.blackrussia.launcher.other.Lists;
import com.google.firebase.database.DatabaseReference;
import java.util.ArrayList;

public class MonitoringFragment
extends Fragment {
    DatabaseReference databaseNews;
    DatabaseReference databaseServers;
    NewsAdapter newsAdapter;
    ArrayList<News> nlist;
    RecyclerView recyclerNews;
    RecyclerView recyclerServers;
    ServersAdapter serversAdapter;
    ArrayList<Servers> slist;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup object, Bundle bundle) {
        layoutInflater = layoutInflater.inflate(2131558468, (ViewGroup)object, false);
        AnimationUtils.loadAnimation((Context)this.getActivity(), (int)2130771992);
        object = (RecyclerView)layoutInflater.findViewById(2131362241);
        this.recyclerNews = object;
        ((RecyclerView)object).setHasFixedSize(true);
        object = new LinearLayoutManager((Context)this.getActivity(), 0, false);
        this.recyclerNews.setLayoutManager((RecyclerView.LayoutManager)object);
        this.nlist = Lists.nlist;
        object = new NewsAdapter(this.getContext(), this.nlist);
        this.newsAdapter = object;
        this.recyclerNews.setAdapter((RecyclerView.Adapter)object);
        object = (RecyclerView)layoutInflater.findViewById(2131362254);
        this.recyclerServers = object;
        ((RecyclerView)object).setHasFixedSize(true);
        object = new LinearLayoutManager((Context)this.getActivity());
        this.recyclerServers.setLayoutManager((RecyclerView.LayoutManager)object);
        this.slist = Lists.slist;
        this.serversAdapter = object = new ServersAdapter(this.getContext(), this.slist);
        this.recyclerServers.setAdapter((RecyclerView.Adapter)object);
        return layoutInflater;
    }
}

