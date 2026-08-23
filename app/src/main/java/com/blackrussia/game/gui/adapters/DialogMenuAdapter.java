/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package com.blackrussia.game.gui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.blackrussia.game.gui.adapters.DialogMenuAdapter$DialogMenuHolder$$ExternalSyntheticLambda0;
import com.blackrussia.game.gui.models.DataDialogMenu;
import java.util.List;

public class DialogMenuAdapter
extends RecyclerView.Adapter<DialogMenuHolder> {
    private List<DataDialogMenu> lDataDialogMenu;
    private OnUserClickListener onUserClickListener;

    public DialogMenuAdapter(List<DataDialogMenu> list, OnUserClickListener onUserClickListener) {
        this.lDataDialogMenu = list;
        this.onUserClickListener = onUserClickListener;
    }

    @Override
    public int getItemCount() {
        return this.lDataDialogMenu.size();
    }

    @Override
    public void onBindViewHolder(DialogMenuHolder dialogMenuHolder, int n) {
        DataDialogMenu dataDialogMenu = this.lDataDialogMenu.get(n);
        dialogMenuHolder.nameBlockButton.setText((CharSequence)dataDialogMenu.getNameButton());
        dialogMenuHolder.imageViewBlockButton.setImageResource(dataDialogMenu.getImgDrawableButton());
    }

    @Override
    public DialogMenuHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
        return new DialogMenuHolder(this, LayoutInflater.from((Context)viewGroup.getContext()).inflate(2131558489, viewGroup, false));
    }

    /*
     * Illegal identifiers - consider using --renameillegalidents true
     */
    public class DialogMenuHolder
    extends RecyclerView.ViewHolder {
        ImageView bcgFill;
        ImageView imageViewBlockButton;
        TextView nameBlockButton;
        final DialogMenuAdapter this$0;

        public DialogMenuHolder(DialogMenuAdapter dialogMenuAdapter, View view) {
            this.this$0 = dialogMenuAdapter;
            super(view);
            this.nameBlockButton = (TextView)view.findViewById(2131362181);
            this.imageViewBlockButton = (ImageView)view.findViewById(2131362180);
            this.bcgFill = (ImageView)view.findViewById(2131361892);
            view.setOnClickListener((View.OnClickListener)new DialogMenuAdapter$DialogMenuHolder$$ExternalSyntheticLambda0(this));
        }

        public /* synthetic */ void lambda$new$0$com-blackrussia-game-gui-adapters-DialogMenuAdapter$DialogMenuHolder(View view) {
            this.this$0.onUserClickListener.click((DataDialogMenu)this.this$0.lDataDialogMenu.get(this.getLayoutPosition()), view);
        }
    }

    public static interface OnUserClickListener {
        public void click(DataDialogMenu var1, View var2);
    }
}

