/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.animation.Animator
 *  android.animation.Animator$AnimatorListener
 *  android.app.Activity
 *  android.content.Context
 *  android.graphics.Color
 *  android.graphics.PorterDuff$Mode
 *  android.graphics.drawable.Drawable
 *  android.text.Html
 *  android.view.LayoutInflater
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.view.ViewGroup$LayoutParams
 *  android.view.animation.AnimationUtils
 *  android.widget.Button
 *  android.widget.FrameLayout
 *  android.widget.ImageView
 *  android.widget.LinearLayout
 *  android.widget.LinearLayout$LayoutParams
 *  android.widget.TextView
 */
package com.blackrussia.game.gui;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.common.AnimatedRoundCornerProgressBar;
import com.akexorcist.roundcornerprogressbar.common.BaseRoundCornerProgressBar;
import com.blackrussia.game.gui.ChooseServer$$ExternalSyntheticLambda0;
import com.blackrussia.game.gui.ChooseServer$$ExternalSyntheticLambda1;
import com.blackrussia.game.gui.ChooseServer$$ExternalSyntheticLambda2;
import com.blackrussia.game.gui.ChooseServer$$ExternalSyntheticLambda3;
import com.blackrussia.game.gui.ChooseServer$$ExternalSyntheticLambda4;
import com.blackrussia.game.gui.util.Utils;
import com.blackrussia.launcher.model.Servers;
import com.blackrussia.launcher.other.Lists;
import com.nvidia.devtech.NvEventQueueActivity;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Formatter;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class ChooseServer {
    Activity aactivity;
    Button allButton;
    LinearLayout chooseServerLayout;
    View findViewById;
    View findViewById2;
    LinearLayout linearLayout2;
    LinearLayout loadingLayout;
    TextView mProgress;
    ArrayList<Servers> mServers;
    Button myButton;
    TextView percentText;
    FrameLayout serverLayout;
    int size;
    int type;

    public ChooseServer(Activity activity) {
        this.aactivity = activity;
        this.mServers = Lists.slist;
        this.serverLayout = (FrameLayout)activity.findViewById(2131361918);
        this.percentText = (TextView)activity.findViewById(2131361901);
        this.chooseServerLayout = (LinearLayout)activity.findViewById(2131361952);
        this.loadingLayout = (LinearLayout)activity.findViewById(2131361953);
        this.type = NvEventQueueActivity.getInstance().getLastServer();
        this.findViewById = this.aactivity.findViewById(2131362216);
        this.findViewById2 = this.aactivity.findViewById(2131362206);
        this.myButton = (Button)this.aactivity.findViewById(2131362376);
        this.allButton = (Button)this.aactivity.findViewById(2131361875);
        Utils.HideLayout((View)this.serverLayout, false);
    }

    public void Show() {
        Utils.ShowLayout((View)this.serverLayout, false);
    }

    public void ShowAdmin() {
        this.chooseServerLayout.setBackground(ResourcesCompat.getDrawable(this.aactivity.getResources(), 2131231112, null));
    }

    public void Update(int n) {
        if (n <= 100) {
            this.percentText.setText((CharSequence)new Formatter().format("%d%s", n, "%").toString());
        } else {
            this.chooseServerLayout.setVisibility(0);
            this.chooseServerLayout.setAlpha(0.0f);
            this.chooseServerLayout.animate().setDuration(1500L).alpha(1.0f).setListener(new Animator.AnimatorListener(this){
                final ChooseServer this$0;
                {
                    this.this$0 = chooseServer;
                }

                public void onAnimationCancel(Animator animator2) {
                }

                public void onAnimationEnd(Animator animator2) {
                    this.this$0.loadingLayout.setVisibility(8);
                }

                public void onAnimationRepeat(Animator animator2) {
                }

                public void onAnimationStart(Animator animator2) {
                    this.this$0.initUi();
                }
            }).start();
        }
    }

    public void initUi() {
        LinearLayout linearLayout;
        Object object;
        Object object2;
        this.loadingLayout.setVisibility(8);
        int n = this.type;
        long l = 4636737291354636288L;
        if (n != -1) {
            ((TextView)this.aactivity.findViewById(2131361913)).setText((CharSequence)this.mServers.get(this.type).getname());
            object2 = this.aactivity.findViewById(2131362374);
            object = (RoundCornerProgressBar)this.aactivity.findViewById(2131361915);
            linearLayout = object2.getBackground();
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("#");
            ((StringBuilder)object2).append(this.mServers.get(this.type).getColor());
            linearLayout.setColorFilter(Color.parseColor((String)((StringBuilder)object2).toString()), PorterDuff.Mode.SRC_ATOP);
            object2 = new StringBuilder();
            ((StringBuilder)object2).append("#");
            ((StringBuilder)object2).append(this.mServers.get(this.type).getColor());
            ((BaseRoundCornerProgressBar)((Object)object)).setProgressColor(Color.parseColor((String)((StringBuilder)object2).toString()));
            ((AnimatedRoundCornerProgressBar)((Object)object)).setProgress((float)((int)(Double.parseDouble(String.valueOf(this.mServers.get(this.type).getOnline())) / Double.parseDouble(String.valueOf(this.mServers.get(this.type).getmaxOnline())) * 100.0)));
            object2 = (ImageView)this.aactivity.findViewById(2131361912);
            object = new StringBuilder();
            ((StringBuilder)object).append("#");
            ((StringBuilder)object).append(this.mServers.get(this.type).getColor());
            object2.setColorFilter(Color.parseColor((String)((StringBuilder)object).toString()), PorterDuff.Mode.SRC_ATOP);
            object = (TextView)this.aactivity.findViewById(2131361914);
            object2 = new StringBuilder();
            ((StringBuilder)object2).append(this.mServers.get(this.type).getOnline());
            ((StringBuilder)object2).append("<font color='#808080'>/");
            ((StringBuilder)object2).append(this.mServers.get(this.type).getmaxOnline());
            object.setText((CharSequence)Html.fromHtml((String)((StringBuilder)object2).toString()));
        }
        if (this.type != -1) {
            this.findViewById2.setAlpha(0.0f);
            this.findViewById2.setVisibility(8);
            this.findViewById.setVisibility(0);
        } else {
            this.findViewById.setAlpha(0.0f);
            this.findViewById.setVisibility(8);
            this.findViewById2.setVisibility(0);
            this.myButton.setVisibility(8);
            this.allButton.setVisibility(8);
        }
        linearLayout = (LinearLayout)this.aactivity.findViewById(2131362334);
        linearLayout.setScrollbarFadingEnabled(false);
        this.size = this.mServers.size() / 4 + 1;
        this.mProgress = (TextView)this.aactivity.findViewById(2131361901);
        object = "fsdf";
        this.aactivity.findViewById(2131362375).setOnClickListener((View.OnClickListener)new ChooseServer$$ExternalSyntheticLambda0(this));
        this.aactivity.findViewById(2131361917).setOnClickListener((View.OnClickListener)new ChooseServer$$ExternalSyntheticLambda3(this, "fsdf"));
        int n2 = this.mServers.size() / 4 + 1;
        this.myButton.setOnClickListener((View.OnClickListener)new ChooseServer$$ExternalSyntheticLambda1(this));
        this.allButton.setOnClickListener((View.OnClickListener)new ChooseServer$$ExternalSyntheticLambda2(this));
        for (n = 0; n < n2; ++n) {
            int n3;
            this.linearLayout2 = new LinearLayout((Context)this.aactivity);
            object2 = new LinearLayout.LayoutParams(-1, -1);
            ((LinearLayout.LayoutParams)object2).weight = 1 / n2;
            if (n < n2 - 1) {
                object2.setMargins(0, 0, 0, NvEventQueueActivity.dpToPx(12.0f, (Context)this.aactivity));
            }
            this.linearLayout2.setLayoutParams((ViewGroup.LayoutParams)object2);
            this.linearLayout2.setOrientation(0);
            for (int i = 0; i < 4 && (n3 = n * 4 + i) < this.mServers.size(); ++i) {
                n3 = this.mServers.size() - n3 - 1;
                View view = ((LayoutInflater)this.aactivity.getSystemService("layout_inflater")).inflate(2131558437, null, false);
                view.setOnClickListener((View.OnClickListener)new ChooseServer$$ExternalSyntheticLambda4(this, (String)object, n3));
                ((TextView)view.findViewById(2131361913)).setText((CharSequence)this.mServers.get(n3).getname());
                Object object3 = new LinearLayout.LayoutParams(NvEventQueueActivity.dpToPx(160.0f, (Context)this.aactivity), NvEventQueueActivity.dpToPx(80.0f, (Context)this.aactivity));
                ((LinearLayout.LayoutParams)object3).weight = 0.25f;
                if (n < 5) {
                    object3.setMargins(0, 0, NvEventQueueActivity.dpToPx(20.0f, (Context)this.aactivity), 0);
                }
                view.setLayoutParams((ViewGroup.LayoutParams)object3);
                Object object4 = view.findViewById(2131362374);
                Object object5 = (RoundCornerProgressBar)view.findViewById(2131361915);
                object3 = (ImageView)view.findViewById(2131361912);
                Drawable drawable2 = object4.getBackground();
                object4 = new StringBuilder();
                ((StringBuilder)object4).append("#");
                ((StringBuilder)object4).append(this.mServers.get(n3).getColor());
                drawable2.setColorFilter(Color.parseColor((String)((StringBuilder)object4).toString()), PorterDuff.Mode.SRC_ATOP);
                object4 = new StringBuilder();
                ((StringBuilder)object4).append("#");
                ((StringBuilder)object4).append(this.mServers.get(n3).getColor());
                ((BaseRoundCornerProgressBar)((Object)object5)).setProgressColor(Color.parseColor((String)((StringBuilder)object4).toString()));
                ((AnimatedRoundCornerProgressBar)((Object)object5)).setProgress((float)((int)(Double.parseDouble(String.valueOf(this.mServers.get(n3).getOnline())) / Double.parseDouble(String.valueOf(this.mServers.get(n3).getmaxOnline())) * 100.0)));
                object5 = new StringBuilder();
                ((StringBuilder)object5).append("#");
                ((StringBuilder)object5).append(this.mServers.get(n3).getColor());
                object3.setColorFilter(Color.parseColor((String)((StringBuilder)object5).toString()), PorterDuff.Mode.SRC_ATOP);
                object5 = (TextView)view.findViewById(2131361914);
                object3 = new StringBuilder();
                ((StringBuilder)object3).append(this.mServers.get(n3).getOnline());
                ((StringBuilder)object3).append("<font color='#808080'>/");
                ((StringBuilder)object3).append(this.mServers.get(n3).getmaxOnline());
                object5.setText((CharSequence)Html.fromHtml((String)((StringBuilder)object3).toString()));
                this.linearLayout2.addView(view);
                l = 4636737291354636288L;
            }
        }
        linearLayout.addView((View)this.linearLayout2);
    }

    public /* synthetic */ void lambda$initUi$0$com-blackrussia-game-gui-ChooseServer(View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this.aactivity, (int)2130771992));
    }

    public /* synthetic */ void lambda$initUi$1$com-blackrussia-game-gui-ChooseServer(String string2, View view) {
        try {
            NvEventQueueActivity.getInstance().sendRPC(2, string2.getBytes("windows-1251"), this.type);
            Utils.HideLayout((View)this.serverLayout, true);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }
    }

    public /* synthetic */ void lambda$initUi$2$com-blackrussia-game-gui-ChooseServer(View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this.aactivity, (int)2130771992));
        view.setBackground(ResourcesCompat.getDrawable(this.aactivity.getResources(), 2131230924, null));
        this.allButton.setBackground(ResourcesCompat.getDrawable(this.aactivity.getResources(), 2131230923, null));
        if (this.findViewById.getVisibility() != 0) {
            this.findViewById2.animate().alpha(0.0f).setDuration(100L).setListener(new Animator.AnimatorListener(this){
                final ChooseServer this$0;
                {
                    this.this$0 = chooseServer;
                }

                public void onAnimationCancel(Animator animator2) {
                }

                public void onAnimationEnd(Animator animator2) {
                    this.this$0.findViewById2.setAlpha(0.0f);
                    this.this$0.findViewById2.setVisibility(8);
                    this.this$0.findViewById.setVisibility(0);
                    this.this$0.findViewById.setAlpha(0.0f);
                    this.this$0.findViewById.animate().alpha(1.0f).setDuration(100L).setListener(null).start();
                }

                public void onAnimationRepeat(Animator animator2) {
                }

                public void onAnimationStart(Animator animator2) {
                }
            });
        }
    }

    public /* synthetic */ void lambda$initUi$3$com-blackrussia-game-gui-ChooseServer(View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this.aactivity, (int)2130771992));
        view.setBackground(ResourcesCompat.getDrawable(this.aactivity.getResources(), 2131230924, null));
        this.myButton.setBackground(ResourcesCompat.getDrawable(this.aactivity.getResources(), 2131230923, null));
        if (this.findViewById2.getVisibility() != 0) {
            this.findViewById.animate().alpha(0.0f).setDuration(100L).setListener(new Animator.AnimatorListener(this){
                final ChooseServer this$0;
                {
                    this.this$0 = chooseServer;
                }

                public void onAnimationCancel(Animator animator2) {
                }

                public void onAnimationEnd(Animator animator2) {
                    this.this$0.findViewById.setAlpha(0.0f);
                    this.this$0.findViewById.setVisibility(8);
                    this.this$0.findViewById2.setVisibility(0);
                    this.this$0.findViewById2.setAlpha(0.0f);
                    this.this$0.findViewById2.animate().alpha(1.0f).setDuration(100L).setListener(null).start();
                }

                public void onAnimationRepeat(Animator animator2) {
                }

                public void onAnimationStart(Animator animator2) {
                }
            });
        }
    }

    public /* synthetic */ void lambda$initUi$4$com-blackrussia-game-gui-ChooseServer(String string2, int n, View view) {
        view.startAnimation(AnimationUtils.loadAnimation((Context)this.aactivity, (int)2130771992));
        try {
            NvEventQueueActivity.getInstance().sendRPC(2, string2.getBytes("windows-1251"), n);
            Utils.HideLayout((View)this.serverLayout, true);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        }
    }
}

