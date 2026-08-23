/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.net.Uri
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
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.blackrussia.launcher.model.News;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.smarteist.autoimageslider.SliderViewAdapter;
import java.util.ArrayList;
import java.util.List;

public class SliderStoriesAdapter
extends SliderViewAdapter<Holder> {
    private final Context context;
    private List<News> stories = new ArrayList<News>();

    public SliderStoriesAdapter(Context context) {
        this.context = context;
    }

    public void addItem(News news) {
        this.stories.add(news);
        this.notifyDataSetChanged();
    }

    public void addItems(List<News> list) {
        this.stories = list;
        this.notifyDataSetChanged();
    }

    public void deleteItem(int n) {
        this.stories.remove(n);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.stories.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int n) {
        News news = this.stories.get(n);
        holder.title.setText((CharSequence)news.getTitle());
        ((RequestBuilder)Glide.with(this.context).load(news.getImageUrl())).into(holder.image);
        FrameLayout frameLayout = holder.more;
        n = news.getUrl().isEmpty() ? 8 : 0;
        frameLayout.setVisibility(n);
        holder.more.setOnClickListener(new View.OnClickListener(this, holder, news){
            final SliderStoriesAdapter this$0;
            final Holder val$holder;
            final News val$story;
            {
                this.this$0 = sliderStoriesAdapter;
                this.val$holder = holder;
                this.val$story = news;
            }

            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation((Context)this.val$holder.hcontext, (int)2130771992));
                new Handler().postDelayed(new Runnable(this){
                    final 1 this$1;
                    {
                        this.this$1 = var1_1;
                    }

                    @Override
                    public void run() {
                        this.this$1.val$holder.hcontext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse((String)this.this$1.val$story.getUrl())));
                    }
                }, 200L);
            }
        });
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup viewGroup) {
        return new Holder(this, LayoutInflater.from((Context)viewGroup.getContext()).inflate(2131558481, (ViewGroup)null));
    }

    public class Holder
    extends SliderViewAdapter.ViewHolder {
        Context hcontext;
        ImageView image;
        View itemView;
        FrameLayout more;
        TextView text;
        final SliderStoriesAdapter this$0;
        TextView title;

        public Holder(SliderStoriesAdapter sliderStoriesAdapter, View view) {
            this.this$0 = sliderStoriesAdapter;
            super(view);
            this.title = (TextView)view.findViewById(2131362483);
            this.text = (TextView)view.findViewById(2131362456);
            this.image = (ImageView)view.findViewById(2131362150);
            this.more = (FrameLayout)view.findViewById(2131362233);
            this.hcontext = sliderStoriesAdapter.context;
            this.itemView = view;
        }
    }
}

