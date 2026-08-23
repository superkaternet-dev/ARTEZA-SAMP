/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.util.Log
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup
 *  android.widget.ImageView
 *  android.widget.TextView
 */
package com.blackrussia.game.gui.dialogs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.blackrussia.game.gui.dialogs.DialogAdapter$$ExternalSyntheticLambda0;
import com.blackrussia.game.gui.util.Utils;
import java.util.ArrayList;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class DialogAdapter
extends RecyclerView.Adapter {
    private int mCurrentSelectedPosition = 0;
    private View mCurrentSelectedView;
    private final ArrayList<TextView> mFieldHeaders;
    private final ArrayList<String> mFieldTexts;
    private final ArrayList<ArrayList<TextView>> mFields;
    private OnClickListener mOnClickListener;
    private OnDoubleClickListener mOnDoubleClickListener;

    public DialogAdapter(ArrayList<String> arrayList, ArrayList<TextView> arrayList2) {
        this.mFieldTexts = arrayList;
        this.mFieldHeaders = arrayList2;
        this.mFields = new ArrayList();
    }

    public ArrayList<ArrayList<TextView>> getFields() {
        return this.mFields;
    }

    @Override
    public int getItemCount() {
        return this.mFieldTexts.size();
    }

    public /* synthetic */ void lambda$onBindViewHolder$0$com-blackrussia-game-gui-dialogs-DialogAdapter(ViewHolder object, View view) {
        if (this.mCurrentSelectedPosition != ((RecyclerView.ViewHolder)object).getAdapterPosition()) {
            view = this.mCurrentSelectedView;
            if (view != null) {
                view.setVisibility(8);
            }
            this.mCurrentSelectedPosition = ((RecyclerView.ViewHolder)object).getAdapterPosition();
            this.mCurrentSelectedView = ((ViewHolder)object).mFieldBg;
            ((ViewHolder)object).mFieldBg.setVisibility(0);
            this.mOnClickListener.onClick(((RecyclerView.ViewHolder)object).getAdapterPosition(), ((ViewHolder)object).mFields.get(0).getText().toString());
            return;
        }
        object = this.mOnDoubleClickListener;
        if (object != null) {
            object.onDoubleClick();
        }
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int n) {
        this.onBindViewHolder((ViewHolder)viewHolder, n);
    }

    public void onBindViewHolder(ViewHolder viewHolder, int n) {
        String[] stringArray = this.mFieldTexts.get(n).split("\t");
        ImageView imageView = new ArrayList();
        for (int i = 0; i < stringArray.length; ++i) {
            TextView textView = viewHolder.mFields.get(i);
            textView.setText((CharSequence)Utils.transfromColors(stringArray[i].replace("\\t", "")));
            textView.setVisibility(0);
            imageView.add(textView);
        }
        this.mFields.add((ArrayList<TextView>)imageView);
        if (this.mCurrentSelectedPosition == n) {
            imageView = viewHolder.mFieldBg;
            this.mCurrentSelectedView = imageView;
            imageView.setVisibility(0);
            this.mOnClickListener.onClick(n, viewHolder.mFields.get(0).getText().toString());
        } else {
            viewHolder.mFieldBg.setVisibility(8);
        }
        viewHolder.getView().setOnClickListener((View.OnClickListener)new DialogAdapter$$ExternalSyntheticLambda0(this, viewHolder));
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int n) {
        return new ViewHolder(LayoutInflater.from((Context)viewGroup.getContext()).inflate(2131558515, viewGroup, false));
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener) {
        this.mOnDoubleClickListener = onDoubleClickListener;
    }

    public void updateSizes() {
        int n;
        int n2;
        int[] nArray = new int[4];
        for (n2 = 0; n2 < this.mFields.size(); ++n2) {
            for (n = 0; n < this.mFields.get(n2).size(); ++n) {
                int n3 = this.mFields.get(n2).get(n).getWidth();
                if (nArray[n] >= n3) continue;
                nArray[n] = n3;
            }
        }
        for (n2 = 0; n2 < nArray.length; ++n2) {
            n = this.mFieldHeaders.get(n2).getWidth();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(nArray[n2]);
            stringBuilder.append("\t");
            stringBuilder.append((Object)this.mFieldHeaders.get(n2).getText());
            stringBuilder.append(" ");
            stringBuilder.append(n);
            Log.i((String)"DIALOG", (String)stringBuilder.toString());
            if (nArray[n2] >= n) continue;
            nArray[n2] = n;
        }
        for (n2 = 0; n2 < this.mFields.size(); ++n2) {
            for (n = 0; n < this.mFields.get(n2).size(); ++n) {
                this.mFields.get(n2).get(n).setWidth(nArray[n]);
            }
        }
        for (n2 = 0; n2 < this.mFieldHeaders.size(); ++n2) {
            this.mFieldHeaders.get(n2).setWidth(nArray[n2]);
        }
    }

    public static interface OnClickListener {
        public void onClick(int var1, String var2);
    }

    public static interface OnDoubleClickListener {
        public void onDoubleClick();
    }

    public static class ViewHolder
    extends RecyclerView.ViewHolder {
        public ImageView mFieldBg;
        public ArrayList<TextView> mFields = new ArrayList();
        private final View mView;

        public ViewHolder(View object) {
            super((View)object);
            this.mView = object;
            this.mFieldBg = (ImageView)object.findViewById(2131362344);
            object = (ConstraintLayout)object.findViewById(2131362349);
            for (int i = 1; i < object.getChildCount(); ++i) {
                this.mFields.add((TextView)object.getChildAt(i));
            }
        }

        public View getView() {
            return this.mView;
        }
    }
}

