/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Handler
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.animation.AnimationUtils
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package com.blackrussia.launcher.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.blackrussia.launcher.activity.StoryActivity;
import com.blackrussia.launcher.model.News;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import java.util.ArrayList;

public class NewsAdapter
extends RecyclerView.Adapter<NewsViewHolder> {
    Context context;
    ArrayList<News> nlist;

    public NewsAdapter(Context context, ArrayList<News> arrayList) {
        this.context = context;
        this.nlist = arrayList;
    }

    private void setAnimation(View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this.context, (int)2130771992));
    }

    @Override
    public int getItemCount() {
        return this.nlist.size();
    }

    @Override
    public void onBindViewHolder(NewsViewHolder newsViewHolder, int n) {
        News news = this.nlist.get(n);
        newsViewHolder.title.setText((CharSequence)news.getTitle());
        ((RequestBuilder)Glide.with(this.context).load(news.getImageUrl())).into(newsViewHolder.image);
        newsViewHolder.container.setOnClickListener(new View.OnClickListener(this, n){
            final NewsAdapter this$0;
            final int val$position;
            {
                this.this$0 = newsAdapter;
                this.val$position = n;
            }

            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation((Context)this.this$0.context, (int)2130771992));
                new Handler().postDelayed(new Runnable(this){
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void run() {
                        this.this$1.this$0.startStorySlider(this.this$1.val$position);
                    }
                }, 200L);
            }
        });
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
        return new NewsViewHolder(LayoutInflater.from((Context)this.context).inflate(2131558479, viewGroup, false));
    }

    public void startStorySlider(int n) {
        Intent intent = new Intent(this.context, StoryActivity.class);
        intent.putExtra("position", n);
        this.context.startActivity(intent);
    }

    public static class NewsViewHolder
    extends RecyclerView.ViewHolder {
        FrameLayout container;
        ImageView image;
        TextView title;

        public NewsViewHolder(View view) {
            super(view);
            this.title = (TextView)view.findViewById(2131362483);
            this.image = (ImageView)view.findViewById(2131362150);
            this.container = (FrameLayout)view.findViewById(2131361965);
        }
    }
}

