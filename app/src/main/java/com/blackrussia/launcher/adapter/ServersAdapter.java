/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.graphics.Color
 *  android.graphics.PorterDuff$Mode
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.view.animation.AnimationUtils
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.TextView
 *  android.widget.Toast
 */
package com.blackrussia.launcher.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.blackrussia.launcher.model.Servers;
import com.dinuscxj.progressbar.CircleProgressBar;
import java.util.ArrayList;

public class ServersAdapter
extends RecyclerView.Adapter<ServersViewHolder> {
    Context context;
    ArrayList<Servers> slist;

    public ServersAdapter(Context context, ArrayList<Servers> arrayList) {
        this.context = context;
        this.slist = arrayList;
    }

    @Override
    public int getItemCount() {
        return this.slist.size();
    }

    @Override
    public void onBindViewHolder(ServersViewHolder serversViewHolder, int n) {
        Servers servers = this.slist.get(n);
        Object object = serversViewHolder.bearPaw;
        Object object2 = new StringBuilder();
        ((StringBuilder)object2).append("#");
        ((StringBuilder)object2).append(servers.getColor());
        object.setColorFilter(Color.parseColor((String)((StringBuilder)object2).toString()), PorterDuff.Mode.SRC_ATOP);
        object2 = serversViewHolder.people;
        object = new StringBuilder();
        ((StringBuilder)object).append("#");
        ((StringBuilder)object).append(servers.getColor());
        object2.setColorFilter(Color.parseColor((String)((StringBuilder)object).toString()), PorterDuff.Mode.SRC_ATOP);
        object = serversViewHolder.backColor.getBackground();
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("#");
        ((StringBuilder)object2).append(servers.getColor());
        object.setColorFilter(Color.parseColor((String)((StringBuilder)object2).toString()), PorterDuff.Mode.SRC_ATOP);
        serversViewHolder.name.setText((CharSequence)servers.getname());
        object2 = serversViewHolder.name;
        object = new StringBuilder();
        ((StringBuilder)object).append("#");
        ((StringBuilder)object).append(servers.getColor());
        object2.setTextColor(Color.parseColor((String)((StringBuilder)object).toString()));
        serversViewHolder.dopname.setText((CharSequence)servers.getDopname());
        serversViewHolder.textonline.setText((CharSequence)Integer.toString(servers.getOnline()));
        object = serversViewHolder.textmaxonline;
        object2 = new StringBuilder();
        ((StringBuilder)object2).append("/");
        ((StringBuilder)object2).append(Integer.toString(servers.getmaxOnline()));
        object.setText((CharSequence)new String(((StringBuilder)object2).toString()));
        object2 = serversViewHolder.progressBar;
        object = new StringBuilder();
        ((StringBuilder)object).append("#");
        ((StringBuilder)object).append(servers.getColor());
        ((CircleProgressBar)((Object)object2)).setProgressStartColor(Color.parseColor((String)((StringBuilder)object).toString()));
        object2 = serversViewHolder.progressBar;
        object = new StringBuilder();
        ((StringBuilder)object).append("#");
        ((StringBuilder)object).append(servers.getColor());
        ((CircleProgressBar)((Object)object2)).setProgressEndColor(Color.parseColor((String)((StringBuilder)object).toString()));
        serversViewHolder.progressBar.setProgress(servers.getOnline());
        serversViewHolder.progressBar.setMax(servers.getmaxOnline());
        serversViewHolder.container.setOnClickListener(new View.OnClickListener(this){
            final ServersAdapter this$0;
            {
                this.this$0 = serversAdapter;
            }

            public void onClick(View view) {
                view.startAnimation(AnimationUtils.loadAnimation((Context)this.this$0.context, (int)2130771992));
                Toast.makeText((Context)this.this$0.context, (CharSequence)"\u0414\u043b\u044f \u043d\u0430\u0447\u0430\u043b\u0430 \u0438\u0433\u0440\u044b \u043d\u0430\u0436\u043c\u0438\u0442\u0435 \u043a\u0440\u0430\u0441\u043d\u0443\u044e \u043a\u043d\u043e\u043f\u043a\u0443", (int)0).show();
            }
        });
    }

    @Override
    public ServersViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
        return new ServersViewHolder(LayoutInflater.from((Context)this.context).inflate(2131558480, viewGroup, false));
    }

    public static class ServersViewHolder
    extends RecyclerView.ViewHolder {
        LinearLayout backColor;
        ImageView bearPaw;
        FrameLayout container;
        TextView dopname;
        TextView name;
        ImageView people;
        CircleProgressBar progressBar;
        TextView textmaxonline;
        TextView textonline;
        ImageView x2;

        public ServersViewHolder(View view) {
            super(view);
            this.name = (TextView)view.findViewById(2131362059);
            this.dopname = (TextView)view.findViewById(2131362367);
            this.progressBar = (CircleProgressBar)view.findViewById(2131362289);
            this.bearPaw = (ImageView)view.findViewById(2131361893);
            this.x2 = (ImageView)view.findViewById(2131362522);
            this.people = (ImageView)view.findViewById(2131362267);
            this.textonline = (TextView)view.findViewById(2131362252);
            this.textmaxonline = (TextView)view.findViewById(2131362253);
            this.backColor = (LinearLayout)view.findViewById(2131361888);
            this.container = (FrameLayout)view.findViewById(2131361965);
        }
    }
}

