/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.Context
 *  android.os.Handler
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.animation.Animation
 *  android.view.animation.AnimationUtils
 *  android.widget.LinearLayout
 *  android.widget.TextView
 */
package com.blackrussia.game.gui;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.blackrussia.game.gui.Menu$$ExternalSyntheticLambda0;
import com.blackrussia.game.gui.Menu$$ExternalSyntheticLambda1;
import com.blackrussia.game.gui.Menu$$ExternalSyntheticLambda2;
import com.blackrussia.game.gui.Menu$$ExternalSyntheticLambda3;
import com.blackrussia.game.gui.Menu$$ExternalSyntheticLambda4;
import com.blackrussia.game.gui.adapters.DialogMenuAdapter;
import com.blackrussia.game.gui.models.DataDialogMenu;
import com.blackrussia.game.gui.util.Utils;
import com.nvidia.devtech.NvEventQueueActivity;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class Menu {
    public Activity activity;
    private final Animation anim;
    private final ArrayList<DataDialogMenu> dataDialogMenuArrayList = new ArrayList();
    private int index = -1;
    public View mRootView;
    public TextView menuTitle;
    public LinearLayout menu_layout;

    public Menu(Activity activity) {
        this.activity = activity;
        this.anim = AnimationUtils.loadAnimation((Context)activity, (int)2130771992);
        this.menu_layout = (LinearLayout)activity.findViewById(2131362215);
        activity.findViewById(2131361902).setOnClickListener((View.OnClickListener)new Menu$$ExternalSyntheticLambda0(this));
        this.mRootView = ((LayoutInflater)activity.getSystemService("layout_inflater")).inflate(2131558488, null, false);
        Utils.HideLayout((View)this.menu_layout, false);
    }

    private void setDataInRecyclerView(DialogMenuAdapter.OnUserClickListener object, ArrayList<DataDialogMenu> arrayList, RecyclerView recyclerView, View view, int n) {
        object = new DialogMenuAdapter(arrayList, (DialogMenuAdapter.OnUserClickListener)object);
        recyclerView.setLayoutManager(new GridLayoutManager(this, view.getContext(), n, view){
            final Menu this$0;
            final View val$view;
            {
                this.this$0 = menu;
                this.val$view = view;
                super(context, n);
            }

            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams layoutParams) {
                float f = 30.0f / this.val$view.getResources().getDisplayMetrics().density;
                int n = (int)f;
                layoutParams.setMarginStart(n);
                layoutParams.setMarginEnd(n);
                layoutParams.setMargins(0, n, 0, 0);
                layoutParams.width = (int)((float)(this.getWidth() / this.getSpanCount()) - f);
                return true;
            }
        });
        recyclerView.setAdapter((RecyclerView.Adapter)object);
    }

    private void setDialogMenu() {
        this.dataDialogMenuArrayList.clear();
        this.dataDialogMenuArrayList.add(new DataDialogMenu(8, 2131231099, "\u041f\u0435\u0440\u0435\u0434\u0430\u0442\u044c \u043f\u0430\u0441\u043f\u043e\u0440\u0442"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(9, 2131231097, "\u041f\u0435\u0440\u0435\u0434\u0430\u0442\u044c \u043c\u0435\u0434.\u043a\u0430\u0440\u0442\u0443"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(10, 2131231098, "\u041f\u0435\u0440\u0435\u0434\u0430\u0442\u044c \u043b\u0438\u0446\u0435\u043d\u0437\u0438\u0438"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(11, 2131231096, "\u041f\u0435\u0440\u0435\u0434\u0430\u0442\u044c \u041f\u0422\u0421"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(12, 2131231095, "\u0421\u043e\u0432\u0435\u0440\u0448\u0438\u0442\u044c \u043e\u0431\u043c\u0435\u043d"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(13, 2131231093, "\u041d\u0430\u0437\u0430\u0434"));
    }

    private void setMenu() {
        this.dataDialogMenuArrayList.clear();
        this.dataDialogMenuArrayList.add(new DataDialogMenu(398, 2131230859, "\u041d\u0430\u0432\u0438\u0433\u0430\u0442\u043e\u0440"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(1, 2131230862, "\u0412\u044b\u0437\u043e\u0432 \u0442\u0430\u043a\u0441\u0438"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(2, 2131230860, "\u041c\u0435\u043d\u044e"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(3, 2131230858, "\u041e\u0431\u0449\u0435\u043d\u0438\u0435"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(4, 2131230856, "\u0418\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u044c"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(5, 2131230855, "\u0410\u043d\u0438\u043c\u0430\u0446\u0438\u0438"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(6, 2131230861, "\u0414\u043e\u043d\u0430\u0442"));
        this.dataDialogMenuArrayList.add(new DataDialogMenu(7, 2131230857, "\u0410\u0432\u0442\u043e\u043c\u043e\u0431\u0438\u043b\u0438"));
    }

    public void ShowMenu() {
        this.Update(false);
        Utils.ShowLayout((View)this.menu_layout, true);
    }

    public void Update(boolean bl) {
        RecyclerView recyclerView = (RecyclerView)this.activity.findViewById(2131361911);
        this.index = -1;
        this.menuTitle = (TextView)this.activity.findViewById(2131361904);
        if (!bl) {
            this.setMenu();
            this.menuTitle.setText((CharSequence)"\u0414\u0435\u0439\u0441\u0442\u0432\u0438\u044f");
            this.setDataInRecyclerView(new Menu$$ExternalSyntheticLambda1(this), this.dataDialogMenuArrayList, recyclerView, this.mRootView, 4);
            return;
        }
        this.setDialogMenu();
        this.menuTitle.setText((CharSequence)"\u041e\u0431\u0449\u0435\u043d\u0438\u0435");
        this.setDataInRecyclerView(new Menu$$ExternalSyntheticLambda2(this), this.dataDialogMenuArrayList, recyclerView, this.mRootView, 3);
    }

    public void close() {
        Utils.HideLayout((View)this.menu_layout, true);
        NvEventQueueActivity.getInstance().togglePlayer(0);
    }

    public /* synthetic */ void lambda$Update$1$com-blackrussia-game-gui-Menu() {
        if (this.index == 3) {
            this.Update(true);
        } else {
            try {
                NvEventQueueActivity.getInstance().sendRPC(1, String.valueOf(this.index).getBytes("windows-1251"), this.index);
                this.close();
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
            }
        }
    }

    public /* synthetic */ void lambda$Update$2$com-blackrussia-game-gui-Menu(DataDialogMenu dataDialogMenu, View view) {
        this.index = dataDialogMenu.getId();
        view.startAnimation(this.anim);
        new Handler().postDelayed((Runnable)new Menu$$ExternalSyntheticLambda3(this), 300L);
    }

    public /* synthetic */ void lambda$Update$3$com-blackrussia-game-gui-Menu() {
        if (this.index == 13) {
            this.Update(false);
        } else {
            try {
                NvEventQueueActivity.getInstance().sendRPC(1, String.valueOf(this.index).getBytes("windows-1251"), this.index);
                this.close();
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
            }
        }
    }

    public /* synthetic */ void lambda$Update$4$com-blackrussia-game-gui-Menu(DataDialogMenu dataDialogMenu, View view) {
        this.index = dataDialogMenu.getId();
        view.startAnimation(this.anim);
        new Handler().postDelayed((Runnable)new Menu$$ExternalSyntheticLambda4(this), 300L);
    }

    public /* synthetic */ void lambda$new$0$com-blackrussia-game-gui-Menu(View view) {
        this.close();
    }
}

